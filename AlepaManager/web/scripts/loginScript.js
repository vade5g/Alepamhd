var main = function() {
    //hide unwanted things initially
    $("#registerForm").hide();
    $("#submitRegistrationButton").hide();
    
    //extension to reveal hidden textfields on "register" click
    $( "#registerButton" ).click(function() {
        $("#loginPageSquare").animate({
            height: "+=330px",
            top: "-=10%"
        }, 500);
        $("#registerForm").show();
        $("#submitRegistrationButton").show();
        $("#registerButton").hide();
    });
    
    //tell the request function if we are trying to login or register
    $("#loginButton").click(function() {
        sendRequest("login");
    });
    $("#submitRegistrationButton").click(function() {
        sendRequest("register");
    });
    
    var loginUsernameField = $("#loginUsername");
    var loginPasswordField = $("#loginPassword");
    var registerFullNameField = $("#registerFullName");
    var registerUsernameField = $("#registerUsername");
    var registerPasswordField = $("#registerPassword");
    var registerEmailField = $("#registerEmail");
    var registerCategoryField = $("#registerCategoryOptions");
    
    //AJAX request function for both login and register
    function sendRequest(action) {
        var url;
        if (action==="login") {
            if (checkValid("login")===false) {
                
            } else {
                var username = loginUsernameField.val();
                var password = loginPasswordField.val();
                url = "resources/users/login/"+username+"/"+password;
            }
        } else if (action==="register") {
            if (checkValid("register")===false) {
                
            } else {
                var fullnameString = registerFullNameField.val();
                fullnameString = fullnameString.trim();
                fullnameString = fullnameString.split(" ");
                var firstname = fullnameString[0];
                var lastname = fullnameString[1];
                var username = registerUsernameField.val();
                var password = registerPasswordField.val();
                var email = registerEmailField.val();
                var category = registerCategoryField.val();
                url = "resources/users/register/"+firstname+"/"+lastname+"/"
                    +username+"/"+password+"/"+category+"/"+email;
            }

        }
        req = new XMLHttpRequest();
        req.open("POST", url, true);
        req.onreadystatechange = callback;
        req.send(null);
    }
    
    function callback() {
        if (req.readyState === 4) {
            if (req.status === 200) {
                alert(req.responseText);
                if (req.responseText.includes("Login successful")) {
                    window.location.href = "overview.html";
                }
            }
        }
    }

    function checkValid(action) {
        if (action==="login") {
            if (checkField(loginUsernameField.val())===false) {
                alert("Please enter a valid username.\n"
                    + "Are you sure the username you entered is only one word?");
                return false;
            } else if (checkField(loginPasswordField.val())===false) {
                alert("Please enter a valid password.\n"
                    + "Make sure there are no spaces in your password.");
                return false;
            }
        } else if (action==="register") {
            var fullnameString = registerFullNameField.val();
            fullnameString = fullnameString.trim().split(" ");
            if (fullnameString.length!==2) {
                alert("Please enter a valid full name.\n"
                    + "Your full name must consist of 2 parts - your first and last name.\n"
                    + "For example: Jaska Meikäläinen");
                return false;
            }else if (checkField(registerUsernameField.val())===false) {
                alert("Please enter a valid username.\n"
                    + "usernames must be one word and cannot contain spaces "
                    + "or special characters");
                return false;
            } else if (checkField(registerPasswordField.val())===false) {
                alert("Please enter a valid password.\n"
                    + "passwords must be one word and cannot contain spaces "
                    + "or special characters");
                return false;
            } else if (validateEmail(registerEmailField.val())===false) {
                alert("Please enter a valid email.\n"
                        + "email must be in form name@example.com.");
                return false;
            } else if (registerCategoryField.val()===null) {
                alert("Please choose a role from the list.\n");
                return false;
            }
        }
        return true;
        
        function checkField(string) {
            var entry = string;
            entry = entry.trim();
            var entryList = entry.split(" ");
            if (entry==="" || entryList.length > 1) {
                return false;
            } else {
                return true;
            }
        }
        function validateEmail(email) 
        {
            var re = /\S+@\S+\.\S+/;
            return re.test(email);
        }
    }
    
};

$(document).ready(main);



