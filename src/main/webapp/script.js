
function _(tagname) {
	return document.querySelector(tagname);
}
function __(tagsname) {
	return document.querySelectorAll(tagsname);
}

// get cookies
function getCookies() {
	var cookiesObj = {}
	let cookies = document.cookie.split(/[=;:]/).map(x => x.trim());
	for (i = 0; i < cookies.length; i += 2) {
		cookiesObj[cookies[i]] = cookies[i + 1];
	}
	return cookiesObj
}

// get single cookie
function getCookie(cookieName){
	var cookiesObj = getCookies();
	return cookiesObj[cookieName];
}


function applyAccount() {
	// check mobile number has active account!
	// check mobile number's previous application is pending

	var newCustomer = {
		account_type: _('#account_type').value,
		first_name: _('#first_name').value,
		last_name: _('#last_name').value,
		home_address: _('#home_address').value,
		gender: _('#gender').value,
		date_of_birth: _('#date_of_birth').value,
		email_address: _('#email_address').value,
		mobile_no: _('#mobile_no').value
	}

	sendRequest("ApplyForAccount", "application/x-www-form-urlencoded", newCustomer, applicationResponse);

}



function applicationResponse(responseText) {

	var response = JSON.parse(responseText);

	if (response.statuscode == 400) {
		alert(response.errorMessage);
	} else if (response.statuscode == 200) {
		alert("Your ref id : " + response.data.refID);
	}
}


// to send a post request with json data
function sendRequest(url, contentType, data, callbackfn) {
	var xhr = new XMLHttpRequest();

	xhr.onreadystatechange = function () {
		if (this.readyState == 4) {
			if (this.status == 200) {
				callbackfn(this.responseText);
			}
		}
	};

	url = "http://localhost:8080/zbi.bank/" + url;

	xhr.open("POST", url, true);

	xhr.setRequestHeader('Content-Type', contentType);

	xhr.onerror = function () {
		// redirect to error page
		console.error('Request failed due to network error.');
	};

	if (data != null) {
		xhr.send(JSON.stringify(data));
	} else {
		xhr.send();
	}


}



