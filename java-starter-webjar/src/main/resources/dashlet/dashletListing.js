class DashletListing {
    constructor() {
    
    }
    
    backToDashboarListing = function() {
		location.href = contextPath+"/cf/dbm";
	}
	
	downloadDashlet = function(){
	let context = this;
		$.ajax({
			url: contextPath+"/cf/ddl",
			type:"POST",
			success:function(data){
				showMessage("Dashlets downloaded successfully", "success");
			},
	       	error : function(xhr, error){
	       		showMessage("Error occurred while downloading dashlets", "error");
	       	}
		});
	}
	
	uploadDashlet = function(){
	let context = this;
		$.ajax({
			url: contextPath+"/cf/udl",
		    type:"POST",
		    success:function(data){
				showMessage("Dashlets uploaded successfully", "success");
			},
	       	error : function(xhr, error){
	       		showMessage("Error occurred while uploading dashlets", "error");
	       	}
		});
	}
	
   	downloadDashletById = function(thisObj){
   		let context = this;
	  	let dashletId = thisObj.id;
	  	$.ajax({
			url: contextPath+"/cf/ddlbi",
			type:"POST",
	        data:{
	        	dashletId : dashletId,
	        },
			success:function(data){
				showMessage("Dashlet downloaded successfully", "success");
			},
	       	error : function(xhr, error){
	       		showMessage("Error occurred while downloading dashlet", "error");
	       	}
	    });  
  	}
	
	uploadDashletById = function(thisObj){
		let context = this;
	  	let dashletId = thisObj.id;
		let dashletName = $("#"+dashletId).attr("name");
	  	$.ajax({
			url: contextPath+"/cf/udlbn",
			type:"POST",
	        data:{
	        	dashletName : dashletName,
	        },
			success:function(data){
				showMessage("Dashlet uploaded successfully", "success");
			},
	       	error : function(xhr, error){
	       		showMessage("Error occurred while uploading dashlet", "error");
	       	}
	    });  
  	}
  	
  	submitRevisionForm = function(sourceElement) {
		let selectedId = sourceElement.id.split("_")[0];
		let moduleName = $("#"+sourceElement.id).attr("name")
      	$("#entityId").val(selectedId);
		$("#moduleName").val(moduleName);
      	$("#revisionForm").submit();
    }
    
   	submitForm = function(element) {
		$("#dashletId").val(element.id);
		$("#formDMRedirect").submit();
	}
	
}