/*
Navicat MySQL Data Transfer

Source Server         : DB
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : demo

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2019-07-31 09:03:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for commodity_info
-- ----------------------------
DROP TABLE IF EXISTS `commodity_info`;
CREATE TABLE `commodity_info` (
  `commodity_id` varchar(32) NOT NULL COMMENT '商品id',
  `cc_id` varchar(32) DEFAULT NULL COMMENT '课程分类id',
  `course_name` varchar(20) DEFAULT NULL COMMENT '课程名称',
  `commodity_status` int(11) DEFAULT NULL COMMENT '商品状态（下架：0，上架：1）',
  `price` double DEFAULT NULL COMMENT '价格',
  `status` int(11) DEFAULT NULL COMMENT '删除状态（删除：0，未删除：1）',
  `validity` int(11) DEFAULT NULL COMMENT '有效期(-1为永久有效)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(20) DEFAULT NULL COMMENT '创建人',
  `introduce` varchar(500) DEFAULT NULL COMMENT '特色简介',
  `preferential` double DEFAULT NULL COMMENT '优惠价',
  `persion_num` int(11) DEFAULT NULL COMMENT '购买人数',
  `class_time` int(11) DEFAULT NULL COMMENT '课时',
  `open_class_time` date DEFAULT NULL COMMENT '开班时间',
  `lesson_type` int(11) DEFAULT NULL COMMENT '授课类型',
  `lecturer` varchar(20) DEFAULT NULL COMMENT '主讲老师',
  `audition_video_path` varchar(255) DEFAULT NULL COMMENT '试听视频',
  `picture_path` varchar(200) DEFAULT NULL COMMENT '课程封面',
  `contact_info` varchar(20) DEFAULT NULL COMMENT '咨询方式',
  `aoto_mail` int(11) DEFAULT NULL COMMENT '自动发送消息(不自动发送：0，自动发送：1)',
  `course_introduce` varchar(500) DEFAULT NULL COMMENT '课程简介',
  `course_id` varchar(32) DEFAULT NULL COMMENT '课程id',
  `course_type` int(11) DEFAULT NULL COMMENT '课程类型(单一课程：0，套餐：1)',
  PRIMARY KEY (`commodity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for course_category_info
-- ----------------------------
DROP TABLE IF EXISTS `course_category_info`;
CREATE TABLE `course_category_info` (
  `cc_id` varchar(32) NOT NULL COMMENT '课程分类id',
  `cc_name` varchar(20) DEFAULT NULL COMMENT '分类名称',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父分类id',
  `cc_status` int(11) DEFAULT NULL COMMENT '状态(删除：0，正常：1)',
  `cc_sort` int(11) DEFAULT NULL COMMENT '顺序',
  `cc_create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `cc_creator` varchar(10) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`cc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file` (
  `file_id` varchar(32) NOT NULL COMMENT '文件ID',
  `commodity_id` varchar(32) DEFAULT NULL COMMENT '商品ID',
  `file_name` varchar(50) DEFAULT NULL COMMENT '文件名',
  `file_path` varchar(200) DEFAULT NULL COMMENT '文件路径',
  `file_time` datetime DEFAULT NULL COMMENT '创建时间',
  `isdelete` int(11) DEFAULT NULL COMMENT '是否删除(删除：0，未删除：1)',
  `isvalidity` int(11) DEFAULT NULL COMMENT '是否有效(无效：0，有效：1)',
  `save_name` varchar(50) DEFAULT NULL COMMENT '存储文件名',
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for invoice_info
-- ----------------------------
DROP TABLE IF EXISTS `invoice_info`;
CREATE TABLE `invoice_info` (
  `id` int(11) NOT NULL COMMENT '订单id',
  `invoice_id` int(11) DEFAULT NULL COMMENT '发票id',
  `invoice_head` varchar(50) DEFAULT NULL COMMENT '发票头',
  `invoice_type` varchar(20) DEFAULT NULL COMMENT '发票类型',
  `address` varchar(50) DEFAULT NULL COMMENT '地址',
  `zip_code` int(11) DEFAULT NULL COMMENT '邮编',
  `taxpayer_no` varchar(50) DEFAULT NULL COMMENT '纳税人识别号',
  `register_add` varchar(50) DEFAULT NULL COMMENT '注册地址',
  `register_num` int(11) DEFAULT NULL COMMENT '注册电话',
  `open_bank` varchar(50) DEFAULT NULL COMMENT '开户行',
  `account` varchar(50) DEFAULT NULL COMMENT '公司账号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for logging_event
-- ----------------------------
DROP TABLE IF EXISTS `logging_event`;
CREATE TABLE `logging_event` (
  `timestmp` bigint(20) NOT NULL,
  `formatted_message` text NOT NULL,
  `logger_name` varchar(254) NOT NULL,
  `level_string` varchar(254) NOT NULL,
  `thread_name` varchar(254) DEFAULT NULL,
  `reference_flag` smallint(6) DEFAULT NULL,
  `arg0` varchar(254) DEFAULT NULL,
  `arg1` varchar(254) DEFAULT NULL,
  `arg2` varchar(254) DEFAULT NULL,
  `arg3` varchar(254) DEFAULT NULL,
  `caller_filename` varchar(254) NOT NULL,
  `caller_class` varchar(254) NOT NULL,
  `caller_method` varchar(254) NOT NULL,
  `caller_line` char(4) NOT NULL,
  `event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for logging_event_exception
-- ----------------------------
DROP TABLE IF EXISTS `logging_event_exception`;
CREATE TABLE `logging_event_exception` (
  `event_id` bigint(20) NOT NULL,
  `i` smallint(6) NOT NULL,
  `trace_line` varchar(254) NOT NULL,
  PRIMARY KEY (`event_id`,`i`),
  CONSTRAINT `logging_event_exception_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `logging_event` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for logging_event_property
-- ----------------------------
DROP TABLE IF EXISTS `logging_event_property`;
CREATE TABLE `logging_event_property` (
  `event_id` bigint(20) NOT NULL,
  `mapped_key` varchar(254) NOT NULL,
  `mapped_value` text,
  PRIMARY KEY (`event_id`,`mapped_key`),
  CONSTRAINT `logging_event_property_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `logging_event` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for meal_info
-- ----------------------------
DROP TABLE IF EXISTS `meal_info`;
CREATE TABLE `meal_info` (
  `id` int(11) NOT NULL COMMENT 'id',
  `com_commodity_id` int(11) DEFAULT NULL COMMENT '套餐id',
  `commodity_id` int(11) DEFAULT NULL COMMENT '商品id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `id` int(11) NOT NULL COMMENT '订单id',
  `username` varchar(20) DEFAULT NULL COMMENT '用户名',
  `create_time` date DEFAULT NULL COMMENT '创建日期',
  `org` varchar(50) DEFAULT NULL COMMENT '机构',
  `isbill` int(11) DEFAULT NULL COMMENT '是否发票（否：0，是：1）',
  `isdraw_bill` int(11) DEFAULT NULL COMMENT '是否已经开发票（否：0，是：1）',
  `order_no` varchar(20) DEFAULT NULL COMMENT '订单号',
  `payment_status` int(11) DEFAULT NULL COMMENT '支付状态(未支付：0，已支付：1)',
  `amount` double DEFAULT NULL COMMENT '总金额',
  `receiver` varchar(20) DEFAULT NULL COMMENT '收货人',
  `tel_num` int(11) DEFAULT NULL COMMENT '手机号',
  `pay_type` varchar(20) DEFAULT NULL COMMENT '支付方式',
  `commodity_id` int(11) DEFAULT NULL COMMENT '商品id',
  `cost_price` double DEFAULT NULL COMMENT '原价',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_log
-- ----------------------------
DROP TABLE IF EXISTS `system_log`;
CREATE TABLE `system_log` (
  `id` varchar(32) NOT NULL,
  `description` varchar(50) DEFAULT NULL COMMENT '日志信息描述',
  `method` varchar(100) DEFAULT NULL COMMENT '方法名称',
  `log_type` varchar(10) DEFAULT NULL COMMENT '日志类型 0是正常，1是异常',
  `request_ip` varchar(30) DEFAULT NULL COMMENT '请求的ip',
  `exception_code` varchar(50) DEFAULT NULL COMMENT '异常错误码',
  `exception_detail` varchar(255) DEFAULT NULL COMMENT '异常详情',
  `params` varchar(1000) DEFAULT NULL COMMENT '请求参数',
  `user_id` varchar(32) DEFAULT NULL COMMENT '请求的用户id',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统日志表';

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` varchar(32) NOT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
