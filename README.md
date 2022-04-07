# RingLog
## 基于Spring Boot整合MongoDB实现的日志系统。

***

### 启动项目
* 配置application.properties文件，填写Mongodb配置及UDP服务端口。
* 启动项目。

***

### 日志收集
使用UDP接收外部日志。
#### UDP传输方式
* 先将本地日志转为如下格式的JSON字符串。
```json
{
  "lv":6,
  "tag":"test",
  "content":"redigo: nil returned",
  "host":"88.88.88.88:8888",
  "file":"/hello/service/test.go",
  "line":15,
  "time":1648380678385
}
```
* 再将该JSON字符串转为字节数组，使用UDP发送到RingLog服务端，服务端UDP监听端口默认为8999。

***