ALTER TABLE jq_module_listing ADD is_home_page INT(4) DEFAULT '0' AFTER is_inside_menu;

ALTER TABLE jq_module_role_association
 DROP FOREIGN KEY jq_module_role_association_ibfk_1,
 CHANGE role_id role_id VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL FIRST,
 ADD updated_by VARCHAR(100) AFTER module_id,
 ADD updated_date TIMESTAMP ON UPDATE CURRENT_TIMESTAMP DEFAULT current_timestamp(),
 ADD is_deleted INT(4) DEFAULT '0';
ALTER TABLE jq_module_role_association
 ADD CONSTRAINT jq_module_role_association_ibfk_1 FOREIGN KEY (module_id) REFERENCES jq_module_listing (module_id) ON UPDATE NO ACTION ON DELETE NO ACTION,
 DROP PRIMARY KEY,
 ADD PRIMARY KEY (role_id);
