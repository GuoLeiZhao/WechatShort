-- ======================================================================
--  duanju_yizhao  —  delivery database (schema + base config data)
--  * All 72 tables' structure included.
--  * Config/dictionary tables carry seed data; user/token/auth/msg tables
--    are structure-only. Secrets replaced with __YOUR_*__ placeholders.
--  * One default super-admin account is seeded (see below).
-- ======================================================================
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- MySQL dump 10.13  Distrib 5.7.44, for Linux (x86_64)
--
-- Host: localhost    Database: duanju_yizhao
-- ------------------------------------------------------
-- Server version	5.7.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activity`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '轮播id',
  `create_at` varchar(255) DEFAULT NULL COMMENT '时间',
  `image_url` varchar(255) DEFAULT NULL COMMENT '图片',
  `state` varchar(255) DEFAULT NULL COMMENT '分类',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '跳转地址',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '标题',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=big5 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ad_advert_event`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ad_advert_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(255) DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL,
  `click_id` varchar(255) DEFAULT NULL,
  `link_id` varchar(255) DEFAULT NULL,
  `album_id` varchar(255) DEFAULT NULL,
  `episode_id` varchar(255) DEFAULT NULL,
  `event_type` varchar(128) DEFAULT NULL COMMENT '事件名称，播放器回传',
  `advert_type` varchar(128) DEFAULT NULL COMMENT '事件名称，播放器回传',
  `seq` int(10) DEFAULT NULL COMMENT '剧集',
  `path` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `index_clickid` (`click_id`),
  KEY `index_openid` (`open_id`),
  KEY `index_create_at` (`created_at`),
  KEY `index_ advert_type` (`advert_type`),
  KEY `index_event_type` (`event_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='播放器播放事件';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ad_app`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ad_app` (
  `app_id` varchar(255) NOT NULL,
  `app_type` varchar(32) DEFAULT NULL COMMENT '小程序类型\nWECHAT\nDOUYIN\nKUAISHOU',
  `app_name` varchar(255) DEFAULT NULL,
  `is_open_link` tinyint(1) DEFAULT '0' COMMENT '开启链接',
  `link_id` varchar(32) DEFAULT NULL COMMENT '默认绑定的链接id',
  `start_time` int(10) DEFAULT '3000' COMMENT '首次时长（ms）',
  `next_time` int(10) DEFAULT '3000' COMMENT '后续时长（ms）',
  `ad_unit_ids` text COMMENT '广告ids',
  `activity_id` varchar(255) DEFAULT NULL COMMENT '侧边栏激活广告id',
  `im_id` varchar(255) DEFAULT NULL COMMENT 'im客服id',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ad_black_list`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ad_black_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `black_key` varchar(128) DEFAULT NULL,
  `black_value` varchar(255) DEFAULT NULL,
  `app_id` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `index_black_value` (`black_value`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ad_customer`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ad_customer` (
  `advertiser_id` bigint(20) NOT NULL COMMENT '广告主id（一个对象只会返回企业号和广告主其中一种）',
  `advertiser_name` varchar(255) DEFAULT NULL COMMENT '广告主名称（一个对象只会返回企业号和广告主其中一种）',
  `advertiser_type` varchar(255) DEFAULT 'NORMAL' COMMENT '广告主类型\n枚举值：DOU+ DOU+类广告主账号、NORMAL普通广告主帐号、LOCAL：本地推',
  `cc_account_id` varchar(32) DEFAULT NULL COMMENT '巨量引擎工作台（原纵横组织）id',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`advertiser_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ad_event_history`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ad_event_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `adid` varchar(255) DEFAULT NULL,
  `creativeid` varchar(255) DEFAULT NULL,
  `creativetype` varchar(255) DEFAULT NULL,
  `clickid` varchar(255) DEFAULT NULL,
  `event_type` varchar(128) DEFAULT NULL COMMENT '事件类型',
  `app_id` varchar(255) DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL,
  `link_id` varchar(32) DEFAULT NULL,
  `album_id` varchar(255) DEFAULT NULL COMMENT '推广主剧id',
  `episode_id` varchar(255) DEFAULT NULL COMMENT '推广剧集id',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_open_id` (`open_id`) USING BTREE,
  KEY `index_app_id` (`app_id`) USING BTREE,
  KEY `index_event_type` (`event_type`) USING BTREE,
  KEY `index_create_at` (`created_at`),
  KEY `index_link_id` (`link_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ad_feedback`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ad_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type` varchar(50) NOT NULL COMMENT '投诉类型',
  `description` text NOT NULL COMMENT '问题描述',
  `contact_phone` varchar(20) NOT NULL COMMENT '联系方式（手机号）',
  `age_over_14` tinyint(1) NOT NULL COMMENT '是否已满14周岁',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID（可选，未登录用户可能为空）',
  `ip_address` varchar(50) DEFAULT NULL COMMENT '用户IP地址',
  `device_info` text COMMENT '设备信息',
  `platform` varchar(20) DEFAULT NULL COMMENT '平台信息（android/ios/h5/小程序等）',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '处理状态：0-待处理，1-处理中，2-已处理，3-已关闭',
  `result` text COMMENT '处理结果',
  `handler_id` bigint(20) DEFAULT NULL COMMENT '处理人ID',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `remark` text COMMENT '备注',
  `created_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投诉反馈表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ad_link`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ad_link` (
  `link_id` varchar(32) NOT NULL,
  `link_name` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `advertiser_id` bigint(20) DEFAULT NULL,
  `album_id` varchar(255) DEFAULT NULL COMMENT '推广主剧id',
  `episode_id` varchar(255) DEFAULT NULL COMMENT '推广剧集id',
  `app_id` varchar(64) DEFAULT NULL COMMENT '小程序appid',
  `landing_channel` varchar(32) DEFAULT 'JULIANG' COMMENT '落地渠道\n微小\n抖小\n快小\n',
  `promotion_channel` varchar(32) DEFAULT 'DOUYIN' COMMENT '推广渠道\n巨量-直投首页',
  `return_name` varchar(255) DEFAULT '默认回传' COMMENT '回传名称',
  `return_type` varchar(32) DEFAULT NULL COMMENT '回传类型\n关键行为\n变现ROI',
  `return_cycle` varchar(32) DEFAULT NULL COMMENT '回传周期\n按小时回传',
  `return_cycle_hours` tinyint(4) DEFAULT '1' COMMENT '按小时回传配置',
  `return_setting` varchar(32) DEFAULT NULL COMMENT '回传设置\nipu+ecpm摸高\nipu+ecpm过滤\nipu+arpu',
  `ipu` decimal(10,2) DEFAULT NULL,
  `ecpm_arpu` decimal(10,2) DEFAULT NULL,
  `rate` decimal(10,2) DEFAULT '100.00' COMMENT '回传概率',
  `ad_show_type` varchar(128) DEFAULT 'VIOLENT' COMMENT '广告展现类型\nVIOLENT 暴力变现\nMODEL_XIAOMAI 小迈模式\n',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`link_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ad_ma_ecpm`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ad_ma_ecpm` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `log_id` varchar(255) DEFAULT NULL,
  `event` varchar(32) DEFAULT NULL,
  `client_key` varchar(255) DEFAULT NULL,
  `from_user_id` varchar(255) DEFAULT NULL,
  `content_id` varchar(255) DEFAULT NULL,
  `mp_id` varchar(255) DEFAULT NULL COMMENT '小程序ID',
  `app_name` varchar(255) DEFAULT NULL,
  `cost` varchar(255) DEFAULT NULL,
  `open_id` varchar(128) DEFAULT NULL,
  `event_time` datetime DEFAULT NULL,
  `req_id` varchar(128) DEFAULT NULL,
  `ad_type` varchar(64) DEFAULT NULL,
  `link_id` varchar(32) DEFAULT NULL,
  `is_handle` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_open_id_link_id_is_handle_create_at` (`open_id`,`created_at`,`link_id`,`is_handle`) USING BTREE,
  KEY `index_created_at` (`created_at`),
  KEY `index_mp_id` (`mp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ad_play`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ad_play` (
  `play_id` varchar(32) NOT NULL,
  `play_name` varchar(255) DEFAULT NULL COMMENT '短剧标题',
  `play_image` varchar(500) DEFAULT NULL COMMENT '封面图ID',
  `play_desc` text COMMENT '剧情简介',
  `play_path` varchar(255) DEFAULT NULL COMMENT '路径',
  `douyin_status` varchar(32) DEFAULT NULL COMMENT '抖音状态',
  `wechat_status` varchar(32) DEFAULT NULL COMMENT '微信状态',
  `kuaishou_status` varchar(32) DEFAULT NULL COMMENT '快手状态',
  `play_rating` int(11) DEFAULT NULL COMMENT '评级 1-5',
  `album_id` varchar(32) DEFAULT NULL COMMENT '抖音返回的album_id',
  `seq_num` int(11) DEFAULT '80' COMMENT '总集数',
  `free_seq_num` int(11) DEFAULT '3' COMMENT '免费集数',
  `year` int(11) DEFAULT '2024' COMMENT '发行年份',
  `album_status` int(11) DEFAULT '3' COMMENT '更新状态：1未上映 2更新中 3已完结',
  `recommendation` varchar(255) DEFAULT '非常好看的短剧' COMMENT '推荐语',
  `qualification` int(11) DEFAULT '1' COMMENT '资质状态：1未报审 2报审通过 3报审不通过 4不建议报审',
  `tag_list` varchar(255) DEFAULT NULL COMMENT '短剧标签',
  `duration` int(11) DEFAULT NULL COMMENT '单集时长(分钟)',
  `production_organisation` varchar(255) DEFAULT NULL COMMENT '制作机构',
  `director` varchar(255) DEFAULT NULL COMMENT '导演',
  `producer` varchar(255) DEFAULT NULL COMMENT '制片人',
  `actor` varchar(255) DEFAULT NULL COMMENT '演员',
  `cost_distribution_uri` varchar(255) DEFAULT NULL COMMENT '成本配置图片ID',
  `production_cost` int(11) DEFAULT '10' COMMENT '制作成本：10(30万以下) 20(30~100万) 30(100万以上)',
  `screen_writer` varchar(255) DEFAULT NULL COMMENT '编剧',
  `record_type` int(11) DEFAULT NULL COMMENT '备案号类型：10普通备案号 20重点备案号',
  `broadcast_record_number` varchar(255) DEFAULT NULL COMMENT '广电备案号',
  `assurance_uri` varchar(255) DEFAULT NULL COMMENT '承诺书图片ID',
  `is_paid` tinyint(1) DEFAULT '1' COMMENT '是否收费：1收费 0免费',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：1上架 0下架',
  `course_label` varchar(255) DEFAULT NULL COMMENT '内部标签',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`play_id`) USING BTREE,
  KEY `index_album_id` (`album_id`),
  KEY `index_ created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短剧主剧管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ad_play_material`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ad_play_material` (
  `material_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '素材ID',
  `material_type` int(1) NOT NULL COMMENT '素材类型：1-视频，2-图片',
  `material_name` varchar(255) DEFAULT NULL COMMENT '素材名称',
  `material_url` varchar(1000) DEFAULT NULL COMMENT '素材URL',
  `format` varchar(10) DEFAULT NULL COMMENT '素材格式（视频专用）',
  `open_id` varchar(64) DEFAULT NULL COMMENT '抖音开放平台返回ID（图片为open_pic_id，视频为open_video_id）',
  `dy_cloud_id` varchar(64) DEFAULT NULL COMMENT '抖音云ID（仅视频使用）',
  `success_to_dy_cloud` tinyint(1) DEFAULT '0' COMMENT '是否同步至抖音云成功',
  `material_status` int(1) DEFAULT '0' COMMENT '素材状态：0-上传中，1-上传成功，2-上传失败',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`material_id`),
  KEY `idx_material_type` (`material_type`),
  KEY `idx_material_name` (`material_name`),
  KEY `idx_material_status` (`material_status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=344 DEFAULT CHARSET=utf8mb4 COMMENT='广告短剧素材表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ad_play_series`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ad_play_series` (
  `series_id` varchar(32) NOT NULL,
  `play_id` varchar(32) DEFAULT NULL COMMENT '主剧id',
  `series_title` varchar(255) DEFAULT NULL,
  `series_num` int(10) DEFAULT NULL COMMENT '第几集',
  `series_image` varchar(500) DEFAULT NULL,
  `series_path` varchar(255) DEFAULT NULL,
  `series_desc` varchar(255) DEFAULT NULL,
  `douyin_status` varchar(32) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`series_id`) USING BTREE,
  KEY `index_ad_play_id` (`play_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ad_statics`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ad_statics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `time_granularity` varchar(64) DEFAULT 'STAT_TIME_GRANULARITY_DAILY' COMMENT '时间粒度\n默认值: STAT_TIME_GRANULARITY_DAILY允许值:STAT_TIME_GRANULARITY_DAILY (按天维度),STAT_TIME_GRANULARITY_HOURLY (按小时维度)',
  `stat_datetime` datetime DEFAULT NULL COMMENT '记录时间 YYYY-MM-DD HH',
  `advertiser_id` bigint(20) DEFAULT NULL COMMENT '广告主ID',
  `advertiser_name` varchar(255) DEFAULT NULL COMMENT '广告账户名称',
  `link_id` varchar(32) DEFAULT NULL COMMENT '链接ID',
  `balance` float DEFAULT NULL COMMENT '账户余额',
  `payment` float DEFAULT NULL COMMENT '消耗',
  `cost` float DEFAULT NULL COMMENT '回收',
  `roi` float DEFAULT NULL COMMENT '首日ROI',
  `ad_show` bigint(20) DEFAULT NULL COMMENT '展示数',
  `cost_per_thousand_show` float DEFAULT NULL COMMENT '千展成本',
  `click` bigint(20) DEFAULT NULL COMMENT '点击数',
  `click_rate` float DEFAULT NULL COMMENT '点击率',
  `cost_per_click` float DEFAULT NULL COMMENT '平均点击成本',
  `jump_count` bigint(20) DEFAULT NULL COMMENT '跳转数',
  `jump_rate` float DEFAULT NULL COMMENT '跳转率',
  `launch_count` bigint(20) DEFAULT NULL COMMENT '拉起数',
  `launch_rate` float DEFAULT NULL COMMENT '拉起率',
  `launch_cost` float DEFAULT NULL COMMENT '拉起成本',
  `arpu` float DEFAULT NULL COMMENT 'ARPU',
  `ipu` float DEFAULT NULL COMMENT 'IPU',
  `avg_ecpm` float DEFAULT NULL COMMENT '平均ECPM',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `index_stat_datetime` (`stat_datetime`),
  KEY `index_advertiser_id` (`advertiser_id`),
  KEY `index_time` (`time_granularity`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='广告统计表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ad_user_asset`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ad_user_asset` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `app_id` varchar(255) DEFAULT NULL COMMENT '应用ID',
  `open_id` varchar(255) DEFAULT NULL COMMENT '用户OpenID',
  `dev_user_account_id` varchar(255) DEFAULT NULL COMMENT '开发者账户ID',
  `asset_type` int(11) DEFAULT NULL COMMENT '资产类型: 1-会员, 2-代币, 3-抵用券, 4-已获得的短剧剧集信息',
  `flow_direction` int(11) DEFAULT NULL COMMENT '流水方向（需业务定义具体含义）',
  `flow_type` varchar(255) DEFAULT NULL COMMENT '流水类型（字段名严格驼峰）',
  `out_change_id` varchar(255) DEFAULT NULL COMMENT '外部变更ID',
  `flow_content` text COMMENT '流水内容（JSON或文本）',
  `version` varchar(50) DEFAULT NULL COMMENT '版本号',
  `idempotent_id` varchar(255) DEFAULT NULL COMMENT '幂等ID（需唯一）',
  `change_time` varchar(255) DEFAULT NULL COMMENT '变更时间',
  `clickid` varchar(255) DEFAULT NULL COMMENT '点击ID',
  `link_id` varchar(255) DEFAULT NULL COMMENT '链路ID',
  `tt_album_id` varchar(255) DEFAULT NULL COMMENT '短剧专辑ID',
  `tt_episode_id` varchar(255) DEFAULT NULL COMMENT '短剧剧集ID',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `uk_idempotent` (`idempotent_id`) USING BTREE,
  KEY `index_create_at` (`created_at`),
  KEY `index_open_id` (`open_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户资产流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ad_user_auth`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ad_user_auth` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `auth_time` datetime DEFAULT NULL,
  `app_id` varchar(64) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_app_id` (`app_id`),
  KEY `idx_auth_time` (`auth_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ad_video_event`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ad_video_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(255) DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL,
  `click_id` varchar(255) DEFAULT NULL,
  `link_id` varchar(255) DEFAULT NULL,
  `album_id` varchar(255) DEFAULT NULL,
  `episode_id` varchar(255) DEFAULT NULL,
  `event_type` varchar(128) DEFAULT NULL COMMENT '事件名称，播放器回传',
  `seq` int(10) DEFAULT NULL COMMENT '剧集',
  `path` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='播放器播放事件';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ai_data`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ai_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '数据id',
  `date` varchar(255) DEFAULT NULL COMMENT '任务时间',
  `task_name` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `line` varchar(255) DEFAULT NULL COMMENT '语音线路',
  `model` varchar(255) DEFAULT NULL COMMENT '模型',
  `add_num` int(11) DEFAULT NULL COMMENT '添加量',
  `roi` decimal(10,0) NOT NULL COMMENT 'roi',
  `call_num` bigint(20) NOT NULL COMMENT '拨打量',
  `call_completion` bigint(20) NOT NULL COMMENT '接通量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ai_sendsuccess`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ai_sendsuccess` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_name` varchar(255) DEFAULT NULL,
  `send_success_rate` decimal(10,4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='爱赢到达添加率计算';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ai_sms`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ai_sms` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `date` datetime NOT NULL COMMENT '日期',
  `customer` varchar(255) DEFAULT NULL COMMENT '客户',
  `task_name` varchar(255) DEFAULT NULL COMMENT '短信任务名称',
  `gaid_name` varchar(255) DEFAULT NULL COMMENT 'gaid包名',
  `push_num` decimal(10,2) DEFAULT NULL COMMENT '提交量',
  `success_num` decimal(10,2) DEFAULT NULL COMMENT '成功数',
  `click_num` decimal(10,2) DEFAULT NULL COMMENT '点击量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短信任务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ai_sms_supply`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ai_sms_supply` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `date` datetime DEFAULT NULL COMMENT '日期',
  `type` int(11) DEFAULT NULL COMMENT '类型 1 语音ai ，2 sms短信',
  `customer` varchar(255) DEFAULT NULL COMMENT '客户',
  `supply_num` int(11) DEFAULT NULL COMMENT '供量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供量表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `app`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'app升级配置',
  `android_wgt_url` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '安卓升级地址',
  `create_at` varchar(600) DEFAULT NULL COMMENT '创建时间',
  `des` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述',
  `ios_version` varchar(255) DEFAULT NULL COMMENT '苹果版本',
  `ios_wgt_url` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '苹果升级地址',
  `method` varchar(600) DEFAULT NULL COMMENT '是否强制升级',
  `version` varchar(600) DEFAULT NULL COMMENT '安卓版本',
  `wgt_url` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '通用下载地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=big5 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `banner`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `banner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `image_url` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `state` int(11) DEFAULT NULL COMMENT '状态1正常2隐藏',
  `classify` int(11) DEFAULT NULL COMMENT '分类1banner图2首页分类',
  `url` varchar(255) DEFAULT NULL COMMENT '跳转地址 ',
  `sort` int(11) DEFAULT NULL COMMENT '顺序',
  `describes` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  KEY `index_classify` (`classify`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cash_out`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cash_out` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '申请提现id',
  `create_at` varchar(255) DEFAULT NULL COMMENT '申请时间',
  `is_out` bit(1) DEFAULT NULL COMMENT '是否转账',
  `money` varchar(255) DEFAULT NULL COMMENT '提现金额',
  `out_at` varchar(255) DEFAULT NULL COMMENT '转账时间',
  `relation_id` varchar(255) DEFAULT NULL COMMENT '会员编号',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `zhifubao` varchar(255) DEFAULT NULL COMMENT '支付宝账号',
  `zhifubao_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '支付宝姓名',
  `order_number` varchar(255) DEFAULT NULL COMMENT '订单编号',
  `state` int(11) DEFAULT NULL COMMENT '状态 0待转账 1成功 -1退款',
  `refund` varchar(1000) CHARACTER SET utf8 DEFAULT NULL COMMENT '原因',
  `classify` int(11) DEFAULT '1',
  `rate` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_name` (`relation_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=big5 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comment_good`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment_good` (
  `comment_good_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论点赞id',
  `course_comment_id` int(11) DEFAULT NULL COMMENT '评论id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`comment_good_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `common_info`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `common_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置文件id',
  `create_at` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `max` varchar(255) DEFAULT NULL,
  `min` varchar(2000) DEFAULT NULL COMMENT '配置文件名称',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  `value` text COMMENT '值',
  `condition_from` varchar(255) DEFAULT NULL COMMENT '分类',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=825 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `coupon`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `coupon` (
  `coupon_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '优惠券id',
  `coupon_name` varchar(255) DEFAULT NULL COMMENT '优惠券名称',
  `money` decimal(10,2) DEFAULT NULL COMMENT '可抵扣金额',
  `coupon_type` int(11) DEFAULT NULL COMMENT '所属类型',
  PRIMARY KEY (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='优惠卷';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `coupon_user`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `coupon_user` (
  `coupon_user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户优惠卷id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `coupon_money` decimal(10,0) DEFAULT NULL COMMENT '优惠券金额',
  `coupon_name` varchar(255) DEFAULT NULL COMMENT '优惠券使用规则',
  PRIMARY KEY (`coupon_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `course`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course` (
  `course_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '课程id',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `title_img` text COMMENT '封面图',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `classify_id` int(11) DEFAULT NULL COMMENT '分类',
  `pay_num` int(11) DEFAULT NULL COMMENT '购买次数',
  `course_label` varchar(255) DEFAULT NULL COMMENT '课程标签',
  `img` varchar(500) DEFAULT NULL COMMENT '内容图',
  `details` longtext COMMENT '课程介绍',
  `is_delete` int(11) DEFAULT NULL COMMENT '删除标识 0未删除 1已删除',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(64) DEFAULT NULL COMMENT '更新时间',
  `msg_url` varchar(255) DEFAULT NULL COMMENT '视频地址',
  `msg_type` int(11) DEFAULT NULL COMMENT '上传方式0OSS-1本地',
  `is_recommend` int(11) DEFAULT NULL COMMENT '是否是推荐商品',
  `banner_id` int(11) DEFAULT NULL COMMENT '首页金刚区分类',
  `course_type` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT '1' COMMENT '状态 1上架 2下架',
  `banner_img` varchar(500) DEFAULT NULL COMMENT '轮播图',
  `is_over` int(11) DEFAULT NULL COMMENT '是否完结 0否 1是 ',
  `is_price` int(11) DEFAULT NULL COMMENT '是否收费 1是 2免费',
  `view_counts` int(11) DEFAULT NULL COMMENT '播放量',
  `jifen` decimal(10,2) DEFAULT '0.00' COMMENT '积分价格',
  `wx_media_id` bigint(20) DEFAULT NULL COMMENT '微信媒资库ID',
  `wx_media_out_date` datetime DEFAULT NULL COMMENT '微信媒资过期时间',
  `tt_album_id` varchar(255) DEFAULT NULL COMMENT '抖音短剧 id',
  PRIMARY KEY (`course_id`),
  KEY `banner_id` (`banner_id`),
  KEY `classify_id` (`classify_id`),
  KEY `index_wx_mid` (`wx_media_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `course_classification`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course_classification` (
  `classification_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '课程分类id',
  `classification_name` varchar(255) DEFAULT NULL COMMENT '课程分类名称',
  `is_delete` int(11) DEFAULT NULL COMMENT '假删除0正常1已删除',
  PRIMARY KEY (`classification_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `course_collect`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course_collect` (
  `course_collect_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '收藏id',
  `course_id` int(11) DEFAULT NULL COMMENT '课程id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `course_details_id` int(11) DEFAULT NULL,
  `classify` int(11) DEFAULT NULL,
  `update_time` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`course_collect_id`),
  KEY `course_id` (`course_id`),
  KEY `idx_userid_coursedetailsid_classify` (`user_id`,`course_details_id`,`classify`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `course_comment`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course_comment` (
  `course_comment_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '课程评论id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `course_id` int(11) DEFAULT NULL COMMENT '课程id',
  `goods_num` int(11) DEFAULT '0' COMMENT '点赞数',
  `content` varchar(1000) DEFAULT NULL COMMENT '评论内容',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`course_comment_id`),
  KEY `course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `course_details`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course_details` (
  `course_details_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '课程目录id',
  `course_id` int(11) DEFAULT NULL COMMENT '课程id',
  `course_details_name` varchar(500) DEFAULT NULL COMMENT '课程名称',
  `video_url` text COMMENT '视频地址',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `title_img` varchar(500) DEFAULT NULL COMMENT '封面图',
  `content` varchar(500) DEFAULT NULL COMMENT '介绍',
  `good_num` int(11) DEFAULT NULL COMMENT '点赞数',
  `price` decimal(10,2) DEFAULT NULL COMMENT '售价',
  `is_price` int(11) DEFAULT NULL COMMENT '是否收费 1是 2否',
  `good` int(11) DEFAULT NULL COMMENT '是否是推荐 1是',
  `jifen` decimal(10,2) DEFAULT '0.00' COMMENT '积分价格',
  `wx_media_id` bigint(20) DEFAULT NULL,
  `tt_episode_id` varchar(255) DEFAULT NULL COMMENT '抖音剧集id',
  PRIMARY KEY (`course_details_id`),
  KEY `course_details_course_details_name_index` (`course_details_name`),
  KEY `course_details_course_id_course_details_name_index` (`course_id`,`course_details_name`),
  KEY `course_details_course_id_index` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `course_user`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course_user` (
  `course_user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '我的课程id',
  `course_id` int(11) DEFAULT NULL COMMENT '课程id',
  `order_id` int(11) DEFAULT NULL COMMENT '订单id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(64) DEFAULT NULL COMMENT '更新时间',
  `course_details_id` int(11) DEFAULT NULL COMMENT '集id',
  `classify` int(11) DEFAULT NULL COMMENT '1整集2单集',
  PRIMARY KEY (`course_user_id`),
  KEY `course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goods`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goods` (
  `goods_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `goods_title` varchar(255) DEFAULT NULL COMMENT '商品标题',
  `longitude` varchar(255) DEFAULT NULL COMMENT '经度',
  `latitude` varchar(255) DEFAULT NULL COMMENT '纬度',
  `address` varchar(500) DEFAULT NULL COMMENT '地址',
  `title_img` varchar(1000) DEFAULT NULL COMMENT '封面图',
  `img` varchar(2000) DEFAULT NULL COMMENT '详情图',
  `sum_num` int(11) DEFAULT NULL COMMENT '总数量',
  `end_num` int(11) DEFAULT NULL COMMENT '剩余数量',
  `goods_price` decimal(10,2) DEFAULT NULL COMMENT '商品价格',
  `member_price` decimal(10,2) DEFAULT NULL COMMENT '会员满',
  `member_money` decimal(10,2) DEFAULT NULL COMMENT '会员返',
  `price` decimal(10,2) DEFAULT NULL COMMENT '普通用户满',
  `money` decimal(10,2) DEFAULT NULL COMMENT '普通用户返',
  `start_time` varchar(64) DEFAULT NULL COMMENT '开始时间',
  `end_time` varchar(64) DEFAULT NULL COMMENT '结束时间',
  `classify` int(11) DEFAULT NULL COMMENT '分类 1 饿了么  2美团',
  `type_id` int(11) DEFAULT NULL COMMENT '类型',
  `num_star` int(11) DEFAULT NULL COMMENT '几颗星',
  `num_img` int(11) DEFAULT NULL COMMENT '几张图',
  `num_word` int(11) DEFAULT NULL COMMENT '多少字',
  `remark` text COMMENT '备注  富文本',
  `url` text COMMENT '小程序跳转参数',
  `status` int(11) DEFAULT NULL COMMENT '1 上架 2下架',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `province` varchar(255) DEFAULT NULL COMMENT '省',
  `city` varchar(255) DEFAULT NULL COMMENT '市',
  `district` varchar(255) DEFAULT NULL COMMENT '区',
  `activity_ids` varchar(255) DEFAULT NULL COMMENT '金刚区分类',
  `is_goods` int(11) DEFAULT NULL COMMENT '是否是精选商品  0否 1是',
  `scope` int(11) DEFAULT NULL COMMENT '范围',
  `member_privilege` varchar(1000) DEFAULT NULL COMMENT '会员权益',
  `privilege` varchar(1000) DEFAULT NULL COMMENT '普通用户权益',
  `describes` varchar(1000) DEFAULT NULL COMMENT '门店介绍',
  `shop_describe` varchar(1000) DEFAULT NULL COMMENT '商家介绍',
  `phone` varchar(1000) DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goods_type`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goods_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品类型id',
  `create_at` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建时间',
  `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '类型名称',
  `img` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级类型id',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `help_classify`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `help_classify` (
  `help_classify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '帮助中心分类',
  `help_classify_name` varchar(500) DEFAULT NULL COMMENT '分类名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `parent_id` int(11) DEFAULT NULL COMMENT '上级id',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `types` int(11) DEFAULT NULL,
  PRIMARY KEY (`help_classify_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `help_word`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `help_word` (
  `help_word_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '帮助文档id',
  `help_word_title` varchar(255) DEFAULT NULL COMMENT '帮助标题',
  `help_word_content` longtext COMMENT '帮助文档内容',
  `help_classify_id` int(11) DEFAULT NULL COMMENT '帮助分类id',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`help_word_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `invite`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invite` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) DEFAULT NULL COMMENT '邀请者id',
  `invitee_user_id` int(11) DEFAULT NULL COMMENT '被邀请者id',
  `state` int(11) DEFAULT NULL COMMENT '状态 0非会员 1会员',
  `money` decimal(10,2) DEFAULT NULL COMMENT '邀请收益',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='邀请信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `invite_activity`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invite_activity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(64) DEFAULT NULL COMMENT '名称',
  `hash` varchar(8) DEFAULT NULL COMMENT '6 位唯一标识，数字加大写字母',
  `status` varchar(8) DEFAULT NULL COMMENT '领取状态',
  `user_id` bigint(20) DEFAULT NULL COMMENT '领取用户',
  `num` double DEFAULT NULL COMMENT '数量',
  `receive_time` datetime DEFAULT NULL COMMENT '领取时间',
  `type` varchar(12) DEFAULT '0' COMMENT '类型',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `invite_money`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invite_money` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '邀请收益钱包id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `money_sum` decimal(10,2) DEFAULT NULL COMMENT '总获取收益',
  `money` decimal(10,2) DEFAULT NULL COMMENT '当前金额',
  `cash_out` decimal(10,2) DEFAULT NULL COMMENT '累计提现',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `kuai_shou_watch`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kuai_shou_watch` (
  `id` varchar(64) NOT NULL COMMENT 'id',
  `channel` varchar(64) DEFAULT NULL COMMENT '渠道来源',
  `imei2` varchar(64) DEFAULT NULL COMMENT '安卓设备id的md5摘要，对15位数字的 IMEI （比如860576038225452）进行 MD5，32位',
  `imei3` varchar(64) DEFAULT NULL COMMENT '安卓设备ID，imei 的sha1',
  `idfa` varchar(64) DEFAULT NULL COMMENT 'ios 设备id, idfa 明文直接 md5（大写）比如：32ED3EE5-9968-4F25-A015-DE3CFF569568',
  `kenyid_caa` varchar(64) DEFAULT NULL COMMENT 'IOS 获取不到idfa后的设备id，将填充URLEncode后的JSON数组，支持多版本的中广协CAID下发，MD5是32位[小]写加密',
  `mac` varchar(64) DEFAULT NULL COMMENT '默认md5，可跟联盟运营申请替换明文',
  `androidid` varchar(64) DEFAULT NULL COMMENT '默认md5，可跟联盟运营申请替换明文',
  `os` varchar(64) DEFAULT NULL COMMENT '操作系统',
  `model` varchar(64) DEFAULT NULL COMMENT '机型',
  `ua` varchar(64) DEFAULT NULL COMMENT '用户代理(User Agent)',
  `ts` varchar(64) DEFAULT NULL COMMENT '设备时间戳',
  `oaid` varchar(64) DEFAULT NULL COMMENT 'Android设备标识，Android Q版本 以上使用，原值(不通手机品牌格式会有区别)',
  `oaid2` varchar(64) DEFAULT NULL COMMENT 'Android设备标识，Android Q版本 以上使用，md5加密后替换',
  `pos` varchar(64) DEFAULT NULL COMMENT '加密后的广告位id，可选择账号id、产品id、rtaid 维度加密',
  `accountid` varchar(64) DEFAULT NULL COMMENT '广告主账户ID，原值',
  `dname` varchar(64) DEFAULT NULL COMMENT '计划名称，原值',
  `did` varchar(64) DEFAULT NULL COMMENT '计划ID，原值',
  `cid` varchar(64) DEFAULT NULL COMMENT '广告创意ID，原值',
  `aid` varchar(64) DEFAULT NULL COMMENT '广告组ID，原值',
  `ip` varchar(64) DEFAULT NULL COMMENT '用户IP地址',
  `callback` varchar(64) DEFAULT NULL COMMENT '回调信息，编码一次的URL，长度小于10k',
  `callback_param` varchar(64) DEFAULT NULL COMMENT 'callback值，非url',
  `device_width` varchar(64) DEFAULT NULL COMMENT '设备宽',
  `device_height` varchar(64) DEFAULT NULL COMMENT '设备高',
  `screen_width` varchar(64) DEFAULT NULL COMMENT '屏幕宽',
  `screen_height` varchar(64) DEFAULT NULL COMMENT '屏幕高',
  `industryname` varchar(64) DEFAULT NULL COMMENT '投放媒体的一级行业，md5摘要，需跟联盟运营申请加白之后替换',
  `cost_price` varchar(64) DEFAULT NULL COMMENT '本次曝光扣费金额，单位厘，需跟联盟运营申请之后替换',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='快手联盟监测链接回调';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `message_info`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '内容',
  `create_at` varchar(600) DEFAULT NULL COMMENT '创建时间',
  `image` varchar(600) DEFAULT NULL COMMENT '图片',
  `is_see` varchar(600) DEFAULT NULL,
  `send_state` varchar(600) DEFAULT NULL,
  `send_time` varchar(600) DEFAULT NULL,
  `state` varchar(600) DEFAULT NULL COMMENT '分类',
  `title` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '标题',
  `url` varchar(600) DEFAULT NULL COMMENT '地址',
  `type` varchar(600) DEFAULT NULL,
  `platform` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=big5 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `msg`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(255) DEFAULT NULL COMMENT '短信验证码',
  `phone` varchar(255) DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`id`),
  KEY `index_name` (`code`,`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=big5 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orders`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `orders_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `orders_no` varchar(32) DEFAULT NULL COMMENT '订单编号',
  `trade_no` varchar(64) DEFAULT NULL COMMENT '支付宝支付单号',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `course_id` int(11) DEFAULT NULL COMMENT '课程id',
  `pay_money` decimal(10,2) DEFAULT NULL COMMENT '支付金额',
  `pay_way` int(11) DEFAULT NULL COMMENT '支付方式 1微信小程序  2微信公众号 3微信App 4支付宝',
  `status` int(11) DEFAULT NULL COMMENT '状态 0待支付  1已支付  2已退款',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `refund_content` varchar(255) DEFAULT NULL COMMENT '退款原因',
  `orders_type` int(11) DEFAULT NULL COMMENT '订单种类 1课程 2会员',
  `vip_name_type` int(11) DEFAULT NULL COMMENT '会员类型0月/1季度/2年',
  `course_details_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`orders_id`),
  KEY `orders_course_details_id_index` (`course_details_id`),
  KEY `orders_course_id_index` (`course_id`),
  KEY `orders_course_id_user_id_index` (`course_id`,`user_id`),
  KEY `orders_user_id_index` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pay_details`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pay_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '充值订单id',
  `classify` int(11) DEFAULT NULL COMMENT '分类（1微信 1app微信 2微信公众号 3微信小程序 4支付宝）',
  `trade_no` varchar(32) DEFAULT NULL COMMENT '支付宝支付单号',
  `money` double(255,2) DEFAULT NULL COMMENT '充值金额',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `state` int(11) DEFAULT NULL COMMENT '0待支付 1支付成功 2失败',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `pay_time` varchar(64) DEFAULT NULL COMMENT '支付时间',
  `type` int(11) DEFAULT NULL COMMENT '支付类型 1 用户  2会员',
  `remark` varchar(255) DEFAULT NULL,
  `order_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `search`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `search` (
  `search_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '搜索id',
  `search_name` varchar(255) DEFAULT NULL COMMENT '搜索名称',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `update_time` varchar(64) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`search_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shop_item`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shop_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `pic` varchar(255) DEFAULT NULL COMMENT '封面',
  `desc` text COMMENT '描述',
  `price` bigint(20) DEFAULT '0' COMMENT '价格',
  `stock` bigint(20) DEFAULT '100' COMMENT '库存',
  `sort` bigint(20) DEFAULT '1001001001' COMMENT '排序权重',
  `shelves` tinyint(1) DEFAULT '0' COMMENT '是否上架',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  `sup_name` varchar(255) DEFAULT NULL COMMENT '惊喜名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shop_order`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shop_order` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `order_user_id` bigint(20) DEFAULT NULL COMMENT '购买用户 id',
  `item_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `item_name` varchar(64) DEFAULT NULL COMMENT '商品名称',
  `item_pic` varchar(255) DEFAULT NULL COMMENT '商品封面',
  `order_status` varchar(16) DEFAULT NULL COMMENT '订单状态',
  `buyer_name` varchar(32) DEFAULT NULL COMMENT '买家姓名',
  `buyer_phone` varchar(32) DEFAULT NULL COMMENT '买家手机',
  `buyer_address` varchar(32) DEFAULT NULL COMMENT '买家收货地址',
  `transfer_no` varchar(64) DEFAULT NULL COMMENT '物流编号',
  `delivery_status` varchar(16) DEFAULT NULL COMMENT '物流状态',
  `delivery_date` datetime DEFAULT NULL COMMENT '发货日期',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  `buyer_city` varchar(64) DEFAULT NULL COMMENT '省市区',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `short_url`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `short_url` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `surl` varchar(16) DEFAULT NULL COMMENT '短链',
  `lurl` varchar(255) DEFAULT NULL COMMENT '长链',
  `views` bigint(20) DEFAULT NULL COMMENT '调用次数(PV)',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `name` varchar(64) DEFAULT NULL,
  `bak_url` varchar(255) DEFAULT NULL COMMENT '备用链接',
  `type` varchar(8) DEFAULT NULL COMMENT '短链类型',
  PRIMARY KEY (`id`),
  KEY `short_url_surl_index` (`surl`),
  KEY `short_url_type_index` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短链映射表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `short_url_log`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `short_url_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `short_url_id` bigint(20) NOT NULL COMMENT '短链id',
  `ip` varchar(255) DEFAULT NULL COMMENT 'ip',
  `country` varchar(255) DEFAULT NULL COMMENT '国',
  `region` varchar(255) DEFAULT NULL COMMENT '省',
  `city` varchar(255) DEFAULT NULL COMMENT '市',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `short_url_log_short_url_id_index` (`short_url_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短链点击ip记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_captcha`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_captcha` (
  `uuid` char(36) NOT NULL COMMENT 'uuid',
  `code` varchar(6) NOT NULL COMMENT '验证码',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统验证码';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_config`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_key` varchar(50) DEFAULT NULL COMMENT 'key',
  `param_value` varchar(2000) DEFAULT NULL COMMENT 'value',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `param_key` (`param_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统配置信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_dict`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '字典名称',
  `type` varchar(100) NOT NULL COMMENT '字典类型',
  `code` varchar(100) NOT NULL COMMENT '字典码',
  `value` varchar(1000) NOT NULL COMMENT '字典值',
  `order_num` int(11) DEFAULT '0' COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标记  -1：已删除  0：正常',
  PRIMARY KEY (`id`),
  UNIQUE KEY `type` (`type`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='数据字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_log`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `time` bigint(20) NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `userid` varchar(255) DEFAULT NULL COMMENT '动作',
  `type` varchar(8) DEFAULT NULL COMMENT 'sys 是系统日志 / user 是埋点日志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_menu`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='菜单管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_oss`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_oss` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(200) DEFAULT NULL COMMENT 'URL地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文件上传';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_role`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_role_menu`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色与菜单对应关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_user`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) DEFAULT NULL COMMENT '盐',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_user_role`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户与角色对应关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_user_token`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_token` (
  `user_id` bigint(20) NOT NULL,
  `token` varchar(100) NOT NULL COMMENT 'token',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统用户Token';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_user`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(500) DEFAULT NULL COMMENT '用户名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `sex` int(11) DEFAULT NULL COMMENT '性别',
  `open_id` varchar(500) DEFAULT NULL COMMENT '微信小程序openId',
  `wx_open_id` varchar(500) DEFAULT NULL COMMENT '微信App  openId',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(64) DEFAULT NULL COMMENT '更新时间',
  `apple_id` varchar(64) DEFAULT NULL COMMENT '苹果id',
  `sys_phone` int(11) DEFAULT NULL COMMENT '手机类型 1安卓 2ios',
  `status` int(11) DEFAULT NULL COMMENT '状态 1正常 2禁用',
  `platform` varchar(255) DEFAULT NULL COMMENT '来源 APP 小程序  公众号',
  `jifen` int(11) DEFAULT NULL COMMENT '积分',
  `invitation_code` varchar(255) DEFAULT NULL COMMENT '邀请码',
  `inviter_code` varchar(255) DEFAULT NULL COMMENT '邀请人邀请码',
  `clientid` varchar(255) DEFAULT NULL COMMENT 'app消息推送id',
  `zhi_fu_bao_name` varchar(255) DEFAULT NULL COMMENT '支付宝名称',
  `zhi_fu_bao` varchar(255) DEFAULT NULL COMMENT '支付宝姓名',
  `wx_id` varchar(64) DEFAULT NULL COMMENT '公众号id',
  `rate` decimal(10,2) DEFAULT NULL COMMENT '佣金',
  `on_line_time` varchar(64) DEFAULT NULL COMMENT '最后一次在线时间',
  `wx_union_id` varchar(128) DEFAULT NULL COMMENT '微信 union id',
  `tt_click_id` varchar(255) DEFAULT NULL COMMENT '巨量广告点击id',
  `tt_link_id` varchar(255) DEFAULT NULL COMMENT '链接id',
  `tt_options` text COMMENT '广告链接信息',
  PRIMARY KEY (`user_id`) USING BTREE,
  KEY `index_link_id` (`tt_link_id`),
  KEY `index_open_id` (`open_id`),
  KEY `index_click_id` (`tt_click_id`),
  KEY `idx_onlinetime` (`on_line_time`),
  KEY `idx_createtime` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tmp_task_analysis`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tmp_task_analysis` (
  `id` int(11) NOT NULL COMMENT '主键id',
  `task_name` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `send_cnt` int(11) DEFAULT NULL COMMENT '发送量',
  `send_suc_cnt` int(11) DEFAULT NULL COMMENT '发送成功量',
  `click_cnt` int(11) DEFAULT NULL COMMENT '点击量',
  `package_name` varchar(255) DEFAULT NULL COMMENT '包名',
  `channel_name` varchar(255) DEFAULT NULL COMMENT '通道',
  `sms_detail` varchar(255) DEFAULT NULL COMMENT '短信文案',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_integral`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_integral` (
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `integral_num` int(11) DEFAULT NULL COMMENT '积分数量',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_integral_details`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_integral_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '积分详情id',
  `content` varchar(255) DEFAULT NULL COMMENT '内容',
  `classify` int(11) DEFAULT NULL COMMENT '获取类型 1签到2报名',
  `type` int(11) DEFAULT NULL COMMENT '分类 1增加 2减少',
  `num` int(11) DEFAULT NULL COMMENT '数量',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `day` int(11) DEFAULT NULL,
  `good_id` int(11) DEFAULT NULL COMMENT '物品id',
  `good_name` varchar(64) DEFAULT NULL COMMENT '物品名称',
  `good_img` text COMMENT '物品图片',
  `order_id` varchar(64) DEFAULT NULL COMMENT '订单 id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_money`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_money` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '钱包id',
  `money` decimal(10,2) DEFAULT NULL COMMENT '钱包金额',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户钱包';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_money_details`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_money_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '钱包详情id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `by_user_id` int(11) DEFAULT NULL COMMENT '对应用户id',
  `title` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '标题',
  `classify` int(11) DEFAULT NULL COMMENT '1注册  2首次购买  3购买 4提现',
  `type` int(11) DEFAULT '1' COMMENT '类别（1充值2支出）',
  `state` int(11) DEFAULT '1' COMMENT '状态 1待支付 2已到账 3取消',
  `money` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `content` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '内容',
  `create_time` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_vip`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_vip` (
  `vip_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '会员id',
  `vip_name_type` int(11) DEFAULT NULL COMMENT '会员类型0月/1季度/2年',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `is_vip` int(11) DEFAULT NULL COMMENT '是否是会员  1否 2是',
  `create_time` varchar(255) DEFAULT NULL COMMENT '购买时间',
  `end_time` varchar(255) DEFAULT NULL COMMENT '到期时间',
  PRIMARY KEY (`vip_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `video_landing_page`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `video_landing_page` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 id',
  `app_id` varchar(32) DEFAULT NULL COMMENT '小程序 appId',
  `name` varchar(32) DEFAULT NULL COMMENT '短剧名称',
  `group_name` varchar(32) DEFAULT NULL COMMENT '短剧分组（例如点众 / 柠七）',
  `type` varchar(32) DEFAULT NULL COMMENT '短剧类型（自有 / 三方小程序公包 / 三方小程序私包）',
  `pic` text COMMENT '封面图',
  `description` varchar(256) DEFAULT NULL COMMENT '短剧介绍',
  `url` text COMMENT '跳转链接，',
  `score` double DEFAULT '0' COMMENT '分数',
  `sort` tinyint(4) DEFAULT '0' COMMENT '排名',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='落地页短剧';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `video_landing_page_click_static`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `video_landing_page_click_static` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `landing_page_id` bigint(20) DEFAULT NULL COMMENT '点击短剧',
  `landing_page_name` varchar(256) DEFAULT NULL COMMENT '点击短剧',
  `phone` varchar(16) DEFAULT NULL COMMENT '手机号码',
  `nick_name` varchar(256) DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(256) DEFAULT NULL COMMENT '头像',
  `num` bigint(20) DEFAULT '0' COMMENT '点击数量',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `video_landing_page_share`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `video_landing_page_share` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 id',
  `app_id` varchar(32) DEFAULT NULL COMMENT '小程序 appId',
  `name` varchar(32) DEFAULT NULL COMMENT '短剧名称',
  `group_name` varchar(32) DEFAULT NULL COMMENT '短剧分组（例如点众 / 柠七）',
  `pic` text COMMENT '封面图',
  `url` text COMMENT '跳转链接',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `type` varchar(8) DEFAULT NULL COMMENT '卡片类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='落地页短剧';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vip_details`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vip_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `vip_name_type` int(11) DEFAULT NULL COMMENT '会员类型0月/1季/2年',
  `money` decimal(10,2) DEFAULT NULL COMMENT '会员价格',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-16 16:38:00


-- ---------- base config data ----------
-- MySQL dump 10.13  Distrib 5.7.44, for Linux (x86_64)
--
-- Host: localhost    Database: duanju_yizhao
-- ------------------------------------------------------
-- Server version	5.7.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `ad_app`
--

LOCK TABLES `ad_app` WRITE;
/*!40000 ALTER TABLE `ad_app` DISABLE KEYS */;
INSERT INTO `ad_app` (`app_id`, `app_type`, `app_name`, `is_open_link`, `link_id`, `start_time`, `next_time`, `ad_unit_ids`, `activity_id`, `im_id`, `created_at`, `created_by`, `modified_at`, `deleted`) VALUES ('dsfsf','DOUYIN','测试1',0,NULL,3000,3000,NULL,NULL,NULL,'2026-03-04 08:52:22',NULL,'2026-03-04 08:52:22',0);
/*!40000 ALTER TABLE `ad_app` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ad_customer`
--

LOCK TABLES `ad_customer` WRITE;
/*!40000 ALTER TABLE `ad_customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `ad_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ad_link`
--

LOCK TABLES `ad_link` WRITE;
/*!40000 ALTER TABLE `ad_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `ad_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ad_play`
--

LOCK TABLES `ad_play` WRITE;
/*!40000 ALTER TABLE `ad_play` DISABLE KEYS */;
/*!40000 ALTER TABLE `ad_play` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ad_play_material`
--

LOCK TABLES `ad_play_material` WRITE;
/*!40000 ALTER TABLE `ad_play_material` DISABLE KEYS */;
/*!40000 ALTER TABLE `ad_play_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ad_play_series`
--

LOCK TABLES `ad_play_series` WRITE;
/*!40000 ALTER TABLE `ad_play_series` DISABLE KEYS */;
/*!40000 ALTER TABLE `ad_play_series` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `common_info`
--

LOCK TABLES `common_info` WRITE;
/*!40000 ALTER TABLE `common_info` DISABLE KEYS */;
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`, `desc`, `remark`) VALUES (1,'2020-03-29 00:18:08',NULL,'客服二维码',1,'https://your-cdn-domain.com/custom.jpg','image',NULL,NULL),(2,'2020-02-21 21:17:17',NULL,'公众号二维码',2,'https://your-cdn-domain.com/erweima.jpg','image',NULL,NULL),(3,'2020-11-04 10:31:40',NULL,'注册邀请码是否必填',3,'否','kaiguan',NULL,NULL),(5,'2024-01-28 16:04:43',NULL,'微信公众号APPID	',5,'__WX_APPID__','weixin',NULL,NULL),(16,'2024-01-28 16:04:49',NULL,'微信公众号秘钥	',21,'__WX_SECRET__','weixin',NULL,NULL),(17,'2024-02-28 20:16:11',NULL,'公众号Token',16,'__WX_TOKEN__','weixin',NULL,NULL),(18,'2024-02-28 20:16:22',NULL,'公众号EncodingAESKey	',17,'__WX_AESKEY__','weixin',NULL,NULL),(20,'2024-01-09 16:54:25',NULL,'后台管理平台域名配置	',20,'https://your-admin-domain.com','xitong',NULL,NULL),(21,'2024-01-09 16:54:28',NULL,'h5服务域名配置	',19,'https://your-admin-domain.com','xitong',NULL,NULL),(22,'2024-01-28 19:02:11',NULL,'短信服务商（1 开启腾讯云 2 开启阿里云 3短信宝）',79,'2','duanxin',NULL,NULL),(23,'2024-01-09 16:54:31',NULL,'后台服务名称	',12,'https://your-admin-domain.com','xitong',NULL,NULL),(33,'2021-03-15 21:19:27',NULL,'腾讯云短信clientId	',31,NULL,'duanxin',NULL,NULL),(34,'2020-03-28 00:47',NULL,'腾讯云短信clientSecret	',32,NULL,'duanxin',NULL,NULL),(47,'2024-04-08 18:42:37',NULL,'微信小程序APPID',45,'__WX_APPID_2__','weixin',NULL,NULL),(48,'2024-04-08 18:42:50',NULL,'微信小程序秘钥',46,'__WX_SECRET_2__','weixin',NULL,NULL),(51,'2020-03-29 00:21',NULL,'分享安卓下载地址',49,'__ANDROID_DOWNLOAD_URL__','xitongs',NULL,NULL),(52,'2020-03-29 00:21',NULL,'分享苹果下载地址',50,'__IOS_DOWNLOAD_URL__','xitongs',NULL,NULL),(58,'2020-03-29 00:21',NULL,'开启微信登录',53,'是','xitongs',NULL,NULL),(69,'2020-06-04 16:34',NULL,'APP消息推送PushAppKey',60,'__UMENG_PUSH_APPKEY__','push',NULL,NULL),(70,'2020-06-04 16:34',NULL,'APP消息推送PushAppId',61,'__UMENG_PUSH_APPID__','push',NULL,NULL),(71,'2020-06-04 16:34',NULL,'APP消息推送PushMasterSecret',62,'__UMENG_PUSH_MASTERSECRET__','push',NULL,NULL),(72,'2024-01-09 16:27:11',NULL,'企业支付宝APPID',63,'__ALIPAY_APP_ID__','zhifubao',NULL,NULL),(73,'2024-01-09 16:25:21',NULL,'企业支付宝公钥',64,'__ALIPAY_PUBLIC_KEY__','zhifubao',NULL,NULL),(74,'2024-01-09 16:25:33',NULL,'企业支付宝商户秘钥',65,'__ALIPAY_PRIVATE_KEY__','zhifubao',NULL,NULL),(77,'2023-12-12 13:02:45',NULL,'文件上传阿里云Endpoint',68,'http://oss-cn-hangzhou-internal.aliyuncs.com','oss',NULL,NULL),(78,'2023-12-13 14:21:43',NULL,'文件上传阿里云账号accessKeyId',69,'__OSS_ACCESS_KEY_ID__','oss',NULL,NULL),(79,'2023-12-13 14:21:50',NULL,'文件上传阿里云账号accessKeySecret',70,'__OSS_ACCESS_KEY_SECRET__','oss',NULL,NULL),(80,'2023-12-12 13:02:18',NULL,'文件上传阿里云Bucket名称',71,'__OSS_BUCKET__','oss',NULL,NULL),(81,'2023-12-12 13:02:09',NULL,'文件上传阿里云Bucket域名',72,'https://__OSS_BUCKET__.oss-cn-hangzhou-internal.aliyuncs.com','oss',NULL,NULL),(83,'2020-07-27 15:17',NULL,'微信APPappId',74,NULL,'weixin',NULL,NULL),(84,'2024-01-30 20:37:19',NULL,'微信商户key',75,'__WXPAY_API_KEY__','weixin',NULL,NULL),(85,'2024-01-30 19:58:35',NULL,'微信商户号mchId',76,'__WXPAY_MCH_ID__','weixin',NULL,NULL),(88,'2021-02-24 18:46:57',NULL,'官方邀请码',88,'666666','xitong',NULL,NULL),(89,'2023-08-28 11:23:02',NULL,'会员赏金百分比',80,'0.5','fuwufei',NULL,NULL),(90,'2024-01-28 19:10:24',NULL,'短信签名',81,'示例科技','duanxin',NULL,NULL),(96,'2020-07-27 15:17',NULL,'阿里云登陆或注册模板code（开启阿里云短信必须配置）',82,'__SMS_TPL_LOGIN__','duanxin',NULL,NULL),(97,'2020-07-27 15:17',NULL,'阿里云找回密码模板code（开启阿里云短信必须配置）',83,'__SMS_TPL_RESETPWD__','duanxin',NULL,NULL),(98,'2020-07-27 15:17',NULL,'阿里云绑定手机号模板code（开启阿里云短信必须配置）',84,'__SMS_TPL_BINDPHONE__','duanxin',NULL,NULL),(99,'2020-07-27 15:17',NULL,'阿里云短信accessKeyId',85,'__SMS_ACCESS_KEY_ID__','duanxin',NULL,NULL),(100,'2020-07-27 15:17',NULL,'阿里云短信accessSecret',86,'__SMS_ACCESS_SECRET__','duanxin',NULL,NULL),(101,'2020-07-27 15:17',NULL,'支付宝转账方式 1证书 2秘钥 3手动',98,'3','zhifubao',NULL,NULL),(102,'2023-06-06 16:50:22',NULL,'初始签到积分',102,'100','fuwufei',NULL,NULL),(103,'2020-07-27 15:17',NULL,'积分签到累计增加',103,'10','fuwufei',NULL,NULL),(104,'2020-07-27 15:17',NULL,'积分兑换比例 1元等于',104,'100','fuwufei',NULL,NULL),(124,'2024-02-01 02:41:15',NULL,'是否开启公众号自动登陆',108,'是','kaiguan',NULL,NULL),(125,'2020-07-27 15:17',NULL,'是否开启公众号自动注册',109,'是','kaiguan',NULL,NULL),(130,'2020-11-04 10:31:40',NULL,'提现最低额度',112,'1','xitong',NULL,NULL),(140,'2020-07-27 15:17',NULL,'腾讯地图key',128,'__TENCENT_MAP_KEY__','xitong',NULL,NULL),(170,'2020-11-04 10:31:40',NULL,'提现手续费',152,'0.01','fuwufei',NULL,NULL),(171,'2020-11-04 10:31:40',NULL,'最高提现金额',153,'10000','fuwufei',NULL,NULL),(172,'2026-02-04 14:20:40',NULL,'用户协议',154,'<p>欢迎注册！在您注册过程中，您需要完成我们的注册流程并通过点击同意的形式在线签署以下协议，请您务必仔细阅读、充分理解协议中的条款内容后再点击同意。</p><p>【请您注意】如果您不同意以下协议全部或任何条款约定，请您停止注册。您停止注册后将仅可以浏览我们的商品信息但无法享受我们的产品或服务。如您按照注册流程提示填写信息，阅读并点击同意上述协议且完成全部注册流程后，即表示您已充分阅读、理解并接受协议的全部内容，并表明您同意我们可以依据协议内容来处理您的个人信息）。如您对以下协议内容有任何疑问，您可随时通过《隐私保护政策》中的联系方式联系我们。</p><p>隐私保护政策</p><p>本隐私保护政策更新日期：2024年8月16日</p><p>本隐私保护政策生效日期：2024年8月16日</p><p>本隐私保护政策版本：1.0.0</p><p>提示条款</p><p>${company_name:示例文化科技有限公司}及其关联公司（包括且不限于示例科技有限公司）（以下简称“${simple_company_name:示例文化科技}”）非常重视用户个人信息的保护，并恪守以下原则，保护您的个人信息：权责一致原则、目的明确原则、选择同意原则、最小必要原则、确保安全原则、主体参与原则、公开透明原则等。您在使用本产品的产品或服务时，本产品可能会收集和使用您的相关信息。请您务必仔细阅读并透彻理解本政策。为了更好的改进产品体验，同时持续提供用户最佳的服务。在不涉及用户个人隐私及敏感数据的前提下， 本产品会采用如下方式收集和使用您的设备和其他相关信息，本政策将具体说明和解释这些信息的收集和使用情况。</p><p>本产品的登记著作权人，依法享有本产品的著作权。</p><p>本产品由${simple_company_name:示例文化科技}运营并向您提供阅读服务，并由${simple_company_name:示例文化科技}对您依法享有权利、履行义务、承担法律责任。</p><p>本隐私保护政策旨在帮助您了解我们会收集哪些数据、为什么收集这些数据、会利用这些数据做什么以及我们如何保护这些数据。了解这些内容，对于您行使个人权利及保护您的个人信息至关重要，请您在使用本产品产品或服务前务必抽出时间认真阅读本政策。本协议是您与本产品经营者-${company_name:示例文化科技有限公司}就您使用本产品产品或服务达成的协议。请注意，您可以通过产品中的设置功能或控制项对您的信息进行动态管理。我们同时提醒您，当您通过本产品产品或服务使用第三方产品或服务时，您的信息应当适用第三方的隐私条款。</p><p>&nbsp;</p><p>第一部分 定义</p><p>&nbsp;</p><p>本产品：指服务提供者开发、运营的所有小剧场产品及相关服务，包括但不限于${app_name:短剧小剧场}及其他由本公司提供的各类小剧场应用。本产品直接向用户提供的各项信息技术服务内容，包括商品的相关在线服务，评论信息的存储服务，本地内容的阅读服务，以及根据用户选择而为用户提供的可以跳转使用第三方服务的链接服务，以及本产品不断新推出的服务的平台或终端。如用户通过链接服务进而使用第三方服务的，应与第三方另行达成隐私保护协议。</p><p>本产品服务提供者：${company_name:示例文化科技有限公司}及其关联公司（包括且不限于示例科技有限公司）。</p><p>关联公司：指是指一方现在或将来控制、受控制或与其处于共同控制下的任何公司、合法机构以及上述公司、合法机构的合法继承人。控制是指（1）直接或间接持有过半数的投票权；或（2）直接或间接享有过半数的可分配利润；或（3）直接或间接控制董事会半数以上的成员的组成；或（4）直接或间接持有半数的注册资本。</p><p>个人信息：指以电子或者其他方式记录的能够单独或者与其他信息结合识别特定自然人身份或者反映特定自然人活动情况的各种信息。</p><p>个人敏感信息：包括身份证件号码、个人生物识别信息、银行账号、财产信息、行踪轨迹、交易信息、14岁以下（含）儿童信息等的个人信息（我们将在本隐私保护政策中对具体个人敏感信息以粗体进行显著标识）。</p><p>第二部分 隐私保护政策</p><p>本隐私保护政策部分将帮助您了解以下内容：</p><p>1、我们如何收集和使用您的个人信息</p><p>2、我们所收集的信息</p><p>3、我们如何使用Cookie和同类技术</p><p>4、我们如何共享、转让、公开披露您的个人信息</p><p>5、我们如何保护您的个人信息</p><p>6、您如何管理您的个人信息</p><p>7、我们如何处理未成年人的个人信息</p><p>8、我们如何存储您的个人信息</p><p>9、本隐私保护政策如何更新</p><p>10、如何联系我们</p><p>一、我们如何收集和使用您的信息</p><p>我们会出于本政策所述的以下目的，收集和使用您的个人信息：</p><p>（一）帮助您成为我们的用户</p><p>1.为成为本产品的用户，以便我们为您提供服务，您需要提供手机号码，并创建用户名和密码。如果您仅需使用浏览、搜索等基本服务，您不需要注册成为我们的用户及提供上述信息。在浏览过程中我们可能收集您使用本产品的设备信息[包括设备型号、软件安装列表、设备Mac地址、设备序列号、设备唯一标识符（设备IMEI、IMSI、OAID、安卓系统封装用户id）、操作系统版本、分辨率、电信运营商]以便为您提供阅读展示的最优方案。此外，在您使用在浏览阅读功能的过程中，我们会自动收集您使用本产品的详细情况，并作为有关的日志保存，包括但不限于您输入的搜索关键词信息和点击的链接，您浏览的内容，您使用的语言、访问的日期和时间、及您请求的网页记录、操作系统、软件版本号、登录IP信息。</p><p>您注册本产品帐号时，可能会向我们提供昵称、密码、手机号码，成为本产品用户后，您将会享受更多专属服务。如果您提供微信、微博、QQ等进行绑定，当您遇到帐号丢失、忘记密码等帐号问题时，可以更便捷的找回帐号和密码。但如果您不提供这些信息，将不会影响您使用本产品提供的基础服务，但可能会影响到您使用本产品需要进行实名认证、个性化服务等的附加功能。</p><p>（二）为您展示和推送产品或服务</p><p>为改善我们的产品或服务、向您提供个性化的服务，我们会根据您的浏览及搜索记录、设备信息、订阅信息，提取您的浏览、阅读、搜索偏好、行为习惯、位置信息等信息作特征分析和用户画像，以便为您提供更合适的定制化服务和短信或者语音电话营销。例如，通过我方APP端口内和短信、语音向您展现或推荐相关程度更高的搜索结果、信息流或广告及您可能感兴趣的信息（包括金融、教育等相关服务推荐）。我们可能将您的画像信息与您授权的其他信息结合起来，更好的满足您的需求。</p><p>我们将在向用户推荐合作方产品或服务时向该合作方共享用户信息，包括去标识化或匿名化后的设备信息、手机号。合作方亦会根据我们提供的用户信息向其他合法留存您个人信息的合作机构查询用户的个人信息用于用户画像、数据建模、联邦学习等，并将加工后的用户画像等数据进行存留。</p><p>合作机构：运营服务商（中国移动、中国联通、中国电信）、证券公司、第三方金融机构（顶点财经）、关联公司，已与我们联合合作并提供产品/服务。通过使用您同意我们收集的个人信息（注册信息、网页浏览信息、我们自行合法采集的个人信息），我们得以向您提供其它的商业营销内容，您知悉这是我们面向用户提供的主要服务。您同意我们向您展现或触达我们的商业合作伙伴提供的产品及服务的信息流或者广告/推广信息。具体我们可能会通过短信、电话或其它方式向您提供您可能感兴趣的商业广告、商业活动等商业性信息。例如，我们可能会基于您的偏好特征向您触达培训、教育、保险、金融理财、电商等领域的产品或服务的商业信息。我们会严格保护您的个人信息，不会超出法律法规和本政策的内容将您的个人信息提供给第三方。</p><p>如果您不想接收我们的推荐信息或电话，您可以通过以下方式要求取消营销服务：</p><p>1、联系客服电话13325716083进行取消；</p><p>2、发送取消服务请求至客服邮箱(mzwork01@163.com);</p><p>3、若您已收到相关营销服务短信，可按短信内容提示“退订回T”、“退订回D”等方式进行取消；</p><p>4、注销账号等方式均可取消营销服务；</p><p>（三）订购与支付</p><p>您通过本产品订购会员等产品时产生的记录，包括订购记录、消费记录，这些信息会存储于我们的服务器中。以上信息为软件功能必须的信息，如您不同意我们记录前述信息，我们将无法为您提供相关商品购买和使用服务。如您使用订购与支付功能，第三方向您提供支付服务的公司将直接收集与支付相关的信息。在使用银行卡或信用卡支付时，需要提供姓名、银行卡号、身份证号、手机号码。</p><p>（四）搜索联想推荐</p><p>当您在搜索栏输入查询时，本产品会将您键入的文字上传到本产品的服务器。这样，“建议”功能就能自动向您推荐您可能正要查找的字词。</p><p>（五）书签备份及同步功能</p><p>本产品将为您提供书签于本产品的数据服务器上的备份以及同步功能。为保证您数据的安全，本产品会对数据服务器采取严密的安全保护措施。</p><p>（六）版本检查更新</p><p>本产品会在您启动本产品软件并联网后定期与本产品的服务器进行通信，查询是否有最新可以升级的版本，发现新版本会提示您下载安装，如果您不想升级可以选择拒绝，如果您选择升级可以第一时间使用本产品最新最稳定的版本。</p><p>（七）用户意见反馈</p><p>您在使用本产品时，可以选择向本产品反馈意见。在您提交的意见中，本产品会上传手机机型、软件版本号和操作系统的版本号，以便更好对您提出的问题进行定位分析。您在反馈意见中所填写的联系方式，只会用于本产品的客服与您沟通与反馈，绝对不会泄露给任何第三方。</p><p>（八）其他用途</p><p>我们将信息用于本政策未载明的其他用途，或者将基于特定目的收集而来的信息用于其他目的时，会事先征求您的同意。</p><p>二、 我们所收集的信息</p><p>（一）、 您向我们提供的信息</p><p>本产品的很多服务都要求您注册本产品帐户。而在您注册本产品帐户时，本产品可能会要求您提供以下个人信息，包括您的昵称、头像、电话号码。如果您仅需使用浏览、搜索等基本服务，您不需要注册成为我们的用户及提供上述信息。</p><p>（二）、我们在您使用服务过程中收集的信息</p><p>为向您提供安全稳定的服务、保障您的账户财产安全、预防冒名登陆，我们需要记录您的设备型号、IP地址、设备软件版本信息、设备识别码、设备Mac地址。</p><p>您在使用本软件时，为提供、处理、维护、改善、开发我们的商品和提供给您服务之目的，我们会收集关于您使用的服务以及使用方式的信息并将这些信息进行关联，这些信息包括：</p><p>设备信息：我们会根据您在软件安装及使用中授予的具体权限，在APP启动或置于后台时接收并记录您所使用的设备相关信息[包括设备型号、软件列表、设备Mac地址、设备序列号、操作系统版本、设备设置、唯一设备标识符（包括手机号码）等软硬件特征信息]、设备所在位置相关信息（包括IP地址、GPS位置信息）。使本软件功能可以兼容不同系统和机型、确定未登陆前用户标识、判断程序崩溃原因、优化内容展示效果，提升阅读体验。</p><p>日志信息：当您使用我们的网站或客户端提供的产品或服务时，我们会自动收集您对我们服务的详细使用情况，作为有关网络日志保存。包括您的搜索查询内容、IP地址、浏览器的类型、电信运营商、使用的语言、访问日期和时间及您访问的网页记录。</p><p>具体权限信息：您在使用本软件时，为给您提供服务之目的，本软件可能获取您终端设备的如下权限：</p><p>允许程序挂载、反挂载外部文件系统，使本软件可存储外置SD卡；</p><p>允许访问网络权限，使本软件可正常联网；</p><p>允许访问网络状态权限，使本软件获取当前网络连接状态；</p><p>允许程序改变网络状态权限，以便本软件提供“一键登录”功能；</p><p>允许程序改变WIFI权限状态，使本软件获取当前WiFi连接状态；</p><p>允许程序获取当前WiFi接入的状态以及WLAN热点的信息，使本软件获取当前WiFi连接状态及热点相关信息；</p><p>允许程序振动，使本软件向用户推送通知时，可以震动提醒；</p><p>允许程序获取当前或最近执行的任务信息，使本软件可以判断程序奔溃原因；</p><p>允许程序读取或写入系统设置，使本软件可以在用户使用夜间模式时，更改用户界面设置，修改手机亮度；</p><p>创建快捷方式权限，使本软件可以新建本产品快捷方式到屏幕；</p><p>允许程序开机自动运行，使本软件在开机时能够正常向用户及时发送订阅更新提醒，消息提醒；</p><p>允许应用程序请求安装包，使本软件可以进行应用内升级APP以及第三方广告下载安装；</p><p>允许程序在手机锁屏后进程仍然允许，使本软件可提供推送功能、语音听书功能；</p><p>允许修改屏幕亮度，使本软件可修改夜间模式和日间模式屏幕亮度；</p><p>允许程序调用killBackgroundProcesses(String).方法结束后台进程，使本软件退出APP后结束后台进程；</p><p>允许程序获取手机安装列表，使本软件广告第三方判断是否将安装APP进行打开；</p><p>允许程序访问电话状态权限，通过获取您的电话状态权限以便我们记录您的设备识别码（即IMEI或IMSI）进行匿名化处理，用于统计和账户安全风控；</p><p>允许访问相册权限，使用户进行反馈和更换头像时，选择相册里图片进行上传；</p><p>允许访问本地存储，通过开启本地存储权限，实现下载本软件内的短剧剧集到您的手机存储，以及通过手机存储将您的本地短剧剧集上传到本软件；</p><p>第三方SDK/API接入服务所需开通的权限。我们的产品和服务可能包括第三方的产品和服务，以及第三方网站的链接。当您使用这些产品或服务时，也可能收集您的信息。点击查看目前我们接入的第三方SDK/API详情。</p><p>&nbsp;</p><p>请注意，单独的设备信息、日志信息等是无法识别特定自然人身份的信息。如果我们将这类非个人信息与其他信息结合用于识别特定自然人身份，或者将其与个人信息结合使用，则在结合使用期间，这类非个人信息将被视为个人信息，除取得您授权或法律法规另有规定外，我们会将该类个人信息做匿名化、去标识化处理。</p><p>为展示您账户的阅读历史信息，我们会收集您在使用我们服务过程中产生的阅历信息用于向您展示及便于您对书单进行管理。</p><p>当您与我们联系时，我们可能会保存您的通信/通话记录和内容或您留下的联系方式等信息，以便与您联系或帮助您解决问题，或记录相关问题的处理方案及结果。</p><p>当您长时间不使用我们的产品时，我们可能会通过您注册时提供的手机号码推送新闻更新提醒等信息。</p><p>（三）征得授权同意的例外</p><p>根据相关法律法规规定，以下情形中收集您的个人信息无需征得您的授权同意：</p><p>1、与国家安全、国防安全有关的；</p><p>2、与公共安全、公共卫生、重大公共利益有关的；</p><p>3、与犯罪侦查、起诉、审判和判决执行等有关的；</p><p>4、出于维护个人信息主体或其他个人的生命、财产等重大合法权益但又很难得到您本人同意的；</p><p>5、所收集的个人信息是您自行向社会公众公开的；</p><p>6、从合法公开披露的信息中收集个人信息的，如合法的新闻报道、政府信息公开等渠道；</p><p>7、法律法规规定的其他情形。</p><p>&nbsp;</p><p>如我们停止运营本产品的功能或服务，我们将及时停止继续收集您个人信息的活动，将停止运营的通知以逐一送达或公告的形式通知您，对所持有的个人信息进行删除或匿名化处理。</p><p>&nbsp;</p><p>三、我们如何使用Cookie和同类技术</p><p>为确保本产品正常运转、为您获得更轻松的访问体验、向您推荐您可能感兴趣的内容，我们会在您的计算机或移动设备上存储名为Cookie的小数据文件。Cookie通常包含标识符、站点名称以及一些号码和字符。借助于Cookie，网站能够存储您的偏好等数据。</p><p>您可根据自己的偏好管理或删除Cookie。您可以清除计算机上保存的所有Cookie，大部分网络浏览器都设有阻止Cookie的功能。但如果您这么做，则需要在每一次访问我们的网站时更改用户设置。如需详细了解如何更改浏览器设置，请访问您使用的浏览器的相关设置页面。</p><p>四、我们如何共享、转让、公开披露您的个人信息</p><p>（一）共享</p><p>我们不会与本产品服务提供者以外的公司、组织和个人共享您的个人信息，但以下情况除外：</p><p>1、在获取明确同意的情况下共享：获得您的明确同意后，我们会与其他方共享您的个人信息。</p><p>2、在法定情形下的共享：我们可能会根据法律法规规定、诉讼争议解决需要，或按行政、司法机关依法提出的要求，对外共享您的个人信息。</p><p>3、用于外部处理。本产品可能会向本产品的关联公司或其他可信赖的商家或个人提供用户个人信息，让他们根据本产品的指示并遵循本产品的隐私保护政策以及其他任何相应的保密和安全措施来为本产品处理这些信息。</p><p>本产品可能会与公众以及合作伙伴（发布商、广告客户或关联网站）分享汇总的非个人身份识别信息。包括，本产品可能会与公众分享某些脱敏信息，以展示本产品服务的整体使用趋势。</p><p>（二）转让</p><p>我们不会将您的个人信息转让给任何公司、组织和个人，但以下情况除外：</p><p>（1）在获取明确同意的情况下转让：获得您的明确同意后，我们会向其他方转让您的个人信息；</p><p>（2）在本产品服务提供者发生合并、收购或破产清算情形，或其他涉及合并、收购或破产清算情形时，如涉及到个人信息转让，我们会要求新的持有您个人信息的公司、组织继续受本政策的约束，否则我们将要求该公司、组织和个人重新向您征求授权同意。</p><p>（三）公开披露</p><p>我们仅会在以下情况下，公开披露您的个人信息：</p><p>（1）获得您明确同意或基于您的主动选择，我们可能会公开披露您的个人信息；</p><p>（2）如果我们确定您出现违反法律法规或严重违反本产品相关协议规则的情况，或为保护本产品及其关联公司用户或公众的人身财产安全免遭侵害，我们可能依据法律法规或本产品相关协议规则征得您同意的情况下披露关于您的个人信息，包括相关违规行为以及本产品已对您采取的措施。</p><p>（四）共享、转让、公开披露个人信息时事先征得授权同意的例外</p><p>以下情形中，共享、转让、公开披露您的个人信息无需事先征得您的授权同意：</p><p>1、与国家安全、国防安全有关的；</p><p>2、与公共安全、公共卫生、重大公共利益有关的；</p><p>3、与犯罪侦查、起诉、审判和判决执行等有关的；</p><p>4、出于维护您或其他个人的生命、财产等重大合法权益但又很难得到本人同意的；</p><p>5、您自行向社会公众公开的个人信息；</p><p>6、从合法公开披露的信息中收集个人信息的，如合法的新闻报道、政府信息公开等渠道。</p><p>根据法律规定，共享、转让经去标识化处理的个人信息，且确保数据接收方无法复原并重新识别个人信息主体的，不属于个人信息的对外共享、转让及公开披露行为，对此类数据的保存及处理将无需另行向您通知并征得您的同意。</p><p>&nbsp;</p><p>五、我们如何保护您的个人信息安全</p><p>（1）本产品会采取适当的符合业界标准的安全措施和技术手段存储和保护您的个人信息，以防止其丢失、被误用、受到未授权访问或泄漏、被篡改或毁坏，如通过网络安全层技术SSL进行加密传输、信息加密存储、严格限制数据中心的访问、使用专用网络通道及网络代理。您的个人信息存放在有密码控制的服务器中，访问均是受到限制的。</p><p>（2）我们设立了个人信息保护责任部门，建立了相关内控制度，对可能接触到您的信息的工作人员采用最小够用授权原则。即我们仅允许有必要知晓这些信息的本产品和其关联公司的员工在采取合理的措施验证身份之后，访问或修改这些信息。同时，我们会严格要求他们履行保密及安全义务，如果未能履行这些义务，其会被追究法律责任或被终止与本产品的合作关系。</p><p>（3）为了保护您的信息安全，如果用户个人信息有误，本产品会在严格验证并核实申请人身份后，根据用户要求访问、修正或删除相关信息（除非本产品出于合法的原因而必须保留这些个人信息）。</p><p>（4）我们会采取合理可行的措施，尽力避免收集无关的个人信息。我们只会在达成本政策所述目的所需的期限内保留您的个人信息，除非需要延长保留期或受到法律的允许。</p><p>（5）互联网并非绝对安全的环境，电子邮件、即时通讯、社交软件、交易平台等与其他用户的交流方式无法确定是否完全加密，请您在进行交互使用时，注意保护您个人信息的安全。</p><p>（6）请您理解，由于计算机及互联网技术的飞速发展及同步速度的限制，可能存在或出现各种恶意或非恶意的攻击手段。虽然本产品持续致力于提升和加强安全措施，以保护您的信息免遭意外或破坏，但仍无法始终保证信息的百分之百安全。</p><p>（7）您使用产品或服务时所用的系统和通讯网络，或硬件设备等本产品均无法控制，请您了解并注意保护您的个人信息安全。</p><p>（8）请注意，您在使用我们服务时自愿共享甚至公开分享的信息，可能会涉及您或他人的个人信息甚至个人敏感信息，如您在评价时选择上传包含个人信息的图片。请您更加谨慎地考虑，是否在使用我们的服务时共享甚至公开分享相关信息。</p><p>请使用复杂密码，协助我们保证您的账号安全。我们将尽力保障您发送给我们的任何信息的安全性。如果我们的物理、技术或管理防护设施遭到破坏，导致信息被非授权访问、公开披露、篡改或毁坏，导致您的合法权益受损，我们将承担相应的法律责任。</p><p>（9）在不幸发生个人信息安全事件后，我们将按照法律法规的要求向您告知：安全事件的基本情况和可能的影响、我们已采取或将要采取的处置措施、您可自主防范和降低风险的建议、对您的补救措施等。事件相关情况我们将以邮件、信函、电话、推送通知等方式告知您，难以逐一告知个人信息主体时，我们会采取合理、有效的方式发布公告。</p><p>同时，我们还将按照监管部门要求，上报个人信息安全事件的处置情况。</p><p>&nbsp;</p><p>六、您如何管理您的个人信息</p><p>您可以通过以下方式访问及管理您的个人信息：</p><p>（一）访问您的个人信息</p><p>（1）您有权访问您的个人信息，法律法规规定的例外情况除外。您可以通过以下方式自行访问您的个人信息：</p><p>账户信息：如果您希望访问或编辑您的账户中的个人基本资料信息、查看连续包月退订说明、更改您的密码等，进入本产品后，点击“我的”—“头像”进入账号信息页面执行此类操作。</p><p>阅历、收藏信息：您可以进入本产品后，点击“我的”-“我的工具”-点击“浏览历史”或“收藏”中查阅或清除您的阅读记录、收藏记录等。</p><p>（2）如果您无法通过上述路径访问该等个人信息，您可以随时通过在线人工客服与我们取得联系。我们将在合理时间内回复您的访问请求。</p><p>（3）对于您在使用我们的产品或服务过程中产生的其他个人信息，我们将根据本条“（五）响应您的上述请求”中的相关安排向您提供。</p><p>（二）更正或补充您的个人信息</p><p>当您发现我们处理的关于您的个人信息有错误时，您有权要求我们做出更正或补充。您可以通过“（一）访问您的个人信息”中列明的方式提出更正或补充申请。</p><p>（三）改变或撤回您的授权</p><p>每个业务功能需要一些基本的个人信息才能得以完成（见本隐私保护政策“第二部分”）。除此之外，您可以通过注销、修改个人设置、删除相关信息等方式撤回部分授权，也可以通过手机的设置中关闭本软件的部分授权。</p><p>当您收回同意后，我们将不再处理相应的个人信息。但您收回同意的决定，不会影响此前基于您的授权而开展的个人信息处理。</p><p>（四）删除您的个人信息</p><p>（1）发生以下情形，您可以向本产品提出删除个人信息的请求：</p><p>如果本产品处理个人信息的行为违反法律法规；</p><p>如果收集、使用您的个人信息却未征得您的同意；</p><p>如果本产品处理个人信息的行为违反了与您的约定；</p><p>如果您不再使用本产品的产品或服务，或您主动注销了账号；</p><p>如果本产品终止产品运营及服务。</p><p>若本产品决定响应您的删除请求，本产品还将同时尽可能通知从本产品处获得您的个人信息的主体，要求其及时删除（除非法律法规另有规定，或这些主体已独立获得您的授权）。</p><p>当您或本产品协助您删除相关信息后，因为适用的法律和安全技术，本产品可能无法立即从备份系统中删除相应的信息，本产品将安全地存储您的个人信息并将其与任何进一步处理隔离，直到备份可以清除或实现匿名。</p><p>（2）注销您的账号</p><p>您可以通过“设置”-“账号安全”-“注销账号”来注销您的账号；</p><p>联系本产品的客服寻求帮助，协助您申请注销您的账号。</p><p>在您主动注销账号之后，本产品将停止为您提供产品或服务，根据适用法律的要求删除您的个人信息，或使其匿名化处理。</p><p>（五）约束信息系统自动决策</p><p>在某些业务功能中，我们可能仅依据信息系统、算法等在内的非人工自动决策机制做出决定。如果这些决定显著影响您的合法权益，您有权要求我们做出解释，我们也将在不侵害本产品商业秘密或其他用户权益、社会公共利益的前提下提供申诉方法。</p><p>（六）响应您的上述请求</p><p>（1）为保障安全，您可能需要提供书面请求，或以其他方式证明您的身份。我们可能会先要求您验证自己的身份，然后再处理您的请求。</p><p>（2）我们将在合理时间内做出答复。如您不满意，还可以通过本隐私政策第十条载明的客服联系方式发起投诉。</p><p>（3）对于您合理的请求，我们原则上不收取费用。对于那些无端重复、需要过多技术手段（例如，需要开发新系统或从根本上改变现行惯例）、给他人合法权益带来风险或者非常不切实际的请求，我们可能会予以拒绝。</p><p>（4）在以下情形中，按照法律法规要求，我们将无法响应您的请求：</p><p>1、与国家安全、国防安全有关的；</p><p>2、与公共安全、公共卫生、重大公共利益有关的；</p><p>3、与犯罪侦查、起诉、审判和执行判决等有关的；</p><p>4、有充分证据表明个人信息主体存在主观恶意或滥用权利的；</p><p>5、响应您的请求将导致您或其他个人、组织的合法权益受到严重损害的；</p><p>6、涉及商业秘密的。</p><p>&nbsp;</p><p>七、我们如何处理未成年人的个人信息</p><p>（1）如果没有父母或监护人的同意，未成年人不得创建自己的用户账户。如您为未成年人的，建议您请您的父母或监护人仔细阅读本隐私保护政策，并在征得您的父母或监护人同意的前提下使用我们的服务或向我们提供信息。</p><p>（2）对于经父母或监护人同意使用我们的产品或服务而收集未成年人个人信息的情况，我们只会在法律法规允许、父母或监护人明确同意或者保护未成年人所必要的情况下使用、共享、转让或披露此信息。</p><p>&nbsp;</p><p>八、您的个人信息如何存储</p><p>（一）信息存储的地点：</p><p>我们在中华人民共和国境内运营中收集和产生的个人信息，存储在中国境内，以下情形除外：</p><p>1、法律法规有明确规定；</p><p>2、获得您的明确授权；</p><p>3、您通过互联网进行跨境交易等个人主动行为。</p><p>针对以上情形，我们会确保依据本隐私保护政策对您的个人信息提供足够的保护。</p><p>（二）信息存储时间：</p><p>我们会采取合理可行的措施，尽力避免收集无关的个人信息。我们只会在达成本政策所述目的所需的期限内保留您的个人信息，除非法律有强制的存留要求。而我们判断前述期限的标准包括：</p><p>1、完成与您相关的交易目的、维护相应交易及业务记录、应对您可能的查询或投诉。</p><p>2、保证我们为您提供服务的安全和质量。</p><p>3、您是否同意更长的留存期间。</p><p>4、是否存在保留期限的其他特别约定。</p><p>在您的个人信息超出保留期间后，我们会根据适用法律的要求删除您的个人信息，或使其匿名化处理。</p><p>九、本隐私保护政策如何更新</p><p>我们的隐私保护政策可能变更。</p><p>（1）未经您明确同意，我们不会限制您按照本隐私保护政策所应享有的权利。</p><p>（2）对于重大变更，我们还会提供更为显著的通知（包括我们会通过官网或程序首页公示的方式进行通知甚至向您提供弹窗提示）。</p><p>本政策所指的重大变更包括但不限于：</p><p>1、我们的服务模式发生重大变化。如处理个人信息的目的、处理的个人信息类型、个人信息的使用方式等；</p><p>2、我们在控制权等方面发生重大变化。如并购重组等引起的所有者变更等；</p><p>3、个人信息共享、转让或公开披露的主要对象发生变化；</p><p>4、您参与个人信息处理方面的权利及其行使方式发生重大变化；</p><p>5、我们负责处理个人信息安全的责任部门、联络方式及投诉渠道发生变化时；</p><p>6、个人信息安全影响评估报告表明存在高风险时。</p><p>十、关于广告和营销协议</p><p>本人同意并授权：</p><p><br></p><p>为向本人提供优质营销服务，示例科技有限公司可将本人的手机号、设备号等信息提供给示例科技相关的第三方合作机构，包含但不限于如中国移动、中国电信、中国联通，阿里云计算（北京）有限责任公司、中国银联股份有限公司、银联智策顾问（上海）有限公司、银联商务支付股份有限公司及其关联公司等，并对本人进行短信、语音外呼、信息流等模式的触达，本人同意接收示例科技有限公司及其第三方合作机构的相关营销信息、广告信息。</p><p><br></p><p>我们将在向用户推荐合作方产品或服务时向该合作方共享用户信息，包括去标识化或匿名化后的设备信息、手机号。合作方亦会根据我们提供的用户信息向其他合法留存您个人信息的合作机构查询用户的个人信息用于用户画像、数据建模、联邦学习等，并将加工后的用户画像等数据进行存留。</p><p>合作机构：运营服务商（中国移动、中国联通、中国电信）、证券公司、第三方金融机构（顶点财经）、关联公司，已与我们联合合作并提供产品/服务。</p><p>&nbsp;</p><p>十一、如何联系我们</p><p>您可以通过以下方式与我们联系，我们将在合理时间天内回复您的请求：</p><p>（1）我们设立了个人信息保护专职部门。如您有任何疑问、意见或建议，您都可以通过帮助与反馈途径联系在线人工客服或通过发送邮件至【mzwork01@163.com】与我们联系。</p><p>（2）如果您对我们的回复不满意，特别是您认为我们的个人信息处理行为损害了您的合法权益，您还可以通过向被告住所地有管辖权的法院提起诉讼来寻求解决方案。</p><p>（3）本产品服务提供者${company_name:示例文化科技有限公司}。</p>','xieyi',NULL,NULL),(173,'2026-02-04 14:20:20',NULL,'隐私协议',155,'<p>更新日期：2024年8月16日</p><p>生效日期：2024年8月16日</p><p>特别提示</p><p>${company_name:示例文化科技有限公司}及其关联公司（包括且不限于示例科技有限公司）（以下简称“${simple_company_name:示例文化科技}”）再次特别提醒您（用户）在注册成为用户之前，请认真阅读本《用户协议》（以下简称“协议”），确保您充分理解本协议中各条款。请您审慎阅读并选择接受或不接受本协议。除非您接受本协议所有条款，否则您无权注册、登录或使用本协议所涉服务。您的注册、登录、使用等行为将视为对本协议的接受，并同意接受本协议各项条款的约束。</p><p>我司非常重视用户个人信息的保护，并恪守以下原则，保护您的个人信息：权责一致原则、目的明确原则、选择同意原则、最小必要原则、确保安全原则、主体参与原则、公开透明原则等。您在使用${app_name:短剧小剧场}（以下称“本产品”，包括但不限于${app_name:短剧小剧场}及其他由本公司提供的各类小剧场应用）的产品或服务时，本产品可能会收集和使用您的相关信息。请您务必仔细阅读并透彻理解本协议。为了更好的改进产品体验，同时持续提供用户最佳的服务。在不涉及用户个人隐私及敏感数据的前提下， 本产品会采用如下方式收集和使用您的设备和其他相关信息，本政策将具体说明和解释这些信息的收集和使用情况。</p><p>第一部分 注册</p><p>1、注册资格：用户须具有法定的相应权利能力和行为能力的自然人、法人或其他组织，能够独立承担法律责任。您完成注册APP平台同意的方式实际使用本平台服务时，即视为您确认自己具备主体资格，能够独立承担法律责任。若因您不具备主体资格，而导致的一切后果，由您及您的监护人自行承担。</p><p>2、注册资料:用户应自行诚信向本站提供注册资料，用户同意其提供的注册资料真实、准确、完整、合法有效，用户注册资料如有变动的，应及时更新其注册资料。如果用户提供的注册资料不合法、不真实、不准确、不详尽的，用户需承担因此引起的相应责任及后果，并且橙心保留终止用户使用本平台各项服务的权利。</p><p>3、用户确认：自身已年满18周岁，并拥有完全民事行为能力。</p><p>第二部分 隐私保护政策</p><p>本隐私保护政策部分将帮助您了解以下内容：</p><p>1、我们如何收集和使用您的个人信息</p><p>2、我们所收集的信息</p><p>3、我们如何使用Cookie和同类技术</p><p>4、我们如何共享、转让、公开披露您的个人信息</p><p>5、我们如何保护您的个人信息</p><p>6、您如何管理您的个人信息</p><p>7、我们如何处理未成年人的个人信息</p><p>8、我们如何存储您的个人信息</p><p>9、本隐私保护政策如何更新</p><p>10、如何联系我们</p><p>一、我们如何收集和使用您的信息</p><p>我们会出于本政策所述的以下目的，收集和使用您的个人信息：</p><p>（一）帮助您成为我们的用户</p><p>1.为成为本产品的用户，以便我们为您提供服务，您需要提供手机号码，并创建用户名和密码。如果您仅需使用浏览、搜索等基本服务，您不需要注册成为我们的用户及提供上述信息。在浏览过程中我们可能收集您使用本产品的设备信息[包括设备型号、软件安装列表、设备Mac地址、设备序列号、设备唯一标识符（设备IMEI、IMSI、OAID、安卓系统封装用户id）、操作系统版本、分辨率、电信运营商]以便为您提供阅读展示的最优方案。此外，在您使用在浏览阅读功能的过程中，我们会自动收集您使用本产品的详细情况，并作为有关的日志保存，包括但不限于您输入的搜索关键词信息和点击的链接，您浏览的内容，您使用的语言、访问的日期和时间、及您请求的网页记录、操作系统、软件版本号、登录IP信息。</p><p>您注册本产品帐号时，可能会向我们提供昵称、密码、手机号码，成为本产品用户后，您将会享受更多专属服务。如果您提供微信、微博、QQ等进行绑定，当您遇到帐号丢失、忘记密码等帐号问题时，可以更便捷的找回帐号和密码。但如果您不提供这些信息，将不会影响您使用本产品提供的基础服务，但可能会影响到您使用本产品需要进行实名认证、个性化服务等的附加功能。</p><p>（二）为您展示和推送产品或服务</p><p>为改善我们的产品或服务、向您提供个性化的服务，我们会根据您的浏览及搜索记录、设备信息、订阅信息，提取您的浏览、阅读、搜索偏好、行为习惯、位置信息等信息作特征分析和用户画像，以便为您提供更合适的定制化服务和短信或者语音电话营销。例如，通过我方APP端口内和短信、语音向您展现或推荐相关程度更高的搜索结果、信息流或广告及您可能感兴趣的信息（包括金融、教育等相关服务推荐）。我们可能将您的画像信息与您授权的其他信息结合起来，更好的满足您的需求。</p><p>我们需要把您的手机号码提供给示例科技相关的第三方合作机构，包含但不限于如中国移动、中国电信、中国联通、银联智策、腾讯、中国银联股份有限公司、银联智策顾问（上海）有限公司、银联商务支付股份有限公司及其关联公司等第三方合作方，用于向上述合作方查询基于合作方数据的用户画像、大致位置信息(如地区代码、城市代码等)、终端设备信息(如终端型号、品牌、os 类型等)、通信行为、APP/域名访问及搜索偏好、流量等数据、消费信息、银行卡交易信息等内容，生成的模型分值。上述模型分值将用于我们更准确地向您推荐商品与服务。通过使用您同意我们收集的个人信息（注册信息、网页浏览信息、我们自行合法采集的个人信息），我们得以向您提供其它的商业营销内容，您知悉这是我们面向用户提供的主要服务。您同意我们向您展现或触达我们的商业合作伙伴提供的产品及服务的信息流或者广告/推广信息。具体我们可能会通过短信、电话或其它方式向您提供您可能感兴趣的商业广告、商业活动等商业性信息。例如，我们可能会基于您的偏好特征向您触达培训、教育、保险、金融理财、电商等领域的产品或服务的商业信息。我们会严格保护您的个人信息，不会超出法律法规和本政策的内容将您的个人信息提供给第三方。</p><p>如果您不想接收我们的推荐信息或电话，您可以通过以下方式要求取消营销服务：</p><p>1、联系客服电话13325716083进行取消；</p><p>2、发送取消服务请求至客服邮箱(mzwork01@163.com);</p><p>3、若您已收到相关营销服务短信，可按短信内容提示“退订回T”、“退订回D”等方式进行取消；</p><p>4、注销账号等方式均可取消营销服务；</p><p>（三）订购与支付</p><p>您通过本产品订购会员等产品时产生的记录，包括订购记录、消费记录，这些信息会存储于我们的服务器中。以上信息为软件功能必须的信息，如您不同意我们记录前述信息，我们将无法为您提供相关商品购买和使用服务。如您使用订购与支付功能，第三方向您提供支付服务的公司将直接收集与支付相关的信息。在使用银行卡或信用卡支付时，需要提供姓名、银行卡号、身份证号、手机号码。</p><p>（四）搜索联想推荐</p><p>当您在搜索栏输入查询时，本产品会将您键入的文字上传到本产品的服务器。这样，“建议”功能就能自动向您推荐您可能正要查找的字词。</p><p>（五）书签备份及同步功能</p><p>本产品将为您提供书签于本产品的数据服务器上的备份以及同步功能。为保证您数据的安全，本产品会对数据服务器采取严密的安全保护措施。</p><p>（六）版本检查更新</p><p>本产品会在您启动本产品软件并联网后定期与本产品的服务器进行通信，查询是否有最新可以升级的版本，发现新版本会提示您下载安装，如果您不想升级可以选择拒绝，如果您选择升级可以第一时间使用本产品最新最稳定的版本。</p><p>（七）用户意见反馈</p><p>您在使用本产品时，可以选择向本产品反馈意见。在您提交的意见中，本产品会上传手机机型、软件版本号和操作系统的版本号，以便更好对您提出的问题进行定位分析。您在反馈意见中所填写的联系方式，只会用于本产品的客服与您沟通与反馈，绝对不会泄露给任何第三方。</p><p>（八）其他用途</p><p>我们将信息用于本政策未载明的其他用途，或者将基于特定目的收集而来的信息用于其他目的时，会事先征求您的同意。</p><p>二、 我们所收集的信息</p><p>（一）、 您向我们提供的信息</p><p>本产品的很多服务都要求您注册本产品帐户。而在您注册本产品帐户时，本产品可能会要求您提供以下个人信息，包括您的昵称、头像、电话号码。如果您仅需使用浏览、搜索等基本服务，您不需要注册成为我们的用户及提供上述信息。</p><p>（二）、我们在您使用服务过程中收集的信息</p><p>为向您提供安全稳定的服务、保障您的账户财产安全、预防冒名登陆，我们需要记录您的设备型号、IP地址、设备软件版本信息、设备识别码、设备Mac地址。</p><p>您在使用本软件时，为提供、处理、维护、改善、开发我们的商品和提供给您服务之目的，我们会收集关于您使用的服务以及使用方式的信息并将这些信息进行关联，这些信息包括：</p><p>设备信息：我们会根据您在软件安装及使用中授予的具体权限，在APP启动或置于后台时接收并记录您所使用的设备相关信息[包括设备型号、软件列表、设备Mac地址、设备序列号、操作系统版本、设备设置、唯一设备标识符（包括手机号码）等软硬件特征信息]、设备所在位置相关信息（包括IP地址、GPS位置信息）。使本软件功能可以兼容不同系统和机型、确定未登陆前用户标识、判断程序崩溃原因、优化内容展示效果，提升阅读体验。</p><p>日志信息：当您使用我们的网站或客户端提供的产品或服务时，我们会自动收集您对我们服务的详细使用情况，作为有关网络日志保存。包括您的搜索查询内容、IP地址、浏览器的类型、电信运营商、使用的语言、访问日期和时间及您访问的网页记录。</p><p>具体权限信息：您在使用本软件时，为给您提供服务之目的，本软件可能获取您终端设备的如下权限：</p><p>允许程序挂载、反挂载外部文件系统，使本软件可存储外置SD卡；</p><p>允许访问网络权限，使本软件可正常联网；</p><p>允许访问网络状态权限，使本软件获取当前网络连接状态；</p><p>允许程序改变网络状态权限，以便本软件提供“一键登录”功能；</p><p>允许程序改变WIFI权限状态，使本软件获取当前WiFi连接状态；</p><p>允许程序获取当前WiFi接入的状态以及WLAN热点的信息，使本软件获取当前WiFi连接状态及热点相关信息；</p><p>允许程序振动，使本软件向用户推送通知时，可以震动提醒；</p><p>允许程序获取当前或最近执行的任务信息，使本软件可以判断程序奔溃原因；</p><p>允许程序读取或写入系统设置，使本软件可以在用户使用夜间模式时，更改用户界面设置，修改手机亮度；</p><p>创建快捷方式权限，使本软件可以新建本产品快捷方式到屏幕；</p><p>允许程序开机自动运行，使本软件在开机时能够正常向用户及时发送订阅更新提醒，消息提醒；</p><p>允许应用程序请求安装包，使本软件可以进行应用内升级APP以及第三方广告下载安装；</p><p>允许程序在手机锁屏后进程仍然允许，使本软件可提供推送功能、语音听书功能；</p><p>允许修改屏幕亮度，使本软件可修改夜间模式和日间模式屏幕亮度；</p><p>允许程序调用killBackgroundProcesses(String).方法结束后台进程，使本软件退出APP后结束后台进程；</p><p>允许程序获取手机安装列表，使本软件广告第三方判断是否将安装APP进行打开；</p><p>允许程序访问电话状态权限，通过获取您的电话状态权限以便我们记录您的设备识别码（即IMEI或IMSI）进行匿名化处理，用于统计和账户安全风控；</p><p>允许访问相册权限，使用户进行反馈和更换头像时，选择相册里图片进行上传；</p><p>允许访问本地存储，通过开启本地存储权限，实现下载本软件内的短剧剧集到您的手机存储，以及通过手机存储将您的本地短剧剧集上传到本软件；</p><p>第三方SDK/API接入服务所需开通的权限。我们的产品和服务可能包括第三方的产品和服务，以及第三方网站的链接。当您使用这些产品或服务时，也可能收集您的信息。点击查看目前我们接入的第三方SDK/API详情。</p><p>&nbsp;</p><p>请注意，单独的设备信息、日志信息等是无法识别特定自然人身份的信息。如果我们将这类非个人信息与其他信息结合用于识别特定自然人身份，或者将其与个人信息结合使用，则在结合使用期间，这类非个人信息将被视为个人信息，除取得您授权或法律法规另有规定外，我们会将该类个人信息做匿名化、去标识化处理。</p><p>为展示您账户的阅读历史信息，我们会收集您在使用我们服务过程中产生的阅历信息用于向您展示及便于您对书单进行管理。</p><p>当您与我们联系时，我们可能会保存您的通信/通话记录和内容或您留下的联系方式等信息，以便与您联系或帮助您解决问题，或记录相关问题的处理方案及结果。</p><p>当您长时间不使用我们的产品时，我们可能会通过您注册时提供的手机号码推送新闻更新提醒等信息。</p><p>（三）征得授权同意的例外</p><p>根据相关法律法规规定，以下情形中收集您的个人信息无需征得您的授权同意：</p><p>1、与国家安全、国防安全有关的；</p><p>2、与公共安全、公共卫生、重大公共利益有关的；</p><p>3、与犯罪侦查、起诉、审判和判决执行等有关的；</p><p>4、出于维护个人信息主体或其他个人的生命、财产等重大合法权益但又很难得到您本人同意的；</p><p>5、所收集的个人信息是您自行向社会公众公开的；</p><p>6、从合法公开披露的信息中收集个人信息的，如合法的新闻报道、政府信息公开等渠道；</p><p>7、法律法规规定的其他情形。</p><p>&nbsp;</p><p>如我们停止运营本产品的功能或服务，我们将及时停止继续收集您个人信息的活动，将停止运营的通知以逐一送达或公告的形式通知您，对所持有的个人信息进行删除或匿名化处理。</p><p>&nbsp;</p><p>三、我们如何使用Cookie和同类技术</p><p>为确保本产品正常运转、为您获得更轻松的访问体验、向您推荐您可能感兴趣的内容，我们会在您的计算机或移动设备上存储名为Cookie的小数据文件。Cookie通常包含标识符、站点名称以及一些号码和字符。借助于Cookie，网站能够存储您的偏好等数据。</p><p>您可根据自己的偏好管理或删除Cookie。您可以清除计算机上保存的所有Cookie，大部分网络浏览器都设有阻止Cookie的功能。但如果您这么做，则需要在每一次访问我们的网站时更改用户设置。如需详细了解如何更改浏览器设置，请访问您使用的浏览器的相关设置页面。</p><p>四、我们如何共享、转让、公开披露您的个人信息</p><p>（一）共享</p><p>我们不会与本产品服务提供者以外的公司、组织和个人共享您的个人信息，但以下情况除外：</p><p>1、在获取明确同意的情况下共享：获得您的明确同意后，我们会与其他方共享您的个人信息。</p><p>2、在法定情形下的共享：我们可能会根据法律法规规定、诉讼争议解决需要，或按行政、司法机关依法提出的要求，对外共享您的个人信息。</p><p>3、用于外部处理。本产品可能会向本产品的关联公司或其他可信赖的商家或个人提供用户个人信息，让他们根据本产品的指示并遵循本产品的隐私保护政策以及其他任何相应的保密和安全措施来为本产品处理这些信息。</p><p>本产品可能会与公众以及合作伙伴（发布商、广告客户或关联网站）分享汇总的非个人身份识别信息。包括，本产品可能会与公众分享某些脱敏信息，以展示本产品服务的整体使用趋势。</p><p>（二）转让</p><p>我们不会将您的个人信息转让给任何公司、组织和个人，但以下情况除外：</p><p>（1）在获取明确同意的情况下转让：获得您的明确同意后，我们会向其他方转让您的个人信息；</p><p>（2）在本产品服务提供者发生合并、收购或破产清算情形，或其他涉及合并、收购或破产清算情形时，如涉及到个人信息转让，我们会要求新的持有您个人信息的公司、组织继续受本政策的约束，否则我们将要求该公司、组织和个人重新向您征求授权同意。</p><p>（三）公开披露</p><p>我们仅会在以下情况下，公开披露您的个人信息：</p><p>（1）获得您明确同意或基于您的主动选择，我们可能会公开披露您的个人信息；</p><p>（2）如果我们确定您出现违反法律法规或严重违反本产品相关协议规则的情况，或为保护本产品及其关联公司用户或公众的人身财产安全免遭侵害，我们可能依据法律法规或本产品相关协议规则征得您同意的情况下披露关于您的个人信息，包括相关违规行为以及本产品已对您采取的措施。</p><p>（四）共享、转让、公开披露个人信息时事先征得授权同意的例外</p><p>以下情形中，共享、转让、公开披露您的个人信息无需事先征得您的授权同意：</p><p>1、与国家安全、国防安全有关的；</p><p>2、与公共安全、公共卫生、重大公共利益有关的；</p><p>3、与犯罪侦查、起诉、审判和判决执行等有关的；</p><p>4、出于维护您或其他个人的生命、财产等重大合法权益但又很难得到本人同意的；</p><p>5、您自行向社会公众公开的个人信息；</p><p>6、从合法公开披露的信息中收集个人信息的，如合法的新闻报道、政府信息公开等渠道。</p><p>根据法律规定，共享、转让经去标识化处理的个人信息，且确保数据接收方无法复原并重新识别个人信息主体的，不属于个人信息的对外共享、转让及公开披露行为，对此类数据的保存及处理将无需另行向您通知并征得您的同意。</p><p>&nbsp;</p><p>五、我们如何保护您的个人信息安全</p><p>（1）本产品会采取适当的符合业界标准的安全措施和技术手段存储和保护您的个人信息，以防止其丢失、被误用、受到未授权访问或泄漏、被篡改或毁坏，如通过网络安全层技术SSL进行加密传输、信息加密存储、严格限制数据中心的访问、使用专用网络通道及网络代理。您的个人信息存放在有密码控制的服务器中，访问均是受到限制的。</p><p>（2）我们设立了个人信息保护责任部门，建立了相关内控制度，对可能接触到您的信息的工作人员采用最小够用授权原则。即我们仅允许有必要知晓这些信息的本产品和其关联公司的员工在采取合理的措施验证身份之后，访问或修改这些信息。同时，我们会严格要求他们履行保密及安全义务，如果未能履行这些义务，其会被追究法律责任或被终止与本产品的合作关系。</p><p>（3）为了保护您的信息安全，如果用户个人信息有误，本产品会在严格验证并核实申请人身份后，根据用户要求访问、修正或删除相关信息（除非本产品出于合法的原因而必须保留这些个人信息）。</p><p>（4）我们会采取合理可行的措施，尽力避免收集无关的个人信息。我们只会在达成本政策所述目的所需的期限内保留您的个人信息，除非需要延长保留期或受到法律的允许。</p><p>（5）互联网并非绝对安全的环境，电子邮件、即时通讯、社交软件、交易平台等与其他用户的交流方式无法确定是否完全加密，请您在进行交互使用时，注意保护您个人信息的安全。</p><p>（6）请您理解，由于计算机及互联网技术的飞速发展及同步速度的限制，可能存在或出现各种恶意或非恶意的攻击手段。虽然本产品持续致力于提升和加强安全措施，以保护您的信息免遭意外或破坏，但仍无法始终保证信息的百分之百安全。</p><p>（7）您使用产品或服务时所用的系统和通讯网络，或硬件设备等本产品均无法控制，请您了解并注意保护您的个人信息安全。</p><p>（8）请注意，您在使用我们服务时自愿共享甚至公开分享的信息，可能会涉及您或他人的个人信息甚至个人敏感信息，如您在评价时选择上传包含个人信息的图片。请您更加谨慎地考虑，是否在使用我们的服务时共享甚至公开分享相关信息。</p><p>请使用复杂密码，协助我们保证您的账号安全。我们将尽力保障您发送给我们的任何信息的安全性。如果我们的物理、技术或管理防护设施遭到破坏，导致信息被非授权访问、公开披露、篡改或毁坏，导致您的合法权益受损，我们将承担相应的法律责任。</p><p>（9）在不幸发生个人信息安全事件后，我们将按照法律法规的要求向您告知：安全事件的基本情况和可能的影响、我们已采取或将要采取的处置措施、您可自主防范和降低风险的建议、对您的补救措施等。事件相关情况我们将以邮件、信函、电话、推送通知等方式告知您，难以逐一告知个人信息主体时，我们会采取合理、有效的方式发布公告。</p><p>同时，我们还将按照监管部门要求，上报个人信息安全事件的处置情况。</p><p>&nbsp;</p><p>六、您如何管理您的个人信息</p><p>您可以通过以下方式访问及管理您的个人信息：</p><p>（一）访问您的个人信息</p><p>（1）您有权访问您的个人信息，法律法规规定的例外情况除外。您可以通过以下方式自行访问您的个人信息：</p><p>账户信息：如果您希望访问或编辑您的账户中的个人基本资料信息、查看连续包月退订说明、更改您的密码等，进入本产品后，点击“我的”—“头像”进入账号信息页面执行此类操作。</p><p>阅历、收藏信息：您可以进入本产品后，点击“我的”-“我的工具”-点击“浏览历史”或“收藏”中查阅或清除您的阅读记录、收藏记录等。</p><p>（2）如果您无法通过上述路径访问该等个人信息，您可以随时通过在线人工客服与我们取得联系。我们将在合理时间内回复您的访问请求。</p><p>（3）对于您在使用我们的产品或服务过程中产生的其他个人信息，我们将根据本条“（五）响应您的上述请求”中的相关安排向您提供。</p><p>（二）更正或补充您的个人信息</p><p>当您发现我们处理的关于您的个人信息有错误时，您有权要求我们做出更正或补充。您可以通过“（一）访问您的个人信息”中列明的方式提出更正或补充申请。</p><p>（三）改变或撤回您的授权</p><p>每个业务功能需要一些基本的个人信息才能得以完成（见本隐私保护政策“第二部分”）。除此之外，您可以通过注销、修改个人设置、删除相关信息等方式撤回部分授权，也可以通过手机的设置中关闭本软件的部分授权。</p><p>当您收回同意后，我们将不再处理相应的个人信息。但您收回同意的决定，不会影响此前基于您的授权而开展的个人信息处理。</p><p>（四）删除您的个人信息</p><p>（1）发生以下情形，您可以向本产品提出删除个人信息的请求：</p><p>如果本产品处理个人信息的行为违反法律法规；</p><p>如果收集、使用您的个人信息却未征得您的同意；</p><p>如果本产品处理个人信息的行为违反了与您的约定；</p><p>如果您不再使用本产品的产品或服务，或您主动注销了账号；</p><p>如果本产品终止产品运营及服务。</p><p>若本产品决定响应您的删除请求，本产品还将同时尽可能通知从本产品处获得您的个人信息的主体，要求其及时删除（除非法律法规另有规定，或这些主体已独立获得您的授权）。</p><p>当您或本产品协助您删除相关信息后，因为适用的法律和安全技术，本产品可能无法立即从备份系统中删除相应的信息，本产品将安全地存储您的个人信息并将其与任何进一步处理隔离，直到备份可以清除或实现匿名。</p><p>（2）注销您的账号</p><p>您可以通过“设置”-“账号安全”-“注销账号”来注销您的账号；</p><p>联系本产品的客服寻求帮助，协助您申请注销您的账号。</p><p>在您主动注销账号之后，本产品将停止为您提供产品或服务，根据适用法律的要求删除您的个人信息，或使其匿名化处理。</p><p>（五）约束信息系统自动决策</p><p>在某些业务功能中，我们可能仅依据信息系统、算法等在内的非人工自动决策机制做出决定。如果这些决定显著影响您的合法权益，您有权要求我们做出解释，我们也将在不侵害本产品商业秘密或其他用户权益、社会公共利益的前提下提供申诉方法。</p><p>（六）响应您的上述请求</p><p>（1）为保障安全，您可能需要提供书面请求，或以其他方式证明您的身份。我们可能会先要求您验证自己的身份，然后再处理您的请求。</p><p>（2）我们将在合理时间内做出答复。如您不满意，还可以通过本隐私政策第十条载明的客服联系方式发起投诉。</p><p>（3）对于您合理的请求，我们原则上不收取费用。对于那些无端重复、需要过多技术手段（例如，需要开发新系统或从根本上改变现行惯例）、给他人合法权益带来风险或者非常不切实际的请求，我们可能会予以拒绝。</p><p>（4）在以下情形中，按照法律法规要求，我们将无法响应您的请求：</p><p>1、与国家安全、国防安全有关的；</p><p>2、与公共安全、公共卫生、重大公共利益有关的；</p><p>3、与犯罪侦查、起诉、审判和执行判决等有关的；</p><p>4、有充分证据表明个人信息主体存在主观恶意或滥用权利的；</p><p>5、响应您的请求将导致您或其他个人、组织的合法权益受到严重损害的；</p><p>6、涉及商业秘密的。</p><p>&nbsp;</p><p>七、我们如何处理未成年人的个人信息</p><p>（1）如果没有父母或监护人的同意，未成年人不得创建自己的用户账户。如您为未成年人的，建议您请您的父母或监护人仔细阅读本隐私保护政策，并在征得您的父母或监护人同意的前提下使用我们的服务或向我们提供信息。</p><p>（2）对于经父母或监护人同意使用我们的产品或服务而收集未成年人个人信息的情况，我们只会在法律法规允许、父母或监护人明确同意或者保护未成年人所必要的情况下使用、共享、转让或披露此信息。</p><p>&nbsp;</p><p>八、您的个人信息如何存储</p><p>（一）信息存储的地点：</p><p>我们在中华人民共和国境内运营中收集和产生的个人信息，存储在中国境内，以下情形除外：</p><p>1、法律法规有明确规定；</p><p>2、获得您的明确授权；</p><p>3、您通过互联网进行跨境交易等个人主动行为。</p><p>针对以上情形，我们会确保依据本隐私保护政策对您的个人信息提供足够的保护。</p><p>（二）信息存储时间：</p><p>我们会采取合理可行的措施，尽力避免收集无关的个人信息。我们只会在达成本政策所述目的所需的期限内保留您的个人信息，除非法律有强制的存留要求。而我们判断前述期限的标准包括：</p><p>1、完成与您相关的交易目的、维护相应交易及业务记录、应对您可能的查询或投诉。</p><p>2、保证我们为您提供服务的安全和质量。</p><p>3、您是否同意更长的留存期间。</p><p>4、是否存在保留期限的其他特别约定。</p><p>在您的个人信息超出保留期间后，我们会根据适用法律的要求删除您的个人信息，或使其匿名化处理。</p><p>九、本隐私保护政策如何更新</p><p>我们的隐私保护政策可能变更。</p><p>（1）未经您明确同意，我们不会限制您按照本隐私保护政策所应享有的权利。</p><p>（2）对于重大变更，我们还会提供更为显著的通知（包括我们会通过官网或程序首页公示的方式进行通知甚至向您提供弹窗提示）。</p><p>本政策所指的重大变更包括但不限于：</p><p>1、我们的服务模式发生重大变化。如处理个人信息的目的、处理的个人信息类型、个人信息的使用方式等；</p><p>2、我们在控制权等方面发生重大变化。如并购重组等引起的所有者变更等；</p><p>3、个人信息共享、转让或公开披露的主要对象发生变化；</p><p>4、您参与个人信息处理方面的权利及其行使方式发生重大变化；</p><p>5、我们负责处理个人信息安全的责任部门、联络方式及投诉渠道发生变化时；</p><p>6、个人信息安全影响评估报告表明存在高风险时。</p><p>十、关于广告和营销协议</p><p>本人同意并授权：</p><p>为向本人提供优质营销服务，${company_name:示例文化科技有限公司}可将本人的手机号、设备号等信息提供给包含但不限于阿里云计算(北京)有限责任公司（以下简称“阿里云”）等第三方合作机构，并对本人进行短信、语音外呼、信息流等模式的触达，本人同意接收${company_name:示例文化科技有限公司}及其第三方合作机构的相关营销信息、广告信息。</p><p>我们需要把您的手机号码提供给示例科技相关的第三方合作机构，包含但不限于如中国移动、中国电信、中国联通、银联智策、腾讯、中国银联股份有限公司、银联智策顾问（上海）有限公司、银联商务支付股份有限公司及其关联公司等第三方合作方，用于向上述合作方查询基于合作方数据的用户画像、大致位置信息(如地区代码、城市代码等)、终端设备信息(如终端型号、品牌、os 类型等)、通信行为、APP/域名访问及搜索偏好、流量等数据、消费信息、银行卡交易信息等内容，生成的模型分值。上述模型分值将用于我们更准确地向您推荐商品与服务。</p><p>&nbsp;</p><p>十一、如何联系我们</p><p>您可以通过以下方式与我们联系，我们将在合理时间天内回复您的请求：</p><p>（1）我们设立了个人信息保护专职部门。如您有任何疑问、意见或建议，您都可以通过帮助与反馈途径联系在线人工客服或通过发送邮件至【mzwork01@163.com】与我们联系。</p><p>（2）如果您对我们的回复不满意，特别是您认为我们的个人信息处理行为损害了您的合法权益，您还可以通过向被告住所地有管辖权的法院提起诉讼来寻求解决方案。</p><p>（3）本产品服务提供者${company_name:示例文化科技有限公司}。</p><p class=\"ql-align-right\"><br></p>','xieyi',NULL,NULL),(186,'2020-07-27 15:17',NULL,'云购支付宝商户号',168,NULL,'zhifubao',NULL,NULL),(187,'2020-07-27 15:17',NULL,'云购支付宝秘钥',169,NULL,'zhifubao',NULL,NULL),(200,'2020-11-04 10:31:40',NULL,'支付宝证书地址',200,NULL,'zhifubao',NULL,NULL),(201,'2020-11-04 10:31:40',NULL,'支付宝方式 1证书 2秘钥 3云购os',201,'2','zhifubao',NULL,NULL),(202,'2023-03-08 17:23:22',NULL,'客服配置  1  二维码客服 2 微信公众号客服  3电话客服',202,'1','kefu',NULL,NULL),(203,'2020-11-04 10:31:40',NULL,'微信客服appid  ',203,'2','kefu',NULL,NULL),(204,'2024-02-26 18:53:25',NULL,'微信客服链接',204,'https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/duanju-mp-index/img/qiwei.jpg','kefu',NULL,NULL),(205,'2023-03-08 17:25:27',NULL,'客服二维码',205,'https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/duanju-mp-index/img/qiwei.jpg','image',NULL,NULL),(206,'2020-11-04 10:31:40',NULL,'客服电话',206,'2','kefu',NULL,NULL),(207,'2024-02-26 18:52:00',NULL,'客服微信号',207,'__KEFU_WECHAT__','kefu',NULL,NULL),(234,'2023-12-12 13:03:47',NULL,'上传方式 1阿里云oss 2腾讯云oss 3本地',234,'1','oss',NULL,NULL),(235,'2023-03-13 10:50:29',NULL,'小程序是否上架1',235,'是','kaiguan',NULL,NULL),(236,'2023-03-13 10:50:29',NULL,'小程序是否上架2',236,'是','kaiguan',NULL,NULL),(237,'2023-09-01 13:49:55',NULL,'小程序是否获取手机号',237,'否','kaiguan',NULL,NULL),(238,'2020-07-27 15:17',NULL,'短信宝用户名',238,NULL,'duanxin',NULL,NULL),(239,'2020-07-27 15:17',NULL,'短信宝密码',239,NULL,'duanxin',NULL,NULL),(240,'2020-11-04 10:31:40',NULL,'腾讯云短信SecretId',240,NULL,'duanxin',NULL,NULL),(241,'2020-11-04 10:31:40',NULL,'腾讯云短信SecretKey',241,NULL,'duanxin',NULL,NULL),(242,'2020-11-04 10:31:40',NULL,'腾讯云短信登录或注册模板id',242,'__TXSMS_TPL_LOGIN__','duanxin',NULL,NULL),(243,'2020-11-04 10:31:40',NULL,'腾讯云短信找回密码模板id',243,'__TXSMS_TPL_RESETPWD__','duanxin',NULL,NULL),(244,'2020-11-04 10:31:40',NULL,'腾讯云短信绑定手机号模板id',244,'__TXSMS_TPL_BINDPHONE__','duanxin',NULL,NULL),(245,'2023-09-01 13:15:35',NULL,'充值列表',245,'0.01,50,100,200','fuwufei',NULL,NULL),(246,'2024-10-11 14:19:11',NULL,'用户注销协议',246,'<p>短剧尊重并保护所有使用服务用户的个人隐私权。为了给您提供更准确、更有个性化的服务，短剧会按照本隐私权政策的规定使用和披露您的个人信息。但短剧将以高度的勤勉、审慎义务对待这些信息。除本隐私权政策另有规定外，在未征得您事先许可的情况下，短剧不会将这些信息对外披露或向第三方提供。短剧会不时更新本隐私权政策。 您在同意短剧服务使用协议之时，即视为您已经同意本隐私权政策全部内容。本隐私权政策属于短剧服务使用协议不可分割的一部分。</p><p>1. 适用范围</p><p>(a) 在您注册短剧帐号时，您根据短剧要求提供的个人注册信息；</p><p>(b) 在您使用短剧网络服务，或访问短剧平台网页时，短剧自动接收并记录的您的浏览器和计算机上的信息，包括但不限于您的IP地址、浏览器的类型、使用的语言、访问日期和时间、软硬件特征信息及您需求的网页记录等数据；</p><p>(c) 短剧通过合法途径从商业伙伴处取得的用户个人数据。</p><p>您了解并同意，以下信息不适用本隐私权政策：</p><p>(a) 您在使用短剧平台提供的搜索服务时输入的关键字信息；</p><p>(b) 短剧收集到的您在短剧发布的有关信息数据，包括但不限于参与活动、成交信息及评价详情；</p><p>(c) 违反法律规定或违反短剧规则行为及短剧已对您采取的措施。</p><p>2. 信息使用</p><p>(a)短剧不会向任何无关第三方提供、出售、出租、分享或交易您的个人信息，除非事先得到您的许可，或该第三方和短剧（含短剧关联公司）单独或共同为您提供服务，且在该服务结束后，其将被禁止访问包括其以前能够访问的所有这些资料。</p><p>(b) 短剧亦不允许任何第三方以任何手段收集、编辑、出售或者无偿传播您的个人信息。任何短剧平台用户如从事上述活动，一经发现，短剧有权立即终止与该用户的服务协议。</p><p>(c) 为服务用户的目的，短剧可能通过使用您的个人信息，向您提供您感兴趣的信息，包括但不限于向您发出产品和服务信息，或者与短剧合作伙伴共享信息以便他们向您发送有关其产品和服务的信息（后者需要您的事先同意）。</p><p><br></p><p><br></p><p>3. 信息披露</p><p>在如下情况下，短剧将依据您的个人意愿或法律的规定全部或部分的披露您的个人信息：</p><p>(a) 经您事先同意，向第三方披露；</p><p>(b)为提供您所要求的产品和服务，而必须和第三方分享您的个人信息；</p><p>(c) 根据法律的有关规定，或者行政或司法机构的要求，向第三方或者行政、司法机构披露；</p><p>(d) 如您出现违反中国有关法律、法规或者短剧服务协议或相关规则的情况，需要向第三方披露；</p><p>(e) 如您是适格的知识产权投诉人并已提起投诉，应被投诉人要求，向被投诉人披露，以便双方处理可能的权利纠纷；</p><p>(f) 在短剧平台上创建的某一交易中，如交易任何一方履行或部分履行了交易义务并提出信息披露请求的，短剧有权决定向该用户提供其交易对方的联络方式等必要信息，以促成交易的完成或纠纷的解决。</p><p>(g) 其它短剧根据法律、法规或者网站政策认为合适的披露。</p><p>4. 信息存储和交换</p><p>短剧收集的有关您的信息和资料将保存在短剧及（或）其关联公司的服务器上，这些信息和资料可能传送至您所在国家、地区或短剧收集信息和资料所在地的境外并在境外被访问、存储和展示。</p><p><br></p><p>5. Cookie的使用</p><p>(a) 在您未拒绝接受cookies的情况下，短剧会在您的计算机上设定或取用cookies ，以便您能登录或使用依赖于cookies的短剧平台服务或功能。短剧使用cookies可为您提供更加周到的个性化服务，包括推广服务。</p><p>(b) 您有权选择接受或拒绝接受cookies。您可以通过修改浏览器设置的方式拒绝接受cookies。但如果您选择拒绝接受cookies，则您可能无法登录或使用依赖于cookies的短剧网络服务或功能。</p><p>(c) 通过短剧所设cookies所取得的有关信息，将适用本政策。</p><p>6. 信息安全</p><p>(a) 短剧帐号均有安全保护功能，请妥善保管您的用户名及密码信息。短剧将通过对用户密码进行加密等安全措施确保您的信息不丢失，不被滥用和变造。尽管有前述安全措施，但同时也请您注意在信息网络上不存在“完善的安全措施”。</p><p>(b) 在使用短剧网络服务进行网上交易时，您不可避免的要向交易对方或潜在的交易对方披露自己的个人信息，如联络方式或者邮政地址。请您妥善保护自己的个人信息，仅在必要的情形下向他人提供。如您发现自己的个人信息泄密，尤其是短剧用户名及密码发生泄露，请您立即联络短剧客服，以便短剧采取相应措施。</p><p>7.本隐私政策的更改</p><p>(a)如果决定更改隐私政策，我们会在本政策中、本公司网站中以及我们认为适当的位置发布这些更改，以便您了解我们如何收集、使用您的个人信息，哪些人可以访问这些信息，以及在什么情况下我们会透露这些信息。</p><p>(b)本公司保留随时修改本政策的权利，因此请经常查看。如对本政策作出重大更改，本公司会通过网站通知的形式告知。</p><p><br></p><p><br></p><p>&nbsp;</p>','xieyi',NULL,NULL),(247,'2023-09-05 18:48:17',NULL,'是否开启同步资源',247,'是','kaiguan',NULL,NULL),(248,'2023-09-06 18:55:17',NULL,'是否开启会员',248,'是','kaiguan',NULL,NULL),(249,'2023-09-06 18:55:17',NULL,'热搜词',249,'回到古代,都市,极品,总裁','xitong',NULL,NULL),(250,'2023-09-18 10:27:40',NULL,'短剧视频采集地址',250,'__VIDEO_COLLECT_API__','xitong',NULL,NULL),(251,'2023-10-12 15:14:18',NULL,'是否开启看广告',251,'是','kaiguan',NULL,NULL),(252,'2023-10-12 10:50:17',NULL,'激励广告id',252,'__AD_UNIT_ID__','xitong',NULL,NULL),(253,'2023-10-12 10:50:17',NULL,'首页关注公众号文案',253,NULL,'xitong',NULL,NULL),(800,'2022-11-01 11:18:00',NULL,'腾讯云oss AccessKey',800,NULL,'oss',NULL,NULL),(801,'2022-11-01 11:18:00',NULL,'腾讯云oss SecretKey',801,NULL,'oss',NULL,NULL),(802,'2023-12-12 13:04:26',NULL,'腾讯云oss bucket地域',802,NULL,'oss',NULL,NULL),(803,'2023-12-12 13:04:32',NULL,'腾讯云oss bucketName',803,NULL,'oss',NULL,NULL),(804,'2023-12-12 13:04:37',NULL,'腾讯云oss Bucket域名',804,NULL,'oss',NULL,NULL),(805,'2024-03-06 14:32:14','0','周日',805,'10','task_sign_in',NULL,NULL),(806,'2024-03-06 14:32:17','1','周一',806,'10','task_sign_in',NULL,NULL),(807,'2024-03-06 14:32:19','2','周二',807,'10','task_sign_in',NULL,NULL),(808,'2024-03-06 14:32:21','3','周三',808,'10','task_sign_in',NULL,NULL),(809,'2024-03-06 14:32:24','4','周四',809,'10','task_sign_in',NULL,NULL),(810,'2024-03-06 14:32:27','5','周五',810,'10','task_sign_in',NULL,NULL),(811,'2024-03-06 14:32:29','6','周六',811,'10','task_sign_in',NULL,NULL),(812,'2022-11-01 11:18:00',NULL,'看一集短剧',812,'10','task_day',NULL,NULL),(813,'2022-11-01 11:18:00',NULL,'解锁一部短剧',813,'20','task_day',NULL,NULL),(815,'2022-11-01 11:18:00',NULL,'收藏一部短剧',815,'20','task_day',NULL,NULL),(816,'2022-11-01 11:18:00','390','490',816,'3.9','charge','解锁10集','特惠'),(817,'2022-11-01 11:18:00','6680','3000',817,'19.9','charge','解锁全集(全网最低)','推荐'),(824,'2022-11-01 11:18:00',NULL,'点赞10集短剧',824,'30','task_day',NULL,NULL);
/*!40000 ALTER TABLE `common_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-16 16:38:00


-- ----------------------------
-- Default super-admin account (SHA256(salt+password))
--   username: admin
--   password: admin888
-- Change this password immediately after first login.
-- ----------------------------
INSERT INTO `sys_user`
  (`user_id`,`username`,`password`,`salt`,`email`,`mobile`,`status`,`create_user_id`,`create_time`)
VALUES
  (1,'admin','a78a9e92aaafb0f9a0d857c3a87d9302a5b45f548aec1b5fe006782373194455','Xk9Lm2Qp7Rt4Wz6a','admin@example.com','13800000000',1,1,'2025-01-01 00:00:00');


-- ================= 后台菜单/角色种子数据（admin 左侧菜单必需）=================
SET NAMES utf8mb4;

-- sys_menu: 109 rows
DELETE FROM `sys_menu`;
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (1,0,'系统管理',NULL,NULL,0,'system',20);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (2,0,'管理员列表','sys/user',NULL,1,'admin',10);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (3,0,'角色管理','sys/role',NULL,1,'role',11);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (4,0,'菜单管理','sys/menu',NULL,1,'menu',12);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (15,2,'查看',NULL,'sys:user:list,sys:user:info',2,NULL,0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (16,2,'新增',NULL,'sys:user:save,sys:role:select',2,NULL,0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (17,2,'修改',NULL,'sys:user:update,sys:role:select',2,NULL,0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (18,2,'删除',NULL,'sys:user:delete',2,NULL,0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (19,3,'查看',NULL,'sys:role:list,sys:role:info',2,NULL,0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (20,3,'新增',NULL,'sys:role:save,sys:menu:list',2,NULL,0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (21,3,'修改',NULL,'sys:role:update,sys:menu:list',2,NULL,0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (22,3,'删除',NULL,'sys:role:delete',2,NULL,0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (23,4,'查看',NULL,'sys:menu:list,sys:menu:info',2,NULL,0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (24,4,'新增',NULL,'sys:menu:save,sys:menu:select',2,NULL,0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (25,4,'修改',NULL,'sys:menu:update,sys:menu:select',2,NULL,0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (26,4,'删除',NULL,'sys:menu:delete',2,NULL,0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (27,1,'参数管理','sys/config','sys:config:list,sys:config:info,sys:config:save,sys:config:update,sys:config:delete',1,'config',6);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (29,1,'系统日志','sys/log','sys:log:list',1,'log',9);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (31,1,'字典管理','sys/dict','sys:dict:list,sys:dict:info,sys:dict:save,sys:dict:update,sys:dict:delete',1,'sousuo',7);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (32,0,'用户中心','userList','',1,'yonghul',1);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (33,0,'数据中心','home','',1,'shuju',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (34,0,'财务中心','financeList','',1,'caiwu',1);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (35,34,'查看','','financeList:list',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (36,34,'转账','','financeList:transfer',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (37,34,'退款','','financeList:refund',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (38,34,'退款','','financeList:add',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (39,34,'修改','','financeList:update',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (40,34,'删除','','financeList:delete',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (41,0,'消息中心','message','',1,'xiaoxi',1);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (42,41,'查看','','message:list',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (43,41,'消息推送','','message:push',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (44,0,'资源中心','mission','',1,'renwu',3);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (45,44,'查看','','mission:list',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (46,44,'添加','','mission:add',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (47,44,'修改','','mission:update',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (48,44,'删除','','mission:delete',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (49,44,'下架','','mission:sold',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (50,0,'首页装修','bannerList','',1,'shangpin',2);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (51,50,'查看','','bannerList:list',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (52,50,'添加','','bannerList:add',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (53,50,'修改','','bannerList:update',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (54,50,'删除','','bannerList:delete',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (57,0,'系统配置','allocationList','',1,'menu',9);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (58,57,'查看','','allocationList:list',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (59,57,'修改','','allocationList:update',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (60,32,'查看','','userList:list',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (61,32,'删除','','userList:delete',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (62,0,'订单中心','orderCenter','',1,'log',4);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (63,62,'查看','','orderCenter:list',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (64,62,'删除','','orderCenter:delete',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (66,0,'会员列表','viplist','',1,'fenleilist',5);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (67,66,'查看','','viplist:list',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (69,41,'添加','','message:add',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (70,41,'修改','','message:update',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (71,41,'删除','','message:delete',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (72,66,'添加','','viplist:add',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (73,66,'修改','','viplist:update',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (74,66,'删除','','viplist:delete',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (75,32,'修改比例','','userList:updatebl',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (76,32,'修改会员','','userList:updateVip',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (77,0,'邀请排行榜','riderTop','',1,'tubiao',6);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (78,77,'查看','','riderTop:list',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (79,62,'退款','','orderCenter:tuikuan',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (80,32,'修改信息','','userList:update',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (81,0,'注销信息','messageZx','',1,'renwu1',7);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (82,81,'查看','','messageZx:list',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (83,81,'注销','','messageZx:shenhe',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (84,32,'修改积分','','userList:updateJf',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (85,32,'修改钱包','','userList:updateQb',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (86,1,'升级配置','app','',1,'config',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (87,86,'查看','','app:list',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (88,86,'添加','','app:add',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (89,86,'修改','','app:update',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (90,86,'删除','','app:delete',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (91,0,'帮助中心','materialsList','',1,'order',8);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (92,91,'查看','','materialsList:list',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (93,91,'添加','','materialsList:add',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (94,91,'修改','','materialsList:update',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (95,91,'删除','','materialsList:delete',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (96,0,'落地页管理','landingPage','',1,'editor',3);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (97,96,'排行榜编辑','','landingPage:updateRank',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (98,96,'排行榜删除','','landingPage:deleteRank',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (99,96,'新增排行榜','','landingPage:addRank',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (100,96,'分享页编辑','','landingPage:updateShare',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (101,96,'分享页删除','','landingPage:deleteShare',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (102,96,'新增分享页','','landingPage:addShare',2,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (103,0,'企业微信管理','enter-wechat','',1,'pinglun',3);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (104,103,'新增企业群发','enter-wechat:add','enter-wechat:add',2,'',3);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (105,0,'短剧管理','','',0,'pdd',5);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (106,105,'商品管理','goodsList','',1,'shangpin',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (107,105,'订单管理','orderList','',1,'order',1);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (108,0,'营销中心','','',0,'peizhi',21);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (109,108,'链接管理','/ad/link','ad:link',1,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (110,108,'短剧管理','/ad/play','ad:play',0,'zhedie',1);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (111,110,'主剧管理','/ad/play/index','ad:play:index',1,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (112,110,'剧集管理','/ad/play/series','ad:play:series',1,'',1);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (113,108,'广告账户','/ad/customer','ad:customer',1,'',2);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (114,108,'小程序管理','/ad/app','ad:app',1,'',3);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (115,108,'黑名单管理','/ad/black','ad:black',1,'',4);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (116,108,'广告收入管理','/ad/cost','ad:cost',1,'',5);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (117,108,'数据看板','/ad/statics','ad:statics',0,'zhedie',6);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (118,117,'数据总览','/ad/statics/all','ad:statics:all',1,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (119,117,'个性化看板','/ad/statics/personal','ad:statics:personal',1,'',1);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (120,108,'实用工具','/ad/tool','ad:tool',0,'zhedie',7);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (121,120,'有米云素材','/ad/tool/appgrowing','ad:tool:appgrowing',1,'',0);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (122,120,'ADXray素材','/ad/tool/adxray','ad:tool:adxray',1,'',1);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (123,108,'账号管理','/ad/sys-user','ad:sys-user',1,'',8);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (124,108,'角色管理','/ad/sys-role','ad:sys-role',1,'',9);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`perms`,`type`,`icon`,`order_num`) VALUES (125,109,'新增链接','','ad:link:add',2,'',0);

-- sys_role: 6 rows
DELETE FROM `sys_role`;
INSERT INTO `sys_role` (`role_id`,`role_name`,`remark`,`create_user_id`,`create_time`) VALUES (1,'超级管理员','',37,'2021-06-04 17:38:28');
INSERT INTO `sys_role` (`role_id`,`role_name`,`remark`,`create_user_id`,`create_time`) VALUES (2,'演示用户','演示用户',11,'2021-08-20 10:13:44');
INSERT INTO `sys_role` (`role_id`,`role_name`,`remark`,`create_user_id`,`create_time`) VALUES (3,'营销中心管理员','',37,'2025-07-18 15:41:07');
INSERT INTO `sys_role` (`role_id`,`role_name`,`remark`,`create_user_id`,`create_time`) VALUES (4,'营销中心运营','',53,'2025-07-18 15:41:38');
INSERT INTO `sys_role` (`role_id`,`role_name`,`remark`,`create_user_id`,`create_time`) VALUES (5,'测试','',37,'2025-07-18 16:22:39');
INSERT INTO `sys_role` (`role_id`,`role_name`,`remark`,`create_user_id`,`create_time`) VALUES (6,'账号管理','',53,'2025-09-02 16:12:31');

-- sys_role_menu: 212 rows
DELETE FROM `sys_role_menu`;
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1532,2,33);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1533,2,32);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1534,2,60);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1535,2,61);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1536,2,75);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1537,2,76);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1538,2,80);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1539,2,84);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1540,2,85);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1541,2,35);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1542,2,36);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1543,2,37);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1544,2,38);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1545,2,39);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1546,2,41);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1547,2,42);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1548,2,43);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1549,2,69);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1550,2,70);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1551,2,71);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1552,2,51);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1553,2,45);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1554,2,46);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1555,2,47);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1556,2,49);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1557,2,63);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1558,2,79);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1559,2,67);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1560,2,72);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1561,2,73);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1562,2,77);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1563,2,78);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1564,2,81);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1565,2,82);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1566,2,83);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1567,2,91);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1568,2,92);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1569,2,93);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1570,2,94);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1571,2,95);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1572,2,58);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1573,2,15);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1574,2,19);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1575,2,23);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1576,2,87);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1577,2,-666666);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1578,2,34);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1579,2,50);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1580,2,44);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1581,2,62);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1582,2,66);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1583,2,57);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1584,2,2);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1585,2,3);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1586,2,4);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1587,2,1);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1588,2,86);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1971,3,108);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1972,3,109);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1973,3,110);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1974,3,111);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1975,3,112);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1976,3,113);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1977,3,114);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1978,3,115);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1979,3,116);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1980,3,117);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1981,3,118);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1982,3,119);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1983,3,120);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1984,3,121);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1985,3,122);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1986,3,123);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1987,3,124);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1988,3,-666666);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1992,5,108);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1993,5,109);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1994,5,110);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1995,5,111);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1996,5,112);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1997,5,113);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1998,5,114);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (1999,5,115);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2000,5,116);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2001,5,117);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2002,5,118);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2003,5,119);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2004,5,120);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2005,5,121);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2006,5,122);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2007,5,123);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2008,5,124);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2009,5,-666666);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2010,1,33);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2011,1,32);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2012,1,60);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2013,1,61);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2014,1,75);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2015,1,76);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2016,1,80);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2017,1,84);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2018,1,85);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2019,1,34);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2020,1,35);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2021,1,36);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2022,1,37);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2023,1,38);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2024,1,39);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2025,1,40);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2026,1,41);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2027,1,42);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2028,1,43);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2029,1,69);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2030,1,70);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2031,1,71);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2032,1,50);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2033,1,51);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2034,1,52);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2035,1,53);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2036,1,54);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2037,1,44);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2038,1,45);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2039,1,46);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2040,1,47);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2041,1,48);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2042,1,49);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2043,1,96);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2044,1,97);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2045,1,98);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2046,1,99);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2047,1,100);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2048,1,101);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2049,1,102);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2050,1,103);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2051,1,104);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2052,1,62);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2053,1,63);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2054,1,64);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2055,1,79);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2056,1,66);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2057,1,67);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2058,1,72);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2059,1,73);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2060,1,74);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2061,1,105);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2062,1,106);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2063,1,107);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2064,1,77);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2065,1,78);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2066,1,81);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2067,1,82);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2068,1,83);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2069,1,91);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2070,1,92);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2071,1,93);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2072,1,94);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2073,1,95);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2074,1,57);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2075,1,58);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2076,1,59);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2077,1,2);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2078,1,15);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2079,1,16);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2080,1,17);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2081,1,18);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2082,1,3);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2083,1,19);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2084,1,20);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2085,1,21);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2086,1,22);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2087,1,4);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2088,1,23);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2089,1,24);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2090,1,25);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2091,1,26);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2092,1,1);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2093,1,86);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2094,1,87);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2095,1,88);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2096,1,89);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2097,1,90);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2098,1,27);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2099,1,31);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2100,1,29);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2101,1,108);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2102,1,109);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2103,1,110);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2104,1,111);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2105,1,112);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2106,1,113);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2107,1,114);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2108,1,115);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2109,1,116);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2110,1,117);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2111,1,118);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2112,1,119);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2113,1,120);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2114,1,121);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2115,1,122);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2116,1,123);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2117,1,124);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2118,1,-666666);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2119,6,2);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2120,6,15);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2121,6,16);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2122,6,17);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2123,6,18);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2124,6,-666666);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2125,4,109);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2126,4,125);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2127,4,-666666);
INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES (2128,4,108);

