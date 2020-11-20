REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id) 
VALUES ("dashboardMasterListingGrid", 'Dashboard Master Listing', 'Dashboard Master', 'dashboardMasterListing'
,'dashboardName,dashboardType,contextDescription,createdDate,createdBy,lastUpdatedDate', 2);

REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id) 
VALUES ("dashletMasterListingGrid", 'Dashlet Master Listing', 'Dashlet Master', 'dashletMasterListing','dashletName,dashletTitle,dashletTypeId,createdDate,createdBy,updatedDate,updatedBy,status', 2);

REPLACE INTO grid_details (grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id)  VALUES 
("dynamicFormListingGrid", 'Dynamic Form Master', 'Dynamic Form Master Listing', 'dynamicFormListing','formName,formDescription,formTypeId,createdDate,createdBy', 2);

REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id) 
VALUES ("templateListingGrid", 'Template Listing', 'Template Listing', 'templateListing', 'templateName,templateTypeId,createdBy,updatedBy', 2);

REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id) 
VALUES ("resourceBundleListingGrid", 'DB Resource Bundle Listing', 'DB Resource Bundle Listing', 'dbResourceListing',
'resourceKey,languageName,resourceBundleText', 2);

REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id)
 VALUES ("gridDetailsListing", 'Grid Details Listing', 'Grid Details Listing', 'gridDetails', 'gridId,gridName,gridDesc,gridTableName,gridColumnName,gridTypeId', 2);

REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id) VALUES ("notificationDetailsListing", 'Notification Details Listing', 'Notification Details Listing', 'notificationlisting', 'targetPlatform,messageType,validFrom,messageText,validTill,messageFormat,selectionCriteria,updatedBy,updatedDate', 2);

REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id)
VALUES ("moduleListingGrid", 'Menu Module Listing', 'Menu Module Listing', 'moduleListing', 'moduleId,moduleName,moduleURL,parentModuleName,sequence,isInsideMenu', 2);

REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type, grid_type_id) 
VALUES ("dynarestGrid", 'Dynamic Rest API listing', 'Dynamic Rest API listing', 'jws_dynamic_rest_details'
,'jws_dynamic_rest_id,jws_dynamic_rest_url,jws_method_name,jws_method_description,jws_request_type_id,jws_platform_id,jws_dynamic_rest_type_id', 1, 2);

REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type, grid_type_id) 
VALUES ("propertyMasterListing", 'Property master listing', 'Property master listing', 'jws_property_master','*', 1, 2);

REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id) 
VALUES ("fileUploadConfigGrid", 'File Upload Config', 'File Upload Config', 'fileUploadConfigListing'
,'fileUploadConfigId,fileTypeSupported,maxFileSize,noOfFiles,updatedBy,updatedDate', 2);


