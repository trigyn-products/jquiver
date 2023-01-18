class FileBinMaster {

	constructor() {
		this.selectValidator;
		this.uploadValidator;
		this.viewValidator;
		this.deleteValidator;
		this.fileConfigObj;
	}

	loadFinBinDetails = function() {
		let context = this;
		loadDefaultTab("filebin-default-template", context.updateFileBinTemplate);
		context.initializeFileSlider();
		if (edit == 1) {
			context.getEntityRoles();
		} else {
			let defaultAdminRole = { "roleId": "ae6465b3-097f-11eb-9a16-f48e38ab9348", "roleName": "ADMIN" };
			multiselect.setSelectedObject(defaultAdminRole);
		}

		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }, waitSeconds: 120 });
		require(["vs/editor/editor.main"], function() {
			context.selectValidator = monaco.editor.create(document.getElementById("selectValidator"), {
				value: context.fileConfigObj.selectQueryContent,
				language: "sql",
				roundedSelection: false,
				scrollBeyondLastLine: false,
				readOnly: false,
				theme: "hc-dark",
				wordWrap: "wordWrapColumn",
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
					if (textUntilPosition == "$") {
						if (model.id == "$model1") {
							if ($("#queryType_selectValidator").val() == "4") {
								return { suggestions: JSON.parse(JSON.stringify(newJSSuggestionsArray)) };
							} else {
								return { suggestions: JSON.parse(JSON.stringify(newSuggestionsArray)) };
							}
						} else if (model.id == "$model2") {
							if ($("#queryType_uploadValidator").val() == "4") {
								return { suggestions: JSON.parse(JSON.stringify(newJSSuggestionsArray)) };
							}
							else {
								return { suggestions: JSON.parse(JSON.stringify(newSuggestionsArray)) };
							}
						} else if (model.id == "$model2") {
							if ($("#queryType_uploadValidator").val() == "4") {
								return { suggestions: JSON.parse(JSON.stringify(newJSSuggestionsArray)) };
							}
							else {
								return { suggestions: JSON.parse(JSON.stringify(newSuggestionsArray)) };
							}
						} else if (model.id == "$model3") {
							if ($("#queryType_viewValidator").val() == "4") {
								return { suggestions: JSON.parse(JSON.stringify(newJSSuggestionsArray)) };
							}
							else {
								return { suggestions: JSON.parse(JSON.stringify(newSuggestionsArray)) };
							}
						}
						else if (model.id == "$model4") {
							if ($("#queryType_deleteValidator").val() == "4") {
								return { suggestions: JSON.parse(JSON.stringify(newJSSuggestionsArray)) };
							}
							else {
								return { suggestions: JSON.parse(JSON.stringify(newSuggestionsArray)) };
							}
						}
					}
				}
			});
			context.selectValidator.onDidChangeCursorSelection((e) => {
				if ($("#queryType_selectValidator").val() == "4") {
					if (e.source == "snippet") {
						var position = e.oldSelections[0]; // Get current mouse position
						var text = context.selectValidator.getValue(position);
						var splitedText = text.split("\n");
						var lineContent = splitedText[position.endLineNumber - 1]; // Get selected line content
						var textArray = lineContent.split('');
						var line = context.selectValidator.getPosition().lineNumber;
						var col = context.selectValidator.getPosition().column;
						if (textArray.includes("$")) {
							var textToInsert = ""; // text to be inserted
							splitedText[position.endLineNumber - 1] = [lineContent.slice(0, position.endColumn - 2), textToInsert, lineContent.slice(position.endColumn - 1)].join(''); // Append the text exactly at the selected position (position.column -1)
							context.selectValidator.setValue(splitedText.join("\n")); // Save the value back to the Editor
							context.selectValidator.setPosition({ lineNumber: line, column: col });
						}
						else {
							splitedText[position.endLineNumber - 1];
							context.selectValidator.setValue(splitedText.join("\n")); // Save the value back to the Editor
							context.selectValidator.setPosition({ lineNumber: line, column: col });
							context.selectValidator.focus();
						}
					}
				}
			});
			context.selectValidator.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, function() {
				typeOfAction('file-upload-config', $("#savedAction").find("button"), fileBinMaster.saveData.bind(fileBinMaster), fileBinMaster.backToPreviousPage);
			});
			context.selectValidator.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Shift | monaco.KeyCode.KEY_M, function() {
				resizeMonacoEditor(context.selectValidator, "selectValidator_container", "selectValidator");
			});
			context.selectValidator.onDidChangeModelContent(function() {
				$("#errorMessage").hide();
			});
			if ($("#selectValidator_query").text().trim() !== "") {
				$("#selectValidator_chkbox").attr("checked", "checked");
				$("#selectValidator_div").css("opacity", "1.0");
			}
		});

		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }, waitSeconds: 120 });
		require(["vs/editor/editor.main"], function() {
			context.uploadValidator = monaco.editor.create(document.getElementById("uploadValidator"), {
				value: context.fileConfigObj.uploadQueryContent,
				language: "sql",
				roundedSelection: false,
				scrollBeyondLastLine: false,
				readOnly: false,
				theme: "vs-dark",
				wordWrap: "wordWrapColumn",
				wordWrapColumn: 100,
				wordWrapMinified: true,
				wrappingIndent: "indent"
			});
			context.uploadValidator.onDidChangeCursorSelection((e) => {
				if ($("#queryType_uploadValidator").val() == "4") {
					if (e.source == "snippet") {
						var position = e.oldSelections[0]; // Get current mouse position
						var text = context.uploadValidator.getValue(position);
						var splitedText = text.split("\n");
						var lineContent = splitedText[position.endLineNumber - 1]; // Get selected line content
						var textArray = lineContent.split('');
						var line = context.uploadValidator.getPosition().lineNumber;
						var col = context.uploadValidator.getPosition().column;
						if (textArray.includes("$")) {
							var textToInsert = ""; // text to be inserted
							splitedText[position.endLineNumber - 1] = [lineContent.slice(0, position.endColumn - 2), textToInsert, lineContent.slice(position.endColumn - 1)].join(''); // Append the text exactly at the selected position (position.column -1)
							context.uploadValidator.setValue(splitedText.join("\n")); // Save the value back to the Editor
							context.uploadValidator.setPosition({ lineNumber: line, column: col });
						}
						else {
							splitedText[position.endLineNumber - 1];
							context.uploadValidator.setValue(splitedText.join("\n")); // Save the value back to the Editor
							context.uploadValidator.setPosition({ lineNumber: line, column: col });
							context.uploadValidator.focus();
						}
					}
				}
			});
			context.uploadValidator.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, function() {
				typeOfAction('file-upload-config', $("#savedAction").find("button"), fileBinMaster.saveData.bind(fileBinMaster), fileBinMaster.backToPreviousPage);
			});
			context.uploadValidator.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Shift | monaco.KeyCode.KEY_M, function() {
				resizeMonacoEditor(context.uploadValidator, "uploadValidator_container", "uploadValidator");
			});
			context.uploadValidator.onDidChangeModelContent(function() {
				$("#errorMessage").hide();
			});
			if ($("#uploadValidator_query").text().trim() !== "") {
				$("#uploadValidator_chkbox").attr("checked", "checked");
				$("#uploadValidator_div").css("opacity", "1.0");
			}
		});

		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }, waitSeconds: 120 });
		require(["vs/editor/editor.main"], function() {
			context.viewValidator = monaco.editor.create(document.getElementById("viewValidator"), {
				value: context.fileConfigObj.viewQueryContent,
				language: "sql",
				roundedSelection: false,
				scrollBeyondLastLine: false,
				readOnly: false,
				theme: "vs-dark",
				wordWrap: "wordWrapColumn",
				wordWrapColumn: 100,
				wordWrapMinified: true,
				wrappingIndent: "indent"
			});
			context.viewValidator.onDidChangeCursorSelection((e) => {
				if ($("#queryType_viewValidator").val() == "4") {
					if (e.source == "snippet") {
						var position = e.oldSelections[0]; // Get current mouse position
						var text = context.viewValidator.getValue(position);
						var splitedText = text.split("\n");
						var lineContent = splitedText[position.endLineNumber - 1]; // Get selected line content
						var textArray = lineContent.split('');
						var line = context.viewValidator.getPosition().lineNumber;
						var col = context.viewValidator.getPosition().column;
						if (textArray.includes("$")) {
							var textToInsert = ""; // text to be inserted
							splitedText[position.endLineNumber - 1] = [lineContent.slice(0, position.endColumn - 2), textToInsert, lineContent.slice(position.endColumn - 1)].join(''); // Append the text exactly at the selected position (position.column -1)
							context.viewValidator.setValue(splitedText.join("\n")); // Save the value back to the Editor
							context.viewValidator.setPosition({ lineNumber: line, column: col });
						}
						else {
							splitedText[position.endLineNumber - 1];
							context.viewValidator.setValue(splitedText.join("\n")); // Save the value back to the Editor
							context.viewValidator.setPosition({ lineNumber: line, column: col });
							context.viewValidator.focus();
						}
					}
				}
			});
			context.viewValidator.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, function() {
				typeOfAction('file-upload-config', $("#savedAction").find("button"), fileBinMaster.saveData.bind(fileBinMaster), fileBinMaster.backToPreviousPage);
			});
			context.viewValidator.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Shift | monaco.KeyCode.KEY_M, function() {
				resizeMonacoEditor(context.viewValidator, "viewValidator_container", "viewValidator");
			});
			context.viewValidator.onDidChangeModelContent(function() {
				$("#errorMessage").hide();
			});
			if ($("#viewValidator_query").text().trim() !== "") {
				$("#viewValidator_chkbox").attr("checked", "checked");
				$("#viewValidator_div").css("opacity", "1.0");
			}
		});

		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }, waitSeconds: 120 });
		require(["vs/editor/editor.main"], function() {
			context.deleteValidator = monaco.editor.create(document.getElementById("deleteValidator"), {
				value: context.fileConfigObj.deleteQueryContent,
				language: "sql",
				roundedSelection: false,
				scrollBeyondLastLine: false,
				readOnly: false,
				theme: "vs-dark",
				wordWrap: "wordWrapColumn",
				wordWrapColumn: 100,
				wordWrapMinified: true,
				wrappingIndent: "indent"
			});
			context.deleteValidator.onDidChangeCursorSelection((e) => {
				if ($("#queryType_deleteValidator").val() == "4") {
					if (e.source == "snippet") {
						var position = e.oldSelections[0]; // Get current mouse position
						var text = context.deleteValidator.getValue(position);
						var splitedText = text.split("\n");
						var lineContent = splitedText[position.endLineNumber - 1]; // Get selected line content
						var textArray = lineContent.split('');
						var line = context.deleteValidator.getPosition().lineNumber;
						var col = context.deleteValidator.getPosition().column;
						if (textArray.includes("$")) {
							var textToInsert = ""; // text to be inserted
							splitedText[position.endLineNumber - 1] = [lineContent.slice(0, position.endColumn - 2), textToInsert, lineContent.slice(position.endColumn - 1)].join(''); // Append the text exactly at the selected position (position.column -1)
							context.deleteValidator.setValue(splitedText.join("\n")); // Save the value back to the Editor
							context.deleteValidator.setPosition({ lineNumber: line, column: col });
						}
						else {
							splitedText[position.endLineNumber - 1];
							context.deleteValidator.setValue(splitedText.join("\n")); // Save the value back to the Editor
							context.deleteValidator.setPosition({ lineNumber: line, column: col });
							context.deleteValidator.focus();
						}
					}
				}
			});
			context.deleteValidator.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, function() {
				typeOfAction('file-upload-config', $("#savedAction").find("button"), fileBinMaster.saveData.bind(fileBinMaster), fileBinMaster.backToPreviousPage);
			});
			context.deleteValidator.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Shift | monaco.KeyCode.KEY_M, function() {
				resizeMonacoEditor(context.editor, "deleteValidator_container", "deleteValidator");
			});
			context.deleteValidator.onDidChangeModelContent(function(event) {
				$("#errorMessage").hide();
			});
			if ($("#deleteValidator_query").text().trim() !== "") {
				$("#deleteValidator_chkbox").attr("checked", "checked");
				$("#deleteValidator_div").css("opacity", "1.0");
			}
			if (edit == 0) {
				context.disableAllValidator();
			}
			context.getDefaultQueries();

		});
	}

	setQueryContent = function() {
		const context = this;
		if ($("#fileBinId").val().trim() !== "") {
			$.ajax({
				type: "POST",
				url: contextPath + "/cf/gqc",
				data: {
					fileBinId: $("#fileBinId").val()
				},
				success: function(data) {
					context.fileConfigObj = JSON.parse(data);
				},
				error: function(xhr, error) {
					showMessage("Error occurred while fetching query content", "error");
				},
			});
		} else {
			context.fileConfigObj = new Object();
			context.fileConfigObj.selectQueryContent = "";
			context.fileConfigObj.viewQueryContent = "";
			context.fileConfigObj.uploadQueryContent = "";
			context.fileConfigObj.deleteQueryContent = "";
		}
	}

	initializeFileSlider = function() {
		let context = this;
		$("#fileSliderDiv").slider({
			orientation: "horizontal",
			min: 1,
			max: 50,
			value: $("#noOfFiles").val() == "" ? 15 : $("#noOfFiles").val(),
			slide: function(event, ui) {
				$("#fileSliderSpan").text(ui.value);
				$("#noOfFiles").val(ui.value);
			}
		});
		let fileCount = $("#fileSliderDiv").slider("value");
		$("#fileSliderSpan").text(fileCount);
		$("#noOfFiles").val(fileCount);
	}

	disableAllValidator = function() {
		let context = this;
		$("*[id$=_chkbox]").each(function(index, element) {
			$(element).attr("checked", false);
			let elementId = $(element).attr("id").split("_")[0];
			context.enableDisableValidator(elementId);
		});
	}

	getDefaultQueries = function() {
		$.ajax({
			type: "POST",
			url: contextPath + "/api/file-bin-default-queries",
			async: false,
			success: function(data) {
				let defaultQueries = JSON.parse(data[0].defaultQuery.replace(/(\r\n|\n|\r)/gm, ""));
				$.each(defaultQueries, function(index, queryObj) {
					$.each(queryObj, function(key, value) {
						if ($("#" + key).text().trim() === "") {
							$("#" + key).text(value);
						}
					});
				})
			},
			error: function(xhr, error) {
				showMessage("Error occurred while fetching default queries", "error");
			},
		});
	}

	enableDisableValidator = function(editorVariableName) {
		let context = this;
		let editor = context[editorVariableName];
		if ($("#" + editorVariableName + "_chkbox").prop("checked")) {
			$("#" + editorVariableName + "_div").css("opacity", "1.0");
			$("#" + editorVariableName + "_container").show();
			editor.setValue($("#" + editorVariableName + "_query").text());
			editor.updateOptions({ readOnly: false });
			$("#queryType_" + editorVariableName).prop("disabled", false);
			$("#datasource_" + editorVariableName).prop("disabled", false);
		} else {
			$("#" + editorVariableName + "_div").css("opacity", "0.4");
			$("#" + editorVariableName + "_query").text(editor.getValue().toString());
			editor.setValue("");
			editor.updateOptions({ readOnly: true });
			$("#queryType_" + editorVariableName).prop("disabled", true);
			$("#datasource_" + editorVariableName).prop("disabled", true);
		}
	}

	validateMaxFileSize = function(event) {
		let maxAllowedSi = 99999999999999999999;
		let currentSize = $("#maxFileSizeUi").val();
		if (currentSize > maxAllowedSi) {
			event.preventDefault();
		}
		return true;
	}

	saveCurrentFileSize = function() {
		$("#previousFileSize").val($("#maxFileSizeUi").val());
		$("#initialFileSizeScale").val($("#size").val());
	}

	updateFileSize = function() {
		let context = this;
		let intialScale = Number.parseInt($("#initialFileSizeScale").val());
		let previousScale = Number.parseInt($("#previousFileSizeScale").val());
		let currentScale = Number.parseInt($("#size").val());
		let maxSize;
		maxSize = $("#previousFileSize").val() / Math.pow(1024, currentScale - intialScale);
		//	maxSize = $("#previousFileSize").val() / Math.pow(1000, currentScale - intialScale); 
		//	Formula : divide the digital storage value by 1e+6
		let maxAllowedSi = 99999999999999999999;
		if (maxSize <= maxAllowedSi) {
			$("#previousFileSizeScale").val(currentScale);
			$("#maxFileSizeUi").val(maxSize.toFixed(3));
			$("#maxFileSize").val(maxSize.toFixed(3));
		}
	}

	saveData = function() {
		let context = this;
		let isDataSaved = false;

		context.updateFileSize();
		context.prepareValidatorContent();
		let isValidForm = context.validateForm();

		let scale = $("#initialFileSizeScale").val();
		let fileSize = $("#previousFileSize").val();

		if (scale > 1) {
			fileSize = fileSize * Math.pow(1024, scale - 1);
		}
		$("#maxFileSize").val(fileSize);
		$("#selectValidator_query").val(JSON.stringify($("#selectValidator_query").val()).replace(/(^")|("$)/g, ""));
		$("#uploadValidator_query").val(JSON.stringify($("#uploadValidator_query").val()).replace(/(^")|("$)/g, ""));
		$("#viewValidator_query").val(JSON.stringify($("#viewValidator_query").val()).replace(/(^")|("$)/g, ""));
		$("#deleteValidator_query").val(JSON.stringify($("#deleteValidator_query").val()).replace(/(^")|("$)/g, ""));

		if (isValidForm === true) {
			let formData = $("#addEditForm").serialize() + "&formId=" + formId;
			if (edit === 1) {
				formData = formData + "&edit=" + edit;
			}
			$.ajax({
				type: "POST",
				url: contextPath + "/cf/sdf",
				async: false,
				data: formData,
				success: function(data) {
					edit = 1;
					isDataSaved = true;
					context.saveEntityRoleAssociation();
					showMessage("Information saved successfully", "success");
				},
				error: function(xhr, error) {
					showMessage("Error occurred while saving", "error");
				},
			});
		}
		context.getDefaultQueries();
		return isDataSaved;
	}

	validateForm = function() {
		let fileBinId = $("#fileBinId").val().trim();
		let maxFileSize = $("#maxFileSize").val().trim();
		if (fileBinId !== "" && maxFileSize !== "" && maxFileSize >= 1) {
			$("#errorMessage").hide();
			return true;
		}
		if (fileBinId === "" || maxFileSize === "") {
			$("#errorMessage").html("File Bin Id and Max File Size cannot be blank");
			$("#errorMessage").show();
			return false;
		}
		return true;
	}

	prepareValidatorContent = function() {
		let context = this;
		$("*[id$=_chkbox]").each(function(index, element) {
			let isChecked = $(element).prop("checked");
			let elementId = $(element).attr("id").split("_")[0];
			if (isChecked == true) {
				$("#" + elementId + "_query").text(context[elementId].getValue().toString().trim());
			} else {
				$("#" + elementId + "_query").text("");
			}
		});
	}

	saveEntityRoleAssociation = function() {
		let context = this;
		let roleIds = [];
		let entityRoles = new Object();
		entityRoles.entityName = $("#fileBinId").val().trim();
		entityRoles.moduleId = $("#moduleId").val();
		entityRoles.entityId = $("#fileBinId").val().trim();
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

	getEntityRoles = function() {
		let context = this;
		$.ajax({
			async: false,
			type: "GET",
			url: contextPath + "/cf/ler",
			data: {
				entityId: $("#fileBinId").val().trim(),
				moduleId: $("#moduleId").val(),
			},
			success: function(data) {
				$.each(data, function(key, val) {
					multiselect.setSelectedObject(val);

				});
			}
		});
	}

	updateFileBinTemplate = function() {
		let fileBinId = $("#fileBinId").val().trim();
		if (fileBinId !== "") {
			let jsFileBinId = $("pre span").filter(function() { return ($(this).text() === '\"yourFileBinId\"') });

			$(jsFileBinId).text('"' + fileBinId + '"');
		}
	}

	backToPreviousPage = function() {
		location.href = contextPath + "/cf/fucl";
	}

	refreshEditorContent = function(selectedElem) {
		let context = this;
		let editorIndex = $(selectedElem).attr("id").split("_")[1];
		let editor = context[editorIndex];
		editor.setValue("");
		let selectedOptionQueryType = $("#queryType_" + editorIndex).find(":selected").val();
		if (selectedOptionQueryType === "4") {
			$("#dt-" + editorIndex).hide();
		} else {
			$("#dt-" + editorIndex).show();
		}
	}

	enableDiableDataSource = function(element) {
		let selectedOptionVal = $(element).val();
		if (selectedOptionVal === "1") {
			$("#dt-" + editorIndex).show();
		} else {
			$("#dt-" + editorIndex).hide();
		}
	}
}


