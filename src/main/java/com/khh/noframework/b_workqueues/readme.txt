

这个例子是，演示 rabbitMQ使用 round-robin的方式吧消息发送给消费者
也就是说，当Rabbit队列拥有多个消费者的时候，队列收到的消息将以循环（round-robin）的方法发给消费者，每条消息只会发送给一个订阅的消费者

演示时，需要启动一个NewTask和两个Worker（先启动两个Wokrer方便是观测）

当启动了两个 Worker（一个命名为wa，另一个命名为wb）后，
第一次启动 NewTask发送消息，wa收到消息,并确认消息
第二次启动 NewTask发送消息，wb收到消息,并确认消息
第三次启动 NewTask发送消息，wa收到消息,并确认消息
第四次启动 NewTask发送消息，wb收到消息,并确认消息
第五次启动 NewTask发送消息，wa收到消息,并确认消息
                .
                .
                .

当rabbitMQ退出了或者死机了，那么之前创建的队列和消息会很容易丢失，怎样才能防止这样的事情发生了？
   一、确保队列是持久化的，   channel.queueDeclare(..,true,..,..,..); 这样就算rabbitMQ重启后，队列依然存在
   二、确保消息也是持久化的，setting MessageProperties (which implements BasicProperties) to the value PERSISTENT_TEXT_PLAIN.

但注意：标记消息持久化并不意味着所有消息能完全不丢失
    虽然，它会告诉rabbitMQ把消息存放到磁盘（是磁盘，而不是内存）上，
    但依然存在一个很短时间的情况，RabbitMQ已经接受信息并没有保存它，
            （个人理解：当rabbitMQ已经接受到信息后，rabbitMQ马上挂掉，此时还没有保存已经接受到的消息）
      Also, RabbitMQ doesn't do fsync(2) for every message -- it may be just saved to cache and not really written to the disk.
      The persistence guarantees aren't strong, but it's more than enough for our simple task queue.
      If you need a stronger guarantee then you can use publisher confirms（https://www.rabbitmq.com/confirms.html）.





注意：当在rabbitMQ中声明了一条队列之后，那么在声明同一条队列的时候
        若，声明的队列属性一致，则什么都不做，并返回true
        若，声明的队列属性不一致，那么直接返回一个error

