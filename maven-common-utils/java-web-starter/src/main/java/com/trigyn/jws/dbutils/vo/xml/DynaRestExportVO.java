package com.trigyn.jws.dbutils.vo.xml;

import java.util.Date;
import java.util.Map;

public class DynaRestExportVO {

	private String					jwsDynamicRestId			= null;

	private String					jwsDynamicRestUrl			= null;

	private String					jwsMethodDescription		= null;

	private String					jwsMethodName				= null;

	private Integer					jwsPlatformId				= null;

	private Integer					jwsRbacId					= null;

	private String					serviceLogicFileName		= null;

	private Integer					jwsRequestTypeId			= null;

	private Integer					jwsResponseProducerTypeId	= null;

	private Integer					jwsAllowFiles				= null;

	private Integer					jwsDynamicRestTypeId		= 1;

	private String					jwsHeaderJson				= null;

	private Map<Integer, String>	daoDetailsFileNameMap		= null;

	private Map<Integer, String>	daoDetailsVariableNameMap	= null;

	private Map<Integer, Integer>	daoDetailsQueryTypeMap		= null;

	private Map<Integer, String>	daoDetailsDatasourceIdMap	= null;

	private Date					lastUpdatedTs				= null;
	
	private Integer					hideDaoQuery				= null;//Added for new Column in Rest API
	
	private Integer					isSecured					= 0;
	
	private Integer					isCustomUpdated				= 0;

	public DynaRestExportVO() {

	}

	public DynaRestExportVO(String jwsDynamicRestId, String jwsDynamicRestUrl, String jwsMethodDescription,
			String jwsMethodName, Integer jwsPlatformId, Integer jwsRbacId, String serviceLogicFileName,
			Integer jwsRequestTypeId, Integer jwsResponseProducerTypeId, Integer jwsAllowFiles,
			Integer jwsDynamicRestTypeId, String jwsHeaderJson, Map<Integer, String> daoDetailsFileNameMap,
			Map<Integer, String> daoDetailsVariableNameMap, Map<Integer, Integer> daoDetailsQueryTypeMap,
			Map<Integer, String> daoDetailsDatasourceIdMap, Date lastUpdatedTs, Integer hideDaoQuery, Integer isSecured,
			Integer isCustomUpdated) {
		this.jwsDynamicRestId = jwsDynamicRestId;
		this.jwsDynamicRestUrl = jwsDynamicRestUrl;
		this.jwsMethodDescription = jwsMethodDescription;
		this.jwsMethodName = jwsMethodName;
		this.jwsPlatformId = jwsPlatformId;
		this.jwsRbacId = jwsRbacId;
		this.serviceLogicFileName = serviceLogicFileName;
		this.jwsRequestTypeId = jwsRequestTypeId;
		this.jwsResponseProducerTypeId = jwsResponseProducerTypeId;
		this.jwsAllowFiles = jwsAllowFiles;
		this.jwsDynamicRestTypeId = jwsDynamicRestTypeId;
		this.jwsHeaderJson = jwsHeaderJson;
		this.daoDetailsFileNameMap = daoDetailsFileNameMap;
		this.daoDetailsVariableNameMap = daoDetailsVariableNameMap;
		this.daoDetailsQueryTypeMap = daoDetailsQueryTypeMap;
		this.daoDetailsDatasourceIdMap = daoDetailsDatasourceIdMap;
		this.lastUpdatedTs = lastUpdatedTs;
		this.hideDaoQuery = hideDaoQuery;
		this.isSecured = isSecured;
		this.isCustomUpdated = isCustomUpdated;
	}

	public String getJwsDynamicRestId() {
		return jwsDynamicRestId;
	}

	public void setJwsDynamicRestId(String jwsDynamicRestId) {
		this.jwsDynamicRestId = jwsDynamicRestId;
	}

	public String getJwsDynamicRestUrl() {
		return jwsDynamicRestUrl;
	}

	public void setJwsDynamicRestUrl(String jwsDynamicRestUrl) {
		this.jwsDynamicRestUrl = jwsDynamicRestUrl;
	}

	public String getJwsMethodDescription() {
		return jwsMethodDescription;
	}

