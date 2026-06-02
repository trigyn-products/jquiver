DROP PROCEDURE IF EXISTS gridDetails;

CREATE  OR REPLACE VIEW `jq_grid_details_view` AS
SELECT 
  `jgd`.`grid_id` AS `gridId`, 
  `jgd`.`grid_name` AS `gridName`, 
  `jgd`.`grid_description` AS `gridDesc`, 
  `jgd`.`grid_table_name` AS `gridTableName`, 
  `jgd`.`grid_column_names` AS `gridColumnName`, 
  `jgd`.`grid_type_id` AS `gridTypeId`, 
  COALESCE(
    CONCAT(
      `jus`.`first_name`, ' ', `jus`.`last_name`
    ), 
    COALESCE(
      `jgd`.`last_updated_by`, `jgd`.`created_by`
    )
  ) AS `lastUpdatedBy`, 
  `jgd`.`last_updated_ts` AS `lastUpdatedTs`, 
  COUNT(`jmv`.`version_id`) AS `revisionCount`, 
  MAX(`jmv`.`version_id`) AS `max_version_id` ,
  DATE(`jgd`.`last_updated_ts`) AS `isAfterDate`,
  `jgd`.`datasource_id` AS `datasourceId`
FROM 
  (
    (
      `jq_grid_details` `jgd` 
      LEFT JOIN `jq_user` `jus` ON(
        `jus`.`email` = COALESCE(
          `jgd`.`last_updated_by`, `jgd`.`created_by`
        )
      )
    ) 
    LEFT JOIN `jq_module_version` `jmv` ON(
      `jmv`.`entity_id` = `jgd`.`grid_id` 
      AND `jmv`.`entity_name` = 'jq_grid_details'
    )
  ) 
GROUP BY 
  `jgd`.`grid_id` 
ORDER BY 
  `jgd`.`grid_id` DESC;
  
DROP PROCEDURE IF EXISTS autocompleteListing; 
 
CREATE  OR REPLACE VIEW `jq_autocomplete_listing_view` AS
 SELECT 
  `jau`.`ac_id` AS `autocompleteId`, 
  `jau`.`ac_description` AS `autocompleteDescription`, 
  `jau`.`ac_select_query` AS `acQuery`, 
  COALESCE(
    CONCAT(
      `jus`.`first_name`, ' ', `jus`.`last_name`
    ), 
    COALESCE(
      `jau`.`last_updated_by`, `jau`.`created_by`
    )
  ) AS `lastUpdatedBy`,  
  `jau`.`last_updated_ts` AS `lastUpdatedTs`, 
  `jau`.`ac_type_id` AS `autocompleteTypeId`, 
  COUNT(`jmv`.`version_id`) AS `revisionCount`, 
  MAX(`jmv`.`version_id`) AS `max_version_id`,
  DATE(`jau`.`last_updated_ts`) AS `isAfterDate`,
  `jau`.`datasource_id` AS `datasourceId`
FROM 
  (
    (
      `jq_autocomplete_details` `jau` 
      LEFT JOIN `jq_user` `jus` ON(
        `jus`.`email` = COALESCE(
          `jau`.`last_updated_by`, `jau`.`created_by`
        )
      )
    ) 
    LEFT JOIN `jq_module_version` `jmv` ON(
      `jmv`.`entity_id` = `jau`.`ac_id` 
      AND `jmv`.`entity_name` = 'jq_autocomplete_details'
    )
  ) 
GROUP BY 
  `jau`.`ac_id` 
ORDER BY 
  `jau`.`last_updated_ts` DESC;
  
DROP PROCEDURE IF EXISTS fileUploadConfigListing;

CREATE  OR REPLACE VIEW `jq_file_upload_config_listing_view` AS
  SELECT 
  `fuc`.`file_bin_id` AS `fileBinId`, 
  `fuc`.`file_type_supported` AS `fileTypeSupported`, 
  `fuc`.`max_file_size` AS `maxFileSize`, 
  `fuc`.`no_of_files` AS `noOfFiles`, 
  `fuc`.`last_updated_ts` AS `lastUpdatedTs`,
  COALESCE(
    CONCAT(
      `jus`.`first_name`, ' ', `jus`.`last_name`
    ), 
    COALESCE(
      `fuc`.`last_updated_by`, `fuc`.`created_by`
    )
  ) AS `lastUpdatedBy`, 
  `fuc`.`is_deleted` AS `isDeleted`,  
   DATE(`fuc`.`last_updated_ts`) AS `isAfterDate`,
   `fuc`.`datasource_upload_validator` AS `uploadDataSourceId`,
   `fuc`.`datasource_view_validator` AS `viewDataSourceId`,
   `fuc`.`datasource_delete_validator` AS `deleteDataSourceId`
FROM 
  (
    `jq_file_upload_config` `fuc` 
    LEFT JOIN `jq_user` `jus` ON(
      `jus`.`email` = COALESCE(
        `fuc`.`last_updated_by`, `fuc`.`created_by`
      )
    )
  ) 
WHERE 
  `fuc`.`is_deleted` = 0 
ORDER BY 
  `fuc`.`last_updated_ts` DESC;  

DROP PROCEDURE IF EXISTS dynamicFormListing;  

