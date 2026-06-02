class JQFormIO {

	constructor() {
		this.initEventListeners();
	}

	loadFormIoDetails = function(formioid) {
		let context = this;
		let persistenceType = $("#persistenceType").val();
		if (persistenceType === "1") {
			loadFormIODefaultTab("formio-default-template", context.updateFormIOTemplate, formioid);
		} else if (persistenceType === "2") {
			loadFormIODefaultTab("formio-external-template", context.updateFormIOTemplate, formioid);
		} else if (persistenceType === "3") {
           loadFormIODefaultTab("formio-custom-pluggable-template", context.updateFormIOTemplate, formioid);
		}

	};

	setQueryContent = function() {
		const context = this;
	}

	updateFormIOTemplate = function() {
		let formIoId = $("#formioid").val();
		if (formIoId != "") {
			let jsFormioId = $("pre span").filter(function() { return ($(this).text() === '\"yourFormIoId\"') });
			$(jsFormioId).text('"' + formIoId + '"');
		}
	}


	initEventListeners = () => {

		$(document).on("click", "#reloadCaptcha", function(event) {
			event.preventDefault();
			var isFormIODesignPage = $('#entityName').val();
			if (isFormIODesignPage === 'FormIO') {
				return false; // skip if on the FormIO page
			} else {
				reloadCptcha();
			}
		});

	};
}

function loadFormIODefaultTab(templateName, callbackFun, formioid) {
	$('.ui-tabs-nav li a').click(function() {
		let selectedTab = $(this).attr("data-target");
		if (selectedTab === "htmlContent") {
			previewTabName = "htmlPreview";
			$("#jsContent").hide();
			$("#htmlContent").show();
			$("#selectContent").hide();
			$("#customPluggableContent").hide();
		} else if (selectedTab === "selectPreview") {
			previewTabName = "selectPreview";
			$("#jsContent").hide();
			$("#htmlContent").hide();
			$("#selectContent").show();
			$("#customPluggableContent").hide();
		} else if (selectedTab === "jsPreview") {
			previewTabName = "jsPreview";
			$("#jsContent").show();
			$("#htmlContent").hide();
			$("#customPluggableContent").hide();
		} else if (selectedTab === "customPluggableContent") {
			previewTabName = "customPluggableHtmlContentPreview";
			$("#customPluggableContent").show();
			$("#jsContent").hide();
		    $("#htmlContent").hide();
		    $("#selectContent").hide();
		}

		$.ajax({
			type: "POST",
			async: false,
			url: contextPath + "/cf/gadc",
			data: {
				templateName: templateName,
				selectedTab: selectedTab,
				formioid: formioid
			},
			success: function(data) {
				displayFormIOTabContent(data, selectedTab, callbackFun);
			},
			error: function(xhr, error) {
				showMessage("Error occurred while fetching default content", "error");
			},
		});
	});

}

function displayFormIOTabContent(data, selectedTab, callbackFun) {
	let simplemde = new SimpleMDE({
		element: document.getElementById("previewContent"),
		initialValue: data,
		renderingConfig: {
			codeSyntaxHighlighting: true,
		}
	});
	$("#previewContent").css('display', 'none');
	if (selectedTab == "htmlContent") {
		$('#htmlPreview').html("");
		$('#htmlPreview').wrapInner(simplemde.options.previewRender(simplemde.value()));
		$("#htmlPreview").scrollTop(0);
	} if (selectedTab == "selectContent") {
		$('#selectContent').html("");
		$('#selectContent').wrapInner(simplemde.options.previewRender(simplemde.value()));
		$("#selectContent").scrollTop(0);
	} if (selectedTab == "jsContent") {
		$('#jsPreview').html("");
		$('#jsPreview').wrapInner(simplemde.options.previewRender(simplemde.value()));
		$("#jsPreview").scrollTop(0);
		callbackFun();
	} if (selectedTab == "customPluggableContent") {
		let routeName = $("#routeName").val().trim();
		const renderedContent = simplemde.options.previewRender(simplemde.value().replace(/routeName/g, routeName));
		$('#customPluggableHtmlContentPreview').html("");
		$('#customPluggableHtmlContentPreview').wrapInner(renderedContent);
		$("#customPluggableHtmlContentPreview").scrollTop(0);
			//callbackFun();
	}
}

