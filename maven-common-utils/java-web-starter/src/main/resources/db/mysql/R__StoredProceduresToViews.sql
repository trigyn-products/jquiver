UPDATE jq_module_listing SET last_updated_by = 'admin@jquiver.io' WHERE module_type_id=2 AND is_custom_updated=0;
UPDATE jq_dynamic_rest_details SET last_updated_by = 'admin@jquiver.io' WHERE jws_dynamic_rest_type_id=2 AND is_custom_updated=0;
UPDATE jq_dynamic_form SET last_updated_by = 'admin@jquiver.io' WHERE form_type_id=2 AND is_custom_updated=0;
UPDATE `jq_file_upload_config` SET last_updated_by = 'admin@jquiver.io' WHERE is_custom_updated=0;
UPDATE `jq_autocomplete_details` SET last_updated_by = 'admin@jquiver.io' WHERE ac_type_id=2 AND is_custom_updated=0;
UPDATE `jq_grid_details` SET last_updated_by = 'admin@jquiver.io' WHERE grid_type_id=2 AND is_custom_updated=0;
UPDATE `jq_template_master` SET updated_by = 'admin@jquiver.io' WHERE  is_custom_updated=0;

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
  MAX(`jmv`.`version_id`) AS `max_version_id` 
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
  MAX(`jmv`.`version_id`) AS `max_version_id` 
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
  `fuc`.`is_deleted` AS `isDeleted` 
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
  `df`.`last_updated_ts` AS `lastUpdatedTs`, 
  COALESCE(
    CONCAT(
      `jus`.`first_name`, ' ', `jus`.`last_name`
    ), 
    COALESCE(
      `df`.`last_updated_by`, `df`.`created_by`
    )
  ) AS `lastUpdatedBy` 
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
  MAX(jmv.version_id) AS max_version_id 
FROM 
  jq_dynamic_rest_details AS jdrd 
  LEFT JOIN jq_user AS jus ON jus.email = COALESCE(
    jdrd.last_updated_by, jdrd.created_by
  ) 
  LEFT OUTER JOIN jq_module_version AS jmv ON jmv.entity_id = jdrd.jws_dynamic_rest_id 
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
  ml.target_type_id AS targetTypeId 
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
  tm.updated_date AS updatedDate 
FROM 
  jq_template_master AS tm 
  LEFT JOIN jq_user AS jus ON jus.email = tm.updated_by 
  LEFT OUTER JOIN jq_module_version AS jmv ON jmv.entity_id = tm.template_id 
  AND jmv.entity_name = 'jq_template_master' 
GROUP BY 
  tm.template_id 
ORDER BY 
  tm.updated_date DESC



