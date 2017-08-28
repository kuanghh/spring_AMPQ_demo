package com.khh.demo1.test;

import com.khh.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created by 951087952@qq.com on 2017/8/28.
 *
 * 以下演示了 以同步的方式监听并获取数据
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/demo1/spring-rabbit.xml")
public class TestQuickStart {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue myQueue;

    @Autowired
    private Exchange myExchange;

    @Test
    public void testQuickStart1() throws Exception{

        //把用户对象发送到myQueue队列当中
        rabbitTemplate.convertAndSend(myExchange.getName(),myQueue.getName(),new User(21,"用户21",new Date()));

        //从myQueue队列当中接收对象
        User user = (User) rabbitTemplate.receiveAndConvert(myQueue.getName());
        System.out.println(user.toString());
    }


    /**
     * 以上方法可以写成如下的形式
     * @throws Exception
     */
    @Test
    public void testQuickStart2() throws Exception {
        rabbitTemplate.setExchange(myExchange.getName());
        rabbitTemplate.setRoutingKey(myQueue.getName());//从这里可以看出来RoutingKey就是目的地

        rabbitTemplate.convertAndSend(new User(21, "用户21", new Date()));

        Thread.sleep(1000);
        User user = (User) rabbitTemplate.receiveAndConvert(myQueue.getName());
        System.out.println(user.toString());
    }
}
