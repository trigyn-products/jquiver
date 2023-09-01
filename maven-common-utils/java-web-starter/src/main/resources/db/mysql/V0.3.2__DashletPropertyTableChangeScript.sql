ALTER TABLE jq_dashlet_properties
 DROP FOREIGN KEY jq_dashlet_properties_ibfk_1,
 CHANGE placeholder_name placeholder_name VARCHAR(200) NOT NULL,
 CHANGE display_name display_name VARCHAR(200) NOT NULL;
ALTER TABLE jq_dashlet_properties
 ADD CONSTRAINT jq_dashlet_properties_ibfk_1 FOREIGN KEY (dashlet_id) REFERENCES jq_dashlet (dashlet_id) ON UPDATE RESTRICT ON DELETE RESTRICT;