	public void setJwsMethodDescription(String jwsMethodDescription) {
		this.jwsMethodDescription = jwsMethodDescription;
	}

	public String getJwsMethodName() {
		return jwsMethodName;
	}

	public void setJwsMethodName(String jwsMethodName) {
		this.jwsMethodName = jwsMethodName;
	}

	public Integer getJwsPlatformId() {
		return jwsPlatformId;
	}

	public void setJwsPlatformId(Integer jwsPlatformId) {
		this.jwsPlatformId = jwsPlatformId;
	}

	public Integer getJwsRbacId() {
		return jwsRbacId;
	}

	public void setJwsRbacId(Integer jwsRbacId) {
		this.jwsRbacId = jwsRbacId;
	}

	public String getServiceLogicFileName() {
		return serviceLogicFileName;
	}

	public void setServiceLogicFileName(String serviceLogicFileName) {
		this.serviceLogicFileName = serviceLogicFileName;
	}

	public Integer getJwsRequestTypeId() {
		return jwsRequestTypeId;
	}

	public void setJwsRequestTypeId(Integer jwsRequestTypeId) {
		this.jwsRequestTypeId = jwsRequestTypeId;
	}

	public Integer getJwsResponseProducerTypeId() {
		return jwsResponseProducerTypeId;
	}

	public void setJwsResponseProducerTypeId(Integer jwsResponseProducerTypeId) {
		this.jwsResponseProducerTypeId = jwsResponseProducerTypeId;
	}

	public Integer getJwsAllowFiles() {
		return jwsAllowFiles;
	}

	public void setJwsAllowFiles(Integer jwsAllowFiles) {
		this.jwsAllowFiles = jwsAllowFiles;
	}

	public Integer getJwsDynamicRestTypeId() {
		return jwsDynamicRestTypeId;
	}

	public void setJwsDynamicRestTypeId(Integer jwsDynamicRestTypeId) {
		this.jwsDynamicRestTypeId = jwsDynamicRestTypeId;
	}

	public String getJwsHeaderJson() {
		return jwsHeaderJson;
	}

	public void setJwsHeaderJson(String jwsHeaderJson) {
		this.jwsHeaderJson = jwsHeaderJson;
	}

	public Map<Integer, String> getDaoDetailsFileNameMap() {
		return daoDetailsFileNameMap;
	}

	public void setDaoDetailsFileNameMap(Map<Integer, String> daoDetailsFileNameMap) {
		this.daoDetailsFileNameMap = daoDetailsFileNameMap;
	}

	public Map<Integer, String> getDaoDetailsVariableNameMap() {
		return daoDetailsVariableNameMap;
	}

	public void setDaoDetailsVariableNameMap(Map<Integer, String> daoDetailsVariableNameMap) {
		this.daoDetailsVariableNameMap = daoDetailsVariableNameMap;
	}

	public Map<Integer, Integer> getDaoDetailsQueryTypeMap() {
		return daoDetailsQueryTypeMap;
	}

	public void setDaoDetailsQueryTypeMap(Map<Integer, Integer> daoDetailsQueryTypeMap) {
		this.daoDetailsQueryTypeMap = daoDetailsQueryTypeMap;
	}

	public Map<Integer, String> getDaoDetailsDatasourceIdMap() {
		return daoDetailsDatasourceIdMap;
	}

	public void setDaoDetailsDatasourceIdMap(Map<Integer, String> daoDetailsDatasourceIdMap) {
		this.daoDetailsDatasourceIdMap = daoDetailsDatasourceIdMap;
	}

	public Date getLastUpdatedTs() {
		return lastUpdatedTs;
	}

	public void setLastUpdatedTs(Date lastUpdatedTs) {
		this.lastUpdatedTs = lastUpdatedTs;
	}

	/** Added for New Column in Rest API */
	public Integer getHideDaoQuery() {
		return hideDaoQuery;
	}

	public void setHideDaoQuery(Integer hideDaoQuery) {
		this.hideDaoQuery = hideDaoQuery;
	}
	/** Ends Here*/

	public Integer getIsSecured() {
		return isSecured;
	}

	public void setIsSecured(Integer isSecured) {
		this.isSecured = isSecured;
	}

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}
}
