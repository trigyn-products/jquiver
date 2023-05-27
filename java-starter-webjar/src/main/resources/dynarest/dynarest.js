class DynamicRest {

	constructor(formId) {
		this.formId = formId;
		this.saveUpdateEditors = new Array();
		this.daoDetailsIds = new Array();
		this.serviceLogicContent;
	}

	populateDetails = function(requestTypeId, prodTypeId, dynaresturl) {
		let produceTypeText = "text/html";
		let context = this;
		$.ajax({
			type: "GET",
			url: contextPath + "/api/dynarestDetails",
			async: false,
			data: {
				url: dynaresturl
			},
			success: function(data) {
				let dynarestDetailsArray = data.dynarestDetails;
				if (dynarestDetailsArray !== null && dynarestDetailsArray.length != 0) {
					context.populateServiceLogic(dynarestDetailsArray[0].dynarestServiceLogic);
					for (let counter = 0; counter < dynarestDetailsArray.length; counter++) {
						context.addSaveQueryEditor(null, dynarestDetailsArray[counter].daoDetailsId, dynarestDetailsArray[counter].versionDetails
							, dynarestDetailsArray[counter].variableName, dynarestDetailsArray[counter].dynarestDaoQuery,
							dynarestDetailsArray[counter].dynarestQueryType, dynarestDetailsArray[counter].datasourceId);
					}
					getEntityRoles()
				} else {
					context.populateServiceLogic();
					context.addSaveQueryEditor();
				}

				for (let counter = 0; counter < data["methodTypes"].length; ++counter) {
					let object = data["methodTypes"][counter];
					$("#dynarestRequestTypeId").append("<option value='" + object["value"] + "'>" + object["name"] + "</option>");

				}

				for (let counter = 0; counter < data["producerDetails"].length; ++counter) {
					let object = data["producerDetails"][counter];
					$("#dynarestProdTypeId").append("<option value='" + object["value"] + "'>" + object["name"] + "</option>");
					if (prodTypeId == object["value"]) {
						produceTypeText = object["name"];
						if (produceTypeText == "email/xml") {
							produceTypeText = "application/json";
						}
					}
				}

				$('#dynarestProdTypeId').val(prodTypeId);
				$('#dynarestRequestTypeId').val(requestTypeId);
				if (prodTypeId == "14") {
					$("#lblXmlFormat").html("Email XML Structure");
					$("#xmlFormatDiv").show();
					context.getEmailXMLStructure('jq-email-xml-template');
				}
				if (prodTypeId == "8" && $("#dynarestPlatformId").val() == 3) {
					$("#lblXmlFormat").html("FileInfo Download Code");
					$("#xmlFormatDiv").show();
					context.getEmailXMLStructure('jq-download-file-template');
				}
				//				context.hideShowAllowFiles();
			}
		});
		return produceTypeText;
	}

	populateServiceLogic = function(serviceLogicContent) {
		const context = this;
		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }, waitSeconds: 120 });
		require(["vs/editor/editor.main"], function() {
			context.serviceLogicContent = monaco.editor.create(document.getElementById("htmlEditor"), {
				value: serviceLogicContent,
				language: "java",
				roundedSelection: false,
				scrollBeyondLastLine: false,
				readOnly: false,
				theme: "vs-dark",
				wordWrap: 'wordWrapColumn',
				wordWrapColumn: 100,
				wordWrapMinified: true,
				wrappingIndent: "indent",
				"autoIndent": true
			});
			/**Added for displaying Custom Suggestions in Monaco Editor */
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
			monaco.languages.registerCompletionItemProvider('java', {
				triggerCharacters: ["#"],
				provideCompletionItems: (model, position) => {
					var textUntilPosition = model.getValueInRange({
						startLineNumber: position.lineNumber,
						startColumn: position.column - 1,
						endLineNumber: position.lineNumber,
						endColumn: position.column
					});
					if (textUntilPosition == '#') {
						if (model.id == "$model1") {
							if ($("#dynarestPlatformId").val() == "2") {
								return { suggestions: JSON.parse(JSON.stringify(newSuggestionsArray)) };
							} else if ($("#dynarestPlatformId").val() == "3") {
								return { suggestions: JSON.parse(JSON.stringify(newJSSuggestionsArray)) };
							}
						}
					}
				}
			});
			context.serviceLogicContent.onDidChangeCursorSelection((e) => {
				if (e.source == "snippet") {
					var position = e.oldSelections[0]; // Get current mouse position
					var text = context.serviceLogicContent.getValue(position);
					var splitedText = text.split("\n");
					var lineContent = splitedText[position.endLineNumber - 1]; // Get selected line content
					var textArray = lineContent.split('');
					var newTextArray = lineContent.split('');
					var line = context.serviceLogicContent.getPosition().lineNumber;
					var col = context.serviceLogicContent.getPosition().column;
					var sugPostion;
					if ($("#dynarestPlatformId").val() == "2") {
						if (lineContent.includes("#{")) {
							var textToInsert = "$"; // text to be inserted
							while (newTextArray.lastIndexOf("#") > position.endColumn) {
								newTextArray = newTextArray.slice(0, newTextArray.lastIndexOf("#"));
							}
							sugPostion = newTextArray.lastIndexOf("#");
							splitedText[position.endLineNumber - 1] = [lineContent.slice(0, sugPostion), textToInsert, lineContent.slice(sugPostion + 1)].join(''); // Append the text exactly at the selected position (position.column -1)

						}
						else if (lineContent.includes("#<")) {
							var textToInsert = ""; // text to be inserted
							while (newTextArray.lastIndexOf("#") > position.endColumn) {
								newTextArray = newTextArray.slice(0, newTextArray.lastIndexOf("#"));
							}
							sugPostion = newTextArray.lastIndexOf("#");
							splitedText[position.endLineNumber - 1] = [lineContent.slice(0, sugPostion), textToInsert, lineContent.slice(sugPostion + 1)].join(''); // Append the text exactly at the selected position (position.column -1)
						}
						else {
							splitedText[position.endLineNumber - 1];
						}
						context.serviceLogicContent.setValue(splitedText.join("\n"));
						context.serviceLogicContent.setPosition({ lineNumber: line, column: col });
						context.serviceLogicContent.focus();

					} else if ($("#dynarestPlatformId").val() == "3") {
						if (textArray.includes("#")) {
							var textToInsert = ""; // text to be inserted
							while (newTextArray.lastIndexOf("#") > position.endColumn) {
								newTextArray = newTextArray.slice(0, newTextArray.lastIndexOf("#"));
							}
							sugPostion = newTextArray.lastIndexOf("#");
							splitedText[position.endLineNumber - 1] = [lineContent.slice(0, sugPostion), textToInsert, lineContent.slice(sugPostion + 1)].join(''); // Append the text exactly at the selected position (position.column -1)

						} else {
							splitedText[position.endLineNumber - 1];
						}
						context.serviceLogicContent.setValue(splitedText.join("\n")); // Save the value back to the Editor
						context.serviceLogicContent.setPosition({ lineNumber: line, column: col });
						context.serviceLogicContent.focus();
					}
				}
			});
			/**Ends Here */
			context.serviceLogicContent.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, function() {
				typeOfAction('dynamic-rest-form', $("#savedAction").find("button"), dynarest.saveDynarest.bind(dynarest), dynarest.backToDynarestListingPage);
			});
			context.serviceLogicContent.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Shift | monaco.KeyCode.KEY_M, function() {
				resizeMonacoEditor(context.serviceLogicContent, "htmlContainer", "htmlEditor");
			});
		});

	}

	addSaveQueryEditor = function(element, daoDetailsId, versionDetails, variableName, saveQueryContent, queryType, datasourceId) {
		const context = this;
		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }, waitSeconds: 120 });
		require(["vs/editor/editor.main"], function() {
			let index = context.saveUpdateEditors.length;
			let parentElement;
			if (element != null) {
				parentElement = $(element).parent().parent().parent().parent();
			}
			if (versionDetails != undefined && versionDetails != "") {
				$("#saveScriptContainer").append("<div class='col-3'><div id='compareDiv_" + index + "' class='col-inner-form full-form-fields'><label for='versionId'>Compare with </label>");
				$("#compareDiv_" + index).append("<select class='form-control' id='versionSelect_" + index + "' onchange='addEdit.getSelectTemplateData();' name='versionId' title='Template Versions'>");
				$("#versionSelect_" + index).append("<option value='' selected>Select</option>");
			}
			
			let inputElement = "<div class='col-3'><label for='inputcontainer_" + index + "' style='white-space:nowrap'> Variable Name </label><input id='inputcontainer_" + index + "' type ='text' value='result_" + index + "' class='form-control' /></div>";
			let selectElement = "<div class='col-3'><label for='selectcontainer_" + index + "' style='white-space:nowrap'>Query Type </label><select id='selectcontainer_" + index + "' class='form-control' onchange='dynarest.getRESTXMLStructure(this);'><option value='1'>Select Query</option><option value='2'>Insert-Update-Delete Query</option><option value='3'>Stored Procedure</option><option value='4'>REST Client</option></select></div>";
			let datasourceEditor = "<div class='col-3'><div><label for='datasourcecontainer_" + index + "' style='white-space:nowrap'>Datasource </label><select id='datasourcecontainer_" + index + "' name='dataSourceId' class='form-control' onchange='showHideTableAutocomplete()'><option id='defaultConnection' value=''>Default Connection</option>";

			if(index > 0){
				$("#datasourcecontainer_0 > option").each(function(a_innerIndex, element){
		            if(a_innerIndex == 0){
		                return;
		            }
		            datasourceEditor += $(element)[0].outerHTML;
		        });
			}else{
				let datasource = retrieveDatasource();
				if (datasource != null || datasource != undefined || datasource != "") {
					datasource.forEach(function(dataSourceObj) {
						datasourceEditor += "<option value=" + dataSourceObj.additionalDatasourceId + " data-product-name=" + dataSourceObj.databaseProductName + ">" + dataSourceObj.datasourceName + "</option>";
					});
				}
			}
			
			datasourceEditor += '</select></div></div>';
			let buttonElement = "<div class='btn-icons float-right'><input type='button' id='addEditor_" + index + "' value='Add' class='margin-r-3 btn btn-primary' onclick='dynarest.addSaveQueryEditor(this);'><input type='button' id='removeTemplate_" + index + "' value='Remove' style='margin-left:4px;' class='btn btn-secondary' onclick='dynarest.removeSaveQueryEditor(this);'></div>";

			let daoContainer = $("<div id='daoContainerDiv_" + index + "' class='margin-t-25'><div class='row'>" + inputElement + "" + selectElement + "" + datasourceEditor + "<div class='col-3 margin-t-25 float-right'>" + buttonElement + "</div></div></div>");
			daoContainer.append("<div id='container_" + index + "' class='html_script' style='margin-top: 10px;'><div class='grp_lblinp'><div id='saveSqlContainer_" + index + "' class='ace-editor-container'><div id='saveSqlEditor_" + index + "' class='ace-editor'></div></div></div></div></div>");

			/**Added for displaying Custom Suggestions in Monaco Editor */
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
				triggerCharacters: ["#"],
				provideCompletionItems: (model, position) => {
					var textUntilPosition = model.getValueInRange({
						startLineNumber: position.lineNumber,
						startColumn: position.column - 1,
						endLineNumber: position.lineNumber,
						endColumn: position.column
					});
					let str = model.id;
					let indexCount = str.slice(-1);
					let str1 = str.slice(0, 6);
					let finalindex = indexCount - index;
					if (textUntilPosition == '#') {
						if (model.id == str1 + finalindex) {
							let str = model.id;
							let indexCount = str.slice(-1);
							let index = indexCount - 2;
							if ($("#selectcontainer_" + index).val() == "1" || $("#selectcontainer_" + index).val() == "2") {
								return {
									suggestions: JSON.parse(JSON.stringify(newSuggestionsArray))
								}
							}
						}
					}
				}
			});

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

			let saveUpdateEditor = monaco.editor.create(document.getElementById("saveSqlEditor_" + index), {
				value: saveQueryContent,
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

			saveUpdateEditor.onDidChangeCursorSelection((e) => {
				if (e.source == "snippet") {
					var position = e.oldSelections[0]; // Get current mouse position
					var text = saveUpdateEditor.getValue(position);
					var splitedText = text.split("\n");
					var lineContent = splitedText[position.endLineNumber - 1]; // Get selected line content
					var line = saveUpdateEditor.getPosition().lineNumber;
					var col = saveUpdateEditor.getPosition().column;
					var newTextArray = lineContent.split('');
					var sugPostion;
					if (lineContent.includes("#{")) {
						var textToInsert = "$"; // text to be inserted
						while (newTextArray.lastIndexOf("#") > position.endColumn) {
							newTextArray = newTextArray.slice(0, newTextArray.lastIndexOf("#"));
						}
						sugPostion = newTextArray.lastIndexOf("#");
						splitedText[position.endLineNumber - 1] = [lineContent.slice(0, sugPostion), textToInsert, lineContent.slice(sugPostion + 1)].join(''); // Append the text exactly at the selected position (position.column -1)
					}
					else if (lineContent.includes("#<")) {
						var textToInsert = ""; // text to be inserted
						while (newTextArray.lastIndexOf("#") > position.endColumn) {
							newTextArray = newTextArray.slice(0, newTextArray.lastIndexOf("#"));
						}
						sugPostion = newTextArray.lastIndexOf("#");
						splitedText[position.endLineNumber - 1] = [lineContent.slice(0, sugPostion), textToInsert, lineContent.slice(sugPostion + 1)].join(''); // Append the text exactly at the selected position (position.column -1)
					}
					else {
						splitedText[position.endLineNumber - 1];

					}
					saveUpdateEditor.setValue(splitedText.join("\n")); // Save the value back to the Editor
					saveUpdateEditor.setPosition({ lineNumber: line, column: col });
					saveUpdateEditor.focus();
				}

			});
			/**Ends Here */

			let editorObj = new Object();
			editorObj["index"] = index;
			editorObj["editor"] = saveUpdateEditor;
			context.saveUpdateEditors.push(editorObj);

			if (daoDetailsId != undefined) {
				$("#saveScriptContainer").append("<input type='hidden' id='daoDetailsId_" + daoDetailsId + "' value=" + daoDetailsId + "/>");
				let daoDetailsObject = new Object();
				daoDetailsObject["index"] = index;
				daoDetailsObject["daoDetailsIds"] = daoDetailsId;
				context.daoDetailsIds.push(daoDetailsObject);
			}
			$("#removeTemplate_0").remove();
			saveUpdateEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, function() {
				typeOfAction('dynamic-rest-form', $("#savedAction").find("button"), dynarest.saveDynarest.bind(dynarest), dynarest.backToDynarestListingPage);
			});
			saveUpdateEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Shift | monaco.KeyCode.KEY_M, function() {
				resizeMonacoEditor(saveUpdateEditor, "saveSqlContainer_" + index, "saveSqlEditor_" + index);
			});
			context.updateVariableSeq();
			disableInputSuggestion();
		});

	}

	removeSaveQueryEditor = function(element) {
		let context = this;
		let index = element.id.split("_")[1];
		if (index != 0) {
			jqOpenDeletConfirmation(function() {
				$("#daoContainerDiv_" + index).remove();
				$("#inputcontainer_" + index).remove();
				$("#compareDiv_" + index).remove();
				removeByAttribute(context.saveUpdateEditors, "index", parseInt(index));
				if ($("#daoDetailsId_" + index).length == 1) {
					$("#daoDetailsId_" + index).remove();
					removeByAttribute(context.daoDetailsIds, "index", parseInt(index));
				}
				context.updateVariableSeq();
			});
		}
	}

	saveDynarest = function() {
		let isDataSaved = false;
		let context = this;
		let validData = context.validateDyanrestFields();
		if (validData === false) {
			return false;
		}
		let serviceLogicContent = this.serviceLogicContent.getValue().toString();
		$("#serviceLogic").val(serviceLogicContent);

		let contextHeaderJson = context.headerJson();
		if (contextHeaderJson == -1) {
			return false;
		}
		if (!contextHeaderJson['Powered-By']) {
			contextHeaderJson['Powered-By'] = "JQuiver";
		}
		if (contextHeaderJson['Content-Type'] != $('#dynarestProdTypeId').find(":selected").text()) {
			if ($('#dynarestProdTypeId').find(":selected").text() != "email/xml") {
				showMessage("Produce type and response header content type should be same.", "warn");
				return false;
			} else if ($('#dynarestProdTypeId').find(":selected").text() == "email/xml" && contextHeaderJson['Content-Type'] != "application/json") {
				showMessage("Produce type for email/xml should be application/json", "warn");
				return false;
			}
		}

		let headerJson = JSON.stringify(contextHeaderJson);
		let formData = $("#dynamicRestForm").serialize() + "&headerJson=" + headerJson + "&formId=" + context.formId;
		$.ajax({
			type: "POST",
			url: contextPath + "/cf/sdf",
			async: false,
			data: formData,
			success: function(data) {
				if (data === true) {
					$("#isEdit").val(1);
					isDataSaved = context.saveDAOQueries(context.formId);
				}
			},
			error: function(xhr, data) {
				showMessage("Error occurred while saving", "warn");
			},
		});
		return isDataSaved;
	}

	validateDyanrestFields = function() {
		let context = this;
		let dynarestUrl = $("#dynarestUrl").val();
		if (dynarestUrl === "" || dynarestUrl.indexOf(" ") != -1) {
			$("#dynarestUrl").focus();
			showMessage("Please enter valid URL", "warn");
			return false;
		}

		let dynarestMethodName = $("#dynarestMethodName").val().trim();
		if (dynarestMethodName === "" || dynarestMethodName.indexOf(" ") != -1) {
			$("#dynarestMethodName").focus();
			showMessage("Please enter valid method name", "warn");
			return false;
		}

		let serviceLogicContent = $.trim(context.serviceLogicContent.getValue().toString());
		if (serviceLogicContent === "") {
			showMessage("Service logic can not be blank", "warn");
			return false;
		}

		let headerParam = context.headerJson();
		if (headerParam == -1) {
			return false;
		}

		let queryEditorLength = context.saveUpdateEditors.length;
		let varNamList = new Array();

		for (let iCounter = 0; iCounter < queryEditorLength; iCounter++) {
			let editorObject = context.saveUpdateEditors[iCounter];
			
			if (document.getElementById("addDaoQuery").disabled == false && document.getElementById("addDaoQuery").hidden == false) {
				$.trim($('#inputcontainer_' + iCounter).val('result'));
				(editorObject["editor"].setValue('select 1'));
				break;
			}
			
			let daoQuery = (editorObject["editor"].getValue().toString().trim());
			if (daoQuery === "") {
				showMessage("Query can not be blank", "warn");
				return false;
			}
			let variableName = $.trim($($("[id^=inputcontainer_]")[iCounter]).val());
			if (variableName === "" || variableName.indexOf(" ") != -1) {
				$("[id^=inputcontainer_]")[iCounter].focus();
				showMessage("Variable Name should not be blank", "warn");
				return false;
			}

			if(varNamList.includes(variableName)){
				showMessage("Variable name should be unique", "warn");
				return false;
			}else{
				varNamList.push(variableName);
			}
		}

		return true;
	}


	saveDAOQueries = function(formId) {
		let context = this;
		let isDataSaved = false;
		let saveUpdateQueryArray = new Array();
		let variableNameArray = new Array();
		let queryTypeArray = new Array();
		let datasourceArray = new Array();
		let daoQueryArray = new Array();


		let dashletDetails = new Object();

		let form = $('<form id="saveUpdateQueryForm"></form>');
		form.append('<input name="dynarestUrl" id="dynarestUrlDAO" type="hidden" />');
		form.append('<input name="dynarestMethodName" id="dynarestMethodNameDAO" type="hidden" />');
		form.append('<input name="serviceLogic" id="serviceLogicForm" type="hidden" />');
		form.append('<input name="daoDetailsIds" id="daoDetailsIds" type="hidden" />');
		form.append('<input name="variableName" id="variableName" type="hidden" />');
		form.append('<input name="queryType" id="queryType" type="hidden" />');
		form.append('<input name="daoQueryDetails" id="daoQueryDetails" type="hidden" />');
		form.append('<input name="datasourceDetails" id="datasourceDetails" type="hidden" />');
		form.append('<input name="headerJson" id="headerJson" type="hidden" />');
		form.insertAfter($("#dynamicRestForm"));

		let saveEditorLength = $("[id^=daoContainerDiv_]").length;
		for (let iCounter = 0; iCounter < saveEditorLength; ++iCounter) {
			let index = $("[id^=daoContainerDiv_]")[iCounter].id.split("_")[1];
			let editorObject = context.saveUpdateEditors.find(editors => editors["index"] == index);
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
		/**Fixed Bug for copy from file comparison. */
		let contextHeaderJson = context.headerJson();
		let headerJson = JSON.stringify(contextHeaderJson);
		/**Ends Here */
		$("#dynarestUrlDAO").val($("#dynarestUrl").val());
		$("#dynarestMethodNameDAO").val($("#dynarestMethodName").val());
		$("#serviceLogicForm").val(this.serviceLogicContent.getValue().toString());
		let daoQueryDetailsIs = context.daoDetailsIds.map(daoQuery => daoQuery["daoDetailsIds"]);
		$("#daoDetailsIds").val(JSON.stringify(this.daoQueryDetailsIs));
		$("#variableName").val(JSON.stringify(variableNameArray));
		$("#queryType").val(JSON.stringify(queryTypeArray));
		$("#headerJson").val(headerJson);
		$("#daoQueryDetails").val(JSON.stringify(daoQueryArray));
		$("#datasourceDetails").val(JSON.stringify(datasourceArray));
		let formData = $("#saveUpdateQueryForm").serialize();
		$.ajax({
			type: "POST",
			async: false,
			url: contextPath + "/cf/sdq",
			data: formData,
			success: function(data) {
				if (data !== "") {
					isDataSaved = true;
					$("#dynarestId").val(data);
					let versioningData = $("#dynamicRestForm, #saveUpdateQueryForm").serialize();
					enableVersioning(versioningData);
					saveEntityRoleAssociation(data);
					showMessage("Information saved successfully", "success");
				}
				$("#saveUpdateQueryForm").remove();
			},
			error: function(xhr, error) {
				showMessage("Error occurred while saving", "error");
			},
		});

		return isDataSaved;
	}

	hideShowAllowFiles = function() {
		let dynarestProdTypeId = $("#dynarestProdTypeId").val();
		let dynarestRequestTypeId = $("#dynarestRequestTypeId").val();
		if (dynarestRequestTypeId === "1" || dynarestRequestTypeId === "4") {
			$("#allowFilesDiv").show();
		} else {
			$("#allowFilesCheckbox").prop("checked", false);
			$("#allowFiles").val(0);
			$("#allowFilesDiv").hide();
		}
		showMethodSignature($("#dynarestPlatformId").val());
	}

	updateVariableSeq = function() {
		let sequenceNumber = 1;
		$("*[id*=inputcontainer_]").each(function(index, elem) {
			$(elem).prev().text(sequenceNumber + "] variable");
			sequenceNumber++;
		});
	}

	getEmailXMLStructure = function(templateName) {
		$("#xmlFormatDiv").show();
		if ($("#xmlFormat").text() === '') {
			$.ajax({
				type: "POST",
				async: false,
				url: contextPath + "/cf/gtbn",
				data: {
					templateName: templateName
				},
				success: function(data) {
					$("#xmlContent").val(data);
				},
				error: function(xhr, error) {
					showMessage("Error occurred while fetching template content", "error");
				},

			});
		}
	}

	getRESTXMLStructure = function(selectedElem) {
		let context = this;
		let selectedOptionVal = $(selectedElem).find(":selected").val();
		let editorIndex = $(selectedElem).attr("id").split("_")[1];
		let getCurrentValue = context.saveUpdateEditors[editorIndex].editor.getValue();
		if (selectedOptionVal === "4") {
			$("#datasourcecontainer_" + editorIndex).val(null);
			$("#datasourcecontainer_" + editorIndex).closest('div').hide();
			$.ajax({
				type: "POST",
				async: false,
				url: contextPath + "/cf/gtbn",
				data: {
					templateName: 'jq-web-client-call-xml'
				},
				success: function(data) {
					context.saveUpdateEditors[editorIndex].editor.setValue(data);
				},
				error: function(xhr, error) {
					showMessage("Error occurred while fetching template content", "error");
				},

			});
		} else {
			context.saveUpdateEditors[editorIndex].editor.setValue("");
			$("#datasourcecontainer_" + editorIndex).closest('div').show();
		}
	}

	copyUrlContextPath = function() {
		let input = $("<input>");
		$("body").append(input);
		input.val('${contextPath}' + "/api/" + $("#dynarestUrl").val()).select();
		document.execCommand("copy");
		input.remove();
		showMessage("REST URL copied successfully", "success");
	}

	copyUrlActualPath = function() {
		var $temp = $("<input>");
		$("body").append($temp);
		var uriPrefix = $("#urlPrefixLabel").text();
		var dynaRestUri = $("#dynarestUrl").val();
		$temp.val(uriPrefix + dynaRestUri).select();
		document.execCommand("copy");
		$temp.remove();
		showMessage("Copied successfully.", "success");
	}

	hideErrorMessage = function() {
		$('#errorMessage').hide();
	}

	backToDynarestListingPage = function() {
		window.location = "../cf/dynl";
	}

	headerJson = function() {
		var breakLoop = false;
		let headerJson = {};
		$("#headerTable").find('tr').each(function() {
			let key = $(this).find("input.key").val();
			if (key == "") {
				showMessage("Key is empty in header param", "warn");
				breakLoop = true;
				return false;
			}
			if (key !== undefined) {
				let value = $(this).find("input.value").val();
				if (value == undefined || value == "" || value == null) {
					showMessage("Value is null or empty in header param", "warn");
					breakLoop = true;
					return false;
				}
				headerJson[key] = value;
			}
		});
		if (breakLoop) {
			breakOut = false;
			return -1;
		}
		return headerJson;
	}
}

let saveEntityRoleAssociation = function(dynaRestId) {
	let roleIds = [];
	let entityRoles = new Object();
	entityRoles.entityName = $("#dynarestUrl").val();
	entityRoles.moduleId = $("#moduleId").val();
	entityRoles.entityId = dynaRestId;
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
			entityId: $("#dynarestId").val(),
			moduleId: $("#moduleId").val(),
		},
		success: function(data) {
			$.each(data, function(key, val) {
				multiselect.setSelectedObject(val);

			});
		}
	});
}

var removeByAttribute = function(arr, attr, value) {
	var i = arr.length;
	while (i--) {
		if (arr[i]
			&& arr[i].hasOwnProperty(attr)
			&& (arguments.length > 2 && arr[i][attr] === value)) {
			arr.splice(i, 1);
		}
	}
	return arr;
}

