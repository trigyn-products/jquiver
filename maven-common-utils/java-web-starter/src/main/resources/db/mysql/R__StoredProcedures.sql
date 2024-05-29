
REPLACE INTO jq_grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type, grid_type_id, created_by, created_date, last_updated_ts) 
VALUES ("userListingGrid", 'userListingGrid', 'userListingGrid', 'userListingGrid'
,'email,firstName,lastName,status,isAfterDate', 2, 2, 'admin@jquiver.io', NOW(), NOW()); 


REPLACE INTO jq_grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type, grid_type_id, created_by, created_date, last_updated_ts) 
VALUES ("roleListingGrid", 'roleGrid', 'roleGrid', 'roleGrid'
,'roleName,isActive,isAfterDate', 2, 2, 'admin@jquiver.io', NOW(), NOW()); 

REPLACE INTO jq_grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type, grid_type_id, created_by, created_date, last_updated_ts) 
VALUES ("helpManualListingGrid", 'helpManualListingGrid', 'helpManualListingGrid', 'helpManualListingGrid'
,'manualId,manualName,isSystemManual,isAfterDate', 2, 2, 'admin@jquiver.io', NOW(), NOW()); 



DROP PROCEDURE IF EXISTS autocompleteListing;
CREATE PROCEDURE autocompleteListing(autocompleteId VARCHAR(100), autocompleteDescription VARCHAR(500), lastUpdatedBy VARCHAR(500), autocompleteTypeId INT(11), isAfterDate VARCHAR(50) ,forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN


  SET @resultQuery = ' SELECT au.ac_id AS autocompleteId, au.ac_description AS autocompleteDescription, au.ac_select_query AS acQuery, au.last_updated_by AS lastUpdatedBy ';
  SET @resultQuery = CONCAT(@resultQuery, ', au.last_updated_ts AS lastUpdatedTs, au.ac_type_id AS autocompleteTypeId, COUNT(jmv.version_id) AS revisionCount ');
  SET @resultQuery = CONCAT(@resultQuery, ', MAX(jmv.version_id) AS max_version_id ');
  SET @fromString  = ' FROM jq_autocomplete_details au ';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jq_module_version AS jmv ON jmv.entity_id = au.ac_id AND jmv.entity_name = 'jq_autocomplete_details' ");
  SET @whereString = ' ';
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT autocompleteId IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND au.ac_id LIKE ''%',autocompleteId,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE au.ac_id LIKE ''%',autocompleteId,'%''');
    END IF;  
  END IF;
  
  IF NOT lastUpdatedBy IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND au.last_updated_ts LIKE ''%',lastUpdatedBy,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE au.last_updated_ts LIKE ''%',lastUpdatedBy,'%''');
    END IF;  
  END IF;
  
  IF NOT autocompleteDescription IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND au.ac_description LIKE ''%',autocompleteDescription,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE au.ac_description LIKE ''%',autocompleteDescription,'%''');
    END IF;  
  END IF;
  
  IF NOT autocompleteTypeId IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND au.ac_type_id = ',autocompleteTypeId);
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE au.ac_type_id = ',autocompleteTypeId);
    END IF;  
  END IF;
  
  IF NOT isAfterDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND au.last_updated_ts > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE au.last_updated_ts > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    END IF;  
  END IF;
  
  SET @groupByString = ' GROUP BY au.ac_id ';
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY lastUpdatedTs DESC');
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

