package com.khh.noframework.c_publish_subscribe;

import com.khh.noframework.b_workqueues.NewTask;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.http.client.domain.ExchangeType;
import org.springframework.amqp.core.ExchangeTypes;

/**
 * Created by 951087952@qq.com on 2017/9/1.
 *
 * 消息生产者
 */
public class EmitLog {

    //交换器的名称
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception{

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        /**
         * 声明一个交换器,类型是 fanout ，会发送到所有队列上
         */
        channel.exchangeDeclare(EXCHANGE_NAME, ExchangeTypes.FANOUT);

        String[] messages = {"message1..","message2....","message3......"};
        String message = NewTask.getString(messages);

        channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());

        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }

}
