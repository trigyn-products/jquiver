
SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO jq_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_dynamic_rest_type_id, created_by, created_date, last_updated_ts) VALUES
('753e3330-e349-4d67-be25-f3824203d522', 'validate-user-email-id', 1, 'validateUserEmailId', 'Validate User Email Id', 2, 7, 'function validateEmailId(requestDetails, daoResults) {
    return daoResults["emailIdCount"];
}

validateEmailId(requestDetails, daoResults);', 3, 2, 'aar.dev@trigyn.com', NOW(), NOW());

REPLACE INTO jq_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(110, '753e3330-e349-4d67-be25-f3824203d522', 'emailIdCount','SELECT COUNT(ju.email) AS emailCount FROM jq_user AS ju WHERE ju.email = :email', 1, 2);

SET FOREIGN_KEY_CHECKS=1;
