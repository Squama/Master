/*
Navicat MySQL Data Transfer

Source Server         : cloud
Source Server Version : 50718
Source Host           : rm-uf6n7yzv07k4eyewv5o.mysql.rds.aliyuncs.com:3306
Source Database       : admineap

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2019-02-23 15:27:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_fixedassets_pur
-- ----------------------------
DROP TABLE IF EXISTS `tbl_fixedassets_pur`;
CREATE TABLE `tbl_fixedassets_pur` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `dept_id` varchar(36) DEFAULT NULL,
  `dept_name` varchar(255) DEFAULT NULL,
  `pur_no` varchar(255) DEFAULT NULL,
  `stock` varchar(255) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `purpose` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `file_id` varchar(36) DEFAULT NULL,
  `applyer` varchar(255) DEFAULT NULL,
  `fa_type` varchar(255) DEFAULT NULL COMMENT '10=固定资产\r\n20=器具、工具\r\n30=办公用品',
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tbl_fixedassets_pur_channel
-- ----------------------------
DROP TABLE IF EXISTS `tbl_fixedassets_pur_channel`;
CREATE TABLE `tbl_fixedassets_pur_channel` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `pur_tx_id` varchar(36) DEFAULT NULL,
  `channel_name` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tbl_fixedassets_pur_tx
-- ----------------------------
DROP TABLE IF EXISTS `tbl_fixedassets_pur_tx`;
CREATE TABLE `tbl_fixedassets_pur_tx` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `stk_id` varchar(36) DEFAULT NULL,
  `pur_id` varchar(36) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `english_name` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `norm` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `quantity` varchar(255) DEFAULT NULL,
  `vendor` varchar(255) DEFAULT NULL,
  `channel_id` varchar(255) DEFAULT NULL,
  `channel_name` varchar(255) DEFAULT NULL,
  `fa_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tbl_fixedassets_stk
-- ----------------------------
DROP TABLE IF EXISTS `tbl_fixedassets_stk`;
CREATE TABLE `tbl_fixedassets_stk` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `english_name` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `belonged_stock` varchar(255) DEFAULT NULL,
  `keeped_dept_id` varchar(36) DEFAULT NULL,
  `keeped_dept_name` varchar(255) DEFAULT NULL,
  `norm` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `quantity` varchar(255) DEFAULT NULL,
  `quantity_avl` varchar(255) DEFAULT NULL,
  `vendor` varchar(255) DEFAULT NULL,
  `fa_type` varchar(255) DEFAULT NULL COMMENT '10=固定资产\r\n20=器具、工具\r\n30=办公用品',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tbl_fixedassets_stk_tx
-- ----------------------------
DROP TABLE IF EXISTS `tbl_fixedassets_stk_tx`;
CREATE TABLE `tbl_fixedassets_stk_tx` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `fixed_assets_id` varchar(36) DEFAULT NULL,
  `operation` varchar(255) DEFAULT NULL COMMENT '10=入库\r\n20=领用',
  `amount` varchar(255) DEFAULT NULL,
  `balance` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `operator` varchar(255) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `source_tx_id` varchar(36) DEFAULT NULL,
  `need_return` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tbl_fixedassets_use
-- ----------------------------
DROP TABLE IF EXISTS `tbl_fixedassets_use`;
CREATE TABLE `tbl_fixedassets_use` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `stk_id` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `english_name` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `belonged_stock` varchar(255) DEFAULT NULL,
  `used_dept_id` varchar(36) DEFAULT NULL,
  `used_dept_name` varchar(255) DEFAULT NULL,
  `norm` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `quantity` varchar(255) DEFAULT NULL,
  `vendor` varchar(255) DEFAULT NULL,
  `fa_type` varchar(255) DEFAULT NULL COMMENT '10=固定资产\r\n20=器具、工具\r\n30=办公用品',
  `need_return` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL COMMENT '10=新建 20=审核通过 30=审核通过（需归还） 40=归还申请  50=已归还 60=审核不通过',
  `operator` varchar(255) DEFAULT NULL,
  `return_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
