DROP PROCEDURE IF EXISTS moduleTargetType;
CREATE PROCEDURE `moduleTargetType`(targetTypeId INT(11))
BEGIN

IF NOT targetTypeId IS NULL THEN

  IF targetTypeId = 1 THEN 
    SET @targetQuery = " SELECT dashboard_id AS targetTypeId, dashboard_name AS targetTypeName FROM  dashboard WHERE is_deleted = 0";
  ELSEIF targetTypeId = 2 THEN 
    SET @targetQuery = " SELECT form_id AS targetTypeId, form_name AS targetTypeName FROM dynamic_form ";
  ELSEIF targetTypeId = 3 THEN 
    SET @targetQuery = " SELECT jws_dynamic_rest_id AS targetTypeId, jws_method_name AS targetTypeName FROM jws_dynamic_rest_details ";
  ELSEIF targetTypeId = 5 THEN 
    SET @targetQuery = " SELECT template_id AS targetTypeId, template_name AS targetTypeName FROM template_master  ";
  END IF;
  
END IF;


 PREPARE stmt FROM @targetQuery;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
END;
