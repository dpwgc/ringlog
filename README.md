# RingLog
## 基于Spring Boot整合UDP、MongoDB及本地消息队列实现的日志收集系统。

***

### 启动项目
* 部署MongoDB/MongoDB集群，创建数据库。
* 配置application.properties文件，填写Mongodb配置及UDP服务配置。
* 启动项目（应用首次启动将在指定MongoDB数据库下自动创建user_info集合）。
* 启动后访问网页控制台: http://127.0.0.1:9000/#/ 。
***

### 日志收集方式
使用UDP接收外部日志。
#### UDP收集服务
* 先将本地日志转为如下格式的JSON字符串。
```json
{
  "lv":6,
  "tag":"test",
  "content":"redigo: nil returned",
  "host":"88.88.88.88:8888",
  "file":"/service/test.go",
  "line":15,
  "time":1648380678385,
  "note":"this is a test log"
}
```
* 再将该JSON字符串转为字节数组，使用UDP发送到RingLog服务端，服务端UDP监听端口默认为8999。

***

### 检索日志方式

#### HTTP接口查询

#### 接口URL
> http://127.0.0.1:9000/log/getLog

#### 请求方式
> POST

#### Content-Type
> form-data

#### 请求Header参数
参数名 | 示例值 | 参数类型 | 是否必填 | 参数描述
--- | --- | --- | --- | ---
user | admin | Text | 是 | user_info集合中的管理员账号
pwd | 123456 | Text | 是 | user_info集合中的管理员密码
#### 请求Body参数
参数名 | 示例值 | 参数类型 | 是否必填 | 参数描述
--- | --- | --- | --- | ---
start | 1648380678385 | Text | 是 | 时间区间（开始）
end | 1648380678386 | Text | 是 | 时间区间（结尾）
lv | 6 | Text | 否 | 日志级别
tag | test | Text | 否 | 日志标签
content | redis | Text | 否 | 日志内容
host | 88.88.88.88:8888 | Text | 否 | 日志所属主机
file | /service/test.go | Text | 否 | 日志所属文件
note | this is a test log | Text | 否 | 日志备注信息
#### 成功响应示例
```json
{
	"code": 200,
	"msg": "success",
	"data": [
		{
			"_id": {
				"time": 1649486669000,
				"date": "2022-04-09T06:44:29.000+00:00",
				"timestamp": 1649486669,
				"new": false,
				"machine": -1478292246,
				"inc": 725857564,
				"timeSecond": 1649486669
			},
			"lv": 6,
			"tag": "test",
			"content": "redigo: nil returned",
			"host": "88.88.88.88:8888",
			"file": "/service/test.go",
			"note": "this is a test log",
			"line": 15,
			"time": 1648380678385
		},
		{
			"_id": {
				"time": 1649486670000,
				"date": "2022-04-09T06:44:30.000+00:00",
				"timestamp": 1649486670,
				"new": false,
				"machine": -1478292246,
				"inc": 725857565,
				"timeSecond": 1649486670
			},
			"lv": 6,
			"tag": "test",
			"content": "redigo: nil returned",
			"host": "88.88.88.88:8888",
			"file": "/service/test.go",
			"note": "this is a test log",
			"line": 15,
			"time": 1648380678385
		}
	]
}
```
#### 失败响应示例
* 管理员用户身份验证失败（user与pwd不匹配）
```json
{
	"code": 440,
	"msg": "authentication error"
}
```
***

### Mongodb数据库
#### log_data集合（日志数据，用于存储所有日志信息）
* 存储文档格式
```json
{
  "_id": {
    "$oid": "62512b4da7e30cea2b43b51c"
  },
  "lv": 6,
  "tag": "test",
  "content": "redigo: nil returned",
  "host": "88.88.88.88:8888",
  "file": "/hello/service/test.go",
  "note": "this is a test log",
  "line": 15,
  "time": {
    "$numberLong": "1648380678385"
  }
}
```
#### user_info集合（管理员用户信息，用于检索日志时鉴权）
* 存储文档格式
```json
{
    "_id": {
        "$oid": "624ebc1f728f90ef47d09f45"
    },
    "user": "admin",
    "pwd": "e10adc3949ba59abbe56e057f20f883e"
}
```
pwd密码用MD5加密。
当user_info不存在或user_info内没有用户信息时，应用会自动创建用户，默认用户名admin，密码123456。

*** 

### 项目结构
* config `配置类`
   * InterceptorConfig.java `拦截器配置`
   * MongodbConfig.java `Mongodb连接配置`
   * UdpConfig.java `UDP监听配置`
* controller `控制器层`
   * LogController.java `日志查询接口`
* dao `模板层`
   * LogMsg.java `日志消息模板`
* interceptor `拦截器`
   * ApiInterceptor.java `HTTP接口拦截器`
* server `监听服务层`
   * MQServer.java `本地消息队列消费服务`
   * UdpServer.java `UDP监听消息服务`
* service `HTTP接口服务层`
   * LogService.java `日志查询服务`
* util ``
   * LogUtil.java `日志操作工具`
   * Md5Util.java `MD5密钥生成工具`
   * MongodbUtil.java `Mongodb操作工具`
   * ResultUtil.java `接口返回模板`
* RinglogApplication.java `启动类`