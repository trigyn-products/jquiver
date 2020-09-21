package app.trigyn.common.typeahead.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.trigyn.common.typeahead.dao.TypeAheadDAO;
import app.trigyn.common.typeahead.dao.TypeAheadRepository;
import app.trigyn.common.typeahead.entities.Autocomplete;
import app.trigyn.common.typeahead.model.AutocompleteParams;
import app.trigyn.common.typeahead.model.AutocompleteVO;

@Service
@Transactional(readOnly = false)
public class TypeAheadService {
    
    @Autowired
	private TypeAheadDAO typeAheadDAO = null;

	@Autowired
	private TypeAheadRepository typeAheadRepository = null;
	
	/**
	 * @param autocompleteParams
	 * @return
	 */
	public List<Map<String, Object>> getAutocompleteData(AutocompleteParams autocompleteParams) {
		return typeAheadDAO.getAutocompleteData(autocompleteParams);
	}
	
	/**
	 * @param autocompleteParams
	 * @return
	 */
	public Integer getCountOfData(AutocompleteParams autocompleteParams) {
		return typeAheadDAO.getCountOfData(autocompleteParams);
    }

	public AutocompleteVO getAutocompleteDetailsId(String autocompleteId) throws Exception {
		Autocomplete autocomplete = typeAheadRepository.findById(autocompleteId)
                .orElseThrow(() -> new Exception("Autocomplete not found with id : " + autocompleteId));
        AutocompleteVO autocompleteVO = new AutocompleteVO(autocomplete.getAutocompleteId(), autocomplete.getAutocompleteDesc(), autocomplete.getAutocompleteSelectQuery());
        return autocompleteVO;
	}
    
}