CREATE  OR REPLACE VIEW `jq_dynamic_form_listing_view` AS
SELECT 
  `df`.`form_id` AS `formId`, 
  `df`.`form_description` AS `formDescription`, 
  `df`.`form_name` AS `formName`, 
  `df`.`created_by` AS `createdBy`, 
  `df`.`form_type_id` AS `formTypeId`, 
  COUNT(`jmv`.`version_id`) AS `revisionCount`, 
  `df`.`created_date` AS `createdDate`, 
 COALESCE(`df`.`last_updated_ts`,`df`.`created_date`) AS `lastUpdatedTs`, 
  COALESCE(
    CONCAT(
      `jus`.`first_name`, ' ', `jus`.`last_name`
    ), 
    COALESCE(
      `df`.`last_updated_by`, `df`.`created_by`
    )
  ) AS `lastUpdatedBy`,
  DATE(`df`.`last_updated_ts`) AS `isAfterDate`,
  `df`.`datasource_id` AS `datasourceid`,
  GROUP_CONCAT(DISTINCT `dfsq`.`datasource_id` SEPARATOR ', ') AS `daodatasourceid`
FROM 
  (
    (
      `jq_dynamic_form` `df` 
      LEFT JOIN `jq_user` `jus` ON(
        `jus`.`email` = COALESCE(
          `df`.`last_updated_by`, `df`.`created_by`
        )
      )
    ) 
    LEFT JOIN `jq_module_version` `jmv` ON(
      `jmv`.`entity_id` = `df`.`form_id` 
      AND `jmv`.`entity_name` = 'jq_dynamic_form'
    )
  ) 
  LEFT JOIN `jq_dynamic_form_save_queries` `dfsq` ON(
	`dfsq`.`dynamic_form_id` = `df`.`form_id`)
GROUP BY 
  `df`.`form_id` 
ORDER BY 
  `df`.`last_updated_ts` DESC;
  
DROP PROCEDURE IF EXISTS dynarestListing;   

CREATE  OR REPLACE VIEW `jq_dynarest_api_listing_view` AS
SELECT 
  jdrd.jws_dynamic_rest_id AS dynarestId, 
  jdrd.jws_dynamic_rest_url AS dynarestUrl, 
  jdrd.jws_rbac_id AS rbacId, 
  jdrd.jws_method_name AS methodName, 
  jdrd.jws_method_description AS methodDescription, 
  jdrd.jws_request_type_id AS requestTypeId, 
  jdrd.jws_response_producer_type_id AS producerTypeId, 
  jdrd.jws_service_logic AS serviceLogic, 
  jdrd.jws_platform_id AS platformId, 
  jdrd.jws_allow_files AS allowFiles, 
  jdrd.jws_dynamic_rest_type_id AS dynarestTypeId, 
  jdrd.last_updated_ts AS lastUpdatedTs, 
  COALESCE(
    CONCAT(
      jus.first_name, " ", jus.last_name
    ), 
    COALESCE(
      jdrd.last_updated_by, jdrd.created_by
    )
  ) AS lastUpdatedBy, 
  COUNT(jmv.version_id) AS revisionCount, 
  MAX(jmv.version_id) AS max_version_id ,
  Date(jdrd.last_updated_ts) AS `isAfterDate`,
  GROUP_CONCAT(DISTINCT `jdrdd`.`datasource_id` SEPARATOR ', ') AS `datasourceid`
  FROM 
  (((`jq_dynamic_rest_details` `jdrd` LEFT JOIN `jq_user` `jus` 
  ON (`jus`.`email` = COALESCE(`jdrd`.`last_updated_by`,`jdrd`.`created_by`))) 
  LEFT JOIN `jq_module_version` `jmv` 
  ON(`jmv`.`entity_id` = `jdrd`.`jws_dynamic_rest_id`)) 
  LEFT JOIN `jq_dynamic_rest_dao_details` `jdrdd` 
  ON(`jdrdd`.`jws_dynamic_rest_details_id` = `jdrd`.`jws_dynamic_rest_id`))
GROUP BY 
  dynarestId 
ORDER BY 
  lastUpdatedTs ASC;

DROP PROCEDURE IF EXISTS moduleListing;

CREATE  OR REPLACE VIEW `jq_module_listing_view` AS

SELECT 
  ml.module_id AS moduleId, 
  COALESCE(
    mli18n.module_name, mli18n2.module_name
  ) AS moduleName, 
  ml.module_url AS moduleURL, 
  (
    SELECT 
      module_name 
    FROM 
      `jq_module_listing_i18n` 
    WHERE 
      module_id = ml.parent_id
  ) AS parentModuleName, 
  ml.sequence AS sequence, 
  ml.is_inside_menu AS isInsideMenu, 
  ml.is_home_page AS isHomePage, 
  ml.module_type_id AS moduleTypeId, 
  COALESCE(
    CONCAT(
      jus.first_name, " ", jus.last_name
    ), 
    ml.last_updated_by
  ) AS lastUpdatedBy, 
  ml.last_modified_date AS lastUpdatedTs, 
  ml.target_lookup_id AS targetLookupId, 
  ml.request_param_json AS requestParamJson, 
  ml.target_type_id AS targetTypeId ,
  Date(ml.last_modified_date) AS `isAfterDate`
