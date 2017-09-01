package com.khh.u_spring.test;

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

    /**
     * Exchange是用RountingKey来绑定Queue的,发送只能指定routingKey_queue，但接收是从queue中接收的
     * @throws Exception
     */
    @Test
    public void testQuickStart1() throws Exception{

        //把用户对象发送到myQueue队列当中
        rabbitTemplate.convertAndSend(myExchange.getName(),"routingKey_queue",new User(21,"用户21",new Date()));

        //从myQueue队列当中接收对象
        User user = (User) rabbitTemplate.receiveAndConvert(myQueue.getName());
        System.out.println(user.toString());
    }


    /**
     * 以上方法可以写成如下的形式
     *
     * Exchange是用RountingKey来绑定Queue的
     * @throws Exception
     */
    @Test
    public void testQuickStart2() throws Exception {
        rabbitTemplate.setExchange(myExchange.getName());
        rabbitTemplate.setRoutingKey("routingKey_queue");

        rabbitTemplate.convertAndSend(new User(21, "用户21", new Date()));

        Thread.sleep(1000);
        User user = (User) rabbitTemplate.receiveAndConvert(myQueue.getName());
        System.out.println(user.toString());
    }
}
