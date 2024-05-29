package com.trigyn.jws.dynarest.cipher.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.utils.ApplicationContextUtils;
import com.trigyn.jws.dbutils.utils.CustomResponseEntity;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.vo.FileInfo;
import com.trigyn.jws.dbutils.vo.FileInfo.FileType;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.FileUpload;
import com.trigyn.jws.dynarest.service.CryptoUtils;
import com.trigyn.jws.dynarest.service.FilesStorageServiceImpl;
import com.trigyn.jws.dynarest.service.JwsDynamicRestDetailService;
import com.trigyn.jws.dynarest.utils.CryptoException;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;

import freemarker.core.StopException;

@Component
public class ScriptUtil {

	private final static Logger logger = LogManager.getLogger(ScriptUtil.class);

	@Autowired
	private PropertyMasterService propertyMasterService = null;

	@Autowired
	private FilesStorageServiceImpl filesStorageServiceImpl = null;

	@Autowired
	private JwsDynamicRestDetailService jwsDynamicRestDetailService = null;

	@Autowired
	private JwsDynarestDAO jwsDynarestDAO = null;

	private DBTemplatingService templatingService = null;

	private TemplatingUtils templatingUtils = null;

	private final static String JWS_SALT = "main alag duniya";

	private ActivityLog activitylog = null;
	
	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;

	public ScriptUtil() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

		if (propertyMasterService == null && ApplicationContextUtils.getApplicationContext() != null) {
			propertyMasterService = ApplicationContextUtils.getApplicationContext().getBean("propertyMasterService",
					PropertyMasterService.class);
		}

		if (filesStorageServiceImpl == null && ApplicationContextUtils.getApplicationContext() != null) {
			filesStorageServiceImpl = ApplicationContextUtils.getApplicationContext().getBean("filesStorageServiceImpl",
					FilesStorageServiceImpl.class);
		}

		if (jwsDynamicRestDetailService == null && ApplicationContextUtils.getApplicationContext() != null) {
			jwsDynamicRestDetailService = ApplicationContextUtils.getApplicationContext()
					.getBean("jwsDynamicRestDetailService", JwsDynamicRestDetailService.class);
		}

		if (jwsDynarestDAO == null && ApplicationContextUtils.getApplicationContext() != null) {
			jwsDynarestDAO = ApplicationContextUtils.getApplicationContext().getBean("jwsDynarestDAO",
					JwsDynarestDAO.class);
		}

		if (templatingUtils == null && ApplicationContextUtils.getApplicationContext() != null) {
			templatingUtils = ApplicationContextUtils.getApplicationContext().getBean("templatingUtils",
					TemplatingUtils.class);
		}

		if (templatingService == null && ApplicationContextUtils.getApplicationContext() != null) {
			templatingService = ApplicationContextUtils.getApplicationContext().getBean(DBTemplatingService.class);
		}