FROM 
  jq_module_listing AS ml 
  LEFT JOIN jq_user AS jus ON jus.email = ml.last_updated_by 
  LEFT OUTER JOIN jq_module_listing AS mlParent ON ml.parent_id = mlParent.module_id 
  LEFT OUTER JOIN jq_module_listing_i18n AS mli18n ON ml.module_id = mli18n.module_id 
  AND mli18n.language_id = 1 
  LEFT OUTER JOIN jq_module_listing_i18n AS mli18n2 ON ml.module_id = mli18n2.module_id 
  AND mli18n2.language_id = 1 
  LEFT OUTER JOIN jq_module_listing_i18n AS mli18nParent ON mlParent.module_id = mli18nParent.module_id 
  AND mli18nParent.language_id = 1 
  LEFT OUTER JOIN jq_module_listing_i18n AS mli18nParent2 ON mlParent.module_id = mli18nParent2.module_id 
  AND mli18nParent2.language_id = 1 
WHERE 
  (
    ml.is_home_page IN (0, 1) 
    OR ml.is_home_page IS NULL
  ) 
ORDER BY 
  ml.last_modified_date DESC;
  
DELETE FROM `jq_response_producer_details` WHERE jws_response_producer_type_id = '10';  

DROP PROCEDURE IF EXISTS templateListing;

CREATE  OR REPLACE VIEW `jq_template_listing_view` AS

SELECT 
  tm.template_id AS templateId, 
  tm.template_name AS templateName, 
  tm.template AS template, 
  tm.created_by AS createdBy, 
  COALESCE(
    CONCAT(
      jus.first_name, " ", jus.last_name
    ), 
    tm.updated_by, 
    tm.created_by
  ) AS updatedBy, 
  tm.template_type_id AS templateTypeId, 
  COUNT(jmv.version_id) AS revisionCount, 
  MAX(jmv.version_id) AS max_version_id, 
  tm.updated_date AS updatedDate ,
  DATE(`tm`.`updated_date`)     AS `isAfterDate`
FROM 
  jq_template_master AS tm 
  LEFT JOIN jq_user AS jus ON jus.email = COALESCE(tm.updated_by, tm.created_by)
  LEFT OUTER JOIN jq_module_version AS jmv ON jmv.entity_id = tm.template_id 
  AND jmv.entity_name = 'jq_template_master' 
GROUP BY 
  tm.template_id 
ORDER BY 
  tm.updated_date DESC;


CREATE OR REPLACE VIEW `jq_customResourceBundleListingView` AS
    SELECT 
        `t`.`resourceKey` AS `resourceKey`,
        `t`.`languageName` AS `languageName`,
        `t`.`resourceBundleText` AS `resourceBundleText`,
        `t`.`revisionCount` AS `revisionCount`,
        `t`.`max_version_id` AS `max_version_id`,
        `t`.`updatedBy` AS `updatedBy`,
        `t`.`updatedDate` AS `updatedDate`,
        `t`.`resource_type` AS `resource_type`, 
        `t`.`isAfterDate` AS `isAfterDate` 
    FROM
        ((SELECT 
            `rb`.`resource_key` AS `resourceKey`,
                `lang`.`language_name` AS `languageName`,
                `rb`.`text` AS `resourceBundleText`,
                COALESCE(CONCAT(`jus`.`first_name`,' ',`jus`.`last_name`),COALESCE(`rb`.`updated_by`,`rb`.`created_by`)) AS `updatedBy`,
                COALESCE(`rb`.`updated_date`, `rb`.`created_date`) AS `updatedDate`,
                COUNT(`jmv`.`version_id`) AS `revisionCount`,
                MAX(`jmv`.`version_id`) AS `max_version_id`,
                1 AS resource_type,
                DATE(COALESCE(`rb`.`updated_date`, `rb`.`created_date`)) AS `isAfterDate`
        FROM
            ((`jq_resource_bundle` `rb`
        JOIN `jq_language` `lang` ON (`lang`.`language_id` = `rb`.`language_id`))
		LEFT JOIN `jq_user` `jus` ON(`jus`.`email` = COALESCE(`rb`.`updated_by`,`rb`.`created_by`))
        LEFT JOIN `jq_module_version` `jmv` ON (`jmv`.`entity_id` = `rb`.`resource_key`
            AND `jmv`.`entity_name` = 'jq_resource_bundle'))
        WHERE
            `rb`.`resource_key` NOT LIKE 'jws.%'
        GROUP BY `jmv`.`entity_id`) UNION (SELECT 
            `rb`.`resource_key` AS `resourceKey`,
                `lang`.`language_name` AS `languageName`,
                `rb`.`text` AS `resourceBundleText`,
                COALESCE(CONCAT(`jus`.`first_name`,' ',`jus`.`last_name`),COALESCE(`rb`.`updated_by`,`rb`.`created_by`)) AS `updatedBy`,
                COALESCE(`rb`.`updated_date`, `rb`.`created_date`) AS `updatedDate`,
                COUNT(`jmv`.`version_id`) AS `revisionCount`,
                MAX(`jmv`.`version_id`) AS `max_version_id`,
                2 AS resource_type,
                DATE(COALESCE(`rb`.`updated_date`, `rb`.`created_date`)) AS `isAfterDate`
        FROM
            ((`jq_resource_bundle` `rb`
        JOIN `jq_language` `lang` ON (`lang`.`language_id` = `rb`.`language_id`))
		LEFT JOIN `jq_user` `jus` ON(`jus`.`email` = COALESCE(`rb`.`updated_by`,`rb`.`created_by`))
        LEFT JOIN `jq_module_version` `jmv` ON (`jmv`.`entity_id` = `rb`.`resource_key`
            AND `jmv`.`entity_name` = 'jq_resource_bundle'))
        WHERE
            `rb`.`resource_key` LIKE 'jws.%'
        GROUP BY `jmv`.`entity_id`)) `t`;

      
      
 CREATE OR REPLACE VIEW `jq_export_resource_budle_view` AS
    SELECT 
        `t`.`resourceKey` AS `resourceKey`,
        `t`.`languageName` AS `languageName`,
        `t`.`languageId` AS `languageId`,
        `t`.`resourceBundleText` AS `resourceBundleText`,
        `t`.`resource_type` AS `resource_type`,
        `t`.`updatedBy` AS `updatedBy`,
        `t`.`updatedDate` AS `updatedDate`
    FROM
        ((SELECT 
            `rb`.`resource_key` AS `resourceKey`,
                `lang`.`language_name` AS `languageName`,
                `lang`.`language_id` AS `languageId`,
                `rb`.`text` AS `resourceBundleText`,
                1 AS `resource_type`,
                COALESCE(CONCAT(`jus`.`first_name`,' ',`jus`.`last_name`),COALESCE(`rb`.`updated_by`,`rb`.`created_by`)) AS `updatedBy`,
                COALESCE(`rb`.`updated_date`, `rb`.`created_date`) AS `updatedDate`
        FROM
            ((`jq_resource_bundle` `rb`
        JOIN `jq_language` `lang` ON (`lang`.`language_id` = `rb`.`language_id`))
		LEFT JOIN `jq_user` `jus` ON(`jus`.`email` = COALESCE(`rb`.`updated_by`,`rb`.`created_by`))
        LEFT JOIN `jq_module_version` `jmv` ON (`jmv`.`entity_id` = `rb`.`resource_key`
            AND `jmv`.`entity_name` = 'jq_resource_bundle'))
        WHERE
            `rb`.`resource_key` NOT LIKE 'jws.%') UNION (SELECT 
            `rb`.`resource_key` AS `resourceKey`,
                `lang`.`language_name` AS `languageName`,
                `lang`.`language_id` AS `languageId`,
                `rb`.`text` AS `resourceBundleText`,
                2 AS `resource_type`,
                COALESCE(CONCAT(`jus`.`first_name`,' ',`jus`.`last_name`),COALESCE(`rb`.`updated_by`,`rb`.`created_by`)) AS `updatedBy`,
                COALESCE(`rb`.`updated_date`, `rb`.`created_date`) AS `updatedDate`
        FROM
            ((`jq_resource_bundle` `rb`
        JOIN `jq_language` `lang` ON (`lang`.`language_id` = `rb`.`language_id`))
		LEFT JOIN `jq_user` `jus` ON(`jus`.`email` = COALESCE(`rb`.`updated_by`,`rb`.`created_by`))
        LEFT JOIN `jq_module_version` `jmv` ON (`jmv`.`entity_id` = `rb`.`resource_key`
            AND `jmv`.`entity_name` = 'jq_resource_bundle'))
        WHERE
            `rb`.`resource_key` LIKE 'jws.%')) `t`;
            
 DROP PROCEDURE IF EXISTS `propertyMasterListing`; 
