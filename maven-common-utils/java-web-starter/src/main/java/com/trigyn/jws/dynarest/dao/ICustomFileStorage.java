package com.trigyn.jws.dynarest.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.utils.ApplicationContextUtils;
import com.trigyn.jws.dynarest.repository.FileUploadRepository;
import com.trigyn.jws.dynarest.repository.FileUploadTempRepository;

public interface ICustomFileStorage {

	/* 
	 * User Object is not passed here as , Pre-Authentication is maintain at APICall
	 */
	
	/**
	 * Process {@code @Autowired} injection for the Required bean in the Implementation
	 */
	static Map<String, Object> getDefinedObjectes() {
		Map<String, Object> mapOfRequiredObjects = new HashMap<>();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(ICustomFileStorage.class);
		if (null!=ApplicationContextUtils.getApplicationContext()) {
			PropertyMasterService propertyMasterService = ApplicationContextUtils.getApplicationContext()
					.getBean("propertyMasterService", PropertyMasterService.class);
			mapOfRequiredObjects.put("propertyMasterService", propertyMasterService);
			
			FileUploadTempRepository fileUploadTempRepository = ApplicationContextUtils.getApplicationContext()
					.getBean("fileUploadTempRepository", FileUploadTempRepository.class);
			mapOfRequiredObjects.put("fileUploadTempRepository", fileUploadTempRepository);
		
			FileUploadRepository fileUploadRepository = ApplicationContextUtils.getApplicationContext()
					.getBean("fileUploadRepository", FileUploadRepository.class);
			mapOfRequiredObjects.put("fileUploadRepository", fileUploadRepository);
		}
		
		return mapOfRequiredObjects;

	}

	/**
	 * 
	 *  Usage: To upload file on the remote location , first of all initiate the connection to your 
	 *  remote file storage . Once the connection is done, fetch all the required object method 
	 *  ie getDefinedObjectes() from default method of Interface, this method has all the beans defined 
	 *  which will be required to do the upload process.
	 *  To get the details of FileUploadTemp, use bean of FileUploadTempRepository to get the fileUploadTemp object.
	 *  define your remote directory (destination file location) in you Custom File Storage Class (eg SFTPConnector class:- 
	 *  class which will be created to do the Remote File Storage process).
	 *  For local directory (source File location), use PropertyMasterService bean from the map which will be return by getDefinedObjectes() 
	 *  of the ICustomFileStorage Interface .
	 *  Now you have both remote directory and local Directory,
	 *  With the help of you fileStorage protocol method(ie: ChannelSftp has  put(String src, String dst) method it take "src" and "dst" as parameters -to upload file from local directory to remote location)
	 *  upload source file to destination file.
	 *  Store file with PhysicalFileName in remote location which you will get form FileUploadTemp object.
	 *  Once the upload is done return the file path of the remote directory which the destination file is store,
	 *  to update the remote file path in the Database.
	 *  
	 *  Once it is done disconnect the connection.
	 *   
	 * @param fileBinId the  Id of FileBinId to find fileUploaTempDetails
	 * @param fileAssociationId the  fileAssociationId of FileBinId to find fileUploaTempDetails
	 * @param fileUploadTempId the  Id of FileBinId to find fileUploaTempDetails
	 *  User Object is not passed here as , preAuthentication is maintain at APICall

	 *   
	 */
	public String uploadFile(String fileBinId, String fileAssociationId, String fileUploadTempId) throws Exception;

	// view or download or copy
	/**
	 * This method is use to view/download/copy the file from remote directory to local 
	 * directory.
	 * Here the  source file is from remote director and destination file will be store in local 
	 * directory
	 * To get details for upload file use FileUploadRepository bean from getDefinedObjectes() method of the 
	 * interface. 
	 * Once you get upload file details from the Database, you will get the PhysicalFileName and File Path of the 
	 * file you need to view .
	 * To get local directory path use PropertyMasterService bean from  getDefinedObjectes() method of the 
	 * interface. 
	 * Once you get the local directory path , use file upload protor call method (i.e for ChannelSftp has get(String src, String dst) method -it takes src and dst as parametes)
	 * Here "src" is remote directory and "dst" is local directory.
	 * Once the file is copied to local directory ..........
	 * disconnect the connection Once done.
	 *
	 * View/Download/Copy
	 * @param fileUploadId the  Id of fileUploadTemp to find fileUploaTempDetails
	 *
	 */
	public ResponseEntity<InputStreamResource> viewFile(String fileUploadId) throws Exception;

	/**
	 *  To delete file from the remote location , first of all initiate the connection to your 
	 *  remote file storage . Once the connection is done, fetch all the required object method 
	 *  ie getDefinedObjectes() from default method of Interface, this method has all the beans defined 
	 *  which will be required to do the delete process.
	 *  To get the details of FileUploadTemp, use bean of FileUploadTempRepository to get the fileUploadTemp object.
	 *  define your remote directory (destination file location) in you Custom File Storage Class (eg SFTPConnector class:- 
	 *  class which will be created to do the Remote File Storage process).
	 *  With the help of you fileStorage protocol method(ie: ChannelSftp has  rm(String path) method it path as parameters -"path" is file path for the file to be deleted)
	 *  Pass PhysicalFileName  alone with the file path got form fileuploadTemp object.
	 *  Once the delet is done  disconnect the connection.
	 *  
	 * Delete
	 * @param fileUploadId the  Id of fileUploadTemp to find fileUploaTempDetails
	 */
	public ResponseEntity<?> deleteFileById(String fileUploadId) throws Exception;

}
