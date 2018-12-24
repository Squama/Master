/*
Navicat MySQL Data Transfer

Source Server         : syData
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : admineap

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-12-24 10:40:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_role_copy
-- ----------------------------
DROP TABLE IF EXISTS `tbl_role_copy`;
CREATE TABLE `tbl_role_copy` (
  `id` varchar(36) NOT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `remark` longtext,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tbl_role_copy
-- ----------------------------
INSERT INTO `tbl_role_copy` VALUES ('402880eb5c4ec2a4015c4ec591390000', '2017-05-28 19:16:29', '0', '2017-05-28 19:16:29', '0', 'COMMON', '一般用户', '系统注册的用户，不可删除', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e61d54c2a0161dc99ebf10002', '2018-02-28 21:28:50', '0', '2018-02-28 21:28:50', '0', 'temp', '临时组', '临时使用人员', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e61d54c2a0161dc9ae0800003', '2018-02-28 21:29:53', '0', '2018-12-15 15:44:01', '2', 'materiel', '材料管理', '项目部材料员', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e61d54c2a0161dc9b81400004', '2018-02-28 21:30:34', '0', '2018-02-28 21:30:34', '0', 'store', '仓库管理', '项目部、基地仓管员', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e61d54c2a0161dc9cc81b0005', '2018-02-28 21:31:58', '0', '2018-12-17 15:04:15', '1', 'project-quality', '质量管理负责人', '项目部质量员', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e61d54c2a0161dc9d3e6b0006', '2018-02-28 21:32:28', '0', '2018-12-17 15:04:32', '1', 'project-safty', '安全管理负责人', '项目部安全员', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e61d54c2a0161dc9eae030007', '2018-02-28 21:34:02', '0', '2018-12-15 15:46:53', '1', 'project-const', '施工员', '幢号（专业）施工员', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e61d54c2a0161dc9f769e0008', '2018-02-28 21:34:54', '0', '2018-02-28 21:34:54', '0', 'project-data', '项目资料管理', '项目部（资料员）文件管理人员', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e61d54c2a0161dca01f4c0009', '2018-02-28 21:35:37', '0', '2018-12-15 15:45:30', '1', 'project-manager', '项目经理', '项目生产（进度、质安、成本等）控制人', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e61d54c2a0161dca0b57e000a', '2018-02-28 21:36:15', '0', '2018-12-15 15:51:37', '1', 'office', '办公室主任', '公司收发文机构、日常办公管理', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e61d54c2a0161dca191bd000b', '2018-02-28 21:37:12', '0', '2018-02-28 21:37:12', '0', 'engineering', '公司工程管理', '公司质量、安全、生产、技术管理', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e61d54c2a0161dca24803000c', '2018-02-28 21:37:58', '0', '2018-12-15 15:53:50', '2', 'account', '财务人员', '各项目资金支付控制', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e61d54c2a0161dca2dad7000d', '2018-02-28 21:38:36', '0', '2018-12-15 15:48:42', '1', 'cost', '经营科人员', '公司预、决算控制', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e61d54c2a0161dca373da000e', '2018-02-28 21:39:15', '0', '2018-02-28 21:39:15', '0', 'clique', '集团公司管理', '数据查阅、管理', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e61d54c2a0161dca4164f000f', '2018-02-28 21:39:57', '0', '2018-12-15 15:50:42', '1', 'branch', '总经理', '岗位设置、人员授权、数据查阅管理', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e61d54c2a0161dca467560010', '2018-02-28 21:40:17', '0', '2018-02-28 21:40:17', '0', 'software', '软件管理', '信息维护', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e61d54c2a0161dca4c4ac0011', '2018-02-28 21:40:41', '0', '2018-02-28 21:40:41', '0', 'project-manage', '项目管理', '（职业）项目经理', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e63ba8bb40163bf6a84530006', '2018-06-02 15:33:39', '0', '2018-06-02 15:34:07', '1', 'FW', '公司法务部门', '评审流程法务部门意见专用', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e63ba8bb40163bf6cb9f20008', '2018-06-02 15:36:04', '0', '2018-06-02 15:36:04', '0', 'WORKFLOW-LEADER', '用人部门领导审批', '员工转正 用人部门领导需考核。', '3');
INSERT INTO `tbl_role_copy` VALUES ('4028828e63ee059201643ab72dd50170', '2018-06-26 14:10:41', '0', '2018-12-15 15:46:27', '1', 'WORK', '劳务代理', null, '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e645af66901645b0dc16c0013', '2018-07-02 20:53:06', '0', '2018-12-15 15:52:29', '1', 'projectMANAGE', '总工处', '技术管理方案审批总公审批环节角色', '2');
INSERT INTO `tbl_role_copy` VALUES ('4028828e645af66901645b35ac680042', '2018-07-02 21:36:42', '0', '2018-12-17 15:04:54', '1', 'SKILLMANAGE', '技术管理负责人', null, '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e662d94c00166968cf77a0629', '2018-10-21 20:15:13', '0', '2018-10-21 20:15:13', '0', 'CWFZR', '财务负责人', '报销借款审核专用', '2');
INSERT INTO `tbl_role_copy` VALUES ('4028828e6759b2760167742d6f1a000b', '2018-12-03 21:06:34', '0', '2018-12-03 21:06:34', '0', 'ALLMODEL', '全模块角色', '全部模块权限', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e6783ca09016786c5ff0c007d', '2018-12-07 11:46:22', '0', '2018-12-07 11:46:44', '1', 'PROJECTVLO', '工程量上报人员', '暂时处理工程量上报的人，分配权限', '1');
INSERT INTO `tbl_role_copy` VALUES ('4028828e67a6dd9c0167b0d598fd0024', '2018-12-15 15:47:28', '0', '2018-12-15 15:47:28', '0', 'zly', '质量员', null, '6');
INSERT INTO `tbl_role_copy` VALUES ('4028828e67a6dd9c0167b0d606610025', '2018-12-15 15:47:56', '0', '2018-12-15 15:47:56', '0', 'aqy', '安全员', null, '7');
INSERT INTO `tbl_role_copy` VALUES ('4028828e67a6dd9c0167b0d9b14d0026', '2018-12-15 15:51:56', '0', '2018-12-15 15:51:56', '0', 'bgsry', '办公室人员', null, '8');
INSERT INTO `tbl_role_copy` VALUES ('4028828e67a6dd9c0167b0dad2f40027', '2018-12-15 15:53:10', '0', '2018-12-15 15:53:10', '0', 'jsy', '技术员', null, '9');
