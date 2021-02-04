class HomePage {
    constructor() {

    }
}


HomePage.prototype.fn = {
		
	openNavigation :function() {
		$("#mySidenav").css("width","250px");
		$("#closebtni").addClass("showcls");
		$("#openbtni").addClass("hidecls");
		$('body').css('background-color', 'rgba(0,0,0,0.4)');
		$(".container").addClass("overlaycls");
		$(".jws-body-overlay").addClass("overlaycls");
		$('#searchInput').focus();
	},

	closeNavigation : function() {
		$("#mySidenav").css("width","0");
		$("#closebtni").removeClass("showcls");
		$("#openbtni").removeClass("hidecls");
		$("#openbtni").addClass("showcls");
		$("#closebtni").addClass("hidecls");	    
		$('body').css('background-color', 'white'); 
		$(".container").removeClass("overlaycls");
		$(".jws-body-overlay").removeClass("overlaycls");

	},
	
	
	populateBodyContent : function(url){
	$('#bodyDiv').remove();
		$.ajax({
    		type: "POST",
    		url: contextPathHome+"/cf/mul/"+url,
    		dataType: "html",
    		headers : {
    			"module-url" : url,
    		},
    		success: function (data) {
    			delete contextPath;
    			let bodyHtml = $('<div id="bodyDiv"></div>');
    			bodyHtml.html(data);
    			bodyHtml.insertAfter("#titleDiv");
    		},
    		
    		error : function(xhr, error){
	        	showMessage("Error occurred processing request", "error");
	        },
	        
		})
	
	},
	
	collapsableMenu : function(){
		$('.nav-item a.clickable').on("click", function (e) {
		    if ($(this).hasClass('panel-collapsed')) {
		        $(this).parents('.nav-item').find('.collapsein').slideDown();
		        $(this).removeClass('panel-collapsed');
		        $(this).find('i').removeClass('fa fa-caret-down').addClass('fa fa-caret-up');
		    }
		    else {
		        $(this).parents('.nav-item').find('.collapsein').slideUp();
		        $(this).addClass('panel-collapsed');
		        $(this).find('i').removeClass('fa fa-caret-up').addClass('fa fa-caret-down');
		    }
		});
	},	
	
	clearMenuSearch: function(){
		$("#searchInput").val("");
		this.menuSearchFilter();
	},
	
	menuSearchFilter : function(){
		let inputText = $("#searchInput").val().toUpperCase().trim();
		if(inputText == ""){
			$("#menuUL").find("li ").show();
			return;
		}
		$("#menuUL").find("li").hide();
		$("#menuUL").find("a").each(function(a_index, a_element){
			if($(a_element).text().trim().toUpperCase().indexOf(inputText) > -1){
				let divGroupElement = $(a_element).closest("div");
				let rootElement = $(divGroupElement).prev();
				$(rootElement).removeClass("panel-collapsed");
				$(rootElement).find("i").removeClass('fa fa-caret-down').addClass('fa fa-caret-up');
				$(a_element).parents().show()
			}
		});
	}

}
	
const uuidv4 =  function() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    let r = Math.random() * 16 | 0;
    let v = c == 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}

const showMessage = function(a_messageText, a_messageType){
	$(".jwsValidationDivCls").each(function(index, a_element){
		$(a_element).css({ top: $(a_element).position().top + 100 })
	});
	const currentDivID = "jwsValidationDiv" + uuidv4();
	let messageType = a_messageType.toLowerCase();
	let validationElement = $('<div id="'+currentDivID+'" class="jwsValidationDivCls"></div>');
	$("body").append(validationElement);
	let validationDiv = $("#" + currentDivID);


	if(messageType === "success"){
		validationDiv.addClass("alert alert-success common-validation-cls");
		validationDiv.append("<i class='val-icon fa fa-check'></i>");
	}else if(messageType === "info"){
		validationDiv.addClass("alert alert-info common-validation-cls");
		validationDiv.append("<i class='val-icon fa fa-info'></i>");
	}else if(messageType === "warn"){
		validationDiv.addClass("alert alert-warning common-validation-cls");
		validationDiv.append("<i class='val-icon fa fa-exclamation-triangle'></i>");
	}else if(messageType === "error"){
		validationDiv.addClass("alert alert-danger common-validation-cls");
		validationDiv.append("<i class='val-icon fa fa-exclamation-triangle'></i>");
	}

	validationDiv.append(a_messageText);
	setTimeout(function(){ 
    	$("#" + currentDivID).fadeOut();
    	$("#" + currentDivID).remove();
    }, 3000);

}

const typeOfAction = function(formId, selectedButton, saveFunction, backFunction){
	let selectedButtonId = $(selectedButton).prop("id");
	localStorage.setItem("jwsModuleAction", selectedButtonId);
	    
	if(saveFunction !== undefined || backFunction !== undefined){
	  	executeDefinedFunc(formId, selectedButtonId, saveFunction, backFunction);
	}else{
	   	executeCommonFunc(formId, selectedButtonId);
	}
}