CREATE OR REPLACE VIEW `jq_property_master_listing_view` AS
SELECT
  `jpm`.`owner_type`         AS `ownerType`,
  `jpm`.`owner_id`           AS `ownerId`,
  `jpm`.`property_name`      AS `propertyName`,
  `jpm`.`property_value`     AS `propertyValue`,
  `jpm`.`last_modified_date` AS `lastModifiedDate`,
  CAST(`jpm`.`last_modified_date` AS DATE) AS `isAfterDate`,
  
  `jpm`.`app_version`        AS `appVersion`,
  `jpm`.`comments`           AS `comments`,
  `jpm`.`property_master_id` AS `propertyMasterId`,
  COUNT(`jmv`.`version_id`)  AS `revisionCount`,
  MAX(`jmv`.`version_id`)    AS `max_version_id`,
  COALESCE(CONCAT(`jus`.`first_name`, ' ', `jus`.`last_name` ),`jpm`.`modified_by`) AS `modifiedBy` 
FROM (`jq_property_master` `jpm`  LEFT JOIN `jq_user` `jus` ON(
        `jus`.`email` = `jpm`.`modified_by`)
   LEFT JOIN `jq_module_version` `jmv`
     ON (`jmv`.`entity_id` = `jpm`.`property_master_id`
         AND `jmv`.`entity_name` = 'jq_property_master'))
WHERE `jpm`.`is_deleted` = 0
    AND `jpm`.`property_name` NOT IN('acl-jws','jws-date-format')
GROUP BY `jpm`.`property_master_id`
ORDER BY `jpm`.`last_modified_date` DESC;

CREATE  OR REPLACE VIEW `jq_scriptlib_view` AS
SELECT `sl`.`script_lib_id`
       AS `script_lib_id`,
       `tm`.`template_name`
       AS `template_id`,
       `sl`.`library_name`
       AS `library_name`,
       `sl`.`description`
       AS `description`,
       `sl`.`script_type`
       AS `script_type`,
       `sl`.`created_by`
       AS
`created_by`,
COALESCE(CONCAT(`jus`.`first_name`, ' ', `jus`.`last_name`), `sl`.`updated_by`, `sl`.`created_by`) AS `updated_by`,
`sl`.`updated_date`
       AS `updated_date`,
COUNT(`jmv`.`version_id`)
       AS `revisionCount`,
 MAX(`jmv`.`version_id`)                                  AS
       `max_version_id`,
