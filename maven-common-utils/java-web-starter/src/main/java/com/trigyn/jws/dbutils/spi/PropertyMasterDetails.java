package com.trigyn.jws.dbutils.spi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;

@Component
public class PropertyMasterDetails {

	private static final Logger						logger						= LogManager
			.getLogger(PropertyMasterDetails.class);

	private final ScheduledThreadPoolExecutor		scheduler					= (ScheduledThreadPoolExecutor) Executors
			.newScheduledThreadPool(1);

	private HashMap<PropertyMasterKeyVO, String>	propertyMasterDetails		= new HashMap<>();

	@Autowired
	private PropertyMasterDAO						propertyMasterDetailsDAO	= null;

	@PostConstruct
	private void initalizePropertyMasterDetails() {
		resetPropertyMasterDetails();
		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				resetPropertyMasterDetails();

			}
		}, 5, 5, TimeUnit.MINUTES);
	}

	public PropertyMasterDetails() {

	}

	public String getSystemPropertyValue(String propertyName) {
		return getPropertyValueFromPropertyMaster("system", "system", propertyName);

	}

	public String getPropertyValueFromPropertyMaster(String ownerId, String ownerType, String propertyName) {
		PropertyMasterKeyVO propertyMasterKey = new PropertyMasterKeyVO(ownerId, ownerType, propertyName);
		return this.propertyMasterDetails.getOrDefault(propertyMasterKey,
				getPropertyValueFromPropertyMasterFromDB(ownerId, ownerType, propertyName));

	}

	private String getPropertyValueFromPropertyMasterFromDB(String ownerId, String ownerType, String propertyName) {
		try {
			return propertyMasterDetailsDAO.findPropertyMasterValue(ownerType, ownerId, propertyName);
		} catch (Exception a_exc) {
			logger.error("Error while getting the data from DB : PropertyName : " +propertyName, a_exc);
			return null;
		}
	}

	public void resetPropertyMasterDetails() {
		synchronized (this) {
			List<Map<String, Object>> propertyMasterDetails = propertyMasterDetailsDAO.findAll();
			for (Map<String, Object> details : propertyMasterDetails) {
				PropertyMasterKeyVO	propertyMasterKey	= new PropertyMasterKeyVO(details.get("ownerId").toString(),
						details.get("ownerType").toString(), details.get("propertyName").toString());
				String				propertyValue		= details.get("propertyValue").toString().trim();
				this.propertyMasterDetails.put(propertyMasterKey, propertyValue);

			}
		}
	}

	public void setPropertyMasterDetails(String ownerId, String ownerType, String propertyName, String propertyValue) {
		PropertyMasterKeyVO propertyMasterKey = new PropertyMasterKeyVO(ownerId, ownerType, propertyName);
		this.propertyMasterDetails.put(propertyMasterKey, propertyValue);
	}

	public Map<PropertyMasterKeyVO, String> getAllProperties() {
		return this.propertyMasterDetails;
	}
}
