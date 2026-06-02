package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.workflow.entities.WorkflowDefinition;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "workflowData")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class WorkflowXMLVO extends XMLVO {

    private List<WorkflowDefinition> workflowDetails = new ArrayList<>();

    @XmlElement(name = "workflow")
    public List<WorkflowDefinition> getWorkflowDetails() {
        return workflowDetails;
    }

    public void setWorkflowDetails(List<WorkflowDefinition> workflowDetails) {
        this.workflowDetails = workflowDetails;
    }
}