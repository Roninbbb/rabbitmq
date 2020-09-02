package m1_simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1.建立连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.64.140");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");

        //生产者在与rabbitmq服务器连接的时候使用tcp连接，
        //连接过程非常耗时，所以多个生产者可以使用同一个tcp，
        // 而通道Channel就是区分是哪个生产者发送的消息
        Channel channel = factory.newConnection().createChannel();

        //2.定义队列
        //指定用来发送消息的队列，如果队列不存在，服务器会为你创建队列
        //如果已经存在，直接使用这个队列
        channel.queueDeclare(
                "helloworld",
                false,  //durable 是否是持久队列,服务器重启队列是否还存在
                false,  //exclusive 是否是排他队列(消费者独占的队列) ，是否共享
                false,  //autoDelete 是否自动删除(没有消费者时，自动删除队列)
                null);  //arguments 其他参数属性

        //3.发送消息
        channel.basicPublish(
                "",             //在第三个模式时用到再解释
                "helloworld",
                null,   //其他参数属性
                "helloworld".getBytes());

        System.out.println("消息已发送！");
        //断开连接
        channel.close();

    }
}
