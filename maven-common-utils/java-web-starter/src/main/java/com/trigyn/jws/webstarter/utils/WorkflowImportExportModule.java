package com.trigyn.jws.webstarter.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.service.DownloadUploadModule;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dynarest.repository.FileUploadRepository;
import com.trigyn.jws.dynarest.repository.IFileUploadConfigRepository;
import com.trigyn.jws.workflow.entities.WorkflowDefinition;
import com.trigyn.jws.workflow.repository.interfaces.IWorkflowDefinitionRepository;
import com.trigyn.jws.workflow.service.WorkflowService;
import com.trigyn.jws.workflow.vo.WorkflowDefinitionExportVO;

@Component
public class WorkflowImportExportModule implements DownloadUploadModule<WorkflowDefinition> {
	
	private Map<String, Map<String, Object>>	moduleDetailsMap				= new HashMap<>();

	@Autowired
	private IWorkflowDefinitionRepository		iWorkflowDefinitionRepository	= null;

	@Autowired
	private FileUploadRepository				fileUploadRepository			= null;

	@Autowired
	private IFileUploadConfigRepository			iFileUploadConfigRepository		= null;
	
	@Autowired
	private FileUtilities						fileUtilities			= null;
	
	@Autowired
	private WorkflowService						workflowService			= null;



	@Override
	public void downloadCodeToLocal(WorkflowDefinition object, String folderLocation) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadCodeToDB(String moduleTypeID, String uploadFileName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportData(Object object, String folderLocation) throws Exception {

	    WorkflowDefinition workflow = (WorkflowDefinition) object;

	    List<WorkflowDefinition> workflows = new ArrayList<>();

	    if (workflow != null) {
	        workflows.add(workflow);
	    }

	    String workflowDirectory = "workflow";

	    folderLocation = folderLocation + File.separator + workflowDirectory;

	    File directory = new File(folderLocation);

	    if (!directory.exists()) {
	        directory.mkdirs();
	    }

	    for (WorkflowDefinition wf : workflows) {

	        String fileName = wf.getDefinitionName() + ".bpmn";

	        File file = new File(folderLocation + File.separator + fileName);

	        // Write BPMN XML into file
	        fileUtilities.writeFileContents(wf.getBpmnXml(), file);

	        WorkflowDefinitionExportVO workflowExportVO = new WorkflowDefinitionExportVO(
	                wf.getDefinitionId(),
	                wf.getDefinitionName(),
	                fileName ,
	                wf.getVersion(),
	                wf.getUploadedBy(),
	                wf.getUploadedAt(),
	                wf.getCreatedBy(),
	                wf.getCreatedDate(),
	                wf.getIsActive()
	        );

	        Map<String, Object> map = new HashMap<>();

	        map.put("moduleName", wf.getDefinitionName());
	        map.put("moduleObject", workflowExportVO);

	        moduleDetailsMap.put(wf.getDefinitionId(), map);
	    }
	}

	@Override
	public Object importData(String folderLocation, String uploadFileName, String uploadID, Object importObject) throws Exception {

	    WorkflowDefinitionExportVO exportVO = (WorkflowDefinitionExportVO) importObject;

	    File workflowFile = new File(folderLocation, exportVO.getBpmnFile());

	    if (!workflowFile.exists()) {
	        throw new Exception("Workflow BPMN file missing: " + exportVO.getBpmnFile());
	    }

	    String content = fileUtilities.readContentsOfFile(workflowFile.getAbsolutePath());

	    WorkflowDefinition workflowDefinition = new WorkflowDefinition();

	    workflowDefinition.setDefinitionId(exportVO.getDefinitionId());
	    workflowDefinition.setDefinitionName(exportVO.getDefinitionName());

	    workflowService.saveWorkFlowDetails(
	            workflowDefinition,
	            exportVO.getBpmnFile(),
	            content
	    );

	    return workflowDefinition;
	}
	
//	@Override
//	public Object importData(String folderLocation, String uploadFileName, String uploadID, Object importObject) throws Exception {
//
//	    String user = "admin";
//
//	    if (importObject == null) {
//	        return null;
//	    }
//
//	    WorkflowDefinitionExportVO workflowExportVO = (WorkflowDefinitionExportVO) importObject;
//
//	    if (workflowExportVO == null || workflowExportVO.getBpmnFile() == null) {
//	        return null;
//	    }
//
//	    String workflowFileName = workflowExportVO.getBpmnFile();
//
//	    int lastDotIndex = workflowFileName.lastIndexOf('.');
//
//	    final String bpmnExtension = (lastDotIndex != -1)
//	            ? "." + workflowFileName.substring(lastDotIndex + 1)
//	            : "";
//
//	    WorkflowDefinition workflow = null;
//
//	    if (folderLocation == null) {
//	        throw new Exception("Folder location is null");
//	    }
//
//	    File directory = new File(folderLocation);
//
//	    if (!directory.exists() || !directory.isDirectory()) {
//	        throw new Exception("No such directory present");
//	    }
//
//	    FilenameFilter textFilter = new FilenameFilter() {
//	        public boolean accept(File dir, String name) {
//	            if (name == null) {
//	                return false;
//	            }
//	            if (!StringUtils.isBlank(uploadFileName)) {
//	                return name.equalsIgnoreCase(uploadFileName + bpmnExtension);
//	            } else {
//	                return name.toLowerCase().endsWith(bpmnExtension);
//	            }
//	        }
//	    };
//
//	    File[] files = directory.listFiles(textFilter);
//
//	    if (files == null || files.length == 0) {
//	        return null;
//	    }
//
//	    for (File file : files) {
//
//	        if (file == null || !file.isFile()) {
//	            continue;
//	        }
//
//	        String fileName = file.getName();
//
//	        if (fileName != null && fileName.equals(workflowExportVO.getBpmnFile())) {
//
//	            WorkflowDefinition workflowEntity = null;
//
//	            if (uploadID != null) {
//	                workflowEntity = iWorkflowDefinitionRepository
//	                        .findById(uploadID)
//	                        .orElse(null);
//	            }
//
//	            String content = fileUtilities.readContentsOfFile(file.getAbsolutePath());
//
//	            if (content == null) {
//	                continue;
//	            }
//
//	            workflow = new WorkflowDefinition();
//
//	            workflow.setDefinitionId(workflowExportVO.getDefinitionId());
//	            workflow.setDefinitionName(workflowExportVO.getDefinitionName());
//	            workflow.setBpmnXml(content);
//	            workflow.setVersion(workflowExportVO.getVersion());
//	            workflow.setIsActive(workflowExportVO.getIsActive());
//
//	            if (workflowEntity == null) {
//
//	                workflow.setUploadedBy(user);
//	                workflow.setUploadedAt(workflowExportVO.getUploadedAt());
//	                workflow.setCreatedBy(user);
//	                workflow.setCreatedDate(workflowExportVO.getCreatedDate());
//
//	            } else {
//
//	                workflow.setCreatedBy(workflowEntity.getCreatedBy());
//	                workflow.setCreatedDate(workflowEntity.getCreatedDate());
//
//	                workflow.setUploadedBy(user);
//	                workflow.setUploadedAt(workflowExportVO.getUploadedAt());
//	            }
//	        }
//	    }
//
//	    return workflow;
//	}


	public Map<String, Map<String, Object>> getModuleDetailsMap() {
		return moduleDetailsMap;
	}

	public void setModuleDetailsMap(Map<String, Map<String, Object>> moduleDetailsMap) {
		this.moduleDetailsMap = moduleDetailsMap;
	}

	
}

