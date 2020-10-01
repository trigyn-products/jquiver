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
	        	$("#errorMessage").show();
	        	$('#errorMessage').html("Error occurred processing request");
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