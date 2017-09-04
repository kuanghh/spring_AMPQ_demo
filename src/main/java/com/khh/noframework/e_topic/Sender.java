package com.khh.noframework.e_topic;

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
    public static String EXCHANGE2 = "exchange2";

    //创建三条队列，分别是 queue1，queue2，queue3
    public static String QUEUE1 = "e_queue1";
    public static String QUEUE2 = "e_queue2";
    public static String QUEUE3 = "e_queue3";

    //三个不同的routingKey
    public static String RKEY1 = "*.key1.*";
    public static String RKEY2 = "key2.#";

    public static void main(String[] args) throws Exception{

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明交换器
        channel.exchangeDeclare(EXCHANGE2, ExchangeTypes.TOPIC);

        //声明三条条队列
        channel.queueDeclare(QUEUE1,true,false,false,null);
        channel.queueDeclare(QUEUE2,true,false,false,null);
        channel.queueDeclare(QUEUE3,true,false,false,null);


        channel.queueBind(QUEUE1,EXCHANGE2,"*.key1.*");
        channel.queueBind(QUEUE2,EXCHANGE2,"key2.#");

        String message1 = " this is message1,i want to queue1 received";
        String message2 = " this is message2,i want to queue2 received";
        String message3 = " this is message3,i want to queue1 received";

        channel.basicPublish(EXCHANGE2,"abc.key1.abc", MessageProperties.PERSISTENT_TEXT_PLAIN,message1.getBytes());// 1
        channel.basicPublish(EXCHANGE2,"key2.123.456", MessageProperties.PERSISTENT_TEXT_PLAIN,message2.getBytes());// 2
        channel.basicPublish(EXCHANGE2,"abc.key1", MessageProperties.PERSISTENT_TEXT_PLAIN,message3.getBytes());// 3

        //预期结果:
        //      queue1会收到message1
        //      queue2会收到message2
        //      queue1不会收到message3,因为通配符是*.key1.*，发消息的routingkey是abc.key1，少了后面的words

        channel.close();
        connection.close();
    }

}
