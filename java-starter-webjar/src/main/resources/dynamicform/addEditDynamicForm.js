class AddEditDynamicForm {
	constructor() {
	}

	loadAddEditDynamicForm() {
		const context = this;
		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }, waitSeconds: 120 });
		require(["vs/editor/editor.main"], function() {
			dashletSQLEditor = monaco.editor.create(document.getElementById("sqlEditor"), {
				value: $("#sqlContent").val().trim(),
				language: "sql",
				roundedSelection: false,
				scrollBeyondLastLine: false,
				readOnly: false,
				theme: "vs-dark",
				wordWrap: 'wordWrapColumn',
				wordWrapColumn: 100,
				wordWrapMinified: true,
				wrappingIndent: "indent"
			});

			var newSuggestionsArray = [];
			for (var iCounter = 0; iCounter < suggestionArray.length; iCounter++) {
				var suggestion = suggestionArray[iCounter];
				newSuggestionsArray.push({
					label: suggestion.label,
					kind: suggestion.kinda,
					insertText: suggestion.insertText,
					insertTextRules: monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet
				});
			}
			var newJSSuggestionsArray = [];
			for (var iCounter = 0; iCounter < jsSuggestionArray.length; iCounter++) {
				var suggestion = jsSuggestionArray[iCounter];
				newJSSuggestionsArray.push({
					label: suggestion.label,
					kind: suggestion.kinda,
					insertText: suggestion.insertText,
					insertTextRules: monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet
				});
			}
			monaco.languages.registerCompletionItemProvider('sql', {
				triggerCharacters: ["$"],
				provideCompletionItems: (model, position) => {
					var textUntilPosition = model.getValueInRange({
						startLineNumber: position.lineNumber,
						startColumn: position.column - 1,
						endLineNumber: position.lineNumber,
						endColumn: position.column
					});
					if (textUntilPosition == '$') {
						if (model.id == "$model1") {
							if (selectQueryType == "2") {
								return { suggestions: JSON.parse(JSON.stringify(newJSSuggestionsArray)) };
							} else {
								return { suggestions: JSON.parse(JSON.stringify(newSuggestionsArray)) };
							}
						} else if (model.id == "$model3") {
							let str = model.id;
							let indexCount = str.slice(-1);
							let index = indexCount - 3;
							if ($("#selectcontainer_" + index).val() == "4") {
								return { suggestions: JSON.parse(JSON.stringify(newJSSuggestionsArray)) };
							}
							else {
								return { suggestions: JSON.parse(JSON.stringify(newSuggestionsArray)) };
							}
						}
					}
				}
			});

			dashletSQLEditor.onDidChangeCursorSelection((e) => {
				if (dashletSQLEditor._id == "1") {
					if (selectQueryType == "2") {
						if (e.source == "snippet") {
							var position = e.oldSelections[0]; // Get current mouse position
							var text = dashletSQLEditor.getValue(position);
							var splitedText = text.split("\n");
							var lineContent = splitedText[position.endLineNumber - 1]; // Get selected line content
							var textArray = lineContent.split('');
							var line = dashletSQLEditor.getPosition().lineNumber;
							var col = dashletSQLEditor.getPosition().column;
							if (textArray.includes("$")) {
								var textToInsert = ""; // text to be inserted
								splitedText[position.endLineNumber - 1] = [lineContent.slice(0, position.endColumn - 2), textToInsert, lineContent.slice(position.endColumn - 1)].join(''); // Append the text exactly at the selected position (position.column -1)
								dashletSQLEditor.setValue(splitedText.join("\n")); // Save the value back to the Editor
								dashletSQLEditor.setPosition({ lineNumber: line, column: col });
								dashletSQLEditor.focus();
							}
							else {
								splitedText[position.endLineNumber - 1];
								dashletSQLEditor.setValue(splitedText.join("\n")); // Save the value back to the Editor
								dashletSQLEditor.setPosition({ lineNumber: line, column: col });
								dashletSQLEditor.focus();
							}
						}
					}
				}
			});

			dashletSQLEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, function() {
				typeOfAction('dynamic-form-manage-details', $("#savedAction").find("button"),
					addEdit.saveDynamicForm.bind(addEdit), addEdit.backToDynamicFormListing);
			});
			dashletSQLEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Shift | monaco.KeyCode.KEY_M, function() {
				resizeMonacoEditor(dashletSQLEditor, "sqlContainer", "sqlEditor");
			});
			dashletSQLEditor.onDidChangeModelContent(function() {
				$('#errorMessage').hide();
			});
			$("#sqlContent").remove();
		});

		require(["vs/editor/editor.main"], function() {
			dashletHTMLEditor = monaco.editor.create(document.getElementById("htmlEditor"), {
				language: "html",
				roundedSelection: false,
				scrollBeyondLastLine: false,
				readOnly: false,
				theme: "vs-dark",
				wordWrap: 'wordWrapColumn',
				wordWrapColumn: 250,
				wordWrapMinified: true,
				wrappingIndent: "indent"
			});
			var newSuggestionsArray = [];
			for (var iCounter = 0; iCounter < suggestionArray.length; iCounter++) {
				var suggestion = suggestionArray[iCounter];
				newSuggestionsArray.push({
					label: suggestion.label,
					kind: suggestion.kinda,
					insertText: suggestion.insertText,
					insertTextRules: monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet
				});
			}
			monaco.languages.registerCompletionItemProvider('html', {
				triggerCharacters: ["$"],
				provideCompletionItems: (model, position) => {
					var textUntilPosition = model.getValueInRange({
						startLineNumber: position.lineNumber,
						startColumn: position.column - 1,
						endLineNumber: position.lineNumber,
						endColumn: position.column
					});
					if (textUntilPosition == '$') {
						return {
							suggestions: JSON.parse(JSON.stringify(newSuggestionsArray))
						}
					}
				}
			});

			dashletHTMLEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, function() {
				typeOfAction('dynamic-form-manage-details', $("#savedAction").find("button"),
					addEdit.saveDynamicForm.bind(addEdit), addEdit.backToDynamicFormListing);
			});
			dashletHTMLEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Shift | monaco.KeyCode.KEY_M, function() {
				resizeMonacoEditor(dashletHTMLEditor, "htmlContainer", "htmlEditor");
			});
			dashletHTMLEditor.onDidChangeModelContent(function() {
				$('#errorMessage').hide();
			});
			$("#htmlContent").remove();

			if (formId != "") {
				$.ajax({
					type: "POST",
					url: contextPath + "/cf/gfhc",
					data: { formId: formId },
					success: function(formBody) {
						if (formBody !== undefined && formBody !== "") {
							dashletHTMLEditor.setValue(formBody);
						}
					}
				});
			}
		});

		let formId = $("#formId").val();

		if (formId != "") {
			$.ajax({
				type: "POST",
				url: contextPath + "/cf/gfsq",
				data: { formId: formId },
				success: function(data) {
					for (let counter = 0; counter < data.length; ++counter) {
						context.addSaveQueryEditor(null, data[counter].variableName, data[counter].formSaveQuery,
							data[counter].queryType, data[counter].datasourceId);
					}
					getEntityRoles();
				}
			});
		} else {
			context.addSaveQueryEditor();
		}
		$("#saveSqlContent").remove();
	}

	backToDynamicFormListing() {
		window.location.href = contextPath + "/cf/dfl"
	}

	addSaveQueryEditor(element, variableName, saveQueryContent, queryType, datasourceId) {
		let context = this;
		let formQueryId;
		let formSaveQuery;

		if (saveQueryContent != undefined) {
			formSaveQuery = saveQueryContent.trim();
		}

		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }, waitSeconds: 120 });
		require(["vs/editor/editor.main"], function() {
			let index = dashletSQLEditors.length;
			let parentElement;
			if (element != null) {
				parentElement = $(element).parent().parent().parent().parent();
			}
			let datasource = retrieveDatasource();
			let inputElement = "<div class='col-3'><label for='inputcontainer_" + index + "' style='white-space:nowrap'> Variable Name </label><input id='inputcontainer_" + index + "' type ='text' class='form-control' /></div>";
			let selectElement = "<div class='col-3'><label for='selectcontainer_" + index + "' style='white-space:nowrap'>Query Type </label><select id='selectcontainer_" + index + "' class='form-control' onchange='addEdit.updateDatasourceState(this);'><option value='2'>Insert-Update-Delete Query</option><option value='3'>Stored Procedure</option><option value='4'>Javascript</option></select></div>";
			let datasourceEditor = "<div class='col-3'><div><label for='datasourcecontainer_" + index + "' style='white-space:nowrap'>Datasource </label><select id='datasourcecontainer_" + index + "' name='dataSourceId' class='form-control' onchange='showHideTableAutocomplete()'><option id='defaultConnection' value=''>Default Connection</option>";

			if (datasource != null || datasource != undefined || datasource != "") {
				datasource.forEach(function(dataSourceObj) {
					datasourceEditor += "<option value=" + dataSourceObj.additionalDatasourceId + " data-product-name=" + dataSourceObj.databaseProductName + ">" + dataSourceObj.datasourceName + "</option>";
				});
			}
			datasourceEditor += '</select></div></div>'
			let buttonElement = "<div class='btn-icons float-right'><input type='button' id='addEditor_" + index + "' value='Add' class='margin-r-3 btn btn-primary' onclick='addEdit.addSaveQueryEditor(this);'><input type='button' id='removeTemplate_" + index + "' value='Remove' style='margin-left:4px;' class='btn btn-secondary' onclick='addEdit.removeSaveQueryEditor(this);'></div>";

			let daoContainer = $("<div id='daoContainerDiv_" + index + "' class='margin-t-25'><div class='row'>" + inputElement + "" + selectElement + "" + datasourceEditor + "<div class='col-3 margin-t-25 float-right'>" + buttonElement + "</div></div></div>");
			daoContainer.append("<div id='container_" + index + "' class='html_script' style='margin-top: 10px;'><div class='grp_lblinp'><div id='saveSqlContainer_" + index + "' class='ace-editor-container'><div id='saveSqlEditor_" + index + "' class='ace-editor'></div></div></div></div></div>");

			if (parentElement != undefined) {
				daoContainer.insertAfter(parentElement);
			} else {
				$("#saveScriptContainer").append(daoContainer);
			}

			if (variableName) {
				$("#inputcontainer_" + index).val(variableName);
			}

			if (datasourceId != undefined) {
				$("#datasourcecontainer_" + index).val(datasourceId);
			}

			if (queryType != undefined) {
				$("#selectcontainer_" + index).val(queryType);
				if (queryType == 4) {
					$("#datasourcecontainer_" + index).closest('div').hide();
				}
			}

			dashletSAVESQLEditor = monaco.editor.create(document.getElementById("saveSqlEditor_" + index), {
				value: formSaveQuery,
				language: "sql",
				roundedSelection: false,
				scrollBeyondLastLine: false,
				readOnly: false,
				theme: "vs-dark",
				wordWrap: 'wordWrapColumn',
				wordWrapColumn: 100,
				wordWrapMinified: true,
				wrappingIndent: "indent"
			});

			dashletSAVESQLEditor.onDidChangeCursorSelection((e) => {
				if (dashletSAVESQLEditor._id == "3") {
					let str = dashletSAVESQLEditor._id;
					let indexCount = str;
					let index = indexCount - 3;
					if ($("#selectcontainer_" + index).val() == "4") {
						if (e.source == "snippet") {
							var position = e.oldSelections[0]; // Get current mouse position
							var text = dashletSAVESQLEditor.getValue(position);
							var splitedText = text.split("\n");
							var lineContent = splitedText[position.endLineNumber - 1]; // Get selected line content
							var textArray = lineContent.split('');
							var line = dashletSAVESQLEditor.getPosition().lineNumber;
							var col = dashletSAVESQLEditor.getPosition().column;
							if (textArray.includes("$")) {
								var textToInsert = ""; // text to be inserted
								splitedText[position.endLineNumber - 1] = [lineContent.slice(0, position.endColumn - 2), textToInsert, lineContent.slice(position.endColumn - 1)].join(''); // Append the text exactly at the selected position (position.column -1)
								dashletSAVESQLEditor.setValue(splitedText.join("\n")); // Save the value back to the Editor
								dashletSAVESQLEditor.setPosition({ lineNumber: line, column: col });
								dashletSAVESQLEditor.focus();
							}
							else {
								splitedText[position.endLineNumber - 1];
								dashletSAVESQLEditor.setValue(splitedText.join("\n")); // Save the value back to the Editor
								dashletSAVESQLEditor.setPosition({ lineNumber: line, column: col });
								dashletSAVESQLEditor.focus();
							}
						}
					}
				}
			});

			dashletSAVESQLEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, function() {
				typeOfAction('dynamic-form-manage-details', $("#savedAction").find("button"),
					addEdit.saveDynamicForm.bind(addEdit), addEdit.backToDynamicFormListing);
			});
			dashletSAVESQLEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Shift | monaco.KeyCode.KEY_M, function() {
				resizeMonacoEditor(dashletSAVESQLEditor, "daoContainerDiv_" + index, "saveSqlEditor_" + index);
			});
			dashletSAVESQLEditor.onDidChangeModelContent(function() {
				$('#errorMessage').hide();
			});
			let editorObj = new Object();
			editorObj["index"] = index;
			editorObj["editor"] = dashletSAVESQLEditor;
			dashletSQLEditors.push(editorObj);
			$("#removeTemplate_0").remove();

			initialFormData = context.getFormData();
		});
	}

	removeSaveQueryEditor(element) {
		let index = element.id.split("_")[1];
		if (index != 0) {
			jqOpenDeletConfirmation(function() {
				$("#daoContainerDiv_" + index).remove();
				removeByAttribute(dashletSQLEditors, "index", index);
			});
		}
	}

	saveDynamicForm() {
		let context = this;
		let isDataSaved = false;
		let formValid = addEdit.validateDynamicForm();
		if (formValid) {
			let serializedForm = context.getFormData();
			if (initialFormData === serializedForm) {
				showMessage("Information saved successfully", "success");
				return true;
			}
			initialFormData = serializedForm;

			$.ajax({
				async: false,
				type: "GET",
				cache: false,
				url: contextPath + "/cf/cdd",
				data: {
					formName: $("#formName").val(),
				},
				success: function(data) {
					if (data != "") {
						if (data != $("#formId").val()) {
							return false;
						} else {
							isDataSaved = AddEditDynamicForm.prototype.saveFormData(serializedForm);
						}
					} else {
						isDataSaved = AddEditDynamicForm.prototype.saveFormData(serializedForm);
					}
				}
			});
		}
		return isDataSaved;
	}

	getFormData() {
		$("#formSelectQuery").val(dashletSQLEditor.getValue().toString());
		$("#formBody").val(dashletHTMLEditor.getValue().toString());
		//		let queries = new Array();	
		//		for(let iCounter = 0; iCounter < dashletSQLEditors.length; ++iCounter){
		//			let index = $("[id^=daoContainerDiv_]")[iCounter].id.split("_")[1];
		//			let editorObject = dashletSQLEditors.find(editors => editors["index"] == index);
		//			let queryContent = (editorObject["editor"].getValue().toString().trim());
		//			if(queryContent !== ""){
		//				queries.push(queryContent);
		//			}
		//		}
		//		$("#formSaveQuery").val(JSON.stringify(queries));


		let context = this;
		let isDataSaved = false;
		let queries = new Array();
		let variableNameArray = new Array();
		let queryTypeArray = new Array();
		let datasourceArray = new Array();
		let daoQueryArray = new Array();
		let dashletDetails = new Object();

		let form = $("#dynamicform");
		form.append('<input name="daoDetailsIds" id="daoDetailsIds" type="hidden" />');
		form.append('<input name="variableName" id="variableName" type="hidden" />');
		form.append('<input name="queryType" id="queryType" type="hidden" />');
		form.append('<input name="daoQueryDetails" id="daoQueryDetails" type="hidden" />');
		form.append('<input name="datasourceDetails" id="datasourceDetails" type="hidden" />');

		let saveEditorLength = $("[id^=daoContainerDiv_]").length;
		for (let iCounter = 0; iCounter < saveEditorLength; ++iCounter) {
			let index = $("[id^=daoContainerDiv_]")[iCounter].id.split("_")[1];
			let editorObject = dashletSQLEditors.find(editors => editors["index"] == index);
			let variableName = $('#inputcontainer_' + index).val();
			let queryType = $('#selectcontainer_' + index).val();
			let datasourceId = $('#datasourcecontainer_' + index).val();
			let daoQuery = (editorObject["editor"].getValue().toString().trim());
			variableNameArray.push(variableName);
			queryTypeArray.push(queryType);
			daoQueryArray.push(daoQuery);
			if (datasourceId == '') {
				datasourceId = null;
			}
			datasourceArray.push(datasourceId);
		}

		$("#variableName").val(JSON.stringify(variableNameArray));
		$("#queryType").val(JSON.stringify(queryTypeArray));
		$("#daoQueryDetails").val(JSON.stringify(daoQueryArray));
		$("#datasourceDetails").val(JSON.stringify(datasourceArray));

		return $("#dynamicform").serialize();
	}

	saveFormData(formData) {
		const context = this;
		let isDataSaved = false;
		$.ajax({
			type: "POST",
			async: false,
			url: contextPath + "/cf/sdfd",
			data: formData,
			success: function(data) {
				isDataSaved = true;
				$("#formId").val(data);
				saveEntityRoleAssociation(data);
				showMessage("Information saved successfully", "success");
			},
			error: function(xhr, error) {
				showMessage("Error occurred while saving", "error");
			},

		});
		return isDataSaved;
	}

	updateDatasourceState = function(selectedElem) {

		let context = this;
		let selectedOptionVal = $(selectedElem).find(":selected").val();
		let editorIndex = $(selectedElem).attr("id").split("_")[1];
		let editorObject = dashletSQLEditors.find(editors => editors["index"] == editorIndex);
		(editorObject["editor"]).setValue("");
		if (selectedOptionVal === "4") {
			$("#datasourcecontainer_" + editorIndex).val(null);
			$("#datasourcecontainer_" + editorIndex).closest('div').hide();
		} else {
			$("#datasourcecontainer_" + editorIndex).closest('div').show();
		}
	}

	validateDynamicForm = function() {
		let formName = $("#formName").val().trim();
		let selectQuery = $.trim(dashletSQLEditor.getValue().toString());
		let htmlQuery = $.trim(dashletHTMLEditor.getValue().toString());
		if (formName === "") {
			showMessage("Please enter valid form name", "error");
			return false;
		}
		if (selectQuery === "") {
			showMessage("Select query can not be blank", "error");
			return false;
		}
		if (htmlQuery === "") {
			showMessage("HTML content can not be blank", "error");
			return false;
		}
		let saveUpdateQuery;
		let isVariableEmpty = false;
		for (let iCounter = 0; iCounter < dashletSQLEditors.length; ++iCounter) {
			let index = $("[id^=daoContainerDiv_]")[iCounter].id.split("_")[1];
			let editorObject = dashletSQLEditors.find(editors => editors["index"] == index);
			let queryContent = (editorObject["editor"].getValue().toString().trim());
			let variableName = $('#inputcontainer_' + index).val();
			if (variableName === "") {
				isVariableEmpty = true;
				break;
			}

			saveUpdateQuery = queryContent;

			if (saveUpdateQuery === "") {
				break;
			}
		}
		if (isVariableEmpty === true) {
			showMessage("Variable name can not be blank", "error");
			return false;
		}
		if (saveUpdateQuery === "") {
			showMessage("Save/update query can not be blank", "error");
			return false;
		}
		return true;
	}

	hideErrorMessage() {
		$('#errorMessage').hide();
	}
}

