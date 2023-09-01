package com.trigyn.jws.resourcebundle.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynarest.cipher.utils.ScriptUtil;
import com.trigyn.jws.gridutils.service.GenericUtilsService;
import com.trigyn.jws.gridutils.utility.GenericGridParams;
import com.trigyn.jws.resourcebundle.dao.ResourceBundleDAO;
import com.trigyn.jws.resourcebundle.entities.ResourceBundle;
import com.trigyn.jws.resourcebundle.entities.ResourceBundlePK;
import com.trigyn.jws.resourcebundle.entities.ResourceBundlePQGrid;
import com.trigyn.jws.resourcebundle.repository.interfaces.ILanguageRepository;
import com.trigyn.jws.resourcebundle.repository.interfaces.IResourceBundleRepository;
import com.trigyn.jws.resourcebundle.utils.Constant;
import com.trigyn.jws.resourcebundle.utils.ResourceBundleUtils;
import com.trigyn.jws.resourcebundle.vo.LanguageVO;
import com.trigyn.jws.resourcebundle.vo.ResourceBundleVO;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.webstarter.utils.FileUtil;

@Service
@Transactional(readOnly = true)
public class ResourceBundleService {

	@Autowired
	private ResourceBundleDAO dbResourceDAO = null;

	@Autowired
	private IResourceBundleRepository iResourceBundleRepository = null;

	@Autowired
	private ILanguageRepository iLanguageRepository = null;

	@Autowired
	private ModuleVersionService moduleVersionService = null;

	@Autowired
	private IUserDetailsService userDetailsService = null;

	@Autowired
	private ActivityLog activitylog = null;

	@Autowired
	private IUserDetailsService detailsService = null;

	@Autowired
	private GenericUtilsService genericUtilsService = null;

	@Autowired
	private DBTemplatingService templatingService = null;

	@Autowired
	private TemplatingUtils templatingUtils = null;

	private final static Logger logger = LogManager.getLogger(ResourceBundleService.class);

	private final static String GRIDID = "gridId";

	private final static String FAIL = "fail:";

	public Map<Integer, ResourceBundleVO> getResourceBundleVOMap(String resourceBundleKey) throws Exception {
		try {
			Map<Integer, ResourceBundleVO> resourceBundleVOMap = new HashMap<Integer, ResourceBundleVO>();
			List<ResourceBundleVO> resourceBundleVOList = iResourceBundleRepository
					.findResourceBundleByKey(resourceBundleKey);
			if (!CollectionUtils.isEmpty(resourceBundleVOList)) {
				for (ResourceBundleVO dbResource : resourceBundleVOList) {
					resourceBundleVOMap.put(dbResource.getLanguageId(), dbResource);
				}
			}
			return resourceBundleVOMap;
		} catch (Exception a_excep) {
			logger.error("Error ocurred while fetching resource bundle data : ResourceKey : " + resourceBundleKey,
					a_excep);
			throw new RuntimeException("Error ocurred while fetching resource bundle data");
		}
	}

	public List<LanguageVO> getLanguagesList() throws Exception {
		try {
			return iLanguageRepository.getAllLanguages(Constant.RecordStatus.INSERTED.getStatus());
		} catch (Exception a_excep) {
			logger.error("Error ocurred while fetching list of languages.", a_excep);
			throw new RuntimeException("Error ocurred while fetching list of languages.");
		}
	}

	public Boolean checkResourceKeyExist(String resourceBundleKey) throws Exception {
		Boolean keyAlreadyExist = true;
		try {
			String savedResourceBundleKey = iResourceBundleRepository.checkResourceKeyExist(resourceBundleKey);
			if (resourceBundleKey == null
					|| (resourceBundleKey != null && !resourceBundleKey.equals(savedResourceBundleKey))) {
				keyAlreadyExist = false;
			}
			return keyAlreadyExist;
		} catch (Exception a_excep) {
			logger.error("Error ocurred while fetching resource bundle data : ResourceKey : " + resourceBundleKey,
					a_excep);
			throw new RuntimeException("Error ocurred while fetching resource bundle data");
		}
	}

