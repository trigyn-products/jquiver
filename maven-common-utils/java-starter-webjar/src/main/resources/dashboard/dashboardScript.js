 var refreshIntervals = {};
    function toggleFields(checkbox, dashletId) {
        var autoRefField = document.getElementById('auto_ref_' + dashletId);
        var timeField = document.getElementById('time_' + dashletId);

        autoRefField.disabled = !checkbox.checked;
        timeField.disabled = !checkbox.checked;
        autoRefField.value = '1';
        if (!checkbox.checked) {
            clearInterval(refreshIntervals[dashletId]);
            autoRefField.value = ""; 
        }
    }

   function toggleDropdown(dashletId) {
        var dropdownContent = document.getElementById('actionDropdown_' + dashletId);
    }

var autoRefField;
var isRefreshing = [];
function setAutoRefresh(dashletId) {
    if(isRefreshing.includes(dashletId)){
        autoRefField.value = '';
        return;
    }else{
        isRefreshing.push(dashletId);
        autoRefField = document.getElementById('auto_ref_' + dashletId);
        var timeField = document.getElementById('time_' + dashletId);
        var refreshInterval = parseInt(autoRefField.value, 10);
        var timeUnit = timeField.value;

        switch (timeUnit) {
            case '1':
                refreshInterval *= 1000;
                break;
            case '2':
                refreshInterval *= 1000 * 60;
                break;
            default:
                return;
        }
        clearInterval(refreshIntervals[dashletId]);

    if (refreshInterval > 0) {
            refreshIntervals[dashletId] = setInterval(function () {
                refreshDashletContent(dashletId);
            }, refreshInterval);

            setTimeout(function () {
                isRefreshing = isRefreshing.filter(id => id !== dashletId);
            }, refreshInterval);
        }
}
}
    function validateSeconds(dashletId) {
        var autoRefField = document.getElementById('auto_ref_' + dashletId).value;
        var autoRef = parseInt(autoRefField, 10);
        var timeField = document.getElementById('time_' + dashletId).value;
        
        if (timeField == '1') {
            if (!isNaN(autoRef) && autoRef >= 1 && autoRef <= 60){
                setAutoRefresh(dashletId);               
            } else {
                showMessage("Value should not be less than 1 or more than equal to 60.", "warn");
                document.getElementById('auto_ref_' + dashletId).value = "1";
                return false;
            }
        } else if(timeField == '2'){
            if (!isNaN(autoRef) && autoRef === parseInt(autoRef, 10) && autoRef >= 1 ) {
                setAutoRefresh(dashletId);
            } else {
                showMessage("Please enter a valid positive integer", "warn");
                document.getElementById('auto_ref_' + dashletId).value = "1";
                return false;
            }
     }
    }

    $(document).click(function(e) {
        var dropdownButton = $('.custom-grp-btn'); 
        if (!$(e.target).closest('.refreshmainblock').length && !$(e.target).is(dropdownButton)) {
            $('.collapse').collapse('hide');
        }
    });