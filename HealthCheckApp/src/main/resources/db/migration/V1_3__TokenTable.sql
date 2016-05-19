CREATE TABLE `token_api_details` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `component_name` VARCHAR(255) NOT NULL,
  `login_api` VARCHAR(255),
  `login_api_call_type` ENUM('GET','POST'),
  `login_api_req_json` TEXT,
  `login_invalid_cred_msg` VARCHAR(255),
  PRIMARY KEY (`id`),
  UNIQUE KEY `component_name` (`component_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;