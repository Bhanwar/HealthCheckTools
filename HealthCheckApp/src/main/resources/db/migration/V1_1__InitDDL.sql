DROP TABLE IF EXISTS `components`;

CREATE TABLE `components` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `component_name` VARCHAR(255) NOT NULL,
  `qm_spoc` VARCHAR(255),
  `qa_spoc` VARCHAR(255),
  `authkey` VARCHAR(255),
  `authkey_shared` ENUM('YES','NO') NOT NULL DEFAULT 'NO',
  `endpoint` VARCHAR(255),
  `health_check_api` VARCHAR(255),
  `health_check_api_call_type` ENUM('GET','POST'),
  `health_check_headers` TEXT,
  `health_check_api_req_json` TEXT,
  `health_check_api_resp` VARCHAR(255),
  `first_get_api` VARCHAR(255),
  `first_get_api_call_type` ENUM('GET','POST'),
  `first_get_headers` TEXT,
  `first_get_api_req_json` TEXT,
  `first_get_api_resp` TEXT,
  `second_get_api` VARCHAR(255),
  `second_get_api_call_type` ENUM('GET','POST'),
  `second_get_headers` TEXT,
  `second_get_api_req_json` TEXT,
  `second_get_api_resp` TEXT,
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `component_name` (`component_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `admin` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `authkey` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `authkey` (`authkey`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
