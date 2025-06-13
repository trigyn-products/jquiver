package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.typeahead.entities.Autocomplete;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "autocompleteData")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutocompleteXMLVO extends XMLVO {

	@XmlElement(name = "autocomplete")
	private List<Autocomplete> autocompleteDetails = new ArrayList<>();

	public List<Autocomplete> getAutocompleteDetails() {
		return autocompleteDetails;
	}

	public void setAutocompleteDetails(List<Autocomplete> autocompleteDetails) {
		this.autocompleteDetails = autocompleteDetails;
	}

}
