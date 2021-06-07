(function($) {
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
            return { curPage: dataJSON.curPage, totalRecords: dataJSON.total, data: dataJSON.rows };
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
                load: function(event, ui) {
                	options.loadCallback(event, ui);
                	disableInputSuggestion();
                	ui.colModel.forEach((column, index) => {
					    if(column.sortable == false){
					        $("tr.pq-grid-title-row td[pq-col-indx="+column.leftPos+"]").addClass("pq-grid-col-cusror")  
					    }
					})
                }
            }
            
         
            if(options.enableFilter) {
                pqGridObject['filterModel'] = { on: true, mode: "AND", header: true }; 
            }

         	
            const grid = $(this.element).pqGrid(pqGridObject);
            return grid;
        }
        
    }

    $.fn.grid = function(options) {
        const grid = new Grid(this, options);
        $(this).data('grid', grid);
        return grid.createGrid(grid.options);
    }
}(jQuery));