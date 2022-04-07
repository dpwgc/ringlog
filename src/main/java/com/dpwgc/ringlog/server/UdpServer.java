package com.dpwgc.ringlog.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dpwgc.ringlog.config.UdpConfig;
import com.dpwgc.ringlog.dao.LogMsg;
import com.dpwgc.ringlog.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


/**
 * UDP监听服务
 */
@Slf4j
@WebListener
public class UdpServer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //等待spring boot加载完后再运行UDP监听线程
                    while (true){
                        if(UdpConfig.getUdpPort() != 0) {
                            break;
                        }
                    }
                    System.out.println("UDP server run:"+ UdpConfig.getUdpPort());
                    executeUdpMsg(UdpConfig.getUdpPort());
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void executeUdpMsg(int port) throws SocketException {

        //创建服务器端DatagramSocket，指定端口
        DatagramSocket socket = new DatagramSocket(port);

        while (true) {
            byte[] buffer = new byte[UdpConfig.getUdpMaxDataSize()];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                //接收客户端发来的字节数组
                socket.receive(packet);
                buffer = packet.getData();

                //将字节数据转为String类型
                String bufString = new String(buffer, "GBK").trim();

                //将json字符串转换为LogMSG对象
                String jsonString = JSONObject.toJSONString(bufString);
                String s = JSON.parse(jsonString).toString();
                LogMsg logMsg = JSONObject.parseObject(s, LogMsg.class);

                //插入日志
                LogUtil.set(logMsg);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("UDP close");
    }
}




