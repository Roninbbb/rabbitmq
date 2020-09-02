package m2_work;

import com.rabbitmq.client.*;

import javax.sound.midi.Soundbank;
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

        Channel channel = factory.newConnection().createChannel();
        //2.定义队列
        channel.queueDeclare("task_queue",true,false,false,null);

        //3.消费队列
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                byte[] body = message.getBody();
                String msg = new String(body);
                System.out.println(msg);
                for (int i=0;i<msg.length();i++){
                    if ('.' == msg.charAt(i)){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
                //        channel.basicAck(回执，是否同时确认多条消息);
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
                System.out.println("消息处理完毕！");
            }
        };

        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {

            }
        };

        //每次只抓取一条数据，默认没有数量限制
        channel.basicQos(1);

        //设置手动ack ，手动ack可以保存数据不丢失，一直等到消费者处理完把回执发回给rabbitmq
        //rabbitmq才会把队列消息删除
        channel.basicConsume("task_queue",false,deliverCallback,cancelCallback);

    }
}
