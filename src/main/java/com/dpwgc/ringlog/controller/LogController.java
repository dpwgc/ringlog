package com.dpwgc.ringlog.controller;

import com.dpwgc.ringlog.service.LogService;
import com.dpwgc.ringlog.util.ResultUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 日志检索接口
 */
@CrossOrigin
@RequestMapping("/log")
@RestController
public class LogController {

    @Resource
    LogService logService;

    /**
     * 检索日志（通配符+时间范围）
     * @param value1 检索条件1
     * @param value2 检索条件2
     * @param value3 检索条件3
     * @param start 时间区间开头
     * @param end 时间区间结尾
     * @return ResultUtil<Object>
     */
    @PostMapping("/search")
    public ResultUtil<Object> search(String value1,String value2,String value3,long start,long end){
        return logService.search(value1,value2,value3,start,end);
    }
}
