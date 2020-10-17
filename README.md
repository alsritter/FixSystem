# 校园智能报修系统
<div align=center><img src="https://i.loli.net/2020/09/19/eHwZklO6ao5Qm3y.png"/></div>
<div align=center>
<img src="https://img.shields.io/badge/bulid- upgrade-critical"/>
<img src="https://img.shields.io/badge/style-Flad-green"/>
<img src="https://img.shields.io/badge/require-smart-orange"/>
</div>

## FixSystem 是什么？ 

- **FixSystem 是一个前后端分离的智能报修系统**
    - 随着现代信息技术的快速发展，智慧化校园已是校园信息化的必然发展趋势。智能化提升校园的管理效率并降低人工成本是现代化设备建设的重要组成部分。为了方便学生、教师等学校居住人员的日常保修。使大家的生活更加智能化和便利化而设计的智能保修系统。
    - 所以 FixSystem 是一套专注于校园报修的工具系统，FixSystem 管理端采用 B/S 架构、客户端（学生端、维修工人）使用 app 来登陆报修以及处理日常业务。

    - 点击查看项目演示视频： [![1602587229_1_.png](https://i.loli.net/2020/10/13/H7lv8sUwVnptBPi.png)](https://www.bilibili.com/video/BV1Qp4y1e7Zz)
    - 点击查看项目地址：[![1602587707_1_.png](https://i.loli.net/2020/10/13/VXHN5ToyqYegCsJ.png)](https://gitee.com/KUJOUN/fix-system-web)
    - 管理端在线演示：[![image.png](https://i.loli.net/2020/10/15/Iz2g8Nr4C1JvxyL.png)](http://alsritter.gitee.io/fix-system-page/)
    - 手机端下载地址：[!![image.png](https://i.loli.net/2020/10/15/IfK2GrjVRCo7aOk.png)](https://gitee.com/alsritter/fix-system-app/attach_files/494715/download/fixSystemVersion2.apk)

## FixSystem 软件优势

    - 摒弃传统的报修方法，省时省力。
    
    - 无需手动去填纸质保修单。
    
    - 对设备维修进程一目了然，查看实时维修状态。
    
    - 系统自动生成维修故障数据，如：维修次数等。
    
    - 报修简便、时效快、耗时少、操作更加人性化。
![benefit.png](https://i.loli.net/2020/10/11/R6l7jyior3fNPa9.png)
  
## Fixsystem 亮点功能
-  **耗材管理功能** 
    - 耗材管理功能能够对学校内部仓库的所有材料使用情况进行记录，让管理者能够更方便地进行管理，所有的材料信息在后台中都可以清晰地查看到，方便管理员进行耗材的核实与月度、季度、年度所用耗材的统计。
-  **用户评价功能** 
    - 用户可以对维修工进行评价，评价内容会反馈到管理员，便于管理员查看维修工的实际工作情况，以及对技术水平的评估，对维修团队进行有效的考核激励， 提升维保服务客户满意度。给用户提供更为优质的维修资源。
-  **扫码管理功能** 
    - 用户可以通过扫描二维码，点对点的找到最合适的维修师傅，这些二维码会贴在宿舍的设备上(如空调、洗衣机、电灯等设备)。
    ![saoma.png](https://i.loli.net/2020/10/14/R5oHTPK8A1aNC4G.png)


### 网页路径与App安装 👀

- **管理员** 
  - 点击图标跳转管理端页面：[👨‍💼](http://alsritter.gitee.io/fix-system-page/#/) <br>
⚠️ 注意：因为管理端未做移动端适配，所以请使用电脑打开（不支持 IE、最好使用 chrome 浏览器 100% 缩放打开）
    [历史版本演示（v1.0）](https://www.bilibili.com/video/BV1Qp4y1e7Zz)

- **学生和工人端** 
   - 点击图标下载当前版本 APP（V2.0.1） 下载： [👨‍🔧 🤵](https://gitee.com/alsritter/fix-system-app/attach_files/481253/download)
   - 下载历史版本（V1.0.2 版本）：[👨‍🔧 🤵](https://gitee.com/alsritter/fix-system-app/attach_files/481253/download)

### 项目地址
* [后端项目](https://gitee.com/alsritter/fix-system)
* [学生端、工人端项目](https://gitee.com/alsritter/fix-system-app)
* [管理端项目](https://gitee.com/KUJOUN/fix-system-web)





## FixSystem 开发环境 🌟

- ### 管理代码环境
     1. [gitee 地址](https://gitee.com/KUJOUN/fix-system-web)
     2. [github 地址](https://github.com/alsritter/FixSystem)
     3. 使用 Powershell 编写的自动化脚本，实现自动部署发布

- ### 后端开发环境
    1. 主体：WSL2 部署的 Docker 服务
    2. 数据库：MySQL 5.7、Redis（存储自己设计的一套 Token 系统、以及做缓存服务）
    3. 测试：
        1. 使用 Fiddler4 对请求进行抓包以及 Mock Api
        2. Postman 自动化测试 api
        3. 使用 Junit5 打包时自动单元测试 

- ### 前端开发环境
    1. 主体: vscode
    3. UI设计软件：PS、XD、AI
    4. 测试：Postman
    5. Vue-cli
    6. HbuilderX 打包
    7. 组件库 ElementUI

## FixSystem 开发技术

- ### 后端技术
    1. 主要编程语言：java
    2. 框架：SpringMVC + SpringBoot + Mybatis
    3. 数据缓存技术：Redis
    4. 数据库技术：MySQL
    5. 容器技术：Docker

- ###  前端技术
    1. Less、ES6
    2. 前端框架：Vue-cli、NodeJs
    3. Ajax 请求：Axios
    4. 数据渲染：ElementUI、Echarts
    5. 前后端功能编写技术：

- ### 所用的软件与框架
<div align=center><img src="https://i.loli.net/2020/10/11/PXg6I3K17B2MZtT.jpg"/></div>



## FixSystem 的 API 文档
- 该校园智能保修系统采用前后端分离技术，需要通过 ajax 进行交互，具体的 api 文档可见 :point_right: [API 文档](https://gitee.com/alsritter/fix-system/blob/master/api%20%E6%96%87%E6%A1%A3.md)

- 部分 API 展示如下图：

    ![image.png](https://i.loli.net/2020/09/22/je7NW8h3fmC1iqF.png)    ![image.png](https://i.loli.net/2020/09/22/eOnKDRmIZ1TPoEg.png)

## FixSystem的 系统架构 👨🏻‍💻 
![image.png](https://i.loli.net/2020/09/19/KqawBVUztelTkh2.png)


## FixSystem 能做什么？
>FixSystem 能够处理学校保修问题，随时方便校内人员进行保修处理，校内出现需要维护的物品都可随时与工人联系处理问题。管理人员也可在管理端随时查看进度等消息。下面将介绍本系统的全部功能。

- ### 管理员端(web端)功能 👨‍💼
     - #### 个人中心
       1. 在此可查看管理员工号、用户名、手机号码、身份证、邮箱、职位等个人信息。
       2. 该列表可修改管理员的个人信息。
       3. 管理员个人信息的更改完成后，点击“提交”按钮既可保存。
       4. 点击"重置"既可重置管理员信息。
    ![personceter.png](https://i.loli.net/2020/10/13/58QqDv3AGz9aFl7.png)

    - #### 学生管理
       1. 管理员可在学生管理列表看到当前注册学生学号、性别、电话、姓名
       2. 若想单独查询某个学生的基本信息。可在三个输入框内分别输入学生的“学号”，“手机号”，“姓名”后，点击“搜索”即可查询。
    ![studentmanage.png](https://i.loli.net/2020/10/13/RFCldO2szJZjUL8.png)
     -  #### 工人管理
        1. 管理员可在工人管理列表看到当前注册工人工号、姓名、性别、电话、入职时间等个人信息。
        2. 点击“查”，可以查看工人的全部详细信息，包括历史评分、身份证、职位、本月订单详情等信息。
        3. 该列表还有三个输入框，分别输入工人的“工号”，“手机号”，“姓名”后，点击“搜索”即可查询某个学生的基本信息。
        4. 点击“取消”按钮，即可返回原本全部工人信息的列表。
    ![workermanage.png](https://i.loli.net/2020/10/13/C8bHiAYeoRc4Dmz.png)
     -  #### 添加工人
        1. 在该表单内填写工人全部信息，点击“提交”按钮即可提交新的工人信息。
        2. 还可在上传头像框内上传工人头像。
        3. 点击“重置”按钮重新填写信息。
    ![addworker.png](https://i.loli.net/2020/10/13/Om6EeVTLhCjtwfA.png)
     - #### 订单管理
          1. 可查看订单当前状态，有“待处理”，“进行中”等几种不同的状态。
          2. 在输入框根据提示输入“工号”，“学生学号”，“保修类型”，“联系人姓名”可对某个订单详细的报修信息进行搜索。
          3. 该页面有“单号”，“学生号”，“保修类型”，“联系电话等信息”。
          4. 还可根据用户自己的喜好对订单进行排序
          5. 不同状态的订单也会有不同的功能
          6. 点击 **选**，即出现选择工人的弹窗，可进行分配工人的操作。
          7. 点击 **删**，即可删除订单
          8. 点击 **详**，即可查看订单的详情
    ![orderlist.png](https://i.loli.net/2020/10/13/eJ2M1YB7PkA9mFL.png)
     - #### 耗材管理
       1. 在耗材管理这一页，可以对耗材进行增加，删除，修改操作
       2. 点击删除，即可删除耗材
       3. 点击添加新耗材，即可添加一个耗材，可以选择数量
       4. 点击计数器，即可修改耗材数量
    ![haocaimanage.png](https://i.loli.net/2020/10/13/Ar67yf13aLOYtx8.png)
     - #### Web端统计
       1. 在这一页可以看到本月的订单统计
     - #### 消息中心

       1. 在这一页可以看到管理员为工人发布的消息
       2. 点击发布公告 ，即可发布一则不超过200字的公告

     - #### 退出系统：

- ### 学生端功能 👨‍🎓

  

  - #### 学生端注册

       1. 对于第一次进系统的用户，我们提供了注册功能
       2. 点击注册即可完成注册

    ![studentloginunit.jpg](https://i.loli.net/2020/10/14/5yU4PlsbzQ3aegh.jpg)

  - #### 学生端修改个人资料

       1. 这里可以修改学生的个人信息，
       2. 点击提交即可完成修改 


    ![studentinfo.jpg](https://i.loli.net/2020/10/14/k9FyZ7eG2waRtfD.jpg)

  - #### 学生端发起订单

       1. 在这一页，学生可以对坏了的东西发起一个订单，以等待工人的维修
       2. 点击提交即可完成订单的发起

    ![studentfaqiunit.jpg](https://i.loli.net/2020/10/14/pgvxHLIDSk2zB5R.jpg)

  - #### 学生端当前订单

       1. 在这一页可以看到当前正在进行，和未处理的订单
       2. 点击查看详情，即可查看当前订单的状态，单号，工人等信息

    ![studentdangqiunit.jpg](https://i.loli.net/2020/10/14/27PtmzxQWqMUGby.jpg)

  - #### 学生端历史订单

       1. 在这一页可以看到已完成的订单
       2. 点击详情可以对未评价的订单进行评价


    ![studenthistoryunit.jpg](https://i.loli.net/2020/10/14/dS7vp8rHKP54FVQ.jpg)


- ### 工人端功能 👨‍🔧


  - #### 工人端个人中心
       1. 在这一页可以看到工人的个人信息，比如最近订单评价，本月评分，加入时间等信息


      ![workerunit.jpg](https://i.loli.net/2020/10/14/raQpyhlE9InXPZG.jpg)



  - #### 工人端订单列表

       1. 在这一页可以看到工人当前正在处理的订单信息
       2. 点击完成报修可以结束本订单，并对该订单进行一个结果描述


     ![workerdangqiunit.jpg](https://i.loli.net/2020/10/14/fLEV85w4gOWx2Hr.jpg)



  - #### 工人端历史订单

       1. 在这一页可以看到工人已完成的订单消息
       2. 可以看到评分报修类型和订单的ID等信息
       3. 点击查看详情，可以看到该订单的更多信息


    ![workerhistiryunit.jpg](https://i.loli.net/2020/10/14/lI5NQKoiefqXdsp.jpg)


  - #### 工人端消息中心

       1. 在这一页可以看到管理员为工人发布的消息


    ![workermessagecenter.jpg](https://i.loli.net/2020/10/14/AZ6tWy81irnjPqC.jpg)



##  FixSystem 快速入门 🚀

### 搭建运行环境
系统采用了 Docker 来部署服务，使用前需先在服务器部署 Docker

关于 Docker 的安装可参考 :point_right: ：[官方文档--Ubuntu](https://docs.docker.com/engine/install/ubuntu/) 

```shell
# 安装好 Docker 后再安装 docker-compose
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
