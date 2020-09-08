## API 接口说明

- 接口基准地址：`http://127.0.0.1:8888/`
- 服务端已开启 CORS 跨域支持
- 使用 HTTP Status Code 标识状态
- API 统一使用 Token 认证（除了登陆、注册请求）
- 需要授权的 API ，必须在请求头中使用 `Authorization` 字段提供 `token` 令牌
- 数据返回格式统一使用 JSON

### 支持的请求方法

- GET（SELECT）：从服务器取出资源（一项或多项）。
- POST（CREATE）：在服务器新建一个资源。
- PUT（UPDATE）：在服务器更新资源（客户端提供改变后的完整资源）。
- PATCH（UPDATE）：在服务器更新资源（客户端提供改变的属性）。
- DELETE（DELETE）：从服务器删除资源。

### 通用返回状态说明

| *状态码* | *含义*                | *说明*                                              |
| -------- | --------------------- | --------------------------------------------------- |
| 200      | OK                    | 请求成功                                            |
| 201      | CREATED               | 创建成功                                            |
| 204      | DELETED               | 删除成功                                            |
| 400      | BAD REQUEST           | 请求的地址不存在或者包含不支持的参数                |
| 401      | UNAUTHORIZED          | 未授权                                              |
| 403      | FORBIDDEN             | 被禁止访问                                          |
| 404      | NOT FOUND             | 请求的资源不存在                                    |
| 422      | Unprocesable entity   | [POST/PUT/PATCH] 当创建一个对象时，发生一个验证错误 |
| 500      | INTERNAL SERVER ERROR | 内部错误                                            |
|          |                       |                                                     |

------

### 检查登陆状态
浏览器和服务器之间时通过 session 来确定连接状态的，浏览器第一次请求时服务端会自动生成一个 session，并将这个 sessionId 传回给浏览器，浏览器将这个 sessionId 存放在 cookie 中，下一次浏览器访问服务器时就会将这个 sessionId 以 cookie 的形式传递到服务器，服务器接送到这个 sessionId 后就可以判断发送这个请求的浏览器之前是否访问过。

　　在进行登录认证逻辑时，通常会在登录认证成功后将用户信息保存到 session 中；整个系统会对出登录和登出操作之外的请求进行拦截，在拦截器中会判断 session 中是否有用户的数据，如果有就跳转到 controller 层执行对应的请求，如果没有就直接返回一个提示信息。（PS: 每个客户端第一次访问服务器时服务端都会自动创建一个 session）

