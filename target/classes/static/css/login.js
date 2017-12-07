document.getElementById('email').onchange = function() {
	validateEmail();
};

function validateEmail() {
	var email = document.getElementById('email');
	var validEmail = new RegExp("[a-z0-9.+-]+@appdirect.com$")
	var result = validEmail.test(email.value);
	if (result === false) {
		alert("Please enter valid Appdirect Email Address");
		return false;
	} else {
		return true;
	}
}