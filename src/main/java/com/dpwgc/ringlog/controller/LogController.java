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
@RequestMapping("/log")
@RestController
public class LogController {

    @Autowired
    LogService logService;

    /**
     * 根据检索条件获取日志列表
     * @param start 时间区间（开始）
     * @param end 时间区间（结尾）
     * @param lv 日志级别
     * @param tag 日志标签
     * @param content 日志内容
     * @param host 日志所属主机
     * @param file 日志所属文件
     * @param note 日志备注信息
     * @return ResultUtil<Object>
     */
    @PostMapping("/getLog")
    public ResultUtil<Object> getLog(@RequestParam("start") long start,
                                     @RequestParam("end") long end,
                                     @RequestParam("lv") int lv,
                                     @RequestParam("tag") String tag,
                                     @RequestParam("content") String content,
                                     @RequestParam("host") String host,
                                     @RequestParam("file") String file,
                                     @RequestParam("note") String note) {

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

        //日志备注模糊匹配
        if (note.length() > 0) {
            Pattern notePattern = Pattern.compile(note, Pattern.CASE_INSENSITIVE);
            queryLog.put("note",notePattern);
        }

        return logService.getLog(queryLog);
    }
}
