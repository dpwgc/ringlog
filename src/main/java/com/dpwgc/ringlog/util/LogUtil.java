package com.dpwgc.ringlog.util;

import com.dpwgc.ringlog.dao.LogMsg;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志记录工具（基于mongodb）
 */
@Component
public class LogUtil {

    @Resource
    EsUtil esUtil;

    /**
     * 记录日志
     * @param logs 日志信息
     * @return boolean
     */
    public void set(List<LogMsg> logs) {
        esUtil.setDoc(logs);
    }
}
