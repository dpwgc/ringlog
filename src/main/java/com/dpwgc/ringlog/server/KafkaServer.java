package com.dpwgc.ringlog.server;

import com.alibaba.fastjson.JSONArray;
import com.dpwgc.ringlog.dao.LogMsg;
import com.dpwgc.ringlog.util.LogUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Kafka消费者服务
 */
@Component
public class KafkaServer {

    @Resource
    LogUtil logUtil;

    /**
     * 批量消费消息
     * @param records 消息列表
     */
    @KafkaListener(id = "${spring.kafka.consumer.client-id}",
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.template.default-topic}")
    public void listen(List<ConsumerRecord<String, String>> records) {

        //日志信息列表logList
        List<LogMsg> logList = new ArrayList<>();

        for (ConsumerRecord<String, String> record : records) {

            //将json字符串转为LogMsg列表
            JSONArray jsonArray = JSONArray.parseArray(record.value());
            List<LogMsg> logs = jsonArray.toJavaList(LogMsg.class);

            //将logs添加进日志列表logList
            logList.addAll(logs);
        }

        //批量插入日志
        logUtil.set(logList);
    }
}

