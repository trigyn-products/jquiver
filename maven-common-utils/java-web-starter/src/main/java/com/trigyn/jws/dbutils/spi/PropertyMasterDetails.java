package com.trigyn.jws.dbutils.spi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dbutils.entities.CspConfig;
import com.trigyn.jws.dbutils.entities.PatternFlagResolver;
import com.trigyn.jws.dbutils.entities.XSSPattern;
import com.trigyn.jws.dbutils.repository.IModuleListingRepository;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;

import jakarta.annotation.PostConstruct;

@Component
public class PropertyMasterDetails {

	private static final Logger						logger						= LoggerFactory
			.getLogger(PropertyMasterDetails.class);

	private final ScheduledThreadPoolExecutor		scheduler					= (ScheduledThreadPoolExecutor) Executors
			.newScheduledThreadPool(1);

	private HashMap<PropertyMasterKeyVO, String>	propertyMasterDetails		= new HashMap<>();

	@Autowired
	private PropertyMasterDAO						propertyMasterDetailsDAO	= null;

	private List<Pattern>							xssPatternList				= new ArrayList<>();

	private CspConfig								cspConfig					= new CspConfig();
	
	private List<String>							exclusionList				= new ArrayList<>();
	
	@Autowired
	private IModuleListingRepository				iModuleListingRepository	= null;
	
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
			logger.error("Error while getting the data from DB : PropertyName : " + propertyName, a_exc);
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

	public List<Pattern> getXssPatternList() {
		return xssPatternList;
	}

	public CspConfig getCspConfig() {
		return cspConfig;
	}

	public List<String> getExclusionList() {
		return exclusionList;
	}
	
	@PostConstruct
	public void fetchXSSPatterns() {
		try {
			String				xssPatternJson	= getSystemPropertyValue("xssPattern");
			ObjectMapper		objectMapper	= new ObjectMapper();
			List<XSSPattern>	patterns		= objectMapper.readValue(xssPatternJson,
					new TypeReference<List<XSSPattern>>() {
														});
			String				prettyJson		= objectMapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(xssPatternJson);
			logger.debug("====Print as Pretty Printer XssPatternJson===" + prettyJson);
			for (XSSPattern xssPattern : patterns) {
				Pattern pattern = null;
				if (null != xssPattern.getPatterns()) {
					int flags = PatternFlagResolver.getFlagsFromNames(xssPattern.getPatterns());
					pattern = Pattern.compile(xssPattern.getRegex(), flags);
				} else {
					pattern = Pattern.compile(xssPattern.getRegex());
				}
				xssPatternList.add(pattern);
			}

		} catch (Exception e) {
			logger.error("Error while fetching XSSPattern" + e.getMessage());
			throw new IllegalArgumentException("Error while fetching XSSPattern" + e.getMessage());
		}
	}

	@PostConstruct
	public void fetchCSPHeader() {
		try {
			exclusionList = iModuleListingRepository.getModuleURLBySystem();
			String cspHeaderJson = getSystemPropertyValue("csp-header");
			if (null != cspHeaderJson) {
				ObjectMapper	objectMapper	= new ObjectMapper();
				String			prettyJson		= objectMapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString(cspHeaderJson);
				logger.info("====Print as Pretty Printer cspHeaderJson===" + prettyJson);
				cspConfig = objectMapper.readValue(cspHeaderJson, CspConfig.class);
			} else {
				cspConfig.setCSPEnable(false);
			}
		} catch (Exception e) {
			logger.error("Error while fetching CSPHeader" + e.getMessage());
			throw new IllegalArgumentException("Error while fetching CSPHeader" + e.getMessage());
		}
	}

}
