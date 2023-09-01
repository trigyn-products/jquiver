ALTER TABLE jq_dashboard   
	DROP COLUMN context_id, 
	ADD COLUMN dashboard_body LONGTEXT NOT NULL;
	
ALTER TABLE jq_dashlet   
 DROP COLUMN context_id, 
  DROP INDEX jq_dashlet_ibfk_1,
  DROP FOREIGN KEY jq_dashlet_ibfk_1;

	