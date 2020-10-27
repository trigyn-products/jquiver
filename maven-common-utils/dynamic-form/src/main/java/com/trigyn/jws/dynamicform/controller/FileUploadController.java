package com.trigyn.jws.dynamicform.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dbutils.vo.FileInfo;
import com.trigyn.jws.dynamicform.service.FilesStorageService;

@RestController
@RequestMapping("/cf")
public class FileUploadController {

	@Autowired
	@Qualifier("file-system-storage")
	private FilesStorageService storageService = null;

	@PostMapping(value = "/upload", produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public String uploadFiles(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) {
		String message = "";
		String fileConfigId = httpServletRequest.getParameter("fileConfigData");
		try {
			String fileId = storageService.save(file, fileConfigId);
			Map<String, Object> uploadDetails = new HashMap<String, Object>();
			uploadDetails.put("fileId", fileId);
			uploadDetails.put("success", "1");
			return new ObjectMapper().writeValueAsString(uploadDetails);
		} catch (Exception e) {
			message = "Fail to upload files!";
			return message;
		}
	}
	
	@PostMapping(value = "/m-upload", produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public String uploadFiles(@RequestParam("files[0]") MultipartFile[] files, HttpServletRequest httpServletRequest) {
		String fileConfigId = httpServletRequest.getParameter("fileConfigData");
		String message = "";
		try {
			List<String> fileNames = new ArrayList<>();

			Arrays.asList(files).stream().forEach(file -> {
				String fileId = storageService.save(file, fileConfigId);
				fileNames.add(fileId);
			});
			Map<String, Object> uploadDetails = new HashMap<String, Object>();
			uploadDetails.put("fileIds", fileNames);
			uploadDetails.put("success", "1");
			return new ObjectMapper().writeValueAsString(uploadDetails);
		} catch (Exception e) {
			message = "Fail to upload files!";
			return message;
		}
	}

	@GetMapping("/files")
	public ResponseEntity<List<FileInfo>> getListFiles() {
		List<FileInfo> fileInfos = storageService.loadAll();
		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}
	
	@DeleteMapping("/files/{fileId:.+}")
	public ResponseEntity<?> deleteFile(@PathVariable String fileId) throws Exception {
		storageService.deleteFileById(fileId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@GetMapping("/fileDetails")
	public ResponseEntity<List<FileInfo>> getListFilesByIds(HttpServletRequest httpServletRequest) throws JsonMappingException, JsonProcessingException {
		String fileIds = httpServletRequest.getParameter("files");
		List<String> fileIdList = new ObjectMapper().readValue(fileIds, List.class);
		List<FileInfo> fileInfos = storageService.getFileDetailsByIds(fileIdList);
		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	@GetMapping("/files/{fileId:.+}")
	public ResponseEntity<InputStreamResource> getFile(@PathVariable String fileId) throws IOException {
		Map<String, Object> fileInfo = storageService.load(fileId);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", fileInfo.get("fileName").toString());
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		byte[] file = (byte[]) fileInfo.get("file");
		InputStreamResource streamResource = new InputStreamResource(new ByteArrayInputStream(file));
		return new ResponseEntity<InputStreamResource>(streamResource, headers, HttpStatus.OK);
	}

}