function generateCaptch(imagesrc) {
	const imgElement = document.getElementById('imgCaptcha');
	fetch(imagesrc, {
		headers: {
			'r': localStorage.getItem('r')
		}
	}).then(response => {
		localStorage.setItem('r', response.headers.get('r'));
		if (response.ok) {
			return response.blob();
		} else {
			return null;
		}

	}).then((blob) => {
		if (blob != null) {
			const objectURL = URL.createObjectURL(blob);
			imgElement.src = objectURL;
			$('#imgCaptcha').attr('src', objectURL);
			if ($('#captcha').prop('disabled')) {
				$("#formCaptcha").removeAttr('disabled');
			}
		}
		else {
			//set path and show error Message and disable text
			imgElement.src = contextPath + "/webjars/1.0/images/noimg.png";
			$("#formCaptcha").attr('disabled', 'disabled');
			//$("#captchaGenerationError").html("Captcha generation failed");
			showMessage("Captcha generation failed", "warn");
			// alert(resourceBundleData("jws.clearTxt"));
		}
	}).catch(error => {
		showMessage("Error while  loading Captcha", "warn");

	});
}

function reloadCptcha() {
	var imagesrc = contextPath + "/cf/captcha/" + formId + "_captcha";
	generateCaptch(imagesrc);
	$("#formCaptcha").val('');
}


/*
 * In case of custom UI, this method will be called.
 */
function fileListing(fileObj) {
	let input = $("<input id='" + fileObj["id"] + "' value='" + fileObj["id"] + "' type='text'>");
	//    input.insertAfter($("#fileIdDiv"));
}

/*
 * This is just for demo purpose, to show sample function from the button
 */
function fileName(a_fileName) {
	showMessage("File Name: " + a_fileName, "success");
}

/*
 * This is just for demo purpose, to show sample function from the button
 */
function copyFilePath(a_fileUploadId) {
	let input = $("<input>");
	$("body").append(input);
	input.val(window.location.origin + contextPath + "/cf/files/" + a_fileUploadId).select();
	document.execCommand("copy");
	input.remove();
	showMessage("File path copied successfully", "success");
}

function deduplicateFileUploadUI() {

	const master = document.querySelector('#fileUploadMaster');
	if (!master) return;

	// ✅ Deduplicate: Instruction block (only inside cm-uploadwrap)
	const instructionBlocks = master.querySelectorAll('.cm-uploadwrap .dropzone-title');
	instructionBlocks.forEach((el, i) => i > 0 && el.remove());

	// ✅ Deduplicate: Copy-paste blocks (Click and paste)
	const copyBlocks = master.querySelectorAll('.copyblock.dropzone-title');
	copyBlocks.forEach((el, i) => i > 0 && el.remove());

	// ✅ Deduplicate: File preview cards by file name
	const previews = master.querySelectorAll('.dz-preview');
	const seen = new Set();
	previews.forEach(preview => {
		const name = preview.querySelector('[data-dz-name]')?.textContent.trim();
		if (seen.has(name)) {
			preview.remove();
		} else {
			seen.add(name);
		}
	});

	// ✅ Remove empty duplicate <label class="dropzone-container ...">
	const labels = master.querySelectorAll('label.dropzone-container');
	labels.forEach((label, i) => {
		const hasTitle = label.querySelector('.dropzone-title');
		if (!hasTitle || i > 0) {
			label.remove();
		}
	});
}


$(document).ready(function() {
	
});


const jqFormIO = new JQFormIO();