-- ================= 示例短剧/banner（买家开箱演示数据，媒资ID与域名已清空）=================
-- MySQL dump 10.13  Distrib 5.7.44, for Linux (x86_64)
--
-- Host: localhost    Database: yizhao_sample
-- ------------------------------------------------------
-- Server version	5.7.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` (`course_id`, `title`, `title_img`, `price`, `classify_id`, `pay_num`, `course_label`, `img`, `details`, `is_delete`, `create_time`, `update_time`, `msg_url`, `msg_type`, `is_recommend`, `banner_id`, `course_type`, `status`, `banner_img`, `is_over`, `is_price`, `view_counts`, `jifen`, `wx_media_id`, `wx_media_out_date`, `tt_album_id`) VALUES (622,'对不起我爱你','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',0.00,NULL,0,'70集全','','<p>顾家千金宝儿被抛荒野，意外被卖菜养父夏海救下取名夏炎，流落十余载。夏炎心疼养父成绩优异被破格录取进入私立高中，但因其失声却遭到自己的同胞哥哥顾聪的霸凌。生日宴顾聪更是叫来夏炎养父当着众人对其百般侮辱。养父愧对夏炎觉得自己让女儿丢脸因其发病，查出心衰，夏炎面临巨额费用，体检时不得不接受顾聪3万补习的戏谑，却在补习时与金夫人发生争执，夏炎为救顾聪跳入水中，体力不支溺水⋯</p>',0,'2024-01-09 17:39:00','2024-01-09 17:39:00','',NULL,5,NULL,1,1,'',1,1,3766334,0.00,NULL,NULL,NULL);
INSERT INTO `course` (`course_id`, `title`, `title_img`, `price`, `classify_id`, `pay_num`, `course_label`, `img`, `details`, `is_delete`, `create_time`, `update_time`, `msg_url`, `msg_type`, `is_recommend`, `banner_id`, `course_type`, `status`, `banner_img`, `is_over`, `is_price`, `view_counts`, `jifen`, `wx_media_id`, `wx_media_out_date`, `tt_album_id`) VALUES (623,'凌云之志','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',0.00,NULL,0,'91集全','','<p>出国七年，叶北已成为全球金融巨鳄昆仑财团的董事长，此次低调回国本想给女友江凝月一个惊喜。不曾想江凝月贪恋富贵，提出分手，并想要嫁给从来没用真实身份示人的董事长。同时，叶北偶遇林清雪，两人闪婚结为夫妻。在签约晚宴上，林清雪宣布叶北是其丈夫，震惊全场……</p>',0,'2024-02-04 13:55:14','2024-02-04 13:55:14','',NULL,0,NULL,1,1,'',1,1,4584527,0.00,NULL,NULL,NULL);
INSERT INTO `course` (`course_id`, `title`, `title_img`, `price`, `classify_id`, `pay_num`, `course_label`, `img`, `details`, `is_delete`, `create_time`, `update_time`, `msg_url`, `msg_type`, `is_recommend`, `banner_id`, `course_type`, `status`, `banner_img`, `is_over`, `is_price`, `view_counts`, `jifen`, `wx_media_id`, `wx_media_out_date`, `tt_album_id`) VALUES (624,'如此神秘的他','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',0.00,NULL,0,'98集全','','<p>沈溪为了不再被爸妈逼婚，意外跟一个抠门男陈川闪婚了。反正家里也缺少个做饭做家务的，沈溪看陈川长得又帅，觉得不亏！ 于是乎，两个人开始了婚后生活，女主外，男主内。 结果后来让沈溪感到惊喜的是，在坏父母上门欺负她的时候，陈川挡在前面帮她解决了全部问题。 让沈溪更惊喜的是，后来她发现自己的这个闪婚老公竟是个超级有钱人？？？</p>',0,'2024-02-04 14:04:10','2024-02-04 14:04:10','',NULL,0,NULL,1,1,'',1,1,8635430,0.00,NULL,NULL,NULL);
INSERT INTO `course` (`course_id`, `title`, `title_img`, `price`, `classify_id`, `pay_num`, `course_label`, `img`, `details`, `is_delete`, `create_time`, `update_time`, `msg_url`, `msg_type`, `is_recommend`, `banner_id`, `course_type`, `status`, `banner_img`, `is_over`, `is_price`, `view_counts`, `jifen`, `wx_media_id`, `wx_media_out_date`, `tt_album_id`) VALUES (625,'黄金神瞳','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',0.00,NULL,0,'104集全','','<p>古玩店学徒林飞，意外觉醒黄金神瞳，从此鱼跃龙门，笑傲人生！</p>',0,'2024-02-04 14:10:41','2024-02-04 14:10:41','',NULL,0,NULL,1,1,'',1,1,5752329,0.00,NULL,NULL,NULL);
INSERT INTO `course` (`course_id`, `title`, `title_img`, `price`, `classify_id`, `pay_num`, `course_label`, `img`, `details`, `is_delete`, `create_time`, `update_time`, `msg_url`, `msg_type`, `is_recommend`, `banner_id`, `course_type`, `status`, `banner_img`, `is_over`, `is_price`, `view_counts`, `jifen`, `wx_media_id`, `wx_media_out_date`, `tt_album_id`) VALUES (626,'裴总每天都想父凭子贵','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',0.00,NULL,0,'99集全','','<p>被鞋店店长逼婚，林家家闪婚嫁给了一个二婚带娃的单亲爸爸。 本以为婚后的日子平淡如水，没想到二婚奶爸居然是全城首富！ 林家家：裴弈！你还有什么事瞒着我！ 老公抱着孩子：这对龙凤胎，也是你亲生的……</p>',0,'2024-02-04 14:21:10','2024-02-04 14:21:10','',NULL,0,NULL,1,1,'',1,1,6759198,0.00,NULL,NULL,NULL);
INSERT INTO `course` (`course_id`, `title`, `title_img`, `price`, `classify_id`, `pay_num`, `course_label`, `img`, `details`, `is_delete`, `create_time`, `update_time`, `msg_url`, `msg_type`, `is_recommend`, `banner_id`, `course_type`, `status`, `banner_img`, `is_over`, `is_price`, `view_counts`, `jifen`, `wx_media_id`, `wx_media_out_date`, `tt_album_id`) VALUES (627,'乘风破浪的婚姻','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',0.00,NULL,0,'90集全','','<p>出身大家族的楚清辞为了摆脱父亲对自己的溺爱，隐瞒身份，来到青州嫁给赵锦城，选择靠自己双手独立生活。但赵锦城遇到了将自己包装成名媛的宋倩倩，赵锦城为了攀附权贵和楚清辞离婚，楚清辞一怒之下和纪行渊契约结婚，纪行渊也正好借助楚清辞摆脱联姻。两个看不惯用权势给人划分等级的理想主义者，机缘巧合之下，有了羁绊，在看到对方面对权势压迫，毫不妥协的骄傲姿态，为对方倾心。</p>',0,'2024-02-04 14:26:42','2024-02-04 14:26:42','',NULL,0,NULL,1,1,'',1,1,6655291,0.00,NULL,NULL,NULL);
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `course_details`
--

LOCK TABLES `course_details` WRITE;
/*!40000 ALTER TABLE `course_details` DISABLE KEYS */;
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (48945,622,'1','','2024-01-10 19:17:06','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,2727656,88.00,2,2,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (48946,622,'2','','2024-01-10 19:17:06','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,4754902,88.00,2,2,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (48947,622,'3','','2024-01-10 19:17:06','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,1505047,88.00,2,2,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (48948,622,'4','','2024-01-10 19:17:06','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,6749160,88.00,2,2,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (48949,622,'5','','2024-01-10 19:17:06','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,1356587,88.00,2,2,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (48950,622,'6','','2024-01-10 19:17:06','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,6917861,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49015,623,'1','','2024-02-04 14:00:44','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,4599056,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49016,623,'2','','2024-02-04 14:00:44','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,3844238,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49017,623,'3','','2024-02-04 14:00:44','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,5125084,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49018,623,'4','','2024-02-04 14:00:44','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,6899986,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49019,623,'5','','2024-02-04 14:00:44','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,5038175,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49020,623,'6','','2024-02-04 14:00:44','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,4191977,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49106,624,'1','','2024-02-04 14:09:13','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,6852647,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49107,624,'2','','2024-02-04 14:09:13','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,799230,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49108,624,'3','','2024-02-04 14:09:13','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,3820620,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49109,624,'4','','2024-02-04 14:09:13','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,2618907,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49110,624,'5','','2024-02-04 14:09:13','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,1333739,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49111,624,'6','','2024-02-04 14:09:13','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,5406819,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49204,625,'1','','2024-02-04 14:14:51','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,4604386,88.00,2,2,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49205,625,'2','','2024-02-04 14:14:51','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,2930294,88.00,2,2,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49206,625,'3','','2024-02-04 14:14:51','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,539374,88.00,2,2,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49207,625,'4','','2024-02-04 14:14:51','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,500834,88.00,2,2,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49208,625,'5','','2024-02-04 14:14:51','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,587109,88.00,2,2,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49209,625,'6','','2024-02-04 14:14:51','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,1134107,88.00,2,2,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49308,626,'1','','2024-02-04 14:25:23','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,3363785,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49309,626,'2','','2024-02-04 14:25:23','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,4007930,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49310,626,'3','','2024-02-04 14:25:23','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,2755576,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49311,626,'4','','2024-02-04 14:25:23','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,1455152,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49312,626,'5','','2024-02-04 14:25:23','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,5603878,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49313,626,'6','','2024-02-04 14:25:23','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,2673649,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49407,627,'1','','2024-02-04 14:32:07','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,5184673,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49408,627,'2','','2024-02-04 14:32:07','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,4269227,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49409,627,'3','','2024-02-04 14:32:07','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,5493178,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49410,627,'4','','2024-02-04 14:32:07','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,571704,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49411,627,'5','','2024-02-04 14:32:07','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,6761399,88.00,2,NULL,0.00,NULL,NULL);
INSERT INTO `course_details` (`course_details_id`, `course_id`, `course_details_name`, `video_url`, `create_time`, `title_img`, `content`, `good_num`, `price`, `is_price`, `good`, `jifen`, `wx_media_id`, `tt_episode_id`) VALUES (49412,627,'6','','2024-02-04 14:32:07','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/cover.jpg',NULL,4217814,88.00,2,NULL,0.00,NULL,NULL);
/*!40000 ALTER TABLE `course_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `banner`
--

