
class JQFormIOGridUtils extends HTMLComponent {

	static schema(...extend) {
		return HTMLComponent.schema({
			type: 'gridutilscomponent',
			customType: 'gridutilscomponent',
			label: '',
			key: 'gridutilscomponent',
			placeHolder: 'Grid Utils',
			tag: 'div',
			content: "<div class=\"container\">\r\n    <div class=\"topband\">\r\n        <h2 class=\"title-cls-name float-left\">Your page title here</h2> \r\n        <div class=\"float-right\">\r\n            <button type=\"submit\" class=\"btn btn-primary\" onclick=\"upsert(null)\"> Create New </button>\r\n            <span onclick=\"backToWelcomePage();\">\r\n                <input id=\"backBtn\" class=\"btn btn-secondary\" name=\"backBtn\" value=\"Back\" type=\"button\">\r\n            </span>    \r\n        </div>\r\n        <div class=\"clearfix\"></div>        \r\n    </div>\r\n    <div id=\"yourGridId\" class=\"tablescrollcls\"></div>\r\n</div>"
		});
	}

	static get builderInfo() {
		return {
			title: 'Grid Utils',
			group: 'jquiver',
			icon: 'fa fa-th-list',
			weight: 70,
			schema: JQFormIOGridUtils.schema()
		};
	}
}

function getJsContent(gridId) {
	return $.ajax({
		type: "POST",
		url: contextPath + "/cf/gadc",
		async: !1,
		data: {
			templateName: "grid-default-template",
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

function getGridUtils() {
	return $.ajax({
		type: "GET",
		url: contextPath+apiPath+"/loadallgrids",
		async: !1,
		error: function() {
			showMessage("Error occurred while Fetching the File Bins.", "error");
		}
	});
}

function onSelectGridUtilsChange(dispTabComponents, logicTabComponents) {
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
	//JQFormIOGridUtils.schema(selectedFileBinVal, jsContent.responseText);
}

/**
 * Change the edit form to add the "Grid Utils" component a select dropdown
 * instead of a textfield so that they can only configure the "Grids" fields.
 */
JQFormIOGridUtils.editForm = (...args) => {
	const HTMLComponent = Formio.Components.components.htmlelement;
	const editForm = HTMLComponent.editForm(...args);
	let displayTab = Formio.Utils.getComponent(editForm.components, "display", true);
	let logicTabComponents = Formio.Utils.getComponent(editForm.components, "logic", true);
	var displayTabComponents = displayTab.components;
	displayTab.components = [];
	displayTab.components.push({
		type: 'select',
		label: 'Grid Utils',
		key: 'gridUtilsType',
		class: "required",
		dataSrc: 'values',
		placeholder: 'Select Grids',
		data: {
			values: getGridUtils().responseText
		},
		onChange: function() {
			onSelectGridUtilsChange(displayTabComponents, logicTabComponents);
		}
	});
	Formio.Utils.eachComponent(displayTabComponents, function(component) {
		displayTab.components.push(component);
	})
	return editForm;
};

Formio.Components.addComponent('gridutilscomponent', JQFormIOGridUtils);