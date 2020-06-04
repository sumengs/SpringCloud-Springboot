package com.sumeng.rabbitmq.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.io.UnsupportedEncodingException;

/**
 * 队列监听器
 * @date: 2020/6/4 11:27
 * @author: sumeng
 */
public class SpringQueueListener implements MessageListener {

    @Override
    public void onMessage(Message message) {

        try {
            String msg = new String(message.getBody(), "utf-8");
            System.out.printf("接收路由名称为：%s，路由键为：%s，队列名为：%s的消息：%s \n",
                    message.getMessageProperties().getReceivedExchange(),
                    message.getMessageProperties().getReceivedRoutingKey(),
                    message.getMessageProperties().getConsumerQueue(),
                    msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