let saveEntityRoleAssociation = function(savedFormId) {
	let roleIds = [];
	let entityRoles = new Object();
	entityRoles.entityName = $("#formName").val();
	entityRoles.moduleId = $("#moduleId").val();
	entityRoles.entityId = savedFormId;
	$.each($("#rolesMultiselect_selectedOptions_ul span.ml-selected-item"), function(key, val) {
		roleIds.push(val.id);
	});

	entityRoles.roleIds = roleIds;

	$.ajax({
		async: false,
		type: "POST",
		contentType: "application/json",
		url: contextPath + "/cf/ser",
		data: JSON.stringify(entityRoles),
		success: function(data) {
		}
	});
}
let getEntityRoles = function() {
	$.ajax({
		async: false,
		type: "GET",
		url: contextPath + "/cf/ler",
		data: {
			entityId: formId,
			moduleId: $("#moduleId").val(),
		},
		success: function(data) {
			$.each(data, function(key, val) {
				multiselect.setSelectedObject(val);

			});
		}
	});
}

let removeByAttribute = function(arrayObject, attr, value) {
	let iCounter = arrayObject.length;
	while (iCounter--) {
		if (arrayObject[iCounter]
			&& arrayObject[iCounter].hasOwnProperty(attr)
			&& (arguments.length > 2 && arrayObject[iCounter][attr] == value)) {
			arrayObject.splice(iCounter, 1);
		}
	}
	return arrayObject;
}

