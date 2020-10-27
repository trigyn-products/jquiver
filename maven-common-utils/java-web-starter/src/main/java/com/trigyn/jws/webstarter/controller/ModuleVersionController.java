package com.trigyn.jws.webstarter.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.service.ModuleVersionService;

@RestController
@RequestMapping("/cf")
public class ModuleVersionController {
	
	@Autowired
	private ModuleVersionService moduleVersionService				= null;
	
	@PostMapping(value="/smv",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void saveModuleVersioning(@RequestBody MultiValueMap<String, String> formData) throws Exception{
		Map<String, Object> versioningData = new HashMap<>();
		String primaryKey = null;
		String entityName = null;
		for (Entry<String, List<String>> formDataMap : formData.entrySet())  {
			versioningData.put(formDataMap.getKey(), formDataMap.getValue().get(0));
		}
		if(versioningData.get("primaryKey") != null) {
			primaryKey = versioningData.get("primaryKey").toString();
		}
		if(versioningData.get("entityName") != null) {
			entityName = versioningData.get("entityName").toString();
		}
		
		moduleVersionService.saveModuleVersion(versioningData, null, primaryKey, entityName);
	}
}
