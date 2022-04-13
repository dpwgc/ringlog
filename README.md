# RingLog
## 基于Spring Boot整合Elasticsearch、Kafka及本地缓冲队列实现的日志收集系统。
***

### 实现原理
* 使用Elasticsearch存储日志数据。
* 使用UDP/TCP监听接收外来日志信息。
* 使用ConcurrentLinkedQueue实现本地缓冲队列，所有外来日志信息先插入本地缓冲队列，再由后台线程（一个或者多个消费者线程）将日志信息异步发送至外部消息队列Kafka。
* 由Kafka消费者将日志信息批量插入Elasticsearch。

![avatar](./img1.jpg)
***

### 启动项目
* 部署Elasticsearch/Elasticsearch集群，创建索引：ring_log。
* 部署Kafka/Kafka集群，创建Topic：ring_log。
* 填写resources/application.properties配置文件，填写Elasticsearch配置、Kafka配置、UDP/TCP监听服务配置。
* 启动项目，访问 http://127.0.0.1:9000/ ，返回"hello"表明项目启动成功"。
* 使用Kibana连接Elasticsearch并检索日志。

![avatar](./img2.jpg)
***

### 日志收集方式
可使用UDP或TCP监听接收外部日志。
#### UDP/TCP客户端发送日志信息
* 先将日志信息转为如下格式的JSON字符串。
```json
{
  "lv":6,
  "tag":"test-0.0.1",
  "content":"redigo: nil returned",
  "host":"88.88.88.88:8888",
  "file":"/service/test.go",
  "line":15,
  "time":1648380678385,
  "note":"this is a test log"
}
```
```json
"{\n  \"lv\":6,\n  \"tag\":\"test-0.0.1\",\n  \"content\":\"redigo: nil returned\",\n  \"host\":\"88.88.88.88:8888\",\n  \"file\":\"/service/test.go\",\n  \"line\":15,\n  \"time\":1648380678385,\n  \"note\":\"this is a test log\"}"
```
* 再将该JSON字符串转为字节数组，使用UDP/TCP发送到RingLog服务端，服务端UDP监听端口默认为8999，TCP监听端口默认为8998。
#### 客户端代码示例
* Go语言
```
 // 使用UDP发送日志信息
 socket, err := net.DialUDP("udp", nil, &net.UDPAddr{
	IP:   net.IPv4(127, 0, 0, 1),
	Port: 8999,
 })
 socket.Write([]byte("{\n  \"lv\":6,\n  \"tag\":\"test-0.0.1\",\n  \"content\":\"redigo: nil returned\",\n  \"host\":\"88.88.88.88:8888\",\n  \"file\":\"/hello/service/test.go\",\n  \"line\":15,\n  \"time\":1648380678385,\n  \"note\":\"this is a test log\"\n}")
```
#### 日志字段说明
* lv 日志等级（整数表示，范围：1~8）
* tag 日志标签（可以是所属项目名+项目版本号）
* content 日志内容
* host 日志所属的主机（可以是主机名称或主机地址+应用程序端口号）
* file 产生该日志的文件路径
* line 日志产生于该文件的第几行
* time 日志产生时间（毫秒级时间戳，13位，long整数）
* note 日志备注（可以是对该日志的简短描述，或是额外的标注补充）
#### 日志等级说明
日志等级 lv（int类型）
* 1 Emergency: system is unusable 导致系统不可用的事故
* 2 Alert: action must be taken immediately 必须立即处理的问题
* 3 Critical: critical conditions 需要立即修复的紧急情况
* 4 Error: error conditions 运行时出现的错误，不需要立即进行修复
* 5 Warning: warning conditions 可能影响系统功能，需要提醒的重要事件
* 6 Notice: normal but significant condition 不影响正常功能，但需要注意的消息
* 7 Informational: informational messages 系统正常运行情况下的一般信息
* 8 Debug: debug-level messages 开发时对系统进行诊断的信息

***

### Elasticsearch数据存储
#### ring_log索引（用于存储所有日志信息）
* 存储文档格式
```json
{
  "_index": "ring_log",
  "_type": "_doc",
  "_id": "X4UUIoAB2Qo5g_1D2_nM",
  "_version": 1,
  "_score": 0,
  "fields": {
    "note": [
      "this is a test log"
    ],
    "file": [
      "/service/test.go"
    ],
    "line": [
      "15"
    ],
    "host": [
      "88.88.88.88:8888"
    ],
    "lv": [
      "6"
    ],
    "time": [
      "1648380678385"
    ],
    "tag": [
      "test-0.0.1"
    ],
    "_class": [
      "com.dpwgc.ringlog.dao.LogMsg"
    ],
    "_class.keyword": [
      "com.dpwgc.ringlog.dao.LogMsg"
    ],
    "content": [
      "redigo: nil returned"
    ]
  }
}
```

*** 

### 项目结构
* config `配置类`
   * EsConfig.java `Elasticsearch连接配置`
   * TcpConfig.java `TCP监听配置`
   * UdpConfig.java `UDP监听配置`
* controller `控制器层`
   * HelloController.java `web访问接口`
* dao `模板层`
   * LogMsg.java `日志消息模板`
* interceptor `拦截器`
   * ApiInterceptor.java `HTTP接口拦截器`
* mapper `接口层`
   * EsMapper.java `Elasticsearch操作接口`
* server `监听服务层`
   * KafkaServer.java `Kafka消息队列消费者服务`
   * MqServer.java `本地缓冲队列消费者服务`
   * TcpServer.java `TCP监听消息服务`
   * UdpServer.java `UDP监听消息服务`
* util ``
   * LogUtil.java `日志操作工具`
   * EsUtil.java `Elasticsearch操作工具`
* RinglogApplication.java `启动类`