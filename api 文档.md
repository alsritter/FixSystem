## API 接口说明

- 接口基准地址：`http://47.103.192.147:8080/`
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

| *状态码* | *含义*                | *说明*                 |
| -------- | --------------------- | ---------------------- |
| 200      | OK                    | 请求成功               |
| 201      | CREATED               | 创建成功               |
| 204      | DELETED               | 删除成功               |
| 400      | BAD REQUEST           | 请求的数据格式不符     |
| 401      | UNAUTHORIZED          | 请求的数字签名不匹配   |
| 403      | FORBIDDEN             | 被禁止访问             |
| 404      | NOT FOUND             | 请求的资源不存在       |
| 500      | INTERNAL SERVER ERROR | 内部错误               |
| 503      | SERVER_BUSY           | 服务器正忙，请稍后再试 |

------

### 检查登陆状态
浏览器和服务器之间时通过 Token 来确定连接状态，浏览器发起登陆成功服务端会自动生成一个 Token，服务器存一份 Token 到 redis 缓存数据库中并设置好超时时间 传回给浏览器，浏览器将这个 Token 存放在 localStorage 中，浏览器加个拦截器，每次访问服务器都把 Token 放在 Authorization 头里面，服务器得到这个 Token 后就可以判断这个用户是否登陆（或者是否登陆超时）


