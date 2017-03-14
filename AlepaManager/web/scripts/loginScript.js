var main = function() {
    
    //setting all the login page element sizes correctly so they don't bug out
    //when screen size is changed around
    $("#loginPageSquare").css("left", $(window).width()/2);
    $("body h1").css("left", $(window).width()/2);
    $(".regInput").css("left", $("#loginPageSquare").width()/2);
    $(".regInput").css("width", $("#loginPageSquare").width()/2);
    $(".regInput").css("margin-left", -$("#loginPageSquare").width()/4);
    //$("#loginPageSquare").css("top", $(window).height()/4);
    $(".loginField").css("width", $("#loginPageSquare").width()/2.4);
    //apply the same measurements when window is resized on the fly by user
    $( window ).resize(function() {
        $("#loginPageSquare").css("left", $(window).width()/2);
        //$("#loginPageSquare").css("top", $(window).height()/4);
        $("body h1").css("left", $(window).width()/2);
        $(".regInput").css("left", $("#loginPageSquare").width()/2);
        $(".regInput").css("width", $("#loginPageSquare").width()/2);
        $(".regInput").css("margin-left", -$("#loginPageSquare").width()/4);
        $(".loginField").css("width", $("#loginPageSquare").width()/2.4);
    });
    
    //hide unwanted things initially
    $("#registerForm").hide();
    $("#submitRegistrationButton").hide();
    
    //extension animation to reveal hidden textfields on "register"-button click
    $( "#registerButton" ).click(function() {
        $("#loginPageSquare").animate({
            height: "+=23em",
            top: "-=5em"
        }, 500);
        $("#registerForm").show();
        $("#submitRegistrationButton").show();
        $("#registerButton").hide();
    });
    
    //tell the request function if we are trying to login or register
    $("#loginButton").click(function() {
        var action="login";
        if (checkValid(action)===true) {
            var type="POST";
            var username = loginUsernameField.val();
            var password = loginPasswordField.val();
            var url = "resources/users/login/"+username+"/"+password;
            sendRequest(type, url, action);
        }
    });
    
    //when user clicks on registration
    $("#submitRegistrationButton").click(function() {
        var action="register";
        if (checkValid(action)===true) {
            //get all the necessary variables from the form fields to be sent
            //to the backend for new user creation
            var fullnameString = registerFullNameField.val();
            fullnameString = fullnameString.trim();
            fullnameString = fullnameString.split(" ");
            var firstname = fullnameString[0];
            var lastname = fullnameString[1];
            var username = registerUsernameField.val();
            var password = registerPasswordField.val();
            var email = registerEmailField.val();
            var category = registerCategoryField.val();
            var url = "resources/users/register/"+firstname+"/"+lastname+"/"
                +username+"/"+password+"/"+category+"/"+email;
            var type = "POST";
            sendRequest(type, url, action);
        }
    });
    
    //accept enter button press as well when logging in
    $("#loginPassword").keypress(function(e) {
        if (e.which === 13) {
            $("#loginButton").trigger("click");
        }
    });
    
    //shortcuts to commonly used fields in login/register process
    var loginUsernameField = $("#loginUsername");
    var loginPasswordField = $("#loginPassword");
    var registerFullNameField = $("#registerFullName");
    var registerUsernameField = $("#registerUsername");
    var registerPasswordField = $("#registerPassword");
    var registerEmailField = $("#registerEmail");
    var registerCategoryField = $("#registerCategoryOptions");
    
    var loggedInUser;
    
    //AJAX request function for both login and register
    function sendRequest(type, url, action) {
        req = new XMLHttpRequest();
        req.open(type, url, true);
        req.onreadystatechange = function() {
            callback(action);
        };
        req.send(null);
    }
    
    //process the result of the AJAX request, parameter action determines how
    //we update the visual representation based on response
    function callback(action) {
        if (req.readyState === 4) {
            if (req.status === 200) {
                if (action==="login") {
                    //failed login includes word "bad"
                    //if login was successful, save the logged in user:
                    if (!req.responseText.includes("bad")) {
                        loggedInUser = req.responseText;
                        //put the returned firstname dash lastname into storage
                        if(typeof(sessionStorage) !== "undefined") {
                            //clear old ones
                            sessionStorage.clear();
                            //set name of logged in user so we remember it later
                            sessionStorage.setItem('loggedInUser', loggedInUser);
                        }
                        alert("Login successful!");
                        //redirect to overview
                        window.location.href = "overview.html";
                    //alert user why the login failed
                    } else if (req.responseText.includes("bad username")) {
                        alert("This username doesn't exist. Please register to continue.");
                    } else if (req.responseText.includes("bad password")) {
                        alert("The password is wrong. Please try again.");
                    }
                //handling registration scenario
                } else if (action==="register") {
                    //always goes through if the fields are OK, do animation
                    //and fill out login fields for the user to save them time
                    alert(req.responseText);
                    if (req.responseText.includes("Registration successful")) {
                        loginUsernameField.val(registerUsernameField.val());
                        loginPasswordField.val(registerPasswordField.val());
                        $('#registerForm')[0].reset();
                        $("#loginPageSquare").animate({
                            height: "-=22em",
                            top: "+=5em"
                        }, 500, function() {
                            $("#registerForm").hide();
                            $("#submitRegistrationButton").hide();
                            $("#registerButton").show();
                        });
                    }
                }
            }
        }
    }

    //function for checking the login and registration fields to make sure the
    //values are proper rather than spaces or illegal characters
    function checkValid(action) {
        //alerts for invalid login
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
        //alerts for invalid register
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
        //no faults found, return true to signify that fields were okay!
        return true;
        
        //internal function to avoid repetition, used for most fields
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
        //regex test for email field
        function validateEmail(email) 
        {
            var re = /\S+@\S+\.\S+/;
            return re.test(email);
        }
    }
    
};

$(document).ready(main);



