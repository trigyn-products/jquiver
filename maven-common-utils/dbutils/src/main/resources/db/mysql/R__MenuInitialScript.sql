SET FOREIGN_KEY_CHECKS=0;
DROP PROCEDURE IF EXISTS moduleTargetType;
CREATE PROCEDURE moduleTargetType(targetLookupId INT(11), targetTypeId VARCHAR(50))
BEGIN

IF NOT targetLookupId IS NULL THEN
 	SET @targetTypeId= REPLACE(targetTypeId,"'","''");
 	SET @targetLookupId= REPLACE(targetLookupId,"'","''");
  IF targetLookupId = 1 THEN 
    SET @targetQuery = " SELECT dashboard_id AS targetTypeId, dashboard_name AS targetTypeName FROM jq_dashboard WHERE is_deleted = 0";
  ELSEIF targetLookupId = 2 THEN 
    SET @targetQuery = " SELECT form_id AS targetTypeId, form_name AS targetTypeName FROM jq_dynamic_form ";
  ELSEIF targetLookupId = 3 THEN 
    SET @targetQuery = " SELECT jws_dynamic_rest_id AS targetTypeId, jws_method_name AS targetTypeName FROM jq_dynamic_rest_details ";
  ELSEIF targetLookupId = 5 THEN 
    SET @targetQuery = " SELECT template_id AS targetTypeId, template_name AS targetTypeName FROM jq_template_master  ";
  ELSE
    SET @targetQuery = " SELECT @targetTypeId AS targetTypeId, @targetLookupId AS targetTypeName  ";
  END IF;
  
END IF;


  
IF NOT targetTypeId IS NULL THEN
  SET @targetTypeId= REPLACE(targetTypeId,"'","''");
  
  IF targetLookupId = 1 THEN 
    SET @targetQuery = CONCAT(@targetQuery,' AND dashboard_id = ''',@targetTypeId,'''');
  ELSEIF targetLookupId = 2 THEN 
    SET @targetQuery = CONCAT(@targetQuery,' WHERE form_id = ''',@targetTypeId,'''');
  ELSEIF targetLookupId = 3 THEN 
    SET @targetQuery = CONCAT(@targetQuery,' WHERE jws_dynamic_rest_id = ''',@targetTypeId,'''');
  ELSEIF targetLookupId = 5 THEN 
    SET @targetQuery = CONCAT(@targetQuery,' WHERE template_id = ''',@targetTypeId,'''');
  END IF;
  
END IF;


 PREPARE stmt FROM @targetQuery;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
END;
REPLACE INTO jq_module_target_lookup (lookup_id,description) VALUES (7,'Target URL');

REPLACE INTO jq_module_target_lookup (lookup_id,description) VALUES (6,'Root');

REPLACE INTO jq_module_target_lookup (lookup_id,description) VALUES (1,'Dashboard');
REPLACE INTO jq_module_target_lookup (lookup_id,description) VALUES (2,'Form Builder');
REPLACE INTO jq_module_target_lookup (lookup_id,description) VALUES (3,'REST API');
REPLACE INTO jq_module_target_lookup (lookup_id,description) VALUES (4,'Model and View');
REPLACE INTO jq_module_target_lookup (lookup_id,description) VALUES (5,'Template');

SET FOREIGN_KEY_CHECKS=1;