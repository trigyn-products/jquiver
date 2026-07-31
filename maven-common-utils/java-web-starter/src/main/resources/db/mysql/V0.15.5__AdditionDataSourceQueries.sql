ALTER TABLE `jq_datasource_lookup` ADD COLUMN `connection_properties` LONGTEXT NULL AFTER `connection_url_pattern`;

ALTER TABLE `jq_additional_datasource` ADD COLUMN `datasource_properties` LONGTEXT NULL AFTER `is_custom_updated`;  


UPDATE jq_datasource_lookup
SET connection_properties='[
{"key":"sslmode","defaultValue":"disable","description":"SSL mode (disable,require,verify-ca,verify-full)"},
{"key":"connectTimeout","defaultValue":"10","description":"Connection timeout in seconds"},
{"key":"socketTimeout","defaultValue":"30","description":"Socket read timeout in seconds"},
{"key":"ApplicationName","defaultValue":"JQuiver","description":"Application name"},
{"key":"tcpKeepAlive","defaultValue":"true","description":"Enable TCP KeepAlive"}
]'
WHERE datasource_lookup_id='87eeb3a4-9611-11eb-a295-f48e38ab8cd7';



UPDATE jq_datasource_lookup
SET connection_properties='[
{"key":"useSSL","defaultValue":"false","description":"Enable SSL"},
{"key":"allowPublicKeyRetrieval","defaultValue":"true","description":"Allow public key retrieval"},
{"key":"serverTimezone","defaultValue":"UTC","description":"Server timezone"},
{"key":"connectTimeout","defaultValue":"10000","description":"Connection timeout (ms)"},
{"key":"socketTimeout","defaultValue":"30000","description":"Socket timeout (ms)"}
]'
WHERE datasource_lookup_id='d03753ea-9611-11eb-a295-f48e38ab8cd7';



UPDATE jq_datasource_lookup
SET connection_properties='[
{"key":"useSsl","defaultValue":"false","description":"Enable SSL"},
{"key":"connectTimeout","defaultValue":"10000","description":"Connection timeout (ms)"},
{"key":"socketTimeout","defaultValue":"30000","description":"Socket timeout (ms)"},
{"key":"tcpKeepAlive","defaultValue":"true","description":"Enable TCP KeepAlive"}
]'
WHERE datasource_lookup_id='d4099a61-9611-11eb-a295-f48e38ab8cd7';




UPDATE jq_datasource_lookup
SET connection_properties='[
{"key":"encrypt","defaultValue":"false","description":"Enable encryption"},
{"key":"trustServerCertificate","defaultValue":"true","description":"Trust server certificate"},
{"key":"loginTimeout","defaultValue":"30","description":"Login timeout"},
{"key":"socketTimeout","defaultValue":"30000","description":"Socket timeout"}
]'
WHERE datasource_lookup_id='d880d3ac-9611-11eb-a295-f48e38ab8cd7';



UPDATE jq_datasource_lookup
SET connection_properties='[
{"key":"oracle.net.CONNECT_TIMEOUT","defaultValue":"10000","description":"Connection timeout (ms)"},
{"key":"oracle.jdbc.ReadTimeout","defaultValue":"30000","description":"Read timeout (ms)"},
{"key":"defaultRowPrefetch","defaultValue":"50","description":"Rows prefetched"}
]'
WHERE datasource_lookup_id='db39e0f9-9611-11eb-a295-f48e38ab8cd7';



insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69be8c-7f4b-11f1-a120-7c8ae1adc180','IBMDb2','com.ibm.db2.jcc.DB2Driver','0',NULL,'IBM DB2','jdbc:db2://host:port/database',NULL);
insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69becb-7f4b-11f1-a120-7c8ae1adc180','SQLite','org.sqlite.JDBC','0',NULL,'SQLite','jdbc:sqlite:/path/to/database.db',NULL);
insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69bf0b-7f4b-11f1-a120-7c8ae1adc180','SAPHANA','com.sap.db.jdbc.Driver','0',NULL,'SAP HANA','jdbc:sap://host:port',NULL);
insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69bf44-7f4b-11f1-a120-7c8ae1adc180','Informix','com.informix.jdbc.IfxDriver','0',NULL,'Informix','jdbc:informix-sqli://host:port/database:INFORMIXSERVER=server',NULL);
insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69bf87-7f4b-11f1-a120-7c8ae1adc180','Firebird','org.firebirdsql.jdbc.FBDriver','0',NULL,'Firebird','jdbc:firebirdsql://host:port/database',NULL);
insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69bfc9-7f4b-11f1-a120-7c8ae1adc180','SybaseASE','com.sybase.jdbc4.jdbc.SybDriver','0',NULL,'Sybase ASE','jdbc:sybase:Tds:host:port/database',NULL);
insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69c00b-7f4b-11f1-a120-7c8ae1adc180','Teradata','com.teradata.jdbc.TeraDriver','0',NULL,'Teradata','jdbc:teradata://host/database=database',NULL);
insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69c04d-7f4b-11f1-a120-7c8ae1adc180','H2','org.h2.Driver','0',NULL,'H2','jdbc:h2:file:/path/database',NULL);
insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69c08f-7f4b-11f1-a120-7c8ae1adc180','HSQLDB','org.hsqldb.jdbc.JDBCDriver','0',NULL,'HSQLDB','jdbc:hsqldb:file:/path/database',NULL);
insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69c104-7f4b-11f1-a120-7c8ae1adc180','Derby','org.apache.derby.jdbc.EmbeddedDriver','0',NULL,'Apache Derby','jdbc:derby:/path/database;create=true',NULL);
insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69c147-7f4b-11f1-a120-7c8ae1adc180','CockroachDB','org.postgresql.Driver','0',NULL,'CockroachDB','jdbc:postgresql://host:port/database',NULL);
insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69c189-7f4b-11f1-a120-7c8ae1adc180','YugabyteDB','org.postgresql.Driver','0',NULL,'YugabyteDB','jdbc:postgresql://host:port/database',NULL);
insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69c1cb-7f4b-11f1-a120-7c8ae1adc180','Greenplum','org.postgresql.Driver','0',NULL,'Greenplum','jdbc:postgresql://host:port/database',NULL);
insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69c20c-7f4b-11f1-a120-7c8ae1adc180','TimescaleDB','org.postgresql.Driver','0',NULL,'TimescaleDB','jdbc:postgresql://host:port/database',NULL);
insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69c24d-7f4b-11f1-a120-7c8ae1adc180','AlloyDB','org.postgresql.Driver','0',NULL,'AlloyDB','jdbc:postgresql://host:port/database',NULL);
insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69c290-7f4b-11f1-a120-7c8ae1adc180','AuroraPostgreSQL','org.postgresql.Driver','0',NULL,'Aurora PostgreSQL','jdbc:postgresql://host:port/database',NULL);
insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69c2d0-7f4b-11f1-a120-7c8ae1adc180','AuroraMySQL','com.mysql.cj.jdbc.Driver','0',NULL,'Aurora MySQL','jdbc:mysql://host:port/database',NULL);
insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69c30a-7f4b-11f1-a120-7c8ae1adc180','TiDB','com.mysql.cj.jdbc.Driver','0',NULL,'TiDB','jdbc:mysql://host:port/database',NULL);
insert into `jq_datasource_lookup` (`datasource_lookup_id`, `database_product_name`, `driver_class_name`, `is_deleted`, `datasource_supported_version`, `db_product_display_name`, `connection_url_pattern`, `connection_properties`) values('2f69c349-7f4b-11f1-a120-7c8ae1adc180','SingleStore','com.mysql.cj.jdbc.Driver','0',NULL,'SingleStore','jdbc:mysql://host:port/database',NULL);


UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69be8c-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69becb-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69bf0b-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69bf44-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69bf87-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69bfc9-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69c00b-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69c04d-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69c08f-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69c104-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69c147-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69c189-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69c1cb-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69c20c-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69c24d-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69c290-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69c2d0-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69c30a-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='2f69c349-7f4b-11f1-a120-7c8ae1adc180';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='87eeb3a4-9611-11eb-a295-f48e38ab8cd7';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='d03753ea-9611-11eb-a295-f48e38ab8cd7';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='d880d3ac-9611-11eb-a295-f48e38ab8cd7';

UPDATE jq_datasource_lookup SET connection_properties='[
  {"key":"connectionTimeout","defaultValue":"20000","description":"Maximum wait time for connection"},
  {"key":"maximumPoolSize","defaultValue":"10","description":"Maximum pool size"},
  {"key":"minimumIdle","defaultValue":"1","description":"Minimum idle connections"},
  {"key":"idleTimeout","defaultValue":"30000","description":"Idle timeout"},
  {"key":"maxLifetime","defaultValue":"1800000","description":"Connection lifetime"},
  {"key":"validationTimeout","defaultValue":"5000"},
  {"key":"connectionTestQuery","defaultValue":"SELECT 1"},
  {"key":"poolName","defaultValue":"AdditionalDatasourcePool"},
  {"key":"leakDetectionThreshold","defaultValue":"0"},
  {"key":"autoCommit","defaultValue":"true"}
]' WHERE datasource_lookup_id='db39e0f9-9611-11eb-a295-f48e38ab8cd7';