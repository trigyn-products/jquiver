ALTER TABLE jq_master_modules
 CHANGE auxiliary_data auxiliary_data LONGTEXT CHARACTER SET utf8 COLLATE utf8_general_ci;

SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO jq_master_modules(module_id, module_name, is_system_module, auxiliary_data, module_type_id) VALUES
('07067149-098d-11eb-9a16-f48e38ab9348', 'Grid Utils', 1, '"<form id=\\"addEditGridForm\\" action=\\"/cf/df\\" method=\\"post\\" class=\\"margin-r-5 pull-left\\"><input type=\\"hidden\\" name=\\"formId\\" value=\\"8a80cb8174bebc3c0174bec1892c0000\\"><input type=\\"hidden\\" name=\\"primaryId\\" id=\\"primaryId\\" value=\\"\\"></form>"', 2), 
('19aa8996-80a2-11eb-971b-f48e38ab8cd7', 'Dashlet', 1, '"<form action=\\"/cf/aedl\\" method=\\"POST\\" id=\\"formDMRedirect\\"><input type=\\"hidden\\" id=\\"dashletId\\" name=\\"dashlet-id\\"></form>"', 10), 
('1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'Templating', 1, '"<form action=\\"/cf/aet\\" method=\\"GET\\" id=\\"formFMRedirect\\"><input type=\\"hidden\\" id=\\"vmMasterId\\" name=\\"vmMasterId\\"></form>"', 3), 
('248ffd91-7760-11eb-94ed-f48e38ab8cd7', 'File Bin', 1, '"<form id=\\"formFileUpload\\" action=\\"/cf/df\\" method=\\"post\\" class=\\"margin-r-5 pull-left\\"><input type=\\"hidden\\" name=\\"formId\\" value=\\"40289d3d750decc701750e3f1e3c0000\\"/><input type=\\"hidden\\" name=\\"fileBinId\\" id=\\"fileBinId\\" value=\\"\\"/></form>"', 9), 
('30a0ff61-0ecf-11eb-94b2-f48e38ab9348', 'Form Builder', 1, '"<form action=\\"/cf/aedf\\" method=\\"POST\\" id=\\"addEditDynamicForm\\">\\t<input type=\\"hidden\\" id=\\"formId\\" name=\\"form-id\\"></form>"', 4), 
('47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'REST API Builder', 1, '"<form id=\\"addEditDynarest\\" action=\\"/cf/df\\" method=\\"post\\" class=\\"margin-r-5 pull-left\\"><input type=\\"hidden\\" name=\\"formId\\" value=\\"8a80cb81749ab40401749ac2e7360000\\"><input type=\\"hidden\\" name=\\"primaryId\\" id=\\"primaryId\\" value=\\"\\"><input type=\\"hidden\\" name=\\"urlPrefix\\" id=\\"urlPrefix\\" value=\\"\\"></form>"', 5), 
('91a81b68-0ece-11eb-94b2-f48e38ab9348', 'TypeAhead Autocomplete', 1, '"<form action=\\"/cf/aea\\" method=\\"GET\\" id=\\"formACRedirect\\"><input type=\\"hidden\\" id=\\"acId\\" name=\\"acId\\"></form>"', 6), 
('b0f8646c-0ecf-11eb-94b2-f48e38ab9348', 'Dashboard', 1, '"<form action=\\"/cf/aedb\\" method=\\"POST\\" id=\\"formDBRedirect\\"><input type=\\"hidden\\" id=\\"dashboardId\\" name=\\"dashboard-id\\"></form>"', 7);


SET FOREIGN_KEY_CHECKS=1;