ALTER TABLE `jq_datasource_lookup`   
	ADD COLUMN `connection_url_pattern` VARCHAR(500);

UPDATE jq_datasource_lookup SET connection_url_pattern = 'jdbc:postgresql://host:port/database' WHERE datasource_lookup_id='87eeb3a4-9611-11eb-a295-f48e38ab8cd7';
UPDATE jq_datasource_lookup SET connection_url_pattern = 'jdbc:mysql://[host][,failoverhost...][:port]/[database][?propertyName1][=propertyValue1][&propertyName2][=propertyValue2]...' WHERE datasource_lookup_id='d03753ea-9611-11eb-a295-f48e38ab8cd7';
UPDATE jq_datasource_lookup SET connection_url_pattern = 'jdbc:mariadb://localhost:3306/DB?user=root&password=myPassword' WHERE datasource_lookup_id='d4099a61-9611-11eb-a295-f48e38ab8cd7';
UPDATE jq_datasource_lookup SET connection_url_pattern = 'jdbc:sqlserver://[serverName[\instanceName][:portNumber]][;property=value[;property=value]]' WHERE datasource_lookup_id='d880d3ac-9611-11eb-a295-f48e38ab8cd7';
UPDATE jq_datasource_lookup SET connection_url_pattern = 'jdbc:oracle:<drivertype>:<user>/<password>@<database>' WHERE datasource_lookup_id='db39e0f9-9611-11eb-a295-f48e38ab8cd7';