DROP PROCEDURE IF EXISTS autocompleteListing;
CREATE PROCEDURE autocompleteListing(autocompleteId varchar(100), autocompleteDescription varchar(500), autocompleteTypeId INT(11) ,forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
  SET @resultQuery = ' SELECT au.ac_id AS autocompleteId, au.ac_description AS autocompleteDescription, au.ac_select_query AS acQuery ';
  SET @resultQuery = CONCAT(@resultQuery, ', au.ac_type_id AS autocompleteTypeId, COUNT(jmv.version_id) AS revisionCount ');
  SET @resultQuery = CONCAT(@resultQuery, ', MAX(jmv.version_id) AS max_version_id ');
  SET @fromString  = ' FROM autocomplete_details au ';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jws_module_version AS jmv ON jmv.entity_id = au.ac_id AND jmv.entity_name = 'autocomplete_details' ");
  SET @whereString = ' ';
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  SET @groupByString = ' GROUP BY au.ac_id ';
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY ac_id DESC');
  END IF;
  
	IF forCount=1 THEN
  	SET @queryString=CONCAT('select count(*) from ( ',@resultQuery, @fromString, @whereString, @groupByString, @orderBy,' ) AS cnt');
  ELSE
  	SET @queryString=CONCAT(@resultQuery, @fromString, @whereString, @groupByString, @orderBy, @limitString);
  END IF;

 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
END;


DROP PROCEDURE IF EXISTS dbResourceListing;
CREATE PROCEDURE dbResourceListing(resourceKey VARCHAR(100), languageName VARCHAR(100), resourceBundleText TEXT, forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
  SET @selectQuery = ' SELECT rb.resource_key AS resourceKey, lang.language_name AS languageName, rb.`text` AS resourceBundleText, COUNT(jmv.version_id) AS revisionCount ';
  SET @selectQuery = CONCAT(@selectQuery, ', MAX(jmv.version_id) AS max_version_id ');
  SET @fromString  = ' FROM resource_bundle AS rb INNER JOIN language AS lang ON lang.language_id = rb.language_id ';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jws_module_version AS jmv ON jmv.entity_id = rb.resource_key AND jmv.entity_name = 'resource_bundle' ");
  
  IF languageName IS NULL THEN  
    SET @whereString = ' WHERE lang.language_id = 1 ';
  ELSEIF NOT languageName IS NULL THEN  
    SET @languageName= REPLACE(languageName,"'","''");
    SET @whereString = CONCAT(' WHERE lang.language_name LIKE ''%',@languageName,'%''');
  END IF;
  
  IF NOT resourceKey IS NULL THEN
    SET @resourceKey= REPLACE(resourceKey,"'","''");
    SET @whereString = CONCAT(@whereString,' AND rb.resource_key LIKE ''%',@resourceKey,'%''');
  END IF;
  
  IF NOT resourceBundleText IS NULL THEN
    SET @resourceBundleText= REPLACE(resourceBundleText,"'","''");
    SET @whereString = CONCAT(@whereString,' AND rb.text LIKE ''%',@resourceBundleText,'%''');
  END IF;
  
  SET @groupByString = ' GROUP BY rb.resource_key ';
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY resourceKey ASC');
  END IF;
  
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
	IF forCount=1 THEN
  	SET @queryString=CONCAT('SELECT COUNT(*) FROM ( ',@selectQuery, @fromString, @whereString, @groupByString, @orderBy,' ) AS cnt');
  ELSE
  	SET @queryString=CONCAT(@selectQuery, @fromString, @whereString, @groupByString, @orderBy, @limitString);
  END IF;

 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
END;


DROP PROCEDURE IF EXISTS dashboardMasterListing;
CREATE PROCEDURE dashboardMasterListing(dashboardName varchar(50), dashboardType varchar(100), contextDescription varchar(1000)
,createdDate varchar(100), createdBy varchar(100),lastUpdatedDate varchar(100), forCount INT, limitFrom INT, limitTo INT
,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
DECLARE db_format VARCHAR(20);

DECLARE curP CURSOR FOR 
SELECT JSON_VALUE(jpm.property_value, '$.db') AS db_format FROM jws_property_master AS jpm WHERE jpm.property_name = 'jws-date-format';

OPEN curP;
FETCH curP INTO db_format;

  SET @resultQuery = CONCAT(" SELECT db.dashboard_id AS dashboardId, db.dashboard_name AS dashboardName, db.dashboard_type AS dashboardType "
  ," , db.created_by AS createdBy "
  ," ,cm.context_description AS contextDescription, COUNT(jmv.version_id) AS revisionCount ") ;
  SET @resultQuery = CONCAT(@resultQuery, ', MAX(jmv.version_id) AS max_version_id ');
  SET @resultQuery = CONCAT(@resultQuery, ', date_format(db.created_date,''', db_format,''') as createdDate ');
  SET @resultQuery = CONCAT(@resultQuery, ', date_format(db.last_updated_date,''', db_format,''') as lastUpdatedDate ');
  SET @fromString  = ' FROM dashboard AS db INNER JOIN context_master cm ON db.context_id = cm.context_id ';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jws_module_version jmv ON jmv.entity_id = db.dashboard_id AND jmv.entity_name = 'dashboard' ");
  SET @whereString = ' WHERE db.is_deleted = 0';
  
  IF NOT dashboardName IS NULL THEN
    SET @dashboardName= REPLACE(dashboardName,"'","''");
    SET @whereString = CONCAT(@whereString,' AND db.dashboard_name like ''%',@dashboardName,'%''');
  END IF;
  
  IF NOT dashboardType IS NULL THEN
    SET @dashboardType= REPLACE(dashboardType,"'","''");
    SET @whereString = CONCAT(@whereString,' AND db.dashboard_type like ''%',@dashboardType,'%''');
  END IF;
  
  IF NOT createdDate IS NULL THEN
    SET @createdDate= REPLACE(createdDate,"'","''");
    SET @whereString = CONCAT(@whereString,' AND db.created_date like ''%',@createdDate,'%''');
  END IF;
  
  IF NOT lastUpdatedDate IS NULL THEN
    SET @lastUpdatedDate= REPLACE(lastUpdatedDate,"'","''");
    SET @whereString = CONCAT(@whereString,' AND db.last_updated_date like ''%',@lastUpdatedDate,'%''');
  END IF;

  IF NOT contextDescription IS NULL THEN
    SET @contextDescription= REPLACE(contextDescription,"'","''");
    SET @whereString = CONCAT(@whereString,' AND cm.context_description like ''%',@contextDescription,'%''');
  END IF;
  
  
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  SET @groupByString = ' GROUP BY db.dashboard_id ';
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY lastUpdatedDate DESC');
  END IF;
  
	IF forCount=1 THEN
  	SET @queryString=CONCAT('SELECT COUNT(*) FROM ( ',@resultQuery, @fromString, @whereString, @groupByString, @orderBy,' ) AS cnt');
  ELSE
  	SET @queryString=CONCAT(@resultQuery, @fromString, @whereString, @groupByString, @orderBy, @limitString);
  END IF;

 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
CLOSE curP;
 
END;

DROP PROCEDURE IF EXISTS dashletMasterListing;
CREATE PROCEDURE dashletMasterListing(dashletName varchar(50), dashletTitle varchar(100),dashletTypeId INT(11),createdDate varchar(100),createdBy varchar(100),updatedDate varchar(100),updatedBy varchar(100),status INT, forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
DECLARE db_format VARCHAR(20);

DECLARE curP CURSOR FOR 
SELECT JSON_VALUE(jpm.property_value, '$.db') AS db_format FROM jws_property_master AS jpm WHERE jpm.property_name = 'jws-date-format';

OPEN curP;
FETCH curP INTO db_format;

  SET @resultQuery = CONCAT("SELECT dl.dashlet_id AS dashletId, dl.dashlet_title AS dashletTitle,dl.dashlet_name AS dashletName, "
  ," dl.updated_by AS updatedBy, dl.created_by AS createdBy,dl.is_active AS status, COUNT(jmv.version_id) AS revisionCount ") ;
  SET @resultQuery = CONCAT(@resultQuery, ', dl.dashlet_type_id AS dashletTypeId ');
  SET @resultQuery = CONCAT(@resultQuery, ', MAX(jmv.version_id) AS max_version_id ');
  SET @resultQuery = CONCAT(@resultQuery, ', date_format(dl.created_date,''', db_format,''') as createdDate ');
  SET @resultQuery = CONCAT(@resultQuery, ', date_format(dl.updated_date,''', db_format,''') as updatedDate ');
  SET @fromString  = ' FROM dashlet AS dl';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jws_module_version jmv ON jmv.entity_id = dl.dashlet_id AND jmv.entity_name = 'dashlet' ");
  SET @whereString = '';
  SET @dateFormat = CONCAT(''', db_format, ''');
  SET @limitString = CONCAT(' limit ','',CONCAT(limitFrom,',',limitTo));
  
  
  IF NOT dashletName IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND dl.dashlet_name LIKE ''%',dashletName,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE dl.dashlet_name LIKE ''%',dashletName,'%''');
    END IF;  
  END IF;
  
  IF NOT dashletTitle IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND dl.dashlet_title LIKE ''%',dashletTitle,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE dl.dashlet_title LIKE ''%',dashletTitle,'%''');
    END IF;  
  END IF;
  
  IF NOT createdDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND date_format(dl.created_date,''',@dateFormat,''') LIKE ''%',createdDate,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE date_format(dl.created_date,''',@dateFormat,''') LIKE ''%',createdDate,'%''');
    END IF;  
  END IF;
  
  IF NOT createdBy IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND dl.created_by LIKE ''%',createdBy,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE dl.created_by LIKE ''%',createdBy,'%''');
    END IF;  
  END IF;
  
    IF NOT updatedDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND date_format(dl.updated_date,''',@dateFormat,''') LIKE ''%',updatedDate,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE date_format(dl.updated_date,''',@dateFormat,''') LIKE ''%',updatedDate,'%''');
    END IF;  
  END IF;
  
  IF NOT updatedBy IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND dl.updated_by LIKE ''%',updatedBy,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE dl.updated_by LIKE ''%',updatedBy,'%''');
    END IF;  
  END IF;
  
  
  IF NOT status IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND dl.is_active LIKE ''%',status,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE dl.is_active LIKE ''%',status,'%''');
    END IF;  
  END IF; 
  
  SET @groupByString = ' GROUP BY dl.dashlet_id ';
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY dl.updated_date DESC');
  END IF;
  
	IF forCount=1 THEN
  	SET @queryString=CONCAT('SELECT COUNT(*) FROM ( ',@resultQuery, @fromString, @whereString, @groupByString, @orderBy,' ) AS cnt');
  ELSE
  	SET @queryString=CONCAT(@resultQuery, @fromString, @whereString, @groupByString, @orderBy, @limitString);
  END IF;

 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