![image.png](https://i.loli.net/2020/09/08/3IRHlf1aYNh4mnS.png)


### localStorage
存储在 localStorage 里面的数据在页面会持久的保存到本地，所以可以做免密登陆

```js
// 保存数据到 localStorage
localStorage.setItem('key', 'value');

// 从 localStorage 获取数据
let data = localStorage.getItem('key');

// 从 localStorage 删除保存的数据
localStorage.removeItem('key');

// 从 localStorage 删除所有保存的数据
localStorage.clear();
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


## 通用工具 api

### 获取信息
- 请求路径：utils/get-info
- 请求方法：get
- 响应数据

```json
{
    "code": 200,
    "message": "信息",
    "data": {
        "endOrderCount": 6,
    "studentCount": 10,
    "waitOrderCount": 9,
    "workerCount": 6
}
}
```




### 请求验证码 👌
- 请求路径：utils/code
- get
- 请求参数

| 参数名 | 参数说明    | 备注     |
| ------ | ----------- | -------- |
| uuid   | 验证码的key | 不能为空 |


- 响应数据

![image.png](https://i.loli.net/2020/09/07/xOip5YLZB4fXs2d.png)


### 检查是否存在 👌
- 请求路径：utils/is-exist
- 请求方法：get
- 请求参数

| 参数名 | 参数说明              | 备注     |
| ------ | --------------------- | -------- |
| id     | studentId 或者 workId | 不能为空 |

- 响应数据

```json
{
    "code": 200,
    "message": "当前用户不存在",
    "data": false
}
```

### 获取错误类型 👌

- 请求路径：utils/fault-class
- 请求方法：get
- 响应数据

```json
{
    "code": 200,
    "message": "错误类型列表",
    "data": [
        
    ]
}
```


## 学生页面

### 登陆 👌 👌🏿

- 请求路径：student/login
- 请求方法：post
- 请求参数

| 参数名    | 参数说明    | 备注     |
| --------- | ----------- | -------- |
| studentId | 学生 id     | 不能为空 |
| password  | 密码        | 不能为空 |
| uuid      | 验证码的key | 不能为空 |
| codevalue | 验证码的值  | 不能为空 |

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


### 注册 👌 👌🏿

- 请求路径：student/sign-up
- 请求方法：post
- 请求参数

| 参数名    | 参数说明    | 备注     |
| --------- | ----------- | -------- |
| uuid      | 验证码的key | 不能为空 |
| codevalue | 验证码的值  | 不能为空 |
| id        | 学生 id     | 不能为空 |
| name      | 名字        | 不能为空 |
| password  | 密码        | 不能为空 |
| phone     | 手机号      | 不能为空 |
| gender    | 性别        | 不能为空 |


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

### 获取个人资料 🔒 👌 👌🏿
- 请求路径：student/user
- 请求方法：get
- 响应数据

```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "studentId": "20182507012912",
        "gender": "男",
        "phone": "13128863338",
        "name": "张三",
        "status": "获取成功"
    }
}
```

### 修改个人页 🔒 👌 👌🏿

- 请求路径：student/user
- 请求方法：patch
- 请求参数

| 参数名 | 参数说明 | 备注     |
| ------ | -------- | -------- |
| name   | 名称     | 不能为空 |
| phone  | 手机号   | 不能为空 |
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

### 上传图片
- 请求路径：student/upload
- 请求方法：post

- 响应数据（因为在后端要更改文件名（避免重复），所以需要返回文件名）

```json
{
    "code": 201,
    "message": "上传成功",
    "data": {
        "2157813812381.jpg"
    }
}
```

### 发起订单 🔒 👌 👌🏿

- 请求路径：student/order
- 请求方法：post
- 请求参数

| 参数名       | 参数说明     | 备注     |
| ------------ | ------------ | -------- |
| faultClass   | 报错类型     | 不能为空 |
| address      | 地址         | 不能为空 |
| contacts     | 联系人的名称 | 可以为空 |
| studentPhone | 联系人的电话 | 可以为空 |
| faultDetails | 说明         | 不能为空 |

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

contacts 和 studentPhone 为空时就默认填入当前账号的信息

### 当前订单 🔒 👌 👌🏿


- 请求路径：student/order
- 请求方法：get

- 响应数据（只能查到正在进行的和未处理的，用来查看用户的级别，0 是只能查看未处理，1 是正在进行的订单，注意看这些字段哪些是空的，以及 **未评价** 的已处理订单）

```json
{
    "code": 200,
    "message": "订单详情",
    "data": [
        {
            "fixTableId": 23,
            "studentId": "201825070129",
            "contacts": "小美",
            "address": "15 栋 614",
            "createdTime": 1597193198000,
            "endTime": null,
            "phone": "13123338855",
            "faultClass": "中央空调",
            "faultDetail": "飒飒大苏打盛大的是",
            "workId": "201895070344",
            "adminWorkId": "201835070344",
            "resultDetails": null,
            "grade": 10,
            "message": null,
            "state": 1
        },
        {
            "fixTableId": 29,
            "studentId": "201825070129",
            "contacts": "老朱",
            "address": "宿舍",
            "createdTime": 1599675141000,
            "endTime": null,
            "phone": "13128888888",
            "faultClass": "一卡通报修",
            "faultDetail": "校园卡丢失",
            "workId": null,
            "adminWorkId": null,
            "resultDetails": null,
            "grade": 10,
            "message": null,
            "state": 0
        }
    ]
}
```

后端读取 Token 来获取学号

### 获取工人详情页 🔒 👌 👌🏿

- 请求路径：student/worker-info
- 请求方法：get
- 请求参数

| 参数名 | 参数说明 | 备注     |
| ------ | -------- | -------- |
| id     | 工人的id | 不能为空 |
- 响应数据

```json
// 注意返回的数据里面有两个 list
// type 表示各种类型的数据处理的数量
// this_month 表示这个月每个订单评分和时间
{
    "code": 201,
    "message": "工人详情页",
    "data": {
        "ordersNumber": 0,
        "joinDate": 1596258000000,
        "gender": "男",
        "phone": "1313218888",
        "avgGrade": 5.0,
        "name": "钱七丰",
        "details": "俺就是个小透明",
        "thisMonth": [
            {
                "date": 1599439603000,
                "grade": 9
            },
            {
                "date": 1599958003000,
                "grade": 9
            },
            {
                "date": 1599958003000,
                "grade": 9
            },
            {
                "date": 1599958003000,
                "grade": 9
            },
            {
                "date": 1599958003000,
                "grade": 9
            },
            {
                "date": 1599958003000,
                "grade": 9
            },
            {
                "date": 1599439603000,
                "grade": 9
            },
            {
                "date": 1599353203000,
                "grade": 9
            },
            {
                "date": 1599526003000,
                "grade": 9
            }
        ],
        "ordersNumberToday": 0,
        "type": [
            {
                "number": 1,
                "typeName": "一卡通报修"
            },
            {
                "number": 1,
                "typeName": "中央空调"
            },
            {
                "number": 5,
                "typeName": "空调"
            },
            {
                "number": 2,
                "typeName": "网络问题"
            }
        ],
        "workId": "201895070999"
    }
}
```



### 处理结果评价 🔒 👌 👌🏿


- 请求路径：student/order-end
- 请求方法：patch
- 请求参数（当工人处理完成之后，前端检查到这个学生最后一个订单如果学生评价为空就弹出 处理结果评价页面）

| 参数名     | 参数说明 | 备注                           |
| ---------- | -------- | ------------------------------ |
| fixTableId | 订单 id  | 不能为空，根据订单的 id 来查询 |
| message    | 学生留言 | 可以为空                       |
| grade      | 学生打分 | 可以为空                       |

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

后端通过读取 Token 里的学号 来判断当前订单是否是这个学生评价的

### 历史订单 🔒 👌 👌🏿

- 请求路径：student/order-list
- 请求方法：get
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
根据当前学生查询对应的订单，如果没有则返回空（后端读取 Token 获取 学生 id 来查）

注意这个订单 id，后面点进每个历史订单查询数据就是通过这个 id 来查的

### 历史订单详情 🔒 👌 👌🏿

- 请求路径：student/order-pass
- 请求方法：get
- 请求参数

| 参数名     | 参数说明 | 备注                           |
| ---------- | -------- | ------------------------------ |
| fixTableId | 订单 id  | 不能为空，根据订单的 id 来查询 |


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
        "contacts": "王五",
        "fault_detail": "宿舍的电脑突然上不了网了.....",
        "worker_phone": "13128866666",
        "create_time": "2020-08-09T20:36:26.000Z",
        "end_time": "2020-08-09T20:36:26.000Z",
        "grade": 4.2
    }
}
```

