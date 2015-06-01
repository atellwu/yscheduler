# YScheduler API 使用说明

YScheduler API通过**HTTP**方式提供服务。

## 权限验证

| Request参数    | 作用             | 备注                        |
|:------------: |:----------------:| :-------------------------:|
| appKey    | 用于标识一个app    | 每次请求必须包含              |
| token     | 用于认证一个app    | 每次请求必须包含              |
| userName  | 用于标识一个操作用户| 当需要调用修改数据的API时必须包含|
| userToken | 用于认证一个操作用户| 当需要调用修改数据的API时必须包含|

## 数据返回格式

数据返回统一返回**JSON**

**例子:**
```
操作成功:  

{"returnValue":{"exists":true},"status":0,"success":true}
```

```
操作失败:  

{"message":"Task name must not be null or empty","returnValue":{},"status":400,"success":false}
```
  
  
**具体说明：**

| Key          | Value含义             | 备注                |
|:-----------: |:--------------------:|:-------------------:|
| status       | 状态码                |状态码请参看下一个表格   |
| success      | 是否成功              |                      |
| message      | 错误信息              |                     |
| returnValue  | 返回数据，类型为JSON Map|不同api会返回不同的数据 |

**状态码：**

| Code         | 含义                          |
|:-----------: |:-----------------------------:|
| 0            | 成功                           |
| 300          | 权限校验失败                    |
| 400          | 业务错误(包括非法操作，参数不合法等)|
| 500          | 未知异常                       |

* **例子**

```
appKey没有传递:  

请求：http://localhost:8080/api/task/name_exists?token=2&taskName=test2
返回：{"message":"appKey must not be null or empty","returnValue":{},"status":300,"success":false}
```

```
token没有传递:  

请求：http://localhost:8080/api/task/name_exists?appKey=1&taskName=test2
返回：{"message":"token must not be null or empty","returnValue":{},"status":300,"success":false}
```

## API详细说明
### create
* * *
* **功能**  
  
  创建一个Task

* **请求方式**  
  
  **POST**  
  **Content-Type: application/x-www-form-urlencoded**

* **请求参数**

| Request参数    | 作用             | 是否必须                        |
|:------------: |:----------------:| :-------------------------:|
| appKey    | 用于标识一个app         | 是              |
| token     | 用于认证一个app         | 是              |
| userName  | 用于标识一个操作用户| 是|
| userToken | 用于认证一个操作用户| 是|
| taskName  | 需要判断是否存在的任务名称| 是               |
| type      | Task类型(1：Shell任务；20:HTTP任务)| 是               |
| crontab   | 定时调度表达式| 是               |
| command   | 任务执行的命令| Shell任务：是；HTTP任务不用填              |
| agent   | 任务执行的agent名称| Shell任务：是；HTTP任务不用填              |
| timeout   | 超时时间(单位：分钟)| 否(必须大于0；否则默认值为0)             |
| retryTimes   | 失败后重试次数| 否(必须大于0；否则默认值为0)             |
| description   | 任务描述| 否           |
| calloutUrl      | HTTP任务的触发URL| Shell任务：任务不用填；HTTP：是              |
| cancelUrl      | HTTP任务的取消运行URL| Shell任务：任务不用填；HTTP：否  |
| needCallback      | 是否需要回调| Shell任务：任务不用填；HTTP：是(必须是"true"或者"false")  |

* **返回数据**

| Key          | 内容             |
|:-----------: |:--------------------:|
| status       | 0表示调用成功，否则表示调用失败(参看之前关于状态码的说明)|
| success      | 是否成功              |
| message      | 错误信息              |
| returnValue  | 创建成功则返回任务ID{id:{id}}|

* **请求例子(只列举部分)**

```
创建Shell任务成功:  

请求：    http://localhost:8080/api/task/create
POST数据：  
        appKey: 1
        token: 2
        taskName: testCreateShell
        type: 1
        crontab: 0 0 0 * * ?
        command: echo 1
        agent: platform-agent1
        timeout: 100
        retryTimes: 2
        description: test
        userName: leo.liang
        userToken: 12321
返回：    {"returnValue":{"id":171},"status":0,"success":true}
```

```
创建HTTP任务成功:  

请求：    http://localhost:8080/api/task/create
POST数据：  
        appKey: 1
        token: 2
        taskName: testCreateHTTP
        type: 20
        crontab: 0 0 0 * * ?
        timeout: 100
        retryTimes: 2
        description: test
        userName: leo.liang
        userToken: 12321
        calloutUrl: http://www.test.com/callout
        cancelUrl: http://www.test.com/cancel
        needCallback: true
返回：    {"returnValue":{"id":172},"status":0,"success":true}
```

```
command没有传递:  

请求：http://localhost:8080/api/task/create
POST数据：  
        appKey: 1
        token: 2
        taskName: testCreateShell
        type: 1
        crontab: 0 0 0 * * ?
        agent: platform-agent1
        timeout: 100
        retryTimes: 2
        description: test
        userName: leo.liang
        userToken: 12321
返回：{"message":"Command must not be null or empty","returnValue":{},"status":400,"success":false}
```
* * *
### name_exists
* * *
* **功能**  
  
  判断一个Task是否已经存在

