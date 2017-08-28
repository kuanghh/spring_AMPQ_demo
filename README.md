# Spring集成AMQP的demo，使用RabbitMQ作为中间件


### AMQP介绍
   (1)在JMS中，主要有三个参与者：消息的生产者，消息的消费者，以及生产者和消费者之间传递消息的通道（队列或者主题），其消息模型请参考spring_jms_demo中的README.MD
   (2)在JMS中，通道有助于解耦消息的生产者和消费者，但这两者依然会与通道耦合，AMQP的生产者并不会直接将消息发布到队列中。
   
   (3)__AMQP在消息的生产者以及传递者信息的队列之间引入了一种间接的机制:ExChange__,关系如下：
   
   __生产者--------> Exchage -----Binding-----> 队列 --------> 消费者__
    
   如上，可以看到，Exchange会绑定到一个或多个队列上，它负责将信息路由到队列上。信息的消费者会从队列中提取数据并进行处理
    
   AMQP定义了四钟不同类型Exchange，每一种都有不同的路由算法，这些算法决定了是否要将信息放到队列中。
   
   根据Exchange的算法不同，它可能会使用消息的routing key和/参数，并将其与Exchange和队列之间binding的routing key和参数进行对比。(routing key可大致理解为Email的收件人地址，指定了预期的接受者)
   如果对比结果满足响应的算法，那么消息将会路由到队列上，否则的话，将不会路由到队列上
   
   四钟标准的AMQP Exchange如下所示：
   
        Direct： 如果消息的routing key 与 binding 的 routing key 直接匹配的话，消息将会路由到该队列上
        Topic ： 如果消息的routing key 与 binding 的 rounting key 符合通配符匹配的话，消息将会路由到该队列上
        Headers ： 如果消息参数表中的头信息和值都与bingding参数列表中想匹配，消息将会路由到该队列上
        Fanout ： 不管消息的 routing key 和 参数列表的头信息/值是什么，消息将会路由到所有队列上
   
   
   (4)ConnectionFactory、Connection、Channel都是RabbitMQ对外提供的API中最基本的对象。Connection是RabbitMQ的socket链接，它封装了socket协议相关部分逻辑。ConnectionFactory为Connection的制造工厂。
      Channel是我们与RabbitMQ打交道的最重要的一个接口，我们大部分的业务操作是在Channel这个接口中完成的，包括定义Queue、定义Exchange、绑定Queue与Exchange、发布消息等
    

### 如何开启Rabbit服务(windows7 版本)
   (1)从官网下载http://www.rabbitmq.com/，下载完rabbitMQ，只有一个exe安装程序.
   
   (2)因为rabbitMQ的安装还需要Erlang支持，所以去官网下载 http://www.erlang.org/downloads
   
   (3)Erlang下载后只有一个exe，安装后即可安装rabbitMQ
   
   (4) 进入rabbit安装目录下的sbin目录，已管理员身份打开cmd，输入rabbitmq-server.bat start开启rabbitMQ服务
       如果出现错误：ERROR: node with name "rabbit" already running on "..."(其实是，当安装完后，就会自动启动服务，但因为需要配置监控，所以建议先停止服务)
       那么此时打开windows任务管理器(ctrl + alt + delete)，把服务中的rabbitmq服务停止掉，再重复以上操作即可
   
   (5)监控配置：先把开启了rabbitMQ服务的cmd窗口关闭，再新打开cmd窗口，进入sbin目录，输入rabbitmq-plugins enable rabbitmq_management，
        然后输入rabbitmq-service.bat stop停止rabbitMQ服务，如果提示“没有启动 RabbitMQ 服务“，不用管它
        接着输入rabbitmq-service.bat install
        最后输入rabbitmq-service.bat start，或者rabbitmq-server.bat start
        
   (6)打开浏览器，访问http://localhost:15672，输入账号:guest,密码:guest即可访问