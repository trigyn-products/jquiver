class HomePage {
    constructor() {

    }
}


HomePage.prototype.fn = {

	openNavigation :function() {
		$("#mySidenav").css("width","250px");
		$("#main").css("margin-left","250px");
		$("#closebtni").addClass("showcls");
		$("#openbtni").addClass("hidecls");
 
	},

	closeNavigation : function() {
		$("#mySidenav").css("width","0");
		$("#main").css("margin-left","0");
		$("#closebtni").removeClass("showcls");
		$("#openbtni").removeClass("hidecls");
		$("#openbtni").addClass("showcls");
		$("#closebtni").addClass("hidecls");
	},
	
	
	populateBodyContent : function(url){
	$('#bodyDiv').remove();
		$.ajax({
    		type: "GET",
    		url: contextPathHome+url,
    		dataType: "html",
    		success: function (data) {
    			delete contextPath;
    			let bodyHtml = $('<div id="bodyDiv"></div>');
    			bodyHtml.html(data);
    			bodyHtml.insertAfter("#titleDiv");
    		},
    		
    		error : function(xhr, error){
	        	$("#errorMessage").show();
	        	$('#errorMessage').html("Error occurred processing request");
	        },
	        
		})
	
	},
	
	



}