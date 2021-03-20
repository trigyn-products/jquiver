class HelpManual {

	constructor(){
		
	}
	
	getManualEntities = function(manualId) {
		const context = this;
        if(manualId != undefined) {
            $.ajax({
                type : "GET",
                async: false,
                url : contextPath+"/api/get-manual-details",
                data : {
                    manualId: manualId
                },
                success : function(helpManualArray) {
                	helpManualArray.forEach((manualDetails, index, helpManualArray) =>{
                		manualDetails.entry_content = manualDetails.entry_content.replaceAll("/cf/files/", contextPath+"/cf/files/") ;
                		return manualDetails;
                	});
                    context.helpManualDetails = helpManualArray;
                }
            });
        }
    }
    
    loadManualPreview = function(event, element){
	  const id = element.id;
	  var i, tabcontent, tablinks;
	  tabcontent = document.getElementsByClassName("tabcontent");
	  for (i = 0; i < tabcontent.length; i++) {
	    tabcontent[i].style.display = "none";
	  }
	
	  // Get all elements with class="tablinks" and remove the class "active"
	  tablinks = document.getElementsByClassName("tablinks");
	  for (i = 0; i < tablinks.length; i++) {
	    tablinks[i].className = tablinks[i].className.replace(" active", "");
	  }
	
	  // Show the current tab, and add an "active" class to the link that opened the tab
	  let simplemde = new SimpleMDE({
	  	initialValue : manual.helpManualDetails.find(details => {return details["manual_entry_id"] == element.id})["entry_content"],
	  	renderingConfig: {
	        codeSyntaxHighlighting: true,
    	}
	  });
	  $("#previewContent").css('display', 'none');
	  $('#previews').html("");
	  $('#previews').wrapInner(simplemde.options.previewRender(simplemde.value()));
	  event.currentTarget.className += " active";
    }
    
    getManualDetails = function() {
    	let manualDetails;
    	$.ajax({
		  type : "GET",
		  async: false,
		  url : contextPath+"/api/manual-type",
          success : function(data) {
			manualDetails = data;
		  },
	      error : function(xhr, error){
			showMessage("Error occurred while getting manual data", "error");
	      },
		});
		return manualDetails;
    }
    
    saveManualDetails = function(fileIds) {
    	if(fileIds !== undefined && fileIds.length > 0) {
    		$.ajax({
			  type : "POST",
			  async: false,
			  url : contextPath+"/cf/smfd",
			  data: {
			  	fileIds: JSON.stringify(fileIds),
			  	entryName: $("#entryname").val(),
			  	manualId: manualType
			  },
	          success : function(data) {
			  },
		      error : function(xhr, error){
				showMessage("Error occurred while getting manual data", "error");
		      },
			});
    	}
    }

}