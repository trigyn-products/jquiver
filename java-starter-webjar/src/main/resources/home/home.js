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
	
	
	
	menuSearchFilter : function(){
		const inputText = $("#searchInput");
		const filterText = inputText.val().toUpperCase();
		
		$(".searchedClass").each(function(){
			$(this).remove();
		});
		if(filterText == "")
		{
			$("#menuUL li").each(function(i) {
				$(this).css('display', 'block');
			});
			return;
		}
		else
		{
			$("#menuUL li").each(function(i) {
				$(this).css('display', 'none');
			});
		}
		
		$("#menuUL li").each(function(){
			var text = $(this).text();
			var li;
			if (text.toUpperCase().indexOf(filterText) > -1) {
				li=$(this).clone();
				li.css('display', 'block');
				li.addClass("searchedClass");
				$("#menuUL").append(li);
			}
		});
	},
	
	
}

const showMessage = function(a_messageText, a_messageType){
	$("#jwsValidationDiv").remove();
	let messageType = a_messageType.toLowerCase();
	let validationElement = $('<div id="jwsValidationDiv"></div>');
	$("body").append(validationElement);
	let validationDiv = $("#jwsValidationDiv");

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
    	$("#jwsValidationDiv").remove();
    }, 3000);

}