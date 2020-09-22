# 校园智能报修系统
![image.png](https://i.loli.net/2020/09/19/eHwZklO6ao5Qm3y.png)
## FixSystem 是什么？😀

**FixSystem 是一个前后端分离的智能报修系统**

随着高校信息化的快速发展，智慧化校园已是校园信息化的必然发展趋势。维修管理已作为数字化后勤、智慧化勤的一部分，不可缺少。所以 **FixSystem** 是一套专注于校园报修的工具系统，**FixSystem** 管理端采用 B/S 架构、客户端（学生端、维修工人）使用 app 来登陆报修以及处理日常业务

项目演示视频：[点击这里](https://www.bilibili.com/video/BV1Qp4y1e7Zz)
项目地址：[点击这里](https://gitee.com/KUJOUN/fix-system-web)

> 管理端页面
![GIF.gif](https://i.loli.net/2020/09/19/4TjwSvyaObZkgQ6.gif)
![image.png](https://i.loli.net/2020/09/19/oQB7HNGlkqFEdtM.png)

> 学生端、维修工页面
![20200922223611.jpg](https://i.loli.net/2020/09/22/LvwH3NZBSk7JpKC.jpg)
![20200922223618.jpg](https://i.loli.net/2020/09/22/Tudt1zqsCw6phPf.jpg)

### 在线演示地址 👀

> **管理员端** 👨‍💼
在线演示地址：[管理员端在线演示](http://alsritter.gitee.io/fix-system-page/#/)
⚠️ 注意：因为管理端未做移动端适配，所以请使用电脑打开（不支持 IE、最好使用 chrome 浏览器打开）



> **学生和工人端** 👨‍🔧 🤵
APP 下载地址：[点击下载](https://gitee.com/alsritter/fix-system-app/attach_files/481253/download)




### 项目地址
* [后端项目](https://gitee.com/alsritter/fix-system)
* [学生端、工人端项目](https://gitee.com/alsritter/fix-system-app)
* [管理端项目](https://gitee.com/KUJOUN/fix-system-web)




### 开发环境 🌟

FixSystem **后端** 采用的是 Java 语言开发，使用的是 SpringBoot 框架，采用 Restful API 的形式与前端交互，使用 Mybatis 来处理持久层，采用 Redis 来做缓存数据库，登陆鉴权则是采用 Redis + Token 的形式。


FixSystem **前端** 采用的是 Vue + ElementUI + ECharts 开发

前端技术 | 后端技术
------- | -------
Vue-cli | SpringBoot
Axios | SpringMVC
ElementUI | MyBatis
Less | Redis
ECharts | Docker



### API 文档
因为项目是前后端分离的，所以是通过 ajax 进行交互，具体的 api 文档参见：[API 文档](https://gitee.com/alsritter/fix-system/blob/master/api%20%E6%96%87%E6%A1%A3.md)

部分 API 展示：
![image.png](https://i.loli.net/2020/09/22/je7NW8h3fmC1iqF.png)![image.png](https://i.loli.net/2020/09/22/eOnKDRmIZ1TPoEg.png)

### 系统架构 👨🏻‍💻 
![image.png](https://i.loli.net/2020/09/19/KqawBVUztelTkh2.png)


## FixSystem 能做什么？


### 管理员端功能 👨‍💼
[![wXaTyT.md.png](https://s1.ax1x.com/2020/09/22/wXaTyT.md.png)](https://imgchr.com/i/wXaTyT)

>  web端订单功能

* 不同状态的订单
* 订单的ID
* 联系人电话等信息
* 可以根据用户自己的喜欢好来进行排序
* 不同状态的订单也会有不同的功能
* 点击 **查** ，即可查看详细的报修信息
* 点击 **选**，即可分配工人
* 点击 **删**，即可删除订单
* 点击 **详**，即可查看订单的详情



> web端学生管理

* 在学生管理一页可以看到当前注册学生的信息

> web端工人管理

* 在工人管理一页同样可以看到工人的信息
* 点击“查看详情”，可以看到的详细信息
* 点击录入工人可以为数据库添加一个新的工人



> web端耗材管理

* 在耗材管理这一页，可以对耗材进行增加，删除，修改操作
* 点击删除，即可删除耗材
* 点击添加新耗材，即可添加一个耗材，可以选择数量
* 点击计数器，即可修改耗材数量

> web端统计

* 在这一页可以看到本月的订单统计


> web端消息中心

* 在这一页可以看到管理员为工人发布的消息
* 点击发布公告 ，即可发布一则不超过200字的公告

> web端个人中心

* 在这一页可以修改管理员的个人信息
* 点击提交可以保存管理员个人信息的更改
* 点击重置则会重置表单
* 点击放弃则会返回到订单界面

### 学生端功能 👨‍🎓
![image.png](https://i.loli.net/2020/09/22/TQJSMtiGID2Cjdv.png)

> 学生端注册

* 对于第一次进系统的用户，我们提供了注册功能
* 点击注册即可完成注册

> 学生端修改个人资料

* 这里可以修改学生的个人信息，
* 点击提交即可完成修改

> 学生端发起订单

* 在这一页，学生可以对坏了的东西发起一个订单，以等待工人的维修
* 点击提交即可完成订单的发起

> 学生端当前订单

* 在这一页可以看到当前正在进行，和未处理的订单
* 点击查看详情，即可查看当前订单的状态，单号，工人等信息

> 学生端历史订单

* 在这一页可以看到已完成的订单
* 点击详情可以对未评价的订单进行评价



### 工人端功能 👨‍🔧
![image.png](https://i.loli.net/2020/09/22/ckvoIs59AXnfBUF.png)

> 工人端个人中心

* 在这一页可以看到工人的个人信息，比如最近订单评价，本月评分，加入时间等信息



> 工人端订单列表

* 在这一页可以看到工人当前正在处理的订单信息
* 点击完成报修可以结束本订单，并对该订单进行一个结果描述



> 工人端历史订单

* 在这一页可以看到工人已完成的订单消息
* 可以看到评分报修类型和订单的ID等信息
* 点击查看详情，可以看到该订单的更多信息

> 工人端消息中心

* 在这一页可以看到管理员为工人发布的消息



## 快速入门 🚀

### 搭建运行环境
因为采用了 Docker 来部署服务，所以需要先在服务器部署一下 Docker
关于 Docker 的安装参考：[官方文档--Ubuntu](https://docs.docker.com/engine/install/ubuntu/) 

```shell
# 安装好 Docker 后再安装一下 docker-compose
sudo apt-get install docker-compose

# 创建 docker-compose.yml 文件
touch docker-compose.yml
```
编写下列配置信息到 `docker-compose.yml` 里面
```yml
version: '3'
services:
    service_redis:
        container_name: container_redis
        image: redis:4.0.14
        environment:
            - TZ=Asia/Shanghai
        ports:
            - "6379:6379"
        volumes:
            - ./config/redis/redis.conf:/usr/local/etc/redis/redis.conf
            - ./data/redis/:/data/
            - ./log/redis/:/var/log/redis/
        command: redis-server /usr/local/etc/redis/redis.conf
        # restart: always 表示每次重启 docker 这个会自动启动
        restart: always
    service_mysql:
        container_name: container_mysql
        image: mysql:5.7
        environment:
            TZ: Asia/Shanghai
            MYSQL_ROOT_PASSWORD: root
            MYSQL_ROOT_HOST: '%'
        ports:
            - "3306:3306"
        volumes:
            - ./config/mysql/my.cnf:/etc/mysql/conf.d/my.cnf
            - ./data/mysql/:/var/lib/mysql/
            - ./data/init/:/docker-entrypoint-initdb.d/
            - ./log/mysql/:/var/log/mysql/
        command: [
            '--character-set-server=utf8mb4',
            '--collation-server=utf8mb4_unicode_ci'
        ]
        restart: always
```

再启动服务，Docker 就好自动下载相应的 mysql、redis
```shell
# docker-compose 启动
docker-compose up -d
```

创建数据库：运行 [mysql 脚本](https://gitee.com/alsritter/fix-system/blob/master/sql/-2020_09_19_18_25_42-dump.sql)

到这里为止已经把基本环境搭建好了，现在执行编译启动后端的库就能稳定提供 api 了 具体参考：[API 文档](https://gitee.com/alsritter/fix-system/blob/master/api%20%E6%96%87%E6%A1%A3.md)

## License
```
MIT License

Copyright (c) 2020 alsritter

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 特别感谢
所有杰出的 贡献者 ❤️
<br>
[![alsritter](https://portrait.gitee.com/uploads/avatars/user/2188/6565378_alsritter_1588856244.png!avatar60)](https://gitee.com/alsritter)[![KUJOUN](https://portrait.gitee.com/uploads/avatars/user/2520/7562459_KUJOUN_1599700971.png!avatar60)](https://gitee.com/KUJOUN)[![PeterPan0824](https://portrait.gitee.com/uploads/avatars/user/2674/8023282_PeterPan0824_1599298335.png!avatar60)](https://gitee.com/PeterPan0824)[![hot soda](https://portrait.gitee.com/uploads/avatars/user/2670/8011803_hot-soda_1600523602.png!avatar60)](https://gitee.com/hot-soda)[![bei shang](https://portrait.gitee.com/uploads/avatars/user/1834/5503755_bei_shang_1600523771.png!avatar60)](https://gitee.com/bei_shang)

<br>

**指导老师**： **廖伟国**
