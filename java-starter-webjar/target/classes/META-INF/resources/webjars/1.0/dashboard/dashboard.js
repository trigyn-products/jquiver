(function($){

    const dashboardOptions = {
        dashboardId: 1,
        additionalParameters: {}
    };

    class Dashboard {
        constructor(element, options) {
            this.element = element;
            this.options = options;
        }

        initalize = function() {
            const context = this;
            $.ajax({
                type: "POST",
                url:  contextPath+"/cf/dls",
                data: {
                    "dashboardId":  this.options.dashboardId,
                },
                success: function (data) {
                    $(context.element).html(data);
                },
                error: function (xhr, error) {
    
                }
            });
        }
    }

    $.fn.dashboard = function(options) {
        this.options = $.extend(dashboardOptions, options);
        const dashboard = new Dashboard(this, options);
        $(this).data('dashboard-data', dashboard);
        dashboard.initalize();
    }

}(jQuery));