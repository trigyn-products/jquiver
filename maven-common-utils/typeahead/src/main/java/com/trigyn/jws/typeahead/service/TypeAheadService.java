package com.trigyn.jws.typeahead.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.typeahead.dao.TypeAheadDAO;
import com.trigyn.jws.typeahead.dao.TypeAheadRepository;
import com.trigyn.jws.typeahead.entities.Autocomplete;
import com.trigyn.jws.typeahead.model.AutocompleteParams;
import com.trigyn.jws.typeahead.model.AutocompleteVO;

@Service
@Transactional(readOnly = false)
public class TypeAheadService {

	@Autowired
	private TypeAheadDAO			typeAheadDAO			= null;

	@Autowired
	private TypeAheadRepository		typeAheadRepository		= null;

	@Autowired
	private ModuleVersionService	moduleVersionService	= null;

	public List<Map<String, Object>> getAutocompleteData(AutocompleteParams autocompleteParams) throws Exception {
		return typeAheadDAO.getAutocompleteData(autocompleteParams);
	}

	public Integer getCountOfData(AutocompleteParams autocompleteParams) {
		return typeAheadDAO.getCountOfData(autocompleteParams);
	}

	public AutocompleteVO getAutocompleteDetailsId(String autocompleteId) throws Exception {
		Autocomplete	autocomplete	= typeAheadRepository.findById(autocompleteId)
				.orElseThrow(() -> new Exception("Autocomplete not found with id : " + autocompleteId));
		AutocompleteVO	autocompleteVO	= new AutocompleteVO(autocomplete.getAutocompleteId(),
				autocomplete.getAutocompleteDesc(), autocomplete.getAutocompleteSelectQuery());
		return autocompleteVO;
	}

	@Transactional(readOnly = false)
	public String saveAutocompleteDetails(MultiValueMap<String, String> formDataMap, Integer sourceTypeId)
			throws Exception {
		String			autoCompleteId			= formDataMap.getFirst("autocompleteId");
		String			autoCompleteDesc		= formDataMap.getFirst("autocompleteDesc");
		String			autoCompleteSelectQuery	= formDataMap.getFirst("autocompleteQuery");

		Autocomplete	autocomplete			= new Autocomplete();
		autocomplete.setAutocompleteId(autoCompleteId);
		autocomplete.setAutocompleteDesc(autoCompleteDesc);
		autocomplete.setAutocompleteSelectQuery(autoCompleteSelectQuery);

		AutocompleteVO autocompleteVO = convertEntityToVO(autoCompleteId, autoCompleteDesc, autoCompleteSelectQuery);

		typeAheadRepository.save(autocomplete);
		moduleVersionService.saveModuleVersion(autocompleteVO, null, autoCompleteId, "jq_autocomplete_details",
				sourceTypeId);

		return autoCompleteId;

	}

	public AutocompleteVO convertEntityToVO(String autoCompleteId, String autoCompleteDesc,
			String autoCompleteSelectQuery) {
		AutocompleteVO autocompleteVO = new AutocompleteVO();
		autocompleteVO.setAutocompleteId(autoCompleteId);
		autocompleteVO.setAutocompleteDesc(autoCompleteDesc);
		autocompleteVO.setAutocompleteQuery(autoCompleteSelectQuery);
		return autocompleteVO;
	}

	//	public List<String> getAllTablesListInSchema() {
	//		return typeAheadDAO.getAllTablesListInSchema();
	//	}

	public List<Map<String, Object>> getColumnNamesByTableName(String tableName) {
		return typeAheadDAO.getColumnNamesByTableName(tableName);
	}

}