## 维修工人页面

### 登陆 👌 👌🏿

- 请求路径：worker/login
- 请求方法：post
- 请求参数

| 参数名    | 参数说明    | 备注     |
| --------- | ----------- | -------- |
| workId    | 工号        | 不能为空 |
| password  | 密码        | 不能为空 |
| uuid      | 验证码的key | 不能为空 |
| codevalue | 验证码的值  | 不能为空 |

- 响应数据

```json
{
    "code": 200,
    "message": "维修工人登陆页面",
    "data": {
        "workId": "201825070120",
        "name": "张三",
        "gender": "男",
        "joinDate": "2020-08-09T20:36:26.000Z",
        "phone": "13128866666",
        "details": "工人的详细介绍信息",
        "ordersNumber": 21,
        "avgGrade": 4.2
    }
}
```

前端拿到数据之后存在 sessionStorage，以后之前读取这里面的东西就好了

### 工人详情页 🔒 👌 👌🏿

- 请求路径：worker/home
- 请求方法：get

- 响应数据

```json
// 注意返回的数据里面有两个 list
// type 表示各种类型的数据处理的数量
// this_month 表示这个月每个订单评分和时间
{
    "code": 201,
    "message": "工人详情页",
    "data": {
        "ordersNumber": 0,
        "joinDate": 1596258000000,
        "gender": "男",
        "phone": "1313218888",
        "avgGrade": 5.0,
        "name": "钱七丰",
        "details": "俺就是个小透明",
        "thisMonth": [
            {
                "date": 1599439603000,
                "grade": 9
            },
            {
                "date": 1599958003000,
                "grade": 9
            },
            {
                "date": 1599958003000,
                "grade": 9
            },
            {
                "date": 1599958003000,
                "grade": 9
            },
            {
                "date": 1599958003000,
                "grade": 9
            },
            {
                "date": 1599958003000,
                "grade": 9
            },
            {
                "date": 1599439603000,
                "grade": 9
            },
            {
                "date": 1599353203000,
                "grade": 9
            },
            {
                "date": 1599526003000,
                "grade": 9
            }
        ],
        "ordersNumberToday": 0,
        "type": [
            {
                "number": 1,
                "typeName": "一卡通报修"
            },
            {
                "number": 1,
                "typeName": "中央空调"
            },
            {
                "number": 5,
                "typeName": "空调"
            },
            {
                "number": 2,
                "typeName": "网络问题"
            }
        ],
        "workId": "201895070999"
    }
}
```



