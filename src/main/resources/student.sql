/*
Navicat MySQL Data Transfer

Source Server         : localhost_myProject
Source Server Version : 50173
Source Host           : localhost:3306
Source Database       : db_test

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2016-04-18 10:56:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `student`
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `age` int(10) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('1', '张三', '12', 'zs');
INSERT INTO `student` VALUES ('2', '老师', '33', 'ls');
INSERT INTO `student` VALUES ('3', '老师', '33', 'ls2');
INSERT INTO `student` VALUES ('4', '李四', '70', 'ls');
INSERT INTO `student` VALUES ('5', '李四', '88', 'ls2');
INSERT INTO `student` VALUES ('6', '流量', '121313', 'll');
INSERT INTO `student` VALUES ('7', '丁毅', '25', null);
