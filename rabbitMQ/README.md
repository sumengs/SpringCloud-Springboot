## RabbitMQ

#### 什么是MQ

 - 定义：是一个MessageQueue，用于系统与系统之间进行消息通信
 - 优势：
     - 应用解耦：提升应用的可维护性和容错性，可扩展性
     - 异步并发：提升应用的并发吞吐性能，提高系统性能
     - 削峰填谷：提升应用的高可用
     - 分布式事务：提供分布式事务解决方案，解决数据的一致性、原子性等
 - 劣势
     - 系统可用性降低
         - 系统引入的外部依赖越多，系统稳定性越差，一旦MQ宕机，就会对业务造成影响。如何保证MQ的高可用
     - 系统复杂度提高
         - MQ 的加入大大增加了系统的复杂度，以前系统间是同步的远程调用，现在是通过MQ进行异步调用。如何保证消息没有没重复消费？怎么处理消息丢失情况？那么保证消息传递的顺序性
     - 一致性问题
         - A系统处理完业务，通过MQ给B、C、D三个系统发消息数据。如果B系统、C系统处理成功、D系统处理失败。如何保证消息数据处理的一致性？
         
#### 什么是RabbitMQ

 - 定义：基于AMQP协议实现的MQ
 - 组成：
    - 生产者：Producer
        - Connection
            - Channel
    - 消费者：Consumer
        - Connection
         - Channel
    - 服务器：Broker
        - VirtualHost（database）
            - Exchange
                - Binding
                    - RouteKey
            - Queue
              
#### 生产者发送消息

 - 创建通讯通道
     - 建立连接（5个属性）
         - ip
         - port
         - 用户名和密码
         - VirtualHost
     - 创建通道Channel
     ```java
     //1.创建连接工厂
     ConnectionFactory factory = new ConnectionFactory();
     //2. 设置参数
     factory.setHost("172.16.98.133");//ip  默认值 localhost
     factory.setPort(5672); //端口  默认值 5672
     factory.setVirtualHost("/itcast");//虚拟机 默认值/
     factory.setUsername("heima");//用户名 默认 guest
     factory.setPassword("heima");//密码 默认值 guest
     //3. 创建连接 Connection
     Connection connection = factory.newConnection();
     //4. 创建Channel
     Channel channel = connection.createChannel();
     ```
 - 发送消息
     - 定义
        - 定义exchange
        - 定义队列
        - 定义绑定关系
    ```java
    /* 参数：
        1. queue：队列名称
        2. durable:是否持久化，当mq重启之后，还在
        3. exclusive：
            * 是否独占。只能有一个消费者监听这队列
            * 当Connection关闭时，是否删除队列
            *
        4. autoDelete:是否自动删除。当没有Consumer时，自动删除掉
        5. arguments：参数。

     */
    //如果没有一个名字叫hello_world的队列，则会创建该队列，如果有则不会创建
    channel.queueDeclare("hello_world",true,false,false,null);
    
    String exchangeName = "test_fanout";
    //5. 创建交换机
    channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT,true,false,false,null);
    //6. 创建队列
    String queue1Name = "test_fanout_queue1";
    String queue2Name = "test_fanout_queue2";
    channel.queueDeclare(queue1Name,true,false,false,null);
    channel.queueDeclare(queue2Name,true,false,false,null);
    //7. 绑定队列和交换机
    /*
    queueBind(String queue, String exchange, String routingKey)
    参数：
        1. queue：队列名称
        2. exchange：交换机名称
        3. routingKey：路由键，绑定规则
            如果交换机的类型为fanout ，routingKey设置为""
     */
    channel.queueBind(queue1Name,exchangeName,"");
    channel.queueBind(queue2Name,exchangeName,"");
    ```
     - 发送消息
     ```java
     /*
     basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body)
     参数：
         1. exchange：交换机名称。简单模式下交换机会使用默认的 ""
         2. routingKey：路由名称
         3. props：配置信息
         4. body：发送消息数据

      */

     String body = "hello rabbitmq~~~";

     //6. 发送消息
     channel.basicPublish("","hello_world",null,body.getBytes());
     ```
 - 释放资源
```java
//7.释放资源
channel.close();
connection.close();
```

#### 消费者的消息接收

- 创建通讯通道
- 接收消息
  - 定义消息的处理逻辑
    ```java
    // 接收消息
    Consumer consumer = new DefaultConsumer(channel){
        /*
            回调方法，当收到消息后，会自动执行该方法

            1. consumerTag：标识
            2. envelope：获取一些信息，交换机，路由key...
            3. properties:配置信息
            4. body：数据

         */
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            System.out.println("consumerTag："+consumerTag);
            System.out.println("Exchange："+envelope.getExchange());
            System.out.println("RoutingKey："+envelope.getRoutingKey());
            System.out.println("properties："+properties);
            System.out.println("body："+new String(body));
        }
    };
    ```
  - 监听消息

    ```java
    channel.basicConsume("hello_world",true,consumer);
    ```
- 释放资源，不需要

#### 几种模式的区别

> - 在Rabbitmq中一个队列可以有多个Consumer，一条消息在同一时间只发送给一个队列的一个Consumer，且Rabbitmq使用轮询机制来分发消息。

| 项         | Simple   | Work     | Pub/Sub            | Routing          | Topic                 |
| ---------- | -------- | -------- | ------------------ | ---------------- | --------------------- |
| exchange   | 默认     | 默认     | 自定义（fanout）   | 自定义（Direct） | 自定义（Topic）       |
| routingKey | 队列名称 | 队列名称 | 空字符串           | 自定义（精确）   | 自定义（模糊）        |
| consumer   | 1        | 多个     | 多个               | 多个             | 多个                  |
| 特点       |          |          | 所有对接都收到消息 |                  | #:多个匹配，*单个匹配 |

     
#### RabbitMQ 工作模式

1. 简单模式（hello world 模式）
2. work queues
3. Publish/Subscribe发布订阅模式
4. Routing路由模式
5. Topic主题模式
6. RPC远程调用模式