### 当前订单 🔒 👌


- 请求路径：worker/order
- 请求方法：get


- 响应数据

```json
{
    "code": 200,
    "message": "订单详情",
    "data": [
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
            "message": null,
            "state": 1
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
            "message": null,
            "state": 1
        }
    ]
}
```

后端读取 Token 获取 id 来查

### 历史订单 🔒 👌

- 请求路径：worker/order-list
- 请求方法：get


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
后端读取 Token 获取 id 来查

注意这个订单 id，后面点进每个历史订单查询数据就是通过这个 id 来查的


### 历史订单详情 🔒 👌

- 请求路径：worker/order-pass
- 请求方法：get
- 请求参数

| 参数名     | 参数说明 | 备注                           |
| ---------- | -------- | ------------------------------ |
| fixTableId | 订单 id  | 不能为空，根据订单的 id 来查询 |


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
        "contacts": "王五",
        "faultDetail": "宿舍的电脑突然上不了网了.....",
        "workerPhone": "13128866666",
        "createTime": "2020-08-09T20:36:26.000Z",
        "endTime": "2020-08-09T20:36:26.000Z",
        "grade": 4.2
    }
}
```

### 处理结果 🔒 👌


- 请求路径：worker/order-end
- 请求方法：patch
- 请求参数

| 参数名        | 参数说明 | 备注                           |
| ------------- | -------- | ------------------------------ |
| fixTableId    | 订单 id  | 不能为空，根据订单的 id 来查询 |
| resultDetails | 处理结果 | 可以为空                       |

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
后端读取 Token 获取 id 来查

### 消息中心 🔒  👌

- 请求路径：worker/message-list
- 请求方法：get

- 响应数据

```json
{
    "code": 200,
    "message": "消息中心",
    "data": [
        {
            "messageId": 15,
            "name": "发布者名字",
            "createDate": "2020-08-09T20:36:26.000Z",
            "message": "消息中心"
        },
        {
            "messageId": 16,
            "name": "发布者名字",
            "createDate": "2020-08-09T20:36:26.000Z",
            "message": "消息中心"
        }
    ]
}
```
注意这个消息 id，后面点进每个消息查询数据就是通过这个 id 来查的


### 获取消息 🔒  👌
上面的那个获取消息列表如果数据大于 50 则自动省略，所以消息细节需要使用这个来获取

- 请求路径：worker/message
- 请求方法：get
- 请求参数

| 参数名    | 参数说明 | 备注                           |
| --------- | -------- | ------------------------------ |
| messageId | 消息 id  | 不能为空，根据消息的 id 来查询 |

- 响应数据

```json
{
    "code": 200,
    "message": "消息",
    "data": {
            "messageId": 15,
            "name": "发布者名字",
            "createDate": "2020-08-09T20:36:26.000Z",
            "message": "消息"
        }
}
```

### 获取管理员名称 🔒 
- 请求路径：worker/get-admin-name
- 请求方法：get
- 请求参数

| 参数名 | 参数说明  | 备注     |
| ------ | --------- | -------- |
| workId | 管理员 id | 不能为空 |

- 响应数据

```json
{
    "code": 200,
    "message": "管理员姓名",
    "data": "alsritter"
}
```

## 管理员页面

### 登陆 👌 👌🏿

- 请求路径：admin/login
- 请求方法：post
- 请求参数

| 参数名    | 参数说明    | 备注     |
| --------- | ----------- | -------- |
| workId    | 工号        | 不能为空 |
| password  | 密码        | 不能为空 |
| uuid      | 验证码的key | 不能为空 |
| codevalue | 验证码的值  | 不能为空 |

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
      "token": "df2fd4702b4b28070a43f4f1428d1797"
      }
}
```

### 搜索订单 🔒
- 请求路径：admin/search-order
- 请求方法：get
- 请求参数

