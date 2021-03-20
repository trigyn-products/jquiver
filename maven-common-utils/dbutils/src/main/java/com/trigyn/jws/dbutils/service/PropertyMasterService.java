package com.trigyn.jws.dbutils.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;

@Service
@Transactional
public class PropertyMasterService {

	@Autowired
	private PropertyMasterDAO		propertyMasterDAO		= null;

	@Autowired
	private PropertyMasterDetails	propertyMasterDetails	= null;

	public String findPropertyMasterValue(String propertyName) throws Exception {
		return propertyMasterDetails.getSystemPropertyValue(propertyName);
	}

	public String findPropertyMasterValue(String ownerType, String ownerId, String propertyName) throws Exception {
		return propertyMasterDetails.getPropertyValueFromPropertyMaster(ownerId, ownerType, propertyName);
	}

	public String getDateFormatByName(String ownerType, String ownerId, String propertyName, String formatName)
			throws Exception {
		Gson				gson			= new Gson();
		String				jwsDateFormat	= propertyMasterDetails.getPropertyValueFromPropertyMaster(ownerId,
				ownerType, propertyName);
		Map<Object, Object>	dateFormatMap	= gson.fromJson(jwsDateFormat, Map.class);
		String				dbDateFormat	= (String) dateFormatMap.get(formatName);
		return dbDateFormat;
	}

}
