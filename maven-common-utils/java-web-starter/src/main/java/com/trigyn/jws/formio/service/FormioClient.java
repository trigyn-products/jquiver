package com.trigyn.jws.formio.service;

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.postgresql.util.PGobject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.trigyn.jws.dbutils.service.ApplicationContextProvider;
import com.trigyn.jws.dynamicform.dao.DynamicFormDAO;
import com.trigyn.jws.formio.resources.ResourceLoader;
import com.trigyn.jws.formio.storages.FileStorage;
import com.trigyn.jws.formio.storages.FormioBase64FileStorage;
import com.trigyn.jws.formio.utils.FormIOUtils;
import com.trigyn.jws.resourcebundle.repository.interfaces.IResourceBundleRepository;

import jakarta.servlet.http.HttpServletRequest;

public class FormioClient implements FormClient {

	public FormioClient() {
		super();
	}

	private static final Map<String, Boolean>	SUBMISSION_PROCESSING_DECISIONS_CACHE	= new ConcurrentHashMap<>();
	private static final String					GRID_NO_ROW_WRAPPING_PROPERTY			= "noRowWrapping";

	private static final ObjectMapper			JSON_MAPPER								= new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).setDefaultMergeable(false);
	private static final ParseContext			JAYWAY_PARSER							= JsonPath
			.using(Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).build());
	private ResourceLoader						defaultResourceLoader;

	private DynamicFormDAO						dynamicFormDAO							= ApplicationContextProvider
			.getBean(DynamicFormDAO.class);

	private IResourceBundleRepository			iResourceBundleRepository				= ApplicationContextProvider
			.getBean(IResourceBundleRepository.class);

	HttpServletRequest							request									= (HttpServletRequest) ((ServletRequestAttributes) Objects
			.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

	private JsonNode							compArr									= JSON_MAPPER
			.createObjectNode();

	public FormioClient(ResourceLoader defaultResourceLoader) {
		this.defaultResourceLoader = defaultResourceLoader;
	}

	@Override
	public String getFormWithData(String formKey, ObjectNode currentVariables) {
		return getFormWithData(formKey, currentVariables, defaultResourceLoader, new FormioBase64FileStorage());
	}

	@Override
	public String getFormWithData(String formKey, ObjectNode currentVariables, ResourceLoader resourceLoader) {
		return getFormWithData(formKey, currentVariables, resourceLoader, new FormioBase64FileStorage());
	}

	@Override
	public String getFormWithData(String formKey, ObjectNode currentVariables, FileStorage fileStorage) {
		return getFormWithData(formKey, currentVariables, defaultResourceLoader, fileStorage);
	}

	JsonNode jData = JSON_MAPPER.createObjectNode();

	@Override
	public String getFormWithData(String formKey, Object formData) {
		try {
			// Clean and sanitize formData (e.g., convert SQLXML, UUID, etc. to strings)
			Object		safeFormData	= sanitizeFormData(formData);
			String		jsonString		= new Gson().toJson(safeFormData);
			JsonNode	formMetaData	= getFormByKey(formKey);
			JsonNode	formJsonData	= JSON_MAPPER.readTree(jsonString);
			JsonNode	data			= wrapGridData(formJsonData, formMetaData);
			JsonNode	dataMerged		= merge(data);
			((ObjectNode) formMetaData).set("data", dataMerged);
			return dataMerged.toString();
		} catch (IOException exec) {
			throw new RuntimeException("Failed to get form: '" + formKey + "'", exec);
		}
	}

	public JsonNode getFormVariables(JsonNode formDefinition, JsonNode formMetaData, JsonNode currentVariables) {
		return getFormVariables(listChildComponents(formDefinition), formMetaData, currentVariables);
	}

	public JsonNode merge(JsonNode... data) {
		return null;

	}

	public JsonNode merge(JsonNode data) throws IOException {
		Map<String, List<JsonNode>>	jsonMap		= new HashMap<>();
		JsonNode					response	= JSON_MAPPER.createObjectNode();
		if (data.isObject()) {
			response = data;
		} else if (data.isArray()) {
			ArrayNode arrayNode = (ArrayNode) data;
			for (JsonNode jsonNode : arrayNode) {
				List<JsonNode>							jsonNodes	= new ArrayList<>();
				Iterator<Map.Entry<String, JsonNode>>	fields		= jsonNode.fields();
				while (fields.hasNext()) {
					Map.Entry<String, JsonNode> field = fields.next();
					if (jsonMap.containsKey(field.getKey())) {
						List<JsonNode> existingJsonNodes = jsonMap.get(field.getKey());
						existingJsonNodes.add(field.getValue());
						jsonMap.put(field.getKey(), existingJsonNodes);
					} else {
						jsonNodes.add(field.getValue());
						jsonMap.put(field.getKey(), jsonNodes);
					}
				}
			}
			response = JSON_MAPPER.valueToTree(jsonMap);
		}
		return response;
	}

	public JsonNode traverse(JsonNode root, JsonNode data) throws JsonMappingException, JsonProcessingException {

		if (root.isObject()) {
			Iterator<String> fieldNames = root.fieldNames();
			while (fieldNames.hasNext()) {
				String		fieldName	= fieldNames.next();
				JsonNode	fieldValue	= root.get(fieldName);
				if (fieldValue.has("input") && fieldValue.get("input").asBoolean()) {
					Map.Entry<String, ? extends JsonNode> json = getFormVariable(root, data, data);
					if (json != null)
						((ObjectNode) jData).put(json.getKey(), json.getValue());
				} else {
					traverse(fieldValue, data);
				}
			}
		} else if (root.isArray()) {
			ArrayNode arrayNode = (ArrayNode) root;

			arrayNode.forEach(node -> {

				if (node.has("components")) {
					try {
						Map.Entry<String, ? extends JsonNode> json = getFormVariable(node, data, data);
						{
							if (json != null) {
								((ObjectNode) compArr).put(json.getKey(), json.getValue());
							}
						}
						traverse(node, data);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					if (node.has("input") && node.get("input").asBoolean()) {
						Iterator<String> fieldNames = compArr.fieldNames();
						while (fieldNames.hasNext()) {
							String		fieldName	= fieldNames.next();
							JsonNode	fieldValue	= compArr.get(fieldName);
							if (fieldValue != null) {
								Map.Entry<String, ? extends JsonNode> json = getFormVariable(node, data, data);
								{
									if (json != null && json.getKey() != null) {

										((ObjectNode) fieldValue).put(json.getKey(), json.getValue());
									}
								}
							} else {
								Map.Entry<String, ? extends JsonNode> json = getFormVariable(node, data, data);
								{
									if (json != null && json.getKey() != null) {

										((ObjectNode) jData).put(json.getKey(), json.getValue());
									}
								}
							}

						}
					}
				}

			});
			return compArr;
		}
		return compArr;
	}

	@Override
	public String getFormWithData(String formKey, ObjectNode currentVariables, ResourceLoader resourceLoader,
			FileStorage fileStorage) {
		try {
			JsonNode	formDefinition	= getFormByKey(formKey);
			JsonNode	cleanData		= null;
			JsonNode	data			= wrapGridData(cleanData, formDefinition);
			((ObjectNode) formDefinition).set("data", data);
			return formDefinition.toString();
		} catch (Exception exec) {
			throw new RuntimeException("Failed to get form: '" + formKey + "'", exec);
		}
	}

	@Override
	public boolean shouldProcessSubmission(String formKey, String submissionState) {
		return shouldProcessSubmission(formKey, submissionState, defaultResourceLoader);
	}

	@Override
	public boolean shouldProcessSubmission(String formKey, String submissionState, ResourceLoader resourceLoader) {
		JsonNode	formDefinition	= getFormByKey(formKey);
		String		cacheKey		= String.format("%s-%s", formDefinition.toString(), submissionState);
		return SUBMISSION_PROCESSING_DECISIONS_CACHE.computeIfAbsent(cacheKey,
				key -> shouldProcessSubmission(formDefinition, submissionState));
	}

	@Override
	public List<String> getRootFormFieldNames(String formKey) {
		return getRootFormFieldNames(formKey, defaultResourceLoader);
	}

	@Override
	public List<String> getRootFormFieldNames(String formKey, ResourceLoader resourceLoader) {
		JsonNode formDefinition = getFormByKey(formKey);
		return Optional.of(listChildComponents(formDefinition)).stream().flatMap(Collection::stream)
				.filter(component -> component.path("input").asBoolean())
				.filter(component -> StringUtils.isNotBlank(component.path("key").asText()))
				.map(component -> component.get("key").asText()).collect(Collectors.toList());
	}

	@Override
	public List<String> getFormFieldPaths(String formKey, ResourceLoader resourceLoader) {
		JsonNode formDefinition = getFormByKey(formKey);
		return Optional.of(listChildComponents(formDefinition)).stream().flatMap(Collection::stream)
				.filter(component -> component.path("input").asBoolean())
				.filter(component -> StringUtils.isNotBlank(component.path("key").asText()))
				.map(this::getComponentTreeNames).flatMap(Collection::stream).collect(Collectors.toList());
	}

	private List<String> getComponentTreeNames(JsonNode component) {
		List<String>	result			= new ArrayList<>();
		String			componentName	= component.path("key").asText();
		result.add(componentName);
		if (!isContainerComponent(component) && !isArrayComponent(component))
			return result;
		Optional.of(listChildComponents(component)).stream().flatMap(Collection::stream)
				.filter(childComponent -> childComponent.path("input").asBoolean())
				.filter(childComponent -> StringUtils.isNotBlank(childComponent.path("key").asText()))
				.map(this::getComponentTreeNames).flatMap(Collection::stream)
				.forEach(childComponent -> result.add(String.format("%s.%s", componentName, childComponent)));
		return result;
	}

	public JsonNode getFormByKey(String formIoId) {
		try {
			FormIOUtils	formIOUtils	= new FormIOUtils();
			String		formIoJson	= formIOUtils.getFormMetaData(formIoId).toString();
			if (formIoJson.isBlank() == false && formIoJson != null) {
				return JSON_MAPPER.readTree(formIoJson);
			}
		} catch (JsonProcessingException je) {
			throw new RuntimeException("Failed to get form: '" + formIoId + "'", je);
		} catch (Exception exec) {
			throw new RuntimeException("Failed to get form: '" + formIoId + "'", exec);
		}
		return null;
	}

	String getFormIoCommand(String operation, String formDefinition, String data, String customComponentsDir)
			throws IOException {
		ObjectNode command = JSON_MAPPER.createObjectNode();
		command.set("form", JSON_MAPPER.readTree(formDefinition));
		command.set("data", JSON_MAPPER.readTree(data));
		command.put("operation", operation);
		command.put("resourcePath", toSafePath(customComponentsDir));
		return command.toString();
	}

	private String toSafePath(String customComponentsDir) {
		return customComponentsDir.replaceAll("\\\\", "\\\\\\\\");
	}

	protected JsonNode expandSubforms(JsonNode component) {
		Collector<JsonNode, ArrayNode, ArrayNode>	arrayNodeCollector		= Collector.of(JSON_MAPPER::createArrayNode,
				ArrayNode::add, ArrayNode::addAll);
		Function<JsonNode, JsonNode>				expandSubformsFunction	= getExpandSubformsFunction();
		JsonNode									childComponents			= !component.isArray()
				? getChildComponents(component)
				: component;
		JsonNode									components				= toStream(childComponents)
				.map(expandSubformsFunction).collect(arrayNodeCollector);
		return !component.isArray() ? ((ObjectNode) component).set("components", components) : components;
	}

	private Function<JsonNode, JsonNode> getExpandSubformsFunction() {
		String[] componentsWithChildren = { "container", "tree", "datagrid", "editgrid", "well", "columns", "fieldset",
				"panel", "table", "tabs" };
		return component -> {
			if (hasTypeOf(component, componentsWithChildren) || component.isArray()) {
				return expandSubforms(component);
			} else if (hasTypeOf(component, "form")) {
				return convertToContainer(component);
			} else if (component.has("components")) {
				return expandSubforms(component);
			} else {
				return component;
			}
		};
	}

	private JsonNode convertToContainer(JsonNode formDefinition) {
		List<String>							formAttributes				= asList("src", "reference", "form",
				"unique", "project", "path");
		Predicate<Map.Entry<String, JsonNode>>	nonFormAttributesPredicate	= field -> !formAttributes
				.contains(field.getKey());
		return JSON_MAPPER.valueToTree(toFieldStream(formDefinition).filter(nonFormAttributesPredicate)
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue)));
	}

	private boolean shouldProcessSubmission(JsonNode form, String submissionState) {
		Filter saveStateComponentsFilter = Filter
				.filter((Criteria.where("action").eq("saveState").and("state").eq(submissionState)));
		return toStream(JAYWAY_PARSER.parse(form).read("$..components[?]", saveStateComponentsFilter))
				.map(component -> component.at("/properties/isSubmissionProcessed").asBoolean(true)).findFirst()
				.orElse(true);
	}

	public JsonNode wrapGridData(JsonNode data, JsonNode definition) {
		if (data.isObject()) {
			return wrapGridDataInObject(data, definition);
		}
		if (data.isArray()) {
			return wrapGridDataInArray((ArrayNode) data, definition);
		}
		return data;
	}

	public JsonNode wrapGridDataInObject(JsonNode data, JsonNode definition) {
		JsonNode dataWithWrappedChildren = getFormVariables(definition, data, data);
		return dataWithWrappedChildren;
	}

	public JsonNode wrapGridDataInArray(ArrayNode data, JsonNode definition) {
		ArrayNode wrappedData = data.deepCopy();
		if (isGridUnwrapped(definition)) {
			String wrapperName = definition.at("/components/0/key").asText();
			for (int index = 0; index < data.size(); index++) {
				JsonNode	arrayElement	= data.get(index);
				ObjectNode	wrapper			= JsonNodeFactory.instance.objectNode();
				wrapper.set(wrapperName, arrayElement);
				wrappedData.set(index, wrapper);
			}
		}
		/*
		 * for (int index = 0; index < data.size(); index++) { JsonNode wrappedElement =
		 * wrapGridData(wrappedData.get(index), definition); wrappedData.set(index,
		 * wrappedElement); }
		 */
		return wrappedData;
	}

	public boolean isGridUnwrapped(JsonNode definition) {
		// JsonNode noRowWrappingProperty =
		// definition.at(String.format("/properties/%s",
		// GRID_NO_ROW_WRAPPING_PROPERTY));
		return true;
	}

	public boolean hasChildComponents(JsonNode definition) {
		return !definition.at("/components").isMissingNode();
	}

	public JsonNode getChildComponents(JsonNode component) {
		if (hasTypeOf(component, "columns"))
			return component.get("columns");
		if (hasTypeOf(component, "table"))
			return component.get("rows");
		return component.get("components");
	}

	public List<JsonNode> listChildComponents(JsonNode definition) {
		final Set<String>	layoutComponentTypes	= new HashSet<>(
				asList("well", "table", "columns", "fieldset", "panel", "Tabs"));
		final Set<String>	containerComponentTypes	= new HashSet<>(asList("well", "fieldset", "panel", "Tabs"));
		List<JsonNode>		nodes					= new ArrayList<>();
		toStream(definition.get("components"))
				.filter(component -> !layoutComponentTypes.contains(component.get("type").asText()))
				.forEach(nodes::add);
		toStream(definition.get("components"))
				.filter(component -> containerComponentTypes.contains(component.get("type").asText()))
				.flatMap(component -> toStream(component.get("components"))).forEach(nodes::add);
		toStream(definition.get("components")).filter(component -> "columns".equals(component.get("type").asText()))
				.flatMap(component -> toStream(component.get("columns")))
				.flatMap(component -> toStream(component.get("components"))).forEach(nodes::add);
		toStream(definition.get("components")).filter(component -> "table".equals(component.get("type").asText()))
				.flatMap(component -> toStream(component.get("rows"))).flatMap(this::toStream)
				.flatMap(component -> toStream(component.get("components"))).forEach(nodes::add);
		return nodes;
	}

	public JsonNode unwrapGridData(JsonNode data, JsonNode definition) {
		if (hasChildComponents(definition)) {
			List<JsonNode> childComponents = listChildComponents(definition);
			if (data.isObject()) {
				return unwrapGridDataFromObject(data, childComponents);
			}
			if (data.isArray()) {
				return unwrapGridDataFromArray(data, childComponents);
			}
		}
		return data;
	}

	public JsonNode unwrapGridDataFromObject(JsonNode data, List<JsonNode> childComponents) {
		ObjectNode unwrappedData = JsonNodeFactory.instance.objectNode();
		for (JsonNode childDefinition : childComponents) {
			String key = childDefinition.get("key").asText();
			if (data.has(key)) {
				unwrappedData.set(key, unwrapGridData(data, childDefinition, key));
			}
		}
		return unwrappedData;
	}

	public JsonNode unwrapGridDataFromArray(JsonNode data, List<JsonNode> childComponents) {
		ArrayNode unwrappedArray = data.deepCopy();
		for (int index = 0; index < data.size(); index++) {
			ObjectNode currentNode = JsonNodeFactory.instance.objectNode();
			for (JsonNode childDefinition : childComponents) {
				String		key				= childDefinition.get("key").asText();
				JsonNode	unwrappedData	= unwrapGridData(data.get(index), childDefinition, key);
				currentNode.set(key, unwrappedData);
			}
			unwrappedArray.set(index, currentNode);
		}
		return unwrappedArray;
	}

	public JsonNode unwrapGridData(JsonNode data, JsonNode childDefinition, String key) {
		if (!data.has(key)) {
			return data;
		}
		data = unwrapGridData(data.get(key), childDefinition);
		if (isArrayComponent(childDefinition)) {
			data = unwrapGridData(childDefinition, (ArrayNode) data);
		}
		return data;
	}

	public JsonNode unwrapGridData(JsonNode gridDefinition, ArrayNode data) {
		ArrayNode	components				= (ArrayNode) gridDefinition.get("components");
		ArrayNode	unwrappedData			= JsonNodeFactory.instance.arrayNode();
		JsonNode	noRowWrappingProperty	= gridDefinition
				.at(String.format("/properties/%s", GRID_NO_ROW_WRAPPING_PROPERTY));
		if (!noRowWrappingProperty.isMissingNode() && TRUE.equals(noRowWrappingProperty.asBoolean())
				&& (components.size() == 1)) {
			data.forEach(node -> unwrappedData.add(node.elements().next()));
		} else {
			unwrappedData.addAll(data);
		}
		return unwrappedData;
	}

	public boolean isContainerComponent(JsonNode componentDefinition) {
		return hasTypeOf(componentDefinition, "form", "container", "survey");
	}

	public boolean isArrayComponent(JsonNode componentDefinition) {
		return hasTypeOf(componentDefinition, "datagrid", "editgrid");
	}

	public boolean isLayoutComponent(JsonNode component) {
		return hasTypeOf(component, "well", "columns", "fieldset", "panel", "table", "tabs");
	}

	public boolean hasTypeOf(JsonNode component, String... types) {
		JsonNode	typeField		= component.get("type");
		String		componentType	= typeField != null ? typeField.asText() : "";
		boolean		result			= false;
		for (String type : types)
			result |= componentType.equals(type);
		return result;
	}

	public Stream<JsonNode> toStream(JsonNode node) {
		return StreamSupport.stream(node.spliterator(), false);
	}

	public Stream<Map.Entry<String, JsonNode>> toFieldStream(JsonNode node) {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(node.fields(), Spliterator.ORDERED), false);
	}

	public JsonNode getFormVariables(List<JsonNode> formComponents, JsonNode submittedVariables,
			JsonNode currentVariables) {
		return Optional.ofNullable(formComponents).map(Collection::stream).orElse(Stream.empty())
				.filter(component -> StringUtils.isNotBlank(component.get("key").asText()))
				.flatMap(this::extractChildrenIfLayoutComponent)
				.map(component -> getFormVariable(component, submittedVariables, currentVariables))
				.filter(Objects::nonNull).collect(JSON_MAPPER::createObjectNode, (resultData,
						cleanDataEntry) -> resultData.set(cleanDataEntry.getKey(), cleanDataEntry.getValue()),
						ObjectNode::setAll);
	}

	public Stream<JsonNode> extractChildrenIfLayoutComponent(JsonNode component) {
		if (isLayoutComponent(component)) {
			return extractChildrenFromLayoutComponent(component);
		} else {
			return Stream.of(component);
		}
	}

	public Stream<JsonNode> extractChildrenFromLayoutComponent(JsonNode component) {
		if (hasTypeOf(component, "table")) {
			return toStream(component.get("rows")).flatMap(this::toStream)
					.flatMap(cell -> toStream(cell.get("components"))).flatMap(this::extractChildrenIfLayoutComponent);
		}
		if (hasTypeOf(component, "columns")) {
			return toStream(component.get("columns")).flatMap(column -> toStream(column.get("components")))
					.flatMap(this::extractChildrenIfLayoutComponent);
		}
		if (hasTypeOf(component, "tabs")) {
			return toStream(component.get("components")).flatMap(tab -> toStream(tab.get("components")))
					.flatMap(this::extractChildrenIfLayoutComponent);
		}
		return toStream(component.get("components")).flatMap(this::extractChildrenIfLayoutComponent);
	}

	public Map.Entry<String, ? extends JsonNode> getFormVariable(JsonNode component, JsonNode submittedVariables,
			JsonNode currentVariables) {
		if (isContainerComponent(component)) {
			return getContainerVariable(component, submittedVariables, currentVariables);
		} else if (isArrayComponent(component)) {
			return getArrayComponentVariable(component, submittedVariables, currentVariables);
		} else {
			return getSimpleComponentVariable(component, submittedVariables, currentVariables);
		}
	}

	public Map.Entry<String, ? extends JsonNode> getContainerVariable(JsonNode component, JsonNode submittedVariables,
			JsonNode currentVariables) {
		String		componentKey	= component.get("key").asText();
		/*
		 * submittedVariables = submittedVariables.has(componentKey) ?
		 * submittedVariables.get(componentKey) : JSON_MAPPER.createObjectNode();
		 * currentVariables = currentVariables.has(componentKey) ?
		 * currentVariables.get(componentKey) : JSON_MAPPER.createObjectNode();
		 */
		JsonNode	containerValue	= getFormVariables(listChildComponents(component), submittedVariables,
				currentVariables);
		return new SimpleEntry<>(componentKey, containerValue);
	}

	public Map.Entry<String, ArrayNode> getArrayComponentVariable(JsonNode components, JsonNode submittedVariables,
			JsonNode currentVariables) {
		ArrayNode	containerValue		= JSON_MAPPER.createArrayNode();
		String		componentKey		= components.get("key").asText();
		JsonNode	editableArrayData	= submittedVariables.has(componentKey) ? submittedVariables.get(componentKey)
				: JSON_MAPPER.createObjectNode();
		JsonNode	readOnlyArrayData	= currentVariables.has(componentKey) ? currentVariables.get(componentKey)
				: JSON_MAPPER.createArrayNode();
		if (editableArrayData != null) {
			for (int i = 0; i < editableArrayData.size(); i++) {
				JsonNode	editableArrayItemData		= editableArrayData.get(i);
				JsonNode	readOnlyDataArrayItemData	= readOnlyArrayData.has(i) ? readOnlyArrayData.get(i)
						: JSON_MAPPER.createObjectNode();
				JsonNode	containerItemValue			= getFormVariables(listChildComponents(components),
						editableArrayItemData, readOnlyDataArrayItemData);
				containerValue.add(containerItemValue);
			}
		}
		return containerValue.size() == 0 ? null : new SimpleEntry<>(componentKey, containerValue);
	}

	public Map.Entry<String, ? extends JsonNode> getSimpleComponentVariable(JsonNode component, JsonNode editableData,
			JsonNode readOnlyData) {
		if (component.get("key") != null) {
			String					componentKey		= component.get("key").asText();
			Entry<String, JsonNode>	editableDataEntry	= editableData != null && editableData.has(componentKey)
					? new SimpleEntry<>(componentKey, editableData.get(componentKey))
					: null;
			Entry<String, JsonNode>	readOnlyDataEntry	= readOnlyData != null && readOnlyData.has(componentKey)
					? new SimpleEntry<>(componentKey, readOnlyData.get(componentKey))
					: null;
			return !component.path("disabled").asBoolean() ? editableDataEntry : readOnlyDataEntry;
		}
		return null;

	}

	private Object sanitizeFormData(Object data) {
		if (data instanceof Map) {
			Map<String, Object> sanitized = new LinkedHashMap<>();
			((Map<?, ?>) data).forEach((key, value) -> {
				try {
					sanitized.put(String.valueOf(key), sanitizeValue(value));
				} catch (SQLException e) {
					throw new RuntimeException("Failed to convert SQLXML to String", e);
				}
			});
			return sanitized;
		} else if (data instanceof List) {
			List<Object> sanitized = new ArrayList<>();
			for (Object item : (List<?>) data) {
				sanitized.add(sanitizeFormData(item));
			}
			return sanitized;
		}
		return data;
	}

	private Object sanitizeValue(Object value) throws SQLException {
		if (value == null)
			return null;

		if (value instanceof java.sql.SQLXML) {
			String xmlContent = ((SQLXML) value).getString();
			return xmlContent;
		}

		if (value instanceof PGobject) {
			PGobject pgObj = (PGobject) value;
			if ("json".equalsIgnoreCase(pgObj.getType()) || "jsonb".equalsIgnoreCase(pgObj.getType())) {
				// Optionally parse JSON string if needed
				return pgObj.getValue(); // returns JSON string
			}
		}

		// if (value instanceof UUID) {
		// return value.toString();
		// }

		// if (value instanceof java.sql.Clob) {
		// try {
		// Clob clob = (Clob) value;
		// return clob.getSubString(1, (int) clob.length());
		// } catch (Exception e) {
		// return null;
		// }
		// }

		// if (value instanceof java.sql.Blob || value instanceof
		// java.util.concurrent.locks.ReentrantLock) {
		// return null; // Not serializable
		// }

		// if (value instanceof Map || value instanceof List) {
		// return sanitizeFormData(value); // Recursive sanitization
		// }

		return value; // Primitive, String, etc.
	}

}
