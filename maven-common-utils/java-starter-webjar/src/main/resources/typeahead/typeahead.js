
(function($) {

    class TypeAhead {
        constructor(element, options) {
            this.element = element;
            this.options = options;
            this.selectedObject = [];
        }

        loadServerPages = function ( searchTerm, pageNumber, pageSize) {
            const context = this;
            var deferred = $.Deferred();
            var searchval = $.trim($(this.element).val());
            if(this.options.prefetch || searchval !== ""){
                $.ajax({
                    type: "POST",
                    url:  contextPath+"/cf/autocomplete-data",
                    data: { 
                            searchText: searchval,
                            autocompleteId : context.options.autocompleteId,
                            startIndex : pageNumber * pageSize,
                            pageSize : pageSize,
                            additionalParamaters: JSON.stringify(context.options.additionalParamaters),
                            requestParameters: JSON.stringify(context.options.requestParameters),
                        },
                    dataType: "json",
                    success: function (data) {
                        deferred.resolve(data);
                    },
                    error: function (xhr, error) {
                        throw Error("Error while loading the data for typeahead " + error);
                    }
                });
			} else if(searchval == "") {
                var emptyMsgArray = new Array();
                if(pageNumber < 1) {
                    var emptyMsg = new Object();
                    emptyMsg.emptyMsg = "<p class='emptyInfoMessage'>Please type to search</p>"
                    emptyMsgArray.push(emptyMsg);
                    deferred.resolve(emptyMsgArray);
                }
                else {
                    deferred.resolve(emptyMsgArray);
                }
            }
            return deferred.promise();
        }
        
        getSelectedObject = function(){
        	return this.selectedObject;
        }
        
        setSelectedObject = function(list){
        	return this.selectedObject = list;
        }
    }

    class Autocomplete extends TypeAhead {
        constructor(element, options, selectedItem) {
            super(element, options);
            if(this.options.prefetch === undefined){
            	this.options.prefetch = false;
            }
            this.initWithValues(selectedItem);
            this.resetAutocompleteOnBlur();
        }
        
        initWithValues = function(item){
        	if(item !== undefined){
	        	this.selectedObject = item;
	        	let value = this.options.extractText(item);
	        	$(this.element).val(value);
        	}
        	this.init(this.options);
	        return this.selectedObject;
        }
        
        setSelectedObject = function(item){
        	if(item !== undefined){
	        	this.selectedObject = item;
	        	let value = this.options.extractText(item);
        		$(this.element).val(value);
        		$(this.element).keyup();
        	}
        }
        
        resetAutocomplete = function(){
        	this.selectedObject = {};
        	$(this.element).val("");
        	$(this.element).keyup();
        	if(typeof this.options.resetAutocomplete !== undefined && typeof this.options.resetAutocomplete === "function"){
        		this.options.resetAutocomplete(this);
        	}
        }
        
        resetAutocompleteOnBlur = function(){
            let context = this;
            $(this.element).blur(context, function(autocompleteObj){
            	let isSelected = false;
   				for(let key in autocompleteObj.data.selectedObject){
    				if(autocompleteObj.data.selectedObject[key] === $(autocompleteObj.data.element).val()){
    					isSelected = true;
    				}
				}
				if(isSelected == false){
					autocompleteObj.data.resetAutocomplete();
				}
			})
        }
        
    }

    Autocomplete.prototype.init = function(options) {
        const context = this;
        $(this.element).richAutocomplete({
            loadPage: function (searchTerm, pageNumber, pageSize) {
                return context.loadServerPages(searchTerm, pageNumber, pageSize);
            },
            paging: options.paging,
            filter: options.filter,
            items: options.items,
            pageSize: options.pageSize,
            emptyRender: options.emptyRender,
            select: function (item) {
				context.setSelectedObject(item);
				return options.select(item);
			},
            render: options.render,
            extractText: options.extractText,
            selectedObjectData: options.selectedObjectData
        });
        let placeholderVal = $.trim($(this.element).prop("placeholder"));
        if(placeholderVal === "" && this.options.prefetch === false){
        	$(this.element).prop("placeholder","Please type to search");
        }
        if(this.options.enableClearText === true){
        	let elementId =  $(this.element).attr("id");
        	let titleTxt = resourceBundleData("jws.clearTxt");
        	let clearTxt = $('<span id="'+elementId+'_clearTxt" class="autocomplete-clear-txt"  title="'+titleTxt["jws.clearTxt"]+'"><i class="fa fa-times" aria-hidden="true"></i></span>');
        	clearTxt.insertAfter(this.element);
        	$("#"+elementId+"_clearTxt").bind("click", function(){
				context.resetAutocomplete();
        	});
        }
        
    }

    class Multiselect extends TypeAhead {
    	selectedObjects = new Array();
        constructor(element, options, selectedItems) {
        	super(element, options);
        	const context = this;
        	let multiselectId = element.attr("id");
        	let multiselectClearDiv = $('<div class="multiselectcount_clear_block"></div>');
        	let multiselectRemoveDiv = $('<div id="'+multiselectId+'_removeAll" class="pull-right disable_cls"></div>');
        	let multiselectCountDiv = $('<div id="'+multiselectId+'_count" class="multiselectcount pull-right disable_cls"></div>');
        	if(options.enableClearAll === false){
        		multiselectRemoveDiv.append('<span id="'+multiselectId+'_removeAllSpan" class="clearall-disabled-cls">'+resourceBundleData("jws.clearAll")+'</span>');
        	}else{
        		multiselectRemoveDiv.append('<span id="'+multiselectId+'_removeAllSpan" class="clearall-cls" >'+resourceBundleData("jws.clearAll")+'</span>');
        	}
        	multiselectCountDiv.append('<span title="hide show" style="pointer-events:none">0</span>');
        	multiselectClearDiv.append(multiselectRemoveDiv).append(multiselectCountDiv);
        	$(element).parent().before(multiselectClearDiv);
        	$(element).parent().after('<div id="'+multiselectId+'_selectedOptions"></div>');
        	context.options.multiselectItem = $("#"+multiselectId+"_selectedOptions");
        	
        	$("#"+multiselectId+"_count").bind("click", function() {
        		context.showHideDataDiv(multiselectId+"_selectedOptions");
        	});
        	if(options.enableClearAll !== false){
	        	$("#"+multiselectId+"_removeAllSpan").bind("click", function() {
	        		context.removeAllElements(multiselectId);
	        	});
        	}
            
            if(this.options.prefetch === undefined){
            	this.options.prefetch = true;
            }
            this.init(this.options);
            this.initWithValues(selectedItems);
        }
        
        setSelectedObject = function(item){
        	Multiselect.prototype.createElementForMultiselect(this, this.element[0].id, item);
        	return this.selectedObject;
        }
        
        setSelectedObjectArray = function(item){
        	for(let iCounter = 0; iCounter < item.length; iCounter++) {
        		Multiselect.prototype.createElementForMultiselect(this, this.element[0].id, item[iCounter]);
        	}
        	return this.selectedObject;
        }
        
        initWithValues = function(items){
        	for(let iCounter = 0; iCounter < items.length; iCounter++) {
        		Multiselect.prototype.createElementForMultiselect(this, this.element[0].id, items[iCounter]);
        	}
        	return this.selectedObject;
        }
        
		resetDependent = function(componentArray){
			let componentIdArray = new Array();
			let dependentCompChanged = true;
			
			componentArray.forEach(function (component) {
    			let removeElem = multiselect.removeAllDependent(component.componentId, component.context);
				removeElem.done(function () {
    				dependentCompChanged = true;
				});

				removeElem.fail(function () {
    				dependentCompChanged = false;
				});
			});
			
        	return dependentCompChanged;
        }
        
        resetMultiselect = function(){
        	$(this.element).val("");
        	$(this.element).blur();
        	$(this.element).keyup();
        }
    }

    Multiselect.prototype.init = function(options) {
    const context = this;
        $(this.element).richAutocomplete({
            loadPage: function (searchTerm, pageNumber, pageSize) {
                return context.loadServerPages(searchTerm, pageNumber, pageSize);
            },
            paging: options.paging,
            pageSize: options.pageSize,
            filter: options.filter,
            items: options.items,
            emptyRender: options.emptyRender,
            render: options.render,
            extractText: options.extractText,
            select: options.select,
            selectedObjectData: options.selectedObjectData
        });
        let placeholderVal = $.trim($(this.element).prop("placeholder"));
        if(placeholderVal === "" && this.options.prefetch === false){
        	$(this.element).prop("placeholder","Please type text to search");
        }
        const maxHeight = 200;
        let multiselectId = options.multiselectItem[0].id;
        this.list = $('<div class="ml-selected-items-div"><ul id="'+multiselectId+'_ul" class="ml-selected-items-list"></ul></div>');
        this.list.css('max-height', maxHeight + 'px');
        this.list.show();
        options.multiselectItem.append(this.list)
    }

    Multiselect.prototype.createElementForMultiselect = function(context, multiselectId, itemData) {
        if(context.options.duplicateCheckRule(context.selectedObjects, itemData) == false) {
        	const elementId =  Object.keys(itemData).filter(key => (key.indexOf("Id") != -1));
        	context.selectedObject.push(itemData);
            const element = context.options.selectedItemRender(itemData);
            let listsElement = $("<li></li>");
            let itemSpan = $('<span id="'+itemData[elementId]+'" class="ml-selected-item">'+element+'</span>');
            let deleteItemContext = $('<span class="float-right closeicon"><i class="fa fa-times-circle-o" aria-hidden="true"></i></span>');
            deleteItemContext.data("selected-item", itemData);
    
            listsElement.append(itemSpan);
            listsElement.append(deleteItemContext);
            
            $("#"+multiselectId+"_selectedOptions_ul").append(listsElement);
            
            var deleteItem = function(event) {
                var data = $(deleteItemContext).data('selected-item');
                context.deleteItem.apply(deleteItemContext, [multiselectId, data, context]);
            };
            let noOfElements = parseInt($("#"+multiselectId+"_count > span").text());
    		$("#"+multiselectId+"_count > span").text(noOfElements+1);
    		$("#"+multiselectId+"_count").removeClass("disable_cls");
    		$("#"+multiselectId+"_count > span" ).css('pointer-events','auto');
    		$("#"+multiselectId+"_removeAll").removeClass("disable_cls");
    		$("#"+multiselectId+"_removeAll > span" ).css('pointer-events','auto');

            deleteItemContext.click(deleteItem);
    
            context.selectedObjects.push(itemData);
        }else{
        	showMessage("Data already present in the list", "info")
        }

        $(context.element).val("");
    }

    Multiselect.prototype.deleteItem = function(multiselectId, item, context) {
		let element = $(this);
		let selectedText = item.text;
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
					"class":'btn btn-secondary',
					click: function() { 
						$(this).dialog("destroy");
						$(this).remove();
					},
				},
				{
					text	: "Delete",
					"class":'btn btn-primary',
					click	: function(){
				        let noOfElements = parseInt($("#"+multiselectId+"_count > span").text()) - 1;
				    	$("#"+multiselectId+"_count > span").text(noOfElements);
				    	if(noOfElements === 0){
				    		$("#"+multiselectId+"_count").addClass("disable_cls");
				    		$("#"+multiselectId+"_count > span" ).css('pointer-events','none');
				    		$("#"+multiselectId+"_removeAll").addClass("disable_cls");
				    		$("#"+multiselectId+"_removeAll > span" ).css('pointer-events','none');
				    	}
				        $(element.parent()).remove();
						context.selectedObjects = $.grep(context.selectedObjects, function(savedObj, i) {
    						return savedObj.key !== item.key;
						});
						$(this).dialog("destroy");
						$(this).remove();
						showMessage("Deleted successfully.", "success");
				        return item;
					}
	           	},
	       ],
	       open		: function( event, ui ) {	    	
		   	   $('.ui-dialog-titlebar')
		   	    .find('button').removeClass('ui-dialog-titlebar-close').addClass('ui-button ui-corner-all ui-widget ui-button-icon-only ui-dialog-titlebar-close')
		       .prepend('<span class="ui-button-icon ui-icon ui-icon-closethick"></span>').append('<span class="ui-button-icon-space"></span>');
		   }	
	
		});
		
    },
    
    
    Multiselect.prototype.removeAllDependent = function(multiselectId, a_context){
		let deferredObject = $.Deferred();
		const context = a_context;

		let multiselectIdDivId = context.options.multiselectItem[0].id;
		context.selectedObjects = new Array();
		$("#"+multiselectId+"_count").addClass("disable_cls");
		$("#"+multiselectId+"_count > span" ).css('pointer-events','none');
		$("#"+multiselectId+"_count > span").text("0");
		$("#"+multiselectId+"_removeAll").addClass("disable_cls");
		$("#"+multiselectId+"_removeAll > span" ).css('pointer-events','none');
		$("#"+multiselectIdDivId+"_ul").empty();
		showMessage("All items removed successfully.", "success");
		deferredObject.resolve();
		return deferredObject.promise();
		
    }
    
    Multiselect.prototype.removeAllElements = function(multiselectId){
		const context = this;
		let deleleteElement = $('<div id="deleteConfirmation"></div>');
		$("body").append(deleleteElement);
    	$("#deleteConfirmation").html("Are you sure you want to clear all selected items?");
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
					"class":'btn btn-secondary',
					click: function() { 
						$(this).dialog("destroy");
						$(this).remove();
					},
				},
				{
					text	: "Delete",
					"class":'btn btn-primary',
					click	: function(){
				    	let multiselectIdDivId = context.options.multiselectItem[0].id;
				    	context.selectedObjects = new Array();
				    	$("#"+multiselectId+"_count").addClass("disable_cls");
				    	$("#"+multiselectId+"_count > span" ).css('pointer-events','none');
				    	$("#"+multiselectId+"_count > span").text("0");
				    	$("#"+multiselectId+"_removeAll").addClass("disable_cls");
				    	$("#"+multiselectId+"_removeAll > span" ).css('pointer-events','none');
				    	$("#"+multiselectIdDivId+"_ul").empty();
				    	$(this).dialog("destroy");
						$(this).remove();
						showMessage("All items removed successfully.", "success");
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
    
    Multiselect.prototype.selectWithDependent = function(){
    
    }
    
    Multiselect.prototype.removeAll = function(){
    	let multiselectIdDivId = this.options.multiselectItem[0].id;
    	let multiselectId = multiselectIdDivId.split("_")[0];
    	this.selectedObjects = new Array();
    	$("#"+multiselectId+"_count").addClass("disable_cls");
    	$("#"+multiselectId+"_count > span" ).css('pointer-events','none');
    	$("#"+multiselectId+"_count > span").text("0");
    	$("#"+multiselectId+"_removeAll").addClass("disable_cls");
    	$("#"+multiselectId+"_removeAll > span" ).css('pointer-events','none');
    	$("#"+multiselectIdDivId+"_ul").empty();
    	this.selectedObject = new Array();
    }
    
    Multiselect.prototype.showHideDataDiv = function(multiselectId){
    	if($("#"+multiselectId+"_ul").is(":visible")){
			$("#"+multiselectId+"_ul").hide();
		}else{
			$("#"+multiselectId+"_ul").show();
		}
    }
    
    $.fn.autocomplete = function(options, selectedItem) {
		selectedItem = selectedItem == undefined ? {} : selectedItem;
        const tOptions = {
            autocompleteId: String,
            emptyMsgRender: function() {
        
            },
            additionalParamaters: {},
            requestParameters: {},
            paging: true,
            pageSize: 10,
            emptyRender: function() {
                return "<p>No Records found</p>";
            },
            select: function(item) {

            },
            render: function(item) {
        
            },
            extractText: function(item) {
    
            },
            selectedObjectData: function(item) {
                this.selectedObject.push(item);
                return this.selectedObject;
            },

            multiselectItem: Object,
            debounce: 500,
            duplicateCheckRule: function(list, obj) {
                var iCounter;
                for (iCounter = 0; iCounter < list.length; iCounter++) {
                    if (JSON.stringify(list[iCounter]) === JSON.stringify(obj)) {
                        return true;
                    }
                }
                return false;
            }
        };

        options = $.extend(tOptions, options);
        $(this).data('autocomplete', new Autocomplete(this, options, selectedItem));
        return $(this).data('autocomplete');
    }

    $.fn.multiselect = function(options, selectedItems) {
        selectedItems = selectedItems == undefined ? [] : selectedItems;
        const tOptions = {
            autocompleteId: String,
            emptyMsgRender: function() {
        
            },
            additionalParamaters: {},
            requestParameters: {},
            paging: true,
            pageSize: 10,
            emptyRender: function() {
                return "<p>No Records found</p>";
            },
            select: function(item) {
        
            },
            render: function(item) {
        
            },
            extractText: function(item) {
    
            },
            selectedObjectData: function(item) {
                this.selectedObject.push(item);
                return this.selectedObject;
            },
            resetDependentInput: function(){
            
            },
            multiselectItem: Object,
            debounce: 500,
            duplicateCheckRule: function(list, obj) {
                var iCounter;
                for (iCounter = 0; iCounter < list.length; iCounter++) {
                    if (JSON.stringify(list[iCounter]) === JSON.stringify(obj)) {
                        return true;
                    }
                }
                return false;
            }
        };

        options = $.extend(tOptions, options);
        const multiselect = new Multiselect(this, options, selectedItems);
        $(this).data('multiselect', multiselect);
        return multiselect;
    }

}(jQuery));
