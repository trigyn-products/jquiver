SET FOREIGN_KEY_CHECKS=0; 


DELETE FROM jq_dynamic_rest_dao_details WHERE jws_dynamic_rest_details_id =
(SELECT jws_dao_details_id FROM jq_dynamic_rest_details WHERE jws_method_name = "getEmployeeDetails");

DELETE FROM jq_dynamic_rest_details WHERE jws_method_name = "getEmployeeDetails";

DELETE FROM jq_dynamic_rest_dao_details WHERE jws_dynamic_rest_details_id =
(SELECT jws_dao_details_id FROM jq_dynamic_rest_details WHERE jws_method_name = "getEmpDetails");

DELETE FROM jq_dynamic_rest_details WHERE jws_method_name = "getEmpDetails";

SET FOREIGN_KEY_CHECKS=1;