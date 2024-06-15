/*
 Navicat Premium Data Transfer

 Source Server         : cinema
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44)
 Source Host           : localhost:3306
 Source Schema         : cinema

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44)
 File Encoding         : 65001

 Date: 15/06/2024 20:27:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_bill
-- ----------------------------
DROP TABLE IF EXISTS `sys_bill`;
CREATE TABLE `sys_bill` (
  `bill_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Order ID',
  `pay_state` tinyint(3) NOT NULL DEFAULT '0' COMMENT 'Payment status, 0 for unpaid, 1 for paid',
  `cancel_state` tinyint(3) NOT NULL DEFAULT '0' COMMENT 'Cancellation status, 0 for not canceled, 1 for canceled',
  `user_id` bigint(20) unsigned NOT NULL COMMENT 'User ID',
  `session_id` bigint(20) unsigned NOT NULL COMMENT 'Session ID',
  `seats` json NOT NULL COMMENT 'Purchased seat numbers, possibly multiple, transmitted as JSON',
  `create_time` datetime DEFAULT NULL COMMENT 'Creation time',
  `deadline` datetime DEFAULT NULL COMMENT 'Expiration time',
  `cancel_time` datetime DEFAULT NULL COMMENT 'Cancellation time, indicating user-initiated order cancellation, not system timeout cancellation',
  PRIMARY KEY (`bill_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_bill
-- ----------------------------
BEGIN;
INSERT INTO `sys_bill` (`bill_id`, `pay_state`, `cancel_state`, `user_id`, `session_id`, `seats`, `create_time`, `deadline`, `cancel_time`) VALUES (84, 0, 0, 32, 64, '[\"6-4\", \"6-5\", \"6-6\"]', '2024-06-15 12:00:59', '2024-06-15 12:15:59', NULL);
INSERT INTO `sys_bill` (`bill_id`, `pay_state`, `cancel_state`, `user_id`, `session_id`, `seats`, `create_time`, `deadline`, `cancel_time`) VALUES (85, 0, 0, 32, 64, '[\"7-4\", \"7-5\", \"7-6\"]', '2024-06-15 12:01:15', '2024-06-15 12:16:15', NULL);
INSERT INTO `sys_bill` (`bill_id`, `pay_state`, `cancel_state`, `user_id`, `session_id`, `seats`, `create_time`, `deadline`, `cancel_time`) VALUES (86, 0, 0, 30, 64, '[\"5-4\", \"5-5\", \"5-6\"]', '2024-06-15 12:01:50', '2024-06-15 12:16:50', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_carousel
-- ----------------------------
DROP TABLE IF EXISTS `sys_carousel`;
CREATE TABLE `sys_carousel` (
  `carousel_id` int(11) DEFAULT NULL,
  `carousel_picture` varchar(255) DEFAULT NULL,
  `carousel_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of sys_carousel
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_cinema
-- ----------------------------
DROP TABLE IF EXISTS `sys_cinema`;
CREATE TABLE `sys_cinema` (
  `cinema_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Cinema ID',
  `cinema_name` varchar(255) NOT NULL COMMENT 'Cinema name',
  `hall_category_list` json DEFAULT NULL COMMENT 'Hall categories owned by the cinema',
  `cinema_picture` json DEFAULT NULL COMMENT 'Cinema pictures, stored as multiple images in JSON format',
  `cinema_phone` varchar(20) NOT NULL COMMENT 'Cinema phone number',
  `cinema_address` varchar(255) NOT NULL COMMENT 'Cinema address',
  `work_start_time` varchar(10) DEFAULT NULL COMMENT 'Opening time',
  `work_end_time` varchar(10) DEFAULT NULL COMMENT 'Closing time',
  PRIMARY KEY (`cinema_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_cinema
-- ----------------------------
BEGIN;
INSERT INTO `sys_cinema` (`cinema_id`, `cinema_name`, `hall_category_list`, `cinema_picture`, `cinema_phone`, `cinema_address`, `work_start_time`, `work_end_time`) VALUES (1, 'Galaxy Cinema', '[\"Laser Hall\", \"IMAX Hall\", \"CGS China Giant Screen Hall\", \"Dolby panoramic sound Hall\", \"Dolby Cinema Hall\", \"RealD Hall\", \"RealD 6FLag Hall\", \"4DX Hall\", \"DTS:X Mirror Sound Hall\", \"Children Hall\", \"4K Hall\", \"60 FPS Hall\", \"Giant Screen Hall\", \"MX4D Hall\"]', '[\"/images/cinema/2024/06/15/4d435658315d4e80b41693612e47de75.jpg\", \"/images/cinema/2024/06/15/618ef511ceb6466294ade764c3b41126.jpg\"]', '19002224', 'Tầng 3, Thiso Mall Sala, 10 Mai Chí Thọ, Phường Thủ Thiêm, Thành phố Thủ Đức', '09:00', '23:59');
COMMIT;

-- ----------------------------
-- Table structure for sys_hall
-- ----------------------------
DROP TABLE IF EXISTS `sys_hall`;
CREATE TABLE `sys_hall` (
  `hall_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Hall ID',
  `cinema_id` bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT 'Cinema ID',
  `hall_name` varchar(255) NOT NULL COMMENT 'Hall name',
  `hall_category` varchar(255) NOT NULL COMMENT 'Hall category',
  `row_start` varchar(10) DEFAULT '1' COMMENT 'Starting row number, such as Row 1/Row A, default is Row 1',
  `row_nums` smallint(5) unsigned NOT NULL DEFAULT '10' COMMENT 'Total number of rows, default is 10',
  `seat_nums_row` smallint(5) unsigned NOT NULL DEFAULT '18' COMMENT 'Number of seats per row, default is 18',
  `seat_nums` smallint(5) unsigned NOT NULL DEFAULT '180' COMMENT 'Total number of available seats, used to determine if the hall is fully booked, default is 180',
  `seat_state` json NOT NULL COMMENT 'Status of all seats, 0 means available, 1 means disabled, 2 means sold, reflected in the session table',
  `del_state` tinyint(3) NOT NULL DEFAULT '0' COMMENT 'Deletion flag, 0 for not deleted, 1 for deleted, default is 0',
  PRIMARY KEY (`hall_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_hall
-- ----------------------------
BEGIN;
INSERT INTO `sys_hall` (`hall_id`, `cinema_id`, `hall_name`, `hall_category`, `row_start`, `row_nums`, `seat_nums_row`, `seat_nums`, `seat_state`, `del_state`) VALUES (1, 1, 'Hall 1', 'Laser Hall', '1', 10, 10, 91, '{\"1\": [0, 0, 0, 0, 0, 0, 1, 0, 0, 0], \"2\": [0, 0, 0, 0, 0, 0, 1, 0, 0, 0], \"3\": [0, 0, 0, 0, 0, 0, 1, 0, 0, 0], \"4\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"5\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"6\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"7\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"8\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"9\": [0, 0, 0, 0, 0, 0, 0, 1, 1, 1], \"10\": [0, 0, 0, 0, 0, 0, 0, 1, 1, 1]}', 0);
INSERT INTO `sys_hall` (`hall_id`, `cinema_id`, `hall_name`, `hall_category`, `row_start`, `row_nums`, `seat_nums_row`, `seat_nums`, `seat_state`, `del_state`) VALUES (2, 1, 'Hall 2', 'IMAX', '1', 10, 18, 160, '{\"1\": [0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0], \"2\": [0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0], \"3\": [0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0], \"4\": [0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0], \"5\": [0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0], \"6\": [0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0], \"7\": [0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0], \"8\": [0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0], \"9\": [0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0], \"10\": [0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0]}', 0);
INSERT INTO `sys_hall` (`hall_id`, `cinema_id`, `hall_name`, `hall_category`, `row_start`, `row_nums`, `seat_nums_row`, `seat_nums`, `seat_state`, `del_state`) VALUES (3, 1, 'Hall 3', 'Dolby Atmos Hall', '1', 7, 7, 49, '{\"1\": [0, 0, 0, 0, 0, 0, 0], \"2\": [0, 0, 0, 0, 0, 0, 0], \"3\": [0, 0, 0, 0, 0, 0, 0], \"4\": [0, 0, 0, 0, 0, 0, 0], \"5\": [0, 0, 0, 0, 0, 0, 0], \"6\": [0, 0, 0, 0, 0, 0, 0], \"7\": [0, 0, 0, 0, 0, 0, 0]}', 0);
INSERT INTO `sys_hall` (`hall_id`, `cinema_id`, `hall_name`, `hall_category`, `row_start`, `row_nums`, `seat_nums_row`, `seat_nums`, `seat_state`, `del_state`) VALUES (4, 1, 'Hall 4', '3D', '1', 15, 18, 264, '{\"1\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"2\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"3\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"4\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0], \"5\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0], \"6\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0], \"7\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"8\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"9\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"10\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"11\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"12\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"13\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"14\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"15\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]}', 0);
INSERT INTO `sys_hall` (`hall_id`, `cinema_id`, `hall_name`, `hall_category`, `row_start`, `row_nums`, `seat_nums_row`, `seat_nums`, `seat_state`, `del_state`) VALUES (5, 1, 'Hall 5', 'IMAX', '1', 10, 10, 92, '{\"1\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"2\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"3\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"4\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"5\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"6\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"7\": [1, 0, 0, 0, 0, 0, 0, 0, 0, 1], \"8\": [1, 0, 0, 0, 0, 0, 0, 0, 0, 1], \"9\": [1, 0, 0, 0, 0, 0, 0, 0, 0, 1], \"10\": [1, 0, 0, 0, 0, 0, 0, 0, 0, 1]}', 0);
INSERT INTO `sys_hall` (`hall_id`, `cinema_id`, `hall_name`, `hall_category`, `row_start`, `row_nums`, `seat_nums_row`, `seat_nums`, `seat_state`, `del_state`) VALUES (6, 1, 'Hall 6', 'IMAX MAX4D', '1', 20, 18, 360, '{\"1\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"2\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"3\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"4\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"5\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"6\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"7\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"8\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"9\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"10\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"11\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"12\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"13\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"14\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"15\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"16\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"17\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"18\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"19\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"20\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]}', 0);
INSERT INTO `sys_hall` (`hall_id`, `cinema_id`, `hall_name`, `hall_category`, `row_start`, `row_nums`, `seat_nums_row`, `seat_nums`, `seat_state`, `del_state`) VALUES (7, 1, 'Hall 7', 'MX4D', '1', 15, 18, 246, '{\"1\": [1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1], \"2\": [1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1], \"3\": [1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1], \"4\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"5\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"6\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"7\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"8\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"9\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"10\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"11\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"12\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"13\": [1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1], \"14\": [1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1], \"15\": [1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1]}', 0);
INSERT INTO `sys_hall` (`hall_id`, `cinema_id`, `hall_name`, `hall_category`, `row_start`, `row_nums`, `seat_nums_row`, `seat_nums`, `seat_state`, `del_state`) VALUES (8, 1, 'Hall 10', '4DX Hall', '1', 12, 10, 110, '{\"1\": [1, 0, 0, 0, 0, 0, 0, 0, 0, 1], \"2\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"3\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"4\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"5\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"6\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"7\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"8\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"9\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"10\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"11\": [1, 1, 0, 0, 0, 0, 0, 0, 1, 1], \"12\": [1, 1, 0, 0, 0, 0, 0, 0, 1, 1]}', 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_movie
-- ----------------------------
DROP TABLE IF EXISTS `sys_movie`;
CREATE TABLE `sys_movie` (
  `movie_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Movie ID',
  `movie_name` varchar(255) NOT NULL COMMENT 'Movie name',
  `movie_length` int(11) DEFAULT NULL COMMENT 'Movie duration (in minutes)',
  `movie_poster` varchar(10000) DEFAULT NULL COMMENT 'Movie poster',
  `movie_area` varchar(255) DEFAULT NULL COMMENT 'Movie area',
  `release_date` datetime DEFAULT NULL COMMENT 'Release date',
  `movie_box_office` decimal(20,2) unsigned NOT NULL DEFAULT '0.00' COMMENT 'Total box office revenue',
  `movie_introduction` varchar(10000) DEFAULT NULL COMMENT 'Movie introduction',
  `movie_pictures` json DEFAULT NULL COMMENT 'Movie pictures collection',
  `del_state` tinyint(3) NOT NULL DEFAULT '0' COMMENT 'Deletion flag, 0 for not deleted, 1 for deleted, default is 0',
  PRIMARY KEY (`movie_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_movie
-- ----------------------------
BEGIN;
INSERT INTO `sys_movie` (`movie_id`, `movie_name`, `movie_length`, `movie_poster`, `movie_area`, `release_date`, `movie_box_office`, `movie_introduction`, `movie_pictures`, `del_state`) VALUES (1, 'NHỮNG GÃ TRAI HƯ: CHƠI HAY BỊ XƠI', 115, '[\"/images/movie/2024/06/15/25bee880df8644ec8cdc3fe3be097860.jpg\"]', 'United States', '2024-06-07 00:00:00', 0.00, 'Mùa hè này, bom tấn hài hành động Bad Boys đã quay trở lại nhưng lần này có một sự thay đổi lớn: dân chơi Miami hiện đang bị truy nã. Cảnh sát trưởng mà họ kính trọng bị nghi ngờ đã tham gia vào đường dây tham nhũng điều này khiến Mike và Marcus không thể ngồi yên. Từ đây, hàng loạt bất ngờ xảy ra đẩy Mike và Marcus trở thành kẻ bị tình nghi số 01 mà không có cách nào minh oan.', '[]', 0);
INSERT INTO `sys_movie` (`movie_id`, `movie_name`, `movie_length`, `movie_poster`, `movie_area`, `release_date`, `movie_box_office`, `movie_introduction`, `movie_pictures`, `del_state`) VALUES (2, 'Kẻ Trộm Mặt Trăng 4', NULL, '[\"/images/movie/2024/06/13/a4f3e7cf917e4ad5a24b2fe2f2061a64.jpg\"]', 'United States', '2024-07-05 00:00:00', 0.00, 'Gru phải đối mặt với kẻ thù mới là Maxime Le Mal và Valentina đang lên kế hoạch trả thù anh, buộc gia đình anh phải lẩn trốn đi nơi khác. Bên cạnh việc đấu tranh bảo vệ gia đình, Gru đồng thời còn phải tìm ra điểm chung với thành viên mới nhất trong nhà đó là Gru Jr.', '[]', 0);
INSERT INTO `sys_movie` (`movie_id`, `movie_name`, `movie_length`, `movie_poster`, `movie_area`, `release_date`, `movie_box_office`, `movie_introduction`, `movie_pictures`, `del_state`) VALUES (3, 'Avenger 5', 140, '[\"/images/movie/2024/06/15/1b2dc7bdd31b486aaaa2c25f1a3d4fb3.jpg\"]', 'United States', '2024-06-30 00:00:00', 0.00, 'Avenger 5', '[]', 0);
INSERT INTO `sys_movie` (`movie_id`, `movie_name`, `movie_length`, `movie_poster`, `movie_area`, `release_date`, `movie_box_office`, `movie_introduction`, `movie_pictures`, `del_state`) VALUES (4, 'Doraemon', 100, '[\"/images/movie/2024/06/15/3548991c5dd04970a04e572ab3ddaae6.jpg\"]', 'Japan', '2024-06-16 00:00:00', 0.00, NULL, '[]', 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_movie_category
-- ----------------------------
DROP TABLE IF EXISTS `sys_movie_category`;
CREATE TABLE `sys_movie_category` (
  `movie_category_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Movie category ID',
  `movie_category_name` varchar(255) NOT NULL COMMENT 'Movie category name',
  PRIMARY KEY (`movie_category_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_movie_category
-- ----------------------------
BEGIN;
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (1, 'Romance');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (2, 'Comedy');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (3, 'Animation');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (4, 'Drama');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (5, 'Horror');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (6, 'Action');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (7, 'Science Fiction');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (8, 'Mystery');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (9, 'Thriller');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (10, 'Crime');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (11, 'Adventure');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (12, 'War');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (13, 'Fantasy');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (15, 'Family');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (16, 'Costume Drama');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (17, 'Martial Arts');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (18, 'Western');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (19, 'History');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (20, 'Biography');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (21, 'Musical');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (22, 'Film Noir');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (23, 'Short Film');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (24, 'Documentary');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (25, 'Other');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (26, 'History');
INSERT INTO `sys_movie_category` (`movie_category_id`, `movie_category_name`) VALUES (27, 'Sport');
COMMIT;

-- ----------------------------
-- Table structure for sys_movie_to_category
-- ----------------------------
DROP TABLE IF EXISTS `sys_movie_to_category`;
CREATE TABLE `sys_movie_to_category` (
  `movie_id` bigint(20) unsigned NOT NULL COMMENT 'Movie ID',
  `movie_category_id` bigint(20) unsigned NOT NULL COMMENT 'Movie category ID',
  PRIMARY KEY (`movie_id`,`movie_category_id`) USING BTREE,
  KEY `movie_category_id` (`movie_category_id`) USING BTREE,
  CONSTRAINT `sys_movie_to_category_ibfk_1` FOREIGN KEY (`movie_category_id`) REFERENCES `sys_movie_category` (`movie_category_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_movie_to_category
-- ----------------------------
BEGIN;
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (1, 1);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (4, 1);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (20, 1);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (23, 1);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (31, 1);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (1, 2);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (8, 2);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (9, 2);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (12, 2);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (16, 2);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (18, 2);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (20, 2);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (31, 2);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (32, 2);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (1, 3);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (4, 3);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (5, 3);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (15, 3);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (16, 3);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (17, 3);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (20, 3);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (23, 3);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (31, 3);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (1, 4);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (2, 4);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (5, 4);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (9, 4);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (17, 4);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (19, 4);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (10, 5);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (3, 6);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (8, 6);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (10, 6);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (11, 6);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (14, 6);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (15, 6);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (16, 6);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (18, 6);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (32, 6);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (3, 7);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (6, 7);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (11, 7);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (18, 7);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (2, 8);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (4, 8);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (20, 8);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (2, 9);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (20, 9);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (8, 10);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (14, 10);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (16, 10);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (20, 10);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (7, 11);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (14, 11);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (5, 13);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (6, 13);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (12, 15);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (6, 19);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (13, 23);
INSERT INTO `sys_movie_to_category` (`movie_id`, `movie_category_id`) VALUES (13, 24);
COMMIT;

-- ----------------------------
-- Table structure for sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource` (
  `resource_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Resource ID',
  `resource_name` varchar(255) NOT NULL COMMENT 'Menu name',
  `path` varchar(255) NOT NULL DEFAULT '' COMMENT 'Menu path, used to store page address',
  `level` int(10) unsigned NOT NULL DEFAULT '1' COMMENT 'Resource permission level, 1 indicates this resource is the highest level permission',
  `parent_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'Parent menu ID of the current menu',
  PRIMARY KEY (`resource_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=623 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_resource
-- ----------------------------
BEGIN;
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (1, 'Cinema Management', '', 1, 0);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (2, 'Movie Management', '', 1, 0);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (3, 'Hall Management', '', 1, 0);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (4, 'Order Management', '', 1, 0);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (5, 'User Management', '', 1, 0);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (6, 'Permission Management', '', 1, 0);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (11, 'Cinema Information Management', 'cinema', 2, 1);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (21, 'Movie Information Management', 'movie', 2, 2);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (22, 'Movie Category Management', 'movieCategory', 2, 2);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (31, 'Hall Information Management', 'hall', 2, 3);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (32, 'Session Information Management', 'session', 2, 3);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (41, 'Order Information Management', 'bill', 2, 4);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (51, 'User Information Management', 'user', 2, 5);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (61, 'Role Information Management', 'role', 2, 6);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (62, 'Resource Information Management', 'resource', 2, 6);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (111, 'Query Cinema Information Permission', '', 3, 11);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (112, 'Modify Cinema Information Permission', '', 3, 11);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (211, 'Query Movie Information Permission', '', 3, 21);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (212, 'Modify Movie Information Permission', '', 3, 21);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (221, 'Query Movie Category Permission', '', 3, 22);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (222, 'Modify Movie Category Permission', '', 3, 22);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (311, 'Query Hall Information Permission', '', 3, 31);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (312, 'Modify Hall Information Permission', '', 3, 31);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (321, 'Query Session Information Permission', '', 3, 32);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (322, 'Modify Session Information Permission', '', 3, 32);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (411, 'Query Order Information Permission', '', 3, 41);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (412, 'Modify Order Information Permission', '', 3, 41);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (511, 'Query User Information Permission', '', 3, 51);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (512, 'Modify User Information Permission', '', 3, 51);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (611, 'Query Role Information Permission', '', 3, 61);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (612, 'Modify Role Information Permission', '', 3, 61);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (621, 'Query Resource Information Permission', '', 3, 62);
INSERT INTO `sys_resource` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (622, 'Modify Resource Information Permission', '', 3, 62);
COMMIT;

-- ----------------------------
-- Table structure for sys_resource_copy
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource_copy`;
CREATE TABLE `sys_resource_copy` (
  `resource_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Resource ID',
  `resource_name` varchar(255) NOT NULL COMMENT 'Menu name',
  `path` varchar(255) NOT NULL DEFAULT '' COMMENT 'Menu path, used to store page address',
  `level` int(10) unsigned NOT NULL DEFAULT '1' COMMENT 'Resource permission level, 1 indicates this resource is the highest level permission',
  `parent_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'Parent menu ID of the current menu',
  PRIMARY KEY (`resource_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_resource_copy
-- ----------------------------
BEGIN;
INSERT INTO `sys_resource_copy` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (1, 'Cinema Management', '', 1, 0);
INSERT INTO `sys_resource_copy` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (2, 'Movie Management', '', 1, 0);
INSERT INTO `sys_resource_copy` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (3, 'Hall Management', '', 1, 0);
INSERT INTO `sys_resource_copy` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (4, 'Order Management', '', 1, 0);
INSERT INTO `sys_resource_copy` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (5, 'User Management', '', 1, 0);
INSERT INTO `sys_resource_copy` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (6, 'Permission Management', '', 1, 0);
INSERT INTO `sys_resource_copy` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (11, 'Cinema Information Management', 'cinema', 2, 1);
INSERT INTO `sys_resource_copy` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (21, 'Movie Information Management', 'movie', 2, 2);
INSERT INTO `sys_resource_copy` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (22, 'Movie Category Management', 'movieCategory', 2, 2);
INSERT INTO `sys_resource_copy` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (31, 'Hall Information Management', 'hall', 2, 3);
INSERT INTO `sys_resource_copy` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (32, 'Session Information Management', 'session', 2, 3);
INSERT INTO `sys_resource_copy` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (41, 'Order Information Management', 'bill', 2, 4);
INSERT INTO `sys_resource_copy` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (51, 'User Information Management', 'user', 2, 5);
INSERT INTO `sys_resource_copy` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (61, 'Role Information Management', 'role', 2, 6);
INSERT INTO `sys_resource_copy` (`resource_id`, `resource_name`, `path`, `level`, `parent_id`) VALUES (62, 'Resource Information Management', 'resource', 2, 6);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Role ID',
  `role_name` varchar(255) NOT NULL COMMENT 'Role name',
  `role_desc` varchar(255) NOT NULL COMMENT 'Role description',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` (`role_id`, `role_name`, `role_desc`) VALUES (1, 'System Administrator', 'Manage the entire cinema online ticketing system');
INSERT INTO `sys_role` (`role_id`, `role_name`, `role_desc`) VALUES (2, 'Cinema Administrator', 'Manage the relevant information of their affiliated cinema');
INSERT INTO `sys_role` (`role_id`, `role_name`, `role_desc`) VALUES (3, 'User', 'User, can only use the front end to browse relevant information, place orders, modify personal information, etc.');
INSERT INTO `sys_role` (`role_id`, `role_name`, `role_desc`) VALUES (5, 'Cinema Ticketing Staff', 'Sell movie tickets at the cinema');
INSERT INTO `sys_role` (`role_id`, `role_name`, `role_desc`) VALUES (6, 'Security', 'Security');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_resource`;
CREATE TABLE `sys_role_resource` (
  `role_id` bigint(20) unsigned NOT NULL COMMENT 'Role ID',
  `resource_id` bigint(20) unsigned NOT NULL COMMENT 'Resource ID, only 3rd-level permissions are used as the basis for permission judgment',
  PRIMARY KEY (`role_id`,`resource_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_role_resource
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (1, 111);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (1, 112);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (1, 211);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (1, 212);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (1, 221);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (1, 222);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (1, 311);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (1, 312);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (1, 321);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (1, 322);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (1, 411);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (1, 412);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (1, 511);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (1, 512);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (1, 611);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (1, 612);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (1, 621);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (1, 622);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (2, 211);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (2, 221);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (2, 231);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (2, 241);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (2, 251);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (2, 261);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (2, 311);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (2, 312);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (2, 321);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (2, 411);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (2, 412);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (2, 511);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (2, 621);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (10, 621);
INSERT INTO `sys_role_resource` (`role_id`, `resource_id`) VALUES (10, 622);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_resource_copy
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_resource_copy`;
CREATE TABLE `sys_role_resource_copy` (
  `role_id` bigint(20) unsigned NOT NULL COMMENT 'Role ID',
  `resource_id` bigint(20) unsigned NOT NULL COMMENT 'Resource ID, only 3rd-level permissions are used as the basis for permission judgment',
  PRIMARY KEY (`role_id`,`resource_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_role_resource_copy
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (1, 11);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (1, 21);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (1, 22);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (1, 31);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (1, 32);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (1, 41);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (1, 51);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (1, 61);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (1, 62);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (2, 21);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (2, 22);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (2, 23);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (2, 24);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (2, 25);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (2, 26);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (2, 31);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (2, 32);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (2, 41);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (2, 51);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (2, 62);
INSERT INTO `sys_role_resource_copy` (`role_id`, `resource_id`) VALUES (10, 62);
COMMIT;

-- ----------------------------
-- Table structure for sys_session
-- ----------------------------
DROP TABLE IF EXISTS `sys_session`;
CREATE TABLE `sys_session` (
  `session_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Session ID',
  `hall_id` bigint(20) unsigned NOT NULL COMMENT 'Hall ID',
  `language_version` varchar(20) NOT NULL COMMENT 'Language version',
  `movie_id` bigint(20) unsigned NOT NULL COMMENT 'Movie ID',
  `play_time` varchar(20) NOT NULL COMMENT 'Start time of the session',
  `end_time` varchar(20) NOT NULL COMMENT 'End time of the session',
  `deadline` varchar(20) NOT NULL COMMENT 'Deadline for operation, movie and hall information cannot be deleted after this time',
  `session_date` date NOT NULL COMMENT 'Date of the session',
  `session_price` decimal(10,2) NOT NULL COMMENT 'Ticket price',
  `session_tips` varchar(255) DEFAULT NULL COMMENT 'Hall tips (e.g., mask requirements, benefits displayed next to the seat selection and ticket purchase location, format like: Hall 6 - (Mask required, no eating). Free parking for 3 hours with ticket)',
  `session_seats` json NOT NULL COMMENT 'Seat information, stored as {key: [], key: []} where key represents the row and the array (index + 1) represents the seat number. Value 0 indicates available, 1 indicates empty (no seat), 2 indicates selected, and 3 indicates sold.',
  `seat_nums` smallint(5) unsigned DEFAULT '0' COMMENT 'Total number of seats',
  `sall_nums` smallint(5) NOT NULL DEFAULT '0' COMMENT 'Number of seats sold',
  `del_state` tinyint(3) NOT NULL DEFAULT '0' COMMENT 'Deletion flag, 0 not deleted, 1 deleted, default 0',
  PRIMARY KEY (`session_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_session
-- ----------------------------
BEGIN;
INSERT INTO `sys_session` (`session_id`, `hall_id`, `language_version`, `movie_id`, `play_time`, `end_time`, `deadline`, `session_date`, `session_price`, `session_tips`, `session_seats`, `seat_nums`, `sall_nums`, `del_state`) VALUES (64, 1, 'Vietsub', 1, '18:00', '19:55', '2024-06-16 20:15:00', '2024-06-16', 10.00, NULL, '{\"1\": [0, 0, 0, 0, 0, 0, 1, 0, 0, 0], \"2\": [0, 0, 0, 0, 0, 0, 1, 0, 0, 0], \"3\": [0, 0, 0, 0, 0, 0, 1, 0, 0, 0], \"4\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"5\": [0, 0, 0, 3, 3, 3, 0, 0, 0, 0], \"6\": [0, 0, 0, 3, 3, 3, 0, 0, 0, 0], \"7\": [0, 0, 0, 3, 3, 3, 0, 0, 0, 0], \"8\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"9\": [0, 0, 0, 0, 0, 0, 0, 1, 1, 1], \"10\": [0, 0, 0, 0, 0, 0, 0, 1, 1, 1]}', 91, 9, 0);
INSERT INTO `sys_session` (`session_id`, `hall_id`, `language_version`, `movie_id`, `play_time`, `end_time`, `deadline`, `session_date`, `session_price`, `session_tips`, `session_seats`, `seat_nums`, `sall_nums`, `del_state`) VALUES (65, 8, 'Vietsub', 4, '18:00', '19:40', '2024-06-16 20:00:00', '2024-06-16', 10.00, NULL, '{\"1\": [1, 0, 0, 0, 0, 0, 0, 0, 0, 1], \"2\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"3\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"4\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"5\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"6\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"7\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"8\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"9\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"10\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"11\": [1, 1, 0, 0, 0, 0, 0, 0, 1, 1], \"12\": [1, 1, 0, 0, 0, 0, 0, 0, 1, 1]}', 110, 0, 0);
INSERT INTO `sys_session` (`session_id`, `hall_id`, `language_version`, `movie_id`, `play_time`, `end_time`, `deadline`, `session_date`, `session_price`, `session_tips`, `session_seats`, `seat_nums`, `sall_nums`, `del_state`) VALUES (66, 5, 'Vietsub', 1, '16:00', '17:55', '2024-06-16 18:15:00', '2024-06-16', 10.00, NULL, '{\"1\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"2\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"3\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"4\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"5\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"6\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"7\": [1, 0, 0, 0, 0, 0, 0, 0, 0, 1], \"8\": [1, 0, 0, 0, 0, 0, 0, 0, 0, 1], \"9\": [1, 0, 0, 0, 0, 0, 0, 0, 0, 1], \"10\": [1, 0, 0, 0, 0, 0, 0, 0, 0, 1]}', 92, 0, 0);
INSERT INTO `sys_session` (`session_id`, `hall_id`, `language_version`, `movie_id`, `play_time`, `end_time`, `deadline`, `session_date`, `session_price`, `session_tips`, `session_seats`, `seat_nums`, `sall_nums`, `del_state`) VALUES (67, 4, 'Vietsub', 4, '09:00', '10:40', '2024-06-17 11:00:00', '2024-06-17', 10.00, NULL, '{\"1\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"2\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"3\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"4\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0], \"5\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0], \"6\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0], \"7\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"8\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"9\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"10\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"11\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"12\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"13\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"14\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], \"15\": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]}', 264, 0, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'User ID',
  `user_name` varchar(255) NOT NULL COMMENT 'Username (used for login, must be unique)',
  `password` varchar(255) NOT NULL COMMENT 'Password (stored in encrypted form)',
  `salt` varchar(255) DEFAULT NULL COMMENT 'Salt used for password hashing',
  `email` varchar(255) DEFAULT NULL COMMENT 'User email, used for personal information and notifications',
  `phone_number` varchar(20) DEFAULT NULL COMMENT 'User phone number, used for registration and notifications',
  `sex` tinyint(3) unsigned NOT NULL COMMENT 'User gender, 1 for male, 0 for female',
  `user_picture` varchar(255) DEFAULT NULL COMMENT 'User profile picture',
  `role_id` bigint(20) unsigned NOT NULL DEFAULT '3' COMMENT 'Role ID',
  `birthday` date DEFAULT NULL COMMENT 'Birthday',
  `autograph` varchar(1000) DEFAULT NULL COMMENT 'Personal signature',
  `del_state` tinyint(3) NOT NULL DEFAULT '0' COMMENT 'Deletion flag, 0 for not deleted, 1 for deleted, default 0',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`user_id`, `user_name`, `password`, `salt`, `email`, `phone_number`, `sex`, `user_picture`, `role_id`, `birthday`, `autograph`, `del_state`) VALUES (1, 'admin', '2f6b5c3044b271f5c07545aa862cf35c', '0v#C4w&i', 'admin@gmail.com', '13111212312', 1, '[]', 1, '1999-10-15', '', 0);
INSERT INTO `sys_user` (`user_id`, `user_name`, `password`, `salt`, `email`, `phone_number`, `sex`, `user_picture`, `role_id`, `birthday`, `autograph`, `del_state`) VALUES (30, 'vanhung4499', '9e1e9375976e80d9b90d80c960b0b5f8', '$rKL!y4&', 'vanhung4499@gmail.com', '0377930334', 1, '[]', 3, '1999-04-04', NULL, 0);
INSERT INTO `sys_user` (`user_id`, `user_name`, `password`, `salt`, `email`, `phone_number`, `sex`, `user_picture`, `role_id`, `birthday`, `autograph`, `del_state`) VALUES (31, 'cineadmin', '9e1e9375976e80d9b90d80c960b0b5f8', '$rKL!y4&', 'cine@gmail.com', '0377942345', 1, '[]', 2, '1999-04-04', NULL, 0);
INSERT INTO `sys_user` (`user_id`, `user_name`, `password`, `salt`, `email`, `phone_number`, `sex`, `user_picture`, `role_id`, `birthday`, `autograph`, `del_state`) VALUES (32, 'hungnv4499', 'f3e6038be777bbf7837afdbcc00fc593', 'TFm6m!g5', 'o0ohung4499o0o@gmail.com', '0377930334', 1, '[\"/images/user/2024/06/15/47e7506e323c4dc9aa200b666ece1a6f.jpeg\"]', 3, '1999-04-04', NULL, 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
