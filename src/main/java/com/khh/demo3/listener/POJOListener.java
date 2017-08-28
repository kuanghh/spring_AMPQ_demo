package com.khh.demo3.listener;

import com.khh.entity.User;
import org.springframework.amqp.core.Message;

/**
 * Created by 951087952@qq.com on 2017/8/28.
 * 自定义MDP，不需要实现MessageListener接口
 */
public class POJOListener {

    public void myMessage(Message message)throws Exception {
        System.out.println("POJOListener 监听信息如下:  ");
        System.out.println(message.toString());
    }

    public void myMessage(User user)throws Exception {
        System.out.println("POJOListener 监听信息如下:  ");
        System.out.println(user.toString());
    }

}
