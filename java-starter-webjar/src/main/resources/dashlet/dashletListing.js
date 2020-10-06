class DashletListing {
    constructor() {
    
    }
    
    backToDashboarListing = function() {
		location.href = contextPath+"/cf/dbm";
	}
	
	downloadDashlet = function(){
	let context = this;
		$.ajax({
			url:"/cf/ddl",
			type:"POST",
			success:function(data){
				$('#snackbar').html("Templates downloaded successfully.");
				context.showSnackbarDashletListing();
			}
		});
	}
	
	uploadDashlet = function(){
	let context = this;
		$.ajax({
			url:"/cf/udl",
		    type:"POST",
		    success:function(data){
		    	$('#snackbar').html("Templates uploaded successfully.");
				context.showSnackbarDashletListing();
			}
		});
	}
	
   	downloadDashletById = function(thisObj){
   		let context = this;
	  	let dashletId = thisObj.id;
	  	$.ajax({
			url:"/cf/ddlbi",
			type:"POST",
	        data:{
	        	dashletId : dashletId,
	        },
			success:function(data){
				$('#snackbar').html("Template downloaded successfully.");
				context.showSnackbarDashletListing();
			}
	    });  
  	}
	
	uploadDashletById = function(thisObj){
		let context = this;
	  	let dashletId = thisObj.id;
		let dashletName = $("#"+dashletId).attr("name");
	  	$.ajax({
			url:"/cf/udlbn",
			type:"POST",
	        data:{
	        	dashletName : dashletName,
	        },
			success:function(data){
				$('#snackbar').html("Template uploaded successfully.");
				context.showSnackbarDashletListing();
			}
	    });  
  	}
  	
   	submitForm = function(element) {
	  $("#dashletId").val(element.id);
	  $("#formDMRedirect").submit();
	}
	
  	showSnackbarDashletListing = function() {
	   	let snackBar = $("#snackbar");
	   	snackBar.addClass('show');
	   	setTimeout(function(){ 
	   		snackBar.removeClass("show");
	   	}, 3000);
	}
	
}