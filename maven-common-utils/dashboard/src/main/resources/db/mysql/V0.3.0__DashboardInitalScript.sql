DROP TABLE IF EXISTS `context_master`;
CREATE TABLE `context_master` (
  `context_id` varchar(50) NOT NULL,
  `context_description` varchar(500) DEFAULT NULL,
  `allow_dashboard_addition` int(11) DEFAULT NULL,
  `created_by` varchar(20) DEFAULT 'ADMIN',
  `created_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`context_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `dashboard`;
CREATE TABLE `dashboard` (
  `dashboard_id` varchar(50) NOT NULL,
  `dashboard_name` varchar(50) NOT NULL,
  `context_id` varchar(50) NOT NULL,
  `dashboard_type` varchar(50) NOT NULL,
  `created_by` varchar(100) NOT NULL,
  `created_date` date DEFAULT NULL,
  `last_updated_date` date DEFAULT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `is_draggable` int(11) NOT NULL DEFAULT 1,
  `is_exportable` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`dashboard_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dashlet`;
CREATE TABLE `dashlet` (
  `dashlet_id` varchar(50) NOT NULL,
  `dashlet_name` varchar(50) DEFAULT NULL,
  `dashlet_title` varchar(100) NOT NULL,
  `x_coordinate` int(11) DEFAULT NULL,
  `y_coordinate` int(11) DEFAULT NULL,
  `dashlet_width` int(11) DEFAULT NULL,
  `dashlet_height` int(11) DEFAULT NULL,
  `context_id` varchar(50) NOT NULL,
  `show_header` int(11) NOT NULL DEFAULT 1,
  `dashlet_query` text NOT NULL,
  `dashlet_body` longtext NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `is_active` int(1) NOT NULL DEFAULT 1,
  `dashlet_query_checksum` varchar(512) DEFAULT NULL,
  `dashlet_body_checksum` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`dashlet_id`),
  KEY `FK1_dashlet_context_id` (`context_id`),
  CONSTRAINT `FK1_dashlet_context_id` FOREIGN KEY (`context_id`) REFERENCES `context_master` (`context_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dashboard_dashlet_association`;
CREATE TABLE `dashboard_dashlet_association` (
  `dashboard_id` varchar(50) NOT NULL,
  `dashlet_id` varchar(50) NOT NULL,
  PRIMARY KEY (`dashboard_id`,`dashlet_id`),
  KEY `dashlet_id` (`dashlet_id`),
  CONSTRAINT `dashboard_dashlet_association_ibfk_1` FOREIGN KEY (`dashboard_id`) REFERENCES `dashboard` (`dashboard_id`),
  CONSTRAINT `dashboard_dashlet_association_ibfk_2` FOREIGN KEY (`dashlet_id`) REFERENCES `dashlet` (`dashlet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dashboard_lookup_category`;
CREATE TABLE `dashboard_lookup_category` (
  `lookup_category_id` varchar(50) NOT NULL,
  `lookup_category` varchar(50) NOT NULL,
  `lookup_description` varchar(100) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `updated_date` date DEFAULT NULL,
  PRIMARY KEY (`lookup_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dashboard_role_association`;
CREATE TABLE `dashboard_role_association` (
  `dashboard_id` varchar(50) NOT NULL,
  `role_id` varchar(50) NOT NULL,
  PRIMARY KEY (`dashboard_id`,`role_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `dashboard_role_association_ibfk_1` FOREIGN KEY (`dashboard_id`) REFERENCES `dashboard` (`dashboard_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dashboard_shared_to_association`;
CREATE TABLE `dashboard_shared_to_association` (
  `user_id` varchar(100) NOT NULL,
  `dashboard_id` varchar(50) NOT NULL,
  `dashboard_permission_type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`dashboard_id`),
  KEY `dashboard_shared_to_association_ibfk_1` (`dashboard_id`),
  CONSTRAINT `dashboard_shared_to_association_ibfk_1` FOREIGN KEY (`dashboard_id`) REFERENCES `dashboard` (`dashboard_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dashlet_configuration`;
CREATE TABLE `dashlet_configuration` (
  `user_id` varchar(100) NOT NULL,
  `dashlet_id` varchar(50) NOT NULL,
  `x_coordinate` int(11) DEFAULT NULL,
  `y_coordinate` int(11) DEFAULT NULL,
  `dashboard_id` varchar(50) NOT NULL,
  PRIMARY KEY (`user_id`,`dashlet_id`,`dashboard_id`),
  KEY `dashlet_id` (`dashlet_id`),
  CONSTRAINT `dashlet_configuration_ibfk_1` FOREIGN KEY (`dashlet_id`) REFERENCES `dashlet` (`dashlet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dashlet_properties`;
CREATE TABLE `dashlet_properties` (
  `property_id` varchar(100) NOT NULL,
  `dashlet_id` varchar(50) DEFAULT NULL,
  `placeholder_name` varchar(20) NOT NULL,
  `display_name` varchar(50) NOT NULL,
  `type_id` varchar(50) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `default_value` varchar(50) DEFAULT NULL,
  `configuration_script` varchar(1000) DEFAULT NULL,
  `is_deleted` int(11) DEFAULT 0,
  `to_display` int(11) DEFAULT NULL,
  `sequence` int(11) NOT NULL,
  PRIMARY KEY (`property_id`),
  KEY `fk_dashlet_id` (`dashlet_id`),
  CONSTRAINT `dashlet_properties_ibfk_1` FOREIGN KEY (`dashlet_id`) REFERENCES `dashlet` (`dashlet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dashlet_property_configuration`;
CREATE TABLE `dashlet_property_configuration` (
  `user_id` varchar(100) NOT NULL,
  `property_id` varchar(100) NOT NULL,
  `property_value` varchar(50) DEFAULT NULL,
  `dashboard_id` varchar(50) NOT NULL,
  PRIMARY KEY (`user_id`,`property_id`,`dashboard_id`),
  KEY `property_id` (`property_id`),
  KEY `dashlet_property_configuration_ibfk_2` (`dashboard_id`),
  CONSTRAINT `dashlet_property_configuration_ibfk_1` FOREIGN KEY (`property_id`) REFERENCES `dashlet_properties` (`property_id`),
  CONSTRAINT `dashlet_property_configuration_ibfk_2` FOREIGN KEY (`dashboard_id`) REFERENCES `dashboard` (`dashboard_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dashlet_role_association`;
CREATE TABLE `dashlet_role_association` (
  `dashlet_id` varchar(50) NOT NULL,
  `role_id` varchar(100) NOT NULL,
  PRIMARY KEY (`dashlet_id`,`role_id`),
  CONSTRAINT `dashlet_role_association_ibfk_1` FOREIGN KEY (`dashlet_id`) REFERENCES `dashlet` (`dashlet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE dashboard ADD UNIQUE INDEX (dashboard_name);
ALTER TABLE dashlet ADD UNIQUE INDEX (dashlet_name);

INSERT INTO context_master(context_id ,context_description  ,allow_dashboard_addition  ,created_by  ,created_date) VALUES ('a0bb79ce-eadd-11ea-a036-e454e805e22f', 'jws'  ,0  ,'admin'  ,NOW());

INSERT into dashboard (dashboard_id, dashboard_name, context_id, dashboard_type, created_by, created_date, last_updated_date, is_deleted, is_draggable, is_exportable) VALUES
('ab7202bf-eadd-11ea-a036-e454e805e22f', 'Java Stater Usages', (SELECT context_id FROM context_master where context_description='jws'), '1', 'admin', NOW(), NOW(), 0, 1, 0);

INSERT INTO dashlet (dashlet_id, dashlet_title, dashlet_name, dashlet_body, dashlet_query, is_active, created_by, created_date, updated_by, updated_date, x_coordinate, y_coordinate, dashlet_width, dashlet_height, context_id, show_header,dashlet_query_checksum,dashlet_body_checksum) VALUES
('09b78b43-eade-11ea-a036-e454e805e22f', 'Grids', 'Grids', '<div>Total of <#list resultSet as queryOutput>${queryOutput.gridCount}</#list> used in application</div>', 'SELECT COUNT(gd.grid_id) AS gridCount FROM grid_details AS gd', 1, 'admin', NOW(), 'admin', NOW(), 0, 0, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1,null,null), 
('0eb8adc4-eade-11ea-a036-e454e805e22f', 'Notification', 'Notification', '<div> Notifications </div>', 'select 1', 1, 'admin', NOW(), 'admin', NOW(), 0, 3, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1,null,null), 
('31c9ffa9-eadf-11ea-a036-e454e805e22f', 'Templates', 'Templates', '<div>Total of <#list resultSet as queryOutput>${queryOutput.templateCount}</#list> used in application</div>', 'SELECT COUNT(tm.template_id) AS templateCount FROM template_master AS tm', 1, 'admin', NOW(), 'admin', NOW(), 6, 3, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1,null,null), 
('37dbbc8d-eadf-11ea-a036-e454e805e22f', 'DB resource bundles', 'DB resource bundles', '<div>Total of <#list resultSet as queryOutput>${queryOutput.resourceBundleCount}</#list> used in application</div>', 'SELECT COUNT(DISTINCT(rb.resource_key)) AS resourceBundleCount FROM resource_bundle AS rb', 1, 'admin', NOW(), 'admin', NOW(), 0, 6, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1,null,null), 
('3d97273b-eadf-11ea-a036-e454e805e22f', 'Dashboards', 'Dashboards', '<div>Total of <#list resultSet as queryOutput>${queryOutput.dashboardCount}</#list> used in application</div>', 'SELECT COUNT(db.dashboard_id) AS dashboardCount FROM  dashboard AS db', 1, 'admin', NOW(), 'admin', NOW(), 6, 3, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1,null,null),
('44cb330d-eadf-11ea-a036-e454e805e22f', 'Dashlets', 'Dashlets', '<head>

<script src="/webjars/1.0/pqGrid/pqgrid.min.js"></script>     
<link rel="stylesheet" href="/webjars/1.0/pqGrid/pqgrid.min.css" />
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
<script src="/webjars/1.0/gridutils/gridutils.js"></script>
</head>

<div class="container">
		
		<div id="divdDashletMasterGrid"></div>

		<div id="snackbar"></div>
</div>


<form action="${(contextPath)!''''}/cf/aedl" method="POST" id="formDMRedirect">
	<input type="hidden" id="dashletId" name="dashlet-id">
</form>
<script>
	$(function () {
		var colM = [
			{ title: "${messageSource.getMessage(''jws.dashletName'')}", width: 130, dataIndx: "dashletName" , align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.dashletTitle'')}", width: 130, dataIndx: "dashletTitle", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.createdBy'')}", width: 100, dataIndx: "createdBy" , align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.createdDate'')}", width: 100, dataIndx: "createdDate", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.updatedBy'')}", width: 100, dataIndx: "updatedBy" , align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.updatedDate'')}", width: 100, dataIndx: "updatedDate" , align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.status'')}", width: 160, dataIndx: "status" , align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.action'')}", width: 50, dataIndx: "action", align: "center", halign: "center", render: editDashlet}
		];
	
		var grid = $("#divdDashletMasterGrid").grid({
			gridId: "dashletMasterListingGrid",
			colModel: colM
		});
	});
	function editDashlet(uiObject) {
		const dashletId = uiObject.rowData.dashletId;
		return ''<span id="''+dashletId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil"  title="${messageSource.getMessage("jws.editDashlet")}"></i></span>''.toString();
	}
	
	function submitForm(element) {
	  $("#dashletId").val(element.id);
	  $("#formDMRedirect").submit();
	}
	
	function backToDashboarListing() {
		location.href = contextPath+"/cf/dbm";
	}
</script>', 'SELECT dashlet_id AS dashletId, dashlet_title AS dashletTitle,dashlet_name AS dashletName,DATE_FORMAT(created_date,"%d %b %Y") AS createdDate,DATE_FORMAT(updated_date,"%d %b %Y") AS updatedDate, updated_by AS updatedBy, created_by AS createdBy,is_active AS status FROM dashlet', 1, 'admin', NOW(), 'admin', NOW(), 6, 0, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1,null,null);

INSERT INTO dashboard_dashlet_association (dashboard_id, dashlet_id) VALUES ('ab7202bf-eadd-11ea-a036-e454e805e22f','09b78b43-eade-11ea-a036-e454e805e22f');
INSERT INTO dashboard_dashlet_association (dashboard_id, dashlet_id) VALUES ('ab7202bf-eadd-11ea-a036-e454e805e22f','0eb8adc4-eade-11ea-a036-e454e805e22f');
INSERT INTO dashboard_dashlet_association (dashboard_id, dashlet_id) VALUES ('ab7202bf-eadd-11ea-a036-e454e805e22f','31c9ffa9-eadf-11ea-a036-e454e805e22f');
INSERT INTO dashboard_dashlet_association (dashboard_id, dashlet_id) VALUES ('ab7202bf-eadd-11ea-a036-e454e805e22f','37dbbc8d-eadf-11ea-a036-e454e805e22f');
INSERT INTO dashboard_dashlet_association (dashboard_id, dashlet_id) VALUES ('ab7202bf-eadd-11ea-a036-e454e805e22f','3d97273b-eadf-11ea-a036-e454e805e22f');
INSERT INTO dashboard_dashlet_association (dashboard_id, dashlet_id) VALUES ('ab7202bf-eadd-11ea-a036-e454e805e22f','44cb330d-eadf-11ea-a036-e454e805e22f');

INSERT INTO dashlet_role_association (dashlet_id, role_id) VALUES ('09b78b43-eade-11ea-a036-e454e805e22f', 'ADMIN');
INSERT INTO dashlet_role_association (dashlet_id, role_id) VALUES ('0eb8adc4-eade-11ea-a036-e454e805e22f', 'ADMIN');
INSERT INTO dashlet_role_association (dashlet_id, role_id) VALUES ('31c9ffa9-eadf-11ea-a036-e454e805e22f', 'ADMIN');
INSERT INTO dashlet_role_association (dashlet_id, role_id) VALUES ('37dbbc8d-eadf-11ea-a036-e454e805e22f', 'ADMIN');
INSERT INTO dashlet_role_association (dashlet_id, role_id) VALUES ('3d97273b-eadf-11ea-a036-e454e805e22f', 'ADMIN');
INSERT INTO dashlet_role_association (dashlet_id, role_id) VALUES ('44cb330d-eadf-11ea-a036-e454e805e22f', 'ADMIN');

INSERT INTO dashboard_lookup_category (lookup_category_id, lookup_category, lookup_description, updated_by, updated_date) VALUES
('368732c8-1e8b-11e8-8d69-000d3a173cc5', 'COMPONENT_TYPE', 'select', 'admin', NOW()), 
('3687356c-1e8b-11e8-8d69-000d3a173cc5', 'COMPONENT_TYPE', 'text', 'admin', NOW()), 
('368741f2-1e8b-11e8-8d69-000d3a173cc5', 'COMPONENT_TYPE', 'rangeslider', 'admin', NOW()), 
('36874643-1e8b-11e8-8d69-000d3a173cc5', 'COMPONENT_TYPE', 'number', 'admin', NOW()), 
('36874738-1e8b-11e8-8d69-000d3a173cc5', 'COMPONENT_TYPE', 'decimal', 'admin', NOW()), 
('368747b0-1e8b-11e8-8d69-000d3a173cc5', 'COMPONENT_TYPE', 'datepicker', 'admin', NOW()), 
('36874811-1e8b-11e8-8d69-000d3a173cc5', 'COMPONENT_TYPE', 'checkbox', 'admin', NOW()), 
('c1f03803-c862-11e7-a62a-f48e38ab9229', 'COMPONENT_TYPE', 'monthpicker', 'admin', NOW());
