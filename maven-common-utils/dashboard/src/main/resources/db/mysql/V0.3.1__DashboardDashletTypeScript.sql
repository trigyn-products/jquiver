ALTER TABLE jq_dashboard MODIFY dashboard_type INT(11) DEFAULT 1;
ALTER TABLE jq_dashlet ADD COLUMN dashlet_type_id INT(11) DEFAULT 1;