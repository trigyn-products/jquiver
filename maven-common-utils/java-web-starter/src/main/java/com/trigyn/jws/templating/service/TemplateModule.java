package com.trigyn.jws.templating.service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.DownloadUploadModule;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dbutils.vo.xml.MetadataXMLVO;
import com.trigyn.jws.dbutils.vo.xml.Modules;
import com.trigyn.jws.dbutils.vo.xml.TemplateExportVO;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.dynarest.service.DynaRestModule;
import com.trigyn.jws.templating.dao.DBTemplatingRepository;
import com.trigyn.jws.templating.dao.TemplateDAO;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.utils.Constant;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.webstarter.utils.ImportExportUtility;
import com.trigyn.jws.webstarter.utils.XMLUtil;
import com.trigyn.jws.webstarter.xml.PermissionXMLVO;

@Component("template")
public class TemplateModule implements DownloadUploadModule<TemplateMaster> {

	@Autowired
	private PropertyMasterDAO					propertyMasterDAO		= null;

	@Autowired
	private TemplateDAO							templateDAO				= null;

	@Autowired
	private DBTemplatingRepository				dbTemplatingRepository	= null;

	@Autowired
	private FileUtilities						fileUtilities			= null;

	@Autowired
	private ModuleVersionService				moduleVersionService	= null;

	private Map<String, Map<String, Object>>	moduleDetailsMap		= new HashMap<>();

	@Autowired
	private IUserDetailsService					detailsService			= null;
	
	@Autowired
	private ImportExportUtility 				importExportUtility 	= null;
	
	@Autowired
	private JwsEntityRoleAssociationRepository	entityRoleAssociationRepository	= null;
	
	@Autowired
	private DynaRestModule	dynaRestModule	= null;
	
	Map<String, String> moduleListMap = null;
	String htmlTableJSON;

