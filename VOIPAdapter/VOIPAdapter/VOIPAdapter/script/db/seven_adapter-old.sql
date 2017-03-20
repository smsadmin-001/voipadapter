/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50612
Source Host           : localhost:3306
Source Database       : seven_adapter

Target Server Type    : MYSQL
Target Server Version : 50612
File Encoding         : 65001

Date: 2015-08-28 13:58:43
*/
CREATE DATABASE seven_adapter DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
use seven_adapter;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `adapter_config`
-- ----------------------------
DROP TABLE IF EXISTS `adapter_config`;
CREATE TABLE `adapter_config` (
  `RequestTime` varchar(100) NOT NULL,
  PRIMARY KEY (`RequestTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of adapter_config
-- ----------------------------

-- ----------------------------
-- Table structure for `speech_list`
-- ----------------------------
DROP TABLE IF EXISTS `speech_list`;
CREATE TABLE `speech_list` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SpeechID` varchar(100) NOT NULL,
  `SpeechTable` varchar(100) NOT NULL,
  `SpeechTime` varchar(100) NOT NULL,
  `Status` smallint(6) NOT NULL DEFAULT '-1' COMMENT '-2-下载失败�?1-待下载，0-待分发，1-待响应，2-处理成功�?-处理失败',
  `FilePath` varchar(500) NOT NULL,
  `RecogStatus` smallint(6) NOT NULL,
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '任务创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '任务更新时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UQ` (`SpeechID`,`SpeechTable`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of speech_list
-- ----------------------------
