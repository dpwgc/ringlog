package com.dpwgc.ringlog.server;

import com.alibaba.fastjson.JSONObject;
import com.dpwgc.ringlog.dao.LogMsg;
import com.dpwgc.ringlog.util.LogUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Kafka消费者服务
 */
@Component
public class KafkaServer {

    /**
     * 批量消费消息
     * @param records 消息列表
     */
    @KafkaListener(id = "${spring.kafka.consumer.client-id}",
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.template.default-topic}")
    public void listen(List<ConsumerRecord<String, String>> records) {

        //日志信息列表
        List<LogMsg> logs = new ArrayList<>();

        for (ConsumerRecord<String, String> record : records) {

            //将json字符串转为LogMsg对象
            LogMsg logMsg = JSONObject.parseObject(record.value(), LogMsg.class);

            //将日志对象添加进日志列表
            logs.add(logMsg);
        }

        //批量插入日志
        LogUtil.set(logs);
    }
}

