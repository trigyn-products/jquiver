package com.trigyn.jws.dynamicform.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dynamicform.entities.HelpManualObjHolder;
import com.trigyn.jws.dynamicform.entities.ManualEntryDetails;
import com.trigyn.jws.dynamicform.entities.ManualEntryDetails.ActionType;
import com.trigyn.jws.dynamicform.service.HelpManualService;
import com.trigyn.jws.dynamicform.vo.TreeNode;
import com.trigyn.jws.dynarest.service.FilesStorageService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.security.config.Authorized;

@RestController
@RequestMapping("/cf")
public class HelpManualController {

	private final static Logger logger = LogManager.getLogger(HelpManualController.class);

	@Autowired
	private MenuService menuService = null;

	@Autowired
	private HelpManualService helpManualService = null;

	@Autowired
	private FilesStorageService filesStorageService = null;

	@Autowired
	private PropertyMasterService propertyMasterService = null;

	@PostMapping(value = "/ehme", produces = MediaType.TEXT_HTML_VALUE)
	@Authorized(moduleName = com.trigyn.jws.usermanagement.utils.Constants.HELPMANUAL)
	public String manualEntityListingPage(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {
		try {
			String manualType = httpServletRequest.getParameter("mt");
			String manualName = httpServletRequest.getParameter("mn");
			Map<String, Object> parameterMap = new HashMap<>();
			String dbDateFormat = propertyMasterService.getDateFormatByName(Constant.PROPERTY_MASTER_OWNER_TYPE,
					Constant.PROPERTY_MASTER_OWNER_ID, Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
					Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);

			parameterMap.put("mt", manualType);
			parameterMap.put("mn", manualName);
			parameterMap.put("dbDateFormat", dbDateFormat);
			return menuService.getTemplateWithSiteLayout("manual-entry-template", parameterMap);
		} catch (Exception a_exception) {
			logger.error("Error while loding manual page ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/shmt", produces = MediaType.TEXT_HTML_VALUE)
	@Authorized(moduleName = com.trigyn.jws.usermanagement.utils.Constants.HELPMANUAL)
	public String saveManualType(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		try {

			String manualId = StringUtils.isBlank(httpServletRequest.getParameter("mt")) == true ? null
					: httpServletRequest.getParameter("mt");
			String isEdit = httpServletRequest.getParameter("ie");
			String name = httpServletRequest.getParameter("name");
			boolean doesExist = helpManualService.manualTypeExist(name);
			if (doesExist) {
				return "exist";
			}
			return helpManualService.saveManualType(manualId, name, isEdit);
		} catch (Exception a_exception) {
			logger.error("Error while loding manual page ", a_exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
		}
		return null;
	}

	@PostMapping(value = "/smfd", produces = MediaType.APPLICATION_JSON_VALUE)
	@Authorized(moduleName = com.trigyn.jws.usermanagement.utils.Constants.HELPMANUAL)
	public void saveFileManualDetails(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			String manualEntryId = httpServletRequest.getParameter("manualEntryId") == "" ? null
					: httpServletRequest.getParameter("manualEntryId");
			String entryName = httpServletRequest.getParameter("entryName");
			String manualId = httpServletRequest.getParameter("manualId");
			List<String> fileIds = new Gson().fromJson(httpServletRequest.getParameter("fileIds"), List.class);
			helpManualService.saveFileForManualEntry(manualEntryId, manualId, entryName, fileIds);
		} catch (Exception a_exception) {
			logger.error("Error while loding manual page ", a_exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
		}
	}	
	
	@RequestMapping(value = "/shmd")
	@ResponseBody
	public String saveManualEntryDetails(@RequestBody HelpManualObjHolder objectNode,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		try {
			Map<String, String> partentIds = new HashMap<String, String>();
			if (null != objectNode.getFinalManualDataList() && !objectNode.getFinalManualDataList().isEmpty()) {

				for (ManualEntryDetails manualEntrtObj : objectNode.getFinalManualDataList()) {
					if (manualEntrtObj.getManualEntryId().startsWith("j1_")) {
						partentIds.put(manualEntrtObj.getManualEntryId(), null);
					}
					if ("#".equalsIgnoreCase(manualEntrtObj.getParentId())) {
						manualEntrtObj.setParentId(null);

					}
					if (ActionType.ADD.getActionType().equalsIgnoreCase(manualEntrtObj.getAction())) {
						String id = manualEntrtObj.getParentId();
						String mentryId = manualEntrtObj.getManualEntryId();
						if (null != id && id.startsWith("j1_")) {
							manualEntrtObj.setParentId(partentIds.get(id).toString());
						}
						manualEntrtObj.setManualEntryId(null);
						String manualEntryId = helpManualService.saveManualEntryDetails(manualEntrtObj);
						if (mentryId.startsWith("j1_")) {
							partentIds.put(mentryId, manualEntryId);
						}
					} else if (ActionType.UPDATE.getActionType().equalsIgnoreCase(manualEntrtObj.getAction())) {
						helpManualService.updateHelpManualEntryId(manualEntrtObj.getManualEntryId(),
								manualEntrtObj.getEntryName(), manualEntrtObj.getEntryContent(),
								manualEntrtObj.getManualId());
					} else if (ActionType.DELETE.getActionType().equalsIgnoreCase(manualEntrtObj.getAction())) {
						helpManualService.deleteHelpManualEntryId(manualEntrtObj.getManualId(),
								manualEntrtObj.getManualEntryId());
					}
				}

			}
			if (null != objectNode.getFormData() && !objectNode.getFormData().isEmpty()) {
				List<Map<String, String>> formData = objectNode.getFormData();
				for (Map<String, String> formEntry : formData) {
					if (formEntry.containsKey("valueType") && formEntry.get("valueType").equalsIgnoreCase("fileBin")) {
						filesStorageService.commitChanges(formEntry.get("FileBinID"),
								formEntry.get("fileAssociationID"));
					}

				}
			}
			return "success";
		} catch (Exception a_exception) {
			logger.error("Error while loding manual page ", a_exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
		}
		return null;
	}

	@DeleteMapping(value = "/dme", produces = MediaType.APPLICATION_JSON_VALUE)
	@Authorized(moduleName = com.trigyn.jws.usermanagement.utils.Constants.HELPMANUAL)
	public void deleteManualEntryDetails(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			String manualType = httpServletRequest.getParameter("mt");
			String manualEntryId = httpServletRequest.getParameter("mei");
			Integer sortIndex = httpServletRequest.getParameter("si") == null ? 0
					: Integer.parseInt(httpServletRequest.getParameter("si"));
			helpManualService.deleteManualEntryId(manualType, manualEntryId, sortIndex);
		} catch (Exception a_exception) {
			logger.error("Error while loding manual page ", a_exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
		}
	}

	private Map<String, Object> validateAndProcessRequestParams(HttpServletRequest httpServletRequest) {
		Map<String, Object> requestParams = new HashMap<>();
		for (String requestParamKey : httpServletRequest.getParameterMap().keySet()) {
			requestParams.put(requestParamKey, httpServletRequest.getParameter(requestParamKey));
		}
		return requestParams;
	}

	@GetMapping(value = "/hmvm", produces = MediaType.TEXT_HTML_VALUE)
	@PreAuthorize("hasPermission('module','Help Manual')")
	public String manualTreeListingPage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			String manualType = httpServletRequest.getParameter("mt");
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("mt", manualType);
			return menuService.getTemplateWithSiteLayout("help-manual-entry-template", parameterMap);
		} catch (Exception a_exception) {
			logger.error("Error while loading Manual Listing page. ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@GetMapping(value = "/hmem", produces = MediaType.TEXT_HTML_VALUE)
	@PreAuthorize("hasPermission('module','Help Manual')")
	public String editHelpManual(@RequestParam String mt, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {
		try {
			String manualType = mt;
			Map<String, Object> parameterMap = new HashMap<>();
			int sortIndex = Integer.parseInt(helpManualService.findlastSortIndex(mt));
			parameterMap.put("sortIndex", sortIndex);
			parameterMap.put("mt", manualType);
			return menuService.getTemplateWithSiteLayout("help-manual-edit-template", parameterMap);
		} catch (Exception a_exception) {
			logger.error("Error while loading Manual Listing page. ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@RequestMapping(value = "hmfc")
	public String fetchContent(@RequestParam String id, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {
		try {

			String content = helpManualService.fetchContent(id);
			List<ManualEntryDetails> parents = new ArrayList<>();
			return content;
		} catch (Exception a_exception) {
			logger.error("Error while loading Manual page. ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@RequestMapping(value = { "hmfn" })
	//@Authorized(moduleName = com.trigyn.jws.usermanagement.utils.Constants.HELPMANUAL)
	public List<TreeNode> fetchSubNodes(@RequestParam String id, @RequestParam String mt,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		try {
			// Fetch Parent Nodes.
			List<ManualEntryDetails> parentslist = null;
			String manualId = mt;
			if (null == id || "#".equalsIgnoreCase(id) || "".equalsIgnoreCase(id))
				parentslist = helpManualService.fetchParentNodes(manualId);
			else
				parentslist = helpManualService.fetchByNodeId(id, manualId);
			List<TreeNode> parents = new ArrayList<>();
			for (ManualEntryDetails parent : parentslist) {
				TreeNode node = new TreeNode();
				node.setId(parent.getManualEntryId());
				node.setText(parent.getEntryName());
				node.setChildren(helpManualService.hasChild(node.getId()));
				node.setEntryContent(parent.getEntryContent());
				if (node.isChildren()) {
					node.setType("folder");
				}
				parents.add(node);
			}
			return parents;
		} catch (Exception a_exception) {
			logger.error("Error while loading Manual page. ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@RequestMapping(value = { "search" })
	@ResponseBody
	public List<TreeNode> search(@RequestBody HelpManualObjHolder objectNode, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {
		try {
			List<ManualEntryDetails> searchList = null;
			String searchText = objectNode.getSearchText();
			if (null != searchText)
				searchList = helpManualService.searchText(searchText, objectNode.getManualId());
			else
				searchList = helpManualService.fetchParentNodes(objectNode.getManualId());
			List<TreeNode> result = new ArrayList<>();
			for (ManualEntryDetails parent : searchList) {
				TreeNode node = new TreeNode();
				node.setId(parent.getManualEntryId());
				node.setText(parent.getEntryName());
				node.setChildren(helpManualService.hasChild(node.getId()));
				node.setEntryContent(parent.getEntryContent());
				if (node.isChildren()) {
					node.setType("folder");
				}
				result.add(node);
			}

			return result;
		} catch (Exception a_exception) {
			logger.error("Error while searching Manual page. ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

}
