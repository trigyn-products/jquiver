package com.trigyn.jws.tags.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.tags.entities.TagEntityDetailsRequest;
import com.trigyn.jws.tags.service.TagService;
import com.trigyn.jws.tags.service.TagViewService;
import com.trigyn.jws.webstarter.controller.TemplateCrudController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/cf")
public class TagViewController {
private final static Logger	logger				= LoggerFactory.getLogger(TemplateCrudController.class);
	
	@Autowired
	private TagViewService tagViewService;

	@PostMapping(value = "/rt")
	public String restoreTagWithEntities(HttpSession session, HttpServletRequest request) throws Exception {
		return tagViewService.restoreTagData(request);
	}

}
