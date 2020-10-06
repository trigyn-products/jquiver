package com.trigyn.jws.typeahead.service;

import java.util.List;
import java.util.Map;

import com.trigyn.jws.dbutils.service.TemplateVersionService;
import com.trigyn.jws.typeahead.dao.TypeAheadDAO;
import com.trigyn.jws.typeahead.dao.TypeAheadRepository;
import com.trigyn.jws.typeahead.entities.Autocomplete;
import com.trigyn.jws.typeahead.model.AutocompleteParams;
import com.trigyn.jws.typeahead.model.AutocompleteVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

@Service
@Transactional(readOnly = false)
public class TypeAheadService {
    
    @Autowired
	private TypeAheadDAO typeAheadDAO 								= null;

	@Autowired
	private TypeAheadRepository typeAheadRepository 				= null;
	
	@Autowired
	private TemplateVersionService templateVersionService			= null;
	
	
	public List<Map<String, Object>> getAutocompleteData(AutocompleteParams autocompleteParams) {
		return typeAheadDAO.getAutocompleteData(autocompleteParams);
	}
	
	
	public Integer getCountOfData(AutocompleteParams autocompleteParams) {
		return typeAheadDAO.getCountOfData(autocompleteParams);
    }

	public AutocompleteVO getAutocompleteDetailsId(String autocompleteId) throws Exception {
		Autocomplete autocomplete = typeAheadRepository.findById(autocompleteId)
                .orElseThrow(() -> new Exception("Autocomplete not found with id : " + autocompleteId));
        AutocompleteVO autocompleteVO = new AutocompleteVO(autocomplete.getAutocompleteId(), autocomplete.getAutocompleteDesc(), autocomplete.getAutocompleteSelectQuery());
        return autocompleteVO;
	}
	
	public String saveAutocompleteDetails(MultiValueMap<String, String> formDataMap) throws Exception {
		String autoCompleteId				= formDataMap.getFirst("autoCompleteId");
		String autoCompleteDesc				= formDataMap.getFirst("autoCompleteDescription");
		String autoCompleteSelectQuery 		= formDataMap.getFirst("autoCompleteSelectQuery");
		
		Autocomplete autocomplete	= new Autocomplete();
		autocomplete.setAutocompleteId(autoCompleteId);
		autocomplete.setAutocompleteDesc(autoCompleteDesc);
		autocomplete.setAutocompleteSelectQuery(autoCompleteSelectQuery);
		typeAheadRepository.saveAndFlush(autocomplete);
		templateVersionService.saveTemplateVersion(autocomplete,null, autoCompleteId, "autocomplete_details");
		
		return autoCompleteId;
		
	}	
    
}