LOCK TABLES `banner` WRITE;
/*!40000 ALTER TABLE `banner` DISABLE KEYS */;
INSERT INTO `banner` (`id`, `create_time`, `name`, `image_url`, `state`, `classify`, `url`, `sort`, `describes`) VALUES (1,'2025-01-01 10:00:00','示例短剧A','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/b1.jpg',1,1,'/pages/index/course/courseDet?courseId=622',1,'');
INSERT INTO `banner` (`id`, `create_time`, `name`, `image_url`, `state`, `classify`, `url`, `sort`, `describes`) VALUES (2,'2025-01-01 10:00:00','示例短剧B','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/b2.jpg',1,1,'/pages/index/course/courseDet?courseId=623',2,'');
INSERT INTO `banner` (`id`, `create_time`, `name`, `image_url`, `state`, `classify`, `url`, `sort`, `describes`) VALUES (3,'2025-01-01 10:00:00','示例短剧C','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/b3.jpg',1,1,'/pages/index/course/courseDet?courseId=624',3,'');
INSERT INTO `banner` (`id`, `create_time`, `name`, `image_url`, `state`, `classify`, `url`, `sort`, `describes`) VALUES (34,'2025-01-01 10:00:00','最新短剧','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/nav/new.png',1,2,'/pages/index/course/courseList?title=最新',1,'看剧只看最新');
INSERT INTO `banner` (`id`, `create_time`, `name`, `image_url`, `state`, `classify`, `url`, `sort`, `describes`) VALUES (35,'2025-01-01 10:00:00','排行榜','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/nav/rank.png',1,2,'/pages/index/course/courseList?sort=1&title=排行',2,'关注最新排行');
INSERT INTO `banner` (`id`, `create_time`, `name`, `image_url`, `state`, `classify`, `url`, `sort`, `describes`) VALUES (36,'2025-01-01 10:00:00','最热短剧','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/nav/hot.png',1,2,'/pages/index/course/courseList?title=最热&sort=2',3,'观看热榜短剧');
INSERT INTO `banner` (`id`, `create_time`, `name`, `image_url`, `state`, `classify`, `url`, `sort`, `describes`) VALUES (50,'2025-01-01 10:00:00','今日推荐','https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/sample/promo.jpg',1,5,'/pages/index/course/courseDet?courseId=622',1,'每日精选好剧');
/*!40000 ALTER TABLE `banner` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-17  4:55:49


SET FOREIGN_KEY_CHECKS = 1;
