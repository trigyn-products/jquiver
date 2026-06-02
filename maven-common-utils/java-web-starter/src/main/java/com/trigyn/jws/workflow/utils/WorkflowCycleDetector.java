package com.trigyn.jws.workflow.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.workflow.entities.WorkflowTransition;
import com.trigyn.jws.workflow.repository.interfaces.IWorkflowTransitionRepository;

@Component
public class WorkflowCycleDetector {
	
	private final static Logger				logger	= LoggerFactory.getLogger(WorkflowCycleDetector.class);

	@Autowired
	private IWorkflowTransitionRepository	transitionRepository;

    public boolean hasCycle(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();

        for (String node : graph.keySet()) {
            if (detectCycle( graph,node, visited, recStack)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean has_Cycle() {
        // Load all transitions from DB
        List<WorkflowTransition> transitions = transitionRepository.findAll();

        // Build adjacency list with String keys (since statusId is VARCHAR)
        Map<String, List<String>> graph = new HashMap<>();
        for (WorkflowTransition transition : transitions) {
            String from = transition.getFromStatus().getStatusId();
            String to   = transition.getToStatus().getStatusId();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        }

        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (String node : graph.keySet()) {
            if (detectCycle(graph, node, visited, recursionStack)) {
                logger.info("Cycle detected! Infinite loop may occur in workflow.");
                return true;
            }
        }

        logger.info("No cycle detected. Workflow transitions are safe.");
        return false;
    }

    private boolean detectCycle(Map<String, List<String>> graph,
                                String current,
                                Set<String> visited,
                                Set<String> recStack) {
        if (recStack.contains(current)) {
            return true;
        }

        if (visited.contains(current)) {
            return false;
        }

        visited.add(current);
        recStack.add(current);

        List<String> neighbors = graph.getOrDefault(current, Collections.emptyList());

        for (String neighbor : neighbors) {
            if (detectCycle(graph, neighbor, visited, recStack)) {
                return true;
            }
        }

        recStack.remove(current);
        return false;
    }
}
