(function($) {

	class FileUpload {
		constuctor(element, options, selectedFiles){
			this.element = element;
			this.options = options;
			this.selectedFiles = [];
			this.uploaded = false;
			this.uploadedFilesId = new Array();
			this.dropzone = [];
		}
		
		getSelectedFiles() {
			return this.uploadedFilesId;
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
							var removeButton = Dropzone.createElement("<i class='fileupload-actions fa fa-trash float-left'></i>");
							var viewButton = Dropzone.createElement("<i class='fileupload-actions fa fa-eye float-right'></i>");
						    removeButton.addEventListener("click", function(e) {context.removeFileEvent(e, dropzone, file)});
						    let fileId = data[iCounter]["fileId"];
	  		  				viewButton.addEventListener("click", function(e) {context.viewFileEvent(e, fileId)});
						    file.previewElement.appendChild(removeButton);
						    file.previewElement.appendChild(viewButton);
							context.uploadedFilesId.push(data[iCounter]["fileId"]);
			  		  	}
			  		  }	
			  });
			}
		}
		
		removeFileEvent(event, _this, file) {
			event.preventDefault();
		    event.stopPropagation();
		  	_this.removeFile(file);
		  	console.log(file);
		  	let fileId = file.id;
		  	if(fileId == undefined){
		  		fileId = JSON.parse(data.xhr.response)["fileIds"][0];
		  	}
		  	this.uploadedFilesId.splice(this.uploadedFilesId.indexOf(fileId), 1);
		    // If you want to the delete the file on the server as well,
		    // you can do the AJAX request here.
		}
	}
	
	$.fn.fileUpload = function(options, selectedFiles) {
	let fileUpload = new FileUpload(this, options, selectedFiles);
	fileUpload.uploadedFilesId = new Array();
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
			  maxFiles: data["allow_multiple_files"],
	  		  acceptedFiles: data["file_type_supported"],
	  		  createImageThumbnails: false,
	  		  success: function(data) {
	  		  	showMessage("File uploaded successfully", "success");
	  		  	console.log(JSON.parse(data.xhr.response)["fileIds"]);
	  		  	fileUpload.uploadedFilesId.concat(JSON.parse(data.xhr.response)["fileIds"]);
	  		  	var viewButton = Dropzone.createElement("<i class='fileupload-actions fa fa-eye float-right'></i>");
	  		  	let fileId = JSON.parse(data.xhr.response)["fileIds"][0];
	  		  	viewButton.addEventListener("click", function(e) {fileUpload.viewFileEvent(e, fileId)});
	  		  	data.previewElement.appendChild(viewButton);
	  		  	if(options.successcallback !== undefined) {
	  		  		options.successcallback.call();
	  		  	}
	  		  },
	  		  error: function(data, errorMessage, xhr){
	  		  	showMessage(errorMessage, "error");
	  		  },
			  init: function() {
			  fileUpload.dropzone = this;
			  fileUpload.showSelectedFiles(selectedFiles, this);
		      this.on("addedfile", function(file) {
			        var removeButton = Dropzone.createElement("<i class='fileupload-actions fa fa-trash float-left'></i>");
			        var _this = this;
			        removeButton.addEventListener("click", function(e) {fileUpload.removeFileEvent(e, _this, file)});
			        file.previewElement.appendChild(removeButton);
			      });
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
