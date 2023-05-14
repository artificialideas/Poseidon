function validateForm() {
  const fullname = document.getElementById('fullname');
  const username = document.getElementById('username');
  const password = document.getElementById('password');
  const role = document.getElementById('role');

    if (fullname.value === "") {
        alert("Please enter a full name.");
        fullname.focus();
        return false;
    }
    if (username.value === "") {
        alert("Please enter a valid username.");
        username.focus();
        return false;
    }
    if (password.value === "" || !(/^(?=[^a-z]*[a-z])(?=[^A-Z]*[A-Z])(?=\\D*\\d)(?=[^!#%]*[!#%])[A-Za-z0-9!#%]{8,30}$/).test(password.value)) {
        alert("Please choose a valid password:<br/>"+
            "- Between 8 and 30 char<br/>"+
            "- At least 1 upper-case char<br/>"+
            "- At least 1 digit<br/>"+
            "- At least 1 symbol<br/>"+
            "- No whitespace");
        password.focus();
        return false;
    }
    if (role.value === null) {
        alert("Please select a role.");
        role.focus();
        return false;
    }

    return true;
}