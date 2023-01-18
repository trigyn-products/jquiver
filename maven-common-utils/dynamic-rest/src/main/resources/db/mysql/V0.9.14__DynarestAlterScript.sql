SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE jq_dynamic_rest_dao_details
ADD COLUMN datasource_id VARCHAR(50) DEFAULT NULL AFTER jws_dao_query_type,
ADD CONSTRAINT jq_dynamic_rest_dao_details_ibfk_3 FOREIGN KEY (datasource_id) REFERENCES jq_additional_datasource (additional_datasource_id);

UPDATE jq_dynamic_rest_details jdrd, jq_dynamic_rest_dao_details jdrdd SET jdrdd.datasource_id = jdrd.datasource_id WHERE jdrd.`jws_dynamic_rest_id` = jdrdd. jws_dynamic_rest_details_id;

ALTER TABLE jq_dynamic_rest_details DROP COLUMN datasource_id, 
  DROP INDEX jq_dynamic_rest_details_ibfk_3,
  DROP FOREIGN KEY jq_dynamic_rest_details_ibfk_3;
  
SET FOREIGN_KEY_CHECKS=1;
