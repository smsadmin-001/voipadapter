/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50612
Source Host           : localhost:3306
Source Database       : seven_adapter

Target Server Type    : MYSQL
Target Server Version : 50612
File Encoding         : 65001

Date: 2016-10-18 11:25:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `adapter_config`
-- ----------------------------
DROP TABLE IF EXISTS `adapter_config`;
CREATE TABLE `adapter_config` (
  `RequestTime` varchar(100) NOT NULL,
  `SrcID` tinyint(4) DEFAULT '0' COMMENT '数据来源标记 0-jsp 1-asp',
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
  `CreateTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `UpdateTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `SrcID` tinyint(4) DEFAULT '0' COMMENT '数据来源标记 0-jsp 1-asp',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UQ` (`SpeechID`,`SpeechTable`,`SrcID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of speech_list
-- ----------------------------
