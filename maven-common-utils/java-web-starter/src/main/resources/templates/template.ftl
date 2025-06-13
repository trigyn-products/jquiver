<#macro templateWithoutParams templateName>${dynamicTemplate.includeTemplate(templateName)}</#macro>
<#macro templateWithParams templateName templateParams>${dynamicTemplate.includeTemplate(templateName, templateParams)}</#macro>
<#macro resourceBundle resourceKey>${messageSource.getMessage(resourceKey)}</#macro>
<#macro resourceBundleWithDefault resourceKey defaultValue>
	${messageSource.getMessageWithDefault(resourceKey, defaultValue)}
</#macro>
<#macro validateRegex regexPattern fieldValue fieldName dataType>
  <#if (fieldValue?? && fieldValue?has_content) && !dynamicTemplate.validateRegex(regexPattern, fieldValue, fieldName, dataType)>
    <#assign hasError = true>    
  </#if>
</#macro>
<#function getSystemProperty keyName>    
	<#list systemProperties as key, value>  
		<#if key.propertyName == keyName>  
			<#return value>  
		</#if>  
	</#list>
</#function>