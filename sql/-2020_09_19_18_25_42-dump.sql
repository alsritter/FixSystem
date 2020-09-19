-- MySQL dump 10.13  Distrib 5.7.28, for Win64 (x86_64)
--
-- Host: 47.103.192.147    Database: my_fix_system
-- ------------------------------------------------------
-- Server version	5.7.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ADMIN_TB`
--

DROP TABLE IF EXISTS `ADMIN_TB`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ADMIN_TB` (
  `work_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工号 工号',
  `name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '姓名',
  `gender` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `password` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `join_date` datetime NOT NULL DEFAULT '2020-08-01 00:00:00' COMMENT '入职时间',
  `phone` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机号',
  `details` varchar(3072) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '详细说明',
  PRIMARY KEY (`work_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表 管理员的个人信息、手机号、个人介绍';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ADMIN_TB`
--

LOCK TABLES `ADMIN_TB` WRITE;
/*!40000 ALTER TABLE `ADMIN_TB` DISABLE KEYS */;
INSERT INTO `ADMIN_TB` VALUES ('201835070111','张三','男','1234','2020-08-01 00:00:00','1312888888','俺就是个小透明'),('201835070322','李四','男','1sss234','2020-08-01 00:00:00','13128834248','俺就是个小透明'),('201835070344','王五','女','12342121','2020-08-01 00:00:00','13128863337','俺就是个小透明'),('201835070999','钱七','男','123sss4','2020-08-01 00:00:00','1313218888','俺就是个小透明'),('201835072122','赵六','女','123dwd4','2020-08-01 00:00:00','1312887778','俺就是个小透明');
/*!40000 ALTER TABLE `ADMIN_TB` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FAULT_TB`
--

DROP TABLE IF EXISTS `FAULT_TB`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FAULT_TB` (
  `fault_class` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '故障类别 类型',
  PRIMARY KEY (`fault_class`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='故障类别 故障类别';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FAULT_TB`
--

LOCK TABLES `FAULT_TB` WRITE;
/*!40000 ALTER TABLE `FAULT_TB` DISABLE KEYS */;
INSERT INTO `FAULT_TB` VALUES ('一卡通报修'),('中央空调'),('其它'),('洗衣机'),('空调'),('网络问题');
/*!40000 ALTER TABLE `FAULT_TB` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Massage_TB`
--

DROP TABLE IF EXISTS `Massage_TB`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Massage_TB` (
  `massage_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '消息 id 自增',
  `create_date` datetime NOT NULL DEFAULT '2020-08-01 00:00:00' COMMENT '发布消息的时间',
  `work_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工号 发布者的工号',
  `massage` varchar(3072) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息内容',
  PRIMARY KEY (`massage_id`),
  KEY `work_id` (`work_id`),
  CONSTRAINT `Massage_TB_ibfk_1` FOREIGN KEY (`work_id`) REFERENCES `ADMIN_TB` (`work_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表 记录发布的消息数据';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Massage_TB`
--

LOCK TABLES `Massage_TB` WRITE;
/*!40000 ALTER TABLE `Massage_TB` DISABLE KEYS */;
INSERT INTO `Massage_TB` VALUES (1,'2020-08-01 00:00:00','201835070111','寻寻觅觅，冷冷清清，凄凄惨惨戚戚。乍暖还寒时候，最难将息。三杯两盏淡酒，怎敌他、晚来风急！雁过也，正伤心，却是旧时相识。\r\n满地黄花堆积，憔悴损，如今有谁堪摘？守着窗儿，独自怎生得黑！梧桐更兼细雨，到黄昏、点点滴滴。这次第，怎一个愁字了得！'),(2,'2020-08-01 00:00:00','201835072122','昨夜雨疏风骤，浓睡不消残酒。试问卷帘人，却道海棠依旧。知否，知否，应是绿肥红瘦。'),(3,'2020-08-01 00:00:00','201835070344','红藕香残玉簟秋。轻解罗裳，独上兰舟。云中谁寄锦书来，雁字回时，月满西楼。\r\n花自飘零水自流。一种相思，两处闲愁。此情无计可消除，才下眉头，却上心头。'),(4,'2020-08-01 00:00:00','201835070111','暖日晴风初破冻，柳眼梅腮，已觉春心动。酒意诗情谁与共，泪融残粉花钿重。\r\n乍试夹衫金缕缝，山枕斜欹，枕损钗头凤。独抱浓愁无好梦，夜阑犹翦灯花弄。'),(5,'2020-08-01 00:00:00','201835070322','昨夜雨疏风骤，浓睡不消残酒。试问卷帘人，却道海棠依旧。知否，知否，应是绿肥红瘦。'),(6,'2020-09-16 15:31:47','201835070344','OK兄弟们全体目光向我看齐嗷'),(7,'2020-09-16 15:32:23','201835070344','我宣布个事儿'),(8,'2020-09-16 15:33:38','201835070344','我是个撒比'),(9,'2020-09-16 15:34:34','201835070344','OK兄弟们全体目光向我看齐嗷'),(10,'2020-09-16 15:35:29','201835070344','我是个伞兵'),(11,'2020-09-16 15:36:08','201835070344','OK兄弟们全体目光向我看齐嗷'),(12,'2020-09-16 15:38:42','201835070344','OK兄弟们全体目光向我看齐嗷'),(13,'2020-09-16 15:38:50','201835070344','OK兄弟们全体目光向我看齐嗷'),(14,'2020-09-16 15:43:12','201835070344','OK兄弟们全体目光向我看齐嗷嗷嗷嗷'),(15,'2020-09-16 15:47:01','201835070344','OK兄弟们全体目光向我看齐嗷'),(16,'2020-09-16 15:48:52','201835070344','OK兄弟们全体目光向我看齐嗷'),(17,'2020-09-16 15:48:59','201835070344','OK兄弟们全体目光向我看齐嗷'),(18,'2020-09-16 15:52:15','201835070344','OK兄弟们全体目光向我看齐嗷'),(19,'2020-09-17 22:40:56','201835070344','\n到百度首页百度首页前后轮驱动\n规范：用户交互设计易用性原则\n\n人人都是产品经理\n\n发布时间：09-1716:20深圳聚力创想信息科技有限公司\n编辑导语：我们在使用各种软件时，会遇到一些弹出通知或者按钮的设计，比如在网页上，有些按钮有立体感，点击时仿佛被按就进去了等等一些设计；本文是作者总结的一篇用户交互设计易用性原则，我们一起来看一下。\n\n\n很久以前，曾经看过类似的一篇文章，基于该文章总结了这么一篇《用户交互设计原则》的文档给团队做原型的同学看；虽然偏理论，但是在指导意义上讲，还是有一定的可用性的，发出来给大家分享一下。\n\n一、系统状态的能见度\n\n软件应该保持相同的模式，透过适当的反应，在合理的时间内通知使用者，让使用者了解正在发生的事情。\n\n从交互设计角度讲，通常通过以下两种方式提高系统状态能见度：\n\n1. 按钮状态\n\n在计算机上浏览页面时，常常发现某些地方光标移过去时颜色会有所改变，提示用户这里可以点击。\n\n触摸屏设备或移动设备上没有光标，所以用以更加浅显易懂的方式告诉用户这里可以被操作；比如更为立体的按钮或者仿真实体的开关图像，用户在点击按钮后，会出现按钮被按下去的凹陷感，提示用户您已经点击到了。\n\n2. 进度条/操作进度展现\n\n该方式通常提示用户您需要等一下，现在正在[读取中]、[下载中]…。\n\n在需要用户等待时，如果没有任何提示，用户就会产生“自从点击了这个按钮后，画面就不动了，我的操作到底有没有被执行呢？不知道要等多久…”等等，会给用户带来很强烈的挫败感和结果不可控感。\n\n进度条/操作进度展现就是为了降低使用者的不耐感，高速用户现在在做什么样的处理、进度如何、大约需要多\n本质嬗变的新零售正在开启新方向\n\n项目管理三大基准（一）：范围基准\n\n产品新人必须掌握的业务分析思维方法论\n\n设为首页© Baidu 使用百度前必读 意见反馈 京ICP证030173号 \n京公网安备11000002000001号\n\n'),(20,'2020-09-18 20:51:09','201835070344','到百度首页百度首页前后轮驱动 规范：用户交互设计易用性原则 人人都是产品经理 发布时间：09-1716:20深圳聚力创想信息科技有限公司 编辑导语：我们在使用各种软件时，会遇到一些弹出通知或者按钮的设计，比如在网页上，有些按钮有立体感，点击时仿佛被按就进去了等等一些设计；本文是作者总结的一篇用户交互设计易用性原则，我们一起来看一下。 很久以前，曾经看过类似的一篇文章，基于该文章总结了这么一篇《用户交互设计原则》的文档给团队做原型的同学看；虽然偏理论，但是在指导意义上讲，还是有一定的可用性的，发出来给大家分享一下。 一、系统状态的能见度 软件应该保持相同的模式，透过适当的反应，在合理的时间内通知使用者，让使用者了解正在发生的事情。 从交互设计角度讲，通常通过以下两种方式提高系统状态能见度： 1. 按钮状态 在计算机上浏览页面时，常常发现某些地方光标移过去时颜色会有所改变，提示用户这里可以点击。 触摸屏设备或移动设备上没有光标，所以用以更加浅显易懂的方式告诉用户这里可以被操作；比如更为立体的按钮或者仿真实体的开关图像，用户在点击按钮后，会出现按钮被按下去的凹陷感，提示用户您已经点击到了。 2. 进度条/操作进度展现 该方式通常提示用户您需要等一下，现在正在[读取中]、[下载中]…。 在需要用户等待时，如果没有任何提示，用户就会产生“自从点击了这个按钮后，画面就不动了，我的操作到底有没有被执行呢？不知道要等多久…”等等，会给用户带来很强烈的挫败感和结果不可控感。 进度条/操作进度展现就是为了降低使用者的不耐感，高速用户现在在做什么样的处理、进度如何、大约需要多 本质嬗变的新零售正在开启新方向 项目管理三大基准（一）：范围基准 产品新'),(21,'2020-09-18 20:52:05','201835070344','到百度首页百度首页前后轮驱动 规范：用户交互设计易用性原则 人人都是产品经理 发布时间：09-1716:20深圳聚力创想信息科技有限公司 编辑导语：我们在使用各种软件时，会遇到一些弹出通知或者按钮的设计，比如在网页上，有些按钮有立体感，点击时仿佛被按就进去了等等一些设计；本文是作者总结的一篇用户交互设计易用性原则，我们一起来看一下。 很久以前，曾经看过类似的一篇文章，基于该文章总结了这么一篇《用户交互设计原则》的文档给团队做原型的同学看；虽然偏理论，但是在指导意义上讲，还是有一定的可用性的，发出来给大家分享一下。 一、系统状态的能见度 软件应该保持相同的模式，透过适当的反应，在合理的时间内通知使用者，让使用者了解正在发生的事情。 从交互设计角度讲，通常通过以下两种方式提高系统状态能见度： 1. 按钮状态 在计算机上浏览页面时，常常发现某些地方光标移过去时颜色会有所改变，提示用户这里可以点击。 触摸屏设备或移动设备上没有光标，所以用以更加浅显易懂的方式告诉用户这里可以被操作；比如更为立体的按钮或者仿真实体的开关图像，用户在点击按钮后，会出现按钮被按下去的凹陷感，提示用户您已经点击到了。 2. 进度条/操作进度展现 该方式通常提示用户您需要等一下，现在正在[读取中]、[下载中]…。 在需要用户等待时，如果没有任何提示，用户就会产生“自从点击了这个按钮后，画面就不动了，我的操作到底有没有被执行呢？不知道要等多久…”等等，会给用户带来很强烈的挫败感和结果不可控感。 进度条/操作进度展现就是为了降低使用者的不耐感，高速用户现在在做什么样的处理、进度如何、大约需要多 本质嬗变的新零售正在开启新方向 项目管理三大基准（一）：范围基准 产品新'),(22,'2020-09-18 20:52:28','201835070344','夜に駆ける'),(23,'2020-09-18 20:53:09','201835070344','初めて会った日から\n从相遇的那天开始\n僕の心の全てを奪った\n我的内心全部都被你夺去\nどこか儚い空気を纏う君は\n缠绕着飘渺氤氲的你\n寂しい目をしてたんだ\n眼神却显得如此寂寞\nいつだってチックタックと\n在这时针不停转动的世界里\n鳴る世界で何度だってさ\n无论多少次\n触れる心無い言葉うるさい声に\n去触碰那残酷之言刺耳之声\n涙が零れそうでも\n都会泫然欲泣\nありきたりな喜びきっと二人なら見つけられる\n但若是两个人的话 一定能寻找到属于我们的平凡的喜悦\n騒がしい日々に笑えない君に\n为在喧嚣的日子里失去笑容的你\n思い付く限り眩しい明日を\n献上所能想象到的一切耀眼的明天');
/*!40000 ALTER TABLE `Massage_TB` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ORDERS_TB`
--

DROP TABLE IF EXISTS `ORDERS_TB`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ORDERS_TB` (
  `fix_table_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '维修表单id 自增的表单id',
  `student_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '申报人id 学生的id',
  `contacts` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系人名字',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `end_time` datetime DEFAULT NULL COMMENT '完成时间',
  `address` varchar(3072) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '所在宿舍',
  `phone` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '联系电话 优先联系这个手机号，找不到再联系学生id上的手机号',
  `fault_class` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '故障类别',
  `fault_detail` varchar(3072) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '故障描述',
  `photo_url` varchar(3072) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片的链接，当有多个图片时使用空格分隔',
  `work_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '维修工id 这个可以先空着，管理员来指定这个师傅的id',
  `admin_work_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '管理员id 哪个管理员处理的就填上哪个管理员的id',
  `grade` int(11) NOT NULL DEFAULT '10' COMMENT '评分 这个评分只是当前订单的评分，师傅的评分计算方式是师傅当前评分加上这个评分除以总订单数，得到的平均值就是师傅评分,默认是10分',
  `massage` varchar(3072) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '学生留言',
  `result_details` varchar(3072) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '处理结果',
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '当前订单状态 展示到界面就是通过订单的状态来区分是否为新订单还是待完成：未处理、待完成、完成，使用数字来代表状态比比较字符串更高效，所以对应数字  0、1、2',
  PRIMARY KEY (`fix_table_id`),
  KEY `student_id` (`student_id`),
  KEY `fault_class` (`fault_class`),
  KEY `work_id` (`work_id`),
  KEY `admin_work_id` (`admin_work_id`),
  CONSTRAINT `ORDERS_TB_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `STUDENT_TB` (`student_id`),
  CONSTRAINT `ORDERS_TB_ibfk_2` FOREIGN KEY (`fault_class`) REFERENCES `FAULT_TB` (`fault_class`),
  CONSTRAINT `ORDERS_TB_ibfk_3` FOREIGN KEY (`work_id`) REFERENCES `WORKER_TB` (`WORK_ID`),
  CONSTRAINT `ORDERS_TB_ibfk_4` FOREIGN KEY (`admin_work_id`) REFERENCES `ADMIN_TB` (`work_id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表 里面显示的学生提交的订单，然后加上学生评分，学生评价，订单状态等信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ORDERS_TB`
--

LOCK TABLES `ORDERS_TB` WRITE;
/*!40000 ALTER TABLE `ORDERS_TB` DISABLE KEYS */;
INSERT INTO `ORDERS_TB` VALUES (9,'201825070131','小美','2020-08-03 19:46:38','2020-09-01 19:46:38','15 栋 611','13128877777','中央空调','空调坏了',NULL,'201825070103','201835070111',5,'师傅动手动脚','很好，没啥大问题',2),(10,'201825070132','李四','2020-08-01 19:46:38','2020-09-02 19:46:38','15 栋 612','13128888888','一卡通报修','sdjakhkajsdhkjasd',NULL,'201825070103','201835070322',5,'sdagshjkagdhjagjsd','dhjashjgdjahsgdhjas',2),(11,'201825070133','王五','2020-08-11 19:46:38','2020-09-15 19:46:38','15 栋 614','13128866666','中央空调','飒飒大苏打盛大的是',NULL,'201895070344','201835070344',5,'dhjagsjdhgajshdgad','dahjksgdhjiagsdhjgahjsdghjasdgj',2),(12,'201825070134','二狗','2020-08-15 19:46:38','2020-09-06 19:46:43','15 栋 611','13100038888','网络问题','给的黄金卡根据考生打火机是德国',NULL,'201895070999','201835070999',5,'jyhiuyiuyiuyiuyiuyi','hsdhahsahdhasdhahsdahdhasdh',2),(31,'201825070132','小美','2020-08-01 19:46:38','2020-09-19 13:47:33','15 栋 611','13128888888','空调','给的黄金卡根据考生打火机是德国',NULL,'201825070103','201835070344',10,NULL,'当前订单未评价',2),(43,'201825070133','小美','2020-08-17 19:46:38',NULL,'15 栋 611','13128866666','一卡通报修','飒飒大苏打盛大的是',NULL,NULL,NULL,10,NULL,NULL,0),(44,'201825070134','李四','2020-08-17 19:46:38',NULL,'15 栋 611','13128855555','中央空调','给的黄金卡根据考生打火机是德国',NULL,NULL,NULL,10,NULL,NULL,0),(45,'201825070135','王五','2020-08-23 12:46:38',NULL,'15 栋 611','13128899999','其它','空调坏了',NULL,NULL,NULL,10,NULL,NULL,0),(50,'201825070129','张三','2020-09-15 00:18:36',NULL,'18 栋 401','13128863333','空调','学生端测试',NULL,NULL,NULL,10,NULL,NULL,0),(51,'201825070129','爱德华','2020-09-15 00:19:52',NULL,'幻想乡','13128863395','中央空调','学生端测试',NULL,NULL,NULL,10,NULL,NULL,0),(54,'201825070129','玛莉亚','2020-09-15 00:18:36','2020-09-16 21:44:45','18 栋 401','13128863333','空调','学生端测试',NULL,'201825070103','201835070999',10,'你也许会觉得拥有许许多多东西很棒，但他永远也比不上将珍贵的礼物送给你真正关心的小马，我学到赠予比接受更好，而善良和慷慨能带来真正的友谊，这比世上任何东西都珍贵。','半随流水半随尘，断送韶光恼杀人。\n蜂蝶有情悲寂寞，园林无处觅精神。\n眼前红雨飘零尽，阶下苍苔点缀新。\n分付家童莫轻扫，醉眠还胜锦为茵。',2),(55,'201825070129','悦安','2020-09-15 00:19:52',NULL,'幻想乡','13128863395','其它','学生端测试',NULL,NULL,NULL,10,NULL,NULL,0),(56,'201825070129','小刚','2020-08-01 19:46:38','2020-09-08 19:46:43','15 栋 611','13128888888','中央空调','sdjakhkajsdhkjasd',NULL,'201825070103','201835070344',7,'我觉得 8 行','sdadasd',2),(57,'201825070129','拉瑞安','2020-08-01 19:46:38','2020-09-12 19:46:43','15 栋 611','13128888888','中央空调','sdjakhkajsdhkjasd',NULL,'201825070103','201835070344',2,'垃圾，我奶奶都修的都比这好','sdadasd',2),(58,'201825070129','泰勒','2020-08-01 19:46:38','2020-09-19 19:46:43','15 栋 611','13128888888','一卡通报修','sdjakhkajsdhkjasd',NULL,'201825070103','201835070344',5,'古庙依青嶂，行宫枕碧流。\n水声山色锁妆楼。往事思悠悠。\n\n云雨朝还暮，烟花春复秋。\n啼猿何必近孤舟。行客自多愁。','我们四年级说听不见就是听不见',2),(59,'201825070129','弗蒂冈','2020-08-01 19:46:38','2020-09-28 19:46:43','15 栋 611','13128888888','中央空调','sdjakhkajsdhkjasd',NULL,'201825070103','201835070344',4,'寻寻觅觅，冷冷清清，凄凄惨惨戚戚。乍暖还寒时候，最难将息。三杯两盏淡酒，怎敌他、晚来风急！雁过也，正伤心，却是旧时相识。\n满地黄花堆积，憔悴损，如今有谁堪摘？守着窗儿，独自怎生得黑！梧桐更兼细雨，到黄昏、点点滴滴。这次第，怎一个愁字了得！','sdadasd',2),(60,'201825070129','泰恩','2020-08-01 19:46:38','2020-09-01 19:46:43','15 栋 611','13128888888','网络问题','sdjakhkajsdhkjasd',NULL,'201825070103','201835070344',5,'当前订单未评价','没啥大问题',2),(61,'201825070129','银狐','2020-08-01 19:46:38','2020-07-06 19:46:43','15 栋 611','13128888888','中央空调','sdjakhkajsdhkjasd',NULL,'201825070103','201835070344',5,'当前订单未评价','声音那么小怎么上战舰',2),(62,'201825070129','小刚','2020-08-01 19:46:38','2020-09-18 19:46:43','15 栋 611','13128888888','一卡通报修','sdjakhkajsdhkjasd',NULL,'201825070103','201835070344',5,'sdadasd','sdadasd',2),(63,'201825070129','李白','2020-09-15 22:32:00',NULL,'唐朝','13128863333','其它','纤云弄巧，飞星传恨，银汉迢迢暗度。金风玉露一相逢，便胜却人间无数。\n柔情似水，佳期如梦，忍顾鹊桥归路！两情若是久长时，又岂在朝朝暮暮。',NULL,NULL,NULL,10,NULL,NULL,0),(64,'201825070129','弗蒂冈','2020-09-15 00:18:36','2020-09-16 19:53:33','半条命2','13128863333','网络问题','来自外星友人的问号：草您?',NULL,'201825070103','201835070999',2,'整那么文艺干嘛？？？','赤日行空暑气浮，炎风簸土几时休。\n云霓久渴斯民望，廊庙当分圣主忧。\n旱魃剿除消沴气，神龙鼓舞起灵湫。\n挽将天上银河水，散作甘霖润九州。',2),(65,'201825070129','于谦','2017-09-15 00:18:36','2020-09-17 02:08:33','中国明朝','13128863333','其它','昨日花开树头红，今日花落树头空。\r\n花开花落寻常事，未必皆因一夜风。\r\n人生行乐须少年，老去看花亦可怜。\r\n典衣沽酒花前饮，醉扫落花铺地眠。\r\n风吹花落依芳草，翠点胭脂颜色好。\r\n韶光有限蝶空忙，岁月无情人自老。\r\n眼看春尽为花愁，可惜朱颜变白头。\r\n莫遣花飞江上去，残红易逐水东流。',NULL,'201825070103','201835070999',2,'刚才忘记评分了，这里给您补一个','诗写的不错\n',2),(66,'201825070129','张三','2020-09-16 21:39:31',NULL,'18 栋 401','13128863333','中央空调','jkgyihug',NULL,NULL,NULL,10,NULL,NULL,0),(67,'201825070129','张三','2020-09-16 21:41:40','2020-09-19 15:40:45','18 栋 401','13128863333','其它','gggggggmmmmmmm',NULL,'201825070103','201835070344',10,'师傅说话很大声！','好！很有精神！',2),(68,'201825070129','梁洛滔','2020-09-17 22:33:22','2020-09-17 22:34:25','美利坚','13128863333','洗衣机','_(:3」∠❀)__(:3」∠❀)__(:3」∠❀)__(:3」∠❀)_\n',NULL,'201825070103','201835070344',10,'傻逼维修工服务态度这么垃圾！','傻逼吧，没事找事',2),(69,'201825070129','张三','2020-09-19 13:48:11',NULL,'18 栋 401','13128863333','洗衣机','',NULL,NULL,NULL,10,NULL,NULL,0),(70,'201825070129','张三','2020-09-19 13:48:17',NULL,'18 栋 401','13128863333','洗衣机','',NULL,NULL,NULL,10,NULL,NULL,0),(71,'201825070129','啵啵啵吧','2020-09-19 13:54:51',NULL,'18 栋 401','13128863333','一卡通报修','校园卡坏了',NULL,NULL,NULL,10,NULL,NULL,0);
/*!40000 ALTER TABLE `ORDERS_TB` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `STUDENT_TB`
--

DROP TABLE IF EXISTS `STUDENT_TB`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `STUDENT_TB` (
  `student_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学号 学号',
  `name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '姓名',
  `gender` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `password` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `phone` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机号',
  PRIMARY KEY (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生信息表 学生的个人信息、手机号、个人介绍';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `STUDENT_TB`
--

LOCK TABLES `STUDENT_TB` WRITE;
/*!40000 ALTER TABLE `STUDENT_TB` DISABLE KEYS */;
INSERT INTO `STUDENT_TB` VALUES ('201825070129','alsritter','女','1234567890','13128866666'),('201825070130','撒大大','男','1234567890','13128800000'),('201825070131','小美','女','adsasd567890','13128822222'),('201825070132','小明','男','sada7890','13128855555'),('201825070133','小紫','女','1234sasda','13128899999'),('201825070134','小刚','男','sasdasd0','13128877777'),('201825070135','小红','女','afasfa890','13128888888'),('201825070140','张三','男','1234567890','13128866666'),('20182507014011','张二狗','男','1234567890','13128866699');
/*!40000 ALTER TABLE `STUDENT_TB` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TOOL_TB`
--

DROP TABLE IF EXISTS `TOOL_TB`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TOOL_TB` (
  `tool_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '工具id',
  `tool_name` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工具名称',
  `tool_count` int(11) NOT NULL DEFAULT '0' COMMENT '工具数量',
  PRIMARY KEY (`tool_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT=' ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TOOL_TB`
--

LOCK TABLES `TOOL_TB` WRITE;
/*!40000 ALTER TABLE `TOOL_TB` DISABLE KEYS */;
INSERT INTO `TOOL_TB` VALUES (1,'网线',3),(3,'焊接器',6),(5,'螺丝刀',17),(13,' 锤子',97),(18,' 锤子',96),(19,' 锤子',97),(21,' 锤子',21),(22,' 剪刀',3),(23,' 胶布',22),(24,' 锤子',99),(25,' 剪刀',3),(26,' 胶布',111),(27,' 电线',32),(28,' 锤子',99),(29,' 锤子',99),(30,' 剪刀',3),(31,' 胶布',1),(32,' 电线',32),(33,' 剪刀',3),(34,' 胶布',182),(35,' 电线',32),(36,'口罩',123),(37,'AK47',47);
/*!40000 ALTER TABLE `TOOL_TB` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `WORKER_TB`
--

DROP TABLE IF EXISTS `WORKER_TB`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WORKER_TB` (
  `WORK_ID` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工号 工号',
  `NAME` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '姓名',
  `GENDER` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `PASSWORD` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `JOIN_DATE` datetime NOT NULL DEFAULT '2020-08-01 00:00:00' COMMENT '入职时间',
  `PHONE` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机号',
  `DETAILS` varchar(3072) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '详细说明',
  `ORDERS_NUMBER` int(11) DEFAULT NULL COMMENT '接单数目',
  `AVG_GRADE` decimal(4,2) NOT NULL COMMENT '评分 用户评分，方便做满意度，满分10分',
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '工人当前状态 0表示空闲、1表示正在工作',
  PRIMARY KEY (`WORK_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='维修工人表 维修工的个人信息、接单的数目、个人评分等';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `WORKER_TB`
--

LOCK TABLES `WORKER_TB` WRITE;
/*!40000 ALTER TABLE `WORKER_TB` DISABLE KEYS */;
INSERT INTO `WORKER_TB` VALUES ('201825070103','陈俊龙','男','1234567','2020-09-14 22:03:02','18126170804','昨日花开树头红，今日花落树头空。\r\n花开花落寻常事，未必皆因一夜风。\r\n人生行乐须少年，老去看花亦可怜。\r\n典衣沽酒花前饮，醉扫落花铺地眠。\r\n风吹花落依芳草，翠点胭脂颜色好。\r\n韶光有限蝶空忙，岁月无情人自老。\r\n眼看春尽为花愁，可惜朱颜变白头。\r\n莫遣花飞江上去，残红易逐水东流。',5,6.75,0),('2018250705641','陈俊龙2号','女','1234355','2020-09-14 22:14:02','13128863335','可可爱爱龙宝',0,10.00,0),('2018250705644','冯谭雅2号','女','12344','2020-09-14 19:47:08','13128863333','一名来自德意志的军官',0,10.00,0),('2018250705646','冯谭雅','女','1234','2020-09-14 10:43:32','13128863336','一名来自德意志的军官',0,10.00,0),('201825070666','九条可怜','女','1234456','2020-09-18 16:11:41','13128834982','普普通通上班族',0,10.00,0),('20182507089786','peterpan4754','男','1234353','2020-09-14 22:45:52','13128890811','peterpan',0,10.00,0),('201895070111','张三丰','男','1232114','2020-08-01 00:00:00','1312888888','俺就是个小透明',0,10.00,0),('201895070322','李四丰','男','1ss211s234','2020-08-01 00:00:00','13128834248','俺就是个小透明',0,10.00,0),('201895070344','王五丰','女','12321142121','2020-08-01 00:00:00','1312821388','俺就是个小透明',0,10.00,0),('201895070999','钱七丰','男','123211sss4','2020-08-01 00:00:00','1313218888','俺就是个小透明',0,10.00,0),('201895072122','赵六丰','女','123211dwd4','2020-08-01 00:00:00','1312887778','俺就是个小透明',0,10.00,0);
/*!40000 ALTER TABLE `WORKER_TB` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-09-19 18:25:50
