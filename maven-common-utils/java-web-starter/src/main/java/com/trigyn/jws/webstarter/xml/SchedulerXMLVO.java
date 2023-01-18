package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.dynarest.entities.JqScheduler;

@XmlRootElement(name = "schedulerData")
@XmlAccessorType(XmlAccessType.FIELD)
public class SchedulerXMLVO extends XMLVO {

	@XmlElement(name = "scheduler")
	private List<JqScheduler> schedulerDetails = new ArrayList<>();

	public List<JqScheduler> getSchedulerDetails() {
		return schedulerDetails;
	}

	public void setSchedulerDetails(List<JqScheduler> schedulerDetails) {
		this.schedulerDetails = schedulerDetails;
	}

}