DROP PROCEDURE IF EXISTS dashboardMasterListing;
CREATE PROCEDURE dashboardMasterListing(dashboardName VARCHAR(50), dashboardType VARCHAR(100), contextDescription VARCHAR(1000)
,createdDate VARCHAR(100), createdBy VARCHAR(100),lastUpdatedTs VARCHAR(100), isAfterDate VARCHAR(50), forCount INT, limitFrom INT, limitTo INT
,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
DECLARE db_format VARCHAR(20);


  
  SET @resultQuery = CONCAT(" SELECT db.dashboard_id AS dashboardId, db.dashboard_name AS dashboardName, db.dashboard_type AS dashboardType "
   ," , COUNT(jmv.version_id) AS revisionCount ") ;
  SET @resultQuery = CONCAT(@resultQuery, ', MAX(jmv.version_id) AS max_version_id ');
  SET @resultQuery = CONCAT(@resultQuery, ', db.created_date AS createdDate ');
  SET @resultQuery = CONCAT(@resultQuery, ', db.last_updated_ts AS lastUpdatedTs , COALESCE(CONCAT(jus.first_name, " ", jus.last_name), db.created_by) AS createdBy ');
  SET @fromString  = ' FROM jq_dashboard AS db' ;
  SET @fromString = CONCAT(@fromString, " LEFT JOIN jq_user jus ON  jus.email = COALESCE(db.created_by) LEFT OUTER JOIN jq_module_version jmv ON jmv.entity_id = db.dashboard_id AND jmv.entity_name = 'jq_dashboard' ");
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
  
  IF NOT lastUpdatedTs IS NULL THEN
    SET @lastUpdatedTs= REPLACE(lastUpdatedTs,"'","''");
    SET @whereString = CONCAT(@whereString,' AND db.last_updated_ts like ''%',@lastUpdatedTs,'%''');
  END IF;
 
  IF NOT dashboardType IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND db.dashboard_type = ',dashboardType);
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE db.dashboard_type = ',dashboardType);
    END IF;  
  END IF;
  
  IF NOT isAfterDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND db.last_updated_ts >= STR_TO_DATE(''',isAfterDate,''',''%Y-%m-%d'')');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE db.last_updated_ts >= STR_TO_DATE(''',isAfterDate,''',''%Y-%m-%d'')');
    END IF;  
  END IF;
  
  
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  SET @groupByString = ' GROUP BY db.dashboard_id ';
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY lastUpdatedTs DESC');
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

DROP PROCEDURE IF EXISTS dashletMasterListing;
CREATE PROCEDURE dashletMasterListing(dashletName VARCHAR(50), dashletTitle VARCHAR(100),dashletTypeId INT(11),createdDate VARCHAR(100),createdBy VARCHAR(100),lastUpdatedTs VARCHAR(100),updatedBy VARCHAR(100),STATUS INT, isAfterDate VARCHAR(50), forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
DECLARE db_format VARCHAR(20);


  SET @resultQuery = CONCAT("SELECT dl.dashlet_id AS dashletId, dl.dashlet_title AS dashletTitle,dl.dashlet_name AS dashletName, "
  ,"  dl.created_by AS createdBy,dl.is_active AS status, COUNT(jmv.version_id) AS revisionCount ") ;
  SET @resultQuery = CONCAT(@resultQuery, ', dl.dashlet_type_id AS dashletTypeId ');
  SET @resultQuery = CONCAT(@resultQuery, ', MAX(jmv.version_id) AS max_version_id ');
  SET @resultQuery = CONCAT(@resultQuery, ', dl.created_date AS createdDate');
  SET @resultQuery = CONCAT(@resultQuery, ', dl.last_updated_ts AS lastUpdatedTs , COALESCE(CONCAT(jus.first_name, " ", jus.last_name), COALESCE( dl.updated_by, dl.created_by) ) AS updatedBy ');
  SET @fromString  = ' FROM jq_dashlet AS dl';
  SET @fromString = CONCAT(@fromString, "  LEFT JOIN jq_user jus ON  jus.email = COALESCE(dl.updated_by, dl.created_by) LEFT OUTER JOIN jq_module_version jmv ON jmv.entity_id = dl.dashlet_id AND jmv.entity_name = 'jq_dashlet' ");
  SET @whereString = '';
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  
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
  
    IF NOT lastUpdatedTs IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND date_format(dl.last_updated_ts,''',@dateFormat,''') LIKE ''%',lastUpdatedTs,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE date_format(dl.last_updated_ts,''',@dateFormat,''') LIKE ''%',lastUpdatedTs,'%''');
    END IF;  
  END IF;
  
  IF NOT updatedBy IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND dl.updated_by LIKE ''%',updatedBy,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE dl.updated_by LIKE ''%',updatedBy,'%''');
    END IF;  
  END IF;
  
  
  IF NOT STATUS IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND dl.is_active LIKE ''%',STATUS,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE dl.is_active LIKE ''%',STATUS,'%''');
    END IF;  
  END IF; 
  
  IF NOT dashletTypeId IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND dl.dashlet_type_id = ',dashletTypeId);
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE dl.dashlet_type_id = ',dashletTypeId);
    END IF;  
  END IF;
  
  IF NOT isAfterDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND dl.last_updated_ts >= STR_TO_DATE(''',isAfterDate,''',''%Y-%m-%d'')');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE dl.last_updated_ts >= STR_TO_DATE(''',isAfterDate,''',''%Y-%m-%d'')');
    END IF;  
  END IF;
  
  SET @groupByString = ' GROUP BY dl.dashlet_id ';
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY lastUpdatedTs DESC');
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

DROP PROCEDURE IF EXISTS dynamicFormListing;
CREATE PROCEDURE dynamicFormListing(formId VARCHAR(50), formName VARCHAR(50), formDescription VARCHAR(100),formTypeId INT(11),createdDate VARCHAR(100),createdBy VARCHAR(100), isAfterDate VARCHAR(50), forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN

  SET @resultQuery = CONCAT("SELECT df.form_id AS formId, df.form_description AS formDescription,df.form_name AS formName, "
  ," df.created_by AS createdBy ") ;
  SET @resultQuery = CONCAT(@resultQuery, ', df.form_type_id AS formTypeId, COUNT(jmv.version_id) AS revisionCount  '); 
  SET @resultQuery = CONCAT(@resultQuery, ', MAX(jmv.version_id) AS max_version_id ');
  SET @resultQuery = CONCAT(@resultQuery, ', df.created_date AS createdDate, df.last_updated_ts AS lastUpdatedTs,df.last_updated_by AS lastUpdatedBy ');
  SET @fromString  = ' FROM jq_dynamic_form AS df ';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jq_module_version  jmv ON jmv.entity_id = df.form_id AND jmv.entity_name = 'jq_dynamic_form' ");
  SET @whereString = '';
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  
  IF NOT formId IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND df.form_id LIKE ''%',formId,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE df.form_id LIKE ''%',formId,'%''');
    END IF;  
  END IF;
  
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
  
  IF NOT formTypeId IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND df.form_type_id = ',formTypeId);
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE df.form_type_id = ',formTypeId);
    END IF;  
  END IF;
  
  IF NOT isAfterDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND df.last_updated_ts > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE df.last_updated_ts > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    END IF;  
  END IF;
  
  SET @groupByString = ' GROUP BY df.form_id ';
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY lastUpdatedTs DESC');
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

