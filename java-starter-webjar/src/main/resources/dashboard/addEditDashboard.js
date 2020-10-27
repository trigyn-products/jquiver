class AddEditDashboard {
    constructor(contextId, dashboardId) {
        this.contextId = contextId;
		this.dashboardId = dashboardId;
    }
}

AddEditDashboard.prototype.fn = {
	
	loadDashboardPage: function(){
    	$("#errorMessage").hide();
    	this.populateDashlets();
    	if(dashboardId!=""){
    		this.getEntityRoles();
    	}else{
    		 let defaultAdminRole= {"roleId":"ae6465b3-097f-11eb-9a16-f48e38ab9348","roleName":"ADMIN"};
    	     multiselect.setSelectedObject(defaultAdminRole);
    	}
    },
    
    
    populateDashlets: function(){
    	$("#associatedDashlets").empty();
    	let selectedContextId = $("#contextId").find(":selected").val();
    	$.ajax({
    		type: "GET",
    		url: contextPath+"/cf/gdbc",
    		dataType: "json",
    		headers: {
    			"context-id": selectedContextId,
    			"dashboardId":  dashboardId,
    		},
    		success: function (data) {
    			if(data.length > 0){
    				let dashletDetailsArray = data;
    				let dashletDiv;
    				for(let iCounter = 0; iCounter < dashletDetailsArray.length; ++iCounter){
    					let dashletDetails = dashletDetailsArray[iCounter];
    					dashletDiv = '<div class="inpugrp pull-left checkbox">'
    					if(dashletDetails.dashboardId === dashboardId){
    						dashletDiv = dashletDiv + "<input type = checkbox checked = checked id = chkdashlet_"+dashletDetails.dashletId+" value="+dashletDetails.dashletId+" name = dashletId class = dashlets pull-left/>";
    					}else{
    						dashletDiv = dashletDiv + "<input type = checkbox id = chkdashlet_"+dashletDetails.dashletId+" value="+dashletDetails.dashletId+" name = dashletId class = dashlets pull-left/>";
    					}
    					dashletDiv = dashletDiv + "<label class = p-2 pull-left for = chkdashlet_"+dashletDetails.dashletId+">"+dashletDetails.dashletName+"</label>";
    					$("#associatedDashlets").append(dashletDiv);
    				}
    			}else if(dashboardId !== undefined && dashboardId !== ""){
    				$("#errorMessage").html("");
    				$("#errorMessage").html("Sorry no dashlets avaiable for this context");
    			}
    		},
    		error: function (xhr, error) {
				showMessage("Error occurred while fetching dashlets", "error");
    		}
    	});
    },
    
    
    saveDashboard: function(){
		let context = this;
		let isDataSaved = false;
		let validData = context.validateDashletDetails();
		if(validData == false){
			return false;
		}
		
		validData = context.validateDashletsChecked();
		if(validData == false){
			return false;
		}	
		
		let dashletIdArray = new Array();
		$("input:checked.dashlets").each(function(val, index){
			dashletIdArray.push(this.value);
		});
		  
		let dashboardDetails = new Object();
		if($("#dashboardId").val() !== ""){
			dashboardDetails.dashboardId = $("#dashboardId").val();
		}
		
		dashboardDetails.dashboardName = $("#dashboardName").val();
		dashboardDetails.dashboardType = $("#dashboardTypeId").find(":selected").val();
		dashboardDetails.contextId = $("#contextId").find(":selected").val();
		dashboardDetails.isDraggable = $("#isDraggableId").val();
		dashboardDetails.isExportable = $("#isExportableId").val();
		dashboardDetails.dashletIdList = dashletIdArray;
				  
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
				$("#errorMessage").html("Please enter dashboard name");
				$("#errorMessage").show();
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
			$("#errorMessage").html("Please select at least one user role");
			$("#errorMessage").show();
			return false;
		},
		
		validateDashletsChecked: function(){
			let checkBoxCount = $(".dashlets");
			let isDahletSelected = false;
			for(let iCounter = 0; iCounter < checkBoxCount.length; ++iCounter){
				if($("#"+$(".dashlets")[iCounter].id).prop("checked") === true){
					$("#errorMessage").hide();
					$("#errorMessage").html("");
					isDahletSelected = true;
					return true;
				}
			}
			$("#errorMessage").html("Please select at least one dashlet");
			$("#errorMessage").show();
			return isDahletSelected;
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
		        url : "/cf/ser", 
		        data : JSON.stringify(entityRoles),
		        success : function(data) {
			    }
		    });
		},
		getEntityRoles : function(){
			$.ajax({
		        async : false,
		        type : "GET",
		        url : "/cf/ler", 
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
		}
}
