package com.dpwgc.ringlog.server;

import com.dpwgc.ringlog.config.UdpConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


/**
 * UDP监听服务
 */
@WebListener
public class UdpServer implements ServletContextListener {

    //UDP socket
    public static DatagramSocket socket;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //等待spring boot加载完后再运行UDP监听线程（避免配置文件中的参数来不及加载进内存）
                    while (true){
                        if(UdpConfig.getUdpPort() != 0) {
                            break;
                        }
                    }
                    System.out.println("[Ring Log] UDP server run:"+ UdpConfig.getUdpPort());
                    listenUdpMsg(UdpConfig.getUdpPort());
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void listenUdpMsg(int port) throws SocketException {

        //创建服务器端DatagramSocket，指定端口
        socket = new DatagramSocket(port);

        //持续监听UDP消息
        while (true) {
            byte[] buffer = new byte[UdpConfig.getUdpMaxDataSize()];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                //接收客户端发来的字节数组
                socket.receive(packet);
                buffer = packet.getData();

                //将buffer数组插入本地mq
                MQServer.mq.add(buffer);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("UDP close");
    }
}




