SET FOREIGN_KEY_CHECKS=0;

replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('8a80cb817570ad44017570b0991c0000', 'application-metrics', '<script src="${(contextPath)!''''}/webjars/1.0/application-metrics/appMetrics.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/chartjs/chart.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/importExport/export.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/view/custom-css" /> </head>

<style>

    .traceblock .tab button{
        position:relative;
    }

    .traceblock .cm-boxwrapper{
        display:flex !important;
        border-radius: 10px 0 0 10px !important;
    }

    .traceblock{

    }

    .traceblock h3{
       font-size: 18px;
    color: #005da3;
    }

    .comndivlabel > div {
        display: flex;
    margin-bottom: 20px;
    }
    .comndivlabel label {
    width: 280px !important;
    font-size: 13px !IMPORTANT;
    word-break: break-all;
    white-space: pre-wrap;
    font-weight: 600;
    color: #495057;
    position: relative;
     display: flex;
    margin-bottom: 0;
 }

.comndivlabel span {
    display: flex;
       width: 100%;
    word-break: break-all;
    white-space: pre-wrap;
}

.activesessionblock{
        margin: 0px !important;
    background: #f4f4f4;
    padding: 10px;
    border-radius: 3px;
    font-size: 17px;
}

.activesessionblock span{

}

.activesessionblock span:first-child{

}

.comonbox{
    border: 1px solid #ccc;
    margin: 10px 17px;
    flex: 1;
    padding: 10px !important;
    background:#fff;
    border-radius: 3px;
}

.comonbox h4{
   font-size: 15px;
    margin: -13px -10px 10px !important;
    background: #f4f4f4;
    padding: 10px;
    border-radius: 3px 3px 0 0;
    border-top: 1px solid #ccc;
    color: #005da3;
}

.comonbox label{
    font-size:14px !important;
}

.customacordian button{
        font-size: 16px !important;
    color: #005da3;
}

 </style>   

<div class="pg-app-matrix">
   <div class="container-fluid" style="display:block !important">
      <div class="topband">
         <h2 class="title-cls-name">Application Metrics</h2>
      </div>

         <h4 class="activesessionblock">
         <span>Total Active Sessions </span> 
        <span id="activeSessions"></span>        
        </h4>

      <div class="row">
         <div class="col-6 margin-t-b comonbox">
           <h4>System metrics</h4>
            <div id="sys-metrics-content">
               <div class="">
                  <label>Server Start time  </label> <span id="serverStartTime"></span>
               </div>
               <div class="">
                  <label>CPU cores  </label> <span id="cpuCores"> </span>
               </div>
               <div class="">
                  <label>Up-time  </label> <span id="uptime"> </span>
               </div>
               <div class="">
                  <label>CPU usages  </label> <span id="cpuUsage"> </span>
                  
                  
               </div>
               <div class=""><label>Process CPU Usage  </label> <span id="processCpuUsage"></span></div>
               <div class=""><label>CPU Load Avg  </label> <span id="cpuloadAvg"></span></div>
               

               
            </div>
            
         </div>
         <div class="col-6 margin-t-b comonbox">
            <h4>Class Metrics</h4>
            <div id="class-metrics-content">
                <div class=""><label>Total Classes Loaded  </label> <span id="totalClassLoaded"></span></div>
                <div class=""><label>Classes Loaded  </label> <span id="classLoaded"></span></div>
                <div class=""><label>Classes Unloaded  </label> <span id="classUnloaded"></span></div>

               
               
            </div>
         </div>
         
        
      </div>
      <div class="row">
          
         <div class="col-12">
            <div class="bs-example">
               <div class="accordion" id="accordionExample">
                  <div class="card">
                     <div class="card-header" id="headingOne">
                        <h2 class="mb-0 customacordian">
                           <button type="button" class="btn btn-link collapsed" data-toggle="collapse" data-target="#collapseOne">GC Metrics <i class="fa fa-plus"></i></button>                                    
                        </h2>
                     </div>
                     <div id="collapseOne" class="collapse show" aria-labelledby="headingOne" data-parent="#accordionExample">
                        <div class="card-body">
                           <div id="gc-metrics-content">
                              <canvas id="gc-metrics-chart" width="500" height="200"></canvas>
                           </div>
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </div>
      
    <div class="row">
         <div class="col-6">
            <div class="bs-example">
               <div class="accordion" id="sysEnvironmentAccord">
                  <div class="card">
                     <div class="card-header" id="headingTwo">
                        <h2 class="mb-0 customacordian">
                           <button type="button" class="btn btn-link collapsed" data-toggle="collapse" data-target="#sysEnvironmentDiv">System Environment<i class="fa fa-plus"></i> </button>
                        </h2>
                     </div>
                     <div id="sysEnvironmentDiv" class="collapse show" aria-labelledby="headingTwo" data-parent="#sysEnvironmentDiv">
                        <div class="card-body">
                           <div id="thread-metrics-content">
                            <div id="sys-env-content" class="comndivlabel">
                            </div>
                           </div>
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
         
         <div class="col-6">
            <div class="bs-example">
               <div class="accordion" id="sysPropertiesAccord">
                  <div class="card">
                     <div class="card-header" id="headingTwo">
                        <h2 class="mb-0 customacordian">
                           <button type="button" class="btn btn-link collapsed" data-toggle="collapse" data-target="#sysPropertiesDiv">System Properties<i class="fa fa-plus"></i> </button>
                        </h2>
                     </div>
                     <div id="sysPropertiesDiv" class="collapse show" aria-labelledby="headingTwo" data-parent="#sysPropertiesDiv">
                        <div class="card-body">
                           <div id="thread-metrics-content">
                            <div id="sys-prop-content" class="comndivlabel">
                            </div>
                           </div>
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
         
    </div>
    
      <div class="row">
         <div class="col-6">
            <div class="bs-example">
               <div class="accordion" id="accordionExample1">
                  <div class="card">
                     <div class="card-header" id="headingTwo">
                        <h2 class="mb-0 customacordian">
                           <button type="button" class="btn btn-link collapsed" data-toggle="collapse" data-target="#collapseTwo">Thread Metrics <i class="fa fa-plus"></i> </button>
                        </h2>
                     </div>
                     <div id="collapseTwo" class="collapse show" aria-labelledby="headingTwo" data-parent="#accordionExample">
                        <div class="card-body">
                           <div id="thread-metrics-content" >
                              <canvas id="thread-metrics-chart"  height="150"></canvas>
                           </div>
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
         <div class="col-6">
            <div class="bs-example">
               <div class="accordion" id="accordionExample2">
                  <div class="card">
                     <div class="card-header" id="headingThree">
                        <h2 class="mb-0 customacordian">
                           <button type="button" class="btn btn-link collapsed" data-toggle="collapse" data-target="#collapseThree">Memory Pool Metrics <i class="fa fa-plus"></i> </button>                     
                        </h2>
                     </div>
                     <div id="collapseThree" class="collapse show" aria-labelledby="headingThree" data-parent="#accordionExample">
                        <div class="card-body">
                           <div id="memory-metrics-content">
                              <canvas id="memory-metrics-chart" width="500" height="500"></canvas>
                           </div>
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </div>
      
     

      <div class="row">
          
           <div class="col-12 traceblock">
            <div class="cm-card traceblock">
                <div class="cm-card-header"> <h3>Application HTTP Trace</h3></div>
                <div class="cm-card-body http-content cm-scrollbar"><div id="http-trace-metrics-content"></div></div>
            </div>
           
            
         </div>
      </div>

   </div>
</div>
<script>
    contextPath = "${contextPath}";
    let appMetrics = new ApplicationMetrics();

 $(document).ready(function(){
       // Add minus icon for collapse element which is open by default
       $(".collapse.show").each(function(){
        $(this).prev(".card-header").find(".fa").addClass("fa-minus").removeClass("fa-plus");
       });
       
       // Toggle plus minus icon on show hide of collapse element
       $(".collapse").on("show.bs.collapse", function(){
        $(this).prev(".card-header").find(".fa").removeClass("fa-plus").addClass("fa-minus");
       }).on("hide.bs.collapse", function(){
        $(this).prev(".card-header").find(".fa").removeClass("fa-minus").addClass("fa-plus");
       });
   });
   
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
</script>
', 'admin', 'admin', NOW(), NULL, 2);

replace into jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active) VALUES
('8a80cb817570ad44017570b099650002', '8a80cb817570ad44017570b0991c0000', 'application-metrics', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1), 
('8a80cb817570ad44017570b099650003', '8a80cb817570ad44017570b0991c0000', 'application-metrics', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1), 
('8a80cb817570ad44017570b61ca50009', '8a80cb817570ad44017570b0991c0000', 'application-metrics', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1);

replace into jq_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_dynamic_rest_type_id, created_by, created_date, last_updated_ts) VALUES
('8dd0e053-1955-11eb-a4c1-e454e805e22f', 'application-metrics-details', 1, 'getJvmMetrics', 'Method to get JVM metrics', 2, 7, 'com.trigyn.jws.applicationmetrics.service.ApplicationMetricsService', 1, 0, 2, 'aar.dev@trigyn.com', NOW(), NOW());

replace into jq_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(28, '8dd0e053-1955-11eb-a4c1-e454e805e22f', 'noparam', 'select 1;', 1, 2);

replace into jq_module_listing (module_id, module_url, parent_id, target_lookup_id, target_type_id, sequence, is_inside_menu, module_type_id) VALUES
('8a80cb817570ad44017570b156c40004', 'health', NULL, 5, '8a80cb817570ad44017570b0991c0000', NULL, 0, 2);

replace into jq_module_listing_i18n (module_id, language_id, module_name) VALUES ('8a80cb817570ad44017570b156c40004', 1, 'application-metrics');

replace into jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active) VALUES
('8a80cb817570b939017570baa17f0001', '8dd0e053-1955-11eb-a4c1-e454e805e22f', 'application-metrics-details', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1), 
('8a80cb817570b939017570baa17f0002', '8dd0e053-1955-11eb-a4c1-e454e805e22f', 'application-metrics-details', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1), 
('8a80cb817570b939017570baa1800003', '8dd0e053-1955-11eb-a4c1-e454e805e22f', 'application-metrics-details', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1);

replace into jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active) VALUES
('8a80cb817570b939017570baa17f0001', '8dd0e053-1955-11eb-a4c1-e454e805e22f', 'application-metrics-details', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1), 
('8a80cb817570b939017570baa17f0002', '8dd0e053-1955-11eb-a4c1-e454e805e22f', 'application-metrics-details', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1), 
('8a80cb817570b939017570baa1800003', '8dd0e053-1955-11eb-a4c1-e454e805e22f', 'application-metrics-details', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1);


UPDATE jq_dynamic_rest_details SET
jws_service_logic = 'com.trigyn.jws.applicationmetrics.service.ApplicationMetricsService'
WHERE jws_method_name = 'getJvmMetrics';

SET FOREIGN_KEY_CHECKS=1;