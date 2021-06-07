
DROP TABLE IF EXISTS jq_encryption_algorithms_lookup;
CREATE TABLE jq_encryption_algorithms_lookup (
  `encryption_algo_id` INT(11) NOT NULL,
  `encryption_algo_name` varchar(100) NOT NULL,  
  PRIMARY KEY (`encryption_algo_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

REPLACE INTO jq_encryption_algorithms_lookup (encryption_algo_id,encryption_algo_name) 
VALUES 
(0,'NA'),
(1,'RSA'),
(2,'AES'),
(3,'DES'),
(4,'SHA');

DROP TABLE IF EXISTS jq_api_client_details;
CREATE TABLE jq_api_client_details(
  `client_id` varchar(100) NOT NULL,
  `client_name` varchar(100) NOT NULL,
  `client_key` varchar(100) NOT NULL,
  `client_secret` text ,
  `client_public_key` text ,
  encryption_algo_id INT(11) DEFAULT 0 NOT NULL,
  inclusion_url_pattern text ,
  `updated_by` varchar(50) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `updated_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`client_id`)
 ,KEY encryption_algo_id (encryption_algo_id)
 ,CONSTRAINT jq_encryption_algo_id_ibfk_1 FOREIGN KEY (`encryption_algo_id`) REFERENCES  jq_encryption_algorithms_lookup (`encryption_algo_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
