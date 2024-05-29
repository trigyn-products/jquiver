(function($) {

//	setTimeout(function() {
//		$(".pq-search-txt").bindFirst("keydown", function(){
//            console.log("testing search");
//        });
//	}, 2000);
    const gridOption = {
        gridId: String,
        gridTitle: String,
        additionalParameters: {},
        dataModel: Object,
        colModel: [{}],
        enableFilter: true,
        draggableColumns : false,
    }
    
    const dataModelOption = {
        location: "remote",
        sorting: "remote",
        dataType: "JSON",
        method: "POST",
        url: "/cf/pq-grid-data",
        getData: function (dataJSON) {
            return { 
                curPage: dataJSON.curPage, totalRecords: dataJSON.total, data: dataJSON.rows 
            };
        }
    }

    class Grid {
        constructor(element, options) {
            this.element = element;
            this.options = $.extend(gridOption, options);
            let dataModel = $.extend(dataModelOption, options.dataModel);
            const postData = {
	            gridId: options.gridId,
	            additionalParameters: JSON.stringify(options.additionalParameters)
	        }
	        dataModel["postData"] = postData;
            this.options.dataModel = dataModel;
        }

        createGrid(options) {
        	
            if(Object.keys(options.dataModel).length === 0) {
                options.dataModel = dataModelOption
            }
            
            if(options.loadCallback == undefined) {
            	options.loadCallback = function(event, ui) {return {event: event, ui: ui}};
            }

            const pqGridObject = {
                flexHeight: true,
                dataModel: options.dataModel,
                menuIcon: true, 
                menuUI:{
                    tabs: ['filter'] 
                },
                colModel: options.colModel,
                additionalParameters: options.additionalParameters,
                pageModel: { type: "remote", rPP: 10, strRpp: "{0}" },
                numberCell: { show: false },
                selectionModel: {type: 'row', swipe: false },
                scrollModel: { autoFit: true }, 
                wrap: false, 
                hwrap: false,
                resizable: false,
                showTop : false,
                dragColumns: {enabled: options.draggableColumns},
                editable : false,
                load: function(event, ui) {
                	setTimeout(function () {
	                	$("div.pq-grid-footer.pq-pager").find('select.ui-corner-all').bindFirst('change', function(a_event) {
	            			$(options.currentGridElement).pqGrid("option", "pageModel").curPage=1;
	            		});
	            		$("tr.pq-grid-header-search-row").find('select.pq-grid-hd-search-field.ui-corner-all').bindFirst('change', function(a_event) {
	            			$(options.currentGridElement).pqGrid("option", "pageModel").curPage=1;
	            		});
                	}, 1000);
                	
                	
                	
                	if($(".pq-search-txt").attr("grid_event") != "1"){
	                	setTimeout(function () {
	                		
	                		$(".pq-search-txt").attr("grid_event","1");
	                		$(".pq-search-txt").bindFirst('keydown', function(a_event) {
	                			if(a_event.originalEvent.keyCode == 13){
	                				$(options.currentGridElement).pqGrid("option", "pageModel").curPage=1;
	                			}
	                		});
	                		$(".pq-search-txt").bind('keyup', function(a_event) { 
	                			if(a_event.originalEvent.keyCode == 27){
	                				$(a_event.originalEvent.target).val("");
	                				$(a_event.originalEvent.target).trigger("change");
	                			}
	                		});
	                	}, 1000);
                	}
                	options.loadCallback(event, ui);
                	ui.colModel.forEach((column, index) => {
					    if(column.sortable == false){
					        $("tr.pq-grid-title-row th[pq-col-indx="+column.leftPos+"]").addClass("pq-grid-col-cusror")  
					    }
					})
                },
                complete: function( event, ui ) {
                	disableInputSuggestion();
                },
            }
            
         
            if(options.enableFilter) {
                pqGridObject['filterModel'] = { on: true, mode: "AND", header: true, menuIcon: true, type : 'remote'}; 
                
            }

         	
            const grid = $(this.element).pqGrid(pqGridObject);
            
            return grid;
        }
        
    }

    $.fn.grid = function(options) {
        options.currentGridElement=$(this);
        const grid = new Grid(this, options);
        $(this).data('grid', grid);
        let gridObject = grid.createGrid(grid.options);
        return gridObject;
    }
}(jQuery));