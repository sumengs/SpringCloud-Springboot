package com.sumeng.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @date: 2020/6/4 10:15
 * @author: sumeng
 */
public class Producer_Topic {

    //交换机名称
    static final String TOPIC_EXCHANGE = "topic_exchange";
    //队列名称
    static final String TOPIC_QUEUE_1 = "topic_queue_1";
    //队列名称
    static final String TOPIC_QUEUE_2 = "topic_queue_2";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //2. 设置参数
        factory.setHost("192.168.26.61");//ip  默认值 localhost
        factory.setPort(5672); //端口  默认值 5672
        factory.setVirtualHost("/");//虚拟机 默认值/
        factory.setUsername("guest");//用户名 默认 guest
        factory.setPassword("guest");//密码 默认值 guest
        //3. 创建连接 Connection
        Connection connection = factory.newConnection();
        //4. 创建Channel
        Channel channel = connection.createChannel();

        /*
         * 声明交换机
         * 参数1：交换机名称
         * 参数2：交换机类型，fanout、topic、direct、header
         */
        channel.exchangeDeclare(TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC);


        //发送信息
        String message = "新增了商品：Topic模式； routing key 为 item.insert";
        channel.basicPublish(TOPIC_EXCHANGE, "item.insert", null, message.getBytes());
        System.out.println("已发送消息：" + message);


        message = "修改了商品：Topic模式； routing key 为 item.update";
        channel.basicPublish(TOPIC_EXCHANGE, "item.update", null, message.getBytes());
        System.out.println("已发送消息：" + message);


        message = "删除了商品：Topic模式； routing key 为 item.delete";
        channel.basicPublish(TOPIC_EXCHANGE, "item.delete", null, message.getBytes());
        System.out.println("已发送消息：" + message);

        //关闭资源
        channel.close();
        connection.close();

    }

}
