ALTER TABLE dashlet_properties
 DROP FOREIGN KEY dashlet_properties_ibfk_1,
 CHANGE placeholder_name placeholder_name VARCHAR(200) NOT NULL,
 CHANGE display_name display_name VARCHAR(200) NOT NULL;
ALTER TABLE dashlet_properties
 ADD CONSTRAINT dashlet_properties_ibfk_1 FOREIGN KEY (dashlet_id) REFERENCES dashlet (dashlet_id) ON UPDATE RESTRICT ON DELETE RESTRICT;
