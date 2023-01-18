 SET FOREIGN_KEY_CHECKS=0;
 
UPDATE jq_master_modules SET auxiliary_data = '"<form id=\\"addEditDynarest\\" action=\\"/cf/df\\" method=\\"post\\" class=\\"margin-r-5 pull-left\\"><input type=\\"hidden\\" name=\\"formId\\" value=\\"f44ac7ab-c61e-4df3-b40f-190262f79a39\\"><input type=\\"hidden\\" name=\\"schedulerid\\" id=\\"schedulerid\\"></form>"' WHERE module_id="fcd0df1f-783f-11eb-94ed-f48e38ab8cd6";
UPDATE jq_master_modules SET auxiliary_data = '"<form id=\\"addEditDynarest\\" action=\\"/cf/df\\" method=\\"post\\" class=\\"margin-r-5 pull-left\\"><input type=\\"hidden\\" name=\\"formId\\" value=\\"2acd30d0-ef07-4ce2-b4f4-02cbfd29bf67\\"><input type=\\"hidden\\" name=\\"clientid\\" id=\\"clientid\\"></form>"' WHERE module_id="ded49cbd-ed7c-40ce-b1f8-c2e34ad33473";
UPDATE jq_master_modules SET auxiliary_data = '"<form id=\\"addEditDynarest\\" action=\\"/cf/df\\" method=\\"post\\" class=\\"margin-r-5 pull-left\\"><input type=\\"hidden\\" name=\\"formId\\" value=\\"a4eee08e-d5a5-4b8e-8422-69e840be7e13\\"><input type=\\"hidden\\" name=\\"additionalDatasourceId\\" id=\\"additionalDatasourceId\\"></form>"' WHERE module_id="799947cc-b6cb-11eb-8529-0242ac130003";
UPDATE jq_master_modules SET auxiliary_data = '"<form id=\\"formFileUpload\\" action=\\"/cf/df\\" method=\\"post\\" class=\\"margin-r-5 pull-left\\"><input type=\\"hidden\\" name=\\"formId\\" value=\\"40289d3d750decc701750e3f1e3c0000\\"><input type=\\"hidden\\" name=\\"fileBinId\\" id=\\"fileBinId\\" value=\\"\\"></form>"' WHERE module_id="248ffd91-7760-11eb-94ed-f48e38ab8cd7";
UPDATE jq_master_modules SET auxiliary_data = '"<form action=\\"/cf/aem\\" method=\\"post\\" id=\\"formMuRedirect\\"><input type=\\"hidden\\" id=\\"moduleId\\" name=\\"module-id\\"></form>"' WHERE module_id="c6cc466a-0ed3-11eb-94b2-f48e38ab9348";

DELETE FROM `jq_master_modules` WHERE `module_id` = '699ac104-8c8f-11eb-8dcd-0242ac130003' ;

SET FOREIGN_KEY_CHECKS=1;
