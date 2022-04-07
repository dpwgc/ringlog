package com.dpwgc.ringlog.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Udp服务配置
 */
@Configuration
public class UdpConfig implements InitializingBean {

    @Value("${udp.port}")
    private int udpPort;

    @Value("${udp.maxDataSize}")
    private int udpMaxDataSize;

    private static int port;
    private static int size;

    /**
     * spring boot项目启动后自动执行
     */
    @Override
    public void afterPropertiesSet() {
        //将配置文件中的信息加载到静态变量中
        port = udpPort;
        size = udpMaxDataSize;
        System.out.println("UDP port:"+port);
    }

    public static int getUdpMaxDataSize() {
        return size;
    }

    public static int getUdpPort() {
        return port;
    }
}
