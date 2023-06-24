package com.trigyn.jws.dbutils.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.trigyn.jws.dbutils.entities.AdditionalDatasource;
import com.trigyn.jws.dbutils.entities.AdditionalDatasourceRepository;

public interface IMonacoSuggestion {
	@Autowired
	public AdditionalDatasourceRepository additionalDatasourceRepository = null;

	public static final String templateSuggestion[][] = {
			{ "loggedInUserEmail", "{loggedInUserName!\\'\\'}" },
			{ "loggedInUserRoleList", "{loggedInUserRoleList[<sequence>] !\\'\\'}" },
			{ "templateWithoutParams ", "<@templateWithoutParams \\'your-template-name\\'/>" },
			{ "templateWithParams  ", "<@templateWithParams \\'your-template-Name\\' templateParam/>" },
			{ "resourceBundle ", "<@resourceBundle \\'your.key\\' />" },
			{ "resourceBundleWithDefault  ", "<@resourceBundleWithDefault \\'your.key\\' \\'Default Value\\'/>" },
			{ "loggedInUserId  ", "{loggedInUserId  !\\'\\'}" },
			{ "loggedInUserFullName  ", "{fullName  !\\'\\'}" },
			{ "userObject  ", "{userObject.yourkey  !\\'\\'}" },
			{ "contextPath  ", "{contextPath  !\\'\\'}" },
			{ "messageSource  ", "{messageSource.getMessage(\\'your.key\\')  !\\'\\'}" },
			{ "systemProperties  ", "{getSystemProperty(\\'yourkey\\')}"},
			{ "httpRequestObject  ", "{httpRequestObject?api.getMethod()  !\\'\\'}" } };
	
	public static final String templateDynamicFormSuggestion[][] = {
			{ "loggedInUserEmail", "{loggedInUserName!\\'\\'}" },
			{ "loggedInUserRoleList", "{loggedInUserRoleList[<sequence>] !\\'\\'}" },
			{ "templateWithoutParams ", "<@templateWithoutParams \\'your-template-name\\'/>" },
			{ "templateWithParams  ", "<@templateWithParams \\'your-template-Name\\' templateParam/>" },
			{ "resourceBundle ", "<@resourceBundle \\'your.key\\' />" },
			{ "resourceBundleWithDefault  ", "<@resourceBundleWithDefault \\'your.key\\' \\'Default Value\\'/>" },
			{ "loggedInUserId  ", "{loggedInUserId  !\\'\\'}" },
			{ "loggedInUserFullName  ", "{fullName  !\\'\\'}" },
			{ "userObject  ", "{userObject.yourkey  !\\'\\'}" },
			{ "contextPath  ", "{contextPath  !\\'\\'}" },
			{ "messageSource  ", "{messageSource.getMessage(\\'your.key\\')  !\\'\\'}" },
			{ "systemProperties  ", "{getSystemProperty(\\'yourkey\\')}"},
			{ "httpRequestObject  ", "{httpRequestObject?api.getMethod()  !\\'\\'}"},
			{ "resultSetObject  ", "{(resultSetObject.\\'yourkey\\') !\\'\\'}" }, 
			{ "resultSetList  ", "{resultSetList?api.get(\\'yourkey\\')}" }
			};
	
	public static final String JStemplateSuggestions[][] = {
			{ "loggedInUserEmail","requestDetails[\\\\\"loggedInUserName\\\\\"]" },
			{ "loggedInUserRoleList", "requestDetails[\\\\\"loggedInUserRoleList\\\\\"]" },
			{ "loggedInUserId  ", "requestDetails[\\\\\"loggedInUserId\\\\\"]" },
			{ "loggedInUserFullName  ", "requestDetails[\\\\\"fullName\\\\\"]" },
			{ "userObject  ", "requestDetails[\\\\\"userObject\\\\\"][\\\\\"<Key Name>\\\\\"]" },
			{ "contextPath  ", "requestDetails[\\\\\"contextPath\\\\\"]" },
			{ "requestDetails  ", "requestDetails[\\\\\"\\\\\"]" },
			{ "daoResults  ", "daoResults[\\\\\"Query Variable Name\\\\\"]" },
			{ "files", "files[\\\\\"<File Input Name>\\\\\"]"  },
			{ "requestBody", "requestBody" },
			{ "header", "requestHeaders[\\\\\"<Key Name>\\\\\"]"},
			{ "session", "httpRequestObject[\\\\\"session\\\\\"]" },
			{ "httpRequestObject", "httpRequestObject" }
			};
	
