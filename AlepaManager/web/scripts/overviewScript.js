var main = function() {
    // hover animations for new note and history images
    var newNoteImg = $("#newNoteImg");
    var historyViewImg = $("#viewHistoryImg");
    var searchUsersImg = $("#searchUsersImg");
    var newNote = $("#newNote");
    var userDatabase = $("#searchUsersDiv");
    var panelElementsList = [newNote, userDatabase];
    var findUserField = $("#findUserField");
    var searchButton = $("#searchButton");
    whiteBorderAnimation(newNoteImg);
    whiteBorderAnimation(historyViewImg);
    whiteBorderAnimation(searchUsersImg);
    newNote.hide();
    userDatabase.hide();
    addPanelClickEvent(newNoteImg, newNote);
    addPanelClickEvent(searchUsersImg, userDatabase);
    
    function addPanelClickEvent(clickElement, toggleElement) {
        $(clickElement).click(function() {
            closeOthers(toggleElement);
            toggleElement.toggle();
        }); 
    }

    //close elements except the one defined in the parameter
    function closeOthers(element) {
        for (var i = 0; i < panelElementsList.length; i++) {
            if (panelElementsList[i] !== element) {
                panelElementsList[i].hide();
            }
        }
    }
    
    function whiteBorderAnimation(x) {
        $(x).mouseenter(function() {
            $(this).css('border', "solid 2px white");
            $(this).css('border-radius', "10px");
        }); 
        $(x).mouseleave(function() {
            $(this).css('border', "none");
        }); 
    }
    
    //category hover animations
    $(".categoryButton").mouseenter(function() {
        $(this).css('background-color', "white");
        $(this).css('color', "black");
    }); 
    $(".categoryButton").mouseleave(function() {
        $(this).css('background-color', "#4286f4");
        $(this).css('color', "white");
    }); 
    
    //detecting when user presses enter on the search
    $(findUserField).keypress(function(e) {
        if(e.which === 13) {
            searchButton.click();
        }
    });

    $(searchButton).click(function() {
        alert("you pressed find!");
        sendRequest("findUser");
    }); 
    
    //AJAX request function for sending requests
    function sendRequest(action) {
        var url;
        if (action==="findUser") {
            var entry = findUserField.val();
            if (/^\w+$/.test(entry)===true) {
                url = "resources/users/find/"+entry;
            } else {
               entry=""; 
            }
            url = "resources/users/find/"+entry;
        } 
        req = new XMLHttpRequest();
        req.open("POST", url, true);
        req.onreadystatechange = callback;
        req.send(null);
    }
    
    function callback() {
        if (req.readyState === 4) {
            if (req.status === 200) {
                alert("req.responseXML");
            }
        }
    }
    
    function parseSearchResults() {
        
    }
    
};

$(document).ready(main);


