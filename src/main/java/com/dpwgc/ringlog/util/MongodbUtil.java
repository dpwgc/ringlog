package com.dpwgc.ringlog.util;

import com.dpwgc.ringlog.config.MongodbConfig;
import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Mongodb连接工具类
 */
public class MongodbUtil {

    /**
     * 与mongodb/mongodb集群建立连接
     * @param uri mongodb连接信息
     * @param database 数据库名称
     * @return DB
     * @throws UnknownHostException
     */
    public static DB connDB(String uri,String database) throws UnknownHostException {
        MongoClientURI mongoClientURI = new MongoClientURI(uri);

        MongoClient mongoClient = new MongoClient(mongoClientURI);

        return mongoClient.getDB(database);
    }

    /**
     * 选取数据库中的指定集合
     * @param db 数据库连接
     * @param collName 集合名称
     * @return DBCollection
     */
    public static DBCollection getColl(DB db,String collName) {
        return db.getCollection(collName);
    }

    /**
     * 在指定集合中插入文档
     * @param collName 集合名称
     * @param doc 要插入的文档对象
     * @return boolean
     */
    public static boolean setDoc(String collName, BasicDBObject doc) {

        //获取指定的mongodb集合
        DBCollection coll = getColl(MongodbConfig.getDB(),collName);

        try {
            //插入文档
            coll.insert(doc);
            return true;
        }catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    /**
     * 在指定集合中批量插入文档
     * @param collName 集合名称
     * @param docs 要插入的文档列表
     * @return boolean
     */
    public static boolean setDoc(String collName, List<DBObject> docs) {

        //获取指定的mongodb集合
        DBCollection coll = getColl(MongodbConfig.getDB(),collName);

        try {
            //批量插入文档
            coll.insert(docs);
            return true;
        }catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    /**
     * 获取指定集合中的文档
     * @param collName 集合名称
     * @param query 查询条件
     * @return List<DBObject>
     */
    public static List<DBObject> getDoc(String collName, BasicDBObject query) {

        DBCursor cursor = getColl(MongodbConfig.getDB(),collName).find(query);

        return cursor.toArray();
    }
}
