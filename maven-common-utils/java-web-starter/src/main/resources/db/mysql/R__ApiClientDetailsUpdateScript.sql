DELETE FROM jq_encryption_algorithms_lookup;

/*Data for the table `jq_encryption_algorithms_lookup` */

REPLACE  INTO `jq_encryption_algorithms_lookup`(`encryption_algo_id`,`encryption_algo_name`) VALUES 
(1,'AES');


/*Data for the table `jq_enc_mode_lookup` */

REPLACE  INTO `jq_enc_mode_lookup`(`mode_id`,`mode_name`) VALUES 
(1,'CBC'),
(2,'CFB'),
(3,'OFB'),
(4,'CTR'),
(5,'GCM'),
(6,'ECB');

/*Data for the table `jq_enc_padd_lookup` */

REPLACE  INTO `jq_enc_padd_lookup`(`padding_id`,`padding_name`) VALUES 
(1,'NoPadding'),
(2,'OAEPWithSHA-1AndMGF1Padding'),
(3,'OAEPWithSHA-256AndMGF1Padding'),
(4,'PKCS1Padding'),
(5,'PKCS5Padding'),
(6,'PKCS7Padding');


/*Data for the table `jq_enc_key_length_lookup` */

REPLACE  INTO `jq_enc_key_length_lookup`(`key_length_id`,`key_length`) VALUES 
(10,56),
(20,128),
(30,168),
(40,192),
(50,256),
(60,1024),
(70,2048);


/*Data for the table `jq_enc_alg_mod_pad_lookup` */

REPLACE  INTO `jq_enc_alg_mod_pad_key_lookup`(`enc_lookup_id`,`enc_algorithm_id`,`enc_mode_id`,`enc_padding_id`,`enc_key_length`) VALUES 
(1,1,1,1,20),
(2,1,1,5,20),
(3,1,6,1,20),
(4,1,6,5,20);