| 参数名 | 参数说明 | 备注     |
| ------ | -------- | -------- |
| word   | 关键字   | 不能为空 |

- 响应数据

```json
{
    "code": 200,
    "message": "搜索成功",
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
            "message": null,
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
            "message": null,
            "state": 1
        }
    ]
}
```

### 搜索学生 🔒
- 请求路径：admin/search-student
- 请求方法：get
- 请求参数

| 参数名 | 参数说明 | 备注     |
| ------ | -------- | -------- |
| id     | 关键字   | 不能为空 |

- 响应数据

```json
{
    "code": 200,
    "message": "搜索成功",
    "data": [
        {
            "id": "201825070134",
            "name": "小刚",
            "gender": "男",
            "password": "sasdasd0",
            "phone": "13128877777"
        }
    ]
}
```

### 搜索工人 🔒
不能全部为空

- 请求路径：admin/search-worker
- 请求方法：get
- 请求参数

| 参数名 | 参数说明 | 备注     |
| ------ | -------- | -------- |
| id     | 关键字   | 可以为空 |
| name     | 关键字   | 可以为空 |
| name     | 关键字   | 可以为空 |

- 响应数据

```json
{
    "code": 200,
    "message": "搜索成功",
    "data": [
        {
            "id": "201895070322",
            "name": "李四丰",
            "gender": "男",
            "password": "1ss211s234",
            "joinDate": 1596258000000,
            "phone": "13128834248",
            "details": "俺就是个小透明",
            "ordersNumber": 0,
            "avgGrade": 10.0,
            "state": 0
        }
    ]
}
```


### 搜索订单 🔒
- 请求路径：admin/search-order
- 请求方法：get
- 请求参数

| 参数名 | 参数说明 | 备注     |
| ------ | -------- | -------- |
| word   | 关键字   | 不能为空 |

- 响应数据

```json
{
    "code": 201,
    "message": "搜索成功",
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
            "message": null,
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
            "message": null,
            "state": 1
        }
    ]
}
```

### 获取订单 List 🔒 👌 👌🏿

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
            "message": null,
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
            "message": null,
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
            "message": "jyhiuyiuyiuyiuyiuyi",
            "state": 2
        }
    ]
}
```
注意这个订单 id，后面点进每个历史订单查询数据就是通过这个 id 来查的

### 订单详情 🔒 👌 👌🏿

- 请求路径：admin/order
- 请求方法：get
- 请求参数

| 参数名     | 参数说明 | 备注                           |
| ---------- | -------- | ------------------------------ |
| fixTableId | 订单 id  | 不能为空，根据订单的 id 来查询 |


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
      "message": null,
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
        "message": null,
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
      "message": "dhjagsjdhgajshdgad",
      "state": 2
    }
}        
```

### 取消订单 🔒 👌 👌🏿

- 请求路径：admin/order
- 请求方法：delete
- 请求参数

| 参数名     | 参数说明 | 备注 |
| ---------- | -------- | ---- |
| fixTableId | 订单号   | 非空 |

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



### 取空闲工人列表 🔒 👌 👌🏿

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
            "workId": "201895070111",
            "name": "张三丰",
            "gender": "男",
            "password": "1232114",
            "joinDate": 1596258000000,
            "phone": "1312888888",
            "details": "俺就是个小透明",
            "ordersNumber": 0,
            "avgGrade": 10.0,
            "state": 0
        },
        {
            "workId": "201895070322",
            "name": "李四丰",
            "gender": "男",
            "password": "1ss211s234",
            "joinDate": 1596258000000,
            "phone": "13128834248",
            "details": "俺就是个小透明",
            "ordersNumber": 0,
            "avgGrade": 10.0,
            "state": 0
        }
    ]
}
```

### 选择工人 🔒 👌 👌🏿

- 请求路径：admin/select-worker
- 请求方法：patch
- 请求参数

| 参数名     | 参数说明 | 备注 |
| ---------- | -------- | ---- |
| workId     | 工号     | 非空 |
| fixTableId | 订单号   | 非空 |

- 响应数据


```json
// 注意：后端收到这个请求后要把工人的状态改成 1 （0 空闲、1 工作中）

