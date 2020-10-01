function progress(e){
    if(e.lengthComputable){
	    var bar = $('.bar');
	    var percent = $('.percent');
	    var status = $('#status');
        var max = e.total;
        var current = e.loaded;
        var percentage = (current * 100)/max;
        var percentVal = Math.floor(percentage) + '%';
        bar.width(percentVal);
        percent.html(percentVal);
        console.log(percentVal);
        if(percentage >= 100)
        {
           // process completed  
        }
    }  
 }
 
function uploadFile(element) {
	var fileData = $(element).prop("files")[0];
	var formData = new FormData();
	formData.append("files", fileData);
	$(element).after("<div class='progress'><div class='bar'></div><div class='percent'>0%</div></div>");
    $.ajax({
        type:'POST',
        url: contextPath+'/cf/m-upload',
        data:formData,
        xhr: function() {
            var myXhr = $.ajaxSettings.xhr();
            if(myXhr.upload){
                myXhr.upload.addEventListener('progress', progress, false);
            }
            return myXhr;
        },
        cache:false,
        contentType: false,
        processData: false,

        success:function(data){
            console.log(data);
        },
        error: function(data){
            console.log(data);
        }
    });
}