	@Override
	public void downloadCodeToLocal(TemplateMaster a_templateMaster, String folderLocation) throws Exception {
		List<TemplateMaster>	templates	= new ArrayList<>();
		List<TemplateVO>		templateVOs	= new ArrayList<>();
		Set<String> existingModuleIDs = new HashSet<>();
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode jsonHtmlArr = mapper.createArrayNode();
		ArrayNode jsonPermArr = mapper.createArrayNode();
		
		if (a_templateMaster != null) {
			templates.add(a_templateMaster);
			templateVOs = templates.stream().map((template) -> new TemplateVO(template.getTemplateId(), template.getTemplateName(),
					template.getTemplate(), template.getTemplateTypeId(), template.getChecksum(), template.getCreatedBy(),template.getUpdatedBy(), template.getUpdatedDate()))
					.collect(Collectors.toList());
		} else {
			templateVOs = getAllDefaultTemplates();
		}

		String	ftlCustomExtension	= Constant.CUSTOM_FILE_EXTENSION;
		String	templateDirectory	= Constant.TEMPLATE_DIRECTORY_NAME;
		// String folderLocation = propertyMasterDAO.findPropertyMasterValue("system",
		// "system", "template-storage-path");
		folderLocation = folderLocation + File.separator + templateDirectory;
		MetadataXMLVO						metadataXMLVO		= null;
		String								version				= propertyMasterDAO.findPropertyMasterValue("system", "system", "version");
		UserDetailsVO						detailsVO			= detailsService.getUserDetails();
		String								userName			= detailsVO.getUserName();
		Map<String, Map<String, Object>>	moduleDetailsMap	= new HashMap<>();

		if (!new File(folderLocation).exists()) {
			File fileDirectory = new File(folderLocation);
			fileDirectory.mkdirs();
		} else {
			File file = new File(folderLocation + File.separator + "metadata.xml");
			if (file.exists()) {
				metadataXMLVO = (MetadataXMLVO) unMarshaling(MetadataXMLVO.class, file.getAbsolutePath());
				if (metadataXMLVO != null && metadataXMLVO.getExportModules() != null &&
				        metadataXMLVO.getExportModules().getModule() != null) {
				        for (Modules vo : metadataXMLVO.getExportModules().getModule()) {
				            existingModuleIDs.add(vo.getModuleID());
				        }
				    }
			}
		}
		
		Map<String, XMLVO> xmlVOMap = new HashMap<>();
		String targetLocation = null;

		targetLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		
		MetadataXMLVO metaXMLVO = importExportUtility.readMetaDataXML(targetLocation);
		if(metadataXMLVO != null && metadataXMLVO.getInfo() != null && metadataXMLVO.getInfo() != "" ) {
		//	existingLocalData = new JSONArray(metadataXMLVO.getInfo());
		}
		xmlVOMap = importExportUtility.readFiles(targetLocation, metaXMLVO, moduleListMap);

		XMLVO xmlVO = xmlVOMap.get(Constant.PERMISSION + ".xml");

		PermissionXMLVO permissionXMLVO = (xmlVO == null) ? null : (PermissionXMLVO) xmlVO;

		Map<String, Object> moduleListMap = new HashMap<>();

		for (TemplateVO templateVO : templateVOs) {
			if (existingModuleIDs.contains(templateVO.getTemplateId())) {
		        continue; // Skip if already present in XML
		    }
			
			Map<String, Object> map = new HashMap<>();

			Map<String, Integer> positionMap = new HashMap<>();
			if (permissionXMLVO != null && permissionXMLVO.getJwsRoleDetails().isEmpty() == false) {
				int counter = 0;
				for (JwsEntityRoleAssociation permission : permissionXMLVO.getJwsRoleDetails()) {
					positionMap.put(permission.getEntityRoleId(), counter);
					counter = counter + 1;
				}
			}
			
			List<JwsEntityRoleAssociation> roles = entityRoleAssociationRepository.getEntityRoles(templateVO.getTemplateId(), Constant.TEMP_MOD_ID);
			for (JwsEntityRoleAssociation role : roles) {
				ObjectNode modulePermObj = mapper.createObjectNode();
			    permissionXMLVO.getJwsRoleDetails().add(role.getObject());
			    modulePermObj.put("moduleType", "Permission");
			    modulePermObj.put("moduleID", role.getEntityRoleId());
			    modulePermObj.put("moduleName", role.getEntityName());
			    modulePermObj.put("moduleVersion", "NA");
			    jsonPermArr.add(modulePermObj);
			}

			
			
			
			String	newFileCheckSum	= null;
			File	file			= new File(folderLocation + File.separator + templateVO.getTemplateName() + ftlCustomExtension);
			if (file.exists()) {

				String	generatedFileCheckSum	= fileUtilities.generateFileChecksum(file);
				String	existingCheckSum		= templateVO.getChecksum();
				if (!generatedFileCheckSum.equalsIgnoreCase(existingCheckSum)) {
					file.delete();
					newFileCheckSum = fileUtilities.writeFileContents(templateVO.getTemplate(), file);
					templateVO.setChecksum(newFileCheckSum);
				} else {
					templateVO.setChecksumChanged(false);
				}
			} else {
				newFileCheckSum = fileUtilities.writeFileContents(templateVO.getTemplate(), file);
				templateVO.setChecksum(newFileCheckSum);
			}
	//	}
		//for (TemplateVO templateVO : templateVOs) {
			if (templateVO.isChecksumChanged()) {
				templateDAO.updateChecksum(templateVO);

				TemplateExportVO	temMaster	= new TemplateExportVO(templateVO.getTemplateId(), templateVO.getTemplateName(),
						templateVO.getTemplateTypeId(), templateVO.getTemplateName() + ftlCustomExtension, templateVO.getUpdatedDate(), templateVO.getUpdatedBy(), templateVO.getCreatedBy(), newFileCheckSum);

		//		Map<String, Object>	map			= new HashMap<>();
				map.put("moduleName", templateVO.getTemplateName());
				map.put("moduleObject", temMaster);
				moduleDetailsMap.put(templateVO.getTemplateId(), map);
				
				// Build JSON object for exportData
				ObjectNode moduleObj = mapper.createObjectNode();
				moduleObj.put("moduleType", "Templates");
				moduleObj.put("moduleID", templateVO.getTemplateId());
				moduleObj.put("moduleName", templateVO.getTemplateName());
				moduleObj.put("moduleVersion", "1.0");

				// Add object to array
				jsonHtmlArr.add(moduleObj);
				
				moduleListMap.put("DynaRest", jsonHtmlArr);
				moduleListMap.put("Permission", jsonPermArr);

			}
		}
		
		htmlTableJSON = dynaRestModule.exportConfigData(folderLocation, null , moduleListMap,true);
		if (permissionXMLVO != null)
			XMLUtil.marshaling(permissionXMLVO, "Permission", targetLocation);
		
		generateMetadataXML(metadataXMLVO, moduleDetailsMap, folderLocation, version, userName);
	}

