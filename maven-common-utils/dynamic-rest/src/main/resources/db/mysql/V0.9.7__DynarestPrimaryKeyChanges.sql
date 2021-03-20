ALTER TABLE jq_dynamic_rest_dao_details
 DROP FOREIGN KEY jq_dynamic_rest_dao_details_ibfk_1,
 CHANGE jws_dynamic_rest_details_id jws_dynamic_rest_details_id VARCHAR(50) NOT NULL;
  
ALTER TABLE jq_dynamic_rest_response_params
 DROP FOREIGN KEY jq_dynamic_rest_response_params_ibfk_1,
 CHANGE jws_dynamic_rest_details_id jws_dynamic_rest_details_id VARCHAR(50) NOT NULL;

ALTER TABLE jq_dynamic_rest_role_association
 DROP FOREIGN KEY jq_dynamic_rest_role_association_ibfk_1,
 CHANGE jws_dynamic_rest_id jws_dynamic_rest_id VARCHAR(50) NOT NULL;

ALTER TABLE jq_dynamic_rest_details
 CHANGE jws_dynamic_rest_id jws_dynamic_rest_id VARCHAR(50) NOT NULL;

ALTER TABLE jq_dynamic_rest_dao_details
 ADD CONSTRAINT jq_dynamic_rest_dao_details_ibfk_1 FOREIGN KEY (jws_dynamic_rest_details_id) REFERENCES jq_dynamic_rest_details (jws_dynamic_rest_id) ON UPDATE CASCADE ON DELETE RESTRICT;
 