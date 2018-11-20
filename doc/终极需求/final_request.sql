/*
Navicat MySQL Data Transfer

Source Server         : cloud
Source Server Version : 50718
Source Host           : rm-uf6n7yzv07k4eyewv5o.mysql.rds.aliyuncs.com:3306
Source Database       : admineap

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2018-11-20 13:41:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_fee
-- ----------------------------
DROP TABLE IF EXISTS `tbl_fee`;
CREATE TABLE `tbl_fee` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `project_id` varchar(36) DEFAULT NULL,
  `project_name` varchar(255) DEFAULT NULL,
  `project_sub_id` varchar(36) DEFAULT NULL,
  `project_sub_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `amount` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL COMMENT '10=新建\r\n20=审核中\r\n30=审核通过',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tbl_fee_detail
-- ----------------------------
DROP TABLE IF EXISTS `tbl_fee_detail`;
CREATE TABLE `tbl_fee_detail` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `fee_id` varchar(36) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tbl_measure
-- ----------------------------
DROP TABLE IF EXISTS `tbl_measure`;
CREATE TABLE `tbl_measure` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `project_id` varchar(36) DEFAULT NULL,
  `project_name` varchar(200) DEFAULT NULL,
  `project_sub_id` varchar(36) DEFAULT NULL,
  `project_sub_name` varchar(200) DEFAULT NULL,
  `construct` varchar(255) DEFAULT NULL,
  `issue` varchar(255) DEFAULT NULL,
  `manage` varchar(255) DEFAULT NULL,
  `rule` varchar(255) DEFAULT NULL,
  `tax` varchar(255) DEFAULT NULL,
  `status` varchar(2) DEFAULT NULL COMMENT '10=新建\r\n20=审核中\r\n30=审核通过',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tbl_measure_consume
-- ----------------------------
DROP TABLE IF EXISTS `tbl_measure_consume`;
CREATE TABLE `tbl_measure_consume` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `project_id` varchar(36) DEFAULT NULL,
  `project_sub_id` varchar(36) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `amount` varchar(255) DEFAULT NULL,
  `op` varchar(255) DEFAULT NULL,
  `consume_name` varchar(255) DEFAULT NULL,
  `operator` varchar(255) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tbl_measure_volume
-- ----------------------------
DROP TABLE IF EXISTS `tbl_measure_volume`;
CREATE TABLE `tbl_measure_volume` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `project_id` varchar(32) DEFAULT NULL,
  `project_name` varchar(255) DEFAULT NULL,
  `team_id` varchar(36) DEFAULT NULL,
  `team_name` varchar(255) DEFAULT NULL,
  `measure_type` varchar(255) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `end_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `labor_id` varchar(36) DEFAULT NULL,
  `project_sub_id` varchar(36) DEFAULT NULL,
  `package_detail_id` varchar(36) DEFAULT NULL,
  `volume` varchar(10240) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL COMMENT '10-提交审核\r\n20-综合审核\r\n30-施工负责人\r\n40-预算负责人\r\n50-总负责人\r\n60-财务负责人\r\n70-已完成',
  `apply_mech` varchar(255) DEFAULT NULL,
  `apply_labour` varchar(255) DEFAULT NULL,
  `apply_mat` varchar(255) DEFAULT NULL,
  `apply_debit` varchar(255) DEFAULT NULL,
  `apply_sub` varchar(255) DEFAULT NULL,
  `apply_pack` varchar(255) DEFAULT NULL,
  `business_mech` varchar(255) DEFAULT NULL,
  `business_labour` varchar(255) DEFAULT NULL,
  `business_mat` varchar(255) DEFAULT NULL,
  `business_debit` varchar(255) DEFAULT NULL,
  `business_pack` varchar(255) DEFAULT NULL,
  `business_sub` varchar(255) DEFAULT NULL,
  `final_mech` varchar(255) DEFAULT NULL,
  `final_labour` varchar(255) DEFAULT NULL,
  `final_mat` varchar(255) DEFAULT NULL,
  `final_debit` varchar(255) DEFAULT NULL,
  `final_pack` varchar(255) DEFAULT NULL,
  `final_sub` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tbl_tax
-- ----------------------------
DROP TABLE IF EXISTS `tbl_tax`;
CREATE TABLE `tbl_tax` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `project_id` varchar(36) DEFAULT NULL,
  `project_name` varchar(255) DEFAULT NULL,
  `project_sub_id` varchar(36) DEFAULT NULL,
  `project_sub_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `amount` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL COMMENT '10=新建\r\n20=审核中\r\n30=审核通过',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
