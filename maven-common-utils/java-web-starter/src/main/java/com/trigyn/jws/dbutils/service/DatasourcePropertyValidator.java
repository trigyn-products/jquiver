package com.trigyn.jws.dbutils.service;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.TypeMismatchException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trigyn.jws.dbutils.entities.DatasourcePropertyValidationResult;
import com.trigyn.jws.dbutils.vo.ConnectionPropertyVO;

public final class DatasourcePropertyValidator {

	private static final int MAX_DISTANCE = 3;

	private DatasourcePropertyValidator() {
	}

	public static DatasourcePropertyValidationResult validate(BasicDataSource datasource,
			String datasourcePropertiesJson, List<ConnectionPropertyVO> supportedProperties) {

		if (StringUtils.isBlank(datasourcePropertiesJson)) {
			return new DatasourcePropertyValidationResult("SUCCESS", null);
		}

		Type type = new TypeToken<Map<String, String>>() {
		}.getType();

		Map<String, String> properties = new Gson().fromJson(datasourcePropertiesJson, type);

		if (properties == null || properties.isEmpty()) {
			return new DatasourcePropertyValidationResult("SUCCESS", null);
		}

		BeanWrapper wrapper = new BeanWrapperImpl(datasource);

		Set<String> documentedKeys = new HashSet<>();

		if (supportedProperties != null) {
			for (ConnectionPropertyVO property : supportedProperties) {
				if (StringUtils.isNotBlank(property.getKey())) {
					documentedKeys.add(property.getKey());
				}
			}
		}

		Set<String> beanProperties = new HashSet<>();

		for (PropertyDescriptor pd : wrapper.getPropertyDescriptors()) {
			if (wrapper.isWritableProperty(pd.getName())) {
				beanProperties.add(pd.getName());
			}
		}

		Set<String> allKnownProperties = new HashSet<>();
		allKnownProperties.addAll(beanProperties);
		allKnownProperties.addAll(documentedKeys);

		List<String> unknownProperties = new ArrayList<>();

		for (Map.Entry<String, String> entry : properties.entrySet()) {

			String key = StringUtils.trim(entry.getKey());
			String value = StringUtils.trim(entry.getValue());

			if (StringUtils.isBlank(key)) {
				return new DatasourcePropertyValidationResult("ERROR", "Datasource property name cannot be blank.");
			}

			if (wrapper.isWritableProperty(key)) {

				try {
					wrapper.setPropertyValue(key, value);
				} catch (TypeMismatchException ex) {
					return new DatasourcePropertyValidationResult("ERROR",
							"Invalid value '" + value + "' for datasource property '" + key + "'.");
				}

				continue;
			}

			if (documentedKeys.contains(key)) {
				datasource.addConnectionProperty(key, value);
				continue;
			}

			String suggestion = findClosestProperty(key, allKnownProperties);

			if (suggestion != null) {

			    datasource.addConnectionProperty(key, value);

			    return new DatasourcePropertyValidationResult(
			            "WARNING",
			            "Property '" + key + "' is not recognized. Did you mean '" + suggestion
			                    + "'? Do you want to continue?");
			}

			// Unknown property. Allow it, but warn the user.
			datasource.addConnectionProperty(key, value);
			unknownProperties.add(key);
		}

		if (!unknownProperties.isEmpty()) {

			String message = "Unknown datasource propert" + (unknownProperties.size() > 1 ? "ies" : "y") + ": "
					+ String.join(", ", unknownProperties) ;

			return new DatasourcePropertyValidationResult("WARNING", message);
		}

		return new DatasourcePropertyValidationResult("SUCCESS", null);
	}

	/**
	 * Finds the closest matching datasource property name.
	 *
	 * <p>
	 * The method first checks for properties that start with the user input (prefix
	 * match), since users often type only part of a property name (for example,
	 * "max" for "maxIdle"). If no prefix match is found, it falls back to
	 * Levenshtein distance to detect common typing mistakes (for example, "usrname"
	 * -> "username").
	 * </p>
	 *
	 * @param input      Property name entered by the user.
	 * @param candidates Supported datasource property names.
	 * @return The closest matching property name if a reasonable match is found;
	 *         otherwise {@code null}.
	 */
	private static String findClosestProperty(String input, Set<String> candidates) {

		if (StringUtils.isBlank(input) || candidates == null || candidates.isEmpty()) {
			return null;
		}

		String normalizedInput = input.toLowerCase();

		// Step 1: Prefer prefix match (e.g. "max" -> "maxIdle")
		for (String candidate : candidates) {
			if (candidate.toLowerCase().startsWith(normalizedInput)) {
				return candidate;
			}
		}

		// Step 2: Fall back to Levenshtein distance for typos
		LevenshteinDistance distance = new LevenshteinDistance();

		int bestDistance = Integer.MAX_VALUE;
		String bestMatch = null;

		for (String candidate : candidates) {

			// Ignore completely unrelated words by matching first 2 characters
			if (!candidate.toLowerCase()
					.startsWith(normalizedInput.substring(0, Math.min(2, normalizedInput.length())))) {
				continue;
			}

			int d = distance.apply(normalizedInput, candidate.toLowerCase());

			if (d < bestDistance) {
				bestDistance = d;
				bestMatch = candidate;
			}
		}

		return bestDistance <= MAX_DISTANCE ? bestMatch : null;
	}
}