	public static final String JSfileSuggestions[][] = {
			{ "loggedInUserEmail","requestDetails[\\\\\"loggedInUserName\\\\\"]" },
			{ "loggedInUserRoleList", "requestDetails[\\\\\"loggedInUserRoleList\\\\\"]" },
			{ "loggedInUserId  ", "requestDetails[\\\\\"loggedInUserId\\\\\"]" },
			{ "loggedInUserFullName  ", "requestDetails[\\\\\"fullName\\\\\"]" },
			{ "userObject  ", "requestDetails[\\\\\"userObject\\\\\"][\\\\\"<Key Name>\\\\\"]" },
			{ "contextPath  ", "requestDetails[\\\\\"contextPath\\\\\"]" },
			{ "requestDetails  ", "requestDetails[\\\\\"\\\\\"]" },
			{ "header", "requestHeaders[\\\\\"<Key Name>\\\\\"]"},
			{ "session", "httpRequestObject[\\\\\"session\\\\\"]" },
			{ "httpRequestObject", "httpRequestObject" },
			{ "fileBinId", "requestDetails[\\\\\"fileBinId\\\\\"]" },
			{ "fileUploadId", "requestDetails[\\\\\"fileUploadId\\\\\"]" },
			{ "fileAssociationId", "requestDetails[\\\\\"fileAssociationId\\\\\"]" }
			};

	public static final String JSSuggestions[][] = { 
			{ "jq_getSystemProperty", "jq_getSystemProperty(a_propertyName)" },
			{ "jq_getSystemEnvironment", "jq_getSystemEnvironment(a_propertyName)" },
			{ "jq_updateCookies", "jq_updateCookies(a_strKey, a_strValue)" },
			{ "jq_updateCookies_maxAge", "jq_updateCookies(a_strKey, a_strValue, maxAge)" },
			{ "jq_updateCookies_httpOnly", "jq_updateCookies(a_strKey, a_strValue, httpOnly)" },
			{ "jq_updateCookies_maxAge_httpOnly", "jq_updateCookies(a_strKey, a_strValue, maxAge, httpOnly)" },
			{ "jq_updateCookieSecurity", "jq_updateCookieSecurity(a_strKey, isSecured)" },
			{ "jq_getCookiesFromRequest", "jq_getCookiesFromRequest(a_strKey)" },
			{ "jq_haveCookie", "jq_haveCookie(a_strKey)" }, { "jq_deleteCookie", "jq_deleteCookie(a_strKey)" },
			{ "jq_updateSession", "jq_updateSession(a_strKey, a_strValue)" },
			{ "jq_getValueFromSession", "jq_getValueFromSession(a_strKey)" },
			{ "jq_haveSessionKey", "jq_haveSessionKey(a_strKey)" },
			{ "jq_deleteSessionKey", "jq_deleteSessionKey(a_strKey)" },
			{ "jq_getCreationTime", "jq_getCreationTime()" }, { "jq_getLastAccessedTime", "jq_getLastAccessedTime()" },
			{ "jq_getAllFiles", "jq_getAllFiles(a_filePath)" }, { "jq_deleteFile", "jq_deleteFile(a_filePath)" },
			{ "jq_saveFile", "jq_saveFile(a_strFileContent, a_strTargetFileName)" },
			{ "jq_saveFileFromPath", "jq_saveFileFromPath(a_strFilePath, a_strFileBinID, a_strcontextID)" },
			{ "jq_saveFileBin",
					"jq_saveFileBin(a_strFileContent, a_strTargetFileName, a_strFileBinID, a_strcontextID)" },
			{ "jq_getFileContent", "jq_getFileContent(a_strAbsolutePath)" },
			{ "jq_getFileBinContent", "jq_getFileBinContent(a_strfileUploadID)" },
			{ "jq_copyFile", "jq_copyFile(sourceFilePath, destinationFilePath)" },
			{ "jq_copyFile_TargetFileName", "jq_copyFile(sourceFilePath, destinationFilePath, a_strTargetFileName)" },
			{ "jq_copyFileBinId", "jq_copyFileBinId(a_strfileUploadID, destinationFilePath)" },
			{ "jq_getDBResult", "jq_getDBResult(a_strQuery, a_strdataSourceID, a_requestParams)" },
			{ "jq_callStoredProcedure", "jq_callStoredProcedure(a_strQuery, a_strdataSourceID, a_requestParams)" },
			{ "jq_updateDBQuery", "jq_updateDBQuery(a_strQuery, a_strdataSourceID, a_requestParams)" },
			{ "jq_executeRESTCall", "jq_executeRESTCall(a_strRestXML)" },
			{ "jq_sendMail", "jq_sendMail(a_strMailXML)" },
			{ "jq_evalTemplateByName", "jq_evalTemplateByName(a_strTemplateName, a_requestParams)" },
			{ "jq_evalTemplateByContent", "jq_evalTemplateByContent(a_strTemplateContent, a_requestParams)" },
			{ "jq_convertToPDFFromTemplate",
					"jq_convertToPDFFromTemplate(a_strTemplateName, a_contextValues, a_strImageFolder)" },
			{ "jq_convertToPDFFromString", "jq_convertToPDFFromString(a_strSourceBody, a_strImageFolder)" },
			{ "jq_logActivity", "jq_logActivity(a_requestParams)" },
			{ "jq_addNotifications", "jq_addNotifications(a_requestParams)" } };

