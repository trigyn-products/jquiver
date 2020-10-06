
(function($) {

    class TypeAhead {
        constructor(element, options) {
            this.element = element;
            this.options = options;
            this.selectedObject = [];
        }

        loadServerPages = function (searchTerm, pageNumber, pageSize) {
            const context = this;
            var deferred = $.Deferred();
            var searchval = $.trim(searchTerm);
            if(searchval != "") {
                $.ajax({
                    type: "POST",
                    url: "/cf/autocomplete-data",
                    data: { 
                            searchText: searchval,
                            autocompleteId : context.options.autocompleteId,
                            startIndex : pageNumber * pageSize,
                            pageSize : pageSize,
                            additionalParamaters: JSON.stringify(context.options.additionalParamaters)
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
            this.init(this.options);
            this.initWithValues(selectedItem);
        }
        
        initWithValues = function(item){
        	this.selectedObject = item;
        	let value = this.options.extractText(item);
        	$(this.element).val(value);
        	return this.selectedObject;
        }
    }

    Autocomplete.prototype.init = function(options) {
        const context = this;
        $(this.element).richAutocomplete({
            loadPage: function (searchTerm, pageNumber, pageSize) {
                return context.loadServerPages(searchTerm, pageNumber, pageSize);
            },
            paging: options.paging,
            pageSize: options.pageSize,
            emptyRender: options.emptyRender,
            select: options.select,
            render: options.render,
            extractText: options.extractText,
            selectedObjectData: options.selectedObjectData
        });
    }

    class Multiselect extends TypeAhead {
        selectedObjects = new Array();
        constructor(element, options, selectedItems) {
            super(element, options);
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
    }

    Multiselect.prototype.init = function(options) {
    const context = this;
        $(this.element).richAutocomplete({
            loadPage: function (searchTerm, pageNumber, pageSize) {
                return context.loadServerPages(searchTerm, pageNumber, pageSize);
            },
            paging: options.paging,
            pageSize: options.pageSize,
            emptyRender: options.emptyRender,
            render: options.render,
            extractText: options.extractText,
            select: options.select,
            selectedObjectData: options.selectedObjectData
        });
        
        const maxHeight = 200;
        let multiselectId = options.multiselectItem[0].id;
        this.list = $('<div class="ml-selected-items-div"><ul id="'+multiselectId+'_ul" class="ml-selected-items-list"></ul></div>');
        this.list.css('max-height', maxHeight + 'px');
        this.list.show();
        options.multiselectItem.append(this.list)
    }

    Multiselect.prototype.createElementForMultiselect = function(context, multiselectId, itemData) {
        if(context.options.duplicateCheckRule(context.selectedObjects, itemData) == false) {
        	context.selectedObject.push(itemData);
            const element = context.options.selectedItemRender(itemData);
            let listsElement = $("<li></li>");
            let itemSpan = $('<span class="ml-selected-item">'+element+'</span>');
            let deleteItemContext = $('<span class="float-right closeicon"><i class="fa fa-times-circle-o" aria-hidden="true"></i></span>');
            deleteItemContext.data("selected-item", itemData);
    
            listsElement.append(itemSpan);
            listsElement.append(deleteItemContext);
            
            $(".ml-selected-items-list").append(listsElement);
            
            var deleteItem = function(event) {
                var data = $(deleteItemContext).data('selected-item');
                context.deleteItem.apply(deleteItemContext, [multiselectId, data]);
                context.selectedObjects.splice(itemData, 1);
            };
            let noOfElements = parseInt($("#"+multiselectId+"_count > span").text());
    		$("#"+multiselectId+"_count > span").text(noOfElements+1);
    		$("#"+multiselectId+"_count").removeClass("disable_cls");
    		$("#"+multiselectId+"_count > span" ).css('pointer-events','auto');
    		$("#"+multiselectId+"_removeAll").removeClass("disable_cls");
    		$("#"+multiselectId+"_removeAll > span" ).css('pointer-events','auto');

            deleteItemContext.click(deleteItem);
    
            context.selectedObjects.push(itemData);
        }

        $(context.element).val("");
    }

    Multiselect.prototype.deleteItem = function(multiselectId, item) {
    	$("#"+multiselectId+"_deleteConfirmation").html("Are you sure you want to remove '"+item.text + "'?");
		$("#"+multiselectId+"_deleteConfirmation").dialog({
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
						$(this).dialog('close');
					},
				},
				{
					text	: "Delete",
					click	: function(){
						$(this).dialog('close');
				        let noOfElements = parseInt($("#"+multiselectId+"_count > span").text()) - 1;
				    	$("#"+multiselectId+"_count > span").text(noOfElements);
				    	if(noOfElements === 0){
				    		$("#"+multiselectId+"_count").addClass("disable_cls");
				    		$("#"+multiselectId+"_count > span" ).css('pointer-events','none');
				    		$("#"+multiselectId+"_removeAll").addClass("disable_cls");
				    		$("#"+multiselectId+"_removeAll > span" ).css('pointer-events','none');
				    	}
				        $(this.parent()).remove();
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
    
    Multiselect.prototype.removeAllElements = function(multiselectId){
		const context = this;
    	$("#"+multiselectId+"_deleteConfirmation").html("Are you sure you want to clear all selected items?");
		$("#"+multiselectId+"_deleteConfirmation").dialog({
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
						$(this).dialog('close');
					},
				},
				{
					text	: "Delete",
					click	: function(){
						$(this).dialog('close');
				    	let multiselectIdDivId = context.options.multiselectItem[0].id;
				    	context.selectedObjects = new Array();
				    	$("#"+multiselectId+"_count").addClass("disable_cls");
				    	$("#"+multiselectId+"_count > span" ).css('pointer-events','none');
				    	$("#"+multiselectId+"_count > span").text("0");
				    	$("#"+multiselectId+"_removeAll").addClass("disable_cls");
				    	$("#"+multiselectId+"_removeAll > span" ).css('pointer-events','none');
				    	$("#"+multiselectIdDivId+"_ul").empty();
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
        const multiselect = new Multiselect(this, options, selectedItems);
        $(this).data('multiselect', multiselect);
        return multiselect;
    }

}(jQuery));
