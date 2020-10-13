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