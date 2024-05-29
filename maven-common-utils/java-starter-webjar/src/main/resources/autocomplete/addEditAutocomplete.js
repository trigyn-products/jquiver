class AddEditAutocomplete {
	constructor() {
	}

	loadAutocompletDetails = function() {
		const context = this;

		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }, waitSeconds: 120 });
		require(["vs/editor/editor.main"], function() {
			context.sqlQuery = monaco.editor.create(document.getElementById("sqlEditor"), {
				value: $("#sqlContentDiv").val().trim(),
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
			monaco.languages.registerCompletionItemProvider('sql', {
				triggerCharacters: ["@"],
				provideCompletionItems: (model, position) => {
					var textUntilPosition = model.getValueInRange({
						startLineNumber: position.lineNumber,
						startColumn: position.column - 1,
						endLineNumber: position.lineNumber,
						endColumn: position.column
					});
					if (textUntilPosition == '@') {
						return {
							suggestions: JSON.parse(JSON.stringify(newSuggestionsArray))
						}
					}
				}
			});
			context.sqlQuery.onDidChangeCursorSelection((e) => {
				if (e.source == "snippet") {
					var position = e.oldSelections[0]; // Get current mouse position
					var text = context.sqlQuery.getValue(position);
					var splitedText = text.split("\n");
					var lineContent = splitedText[position.endLineNumber - 1]; // Get selected line content
					var line = context.sqlQuery.getPosition().lineNumber;
					var col = context.sqlQuery.getPosition().column;
					var newTextArray = lineContent.split('');
					var sugPostion;
					if (lineContent.includes("@{")) {
						var textToInsert = "$"; // text to be inserted
						while (newTextArray.lastIndexOf("@") > position.endColumn) {
							newTextArray = newTextArray.slice(0, newTextArray.lastIndexOf("@"));
						}
						sugPostion = newTextArray.lastIndexOf("@");
						splitedText[position.endLineNumber - 1] = [lineContent.slice(0, sugPostion), textToInsert, lineContent.slice(sugPostion + 1)].join(''); // Append the text exactly at the selected position (position.column -1)
					} else if (lineContent.includes("@<")) {
						var textToInsert = ""; // text to be inserted
						while (newTextArray.lastIndexOf("@") > position.endColumn) {
							newTextArray = newTextArray.slice(0, newTextArray.lastIndexOf("@"));
						}
						sugPostion = (newTextArray.lastIndexOf("@")) - 2;
						splitedText[position.endLineNumber - 1] = [lineContent.slice(0, sugPostion), textToInsert, lineContent.slice(sugPostion + 1)].join(''); // Append the text exactly at the selected position (position.column -1)
					} else {
						splitedText[position.endLineNumber - 1];
					}

					context.sqlQuery.setValue(splitedText.join("\n")); // Save the value back to the Editor
					context.sqlQuery.setPosition({ lineNumber: line, column: col });
					context.sqlQuery.focus();
				}

			});
			context.sqlQuery.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, function() {
				typeOfActionWithIsEdit('autocomplete-manage-details', $("#savedAction").find("button"), isEdit,
					addEditAutocomplete.saveAutocompleteDetail.bind(addEditAutocomplete), addEditAutocomplete.backToListingPage);
			});
			context.sqlQuery.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Shift | monaco.KeyCode.KEY_M, function() {
				resizeMonacoEditor(context.sqlQuery, "sqlContainer", "sqlEditor");
			});
			context.sqlQuery.onDidChangeModelContent(function() {
				$('#errorMessage').hide();
			});
			//	        	$("#contentDiv").remove();
		});
	}

	saveAutocompleteDetail = function() {
		const context = this;
		let isDataSaved = false;
		const validData = this.validateData();
		if (validData == false) {
			return false;
		}
		const sqlEditor = context.sqlQuery.getValue();
		$("#acSelectQuery").val(context.sqlQuery.getValue().toString());

		const form = $("#autocompleteForm");
		let serializedForm = form.serializeArray();
		for (let iCounter = 0, length = serializedForm.length; iCounter < length; iCounter++) {
			serializedForm[iCounter].value = $.trim(serializedForm[iCounter].value);
		}
		serializedForm = $.param(serializedForm);

		$.ajax({
			type: "POST",
			async: false,
			url: contextPath + "/cf/sacd",
			data: serializedForm,
			success: function(data) {
				isDataSaved = true;
				context.saveEntityRoleAssociation(data);
				showMessage("Autocomplete saved successfully", "success");
			},
			error: function(xhr, error) {
				showMessage("Error occurred while saving", "error");
			},
		});
		return isDataSaved;
	}

	validateData = function() {
		$('#errorMessage').hide();
		let context = this;
		let autocompleteId = $.trim($("#autoId").val());
		let autocompleteQuery = $.trim(context.sqlQuery.getValue().toString());

		if (autocompleteId === "") {
			showMessage("Please enter valid autocomplete id", "warn");
			$("#autoId").focus();
			$("#autoId").closest("div").parent().effect("highlight", {}, 3000);
			return false;
		}
		if (autocompleteQuery === "") {
			showMessage("Please enter valid autocomplete query", "warn");
			$(context.sqlQuery).focus();
			$(context.sqlQuery).closest("div").parent().effect("highlight", {}, 3000);
			return false;
		}
		return true;
	}

	backToListingPage = function() {
		location.href = contextPath + "/cf/adl";
	}
	saveEntityRoleAssociation = function(autoCompleteId) {
		let roleIds = [];
		let entityRoles = new Object();
		entityRoles.entityName = autoCompleteId;
		entityRoles.moduleId = $("#moduleId").val();
		entityRoles.entityId = autoCompleteId;
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
		$.ajax({
			async: false,
			type: "GET",
			url: contextPath + "/cf/ler",
			data: {
				entityId: $("#autoId").val(),
				moduleId: $("#moduleId").val(),
			},
			success: function(data) {
				$.each(data, function(key, val) {
					multiselect.setSelectedObject(val);

				});
			}
		});
	}

	createQuery = function() {
		const context = this;
		let tableName = $("#autocompleteTable").val();
		let additionalDataSourceId = $("#dataSource").find(":selected").val();
		let productName = $("#dataSource").find(":selected").data("product-name");

		if (tableName !== "") {
			$.ajax({
				type: "POST",
				url: contextPath + "/cf/cnbtn",
				data: {
					tableName: tableName,
					additionalDataSourceId: additionalDataSourceId
				},
				success: function(data) {
					let limitQuery;
					if (productName === "sqlserver") {
						limitQuery = " ORDER BY (SELECT NULL) OFFSET :startIndex ROWS FETCH NEXT :pageSize ROWS ONLY ";
					} else if (productName === "oracle:thin") {
						limitQuery = " OFFSET :startIndex ROWS FETCH NEXT :pageSize ROWS ONLY ";
					} else if (productName === "postgresql") {
						limitQuery = " OFFSET :startIndex ROWS FETCH NEXT :pageSize ROWS ONLY ";
					} else {
						limitQuery = " LIMIT :startIndex, :pageSize";
					}
					context.sqlQuery.setValue("SELECT " + data[0].columnName + " FROM " + tableName + limitQuery);
				}
			});
		} else {
			context.sqlQuery.setValue("");
		}
	}

	updateAutocompleTemplate = function() {
		let autocompleteId = $("#autoId").val().trim();
		if (autocompleteId !== "") {
			let htmlAutocompleteId = $("pre span").filter(function() { return ($(this).text() === '\"autocompleteId\"') });
			let jsAutocompleteId = $("pre span").filter(function() { return ($(this).text() === '\"#autocompleteId\"') });

			$(htmlAutocompleteId).text('"' + autocompleteId + '"');
			$(jsAutocompleteId).text('"#' + autocompleteId + '"');
		}
	}

	hideErrorMessage = function() {
		$('#errorMessage').hide();
	}

}