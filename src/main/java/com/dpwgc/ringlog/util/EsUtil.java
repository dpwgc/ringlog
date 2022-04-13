package com.dpwgc.ringlog.util;

import com.dpwgc.ringlog.dao.LogMsg;
import com.dpwgc.ringlog.mapper.EsMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Elasticsearch操作工具
 */
@Component
public class EsUtil {

    @Resource
    EsMapper esMapper;

    /**
     * 批量插入文档
     * @param logs 日志列表
     */
    public void setDoc(List<LogMsg> logs) {
        try {
            esMapper.saveAll(logs);
        }catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