{
    "code": 200,
    "message": "指定工人成功",
    "data": {
        "status": "SUCCESS"
    }
}
```


### 修改个人资料 🔒  👌

- 请求路径：admin/user
- 请求方法：patch
- 请求参数

| 参数名  | 参数说明     | 备注     |
| ------- | ------------ | -------- |
| name    | 姓名         | 不能为空 |
| phone   | 手机号       | 可以为空 |
| gender  | 性别         | 可以为空 |
| details | 个人资料详情 | 可以为空 |
| address | 家庭住址 | 可以为空 |
| department | 所属部门 | 可以为空 |
| email | 邮箱 | 可以为空 |
| place | 籍贯 | 可以为空 |
| ground | 职位 | 可以为空 |
| idnumber | 身份证号 | 可以为空 |

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

后端读取 Token 获取 id 来查


### 获取个人资料 🔒 👌
- 请求路径：admin/user
- 请求方法：get
- 响应数据

```json
{
    "code": 200,
    "message": "获取成功",
    "data": {"code":200,"message":"获取成功","data":{"address":"天桥底","department":"系统管理员","email":"123456@gmail.com","place":"广东","idnumber":"110105200201010395","ground":"后勤部老师","id":"201835070344","name":"王五","gender":"女","password":null,"joinDate":1596258000000,"phone":"1312821388","details":"俺就是个小透明"}}
}
```

### 获取耗材 🔒  👌
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


### 修改耗材数量 🔒  👌
- 请求路径：admin/tool
- 请求方法：patch
- 请求参数

| 参数名    | 参数说明 | 备注     |
| --------- | -------- | -------- |
| toolId    | 耗材 id  | 不能为空 |
| toolCount | 耗材数量 | 不能为空 |

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

### 新建耗材 🔒 👌
- 请求路径：admin/tool
- 请求方法：post
- 请求参数

| 参数名    | 参数说明 | 备注     |
| --------- | -------- | -------- |
| toolName  | 耗材名称 | 不能为空 |
| toolCount | 耗材数量 | 不能为空 |

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

### 删除耗材 🔒 👌
- 请求路径：admin/tool
- 请求方法：delete
- 请求参数

| 参数名 | 参数说明 | 备注     |
| ------ | -------- | -------- |
| toolId | 耗材 id  | 不能为空 |

- 响应数据

```json
// 空
```

### 学生列表 🔒 👌
- 请求路径：admin/student-list
- 请求方法：get

- 响应数据


```json
{
    "code": 200,
    "message": "获取学生列表",
    "data": [
        {
            "studentId": "201825070129",
            "gender": "男",
            "phone": "13128866666",
            "name": "alsritter"
        },
        {
            "studentId": "2018250701291",
            "gender": "男",
            "phone": "13128863338",
            "name": "赵6"
        },
        {
            "studentId": "20182507012912",
            "gender": "男",
            "phone": "13128863338",
            "name": "张三"
        },
        {
            "studentId": "2018250701291321",
            "gender": "男",
            "phone": "13128863339",
            "name": "老王"
        }
    ]
}
```



### 职工列表 🔒 👌

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

### 录入维修工 🔒 👌

- 请求路径：admin/sign-up-w
- 请求方法：post
- 请求参数

| 参数名   | 参数说明     | 备注     |
| -------- | ------------ | -------- |
| workId   | 工人 id      | 不能为空 |
| name     | 名字         | 不能为空 |
| password | 密码         | 不能为空 |
| phone    | 手机号       | 不能为空 |
| gender   | 性别         | 不能为空 |
| details  | 详细介绍信息 | 不能为空 |
| address | 家庭住址 | 可以为空 |
| department | 所属部门 | 可以为空 |
| email | 邮箱 | 可以为空 |
| place | 籍贯 | 可以为空 |
| ground | 职位 | 可以为空 |
| idnumber | 身份证号 | 可以为空 |

- 响应数据

```json
{
    "code": 201,
    "message": "录入成功",
    "data": {
        "status": "录入成功"
    }
}
```

后端读取 Token 获取 id 再插入，“处理管理员” 这个字段

### 工人详情页 🔒 👌

- 请求路径：admin/worker
- 请求方法：get
- 请求参数

| 参数名 | 参数说明 | 备注     |
| ------ | -------- | -------- |
| workId | 工号     | 不能为空 |

- 响应数据

```json
// 注意返回的数据里面有两个 list
// type 表示各种类型的数据处理的数量
// this_month 表示这个月每个订单评分和时间
{
    "code": 201,
    "message": "工人详情页",
    "data": {
        "ordersNumber": 0,
        "joinDate": 1596258000000,
        "gender": "男",
        "phone": "1313218888",
        "avgGrade": 5.0,
        "name": "钱七丰",
        "details": "俺就是个小透明",
        "thisMonth": [
            {
                "date": 1599439603000,
                "grade": 9
            },
            {
                "date": 1599958003000,
                "grade": 9
            },
            {
                "date": 1599958003000,
                "grade": 9
            },
            {
                "date": 1599958003000,
                "grade": 9
            },
            {
                "date": 1599958003000,
                "grade": 9
            },
            {
                "date": 1599958003000,
                "grade": 9
            },
            {
                "date": 1599439603000,
                "grade": 9
            },
            {
                "date": 1599353203000,
                "grade": 9
            },
            {
                "date": 1599526003000,
                "grade": 9
            }
        ],
        "ordersNumberToday": 0,
        "type": [
            {
                "number": 1,
                "typeName": "一卡通报修"
            },
            {
                "number": 1,
                "typeName": "中央空调"
            },
            {
                "number": 5,
                "typeName": "空调"
            },
            {
                "number": 2,
                "typeName": "网络问题"
            }
        ],
        "workId": "201895070999"
    }
}
```


### 统计页 🔒 👌

- 请求路径：admin/statistics
- 请求方法：get

- 响应数据

```json
// orders_number 总数
// orders_number_0 未处理
// orders_number_1 正在进行
// orders_number_2 今日已处理
// this_month 表示这个月的每个订单

