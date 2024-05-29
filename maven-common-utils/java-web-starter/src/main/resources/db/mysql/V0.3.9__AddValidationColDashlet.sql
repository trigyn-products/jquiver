ALTER TABLE `jq_dashlet_properties`   
	ADD COLUMN `validation` LONGTEXT NULL AFTER `value`;
	
ALTER TABLE `jq_dashboard_lookup_category`   
	ADD COLUMN `validation` LONGTEXT NULL AFTER `lookup_description`;