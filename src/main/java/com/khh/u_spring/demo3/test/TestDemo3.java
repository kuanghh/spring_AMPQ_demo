package com.khh.u_spring.demo3.test;

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
 *
 *  demo3 是看到了demo2中的MDP需要实现MessageListener接口，为了不实现那个接口，提供了MessageListenerAdapter，
 *  详细使用方法请看配置文件
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/demo3/spring-rabbit.xml")
public class TestDemo3 {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    @Autowired
    private Exchange myExchange;

    /**
     * 因为在spring.rabbit.xml中 配置了 listen-container ，配置了MyListener对myQueue的RoutingKey监听，
     * 当send信息到myQueue的时候，MyListener会监听得到
     * @throws Exception
     */
    @Test
    public void test1() throws Exception{
        rabbitTemplate.setExchange(myExchange.getName());
        rabbitTemplate.convertAndSend("routingKey_queue",new User(12,"用户12",new Date()));
//        rabbitTemplate.convertAndSend(queue.getName(),"1324");
        Thread.sleep(2000);
    }
}
