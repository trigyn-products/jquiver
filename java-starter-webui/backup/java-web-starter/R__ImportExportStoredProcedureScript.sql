DROP PROCEDURE IF EXISTS manageEntityPermissionListing;
CREATE PROCEDURE manageEntityPermissionListing
(entityName VARCHAR(1000), moduleName VARCHAR(100), moduleId VARCHAR(50), roleName VARCHAR(100), isAfterDate VARCHAR(50),forCount INT, limitFrom INT, limitTo INT
,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN

  SET @resultQuery = CONCAT(" SELECT jera.entity_role_id AS entityRoleId,jera.entity_id AS entityId, jera.entity_name AS entityName ");
  SET @resultQuery = CONCAT(@resultQuery, " ,jera.module_id AS moduleId, jmm.module_name AS moduleName, jr.role_name AS roleName, jera.last_updated_date AS lastUpdatedDate, ju.email AS lastUpdatedBy ") ;
  SET @fromString  = ' FROM  jq_entity_role_association AS jera '
  ' LEFT OUTER JOIN jq_role AS jr ON jr.role_id = jera.role_id AND jr.is_active = 1 '
  ' LEFT OUTER JOIN jq_master_modules jmm ON jmm.module_id = jera.module_id  '
  ' LEFT OUTER JOIN jq_user ju ON ju.user_id = jera.last_updated_by  ';
  SET @whereString = ' WHERE jera.is_active = 1 ';
   
  IF NOT moduleId IS NULL THEN
    SET @moduleId= REPLACE(moduleId,"'","''");
    SET @whereString = CONCAT(@whereString,' AND jera.module_id like ''%',@moduleId,'%''');
  END IF;
  
  IF NOT entityName IS NULL THEN
    SET @entityName= REPLACE(entityName,"'","''");
    SET @whereString = CONCAT(@whereString,' AND jera.entity_name like ''%',@entityName,'%''');
  END IF;
  
  IF NOT moduleName IS NULL THEN
    SET @moduleName= REPLACE(moduleName,"'","''");
    SET @whereString = CONCAT(@whereString,' AND jmm.module_name like ''%',@moduleName,'%''');
  END IF;
  
  IF NOT roleName IS NULL THEN
    SET @roleName= REPLACE(roleName,"'","''");
    SET @whereString = CONCAT(@roleName,' AND jr.role_name like ''%',@roleName,'%''');
  END IF;
  
  IF NOT isAfterDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND jera.last_updated_date > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    ELSE
      SET @whereString = CONCAT(@whereString,' WHERE jera.last_updated_date > STR_TO_DATE(''',isAfterDate,''',''%d-%b-%Y'')');
    END IF;  
  END IF;
  
  
  
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY last_updated_date DESC');
  END IF;
  
  
	IF forCount=1 THEN
  	SET @queryString=CONCAT('SELECT COUNT(*) FROM ( ',@resultQuery, @fromString, @whereString,@orderBy,' ) AS cnt');
  ELSE
  	SET @queryString=CONCAT(@resultQuery, @fromString, @whereString, @orderBy, @limitString);
  END IF;


  
 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
 
END;


REPLACE INTO jq_grid_details(grid_id,grid_name,grid_description,grid_table_name,grid_column_names, query_type, grid_type_id, created_by, created_date, last_updated_ts)
 VALUES ('manageEntityPermissionListing','Import Export Permission Listing','Import Export Permission Listing','manageEntityPermissionListing' 
,'entityName,moduleName,moduleId,roleName,lastUpdatedDate, lastUpdatedBy,isAfterDate', 2 , 2, 'mini.pillai@jquiver.io', NOW(), NOW());

  
REPLACE INTO jq_entity_role_association(entity_role_id,entity_id,entity_name,module_id,role_id,last_updated_date,last_updated_by,is_active,module_type_id) 
VALUES ('43f3ea6e-5596-11eb-9e7a-f48e38ab8cd7','manageEntityPermissionListing','manageEntityPermissionListing','07067149-098d-11eb-9a16-f48e38ab9348','ae6465b3-097f-11eb-9a16-f48e38ab9348',NOW(),'admin',1,0);

REPLACE INTO jq_entity_role_association (entity_role_id,entity_id,entity_name,module_id,role_id,last_updated_date,last_updated_by,is_active,module_type_id) 
VALUES ('343dd25b-5596-11eb-9e7a-f48e38ab8cd7','manageEntityPermissionListing','manageEntityPermissionListing','07067149-098d-11eb-9a16-f48e38ab9348','b4a0dda1-097f-11eb-9a16-f48e38ab9348',NOW(),'admin',1,0);

REPLACE INTO jq_entity_role_association (entity_role_id,entity_id,entity_name,module_id,role_id,last_updated_date,last_updated_by,is_active,module_type_id) 
VALUES ('3b433058-5596-11eb-9e7a-f48e38ab8cd7','manageEntityPermissionListing','manageEntityPermissionListing','07067149-098d-11eb-9a16-f48e38ab9348','2ace542e-0c63-11eb-9cf5-f48e38ab9348',NOW(),'admin',1,0);