	@Override
	public void uploadCodeToDB(String templateTypeID,String uploadFileName) throws Exception {
		String	user				= "admin";
		String	ftlCustomExtension	= Constant.CUSTOM_FILE_EXTENSION;
		String	templateDirectory	= Constant.TEMPLATE_DIRECTORY_NAME;
		String	folderLocation		= propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation = folderLocation + File.separator + templateDirectory;
		File directory = new File(folderLocation);

		if (!directory.exists()) {
			throw new Exception("No such directory present");
		}

		MetadataXMLVO	metadataXMLVO	= null;
		File			metadatFile		= new File(folderLocation + File.separator + "metadata.xml");
		if (metadatFile.exists() && metadatFile.isFile() && metadatFile.getName().equals("metadata.xml")) {
			metadataXMLVO = (MetadataXMLVO) unMarshaling(MetadataXMLVO.class, metadatFile.getAbsolutePath());
		}

		for (Modules module : metadataXMLVO.getExportModules().getModule()) {
			String				moduleID			= module.getModuleID();
			TemplateExportVO	templateExportVO	= module.getTemplate();

			FilenameFilter		textFilter			= new FilenameFilter() {
														public boolean accept(File dir, String name) {
															if (!StringUtils.isBlank(uploadFileName)) {
																return name.toLowerCase()
																		.equalsIgnoreCase(uploadFileName + ftlCustomExtension);
															} else {
																return name.toLowerCase().endsWith(ftlCustomExtension);
															}
														}
													};
			File[]				files				= directory.listFiles(textFilter);
			for (File file : files) {
				if (file.isFile()) {
					String fileName = file.getName();
					Integer templateTypeId = templateExportVO.getTemplateTypeId();
					if (Integer.parseInt(templateTypeID) == templateTypeId && fileName.equals(templateExportVO.getTemplateFileName())) {
						TemplateMaster	template				= templateDAO.findTemplateById(moduleID);

						String			content					= fileUtilities.readContentsOfFile(file.getAbsolutePath());
						String			generateFileCheckSum	= fileUtilities.generateFileChecksum(file);
						if (template == null) {
							template = new TemplateMaster();
							template.setTemplate(content);
							template.setChecksum(generateFileCheckSum);
							template.setTemplateName(templateExportVO.getTemplateName());
							template.setUpdatedDate(new Date());
							template.setUpdatedBy(user);
							template.setCreatedBy(user);
							dbTemplatingRepository.save(template);
						} else {
							boolean isSave = false;
							if (!generateFileCheckSum.equalsIgnoreCase(template.getChecksum())) {
								template.setTemplate(content);
								template.setChecksum(generateFileCheckSum);
								template.setUpdatedBy(user);
								template.setUpdatedDate(new Date());
								isSave = true;
							}
							if (!template.getTemplateName().equals(templateExportVO.getTemplateName())) {
								template.setTemplateName(templateExportVO.getTemplateName());
								isSave = true;
							}

							if (!template.getTemplateId().equals(templateExportVO.getTemplateId())) {
								template.setTemplateTypeId(templateExportVO.getTemplateTypeId());
								isSave = true;
							}
							if (isSave)
								dbTemplatingRepository.save(template);
						}
						TemplateVO templateVO = new TemplateVO(template.getTemplateId(), template.getTemplateName(),
								template.getTemplate(), template.getTemplateTypeId(), template.getChecksum(), template.getCreatedBy(), template.getUpdatedBy(), new Date());
						moduleVersionService.saveModuleVersion(templateVO, null, template.getTemplateId(), "jq_template_master",
								Constant.UPLOAD_SOURCE_VERSION_TYPE);
					}
				}
			}
		}

	}

