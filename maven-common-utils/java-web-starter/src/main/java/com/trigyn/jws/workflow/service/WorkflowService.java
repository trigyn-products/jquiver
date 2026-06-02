package com.trigyn.jws.workflow.service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.itextpdf.io.exceptions.IOException;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.resourcebundle.service.ResourceBundleService;
import com.trigyn.jws.workflow.entities.WorkflowDefinition;
import com.trigyn.jws.workflow.entities.WorkflowInstance;
import com.trigyn.jws.workflow.entities.WorkflowStatus;
import com.trigyn.jws.workflow.entities.WorkflowTransition;
import com.trigyn.jws.workflow.repository.interfaces.IWorkflowDefinitionRepository;
import com.trigyn.jws.workflow.repository.interfaces.IWorkflowInstanceRepository;
import com.trigyn.jws.workflow.repository.interfaces.IWorkflowStatusRepository;
import com.trigyn.jws.workflow.repository.interfaces.IWorkflowTransitionRepository;
import com.trigyn.jws.workflow.utils.StatusType;
import com.trigyn.jws.workflow.utils.WorkflowCycleDetector;

@Service
public class WorkflowService {

	@Autowired
	private IWorkflowStatusRepository iStatusRepo;

	@Autowired
	private IWorkflowTransitionRepository iTransitionRepo;

	@Autowired
	private IWorkflowInstanceRepository iInstanceRepo;

	@Autowired
	private IWorkflowDefinitionRepository iDefinitionRepo;

	@Autowired
	private WorkflowCycleDetector iCycleDetector;

	@Autowired
	private IUserDetailsService detailsService = null;

	private final static Logger logger = LoggerFactory.getLogger(ResourceBundleService.class);

//	public WorkflowItem initiateWorkflow(String entityId, String initialStatusId) {
//
//		WorkflowStatus	status		= iStatusRepo.findById(initialStatusId)
//				.orElseThrow(() -> new RuntimeException("Invalid initial status"));
//
//		WorkflowItem	instance	= new WorkflowItem();
//
//		instance.setEntityId(entityId);
//
//		instance.setCurrentStatus(status);
//
//		return iItemRepo.save(instance);
//
//	}

//	public WorkflowItem transition(String instanceId, String targetStatusId) {
//
//		WorkflowItem					instance		= iItemRepo.findById(instanceId)
//
//				.orElseThrow(() -> new RuntimeException("Instance not found"));
//
//		WorkflowStatus					currentStatus	= instance.getCurrentStatus();
//
//		WorkflowStatus					nextStatus		= iStatusRepo.findById(targetStatusId)
//
//				.orElseThrow(() -> new RuntimeException("Target status not found"));
//
//		Optional<WorkflowTransition>	validTransition	= iTransitionRepo
//
//				.findByFromStatusAndToStatus(currentStatus, nextStatus);
//
//		if (validTransition.isEmpty()) {
//
//			throw new IllegalStateException("Invalid status transition");
//
//		}
//
//		instance.setCurrentStatus(nextStatus);
//
//		return iItemRepo.save(instance);
//
//	}

//	public List<WorkflowStatus> getNextPossibleStatuses(String instanceId) {
//
//		WorkflowItem instance = iItemRepo.findById(instanceId)
//
//				.orElseThrow(() -> new RuntimeException("Instance not found"));
//
//		return iTransitionRepo.findNextStatusesByCurrent(instance.getCurrentStatus().getStatusId());
//
//	}

