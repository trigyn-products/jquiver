(function($) {
	class FileUpload {
		constuctor(element, options, selectedFiles) {
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
				type: "DELETE",
				async: false,
				url: contextPath + "/cf/files/" + fileId,
				success: function(data) {
					//console.log("success");
				}
			});
		}

		getByteSize(byteSize) {
			byteSize = Number.parseInt(byteSize);
			return byteSize / (1024 * 1024);
		}

		viewFileEvent(event, fileId) {
			window.open(contextPath + "/cf/files/" + fileId, '_blank');
		}

		deleteFileById(fileId) {
			let index = this.dropzone.getAcceptedFiles().map(file =>
				file.id != undefined ? file.id : JSON.parse(file.xhr.response).fileIds[0]).indexOf(fileId);
			var file = "";
			if (index != -1)
				file = this.dropzone.getAcceptedFiles()[index];
			else {
				if (getCookie("fileBinDetails") != "" && getCookie("fileBinDetails") != undefined) {
					let fileBinJsonArrTemp = JSON.parse(getCookie("fileBinDetails"));
					$.each(fileBinJsonArrTemp, function(key, cookieData) {
						if (cookieData.id != undefined && cookieData.id == fileId) {
							file = { name: cookieData.name, size: cookieData.size, id: cookieData.id, accepted: true };
						}
					});
				}
			}
			this.removeFileEvent(new Event("remove"), this.dropzone, file)
		}
		
		showSelectedFiles(fileIds, dropZoneObj, fileUploadObj) {
			let dropzone = this.dropzone;
			let context = this;
			if (fileIds != undefined && fileIds.length > 0) {
				$.ajax({
					type: "GET",
					async: false,
					url: contextPath + "/cf/fileDetails",
					data: {
						fileUploadIds: JSON.stringify(fileIds)
					},
					success: function(data) {
						for (let iCounter = 0; iCounter < data.length; ++iCounter) {
							let file = { name: data[iCounter]["fileName"], size: data[iCounter]["sizeInBytes"], id: data[iCounter]["fileId"], accepted: true };
							let options = this.options;
							appendFileToUI(context, dropzone, options, file);
						}
					}, error: function(data, errorMessage, xhr) {
						let customErrorMessage;
						if (data.status == 403) { 
							customErrorMessage = fileBinDisplayTexts["jws.enoughAccess"];
						}
						let spanElement = $("<span class='fa fa-ban' id='dropZoneErrorMessage'></span>")
						$(".filepreviewcontainer").html(spanElement)
						$("#dropZoneErrorMessage").text(customErrorMessage);
					},
				});
			}
		}

		/**
		  * This method is to save file informations w.r.t All associations.
		  */
		saveAllFiles() {
			var returnMessage = "Success";
			if (this.options.fileBinId != undefined) {
				let options = this.options;
				if (options.fileBinId != undefined) {
					$.ajax({
						type: "POST",
						async: false,
						url: contextPath + "/cf/smfa-all",
						data: {
							fileBinId: options.fileBinId
						},
						success: function(data) {
							// below code is used to prepare cookieFileUploadIds stored in cookie and passed to clear on load.
							if (getCookie("fileBinDetails") != "" && getCookie("fileBinDetails") != undefined) {
								let fileBinJsonArrTemp = JSON.parse(getCookie("fileBinDetails"));
								if (fileBinJsonArrTemp != undefined) {
									$.each(fileBinJsonArrTemp, function(key, cookieData) {
										deleteFileUploadTempIdCookie(data.id);
									});
								}
							}
							showMessage("File saved successfully", "success");
						}, error: function(data, errorMessage, xhr) {
							returnMessage = "Fail";
							let customErrorMessage;
							if (data.status == 403) {
								customErrorMessage = fileBinDisplayTexts["jws.enoughAccess"];
							}
							let spanElement = $("<span class='fa fa-ban' id='dropZoneErrorMessage'></span>")
							$(".filepreviewcontainer").html(spanElement)
							$("#dropZoneErrorMessage").text(customErrorMessage);
						},
					});
				}
			}
			return returnMessage;
		}

		/**
		   * This method is to save file w.r.t selected association.
		   */
		//saveSelectedFiles(a_filesBin, formName) {
		saveSelectedFiles() {
			let l_formName = "addEditForm";
			let a_filesBin = fileBins;
			var returnMessage = "Success";
			if (a_filesBin == null || a_filesBin == undefined) {
				showMessage(fileBinDisplayTexts["jws.fileBinMandatory"], "error");
				//return;
			}
			
			if (typeof getFormName == "function") {
				l_formName = getFormName();
			}
			let serializedForm = $("#" + l_formName).serializeArray();
			if (a_filesBin) {
				for (let fileBinCounter = 0; fileBinCounter < a_filesBin.length; fileBinCounter++) {
					serializedForm.push(a_filesBin[fileBinCounter]);
				}
			}
			let formData = new FormData();
			formData.append('formData', JSON.stringify(serializedForm.formatSerializedArray()));
			if (formData != null) {
				$.ajax({
					type: "POST",
					async: false,
					url: contextPath + "/cf/smfa",
					data: formData,
					processData: false,
					contentType: false,
					cache: false,
					dataType: "json",
					success: function(data) {
						// below code is used to prepare cookieFileUploadIds stored in cookie and passed to clear on load.
						if (getCookie("fileBinDetails") != "" && getCookie("fileBinDetails") != undefined) {
							let fileBinJsonArrTemp = JSON.parse(getCookie("fileBinDetails"));
							if (fileBinJsonArrTemp != undefined) {
								for (let fileBinCounter = 0; fileBinCounter < a_filesBin.length; fileBinCounter++) {
									$.each(fileBinJsonArrTemp, function(key, cookieData) {
										if (cookieData.fileBinId != undefined && cookieData.fileBinId == a_filesBin[fileBinCounter].FileBinID && cookieData.fileAssociationId != undefined && cookieData.fileAssociationId == a_filesBin[fileBinCounter].fileAssociationID && cookieData.fileUploadTempId != undefined && cookieData.fileUploadTempId == a_filesBin[fileBinCounter].fileUploadTempId) {
											deleteFileUploadTempIdCookie(cookieData.id);
										}
									});
								}
							}
						}
						showMessage(fileBinDisplayTexts["jws.fileSavedSuccess"], "success");
					},
					error: function(data, errorMessage, xhr) {
						let customErrorMessage;
						if (data.status == 403) {
							customErrorMessage = fileBinDisplayTexts["jws.enoughAccess"];
						}
						let spanElement = $("<span class='fa fa-ban' id='dropZoneErrorMessage'></span>")
						$(".filepreviewcontainer").html(spanElement)
						$("#dropZoneErrorMessage").text(customErrorMessage);
						returnMessage = "Fail";
					}

				});
			}
			return returnMessage;
		}

		loadSelectedFiles() {
			let dropzone = this.dropzone;
			let context = this;
			let options = this.options;
			let cookieFileUploadIds = [];
			let fileUploadTemps = [];
			if (options.fileBinId != undefined) {

				// below code is used to prepare cookieFileUploadIds stored in cookie and passed to clear on load.
				if (getCookie("fileBinDetails") != "" && getCookie("fileBinDetails") != undefined) {

					let fileBinJsonArrTemp = JSON.parse(getCookie("fileBinDetails"));
					if (fileBinJsonArrTemp != undefined) {
						$.each(fileBinJsonArrTemp, function(key, data) {
							if (data.fileUploadTempId != undefined && options.fileBinId != undefined && options.fileAssociationId != undefined && options.fileBinId == data.fileBinId && options.fileAssociationId == data.fileAssociationId) {
								fileUploadTemps.push(data.fileUploadTempId);
								cookieFileUploadIds.push(data.id);
							}
						});
					}
				}
				$.ajax({
					type: "GET",
					async: false,
					url: contextPath + "/cf/fdbbi",
					data: {
						fileBinId: options.fileBinId,
						fileAssociationId: options.fileAssociationId,
						fileUploadTempIds: fileUploadTemps.join(','),
						isEdit: options.isEdit
					},
					success: function(data) {
						for (let iCounter = 0; iCounter < data.length; ++iCounter) {
							if (data[iCounter]["warningMessage"] === "FOUND") {
								let file = { name: data[iCounter]["fileName"], size: data[iCounter]["sizeInBytes"], id: data[iCounter]["fileId"], accepted: true };
								appendFileToUI(context, dropzone, options, file);
							} else {
								showMessage(data[iCounter]["warningMessage"], "warn");
							}
						}
						//clearFileBinCookie();
						if (cookieFileUploadIds != "" && cookieFileUploadIds != undefined) {
							$.each(cookieFileUploadIds, function(key, cookieData) {
								deleteFileUploadTempIdCookie(cookieData);
							});
						}
						//append Files To UI using cookie stored values
						appendFileFromCookieToUI(options);
					}, error: function(data, errorMessage, xhr) {
						let customErrorMessage;
						if (data.status == 403) {
							customErrorMessage = fileBinDisplayTexts["jws.enoughAccess"];
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
			$("#deleteConfirmation").html(fileBinDisplayTexts["jws.deleteMessage"]);
			$("#deleteConfirmation").dialog({
				bgiframe: true,
				autoOpen: true,
				modal: true,
				closeOnEscape: true,
				draggable: true,
				resizable: false,
				dialogClass: "deletepopupblock",
				title: fileBinDisplayTexts["jws.deleteBtnText"],
				buttons: [{
					text: fileBinDisplayTexts["jws.cancel"],
					"class":'btn btn-secondary',
					click: function() {
						$(this).dialog("destroy");
						$(this).remove();
					},
				},
				{
					text: fileBinDisplayTexts["jws.deleteBtnText"],
					"class":'btn btn-primary',
					click: function() {
						$.ajax({
							type: "DELETE",
							url: contextPath + "/cf/files/" + fileUploadId,
							success: function(data) {
								event.preventDefault();
								event.stopPropagation();
								_this.removeFile(file);
								deleteFileUploadTempIdCookie(fileUploadId);
								let fileId = file.id;
								if (fileId == undefined) {
									fileId = JSON.parse(file.xhr.response)["fileIds"][0];
								}
								// Below code is commented to for after removing fileBinTempMap variable
								/*fileBinTempMap.forEach((values, keys)=>{
									for( var iFileCnt = 0; iFileCnt < values.length; iFileCnt++){ 
										if(values[iFileCnt].id==fileId){
											values.splice(iFileCnt, 1); 											
											fileBinTempMap.set(keys, values);
										}
									}  // End of inner for loop with values    					
								});*/
								if (fileUploadObj.options.deletecallback != undefined) {
									fileUploadObj.options.deletecallback(fileId);
								}
								fileBins[fileBins.length] = { "FileBinID": fileUploadObj.options.fileBinId, "fileAssociationID": fileUploadObj.options.fileAssociationId, "valueType": "fileBin", "fileUploadTempId": fileUploadId };
								$("#deleteConfirmation").dialog("destroy");
								$("#deleteConfirmation").remove();
								showMessage(fileBinDisplayTexts["jws.fileDeletedSucess"], "success");
							},
							error: function(xhr, error) {
								if (xhr.status == 403) {
									showMessage(fileBinDisplayTexts["jws.enoughPrivilege"], "error");
								} else {
									showMessage(fileBinDisplayTexts["jws.errorOccurDelete"], "error");
								}
								$("#deleteConfirmation").dialog("destroy");
								$("#deleteConfirmation").remove();
							},
						});

					}
				},
				],
				open: function(event, ui) {
					$('.ui-dialog-titlebar')
						.find('button').removeClass('ui-dialog-titlebar-close').addClass('ui-button ui-corner-all ui-widget ui-button-icon-only ui-dialog-titlebar-close')
						.prepend('<span class="ui-button-icon ui-icon ui-icon-closethick"></span>').append('<span class="ui-button-icon-space"></span>');
				}

			});
		}

		removeUploadedFile(event, _this, file) {
			event.preventDefault();
			event.stopPropagation();
			_this.removeFile(file);
		}

		getSelectedFiles() {
			let dropzone = this.dropzone;
			let context = this;
			let options = this.options;
			if (options.fileBinId != undefined) {
				if (dropzone != undefined) {
					dropzone.removeAllFiles(true);
				}
				// Display Options 0 - All (default), 1 - Persistant storage , 2 - cookie
				var displayOptions = options.displayOptions != undefined ? options.displayOptions : 0;
				//Display Options 2 : appendFileToUI using cookie stored values
				if (displayOptions == 0 || displayOptions == 2) {
					appendFileFromCookieToUI(options);
				}
				if (displayOptions == 0 || displayOptions == 1) {
					$.ajax({
						type: "GET",
						async: false,
						url: contextPath + "/cf/fdbbi",
						data: {
							fileBinId: options.fileBinId,
							fileAssociationId: options.fileAssociationId,
							isEdit: options.isEdit
						},
						success: function(data) {
							//append Files To UI without using cookies (From persistant table)
							if (dropzone != undefined) {
								//dropzone.removeAllFiles(true);
								//this is for saved files from server
								for (let iCounter = 0; iCounter < data.length; ++iCounter) {
									let file = { name: data[iCounter]["fileName"], size: data[iCounter]["sizeInBytes"], id: data[iCounter]["fileId"], accepted: true };
									appendFileToUI(context, dropzone, options, file);
								}//end of for loop	
							}

						}, error: function(data, errorMessage, xhr) {
							if (data != undefined && data.status == 403) {
								showMessage(fileBinDisplayTexts["jws.enoughAccess"], "error");
							} else {
								showMessage(errorMessage, "error");
							}
						},
					});
				}
			}
		}

		disableDropZone(message) {
			this.dropzone.disable();
			//$(this.dropzone.element).find(".dropzone-title").html(message);
			//$(this.dropzone.element).find(".cm-uploadicon").addClass("dropzone-disable-img-cls");
			//$(this.dropzone.element).find(".dropzone-title").addClass("dropzone-disable-message-cls");
		}

		enableDropZone(fileAssocId) {
			this.dropzone.enable();
			this.options.fileAssociationId = fileAssocId;
			//$(this.dropzone.element).find(".cm-uploadicon").removeClass("dropzone-disable-img-cls");
			//$(this.dropzone.element).find(".dropzone-title").removeClass("dropzone-disable-message-cls");
			//$(this.dropzone.element).find(".dropzone-title").html('Drag and drop your files or <span class="browse">browse</span> your files');
		}
	}

	function appendFileToUI(context, a_dropzone, a_options, a_fileObject) {
		a_dropzone.emit("addedfile", a_fileObject);
		a_dropzone.emit("complete", a_fileObject);
		a_dropzone.files.push(a_fileObject);
		let viewButton = Dropzone.createElement("<div class='fileicons'><span class='iconcovercls'><i class='fileupload-actions fa fa-download float-right' title='" + resourceBundleData("jws.viewFile") + "'></i></span></div>");
		let fileId = a_fileObject["id"];
		viewButton.addEventListener("click", function(event) {
			context.viewFileEvent(event, fileId);
		});
		a_fileObject.previewElement.appendChild(viewButton);
		if (typeof a_options.renderer !== undefined && typeof a_options.renderer === "function") {
			let cutomButton = a_options.renderer(a_fileObject);
			cutomButton = Dropzone.createElement(cutomButton);
			a_fileObject.previewElement.appendChild(cutomButton);
		}
	}

	function appendFileFromCookieToUI(a_options) {
		if (getCookie("fileBinDetails") != "" && getCookie("fileBinDetails") != undefined) {
			let fileBinJsonArrTemp = JSON.parse(getCookie("fileBinDetails"));
			$.each(fileBinJsonArrTemp, function(key, cookieData) {
				if (cookieData.fileUploadTempId = !undefined && cookieData.fileAssociationId != undefined && a_options.fileBinId == cookieData.fileBinId && a_options.fileAssociationId == cookieData.fileAssociationId) {
					let file = { name: cookieData.name, size: cookieData.size, id: cookieData.id, accepted: true };
					if (typeof a_options.renderer !== undefined && typeof a_options.renderer === "function") {
						a_options.renderer(file);
					}
				}
			});
		}
	}

	/**
	 * This is to support multiple file association id for one file bin.
	 * In case there are multiple bins in one page and each bin have 
	 * multiple association, this method will handle the situation.
	 */
	function appendAssociationID(options, fileAssocId) {
		var hasFileBinEntry = false;
		if (fileBins != undefined && fileBins != null) {
			for (let fileBinCounter = 0; fileBinCounter < fileBins.length; fileBinCounter++) {
				if (fileBins[fileBinCounter]["FileBinID"] == options.fileBinId
					&& fileBins[fileBinCounter]["fileUploadTempId"] == options.fileUploadTempId
					&& fileBins[fileBinCounter]["fileAssociationID"] == fileAssocId) {
					hasFileBinEntry = true;
					break;
				}
			}
			if (hasFileBinEntry == false && options.fileUploadTempId != undefined) {
				fileBins[fileBins.length] = { "FileBinID": options.fileBinId, "fileAssociationID": options.fileAssociationId, "valueType": "fileBin", "fileUploadTempId": options.fileUploadTempId };
			}
		}
	}
	//this is to hold temp file in memory
	//
	$.fn.fileUpload = function(options, selectedFiles) {
		let fileUpload = new FileUpload(this, options, selectedFiles);
		if(fileBinDisplayTexts == null){
				 fileBinDisplayTexts = resourceBundleData("jws.enoughAccess,jws.enoughPrivilege,jws.deleteMessage,jws.deleteBtnText,jws.cancel,jws.fileDeletedSucess,jws.errorOccurDelete,jws.fileBinMandatory,jws.fileSavedSuccess,jws.fileUploaded,jws.click,jws.pasteHere,jws.browse,jws.dragAndDrop,jws.fileAlreadyExist,jws.duplicate,jws.replace,jws.confirm");
			}
		let fileUploadId = "";
		fileUpload.options = options;

		if (options.isEdit === undefined) {
			options.isEdit = true;
		}

		if (options.isEdit == false) {
			this.disableDropZone("You can't edit");
		}

		//if(fileBins != undefined && fileBins != null){
		//fileBins[fileBins.length] = { "FileBinID": options.fileBinId, "fileAssociationID": options.fileAssociationId, "valueType": "fileBin" };
		//}
		// tabindex is added to to get the the focus of the paste div
		var tabindex = -1;
		if (options.createFileBin !== undefined && options.createFileBin === true) {
			tabindex += 1;
			let tabindexText = tabindex.toString();
			$(this).wrap('<div class="pg-form-dropzone"><div class="row"><div class="col-12 dropzone-wrapper"><div class="cm-dropzone-wrap"></div></div></div></div>');
			// Added new div for paste section
			let copyDiv = $('<div tabindex="' + tabindexText.trim() + '" contenteditable="true" class="copyblock dropzone-title"><div class="pastecontent"><img  src="' + contextPath + '/webjars/1.0/images/copy.png"><Strong>' +fileBinDisplayTexts["jws.click"]+ '</strong>'+ fileBinDisplayTexts["jws.pasteHere"] +'</div></div>');
			let labelDiv = $('<label for="fileupload" class="dropzone-container dz-default dz-message"><div class="cm-uploadwrap clearfix"></div></label><div class="filepreviewcontainer cm-scrollbar"></div>');
			$(this).append(labelDiv);
			$(this).find(".cm-uploadwrap").append($('<div class="dropzone-title"><img src="' + contextPath + '/webjars/1.0/images/multifileupload.svg"><strong class="browse">' +fileBinDisplayTexts["jws.browse"]+ ' </strong> ' +fileBinDisplayTexts["jws.dragAndDrop"]+ '</div>'));
			// $(this).append($('<label for="fileupload" class="dropzone-container dz-default dz-message"><div class="cm-uploadwrap clearfix"><div class="cm-uploadicon"><img src="'+contextPath+'/webjars/1.0/images/multifileupload.svg"></div><div class="dropzone-title">Drag and drop your files or <span class="browse">browse</span> your files</div></div></label>'));
			/** Customizing the dropzone view **/
			$(this).find(".dropzone-container").after(copyDiv);
			fileUpload.template = '<div id="template" class="col-6 file-row"><div><span class="preview"><img data-dz-thumbnail /></span></div><div>'
				+ '<p class="name" data-dz-name></p><strong class="error text-danger" data-dz-errormessage></strong></div>'
				+ '<span><p class="size" data-dz-size></p>'
				// + '<div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0">'
				// + '<div class="progress-bar progress-bar-success" style="width:0%;" data-dz-uploadprogress></div></div>'
				+ '</span></div><div class="clearfix"></div>';

			Dropzone.autoDiscover = false;
			const context = this;
			var isReplaceExistingFile = "false";
			var existingFileId = "";
			$.ajax({
				type: "GET",
				async: false,
				url: contextPath + "/api/fileconfig-details",
				data: {
					fileBinId: options.fileBinId
				},
				success: function(data) {
					if (data["file_bin_id"] != options.fileBinId) {
						showMessage("Invalid filebin.", "error");
						if (this.dropzone != undefined) {
							this.dropzone.disableDropZone("Invalid filebin.");
							$(".fileupload").prop('disabled', 'disabled');
						}
						return false;
					}
					$(context).dropzone({
						url: contextPath + "/cf/m-upload",
						paramName: "files",
						clickable: true,
						maxFilesize: fileUpload.getByteSize(data["max_file_size"]).toFixed(2),
						uploadMultiple: true,
						maxFiles: Number.parseInt(data["no_of_files"]),
						acceptedFiles: data["file_type_supported"],
						createImageThumbnails: false,
						// previewTemplate: fileUpload.template,
						previewsContainer: $(context).find(".filepreviewcontainer")[0],
						// clickable: ".start-upload",
						success: function(data) {
							isReplaceExistingFile = "false";
							var tempDetais = JSON.parse(data.xhr.response)["updateTempDetails"];
							let fileObj = new Object();
							fileObj.name = data.name;
							fileObj.id = JSON.parse(data.xhr.response)["fileIds"][0];
							options.fileUploadTempId = Object.values(tempDetais)[0];
							fileObj.fileUploadTempId = Object.values(tempDetais)[0];
							fileObj.size = data.upload.total;

							updateFileBinCookie(options, fileObj);
							showMessage(fileBinDisplayTexts["jws.fileUploaded"], "success");

							let viewButton = Dropzone.createElement("<div class='fileicons'><span class='iconcovercls'><i class='fileupload-actions float-right' title='" + resourceBundleData("jws.viewFile") + "'></i></span></div>");
							let fileId = JSON.parse(data.xhr.response)["fileIds"][0];
							viewButton.addEventListener("click", function(e) {
								fileUpload.viewFileEvent(e, fileId)
							});
							data.previewElement.appendChild(viewButton);
							if (options.successcallback !== undefined) {
								options.successcallback(fileId);
							}
							if (typeof fileUpload.options.renderer !== undefined && typeof fileUpload.options.renderer === "function") {
								let cutomButton = fileUpload.options.renderer(fileObj);
								cutomButton = Dropzone.createElement(cutomButton);
								data.previewElement.appendChild(cutomButton);
							}
						},
						error: function(data, errorMessage, xhr) {
							if (xhr != undefined && xhr.status == 403) {
								showMessage(fileBinDisplayTexts["jws.enoughAccess"], "warn");
							} else {
								showMessage(errorMessage, "error");
							}
							fileUpload.removeUploadedFile(new Event("remove"), this, data);
							$("#confirm").dialog('destroy').remove();
						},
						init: function() {
							fileUpload.dropzone = this;
							this.on("sending", function(file, xhr, formData) {
								formData.append("fileBinId", options.fileBinId);
								formData.append("fileAssociationId", options.fileAssociationId);
								if (isReplaceExistingFile != undefined) {
									formData.append("isReplaceExistingFile", isReplaceExistingFile);
								}
								if (existingFileId != undefined) {
									formData.append("existingFileId", existingFileId);
								}

							});

							if ((fileUpload.options.renderer instanceof Function) == false) {
								this.on("addedfile", function(file) {
									let removeButton = Dropzone.createElement("<div class='fileicons'><span class='iconcovercls'><i class='fileupload-actions fa fa-trash float-left'  title='" + resourceBundleData("jws.deleteFile") + "'></i></span></div>");
									let _this = this;
									removeButton.addEventListener("click", function(e) {
										fileUpload.removeFileEvent(e, _this, file)
									});
									file.previewElement.appendChild(removeButton);
								});
							}
							fileUpload.dropzone.on("addedfile", function(file) {
								isUploadValid = false;
								isReplaceExistingFile = "false";
								if (this.files.length) {
									var fileCount = this.files.length;
									if (fileCount > 1 && document.readyState === 'complete') {
										for (iFileCounter = 0, iFileLen = this.files.length; iFileCounter < iFileLen - 1; iFileCounter++) {
											if (this.files[iFileCounter].name.toUpperCase() === file.name.toUpperCase()) {
												fileUpload.dropzone.options.autoProcessQueue = false;
												var existFileRef = this.files[iFileCounter];
												fileUploadId = existFileRef.id;
												// Below code is used when we have to delete the file by temp id
												if (fileUploadId == undefined) {
													fileUploadId = getFileUploadIdUsingCookie(file.name.toUpperCase());
												}
												$("<div id='confirm' class='hide'>"+ fileBinDisplayTexts['jws.fileAlreadyExist'] +"</div>").
													dialog({
														bgiframe: true,
														autoOpen: true,
														modal: true,
														draggable: true,
														resizable: false,
														title: fileBinDisplayTexts["jws.confirm"],
														position: {
															my: "center", at: "center"
														},
														buttons: [{
															text: fileBinDisplayTexts["jws.duplicate"],
															click: function() {
																isProcessQueue = true;
																file.status = "queued";
																fileUpload.dropzone.processQueue();
																$(this).dialog('close');
															}
														}, {
															text: fileBinDisplayTexts["jws.replace"],
															click: function() {

																isReplaceExistingFile = "true";
																existingFileId = fileUploadId;
																fileUpload.dropzone.removeFile(existFileRef);
																file.status = "queued";
																fileUpload.dropzone.processQueue();
																$(this).dialog('close');
															},
														}, {
															text: fileBinDisplayTexts["jws.cancel"],
															click: function() {
																isProcessQueue = false;
																fileUpload.dropzone.removeFile(file);
																$(this).dialog('close');
															},
														},

														],
														open: function(event, ui) {
															$('.ui-dialog-titlebar')
																.find('button').removeClass('ui-dialog-titlebar-close').addClass('ui-button ui-corner-all ui-widget ui-button-icon-only ui-dialog-titlebar-close')
																.prepend('<span class="ui-button-icon ui-icon ui-icon-closethick"></span>').append('<span class="ui-button-icon-space"></span>');
														}
													});

											}
										}

									}

								}

							});

							fileUpload.showSelectedFiles(this.selectedFiles, this, fileUpload.options.renderer);
							fileUpload.loadSelectedFiles();
						}
					});
				},
				error: function(xhr, error) {
					showMessage("Error occurred uploading file", "error");
				},
			});
			return fileUpload;
		} else {
			return fileUpload;
		}
	}

	/*
	 * Method 		: deleteFileUploadTempIdFromCookie
	 * Description 	: This method is used to delete the id from cookie
	 */
	function deleteFileUploadTempIdCookie(fileUploadId) {
		if (getCookie("fileBinDetails") != "" && getCookie("fileBinDetails") != undefined) {
			let fileBinJsonLocalArr = JSON.parse(getCookie("fileBinDetails"));
			let tempFileBinJsonArr = [];
			// iterate over fileBinJsonArr cookie array values
			if (fileBinJsonLocalArr.length > 0) {
				$.each(fileBinJsonLocalArr, function(key, data) {
					if (data.id != fileUploadId) {
						tempFileBinJsonArr.push(data);
					}
				});

				if (tempFileBinJsonArr != undefined) {
					setCookie("fileBinDetails", JSON.stringify(tempFileBinJsonArr), 1);
				}
				let fileBinJsonArr1 = JSON.parse(getCookie("fileBinDetails"));
			}

		}
	}

	function getFileUploadIdUsingCookie(name) {
		let id = "";
		if (getCookie("fileBinDetails") != "" && getCookie("fileBinDetails") != undefined) {
			let fileBinJsonLocalArr = JSON.parse(getCookie("fileBinDetails"));
			if (fileBinJsonLocalArr.length > 0) {
				$.each(fileBinJsonLocalArr, function(key, cookieData) {
					if (cookieData.name.toUpperCase() === name.toUpperCase()) {
						id = cookieData.id;
						return false;
					}
				});
			}
		}

		return id;
	}

	/*
	 * method 		: clearFileBinCookie
	 * Description 	: This method is used to clear all the temp id's from the cookie
	 */
	function clearFileBinCookie() {
		// clearing the cookie on load and resetting the fileBinTempMap to new value
		if (getCookie("fileBinDetails") != undefined) {
			deleteCookie('fileBinDetails');
		}
	}

	/*
	 * Method 		: updateFileBinCookie
	 * Description 	: This method is used to add the temp id to the cookie
	 */
	function updateFileBinCookie(options, fileObj) {
		var filebinJson = {
			"name": fileObj.name,
			"id": fileObj.id,
			"size": fileObj.size,
			"fileBinId": options.fileBinId,
			"fileAssociationId": options.fileAssociationId,
			"fileUploadTempId": options.fileUploadTempId
		}
		let fileBinJsonLocalArr = [];
		if (getCookie("fileBinDetails") != "" && getCookie("fileBinDetails") != undefined) {
			fileBinJsonLocalArr = JSON.parse(getCookie("fileBinDetails"));
		}
		if (filebinJson != undefined) {
			fileBinJsonLocalArr.push(filebinJson);
		}
		setCookie("fileBinDetails", JSON.stringify(fileBinJsonLocalArr), 1);
		appendAssociationID(options, options.fileAssociationId);
	}

}(jQuery));
// Added new  section to paste section
// It will support multiple dropzone on a single form
$(document).ready(function() {
	$(".fileupload").on("paste", function(pasteEevent) {
		var isFirefox = (navigator.userAgent.indexOf('Firefox') !== -1);
		if (isFirefox) {
			$("<div id='confirm' class='hide'>Copy paste file from Clipboard is not supported. If you are trying to upload image, then take a snapshot or browse.</div>").
				dialog({
					bgiframe: true,
					autoOpen: true,
					modal: true,
					draggable: true,
					resizable: false,
					closeOnEscape: true,
					title: "Alert",
					position: {
						my: "center", at: "center"
					},
					buttons: [{
						text: "Ok",
						click: function() {
							$(this).dialog('close');
						}
					}

					],
					open: function(event, ui) {
						$('.ui-dialog-titlebar')
							.find('button').removeClass('ui-dialog-titlebar-close').addClass('ui-button ui-corner-all ui-widget ui-button-icon-only ui-dialog-titlebar-close')
							.prepend('<span class="ui-button-icon ui-icon ui-icon-closethick"></span>').append('<span class="ui-button-icon-space"></span>');
					}
				});
		}
		var fileUploadElement = $(this);
		pasteEevent.preventDefault();
		var dropzoneEleId = fileUploadElement.attr('id');
		if (dropzoneEleId != undefined) {
			var items = (pasteEevent.clipboardData || pasteEevent.originalEvent.clipboardData).items;
			for (index in items) {
				var item = items[index];
				if (item.kind === 'file') {
					$('#' + dropzoneEleId).get(0).dropzone.addFile(item.getAsFile());
				}
			}
		}
	});
});
