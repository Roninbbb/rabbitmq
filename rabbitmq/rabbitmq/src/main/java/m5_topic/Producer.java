package m5_topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1.连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.64.140");
        factory.setUsername("admin");
        factory.setPassword("admin");

        Channel channel = factory.newConnection().createChannel();

        //2.定义交换机
        channel.exchangeDeclare("topic_logs", BuiltinExchangeType.TOPIC);

        //3.发送消息，携带路由键
        while (true){
            System.out.print("输入消息：");
            String msg = new Scanner(System.in).nextLine();
            System.out.print("输入路由键：");
            String key = new Scanner(System.in).nextLine();
            channel.basicPublish("topic_logs",key,null,msg.getBytes());

        }


    }
}
