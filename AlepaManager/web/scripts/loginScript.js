var main = function() {
    
    $("#registerForm").hide();
    $("#submitRegistrationButton").hide();
    
    $( "#registerButton" ).click(function() {
        $("#loginPageSquare").animate({
            height: "+=45%",
            top: "-=10%"
        }, 500);
        $("#registerForm").show();
        $("#submitRegistrationButton").show();
        $("#registerButton").hide();
    });
    
    $("#loginButton").click(function() {
        window.location.href = "overview.html";
    });
};

$(document).ready(main);



