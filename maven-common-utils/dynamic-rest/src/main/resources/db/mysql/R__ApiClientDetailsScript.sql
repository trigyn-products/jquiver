SET FOREIGN_KEY_CHECKS=0;

replace into jq_module_listing_i18n (module_id, language_id, module_name) VALUES ('26b42b12-8815-427c-9945-f1c7aa7064b3', 1, 'API Clients');

 REPLACE INTO jq_master_modules(module_id, module_name, sequence, is_system_module, grid_details_id, module_type, is_perm_supported, is_entity_perm_supported, is_imp_exp_supported) VALUES
('ded49cbd-ed7c-40ce-b1f8-c2e34ad33473', 'API Clients', 20, 1, 'jq_api_client_details_grid', 'ApiClientDetails', 0, 0, 1),
('799947cc-b6cb-11eb-8529-0242ac130003', 'Additional Datasource', 21, 1, 'jq-additional-datasourceGrid', 'AdditionalDatasource', 0, 0, 1);


Delete from  jq_encryption_algorithms_lookup  where encryption_algo_id = 4;
SET FOREIGN_KEY_CHECKS=1;