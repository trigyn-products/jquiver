package com.trigyn.jws.typeahead.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.typeahead.dao.TypeAheadDAO;
import com.trigyn.jws.typeahead.dao.TypeAheadRepository;
import com.trigyn.jws.typeahead.entities.Autocomplete;
import com.trigyn.jws.typeahead.model.AutocompleteParams;
import com.trigyn.jws.typeahead.model.AutocompleteVO;
import com.trigyn.jws.typeahead.utility.Constant;

@Service
@Transactional(readOnly = false)
public class TypeAheadService {

	private final static Logger		logger					= LogManager.getLogger(TypeAheadService.class);

	@Autowired
	private TypeAheadDAO			typeAheadDAO			= null;

	@Autowired
	private TypeAheadRepository		typeAheadRepository		= null;

	@Autowired
	private ModuleVersionService	moduleVersionService	= null;

	@Autowired
	private IUserDetailsService		userDetailsService		= null;

	public List<Map<String, Object>> getAutocompleteData(AutocompleteParams autocompleteParams, Map<String, Object> requestParamMap)
			throws Exception {
		return typeAheadDAO.getAutocompleteData(autocompleteParams, requestParamMap);
	}

	public Integer getCountOfData(AutocompleteParams autocompleteParams) {
		return typeAheadDAO.getCountOfData(autocompleteParams);
	}

	public AutocompleteVO getAutocompleteDetailsId(String autocompleteId) throws Exception {
		Autocomplete	autocomplete	= typeAheadRepository.findById(autocompleteId)
				.orElseThrow(() -> new Exception("Autocomplete not found with id : " + autocompleteId));
		String			selectQuery		= "<#noparse>" + autocomplete.getAutocompleteSelectQuery() + "</#noparse>";
		AutocompleteVO	autocompleteVO	= new AutocompleteVO(autocomplete.getAutocompleteId(), autocomplete.getAutocompleteDesc(),
				selectQuery, autocomplete.getDatasourceId());
		return autocompleteVO;
	}

	@Transactional(readOnly = false)
	public String saveAutocompleteDetails(MultiValueMap<String, String> formDataMap, Integer sourceTypeId) throws Exception {
		logger.debug("Inside TypeAheadService.saveAutocompleteDetails(formDataMap: {}, sourceTypeId: {})", formDataMap, sourceTypeId);

		Autocomplete			autocomplete			= new Autocomplete();
		UserDetailsVO			userDetailsVO			= userDetailsService.getUserDetails();
		Date					date					= new Date();
		String					autoCompleteId			= formDataMap.getFirst("autocompleteId");
		String					autoCompleteDesc		= formDataMap.getFirst("autocompleteDesc");
		String					autoCompleteSelectQuery	= formDataMap.getFirst("autocompleteQuery");
		String					dataSourceId			= formDataMap.getFirst("dataSourceId");

		Optional<Autocomplete>	autocompleteOptional	= typeAheadRepository.findById(autoCompleteId);
		if (autocompleteOptional != null && autocompleteOptional.isEmpty() == false) {
			autocomplete = autocompleteOptional.get();
			autocomplete.setLastUpdatedBy(userDetailsVO.getUserName());
		} else {
			autocomplete.setAutocompleteId(autoCompleteId);
			autocomplete.setCreatedBy(userDetailsVO.getUserName());
			autocomplete.setCreatedDate(date);
			if (StringUtils.isBlank(dataSourceId) == false) {
				autocomplete.setDatasourceId(dataSourceId);
			}
		}
		autocomplete.setAutocompleteDesc(autoCompleteDesc);
		autocomplete.setAutocompleteSelectQuery(autoCompleteSelectQuery);
		autocomplete.setLastUpdatedTs(date);

		AutocompleteVO autocompleteVO = convertEntityToVO(autoCompleteId, autoCompleteDesc, autoCompleteSelectQuery);

		typeAheadRepository.save(autocomplete);
		moduleVersionService.saveModuleVersion(autocompleteVO, null, autoCompleteId, "jq_autocomplete_details", sourceTypeId);

		return autoCompleteId;

	}

	public AutocompleteVO convertEntityToVO(String autoCompleteId, String autoCompleteDesc, String autoCompleteSelectQuery) {
		logger.debug("Inside TypeAheadService.convertEntityToVO(autoCompleteId: {}, autoCompleteDesc: {}, autoCompleteSelectQuery: {})",
				autoCompleteId, autoCompleteDesc, autoCompleteSelectQuery);

		AutocompleteVO autocompleteVO = new AutocompleteVO();
		autocompleteVO.setAutocompleteId(autoCompleteId);
		autocompleteVO.setAutocompleteDesc(autoCompleteDesc);
		autocompleteVO.setAutocompleteQuery(autoCompleteSelectQuery);
		return autocompleteVO;
	}

	//	public List<String> getAllTablesListInSchema() {
	//		return typeAheadDAO.getAllTablesListInSchema();
	//	}

	@Transactional(readOnly = false)
	public void updateAutocompleteDataSource(String autoCompleteId, String dataSourceId) throws Exception {
		logger.debug("Inside TypeAheadService.updateAutocompleteDataSource(autoCompleteId: {}, dataSourceId: {})", autoCompleteId,
				dataSourceId);

		Autocomplete			autocomplete			= new Autocomplete();
		UserDetailsVO			userDetailsVO			= userDetailsService.getUserDetails();
		Date					date					= new Date();

		Optional<Autocomplete>	autocompleteOptional	= typeAheadRepository.findById(autoCompleteId);
		if (autocompleteOptional != null && autocompleteOptional.isEmpty() == false) {
			autocomplete = autocompleteOptional.get();
			autocomplete.setLastUpdatedBy(userDetailsVO.getUserName());
			autocomplete.setLastUpdatedTs(date);
			autocomplete.setDatasourceId(StringUtils.isBlank(dataSourceId) == true ? null : dataSourceId);

			AutocompleteVO autocompleteVO = convertEntityToVO(autoCompleteId, autocomplete.getAutocompleteDesc(),
					autocomplete.getAutocompleteSelectQuery());

			typeAheadRepository.save(autocomplete);
			moduleVersionService.saveModuleVersion(autocompleteVO, null, autoCompleteId, "jq_autocomplete_details",
					Constant.MASTER_SOURCE_VERSION_TYPE);
		}

	}

	public List<Map<String, Object>> getColumnNamesByTableName(String additionalDataSourceId, String tableName) {
		logger.debug("Inside TypeAheadService.getColumnNamesByTableName(additionalDataSourceId: {}, tableName: {})", additionalDataSourceId,
				tableName);
		return typeAheadDAO.getColumnNamesByTableName(additionalDataSourceId, tableName);
	}

}