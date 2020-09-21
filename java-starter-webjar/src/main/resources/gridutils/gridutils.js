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

    class Grid {
        constructor(element, options) {
            this.element = element;
            this.options = $.extend(gridOption, options);
        }

        createGrid = function(options) {
            if(Object.keys(options.dataModel).length === 0) {
                options.dataModel = {
                    location: "remote",
                    sorting: "remote",
                    dataType: "JSON",
                    method: "POST",
                    url: "/cf/pq-grid-data",
                    postData: {
                        gridId: options.gridId,
                        additionalParameters: JSON.stringify(options.additionalParameters)
                    },
                    getData: function (dataJSON) {
                        return { curPage: dataJSON.curPage, totalRecords: dataJSON.total, data: dataJSON.rows };
                    }
                }
            }

            const pqGridObject = {
                flexHeight: true,
                dataModel: options.dataModel,
                colModel: options.colModel,
                pageModel: { type: "remote", rPP: 10, strRpp: "{0}" },
                numberCell: { show: false },
                selectionModel: {type: 'row', swipe: false },
                scrollModel: { autoFit: true }, 
                wrap: false, 
                hwrap: false,
                resizable: false,
                showTop : false,
                dragColumns: {enabled: options.draggableColumns},
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