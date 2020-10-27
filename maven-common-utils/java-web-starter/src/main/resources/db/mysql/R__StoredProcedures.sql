REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id) 
VALUES ("dashboardMasterListingGrid", 'Dashboard Master Listing', 'Dashboard Master', 'dashboardMasterListing'
,'dashboardName,dashboardType,contextDescription,createdDate,createdBy,lastUpdatedDate', 2);


DROP PROCEDURE IF EXISTS dashboardMasterListing;
CREATE PROCEDURE `dashboardMasterListing`(dashboardName varchar(50), dashboardType varchar(100), contextDescription varchar(1000)
,createdDate varchar(100), createdBy varchar(100),lastUpdatedDate varchar(100), forCount INT, limitFrom INT, limitTo INT
,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
  SET @resultQuery = CONCAT(" SELECT db.dashboard_id AS dashboardId, db.dashboard_name AS dashboardName, db.dashboard_type AS dashboardType "
  ," , db.created_by AS createdBy, date_format(db.created_date, '%d %b %Y') AS createdDate,date_format(db.last_updated_date, '%d %b %Y') AS lastUpdatedDate "
  ," ,cm.context_description AS contextDescription ") ;
  SET @fromString  = ' FROM dashboard AS db INNER JOIN context_master cm ON db.context_id = cm.context_id ';
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
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY lastUpdatedDate DESC');
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


DROP PROCEDURE IF EXISTS  dashletMasterListing;
CREATE PROCEDURE dashletMasterListing(dashletName varchar(50), dashletTitle varchar(100),dashletTypeId INT(11),createdDate varchar(100),createdBy varchar(100),updatedDate varchar(100),updatedBy varchar(100),status INT, forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
  SET @resultQuery = CONCAT("select dl.dashlet_id as dashletId, dl.dashlet_title AS dashletTitle,dl.dashlet_name AS dashletName, "
  ," date_format(dl.created_date,'%d %b %Y') AS createdDate,date_format(dl.updated_date,'%d %b %Y') AS updatedDate, "
  ," dl.updated_by as updatedBy, dl.created_by as createdBy,dl.is_active AS status ") ;
  SET @resultQuery = CONCAT(@resultQuery, ', dl.dashlet_type_id AS dashletTypeId ');
  SET @fromString  = ' from dashlet AS dl';
  SET @whereString = '';
  SET @dateFormat = '%d %b %Y';
  SET @limitString = CONCAT(' limit ','',CONCAT(limitFrom,',',limitTo));
  
  
  IF NOT dashletName IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND dashlet_name LIKE ''%',dashletName,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE dashlet_name LIKE ''%',dashletName,'%''');
    END IF;  
  END IF;
  
  IF NOT dashletTitle IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND dashlet_title LIKE ''%',dashletTitle,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE dashlet_title LIKE ''%',dashletTitle,'%''');
    END IF;  
  END IF;
  
  IF NOT createdDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND date_format(created_date,''',@dateFormat,''') LIKE ''%',createdDate,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE date_format(created_date,''',@dateFormat,''') LIKE ''%',createdDate,'%''');
    END IF;  
  END IF;
  
  IF NOT createdBy IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND created_by LIKE ''%',createdBy,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE created_by LIKE ''%',createdBy,'%''');
    END IF;  
  END IF;
  
    IF NOT updatedDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND date_format(updated_date,''',@dateFormat,''') LIKE ''%',updatedDate,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE date_format(updated_date,''',@dateFormat,''') LIKE ''%',updatedDate,'%''');
    END IF;  
  END IF;
  
  IF NOT updatedBy IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND updated_by LIKE ''%',updatedBy,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE updated_by LIKE ''%',updatedBy,'%''');
    END IF;  
  END IF;
  
  
  IF NOT status IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND is_active LIKE ''%',status,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE is_active LIKE ''%',status,'%''');
    END IF;  
  END IF; 
  
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY updated_date DESC');
  END IF;
  
	IF forCount=1 THEN
  	SET @queryString=CONCAT('select count(*) from ( ',@resultQuery, @fromString, @whereString, @orderBy,' ) as cnt');
  ELSE
  	SET @queryString=CONCAT(@resultQuery, @fromString, @whereString, @orderBy, @limitString);
  END IF;

 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
END;


REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id) 
VALUES ("dashletMasterListingGrid", 'Dashlet Master Listing', 'Dashlet Master', 'dashletMasterListing','dashletName,dashletTitle,dashletTypeId,createdDate,createdBy,updatedDate,updatedBy,status', 2);


