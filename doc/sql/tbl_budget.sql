/*
Navicat MySQL Data Transfer

Source Server         : admineap
Source Server Version : 50714
Source Host           : localhost:3306
Source Database       : admineap

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2017-11-14 13:54:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_budget
-- ----------------------------
DROP TABLE IF EXISTS `tbl_budget`;
DROP TABLE IF EXISTS `tbl_budget_tx`;
DROP TABLE IF EXISTS `tbl_region`;

CREATE TABLE `tbl_budget` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `budget_no` varchar(64) NOT NULL,
  `budget_name` varchar(255) DEFAULT NULL,
  `project_id` varchar(64) DEFAULT NULL,
  `operator` varchar(64) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `checker` varchar(64) DEFAULT NULL,
  `check_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `supplier_editer` varchar(64) DEFAULT NULL,
  `edit_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `tbl_budget_tx` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `budget_no` varchar(64) NOT NULL,
  `project_id` varchar(64) DEFAULT NULL,
  `region_id` varchar(64) DEFAULT NULL,
  `mat_number` varchar(20) DEFAULT NULL,
  `mat_name` varchar(64) DEFAULT NULL,
  `mat_standard` varchar(50) DEFAULT NULL,
  `unit` varchar(50) DEFAULT NULL,
  `quantity` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `tbl_region` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `levelCode` varchar(36) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `parent_id` varchar(255) DEFAULT NULL,
  `remark` longtext,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
