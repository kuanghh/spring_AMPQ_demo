package com.khh.noframework.helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;


/**
 * Created by 951087952@qq.com on 2017/9/1.
 *
 * 信息的接受者
 *
 */
public class Receive {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");


        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                String message = new String(body,"UTF-8");
                System.out.println("message receive : " + message);

            }
        };

        /**
         * 接受消息：
         *
         *  参数一：队列名称
         *  参数二：是否自动回复，这里解释一下...
         *      自动回复是一个什么样的概念：在rabbitMQ当中，Producer将消息发送到rabbitMQ，再从rabbitMQ中把消息发送到consumer，
         *      每次consumer接受到信息后，都要返回一个确认的消息到rabbitMQ中，当rabbitMQ接受到consumer的确认消息之后，才会把
         *      Producer的消息删除，如果consumer没有确认消息，那么rabbitMQ将不会删除Producer的消息
         *
         *         注意：消费者对消息的确认 和 告诉生产者消息已经被接受了 这两件事毫不相关
         *
         *  参数三：接受消息后，用来处理消息的对象
         */
        channel.basicConsume(QUEUE_NAME,true,consumer);

//        不关闭流，可确保程序一直监听队列
//        channel.close();
//        connection.close();
    }

}
