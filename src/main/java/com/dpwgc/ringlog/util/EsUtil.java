package com.dpwgc.ringlog.util;

import com.dpwgc.ringlog.dao.LogMsg;
import com.dpwgc.ringlog.mapper.EsMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class EsUtil {

    @Resource
    EsMapper esMapper;

    public void setDoc(List<LogMsg> logs) {
        esMapper.saveAll(logs);
    }
}
