package com.trigyn.jws.dashboard.service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dashboard.dao.DashletDAO;
import com.trigyn.jws.dashboard.entities.Dashlet;
import com.trigyn.jws.dashboard.repository.interfaces.IDashletRepository;
import com.trigyn.jws.dashboard.utility.Constants;
import com.trigyn.jws.dashboard.vo.DashletVO;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.DownloadUploadModule;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dbutils.vo.xml.DashletExportVO;
import com.trigyn.jws.dbutils.vo.xml.MetadataXMLVO;
import com.trigyn.jws.dbutils.vo.xml.Modules;

@Component("dashlet")
public class DashletModule implements DownloadUploadModule<Dashlet> {

	@Autowired
	private DashletDAO							dashletDAO				= null;

	@Autowired
	private PropertyMasterDAO					propertyMasterDAO		= null;

	@Autowired
	private FileUtilities						fileUtilities			= null;

	@Autowired
	private IDashletRepository					iDashletRepository		= null;

	@Autowired
	private ModuleVersionService				moduleVersionService	= null;

	private Map<String, Map<String, Object>>	moduleDetailsMap		= new HashMap<>();

	@Autowired
	private IUserDetailsService					detailsService			= null;

	@Override
	public void downloadCodeToLocal(Dashlet a_dashlet, String folderLocation) throws Exception {

		List<Dashlet> dashlets = new ArrayList<>();
		if (a_dashlet != null) {
			dashlets.add(a_dashlet);
		} else {
			dashlets = dashletDAO.getAllDashlets(Constants.DEFAULT_DASHLET_TYPE_ID);
		}

		String	ftlCustomExtension	= ".tgn";
		String	templateDirectory	= Constants.DASHLET_DIRECTORY_NAME;
		String	selectQuery			= "selectQuery";
		String	htmlBody			= "htmlContent";
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
			}
		}

		for (Dashlet dashlet : dashlets) {
			boolean	isCheckSumChanged	= false;
			String	dashletName			= dashlet.getDashletName();
			String	formFolder			= folderLocation + File.separator + dashletName;
			if (!new File(formFolder).exists()) {
				File fileDirectory = new File(formFolder);
				fileDirectory.mkdirs();
			}
			// html
			String dashletBodySum = fileUtilities.checkFileContents(htmlBody, formFolder, dashlet.getDashletBody(),
					dashlet.getDashletBodyChecksum(), ftlCustomExtension);
			if (dashletBodySum != null) {
				isCheckSumChanged = true;
				dashlet.setDashletBodyChecksum(dashletBodySum);
			}

			// query
			String dashletQueryCheckSum = fileUtilities.checkFileContents(selectQuery, formFolder, dashlet.getDashletQuery(),
					dashlet.getDashletQueryChecksum(), ftlCustomExtension);
			if (dashletQueryCheckSum != null) {
				isCheckSumChanged = true;
				dashlet.setDashletQueryChecksum(dashletQueryCheckSum);
			}

			// save checksum
			if (isCheckSumChanged) {
				iDashletRepository.save(dashlet);

				DashletExportVO		dashletExportVO	= new DashletExportVO(dashlet.getDashletId(), dashlet.getDashletName(),
						dashlet.getDashletTitle(), dashlet.getXCoordinate(), dashlet.getYCoordinate(), dashlet.getWidth(),
						dashlet.getHeight(), dashlet.getContextId(), dashlet.getShowHeader(), dashlet.getIsActive(),
						dashlet.getDashletTypeId(), selectQuery + ftlCustomExtension, htmlBody + ftlCustomExtension);

				Map<String, Object>	map				= new HashMap<>();
				map.put("moduleName", dashletName);
				map.put("moduleObject", dashletExportVO);
				moduleDetailsMap.put(dashlet.getDashletId(), map);

				List<Modules> moduleList = new ArrayList<>();
				if (metadataXMLVO != null && metadataXMLVO.getExportModules() != null
						&& metadataXMLVO.getExportModules().getModule() != null) {
					for (Modules vo : metadataXMLVO.getExportModules().getModule()) {
						if (!vo.getModuleID().equals(dashlet.getDashletId())) {
							moduleList.add(vo);
						}
					}
					metadataXMLVO.getExportModules().setModule(moduleList);
				}
			}
		}
		generateMetadataXML(metadataXMLVO, moduleDetailsMap, folderLocation, version, userName);
	}

	@Override
	public void uploadCodeToDB(String uploadFileName) throws Exception {
		String	user				= "admin";
		String	templateDirectory	= Constants.DASHLET_DIRECTORY_NAME;
		String	folderLocation		= propertyMasterDAO.findPropertyMasterValue("system", "system", Constants.TEMPORARY_STORAGE_PATH);
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
			String			moduleID			= module.getModuleID();
			String			moduleName			= module.getModuleName();
			DashletExportVO	dashletExportVO		= module.getDashlet();
			String			selectQuery			= dashletExportVO.getSelectQueryFileName();
			String			htmlBody			= dashletExportVO.getHtmlBodyFileName();
			String			ftlCustomExtension	= "." + dashletExportVO.getHtmlBodyFileName().split("\\.")[1];

			FilenameFilter	textFilter			= new FilenameFilter() {
													public boolean accept(File dir, String name) {
														return name.toLowerCase().endsWith(ftlCustomExtension);
													}
												};

			File[]			directories			= directory.listFiles((new FilenameFilter() {
													@Override
													public boolean accept(File current, String name) {
														if (!StringUtils.isBlank(uploadFileName)) {
															if (name.equalsIgnoreCase(uploadFileName)) {
																return new File(current, name).isDirectory();
															}
														} else {
															return new File(current, name).isDirectory();
														}
														return false;
													}
												}));

			for (File currentDirectory : directories) {
				String	selectCheckSum			= null;
				String	htmlCheckSum			= null;
				String	currentDirectoryName	= currentDirectory.getName();

				if (currentDirectoryName.equals(uploadFileName) && currentDirectoryName.equals(moduleName)) {
					Dashlet dashlet = dashletDAO.findById(moduleID);

					if (dashlet == null) {
						dashlet = new Dashlet();
						dashlet.setCreatedBy(user);
						dashlet.setCreatedDate(new Date());
						dashlet.setDashletName(currentDirectoryName);
						dashlet.setDashletTitle("Uploaded from Local Directory");
					}
					dashlet.setDashletName(currentDirectoryName);
					dashlet.setDashletTitle(dashletExportVO.getDashletTitle());
					dashlet.setContextId(dashletExportVO.getContextId());
					dashlet.setDashletTypeId(dashletExportVO.getDashletTypeId());
					dashlet.setHeight(dashletExportVO.getHeight());
					dashlet.setIsActive(dashletExportVO.getIsActive());
					dashlet.setShowHeader(dashletExportVO.getShowHeader());
					dashlet.setWidth(dashletExportVO.getWidth());
					dashlet.setXCoordinate(dashletExportVO.getxCoordinate());
					dashlet.setYCoordinate(dashletExportVO.getyCoordinate());

					File[]	directoryFiles	= currentDirectory.listFiles(textFilter);
					Integer	filesPresent	= directoryFiles.length;
					if (filesPresent == 2) {
						File	selectFile		= new File(currentDirectory.getAbsolutePath() + File.separator + selectQuery);
						File	hmtlBodyFile	= new File(currentDirectory.getAbsolutePath() + File.separator + htmlBody);
						if (!selectFile.exists() || !hmtlBodyFile.exists()) {
							throw new Exception(
									"selectQuery  file not and hmtlQueryfile are mandatory  for saving dashlet" + currentDirectoryName);
						} else {
							// set select
							selectCheckSum = fileUtilities.generateFileChecksum(selectFile);
							if (!selectCheckSum.equalsIgnoreCase(dashlet.getDashletQueryChecksum())) {

								dashlet.setDashletQuery(fileUtilities.readContentsOfFile(selectFile.getAbsolutePath()));
								dashlet.setDashletQueryChecksum(selectCheckSum);
							}

							// set html
							htmlCheckSum = fileUtilities.generateFileChecksum(hmtlBodyFile);
							if (!htmlCheckSum.equalsIgnoreCase(dashlet.getDashletBodyChecksum())) {

								dashlet.setDashletBody(fileUtilities.readContentsOfFile(hmtlBodyFile.getAbsolutePath()));
								dashlet.setDashletBodyChecksum(htmlCheckSum);
							}
							iDashletRepository.save(dashlet);
							saveDashletVersioning(dashlet);
						} // saveQuery
					} else {
						throw new Exception("Invalid count of files for saving dashlet" + currentDirectoryName);
					}
				}
			}
		}

	}

	@Override
	public void exportData(Object object, String folderLocation) throws Exception {
		Dashlet			a_dashlet	= (Dashlet) object;
		List<Dashlet>	dashlets	= new ArrayList<>();
		if (a_dashlet != null) {
			dashlets.add(a_dashlet);
		} else {
			dashlets = dashletDAO.getAllDashlets(Constants.DEFAULT_DASHLET_TYPE_ID);
		}

		String	ftlCustomExtension	= ".tgn";
		String	templateDirectory	= Constants.DASHLET_DIRECTORY_NAME;
		String	selectQuery			= "selectQuery";
		String	htmlBody			= "htmlContent";
		// String folderLocation = propertyMasterDAO.findPropertyMasterValue("system",
		// "system", "template-storage-path");
		folderLocation = folderLocation + File.separator + templateDirectory;

		for (Dashlet dashlet : dashlets) {
			boolean	isCheckSumChanged	= false;
			String	dashletName			= dashlet.getDashletName();
			String	formFolder			= folderLocation + File.separator + dashletName;
			if (!new File(formFolder).exists()) {
				File fileDirectory = new File(formFolder);
				fileDirectory.mkdirs();
			}
			// html
			String dashletBodySum = fileUtilities.checkFileContents(htmlBody, formFolder, dashlet.getDashletBody(),
					dashlet.getDashletBodyChecksum(), ftlCustomExtension);
			if (dashletBodySum != null) {
				isCheckSumChanged = true;
				dashlet.setDashletBodyChecksum(dashletBodySum);
			}

			// query
			String dashletQueryCheckSum = fileUtilities.checkFileContents(selectQuery, formFolder, dashlet.getDashletQuery(),
					dashlet.getDashletQueryChecksum(), ftlCustomExtension);
			if (dashletQueryCheckSum != null) {
				isCheckSumChanged = true;
				dashlet.setDashletQueryChecksum(dashletQueryCheckSum);
			}

			// save checksum
			if (isCheckSumChanged) {
				iDashletRepository.save(dashlet);
			}

			DashletExportVO		dashletExportVO	= new DashletExportVO(dashlet.getDashletId(), dashlet.getDashletName(),
					dashlet.getDashletTitle(), dashlet.getXCoordinate(), dashlet.getYCoordinate(), dashlet.getWidth(), dashlet.getHeight(),
					dashlet.getContextId(), dashlet.getShowHeader(), dashlet.getIsActive(), dashlet.getDashletTypeId(),
					selectQuery + ftlCustomExtension, htmlBody + ftlCustomExtension);

			Map<String, Object>	map				= new HashMap<>();
			map.put("moduleName", dashletName);
			map.put("moduleObject", dashletExportVO);
			moduleDetailsMap.put(dashlet.getDashletId(), map);
		}

	}

	@Override
	public Object importData(String folderLocation, String uploadFileName, String uploadID, Object importObject) throws Exception {
		String			user				= "admin";
		DashletExportVO	dashletExportVO		= (DashletExportVO) importObject;

		String			selectQuery			= dashletExportVO.getSelectQueryFileName();
		String			htmlBody			= dashletExportVO.getHtmlBodyFileName();
		String			ftlCustomExtension	= "." + dashletExportVO.getHtmlBodyFileName().split("\\.")[1];

		Dashlet			dashlet				= null;
		File			directory			= new File(folderLocation);
		if (!directory.exists()) {
			throw new Exception("No such directory present");
		}
		FilenameFilter	textFilter	= new FilenameFilter() {
										public boolean accept(File dir, String name) {
											return name.toLowerCase().endsWith(ftlCustomExtension);
										}
									};

		File[]			directories	= directory.listFiles((new FilenameFilter() {
										@Override
										public boolean accept(File current, String name) {
											if (!StringUtils.isBlank(uploadFileName)) {
												if (name.equalsIgnoreCase(uploadFileName)) {
													return new File(current, name).isDirectory();
												}
											} else {
												return new File(current, name).isDirectory();
											}
											return false;
										}
									}));

		for (File currentDirectory : directories) {
			String	selectCheckSum			= null;
			String	htmlCheckSum			= null;
			String	currentDirectoryName	= currentDirectory.getName();

			if (currentDirectoryName.equals(uploadFileName)) {
				Dashlet dashletEntity = dashletDAO.findById(uploadID);
				dashlet = new Dashlet();
				if (dashletEntity != null) {
					dashlet = dashletEntity.getObject();
				} else {
					dashlet.setCreatedBy(user);
					dashlet.setCreatedDate(new Date());
				}
				dashlet.setDashletId(dashletExportVO.getDashletId());
				dashlet.setDashletName(currentDirectoryName);
				dashlet.setDashletTitle(dashletExportVO.getDashletTitle());
				dashlet.setContextId(dashletExportVO.getContextId());
				dashlet.setDashletTypeId(dashletExportVO.getDashletTypeId());
				dashlet.setHeight(dashletExportVO.getHeight());
				dashlet.setIsActive(dashletExportVO.getIsActive());
				dashlet.setShowHeader(dashletExportVO.getShowHeader());
				dashlet.setWidth(dashletExportVO.getWidth());
				dashlet.setXCoordinate(dashletExportVO.getxCoordinate());
				dashlet.setYCoordinate(dashletExportVO.getyCoordinate());

				File[]	directoryFiles	= currentDirectory.listFiles(textFilter);
				Integer	filesPresent	= directoryFiles.length;
				if (filesPresent == 2) {
					File	selectFile		= new File(currentDirectory.getAbsolutePath() + File.separator + selectQuery);
					File	hmtlBodyFile	= new File(currentDirectory.getAbsolutePath() + File.separator + htmlBody);
					if (!selectFile.exists() || !hmtlBodyFile.exists()) {
						throw new Exception(
								"selectQuery  file not and hmtlQueryfile are mandatory  for saving dashlet" + currentDirectoryName);
					} else {
						// set select
						selectCheckSum = fileUtilities.generateFileChecksum(selectFile);

						dashlet.setDashletQuery(fileUtilities.readContentsOfFile(selectFile.getAbsolutePath()));
						dashlet.setDashletQueryChecksum(selectCheckSum);

						// set html
						htmlCheckSum = fileUtilities.generateFileChecksum(hmtlBodyFile);

						dashlet.setDashletBody(fileUtilities.readContentsOfFile(hmtlBodyFile.getAbsolutePath()));
						dashlet.setDashletBodyChecksum(htmlCheckSum);
					} // saveQuery
				} else {
					throw new Exception("Invalid count of files for saving dashlet" + currentDirectoryName);
				}
			} else {
				throw new Exception(currentDirectoryName + " directory not found.");
			}
		}
		return dashlet;

	}

	public Map<String, Map<String, Object>> getModuleDetailsMap() {
		return moduleDetailsMap;
	}

	public void setModuleDetailsMap(Map<String, Map<String, Object>> moduleDetailsMap) {
		this.moduleDetailsMap = moduleDetailsMap;
	}

	public void saveDashletVersioning(Dashlet dashlet) throws Exception {
		DashletVO dashletVO = convertDashletEntityToVO(dashlet);
		moduleVersionService.saveModuleVersion(dashletVO, null, dashlet.getDashletId(), "jq_dashlet", Constants.UPLOAD_SOURCE_VERSION_TYPE);
	}

	public DashletVO convertDashletEntityToVO(Dashlet dashlet) {
		DashletVO dashletVO = new DashletVO();
		dashletVO.setDashletId(dashlet.getDashletId());
		dashletVO.setDashletName(dashlet.getDashletName());
		dashletVO.setDashletTitle(dashlet.getDashletTitle());
		dashletVO.setXCoordinate(dashlet.getXCoordinate());
		dashletVO.setYCoordinate(dashlet.getYCoordinate());
		dashletVO.setWidth(dashlet.getWidth());
		dashletVO.setHeight(dashlet.getHeight());
		dashletVO.setHeight(dashlet.getHeight());
		dashletVO.setDashletQuery(dashlet.getDashletQuery());
		dashletVO.setDashletBody(dashlet.getDashletBody());
		dashletVO.setShowHeader(dashlet.getShowHeader());
		dashletVO.setContextId(dashlet.getContextId());
		dashletVO.setIsActive(dashlet.getIsActive());
		if (dashlet.getProperties() == null) {
			dashletVO.setDashletPropertVOList(new ArrayList<>());
		}
		return dashletVO;
	}
}
