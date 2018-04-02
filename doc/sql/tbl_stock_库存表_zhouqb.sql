/*
Navicat MySQL Data Transfer

Source Server         : Mysql
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : admineap

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-11-15 21:28:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_stock
-- ----------------------------
DROP TABLE IF EXISTS `tbl_stock`;
CREATE TABLE `tbl_stock` (
  `ID` varchar(64) NOT NULL COMMENT '库存ID',
  `project_ID` varchar(64) NOT NULL COMMENT '所属项目ID',
  `mat_ID` varchar(64) NOT NULL COMMENT '物料编号',
  `stock_Num` int(8) DEFAULT NULL COMMENT '库存量',
  `storage_person_ID` varchar(64) DEFAULT NULL COMMENT '入库人ID',
  `storage_time` datetime DEFAULT NULL COMMENT '入库时间',
  `useType` varchar(64) DEFAULT NULL COMMENT '使用类型（1：入库，2：出库，3：调度）',
  `reserve1` varchar(255) DEFAULT NULL,
  `reserve2` varchar(255) DEFAULT NULL,
  `reserve3` varchar(255) DEFAULT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tbl_stock_history
-- ----------------------------
DROP TABLE IF EXISTS `tbl_stock_history`;
CREATE TABLE `tbl_stock_history` (
  `ID` varchar(64) NOT NULL COMMENT '库存记录ID',
  `project_ID` varchar(64) NOT NULL COMMENT '所属项目ID',
  `budget_ID` varchar(64) DEFAULT NULL,
  `mat_ID` varchar(64) NOT NULL COMMENT '物料ID',
  `stock_change_Num` int(8) DEFAULT NULL COMMENT '操作数量',
  `stock_Source` varchar(64) DEFAULT NULL COMMENT '库存来源',
  `useTpye` varchar(64) DEFAULT NULL COMMENT '操作类型（1：采购入库，2：消耗出库，3：调度入库，4：调度出库）',
  `operation_person_ID` varchar(64) DEFAULT NULL COMMENT '操作人员ID',
  `operation_time` datetime DEFAULT NULL COMMENT '操作时间',
  `remark` varchar(255) DEFAULT NULL,
  `reserve1` varchar(255) DEFAULT NULL COMMENT '预留字段1',
  `reserve2` varchar(255) DEFAULT NULL COMMENT '预留字段1',
  `reserve3` varchar(255) DEFAULT NULL COMMENT '预留字段1',
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