DROP PROCEDURE IF EXISTS dynamicFormListing;
CREATE PROCEDURE dynamicFormListing(formName varchar(50), formDescription varchar(100),formTypeId INT(11),createdDate varchar(100),createdBy varchar(100), forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
  SET @resultQuery = CONCAT("select df.form_id as formId, df.form_description AS formDescription,df.form_name AS formName, "
  ," date_format(df.created_date,'%d %b %Y') AS createdDate, df.created_by as createdBy") ;
  SET @resultQuery = CONCAT(@resultQuery, ', df.form_type_id AS formTypeId '); 
  SET @fromString  = ' from dynamic_form AS df ';
  SET @whereString = '';
  SET @dateFormat = '%d %b %Y';
  SET @limitString = CONCAT(' limit ','',CONCAT(limitFrom,',',limitTo));
  
  
  IF NOT formName IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND form_name LIKE ''%',formName,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE form_name LIKE ''%',formName,'%''');
    END IF;  
  END IF;
  
  IF NOT formDescription IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND form_description LIKE ''%',formDescription,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE form_description LIKE ''%',formDescription,'%''');
    END IF;  
  END IF;
  
  IF NOT createdDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND date_format(created_date,''',@dateFormat,''') LIKE ''%',createdDate,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE date_format(created_date,''',@dateFormat,''') LIKE ''%',createdDate,'%''');
    END IF;  
  END IF;
  
  IF NOT createdBy IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND created_by LIKE ''%',createdBy,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE created_by LIKE ''%',createdBy,'%''');
    END IF;  
  END IF;
  
  
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY created_date DESC');
  END IF;
  
	IF forCount=1 THEN
  	SET @queryString=CONCAT('select count(*) from ( ',@resultQuery, @fromString, @whereString, @orderBy,' ) as cnt');
  ELSE
  	SET @queryString=CONCAT(@resultQuery, @fromString, @whereString, @orderBy, @limitString);
  END IF;

 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
END;

REPLACE INTO grid_details (grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id)  VALUES 
("dynamicFormListingGrid", 'Dynamic Form Master', 'Dynamic Form Master Listing', 'dynamicFormListing','formName,formDescription,formTypeId,createdDate,createdBy', 2);

DROP PROCEDURE IF EXISTS templateListing;
CREATE PROCEDURE `templateListing`(templateName varchar(100), templateTypeId INT(11), createdBy VARCHAR(100),updatedBy varchar(100)
, forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
  SET @resultQuery = ' SELECT tm.template_id as templateId, tm.template_name as templateName, tm.template as template, tm.updated_by as updatedBy, tm.created_by as createdBy, date_format(tm.updated_date, ''%d %b %Y'') as updatedDate ';
  SET @resultQuery = CONCAT(@resultQuery, ', tm.template_type_id AS templateTypeId ');
  SET @fromString  = ' FROM template_master AS tm ';
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

  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY tm.updated_date DESC');
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

REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id) 
VALUES ("templateListingGrid", 'Template Listing', 'Template Listing', 'templateListing', 'templateName,templateTypeId,createdBy,updatedBy', 2);


REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id) 
VALUES ("resourceBundleListingGrid", 'DB Resource Bundle Listing', 'DB Resource Bundle Listing', 'dbResourceListing',
'resourceKey,languageName,resourceBundleText', 2);

