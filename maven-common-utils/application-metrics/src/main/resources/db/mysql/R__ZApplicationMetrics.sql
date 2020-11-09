SET FOREIGN_KEY_CHECKS=0;

replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('8a80cb817570ad44017570b0991c0000', 'application-metrics', '<script src="/webjars/1.0/application-metrics/appMetrics.js"></script>
<script src="/webjars/1.0/chartjs/chart.js"></script>
<div class="">
    <div class="row">
        <div class="col-12 margin-t-b">
            <h3>System metrics</h3>
            <div id="sys-metrics-content" class="row">
                <div class="col-6 float-left">
                    <label>Server Start time</label> : <span id="serverStartTime"></span>
                </div>
                <div class="col-3 float-left">
                    <label>CPU cores</label> : <span id="cpuCores"></span>
                </div>
                <div class="col-3 float-right">
                    <label>Up-time</label> : <span id="uptime"></span>
                </div>
                <div class="col-12">
                    <label>CPU usages</label> : <span id="cpuUsage" style="margin-right: 50px;"></span>
                    <label>Process CPU Usage</label> : <span id="processCpuUsage" style="margin-right: 50px;"></span>
                    <label>CPU Load Avg</label> : <span id="cpuloadAvg" style="margin-right: 50px;"></span>
                </div>
            </div>
        </div>
        <div class="col-3 margin-t-b">
            <h3>Class Metrics</h3>
            <div id="class-metrics-content">
                <label>Total Classes Loaded</label> : <span id="totalClassLoaded"></span>
                <label>Classes Loaded</label> : <span id="classLoaded"></span>
                <label>Classes Unloaded</label> : <span id="classUnloaded"></span>
            </div>
        </div>
        <div class="col-9 margin-t-b">
            <h3>GC Metrics</h3>
            <div id="gc-metrics-content">
                <canvas id="gc-metrics-chart" width="500" height="200"></canvas>
            </div>
        </div>
        <div class="col-6 margin-t-b">
            <h3>Thread Metrics</h3>
            <div id="thread-metrics-content">
                <canvas id="thread-metrics-chart" width="500" height="500"></canvas>
            </div>
        </div>
        <div class="col-6 margin-t-b">
            <h3>Memory Pool Metrics</h3>
            <div id="memory-metrics-content">
                <canvas id="memory-metrics-chart" width="900" height="950"></canvas>
            </div>
        </div>
        <div class="col-12 margin-t-b http-content">
            <h3>Application Http Trace</h3>
            <div id="http-trace-metrics-content"></div>
        </div>
    </div>
</div>

<script>
    let contextPath = "${contextPath}";
    let appMetrics = new ApplicationMetrics();

    setInterval(function(){
        $.ajax({
            type : "GET",
            async: false,
            url : contextPath+"/api/application-metrics-details",
            success : function(data) {
                appMetrics.applicationDetails = data;
            }
        });
    }, 5000);

    $.ajax({
        type : "GET",
        async: false,
        url : contextPath+"/api/application-metrics-details",
        success : function(data) {
            appMetrics.applicationDetails = data;
            appMetrics.initDetails();
        }
    });
</script>', 'admin', 'admin', NOW(), NULL, 1);

replace into jws_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active) VALUES
('8a80cb817570ad44017570b099650002', '8a80cb817570ad44017570b0991c0000', 'application-metrics', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1), 
('8a80cb817570ad44017570b099650003', '8a80cb817570ad44017570b0991c0000', 'application-metrics', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1), 
('8a80cb817570ad44017570b61ca50009', '8a80cb817570ad44017570b0991c0000', 'application-metrics', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1);

replace into jws_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_dynamic_rest_type_id) VALUES
('8dd0e053-1955-11eb-a4c1-e454e805e22f', 'application-metrics-details', 1, 'getJvmMetrics', 'Method to get JVM metrics', 2, 7, 'com.trigyn.jws.applicationmetrics.service.ApplicationMetricsService', 1, 0, 1);

replace into jws_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(28, '8dd0e053-1955-11eb-a4c1-e454e805e22f', 'noparam', 'select 1;', 1, 1);

replace into module_listing (module_id, module_url, parent_id, target_lookup_id, target_type_id, sequence, is_inside_menu) VALUES
('8a80cb817570ad44017570b156c40004', 'health', NULL, 5, '8a80cb817570ad44017570b0991c0000', NULL, 1);

replace into module_listing_i18n (module_id, language_id, module_name) VALUES ('8a80cb817570ad44017570b156c40004', 1, 'application-metrics');

replace into jws_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active) VALUES
('8a80cb817570b939017570baa17f0001', '8dd0e053-1955-11eb-a4c1-e454e805e22f', 'application-metrics-details', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1), 
('8a80cb817570b939017570baa17f0002', '8dd0e053-1955-11eb-a4c1-e454e805e22f', 'application-metrics-details', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1), 
('8a80cb817570b939017570baa1800003', '8dd0e053-1955-11eb-a4c1-e454e805e22f', 'application-metrics-details', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1);

replace into jws_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active) VALUES
('8a80cb817570b939017570baa17f0001', '8dd0e053-1955-11eb-a4c1-e454e805e22f', 'application-metrics-details', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1), 
('8a80cb817570b939017570baa17f0002', '8dd0e053-1955-11eb-a4c1-e454e805e22f', 'application-metrics-details', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1), 
('8a80cb817570b939017570baa1800003', '8dd0e053-1955-11eb-a4c1-e454e805e22f', 'application-metrics-details', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1);

SET FOREIGN_KEY_CHECKS=1;