const SaveAction = {
	Return: 1,
	CreateNew: 2,
	Edit: 3
};

Object.freeze(SaveAction);
var isInAction = false;

function onSaveButtonClick(a_actionType) {
	if (isInAction) {
		showMessage("Multiple save action is not allowed", "error");
		return;
	}
	isInAction = true;
	let formData = null;
	try {
		formData = validateData();
	} catch (excp) {
		showMessage("Exception occurred while validating form", "error");
		isInAction = false;
		return;
	}
	if (formData === undefined) {
		showMessage("All fields are mandatory", "warn");
		isInAction = false;
		return false;
	}

	if (typeof onValidation == "function") {
		formData = onValidation(formData);
		if (formData == null || formData == undefined) {
			isInAction = false;
			return false;
		}
	}

	let successMsg = "Information saved successfully";
	if (typeof getSuccessMessage == "function") {
		try {
			successMsg = getSuccessMessage();
		} catch (excp) {
			isInAction = false;
			showMessage("Exception occurred while geeting success message", "error");
			return;
		}
	}
	let errorMsg = "Error occurred while saving";
	if (typeof getErrorMessage == "function") {
		try {
			errorMsg = getErrorMessage();
		} catch (excp) {
			isInAction = false;
			showMessage("Exception occurred while getting error message", "error");
			return;
		}
	}

	if (formData != null) {
		$.ajax({
			type: "POST",
			async: true,
			enctype: 'multipart/form-data',
			processData: false,
			contentType: false,
			data: formData,
			success: function(data) {
				showMessage(successMsg, "success");
				isInAction = false;
				if (typeof onSuccess == "function") {
					try {
						onSuccess(data, a_actionType);
					} catch (excp) {
						showMessage("Exception occurred while executing onSuccess", "error");
						return;
					}
				}
				if (a_actionType == SaveAction.Return) {
					if (typeof backToPreviousPage == "function") {
						try {
							backToPreviousPage();
						} catch (excp) {
							showMessage("Exception occurred while executing backToPreviousPage", "error");
							return;
						}
					}
					localStorage.setItem("jwsModuleAction", "saveAndReturn");
				} else if (a_actionType == SaveAction.CreateNew) {
					window.location = window.location.href.split("?")[0];
					localStorage.setItem("jwsModuleAction", "saveAndCreateNew");
				} else {
					localStorage.setItem("jwsModuleAction", "saveAndEdit");
				}
			},
			error: function(xhr, error) {
				isInAction = false;
				// captcha error handling	
				if (xhr.status == 412) {
					showMessage("Invalid Captcha", "error");
					$("#reloadCaptcha").trigger("click");
				}
				else {
					showMessage(errorMsg, "error");
					if (typeof onError == "function") {
						onError(xhr, error);
					}
				}
			},
		});
	}
	fileBinTempMap = new Map();
}

