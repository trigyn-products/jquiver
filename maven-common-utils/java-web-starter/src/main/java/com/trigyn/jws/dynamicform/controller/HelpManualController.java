package com.trigyn.jws.dynamicform.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.utils.CustomeFileStorageException;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dbutils.vo.xml.FileUploadExportVO;
import com.trigyn.jws.dbutils.vo.xml.HelpManualTypeExportVO;
import com.trigyn.jws.dbutils.vo.xml.ManualEntryDetailsExportVO;
import com.trigyn.jws.dynamicform.dao.HelpManualDAO;
import com.trigyn.jws.dynamicform.dao.IManualTypeRepository;
import com.trigyn.jws.dynamicform.entities.HelpManualObjHolder;
import com.trigyn.jws.dynamicform.entities.ManualEntryDetails;
import com.trigyn.jws.dynamicform.entities.ManualEntryDetails.ActionType;
import com.trigyn.jws.dynamicform.entities.ManualType;
import com.trigyn.jws.dynamicform.service.HelpManualService;
import com.trigyn.jws.dynamicform.vo.TreeNode;
import com.trigyn.jws.dynarest.dao.FileUploadConfigDAO;
import com.trigyn.jws.dynarest.entities.FileUpload;
import com.trigyn.jws.dynarest.entities.FileUploadConfig;
import com.trigyn.jws.dynarest.service.FileUploadConfigService;
import com.trigyn.jws.dynarest.service.FilesStorageService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.security.config.Authorized;
import com.trigyn.jws.webstarter.service.ImportService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/cf")
public class HelpManualController {

	private final static Logger logger = LoggerFactory.getLogger(HelpManualController.class);

	@Autowired
	private MenuService           menuService 			= null;

	@Autowired
	private HelpManualService     helpManualService 	= null;

	@Autowired
	private FilesStorageService   filesStorageService 	= null;

	@Autowired
	private PropertyMasterService propertyMasterService = null;
	
	@Autowired
	private FileUtilities		  fileUtilities			= null;
	
	@Autowired
	private HelpManualDAO 		  helpManualDAO 		= null;
	
	@Autowired
	private FileUploadConfigDAO	  fileUploadConfigDAO	= null;
	
	@Autowired
	private ImportService	  	  importService			= null;
	
	@Autowired
	private FileUploadConfigService	fileUploadConfigService	= null;
	
