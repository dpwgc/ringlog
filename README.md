# RingLog
## 基于Spring Boot整合MongoDB实现的日志系统。

***

### 启动项目
* 配置application.properties文件，填写Mongodb配置及UDP服务端口。
* 启动项目。

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
end | 1648380678386 | Text | 否 | 时间区间（结尾）
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