package com.trigyn.jws.webstarter.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.webstarter.service.ImportService;

@Configuration
public class StaticCodeImportConfiguration {

	private final static Logger		logger					= LogManager.getLogger(StaticCodeImportConfiguration.class);

	private static Boolean			isCodeImported			= false;

	private static String			webUIJarPrefix			= "webui-";

	private static String			webUIJarName			= "";

	private static String			webUIWarPath			= "";

	private static String			webUIJarPath			= "";

	@Autowired
	private PropertyMasterService	propertyMasterService	= null;

	@Autowired
	private ImportService			importService			= null;

	@Autowired
	private ServletContext			servletContext			= null;

	@PostConstruct
	public void initialiseScheduler() {
		try {

			System.out.println("############# Inside Static Code Importer");

			Properties prop = new Properties();
			prop.load(getClass().getClassLoader().getResourceAsStream("webui-pom.properties"));
			String webUIJarVersion = prop.getProperty("webui-version");

			if (webUIJarVersion.contains(webUIJarPrefix) == false) {
				webUIJarName = webUIJarPrefix + webUIJarVersion + ".jar";
			} else {
				webUIJarName = webUIJarVersion + ".jar";
			}
			webUIJarPrefix	= webUIJarVersion.split("-")[0];
			webUIWarPath	= "/WEB-INF/lib/" + webUIJarName;
			webUIJarPath	= "BOOT-INF" + File.separator + "lib" + File.separator + webUIJarName;

			prop.load(getClass().getClassLoader().getResourceAsStream("jws-pom.properties"));
			String	currentVersion		= prop.getProperty("jws.version");
			String	lastDeployedVersion	= propertyMasterService.findPropertyMasterValue("last-deployed-version");

			if (isCodeImported == false) {
				String profile = propertyMasterService.findPropertyMasterValue("profile");
				if (lastDeployedVersion == null || currentVersion.equals(lastDeployedVersion) == false) {
					System.out.println("############# Importing through static code importer");
					if (profile != null && profile.equalsIgnoreCase("dev")) {

						// import file from temporary storage path
						importService.importFileOnLoad(null, true);
					} else {
						String filePath = explodeJar();
						if (filePath != null) {
							importService.importFileOnLoad(new File(filePath), false);
						}
					}

					PropertyMaster lastDeployedVersionPropertyMaster = propertyMasterService
							.findPropertyMasterByName("last-deployed-version");
					if (lastDeployedVersionPropertyMaster == null) {
						lastDeployedVersionPropertyMaster = new PropertyMaster();
						lastDeployedVersionPropertyMaster.setAppVersion(1.4);
						lastDeployedVersionPropertyMaster.setPropertyName("last-deployed-version");
						lastDeployedVersionPropertyMaster.setComments("Last deployed version of JQuiver");
						lastDeployedVersionPropertyMaster.setIsDeleted(0);
						lastDeployedVersionPropertyMaster.setLastModifiedDate(new Date());
						lastDeployedVersionPropertyMaster.setModifiedBy("admin@jquiver.io");
						lastDeployedVersionPropertyMaster.setOwnerId("system");
						lastDeployedVersionPropertyMaster.setOwnerType("system");
					}
					lastDeployedVersionPropertyMaster.setPropertyValue(currentVersion);
					propertyMasterService.save(lastDeployedVersionPropertyMaster);

					PropertyMaster lastDeployedDatePropertyMaster = propertyMasterService
							.findPropertyMasterByName("last-deployed-date");
					if (lastDeployedDatePropertyMaster == null) {
						lastDeployedDatePropertyMaster = new PropertyMaster();
						lastDeployedDatePropertyMaster.setAppVersion(1.4);
						lastDeployedDatePropertyMaster.setPropertyName("last-deployed-date");
						lastDeployedDatePropertyMaster.setComments("Last deployed date time of JQuiver");
						lastDeployedDatePropertyMaster.setIsDeleted(0);
						lastDeployedDatePropertyMaster.setLastModifiedDate(new Date());
						lastDeployedDatePropertyMaster.setModifiedBy("admin@jquiver.io");
						lastDeployedDatePropertyMaster.setOwnerId("system");
						lastDeployedDatePropertyMaster.setOwnerType("system");
					}
					Date date = DateUtils.addMinutes(Calendar.getInstance().getTime(), 15); ;
					lastDeployedDatePropertyMaster
							.setPropertyValue(new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss").format(date));
					propertyMasterService.save(lastDeployedDatePropertyMaster);

				}

			}
			isCodeImported = true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String explodeJar() throws IOException {

		String	classpath	= System.getProperty("java.class.path");
		String	cPaths[]	= classpath.split(File.pathSeparator);
		File	f;
		for (String p : cPaths) {
			f = new File(p);
			if (f.isFile()) {
				if (p.toLowerCase().contains(webUIJarName.toLowerCase())) {
					return f.getAbsolutePath();
				}
			}
		}

		for (String p : cPaths) {
			f = new File(p);
			if (f.isDirectory() && f.canRead()) {
				File fs = getWebUIJar(f);
				if (fs != null) {
					return f.getAbsolutePath();
				}
			}
		}
		String fullPath = servletContext.getRealPath(webUIWarPath);
		if (fullPath != null && new File(fullPath).exists()) {
			return fullPath;
		}

		String isJar = System.getProperty("ij");
		if ((cPaths.length == 1 && cPaths[0].endsWith(".jar")) || (isJar != null && isJar.equalsIgnoreCase("true"))) {
			File file = null;
			for (String p : cPaths) {
				file = new File(p);
			}
			String unZipFilePath = ZipUtil.unzip(new FileInputStream(file), null);
			fullPath = unZipFilePath + webUIJarPath;
			if (fullPath != null && new File(unZipFilePath).exists() && new File(fullPath).exists()) {
				return fullPath;
			}
		}

		return null;
	}

	private static File getWebUIJar(File a_parentFolder) {
		if (a_parentFolder.isDirectory() && a_parentFolder.canRead()) {
			File fss[] = a_parentFolder.listFiles();
			for (File fs : fss) {
				if (fs.isFile()) {
					if (fs.getAbsolutePath().toLowerCase().contains(webUIJarName.toLowerCase())) {
						return fs;
					}
				} else if (fs.isDirectory() && fs.canRead()) {
					return getWebUIJar(fs);
				}
			}
		}
		return null;
	}

}
