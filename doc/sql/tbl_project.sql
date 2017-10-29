/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50714
Source Host           : 127.0.0.1:3306
Source Database       : root

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2017-10-29 17:49:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_project
-- ----------------------------
DROP TABLE IF EXISTS `tbl_project`;
CREATE TABLE `tbl_project` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `project_code` varchar(50) DEFAULT NULL,
  `project_name` varchar(200) DEFAULT NULL,
  `project_manager` varchar(50) DEFAULT NULL,
  `project_rank` varchar(10) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `safty_file` varchar(36) DEFAULT NULL,
  `construction_file` varchar(36) DEFAULT NULL,
  `bids_file` varchar(36) DEFAULT NULL,
  `bids_win_file` varchar(36) DEFAULT NULL,
  `bids_win_notice_file` varchar(36) DEFAULT NULL,
  `quality_file` varchar(36) DEFAULT NULL,
  `cost_file` varchar(36) DEFAULT NULL,
  `schedule_file` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
