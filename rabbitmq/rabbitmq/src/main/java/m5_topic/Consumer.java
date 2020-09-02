package m5_topic;

import com.rabbitmq.client.*;
import org.omg.CORBA.StringHolder;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.64.140");
        factory.setUsername("admin");
        factory.setPassword("admin");

        Channel channel = factory.newConnection().createChannel();

        //定义交换机
        channel.exchangeDeclare("topic_logs", BuiltinExchangeType.TOPIC);

        //定义队列
        String queue = UUID.randomUUID().toString();
        channel.queueDeclare(queue,false,true,true,null);

        //绑定
        System.out.print("输出绑定键，用空格隔开:");
        String key = new Scanner(System.in).nextLine();
        String[] keys = key.split(" ");
        for (String s : keys){
            channel.queueBind(queue,"topic_logs",s);
        }

        //消费队列
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                String msg = new String(message.getBody());
                String routingKey = message.getEnvelope().getRoutingKey();
                System.out.println(msg +"-->"+routingKey);

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
