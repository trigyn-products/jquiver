function saveData (){
	let primaryKey = $("#gridId").val();
	let isEdit = $("#isEdit").val();
    $("#primaryKey").val(primaryKey);
    if(validateFields() == false){
    	$("#errorMessage").show();
	    return false;
	}
	if (saveModDetails(isEdit, $("#gridId").val()) == false) {
		return false;
	}
	let isDataSaved = false;
	if($("#customFilterCriteria").val().trim() !== ''){
		$("#customFilterCriteriaHidden").val($("#customFilterCriteria").val().replace(/'/g, "''") );
	}else{
		$("#customFilterCriteriaHidden").val("");
	}
	var formattedDate = dateFormat();
	$("#lastUpdatedTs").val(formattedDate);
	$("#createdDate").val(formattedDate);
	let formData = $("#addEditForm").serialize()+ "&formId="+formId;
	$.ajax({
	  type : "POST",
	  url : contextPath+"/cf/sdf",
	  async: false,
	  data : formData,
      success : function(data) {
    	  isDataSaved = true;
          saveEntityRoleAssociation($("#gridId").val());
          enableVersioning(formData);
		  showMessage("Information saved successfully", "success");
      },
	  error : function(xhr, error){
		showMessage("Error occurred while saving", "error");
	  },
	});
	return isDataSaved;
}

function dateFormat(){
	let now = new Date();
	let formattedDate = now.getFullYear() + "-" +
	    String(now.getMonth() + 1).padStart(2, '0') + "-" +
	    String(now.getDate()).padStart(2, '0') + " " +
	    String(now.getHours()).padStart(2, '0') + ":" +
	    String(now.getMinutes()).padStart(2, '0') + ":" +
	    String(now.getSeconds()).padStart(2, '0');
	return formattedDate;
}