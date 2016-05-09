DROP TABLE IF EXISTS `components`;

CREATE TABLE `components` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `component_name` VARCHAR(255),
  `qm_spoc` VARCHAR(255),
  `qa_spoc` VARCHAR(255),
  `authkey` VARCHAR(255),
  `endpoint` VARCHAR(255),
  `health_check_api` VARCHAR(255),
  `health_check_api_call_type` ENUM('GET','GETJSON','POST'),
  `health_check_api_resp` VARCHAR(255),
  `first_get_api` VARCHAR(255),
  `first_get_api_call_type` ENUM('GET','GETJSON','POST'),
  `first_get_api_req_json` TEXT,
  `first_get_api_resp` VARCHAR(255),
  `second_get_api` VARCHAR(255),
  `second_get_api_call_type` ENUM('GET','GETJSON','POST'),
  `second_get_api_req_json` TEXT,
  `second_get_api_resp` VARCHAR(255),
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `component_name` (`component_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;