CLOSE curP;
END;

DROP PROCEDURE IF EXISTS dynamicFormListing;
CREATE PROCEDURE dynamicFormListing(formName varchar(50), formDescription varchar(100),formTypeId INT(11),createdDate varchar(100),createdBy varchar(100), forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
DECLARE db_format VARCHAR(20);

DECLARE curP CURSOR FOR 
SELECT JSON_VALUE(jpm.property_value, '$.db') AS db_format FROM jws_property_master AS jpm WHERE jpm.property_name = 'jws-date-format';

OPEN curP;
FETCH curP INTO db_format;

  SET @resultQuery = CONCAT("SELECT df.form_id AS formId, df.form_description AS formDescription,df.form_name AS formName, "
  ," df.created_by AS createdBy ") ;
  SET @resultQuery = CONCAT(@resultQuery, ', df.form_type_id AS formTypeId, COUNT(jmv.version_id) AS revisionCount  '); 
  SET @resultQuery = CONCAT(@resultQuery, ', MAX(jmv.version_id) AS max_version_id ');
  SET @resultQuery = CONCAT(@resultQuery, ', date_format(df.created_date,''', db_format,''') as createdDate ');
  SET @fromString  = ' from dynamic_form AS df ';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jws_module_version  jmv ON jmv.entity_id = df.form_id AND jmv.entity_name = 'dynamic_form' ");
  SET @whereString = '';
  SET @dateFormat = CONCAT(''', db_format, ''');
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  
  IF NOT formName IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND df.form_name LIKE ''%',formName,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE df.form_name LIKE ''%',formName,'%''');
    END IF;  
  END IF;
  
  IF NOT formDescription IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND df.form_description LIKE ''%',formDescription,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE df.form_description LIKE ''%',formDescription,'%''');
    END IF;  
  END IF;
  
  IF NOT createdDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND date_format(df.created_date,''',@dateFormat,''') LIKE ''%',createdDate,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE date_format(df.created_date,''',@dateFormat,''') LIKE ''%',createdDate,'%''');
    END IF;  
  END IF;
  
  IF NOT createdBy IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND df.created_by LIKE ''%',createdBy,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE df.created_by LIKE ''%',createdBy,'%''');
    END IF;  
  END IF;
  
  SET @groupByString = ' GROUP BY df.form_id ';
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY df.created_date DESC');
  END IF;
  
	IF forCount=1 THEN
  	SET @queryString=CONCAT('SELECT COUNT(*) FROM ( ',@resultQuery, @fromString, @whereString, @groupByString, @orderBy,' ) AS cnt');
  ELSE
  	SET @queryString=CONCAT(@resultQuery, @fromString, @whereString, @groupByString, @orderBy, @limitString);
  END IF;

 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
