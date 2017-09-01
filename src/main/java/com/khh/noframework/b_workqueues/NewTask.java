package com.khh.noframework.b_workqueues;

import com.rabbitmq.client.*;

/**
 * Created by 951087952@qq.com on 2017/9/1.
 *
 * 消息生产者
 *
 * 这个例子用来演示 rabbitMQ使用 round-robin的方式吧消息发送给消费者
 *
 * 当rabbitMQ退出了或者死机了，那么之前创建的队列和消息会很容易丢失，怎样才能防止这样的事情发生了？
 *   一、确保队列是持久化的，   channel.queueDeclare(..,true,..,..,..); 这样就算rabbitMQ重启后，队列依然存在
 *   二、确保消息也是持久化的，setting MessageProperties (which implements BasicProperties) to the value PERSISTENT_TEXT_PLAIN.
 *
 *
 *  注意：当在rabbitMQ中声明了一条队列之后，那么在声明同一条队列的时候
 *       若，声明的队列属性一致，则什么都不做，并返回true
 *       若，声明的队列属性不一致，那么直接返回一个error
 *
 */
public class NewTask {

    //队列的名称
    private static String  TASK_QUEUE_NAME = "task_queu";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();


        channel.queueDeclare(TASK_QUEUE_NAME,true,false,false,null);

        String[] messages = {"xiaoming.","zhangsan...","lisi....."};
        String strs = getString(messages);

        System.out.println("message send : " + strs);

        /**
         * MessageProperties.PERSISTENT_TEXT_PLAIN,确保发送的消息持久化
         */
        channel.basicPublish("",TASK_QUEUE_NAME,MessageProperties.PERSISTENT_TEXT_PLAIN,strs.getBytes("UTF-8"));

        channel.close();
        connection.close();
    }

    public static String getString(String[] strings) throws Exception{
        if(strings.length < 1){
            return "hello world";
        }
        return joinString(strings," ");
    }

    public static String joinString(String[] strings,String delimiter) throws Exception{

        int length = strings.length;
        if(length == 0){
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(strings[0]);

        for (int i = 1; i < length; i++) {
            stringBuilder.append(delimiter).append(strings[i]);
        }
        return stringBuilder.toString();
    }

}
