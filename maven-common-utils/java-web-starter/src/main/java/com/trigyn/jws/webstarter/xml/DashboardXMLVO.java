package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;

@XmlRootElement(name = "dashboardData")
@XmlAccessorType (XmlAccessType.FIELD)
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
