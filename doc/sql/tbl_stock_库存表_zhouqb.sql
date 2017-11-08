
/*按钮数据*/
INSERT INTO `admineap`.`tbl_function` (`id`, `create_date_time`, `deleted`, `update_date_time`, `version`, `code`, `functype`, `icon`, `levelCode`, `name`, `parent_id`, `remark`, `url`) VALUES ('8a88c2735f950968015f9531f9a70001', '2017-11-07 14:36:41', '0', '2017-11-07 14:36:41', '0', 'KCXXCX', '1', 'fa fa-cube', '000044000001', '库存信息查询', '8a88c2735f950968015f953098400000', NULL, '/stock/list');
INSERT INTO `admineap`.`tbl_function` (`id`, `create_date_time`, `deleted`, `update_date_time`, `version`, `code`, `functype`, `icon`, `levelCode`, `name`, `parent_id`, `remark`, `url`) VALUES ('8a88c2735f950968015f953098400000', '2017-11-07 14:35:11', '0', '2017-11-07 14:35:11', '0', 'CKGL', '0', 'fa fa-cubes', '000044', '仓库管理', NULL, NULL, NULL);



/*
Navicat MySQL Data Transfer

Source Server         : Mysql
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : admineap

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-11-08 13:25:46
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_stock
-- ----------------------------
DROP TABLE IF EXISTS `tbl_stock`;
CREATE TABLE `tbl_stock` (
  `ID` varchar(64) NOT NULL COMMENT '库存ID',
  `budget_ID` varchar(64) DEFAULT NULL,
  `project_ID` varchar(64) NOT NULL COMMENT '所属项目ID',
  `mat_ID` varchar(64) NOT NULL COMMENT '物料编号',
  `stock_Num` int(8) DEFAULT NULL COMMENT '库存量',
  `storage_person_ID` varchar(64) DEFAULT NULL COMMENT '入库人ID',
  `storage_time` date DEFAULT NULL COMMENT '入库时间',
  `useType` varchar(64) DEFAULT NULL COMMENT '使用类型（1：入库，2：出库，3：调度）',
  `reserve1` varchar(255) DEFAULT NULL,
  `reserve2` varchar(255) DEFAULT NULL,
  `reserve3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tbl_stock_history
-- ----------------------------
DROP TABLE IF EXISTS `tbl_stock_history`;
CREATE TABLE `tbl_stock_history` (
  `ID` varchar(64) NOT NULL COMMENT '库存记录ID',
  `stock_ID` varchar(64) DEFAULT NULL COMMENT '对应库存ID',
  `project_ID` varchar(64) DEFAULT NULL COMMENT '所属项目ID',
  `target_project_ID` varchar(64) DEFAULT NULL COMMENT '目标项目ID',
  `mat_ID` varchar(64) DEFAULT NULL COMMENT '物料ID',
  `stock_change_Num` int(8) DEFAULT NULL COMMENT '操作数量',
  `useTpye` varchar(64) DEFAULT NULL COMMENT '操作类型（1：采购入库，2：消耗出库，3：调度入库，4：调度出库）',
  `operation_person_ID` varchar(64) DEFAULT NULL COMMENT '操作人员ID',
  `operation_time` date DEFAULT NULL COMMENT '操作时间',
  `reserve1` varchar(255) DEFAULT NULL COMMENT '预留字段1',
  `reserve2` varchar(255) DEFAULT NULL COMMENT '预留字段1',
  `reserve3` varchar(255) DEFAULT NULL COMMENT '预留字段1',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
