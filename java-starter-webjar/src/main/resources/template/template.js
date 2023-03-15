
class TemplateEngine {
	constructor(templateId) {
		this.templateId = templateId;
	}
	backToTemplateListingPage = function() {
		location.href = contextPath + "/cf/te";
	}
	initPage = function() {
		const context = this;
		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }, waitSeconds: 120 });
		require(["vs/editor/editor.main"], function() {
			context.editor = monaco.editor.create(document.getElementById("htmlEditor"), {
				value: "",
				language: "html",
				theme: "vs-dark",
				roundedSelection: false,
				scrollBeyondLastLine: false,
				readOnly: false,
				wordWrap: 'wordWrapColumn',
				wordWrapColumn: 120,
				wrappingIndent: "indent",
				wordWrapMinified: true,
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
			context.editor.onDidChangeCursorSelection((e) => {
				if (e.source == "snippet") {
					var position = e.oldSelections[0]; // Get current mouse position
					var text = context.editor.getValue(position);
					var splitedText = text.split("\n");
					var lineContent = splitedText[position.endLineNumber - 1]; // Get selected line content
					var line = context.editor.getPosition().lineNumber;
					var col = context.editor.getPosition().column;
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
					context.editor.setValue(splitedText.join("\n")); // Save the value back to the Editor
					context.editor.setPosition({ lineNumber: line, column: col });
					context.editor.focus();
				}
			});
			context.setTemplateValue();
			context.editor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, function() {
				typeOfAction('template-manage-details', $("#savedAction").find("button"),
					templateMaster.validateSaveVelocity.bind(templateMaster), templateMaster.backToTemplateListingPage);
			});
			context.editor.onDidChangeModelContent(function() {
				$('#errorMessage').hide();
			});
			context.editor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Shift | monaco.KeyCode.KEY_M, function() {
				resizeMonacoEditor(context.editor, "htmlContainer", "htmlEditor");
			});
		});
	}

	setTemplateValue = function() {
		const context = this;
		if (context.templateId != "0") {
			$.ajax({
				type: "get",
				url: contextPath + "/cf/gtbi",
				data: {
					templateId: this.templateId
				},
				success: function(data) {
					context.editor.setValue(data);
					context.getEntityRoles();
				},
				error: function(xhr, error) {
					showMessage("Error occurred while fetching template content", "error");
				},
			});
		}
	}

	validateSaveVelocity = function() {
		const context = this;
		let isDataSaved = false;
		const validTemplate = this.validateTemplate();
		if (validTemplate) {
			const templateName = $("#vmName").val();
			$.ajax({
				async: false,
				type: "POST",
				cache: false,
				url: contextPath + "/cf/ctd",
				data: {
					templateName: templateName,
				},
				success: function(data) {
					if (data != "") {
						if (data == context.templateId) {
							isDataSaved = context.onSaveAndClose();
						} else {
							return false;
						}
					} else {
						isDataSaved = context.onSaveAndClose();
					}
				},
				error: function(xhr, error) {
					showMessage("Error occurred while saving", "error");
				},
			});
		} else {
			$('#errorMessage').html("Template name and content cannot be blank");
			$('#errorMessage').show();
		}
		return isDataSaved;
	}

	validateTemplate = function() {
		const context = this;
		let templateName = $.trim($("#vmName").val());
		let velocityTempData = context.editor.getValue().trim();
		return templateName !== "" && velocityTempData !== "";
	}

	onSaveAndClose = function() {
		const context = this;
		let isDataSaved = false;
		const velocityName = $("#vmName").val().trim();
		let velocityTempData = context.editor.getValue().trim();
		$.ajax({
			async: false,
			type: "POST",
			cache: false,
			url: contextPath + "/cf/std",
			data: {
				velocityId: context.templateId,
				velocityName: velocityName,
				velocityTempData: velocityTempData
			},
			success: function(data) {
				context.templateId = data;
				context.saveEntityRoleAssociation(context.templateId);
				isDataSaved = true;
				showMessage("Information saved successfully", "success");
			},
			error: function(xhr, error) {
				showMessage("Error occurred while saving", "error");
			},
		});
		return isDataSaved;
	}

	saveEntityRoleAssociation = function(templateId) {
		let roleIds = [];
		let entityRoles = new Object();
		entityRoles.entityName = $("#vmName").val().trim();
		entityRoles.moduleId = $("#moduleId").val();
		entityRoles.entityId = templateId;
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
				entityId: this.templateId,
				moduleId: $("#moduleId").val(),
			},
			success: function(data) {
				$.each(data, function(key, val) {
					multiselect.setSelectedObject(val);
				});
			}
		});
	}
}