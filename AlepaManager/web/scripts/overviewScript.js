/* global req, responseXML */

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
    var note = $("#note");
    whiteBorderAnimation($("#bellArea"));
    whiteBorderAnimation($(".rightPanel"));
    whiteBorderAnimation($('#noteTable td'));
    newNote.hide();
    userDatabase.hide();
    //note.hide();
    $("#notificationWindow").hide();
    addPanelClickEvent(newNoteImg, newNote);
    addPanelClickEvent(searchUsersImg, userDatabase);
    
    refreshNoteClickEvents();
    var storedUser = sessionStorage.getItem('loggedInUser');
    $("#newNoteAuthor").val(storedUser);
    var storedUserID;
    setStoredUserID();
    
    function setStoredUserID() {
        var action = "findUser";
        var type="GET";
        var url = "resources/users/find/"+storedUser.toString().split(" ")[1];
        sendRequest(type, url, action);
        req = new XMLHttpRequest();
        req.open(type, url, true);
        req.onreadystatechange = function() {
            if (req.readyState===4) {
                var users = req.responseXML.getElementsByTagName("useris")[0];
                var user = users.childNodes[0];
                storedUserID = user.getElementsByTagName("id")[0].childNodes[0].nodeValue;
                storedUserID = Integer.parseInt(storedUserID);
            }
        };
        req.send(null);
    }
    
    function addPanelClickEvent(clickElement, toggleElement) {
        $(clickElement).click(function() {
            closeOthers(toggleElement);
            toggleElement.toggle();
        }); 
    }
    function refreshNoteClickEvents() {
        $(".individualNote").click(function() {
            var action="getNote";
            var type="GET";
            var title = $(this).text();
            var url = "resources/activenotes/"+title;
            sendRequest(type, url, action);
        }); 
        $("#note button").click(function() {
            note.hide();
        }); 
    }
    
    $(".topAnimation").click(function() {
        var name = ($(this).text());
        $("#topInfoBar").animate({
                height: "-=5em"
            }, 250, function() {
                $("#topInfoBar p").text("Category: " + name);
            });
        $("#topInfoBar").animate({
                height: "+=5em"
            }, 250);
    });
    
    $("#bellArea").click(function() {
        $("#notificationWindow").show();
    });
    $("#bellArea").mouseleave(function() {
        if ($('#notificationWindow:hover').length === 0 && $('#notificationContainer:hover').length === 0) {
            $("#notificationWindow").hide();
        }
    }); 
    $("#notificationWindow").mouseleave(function() {
        if ($('#bellArea:hover').length === 0
                && $('#notificationContainer:hover').length === 0) {
            $("#notificationWindow").hide();
        }
    }); 
    $("#notificationContainer").mouseleave(function() {
        if ($('#bellArea:hover').length === 0
                && $('#notificationWindow:hover').length === 0) {
            $("#notificationWindow").hide();
        }
    }); 


    //close elements except the one defined in the parameter
    function closeOthers(element) {
        for (var i = 0; i < panelElementsList.length; i++) {
            if (panelElementsList[i] !== element) {
                panelElementsList[i].hide();
            }
        }
    }
    
    function whiteBorderAnimation(x) {
        $(x).css('border', "solid 0.15em rgba(255, 255, 255, 0.0)");
        $(x).css('border-radius', "10px");
        $(x).mouseenter(function() {
            $(this).css('border', "solid 0.15em white");
        }); 
        $(x).mouseleave(function() {
            $(x).css('border', "solid 0.15em rgba(255, 255, 255, 0.0)");
        }); 
    }
    
    //category hover animations
    $(".categoryButton").mouseenter(function() {
        $(this).css('background-color', "white");
        $(this).css('color', "black");
    }); 
    $(".categoryButton").mouseleave(function() {
        $(this).css('background-color', "#e21f25");
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
        var action="addNote";
        var type="POST";
        var url;
        if (noteFieldsOK()===true) {
            url = checkNoteFields();
            sendRequest(type, url, action);
            alert("new note added!");
        } else {
            alert("Please fill out all the required fields!");
        }
    }); 
    
    $(historyViewImg).click(function() {
        var action="getHistory";
        var type="GET";
        var url = "resources/history/" + storedUserID;
        sendRequest(type, url, action);
    }); 
    
    $(searchButton).click(function() {
        var action = "findUser";
        var type="GET";
        var entry = findUserField.val();
        if (/^\w+$/.test(entry)===false) {
            entry="";
        }
        var url = "resources/users/find/"+entry;
        sendRequest(type, url, action);
    }); 
    
    //AJAX request function for sending requests
    function sendRequest(type, url, action) {
        req = new XMLHttpRequest();
        req.open(type, url, true);
        req.onreadystatechange = function() {
            callback(action);
        };
        req.send(null);
    }
    
    function callback(action) {
        if (req.readyState === 4) {
            if (req.status === 200) {
                if (action === "findUser") {
                    processSearchResults(req.responseXML);
                } else if (action === "addNote") {
                    addNoteToView(req.responseText);
                } else if (action==="getNote") {
                    changeNote(req.responseXML);
                } else if (action==="getHistory") {
                    changeAreaToHistory(req.responseXML);
                } 
            }
        }
    }
    
    function changeAreaToHistory(responseXML) {
        clearNotesArea();  
        var notes = responseXML.getElementsByTagName("notes")[0];
        for (var loop = 0; loop < notes.childNodes.length; loop++) {
            var note = notes.childNodes[loop];
            var title = note.getElementsByTagName("title")[0];
            title = title.childNodes[0].nodeValue;
            addNoteToView(title);
        }
    }
    
    //This Method removes table rows and table data from the table area
    function clearNotesArea() {
        $("#noteTable tr").remove();
        $("#noteTable td").remove();
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
        var url = "resources/activenotes/"+title+"/"+target+"/"+author+"/"+message+"/"+deadline+"/"+category;
        return url;
    }
    
    function addNoteToView(title) {
        if ($("#noteTable tr").length === 0) {
            $("#noteTable").append("<tr><td class='individualNote'><p class='noteText'>"+title+"</p></td></tr>");
        } else {
            var lastRowLength = $( "#noteTable tr:last td" ).length;
            if (lastRowLength === 6) {
                $('#noteTable tr:last').after("<tr><td class='individualNote'><p class='noteText'>"+title+"</p></td></tr>");
            } else {
                $('#noteTable tr:last td:last').after("<td class='individualNote'><p class='noteText'>"+title+"</p></td>");
            }
        }
        whiteBorderAnimation($('#noteTable td'));
        refreshNoteClickEvents();
    }
    
    function changeNote(responseXML) {
        var noteObject = responseXML.getElementsByTagName("note")[0];
        var title = noteObject.getElementsByTagName("title")[0];
        var target = noteObject.getElementsByTagName("targetUser")[0];
        var author = noteObject.getElementsByTagName("author")[0];
        var message = noteObject.getElementsByTagName("message")[0];
        var deadline = noteObject.getElementsByTagName("deadline")[0];
        var category = noteObject.getElementsByTagName("category")[0];
        title = title.childNodes[0].nodeValue;
        target = target.childNodes[0].nodeValue;
        author = author.childNodes[0].nodeValue;
        message = message.childNodes[0].nodeValue;
        deadline = deadline.childNodes[0].nodeValue;
        category = category.childNodes[0].nodeValue;
        $("#noteTitle").text(title);
        $("#noteTarget").text("Targeted to: " + target);
        $("#noteAuthor").text("Sent by " + author);
        $("#noteMessage").text("Message: " + message);
        $("#noteDueDate").text("Due by: " + deadline);
        $("#noteCategory").text("Category: " + category);
        note.show();
    }
};

$(document).ready(main);


