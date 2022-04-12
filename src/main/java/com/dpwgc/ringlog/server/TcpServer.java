package com.dpwgc.ringlog.server;

import com.dpwgc.ringlog.config.TcpConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * TCP监听服务
 */
@WebListener
public class TcpServer implements ServletContextListener {

    /**
     * 启动TCP监听线程
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        new Thread(() -> {
            try {
                //等待spring boot加载完后再运行UDP监听线程（避免配置文件中的参数来不及加载进内存）
                while (true){
                    if(TcpConfig.getTcpPort() != 0) {
                        break;
                    }
                }

                //如果TCP监听端口设为-1，则表示不开启TCP监听
                if(TcpConfig.getTcpPort() == -1) {
                    return;
                }

                //开启TCP监听
                System.out.println("[Ring Log] TCP server run:"+ TcpConfig.getTcpPort());
                listenTcpMsg(TcpConfig.getTcpPort());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 监听TCP消息
     */
    private void listenTcpMsg(int port) throws IOException {

        ServerSocket server = new ServerSocket(port);
        while (true) {
            //server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的
            Socket socket = server.accept();

            //每接收到一个Socket就创建一个线程来处理它
            new Thread(new Task(socket)).start();
        }
    }

    /**
     * 用来处理Socket请求的
     */
    static class Task implements Runnable {

        private Socket socket;

        public Task(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                handleSocket();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 跟客户端Socket进行通信
         * @throws Exception
         */
        private void handleSocket() throws Exception {

            //接收TCP客户端发来的日志信息
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            char[] buffer = new char[TcpConfig.getTcpMaxDataSize()];
            br.read(buffer);

            //将char[]转string
            StringBuilder bufString = new StringBuilder("");
            for(int i=0;i<TcpConfig.getTcpMaxDataSize();i++){
                //如果读到空字符，则结束读取
                if (buffer[i] == '\u0000') {
                    break;
                }
                bufString.append(buffer[i]);
            }

            //将日志信息转为字节数组插入本地mq
            MqServer.mq.add(bufString.toString().getBytes());

            //回复客户端
            Writer writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
            writer.write("ok");
            writer.flush();

            //关闭连接
            writer.close();
            br.close();
            socket.close();
        }
    }
}
