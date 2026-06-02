package com.trigyn.jws.tags.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.tags.entities.TagEntityDetailsRequest;
import com.trigyn.jws.tags.service.TagService;

@RestController
@RequestMapping("/cf")
public class TagController {
	private final static Logger	logger	= LoggerFactory.getLogger(TagController.class);

	@Autowired
	private TagService			tagService;

	@PostMapping("/st")
	public ResponseEntity<Map<String, Object>> saveTagWithEntities(@RequestBody TagEntityDetailsRequest request) {
		Map<String, Object> response = new HashMap<>();

		try {
			if (request == null || request.getTagName() == null || request.getTagName().trim().isEmpty()) {
				response.put("status", "error");
				response.put("message", "Tag name is required.");
				logger.warn("Tag name validation failed: request or tagName is null/empty.");
				return ResponseEntity.badRequest().body(response);
			}
			String tagName = request.getTagName().trim();
			logger.debug("Received tag creation request for tagName: {}", tagName);
			// Duplicate check
			boolean tagExists = tagService.isTagNameExists(tagName);
			if (tagExists) {
				logger.info("Tag name '{}' already exists.", tagName);
				response.put("status", "error");
				response.put("message", "Tag name already exists: " + tagName);
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
			}
			if (request.getEntityMappings() != null) {
				logger.info("Entity Mapping Size: " + request.getEntityMappings().size());
			} else {
				logger.info("Entity Mapping: null");
			}

			// Save tag and associated entities
			tagService.saveTagWithEntities(request);
			logger.info("Tag '{}' saved successfully with associated entities.", tagName);

			response.put("status", "success");
			response.put("message", "Tag saved successfully!");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("Error saving tag: {}", e.getMessage(), e);
			response.put("status", "error");
			response.put("message", "Error saving tag: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
}
