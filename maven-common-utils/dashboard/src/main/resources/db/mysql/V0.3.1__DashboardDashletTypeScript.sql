ALTER TABLE dashboard MODIFY dashboard_type INT(11) DEFAULT 1;
ALTER TABLE dashlet ADD COLUMN dashlet_type_id INT(11) DEFAULT 1;