	public static String getTemplateDynamicFormSuggestion() {

		return getSuggestion(templateDynamicFormSuggestion);
	}
	
	public static String getTemplateSuggestion() {

		return getSuggestion(templateSuggestion);
	}


	public static String getJSSuggestion(AdditionalDatasourceRepository dataSourceObject) {
		String dataSourceId = "";
		String datasourceName = "";
		List<AdditionalDatasource> list = dataSourceObject.findAll();
		String[][] DSSuggestions = new String[list.size()][list.size()];
		for (int iCounter = 0; iCounter < list.size(); iCounter++) {
			dataSourceId = list.get(iCounter).getAdditionalDatasourceId();
			datasourceName = list.get(iCounter).getDatasourceName();
			String[] DSSuggestionsnew = { datasourceName + "-DS", dataSourceId };
			DSSuggestions[iCounter] = DSSuggestionsnew;
		}

		String finalSuggestionList[][] = new String[JStemplateSuggestions.length + JSSuggestions.length
				+ DSSuggestions.length][];
		System.arraycopy(JStemplateSuggestions, 0, finalSuggestionList, 0, JStemplateSuggestions.length);
		System.arraycopy(DSSuggestions, 0, finalSuggestionList, JStemplateSuggestions.length, DSSuggestions.length);
		System.arraycopy(JSSuggestions, 0, finalSuggestionList, JStemplateSuggestions.length + DSSuggestions.length,
				JSSuggestions.length);

		return getSuggestion(finalSuggestionList);

	}
	
	public static String getfileJSSuggestion(AdditionalDatasourceRepository dataSourceObject) {
		String dataSourceId = "";
		String datasourceName = "";
		List<AdditionalDatasource> list = dataSourceObject.findAll(); 
		String[][] DSSuggestions = new String[list.size()][list.size()];
		for (int iCounter = 0; iCounter < list.size(); iCounter++) {
			dataSourceId = list.get(iCounter).getAdditionalDatasourceId();
			datasourceName = list.get(iCounter).getDatasourceName();
			String[] DSSuggestionsnew = { datasourceName + "-DS", dataSourceId };
			DSSuggestions[iCounter] = DSSuggestionsnew;
		}

		String finalSuggestionList[][] = new String[JSfileSuggestions.length + JSSuggestions.length
				+ DSSuggestions.length][];
		System.arraycopy(JSfileSuggestions, 0, finalSuggestionList, 0, JSfileSuggestions.length);
		System.arraycopy(DSSuggestions, 0, finalSuggestionList, JSfileSuggestions.length, DSSuggestions.length);
		System.arraycopy(JSSuggestions, 0, finalSuggestionList, JSfileSuggestions.length + DSSuggestions.length,
				JSSuggestions.length);

		return getSuggestion(finalSuggestionList);

	}

	public static String getSuggestion(String a_suggestion[][]) {
		StringBuffer sb = new StringBuffer();

		sb.append("[");
		for (String[] suggestion : a_suggestion) {
			if (suggestion != null && suggestion[0] != null && suggestion[1] != null) {
				if (sb.length() > 1) {
					sb.append(",");
				}
				sb.append("{\"label\":\"" + suggestion[0]);
				sb.append("\"");
				sb.append(",");
				sb.append("\"insertText\":\"" + suggestion[1] + "\"}");
			}

		}
		sb.append("]");

		return sb.toString();
	}

}
