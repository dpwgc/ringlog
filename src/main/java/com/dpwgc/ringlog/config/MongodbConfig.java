package com.dpwgc.ringlog.config;

import com.dpwgc.ringlog.util.Md5Util;
import com.dpwgc.ringlog.util.MongodbUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * Mongodb配置类
 */
@Configuration
public class MongodbConfig implements InitializingBean {

    @Value("${spring.data.mongodb.uri}")
    private String uri;

    @Value("${spring.data.mongodb.database}")
    private String database;

    /**
     * mongodb连接
     */
    public static DB db;

    /**
     * spring boot项目启动后自动执行mongodb连接初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        System.out.println("[Ring Log] MongoDB loading...");

        db = MongodbUtil.connDB(uri,database);

        //获取用户集合user_info
        DBCollection coll = MongodbUtil.getColl(db,"user_info");
        //如果发现mongodb中没有user_info集合或者user_info集合为空
        if (coll.count() == 0) {
            //自动新建用户，默认用户名admin，密码123456（md5加密存入user_info）
            BasicDBObject doc = new BasicDBObject();
            doc.put("user", "admin");
            doc.put("pwd", Md5Util.getMd5("123456"));
            MongodbUtil.setDoc("user_info",doc);
        }

        System.out.println("[Ring Log] MongoDB database:"+db.toString());
    }

    /**
     * 获取mongodb连接
     * @return DB
     */
    public static DB getDB() {
        return db;
    }
}
