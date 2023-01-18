SET FOREIGN_KEY_CHECKS=0;

DROP PROCEDURE IF EXISTS dynarestListing;
CREATE PROCEDURE `dynarestListing`(dynarestUrl varchar(256), rbacId INT(11), methodName VARCHAR(512)
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
  SET @fromString  = ' FROM jq_dynamic_rest_details AS jdrd ';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jq_module_version AS jmv ON jmv.entity_id = jdrd.jws_dynamic_rest_id AND jmv.entity_name = 'jq_dynamic_rest_details' ");
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

SET FOREIGN_KEY_CHECKS=1;