CLOSE curP;
END;

DROP PROCEDURE IF EXISTS dynarestListing;
CREATE PROCEDURE dynarestListing(dynarestUrl varchar(256), rbacId INT(11), methodName VARCHAR(512)
, methodDescription TEXT , requestTypeId INT(11), producerTypeId INT(11), serviceLogic MEDIUMTEXT,
 platformId INT(11), allowFiles TINYINT(11), dynarestTypeId INT(11),forCount INT, limitFrom INT,
 limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
  SET @resultQuery = ' SELECT jdrd.jws_dynamic_rest_id AS dynarestId, jdrd.jws_dynamic_rest_url AS dynarestUrl ';
  SET @resultQuery = CONCAT(@resultQuery, ', jdrd.jws_rbac_id AS rbacId, jdrd.jws_method_name AS methodName ');
  SET @resultQuery = CONCAT(@resultQuery, ', jdrd.jws_method_description AS methodDescription, jdrd.jws_request_type_id AS requestTypeId '); 
  SET @resultQuery = CONCAT(@resultQuery, ', jdrd.jws_response_producer_type_id AS producerTypeId, jdrd.jws_service_logic AS serviceLogic ');
  SET @resultQuery = CONCAT(@resultQuery, ', jdrd.jws_platform_id AS platformId, jdrd.jws_allow_files AS allowFiles, jdrd.jws_dynamic_rest_type_id AS dynarestTypeId ');
  SET @resultQuery = CONCAT(@resultQuery, ', COUNT(jmv.version_id) AS revisionCount ');
  SET @resultQuery = CONCAT(@resultQuery, ', MAX(jmv.version_id) AS max_version_id ');
  SET @fromString  = ' FROM jws_dynamic_rest_details AS jdrd ';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jws_module_version AS jmv ON jmv.entity_id = jdrd.jws_dynamic_rest_id ");
  SET @whereString = ' ';
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT requestTypeId IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND jdrd.jws_request_type_id = ',requestTypeId);
    ELSE
      SET @whereString = CONCAT('WHERE jdrd.jws_request_type_id = ',requestTypeId);
    END IF;  
  END IF;
  
  
  IF NOT platformId IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND jdrd.jws_platform_id = ',platformId);
    ELSE
      SET @whereString = CONCAT('WHERE jdrd.jws_platform_id = ',platformId);
    END IF;  
  END IF;

  SET @groupByString = ' GROUP BY dynarestId ';
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY methodName ASC');
  END IF;
  
	IF forCount=1 THEN
  	SET @queryString=CONCAT('SELECT COUNT(*) FROM ( ',@resultQuery, @fromString, @whereString, @groupByString, @orderBy,' ) AS cnt');
  ELSE
  	SET @queryString=CONCAT(@resultQuery, @fromString, @whereString, @groupByString, @orderBy, @limitString);
  END IF;

 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
END;

DROP PROCEDURE IF EXISTS gridDetails;
CREATE PROCEDURE gridDetails(gridId varchar(100), gridName varchar(500), gridDesc varchar(500), gridTableName varchar(100)
, gridColumnName varchar(500),gridTypeId INT(11), forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
  SET @resultQuery = ' SELECT gd.grid_id AS gridId, gd.grid_name AS gridName, gd.grid_description AS gridDesc, gd.grid_table_name AS gridTableName, gd.grid_column_names AS gridColumnName ';
  SET @resultQuery = CONCAT(@resultQuery, ', gd.grid_type_id AS gridTypeId , COUNT(jmv.version_id) AS revisionCount ');
  SET @resultQuery = CONCAT(@resultQuery, ', MAX(jmv.version_id) AS max_version_id ');
  SET @fromString  = ' FROM grid_details AS gd ';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jws_module_version AS jmv ON jmv.entity_id = gd.grid_id AND jmv.entity_name = 'grid_details' ");

  SET @whereString = ' ';
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT gridId IS NULL THEN
    SET @gridId= REPLACE(gridId,"'","''");
    SET @whereString = CONCAT(@whereString,' WHERE grid_id LIKE ''%',@gridId,'%''');
  END IF;
  
  IF NOT gridName IS NULL THEN
    SET @gridName= REPLACE(gridName,"'","''");
    SET @whereString = CONCAT(@whereString,' WHERE grid_name LIKE ''%',@gridName,'%''');
  END IF;
  
  IF NOT gridDesc IS NULL THEN
    SET @gridDesc= REPLACE(gridDesc,"'","''");
    SET @whereString = CONCAT(@whereString,' WHERE grid_description LIKE ''%',@gridDesc,'%''');
  END IF;
  
  IF NOT gridTableName IS NULL THEN
    SET @gridTableName= REPLACE(gridTableName,"'","''");
    SET @whereString = CONCAT(@whereString,' WHERE grid_table_name LIKE ''%',@gridTableName,'%''');
  END IF;

  IF NOT gridColumnName IS NULL THEN
    SET @gridColumnName= REPLACE(gridColumnName,"'","''");
    SET @whereString = CONCAT(@whereString,' WHERE grid_column_names LIKE ''%',@gridColumnName,'%''');
  END IF;
  
  SET @groupByString = ' GROUP BY gd.grid_id ';
    
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY grid_id ASC');
  END IF;
  
  IF forCount=1 THEN
  	SET @queryString=CONCAT('SELECT COUNT(*) FROM ( ',@resultQuery, @fromString, @whereString, @groupByString, @orderBy,' ) AS cnt');
  ELSE
  	SET @queryString=CONCAT(@resultQuery, @fromString, @whereString, @groupByString, @orderBy, @limitString);
  END IF;

 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
END;

DROP PROCEDURE IF EXISTS notificationlisting;
CREATE PROCEDURE notificationlisting(targetPlatform varchar(100), messageType varchar(500), validFrom varchar(500), messageText varchar(500), validTill varchar(100), messageFormat varchar(100), selectionCriteria varchar(500), updatedBy varchar(100), updatedDate varchar(100), forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
DECLARE db_format VARCHAR(20);

DECLARE curP CURSOR FOR 
SELECT JSON_VALUE(jpm.property_value, '$.db') AS db_format FROM jws_property_master AS jpm WHERE jpm.property_name = 'jws-date-format';

OPEN curP;
FETCH curP INTO db_format;

  SET @resultQuery = ' SELECT gun.notification_id AS notificationId, gun.target_platform AS targetPlatform, message_text AS messageText,  ';
  SET @resultQuery = CONCAT(@resultQuery, ' gun.message_type AS messageType, gun.message_format AS messageFormat, gun.selection_criteria AS selectionCriteria, gun.updated_by AS updatedBy, COUNT(jmv.version_id) AS revisionCount ');
  SET @resultQuery = CONCAT(@resultQuery, ', MAX(jmv.version_id) AS max_version_id ');
  SET @resultQuery = CONCAT(@resultQuery, ', date_format(gun.message_valid_from,''', db_format,''') as validFrom ');
  SET @resultQuery = CONCAT(@resultQuery, ', date_format(gun.message_valid_till,''', db_format,''') as validTill ');
  SET @resultQuery = CONCAT(@resultQuery, ', date_format(gun.updated_date,''', db_format,''') as updatedDate ');
  SET @fromString  = ' FROM generic_user_notification AS gun';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jws_module_version AS jmv ON jmv.entity_id = gun.notification_id AND jmv.entity_name = 'generic_user_notification' ");
  SET @whereString = '';
  SET @dateFormat = CONCAT(''', db_format,''');
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT targetPlatform IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND gun.target_platform LIKE ''%',targetPlatform,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE gun.target_platform LIKE ''%',targetPlatform,'%''');
    END IF;  
  END IF;
  
  IF NOT messageType IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND gun.message_type LIKE ''%',messageType,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE gun.message_type LIKE ''%',messageType,'%''');
    END IF;  
  END IF;
  
  IF NOT validFrom IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND date_format(gun.message_valid_from,''',@dateFormat,''') LIKE ''%',validFrom,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE date_format(gun.message_valid_from,''',@dateFormat,''') LIKE ''%',validFrom,'%''');
    END IF;  
  END IF;
  
  IF NOT messageText IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND gun.message_text LIKE ''%',messageText,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE gun.message_text LIKE ''%',messageText,'%''');
    END IF;  
  END IF;
  
  IF NOT validTill IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND date_format(gun.message_valid_till,''',@dateFormat,''') LIKE ''%',validTill,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE date_format(gun.message_valid_till,''',@dateFormat,''') LIKE ''%',validTill,'%''');
    END IF;  
  END IF;
  
  IF NOT messageFormat IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND gun.message_format LIKE ''%',messageFormat,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE gun.message_format LIKE ''%',messageFormat,'%''');
    END IF;  
  END IF;
  
  IF NOT selectionCriteria IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND gun.selection_criteria LIKE ''%',selectionCriteria,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE gun.selection_criteria LIKE ''%',selectionCriteria,'%''');
    END IF;  
  END IF;
  
  IF NOT updatedBy IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND gun.updated_by LIKE ''%',updatedBy,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE gun.updated_by LIKE ''%',updatedBy,'%''');
    END IF;  
  END IF;
  
  IF NOT updatedDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND date_format(gun.updated_date,''',@dateFormat,''') LIKE ''%',updatedDate,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE date_format(gun.updated_date,''',@dateFormat,''') LIKE ''%',updatedDate,'%''');
    END IF;  
  END IF;
  
  SET @groupByString = ' GROUP BY gun.notification_id ';
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY gun.updated_date DESC');
  END IF;
  
	IF forCount=1 THEN
  	SET @queryString=CONCAT('SELECT COUNT(*) FROM ( ',@resultQuery, @fromString, @whereString, @groupByString, @orderBy,' ) AS cnt');
  ELSE
  	SET @queryString=CONCAT(@resultQuery, @fromString, @whereString, @groupByString, @orderBy, @limitString);
  END IF;
  
  

 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