function validateData() {
	let formName = "addEditForm";
	if (typeof getFormName == "function") {
		formName = getFormName();
	}
	let selectElements = $("#" + formName).find("select").not(".optional");
	for (let iCounter = 0, length = selectElements.length; iCounter < length; iCounter++) {
		if ($(selectElements[iCounter]).prop("selectedIndex") < 0) {
			$(selectElements[iCounter]).closest("div").parent().effect("highlight", {}, 3000);
			$(selectElements[iCounter]).focus();
			return undefined;
		}
	}

	let serializedForm = $("#" + formName).serializeArray();
	serializedForm.push({ "name": "isEdit", "value": (isEdit + ""), "valueType": "int" });
	serializedForm.push({ "name": "formId", "value": formId, "valueType": "varchar" });
	for (let iCounter = 0, length = serializedForm.length; iCounter < length; iCounter++) {
		let fieldValue = $.trim(serializedForm[iCounter].value);
		let fieldName = $.trim(serializedForm[iCounter].name);
		let isFieldVisible = $("#" + fieldName).is(":visible");
		let isOptional = $("#" + fieldName).hasClass("optional");

		if (fieldValue !== "") {
			serializedForm[iCounter].value = fieldValue;
		} else if (isFieldVisible === true && isOptional === false) {
			$("#" + fieldName).focus();
			$("#" + fieldName).closest("div").parent().effect("highlight", {}, 3000);
			return undefined;
		}
	}
	serializedForm = serializedForm.formatSerializedArray();

	let files = $("#" + formName).find("input:file:not(.optional)");
	let hasFile = 0;
	for (let iCounter = 0, length = files.length; iCounter < length; iCounter++) {
		if ($(files[iCounter])[0] != null && $(files[iCounter])[0].files[0] != null) {
			hasFile = 1;
			break;
		}
	}

	if (hasFile == 0) {
		serializedForm.push({ "name": "hasFile", "value": "0", "valueType": "int" });
	} else {
		serializedForm.push({ "name": "hasFile", "value": "1", "valueType": "int" });
	}

	for (let iCounter = 0, length = files.length; iCounter < length; iCounter++) {
		if ($(files[iCounter]).val().trim().length < 1 && isOptional === false) {
			$(files[iCounter]).focus();
			$(files[iCounter]).closest("div").parent().effect("highlight", {}, 3000);
			return undefined;
		}
	}


	if (fileBins) {
		for (let fileBinCounter = 0; fileBinCounter < fileBins.length; fileBinCounter++) {
			serializedForm.push(fileBins[fileBinCounter]);
		}
	}

	let data = new FormData();
	data.append('formData', JSON.stringify(serializedForm.formatSerializedArray()));
	if (files.length > 0) {
		for (let iCounter = 0, length = files.length; iCounter < length; iCounter++) {
			data.append($(files[iCounter]).attr("name"), $(files[iCounter])[0].files[0]);
		}
	}
	return data;
}


//Support for FileBin starts here
var fileBins = new Array();
//end of FileBin

