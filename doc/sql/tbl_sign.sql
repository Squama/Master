/*
Navicat MySQL Data Transfer

Source Server         : admineap
Source Server Version : 50718
Source Host           : rm-uf6n7yzv07k4eyewv5o.mysql.rds.aliyuncs.com:3306
Source Database       : admineap

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2018-01-21 23:09:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_sign
-- ----------------------------
DROP TABLE IF EXISTS `tbl_sign`;
CREATE TABLE `tbl_sign` (
  `id` varchar(36) NOT NULL,
  `value` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `mat_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tbl_sign
-- ----------------------------
INSERT INTO `tbl_sign` VALUES ('1', 'Φ', '直径', null);
INSERT INTO `tbl_sign` VALUES ('2', 'Π', '圆周率', null);
INSERT INTO `tbl_sign` VALUES ('3', '[', '槽钢', null);
INSERT INTO `tbl_sign` VALUES ('4', '∟', '角钢', null);
