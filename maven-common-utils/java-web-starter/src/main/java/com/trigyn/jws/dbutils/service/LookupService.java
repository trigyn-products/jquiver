package com.trigyn.jws.dbutils.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.trigyn.jws.dbutils.repository.IJwsLookUpRepository;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.vo.LookupDetailsVO;

@Service
@Transactional(readOnly = true)
public class LookupService {

	@Autowired
	private IJwsLookUpRepository lookUpRepository = null;

	public Map<Integer, String> getLookupDetailsByName(String lookupName) throws Exception {
		Map<Integer, String>	lookupDetailsMap	= new LinkedHashMap<>();
		List<LookupDetailsVO>	lookupDetailsVOs	= lookUpRepository.getLookUpDetailsByName(lookupName, Constant.DEFAULT_LANGUAGE_ID,
				Constant.DEFAULT_LANGUAGE_ID, Constant.RecordStatus.INSERTED.getStatus());
		if (!CollectionUtils.isEmpty(lookupDetailsVOs)) {
			for (LookupDetailsVO lookupDetailsVO : lookupDetailsVOs) {
				lookupDetailsMap.put(lookupDetailsVO.getRecordId(), lookupDetailsVO.getRecordDescription());
			}
		}
		return lookupDetailsMap;
	}

}
