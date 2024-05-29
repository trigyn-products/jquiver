DELETE FROM jq_dashboard_lookup_category WHERE lookup_category_id = 'c1f03803-c862-11e7-a62a-f48e38ab9229';
DELETE FROM jq_template_master WHERE template_id = '46154204-1062-11eb-a867-f48e38ab8cd7';


SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO jq_dashlet_properties (property_id, dashlet_id, placeholder_name, display_name, type_id, value, validation, default_value, configuration_script, is_deleted, to_display, sequence) VALUES
('8178b7ea-b874-4c31-8732-db76419a4f0f', '31c9ffa9-eadf-11ea-a036-e454e805e22f', 'startDate', 'Start Date', '368747b0-1e8b-11e8-8d69-000d3a173cc5', '', '', '30-11-2020', NULL, 0, 1, 0), 
('10126791-74e7-48de-8778-2f5c344e2cc5', '31c9ffa9-eadf-11ea-a036-e454e805e22f', 'endDate', 'End Date', '368747b0-1e8b-11e8-8d69-000d3a173cc5', '', '', '05-01-2021', NULL, 0, 1, 1);

REPLACE INTO jq_dashlet_properties (property_id, dashlet_id, placeholder_name, display_name, type_id, value, validation, default_value, configuration_script, is_deleted, to_display, sequence) VALUES
('e7af6a9e-9b32-4c06-bcb5-d107723b3fcf', '3d97273b-eadf-11ea-a036-e454e805e22f', 'startDate', 'Start Date', '368747b0-1e8b-11e8-8d69-000d3a173cc5', '', '', '05-11-2018', NULL, 0, 1, 0),
('9d3d7bc9-8b06-4b08-95b2-7d0a786381ab', '3d97273b-eadf-11ea-a036-e454e805e22f', 'endDate', 'End Date', '368747b0-1e8b-11e8-8d69-000d3a173cc5', '', '', '05-10-2019', NULL, 0, 1, 1);

REPLACE INTO jq_dashboard_dashlet_association (dashboard_id, dashlet_id) VALUES ('ab7202bf-eadd-11ea-a036-e454e805e22f','76ad58a3-afa3-4efd-a872-9a78a9e01a94');


SET FOREIGN_KEY_CHECKS=1;


