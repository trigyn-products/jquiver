package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.gridutils.entities.GridDetails;

@XmlRootElement(name = "gridData")
@XmlAccessorType (XmlAccessType.FIELD)
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
