package com.khh.noframework.a_helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by 951087952@qq.com on 2017/9/1.
 *
 * 消息生产者
 */
public class Send {

    //队列的名称
    private static String  QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        //创建连接到RabbitMQ代理服务器的连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("localhost");
        //获取一个连接
        Connection connection = factory.newConnection();

        /**
         *  连接与通道的概念：
         *
         *       连接：当应用程序连接rabbitMQ代理服务器时，需要创建一条TCP连接，一旦TCP连接打开，应用程序就可以创建一条AMQP通道
         *
         *       通道：通道是建立在“真实的”TCP连接内的虚拟连接。AMQP命令都是通过通道发布出去的，每条通道都会被指派一个唯一ID。
         *              无论发送消息，订阅队列，接受消息，这些动作都是在通道完成的。这个很类似与activeMQ中的session概念
         *
         *        为什么需要通道？为什么不直接用TCP连接来发送AMQP命令？
         *             因为对于操作系统来说建立和销毁TCP连接是很昂贵的操作。使用通道能节省操作系统的开销
         *
         *         因为通道是有连接产生的，所以可以想象，将连接比喻为电缆，将通道比喻为光纤束
         *
         *
         */

        //创建一个新的通道
        Channel channel = connection.createChannel();

        /**
         * 声明发送到指定的队列
         *  参数一：队列名称
         *  参数二：队列是否持久化
         *  参数三：队列是否独有的，当声明之后，队列会变成私有的，连接会受到限制，此时只有你的应用程序才能够消费队列信息。当你想要限制一个队列只有一个消费者的时候很有帮助
         *  参数四：当声明为true之后，当最后一个消费者取消订阅的时候，队列就会自动移除(如果你使用临时队列只为一个消费者服务的话，请结合使用auto—delete和exclusive使用)
         *  参数五：其它信息的配置，以map的形式设置
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        String message = "hello World";

        /**
         * 发送消息：
         *
         *   参数一：交换器的名称
         *   参数二：队列的名称
         *   参数三： other properties for the message - routing headers etc
         *   参数四：message
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println(" send a message : " + message);


        //关闭连接
        channel.close();
        connection.close();
    }
}
