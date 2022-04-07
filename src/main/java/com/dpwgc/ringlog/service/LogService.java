package com.dpwgc.ringlog.service;

import com.dpwgc.ringlog.util.MongodbUtil;
import com.dpwgc.ringlog.util.ResultUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 日志服务
 */
@Service
public class LogService {

    public ResultUtil<Object> getLog(BasicDBObject query) {
        ResultUtil<Object> resultUtil = new ResultUtil<>();

        List<DBObject> list = MongodbUtil.getDoc("log_data",query);

        resultUtil.setCode(200);
        resultUtil.setMsg("success");
        resultUtil.setData(list);
        return resultUtil;
    }
}
