SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum,template_type_id) VALUES
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
', 'satishchandra.pandey@trigyn.com', 'satishchandra.pandey@trigyn.com', NOW(), NULL,2);



SET FOREIGN_KEY_CHECKS=1;
