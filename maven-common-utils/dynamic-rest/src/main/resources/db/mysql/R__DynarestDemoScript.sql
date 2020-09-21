SET FOREIGN_KEY_CHECKS=0; 

REPLACE INTO jws_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic,jws_service_logic_checksum, jws_platform_id) VALUES
(1, 'jemployees', 1, 'getEmployeeDetails', 'Method to get employee details', 2, 7, 'Map<String, Object> response = new HashMap<>();
response.put("response", parameters.get("employees"));
return response;',null, 1);

REPLACE INTO jws_dynamic_rest_dao_details(
	jws_dao_details_id
  ,jws_dynamic_rest_details_id
  ,jws_result_variable_name
  ,jws_dao_query_template
  ,jws_query_sequence
  ,checksum
) VALUES (
	1
   ,2
  ,"employees"
  ,"select first_name as firstName, last_name as lastName, email_id as emailId, address as address, city as city, date_format(joining_date, '%d %b %Y') as joiningData, employe_skill_sets as skills from employee"
  ,1
  ,null
);

REPLACE INTO jws_dynamic_rest_dao_details(
   jws_dao_details_id
  ,jws_dynamic_rest_details_id
  ,jws_result_variable_name
  ,jws_dao_query_template
  ,jws_query_sequence
  ,checksum
) VALUES (
	2
   ,2
  ,"employees"
  ,"select first_name as firstName, last_name as lastName, email_id as emailId, address as address, city as city, date_format(joining_date, '%d %b %Y') as joiningData, employe_skill_sets as skills from employee"
  ,1
  ,null
);

REPLACE INTO jws_dynamic_rest_dao_details(
   jws_dao_details_id
  ,jws_dynamic_rest_details_id
  ,jws_result_variable_name
  ,jws_dao_query_template
  ,jws_query_sequence
  ,checksum
) VALUES (
	3
   ,1
  ,"employees"
  ,"select first_name as firstName, last_name as lastName, email_id as emailId, address as address, city as city, date_format(joining_date, '%d %b %Y') as joiningData, employe_skill_sets as skills from employee"
  ,1
  ,null
);

REPLACE INTO jws_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic,jws_service_logic_checksum, jws_platform_id) VALUES
(2, '/dyn/api/employees', 1, 'getEmpDetails', 'Method to get employee details', 2, 7, '[
	<#list employees as employee>
		{
		    "firstName" : "${employee?api.get(''firstName'')}",
		    "lastName" : "${employee?api.get(''lastName'')}",
		    "emailId" : "${employee?api.get(''emailId'')}",
		    "address" : "${employee?api.get(''address'')}",
		    "city" : "${employee?api.get(''city'')}",
		    "joiningData" : "${employee?api.get(''joiningData'')}",
		    "skills" : "${employee?api.get(''skills'')}" 
		}
		<#if (employee_has_next)>,</#if>
	</#list>
]',null, 2);

SET FOREIGN_KEY_CHECKS=1;