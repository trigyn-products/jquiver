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
		} else if (selectedTab === "selectPreview") {
			previewTabName = "selectPreview";
			$("#jsContent").hide();
			$("#htmlContent").hide();
			$("#selectContent").show();
		} else if (selectedTab === "jsPreview") {
			previewTabName = "jsPreview";
			$("#jsContent").show();
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
	} else {
		$('#jsPreview').html("");
		$('#jsPreview').wrapInner(simplemde.options.previewRender(simplemde.value()));
		$("#jsPreview").scrollTop(0);
		callbackFun();
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

$(document).ready(function() {
	$('.form-check-input').change(function() {
		var inputType = $(this).attr('type');
		if (inputType == "checkbox") {
			var id = this.name;
			id = id.replace(/[^a-z0-9\s]/gi, '').replace(/[_\s]/g, '-');
			$('#hidden-' + id).remove();
			var val = 0;
			if (this.checked) {
				this.value = 1;
			} else {
				this.value = val;
				$(this).after('<input id = "hidden-' + id + '" name = "' + this.name + '" type="hidden" value = "' + val + '" />');

			}
		}
	});

});


const jqFormIO = new JQFormIO();