CAST(`sl`.`updated_date` AS DATE) AS `isAfterDate`
FROM   (((`jq_script_lib_details` `sl`
          JOIN `jq_template_master` `tm`
            ON( `sl`.`template_id` = `tm`.`template_id` ))
         LEFT JOIN `jq_module_version` `jmv`
                ON( `jmv`.`entity_id` = `sl`.`script_lib_id`
                    AND `jmv`.`entity_name` = 'jq_script_lib_details' ))
        LEFT JOIN `jq_user` `jus`
               ON( `jus`.`user_id` =
                   COALESCE(`sl`.`updated_by`, `sl`.`created_by`) ))
GROUP  BY `sl`.`script_lib_id`
ORDER  BY `sl`.`updated_date` DESC; 

CREATE  OR REPLACE VIEW `jq_help_manual_view` AS
SELECT `jmt`.`manual_id` AS `manual_id`,
`jmt`.`name` AS `name`,
`jmt`.`is_system_manual` AS `is_system_manual`,
`jmt`.`created_by` AS `created_by`,
`jmt`.`created_date` AS `created_date`,
`jmt`.`last_updated_ts` AS `last_updated_ts` ,
COALESCE(CONCAT(`jus`.`first_name`,' ',`jus`.`last_name`),COALESCE(`jmt`.`last_updated_by`,`jmt`.`created_by`)) AS `last_updated_by`,
COUNT(`jme`.`manual_id`) AS totalCount
FROM (`jq_manual_type` `jmt` 
LEFT JOIN `jq_user` `jus` ON(`jus`.`email` = COALESCE(`jmt`.`last_updated_by`,`jmt`.`created_by`))
LEFT JOIN `jq_manual_entry` `jme` ON(`jme`.`manual_id` = `jmt`.`manual_id` ))
GROUP BY jmt.manual_id;


CREATE  OR REPLACE VIEW `jq_help_manual_view` AS
SELECT `jmt`.`manual_id` AS `manual_id`,
`jmt`.`name` AS `name`,
`jmt`.`is_system_manual` AS `is_system_manual`,
`jmt`.`created_by` AS `created_by`,
`jmt`.`created_date` AS `created_date`,
`jmt`.`last_updated_ts` AS `last_updated_ts` ,
COALESCE(CONCAT(`jus`.`first_name`,' ',`jus`.`last_name`),COALESCE(`jmt`.`last_updated_by`,`jmt`.`created_by`)) AS `last_updated_by`,
COUNT(`jme`.`manual_id`) AS totalCount
FROM (`jq_manual_type` `jmt` 
LEFT JOIN `jq_user` `jus` ON(`jus`.`email` = COALESCE(`jmt`.`last_updated_by`,`jmt`.`created_by`))
LEFT JOIN `jq_manual_entry` `jme` ON(`jme`.`manual_id` = `jmt`.`manual_id` ))
GROUP BY jmt.manual_id
ORDER BY last_updated_ts DESC ;



