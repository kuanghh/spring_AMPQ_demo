package com.khh.demo2.test;

import com.khh.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
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
    private Queue queue;

    /**
     * 因为在spring.rabbit.xml中 配置了 listen-container ，配置了MyListener对myQueue的RoutingKey监听，
     * 当send信息到myQueue的时候，MyListener会监听得到
     * @throws Exception
     */
    @Test
    public void test1() throws Exception{
        rabbitTemplate.convertAndSend(queue.getName(),new User(12,"用户12",new Date()));
    }
}
