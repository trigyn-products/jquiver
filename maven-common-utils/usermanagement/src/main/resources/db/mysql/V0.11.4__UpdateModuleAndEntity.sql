ALTER TABLE jws_master_modules
 ADD module_type_id INT(11) AFTER auxiliary_data;
 
 UPDATE jws_master_modules SET module_type_id = "2" WHERE module_id="07067149-098d-11eb-9a16-f48e38ab9348";
 UPDATE jws_master_modules SET module_type_id = "3" WHERE module_id="1b0a2e40-098d-11eb-9a16-f48e38ab9348";
 UPDATE jws_master_modules SET module_type_id = "4" WHERE module_id="30a0ff61-0ecf-11eb-94b2-f48e38ab9348";
 UPDATE jws_master_modules SET module_type_id = "5" WHERE module_id="47030ee1-0ecf-11eb-94b2-f48e38ab9348";
 UPDATE jws_master_modules SET module_type_id = "6" WHERE module_id="91a81b68-0ece-11eb-94b2-f48e38ab9348";
 UPDATE jws_master_modules SET module_type_id = "7" WHERE module_id="b0f8646c-0ecf-11eb-94b2-f48e38ab9348";
 UPDATE jws_master_modules SET module_type_id = "8" WHERE module_id="c6cc466a-0ed3-11eb-94b2-f48e38ab9348";
 
 
 ALTER TABLE jws_entity_role_association
 ADD module_type_id INT(11)  DEFAULT '0' AFTER is_active;
 
 
UPDATE jws_entity_role_association
SET module_type_id = 1
WHERE entity_id IN ("9378ee23-09fa-11eb-a894-f48e38ab8cd7",
                    "99a707e5-09fa-11eb-a894-f48e38ab8cd7",
                    "bef1d368-13be-11eb-9b1e-f48e38ab9348",
                    "16918f98-19f6-11eb-9631-f48e38ab9348",
                    "rolesAutocomplete",
                    "348d7075-138c-11eb-9b1e-f48e38ab9348",
                    "edcafd25-19b9-11eb-9631-f48e38ab9348",
                    "0c7acdf9-138c-11eb-9b1e-f48e38ab9348",
                    "16e9ddec-138c-11eb-9b1e-f48e38ab9348",
                    "28207b8f-138c-11eb-9b1e-f48e38ab9348",
                    "1ef38eb1-138c-11eb-9b1e-f48e38ab9348",
                    "11c869d4-138c-11eb-9b1e-f48e38ab9348",
                    "2d3d1d39-138c-11eb-9b1e-f48e38ab9348",
                    "3b92295c-138c-11eb-9b1e-f48e38ab9348",
                    "42bf58ce-09fa-11eb-a894-f48e38ab8cd7");
                    
UPDATE jws_entity_role_association
SET module_type_id = 2
WHERE entity_id IN ("26f2589f-09fa-11eb-a894-f48e38ab8cd7",
                    "gridDetailsListing",
                    "8a80cb8174bebc3c0174bec1892c0000"
                    );  
                    
UPDATE jws_entity_role_association
SET module_type_id = 3
WHERE entity_id IN ("1dff39e8-001f-11eb-97bf-e454e805e22f",
                    "4d91fbd8-09fa-11eb-a894-f48e38ab8cd7",
                    "templateListingGrid",
                    "b70d0b8b-1da0-11eb-9428-f48e38ab9348"
                    );    
                    
UPDATE jws_entity_role_association
SET module_type_id = 4
WHERE entity_id IN ("ba1845fd-09ac-11eb-a027-f48e38ab8cd7",
                    "be46dd5b-09ac-11eb-a027-f48e38ab8cd7",
                    "dynamicFormListingGrid"
                    );                      

UPDATE jws_entity_role_association
SET module_type_id = 5
WHERE entity_id IN ("8a80cb81749b028401749b062c540002",
                    "b70d043c-1da0-11eb-9428-f48e38ab9348",
                    "dynarestGrid",
                    "8a80cb81749ab40401749ac2e7360000"
                    );                                         
                     
UPDATE jws_entity_role_association
SET module_type_id = 6
WHERE entity_id IN ("7e8438bf-1061-11eb-a867-f48e38ab8cd7",
                    "85f44645-1061-11eb-a867-f48e38ab8cd7",
                    "autocompleteListingGrid"
                    );       
                    
UPDATE jws_entity_role_association
SET module_type_id = 7
WHERE entity_id IN ("365e2aa5-09ac-11eb-a027-f48e38ab8cd7",
                    "410deeb3-09ac-11eb-a027-f48e38ab8cd7",
                    "0d91402d-1062-11eb-a867-f48e38ab8cd7",
                    "1389accc-1062-11eb-a867-f48e38ab8cd7",
                    "dashboardMasterListingGrid",
                    "dashletMasterListingGrid",
                    "365e2aa5-09ac-11eb-a027-f48e38ab8cd7",
                    "410deeb3-09ac-11eb-a027-f48e38ab8cd7"
                    ); 
                    
UPDATE jws_entity_role_association
SET module_type_id = 8
WHERE entity_id IN ("8ba1a465-09fa-11eb-a894-f48e38ab8cd7",
                    "89ee344b-03f6-11eb-a183-e454e805e22f",
                    "moduleListingGrid",
                    "templateListing"
                    );                       
                                                          
 CREATE TABLE persistent_logins(
	username varchar(50) not null,
	series varchar(64) primary key,
	token varchar(64) not null,
	last_used timestamp not null
);                   
                                     
 