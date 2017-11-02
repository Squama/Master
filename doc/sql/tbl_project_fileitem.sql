/*
Navicat MySQL Data Transfer

Source Server         : admineap
Source Server Version : 50714
Source Host           : localhost:3306
Source Database       : admineap

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2017-11-02 14:55:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_project_fileitem
-- ----------------------------
DROP TABLE IF EXISTS `tbl_project_fileitem`;
CREATE TABLE `tbl_project_fileitem` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `version` int(11) DEFAULT NULL,
  `batch_no` varchar(32) DEFAULT NULL,
  `upload_user_id` varchar(255) DEFAULT NULL,
  `file_name` varchar(64) NOT NULL,
  `file_path` varchar(128) NOT NULL,
  `file_type` varchar(20) DEFAULT NULL,
  `file_size` bigint(20) DEFAULT NULL,
  `source_name` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tbl_project_fileitem
-- ----------------------------