	@Transactional
	public void saveWorkFlowDetails(WorkflowDefinition workflowDefinition, String fileName, String fileContent) {
		try {
			// === Validate content ===
			if (fileContent == null || fileContent.trim().isEmpty()) {
				throw new IllegalArgumentException("Workflow BPMN content is required.");
			}

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
			factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
			factory.setExpandEntityReferences(false);
			factory.setNamespaceAware(true);

			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)));
			doc.getDocumentElement().normalize();

			// === Versioning logic ===
			WorkflowDefinition existingDefinition = iDefinitionRepo.findById(workflowDefinition.getDefinitionId())
					.orElse(null);
			boolean isBpmnChangedOrNew = false;

			if (existingDefinition != null) {
				if (!existingDefinition.getBpmnXml().equals(fileContent)) {
					workflowDefinition.setVersion(existingDefinition.getVersion() + 1);
					isBpmnChangedOrNew = true;
				} else {
					workflowDefinition.setVersion(existingDefinition.getVersion());
				}
				workflowDefinition.setDefinitionId(existingDefinition.getDefinitionId());
			} else {
				workflowDefinition.setVersion(1);
				workflowDefinition.setDefinitionId(UUID.randomUUID().toString());
				isBpmnChangedOrNew = true;
			}

			workflowDefinition.setBpmnXml(fileContent);
			workflowDefinition.setCreatedBy(detailsService.getUserDetails().getUserName());
			workflowDefinition.setCreatedDate(new Date());
			workflowDefinition.setUploadedBy(detailsService.getUserDetails().getUserName());
			workflowDefinition.setUploadedAt(new Date());
			workflowDefinition.setIsActive(1);

			workflowDefinition = iDefinitionRepo.save(workflowDefinition);

			if (!isBpmnChangedOrNew)
				return;

			// === Build status map ===
			Map<String, WorkflowStatus> statusIdMap = new HashMap<>();
			Map<String, String> statusRoleMap = new HashMap<>();
			int statusOrder = 1;

			String[] taskNodeTypes = { "startEvent", "userTask", "endEvent" };
			String[] gatewayTypes = { "exclusiveGateway", "parallelGateway", "inclusiveGateway", "eventBasedGateway" };

			// --- Process tasks & events ---
			for (String nodeType : taskNodeTypes) {
				NodeList nodes = doc.getElementsByTagNameNS("*", nodeType);
				for (int i = 0; i < nodes.getLength(); i++) {
					Element nodeElement = (Element) nodes.item(i);
					if (nodeElement == null)
						continue;

					String nodeId = nodeElement.getAttribute("id");
					String nodeName = nodeElement.getAttribute("name");
					if (nodeId == null || nodeId.isEmpty())
						continue;

					WorkflowStatus status = new WorkflowStatus();
					status.setStatusId(UUID.randomUUID().toString());
					status.setStatusName((nodeName == null || nodeName.isEmpty()) ? nodeId : nodeName);
					status.setIsActive(true);
					// status.setIsFinal("endEvent".equalsIgnoreCase(nodeType));
					// status.setIsUserTask("userTask".equalsIgnoreCase(nodeType));
					switch (nodeType) {
					case "startEvent":
						status.setType(StatusType.START_EVENT);
						break;
					case "userTask":
						status.setType(StatusType.USER_TASK);
						break;
					case "endEvent":
						status.setType(StatusType.END_EVENT);
						break;
					case "exclusiveGateway":
					case "parallelGateway":
						status.setType(StatusType.GATEWAY);
						break;
					default:
						status.setType(StatusType.GATEWAY); // fallback
					}

					status.setOrderNo(statusOrder++);
					status.setCreatedBy(detailsService.getUserDetails().getUserName());
					status.setCreatedDate(new Date());
					status.setLastUpdatedBy(detailsService.getUserDetails().getUserName());
					status.setLastUpdatedTs(new Date());
					status.setDefinition(workflowDefinition);
					status.setVersion(workflowDefinition.getVersion());

					if ("userTask".equalsIgnoreCase(nodeType)) {
						String assignee = nodeElement.getAttribute("flowable:assignee");
						if (assignee != null && !assignee.isEmpty()) {
							statusRoleMap.put(nodeId, assignee);
						}
					}

					statusIdMap.put(nodeId, status);
					iStatusRepo.save(status);
				}
			}

			// --- Process gateways as statuses ---
			for (String gatewayType : gatewayTypes) {
				NodeList gateways = doc.getElementsByTagNameNS("*", gatewayType);
				for (int i = 0; i < gateways.getLength(); i++) {
					Element gatewayElement = (Element) gateways.item(i);
					if (gatewayElement == null)
						continue;

					String gatewayId = gatewayElement.getAttribute("id");
					String gatewayName = gatewayElement.getAttribute("name");
					if (gatewayId == null || gatewayId.isEmpty())
						continue;

					WorkflowStatus status = new WorkflowStatus();
					status.setStatusId(UUID.randomUUID().toString());
					status.setStatusName((gatewayName == null || gatewayName.isEmpty()) ? gatewayId : gatewayName);
					status.setIsActive(true);
					// status.setIsFinal(false);
					// status.setIsUserTask(false);
					status.setType(StatusType.GATEWAY);
					status.setOrderNo(statusOrder++);
					status.setCreatedBy(detailsService.getUserDetails().getUserName());
					status.setCreatedDate(new Date());
					status.setLastUpdatedBy(detailsService.getUserDetails().getUserName());
					status.setLastUpdatedTs(new Date());
					status.setDefinition(workflowDefinition);
					status.setVersion(workflowDefinition.getVersion());

					statusIdMap.put(gatewayId, status);
					iStatusRepo.save(status);
				}
			}

			if (statusIdMap.isEmpty()) {
				throw new IllegalStateException("No valid workflow statuses found in BPMN.");
			}

			// === Build graph for sequence flows ===
			NodeList flows = doc.getElementsByTagNameNS("*", "sequenceFlow");
			Map<String, List<String>> graph = new HashMap<>();
			for (int i = 0; i < flows.getLength(); i++) {
				Element flowElement = (Element) flows.item(i);
				if (flowElement == null)
					continue;

				String sourceRef = flowElement.getAttribute("sourceRef");
				String targetRef = flowElement.getAttribute("targetRef");
				if (sourceRef == null || targetRef == null)
					continue;

				graph.computeIfAbsent(sourceRef, k -> new ArrayList<>()).add(targetRef);
			}

			if (iCycleDetector.hasCycle(graph)) {
				throw new IllegalStateException("Cycle detected in workflow. Save aborted.");
			}

			// === Build transitions ===
			List<WorkflowTransition> transitions = new ArrayList<>();
			int transitionOrder = 1;

			for (int i = 0; i < flows.getLength(); i++) {
				Element flowElement = (Element) flows.item(i);
				if (flowElement == null)
					continue;

				String sourceRef = flowElement.getAttribute("sourceRef");
				String targetRef = flowElement.getAttribute("targetRef");
				String flowName = flowElement.getAttribute("name");

				if (sourceRef == null || targetRef == null)
					continue;

				String action = "AUTO";
				String rawActionExpression = null;

				NodeList conditionList = flowElement.getElementsByTagNameNS("*", "conditionExpression");
				if (conditionList.getLength() == 0) {
					conditionList = flowElement.getElementsByTagNameNS("*", "flowable:conditionExpression");
				}

				if (flowName != null && !flowName.trim().isEmpty()) {
					action = flowName.trim();
				} else if (conditionList != null && conditionList.getLength() > 0) {
					Element condElem = (Element) conditionList.item(0);
					if (condElem != null && condElem.getTextContent() != null) {
						rawActionExpression = condElem.getTextContent().replace("\n", "").replace("\r", "").trim();
						action = normalizeAction(rawActionExpression);
					}
				}

				// Build transition directly (no need to skip gateways anymore)
				WorkflowTransition transition = new WorkflowTransition();
				transition.setFromStatus(statusIdMap.get(sourceRef));
				transition.setToStatus(statusIdMap.get(targetRef));
				transition.setTransitionLabel(
						(flowName == null || flowName.isEmpty()) ? "Transition " + (i + 1) : flowName);
				transition.setAction(action);
				transition.setRawActionExpression(rawActionExpression);
				transition.setTransitionId(UUID.randomUUID().toString());
				transition.setOrderNo(transitionOrder++);
				transition.setCreatedBy(detailsService.getUserDetails().getUserName());
				transition.setCreatedDate(new Date());
				transition.setLastUpdatedBy(detailsService.getUserDetails().getUserName());
				transition.setLastUpdatedTs(new Date());
				transition.setWorkflowDefinition(workflowDefinition);
				transition.setVersion(workflowDefinition.getVersion());
				transition.setAllowedRoles(statusRoleMap.get(targetRef));

				iTransitionRepo.save(transition);
				transitions.add(transition);
			}

			if (transitions.isEmpty()) {
				throw new IllegalStateException("No valid workflow transitions found in BPMN.");
			}

		} catch (ParserConfigurationException | IOException | SAXException e) {
			throw new RuntimeException("Workflow parsing error: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new RuntimeException("Error saving workflow: " + e.getMessage(), e);
		}
	}

	/**
	 * Utility to normalize BPMN condition expressions into friendly action labels
	 */
	private String normalizeAction(String rawExpr) {
		if (rawExpr == null || rawExpr.trim().isEmpty()) {
			return "AUTO";
		}

		// Remove all whitespace and line breaks
		String expr = rawExpr.replaceAll("\\s+", "");

		// Shortcut for simple boolean expressions
		if (expr.contains("==true") || expr.contains("=true")) {
			return "Approve";
		}
		if (expr.contains("==false") || expr.contains("=false")) {
			return "Reject";
		}

		// Strip ${...} wrapper if present
		if (expr.startsWith("${") && expr.endsWith("}")) {
			expr = expr.substring(2, expr.length() - 1);
		}

		// Replace operators with friendly text
		expr = expr.replaceAll("==", "_eq_").replaceAll("!=", "_neq_").replaceAll("&&", "_and_")
				.replaceAll("\\|\\|", "_or_").replaceAll("[^a-zA-Z0-9_]", ""); // remove anything else that's unsafe

		// Truncate very long expressions for readability
		if (expr.length() > 50) {
			expr = expr.substring(0, 50);
		}

		return expr.isEmpty() ? "AUTO" : expr;
	}

	/**
	 * Recursively resolve real target nodes by skipping gateways.
	 */
	private List<String> resolveTarget(String nodeId, Map<String, List<String>> graph, Set<String> gatewayNodes) {
		if (!graph.containsKey(nodeId))
			return List.of();

		List<String> result = new ArrayList<>();
		for (String next : graph.get(nodeId)) {
			if (gatewayNodes.contains(next)) {
				// recurse through nested gateways
				result.addAll(resolveTarget(next, graph, gatewayNodes));
			} else {
				result.add(next);
			}
		}

		// Deduplicate in case multiple paths lead to same node
		return result.stream().distinct().toList();
	}

	public Boolean checkDefinationExist(String definitionName) {
		Boolean nameAlreadyExist = true;
		try {
			String savedDefinitionName = iDefinitionRepo.checkDefinationExist(definitionName);
			if (definitionName == null || (definitionName != null && !definitionName.equals(savedDefinitionName))) {
				nameAlreadyExist = false;
			}
			return nameAlreadyExist;
		} catch (Exception a_excep) {
			logger.error("Error ocurred while fetching Work Flow data : definitionName : " + definitionName, a_excep);
			throw new RuntimeException("Error ocurred while fetching Work Flow data");
		}
	}

	@Transactional
	public void migrateRunningInstances(WorkflowDefinition oldDef, WorkflowDefinition newDef,
			Map<String, String> statusMapping) {

		// 1. Fetch all active instances of the old workflow definition
		List<WorkflowInstance> activeInstances = iInstanceRepo.findByDefinitionIdAndIsActive(oldDef.getDefinitionId(),
				true);

		if (activeInstances.isEmpty()) {
			logger.info("No active instances found for definitionId={}", oldDef.getDefinitionId());
			return;
		}

		// 2. Build a map of new statuses (by name)
		Map<String, WorkflowStatus> newStatusMap = iStatusRepo.findByDefinitionId(newDef.getDefinitionId()).stream()
				.collect(Collectors.toMap(WorkflowStatus::getStatusName, s -> s));

		// 3. Iterate & update each instance
		for (WorkflowInstance instance : activeInstances) {

			// Get old status entity (lookup by ID from instance)
			WorkflowStatus oldStatus = iStatusRepo.findById(instance.getCurrentStatusId()).orElse(null);

			if (oldStatus == null) {
				logger.warn("Instance {} has invalid current_status_id={}, skipping...", instance.getInstanceId(),
						instance.getCurrentStatusId());
				continue;
			}

			// Use provided mapping OR fallback to same-name lookup
			String mappedStatusName = statusMapping.getOrDefault(oldStatus.getStatusName(), oldStatus.getStatusName());

			WorkflowStatus newStatus = newStatusMap.get(mappedStatusName);

			if (newStatus != null) {
				instance.setCurrentStatusId(newStatus.getStatusId()); // update FK
				instance.setDefinitionId(newDef.getDefinitionId()); // point to new definition
				instance.setVersion(newDef.getVersion());
				instance.setLastUpdatedTs(new Date()); // update timestamp
				instance.setLastUpdatedBy(detailsService.getUserDetails().getUserName()); // optional: set updater

				iInstanceRepo.save(instance);

				logger.info("Migrated instance {} from {} → {}", instance.getInstanceId(), oldStatus.getStatusName(),
						newStatus.getStatusName());
			} else {
				// Could not map — decide: suspend, cancel, or keep as-is
				logger.warn("Instance {} in unmapped status '{}', keeping old state.", instance.getInstanceId(),
						oldStatus.getStatusName());
			}
		}
	}
}
