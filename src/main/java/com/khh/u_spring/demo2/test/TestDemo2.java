package com.khh.u_spring.demo2.test;

import com.khh.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;


/**
 * Created by 951087952@qq.com on 2017/8/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/demo2/spring-rabbit.xml")
public class TestDemo2 {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Exchange myExchange;

    @Autowired
    private Queue queue;

    /**
     * 因为在spring.rabbit.xml中 配置了 listen-container ，配置了MyListener对myQueue的RoutingKey监听，
     * 当send信息到myQueue的时候，MyListener会监听得到
     * @throws Exception
     */
    @Test
    public void test1() throws Exception{
        //没有这一行的话，会接收不到信息，因为Exchange是通过routingKey绑定Queue的
        //如果没有指定Exchange，那么消息不会发送到绑定了Exchange的Queue,它默认的Exchange是""
        rabbitTemplate.setExchange(myExchange.getName());
        rabbitTemplate.convertAndSend("routingKey_queue",new User(12,"用户12",new Date()));
        Thread.sleep(1000);
    }
}
