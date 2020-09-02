package m3_publishsubstribe;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1.连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.64.140");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");

        Channel channel = factory.newConnection().createChannel();

        //2.定义交换机
        channel.exchangeDeclare("logs", BuiltinExchangeType.FANOUT.getType());

        //3.定义队列
//        String uuid = UUID.randomUUID().toString().replace("-", "");
//        channel.queueDeclare(uuid,false,true,true,null);

        //由服务器自动命名,再获取这个名字
        String queue = channel.queueDeclare().getQueue();
        //第三个参数对于发布订阅模式无效
        //绑定队列
        channel.queueBind(queue,"logs","");

        //4.消费队列
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                String msg = new String(message.getBody());
                System.out.println(msg);
            }
        };

        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {

            }
        };

        channel.basicConsume(queue,true,deliverCallback,cancelCallback);

    }

}
