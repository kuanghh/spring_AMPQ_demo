package com.khh.noframework.workqueues;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by 951087952@qq.com on 2017/9/1.
 *
 * 一个不会自动确认消息的worker
 */
public class NoAutoAckWorker {

    private static String  TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception{

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("localhost");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        /**
         * 参数：maximum number of messages that the server will deliver, 0 if unlimited
         *
         * 官方文档这样说的：设置basicQos（prefetchCount）的意思是，让rabbitMQ最多发送prefetchCount个消息给当前worker
         *
         */
        channel.basicQos(1);

        final Consumer consumer = new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {


                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");

                try{
                    doWork(message);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    /**
                     * 手动确认消息
                     *
                     */
                    channel.basicAck(envelope.getDeliveryTag(),false);
                    System.out.println("    [x] done");
                }
            }
        };

        boolean autoAck = false;

        channel.basicConsume(TASK_QUEUE_NAME,autoAck,consumer);

//        channel.close();
//        connection.close();

    }

    private static void doWork(String message) throws Exception{
        for(char ch : message.toCharArray()){
            if(ch == '.') Thread.sleep(1000);
        }
    }
}
