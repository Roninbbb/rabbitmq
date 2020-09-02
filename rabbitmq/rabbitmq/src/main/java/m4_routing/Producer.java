package m4_routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import javax.sound.midi.Soundbank;
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

        //定义直连交换机
        channel.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);

        //向交换机发送消息，在消息上需要携带路由键
        while (true){
            System.out.print("输入消息：");
            String msg = new Scanner(System.in).nextLine();
            System.out.print("输入路由键：");
            String key = new Scanner(System.in).nextLine();
            channel.basicPublish("direct_logs",key,null,msg.getBytes());
        }

    }
}
