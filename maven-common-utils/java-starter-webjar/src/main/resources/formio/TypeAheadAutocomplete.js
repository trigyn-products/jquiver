
class TypeAheadAutocomplete extends HTMLComponent {

	static schema(...extend) {
		return HTMLComponent.schema({
			type: 'typeautocompletecomponent',
			customType: 'typeautocompletecomponent',
			label: '',
			key: 'typeautocompletecomponent',
			placeHolder: 'TypeAhead/Autocomplete',
			tag: 'div',
			content: "<div class=\"col-6\">\r\n    <div class=\"col-inner-form full-form-fields\">\r\n        <label for=\"flammableState\" style=\"white-space:nowrap\">Autocomplete</label>\r\n        <div class=\"search-cover\">\r\n            <input class=\"form-control\" id=\"autocompleteId\" type=\"text\">\r\n            <i class=\"fa fa-search\" aria-hidden=\"true\"></i>\r\n        </div>\r\n    </div>\r\n</div>"
		});
	}

	static get builderInfo() {
		return {
			title: 'TypeAhead/Autocomplete',
			group: 'jquiver',
			icon: 'fa fa-th-list',
			weight: 70,
			schema: TypeAheadAutocomplete.schema()
		};
	}
}

function getJsContent(gridId) {
	return $.ajax({
		type: "POST",
		url: contextPath + "/cf/gadc",
		async: !1,
		data: {
			templateName: "typeauto-default-template",
			selectedTab: "jsContent"
		},
		success: function(data) {
			//var jsContent = data.replaceAll("```JavaScript", "");
			//jsContent = data.replaceAll("yourFileBinId", fileBinId);
			return data;
		},
		error: function(xhr, error) {
			showMessage("Error occurred while fetching Filebin Javascript content", "error");
		}
	});
}

function getAutocompletes() {
	return $.ajax({
		type: "GET",
		url: contextPath+apiPath+"/loadAutocompletes",
		async: !1,
		error: function() {
			showMessage("Error occurred while Fetching the File Bins.", "error");
		}
	});
}

function onSelectAutocompleteChange(dispTabComponents, logicTabComponents) {
	//$("input[name='data[className]']").val("fileupload dropzone");
	//$("input[name='data[attrs][0][attr]']").val("id");
	//$("input[name='data[attrs][0][value]']").val("fileUploadMaster");
	//$("input[name='data[logic][0][name]']").val("fileupload");
	//var selectedGridUtilsVal = $("select[name='data[gridUtils]'] option:selected").val();
	//document.querySelector(".choices__item[data-value='javascript']").dispatchEvent(new Event('mousedown'));
	//var jsContent = getJsContent(selectedGridUtilsVal);
	//$("input[name='data[logic][0][name]']").val(selectedGridUtilsVal);
	//var content = Formio.Utils.getComponent(dispTabComponents, "content", true).defaultValue = 'Test 123';
	//Formio.Utils.getComponent(dispTabComponents, "content", true).setValue('Test 123');
	//FormioUtils.getComponent(Formio.forms[id].originalComponents, "sampleComponent").setValue("just a sample");
	//TypeAheadAutocomplete.schema(selectedFileBinVal, jsContent.responseText);
}

/**
 * Change the edit form to add the "Grid Utils" component a select dropdown
 * instead of a textfield so that they can only configure the "Grids" fields.
 */
TypeAheadAutocomplete.editForm = (...args) => {
	const HTMLComponent = Formio.Components.components.htmlelement;
	const editForm = HTMLComponent.editForm(...args);
	let displayTab = Formio.Utils.getComponent(editForm.components, "display", true);
	let logicTabComponents = Formio.Utils.getComponent(editForm.components, "logic", true);
	var displayTabComponents = displayTab.components;
	displayTab.components = [];
	displayTab.components.push({
		type: 'select',
		label: 'TypeAhead/Autocomplete',
		key: 'typeautotype',
		class: "required",
		dataSrc: 'values',
		placeholder: 'Select TypeAhead/Autocomplete',
		data: {
			values: getAutocompletes().responseText
		},
		onChange: function() {
			onSelectAutocompleteChange(displayTabComponents, logicTabComponents);
		}
	});
	Formio.Utils.eachComponent(displayTabComponents, function(component) {
		displayTab.components.push(component);
	})
	return editForm;
};

Formio.Components.addComponent('typeautocompletecomponent', TypeAheadAutocomplete);