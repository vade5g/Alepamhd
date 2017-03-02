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
    var searchResultsList = $("#searchResultList");
    var submitNoteButton = $("#submitNote");
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
        if (e.which === 13) {
            e.preventDefault();
            searchButton.click();
        }
    });
    
    $(submitNoteButton).click(function() {
        sendRequest("addNote");
    }); 
    
    $(searchButton).click(function() {
        sendRequest("findUser");
    }); 
    
    //AJAX request function for sending requests
    function sendRequest(action) {
        var url, type;
        if (action==="findUser") {
            type="GET";
            var entry = findUserField.val();
            if (/^\w+$/.test(entry)===false) {
                entry="";
            }
            url = "resources/users/find/"+entry;
        } else if (action==="addNote") {
            type="POST";
            if (noteFieldsOK()===true) {
                url = checkNoteFields();
            } else {
                alert("Please fill out all the required fields!");
                return;
            }
        }
        req = new XMLHttpRequest();
        req.open(type, url, true);
        req.onreadystatechange = callback(action);
        req.send(null);
    }
    
    function callback(action) {
        if (req.readyState === 4) {
            if (req.status === 200) {
                if (action === "findUser") {
                    processSearchResults(req.responseXML);
                } else if (action === "addNote") {
                    addNoteToView();
                }
            }
        }
    }
    
    function processSearchResults(responseXML) {
        //clear the previous results
        clearTable();
        // no matches returned
        var users;
        if (responseXML === null) {
            return false;
        } else {
            users = responseXML.getElementsByTagName("useris")[0];
            for (var loop = 0; loop < users.childNodes.length; loop++) {
                var user = users.childNodes[loop];
                var firstname = user.getElementsByTagName("firstname")[0];
                var lastname = user.getElementsByTagName("lastname")[0];
                appendUserListElement(firstname.childNodes[0].nodeValue,
                                      lastname.childNodes[0].nodeValue);
            }
        }
    }
    
    function appendUserListElement(firstname, lastname) {
        searchResultsList.append('<li>'+firstname+" "+lastname+'</li>');
    }
    
    function clearTable() {
        if (searchResultsList.children().length > 0) {
            searchResultsList.empty();
        }
    }
    
    function noteFieldsOK() {
        if ($("#newNoteTitle").val().trim()===""
          || $("#newNoteAuthor").val().trim()===""
          || $("#newNoteMessage").val().trim()===""
          || $("#registerCategoryOptions").val()===null) {
            return false;
        }
        return true;
    }
    
    function checkNoteFields() {
        //initialize as none, if the field is not empty it's replaced anyway
        //on the backend side "none" is puts that variable as null
        var title="none";var target="none"; var message="none";
        var deadline="none"; var category="none"; var author="none";
        if ($("#newNoteTarget").val().trim()!=="") {
            target = $("#newNoteTarget").val();
        }
        if ($("#newNoteDeadline").val().trim()!=="") {
            deadline = $("#newNoteDeadline").val();
        }
        title = $("#newNoteTitle").val();
        author = $("#newNoteAuthor").val();
        message = $("#newNoteMessage").val();
        category = $("#registerCategoryOptions").val();
        var url = "resources/activenotes/"+title+"/"+target+"/"+author+"/"+message+"/"
                    + deadline+"/"+category;
        return url;
    }
    
    function addNoteToView() {
        $("#noteTable: last tr:last").append('<td>asdfasdfasdf</li>');
    }
    
};

$(document).ready(main);


