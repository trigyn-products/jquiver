(function($) {

	class FileUpload {
		constuctor(element, options, selectedFiles){
			this.element = element;
			this.options = options;
			this.selectedFiles = [];
			this.uploaded = false;
			this.dropzone = [];
		}
		
		getSelectedFileIds() {
			return this.dropzone.getAcceptedFiles().map(file => 
 				file.id != undefined ? file.id : JSON.parse(file.xhr.response).fileIds[0]);
		}
		
		deleteFileFromServerById(fileId) {
			$.ajax({
			  type : "DELETE",
			  async : false,
			  url : contextPath+"/cf/files/" + fileId,
			  success: function(data) {
			  	console.log("success");
			  }
			});
		}
		
		getByteSize(byteSize) {
			byteSize = Number.parseInt(byteSize);
			return byteSize / (1024 * 1024);
		}
		
		viewFileEvent(event, fileId){
			window.open( contextPath+"/cf/files/"+fileId, '_blank');
		}
		
		deleteFileById(fileId) {
			let index = this.dropzone.getAcceptedFiles().map(file => 
 				file.id != undefined ? file.id : JSON.parse(file.xhr.response).fileIds[0]).indexOf(fileId);
 			let file = this.dropzone.getAcceptedFiles()[index];
 			this.removeFileEvent(new Event("remove"), this.dropzone, file)
		}
		
		showSelectedFiles(fileIds, dropZoneObj, fileUploadObj) {
			let dropzone = this.dropzone;
			let context = this;
			if(fileIds != undefined && fileIds.length > 0) {
				$.ajax({
					  type : "GET",
					  async : false,
					  url : contextPath+"/cf/fileDetails",
					  data : {
					  	fileUploadIds : JSON.stringify(fileIds)
					  }, 
					  success: function(data) {
			  		  	for(let iCounter = 0; iCounter < data.length; ++iCounter) {
			  		  		let file = { name: data[iCounter]["fileName"], size: data[iCounter]["sizeInBytes"], id: data[iCounter]["fileId"], accepted: true};
							dropzone.emit("addedfile", file);
							dropzone.emit("complete", file);
		       				dropzone.files.push(file);
							let viewButton = Dropzone.createElement("<i class='fileupload-actions fa fa-download float-right'></i>");
						    let fileId = data[iCounter]["fileId"];
	  		  				viewButton.addEventListener("click", function(event) {
	  		  					context.viewFileEvent(event, fileId)
	  		  				});
						    file.previewElement.appendChild(viewButton);
			  		  	}
			  		  },error: function(data, errorMessage, xhr){
			  		  		let customErrorMessage;
			  		  		if(data.status == 403){
			  		  			customErrorMessage = "You don't have enough privilege to access this module";
			  		  		}
			  		  		let spanElement = $("<span class='fa fa-ban' id='dropZoneErrorMessage'></span>")
		  		  	  		$(".filepreviewcontainer").html(spanElement)
		  		  	  		$("#dropZoneErrorMessage").text(customErrorMessage);
		  		  	  	},	
			  });
			}
		}
		
		loadSelectedFiles() {
			let dropzone = this.dropzone;
			let context = this;
			let options = this.options;
			if(options.fileBinId != undefined) {
				$.ajax({
					  type : "GET",
					  async : false,
					  url : contextPath+"/cf/fdbbi",
					  data : {
					  	fileBinId : options.fileBinId,
					  	fileAssociationId: options.fileAssociationId
					  }, 
					  success: function(data) {
			  		  	for(let iCounter = 0; iCounter < data.length; ++iCounter) {
			  		  		let file = { name: data[iCounter]["fileName"], size: data[iCounter]["sizeInBytes"], id: data[iCounter]["fileId"], accepted: true};
							dropzone.emit("addedfile", file);
							dropzone.emit("complete", file);
		       				dropzone.files.push(file);
							let viewButton = Dropzone.createElement("<i class='fileupload-actions fa fa-download float-right'></i>");
						    let fileId = data[iCounter]["fileId"];
	  		  				viewButton.addEventListener("click", function(event) {
	  		  					context.viewFileEvent(event, fileId)
	  		  				});
	  		  				file.previewElement.appendChild(viewButton);
	  		  				if(typeof options.renderer !== undefined && typeof options.renderer === "function"){
	  		  					let cutomButton = options.renderer(file);	  	
	  		  					cutomButton = Dropzone.createElement(cutomButton);
	  		  					file.previewElement.appendChild(cutomButton);	  	
	  		  				} 
	  		  				
			  		  	}
			  		  }	,error: function(data, errorMessage, xhr){
			  		  		let customErrorMessage;
			  		  		if(data.status == 403){
			  		  			customErrorMessage = "You don't have enough privilege to access this module";
			  		  		}
			  		  		let spanElement = $("<span class='fa fa-ban' id='dropZoneErrorMessage'></span>")
		  		  	  		$(".filepreviewcontainer").html(spanElement)
		  		  	  		$("#dropZoneErrorMessage").text(customErrorMessage);
		  		  	  	},
			  });
			}
		}
		
		removeFileEvent(event, _this, file) {
			let fileUploadObj = this;
			let fileUploadId = file.id !== undefined ? file.id : JSON.parse(file.xhr.responseText).fileIds[0]; 
			let deleleteElement = $('<div id="deleteConfirmation"></div>');
			$("body").append(deleleteElement);
			$("#deleteConfirmation").html("Are you sure you want to delete?");
			$("#deleteConfirmation").dialog({
				bgiframe		: true,
				autoOpen		: true, 
				modal		 	: true,
				closeOnEscape 	: true,
				draggable	 : true,
				resizable	 : false,
				title		 : "Delete",
				buttons		 : [{
						text :"Cancel",
						click: function() { 
							$(this).dialog("destroy");
							$(this).remove();
						},
					},
					{
						text	: "Delete",
						click	: function(){
							$.ajax({
					            type : "DELETE",
					            url : contextPath+"/cf/files/"+fileUploadId,
					            success : function(data) {
					                event.preventDefault();
								    event.stopPropagation();
								  	_this.removeFile(file);
								  	let fileId = file.id;
								  	if(fileId == undefined){
								  		fileId = JSON.parse(file.xhr.response)["fileIds"][0];
								  	}
								  	if(fileUploadObj.options.deletecallback != undefined) {
								  		fileUploadObj.options.deletecallback(fileId);
								  	}
								  	$("#deleteConfirmation").dialog("destroy");
									$("#deleteConfirmation").remove();
					                showMessage("File deleted successfully", "success");
					            },
					            error : function(xhr, error){
					            	if(xhr.status == 403){
			  		  					showMessage("You don't have enough privilege to delete this file", "error");
			  		  				}else{
					                	showMessage("Error occurred while deleting file", "error");
					                }
					                $("#deleteConfirmation").dialog("destroy");
									$("#deleteConfirmation").remove();
					            },
							});
							
						}
		           	},
		       ],
		       open		: function( event, ui ) {	    	
			   	   $('.ui-dialog-titlebar')
			   	    .find('button').removeClass('ui-dialog-titlebar-close').addClass('ui-button ui-corner-all ui-widget ui-button-icon-only ui-dialog-titlebar-close')
			       .prepend('<span class="ui-button-icon ui-icon ui-icon-closethick"></span>').append('<span class="ui-button-icon-space"></span>');
			   }	
		
			});
		}
		
		removeUploadedFile(event, _this, file){
			event.preventDefault();
			event.stopPropagation();
			_this.removeFile(file);
		}
		
		getSelectedFiles(){
			let dropzone = this.dropzone;
			let context = this;
			let options = this.options;
			if(options.fileBinId != undefined) {
				$.ajax({
					  type : "GET",
					  async : false,
					  url : contextPath+"/cf/fdbbi",
					  data : {
					  	fileBinId : options.fileBinId,
					  	fileAssociationId: options.fileAssociationId
					  }, 
					  success: function(data) {
			  		  	for(let iCounter = 0; iCounter < data.length; ++iCounter) {
			  		  		let file = { name: data[iCounter]["fileName"], size: data[iCounter]["sizeInBytes"], id: data[iCounter]["fileId"], accepted: true};
	  		  				if(typeof options.renderer !== undefined && typeof options.renderer === "function"){
	  		  					options.renderer(file);	  	
	  		  				} 
			  		  	}
			  		  },error: function(data, errorMessage, xhr){
			  		  		if(data != undefined && data.status == 403){
		  		  				showMessage("You don't have enough privilege to access this module", "error");
			  				}else{
			  					showMessage(errorMessage, "error");
			  				}
		  		  	  },
			  });
			}
		}
		
		disableDropZone(message){
			this.dropzone.disable();
			$(this.dropzone.element).find(".dropzone-title").html(message);
			$(this.dropzone.element).find(".cm-uploadicon").addClass("dropzone-disable-img-cls");
			$(this.dropzone.element).find(".dropzone-title").addClass("dropzone-disable-message-cls");
		}
		
		enableDropZone(fileAssocId){
		    this.dropzone.enable();
		    this.options.fileAssociationId = fileAssocId;
		    $(this.dropzone.element).find(".cm-uploadicon").removeClass("dropzone-disable-img-cls");
			$(this.dropzone.element).find(".dropzone-title").removeClass("dropzone-disable-message-cls");
            $(this.dropzone.element).find(".dropzone-title").html('Drag and drop your files or <span class="browse">browse</span> your files');
		}
	}
	
	
	$.fn.fileUpload = function(options, selectedFiles) {
	let fileUpload = new FileUpload(this, options, selectedFiles);
	fileUpload.options = options;
	
	if(options.createFileBin !== undefined && options.createFileBin === true){
	
		$(this).wrap('<div class="pg-form-dropzone"><div class="row"><div class="col-12 dropzone-wrapper"><div class="cm-dropzone-wrap"></div></div></div></div>');
		
		let labelDiv = $('<label for="fileupload" class="dropzone-container dz-default dz-message"><div class="cm-uploadwrap clearfix"></div></label><div class="filepreviewcontainer cm-scrollbar"></div>');
		$(this).append(labelDiv);
		$(this).find(".cm-uploadwrap").append($('<div class="cm-uploadicon"><img src="'+contextPath+'/webjars/1.0/images/multifileupload.svg"></div><div class="dropzone-title">Drag and drop your files or <span class="browse">browse</span> your files</div>'));
		// $(this).append($('<label for="fileupload" class="dropzone-container dz-default dz-message"><div class="cm-uploadwrap clearfix"><div class="cm-uploadicon"><img src="'+contextPath+'/webjars/1.0/images/multifileupload.svg"></div><div class="dropzone-title">Drag and drop your files or <span class="browse">browse</span> your files</div></div></label>'));
		/** Customizing the dropzone view **/
		fileUpload.template = '<div id="template" class="col-6 file-row"><div><span class="preview"><img data-dz-thumbnail /></span></div><div>'
	            + '<p class="name" data-dz-name></p><strong class="error text-danger" data-dz-errormessage></strong></div>'
				+ '<span><p class="size" data-dz-size></p>'
	            // + '<div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0">'
	            // + '<div class="progress-bar progress-bar-success" style="width:0%;" data-dz-uploadprogress></div></div>'
	            + '</span></div><div class="clearfix"></div>';
	        	
		Dropzone.autoDiscover = false;
			const context = this;
			$.ajax({
			  type : "GET",
			  async : false,
			  url : contextPath+"/api/fileconfig-details",
			  data : {
			  	fileBinId : options.fileBinId
			  }, 
			  success : function(data) {
				$(context).dropzone({
				  url: contextPath+"/cf/m-upload",
				  paramName: "files",
				  clickable: true,
				  maxFilesize: fileUpload.getByteSize(data["max_file_size"]),
				  uploadMultiple: true, 
				  maxFiles: Number.parseInt(data["no_of_files"]),
		  		  acceptedFiles: data["file_type_supported"],
		  		  createImageThumbnails: false,
		  		  // previewTemplate: fileUpload.template,
		  		  previewsContainer: $(context).find(".filepreviewcontainer")[0],
	  			  // clickable: ".start-upload",
		  		  success: function(data) {
		  		  	let fileObj = new Object();
		  		  	fileObj.name = data.name;
		  		  	fileObj.id = JSON.parse(data.xhr.response)["fileIds"][0];
		  		  	showMessage("File uploaded successfully", "success");
		  		  	console.log(JSON.parse(data.xhr.response)["fileIds"]);
		  		  	var viewButton = Dropzone.createElement("<i class='fileupload-actions fa fa-download float-right'></i>");
		  		  	let fileId = JSON.parse(data.xhr.response)["fileIds"][0];
		  		  	viewButton.addEventListener("click", function(e) {
		  		  		fileUpload.viewFileEvent(e, fileId)
		  		  	});
		  		  	data.previewElement.appendChild(viewButton);
		  		  	if(options.successcallback !== undefined) {
		  		  		options.successcallback(fileId);
		  		  	}
		  		  	if(typeof  fileUpload.options.renderer !== undefined && typeof  fileUpload.options.renderer === "function"){
		  		  		let cutomButton =  fileUpload.options.renderer(fileObj);	  	
		  		  		cutomButton = Dropzone.createElement(cutomButton);
		  		  		data.previewElement.appendChild(cutomButton);	  	
		  		  	} 
		  		  },
		  		  error: function(data, errorMessage, xhr){
			  		if(xhr != undefined && xhr.status == 403){
		  		  		showMessage("You don't have enough privilege to access this module", "error");
			  		}else{
			  			showMessage(errorMessage, "error");
			  		}
		  		  	fileUpload.removeUploadedFile(new Event("remove"), this, data);
		  		  },
				  init: function() {
				  fileUpload.dropzone = this;
				  this.on("sending", function (file, xhr, formData) {
			            formData.append("fileBinId", options.fileBinId);
			            formData.append("fileAssociationId", options.fileAssociationId);
			        });
			      this.on("addedfile", function(file) {
				        let removeButton = Dropzone.createElement("<i class='fileupload-actions fa fa-close float-left'></i>");
				        let _this = this;
				        removeButton.addEventListener("click", function(e) {
				        	fileUpload.removeFileEvent(e, _this, file)
				        });
				        file.previewElement.appendChild(removeButton);
				    });

				      fileUpload.showSelectedFiles(selectedFiles, this, fileUpload.options.renderer);
				   	}
				});
			  },
			  error : function(xhr, error){
				showMessage("Error occurred uploading file", "error");
			  }, 
		 	});
		 	return fileUpload;
		}else{
			return fileUpload;
		}
	}
	
}(jQuery));