		if (activitylog == null && ApplicationContextUtils.getApplicationContext() != null) {
			activitylog = ApplicationContextUtils.getApplicationContext().getBean(ActivityLog.class);
		}
	}

	public String getSystemProperty(String a_propertyName) {
		if (a_propertyName == null) {
			return null;
		}

		return System.getProperty(a_propertyName);
	}

	public String getSystemEnvironment(String a_propertyName) {
		if (a_propertyName == null) {
			return null;
		}

		return System.getenv(a_propertyName);
	}

	public void updateCookies(String a_strKey, String a_strValue) {
		updateCookies(a_strKey, a_strValue, null, null);
	}

	public void updateCookies(String a_strKey, String a_strValue, Integer maxAge) {
		updateCookies(a_strKey, a_strValue, maxAge, null);
	}

	public void updateCookies(String a_strKey, String a_strValue, Boolean httpOnly) {
		updateCookies(a_strKey, a_strValue, null, httpOnly);
	}

	public void updateCookies(String a_strKey, String a_strValue, Integer maxAge, Boolean httpOnly) {
		if (StringUtils.isBlank(a_strKey) == false && StringUtils.isBlank(a_strValue) == false) {
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (sra != null && sra.getRequest() != null && sra.getRequest().getCookies() != null) {
				Cookie cookie = new Cookie(a_strKey, a_strValue);
				cookie.setPath("/");
				if (maxAge != null) {
					cookie.setMaxAge(maxAge);
				}
				if (httpOnly != null) {
					cookie.setHttpOnly(httpOnly);
				}
				sra.getResponse().addCookie(cookie);
			}
		}
	}

	public Cookie updateCookieSecurity(String a_strKey, Boolean isSecured) {
		if (StringUtils.isBlank(a_strKey) == false) {
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (sra != null && sra.getRequest() != null && sra.getRequest().getCookies() != null) {
				Cookie[] cookies = sra.getRequest().getCookies();
				for (Cookie cookie : cookies) {
					if (cookie != null && a_strKey.equals(cookie.getName())) {
						cookie.setSecure(isSecured);
						sra.getResponse().addCookie(cookie);
						return cookie;
					}
				}
				return null;
			}
		}
		return null;
	}

	public Cookie getCookiesFromRequest(String a_strKey) {
		if (StringUtils.isBlank(a_strKey) == false) {
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (sra != null && sra.getRequest() != null && sra.getRequest().getCookies() != null) {
				Cookie[] cookies = sra.getRequest().getCookies();
				for (Cookie cookie : cookies) {
					if (cookie != null && a_strKey.equals(cookie.getName())) {
						return cookie;
					}
				}
				return null;
			}
		}
		return null;
	}

	public boolean haveCookie(String a_strKey) {
		if (StringUtils.isBlank(a_strKey) == false) {
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (sra != null && sra.getRequest() != null && sra.getRequest().getCookies() != null) {
				Cookie[] cookies = sra.getRequest().getCookies();
				for (int counter = 0; counter < cookies.length; counter++) {
					Cookie cookie = cookies[counter];
					if (cookie != null && a_strKey.equals(cookie.getName())) {
						return true;
					}
				}
				return false;
			}
		}
		return false;
	}

	public void deleteCookie(String a_strKey) {
		if (StringUtils.isBlank(a_strKey) == false) {
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (sra != null && sra.getRequest() != null && sra.getRequest().getCookies() != null) {
				Cookie[] cookies = sra.getRequest().getCookies();
				for (Cookie cookie : cookies) {
					if (cookie != null && a_strKey.equals(cookie.getName())) {
						cookie.setMaxAge(0);
						sra.getResponse().addCookie(cookie);
					}
				}
			}
		}
	}

	public void updateSession(String a_strKey, String a_strValue) {
		if (StringUtils.isBlank(a_strKey) == false && StringUtils.isBlank(a_strValue) == false) {
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (sra != null && sra.getRequest() != null && sra.getRequest().getSession() != null) {
				sra.getRequest().getSession().setAttribute(a_strKey, a_strValue);
			}
		}
	}

	public <T> T getValueFromSession(String a_strKey) {
		if (StringUtils.isBlank(a_strKey) == false) {
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (sra != null && sra.getRequest() != null && sra.getRequest().getSession() != null) {
				return (T) sra.getRequest().getSession().getAttribute(a_strKey);
			}
		}
		return null;
	}

	public boolean haveSessionKey(String a_strKey) {
		if (StringUtils.isBlank(a_strKey) == false) {
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (sra != null && sra.getRequest() != null && sra.getRequest().getSession() != null
					&& sra.getRequest().getSession().getAttribute(a_strKey) != null) {
				return true;
			}
		}
		return false;
	}

	public void deleteSessionKey(String a_strKey) {
		if (StringUtils.isBlank(a_strKey) == false) {
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (sra != null && sra.getRequest() != null && sra.getRequest().getSession() != null) {
				sra.getRequest().getSession().removeAttribute(a_strKey);
			}
		}
	}

	public Long getCreationTime() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (sra != null && sra.getRequest() != null && sra.getRequest().getSession() != null) {
			return sra.getRequest().getSession().getCreationTime();
		}
		return null;
	}

	public Long getLastAccessedTime() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (sra != null && sra.getRequest() != null && sra.getRequest().getSession() != null) {
			return sra.getRequest().getSession().getLastAccessedTime();
		}
		return null;
	}

	public final Map<String, Object> getAllFiles(String a_filePath) {
		Map<String, Object> returnObject = new HashMap<String, Object>();

		if (StringUtils.isBlank(a_filePath)) {
			logger.error("File path has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "File path has not been provided");
			return returnObject;
		}
		try {
			File file = new File(a_filePath);
			if (file.exists() == false) {
				logger.error("File path doesnot exists");
				returnObject.put("actionStatus", "false");
				returnObject.put("_error", "File path doesnot exists");
				return returnObject;
			} else if (file.isDirectory() == false) {
				logger.error("Provided file path is not a directory.");
				returnObject.put("actionStatus", "false");
				returnObject.put("_error", "Provided file path is not a directory.");
				return returnObject;
			}

			List<FileInfo> fileInfoList = new ArrayList<>();
			File filesList[] = file.listFiles();
			for (File fileObj : filesList) {
				FileInfo fileInfo = new FileInfo();
				fileInfo.setFileId(fileObj.getName());
				fileInfo.setFileName(fileObj.getName());
				fileInfo.setFileType(FileType.Physical);
				fileInfo.setSizeInBytes(fileObj.length());
				fileInfo.setAbsolutePath(fileObj.getAbsolutePath());
				fileInfo.setCreatedTime(fileObj.lastModified());
				fileInfoList.add(fileInfo);
			}

			returnObject.put("actionStatus", "true");
			returnObject.put("files", fileInfoList);
			return returnObject;
		} catch (Exception e) {
			logger.error("Error while retrieving file: " + a_filePath + " : " + ExceptionUtils.getStackTrace(e));
			returnObject.put("actionStatus", "false");
			returnObject.put("_error",
					"Error while retrieving file: " + a_filePath + " : " + ExceptionUtils.getStackTrace(e));
			return returnObject;
		}
	}

	public final Map<String, String> deleteFile(String a_filePath) {
		Map<String, String> returnObject = new HashMap<String, String>();

		if (StringUtils.isBlank(a_filePath)) {
			logger.error("File path has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "File path has not been provided");
			return returnObject;
		}
		try {
			File file = new File(a_filePath);
			if (file.exists()) {
				file.delete();
			} else {
				logger.error("File doesnot exists: " + a_filePath);
				returnObject.put("actionStatus", "false");
				returnObject.put("_error", "File doesnot exists: " + a_filePath);
				return returnObject;
			}
			returnObject.put("actionStatus", "true");
			returnObject.put("absoluteFilePath", a_filePath);
			return returnObject;
		} catch (Exception e) {
			logger.error("Error while deleting file: " + a_filePath + " : " + ExceptionUtils.getStackTrace(e));
			returnObject.put("actionStatus", "false");
			returnObject.put("_error",
					"Error while deleting file: " + a_filePath + " : " + ExceptionUtils.getStackTrace(e));
			return returnObject;
		}
	}

	// target file name with extn
	public final Map<String, String> saveFile(String a_strFileContent, String a_strTargetFileName) {
		Map<String, String> returnObject = new HashMap<String, String>();

		if (StringUtils.isBlank(a_strFileContent)) {
			logger.error("File content has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "File content has not been provided");
			return returnObject;
		}
		if (StringUtils.isBlank(a_strTargetFileName)) {
			logger.error("Target file name has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "Target file name has not been provided");
			return returnObject;
		}
		String fileUploadLocation = null;
		try {
			fileUploadLocation = propertyMasterService.findPropertyMasterValue("file-upload-location");
			String absoluteFilePath = fileUploadLocation + File.separator + a_strTargetFileName;

			File file = new File(absoluteFilePath);
			if (file.exists()) {
				logger.error("File already exists at location: " + absoluteFilePath);
				returnObject.put("actionStatus", "false");
				returnObject.put("_error", "File already exists at location: " + absoluteFilePath);
				return returnObject;
			}
			file.createNewFile();
			Files.write(Paths.get(absoluteFilePath), a_strFileContent.getBytes());

			returnObject.put("actionStatus", "true");
			returnObject.put("absoluteFilePath", absoluteFilePath);
			return returnObject;
		} catch (IOException e) {
			logger.error("Error while creating file: " + a_strTargetFileName + " at location: " + fileUploadLocation
					+ " : " + ExceptionUtils.getStackTrace(e));
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "Error while creating file: " + a_strTargetFileName + " at location: "
					+ fileUploadLocation + " : " + ExceptionUtils.getStackTrace(e));
			return returnObject;
		} catch (Exception e) {
			logger.error("Error while creating file: " + a_strTargetFileName + " at location: " + fileUploadLocation
					+ " : " + ExceptionUtils.getStackTrace(e));
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "Error while creating file: " + a_strTargetFileName + " at location: "
					+ fileUploadLocation + " : " + ExceptionUtils.getStackTrace(e));
			return returnObject;
		}
	}

	public final Map<String, String> saveFileFromPath(String filePath, String a_strFileBinID, String a_strcontextID) {
		Map<String, String> returnObject = new HashMap<String, String>();

		if (StringUtils.isBlank(filePath)) {
			logger.error("File path has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "File path has not been provided");
			return returnObject;
		}
		if (StringUtils.isBlank(a_strFileBinID)) {
			logger.error("File bin id has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "File bin id has not been provided");
			return returnObject;
		}
		if (StringUtils.isBlank(a_strcontextID)) {
			logger.error("File association id has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "File association id has not been provided");
			return returnObject;
		}
		JdbcTemplate jdbcTemplate = jwsDynarestDAO.updateJdbcTemplateDataSource(null);

		String fileBinExistsQuery = "SELECT COUNT(*) AS cnt from jq_file_upload_config WHERE file_bin_id = '"
				+ a_strFileBinID + "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(fileBinExistsQuery);

		if (CollectionUtils.isEmpty(list) || (CollectionUtils.isEmpty(list) == false
				&& Integer.parseInt(String.valueOf(list.get(0).get("cnt"))) == 0)) {
			throw new RuntimeException("No file bin exists with id: " + a_strFileBinID);
		}

		try {
			LocalDate localDate = LocalDate.now();
			Integer year = localDate.getYear();
			Integer month = localDate.getMonthValue();
			Integer date = localDate.getDayOfMonth();
			String fileUploadDir = propertyMasterService.findPropertyMasterValue("file-upload-location");
			StringJoiner location = new StringJoiner("" + File.separatorChar);
			location.add(fileUploadDir);
			location.add(year.toString()).add(month.toString()).add(date.toString());
			if (Boolean.FALSE.equals(new File(location.toString()).exists())) {
				Files.createDirectories(Paths.get(location.toString()));
			}

			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);

			FileUpload fileUpload = filesStorageServiceImpl.saveFileDetails(location.toString(), file.getName(),
					a_strFileBinID, a_strcontextID);
			Path root = Paths.get(location.toString());
			Files.copy(fis, root.resolve(fileUpload.getPhysicalFileName()));
			CryptoUtils.encrypt(JWS_SALT, root.resolve(fileUpload.getPhysicalFileName()).toFile(),
					root.resolve(fileUpload.getPhysicalFileName()).toFile());
			fis.close();
			returnObject.put("actionStatus", "true");
			returnObject.put("fileUploadID", fileUpload.getFileUploadId());
			return returnObject;
		} catch (Exception a_exc) {
			logger.error(
					"Error while saving file from location: " + filePath + " : " + ExceptionUtils.getStackTrace(a_exc));
			returnObject.put("actionStatus", "false");
			returnObject.put("_error",
					"Error while saving file from location: " + filePath + " : " + ExceptionUtils.getStackTrace(a_exc));
			return returnObject;
		}

	}

	public final Map<String, String> saveFileBin(String a_strFileContent, String a_strTargetFileName,
			String a_strFileBinID, String a_strcontextID) {
		Map<String, String> returnObject = new HashMap<String, String>();

		if (StringUtils.isBlank(a_strFileContent)) {
			logger.error("File content has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "File content has not been provided");
			return returnObject;
		}
		if (StringUtils.isBlank(a_strTargetFileName)) {
			logger.error("Target file name has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "Target file name has not been provided");
			return returnObject;
		}
		if (StringUtils.isBlank(a_strFileBinID)) {
			logger.error("File bin id has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "File bin id has not been provided");
			return returnObject;
		}
		if (StringUtils.isBlank(a_strcontextID)) {
			logger.error("File association id has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "File association id has not been provided");
			return returnObject;
		}
		JdbcTemplate jdbcTemplate = jwsDynarestDAO.updateJdbcTemplateDataSource(null);

		String fileBinExistsQuery = "SELECT COUNT(*) AS cnt from jq_file_upload_config WHERE file_bin_id = '"
				+ a_strFileBinID + "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(fileBinExistsQuery);

		if (CollectionUtils.isEmpty(list) || (CollectionUtils.isEmpty(list) == false
				&& Integer.parseInt(String.valueOf(list.get(0).get("cnt"))) == 0)) {
			logger.error("No file bin exists with id: " + a_strFileBinID);
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "No file bin exists with id: " + a_strFileBinID);
			return returnObject;
		}

		try {
			LocalDate localDate = LocalDate.now();
			Integer year = localDate.getYear();
			Integer month = localDate.getMonthValue();
			Integer date = localDate.getDayOfMonth();
			String fileUploadDir = propertyMasterService.findPropertyMasterValue("file-upload-location");
			StringJoiner location = new StringJoiner("" + File.separatorChar);
			location.add(fileUploadDir);
			location.add(year.toString()).add(month.toString()).add(date.toString());
			if (Boolean.FALSE.equals(new File(location.toString()).exists())) {
				Files.createDirectories(Paths.get(location.toString()));
			}

			String absoluteFilePath = fileUploadDir + File.separator + a_strTargetFileName;
			Files.deleteIfExists(Paths.get(absoluteFilePath));

			File file = new File(absoluteFilePath);
			if (file.exists()) {
				throw new RuntimeException("File already exists at location: " + absoluteFilePath);
			}
			file.createNewFile();
			Files.write(Paths.get(absoluteFilePath), a_strFileContent.getBytes());
			File file1 = new File(absoluteFilePath);
			FileInputStream fis = new FileInputStream(file1);

			FileUpload fileUpload = filesStorageServiceImpl.saveFileDetails(location.toString(), file.getName(),
					a_strFileBinID, a_strcontextID);
			Path root = Paths.get(location.toString());
			Files.copy(fis, root.resolve(fileUpload.getPhysicalFileName()));
			CryptoUtils.encrypt(JWS_SALT, root.resolve(fileUpload.getPhysicalFileName()).toFile(),
					root.resolve(fileUpload.getPhysicalFileName()).toFile());

			fis.close();
			Files.deleteIfExists(Paths.get(absoluteFilePath));
			returnObject.put("actionStatus", "true");
			returnObject.put("fileUploadID", fileUpload.getFileUploadId());
			return returnObject;
		} catch (Exception a_exc) {
			logger.error(
					"Error while saving file : " + a_strTargetFileName + " : " + ExceptionUtils.getStackTrace(a_exc));
			returnObject.put("actionStatus", "false");
			returnObject.put("_error",
					"Error while saving file : " + a_strTargetFileName + " : " + ExceptionUtils.getStackTrace(a_exc));
			return returnObject;
		}
	}

	public final Map<String, String> copyFile(String sourceFilePath, String destinationFilePath) {
		return copyFile(sourceFilePath, destinationFilePath, new File(sourceFilePath).getName());
	}

	public final Map<String, String> copyFile(String sourceFilePath, String destinationFilePath,
			String a_strTargetFileName) {
		Map<String, String> returnObject = new HashMap<String, String>();

		if (StringUtils.isBlank(sourceFilePath)) {
			logger.error("Source file has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "Source file has not been provided");
			return returnObject;
		}
		if (StringUtils.isBlank(destinationFilePath)) {
			logger.error("Destination file path has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "Destination file path has not been provided");
			return returnObject;
		}
		File file = new File(sourceFilePath);
		if (file.exists() == false) {
			logger.error("File doesnot exists at location: " + sourceFilePath);
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "File doesnot exists at location: " + sourceFilePath);
			return returnObject;
		}
		File destFile = new File(destinationFilePath);
		if (destFile.exists() == false) {
			destFile.mkdirs();
		}

		try {
			FileInputStream fis = new FileInputStream(file);
			Path root = Paths.get(destinationFilePath);
			Files.copy(fis, root.resolve(a_strTargetFileName));
			fis.close();
			returnObject.put("actionStatus", "true");
			returnObject.put("copiedFileName", file.getName());
			return returnObject;
		} catch (IOException e) {
			logger.error("Error while copying file : " + ExceptionUtils.getStackTrace(e));
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "Error while copying file : " + ExceptionUtils.getStackTrace(e));
			return returnObject;
		}
	}

	public final Map<String, String> copyFileBinId(String a_strfileUploadID, String destinationFilePath) {
		Map<String, String> returnObject = new HashMap<String, String>();

		if (StringUtils.isBlank(a_strfileUploadID)) {
			logger.error("Source file bin id has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "Source file bin id has not been provided");
			return returnObject;
		}
		if (StringUtils.isBlank(destinationFilePath)) {
			logger.error("Destination file path has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "Destination file path has not been provided");
			return returnObject;
		}
		String query = "SELECT * FROM jq_file_upload WHERE file_upload_id = '" + a_strfileUploadID + "'";
		JdbcTemplate jdbcTemplate = jwsDynarestDAO.updateJdbcTemplateDataSource(null);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(query);
		if (CollectionUtils.isEmpty(list)) {
			logger.error("No file exit with id : " + a_strfileUploadID);
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "No file exit with id : " + a_strfileUploadID);
			return returnObject;
		}

		File destFile = new File(destinationFilePath);
		if (destFile.exists() == false) {
			destFile.mkdirs();
		}

		try {
			Map<String, Object> map = list.get(0);
			String filePath = map.get("file_path") + File.separator + map.get("physical_file_name");
			File file = new File(filePath);
			if (file.exists() == true) {
				byte[] bytes = FileCopyUtils.copyToByteArray(file);
				byte[] fileByteArray = CryptoUtils.decrypt(JWS_SALT, bytes, null);
				Path root = Paths.get(destinationFilePath);
				Files.write(root.resolve(file.getName()), fileByteArray);
				returnObject.put("actionStatus", "true");
				returnObject.put("copiedFileName", file.getName());
				return returnObject;
			} else {
				logger.error("No file exit at location : " + filePath);
				returnObject.put("actionStatus", "false");
				returnObject.put("_error", "No file exit at location : " + filePath);
				return returnObject;
			}
		} catch (IOException e) {
			logger.error("Error while copying file from file bin id : " + ExceptionUtils.getStackTrace(e));
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "Error while copying file : " + ExceptionUtils.getStackTrace(e));
			return returnObject;
		} catch (CryptoException e) {
			logger.error("Error while copying file from file bin id : " + ExceptionUtils.getStackTrace(e));
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "Error while copying file : " + ExceptionUtils.getStackTrace(e));
			return returnObject;
		}
	}

	public final Map<String, String> getFileContent(String a_strAbsolutePath) {
		Map<String, String> returnObject = new HashMap<String, String>();
		if (StringUtils.isBlank(a_strAbsolutePath)) {
			logger.error("File path has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "File path has not been provided");
			return returnObject;

		}
		File file = new File(a_strAbsolutePath);
		if (file.exists() == true) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ByteArrayResource arrayResource = null;
			try {
				byte[] bytes = FileCopyUtils.copyToByteArray(file);
				arrayResource = new ByteArrayResource(bytes);
				returnObject.put("actionStatus", "true");
				returnObject.put("Content : ", arrayResource.toString());
				return returnObject;
			} catch (IOException e) {
				logger.error("Error while getting file content : " + a_strAbsolutePath + " : "
						+ ExceptionUtils.getStackTrace(e));
				returnObject.put("actionStatus", "false");
				returnObject.put("_error", "Error while getting file content : " + a_strAbsolutePath + " : "
						+ ExceptionUtils.getStackTrace(e));
				return returnObject;
			} finally {
				try {
					bos.close();
				} catch (IOException a_exc) {
					logger.error("Error while getting file content : " + a_strAbsolutePath + " : "
							+ ExceptionUtils.getStackTrace(a_exc));
					returnObject.put("actionStatus", "false");
					returnObject.put("_error", "Error while getting file content : " + a_strAbsolutePath + " : "
							+ ExceptionUtils.getStackTrace(a_exc));
					return returnObject;
				}
			}
		} else {
			logger.error("No file exit at : " + a_strAbsolutePath);
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "No file exists at : " + a_strAbsolutePath);
			return returnObject;
		}
	}

	public final Map<String, String> getFileBinContent(String a_strfileUploadID) {
		Map<String, String> returnObject = new HashMap<String, String>();

		if (StringUtils.isBlank(a_strfileUploadID)) {
			logger.error("File upload id has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "File upload id has not been provided");
			return returnObject;
		}
		String query = "SELECT * FROM jq_file_upload WHERE file_upload_id = '" + a_strfileUploadID + "'";
		JdbcTemplate jdbcTemplate = jwsDynarestDAO.updateJdbcTemplateDataSource(null);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(query);

		if (list != null && !list.isEmpty()) {
			Map<String, Object> map = list.get(0);
			String filePath = map.get("file_path") + File.separator + map.get("physical_file_name");
			File file = new File(filePath);
			if (file.exists() == true) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ByteArrayResource arrayResource = null;
				try {
					byte[] bytes = FileCopyUtils.copyToByteArray(file);
					arrayResource = new ByteArrayResource(bytes);
					returnObject.put("actionStatus", "true");
					returnObject.put("Content : ", arrayResource.toString());
					return returnObject;
				} catch (IOException e) {
					logger.error("Error while getting file : " + ExceptionUtils.getStackTrace(e));
					returnObject.put("actionStatus", "false");
					returnObject.put("_error", "Error while getting file : " + ExceptionUtils.getStackTrace(e));
					return returnObject;
				} finally {
					try {
						bos.close();
					} catch (IOException e) {
						logger.error("Error while getting file : " + ExceptionUtils.getStackTrace(e));
						returnObject.put("actionStatus", "false");
						returnObject.put("_error", "Error while getting file : " + ExceptionUtils.getStackTrace(e));
						return returnObject;
					}
				}
			} else {
				logger.error("No file exit at location : " + filePath);
				returnObject.put("actionStatus", "false");
				returnObject.put("_error", "No file exit at location : " + filePath);
				return returnObject;
			}
		} else {
			logger.error("No file exit with id : " + a_strfileUploadID);
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "No file exit with id : " + a_strfileUploadID);
			return returnObject;
		}
	}

	public final Map<String, Object> getDBResult(String a_strQuery, String a_strdataSourceID,
			Map<String, Object> a_requestParams) {
		Map<String, Object> returnObject = new HashMap<String, Object>();
		if (StringUtils.isBlank(a_strQuery)) {
			logger.error("select query has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "select query has not been provided");
			return returnObject;
		}

		try {
			// if a_strdataSourceID is null then take default connection
			NamedParameterJdbcTemplate jdbcTemplate = jwsDynarestDAO
					.updateNamedParameterJdbcTemplateDataSource(a_strdataSourceID);

			if (a_strQuery.startsWith("EXEC")) {
				jdbcTemplate.queryForList(a_strQuery, a_requestParams);
			} else {
				List<Map<String, Object>> dataList = jdbcTemplate.queryForList(a_strQuery, a_requestParams);
				returnObject.put("data_list", dataList);
			}
			returnObject.put("actionStatus", "true");
			return returnObject;
		
		} catch (Exception e) {
			logger.error("Error while getting DB resultset : " + ExceptionUtils.getStackTrace(e));
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "Error while getting DB resultset : " + ExceptionUtils.getStackTrace(e));
			return returnObject;
		}
	}

	public final Map<String, Object> callStoredProcedure(String spName, String a_strdataSourceID,
			Map<String, Object> a_requestParams) {
		Map<String, Object> returnObject = new HashMap<String, Object>();

		if (StringUtils.isBlank(spName)) {
			returnObject.put("spName", "false");
			returnObject.put("_error", "Stored procedure has not been provided");
			return returnObject;
		}

		if (a_requestParams == null) {
			a_requestParams = new HashMap<>();
		}
		try {
			JdbcTemplate jdbcTemplate = jwsDynarestDAO.updateJdbcTemplateDataSource(a_strdataSourceID);
			// if a_strdataSourceID is null then take default connection
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(spName);
			DataSource dataSource = jdbcTemplate.getDataSource();
			try (Connection connection = dataSource.getConnection();) {
				simpleJdbcCall.setCatalogName(connection.getCatalog());
			} catch (SQLException a_sqlException) {
				logger.error(
						"Didn't find the schema name in datasource " + ExceptionUtils.getStackTrace(a_sqlException));
				returnObject.put("actionStatus", "false");
				returnObject.put("_error",
						"Didn't find the schema name in datasource " + ExceptionUtils.getStackTrace(a_sqlException));
				return returnObject;
			}
			SqlParameterSource in = new MapSqlParameterSource(a_requestParams);
			Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
			List<Map<String, Object>> list = (List<Map<String, Object>>) simpleJdbcCallResult.get("#result-set-1");

			returnObject.put("actionStatus", "true");
			returnObject.put("data_list", list);
			return returnObject;
		} catch (Exception e) {
			logger.error("Error while executing stored procedure : " + ExceptionUtils.getStackTrace(e));
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "Error while executing stored procedure : " + ExceptionUtils.getStackTrace(e));
			return returnObject;
		}
	}

	public final Map<String, String> updateDBQuery(String a_strQuery, String a_strdataSourceID,
			Map<String, Object> a_requestParams) {
		Map<String, String> returnObject = new HashMap<String, String>();
		// if a_strdataSourceID is null then take default connection

		if (StringUtils.isBlank(a_strQuery)) {
			logger.error("insert/update/delete query has not been provided");
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "insert/update/delete query has not been provided");
			return returnObject;
		}
		if (a_requestParams == null) {
			a_requestParams = new HashMap<>();
		}
		try {
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = jwsDynarestDAO
					.updateNamedParameterJdbcTemplateDataSource(a_strdataSourceID);
			int affectedRowCount = namedParameterJdbcTemplate.update(a_strQuery, a_requestParams);
			returnObject.put("affectedRowCount", String.valueOf(affectedRowCount));
			returnObject.put("actionStatus", "true");
			return returnObject;
		} catch (Exception e) {
			logger.error("Error while executing save/update query in updateDBQuery: "+ a_strQuery  + ExceptionUtils.getStackTrace(e));
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "Error while executing save/update query : "+ ExceptionUtils.getStackTrace(e));
			return returnObject;
		}
	}

	/**
	 * 
	 * For Internal API calls, URL should be prefix with JAPI not API
	 * 
	 * @param a_strRestXML the a_strRestXML to set
	 * @return customResponseEntity to be returned
	 */
	public final Object executeRESTCall(String a_strRestXML) throws CustomStopException {
		// return custom rest response object

		if (StringUtils.isBlank(a_strRestXML)) {
			throw new RuntimeException("Rest XML has not been provided");
		}
		try {
			CustomResponseEntity customResponseEntity = jwsDynamicRestDetailService.executeRestXML(a_strRestXML);
			return customResponseEntity;
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in executeRESTCall for Stop Exception.", custStopException);
			throw custStopException;
		}
	}

	public final Object sendMail(String a_strMailXML) throws CustomStopException {
		return sendMail(a_strMailXML, new HashMap<>());
	}

	public final Object sendMail(String a_strMailXML, Map<String, Object> requestParams) throws CustomStopException {
		// return response
		if (StringUtils.isBlank(a_strMailXML)) {
			throw new RuntimeException("Mail XML has not been provided");
		}
		try {
			ResponseEntity<?> response = jwsDynamicRestDetailService.executeSendMail(a_strMailXML, requestParams);
			return response;
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in executeSendMail for Stop Exception.", custStopException);
			throw custStopException;
		}
	}

	public final void logActivity(Map<String, String> requestParams) throws Exception,CustomStopException {
		if (null == requestParams) {
			throw new RuntimeException("Parameters have not been provided");
		}
		try {
			activitylog.activitylog(requestParams);
		} catch (StopException stopException) {
			throw new CustomStopException(stopException.getMessageWithoutStackTop());
		}
	}
	
	public final Map<String, String> sendError(Integer a_statuscode, String message) throws CustomStopException {

		if (null == message || message.trim().equalsIgnoreCase("")) {
			message = "jws.defaultErrorMessage";
		}
		if (null == a_statuscode || a_statuscode == 0) {
			a_statuscode = 400;
		}
		throw new CustomStopException(a_statuscode + "_" + message);
	}

	public final String evalTemplateByName(String a_strTemplateName, Map<String, Object> a_requestParams) throws CustomStopException{

		try {
			TemplateVO templateVO = templatingService.getTemplateByName(a_strTemplateName);
			
			return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					((null == a_requestParams) ? new HashMap<String, Object>() : a_requestParams));
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in evalTemplateByName for Stop Exception.", custStopException);
			throw custStopException;
		} catch (Exception a_exception) {
			logger.error("Error occured in evalTemplateByName.", a_exception);
		}
		return a_strTemplateName;
	}

	public final String evalTemplateByContent(String a_strTemplateContent, Map<String, Object> a_requestParams) throws CustomStopException{
		try {

			return templatingUtils.processTemplateContents(a_strTemplateContent, "", ((null == a_requestParams) ? new HashMap<String, Object>() : a_requestParams));
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in evalTemplateByContent for Stop Exception.", custStopException);
			throw custStopException;
		} catch (Exception a_exception) {
			logger.error("Error occured in evalTemplateByContent.", a_exception);

		}
		return a_strTemplateContent;
	}

	public final Map<String, String> convertToPDFFromTemplate(String a_strTemplateName,
			Map<String, Object> a_contextValues, String a_strImageFolder) throws CustomStopException {

		return convertToPDFFromString(evalTemplateByName(a_strTemplateName, a_contextValues), a_strImageFolder);
	}

	public final Map<String, String> convertToPDFFromString(String a_strSourceBody, String a_strImageFolder) {
		Map<String, String> returnObject = new HashMap<String, String>();

		ConverterProperties properties = new ConverterProperties();

		if (a_strImageFolder != null) {
			properties.setBaseUri(a_strImageFolder);
		}
		String absoluteFilePath = "";

		try {
			String fileUploadDir = propertyMasterService.findPropertyMasterValue("file-upload-location");
			// this will ensure unique filename everytime
			String targetPDFName = UUID.randomUUID().toString() + "_" + System.currentTimeMillis();
			absoluteFilePath = fileUploadDir + File.separator + targetPDFName + ".pdf";
			
			Files.deleteIfExists(Paths.get(absoluteFilePath));
			
			HtmlConverter.convertToPdf(a_strSourceBody, new FileOutputStream(absoluteFilePath), properties);
			returnObject.put("actionStatus", "true");
			returnObject.put("absoluteFilePath", absoluteFilePath);
			return returnObject;
		} catch (FileNotFoundException e) {
			logger.error("Error while creating pdf in convertToPDFFromString: " + " at location: " + absoluteFilePath + " : " + ExceptionUtils.getStackTrace(e));
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "Error while creating pdf: " + " at location: " + absoluteFilePath + " : "
					+ ExceptionUtils.getStackTrace(e));
			return returnObject;
		} catch (Exception e) {
			logger.error("Error while creating pdf in convertToPDFFromString: " + " at location: " + absoluteFilePath + " : " + ExceptionUtils.getStackTrace(e));
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "Error while creating pdf: " + " at location: " + absoluteFilePath + " : "
					+ ExceptionUtils.getStackTrace(e));
			return returnObject;
		}
	}
	/**
	 * For Adding notification in case of FailedMail Recipient List is empty which
	 * will last for one month
	 * 
	 * @author Bibhusrita.Nayak
	 * @param a_requestParams the a_requestParams to be set
	 * @return returnObject to return map of respose
	 */
	public final Map<String, String> addNotification(Map<String, Object> a_requestParams) {
		Map<String, String> returnObject = new HashMap<String, String>();
		
		JdbcTemplate jdbcTemplate = jwsDynarestDAO.updateJdbcTemplateDataSource(null);
		if (null == a_requestParams || a_requestParams.get("messageValidFrom") == null
				|| a_requestParams.get("messageValidTill") == null || a_requestParams.get("messageText") == null
				|| a_requestParams.get("messageType") == null || a_requestParams.get("displayOnceTxt") == null
				|| a_requestParams.get("selectionCriteria") == null || a_requestParams.get("createdBy") == null
				|| a_requestParams.get("updatedBy") == null) {
			throw new RuntimeException("Parameters have not been provided");
		}
		
		if (!(a_requestParams.get("messageValidFrom") instanceof Date)) {
			throw new RuntimeException("Please enter Valid From Date");
		}
		if (!(a_requestParams.get("messageValidTill") instanceof Date)) {
			throw new RuntimeException("Please enter Valid Till Date"); 
		}

		try {
			String notificationCountQuery = "SELECT COUNT(*) AS cnt FROM jq_generic_user_notification WHERE DATE(creation_date) = CURDATE()";
					
			List<Map<String, Object>> list = jdbcTemplate.queryForList(notificationCountQuery);
			
			int count = Integer.parseInt(String.valueOf(list.get(0).get("cnt")));
			
			String dailyNotificationCount = propertyMasterService.findPropertyMasterValue("daily-notification-count");
		
			if(count < Integer.parseInt(dailyNotificationCount)) {
			
			int affectedRowCount = namedParameterJdbcTemplate.update(
					"INSERT INTO jq_generic_user_notification (notification_id,message_valid_from,message_valid_till,message_text,"
							+ "message_type, display_once, selection_criteria,created_by,creation_date,updated_by,updated_date) "
							+ "VALUES (UUID(),:messageValidFrom,:messageValidTill,:messageText,:messageType,:displayOnceTxt,:selectionCriteria,:createdBy,NOW(),:updatedBy, NOW(), :datasourceId)",
					a_requestParams);
			returnObject.put("affectedRowCount", String.valueOf(affectedRowCount));
			returnObject.put("actionStatus", "true");
			
			}	
			else {
				throw new RuntimeException("Count of Records more than 500");
			}
	
			return returnObject;
		} catch (Exception e) {
			logger.error("Error while executing insert query in addNotification : " + ExceptionUtils.getStackTrace(e));
			returnObject.put("actionStatus", "false");
			returnObject.put("_error", "Error while executing insert query : " + ExceptionUtils.getStackTrace(e));
			return returnObject;
		}
	}
}

