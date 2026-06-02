package com.trigyn.jws.workflow.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.webstarter.controller.TemplateCrudController;
import com.trigyn.jws.workflow.entities.WorkflowDefinition;
import com.trigyn.jws.workflow.repository.interfaces.IWorkflowDefinitionRepository;
import com.trigyn.jws.workflow.service.WorkflowService;
import com.trigyn.jws.workflow.vo.WorkflowSaveRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/cf")
public class WorkFlowController {
	private final static Logger	logger				= LoggerFactory.getLogger(TemplateCrudController.class);

	@Autowired
	private IUserDetailsService	userDetailsService	= null;

	@Autowired
	private MenuService			menuService			= null;

	@Autowired
	private FileUtilities		fileUtilities		= null;

	@Autowired
	private WorkflowService		workflowService		= null;
	
	@Autowired
	private IWorkflowDefinitionRepository	iDefinitionRepo;

	@PostMapping(value = "/aewf")
	public String addEdiWorkflow(HttpServletResponse httpServletResponse) throws Exception, CustomStopException {

		Map<String, Object>	templateMap		= new HashMap<>();
		Map<String, String>	requestParams	= new HashMap<>();
		try {

			return menuService.getTemplateWithSiteLayout("workflow-manage-details", templateMap);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while loading WorkFlow Add/Edi page.", custStopException);
			throw custStopException;
		} catch (Exception a_exception) {
			logger.error("Error occured while loading WorkFlow Add/Edi page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			fileUtilities.customSendError(httpServletResponse, HttpStatus.INTERNAL_SERVER_ERROR.value(),
					a_exception.getMessage());
			return null;
		}

	}

	@PostMapping(
		    value = "/swf",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<Boolean> saveWorkFlowDefinition(@RequestBody WorkflowSaveRequest request) {
		    HttpHeaders httpHeaders = new HttpHeaders();
		    httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		    try {
		        WorkflowDefinition workflowDefinition = request.getWorkflowDefinition();
		        String fileName = request.getWorkflowFileName();

		        // Decode Base64 back to XML
		        String encodedContent = request.getWorkflowFileContent();
		        String fileContent = new String(
		            Base64.getDecoder().decode(encodedContent),
		            StandardCharsets.UTF_8
		        );

		        // Save workflow
		        workflowService.saveWorkFlowDetails(workflowDefinition, fileName, fileContent);

		        return new ResponseEntity<>(true, httpHeaders, HttpStatus.OK);
		    } catch (Exception ex) {
		        ex.printStackTrace();
		        return new ResponseEntity<>(false, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}




	@GetMapping(value = "/cwfdn", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Boolean> checkResourceData(@RequestParam("dn") String definitionName) throws Exception {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		try {
			Boolean nameAlreadyExist = workflowService.checkDefinationExist(definitionName);
			return new ResponseEntity<>(nameAlreadyExist, httpHeaders, HttpStatus.OK);
		} catch (Exception a_exception) {
			logger.error("Error ocurred while fetching workflow  data: definitionName :" + definitionName, a_exception);
			return new ResponseEntity<>(true, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/dDef", produces = MediaType.TEXT_HTML_VALUE)
	public String deactivateDefinition(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
	        throws IOException {
	    boolean status = false;
	    try {
	        String definitionId = httpServletRequest.getParameter("definitionId");
	        WorkflowDefinition definition = iDefinitionRepo.findById(definitionId)
	                .orElseThrow(() -> new IllegalArgumentException("Definition not found for ID: " + definitionId));

	        if (Constants.INACTIVE.equals(definition.getIsActive())) {
	            logger.info("Definition {} is already inactive.", definitionId);
	        } else {
	            //  Mark as inactive
	            definition.setIsActive(Constants.INACTIVE);
	            iDefinitionRepo.save(definition);
	            status = true;	           
	        }
	    } catch (Exception ex) {
	        logger.error("Error occurred while deactivating definition: definitionId=" 
	                     + httpServletRequest.getParameter("definitionId"), ex);

	        if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
	            return null;
	        }

	        fileUtilities.customSendError(httpServletResponse, HttpStatus.INTERNAL_SERVER_ERROR.value(),
	                ex.getMessage());
	        return String.valueOf(status);
	    }
	    return String.valueOf(status);
	}


}
