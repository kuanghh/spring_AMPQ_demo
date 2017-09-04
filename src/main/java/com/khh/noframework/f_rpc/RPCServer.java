package com.khh.noframework.f_rpc;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by 951087952@qq.com on 2017/9/4.
 */
public class RPCServer {

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    private static int fib(int n){
        if(n == 1){
            return 1;
        }else if(n == 2){
            return 2;
        }
        return fib(n - 1) + fib(n - 2);
    }

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = null;

        try{
            connection      = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(RPC_QUEUE_NAME,false,false,false,null);

            channel.basicQos(1);

            System.out.println(" [x] Awaiting RPC requests");

            Consumer consumer = new DefaultConsumer(channel){

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                            .correlationId(properties.getCorrelationId())//设置关联id
                            .build();
                    String response = "";
                    try{
                        String message = new String(body,"UTF-8");
                        int n = Integer.parseInt(message);
                        System.out.println(" [.] fib(" + message + ")");
                        response += fib(n);

                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        channel.basicPublish("",properties.getReplyTo(),replyProps,response.getBytes());
                        channel.basicAck(envelope.getDeliveryTag(),false);
                    }
                }
            };

            channel.basicConsume(RPC_QUEUE_NAME,false,consumer);

            //loop to prevent reaching finally block
            while(true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException _ignore) {}
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(connection != null){
                connection.close();
            }
        }

    }


}
