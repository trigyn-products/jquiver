
ALTER TABLE `jq_api_client_details` DROP FOREIGN KEY `jq_encryption_algo_id_ibfk_1`;

/*Table structure for table `jq_encryption_algorithms_lookup` */

DROP TABLE IF EXISTS jq_encryption_algorithms_lookup;

CREATE TABLE `jq_encryption_algorithms_lookup` (
  `encryption_algo_id` int(11) NOT NULL,
  `encryption_algo_name` varchar(100) NOT NULL,
  PRIMARY KEY (`encryption_algo_id`)
);


/*Table structure for table `jq_enc_mode_lookup` */

DROP TABLE IF EXISTS jq_enc_mode_lookup;

CREATE TABLE `jq_enc_mode_lookup` (
  `mode_id` int(11) NOT NULL,
  `mode_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`mode_id`)
);


/*Table structure for table `jq_enc_padd_lookup` */

DROP TABLE IF EXISTS jq_enc_padd_lookup;

CREATE TABLE `jq_enc_padd_lookup` (
  `padding_id` int(11) NOT NULL,
  `padding_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`padding_id`)
);

/*Table structure for table `jq_enc_key_length_lookup` */

DROP TABLE IF EXISTS jq_enc_key_length_lookup;

CREATE TABLE `jq_enc_key_length_lookup` (
  `key_length_id` int(11) NOT NULL,
  `key_length` int(11) DEFAULT NULL,
  PRIMARY KEY (`key_length_id`)
);


/*Table structure for table `jq_enc_alg_mod_pad_lookup` */

DROP TABLE IF EXISTS jq_enc_alg_mod_pad_lookup;

CREATE TABLE `jq_enc_alg_mod_pad_key_lookup` (
  `enc_lookup_id` int(11) NOT NULL,
  `enc_algorithm_id` int(11) NOT NULL,
  `enc_mode_id` int(11) NOT NULL,
  `enc_padding_id` int(11) NOT NULL,
  `enc_key_length` int(11) DEFAULT NULL,
  PRIMARY KEY (`enc_lookup_id`),
  KEY `FK_algorithm` (`enc_algorithm_id`),
  KEY `FK_mode` (`enc_mode_id`),
  KEY `FK_padding` (`enc_padding_id`),
  KEY `FK_keyLength` (`enc_key_length`),
  CONSTRAINT `FK_algorithm` FOREIGN KEY (`enc_algorithm_id`) REFERENCES `jq_encryption_algorithms_lookup` (`encryption_algo_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_keyLength` FOREIGN KEY (`enc_key_length`) REFERENCES `jq_enc_key_length_lookup` (`key_length_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_mode` FOREIGN KEY (`enc_mode_id`) REFERENCES `jq_enc_mode_lookup` (`mode_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_padding` FOREIGN KEY (`enc_padding_id`) REFERENCES `jq_enc_padd_lookup` (`padding_id`) ON DELETE CASCADE ON UPDATE CASCADE
);


ALTER TABLE `jq_api_client_details` ADD CONSTRAINT `jq_encryption_algo_id_ibfk_1` FOREIGN KEY (`encryption_algo_id`) REFERENCES `jq_enc_alg_mod_pad_key_lookup`(`enc_lookup_id`);