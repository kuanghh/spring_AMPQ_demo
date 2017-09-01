package com.khh.noframework.c_publish_subscribe;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by 951087952@qq.com on 2017/9/1.
 *
 * 消息消费者
 *
 */
public class ReceiveLogs {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        /**
         * 这样会默认声明一条 non-durable, exclusive, autodelete 的临时队列 ，名字也是随机产生的
         */
        String queueName = channel.queueDeclare().getQueue();

        /**
         * 将指定的队列绑定到指定的交换器上，routingkey是一个路由键
         */
        channel.queueBind(queueName,EXCHANGE_NAME,"");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

}
