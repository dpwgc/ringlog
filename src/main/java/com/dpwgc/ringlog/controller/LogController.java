package com.dpwgc.ringlog.controller;

import com.dpwgc.ringlog.service.LogService;
import com.dpwgc.ringlog.util.ResultUtil;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

/**
 * 日志服务接口
 */
@CrossOrigin
@RequestMapping("/log")
@RestController
public class LogController {

    @Autowired
    LogService logService;

    @PostMapping("/getLog")
    public ResultUtil<Object> getLog(long start,
                                     long end,
                                     int lv,
                                     String tag,
                                     String content,
                                     String host,
                                     String file) {

        BasicDBObject queryLog = new BasicDBObject();

        //日期区间匹配
        if (end >= start) {
            queryLog.put("time", new BasicDBObject("$gte", start).append("$lte", end));
        } else {
            queryLog.put("time", new BasicDBObject("$gte", start));
        }

        //日志等级匹配
        if (lv > 0) {
            queryLog.put("lv", lv);
        }

        //日志标签模糊匹配
        if (tag.length() > 0) {
            Pattern tagPattern = Pattern.compile(tag, Pattern.CASE_INSENSITIVE);
            queryLog.put("tag",tagPattern);
        }

        //日志内容模糊匹配
        if (content.length() > 0) {
            Pattern contentPattern = Pattern.compile(content, Pattern.CASE_INSENSITIVE);
            queryLog.put("content",contentPattern);
        }

        //日志所属主机匹配
        if (host.length() > 0) {
            queryLog.put("host", host);
        }

        //日志所属文件路径模糊匹配
        if (file.length() > 0) {
            Pattern filePattern = Pattern.compile(file, Pattern.CASE_INSENSITIVE);
            queryLog.put("file",filePattern);
        }

        return logService.getLog(queryLog);
    }
}
