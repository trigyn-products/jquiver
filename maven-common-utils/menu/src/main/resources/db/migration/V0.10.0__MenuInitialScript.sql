SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS module_target_lookup;
CREATE TABLE module_target_lookup(
lookup_id INT(11) AUTO_INCREMENT,
description VARCHAR(250) NOT NULL,
PRIMARY KEY (lookup_id))ENGINE=InnoDB CHARSET=utf8;


DROP TABLE IF EXISTS module_listing;
CREATE TABLE module_listing (
module_id VARCHAR(50)
, module_url VARCHAR(250) NOT NULL
, parent_id VARCHAR(50) DEFAULT NULL
, target_lookup_id INT(11) NOT NULL 
, target_type_id VARCHAR(50) NOT NULL
, sequence INT(11) NOT NULL
, PRIMARY KEY (module_id)
,KEY target_lookup_id (target_lookup_id)
,CONSTRAINT module_listing_ibfk_1 FOREIGN KEY (`target_lookup_id`) REFERENCES  module_target_lookup (`lookup_id`)
)ENGINE=InnoDB CHARSET=utf8;

DROP TABLE IF EXISTS module_listing_i18n;
CREATE TABLE module_listing_i18n(
module_id VARCHAR(50) NOT NULL
,language_id INT(11) NOT NULL
, module_name VARCHAR(500) NOT NULL
, PRIMARY KEY (module_id, language_id)
,KEY module_id (module_id)
,KEY language_id (module_id)
,CONSTRAINT module_listing_i18n_ibfk_1 FOREIGN KEY (`module_id`) REFERENCES  module_listing (`module_id`)
,CONSTRAINT module_listing_i18n_ibfk_2 FOREIGN KEY (`language_id`) REFERENCES  language (`language_id`)
)ENGINE=InnoDB CHARSET=utf8;

DROP TABLE IF EXISTS module_role_association;
CREATE TABLE module_role_association(
module_id VARCHAR(50) NOT NULL
,role_id VARCHAR(100) NOT NULL
,PRIMARY KEY (module_id,role_id)
,KEY module_id (module_id)
,CONSTRAINT module_role_association_ibfk_1 FOREIGN KEY (`module_id`) REFERENCES  module_listing (`module_id`)
)ENGINE=InnoDB CHARSET=utf8;


INSERT INTO module_target_lookup (lookup_id,description) VALUES (1,'Dashboard');
INSERT INTO module_target_lookup (lookup_id,description) VALUES (2,'Dynamic Form');
INSERT INTO module_target_lookup (lookup_id,description) VALUES (3,'Dynamic REST');
INSERT INTO module_target_lookup (lookup_id,description) VALUES (4,'Model and View');
INSERT INTO module_target_lookup (lookup_id,description) VALUES (5,'Template');


INSERT INTO module_listing (module_id,module_url,parent_id,target_lookup_id,target_type_id,sequence) VALUES('40289d3d7496acd4017496b333ea0002', '/cf/gd', NULL, 1, '4a747f47-f80f-11ea-9507-f48e38ab8cd7', 1);
INSERT INTO module_listing (module_id,module_url,parent_id,target_lookup_id,target_type_id,sequence) VALUES('40289d3d7496acd4017496b39da00003', '/cf/te', NULL, 1, '4a745831-f80f-11ea-9507-f48e38ab8cd7', 2); 
INSERT INTO module_listing (module_id,module_url,parent_id,target_lookup_id,target_type_id,sequence) VALUES('40289d3d7496acd4017496b420980004', '/cf/rb', NULL, 1, '4a748e48-f80f-11ea-9507-f48e38ab8cd7', 3); 
INSERT INTO module_listing (module_id,module_url,parent_id,target_lookup_id,target_type_id,sequence) VALUES('40289d3d7496acd4017496b4ceea0005', '/cf/adl', NULL, 1, 'e60b39f1-f804-11ea-9507-f48e38ab8cd7', 4); 
INSERT INTO module_listing (module_id,module_url,parent_id,target_lookup_id,target_type_id,sequence) VALUES('40289d3d7496acd4017496b54fb80006', '/cf/nl', NULL, 1, '4a74cd65-f80f-11ea-9507-f48e38ab8cd7', 5);

INSERT INTO module_listing (module_id,module_url,parent_id,target_lookup_id,target_type_id,sequence) VALUES
('40289d3d7496d937017496da5b900000', '/cf/dbm', NULL, 1, '4a72512e-f80f-11ea-9507-f48e38ab8cd7', 6);
INSERT INTO module_listing (module_id,module_url,parent_id,target_lookup_id,target_type_id,sequence) VALUES('40289d3d7496d937017496db17580001', '/cf/dlm', '40289d3d7496d937017496da5b900000', 1, '4a732147-f80f-11ea-9507-f48e38ab8cd7', 7);
INSERT INTO module_listing (module_id,module_url,parent_id,target_lookup_id,target_type_id,sequence) VALUES('40289d3d7496d937017496dbafa10002', '/cf/dfl', NULL, 1, '4a741a58-f80f-11ea-9507-f48e38ab8cd7', 8);


INSERT INTO module_listing_i18n (module_id,language_id,module_name) VALUES ('40289d3d7496acd4017496b333ea0002', 1, 'Grid');
INSERT INTO module_listing_i18n (module_id,language_id,module_name) VALUES ('40289d3d7496acd4017496b39da00003', 1, 'Template Utils'); 
INSERT INTO module_listing_i18n (module_id,language_id,module_name) VALUES ('40289d3d7496acd4017496b420980004', 1, 'DB Resource Bundle'); 
INSERT INTO module_listing_i18n (module_id,language_id,module_name) VALUES ('40289d3d7496acd4017496b4ceea0005', 1, 'TypeAhead');
INSERT INTO module_listing_i18n (module_id,language_id,module_name) VALUES ('40289d3d7496acd4017496b54fb80006', 1, 'Notification'); 
INSERT INTO module_listing_i18n (module_id,language_id,module_name) VALUES ('40289d3d7496d937017496da5b900000', 1, 'Dashboard');
INSERT INTO module_listing_i18n (module_id,language_id,module_name) VALUES ('40289d3d7496d937017496db17580001', 1, 'Dashlet');
INSERT INTO module_listing_i18n (module_id,language_id,module_name) VALUES ('40289d3d7496d937017496dbafa10002', 1, 'Dynamic Form');

SET FOREIGN_KEY_CHECKS = 1;