CREATE OR REPLACE view `jq_custom_module_listing_view`
AS
  SELECT `m`.`module_id`      AS `moduleid`,
         `m`.`module_name`    AS `moduleName`,
         `d`.`dashboard_id`   AS `entityid`,
         `d`.`dashboard_name` AS `entityname`,
         'jq_dashboard'       AS `TABLE_NAME`
  FROM   (`jq_dashboard` `d`
          JOIN `jq_master_modules` `m`
            ON( `m`.`module_name` = 'Dashboard' ))
  WHERE  `d`.`dashboard_type` = 1
  UNION ALL
  SELECT `m`.`module_id`        AS `moduleid`,
         `m`.`module_name`      AS `moduleName`,
         `me`.`manual_entry_id` AS `entityid`,
         `me`.`entry_name`      AS `entityname`,
         'jq_manual_entry'      AS `TABLE_NAME`
  FROM   ((`jq_manual_entry` `me`
           JOIN `jq_manual_type` `mt`
             ON( `mt`.`manual_id` = `me`.`manual_id` ))
          JOIN `jq_master_modules` `m`
            ON( `m`.`module_name` = 'Help Manual' ))
  WHERE  `mt`.`is_system_manual` = 1
  UNION ALL
  SELECT `m`.`module_id`      AS `moduleid`,
         `m`.`module_name`    AS `moduleName`,
         `t`.`template_id`    AS `entityid`,
         `t`.`template_name`  AS `entityname`,
         'jq_template_master' AS `TABLE_NAME`
  FROM   (`jq_template_master` `t`
          JOIN `jq_master_modules` `m`
            ON( `m`.`module_name` = 'Templating' ))
  WHERE  `t`.`template_type_id` = 1
  UNION ALL
  SELECT `m`.`module_id`   AS `moduleid`,
         `m`.`module_name` AS `moduleName`,
         `g`.`grid_id`     AS `entityid`,
         `g`.`grid_name`   AS `entityname`,
         'jq_grid_details' AS `TABLE_NAME`
  FROM   (`jq_grid_details` `g`
          JOIN `jq_master_modules` `m`
            ON( `m`.`module_name` = 'Grid Utils' ))
  WHERE  `g`.`grid_type_id` = 1
  UNION ALL
  SELECT `m`.`module_id`         AS `moduleid`,
         `m`.`module_name`       AS `moduleName`,
         `s`.`script_lib_id`     AS `entityid`,
         `s`.`library_name`      AS `entityname`,
         'jq_script_lib_details' AS `TABLE_NAME`
  FROM   (`jq_script_lib_details` `s`
          JOIN `jq_master_modules` `m`
            ON( `m`.`module_name` = 'Script Library' ))
  WHERE  `s`.`script_type` = 1
  UNION ALL
  SELECT `m`.`module_id`           AS `moduleid`,
         `m`.`module_name`         AS `moduleName`,
         `a`.`ac_id`               AS `entityid`,
         `a`.`ac_id`               AS `entityname`,
         'jq_autocomplete_details' AS `TABLE_NAME`
  FROM   (`jq_autocomplete_details` `a`
          JOIN `jq_master_modules` `m`
            ON( `m`.`module_name` = 'TypeAhead Autocomplete' ))
  WHERE  `a`.`ac_type_id` = 1
  UNION ALL
  SELECT `m`.`module_id`             AS `moduleid`,
         `m`.`module_name`           AS `moduleName`,
         `dr`.`jws_dynamic_rest_id`  AS `entityid`,
         `dr`.`jws_dynamic_rest_url` AS `entityname`,
         'jq_dynamic_rest_details'   AS `TABLE_NAME`
  FROM   (`jq_dynamic_rest_details` `dr`
          JOIN `jq_master_modules` `m`
            ON( `m`.`module_name` = 'REST API' ))
  WHERE  `dr`.`jws_dynamic_rest_type_id` = 1
  UNION ALL
  SELECT `m`.`module_id`         AS `moduleid`,
         `m`.`module_name`       AS `moduleName`,
         `fuc`.`file_bin_id`     AS `entityid`,
         `fuc`.`file_bin_id`     AS `entityname`,
         'jq_file_upload_config' AS `TABLE_NAME`
  FROM   (`jq_file_upload_config` `fuc`
          JOIN `jq_master_modules` `m`
            ON( `m`.`module_name` = 'File Bin' ))
  UNION ALL
  SELECT `m`.`module_id`    AS `moduleid`,
         `m`.`module_name`  AS `moduleName`,
         `fio`.`form_io_id` AS `entityid`,
         `fio`.`form_name`  AS `entityname`,
         'jq_form_io'       AS `TABLE_NAME`
  FROM   (`jq_form_io` `fio`
          JOIN `jq_master_modules` `m`
            ON( `m`.`module_name` = 'Form IO' ))
  WHERE  `fio`.`form_io_type` = 1
  UNION ALL
  SELECT `m`.`module_id`     AS `moduleid`,
         `m`.`module_name`   AS `moduleName`,
         `ml`.`module_id`    AS `entityid`,
         `ml`.`module_url`   AS `entityname`,
         'jq_module_listing' AS `TABLE_NAME`
  FROM   (`jq_module_listing` `ml`
          JOIN `jq_master_modules` `m`
            ON( `m`.`module_name` = 'Router' ))
  WHERE  `ml`.`module_type_id` = 1
  UNION ALL
  SELECT `m`.`module_id`           AS `moduleid`,
         `m`.`module_name`         AS `moduleName`,
         `ml`.`file_upload_id`     AS `entityid`,
         `ml`.`original_file_name` AS `entityname`,
         'jq_file_upload'          AS `TABLE_NAME`
  FROM   (`jq_file_upload` `ml`
          JOIN `jq_master_modules` `m`
            ON( `m`.`module_name` = 'Files' ))
  UNION ALL
  SELECT `m`.`module_id`      AS `moduleid`,
         `m`.`module_name`    AS `modulename`,
         `rb`.`resource_key`  AS `entityid`,
         `rb`.`resource_key`  AS `entityname`,
         'jq_resource_bundle' AS `table_name`
  FROM   (`jq_resource_bundle` `rb`
          JOIN `jq_master_modules` `m`
            ON ( `m`.`module_name` = 'Internalization' )) GROUP BY `rb`.`resource_key`
  UNION ALL
  SELECT `m`.`module_id`                AS `moduleid`,
         `m`.`module_name`              AS `modulename`,
         `gun`.`notification_id`        AS `entityid`,
         `gun`.`notification_id`        AS `entityname`,
         'jq_generic_user_notification' AS `table_name`
  FROM   (`jq_generic_user_notification` `gun`
          JOIN `jq_master_modules` `m`
            ON ( `m`.`module_name` = 'Notification' ))
  UNION ALL
  SELECT `m`.`module_id`                 AS `moduleid`,
         `m`.`module_name`               AS `modulename`,
         `ad`.`additional_datasource_id` AS `entityid`,
         `ad`.`datasource_name`          AS `entityname`,
         'jq_additional_datasource'      AS `table_name`
  FROM   (`jq_additional_datasource` `ad`
          JOIN `jq_master_modules` `m`
            ON ( `m`.`module_name` = 'Additional Datasource' ))
  UNION ALL
  SELECT `m`.`module_id`      AS `moduleid`,
         `m`.`module_name`    AS `modulename`,
         `s`.`scheduler_id`   AS `entityid`,
         `s`.`scheduler_name` AS `entityname`,
         'jq_job_scheduler'   AS `table_name`
  FROM   (`jq_job_scheduler` `s`
          JOIN `jq_master_modules` `m`
            ON ( `m`.`module_name` = 'Scheduler' ))
  WHERE  `s`.`scheduler_type_id` = 1
  UNION ALL
  SELECT `m`.`module_id`    AS `moduleid`,
         `m`.`module_name`  AS `moduleName`,
         `d`.`dashlet_id`   AS `entityid`,
         `d`.`dashlet_name` AS `entityname`,
         'jq_dashlet'       AS `TABLE_NAME`
  FROM   (`jq_dashlet` `d`
          JOIN `jq_master_modules` `m`
            ON( `m`.`module_name` = 'Dashlet' ))
  WHERE  `d`.`dashlet_type_id` = 1
  UNION ALL
  SELECT `m`.`module_id`   AS `moduleid`,
         `m`.`module_name` AS `moduleName`,
         `d`.`form_id`     AS `entityid`,
         `d`.`form_name`   AS `entityname`,
         'jq_dynamic_form' AS `TABLE_NAME`
  FROM   (`jq_dynamic_form` `d`
          JOIN `jq_master_modules` `m`
            ON( `m`.`module_name` = 'Form Builder' ))
  WHERE  `d`.`form_type_id` = 1
  UNION ALL
  SELECT `m`.`module_id`      AS `moduleid`,
         `m`.`module_name`    AS `moduleName`,
         `d`.`property_master_id`   AS `entityid`,
         `d`.`property_name` AS `entityname`,
         'jq_property_master'       AS `TABLE_NAME`
  FROM   (`jq_property_master` `d`
          JOIN `jq_master_modules` `m`
            ON( `m`.`module_name` = 'Application Configuration' ))
  UNION ALL
  SELECT `m`.`module_id`      AS `moduleid`,
         `m`.`module_name`    AS `moduleName`,
         `d`.`client_id`   AS `entityid`,
         `d`.`client_name` AS `entityname`,
         'jq_api_client_details'       AS `TABLE_NAME`
  FROM   (`jq_api_client_details` `d`
          JOIN `jq_master_modules` `m`
            ON( `m`.`module_name` = 'API Clients' ));
            
            
