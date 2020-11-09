package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.typeahead.entities.Autocomplete;

@XmlRootElement(name = "autocompleteData")
@XmlAccessorType (XmlAccessType.FIELD)
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
