package m1_simple;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1.连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.64.140");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");

        //生产者在与rabbitmq服务器连接的时候使用tcp连接，
        //连接过程非常耗时，所以多个生产者可以使用同一个tcp，
        // 而通道Channel就是在tcp里区分是哪个生产者发送的消息
        Channel channel = factory.newConnection().createChannel();

        //2.定义队列
        //告诉服务器想使用 helloworld 队列
        //服务器检查如果不存在，会新建队列
        channel.queueDeclare("helloworld",false,false,false,null);


        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                byte[] body = delivery.getBody();
                String msg = new String(body);
//                char[] chars = msg.toCharArray();
//                for (char r : chars){
//                    if (r == 'o'){
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    System.out.print(r);
//                }
                System.out.println(msg);
            }
        };

        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {

            }
        };

        //3.消费队列
        channel.basicConsume("helloworld",true,deliverCallback,cancelCallback);



    }
}
