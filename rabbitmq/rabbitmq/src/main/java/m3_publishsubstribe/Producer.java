package m3_publishsubstribe;

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
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");

        Channel channel = factory.newConnection().createChannel();

        //定义交换机
        channel.exchangeDeclare("logs", BuiltinExchangeType.FANOUT);

        //发送消息
        while (true){
            System.out.print("输入消息：");
            String msg = new Scanner(System.in).nextLine();
            //第二个参数，对fanout交换机无效
            channel.basicPublish("logs","",null,msg.getBytes());
        }
    }
}
