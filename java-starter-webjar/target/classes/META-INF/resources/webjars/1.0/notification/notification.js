
function getNotifications() {

	$.ajax({
		url: contextPathHome + '/api/gnd',
		type: "GET",
		dataType: "json",
		async: true,
		success: function(data) {
			let jsonObject = JSON.parse(JSON.stringify(data));
			let notificationCookie, msgType, counter;
			/**Added for displaying the count as 9+ when the notification count is more than 9 */
			let MAX_LIMIT = 9;
			if (jsonObject.length >= MAX_LIMIT) {
				$("#viewNotificationBtn").val(+ MAX_LIMIT + '+');
			} else {
				$("#viewNotificationBtn").val(+ jsonObject.length);
			}
			/**Ends Here */
			for (counter = 0; counter < jsonObject.length; counter++) {
				notificationCookie = getCookie(jsonObject[counter]["notificationId"]);
				if (jsonObject[counter] != null && (notificationCookie == null || notificationCookie == "")) {
					if (jsonObject[counter]["messageType"] == 0) {
						msgType = "success";
					} else if (jsonObject[counter]["messageType"] == 1) {
						msgType = "warn";
					} else {
						msgType = "error";
					}

					if (jsonObject[counter]["display_once"] == 0) {
						setCookie(jsonObject[counter]["notificationId"], jsonObject[counter]["notificationId"], 1);
					} else {
						setCookie(jsonObject[counter]["notificationId"], jsonObject[counter]["notificationId"], 365)
					}

					let msg = $('<div/>').text(jsonObject[counter]["messageText"]).html();
					showMessage(msg, msgType);
				}
			}

		},
		error: function(textStatus, errorThrown) {
			showMessage("Error while retrieving notifications", "error");
		}
	});
}
//For implementing view of Notifications 
function loadNotificationView() {
	$("#divNotificationView").html("");
	$.ajax({
		url: contextPath + "/api/gnd",
		type: "GET",
		dataType: "json",
		async: true,
		success: function(data) {
			let jsonObject = JSON.parse(JSON.stringify(data));
			if (jsonObject.length != null && jsonObject.length != 0) {
				let new_div = '';
				for (counter = 0; counter < jsonObject.length; counter++) {
					if (jsonObject[counter]["messageType"] == 0) {
						msgType = "success";
					} else if (jsonObject[counter]["messageType"] == 1) {
						msgType = "warn";
					} else {
						msgType = "error";
					}

					let msg_text = jsonObject[counter]["messageText"];
					/**Written for preventing Cross Site Scripting*/
					let encodedMsgtxt = $('<div />').text(msg_text).html();
					/**Ends Here*/
					let valid_from_date = jsonObject[counter]["msgValidFrom"];
					let validFromDt = new Date(valid_from_date);
					let currDt =new Date(Date.now());
					let displayDate = calculateDays(validFromDt, currDt);
					new_div += '<div id="notificationblockmain" class="popupnotificationlist">' +'<div class="notificationtext">' + encodedMsgtxt +'</div>'    ;
					
					if (msgType === "success") {
						new_div += '<div class="infocls notificationicons"></div>';
					} else if (msgType === "warn") {
						new_div += '<div class="warncls notificationicons"></div>';
					} else if (msgType === "error") {
						new_div += '<div class="alertclss notificationicons"></div>';
					}
					new_div += '<div class="notificationtype">' + displayDate + '</div>'+ '</div>';
				}
				$("#divNotificationView").append(new_div);
				$("#divNotificationView").dialog("open");
			} else {
				showMessage("No notification to be displayed", "info");
			}
		},
		error: function(jqXHR, exception) {
			showMessage("Error occurred while fetching text responses", "error");
		}
	});
}//end of function loadNotificationView

/**Written for calculating the difference between MsgValidFromDt and CurrentDate */
function calculateDays(validFromDt, currDt) {
	let diffTime = Math.abs(currDt - validFromDt);
	let diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
	let displayDt = "";
	if (diffDays < 7) {
		displayDt += diffDays + " day(s)";
	} else if (diffDays < 28) {
		displayDt += Math.round((diffDays / 7)) + " week(s)";
	} else if (diffDays < 365) {
		displayDt += Math.round((diffDays / 30)) + " month(s)";
	} else {
		displayDt += Math.round((diffDays / 365)) + " year(s)";
	}

	return displayDt;
}
/**Ends Here */

$(function() {

	$("#divNotificationView").dialog({
		bgiframe: true,
		autoOpen: false,
		modal: true,
		closeOnEscape: true,
		draggable: true,
		resizable: false,
		title: "Notification(s)",
		dialogClass: "notificationpopup",
		position: {
			my: "center", at: "center"
		},

		open: function(event, ui) {
			$('.ui-dialog-titlebar')
				.find('button').removeClass('ui-dialog-titlebar-close').addClass('ui-button ui-corner-all ui-widget ui-button-icon-only ui-dialog-titlebar-close')
				.prepend('<span class="ui-button-icon ui-icon ui-icon-closethick"></span>').append('<span class="ui-button-icon-space"></span>');
		}
	});
});