DROP PROCEDURE IF EXISTS dynarestListing;
CREATE PROCEDURE dynarestListing(dynarestUrl VARCHAR(256), rbacId INT(11), methodName VARCHAR(512)
, methodDescription TEXT , requestTypeId INT(11), producerTypeId INT(11), serviceLogic MEDIUMTEXT,
 platformId INT(11), allowFiles TINYINT(11), dynarestTypeId INT(11), isAfterDate VARCHAR(50),forCount INT, limitFrom INT,
 limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN


  SET @resultQuery = ' SELECT jdrd.jws_dynamic_rest_id AS dynarestId, jdrd.jws_dynamic_rest_url AS dynarestUrl ';
  SET @resultQuery = CONCAT(@resultQuery, ', jdrd.jws_rbac_id AS rbacId, jdrd.jws_method_name AS methodName ');
  SET @resultQuery = CONCAT(@resultQuery, ', jdrd.jws_method_description AS methodDescription, jdrd.jws_request_type_id AS requestTypeId '); 
  SET @resultQuery = CONCAT(@resultQuery, ', jdrd.jws_response_producer_type_id AS producerTypeId, jdrd.jws_service_logic AS serviceLogic ');
  SET @resultQuery = CONCAT(@resultQuery, ', jdrd.jws_platform_id AS platformId, jdrd.jws_allow_files AS allowFiles, jdrd.jws_dynamic_rest_type_id AS dynarestTypeId ');
  SET @resultQuery = CONCAT(@resultQuery, ', jdrd.last_updated_ts AS lastUpdatedTs,jdrd.last_updated_by AS lastUpdatedBy, COUNT(jmv.version_id) AS revisionCount ');
  SET @resultQuery = CONCAT(@resultQuery, ', MAX(jmv.version_id) AS max_version_id ');
  SET @fromString  = ' FROM jq_dynamic_rest_details AS jdrd ';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jq_module_version AS jmv ON jmv.entity_id = jdrd.jws_dynamic_rest_id ");
  SET @whereString = ' ';
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT dynarestUrl IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND jdrd.jws_dynamic_rest_url LIKE ''%',dynarestUrl,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE jdrd.jws_dynamic_rest_url LIKE ''%',dynarestUrl,'%''');
    END IF;  
  END IF;
  
  IF NOT methodName IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND jdrd.jws_method_name LIKE ''%',methodName,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE jdrd.jws_method_name LIKE ''%',methodName,'%''');
    END IF;  
  END IF;
    
  IF NOT methodDescription IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND jdrd.jws_method_description LIKE ''%',methodDescription,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE jdrd.jws_method_description LIKE ''%',methodDescription,'%''');
    END IF;  
  END IF;
    
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

  IF NOT dynarestTypeId IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND jdrd.jws_dynamic_rest_type_id = ',dynarestTypeId);
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE jdrd.jws_dynamic_rest_type_id = ',dynarestTypeId);
    END IF;  
  END IF;
  
  IF NOT isAfterDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND jdrd.last_updated_ts > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE jdrd.last_updated_ts > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    END IF;  
  END IF;
  
  SET @groupByString = ' GROUP BY dynarestId ';
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY lastUpdatedTs ASC');
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
CREATE PROCEDURE gridDetails(gridId VARCHAR(100), gridName VARCHAR(500), gridDesc VARCHAR(500), gridTableName VARCHAR(100)
, gridColumnName VARCHAR(500),gridTypeId INT(11), isAfterDate VARCHAR(50), forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN

  
  SET @resultQuery = ' SELECT gd.grid_id AS gridId, gd.grid_name AS gridName, gd.grid_description AS gridDesc, gd.grid_table_name AS gridTableName, gd.grid_column_names AS gridColumnName ';
  SET @resultQuery = CONCAT(@resultQuery, ', gd.grid_type_id AS gridTypeId, gd.last_updated_ts AS lastUpdatedTs, gd.last_updated_by AS lastUpdatedBy ');
  SET @resultQuery = CONCAT(@resultQuery, ', COUNT(jmv.version_id) AS revisionCount, MAX(jmv.version_id) AS max_version_id ');
  SET @fromString  = ' FROM jq_grid_details AS gd ';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jq_module_version AS jmv ON jmv.entity_id = gd.grid_id AND jmv.entity_name = 'jq_grid_details' ");

  SET @whereString = ' ';
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT gridId IS NULL THEN
    SET @gridId= REPLACE(gridId,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND grid_id LIKE ''%',@gridId,'%''');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE grid_id LIKE ''%',@gridId,'%''');
    END IF; 
   END IF; 
    
  IF NOT gridName IS NULL THEN
    SET @gridName= REPLACE(gridName,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND grid_name LIKE ''%',@gridName,'%''');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE grid_name LIKE ''%',@gridName,'%''');
    END IF;      
  END IF; 
  
  
  IF NOT gridDesc IS NULL THEN
    SET @gridDesc= REPLACE(gridDesc,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND grid_description LIKE ''%',@gridDesc,'%''');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE grid_description LIKE ''%',@gridDesc,'%''');
    END IF;      
  END IF; 
  
  IF NOT gridTableName IS NULL THEN
    SET @gridTableName= REPLACE(gridTableName,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND grid_table_name LIKE ''%',@gridTableName,'%''');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE grid_table_name LIKE ''%',@gridTableName,'%''');
    END IF;      
  END IF; 
  
  
  IF NOT gridColumnName IS NULL THEN
    SET @gridColumnName= REPLACE(gridColumnName,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND grid_column_names LIKE ''%',@gridColumnName,'%''');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE grid_column_names LIKE ''%',@gridColumnName,'%''');
    END IF;      
  END IF; 
  IF NOT gridTypeId IS NULL THEN
    SET @gridTypeId= gridTypeId;
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND grid_type_id = ',@gridTypeId);
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE grid_type_id = ',@gridTypeId);
    END IF;  
    
  END IF;
  
  IF NOT isAfterDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND gd.last_updated_ts > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE gd.last_updated_ts > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    END IF;  
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
CREATE PROCEDURE `notificationlisting`(messageType VARCHAR(500), validFrom VARCHAR(500), messageText VARCHAR(500), validTill VARCHAR(100),  selectionCriteria VARCHAR(500), updatedBy VARCHAR(100), updatedDate VARCHAR(100), isAfterDate VARCHAR(50), displayOnce INT, forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
DECLARE db_format VARCHAR(20);

DECLARE curP CURSOR FOR 
SELECT JSON_VALUE(jpm.property_value, '$.db') AS db_format FROM jq_property_master AS jpm WHERE jpm.property_name = 'jws-date-format';

OPEN curP;
FETCH curP INTO db_format;

  SET @resultQuery = ' SELECT gun.notification_id AS notificationId, message_text AS messageText,  ';
  SET @resultQuery = CONCAT(@resultQuery, ' gun.message_type AS messageType, gun.selection_criteria AS selectionCriteria, gun.updated_by AS updatedBy, COUNT(jmv.version_id) AS revisionCount ');
  SET @resultQuery = CONCAT(@resultQuery, ', MAX(jmv.version_id) AS max_version_id ');
  SET @resultQuery = CONCAT(@resultQuery, ', date_format(gun.message_valid_from,''', db_format,''') as validFrom ');
  SET @resultQuery = CONCAT(@resultQuery, ', date_format(gun.message_valid_till,''', db_format,''') as validTill ');
  SET @resultQuery = CONCAT(@resultQuery, ', date_format(gun.updated_date,''', '%d-%b-%Y %H:%M:%S',''') as updatedDate ');
  SET @resultQuery = CONCAT(@resultQuery, ', gun.`display_once` AS displayOnce ');
  SET @fromString  = ' FROM jq_generic_user_notification AS gun';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jq_module_version AS jmv ON jmv.entity_id = gun.notification_id AND jmv.entity_name = 'jq_generic_user_notification' ");
  SET @whereString = '';
  SET @dateFormat = CONCAT(''', db_format,''');
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
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
  
  IF NOT displayOnce IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND gun.display_once = ',displayOnce);
    ELSE
      SET @whereString = CONCAT('WHERE gun.display_once = ',displayOnce);
    END IF;  
  END IF;
  
  IF NOT validTill IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND date_format(gun.message_valid_till,''',@dateFormat,''') LIKE ''%',validTill,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE date_format(gun.message_valid_till,''',@dateFormat,''') LIKE ''%',validTill,'%''');
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
  
  IF NOT isAfterDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND gun.updated_date >= STR_TO_DATE(''',isAfterDate,''',''%Y-%m-%d'')');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE gun.updated_date >= STR_TO_DATE(''',isAfterDate,''',''%Y-%m-%d'')');
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



DROP PROCEDURE IF EXISTS templateListing;
CREATE PROCEDURE templateListing(templateName VARCHAR(100), templateTypeId INT(11), createdBy VARCHAR(100),updatedBy VARCHAR(100)
, isAfterDate VARCHAR(50), forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN


  SET @resultQuery = ' SELECT tm.template_id as templateId, tm.template_name as templateName, tm.template as template, tm.updated_by as updatedBy, tm.created_by as createdBy ';
  SET @resultQuery = CONCAT(@resultQuery, ', tm.template_type_id AS templateTypeId , COUNT(jmv.version_id) AS revisionCount ');
  SET @resultQuery = CONCAT(@resultQuery, ', MAX(jmv.version_id) AS max_version_id ');
  SET @resultQuery = CONCAT(@resultQuery, ', tm.updated_date as updatedDate ');
  SET @fromString  = ' FROM jq_template_master AS tm ';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jq_module_version AS jmv ON jmv.entity_id = tm.template_id AND jmv.entity_name = 'jq_template_master' ");
  SET @whereString = '';
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT templateName IS NULL THEN
    SET @templateName= REPLACE(templateName,"'","''");
    SET @whereString = CONCAT(@whereString,' WHERE tm.template_name LIKE ''%',@templateName,'%''');
  END IF;
  
  IF NOT templateTypeId IS NULL THEN
    SET @templateTypeId= templateTypeId;
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND tm.template_type_id = ',@templateTypeId);
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE tm.template_type_id = ',@templateTypeId);
    END IF;  
    
  END IF;
  
  IF NOT createdBy IS NULL THEN
    SET @createdBy= REPLACE(createdBy,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND tm.created_by LIKE ''%',@createdBy,'%''');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE tm.created_by LIKE ''%',@createdBy,'%''');
    END IF;  
    
  END IF;
  
  IF NOT updatedBy IS NULL THEN
    SET @updatedBy= REPLACE(updatedBy,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND tm.updated_by LIKE ''%',@updatedBy,'%''');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE tm.updated_by LIKE ''%',@updatedBy,'%''');
    END IF;  
    
  END IF;

  IF NOT isAfterDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND tm.updated_date > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE tm.updated_date > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    END IF;  
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

END;


DROP PROCEDURE IF EXISTS fileUploadConfigListing;
CREATE PROCEDURE `fileUploadConfigListing`(fileBinId VARCHAR(100), fileTypeSupported VARCHAR(100), maxFileSize VARCHAR(1000)
,noOfFiles INT(11), lastUpdatedBy VARCHAR(100), isAfterDate VARCHAR(50), forCount INT, limitFrom INT, limitTo INT
,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN


  SET @resultQuery = CONCAT(" SELECT fuc.file_bin_id AS fileBinId, fuc.file_type_supported AS fileTypeSupported "
  ," , fuc.max_file_size AS maxFileSize, fuc.no_of_files AS noOfFiles, fuc.last_updated_by AS lastUpdatedBy,fuc.last_updated_ts AS lastUpdatedTs " 
  ," , fuc.is_deleted AS isDeleted ") ;
  SET @fromString  = ' FROM jq_file_upload_config AS fuc ';
  SET @whereString = ' WHERE fuc.is_deleted = 0';
  
  IF NOT fileBinId IS NULL THEN
    SET @fileBinId= REPLACE(fileBinId,"'","''");
    SET @whereString = CONCAT(@whereString,' AND fuc.file_bin_id like ''%',@fileBinId,'%''');
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

  IF NOT lastUpdatedBy IS NULL THEN
    SET @lastUpdatedBy= REPLACE(lastUpdatedBy,"'","''");
    SET @whereString = CONCAT(@whereString,' AND fuc.last_updated_by like ''%',@lastUpdatedBy,'%''');
  END IF;

  IF NOT isAfterDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND fuc.last_updated_ts > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE fuc.last_updated_ts > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    END IF;  
  END IF;
  

  
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY lastUpdatedTs DESC');
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
CREATE PROCEDURE moduleListing(moduleId VARCHAR(100), moduleTypeId INT(11), moduleName VARCHAR(500),
moduleURL VARCHAR(500), parentModuleName VARCHAR(500), sequence VARCHAR(100), isInsideMenu INT(1), isAfterDate VARCHAR(50), forCount INT, 
limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN


  SET @resultQuery = ' SELECT ml.module_id AS moduleId,COALESCE(mli18n.module_name,mli18n2.module_name) AS moduleName, ml.module_url AS moduleURL, 
  		(SELECT module_name FROM `jq_module_listing_i18n` WHERE module_id=ml.parent_id) AS parentModuleName'
    ', ml.sequence AS sequence, ml.is_inside_menu AS isInsideMenu, ml.is_home_page AS isHomePage, ml.module_type_id AS moduleTypeId, ml.last_modified_date AS lastUpdatedTs, ml.target_lookup_id AS targetLookupId, ml.request_param_json AS requestParamJson, ml.target_type_id AS targetTypeId ';
  SET @fromString  = ' FROM jq_module_listing AS ml ';
  SET @fromString = CONCAT(@fromString, ' LEFT OUTER JOIN jq_module_listing AS mlParent ON ml.parent_id = mlParent.module_id ');
  SET @fromString = CONCAT(@fromString, ' LEFT OUTER JOIN jq_module_listing_i18n AS mli18n ON ml.module_id = mli18n.module_id AND mli18n.language_id = 1 ');
  SET @fromString = CONCAT(@fromString, ' LEFT OUTER JOIN jq_module_listing_i18n AS mli18n2 ON ml.module_id = mli18n2.module_id AND mli18n2.language_id = 1 ');
  SET @fromString = CONCAT(@fromString, ' LEFT OUTER JOIN jq_module_listing_i18n AS mli18nParent ON mlParent.module_id = mli18nParent.module_id AND mli18nParent.language_id = 1 ');
  SET @fromString = CONCAT(@fromString, ' LEFT OUTER JOIN jq_module_listing_i18n AS mli18nParent2 ON mlParent.module_id = mli18nParent2.module_id AND mli18nParent2.language_id = 1 ');
  SET @whereString = ' WHERE (ml.is_home_page IN (0,1) OR  ml.is_home_page IS NULL) ';
  
  
  IF NOT moduleName IS NULL THEN
    SET @moduleName= REPLACE(moduleName,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString, ' AND COALESCE(mli18n.module_name,mli18n2.module_name) LIKE ''%',@moduleName,'%'''); 
    ELSE
      SET @whereString = CONCAT(@whereString, 'WHERE COALESCE(mli18n.module_name,mli18n2.module_name) LIKE ''%',@moduleName,'%'''); 
    END IF;  
  END IF;
  
  IF NOT moduleTypeId IS NULL THEN
    SET @moduleTypeId= moduleTypeId;
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND ml.module_type_id = ',@moduleTypeId);
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE ml.module_type_id = ',@moduleTypeId);
    END IF;  
    
  END IF;
  
  IF NOT moduleURL IS NULL THEN
    SET @moduleURL= REPLACE(moduleURL,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString, ' AND ml.module_url LIKE ''%',@moduleURL,'%'''); 
    ELSE
      SET @whereString = CONCAT(@whereString, ' WHERE ml.module_url LIKE ''%',@moduleURL,'%'''); 
    END IF; 
  END IF;
  
  IF NOT parentModuleName IS NULL THEN
    SET @parentModuleName= REPLACE(parentModuleName,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString, ' AND COALESCE(mli18nParent.module_name, mli18nParent2.module_name) LIKE ''%',@parentModuleName,'%''');  
    ELSE
      SET @whereString = CONCAT(@whereString, 'WHERE COALESCE(mli18nParent.module_name, mli18nParent2.module_name) LIKE ''%',@parentModuleName,'%'''); 
    END IF; 
 END IF;
  
  IF NOT sequence IS NULL THEN
    SET @sequence= REPLACE(sequence,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString, ' AND  ml.sequence = ''',@sequence,''''); 
    ELSE
      SET @whereString = CONCAT(@whereString, 'WHERE ml.sequence = ''',@sequence,''''); 
    END IF; 
  END IF;
  
  IF NOT isAfterDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND ml.last_modified_date > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE ml.last_modified_date > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    END IF;  
  END IF;
  

  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY ml.last_modified_date DESC');
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


DROP PROCEDURE IF EXISTS homePageListing;
CREATE PROCEDURE homePageListing(moduleId varchar(100), moduleName varchar(500),
moduleURL varchar(500), roleName varchar(100), rolePriority INT(11), forCount INT, 
limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN


  SET @resultQuery = ' SELECT jr.role_id AS roleId, ml.module_id AS moduleId,COALESCE(mli18n.module_name,mli18n2.module_name) AS moduleName, ml.module_url AS moduleURL, jr.role_name AS roleName, jr.role_priority AS rolePriority ';
  SET @fromString  = ' FROM  jq_role AS jr';
  SET @fromString = CONCAT(@fromString, ' LEFT OUTER JOIN jq_module_role_association AS mra ON jr.role_id = mra.role_id AND mra.is_deleted = 0  ');
  SET @fromString = CONCAT(@fromString, ' LEFT OUTER JOIN jq_module_listing AS ml ON mra.module_id = ml.module_id ');
  SET @fromString = CONCAT(@fromString, ' LEFT OUTER JOIN jq_module_listing_i18n AS mli18n ON mli18n.module_id = ml.module_id AND mli18n.language_id = 1 ');
  SET @fromString = CONCAT(@fromString, ' LEFT OUTER JOIN jq_module_listing_i18n AS mli18n2 ON ml.module_id = mli18n2.module_id AND mli18n2.language_id = 1 ');
  SET @whereString = ' WHERE jr.is_active = 1 AND jr.role_id != "ae6465b3-097f-11eb-9a16-f48e38ab9348" ';
  
  
  IF NOT moduleName IS NULL THEN
    SET @moduleName= REPLACE(moduleName,"'","''");
    SET @whereString = CONCAT(@whereString, 'AND COALESCE(mli18n.module_name,mli18n2.module_name) LIKE ''%',@moduleName,'%'''); 
  END IF;
  
  IF NOT moduleURL IS NULL THEN
    SET @moduleURL= REPLACE(moduleURL,"'","''");
    SET @whereString = CONCAT(@whereString, 'AND ml.module_url LIKE ''%',@moduleURL,'%'''); 
  END IF;
  
  IF NOT roleName IS NULL THEN
    SET @roleName= REPLACE(roleName,"'","''");
    SET @whereString = CONCAT(@whereString, 'AND jr.role_name LIKE ''%',@roleName,'%''');  
 END IF;
 
   IF NOT rolePriority IS NULL THEN
    SET @rolePriority= REPLACE(rolePriority,"'","''");
    SET @whereString = CONCAT(@whereString, 'AND jr.role_priority LIKE ''%',@rolePriority,'%''');  
 END IF;

  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY jr.role_priority DESC');
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


DROP PROCEDURE IF EXISTS datasourceListing;
CREATE PROCEDURE datasourceListing(datasourceName VARCHAR(200), databaseProductName VARCHAR(200), productDisplayName VARCHAR(200),createdBy VARCHAR(500), lastUpdatedBy VARCHAR(500)
, isAfterDate VARCHAR(50), forCount INT, limitFrom INT, limitTo INT, sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN

/* SET @dateFormat = NULL;
   SELECT JSON_VALUE(jpm.property_value, '$.db') INTO @dateFormat FROM jq_property_master AS jpm WHERE jpm.property_name = 'jws-date-format'; */


  SET @resultQuery = CONCAT(" SELECT ad.datasource_name AS datasourceName,jdl.database_product_name AS databaseProductName, ad.additional_datasource_id AS additionalDatasourceId, ad.datasource_lookup_id AS datasourceLookupId "
  ," , jdl.db_product_display_name AS productDisplayName, ad.created_by AS createdBy, ad.created_date AS createdDate, ad.last_updated_by AS lastUpdatedBy, ad.last_updated_ts AS lastUpdatedTs  ") ;
  SET @fromString  = ' FROM jq_additional_datasource AS ad';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jq_datasource_lookup AS jdl ON jdl.datasource_lookup_id = ad.datasource_lookup_id ");
  SET @whereString = ' WHERE ad.is_deleted = 0';
  
 
  IF NOT datasourceName IS NULL THEN
    SET @datasourceName= REPLACE(datasourceName,"'","''");
    SET @whereString = CONCAT(@whereString,' AND ad.datasource_name like ''%',@datasourceName,'%''');
  END IF;
  
  IF NOT productDisplayName IS NULL THEN
    SET @productDisplayName= REPLACE(productDisplayName,"'","''");
    SET @whereString = CONCAT(@whereString,' AND jdl.db_product_display_name like ''%',@productDisplayName,'%''');
  END IF;
  
  IF NOT databaseProductName IS NULL THEN
    SET @databaseProductName= REPLACE(databaseProductName,"'","''");
    SET @whereString = CONCAT(@whereString,' AND jdl.database_product_name like ''%',@databaseProductName,'%''');
  END IF;
  
  IF NOT createdBy IS NULL THEN
    SET @createdBy= REPLACE(createdBy,"'","''");
    SET @whereString = CONCAT(@whereString,' AND ad.created_by like ''%',@createdBy,'%''');
  END IF;
  
  IF NOT lastUpdatedBy IS NULL THEN
    SET @lastUpdatedBy= REPLACE(lastUpdatedBy,"'","''");
    SET @whereString = CONCAT(@whereString,' AND ad.last_updated_by like ''%',@lastUpdatedBy,'%''');
  END IF;
    
  IF NOT isAfterDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND ad.last_updated_ts > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE ad.last_updated_ts > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    END IF;  
  END IF;
  
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY lastUpdatedTs DESC');
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


CREATE  OR REPLACE VIEW `jq_api_client_details_view` AS
SELECT
  `cd`.`client_id`            AS `client_id`,
  `cd`.`client_name`          AS `client_name`,
  `cd`.`client_key`           AS `client_key`,
  `cd`.`client_secret`        AS `client_secret`,
  `cd`.`encryption_algo_id`   AS `encryption_algo_id`,
  `cd`.`updated_by`           AS `updated_by`,
  `cd`.`created_by`           AS `created_by`,
  `cd`.`updated_date`         AS `updated_date`,
  `ed`.`encryption_algo_name` AS `encryption_algo_name`,
  `cd`.`updated_date`         AS `isAfterDate`
FROM (`jq_api_client_details` `cd`
   JOIN `jq_enc_alg_mod_pad_key_lookup` `ampk`
   JOIN `jq_encryption_algorithms_lookup` `ed`)
WHERE `ampk`.`enc_lookup_id` = `cd`.`encryption_algo_id` AND `ed`.`encryption_algo_id` = `ampk`.`enc_algorithm_id`;



DROP PROCEDURE IF EXISTS `apiClientDetails`;

CREATE PROCEDURE `apiClientDetails`(client_name VARCHAR(200), client_key VARCHAR(200), encryption_algo_name VARCHAR(200),updated_by VARCHAR(500), updated_date VARCHAR(500)
, isAfterDate VARCHAR(50), forCount INT, limitFrom INT, limitTo INT, sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN

/* SET @dateFormat = NULL;
   SELECT JSON_VALUE(jpm.property_value, '$.db') INTO @dateFormat FROM jq_property_master AS jpm WHERE jpm.property_name = 'jws-date-format'; */


  SET @resultQuery = CONCAT("SELECT `cd`.`client_id`            AS `client_id`,
  `cd`.`client_name`          AS `client_name`,
  `cd`.`client_key`           AS `client_key`,
  `cd`.`client_secret`        AS `client_secret`,
  `cd`.`encryption_algo_id`   AS `encryption_algo_id`,
  `cd`.`updated_by`           AS `updated_by`,
  `cd`.`created_by`           AS `created_by`,
  `cd`.`updated_date`         AS `updated_date`,
  `ed`.`encryption_algo_name` AS `encryption_algo_name` ") ;
  SET @fromString  = ' FROM `jq_api_client_details` `cd`, `jq_encryption_algorithms_lookup` `ed` ';
  SET @whereString = ' WHERE `ed`.`encryption_algo_id` = `cd`.`encryption_algo_id` ';
  
 
  IF NOT client_name IS NULL THEN
    SET @client_name= REPLACE(client_name,"'","''");
    SET @whereString = CONCAT(@whereString,' AND cd.client_name like ''%',@client_name,'%''');
  END IF;
  
  IF NOT client_key IS NULL THEN
    SET @client_key= REPLACE(client_key,"'","''");
    SET @whereString = CONCAT(@whereString,' AND cd.client_key like ''%',@client_key,'%''');
  END IF;
  
  IF NOT encryption_algo_name IS NULL THEN
    SET @encryption_algo_name= REPLACE(databaseProductName,"'","''");
    SET @whereString = CONCAT(@whereString,' AND ed.encryption_algo_name like ''%',@encryption_algo_name,'%''');
  END IF;
  
  IF NOT updated_by IS NULL THEN
    SET @updated_by= REPLACE(updated_by,"'","''");
    SET @whereString = CONCAT(@whereString,' AND cd.updated_by like ''%',@updated_by,'%''');
  END IF;
  
  IF NOT updated_date IS NULL THEN
    SET @updated_date= REPLACE(updated_date,"'","''");
    SET @whereString = CONCAT(@whereString,' AND cd.updated_date like ''%',@updated_date,'%''');
  END IF;
    
  IF NOT isAfterDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND cd.updated_date > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE cd.updated_date > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    END IF;  
  END IF;
  
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY cd.updated_date DESC');
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


DROP PROCEDURE IF EXISTS manageEntityRoleListing;
CREATE PROCEDURE `manageEntityRoleListing`(entityName VARCHAR(50), moduleId VARCHAR(50), isAfterDate VARCHAR(50),forCount INT, limitFrom INT, limitTo INT
,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN

  SET @selectRoleQuery  =  (SELECT GROUP_CONCAT(CONCAT ('GROUP_CONCAT(CASE WHEN jr.role_name="',jr.role_name,'" THEN CONCAT(jera.role_id,"@::@",jera.is_active) END)  AS `',jr.role_name,'`')) FROM jq_role AS jr ORDER BY jr.role_name);
  SET @resultQuery = CONCAT(" SELECT jera.entity_role_id AS entityRoleId,jera.entity_id AS entityId,jera.entity_name AS entityName,jera.module_id AS moduleId,jmm.module_name AS moduleName, "
  ,@selectRoleQuery ) ;
  
  SET @fromString  = ' FROM  jq_role jr RIGHT OUTER JOIN jq_entity_role_association  jera ON jera.role_id = jr.role_id INNER JOIN jq_master_modules jmm ON jmm.module_id = jera.module_id ';
  
  SET @whereString = ' WHERE jr.is_active=1  AND jera.module_type_id=0 ';
  
  SET @selectRoleModuleTypeQuery  =  (SELECT GROUP_CONCAT(CONCAT ('GROUP_CONCAT(CASE WHEN jr.role_name="',jr.role_name,'" THEN CONCAT(jrma.role_id,"@::@",jrma.is_active) END)  AS `',jr.role_name,'`')) FROM jq_role AS jr ORDER BY jr.role_name);
  
  SET @resultModuleTypeQuery = CONCAT(" SELECT jrma.role_module_id AS entityRoleId, jrma.module_id AS entityId, jmm.module_name AS entityName,'7982cc6a-6bd3-11ed-997d-7c8ae1bb24d8' AS moduleId,'Master module' AS moduleName, "
  ,@selectRoleModuleTypeQuery ) ; 
  
  SET @fromModuleTypeQueryString  = ' FROM jq_role jr LEFT OUTER JOIN jq_role_master_modules_association jrma ON jrma.role_id = jr.role_id LEFT OUTER JOIN jq_master_modules jmm ON jrma.module_id=jmm.module_id';	 
   
  SET @whereModuleTypeQuery = ' WHERE jmm.is_perm_supported=1  GROUP BY jmm.module_name';
  
  IF NOT moduleId IS NULL THEN
    SET @moduleId= REPLACE(moduleId,"'","''");
    SET @whereString = CONCAT(@whereString,' AND jera.module_id like ''%',@moduleId,'%''');
  END IF;
  
  IF NOT entityName IS NULL THEN
    SET @entityName= REPLACE(entityName,"'","''");
    SET @whereString = CONCAT(@whereString,' AND entity_name like ''%',@entityName,'%''');
  END IF; 
  
  
	SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
	IF NOT sortIndex IS NULL THEN
		SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
		SET @orderBy = CONCAT(' ORDER BY jera.last_updated_date DESC');
	END IF;
  
	SET @groupByString = CONCAT(' GROUP BY jera.entity_id ');
	SET @unionString =' UNION ';
	SET @queryFirstString=CONCAT(@resultQuery, @fromString, @whereString, @groupByString);
	SET @querySecondString=CONCAT(@resultModuleTypeQuery, @fromModuleTypeQueryString, @whereModuleTypeQuery);
	IF moduleId IS NULL OR moduleId = '7982cc6a-6bd3-11ed-997d-7c8ae1bb24d8' THEN	
		IF forCount=1 THEN	
			SET @queryString=CONCAT('SELECT COUNT(*) FROM ( ',@queryFirstString, @unionString, @querySecondString,' ) AS cnt'); 	
		ELSE
			SET @queryString=CONCAT(@queryFirstString, @unionString, @querySecondString, @limitString);
		END IF;	
	ELSE
		IF forCount=1 THEN	
			SET @queryString=CONCAT('SELECT COUNT(*) FROM ( ',@queryFirstString,' ) AS cnt'); 	
		ELSE
			SET @queryString=CONCAT(@queryFirstString, @limitString);
		END IF;
	END IF;
  
 	PREPARE stmt FROM @queryString;
 	EXECUTE stmt;
 	DEALLOCATE PREPARE stmt;
 
END;



DROP PROCEDURE IF EXISTS userListingGrid;
CREATE PROCEDURE `userListingGrid`(email VARCHAR(100), firstName VARCHAR(500), lastName VARCHAR(500), `status` INT
, isAfterDate VARCHAR(50), forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN

  
  SET @resultQuery = ' SELECT user_id as user_id, first_name as firstName, last_name AS lastName, email AS email, password AS password, is_active as status ';
  SET @fromString  = ' FROM jq_user ';
  SET @whereString = ' ';
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT email IS NULL THEN
    SET @email= REPLACE(email,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND email LIKE ''%',@email,'%''');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE email LIKE ''%',@email,'%''');
    END IF; 
   END IF; 
    
  IF NOT firstName IS NULL THEN
    SET @firstName= REPLACE(firstName,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND first_name LIKE ''%',@firstName,'%''');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE first_name LIKE ''%',@firstName,'%''');
    END IF;      
  END IF; 
  
  IF NOT lastName IS NULL THEN
    SET @lastName= REPLACE(lastName,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND last_name LIKE ''%',@lastName,'%''');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE last_name LIKE ''%',@lastName,'%''');
    END IF;      
  END IF; 
  
  
  IF NOT `status` IS NULL THEN
    SET @status= REPLACE(`status`,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND is_active = ',@status);
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE is_active =',@status);
    END IF;      
  END IF; 
    
  SET @groupByString = ' ';
    
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY first_name ASC');
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



DROP PROCEDURE IF EXISTS roleGrid;
CREATE PROCEDURE `roleGrid`(roleName VARCHAR(100), `isActive` INT
, isAfterDate VARCHAR(50), forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN

  
  SET @resultQuery = ' SELECT `role_id` as `roleId`, `role_name` as `roleName`, `role_description` AS `roleDescription`, `is_active` AS `isActive`, `role_priority` AS `rolePriority` ';
  SET @fromString  = ' FROM `jq_role` ';
  SET @whereString = ' ';
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT roleName IS NULL THEN
    SET @roleName= REPLACE(roleName,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND role_name LIKE ''%',@roleName,'%''');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE role_name LIKE ''%',@roleName,'%''');
    END IF; 
   END IF; 
    
  
  IF NOT `isActive` IS NULL THEN
    SET @isActive= REPLACE(`isActive`,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND is_active = ',@isActive);
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE is_active =',@status);
    END IF;      
  END IF; 
    
  SET @groupByString = ' ';
    
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY role_name ASC');
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



DROP PROCEDURE IF EXISTS helpManualListingGrid;
CREATE PROCEDURE `helpManualListingGrid`(manualId VARCHAR(100),manualName VARCHAR(100), `isSystemManual` INT
, isAfterDate VARCHAR(50), forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))

BEGIN

  
  SET @resultQuery = ' SELECT `manual_id` as `manualId`, `name` as `manualName`, `is_system_manual` AS `isSystemManual` ';
  SET @fromString  = ' FROM `jq_manual_type` ';
  SET @whereString = ' ';
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT manualId IS NULL THEN
    SET @manualId= REPLACE(manualId,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND manual_id LIKE ''%',@manualId,'%''');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE manual_id LIKE ''%',@manualId,'%''');
    END IF; 
   END IF; 
    
  IF NOT manualName IS NULL THEN
    SET @manualName= REPLACE(manualName,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND name LIKE ''%',@manualName,'%''');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE name LIKE ''%',@manualName,'%''');
    END IF; 
   END IF; 
    
  
  IF NOT `isSystemManual` IS NULL THEN
    SET @isSystemManual= REPLACE(`isSystemManual`,"'","''");
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND is_system_manual = ',@isSystemManual);
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE is_system_manual =',@isSystemManual);
    END IF;      
  END IF; 
    
  SET @groupByString = ' ';
    
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY name ASC');
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


CREATE  OR REPLACE VIEW `jq_manage_entity_role_listing` AS
SELECT `a`.`entityroleid`    AS `entityroleid`,
       `a`.`entityid`        AS `entityid`,
       `a`.`entityname`      AS `entityname`,
       `a`.`moduleid`        AS `moduleid`,
       `a`.`modulename`      AS `modulename`,
       `a`.`role_name`       AS `role_name`,
       `a`.`role_id`         AS `role_id`,
       `a`.`is_active`       AS `is_active`,
       a.role_type_id 		 AS role_type_id FROM   (
       (
                 SELECT    `jera`.`entity_role_id` AS `entityroleid`,
                           `jera`.`entity_id`      AS `entityid`,
                           `jera`.`entity_name`    AS `entityname`,
                           `jera`.`module_id`      AS `moduleid`,
                           `jmm`.`module_name`     AS `modulename`,
                           `jr`.`role_name`        AS `role_name`,
                           `jera`.`role_id`        AS `role_id`,
                           `jera`.`is_active`      AS `is_active`,
                            jera.role_type_id       AS role_type_id
                 FROM      ((`jq_entity_role_association` `jera`
                 LEFT JOIN `jq_role` `jr`
                 ON       (
                                     `jera`.`role_id` = `jr`.`role_id`))
                 JOIN      `jq_master_modules` `jmm`
                 ON       (
                                     `jmm`.`module_id` = `jera`.`module_id`))
                 WHERE     `jr`.`is_active` = 1
                 AND       `jera`.`module_type_id` = 0 )
UNION
      (
                SELECT    `jrma`.`role_module_id`                AS `entityroleid`,
                          `jrma`.`module_id`                     AS `entityid`,
                          `jmm`.`module_name`                    AS `entityname`,
                          '7982cc6a-6bd3-11ed-997d-7c8ae1bb24d8' AS `moduleid`,
                          'Master module'                        AS `modulename`,
                          `jr`.`role_name`                       AS `role_name`,
                          `jrma`.`role_id`                       AS `role_id`,
                          `jrma`.`is_active`                     AS `is_active`,
			   			   jrma.role_type_id		             AS role_type_id
                FROM      ((`jq_role` `jr`
                LEFT JOIN `jq_role_master_modules_association` `jrma`
                ON       (
                                    `jrma`.`role_id` = `jr`.`role_id`))
                LEFT JOIN `jq_master_modules` `jmm`
                ON       (
                                    `jrma`.`module_id` = `jmm`.`module_id`))
                WHERE     `jmm`.`is_perm_supported` = 1 )) `a`;
                
                
