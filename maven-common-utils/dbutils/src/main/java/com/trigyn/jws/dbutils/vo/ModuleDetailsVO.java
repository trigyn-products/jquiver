package com.trigyn.jws.dbutils.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.util.LinkedCaseInsensitiveMap;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ModuleDetailsVO implements Serializable {

	private static final long	serialVersionUID	= -671156919183570354L;

	private String				moduleId			= null;
	private String				moduleName			= null;
	private String				moduleUrl			= null;
	private String				parentModuleId		= null;
	private String				parentModuleName	= null;
	private Integer				sequence			= null;
	private Integer				isInsideMenu		= null;
	private Integer				includeLayout		= null;
	private Integer				targetLookupId		= null;
	private String				targetLookupDesc	= null;
	private String				targetLookupName	= null;
	private String				targetTypeId		= null;
	private Long				subModuleCount		= null;
	private String				headerJson			= null;
	private List<String>		roleIdList			= null;
	private String				requestParamJson	= null;
	private String				externalURL			= null;
	private Integer				openInNewTab		= null;
	private String				menuStyle			= null;
	private Integer				isHomePage			= null;//Added for isHomePage

	public ModuleDetailsVO() {

	}

	public ModuleDetailsVO(String moduleId, String moduleName, String moduleUrl, String parentModuleId, String parentModuleName,
			Integer sequence, Integer isInsideMenu, Integer includeLayout, Integer targetLookupId, String targetLookupDesc,
			String targetTypeId, String headerJson, String requestParamJson, Integer openInNewTab, String menuStyle, Integer isHomePage) {
		this.moduleId			= moduleId;
		this.moduleName			= moduleName;
		this.moduleUrl			= moduleUrl;
		this.parentModuleId		= parentModuleId;
		this.parentModuleName	= parentModuleName;
		this.sequence			= sequence;
		this.isInsideMenu		= isInsideMenu;
		this.includeLayout		= includeLayout;
		this.targetLookupId		= targetLookupId;
		this.targetLookupDesc	= targetLookupDesc;
		this.targetTypeId		= targetTypeId;
		this.headerJson			= headerJson;
		this.requestParamJson 	= requestParamJson;
		this.openInNewTab 		= openInNewTab;
		this.menuStyle 			= menuStyle;
		this.isHomePage = isHomePage;
		
	}

	public ModuleDetailsVO(String moduleId, String moduleName, String moduleUrl) {
		this.moduleId	= moduleId;
		this.moduleName	= moduleName;
		this.moduleUrl	= moduleUrl;
	}

	public ModuleDetailsVO(String moduleId, String moduleName, String moduleUrl, String parentModuleId, String parentModuleName,
			Integer sequence, Long subModuleCount) {
		this.moduleId			= moduleId;
		this.moduleName			= moduleName;
		this.moduleUrl			= moduleUrl;
		this.parentModuleId		= parentModuleId;
		this.parentModuleName	= parentModuleName;
		this.sequence			= sequence;
		this.subModuleCount		= subModuleCount;
	}

	public ModuleDetailsVO(String moduleUrl, Integer targetLookupId, String targetTypeId, Integer includeLayout, String headerJson, String requestParamJson) {
		this.moduleUrl			= moduleUrl;
		this.targetLookupId		= targetLookupId;
		this.targetTypeId		= targetTypeId;
		this.includeLayout		= includeLayout;
		this.headerJson			= headerJson;
		this.requestParamJson 	= requestParamJson;
	}
	
	public ModuleDetailsVO(String moduleId, String moduleName, String moduleUrl, String parentModuleId, String parentModuleName,
			Integer sequence, Long subModuleCount, String targetTypeId, Integer targetLookupId, String requestParamJson, Integer openInNewTab, String menuStyle,Integer isHomePage) {
		this.moduleId			= moduleId;
		this.moduleName			= moduleName;
		this.moduleUrl			= moduleUrl;
		this.parentModuleId		= parentModuleId;
		this.parentModuleName	= parentModuleName;
		this.sequence			= sequence;
		this.subModuleCount		= subModuleCount;
		this.targetTypeId		= targetTypeId;
		this.targetLookupId		= targetLookupId;
		this.requestParamJson 	= requestParamJson;
		this.openInNewTab		= openInNewTab;
		this.menuStyle 			= menuStyle;
		this.isHomePage = isHomePage;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleUrl() {
		return moduleUrl;
	}

	public void setModuleUrl(String moduleUrl) {
		this.moduleUrl = moduleUrl;
	}

	public String getParentModuleId() {
		return parentModuleId;
	}

	public void setParentId(String parentModuleId) {
		this.parentModuleId = parentModuleId;
	}

	public String getParentModuleName() {
		return parentModuleName;
	}

	public void setParentModuleName(String parentModuleName) {
		this.parentModuleName = parentModuleName;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getIsInsideMenu() {
		return isInsideMenu;
	}

	public void setIsInsideMenu(Integer isInsideMenu) {
		this.isInsideMenu = isInsideMenu;
	}

	public Integer getIncludeLayout() {
		return includeLayout;
	}

	public void setIncludeLayout(Integer includeLayout) {
		this.includeLayout = includeLayout;
	}

	public Integer getTargetLookupId() {
		return targetLookupId;
	}

	public void setTargetLookupId(Integer targetLookupId) {
		this.targetLookupId = targetLookupId;
	}

	public String getTargetLookupDesc() {
		return targetLookupDesc;
	}

	public void setTargetLookupDesc(String targetLookupDesc) {
		this.targetLookupDesc = targetLookupDesc;
	}

	public String getTargetTypeId() {
		return targetTypeId;
	}

	public void setTargetTypeId(String targetTypeId) {
		this.targetTypeId = targetTypeId;
	}

	public Long getSubModuleCount() {
		return subModuleCount;
	}

	public void setSubModuleCount(Long subModuleCount) {
		this.subModuleCount = subModuleCount;
	}

	/**
	 * @return the roleIdList
	 */
	public List<String> getRoleIdList() {
		return roleIdList;
	}

	/**
	 * @param roleIdList the roleIdList to set
	 */
	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public String getTargetLookupName() {
		return targetLookupName;
	}

	public void setTargetLookupName(String targetLookupName) {
		this.targetLookupName = targetLookupName;
	}

	public void setParentModuleId(String parentModuleId) {
		this.parentModuleId = parentModuleId;
	}

	public String getHeaderJson() {
		try {
			Map<String, String>	modifiedHeaderData	= new LinkedCaseInsensitiveMap<>();
			Map<String, String>	savedHeaderData		= new LinkedCaseInsensitiveMap<>();
			ObjectMapper		objectMapper		= new ObjectMapper();
			if (this.headerJson != null && this.headerJson.isEmpty() == false) {
				savedHeaderData = objectMapper.readValue(this.headerJson, Map.class);
				if (savedHeaderData.containsKey("Powered-By") == false) {
					modifiedHeaderData.put("Powered-By", "JQuiver");
				}
				if (savedHeaderData.containsKey("Content-Type") == false) {
					modifiedHeaderData.put("Content-Type", "text/html; charset=UTF-8");
				}

				if (savedHeaderData.containsKey("Content-Language") == false) {
					modifiedHeaderData.put("Content-Language", "en_US");
				}

			} else {
				modifiedHeaderData.put("Powered-By", "JQuiver");
				modifiedHeaderData.put("Content-Type", "text/html; charset=UTF-8");
				modifiedHeaderData.put("Content-Language", "en_US");
			}
			if (savedHeaderData!=null && savedHeaderData.size() > 0) {
				for (Map.Entry<String, String> entry : savedHeaderData.entrySet()) {
					if (entry.getKey().equalsIgnoreCase("Content-Type")) {
						modifiedHeaderData.put("Content-Type", entry.getValue());
					}else {
						modifiedHeaderData.put(entry.getKey(), entry.getValue());
					}
				}
			}
			objectMapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			headerJson = objectMapper.writeValueAsString(modifiedHeaderData);
		} catch (JsonProcessingException exception) {
			exception.printStackTrace();
		}
		return headerJson;
	}

	public void setHeaderJson(String headerJson) {
		this.headerJson = headerJson;
	}

	public String getRequestParamJson() {
		return requestParamJson;
	}

	public void setRequestParamJson(String requestParamJson) {
		this.requestParamJson = requestParamJson;
	}
	
	public String getExternalURL() {
		return externalURL;
	}

	public void setExternalURL(String externalURL) {
		this.externalURL = externalURL;
	}

	@Override
	public int hashCode() {
		return Objects.hash(headerJson, includeLayout, isInsideMenu, moduleId, moduleName, moduleUrl, parentModuleId, parentModuleName,
				roleIdList, sequence, subModuleCount, targetLookupDesc, targetLookupId, targetLookupName, targetTypeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ModuleDetailsVO other = (ModuleDetailsVO) obj;
		return Objects.equals(headerJson, other.headerJson) && Objects.equals(includeLayout, other.includeLayout)
				&& Objects.equals(isInsideMenu, other.isInsideMenu) && Objects.equals(moduleId, other.moduleId)
				&& Objects.equals(moduleName, other.moduleName) && Objects.equals(moduleUrl, other.moduleUrl)
				&& Objects.equals(parentModuleId, other.parentModuleId) && Objects.equals(parentModuleName, other.parentModuleName)
				&& Objects.equals(roleIdList, other.roleIdList) && Objects.equals(sequence, other.sequence)
				&& Objects.equals(subModuleCount, other.subModuleCount) && Objects.equals(targetLookupDesc, other.targetLookupDesc)
				&& Objects.equals(targetLookupId, other.targetLookupId) && Objects.equals(targetLookupName, other.targetLookupName)
				&& Objects.equals(targetTypeId, other.targetTypeId);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModuleDetailsVO [moduleId=").append(moduleId).append(", moduleName=").append(moduleName).append(", moduleUrl=")
				.append(moduleUrl).append(", parentModuleId=").append(parentModuleId).append(", parentModuleName=").append(parentModuleName)
				.append(", sequence=").append(sequence).append(", isInsideMenu=").append(isInsideMenu).append(", includeLayout=")
				.append(includeLayout).append(", targetLookupId=").append(targetLookupId).append(", targetLookupDesc=")
				.append(targetLookupDesc).append(", targetLookupName=").append(targetLookupName).append(", targetTypeId=")
				.append(targetTypeId).append(", subModuleCount=").append(subModuleCount).append(", headerJson=").append(headerJson)
				.append(", roleIdList=").append(roleIdList).append("]");
		return builder.toString();
	}

	public Integer getOpenInNewTab() {
		return openInNewTab;
	}

	public void setOpenInNewTab(Integer openInNewTab) {
		this.openInNewTab = openInNewTab;
	}

	public String getMenuStyle() {
		return menuStyle;
	}

	public void setMenuStyle(String menuStyle) {
		this.menuStyle = menuStyle;
	}
	/** Added for isHomePage*/
	public Integer getIsHomePage() {
		return isHomePage;
	}

	public void setIsHomePage(Integer isHomePage) {
		this.isHomePage = isHomePage;
	}
	/**Ends Here*/
}
