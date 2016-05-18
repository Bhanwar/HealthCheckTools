CREATE TABLE `token_api_details` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `component_name` VARCHAR(255) NOT NULL,
  `login_api` VARCHAR(255),
  `login_api_call_type` ENUM('GET','POST'),
  `login_api_req_json` TEXT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `component_name` (`component_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

ALTER TABLE `components` MODIFY `health_check_api_call_type` ENUM('GET','POST');
ALTER TABLE `components` ADD `health_check_headers` TEXT AFTER `health_check_api_call_type`;
ALTER TABLE `components` ADD `health_check_api_req_json` TEXT AFTER `health_check_headers`;
ALTER TABLE `components` MODIFY `first_get_api_call_type` ENUM('GET','POST');
ALTER TABLE `components` ADD `first_get_headers` TEXT AFTER `first_get_api_call_type`;
ALTER TABLE `components` MODIFY `second_get_api_call_type` ENUM('GET','POST');
ALTER TABLE `components` ADD `second_get_headers` TEXT AFTER `second_get_api_call_type`;