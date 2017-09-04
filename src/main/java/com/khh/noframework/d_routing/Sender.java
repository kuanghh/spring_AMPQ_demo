package com.khh.noframework.d_routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.springframework.amqp.core.ExchangeTypes;

/**
 * Created by 951087952@qq.com on 2017/9/4.
 *
 * 演示：一条队列可以 使用不同的 routing key 绑定多次 ，每条队列也可以使用相同的routing key 进行绑定
 */
public class Sender {

    //创建一个交换器
    public static String EXCHANGE1 = "exchange1";

    //创建三条队列，分别是 queue1，queue2，queue3
    public static String QUEUE1 = "queue1";
    public static String QUEUE2 = "queue2";
    public static String QUEUE3 = "queue3";

    //三个不同的routingKey
    public static String RKEY1 = "key1";
    public static String RKEY2 = "key2";
    public static String RKEY3 = "key3";

    public static void main(String[] args) throws Exception{

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明交换器
        channel.exchangeDeclare(EXCHANGE1, ExchangeTypes.DIRECT);

        //声明三条条队列
        channel.queueDeclare(QUEUE1,true,false,false,null);
        channel.queueDeclare(QUEUE2,true,false,false,null);
        channel.queueDeclare(QUEUE3,true,false,false,null);

        //绑定:
        //     使用 key1 将 queue1绑定到exchange
        //     使用 key2 将 queue2绑定到exchange
        //     使用 key3 将 queue2绑定到exchange
        //     使用 key1 将 queue3绑定到exchange

        channel.queueBind(QUEUE1,EXCHANGE1,RKEY1);
        channel.queueBind(QUEUE2,EXCHANGE1,RKEY2);
        channel.queueBind(QUEUE2,EXCHANGE1,RKEY3);
        channel.queueBind(QUEUE3,EXCHANGE1,RKEY1);

        String message1 = " this is message1,i want to queue2 received";
        String message2 = " this is message2,i want to queue1 and queue3 received";


        channel.basicPublish(EXCHANGE1,RKEY2, MessageProperties.PERSISTENT_TEXT_PLAIN,message1.getBytes());// 1
        channel.basicPublish(EXCHANGE1,RKEY3, MessageProperties.PERSISTENT_TEXT_PLAIN,message1.getBytes());// 2

        channel.basicPublish(EXCHANGE1,RKEY1, MessageProperties.PERSISTENT_TEXT_PLAIN,message2.getBytes());// 3

        //预期结果：basicPublish1 会让 queue2接收到，
        //          basicPublish2 会让 queue2接收到，
        //          basicPublish1 会让 queue1和queue3接收到

        channel.close();
        connection.close();
    }

}
