package com.dpwgc.ringlog.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 日志信息模板
 */
@Document(indexName = "#{@indexName}")
public class LogMsg {

    @Id
    @Field(name = "id", type = FieldType.Keyword)
    private String id;      //由es自动创建id

    /**
     * 日志等级lv（int类型）
     * 1 Emergency: system is unusable 导致系统不可用的事故
     * 2 Alert: action must be taken immediately 必须立即处理的问题
     * 3 Critical: critical conditions 需要立即修复的紧急情况
     * 4 Error: error conditions 运行时出现的错误，不需要立即进行修复
     * 5 Warning: warning conditions 可能影响系统功能，需要提醒的重要事件
     * 6 Notice: normal but significant condition 不影响正常功能，但需要注意的消息
     * 7 Informational: informational messages 系统正常运行情况下的一般信息
     * 8 Debug: debug-level messages 开发时对系统进行诊断的信息
     */
    private int lv;         //日志等级

    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String tag;     //日志标签

    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content; //日志内容

    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String host;    //日志所属主机名称

    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String file;    //产生该日志的文件路径

    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String note;    //日志备注（自定义扩展）

    private int line;       //日志产生于该文件的第几行
    private long time;      //日志产生时间

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }
}
