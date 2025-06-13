package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "dashboardData")
@XmlAccessorType(XmlAccessType.FIELD)
public class DashboardXMLVO extends XMLVO {

	public List<Dashboard> getDashboardDetails() {
		return dashboardDetails;
	}

	public void setDashboardDetails(List<Dashboard> dashboardDetails) {
		this.dashboardDetails = dashboardDetails;
	}

	@XmlElement(name = "dashboard")
	private List<Dashboard> dashboardDetails = new ArrayList<>();

}
