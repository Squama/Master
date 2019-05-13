/*
Navicat MySQL Data Transfer

Source Server         : cloud
Source Server Version : 50718
Source Host           : rm-uf6n7yzv07k4eyewv5o.mysql.rds.aliyuncs.com:3306
Source Database       : admineap

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2019-05-13 18:05:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_social_security
-- ----------------------------
DROP TABLE IF EXISTS `tbl_social_security`;
CREATE TABLE `tbl_social_security` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `year` varchar(255) DEFAULT NULL,
  `radix` varchar(255) DEFAULT NULL,
  `avg` varchar(255) DEFAULT NULL,
  `yanglao` varchar(255) DEFAULT NULL,
  `yiliao` varchar(255) DEFAULT NULL,
  `shiye` varchar(255) DEFAULT NULL,
  `gongshang` varchar(255) DEFAULT NULL,
  `shengyu` varchar(255) DEFAULT NULL,
  `gongjijin` varchar(255) DEFAULT NULL,
  `prf` varchar(255) DEFAULT NULL,
  `start_time` varchar(255) DEFAULT NULL,
  `end_time` varchar(255) DEFAULT NULL,
  `yanglao_corp` varchar(255) DEFAULT NULL,
  `yiliao_corp` varchar(255) DEFAULT NULL,
  `shiye_corp` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_social_year` (`year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
