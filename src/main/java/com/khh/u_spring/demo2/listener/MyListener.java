package com.khh.u_spring.demo2.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * Created by 951087952@qq.com on 2017/8/28.
 *  创建一个MDP
 */
public class MyListener implements MessageListener{
    @Override
    public void onMessage(Message message) {
        System.out.println("MyListener 监听信息如下：");
        System.out.println(message.toString());
    }
}
