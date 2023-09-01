ALTER TABLE jq_module_listing
 ADD `last_modified_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp();


 