![image.png](https://i.loli.net/2020/09/03/QBVtMLDSywhc1sk.png)

前端登陆后，后端在 session 作用域里添加一个标识符（学号 or 工号），除登陆和注册请求之外的请求都需要先判断是否有这个标识符

### sessionStorage
存储在 sessionStorage 里面的数据在页面会话结束时会被清除。
也就是说：新开一个页面就是一个新的 sessionStorage

如果关闭浏览器或该页面，sessionStorage 的数据就会消失
```js
// 保存数据到 sessionStorage
sessionStorage.setItem('key', 'value');

// 从 sessionStorage 获取数据
let data = sessionStorage.getItem('key');

// 从 sessionStorage 删除保存的数据
sessionStorage.removeItem('key');

// 从 sessionStorage 删除所有保存的数据
sessionStorage.clear();
```

### 返回的数据格式
```json
{
    "code": 200,
    "message": "登录成功",
    "data": {
        ...
    }
}
```


## 学生页面

### 登陆

- 请求路径：student/login
- 请求方法：post
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| studentId | 学生 id   | 不能为空 |
| password | 密码     | 不能为空 |

- 响应数据

```json
{
    "code": 200,
    "message": "登陆成功",
    "data": {
        "studentId": "20182507012912",
        "gender": "男",
        "phone": "13128863338",
        "name": "张三",
        "status": "登录成功",
        "token": "ecf2ec006bf4babae0b460f524437389"
    }
}
```

前端拿到数据之后存在 sessionStorage，以后之前读取这里面的东西就好了（注意 Token）

### 请求验证码
- 请求路径：student/pb/code
- get
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| uuid | 验证码的key   | 不能为空 |


- 响应数据

![image.png](https://i.loli.net/2020/09/07/xOip5YLZB4fXs2d.png)

### 注册

- 请求路径：student/sign-up
- 请求方法：post
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| uuid | 验证码的key   | 不能为空 |
| image_code | 验证码的值   | 不能为空 |
| studentId | 学生 id   | 不能为空 |
| name | 名字   | 不能为空 |
| password | 密码     | 不能为空 |
| phone | 手机号     | 不能为空 |
| gender | 性别     | 不能为空 |


- 响应数据

```json
{
    "code": 200,
    "message": "登陆成功",
    "data": {
        "studentId": "20182507012912",
        "gender": "男",
        "phone": "13128863338",
        "name": "张三",
        "status": "登录成功",
        "token": "ecf2ec006bf4babae0b460f524437389"
    }
}
```

后端注册成功后需要刷新 redis 缓存的数据

### 检查是否存在
- 请求路径：student/is-exist
- 请求方法：patch
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| studentId | 学生 id   | 不能为空 |

- 响应数据

```json
{
    "code": 200,
    "message": "当前学生不存在",
    "data": false
}
```


### 个人中心

- 请求路径：student/user
- 请求方法：patch
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| studentId | 学生 id   | 不能为空 |
| phone | 手机号     | 不能为空 |
| gender | 性别     | 不能为空 |


- 响应数据

```json
{
    "code": 200,
    "message": "修改成功",
    "data": {
        "status": "修改成功"
    }
}
```


### 发起订单

- 请求路径：student/order
- 请求方法：post
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| studentId | 学生 id   | 不能为空，前端页面的这个报修人的名字不能改，默认就是这个账号用户的名字，所以直接就对应这个他的学号 |
| faultClass | 报错类型     | 不能为空 |
| address | 地址     | 不能为空 |
| contacter | 联系人的名称     | 不能为空，这个随便取都无所谓 |
| studentPhone | 联系人的电话     | 不能为空 |
| faultDetails | 说明     | 可以为空 |

- 响应数据

```json
{
    "code": 201,
    "message": "订单发起成功",
    "data": {
        "status": "订单发起成功"
    }
}
```


### 当前订单


- 请求路径：student/order
- 请求方法：get
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| studentId | 学生 id     | 不能为空，根据当前学生查询对应的订单，如果没有则返回空 |


- 响应数据

```json
{
  "code": 200,
  "message": "当前订单",
  "data": {
      "fixTableId": 15,
      "faultClass": "网络问题",
      "address": "15 栋 611",
      "workId": "201825070120",
      "workerName": "张师傅",
      "studentName": "李四",
      "contacter": "王五",
      "faultDetail": "宿舍的电脑突然上不了网了.....",
      "workerPhone": "13128866666",
      "createTime": "2020-08-09T20:36:26.000Z"
  }
}
// or 当还没有指定师傅的情况这个字段是空的
{
  "code": 200,
  "message": "当前订单",
  "data": {
      "fixTableId": 15,
      "faultClass": "网络问题",
      "address": "15 栋 611",
      "workId": null,
      "workerName": null,
      "studentName": "李四",
      "contacter": "王五",
      "faultDetail": "宿舍的电脑突然上不了网了.....",
      "workerPhone": "13128866666",
      "createTime": "2020-08-09T20:36:26.000Z"
  }
}
```

### 处理结果评价


- 请求路径：student/order-end
- 请求方法：patch
- 请求参数（当工人处理完成之后，前端检查到这个学生最后一个订单如果学生评价为空就弹出 处理结果评价页面）

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| fixTableId | 订单 id     | 不能为空，根据订单的 id 来查询 |
| studentId | 学号     | 不能为空，判断当前订单是否是这个学生评价的|
| massage | 学生留言     | 不能为空|
| grade | 学生打分     | 不能为空|

- 响应数据

```json
{
    "code": 200,
    "message": "处理结果评价成功",
    "data": {
        "status": "处理结果评价成功"
    }
}
```


### 历史订单

- 请求路径：student/order-list
- 请求方法：get
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| studentId | 学生 id     | 不能为空，根据当前学生查询对应的订单，如果没有则返回空 |


- 响应数据

```json
{
    "code": 200,
    "message": "历史订单",
    "data": [
        {
            "fixTableId": 15,
            "faultClass": "网络问题",
            "endTime": "2020-08-09T20:36:26.000Z",
            "grade": 4.2
        },
        {
            "fixTableId": 16,
            "faultClass": "水管问题",
            "endTime": "2020-08-09T20:36:26.000Z",
            "grade": 4.2
        }
    ]
}
```
注意这个订单 id，后面点进每个历史订单查询数据就是通过这个 id 来查的

### 历史订单详情

- 请求路径：student/order-pass
- 请求方法：get
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| fixTableId | 订单 id     | 不能为空，根据订单的 id 来查询 |


- 响应数据

```json
{
    "code": 200,
    "message": "历史订单详情",
    "data": {
        "fix_table_id": 15,
        "fault_class": "网络问题",
        "address": "15 栋 611",
        "workId": "201825070120",
        "worker_name": "张师傅",
        "student_name": "李四",
        "contacter": "王五",
        "fault_detail": "宿舍的电脑突然上不了网了.....",
        "worker_phone": "13128866666",
        "create_time": "2020-08-09T20:36:26.000Z",
        "end_time": "2020-08-09T20:36:26.000Z",
        "grade": 4.2
    }
}
```

## 维修工人页面

### 登陆

- 请求路径：worker/login
- 请求方法：post
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| workId | 工号   | 不能为空 |
| password | 密码     | 不能为空 |

- 响应数据

```json
{
    "code": 200,
    "message": "维修工人登陆页面",
    "data": {
        "workId": "201825070120",
        "name": "张三",
        "gender": "男",
        "JOIN_DATE": "2020-08-09T20:36:26.000Z",
        "phone": "13128866666",
        "details": "工人的详细介绍信息",
        "ordersNumber": 21,
        "avgGrade": 4.2
    }
}
```

前端拿到数据之后存在 sessionStorage，以后之前读取这里面的东西就好了

### 主页

- 请求路径：worker/home
- 请求方法：get
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| workId | 工号   | 不能为空 |

- 响应数据

```json
{
    "code": 200,
    "message": "维修工人主页",
    "data": {
        "ordersNumber": 15,
        "ordersNumberToday": 1
    }
}
```


### 当前订单


- 请求路径：worker/order
- 请求方法：get
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| workId | 工号     | 不能为空，根据当前工人查询对应的订单，如果没有则返回空 |


- 响应数据

```json
{
    "code": 200,
    "message": "当前订单",
    "data": {
        "fixTableId": 15,
        "faultClass": "网络问题",
        "address": "15 栋 611",
        "workId": "201825070120",
        "workerName": "张师傅",
        "studentName": "李四",
        "contacter": "王五",
        "faultDetail": "宿舍的电脑突然上不了网了.....",
        "workerPhone": "13128866666",
        "createTime": "2020-08-09T20:36:26.000Z"
    }
}
```


### 历史订单

- 请求路径：worker/order-list
- 请求方法：get
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| workId | 工号     | 不能为空，根据当前工人查询对应的订单，如果没有则返回空 |


- 响应数据

```json
{
    "code": 200,
    "message": "历史订单",
    "data": [
        {
            "fixTableId": 15,
            "faultClass": "网络问题",
            "endTime": "2020-08-09T20:36:26.000Z",
            "grade": 4.2
        },
        {
            "fixTableId": 16,
            "faultClass": "网络问题",
            "endTime": "2020-08-09T20:36:26.000Z",
            "grade": 4.2
        }
    ]
}
```
注意这个订单 id，后面点进每个历史订单查询数据就是通过这个 id 来查的


### 历史订单详情

- 请求路径：worker/order-pass
- 请求方法：get
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| fixTableId | 订单 id     | 不能为空，根据订单的 id 来查询 |


- 响应数据

```json
{
    "code": 200,
    "message": "历史订单详情",
    "data": {
        "fixTableId": 15,
        "faultClass": "网络问题",
        "address": "15 栋 611",
        "workId": "201825070120",
        "workerName": "张师傅",
        "studentName": "李四",
        "contacter": "王五",
        "faultDetail": "宿舍的电脑突然上不了网了.....",
        "workerPhone": "13128866666",
        "createTime": "2020-08-09T20:36:26.000Z",
        "endTime": "2020-08-09T20:36:26.000Z",
        "grade": 4.2
    }
}
```

### 处理结果


- 请求路径：worker/order-end
- 请求方法：patch
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| fixTableId | 订单 id     | 不能为空，根据订单的 id 来查询 |
| workId | 工号     | 不能为空，判断当前订单是否是这个师傅来处理的|
| resultDetails | 处理结果     | 不能为空|

- 响应数据

```json
// 注意：后端收到这个请求后要把工人的状态改成 0 （0 空闲、1 工作中）

{
    "code": 200,
    "message": "处理结果评价成功",
    "data": {
        "status": "处理结果评价成功"
    }
}
```


### 消息中心

- 请求路径：worker/massage-list
- 请求方法：get

- 响应数据

```json
{
    "code": 200,
    "message": "消息中心",
    "data": [
        {
            "massageId": 15,
            "name": "发布者名字",
            "createDate": "2020-08-09T20:36:26.000Z",
            "massage": "消息中心"
        },
        {
            "massageId": 16,
            "name": "发布者名字",
            "createDate": "2020-08-09T20:36:26.000Z",
            "massage": "消息中心"
        }
    ]
}
```
注意这个消息 id，后面点进每个消息查询数据就是通过这个 id 来查的

## 管理员页面

### 登陆

- 请求路径：admin/login
- 请求方法：post
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| workId | 工号   | 不能为空 |
| password | 密码     | 不能为空 |

- 响应数据

```json
// 失败
{
  "code": 404,
  "message": "登陆失败",
  "data": {
      "status": "登录失败"
  }
}

// 成功
{
  "code": 200,
  "message": "登陆成功",
  "data": {
      "joinDate": "2020-08-01T05:00:00.000+00:00",
      "gender": "男",
      "phone": "13128834248",
      "name": "李四",
      "details": "俺就是个小透明",
      "workId": "201835070322",
      "status": "登录成功",
      "token": "df2fd4702b4b28070a43f4f1428d1797"
      }
}
```


### 任务列

- 请求路径：admin/order-list
- 请求方法：get

- 响应数据（多了一个 state 来标识订单状态，0 1 2 对应：未处理、待完成、完成）

```json
// 注意，这里返回的字段的区别
{
    "code": 200,
    "message": "订单 list",
    "data": [
        {
            "fixTableId": 17,
            "studentId": "201825070131",
            "contacts": "阿巴阿巴",
            "address": "15 栋 611",
            "createdTime": 1599180398000,
            "endTime": null,
            "phone": "13128888888",
            "faultClass": "中央空调",
            "faultDetail": "空调坏了",
            "workId": null,
            "adminWorkId": null,
            "resultDetails": null,
            "grade": 10,
            "massage": null,
            "state": 0
        },
        {
            "fixTableId": 21,
            "studentId": "201825070133",
            "contacts": "张三",
            "address": "15 栋 611",
            "createdTime": 1596501998000,
            "endTime": null,
            "phone": "13128888888",
            "faultClass": "中央空调",
            "faultDetail": "空调坏了",
            "workId": "201895070111",
            "adminWorkId": "201835070111",
            "resultDetails": null,
            "grade": 10,
            "massage": null,
            "state": 1
        },
        {
            "fixTableId": 28,
            "studentId": "201825070134",
            "contacts": "二狗",
            "address": "15 栋 611",
            "createdTime": 1597538798000,
            "endTime": 1599439603000,
            "phone": "13100038888",
            "faultClass": "网络问题",
            "faultDetail": "给的黄金卡根据考生打火机是德国",
            "workId": "201895070999",
            "adminWorkId": "201835070999",
            "resultDetails": "hsdhahsahdhasdhahsdahdhasdh",
            "grade": 9,
            "massage": "jyhiuyiuyiuyiuyiuyi",
            "state": 2
        }
    ]
}
```
注意这个订单 id，后面点进每个历史订单查询数据就是通过这个 id 来查的

### 订单详情

- 请求路径：admin/order
- 请求方法：get
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| fixTableId | 订单 id     | 不能为空，根据订单的 id 来查询 |


- 响应数据

```json
// 有三种状态的数据，所以对应也有三种响应的类型
// 0 1 2 对应：未处理、待完成、完成

// 注意看 不同状态返回的字段有些是空的

{
  "code": 200,
  "message": "订单 list",
  "data": {
      "fixTableId": 17,
      "studentId": "201825070131",
      "contacts": "阿巴阿巴",
      "address": "15 栋 611",
      "createdTime": "2020-09-04T00:46:38.000+00:00",
      "endTime": null,
      "phone": "13128888888",
      "faultClass": "中央空调",
      "faultDetail": "空调坏了",
      "workId": null,
      "adminWorkId": null,
      "resultDetails": null,
      "grade": 10,
      "massage": null,
      "state": 0
    }
}
// or  
{
  "code": 200,
  "message": "订单 list",
  "data": {
        "fixTableId": 21,
        "studentId": "201825070133",
        "contacts": "张三",
        "address": "15 栋 611",
        "createdTime": "2020-08-04T00:46:38.000+00:00",
        "endTime": null,
        "phone": "13128888888",
        "faultClass": "中央空调",
        "faultDetail": "空调坏了",
        "workId": "201895070111",
        "adminWorkId": "201835070111",
        "resultDetails": null,
        "grade": 10,
        "massage": null,
        "state": 1
    }
}

// or    

{
  "code": 200,
  "message": "订单 list",
  "data": {
      "fixTableId": 27,
      "studentId": "201825070132",
      "contacts": "王五",
      "address": "15 栋 614",
      "createdTime": "2020-08-12T00:46:38.000+00:00",
      "endTime": "2020-09-16T00:46:38.000+00:00",
      "phone": "13123338855",
      "faultClass": "中央空调",
      "faultDetail": "飒飒大苏打盛大的是",
      "workId": "201895070344",
      "adminWorkId": "201835070344",
      "resultDetails": "dahjksgdhjiagsdhjgahjsdghjasdgj",
      "grade": 10,
      "massage": "dhjagsjdhgajshdgad",
      "state": 2
    }
}        
```

### 取消订单

- 请求路径：admin/order
- 请求方法：delete
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| fixTableId | 订单号     | 非空 |

- 响应数据

```json
// 因为有两种情况
// 1. 没有人处理这个订单时，就把这个订单删除
// 2. 当已经有师傅在跟进时，先把工人的状态改成 1 （0 空闲、1 工作中），再删除订单
// 后端应该加一层判断，判断订单是否是 0、1 状态，如果是 2 则报错

{
    "code": 204,
    "message": "取消订单成功",
    "data": {
        "status": "取消订单成功"
    }
}
```



### 获取空闲工人列表

- 请求路径：admin/select-worker
- 请求方法：get

- 响应数据（只显示当前空闲的工人）

```json
// 这里可以设置一个师傅评分显示
{
  "code": 200,
  "message": "获取空闲工人列表",
  "data": [
    {
      "workId": "201825070120",
      "workerName": "张师傅",
      "grade": 2.2
    },
    {
      "workId": "201825070120",
      "workerName": "李师傅",
      "grade": 3.1
    },
    {
      "workId": "201825070120",
      "workerName": "梁师傅",
      "grade": 4.2
    }
  ]
}
```

### 选择工人

- 请求路径：admin/select-worker
- 请求方法：patch
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| workId | 工号     | 非空 |

- 响应数据（只显示当前空闲的工人）


```json
// 注意：后端收到这个请求后要把工人的状态改成 1 （0 空闲、1 工作中）

{
    "code": 200,
    "message": "选择成功",
    "data": {
        "status": "选择成功"
    }
}
```


### 个人中心

- 请求路径：admin/user
- 请求方法：patch
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| workId | 工号     | 非空 |
| phone | 手机号     | 不能为空 |
| gender | 性别     | 不能为空 |


- 响应数据

```json
{
    "code": 200,
    "message": "更新成功",
    "data": {
        "status": "更新成功"
    }
}
```
### 获取耗材
- 请求路径：admin/tool-list
- 请求方法：get

- 响应数据

```json
{
   "code": 200,
   "message": "获取耗材",
   "data": [
       {
       "toolId": 1,
       "toolName": "水管",
       "toolCount": 100
       },
       {
       "toolId": 2,
       "toolName": "胶布",
       "toolCount": 100
       },
       {
       "toolId": 3,
       "toolName": "网线",
       "toolCount": 100
       }
   ]
}
```


### 耗材管理
- 请求路径：admin/tool
- 请求方法：patch
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| toolId | 耗材 id     | 不能为空 |
| toolCount | 耗材数量     | 不能为空 |

- 响应数据

```json
// 注意：修改成功了别忘记刷新页面
{
    "code": 200,
    "message": "修改成功",
    "data": {
        "status": "修改成功"
    }
}
```

### 新建耗材
- 请求路径：admin/tool
- 请求方法：post
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| toolName | 耗材名称     | 不能为空 |
| toolCount | 耗材数量     | 不能为空 |

- 响应数据

```json
// 注意：添加成功了别忘记刷新页面

{
    "code": 201,
    "message": "创建成功",
    "data": {
        "status": "创建成功"
    }
}
```

### 删除耗材
- 请求路径：admin/tool
- 请求方法：delete
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| toolId | 耗材 id     | 不能为空 |

- 响应数据

```json
// 注意：删除成功了别忘记刷新页面

{
    "code": 204,
    "message": "删除成功",
    "data": {
        "status": "删除成功"
    }
}
```

### 学生列表
- 请求路径：admin/student-list
- 请求方法：get

- 响应数据


```json
{
  "code": 200,
  "message": "学生列表",
  "data": [
    {
      "studentId": "201825070120",
      "name": "张三",
      "gender": "男",
      "phone": "13128866666"
    },
    {
      "studentId": "201825070120",
      "name": "张三",
      "gender": "男",
      "phone": "13128866666"
    },
    {
      "studentId": "201825070120",
      "name": "张三",
      "gender": "男",
      "phone": "13128866666"
    }
  ]
}
```

### 删除学生
- 请求路径：admin/student
- 请求方法：delete
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| studentId | 学号    | 不能为空 |

- 响应数据

```json
// 注意：删除成功了别忘记刷新页面

{
    "code": 204,
    "message": "删除成功",
    "data": {
        "status": "删除成功"
    }
}
```


### 职工列表

- 请求路径：admin/worker-list
- 请求方法：get

- 响应数据


```json
{
    "code": 200,
    "message": "职员列表",
    "data": [
        {
            "workId": "201825070120",
            "name": "张三",
            "gender": "男",
            "phone": "13128866666",
            "joinDate": "2020-08-09T20:36:26.000Z",
            "orderNumber": 120,
            "details": "的撒娇花费很少看到哈克贺卡圣诞贺卡计划",
            "avgGrade": 5,
            "state": 0
        },
        {
            "workId": "201825070120",
            "name": "张三",
            "gender": "男",
            "phone": "13128866666",
            "joinDate": "2020-08-09T20:36:26.000Z",
            "orderNumber": 120,
            "details": "的撒娇花费很少看到哈克贺卡圣诞贺卡计划",
            "avgGrade": 5,
            "state": 1
        }
    ]
}

```

### 维修工注册

- 请求路径：admin/sign-up-w
- 请求方法：post
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| workId | 工号   | 不能为空 |
| password | 密码     | 不能为空 |
| phone | 手机号     | 不能为空 |
| gender | 性别     | 不能为空 |
| details | 详细介绍信息     | 不能为空 |

- 响应数据

```json
{
    "code": 201,
    "message": "注册成功",
    "data": {
        "status": "注册成功"
    }
}
```


### 删除职员
- 请求路径：admin/worker
- 请求方法：delete
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| workId | 工号    | 不能为空 |

- 响应数据

```json
// 注意：删除成功了别忘记刷新页面

{
    "code": 204,
    "message": "删除成功",
    "data": {
        "status": "删除成功"
    }
}
```

### 工人详情页

- 请求路径：admin/worker
- 请求方法：get
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| workId | 工号   | 不能为空 |

- 响应数据

```json
// 注意返回的数据里面有两个 list
// type 表示各种类型的数据处理的数量
// this_month 表示这个月每个订单评分和时间
{
 "code": 200,
 "message": "工人详情页",
 "data": {
     "workId": "201825070120",
     "name": "张三",
     "gender": "男",
     "joinDate": "2020-08-09T20:36:26.000Z",
     "phone": "13128866666",
     "details": "工人的详细介绍信息",
     "avgGrade": 5,
     "ordersNumber": 15,
     "ordersNumberToday": 1,
     "type": {
         "网络": 10,
         "水电": 25,
         "其它": 30
     },
     "thisMonth": [
         {
             "date": "2020-08-09T20:36:26.000Z",
             "grade": 4.2
         },
         {
             "date": "2020-08-09T20:36:26.000Z",
             "grade": 3.2
         },
         {
             "date": "2020-08-09T20:36:26.000Z",
             "grade": 2.2
         }
     ]
 }
}
```


### 统计页

- 请求路径：admin/statistics
- 请求方法：get

- 响应数据

```json
// orders_number 总数
// orders_number_0 未处理
// orders_number_0 正在进行
// orders_number_0 今日已处理
// this_month 表示这个月的每个订单

{
    "code": 200,
    "message": "统计页",
    "data": {
        "ordersNumber": 15,
        "ordersNumber_0": 2,
        "ordersNumber_1": 2,
        "ordersNumber_2": 2,
        "thisMonth": [
            {
                "date": "2020-08-09T20:36:26.000Z",
                "grade": 4.2
            },
            {
                "date": "2020-08-09T20:36:26.000Z",
                "grade": 3.2
            },
            {
                "date": "2020-08-09T20:36:26.000Z",
                "grade": 2.2
            }
        ]
    }
}
```

### 消息中心

- 请求路径：admin/massage-list
- 请求方法：get

- 响应数据

```json
{
    "code": 200,
    "message": "消息中心",
    "data": [
        {
            "massageId": 15,
            "name": "发布者名字",
            "createDate": "2020-08-09T20:36:26.000Z",
            "massage": "消息中心"
        },
        {
            "massageId": 16,
            "name": "发布者名字",
            "createDate": "2020-08-09T20:36:26.000Z",
            "massage": "消息中心"
        }
    ]
}
```

### 发布消息
- 请求路径：admin/massage
- 请求方法：post
- 请求参数

| 参数名   | 参数说明 | 备注     |
| -------- | -------- | -------- |
| workId | 管理员的工号    | 不能为空 |
| massage | 具体的内容    | 不能为空 |

- 响应数据

```json
{
    "code": 201,
    "message": "创建成功",
    "data": {
        "status": "创建成功"
    }
}
```

