/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 80012
Source Host           : localhost:3306
Source Database       : mybatis

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2020-07-06 21:44:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(30) DEFAULT NULL COMMENT '姓名',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `version` int(10) DEFAULT '1' COMMENT '乐观锁',
  `is_deleted` int(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'liulilin', '18', 'lizeyuwaiting@gmail.com', '2', '0', '2020-07-04 14:36:45', '2020-07-06 02:34:59');
INSERT INTO `user` VALUES ('2', 'shihao', '20', 'lizeyuwaiting@gmail.com', '2', '0', '2020-07-04 14:36:45', '2020-07-06 02:51:52');
INSERT INTO `user` VALUES ('3', 'Tom', '28', 'test3@baomidou.com', '1', '0', '2020-07-04 14:36:45', '2020-07-04 14:36:45');
INSERT INTO `user` VALUES ('4', 'Sandy', '21', 'test4@baomidou.com', '1', '0', '2020-07-04 14:36:45', '2020-07-04 14:36:45');
INSERT INTO `user` VALUES ('5', 'Billie', '24', 'test5@baomidou.com', '1', '0', '2020-07-04 14:36:45', '2020-07-04 14:36:45');
INSERT INTO `user` VALUES ('6', 'liziwei', '22', '964879015@qq.com', '1', '0', '2020-07-04 14:36:45', '2020-07-04 14:36:45');
INSERT INTO `user` VALUES ('7', 'lizeyu', '22', '964879015@qq.com', '1', '0', '2020-07-05 10:09:55', '2020-07-05 11:03:30');
INSERT INTO `user` VALUES ('8', 'lizeyu', '22', '964879015@qq.com', '1', '0', '2020-07-06 10:10:01', '2020-07-05 11:07:53');
INSERT INTO `user` VALUES ('9', 'lizeyu', '22', '964879015@qq.com', '1', '0', '2020-07-06 10:10:04', '2020-07-05 11:10:23');
INSERT INTO `user` VALUES ('10', 'lizeyu', '22', '964879015@qq.com', '1', '0', '2020-07-05 13:21:04', '2020-07-05 13:21:04');
INSERT INTO `user` VALUES ('11', 'liziwei', '22', '964879015@qq.com', '1', '1', '2020-07-06 01:39:17', '2020-07-06 01:40:08');
