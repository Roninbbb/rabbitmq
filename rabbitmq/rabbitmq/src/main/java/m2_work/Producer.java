package m2_work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

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

        //2.定义队列
        channel.queueDeclare("task_queue",true,false,false,null);

        //3.发送消息
        while (true){
            System.out.print("输入消息：");
            String msg = new Scanner(System.in).nextLine();
            //exchange 是默认的交换机
            channel.basicPublish("","task_queue", MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes());
        }

        //关闭连接


    }
}
