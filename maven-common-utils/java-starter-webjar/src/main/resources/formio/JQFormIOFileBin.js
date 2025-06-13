const HTMLComponent = Formio.Components.components.htmlelement;
/**
 * Create a File Bin compoennt and extend from the HTMLComponent.
 */
class JQFormIOFileBin extends HTMLComponent {

	/**
	 * Define the default schema to change the type and tag and label. 
	 */
	static schema(...extend) {
		return HTMLComponent.schema({
			type: 'filebincomponent',
			customType: 'filebincomponent',
			placeholder: 'File Bin',
			tag: 'div',
			//attrs: [{
				//attr: 'id',
				//value: 'fileUploadMaster'
			//}],
			label: '',
			content: "<input type=\"text\" id=\"copyFilePathInput\" name=\"copyFilePathInput\" style=\"display:none\">\r\n<div class=\"clearfix\"></div>\r\n<div class=\"col-12\">\r\n\t<div id=\"fileUploadMaster\" class=\"col-8 fileupload dropzone\"></div>\r\n</div>", 
			//className: '',
			//customClass: 'fileupload dropzone',
			//styles: '',
			hidden: false,
			disabled: false,
		}, ...extend);
	}

	static get builderInfo() {
	//	console.log('Inside Builder Info : ');
		return {
			title: 'File Bin',
			group: 'jquiver',
			icon: 'fa fa-file',
			weight: 2,
			documentation: '/userguide/#html-element-component',
			schema: JQFormIOFileBin.schema()
		};
	}

	static render(content) {
		return super.render('<div ref="jqFormIOFileBin" placeholder="File Upload" class="col-12"><div id="fileUploadMaster" class="col-8 fileupload dropzone"></div></div>');
	}

	static build() {
		super.build();
		let ele = super.renderTemplate("<div id='custom'>name</div>", {});
		let element = super.getElement();
		element.appendChild(ele);
	}
}


function getFileBinJsContent(fileBinId) {
	return $.ajax({
		type: "POST",
		url: contextPath + "/cf/gadc",
		async: !1,
		data: {
			templateName: "filebin-default-template",
			selectedTab: "jsContent"
		},
		success: function(data) {
			var jsContent = data.replaceAll("```JavaScript", "");
			jsContent = data.replaceAll("yourFileBinId", fileBinId);
			return jsContent;
		},
		error: function(xhr, error) {
			showMessage("Error occurred while fetching Filebin Javascript content", "error");
		}
	});
}

function getFileBins() {
	return $.ajax({
		type: "GET",
		url: contextPath+apiPath+"/loadallfilebins",
		async: !1,
		error: function() {
			showMessage("Error occurred while Fetching the File Bins.", "error");
		}
	});
}

function onSelectFileBinChange() {
	//$("input[name='data[className]']").val("fileupload dropzone");
	//$("input[name='data[attrs][0][attr]']").val("id");
	//$("input[name='data[attrs][0][value]']").val("fileUploadMaster");
	//$("input[name='data[logic][0][name]']").val("fileupload");
	//var selectedFileBinVal = $("select[name='data[fileBinType]'] option:selected").val();
	//document.querySelector(".choices__item[data-value='javascript']").dispatchEvent(new Event('mousedown'));
	//var jsContent = getFileBinJsContent(selectedFileBinVal);
	//$("input[name='data[logic][0][name]']").val(selectedFileBinVal);
	//JQFormIOFileBin.schema(selectedFileBinVal, jsContent.responseText);
}


/**
 * Change the edit form to add the "File Bin" component a select dropdown
 * instead of a textfield so that they can only configure the "file bins" fields.
 */

/*
JQFormIOFileBin.editForm = (...args) => {
	const baseForm = HTMLComponent.editForm(...args);
	//console.log(JSON.stringify(baseForm));
	let displayTab = Formio.Utils.getComponent(baseForm.components, "display", true);
	displayTab.components.push(
		{
			type: 'select',
			label: 'File Bin',
			class: "required",
			dataSrc: 'values',
			placeholder: 'Select File Bin',
			data: {
				values: getFileBins().responseText
			},
			key: "fileBin".toLowerCase(),
			onChange: function() {
				onSelectFileBinChange();
			}
		}, {
		type: 'textfield',
		label: 'Content',
		key: 'content',
		input: true,
		defaultValue: "<input type=\"text\" id=\"copyFilePathInput\" name=\"copyFilePathInput\" style=\"display:none\">\r\n<div class=\"clearfix\"></div>\r\n<div class=\"col-12\">\r\n\t<div id=\"fileUploadMaster\" class=\"col-8 fileupload dropzone\"></div>\r\n</div>",
		description: 'Modify : Your File BIn HTML COntent If Required.',
	});

	return baseForm;
};
*/

JQFormIOFileBin.editForm = (...args) => {
	const HTMLComponent = Formio.Components.components.htmlelement;
	const editForm = HTMLComponent.editForm(...args);
	let displayTab = Formio.Utils.getComponent(editForm.components, "display", true);
	var displayTabComponents = displayTab.components;
	displayTab.components = [];
	displayTab.components.push({
		type: 'select',
		label: 'File Bin',
		key: 'fileBinType',
		class: "required",
		dataSrc: 'values',
		data: {
			values: getFileBins().responseText
		},
		onChange: function() {
			onSelectFileBinChange();
		}
	});
	Formio.Utils.eachComponent(displayTabComponents, function(component) {
		displayTab.components.push(component);
	})
	return editForm;
};

Formio.Components.addComponent('filebincomponent', JQFormIOFileBin);

