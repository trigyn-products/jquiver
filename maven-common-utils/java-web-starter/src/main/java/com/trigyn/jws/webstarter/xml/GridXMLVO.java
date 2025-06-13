package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.gridutils.entities.GridDetails;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "gridData")
@XmlAccessorType(XmlAccessType.FIELD)
public class GridXMLVO extends XMLVO {

	@XmlElement(name = "grid")
	private List<GridDetails> gridDetails = new ArrayList<>();

	public List<GridDetails> getGridDetails() {
		return gridDetails;
	}

	public void setGridDetails(List<GridDetails> gridDetails) {
		this.gridDetails = gridDetails;
	}

}