	@Transactional(readOnly = false)
	public void saveResourceBundleDetails(List<ResourceBundleVO> resourceBundleVOList, Integer sourceTypeId)
			throws Exception {
		try {
			List<ResourceBundle> resourceBundleList = new ArrayList<>();
			List<ResourceBundleVO> oldResourceBundleVOObj = new ArrayList<>();
			String action = "";
			UserDetailsVO detailsVO = null;
			if ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes() != null) {
				detailsVO = detailsService.getUserDetails();
			} else {
				detailsVO = new UserDetailsVO("anonymous-user", "anonymous", Arrays.asList("anonymous"),
						"anonymous-user");
			}
			String user = detailsVO.getUserName();
			if (!CollectionUtils.isEmpty(resourceBundleVOList)) {
				String resourceBundleKey = resourceBundleVOList.get(0).getResourceKey();
				String savedResourceBundleKey = iResourceBundleRepository.checkResourceKeyExist(resourceBundleKey);
				if (null == savedResourceBundleKey) {
					action = Constants.Action.ADD.getAction();
				} else {
					action = Constants.Action.EDIT.getAction();
					oldResourceBundleVOObj = iResourceBundleRepository.findResourceBundleByKey(resourceBundleKey);
				}
				for (ResourceBundleVO resourceBundleVO : resourceBundleVOList) {
					if (resourceBundleVO.getText() != null && !resourceBundleVO.getText().equals("")) {
						ResourceBundle resourceBundle = convertResourceBundleVOToEntity(resourceBundleKey,
								resourceBundleVO);
						if (action.equals(Constants.Action.ADD.getAction())) {
							resourceBundle.setCreatedBy(user);
							resourceBundle.setCreatedDate(new Date());
						} else {
							resourceBundle.setCreatedBy(oldResourceBundleVOObj.get(0).getCreatedBy());
							resourceBundle.setCreatedDate(oldResourceBundleVOObj.get(0).getCreatedDate());
							resourceBundle.setUpdatedBy(user);
							resourceBundle.setUpdatedDate(new Date());
						}
						resourceBundleList.add(resourceBundle);
					}
				}

				logActivity(resourceBundleKey, action);
				iResourceBundleRepository.saveAll(resourceBundleList);
				moduleVersionService.saveModuleVersion(resourceBundleVOList, null, resourceBundleKey,
						"jq_resource_bundle", sourceTypeId);
			}

		} catch (Exception ex) {
			logger.error("Error occurred while saving resource bundle data : ResourceBundleKey : "
					+ resourceBundleVOList.get(0).getResourceKey(), ex);
			throw new RuntimeException("Error ocurred while saving resource bundle data");
		}
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in MultiLingual Module.
	 * 
	 * @author Bibhusrita.Nayak
	 * @param resourceBundleKey
	 * @param action
	 * @throws Exception
	 */
	private void logActivity(String resourceBundleKey, String action) throws Exception {
		Map<String, String> requestParams = new HashMap<>();
		UserDetailsVO detailsVO = userDetailsService.getUserDetails();
		Date activityTimestamp = new Date();
		requestParams.put("action", action);
		requestParams.put("entityName", resourceBundleKey);
		requestParams.put("masterModuleType", Constants.Modules.MULTILINGUAL.getModuleName());
		requestParams.put("userName", detailsVO.getUserName());
		requestParams.put("message", "");
		requestParams.put("date", activityTimestamp.toString());
		if (resourceBundleKey.toLowerCase().startsWith("jws".toLowerCase())) {
			requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
		} else {
			requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
		}
		activitylog.activitylog(requestParams);
	}

	private ResourceBundle convertResourceBundleVOToEntity(String resourceBundleKey, ResourceBundleVO resourceBundleVO)
			throws Exception {
		ResourceBundle resourceBundle = new ResourceBundle();
		ResourceBundlePK resourceBundlePK = new ResourceBundlePK();
		resourceBundlePK.setResourceKey(resourceBundleKey);

		resourceBundlePK.setLanguageId(resourceBundleVO.getLanguageId());
		resourceBundle.setId(resourceBundlePK);
		if (resourceBundleVO.getLanguageId().equals(Constant.DEFAULT_LANGUAGE_ID)) {
			resourceBundle.setText(resourceBundleVO.getText());
		} else {
			resourceBundle.setText(ResourceBundleUtils.getUnicode(resourceBundleVO.getText()));
		}
		return resourceBundle;
	}

