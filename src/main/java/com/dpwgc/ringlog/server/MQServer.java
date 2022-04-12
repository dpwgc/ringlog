package com.dpwgc.ringlog.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dpwgc.ringlog.dao.LogMsg;
import com.dpwgc.ringlog.util.LogUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 本地MQ消费服务
 */
@Component
public class MQServer implements InitializingBean {

    //本地消息队列
    public static ConcurrentLinkedQueue<byte[]> mq;

    /**
     * spring boot项目启动后自动开启消息队列消费线程
     */
    @Override
    public void afterPropertiesSet() {
        new Thread(() -> {

            //初始化本地队列
            mq = new ConcurrentLinkedQueue<>();
            System.out.println("[Ring Log] MQ server run");

            while (true) {

                //从本地队列中读取日志消息
                byte[] buffer = mq.poll();

                if(buffer != null) {
                    try {
                        //将字节数组序列化为json字符串
                        String jsonStr = JSON.parse(buffer).toString();

                        //将json字符串转为LogMsg对象
                        LogMsg logMsg = JSONObject.parseObject(jsonStr, LogMsg.class);

                        //插入日志
                        LogUtil.set(logMsg);

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }).start();
    }
}
