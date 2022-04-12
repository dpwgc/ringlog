package com.dpwgc.ringlog.server;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 本地缓冲队列消费者服务
 */
@Component
public class MqServer implements InitializingBean {

    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    //本地缓冲队列消费者线程数量
    @Value("${mq.threadNum}")
    private int threadNum;

    //要使用的kafka主题
    @Value("${spring.kafka.template.default-topic}")
    private String kafkaTopic;

    //本地缓冲队列
    public static ConcurrentLinkedQueue<byte[]> mq;

    /**
     * spring boot项目启动后自动开启消息队列消费线程
     */
    @Override
    public void afterPropertiesSet() {

        //初始化本地队列
        mq = new ConcurrentLinkedQueue<>();
        System.out.println("[Ring Log] MQ server run");

        for(int i=0;i<threadNum;i++) {

            //启动消费者线程
            System.out.println("[Ring Log] MQ consumer thread-"+i);
            new Thread(this::consume).start();
        }
    }

    /**
     * 消费者服务
     */
    public void consume() {
        while (true) {

            //从本地缓冲队列中取出日志信息
            byte[] buffer = mq.poll();

            if(buffer != null) {
                try {
                    //将字节数组序列化为json字符串
                    String jsonStr = JSON.parse(buffer).toString();

                    //将json序列化后的日志信息插入kafka消息队列
                    kafkaTemplate.send(kafkaTopic, jsonStr);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
