package m4_routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;
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

        //定义直连交换机
        channel.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);

        //定义队列
        String queue = channel.queueDeclare().getQueue();
        //绑定队列
        System.out.print("输入绑定键,用空格分隔：");
        String key = new Scanner(System.in).nextLine();
        String[] split = key.split(" ");
        for(String s:split){
            channel.queueBind(queue,"direct_logs",s);
        }
        //消费队列
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                String msg = new String(message.getBody());
                String key = new String(message.getEnvelope().getRoutingKey());
                System.out.println(msg +"   " +key);
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