	public ResourceBundleVO convertResourceBundleEntityToVO(ResourceBundle resourceBundle) throws Exception {
		ResourceBundleVO resourceBundleVO = new ResourceBundleVO();
		resourceBundleVO.setLanguageId(resourceBundle.getId().getLanguageId());
		resourceBundleVO.setResourceKey(resourceBundle.getId().getResourceKey());
		resourceBundleVO.setText(resourceBundle.getText());
		return resourceBundleVO;
	}

	public String findTextByKeyAndLanguageId(String resourceBundleKey, Integer languageId) throws Exception {
		if (languageId == null) {
			languageId = Constant.DEFAULT_LANGUAGE_ID;
		}
		return iResourceBundleRepository.findMessageByKeyAndLanguageId(resourceBundleKey, languageId,
				Constant.DEFAULT_LANGUAGE_ID, Constant.RecordStatus.INSERTED.getStatus());
	}

	public Object getResourceBundleData(String localeId, List<String> keyList) throws Exception {
		return dbResourceDAO.getResourceBundleData(localeId, keyList);
	}

	public String fetchGridData(String saveState, String gridId, JSONObject filter) throws Exception {

		Gson gson = new Gson();
		Map<String, Object> entityStringMap = gson.fromJson(saveState.toString(), Map.class);
		entityStringMap.put(GRIDID, gridId);
		GenericGridParams gridParams = new GenericGridParams("resourceKey", "ASC", 100000, filter);
		Integer matchingRowCount = genericUtilsService.findCount((String) entityStringMap.get("gridId"), gridParams,
				new HashMap());
		gridParams.setStartIndex(0);
		gridParams.setRowsPerPage(matchingRowCount);
		List<Map<String, Object>> gridList = genericUtilsService.findAllRecords((String) entityStringMap.get("gridId"),
				gridParams, new HashMap());
		List<LanguageVO> languageVOList = getLanguagesList();
		List<ResourceBundlePQGrid> resourceBundleList = new ArrayList<>();
		Map<String, Object> templateMap = new HashMap<>();
		templateMap.put("languages", languageVOList);
		Map<String, List<ResourceBundlePQGrid>> rbList = new HashMap<>();
		for (Map<String, Object> map : gridList) {
			ObjectMapper mapper = new ObjectMapper();
			ResourceBundlePQGrid pojo = mapper.convertValue(map, ResourceBundlePQGrid.class);
			resourceBundleList.add(pojo);
		}

		for (ResourceBundlePQGrid rb : resourceBundleList) {
			List<ResourceBundlePQGrid> langList = new ArrayList<>();
			if (rbList.containsKey(rb.getResourceKey())) {
				langList = rbList.get(rb.getResourceKey());
				for (ResourceBundlePQGrid rbLan : langList) {
					if (rbLan.getLanguageId().equals(rb.getLanguageId())) {
						langList.remove(rbLan);
						break;
					}

				}
				langList.add(rb);
				Collections.sort(langList);
				rbList.put(rb.getResourceKey(), langList);
			} else {
				langList.add(rb);

				for (LanguageVO lang : languageVOList) {
					if (!lang.getLanguageId().equals(rb.getLanguageId())) {
						ResourceBundlePQGrid newObj = new ResourceBundlePQGrid();
						newObj.setLanguageId(lang.getLanguageId());
						newObj.setResourceKey(rb.getResourceKey());
						newObj.setResourceBundleText("");
						langList.add(newObj);
					}
				}
				Collections.sort(langList);
				rbList.put(rb.getResourceKey(), langList);
			}

		}

		templateMap.put("resourceBundlelist", rbList);
		return evalTemplateByName("export-resourcebundle", templateMap);

	}


