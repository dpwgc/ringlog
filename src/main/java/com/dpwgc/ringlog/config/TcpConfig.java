package com.dpwgc.ringlog.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * TCP服务配置
 */
@Configuration
public class TcpConfig implements InitializingBean {

    @Value("${tcp.port}")
    private int tcpPort;

    @Value("${tcp.maxDataSize}")
    private int tcpMaxDataSize;

    private static int port;
    private static int size;

    /**
     * spring boot项目启动后自动执行
     */
    @Override
    public void afterPropertiesSet() {
        //将配置文件中的信息加载到静态变量中
        port = tcpPort;
        size = tcpMaxDataSize;
        System.out.println("[Ring Log] TCP port:"+port);
        System.out.println("[Ring Log] TCP max data size:"+size);
    }

    public static int getTcpMaxDataSize() {
        return size;
    }

    public static int getTcpPort() {
        return port;
    }
}
