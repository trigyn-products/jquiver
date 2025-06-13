ALTER TABLE jq_dynamic_form   
	ADD COLUMN is_captcha_enabled INT(1) DEFAULT 0;
	
ALTER TABLE jq_dynamic_form   
	ADD COLUMN is_csrf_enabled INT(1) DEFAULT 0;