DROP PROCEDURE IF EXISTS dbResourceListing;
CREATE PROCEDURE dbResourceListing (resourceKey varchar(100), languageName varchar(100), resourceBundleText text, forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
  SET @selectQuery = ' SELECT rb.resource_key AS resourceKey, lang.language_name AS languageName, rb.`text` AS resourceBundleText ';
  SET @fromString  = ' FROM resource_bundle AS rb INNER JOIN language AS lang ON lang.language_id = rb.language_id ';
  
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
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY resourceKey ASC');
  END IF;
  
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
	IF forCount=1 THEN
  	SET @queryString=CONCAT('SELECT COUNT(*) FROM ( ',@selectQuery, @fromString, @whereString, @orderBy,' ) AS cnt');
  ELSE
  	SET @queryString=CONCAT(@selectQuery, @fromString, @whereString, @orderBy, @limitString);
  END IF;

 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
END;


DROP PROCEDURE IF EXISTS gridDetails;
CREATE PROCEDURE `gridDetails`(gridId varchar(100), gridName varchar(500), gridDesc varchar(500), gridTableName varchar(100)
, gridColumnName varchar(500),gridTypeId INT(11), forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
  SET @resultQuery = ' SELECT gd.grid_id AS gridId, gd.grid_name AS gridName, gd.grid_description AS gridDesc, gd.grid_table_name AS gridTableName, gd.grid_column_names AS gridColumnName ';
  SET @resultQuery = CONCAT(@resultQuery, ', gd.grid_type_id AS gridTypeId ');
  SET @fromString  = ' FROM grid_details AS gd ';

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
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY grid_id ASC');
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


REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id)
 VALUES ("gridDetailsListing", 'Grid Details Listing', 'Grid Details Listing', 'gridDetails', 'gridId,gridName,gridDesc,gridTableName,gridColumnName,gridTypeId', 2);

DROP PROCEDURE IF EXISTS notificationlisting;
CREATE PROCEDURE notificationlisting(targetPlatform varchar(100), messageType varchar(500), validFrom varchar(500), messageText varchar(500), validTill varchar(100), messageFormat varchar(100), selectionCriteria varchar(500), updatedBy varchar(100), updatedDate varchar(100), forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
  SET @resultQuery = ' select notification_id as notificationId, target_platform as targetPlatform, date_format(message_valid_from, ''%d %b %Y'') as validFrom, date_format(message_valid_till, ''%d %b %Y'') as validTill, message_text as messageText,  ';
  SET @resultQuery = CONCAT(@resultQuery, ' message_type as messageType, message_format as messageFormat, selection_criteria as selectionCriteria, updated_by as updatedBy, date_format(updated_date, ''%d %b %Y'') as updatedDate  ');
  SET @fromString  = ' from generic_user_notification ';
  SET @whereString = '';
  SET @dateFormat = '%d %b %Y';
  SET @limitString = CONCAT(' limit ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT targetPlatform IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND target_platform LIKE ''%',targetPlatform,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE target_platform LIKE ''%',targetPlatform,'%''');
    END IF;  
  END IF;
  
  IF NOT messageType IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND message_type LIKE ''%',messageType,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE message_type LIKE ''%',messageType,'%''');
    END IF;  
  END IF;
  
  IF NOT validFrom IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND date_format(message_valid_from,''',@dateFormat,''') LIKE ''%',validFrom,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE date_format(message_valid_from,''',@dateFormat,''') LIKE ''%',validFrom,'%''');
    END IF;  
  END IF;
  
  IF NOT messageText IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND message_text LIKE ''%',messageText,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE message_text LIKE ''%',messageText,'%''');
    END IF;  
  END IF;
  
  IF NOT validTill IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND date_format(message_valid_till,''',@dateFormat,''') LIKE ''%',validTill,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE date_format(message_valid_till,''',@dateFormat,''') LIKE ''%',validTill,'%''');
    END IF;  
  END IF;
  
  IF NOT messageFormat IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND message_format LIKE ''%',messageFormat,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE message_format LIKE ''%',messageFormat,'%''');
    END IF;  
  END IF;
  
  IF NOT selectionCriteria IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND selection_criteria LIKE ''%',selectionCriteria,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE selection_criteria LIKE ''%',selectionCriteria,'%''');
    END IF;  
  END IF;
  
  IF NOT updatedBy IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND updated_by LIKE ''%',updatedBy,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE updated_by LIKE ''%',updatedBy,'%''');
    END IF;  
  END IF;
  
  IF NOT updatedDate IS NULL THEN
    IF  @whereString != '' THEN
      SET @whereString = CONCAT(@whereString,' AND date_format(updated_date,''',@dateFormat,''') LIKE ''%',updatedDate,'%''');
    ELSE
      SET @whereString = CONCAT('WHERE date_format(updated_date,''',@dateFormat,''') LIKE ''%',updatedDate,'%''');
    END IF;  
  END IF;
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY updated_date DESC');
  END IF;
  
	IF forCount=1 THEN
  	SET @queryString=CONCAT('select count(*) from ( ',@resultQuery, @fromString, @whereString, @orderBy,' ) as cnt');
  ELSE
  	SET @queryString=CONCAT(@resultQuery, @fromString, @whereString, @orderBy, @limitString);
  END IF;
  
  

 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
END;

REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id) VALUES ("notificationDetailsListing", 'Notification Details Listing', 'Notification Details Listing', 'notificationlisting', 'targetPlatform,messageType,validFrom,messageText,validTill,messageFormat,selectionCriteria,updatedBy,updatedDate', 2);


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