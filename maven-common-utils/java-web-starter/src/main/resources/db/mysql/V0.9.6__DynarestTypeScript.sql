ALTER TABLE jq_dynamic_rest_details ADD COLUMN jws_dynamic_rest_type_id INT(11) DEFAULT 1;


REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('8a80cb8174922d6b01749235bd840000', 'dynarest-class-template-structure', 'import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ${className} {
    
    <#if (dynamicRestDetails)?? >
      <#list dynamicRestDetails as dynarest>
          /**
          * 
          * ${dynarest.getMethodDescription()}
          *
          */
          public Map<String, Object> ${dynarest.getMethodName()}(Map<String, Object> parameters, Map<String, Object> dAOparameters) {
              ${dynarest.getServiceLogic()}
          }
      </#list>
    </#if>
    
}', 'admin', 'admin', NOW(), NULL, 2);