
(function($) {

    class TypeAhead {
        constructor(element, options) {
            this.element = element;
            this.options = options;
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
    }

    class Autocomplete extends TypeAhead {
        constructor(element, options) {
            super(element, options);
            this.init(this.options);
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
            extractText: options.extractText
        });
    }

    class Multiselect extends TypeAhead {
        selectedObjects = new Array();
        constructor(element, options) {
            super(element, options);
            this.init(this.options);
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
            select: options.select
        });
        
        const maxHeight = 200;
        let multiselectId = options.multiselectItem[0].id;
        this.list = $('<div class="ml-selected-items-div"><ul id="'+multiselectId+'_ul" class="ml-selected-items-list"></ul></div>');
        this.list.css('max-height', maxHeight + 'px');
        this.list.show();
        options.multiselectItem.append(this.list)
    }

    Multiselect.prototype.createElementForMultiselect = function(multiselectId, itemData) {
        const context = this;
        if(context.options.duplicateCheckRule(context.selectedObjects, itemData) == false) {
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
            let noOfElements = parseInt($("#"+multiselectId+"_count").text());
    		$("#"+multiselectId+"_count").text(noOfElements+1);
    		$("#"+multiselectId+"_count").removeClass("disable_cls");
    		$("#"+multiselectId+"_removeAll").removeClass("disable_cls");

            deleteItemContext.click(deleteItem);
    
            context.selectedObjects.push(itemData);
        }

        $(context.element).val("");
    }

    Multiselect.prototype.deleteItem = function(multiselectId, item) {
        let noOfElements = parseInt($("#"+multiselectId+"_count").text()) - 1;
    	$("#"+multiselectId+"_count").text(noOfElements);
    	if(noOfElements === 0){
    		$("#"+multiselectId+"_count").addClass("disable_cls");
    		$("#"+multiselectId+"_removeAll").addClass("disable_cls");
    	}
        $(this.parent()).remove();
        return item;
    },
    
    Multiselect.prototype.removeAllElements = function(multiselectId){
    	const context = this;
    	let multiselectIdDivId = context.options.multiselectItem[0].id;
    	context.selectedObjects = new Array();
    	$("#"+multiselectId+"_count").addClass("disable_cls");
    	$("#"+multiselectId+"_count").text("0");
    	$("#"+multiselectId+"_removeAll").addClass("disable_cls");
    	$("#"+multiselectIdDivId+"_ul").empty();
    }
    
    Multiselect.prototype.showHideDataDiv = function(multiselectId){
    	if($("#"+multiselectId+"_ul").is(":visible")){
			$("#"+multiselectId+"_ul").hide();
		}else{
			$("#"+multiselectId+"_ul").show();
		}
    }
    
    $.fn.autocomplete = function(options) {

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
                return this.selectedObjectData;
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
        $(this).data('autocomplete', new Autocomplete(this, options));
        return options;
    }

    $.fn.multiselect = function(options) {
        
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
                return this.selectedObjectData;
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
        const multiselect = new Multiselect(this, options);
        $(this).data('multiselect', multiselect);
        return multiselect;
    }

}(jQuery));