CREATE OR REPLACE VIEW jq_entities_details_view AS
-- TEMPLATE
	SELECT
	    t.template_id AS id,
	    'Template' AS module_name,
	    m.module_id AS master_module_id,
	    CASE 
	        WHEN t.template_type_id = 2 THEN 'system'
	        ELSE 'custom'
	    END AS entity_type,
	    t.template_id AS entity_id,
	    t.template_name AS entity_name,
	    
	    JSON_OBJECT(
	        'id', t.template_id,
	            'savedQueries', JSON_ARRAY(
	                JSON_OBJECT(
	                    'name', CONCAT('html', '.html')
	                )
	            )
	    ) AS entity_info,
	
	    NULL AS created_date,
	    t.created_by,
	    t.updated_date,
	    t.updated_by
	
	FROM jq_template_master t
	LEFT JOIN jq_master_modules m 
	    ON m.module_name = 'Templating'
	
	UNION ALL
	
	-- Dynamic Form
	SELECT
	    f.form_id AS id,
	    'DynamicForm' AS module_name,
	    m.module_id AS master_module_id,
	    CASE 
	        WHEN f.form_type_id = 2 THEN 'system'
	        ELSE 'custom'
	    END AS entity_type,
	    f.form_id AS entity_id,
	    f.form_name AS entity_name,
	
	    JSON_OBJECT(
	        'id', f.form_id,
	        'selectQuery', CONCAT(
	            'form_select_query-formSelectQuery',
	            CASE f.query_type
	                WHEN 1 THEN '.sql'
	                WHEN 2 THEN '.js'
	                WHEN 3 THEN '.py'
	                WHEN 4 THEN '.php'
	                ELSE '.txt'
	            END
	        ),
	        'html', CONCAT('form_body-formBody', '.html'),
	            'savedQueries', (
	                SELECT JSON_ARRAYAGG(
	                    JSON_OBJECT(
	                        'id', q.dynamic_form_query_id,
	                        'innerQuery', '1',
	                        'variableName', CONCAT(q.sequence,q.result_variable_name, CASE q.dao_query_type
	                            WHEN 1 THEN '.sql'
	                            WHEN 2 THEN '.js'
	                            WHEN 3 THEN '.py'
	                            WHEN 4 THEN '.php'
	                            ELSE '.txt'
	                        END)
	                    )
	                )
	                FROM jq_dynamic_form_save_queries q
	                WHERE q.dynamic_form_id = f.form_id
	            )
	    ) AS entity_info,
	
	    f.created_date,
	    f.created_by,
	    f.last_updated_ts AS updated_date,
	    f.last_updated_by AS updated_by
	
	FROM jq_dynamic_form f
	LEFT JOIN jq_master_modules m 
	    ON m.module_name = 'Form Builder'
	
	UNION ALL
	-- Rest API
	SELECT
	    r.jws_dynamic_rest_id AS id,
	    'RestAPI' AS module_name,
	    m.module_id AS master_module_id,
	    CASE 
	        WHEN r.jws_dynamic_rest_type_id= 2 THEN 'system'
	        ELSE 'custom'
	    END AS entity_type,
	    r.jws_dynamic_rest_id AS entity_id,
	    r.jws_dynamic_rest_url AS entity_name,
	
	    JSON_OBJECT(
	        'id', r.jws_dynamic_rest_id,
	        'name', CONCAT(
	            'serviceLogic',
	            CASE r.jws_platform_id 
	                WHEN 1 THEN '.java'
	                WHEN 2 THEN '.ftl'
	                WHEN 3 THEN '.js'
	                WHEN 4 THEN '.py'
	                WHEN 5 THEN '.php'
	                ELSE '.txt'
	            END
	        ),
	        'savedQueries', (
	                SELECT JSON_ARRAYAGG(
	                    JSON_OBJECT(
	                        'id', d.jws_dao_details_id,
	                        'innerQuery', '1',
	                        'variableName', CONCAT(d.jws_query_sequence,d.jws_result_variable_name, CASE d.jws_dao_query_type
	                            WHEN 1 THEN '.sql'
	                            WHEN 2 THEN '.sql'
	                            WHEN 3 THEN '.sql'
	                            WHEN 4 THEN '.json'
	                            ELSE '.txt'
	                        END)
	                    )
	                )
	                FROM jq_dynamic_rest_dao_details d
	                WHERE d.jws_dynamic_rest_details_id = r.jws_dynamic_rest_id
	            )
	    ) AS entity_info,
	
	    r.created_date,
	    r.created_by,
	    r.last_updated_ts AS updated_date,
	    r.last_updated_by AS updated_by
	
	FROM jq_dynamic_rest_details r
	LEFT JOIN jq_master_modules m 
	    ON m.module_name = 'REST API'
	
	UNION ALL
	
	-- Dashboard
	
	SELECT
	    d.dashboard_id AS id,
	    'Dashboard' AS module_name,
	    m.module_id AS master_module_id,
	    CASE 
	        WHEN d.dashboard_type = 2 THEN 'system'
	        ELSE 'custom'
	    END AS entity_type,
	    d.dashboard_id AS entity_id,
	    d.dashboard_name AS entity_name,
	
	    JSON_OBJECT(
	        'id', d.dashboard_id,
			'html', CONCAT('dashboard_body-formBody', '.html')
	    ) AS entity_info,
	
	    d.created_date,
	    d.created_by,
	    d.last_updated_ts AS updated_date,
	    NULL AS updated_by
	
	FROM jq_dashboard d
	LEFT JOIN jq_master_modules m 
	    ON m.module_name = 'Dashboard'
	
	UNION ALL
	
	-- Dashlet
	
	SELECT
	    d.dashlet_id AS id,
	    'Dashlet' AS module_name,
	    m.module_id AS master_module_id,
	    CASE 
	        WHEN d.dashlet_type_id = 2 THEN 'system'
	        ELSE 'custom'
	    END AS entity_type,
	    d.dashlet_id AS entity_id,
	    d.dashlet_name AS entity_name,
	
	    JSON_OBJECT(
	        'id', d.dashlet_id,
			'selectQuery', CONCAT(
	            'dashlet_query-formSelectQuery',
	            CASE d.dao_query_type
	                WHEN 1 THEN '.sql'
	                WHEN 2 THEN '.js'
	                WHEN 3 THEN '.py'
	                WHEN 4 THEN '.php'
	                ELSE '.txt'
	            END
	        ),
			'html', CONCAT('dashlet_body-formBody', '.html')
	    ) AS entity_info,
	
	    d.created_date,
	    d.created_by,
	    d.last_updated_ts AS updated_date,
	    d.updated_by
	
	FROM jq_dashlet d
	LEFT JOIN jq_master_modules m 
	    ON m.module_name = 'Dashlet'
	
	UNION ALL
	
	-- File Bin
	SELECT
	    f.file_bin_id AS id,
	    'FileBin' AS module_name,
	    m.module_id AS master_module_id,
	    CASE 
	        WHEN f.is_custom_updated = 0 THEN 'system'
	        ELSE 'custom'
	    END AS entity_type,
	    f.file_bin_id AS entity_id,
	    f.file_bin_id AS entity_name,
	
	    JSON_OBJECT(
	    'id', f.file_bin_id,
	    'savedQueries', JSON_ARRAY(
	        
	        JSON_OBJECT(
	            'id', f.file_bin_id,
	            'innerQuery', '1',
	            'variableName', CONCAT(
	                'upload_query_content-uploadQuery',
	                CASE f.upload_query_type
	                    WHEN 1 THEN '.sql'
	                    WHEN 2 THEN '.py'
	                    WHEN 3 THEN '.php'
	                    WHEN 4 THEN '.js'
	                    ELSE '.txt'
	                END
	            )
	        ),
	
	        JSON_OBJECT(
	            'id', f.file_bin_id,
	            'innerQuery', '1',
	            'variableName', CONCAT(
	                'view_query_content-viewQuery',
	                CASE f.view_query_type
	                    WHEN 1 THEN '.sql'
	                    WHEN 2 THEN '.py'
	                    WHEN 3 THEN '.php'
	                    WHEN 4 THEN '.js'
	                    ELSE '.txt'
	                END
	            )
	        ),
	
	        JSON_OBJECT(
	            'id', f.file_bin_id,
	            'innerQuery', '1',
	            'variableName', CONCAT(
	                'delete_query_content-deleteQuery',
	                CASE f.delete_query_type
	                    WHEN 1 THEN '.sql'
	                    WHEN 2 THEN '.py'
	                    WHEN 3 THEN '.php'
	                    WHEN 4 THEN '.js'
	                    ELSE '.txt'
	                END
	            )
	        )
	
	    )
	) AS entity_info,
	
	    f.created_date,
	    f.created_by,
	    f.last_updated_ts AS updated_date,
	    f.last_updated_by AS updated_by
	
	FROM jq_file_upload_config f
	LEFT JOIN jq_master_modules m 
	    ON m.module_name = 'File Bin';         