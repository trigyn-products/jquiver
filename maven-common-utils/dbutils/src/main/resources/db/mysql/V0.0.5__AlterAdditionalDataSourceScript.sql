ALTER TABLE jq_datasource_lookup ADD COLUMN datasource_supported_version decimal(10,2);

UPDATE jq_datasource_lookup SET datasource_supported_version = '9.2' WHERE datasource_lookup_id='87eeb3a4-9611-11eb-a295-f48e38ab8cd7';
UPDATE jq_datasource_lookup SET datasource_supported_version = '8.0' WHERE datasource_lookup_id='d03753ea-9611-11eb-a295-f48e38ab8cd7';
UPDATE jq_datasource_lookup SET datasource_supported_version = '10.2' WHERE datasource_lookup_id='d4099a61-9611-11eb-a295-f48e38ab8cd7';
UPDATE jq_datasource_lookup SET datasource_supported_version = '15.0' WHERE datasource_lookup_id='d880d3ac-9611-11eb-a295-f48e38ab8cd7';
UPDATE jq_datasource_lookup SET datasource_supported_version = '12.0' WHERE datasource_lookup_id='db39e0f9-9611-11eb-a295-f48e38ab8cd7';

ALTER TABLE jq_datasource_lookup ADD COLUMN db_product_display_name varchar(100);

UPDATE jq_datasource_lookup SET db_product_display_name = 'Postgres SQL' WHERE datasource_lookup_id='87eeb3a4-9611-11eb-a295-f48e38ab8cd7';
UPDATE jq_datasource_lookup SET db_product_display_name = 'MySQL' WHERE datasource_lookup_id='d03753ea-9611-11eb-a295-f48e38ab8cd7';
UPDATE jq_datasource_lookup SET db_product_display_name = 'MariaDB' WHERE datasource_lookup_id='d4099a61-9611-11eb-a295-f48e38ab8cd7';
UPDATE jq_datasource_lookup SET db_product_display_name = 'MS SQL Server' WHERE datasource_lookup_id='d880d3ac-9611-11eb-a295-f48e38ab8cd7';
UPDATE jq_datasource_lookup SET db_product_display_name = 'Oracle' WHERE datasource_lookup_id='db39e0f9-9611-11eb-a295-f48e38ab8cd7';