	@Autowired
	private IManualTypeRepository 	iManualTypeRepository 	= null;
	
	
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
			fileUtilities.customSendError(httpServletResponse,HttpStatus.INTERNAL_SERVER_ERROR.value(),a_exception.getMessage());
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
			String headerTemplate = httpServletRequest.getParameter("headerTemplate");
			boolean doesExist = helpManualService.manualTypeExist(name);
			if(isEdit.equalsIgnoreCase("0")) {
				if (doesExist) {
					return "exist";
				}
			}
			return helpManualService.saveManualType(manualId, name, isEdit,headerTemplate);
		} catch (Exception a_exception) {
			logger.error("Error while loding manual page ", a_exception);
			fileUtilities.customSendError(httpServletResponse,HttpStatus.INTERNAL_SERVER_ERROR.value(),a_exception.getMessage());
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
			fileUtilities.customSendError(httpServletResponse,HttpStatus.INTERNAL_SERVER_ERROR.value(),a_exception.getMessage());
		}
	}

	@RequestMapping(value = "/shmd")
	@ResponseBody
	public Map<String,String> saveManualEntryDetails(@RequestBody HelpManualObjHolder objectNode,
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
			Map<String,String> result=new HashMap<String, String>();
			int sortIndex = Integer.parseInt(helpManualService.findlastSortIndex(objectNode.getManualId()));
			result.put("sortIndex", String.valueOf(sortIndex));
			result.put("result", "sucess");
			return result;
		}  catch (CustomeFileStorageException exception) 
		{
			fileUtilities.customSendError(httpServletResponse,HttpStatus.PRECONDITION_REQUIRED.value(),
					exception.getMessage());
		}catch (Exception a_exception) {
			logger.error("Error while loding manual page ", a_exception);
			fileUtilities.customSendError(httpServletResponse,HttpStatus.INTERNAL_SERVER_ERROR.value(),a_exception.getMessage());
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
			fileUtilities.customSendError(httpServletResponse,HttpStatus.INTERNAL_SERVER_ERROR.value(),a_exception.getMessage());
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
			String manualName = httpServletRequest.getParameter("mn");
			if(null!=manualType) {
				ManualType manualTypeDetails = 	iManualTypeRepository.findById(manualType).get();
				Map<String, Object> parameterMap = new HashMap<>();
				parameterMap.put("mt", manualType);
				parameterMap.put("mn", manualName);
				parameterMap.put("headerTemplate", manualTypeDetails.getHeaderTemplate());
	
				return menuService.getTemplateWithSiteLayout("help-manual-entry-template", parameterMap);
			}
			fileUtilities.customSendError(httpServletResponse,HttpStatus.PRECONDITION_FAILED.value(),"Request Param is null");
			return null;
		} catch (Exception a_exception) {
			logger.error("Error while loading Manual Listing page. ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			fileUtilities.customSendError(httpServletResponse,HttpStatus.INTERNAL_SERVER_ERROR.value(),a_exception.getMessage());
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
			fileUtilities.customSendError(httpServletResponse,HttpStatus.INTERNAL_SERVER_ERROR.value(),a_exception.getMessage());
			return null;
		}
	}

	@GetMapping(value = "hmfc")
	public String fetchContent(@RequestParam String id, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {
		try {

			String content = helpManualService.fetchContent(id);
			return content;
		} catch (Exception a_exception) {
			logger.error("Error while loading Manual page. ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			fileUtilities.customSendError(httpServletResponse,HttpStatus.INTERNAL_SERVER_ERROR.value(),a_exception.getMessage());
			return null;
		}
	}

	
	@GetMapping(value = { "hmfn" })
	public List<TreeNode> fetchSubNodes(@RequestParam String id, @RequestParam String mt,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		try {
			// Fetch Parent Nodes.
			List<ManualEntryDetails> parentslist = null;
			String manualId = mt;
			if (null == id || "#".equalsIgnoreCase(id) || "".equalsIgnoreCase(id))
				parentslist = helpManualService.fetchParentNodes(manualId);
			else
				parentslist = helpManualService.fetchByParentAndManualId(id, manualId);
			List<TreeNode> parents = new ArrayList<>();
			for (ManualEntryDetails parent : parentslist) {
				TreeNode node = new TreeNode();
				node.setId(parent.getManualEntryId());
				node.setText(parent.getEntryName());
				node.setChildren(helpManualService.hasChild(node.getId()));
				//node.setEntryContent(parent.getEntryContent());
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
			fileUtilities.customSendError(httpServletResponse,HttpStatus.INTERNAL_SERVER_ERROR.value(),a_exception.getMessage());
			return null;
		}
	}

	@GetMapping(value = { "hmefn" })
	public List<TreeNode> fetchNodesInEdit(@RequestParam String id, @RequestParam String mt,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		try {
			// Fetch Parent Nodes.
			List<ManualEntryDetails> parentslist = null;
			String manualId = mt;
			if (null == id || "#".equalsIgnoreCase(id) || "".equalsIgnoreCase(id))
				parentslist = helpManualService.fetchParentNodes(manualId);
			else
				parentslist = helpManualService.fetchByParentAndManualId(id, manualId);
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
			fileUtilities.customSendError(httpServletResponse,HttpStatus.INTERNAL_SERVER_ERROR.value(),a_exception.getMessage());
			return null;
		}
	}
	
	@GetMapping(value = "/search", produces = "application/json")
	public ArrayList<String> search(@RequestParam String str, @RequestParam String mt,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		try {
			List<ManualEntryDetails> searchList = null;
			String searchText = str;
			if (null != searchText) {
				searchList = helpManualService.searchText(searchText, mt);
			} else {
				searchList = helpManualService.fetchParentNodes(mt);
			}
			List<TreeNode> result = new ArrayList<>();
			ArrayList<String> result1 = new ArrayList<>();
			if (null != searchList && !searchList.isEmpty()) {
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
					result1.add(node.getId());
				}
			}
			return result1;
		} catch (Exception a_exception) {
			logger.error("Error while searching Manual page. ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			fileUtilities.customSendError(httpServletResponse,HttpStatus.INTERNAL_SERVER_ERROR.value(),a_exception.getMessage());
			return null;
		}
	}

	@GetMapping(value = "/searchRD", produces = "application/json")
	@ResponseBody
	public List<TreeNode> searchRD(@RequestParam String str, @RequestParam String mt,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		try {
			List<ManualEntryDetails> searchList = null;
			List<ManualEntryDetails> finalNodeList = null;
			String searchText = str;
			if (null != searchText) {
				searchList = helpManualService.searchText(searchText, mt);
				if (null != searchList && !searchList.isEmpty() && searchList.size() > 0) {
					finalNodeList = callRec(searchList);
				}
			} else {
				searchList = helpManualService.fetchParentNodes(mt);
			}
			List<TreeNode> result = new ArrayList<>();
			if (null != finalNodeList && !finalNodeList.isEmpty()) {
				for (ManualEntryDetails parent : finalNodeList) {
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
			}
			return result;
		} catch (Exception a_exception) {
			logger.error("Error while searching Manual page. ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			fileUtilities.customSendError(httpServletResponse,HttpStatus.INTERNAL_SERVER_ERROR.value(),a_exception.getMessage());
			return null;
		}
	}

	private List<ManualEntryDetails> callRec(List<ManualEntryDetails> searchList) {
		List<ManualEntryDetails> finalNodeList = new ArrayList<>();
		for (ManualEntryDetails details : searchList) {
			if (details.getParentId() != null) {
				finalNodeList.add(details);
				fetchParent(searchList, finalNodeList);
			}
		}
		return finalNodeList;

	}

	private List<ManualEntryDetails> fetchParent(List<ManualEntryDetails> nodeList,
			List<ManualEntryDetails> finalNodeList) {
		List<ManualEntryDetails> alteredNodeList = new ArrayList<>(nodeList);
		if (null != nodeList && nodeList.size() > 0) {
			for (ManualEntryDetails details : nodeList) {
				Optional<ManualEntryDetails> resultNode = null;
				resultNode = helpManualService.fetchByManualEntryId(details.getParentId());
				if (finalNodeList != null && !finalNodeList.contains(resultNode.get())) {
					finalNodeList.add(resultNode.get());
				}
				alteredNodeList.remove(details);
				fetchParent(alteredNodeList, finalNodeList);
			}
		}
		return finalNodeList;
	}
	
	@PostMapping(value = "/lumjd")
	public String getLastUpdatedManualJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		String entityId = a_httpServletRequest.getParameter("entityId");
		return helpManualService.getManualUploadJson(entityId);
	}
	
	@PostMapping(value = "/sjmed")
	public void saveJwsManualEntryDetails(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		String			modifiedContent	= a_httpServletRequest.getParameter("modifiedContent");
		ObjectMapper	objectMapper	= new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		HelpManualTypeExportVO	helpManualTypeExportVO			= objectMapper.readValue(modifiedContent, HelpManualTypeExportVO.class);
		ManualType manualType = convertManualTypeVoToEntity(helpManualTypeExportVO);
		helpManualDAO.saveManualType(manualType);
		
		for (ManualEntryDetailsExportVO entry : helpManualTypeExportVO.getManualEntries()) {
              helpManualService.deleteHelpManualIdEntries(entry.getManualId());
              ManualEntryDetails manualEntryDetails = converthelpManualTypeExportVoToEntity(entry);
              helpManualDAO.saveManualEntryDetails(manualEntryDetails);
          }
		if (helpManualTypeExportVO.getFileUploadConfig() != null) {
			FileUploadConfig	fileUploadConfig	= fileUploadConfigService.convertFileUploadExportVOToEntity(helpManualTypeExportVO.getFileUploadConfig());
			fileUploadConfigDAO.saveFileUploadConfig(fileUploadConfig);
		}
		
		for (FileUploadExportVO entry : helpManualTypeExportVO.getFileUploadList()) {
			FileUpload fileUpload = convertFileUploadExportVOToEntity(entry);
			List<FileUpload> fileUploadList = new ArrayList<>();
			fileUploadList.add(fileUpload);
            importService.saveAndUploadFiles(fileUploadList, com.trigyn.jws.webstarter.utils.Constant.HELP_MANUAL_DIRECTORY_NAME,
					manualType.getName()); 
        }
	}
	
	public ManualType convertManualTypeVoToEntity(HelpManualTypeExportVO entry) {
		ManualType manualType = new ManualType(entry.getManualId(),entry.getName(),
				entry.getIsSystemManual(),entry.getHeaderTemplate(), entry.getCreatedBy(),entry.getCreatedDate(),entry.getLastUpdatedBy(),entry.getLastUpdatedTs());
		return manualType;
	}
	
	public ManualEntryDetails converthelpManualTypeExportVoToEntity(ManualEntryDetailsExportVO entry) {
		ManualEntryDetails manualEntryDetails = new ManualEntryDetails(entry.getManualEntryId(),entry.getManualId(),entry.getEntryName(),
				entry.getEntryContent(),entry.getSortIndex(),entry.getLastUpdatedBy(),entry.getLastModifiedOn(), entry.getCreatedBy(),
				entry.getCreatedDate(),entry.getParentId());
		return manualEntryDetails;
	}
	
	public FileUpload convertFileUploadExportVOToEntity(FileUploadExportVO entry) {
		FileUpload fileUpload = new FileUpload(entry.getFileUploadId(),entry.getPhysicalFileName(),entry.getOriginalFileName(),
				entry.getFilePath(),entry.getUpdatedBy(),entry.getFileBinId(),entry.getFileAssociationId());
		return fileUpload;
	}
	
	
	@GetMapping(value = "/getSortIndex")
	public Integer getSortIndex(@RequestParam String mt)
			throws Exception {
		int sortIndex = Integer.parseInt(helpManualService.findlastSortIndex(mt));
		return sortIndex;
	}
	
	
}