	public final String evalTemplateByName(String a_strTemplateName, Map<String, Object> a_requestParams)  {

		try {
			TemplateVO templateVO = templatingService.getTemplateByName(a_strTemplateName);

			return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					((null == a_requestParams) ? new HashMap<String, Object>() : a_requestParams));
		} catch (Exception ex) {
			logger.error("Error occured in evalTemplateByName", ex);
		}
		return a_strTemplateName;
	}

	/**
	 * This Method create and applies filter,sorting on the fetched data.
	 * 
	 * @param request
	 * @return result
	 * @throws Exception
	 */
	public String exportResourceBundleData(HttpServletRequest request) throws Exception {
		String tempDownloadPath = FileUtil.generateTemporaryFilePath("exportTempPath", UUID.randomUUID().toString());
		try {
			String saveStatus = request.getParameter("entityStringMap");
			String resourceType = request.getParameter("resource_type");
			List<JSONObject> filterList = new ArrayList<>();
			JSONObject gridParams = new JSONObject(saveStatus);
			int colModelArraylen = gridParams.getJSONArray("colModel").length();

			ArrayList<Object> filterArray = new ArrayList<>();
			colModelArraylen--;
			for (int colModelCounter = 0; colModelCounter < colModelArraylen; colModelCounter++) {
				JSONObject obj = new JSONObject();
				if (gridParams.getJSONArray("colModel").getJSONObject(colModelCounter).has("filter") && gridParams
						.getJSONArray("colModel").getJSONObject(colModelCounter).getJSONObject("filter").length() > 0) {
					obj.put("data",
							gridParams.getJSONArray("colModel").getJSONObject(colModelCounter).getString("filter"));
					obj.put("dataIndx",
							gridParams.getJSONArray("colModel").getJSONObject(colModelCounter).getString("dataIndx"));
					filterList.add(obj);
				}

			}
			String condition = "contain";
			String dataType = "string";
			String cbFn = "";

			for (int i = 0; i < filterList.size(); i++) {
				JSONObject json = new JSONObject(filterList.get(i).toString());
				if (json.get("data") != null) {
					JSONObject jsonChildObject = new JSONObject(json.get("data").toString());
					if (jsonChildObject.get("value") != null && jsonChildObject.get("value").equals("") == false) {
						String dataIndx = json.get("dataIndx").toString();
						String value = jsonChildObject.getString("value");
						JSONObject obj1 = new JSONObject();
						obj1.put("dataIndx", dataIndx);
						obj1.put("value", value);
						obj1.put("condition", condition);
						obj1.put("dataType", dataType);
						obj1.put("cbFn", cbFn);
						filterArray.add(obj1);
					}

				}
			}
			if (resourceType != null && !resourceType.equals("0")) {
				JSONObject filter = new JSONObject();
				filter.put("dataIndx", "resource_type");
				filter.put("value", resourceType);
				filter.put("condition", condition);
				filter.put("dataType", dataType);
				filter.put("cbFn", cbFn);
				filterArray.add(filter);
			}
			// json for pq_filter
			HashMap<String, Object> pqFilter = new HashMap<>();
			String mode;
			ArrayList<Object> data1 = new ArrayList<>();
			pqFilter.put("mode", "AND");
			pqFilter.put("data", data1);
			// {"mode":"AND","data":[{"dataIndx":"resourceKey","value":"sa","condition":"contain","dataType":"string","cbFn":""}]}
			for (int filterArrayCounter = 0; filterArrayCounter < filterArray.size(); filterArrayCounter++) {
				List<Object> list = new ArrayList<>();
				if (pqFilter.containsKey("data")) {
					list = (ArrayList) pqFilter.get("data");
				}
				list.add(filterArray.get(filterArrayCounter));
				pqFilter.put("data", list);
			}

			String gridId = request.getParameter("gridId");
			String excelString = fetchGridData(saveStatus, gridId, new JSONObject(pqFilter));
			String fileName = "LanguagePack";
			String filePath = tempDownloadPath + File.separator + fileName + ".xml";
			FileWriter fr = new FileWriter(new File(filePath));
			Writer br = new BufferedWriter(fr);
			br.write(excelString);
			br.close();
			return filePath;
		} catch (Exception ex) {
			logger.error("Error while exporting the configuration ", ex);
			if (ex.getMessage() != null && ex.getMessage().startsWith("Data mismatch while exporting")) {
				return FAIL + ex.getMessage();
			} else {
				return FAIL + "Error occurred while exporting";
			}
		}
	}

	public void downloadExport(HttpServletRequest request, HttpServletResponse response, String path) throws Exception {
		File filePath = new File(path);
		InputStream inputStream = new FileInputStream(filePath);
		response.setContentType("application/force-download");
		response.setHeader("Content-disposition", "attachment; filename=" + filePath.getName());
		IOUtils.copy(inputStream, response.getOutputStream());
		response.flushBuffer();
		inputStream.close();

	}

	/**
	 * Parse file using DOM parser
	 * 
	 * @param request
	 * @param response
	 * @param filePart
	 * @return result
	 * @throws Exception
	 */

	public String importData(HttpServletRequest request, HttpServletResponse response, Part filePart,
			Integer sourceTypeId) throws Exception {
		try {
			String tempDownloadPath = FileUtil.generateTemporaryFilePath("importTempPath",
					UUID.randomUUID().toString());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			OutputStream otpStream = new FileOutputStream(
					new File(tempDownloadPath + File.separator + filePart.getSubmittedFileName()));
			InputStream iptStream = filePart.getInputStream();
			final byte[] bytes = new byte[1024];
			int read = 0;
			while ((read = iptStream.read(bytes)) != -1) {
				otpStream.write(bytes, 0, read);
			}
			// parse XML file
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = db.parse(new File(tempDownloadPath + File.separator + filePart.getSubmittedFileName()));
			doc.getDocumentElement().normalize();
			NodeList list = doc.getElementsByTagName("Row");
			List<String> rejectkeys = new ArrayList<>();
			if (list.getLength() > 1) {
				rejectkeys = getNode(list, sourceTypeId);
			} else {
				throw new Exception("File is empty");
			}
			if (rejectkeys.isEmpty())
				return "success";
			else
				return "rejected:" + rejectkeys.toString();
		} catch (FileUploadException fue) {
			throw new FileUploadException(FAIL + "Error while importing File Bin");
		} catch (Exception ex) {
			if (ex.getMessage() != null) {
				throw new Exception(FAIL + ex.getMessage());
			} else {
				throw new Exception(FAIL + "Error while importing data");
			}
		}

	}

	private List<String> getNode(NodeList nodeList, Integer sourceTypeId) throws Exception {

		ArrayList<String> langCodeList = new ArrayList<>();
		Node tempheaderNode = nodeList.item(0);
		List<ResourceBundleVO> rbvoList = new ArrayList<>();
		List<ResourceBundleVO> rejectRBVOList = new ArrayList<>();
		List<String> rejectkeys = new ArrayList<>();
		NodeList childheaderNodeList = tempheaderNode.getChildNodes();

		List<String> langIdList = new ArrayList<>();
		Map<String, Integer> langMap = new HashMap<>();
		for (LanguageVO lang : getLanguagesList()) {
			langMap.put(lang.getLocaleId(), lang.getLanguageId());
		}
		List<String> headerList = new ArrayList<>();
		for (int childHeaderNodeCounter = 1; childHeaderNodeCounter < childheaderNodeList
				.getLength(); childHeaderNodeCounter++) {

			Node childheadertempNode = childheaderNodeList.item(childHeaderNodeCounter);
			if (childheadertempNode.getNodeName().equals("Cell")
					&& childheadertempNode.getTextContent().contains("-")) {
				String langCode = childheadertempNode.getTextContent().substring(0,
						childheadertempNode.getTextContent().indexOf("-"));
				langCodeList.add(langCode);
				// list of header to compare count of lang in DB
				langIdList.add(childheadertempNode.getTextContent());

			}
			if (childheadertempNode.getTextContent().contains("-"))
				headerList.add(childheadertempNode.getTextContent().substring(0,
						childheadertempNode.getTextContent().indexOf("-")));
			else
				headerList.add(childheadertempNode.getTextContent());

		}
		if (!langCodeList.isEmpty()) {
			long languageCount = iLanguageRepository.getLanguagesCount(langCodeList);

			if (languageCount == langIdList.size()) {
				for (int nodeCounter = 1; nodeCounter < nodeList.getLength(); nodeCounter++) {
					Node tempNode = nodeList.item(nodeCounter);
					NodeList childNodeList = tempNode.getChildNodes();
					Node keytempNode = childNodeList.item(1);
					for (int chileNodeCounter = 3; chileNodeCounter < childNodeList.getLength(); chileNodeCounter++) {
						Node childtempNode = childNodeList.item(chileNodeCounter);
						if (childtempNode.getNodeName().equals("Cell") && !childtempNode.getTextContent().equals("")) {
							ResourceBundleVO resourceBundleObj = new ResourceBundleVO();
							resourceBundleObj.setResourceKey(keytempNode.getTextContent());
							resourceBundleObj.setText(childtempNode.getTextContent());
							resourceBundleObj.setLanguageId(langMap.get(headerList.get(chileNodeCounter - 1)));

							if (!Pattern.matches("^[A-Za-z]+[\\w\\-\\:\\.]*$", resourceBundleObj.getResourceKey())) {
								rejectRBVOList.add(resourceBundleObj);
								if (!rejectkeys.contains(resourceBundleObj.getResourceKey()))
									rejectkeys.add(resourceBundleObj.getResourceKey());
							} else
								rbvoList.add(resourceBundleObj);
						}

					}

				}

				saveResourceBundleList(rbvoList, sourceTypeId);
			} else {
				throw new Exception("Unable to upload file . Please check Header .");
			}

		} else {
			throw new Exception("Unable to upload file . Please check Header .");
		}
		return rejectkeys;
	}

	@Transactional(readOnly = false)
	public void saveResourceBundleList(List<ResourceBundleVO> resourceBundleVOList, Integer sourceTypeId)
			throws Exception {
		try {
			List<ResourceBundle> resourceBundleList = new ArrayList<>();
			List<ResourceBundleVO> resourceBundleVOObj = new ArrayList<>();
			String action = "";
			UserDetailsVO detailsVO = null;
			if ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes() != null) {
				detailsVO = detailsService.getUserDetails();
			} else {
				detailsVO = new UserDetailsVO("anonymous-user", "anonymous", Arrays.asList("anonymous"),
						"anonymous-user");
			}
			String user = detailsVO.getUserName();

			if (!CollectionUtils.isEmpty(resourceBundleVOList)) {

				for (ResourceBundleVO resourceBundleVO : resourceBundleVOList) {
					String resourceBundleKey = resourceBundleVO.getResourceKey();
					String savedResourceBundleKey = iResourceBundleRepository.checkResourceKeyExist(resourceBundleKey);
					if (null == savedResourceBundleKey) {
						action = Constants.Action.ADD.getAction();
					} else {
						action = Constants.Action.EDIT.getAction();
						resourceBundleVOObj = iResourceBundleRepository.findResourceBundleByKey(resourceBundleKey);
					}
					if (resourceBundleVO.getText() != null && !resourceBundleVO.getText().equals("")) {
						ResourceBundle resourceBundle = convertResourceBundleVOToEntity(resourceBundleKey,
								resourceBundleVO);
						if (action.equals(Constants.Action.ADD.getAction())) {
							resourceBundle.setCreatedBy(user);
							resourceBundle.setCreatedDate(new Date());
						} else {
							resourceBundle.setCreatedBy(resourceBundleVOObj.get(0).getCreatedBy());
							resourceBundle.setCreatedDate(resourceBundleVOObj.get(0).getCreatedDate());
							resourceBundle.setUpdatedBy(user);
							resourceBundle.setUpdatedDate(new Date());
						}
						resourceBundleList.add(resourceBundle);

					}

				}

				for (ResourceBundle resourceBundle : resourceBundleList) {
					iResourceBundleRepository.saveAndFlush(resourceBundle);
					iResourceBundleRepository.save(resourceBundle);
				}
				Map<String, List<ResourceBundleVO>> resourceBundleMap = resourceBundleVOList.stream()
						.collect(Collectors.groupingBy(e -> e.getResourceKey()));
				for (Entry<String, List<ResourceBundleVO>> resourceBundleVO : resourceBundleMap.entrySet()) {
					List<ResourceBundleVO> voObjList = resourceBundleVO.getValue();
					String key = resourceBundleVO.getKey();
					moduleVersionService.saveModuleVersion(voObjList, null, key, "jq_resource_bundle", sourceTypeId);

				}

			}
		} catch (Exception ex) {
			logger.error("Error occurred while saving resource bundle data: ", ex);
			throw new RuntimeException("Error ocurred while saving resource bundle data");
		}
	}

}