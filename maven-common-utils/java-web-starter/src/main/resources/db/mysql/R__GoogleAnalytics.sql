

REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum) VALUES
('9378ee23-09fa-11eb-a894-f48e38ab8cdga', 'google-analytics-template', '

<#if innerTemplateObj.enableGoogleAnalytics ?? && innerTemplateObj.googleAnalyticsKey?? 
	&& innerTemplateObj.enableGoogleAnalytics=="true">
					
	<script async src="https://www.googletagmanager.com/gtag/js?id=${innerTemplateObj.googleAnalyticsKey}"></script>
	<script>
	window.dataLayer = window.dataLayer || [];
	function gtag(){dataLayer.push(arguments);}
	gtag("js", new Date());

	gtag("config", "${innerTemplateObj.googleAnalyticsKey}", {
		"page_title" : "Your custom title",
		"page_path": "/${innerTemplateObj.entityType}/${innerTemplateObj.entityName}"
	});

	</script>
</#if>
', 'satishchandra.pandey@trigyn.com', 'satishchandra.pandey@trigyn.com', NOW(), NULL);

REPLACE  INTO  jq_property_master (
  property_master_id
  ,owner_type
  ,owner_id
  ,property_name
  ,property_value
  ,is_deleted
  ,last_modified_date
  ,modified_by
  ,app_version
  ,comments
) VALUES (
   UUID()
  ,'system'
  ,'system'
  ,'enable-google-analytics'
  ,'true'
  ,0
  ,now()
  ,'admin'
  ,'1.000'
  ,'To add google analytics on each screen' 
);


REPLACE  INTO  jq_property_master(
  property_master_id
  ,owner_type
  ,owner_id
  ,property_name
  ,property_value
  ,is_deleted
  ,last_modified_date
  ,modified_by
  ,app_version
  ,comments
) VALUES (
   UUID()
  ,'system'
  ,'system'
  ,'google-analytics-key'
  ,'UA-69071347-3'
  ,0
  ,now()
  ,'admin'
  ,'1.000'
  ,'Google analytics key,that needs to be passed to GA' 
);
