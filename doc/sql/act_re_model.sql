/*
Navicat MySQL Data Transfer

Source Server         : admineap
Source Server Version : 50714
Source Host           : localhost:3306
Source Database       : admineap

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2017-11-21 19:25:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for act_re_model
-- ----------------------------
DROP TABLE IF EXISTS `act_re_model`;
CREATE TABLE `act_re_model` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CREATE_TIME_` timestamp NULL DEFAULT NULL,
  `LAST_UPDATE_TIME_` timestamp NULL DEFAULT NULL,
  `VERSION_` int(11) DEFAULT NULL,
  `META_INFO_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EDITOR_SOURCE_VALUE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EDITOR_SOURCE_EXTRA_VALUE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`),
  KEY `ACT_FK_MODEL_SOURCE` (`EDITOR_SOURCE_VALUE_ID_`),
  KEY `ACT_FK_MODEL_SOURCE_EXTRA` (`EDITOR_SOURCE_EXTRA_VALUE_ID_`),
  KEY `ACT_FK_MODEL_DEPLOYMENT` (`DEPLOYMENT_ID_`),
  CONSTRAINT `ACT_FK_MODEL_DEPLOYMENT` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_re_deployment` (`ID_`),
  CONSTRAINT `ACT_FK_MODEL_SOURCE` FOREIGN KEY (`EDITOR_SOURCE_VALUE_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
  CONSTRAINT `ACT_FK_MODEL_SOURCE_EXTRA` FOREIGN KEY (`EDITOR_SOURCE_EXTRA_VALUE_ID_`) REFERENCES `act_ge_bytearray` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of act_re_model
-- ----------------------------
INSERT INTO `act_re_model` VALUES ('320235', '11', '请假填报-动态表单', 'vacationApprove', 'LEAVE', '2017-07-07 20:52:39', '2017-07-07 21:29:49', '1', '{\"revision\":1,\"name\":\"请假填报-动态表单\",\"description\":\"用于testng测试用例\"}', '320246', '320236', '320237', '');
INSERT INTO `act_re_model` VALUES ('320250', '14', '请假填报-开始填报', 'vacationRequestStart', 'LEAVE', '2017-07-07 21:32:15', '2017-07-07 21:37:21', '1', '{\"revision\":11,\"name\":\"请假填报-开始填报\",\"description\":\"用于流程demo,请假填报\"}', '320261', '320251', '320252', '');
INSERT INTO `act_re_model` VALUES ('320284', '39', '培训审批-外置表单', 'trainApprove', 'TRAINING', '2017-07-07 21:39:50', '2017-11-20 11:33:53', '1', '{\"revision\":2,\"name\":\"培训审批-外置表单\",\"description\":\"培训审批-外置表单\"}', '345001', '320285', '320286', '');
INSERT INTO `act_re_model` VALUES ('337796', '17', '生产试验-指定审批人', 'LAB', 'LAB', '2017-07-08 16:11:27', '2017-11-21 19:24:22', '1', '{\"revision\":1,\"name\":\"生产试验-指定审批人\",\"description\":\"生产试验-指定审批人\"}', '350011', '337797', '337798', '');
INSERT INTO `act_re_model` VALUES ('345007', '29', '预算审批流程', 'budgetApprove', 'BUDGET', '2017-11-20 13:47:33', '2017-11-21 18:18:53', '1', '{\"revision\":7,\"name\":\"预算审批流程\",\"description\":\"第一次尝试\"}', '420001', '345008', '345022', '');
