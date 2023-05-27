// Default Cookie implementation //

/**This method was initially present in home.js */

const resourceBundleData = function(resourceKeys) {
	let resourceBundleDataMap;
	$.ajax({
		async: false,
		type: "POST",
		cache: false,
		url: contextPath + '/cf/getResourceBundleData',
		data: {
			resourceKeys: resourceKeys,
		},
		success: function(data) {
			resourceBundleDataMap = data;
		}
	});
	return resourceBundleDataMap;
}

window.onload = function() { cookieConsent(); };

function pureFadeIn(elem, display) {
	var el = document.getElementById(elem);
	el.style.opacity = 0;
	el.style.display = display || "block";

	(function fade() {
		var val = parseFloat(el.style.opacity);
		if (!((val += .02) > 1)) {
			el.style.opacity = val;
			requestAnimationFrame(fade);
		}
	})();
};

function pureFadeOut(elem) {
	var el = document.getElementById(elem);
	el.style.opacity = 1;

	(function fade() {
		if ((el.style.opacity -= .02) < 0) {
			el.style.display = "none";
		} else {
			requestAnimationFrame(fade);
		}
	})();
};

function setCookieNew(name, value, days) {
	var date = new Date();
	date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
	var expires = "expires=" + date.toUTCString();
	document.cookie = name + "=" + value + ";" + expires + ";path=/";
}

function getCookieNew(name) {
	var cookieName = name + "=";
	let decodedCookie = decodeURIComponent(document.cookie);
	let ca = decodedCookie.split(";");
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == " ") {
			c = c.substring(1);
		}
		if (c.indexOf(cookieName) == 0) {
			return c.substring(cookieName.length, c.length);
		}
	}
	return "";
}
	

function deleteCookie(name) {
	const d = new Date();
    d.setTime(d.getTime() + (24*60*60*1000));
    let expires = "expires="+ d.toUTCString();
    document.cookie = name + "=;" + expires + ";path=/";
}


function cookieConsent() {
	if (!getCookieNew('user_cookie_consent')) {
		document.body.innerHTML += '<div class="cookieConsentContainer" id="cookieConsentContainer"><div class="cookietitle"><div class="cookieimg"><img src="' + contextPath + '/webjars/1.0/images/cookie.svg"></div><div class="cookieTitle"><h3>' + resourceBundleData("jws.cookieTitle") + '</h3></div></div><div class="designcreator"><div class="cookieDesc"><p>' + resourceBundleData("jws.cookieDesc") + '</p></div><div class="cookieButton"><a class="class="cokibtn"" onClick="purecookieDismiss();">' + resourceBundleData("jws.cookieButton") + '</a></div></div>';
		pureFadeIn("cookieConsentContainer");
	}
}

function purecookieDismiss() {
	setCookieNew('user_cookie_consent', '1', 180);
	pureFadeOut("cookieConsentContainer");
}