CLOSE curP;
 
END;

DROP PROCEDURE IF EXISTS propertyMasterListing;
CREATE PROCEDURE propertyMasterListing(ownerType VARCHAR(100), ownerId VARCHAR(150)
, propertyName VARCHAR(150), propertyValue VARCHAR(250), modifiedBy VARCHAR(50), appVersion DECIMAL(7,4), comments TEXT
,forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
  SET @resultQuery = ' SELECT jpm.owner_type AS ownerType, jpm.owner_id AS ownerId, jpm.property_name AS propertyName '; 
  SET @resultQuery = CONCAT(@resultQuery, ', jpm.property_value AS propertyValue, jpm.last_modified_date AS lastModifiedDate ');
  SET @resultQuery = CONCAT(@resultQuery, ', jpm.modified_by AS modifiedBy, jpm.app_version AS appVersion, jpm.comments AS comments, jpm.property_master_id AS propertyMasterId, COUNT(jmv.version_id) AS revisionCount ');
  SET @resultQuery = CONCAT(@resultQuery, ', MAX(jmv.version_id) AS max_version_id ');
  
  SET @fromString  = ' FROM jws_property_master AS jpm ';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jws_module_version AS jmv ON jmv.entity_id = jpm.property_master_id AND jmv.entity_name = 'jws_property_master' ");
  SET @whereString = ' WHERE jpm.is_deleted = 0 ';
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  SET @groupByString = ' GROUP BY propertyMasterId ';
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY lastModifiedDate DESC');
  END IF;
  
	IF forCount=1 THEN
  	SET @queryString=CONCAT('SELECT COUNT(*) FROM ( ',@resultQuery, @fromString, @whereString, @groupByString, @orderBy,' ) AS cnt');
  ELSE
  	SET @queryString=CONCAT(@resultQuery, @fromString, @whereString, @groupByString, @orderBy, @limitString);
  END IF;

 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
END;

DROP PROCEDURE IF EXISTS templateListing;
CREATE PROCEDURE templateListing(templateName varchar(100), templateTypeId INT(11), createdBy VARCHAR(100),updatedBy varchar(100)
, forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN

DECLARE db_format VARCHAR(20);

DECLARE curP CURSOR FOR 
SELECT JSON_VALUE(jpm.property_value, '$.db') AS db_format FROM jws_property_master AS jpm WHERE jpm.property_name = 'jws-date-format';

OPEN curP;
FETCH curP INTO db_format;
  SET @resultQuery = ' SELECT tm.template_id as templateId, tm.template_name as templateName, tm.template as template, tm.updated_by as updatedBy, tm.created_by as createdBy ';
  SET @resultQuery = CONCAT(@resultQuery, ', tm.template_type_id AS templateTypeId , COUNT(jmv.version_id) AS revisionCount ');
  SET @resultQuery = CONCAT(@resultQuery, ', MAX(jmv.version_id) AS max_version_id ');
  SET @resultQuery = CONCAT(@resultQuery, ', date_format(tm.updated_date,''', db_format,''') as updatedDate ');
  SET @fromString  = ' FROM template_master AS tm ';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jws_module_version AS jmv ON jmv.entity_id = tm.template_id AND jmv.entity_name = 'template_master' ");
  SET @whereString = '';
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT templateName IS NULL OR NOT createdBy IS NULL OR NOT updatedBy IS NULL THEN
    SET @whereString = CONCAT(@whereString, " WHERE ");
  END IF;
  
  IF NOT templateName IS NULL THEN
    SET @templateName= REPLACE(templateName,"'","''");
    SET @whereString = CONCAT(@whereString,' tm.template_name LIKE ''%',@templateName,'%''');
  END IF;
  
  IF NOT templateTypeId IS NULL THEN
    SET @templateName= REPLACE(templateTypeId,"'","''");
    SET @whereString = CONCAT(@whereString,' tm.template_type_id = ',@templateTypeId);
  END IF;
  
  IF NOT createdBy IS NULL THEN
    SET @createdBy= REPLACE(createdBy,"'","''");
    SET @whereString = CONCAT(@whereString,' tm.created_by LIKE ''%',@createdBy,'%''');
  END IF;
  
  IF NOT updatedBy IS NULL THEN
    SET @updatedBy= REPLACE(updatedBy,"'","''");
    SET @whereString = CONCAT(@whereString,' tm.updated_by LIKE ''%',@updatedBy,'%''');
  END IF;

  
  SET @groupByString = ' GROUP BY tm.template_id ';
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY tm.updated_date DESC');
  END IF;
  
	IF forCount=1 THEN
  	SET @queryString=CONCAT('SELECT COUNT(*) FROM ( ',@resultQuery, @fromString, @whereString, @groupByString, @orderBy,' ) AS cnt');
  ELSE
  	SET @queryString=CONCAT(@resultQuery, @fromString, @whereString, @groupByString, @orderBy, @limitString);
  END IF;


 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
CLOSE curP;
END;


DROP PROCEDURE IF EXISTS fileUploadConfigListing;
CREATE PROCEDURE `fileUploadConfigListing`(fileUploadConfigId varchar(50), fileTypeSupported varchar(100), maxFileSize varchar(1000)
,noOfFiles INT(11), updatedBy varchar(100),updatedDate varchar(100), forCount INT, limitFrom INT, limitTo INT
,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
  SET @resultQuery = CONCAT(" SELECT fuc.file_upload_config_id AS fileUploadConfigId, fuc.file_type_supported AS fileTypeSupported "
  ," , fuc.max_file_size AS maxFileSize, fuc.no_of_files AS noOfFiles, fuc.updated_by AS updatedBy " 
  ," ,fuc.updated_date AS updatedDate, fuc.is_deleted AS isDeleted ") ;
  SET @fromString  = ' FROM file_upload_config AS fuc ';
  SET @whereString = ' WHERE fuc.is_deleted = 0';
  
  IF NOT fileUploadConfigId IS NULL THEN
    SET @fileUploadConfigId= REPLACE(fileUploadConfigId,"'","''");
    SET @whereString = CONCAT(@whereString,' AND fuc.file_upload_config_id like ''%',@fileUploadConfigId,'%''');
  END IF;
  
  IF NOT fileTypeSupported IS NULL THEN
    SET @fileTypeSupported= REPLACE(fileTypeSupported,"'","''");
    SET @whereString = CONCAT(@whereString,' AND fuc.file_type_supported like ''%',@fileTypeSupported,'%''');
  END IF;
  
  IF NOT maxFileSize IS NULL THEN
    SET @maxFileSize= REPLACE(maxFileSize,"'","''");
    SET @whereString = CONCAT(@whereString,' AND fuc.max_file_size like ''%',@maxFileSize,'%''');
  END IF;
  
  IF NOT noOfFiles IS NULL THEN
    SET @noOfFiles= REPLACE(noOfFiles,"'","''");
    SET @whereString = CONCAT(@whereString,' AND fuc.no_of_files like ''%',@noOfFiles,'%''');
  END IF;

  IF NOT updatedBy IS NULL THEN
    SET @updatedBy= REPLACE(updatedBy,"'","''");
    SET @whereString = CONCAT(@whereString,' AND fuc.updated_by like ''%',@updatedBy,'%''');
  END IF;

  IF NOT updatedDate IS NULL THEN
    SET @updatedDate= REPLACE(updatedDate,"'","''");
    SET @whereString = CONCAT(@whereString,' AND fuc.updated_date like ''%',@updatedDate,'%''');
  END IF;
  
  
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY updatedDate DESC');
  END IF;
  
  IF forCount=1 THEN
  	SET @queryString=CONCAT('SELECT COUNT(*) FROM ( ',@resultQuery, @fromString, @whereString, @orderBy,' ) AS cnt');
  ELSE
  	SET @queryString=CONCAT(@resultQuery, @fromString, @whereString, @orderBy, @limitString);
  END IF;

 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
 
END;


DROP PROCEDURE IF EXISTS moduleListing;
CREATE PROCEDURE `moduleListing`(moduleId varchar(100), moduleName varchar(500),
moduleURL varchar(500), parentModuleName varchar(500), sequence varchar(100), isInsideMenu INT(1), forCount INT, 
limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
  SET @resultQuery = ' SELECT ml.module_id AS moduleId,COALESCE(mli18n.module_name,mli18n2.module_name) AS moduleName, ml.module_url AS moduleURL, COALESCE(mli18nParent.module_name, mli18nParent2.module_name) AS parentModuleName'
    ', ml.sequence AS sequence, ml.is_inside_menu AS isInsideMenu ';
  SET @fromString  = ' FROM module_listing AS ml ';
  SET @fromString = CONCAT(@fromString, ' LEFT OUTER JOIN module_listing AS mlParent ON ml.parent_id = mlParent.module_id ');
  SET @fromString = CONCAT(@fromString, ' LEFT OUTER JOIN module_listing_i18n AS mli18n ON ml.module_id = mli18n.module_id AND mli18n.language_id = 1 ');
  SET @fromString = CONCAT(@fromString, ' LEFT OUTER JOIN module_listing_i18n AS mli18n2 ON ml.module_id = mli18n2.module_id AND mli18n2.language_id = 1 ');
  SET @fromString = CONCAT(@fromString, ' LEFT OUTER JOIN module_listing_i18n AS mli18nParent ON mlParent.module_id = mli18nParent.module_id AND mli18nParent.language_id = 1 ');
  SET @fromString = CONCAT(@fromString, ' LEFT OUTER JOIN module_listing_i18n AS mli18nParent2 ON mlParent.module_id = mli18nParent2.module_id AND mli18nParent2.language_id = 1 ');
  SET @whereString = ' WHERE ml.module_url != "home-module" ';
  
  
  IF NOT moduleName IS NULL THEN
    SET @moduleName= REPLACE(moduleName,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString, 'AND COALESCE(mli18n.module_name,mli18n2.module_name) LIKE ''%',@moduleName,'%'''); 
    ELSE
      SET @whereString = CONCAT(@whereString, 'WHERE COALESCE(mli18n.module_name,mli18n2.module_name) LIKE ''%',@moduleName,'%'''); 
    END IF;  
  END IF;
  
  IF NOT moduleURL IS NULL THEN
    SET @moduleURL= REPLACE(moduleURL,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString, 'AND ml.module_url LIKE ''%',@moduleURL,'%'''); 
    ELSE
      SET @whereString = CONCAT(@whereString, 'WHERE ml.module_url LIKE ''%',@moduleURL,'%'''); 
    END IF; 
  END IF;
  
  IF NOT parentModuleName IS NULL THEN
    SET @parentModuleName= REPLACE(parentModuleName,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString, 'AND COALESCE(mli18nParent.module_name, mli18nParent2.module_name) LIKE ''%',@parentModuleName,'%''');  
    ELSE
      SET @whereString = CONCAT(@whereString, 'WHERE COALESCE(mli18nParent.module_name, mli18nParent2.module_name) LIKE ''%',@parentModuleName,'%'''); 
    END IF; 
 END IF;
  
  IF NOT sequence IS NULL THEN
    SET @sequence= REPLACE(sequence,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString, 'AND  ml.sequence = ''',@sequence,''''); 
    ELSE
      SET @whereString = CONCAT(@whereString, 'WHERE ml.sequence = ''',@sequence,''''); 
    END IF; 
  END IF;
  

  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY ml.sequence ASC');
  END IF;
    
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
	IF forCount=1 THEN
  	SET @queryString=CONCAT('SELECT COUNT(*) FROM ( ',@resultQuery, @fromString, @whereString, @orderBy,' ) AS cnt');
  ELSE
  	SET @queryString=CONCAT(@resultQuery, @fromString, @whereString, @orderBy, @limitString);
  END IF;
  
  
 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
END;

