## GateWay

#### 网关概述

 - 网关旨在为微服务架构提供一种简单而有效的统一的API路由管理方式
 - 在微服务架构中，不同的微服务可以有不同的网络地址，各个微服务之间通过互相调用完成用户请求，客户
   端可能通过调用N个微服务的接口完成一个用户请求。
    - 存在的问题
        - 客户端多次请求不同的微服务，增加客户端的复杂性
        - 认证复杂，每个服务都要进行认证
        - http请求不同服务次数增加，性能不高
 - 网关就是系统的入口，封装了应用程序的内部结构，为客户端提供统一服务，一些与业务本身功能无关的公共逻辑可以在这里实现，诸如认证、鉴权、监控、缓存、负载均衡、流量管控、路由转发等
 - 在目前的网关解决方案里，有Nginx+ Lua、Netflix Zuul 、Spring Cloud Gateway等等

#### 网关快速入门

 1. 搭建网关模块

 2. 引入依赖：starter-gateway

    ```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
    ```

 3. 编写启动类

 4. 编写配置文件

    - id: 自定义的路由名称，需要全局唯一
    - uri: 路由的目的地
    - predicates: 过滤请求URI，找到要路由转发请求
        - Path: 通过请求路径来过滤

    ```yml
    spring:
      cloud:
        # 网关配置
        gateway:
          # 路由配置：转发规则
          routes: #集合。
          # id: 唯一标识。默认是一个UUID
          # uri: 转发路径
          # predicates: 条件,用于请求网关路径的匹配规则
          # filters：配置局部过滤器的
    
          - id: gateway-provider
            # 静态路由
            # uri: http://localhost:8001/
            # 动态路由
            uri: lb://GATEWAY-PROVIDER
            predicates:
            - Path=/goods/**
            filters:
            - AddRequestParameter=username,zhangsan
    ```

 5. 启动测试

#### 网关路由配置 - 静态路由

#### 网关路由配置 - 动态路由

 - 引入eureka-client配置
 - 修改uri属性：uri:lb://服务名称

#### 网关路由配置 - 微服务名称配置

```yml
spring: 
  cloud: 
    discovery: 
      locator: 
        enable: true # 开启微服务发现功能
          lower-case-service-id: true # 将请求路径上的服务器名配置为小写
```

#### 网关过滤器

 - Gateway 支持过滤器功能，对请求或进行拦截，完成一些通用操作
 - Gateway 提供两种过滤器方式："pre"和"post"
 - pre 过滤器，在转发之前执行，可以做参数校验、权限校验、流量监控、日志输出、协议转换等
 - post 过滤器，在响应之前执行，可以做响应内容，响应头的修改，日志的输出，流量监控等
 - Gateway 还提供了两种类型过滤器
     - GatewayFilter：全局过滤器，针对单个路由
     - GlobalFilter：全局过滤器，针对所有路由
     
#### 网关局部过滤器

 - GatewayFilter 局部过滤器，是针对单个路由的过滤器
 - 在Spring Cloud Gateway 组件中提供了大量内置的局部过滤器，对请求和响应做过滤操作
 - 遵循约定大于配置的思想，只需要在配置文件配置局部过滤器名称，并为其指定对应的值，就可以让其生效

#### 网关全局过滤器

 - GlobalFilter 全局过滤器，不需要在配置文件中配置，系统初始化时加载，并作用在每个路由上

 - Spring Cloud Gateway 核心的功能也是通过内置的全局过滤器来完成

 - 自定义全局过滤器步骤：
    1. 定义类实现 GlobalFilter 和 Ordered 接口
    
    ```java
    @Component
    public class MyFilter implements GlobalFilter, Ordered {
        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    
            System.out.println("自定义全局过滤器执行了~~~");
    
            return chain.filter(exchange);//放行
        }
    
        /**
         * 过滤器排序
         * @return 数值越小 越先执行
         */
        @Override
        public int getOrder() {
            return 0;
        }
    }
    ```
    
    2. 复写方法
    
    3. 完成逻辑处理

![image-全局过滤器](image\image-全局过滤器.png)