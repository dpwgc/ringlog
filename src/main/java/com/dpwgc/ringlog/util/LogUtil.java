package com.dpwgc.ringlog.util;

import com.dpwgc.ringlog.dao.LogMsg;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志记录工具（基于mongodb）
 */
public class LogUtil {

    /**
     * 记录日志
     * @param logMsg 日志信息
     * @return boolean
     */
    public static boolean set(LogMsg logMsg) {

        BasicDBObject doc = new BasicDBObject();

        doc.put("lv", logMsg.getLv());
        doc.put("tag", logMsg.getTag());
        doc.put("content", logMsg.getContent());
        doc.put("host", logMsg.getHost());
        doc.put("file", logMsg.getFile());
        doc.put("note", logMsg.getNote());
        doc.put("line", logMsg.getLine());
        doc.put("time", logMsg.getTime());

        //向指定记录集合插入日志文档
        return MongodbUtil.setDoc("log_data",doc);
    }

    /**
     * 批量记录日志
     * @param logs 日志信息列表
     * @return boolean
     */
    public static boolean set(List<LogMsg> logs) {

        List<DBObject> docs = new ArrayList<>();

        for (LogMsg log : logs) {

            DBObject doc = new BasicDBObject();

            doc.put("lv", log.getLv());
            doc.put("tag", log.getTag());
            doc.put("content", log.getContent());
            doc.put("host", log.getHost());
            doc.put("file", log.getFile());
            doc.put("note", log.getNote());
            doc.put("line", log.getLine());
            doc.put("time", log.getTime());

            docs.add(doc);
        }

        //向指定记录集合批量插入日志文档
        return MongodbUtil.setDoc("log_data",docs);
    }
}
