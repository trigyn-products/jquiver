class AddEditDashboard {
    constructor(dashboardId,dashboardHTMLEditor) {
		this.dashboardId = dashboardId;
		this.dashboardHTMLEditor = dashboardHTMLEditor;
    }
}

AddEditDashboard.prototype.fn = {
	
	loadDashboardPage: function(){
    	$("#errorMessage").hide();
    	
    	if(dashboardId!=""){
    		this.getEntityRoles();
    		
    	}else{
    		 let defaultAdminRole= {"roleId":"ae6465b3-097f-11eb-9a16-f48e38ab9348","roleName":"ADMIN"};
    	     multiselect.setSelectedObject(defaultAdminRole);
    	}
    	require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }, waitSeconds: 120});
    	require(["vs/editor/editor.main"], function() {
        dashboardHTMLEditor = monaco.editor.create(document.getElementById("htmlEditor"), {
		        	value: $("#htmlContent").val().trim(),
		            language: "html",
		            roundedSelection: false,
					scrollBeyondLastLine: false,
					readOnly: false,
					theme: "vs-dark",
					"autoIndent": true
	        	});
	        	dashboardHTMLEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, function() {
			    	typeOfActionWithIsEdit('dashboard-manage-details',  $("#savedAction").find("button"), isEdit, 
			    		addEditDashboardFn.saveDashboard.bind(addEditDashboardFn),addEditDashboardFn.backToDashboardListingPage);
				});
	        	dashboardHTMLEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Shift | monaco.KeyCode.KEY_M,function() {
	                resizeMonacoEditor(dashboardHTMLEditor,"htmlContainer", "htmlEditor");
	            });
		    	dashboardHTMLEditor.onDidChangeModelContent( function (){
		    		$('#errorMessage').hide();
				});
	        	$("#htmlContent").remove();
    	});
    	
    },
    
    saveDashboard: function(){
		let context = this;
		let isDataSaved = false;
		let validData = context.validateDashletDetails();
		if(validData == false){
			return false;
		}
		
		let dashletIdArray = new Array();
		$.each($("#associatedDashlets_selectedOptions_ul span.ml-selected-item"), function(key,val){
				 dashletIdArray.push(val.id);
		     });
		  
		let dashboardDetails = new Object();
		if($("#dashboardId").val() !== ""){ 
			dashboardDetails.dashboardId = $("#dashboardId").val();
		}
		
		dashboardDetails.dashboardName = $("#dashboardName").val();
		dashboardDetails.dashboardType = $("#dashboardTypeId").find(":selected").val();
		dashboardDetails.isDraggable = $("#isDraggableId").val();
		dashboardDetails.isExportable = $("#isExportableId").val();
		dashboardDetails.dashletIdList = dashletIdArray;
		dashboardDetails.dashboardBody = dashboardHTMLEditor.getValue();
				  
		if(validData === true){
			$.ajax({
				type : "POST",
				async : false,
				headers: {"user-id": "admin"},
				url : contextPath+"/cf/sdb",
				contentType : "application/json",
				data : JSON.stringify(dashboardDetails),
				success : function(data) {
					$("#dashboardId").val(data);
					context.saveEntityRoleAssociation(data);
					isDataSaved = true;
					showMessage("Information saved successfully", "success");
		       	},
	        	error : function(xhr, error){
					showMessage("Error occurred while saving", "error");
	        	},
			});
		}
		return isDataSaved;
	},

		changeDraggableValue: function(){
			if($("#isDraggableId").prop('checked')){
				$("#isDraggableId").val(1);
			}else{
				$("#isDraggableId").val(0);
			}
		},
		
		changeExportableValue: function(){
			if($("#isExportableId").prop('checked')){
				$("#isExportableId").val(1);
			}else{
				$("#isExportableId").val(0);
			}
		},
		
		validateDashletDetails : function(){
			if($("#dashboardName").val()==""){
				$("#dashboardName").select();
				showMessage("Please enter dashboard name", "error");
				return false;
			}else{
				$("#errorMessage").hide();
				$("#errorMessage").html("");
			}
			
			let dashboardHTMLEditorValidation=dashboardHTMLEditor.getValue().trim();
			if(dashboardHTMLEditorValidation ==""){
				showMessage("Please enter html script", "warn");
				return false;
			}
			
			 if( $('.ml-selected-item').length < 2) {
		        $("#associatedDashlets").select();
		        showMessage("Please select dashlet.", "error");
		        return false;
		    }else{
		        $("#errorMessage").hide();
		        $("#errorMessage").html("");
		    }
				
			return true;
		},
		
		 validateRolesChecked: function(){
			let checkBoxCount = $(".roles");
			for(let iCounter = 0; iCounter < checkBoxCount.length; ++iCounter){
				if($("#"+$(".roles")[iCounter].id).prop("checked") === true){
					$("#errorMessage").hide();
					$("#errorMessage").html("");
					return true;
				}
			}
			showMessage("Please select at least one user role","error");
			return false;
		},
		
		backToDashboardListingPage : function() {
			location.href = contextPath+"/cf/dbm";
		},
		
		saveEntityRoleAssociation : function(dashboardId){
			let roleIds =[];
			let entityRoles = new Object();
			entityRoles.entityName = $("#dashboardName").val();
			entityRoles.moduleId=$("#moduleId").val();
			entityRoles.entityId= dashboardId;
			 $.each($("#rolesMultiselect_selectedOptions_ul span.ml-selected-item"), function(key,val){
				 roleIds.push(val.id);
		     	
		     });
			
			entityRoles.roleIds=roleIds;
			
			$.ajax({
		        async : false,
		        type : "POST",
		        contentType : "application/json",
		        url :  contextPath+"/cf/ser", 
		        data : JSON.stringify(entityRoles),
		        success : function(data) {
			    }
		    });
		},
		getEntityRoles : function(){
			$.ajax({
		        async : false,
		        type : "GET",
		        url :  contextPath+"/cf/ler", 
		        data : {
		        	entityId:dashboardId,
		        	moduleId:$("#moduleId").val(),
		        },
		        success : function(data) {
		            $.each(data, function(key,val){
		            	multiselect.setSelectedObject(val);
		            });
			    }
		    });
		},
		getdashboarddashlet : function(){
			$.ajax({
		        async : false,
		        type : "GET",
		        url :  contextPath+"/cf/dds", 
		        data : {
		        	dashboardId:dashboardId,
		        },
		        success : function(data) {
		            $.each(data, function(key,val){
		            	dashletMultiselect.createElementForMultiselect(dashletMultiselect, "DashletAutocomplete", val);
		            	
		            });
			    }
		    });
		}
}