* **请求方式**  
  
  GET

* **请求参数**

| Request参数    | 作用             | 是否必须                        |
|:------------: |:----------------:| :-------------------------:|
| appKey    | 用于标识一个app         | 是              |
| token     | 用于认证一个app         | 是              |
| taskName  | 需要判断是否存在的任务名称| 是               |

* **返回数据**

| Key          | 内容             |
|:-----------: |:--------------------:|
| status       | 0表示调用成功，否则表示调用失败(参看之前关于状态码的说明)|
| success      | 是否成功              |
| message      | 错误信息              |
| returnValue  | 存在则返回{exists:true}，否则返回{exists:false}|

* **请求例子(只列举部分)**

```
任务存在:  

请求：http://localhost:8080/api/task/name_exists?appKey=1&token=2&taskName=demo-sleepEcho
返回：{"returnValue":{"exists":true},"status":0,"success":true}
```

```
任务不存在:  

请求：http://localhost:8080/api/task/name_exists?appKey=1&token=2&taskName=demo-sleepEcho222
返回：{"returnValue":{"exists":false},"status":0,"success":true}
```

```
taskName没有传递:  

请求：http://localhost:8080/api/task/name_exists?appKey=1&token=2
返回：{"message":"Task name must not be null or empty","returnValue":{},"status":400,"success":false}
```
* * *
### disable_schedule
* * *
* **功能**  
  
  暂停一个任务的定时调度


* **请求方式**  
  
  **POST**  
  **Content-Type: application/x-www-form-urlencoded**

* **请求参数**

| Request参数    | 作用             | 是否必须                        |
|:------------: |:----------------:| :-------------------------:|
| appKey    | 用于标识一个app         | 是              |
| token     | 用于认证一个app         | 是              |
| userName  | 用于标识一个操作用户| 是|
| userToken | 用于认证一个操作用户| 是|
| taskName  | 需要判断是否存在的任务名称| 是               |

* **返回数据**

| Key          | 内容             |
|:-----------: |:--------------------:|
| status       | 0表示调用成功，否则表示调用失败(参看之前关于状态码的说明)|
| success      | 是否成功              |
| message      | 错误信息              |
| returnValue  | 永远为空|

* **请求例子(只列举部分)**

```
成功:  

请求：    http://localhost:8080/api/task/disable_schedule
POST数据：  
        appKey: 1
        token: 2
        taskName: demo-sleepEcho
        userName: leo.liang
        userToken: 12321
返回：    {"returnValue":{},"status":0,"success":true}
```

```
userName没有传递:  

请求：    http://localhost:8080/api/task/disable_schedule
POST数据：  
        appKey: 1
        token: 2
        taskName: demo-sleepEcho
        userToken: 12321
返回：    {"message":"User name must not be null or empty","returnValue":{},"status":400,"success":false}
```

```
user不存在:  

请求：    http://localhost:8080/api/task/disable_schedule
POST数据：  
        appKey: 1
        token: 2
        taskName: demo-sleepEcho
        userName: leo.liang
        userToken: 12321
返回：    {"message":"User(leo.liang) does not exist","returnValue":{},"status":400,"success":false}
```
* * *
### enable_schedule
* * *
* **功能**  
  
  开启一个任务的定时调度


* **请求方式**  
  
  **POST**  
  **Content-Type: application/x-www-form-urlencoded**

* **请求参数**

| Request参数    | 作用             | 是否必须                        |
|:------------: |:----------------:| :-------------------------:|
| appKey    | 用于标识一个app         | 是              |
| token     | 用于认证一个app         | 是              |
| userName  | 用于标识一个操作用户| 是|
| userToken | 用于认证一个操作用户| 是|
| taskName  | 需要判断是否存在的任务名称| 是               |

* **返回数据**

| Key          | 内容             |
|:-----------: |:--------------------:|
| status       | 0表示调用成功，否则表示调用失败(参看之前关于状态码的说明)|
| success      | 是否成功              |
| message      | 错误信息              |
| returnValue  | 永远为空|

* **请求例子(只列举部分)**

```
成功:  

请求：    http://localhost:8080/api/task/enable_schedule
POST数据：
        appKey: 1
        token: 2
        taskName: demo-sleepEcho
        userName: leo.liang
        userToken: 12321
返回：    {"returnValue":{},"status":0,"success":true}
```

```
userName没有传递:  

请求：    http://localhost:8080/api/task/enable_schedule
POST数据：
        appKey: 1
        token: 2
        taskName: demo-sleepEcho
        userToken: 12321
返回：    {"message":"User name must not be null or empty","returnValue":{},"status":400,"success":false}
```

```
user不存在:  

请求：    http://localhost:8080/api/task/enable_schedule
POST数据：
        appKey: 1
        token: 2
        taskName: demo-sleepEcho
        userName: leo.liang
        userToken: 12321
返回：    {"message":"User(leo.liang) does not exist","returnValue":{},"status":400,"success":false}
```

    
