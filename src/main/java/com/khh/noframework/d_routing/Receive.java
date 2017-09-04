package com.khh.noframework.d_routing;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by 951087952@qq.com on 2017/9/4.
 */
public class Receive {


    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.basicConsume(Sender.QUEUE1,true, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(Sender.QUEUE1 + " receive message :" + new String(body,"UTF-8"));
            }
        });

        channel.basicConsume(Sender.QUEUE2,true, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(Sender.QUEUE2 + " receive message :" + new String(body,"UTF-8"));
            }
        });

        channel.basicConsume(Sender.QUEUE3,true, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(Sender.QUEUE3 + " receive message :" + new String(body,"UTF-8"));
            }
        });

    }

}