const executeDefinedFunc = function(formId, selectedButtonId, saveFunction, backFunction){
	let isDataSaved;
	if(selectedButtonId === "saveAndReturn"){
	   	isDataSaved = saveFunction();
	   	if(isDataSaved){
	   		backFunction();
	   	}else{
	   		savedAction(formId,0);
	   	}
	}else if(selectedButtonId === "saveAndEdit"){
	    saveFunction();
	    savedAction(formId,1);
	}else if(selectedButtonId === "saveAndCreateNew"){
	   	isDataSaved = saveFunction();
	   	if(isDataSaved){
	   		resetForm(formId);
	   	}
	   	savedAction(formId,0);
	}
	
}

const executeCommonFunc = function(formId, selectedButtonId){
	let isDataSaved;
	if(selectedButtonId === "saveAndReturn"){
	   	isDataSaved = saveData();
	   	if(isDataSaved){
	   		backToPreviousPage();
	   	}else{
	   		savedAction(formId,0);
	   	}
	}else if(selectedButtonId === "saveAndEdit"){
	    saveData();
	    savedAction(formId,1);
	}else if(selectedButtonId === "saveAndCreateNew"){
	   	isDataSaved = saveData();
	   	if(isDataSaved){
	   		resetForm(formId);
	   	}
	   	savedAction(formId,0);
	}
}
 
const savedAction = function(formId, isEdit){ 	
	
	let actionSaved = localStorage.getItem("jwsModuleAction");
	
	if((isEdit === 0 || isEdit === "") && actionSaved === "saveAndEdit"){
		$("#actionDiv").find("#saveAndEdit").remove();
		return true;
	}
	
	if(actionSaved !== null && actionSaved !== ""){
		let defaultAction = $("#savedAction").find("button");
		let savedActionId = $(defaultAction).prop("id");
		if(savedActionId !== actionSaved){
			let updatedText = $("#actionDiv").find("#"+actionSaved).text();
			
			let savedActionText = $("#savedAction").text();
			$("#actionDiv").find("#"+actionSaved).html(savedActionText);
			$("#actionDiv").find("#"+actionSaved).prop("id",savedActionId);
			
			$(defaultAction).prop("id",actionSaved);
			$(defaultAction).html(updatedText);
		}
	}
	
	if(isEdit === 0 || isEdit === ""){
		$("#actionDiv").find("#saveAndEdit").remove();
	}	
}


const actionOptions = function(sourceElement){
	if(sourceElement !== undefined){
		$("#actionDropdownBtn").addClass("panel-collapsed");
		$("#actionDiv").slideUp();
		return true;
	}
	if ($("#actionDropdownBtn").hasClass("panel-collapsed")) {
		$("#actionDropdownBtn").removeClass("panel-collapsed");
		$("#actionDiv").slideDown();
	}else{
		$("#actionDropdownBtn").addClass("panel-collapsed");
		$("#actionDiv").slideUp();
	}
}


const resetForm = function(a_formId){
    let url = contextPath + "/cf/df";
	let formJson = sessionStorage.getItem(a_formId);
	
	let form = JSON.parse(formJson);
    $(document.body).append(form);
	let formId = $(form);
	$("#"+formId[0].id).submit();
}

const openForm = function(formId, formDetails, formUrl){
	$("#addEditDynForm").remove();
	var url = contextPath + (formUrl != undefined ? formUrl : "/cf/df");
	var form = $("<form id='addEditDynForm' method='post' action='"+url+"'></form>");
	$('body').append(form);
	$("#addEditDynForm").append("<input type='hidden' name='formId' value='"+formId+"'>");
	for(var counter = 0; counter < Object.keys(formDetails).length; ++counter) {
	  	let fieldName = Object.keys(formDetails)[counter];
	    let fieldValue = formDetails[fieldName];
	    $("#addEditDynForm").append("<input type='hidden' name='"+fieldName+"' value='"+fieldValue+"'>");
	}
	$("#addEditDynForm").submit();
}

const enableVersioning = function(formData){
	$.ajax({
		type: "POST",
    	url: contextPath + "/cf/smv",
    	dataType: "html",
    	data: formData,
    	success: function (data) {
	   	},
    	error : function(xhr, error){
	       	showMessage("Error occurred saving versioning information", "error");
	    },
	});
}

const hideShowActionButtons = function(){
    $(document).on("click", function closeMenu (event){
        if("actionDropdownBtn" !== event.target.id){
            actionOptions(event.target.id);
        }
    });
}

Array.prototype.formatSerializedArray = function() {
	for(let counter = 0; counter < this.length; ++counter) {
		if(this[counter]["valueType"] == undefined){
			this[counter]["valueType"] = $("#"+this[counter].name).attr('data-type');
		}
	}
	return this;
}