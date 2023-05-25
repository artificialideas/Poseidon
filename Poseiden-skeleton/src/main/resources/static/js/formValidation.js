function validateForm() {
  //const fullname = document.getElementById('fullname');
  //const username = document.getElementById('username');
  const password = document.getElementById('password');
  //const role = document.getElementById('role');

    /*if (fullname.value === "") {
        alert("Please enter a full name.");
        fullname.focus();
        return false;
    }
    if (username.value === "") {
        alert("Please enter a valid username.");
        username.focus();
        return false;
    }*/
    // We'll only use password message
    if (password.value === "" || !(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?!.*[&%$])(?=.*\W).{8,30}$/).test(password.value)) {
        alert("Please choose a valid password: "+
            "Between 8 and 30 char, "+
            "at least 1 upper-case char, "+
            "at least 1 digit, "+
            "at least 1 symbol, "+
            "no whitespace");
        password.focus();
        return true; // For this exercice and in order to use Password validator on our Controller (back-end), we need to submit the erroneous form
    }
    /*if (role.value === null) {
        alert("Please select a role.");
        role.focus();
        return false;
    }*/

    return true;
}