{
    "code": 200,
    "message": "统计数据",
    "data": {
        "ordersNumber": 14,
        "ordersNumber_0": 6,
        "thisMonth": [
            {
                "date": 1599007598000,
                "grade": 7
            },
            {
                "date": 1599093998000,
                "grade": 8
            },
            {
                "date": 1600217198000,
                "grade": 10
            },
            {
                "date": 1599439603000,
                "grade": 9
            }
        ],
        "ordersNumber_1": 4,
        "ordersNumber_2": 4
    }
}
```

### 获取消息列表 🔒 👌

- 请求路径：admin/message-list
- 请求方法：get

- 响应数据

```json
{
    "code": 200,
    "message": "消息列表",
    "data": [
        {
            "adminId": "201835070999",
            "createDate": 1600123224000,
            "messageId": 1,
            "messageStr": "今晚去吃烤肉吧"
        },
        {
            "adminId": "201835070999",
            "createDate": 1600036824000,
            "messageId": 2,
            "messageStr": "吃个屁！！！"
        }
    ]
}
```

### 获取消息 🔒  👌
上面的那个获取消息列表如果数据大于 50 则自动省略，所以消息细节需要使用这个来获取

- 请求路径：admin/message
- 请求方法：get
- 请求参数

| 参数名    | 参数说明 | 备注                           |
| --------- | -------- | ------------------------------ |
| messageId | 消息 id  | 不能为空，根据消息的 id 来查询 |

- 响应数据

```json
{
    "code": 200,
    "message": "消息",
    "data": {
            "messageId": 15,
            "name": "发布者名字",
            "createDate": "2020-08-09T20:36:26.000Z",
            "message": "消息"
        }
}
```


### 发布消息 🔒 👌
- 请求路径：admin/message
- 请求方法：post
- 请求参数

| 参数名  | 参数说明   | 备注     |
| ------- | ---------- | -------- |
| message | 具体的内容 | 不能为空 |

- 响应数据

```json
{
    "code": 201,
    "message": "推送成功",
    "data": {
        "status": "推送成功"
    }
}
```
后端读取 Token 获取 id 再插入，“处理管理员” 这个字段
