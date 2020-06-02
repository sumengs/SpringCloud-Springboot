package com.sumeng.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @date: 2020/6/2 10:33
 * @author: sumeng
 */
@Component
public class RabbitMQListener {

    @RabbitListener(queues = "boot_queue")
    public void ListenerQueue(Message message){

        //System.out.println(message);
        System.out.println(new String(message.getBody()));
    }

}