	@Override
	public void exportData(Object object, String folderLocation) throws Exception {
		TemplateMaster			a_templateMaster	= (TemplateMaster) object;
		List<TemplateMaster>	templates			= new ArrayList<>();
		List<TemplateVO>		templateVOs			= new ArrayList<>();
		if (a_templateMaster != null) {
			templates.add(a_templateMaster);
			templateVOs = templates.stream().map((template) -> new TemplateVO(template.getTemplateId(), template.getTemplateName(),
					template.getTemplate(),  template.getTemplateTypeId(), template.getChecksum(), template.getCreatedBy(), template.getUpdatedBy() ,template.getUpdatedDate()))
					.collect(Collectors.toList());
		} else {
			templateVOs = getAllDefaultTemplates();
		}

		String	ftlCustomExtension	= Constant.CUSTOM_FILE_EXTENSION;
		String	templateDirectory	= Constant.TEMPLATE_DIRECTORY_NAME;
		// String folderLocation = propertyMasterDAO.findPropertyMasterValue("system",
		// "system", "template-storage-path");
		folderLocation = folderLocation + File.separator + templateDirectory;

		if (!new File(folderLocation).exists()) {
			File fileDirectory = new File(folderLocation);
			fileDirectory.mkdirs();
		}

		for (TemplateVO templateVO : templateVOs) {
			String	newFileCheckSum	= null;
			File	file			= new File(folderLocation + File.separator + templateVO.getTemplateName() + ftlCustomExtension);
			if (file.exists()) {

				String	generatedFileCheckSum	= fileUtilities.generateFileChecksum(file);
				String	existingCheckSum		= templateVO.getChecksum();
				if (!generatedFileCheckSum.equalsIgnoreCase(existingCheckSum)) {
					file.delete();
					newFileCheckSum = fileUtilities.writeFileContents(templateVO.getTemplate(), file);
					templateVO.setChecksum(newFileCheckSum);
				} else {
					templateVO.setChecksumChanged(false);
				}
			} else {
				newFileCheckSum = fileUtilities.writeFileContents(templateVO.getTemplate(), file);
				templateVO.setChecksum(newFileCheckSum);
			}
			TemplateExportVO	temMaster	= new TemplateExportVO(templateVO.getTemplateId(), templateVO.getTemplateName(),
					templateVO.getTemplateTypeId(), templateVO.getTemplateName() + ftlCustomExtension, templateVO.getUpdatedDate(), templateVO.getUpdatedBy(), templateVO.getCreatedBy(), newFileCheckSum);

			Map<String, Object>	map			= new HashMap<>();
			map.put("moduleName", templateVO.getTemplateName());
			map.put("moduleObject", temMaster);
			moduleDetailsMap.put(templateVO.getTemplateId(), map);
		}
		for (TemplateVO templateVO : templateVOs) {
			if (templateVO.isChecksumChanged()) {
				templateDAO.updateChecksum(templateVO);
			}
		}

	}

	@Override
	public Object importData(String folderLocation, String uploadFileName, String uploadID, Object importObject) throws Exception {
		String				user				= "admin";
		TemplateExportVO	templateExportVO	= (TemplateExportVO) importObject;
		String				templateFileName	= templateExportVO.getTemplateFileName();
		int					lastDotIndex		= templateFileName.lastIndexOf('.');
		final String		ftlCustomExtension	= (lastDotIndex != -1)
				? "." + templateFileName.substring(lastDotIndex + 1)
				: "";
			
		TemplateMaster		template			= null;
		File				directory			= new File(folderLocation);
		if (!directory.exists()) {
			throw new Exception("No such directory present");
		}

		FilenameFilter	textFilter	= new FilenameFilter() {
										public boolean accept(File dir, String name) {
											if (!StringUtils.isBlank(uploadFileName)) {
												return name.toLowerCase().equalsIgnoreCase(uploadFileName + ftlCustomExtension);
											} else {
												return name.toLowerCase().endsWith(ftlCustomExtension);
											}
										}
									};
		File[]			files		= directory.listFiles(textFilter);
		for (File file : files) {
			if (file.isFile()) {
				String fileName = file.getName();

				if (fileName.equals(templateExportVO.getTemplateFileName())) {
					TemplateMaster	templateEntity			= templateDAO.findTemplateById(uploadID);
					String			content					= fileUtilities.readContentsOfFile(file.getAbsolutePath());
					String			generateFileCheckSum	= fileUtilities.generateFileChecksum(file);
					if (templateEntity == null) {
						template = new TemplateMaster();
						template.setTemplate(content);
						template.setChecksum(generateFileCheckSum);
						template.setTemplateId(templateExportVO.getTemplateId());
						template.setTemplateName(templateExportVO.getTemplateName());
						template.setTemplateTypeId(templateExportVO.getTemplateTypeId());
						template.setUpdatedDate(templateExportVO.getUpdatedDate());
						template.setUpdatedBy(user);
						template.setCreatedBy(user);
					} else {
						template = new TemplateMaster();
						template.setTemplateId(templateExportVO.getTemplateId());
						template.setCreatedBy(templateEntity.getCreatedBy());
						template.setTemplateName(templateExportVO.getTemplateName());
						template.setTemplateTypeId(templateExportVO.getTemplateTypeId());
						template.setTemplate(content);
						template.setChecksum(generateFileCheckSum);
						template.setUpdatedBy(user);
						template.setUpdatedDate(templateExportVO.getUpdatedDate());
					}
				}
			}
		}
		return template;
	}

	public Map<String, Map<String, Object>> getModuleDetailsMap() {
		return moduleDetailsMap;
	}

	public void setModuleDetailsMap(Map<String, Map<String, Object>> moduleDetailsMap) {
		this.moduleDetailsMap = moduleDetailsMap;
	}

	public List<TemplateVO> getAllDefaultTemplates() {
		List<TemplateVO> templateVOs = dbTemplatingRepository.getAllDefaultTemplates(Constant.DEFAULT_TEMPLATE_TYPE);
		return templateVOs;
	}

}
