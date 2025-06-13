DROP TABLE IF EXISTS jq_captcha;
CREATE TABLE `jq_captcha` (
  `request_id` varchar(50) NOT NULL,
  `captcha` varchar(20) NOT NULL,
  `request_time` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`request_id`)
);