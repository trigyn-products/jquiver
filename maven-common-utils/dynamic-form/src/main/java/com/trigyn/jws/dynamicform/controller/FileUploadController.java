package com.trigyn.jws.dynamicform.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dynamicform.service.FilesStorageService;
import com.trigyn.jws.dynamicform.vo.FileInfo;

@RestController
@RequestMapping("/cf")
public class FileUploadController {

	@Autowired
	@Qualifier("file-system-storage")
	private FilesStorageService storageService = null;

	@PostMapping(value = "/upload", produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public String uploadFiles(@RequestParam("files") MultipartFile file) {
		String message = "";
		try {
			String fileId = storageService.save(file);
			return new ObjectMapper().writeValueAsString("1");
		} catch (Exception e) {
			message = "Fail to upload files!";
			return message;
		}
	}
	
	@PostMapping(value = "/m-upload", produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public String uploadFiles(@RequestParam("files") MultipartFile[] files) {
		String message = "";
		try {
			List<String> fileNames = new ArrayList<>();

			Arrays.asList(files).stream().forEach(file -> {
				String fileId = storageService.save(file);
				fileNames.add(fileId);
			});

			return new ObjectMapper().writeValueAsString("1");
		} catch (Exception e) {
			message = "Fail to upload files!";
			return message;
		}
	}

	@GetMapping("/files")
	public ResponseEntity<List<FileInfo>> getListFiles() {
		List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(FileUploadController.class, "getFile", path.getFileName().toString()).build()
					.toString();

			return new FileInfo(filename, url);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = storageService.load(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

}
