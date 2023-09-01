SET FOREIGN_KEY_CHECKS=0;

replace into jq_module_listing_i18n (module_id, language_id, module_name) VALUES ('26b42b12-8815-427c-9945-f1c7aa7064b3', 1, 'API Clients');

Delete from  jq_encryption_algorithms_lookup  where encryption_algo_id = 4;
Delete from  jq_encryption_algorithms_lookup  where encryption_algo_id = 0;
Delete from  jq_encryption_algorithms_lookup  where encryption_algo_id = 1;
SET FOREIGN_KEY_CHECKS=1;