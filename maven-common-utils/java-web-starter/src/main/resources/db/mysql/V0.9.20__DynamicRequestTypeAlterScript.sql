ALTER TABLE jq_request_type_details ADD COLUMN IF NOT EXISTS priority INT(11) ;

UPDATE jq_request_type_details SET priority = 1 WHERE jws_request_type_details_id = 2;

UPDATE jq_request_type_details SET priority = 2 WHERE jws_request_type_details_id = 1;

UPDATE jq_request_type_details SET priority = 3 WHERE jws_request_type_details_id = 4;

UPDATE jq_request_type_details SET priority = 4 WHERE jws_request_type_details_id = 5;

UPDATE jq_request_type_details SET priority = 5 WHERE jws_request_type_details_id = 7;

UPDATE jq_request_type_details SET priority = 6 WHERE jws_request_type_details_id = 3;

UPDATE jq_request_type_details SET priority = 7 WHERE jws_request_type_details_id = 6;

UPDATE jq_request_type_details SET priority = 8 WHERE jws_request_type_details_id = 8;