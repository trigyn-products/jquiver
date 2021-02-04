(function($) {

	class FileUpload {
		constuctor(element, options, selectedFiles){
			this.element = element;
			this.options = options;
			this.selectedFiles = [];
			this.uploaded = false;
			this.dropzone = [];
		}
		
		getSelectedFiles() {
			return this.dropzone.getAcceptedFiles().map(file => 
 				file.id != undefined ? file.id : JSON.parse(file.xhr.response).fileIds[0]);
		}
		
		deleteFileFromServerById(fileId) {
			$.ajax({
			  type : "DELETE",
			  async : false,
			  url : "/cf/files/" + fileId,
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
			location.href = "/cf/files/"+fileId;
		}
		
		deleteFileById(fileId) {
			let index = this.dropzone.getAcceptedFiles().map(file => 
 				file.id != undefined ? file.id : JSON.parse(file.xhr.response).fileIds[0]).indexOf(fileId);
 			let file = this.dropzone.getAcceptedFiles()[index];
 			this.removeFileEvent(new Event("remove"), this.dropzone, file)
		}
		
		showSelectedFiles(fileIds) {
			let dropzone = this.dropzone;
			let context = this;
			if(fileIds != undefined && fileIds.length > 0) {
				$.ajax({
					  type : "GET",
					  async : false,
					  url : "/cf/fileDetails",
					  data : {
					  	files : JSON.stringify(fileIds)
					  }, 
					  success: function(data) {
			  		  	for(var iCounter = 0; iCounter < data.length; ++iCounter) {
			  		  		let file = { name: data[iCounter]["fileName"], size: data[iCounter]["sizeInBytes"], id: data[iCounter]["fileId"], accepted: true};
							dropzone.emit("addedfile", file);
							dropzone.emit("complete", file);
		       				dropzone.files.push(file);
							var viewButton = Dropzone.createElement("<i class='fileupload-actions fa fa-eye float-right'></i>");
						    let fileId = data[iCounter]["fileId"];
	  		  				viewButton.addEventListener("click", function(e) {
	  		  					context.viewFileEvent(e, fileId)
	  		  				});
						    file.previewElement.appendChild(viewButton);
			  		  	}
			  		  }	
			  });
			}
		}
		
		removeFileEvent(event, _this, file) {
			event.preventDefault();
		    event.stopPropagation();
		  	_this.removeFile(file);
		  	let fileId = file.id;
		  	if(fileId == undefined){
		  		fileId = JSON.parse(file.xhr.response)["fileIds"][0];
		  	}
		  	if(this.options.deletecallback != undefined) {
		  		this.options.deletecallback(fileId);
		  	}
		    // If you want to the delete the file on the server as well,
		    // you can do the AJAX request here.
		}
	}
	
	$.fn.fileUpload = function(options, selectedFiles) {
	let fileUpload = new FileUpload(this, options, selectedFiles);
	fileUpload.options = options;
	
	$(this).wrap('<div class="pg-form-dropzone"><div class="row"><div class="col-12 dropzone-wrapper"><div class="cm-dropzone-wrap"></div></div></div></div>');
	
	let labelDiv = $('<label for="fileupload" class="dropzone-container dz-default dz-message"><div class="cm-uploadwrap clearfix"></div></label><div class="filepreviewcontainer cm-scrollbar"></div>');
	$(this).append(labelDiv);
	$(this).find(".cm-uploadwrap").append($('<div class="cm-uploadicon"><img src="/webjars/1.0/images/multifileupload.svg"></div><div class="dropzone-title">Drag and drop your files or <span class="browse">browse</span> your files</div>'));
	// $(this).append($('<label for="fileupload" class="dropzone-container dz-default dz-message"><div class="cm-uploadwrap clearfix"><div class="cm-uploadicon"><img src="/webjars/1.0/images/multifileupload.svg"></div><div class="dropzone-title">Drag and drop your files or <span class="browse">browse</span> your files</div></div></label>'));
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
		  url : "/api/fileconfig-details",
		  data : {
		  	fileUploadId : options.fileUploadId
		  }, 
		  success : function(data) {
			$(context).dropzone({
			  url: "/cf/m-upload",
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
	  		  	showMessage("File uploaded successfully", "success");
	  		  	console.log(JSON.parse(data.xhr.response)["fileIds"]);
	  		  	var viewButton = Dropzone.createElement("<i class='fileupload-actions fa fa-eye float-right'></i>");
	  		  	let fileId = JSON.parse(data.xhr.response)["fileIds"][0];
	  		  	viewButton.addEventListener("click", function(e) {
	  		  		fileUpload.viewFileEvent(e, fileId)
	  		  	});
	  		  	data.previewElement.appendChild(viewButton);
	  		  	if(options.successcallback !== undefined) {
	  		  		options.successcallback(fileId);
	  		  	}
	  		  },
	  		  error: function(data, errorMessage, xhr){
	  		  	showMessage(errorMessage, "error");
	  		  	fileUpload.removeFileEvent(new Event("remove"), this, data);
	  		  },
			  init: function() {
			  fileUpload.dropzone = this;
			  this.on("sending", function (file, xhr, formData) {
		            formData.append("fileConfigData", options.fileUploadId);
		        });
		      this.on("addedfile", function(file) {
			        var removeButton = Dropzone.createElement("<i class='fileupload-actions fa fa-close float-left'></i>");
			        var _this = this;
			        removeButton.addEventListener("click", function(e) {
			        	fileUpload.removeFileEvent(e, _this, file)
			        });
			        file.previewElement.appendChild(removeButton);
			      });
			      fileUpload.showSelectedFiles(selectedFiles, this);
			   	}
			});
		  },
		  error : function(xhr, error){
			showMessage("Error occurred uploading file", "error");
		  }, 
	 	});
	 	return fileUpload;
	}
	
	
}(jQuery));
