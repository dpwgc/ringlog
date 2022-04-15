package com.dpwgc.ringlog.controller;

import com.dpwgc.ringlog.service.LogService;
import com.dpwgc.ringlog.util.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 日志检索接口
 */
@RequestMapping("/log")
@RestController
public class LogController {

    @Resource
    LogService logService;

    /**
     * 检索日志（通配符+时间范围）
     * @param value 检索内容
     * @param start 时间区间开头
     * @param end 时间区间结尾
     * @return ResultUtil<Object>
     */
    @GetMapping("/search")
    public ResultUtil<Object> search(String value,long start,long end){
        return logService.search(value,start,end);
    }
}
