function saveData (){
	let primaryKey = $("#gridId").val();
    $("#primaryKey").val(primaryKey);
    if(validateFields() == false){
    	$("#errorMessage").show();
	    return false;
	}
	let isDataSaved = false;
	if($("#customFilterCriteria").val().trim() !== ''){
		$("#customFilterCriteriaHidden").val($("#customFilterCriteria").val().replace(/'/g, "''") );
	}else{
		$("#customFilterCriteriaHidden").val("");
	}
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