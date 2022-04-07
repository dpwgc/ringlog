package com.dpwgc.ringlog.dao;

/**
 * 日志信息模板
 */
public class LogMsg {

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
    private String tag;     //日志标签
    private String content; //日志内容
    private String host;    //日志所属主机名称
    private String file;    //产生该日志的文件路径
    private int line;       //日志产生于该文件的第几行
    private long time;      //日志产生时间

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
}
