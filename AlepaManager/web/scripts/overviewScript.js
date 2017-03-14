/* global req, responseXML */

var main = function() {
//declarations for commonly used elements for easier future selection
    var shadow = $("#shade");
    var newNoteImg = $("#writeMessage");
    var historyViewImg = $("#viewHistory");
    var searchUsersImg = $("#searchUsers");
    var newNote = $("#newNote");
    var userDatabase = $("#searchUsersDiv");
    var userInfo = $("#infoArea");
    var note = $("#note");
    var panelElementsList = [newNote, userDatabase, note];
    var findUserField = $("#findUserField");
    var searchButton = $("#searchButton");
    var searchResultsList = $("#searchResultList");
    var submitNoteButton = $("#submitNote");
    whiteBorderAnimation($("#bellArea"));
    whiteBorderAnimation($(".rightPanel"));
    whiteBorderAnimation($('#noteTable td'));
    
    //set user's name in top right based on login
    $("#loggedUser").text(sessionStorage.getItem("loggedInUser"));
    
    //hide stuff that gets toggled from menus
    shadow.hide();
    newNote.hide();
    userDatabase.hide();
    userInfo.hide();
    note.hide();
    $("#notificationWindow").hide();
    
    //add toggle onclick events to newnote and search and their close-buttons
    addPanelClickEvent(newNoteImg, newNote);
    addPanelClickEvent(searchUsersImg, userDatabase);
    
    //when search is clicked straight from panel, it's a bit different
    $(searchUsersImg).click(function() {
        $("#searchUsersDiv").show();
        $("#sendNoteTagLabel").text("Selected:");
        $("#sendNoteToButton").text("User info");
    });
    
    //close button for search window
    $("#searchUsersDiv .closeButton").click(function() {
        $(userDatabase).hide();
        if ($(newNote).is(":hidden")) {
            shadow.hide();
        } 
    });
    
    //close button for newnote
    $("#newNote .closeButton").click(function() {
        $(newNote).hide();
        shadow.hide();
    }); 
    
    //when clicking on the logged in user, display information about them
    $("#loggedUser").click(function() {
        var action = "findUserInfoSelf";
        var type="GET";
        var entry = sessionStorage.getItem("loggedInUser");
        var url = "resources/users/find/"+entry;
        sendRequest(type, url, action);
    }); 
    
    //give the initially visible notes their click events
    refreshNoteClickEvents();
    
    //store name of logged in user
    var storedUser = sessionStorage.getItem('loggedInUser');
    //new note author is always the logged in user, unmodifiable
    $("#newNoteAuthor").val(storedUser);
    var storedUserID;
    
    
    //     THIS SECTION IS A BIT REPETITIVE AS I DIDN'T HAVE THE TIME TO
    //     CREATE A FUNCTION THAT TAKES A NEWLY CREATED XMLHTTPREQUEST OBJECT
    //     AS PARAMETER SO THAT THE INITIAL QUERIES MIGHT ALL BE LAUNCHED AT ONCE
    
    //refresh notifications of user
    var type="GET";
    var storedUser = sessionStorage.getItem('loggedInUser');
    var url = "resources/users/find/"+storedUser;
    var req1 = new XMLHttpRequest();
    req1.open(type, url, true);
    req1.onreadystatechange = function() {
        if (req1.readyState===4) {
            if (req1.status === 200) {
                var users = req1.responseXML.getElementsByTagName("useris")[0];
                var user = users.childNodes[0];
                var notifications = user.getElementsByTagName("notifications")[0].childNodes[0].nodeValue;
                updateNotifications(notifications);
            }
        }
    };
    req1.send(null);
    
    //GET the user's ID based on the login firstname and lastname
    var type="GET";
    var storedUser = sessionStorage.getItem('loggedInUser');
    var url = "resources/users/find/"+storedUser;
    var req2 = new XMLHttpRequest();
    req2.open(type, url, true);
    req2.onreadystatechange = function() {
        if (req2.readyState===4) {
            if (req2.status === 200) {
                var users = req2.responseXML.getElementsByTagName("useris")[0];
                var user = users.childNodes[0];
                storedUserID = user.getElementsByTagName("id")[0].childNodes[0].nodeValue;
                storedUserID = parseInt(storedUserID);
                sessionStorage.setItem('storedUserID', storedUserID);
                //get initial view:
                var type="GET";
                var url = "resources/activenotes/personal/"+sessionStorage.getItem('storedUserID');
                var req3 = new XMLHttpRequest();
                req3.open(type, url, true);
                req3.onreadystatechange = function() {
                    if (req3.readyState===4) {
                        if (req3.status === 200) {
                            changeArea(req3.responseXML);
                        }
                    }
                };
                req3.send(null);
            }
        }
    };
    req2.send(null);

    //give the basic toggle action click event for the 1st paremeter(clickelement)
    function addPanelClickEvent(clickElement, toggleElement) {
        $(clickElement).click(function() {
            closeOthersExcept(toggleElement);
            shadow.toggle();
        }); 
    }
    
    //close elements except the one defined in the parameter
    function closeOthersExcept(element) {
        for (var i = 0; i < panelElementsList.length; i++) {
            if (panelElementsList[i] !== element) {
                panelElementsList[i].hide();
            }
        }
        element.toggle();
    }
    
    //select all the visible notes and give them their click-event
    //this needs to be done so that newly created notes work the same
    function refreshNoteClickEvents() {
        $(".individualNote").click(function() {
            var action="getNote";
            var type="GET";
            var title = $(this).text();
            var url = "resources/activenotes/"+title;
            sendRequest(type, url, action);
            shadow.show();
        }); 
        //at the moment, both send and close do same thing
        $("#closeButton").click(function() {
            note.hide();
            shadow.hide();
        }); 
    }
    
    //clicking DONE on the note
    $("#note .doneBtn").click(function() {
        var target = $("#noteTarget").text();
        var category = $("#noteCategory").text();
        //if the user is not a manager or the person targeted in the note, deny access
        if (category.includes("manager") && target.includes(sessionStorage.getItem("loggedInUser"))===false) {
            alert("You are not authorized to complete this task!\n"
                    + "Only the manager can complete tasks in the manager category.");
            return;
        }
        //if the user clicked done on a finished note, ask for permanent destroy
        if ($("#topInfoBar p").text().includes("history")) {
            var r = confirm("Are you sure you want to permanently remove note?");
        } else {
            var r = confirm("Are you sure you want to mark note as finished?");
        }
        //send disable request to backend, note is deleted if it's inactive
        if (r === true) {
            var action="disable";
            var type="GET";
            var parentDiv = $(this).parent();
            var title = parentDiv.children().first().text();
            var url = "resources/activenotes/disable/"+title;
            sendRequest(type, url, action);
            shadow.hide();
            note.hide();
        } 
    });
    
    //redirecting user back to login when clicking logout
    $(".logoutButton").click(function() {
        window.location.href = "index.html";
    });
    
    //what happens when you click a category button:
    $(".topAnimation").click(function() {
        var name = ($(this).text());
        //set the current category in browser storage
        sessionStorage.setItem('currentCategory', name);
        
        //animate top info bar and change its text for cool visuals
        $("#topInfoBar").animate({
                height: "-=5em"
            }, 150, function() {
                $("#topInfoBar p").text("Category: " + name);
            });
        $("#topInfoBar").animate({
                height: "+=5em"
            }, 150);
        var url, action;
            //update notes by category if category, personal if "your view"
            if (name==="Your view") {
                url = "resources/activenotes/personal/"+storedUserID;
                action="getPersonalNotes";
            } else {
                action="getNotes";
                var type="GET";
                url = "resources/activenotes/category/"+name;
            }
        var type="GET";
        sendRequest(type, url, action);
    });
    
    //Bell area is a separate div to make the area as a whole easier to interact with
    //notification window disappears if cursor isn't on bell area or notif. window
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
    
    //when clicking notification window, send call to backend to reset notifications by id
    $("#notificationWindow").click(function() {
        $("#notificationNumber").text(0);
        $("#notificationWindow").text(0 + " new personal notes");
        var type="GET"; var url="resources/users/resetnotifications/"+sessionStorage.getItem("storedUserID");
        var action="resetNotifications";
        sendRequest(type, url, action);
    });

    //give an element white border animations
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
    
    //category hover change animations
    $(".categoryButton").mouseenter(function() {
        $(this).css('background-color', "white");
        $(this).css('color', "black");
    }); 
    $(".categoryButton").mouseleave(function() {
        $(this).css('background-color', "#e21f25");
        $(this).css('color', "white");
    }); 
    
    //interpret enter key press as the same as clicking "search"
    $(findUserField).keypress(function(e) {
        if (e.which === 13) {
            e.preventDefault();
            searchButton.click();
        }
    });
    
    //what happens when user clicks the plus sign 
    $("#plussign").click(function() {
        $("#searchUsersDiv").show();
        $("#sendNoteTagLabel").text("Tag person:");
        $("#sendNoteToButton").text("Select");
    });

    //what happens when user clicks the "empty" button in search
    $("#clearSearchTag").click(function() {
        $("#sendNoteField").val("");
    });
    
    //what happens when user clicks "send" on newNote
    $(submitNoteButton).click(function() {
        var action="addNote";
        var type="POST";
        var url;
        if (noteFieldsOK()==="ok") {
            url = checkNoteFields();
            //checkNoteFields gives us a finished URL for new note
            sendRequest(type, url, action);
            alert("new note added!");
            shadow.hide();
            newNote.hide();
            $('#categoryBar button:contains('+$("#CategoryOptions").val()+')').trigger("click");
        } else {
            alert(noteFieldsOK());
        }
    }); 
    
    //what happens when you click "view history" on the right side panel
    $(historyViewImg).click(function() {
        var category = sessionStorage.getItem('currentCategory');
        var action, url;
        storedUserID = sessionStorage.getItem('storedUserID');
        action="getHistory";
        //different REST address for user and category, check which one is used
        if (category==="Your view") {
            url = "resources/history/"+storedUserID;
        } else {
            url = "resources/history/category/"+category;
        }
        var type="GET";
        //if top bar doesnt contain "history", animate it
        if ($("#topInfoBar p").text().includes("history")===false) {
            $("#topInfoBar").animate({
                height: "-=5em"
            }, 150, function() {
                $("#topInfoBar p").text($("#topInfoBar p").text()+" (history)");
            });
            $("#topInfoBar").animate({
                height: "+=5em"
            }, 150);
        }
        sendRequest(type, url, action);
        
    }); 
    
    //what happens when user clicks "search" on search window
    $(searchButton).click(function() {
        var action = "findUser";
        var type="GET";
        var entry = findUserField.val();
        //regex check to see that only legal chars are used, replcae with empty if not
        if (/^[A-Za-z ]+$/.test(entry)===false) {
            entry="";
        }
        var url = "resources/users/find/"+entry;
        sendRequest(type, url, action);
    }); 
    
    //AJAX request function for sending requests
    function sendRequest(type, url, action) {
        console.log(action);
        req = new XMLHttpRequest();
        req.open(type, url, true);
        req.onreadystatechange = function() {
            callback(action);
        };
        req.send(null);
    }
    
    //change visual representation based on what action was given on click event
    function callback(action) {
        if (req.readyState === 4) {
            if (req.status === 200) {
                if (action === "findUser") {
                    processSearchResults(req.responseXML);
                } else if (action === "addNote") {
                    refreshCurrentNotes();
                } else if (action==="getNote") {
                    changeNote(req.responseXML);
                } else if (action==="getHistory") {
                    changeArea(req.responseXML);
                } else if (action==="getNotes") {
                    changeArea(req.responseXML);
                } else if (action==="getPersonalNotes") {
                    changeArea(req.responseXML);
                } else if (action==="disable") {
                    historyViewImg.trigger("click");
                } else if (action==="resetNotifications") {
                    $("#personalBar").children().first().trigger("click");
                } else if (action==="findUserInfo") {
                    updateUserInfo(req.responseXML);
                } else if (action==="findUserInfoSelf") {
                    updateUserInfo(req.responseXML, sessionStorage.getItem("loggedInUser"));
                }
            }
        }
    }
    
    //if new note added to a category that is currently open, retrieve whole set again
    function refreshCurrentNotes() {
        var action="getNotes";
        var type="GET";
        if (sessionStorage.getItem("currentCategory")==="Your view") {
            var url = "resources/activenotes/personal/"+sessionStorage.getItem("storedUserID");
        } else {
            var url = "resources/activenotes/category/"+sessionStorage.getItem("currentCategory");
        }
        sendRequest(type, url, action);
    }

    //change notesArea based on the list of notes that was retreived
    //each unique note is processed in AddNoteToView
    function changeArea(responseXML) {
        clearNotesArea();
        var notes = responseXML.getElementsByTagName("notes")[0];
        //loop through each unique note tag and get the attributes
        for (var loop = 0; loop < notes.childNodes.length; loop++) {
            var note = notes.childNodes[loop];
            var title = note.getElementsByTagName("title")[0];
            title = title.childNodes[0].nodeValue;
            var active = note.getElementsByTagName("active")[0];
            active = active.childNodes[0].nodeValue;
            var target = note.getElementsByTagName("targetUser")[0];
            target = target.childNodes[0].nodeValue;
            var expired = note.getElementsByTagName("expired")[0];
            expired = expired.childNodes[0].nodeValue;
            addNoteToView(title,active,target,expired);
            
        }
    }
    
    //This Method removes table rows and table data from the table area
    //called whenever the notesArea is refreshed, we remove all old ones and replace
    function clearNotesArea() {
        $("#noteTable tr").remove();
        $("#noteTable td").remove();
    }
    
    //changing the search window based on server response to user's search entry
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
                var category = user.getElementsByTagName("category")[0];
                appendUserListElement(firstname.childNodes[0].nodeValue,
                                      lastname.childNodes[0].nodeValue,
                                      category.childNodes[0].nodeValue);
            }
            //give each new name on the list a click events
            //also give them their CSS style on the fly
            $("li p").css({
                "display" : "inline-block",
                "cursor" : "pointer"
            });
            $("li p").mouseenter(function() {
                $(this).css({
                    "text-decoration" : "underline"
                });
            }); 
            $("li p").mouseleave(function() {
                $(this).css({
                    "text-decoration" : "none"
                });
            });
            $("li p").click(function() {
                $("#sendNoteField").val($(this).text());
            });
            $("#sendNoteToButton").click(function() {
                if ($("#newNote").is(":visible")===true) {
                    $("#newNoteTarget").val($("#sendNoteField").val());
                    userDatabase.hide();
                } else {
                    var entry = $("#sendNoteField").val();
                    if (entry!=="") {
                        var action = "findUserInfo";
                        var type="GET";
                        var entry = $("#sendNoteField").val();
                        var url = "resources/users/find/"+entry;
                        sendRequest(type, url, action);
                    }
                }
            });
        }
    }
    
    //update the userinfo window elements based on XML response
    function updateUserInfo(responseXML, name) {
        var users = responseXML.getElementsByTagName("useris")[0];
        var user = users.childNodes[0];
        var username = user.getElementsByTagName("username")[0].childNodes[0].nodeValue;
        var category = user.getElementsByTagName("category")[0].childNodes[0].nodeValue;
        var email = user.getElementsByTagName("email")[0].childNodes[0].nodeValue;
        if (name!==undefined) {
            $("#infoName").text(name);
        } else {
            $("#infoName").text($("#sendNoteField").val());
        }
        $("#infoUsername").text("Username: "+username);
        $("#infoCategory").text("Category: "+category);
        $("#infoEmail").text("E-mail: "+email);
        userInfo.show();
    }
    
    $("#infoArea button").click(function() {
        userInfo.hide();
    });
    
    //append a list element to user search
    function appendUserListElement(firstname, lastname) {
        searchResultsList.append('<li><p>'+firstname+" "+lastname+'</p></li>');
    }
    
    //clear search table entirely, called every time a new search is processed
    function clearTable() {
        if (searchResultsList.children().length > 0) {
            searchResultsList.empty();
        }
    }
    
    //function to check if the fields of new note are all ok
    function noteFieldsOK() {
        //return a custom warning if a field is not okay
        if ($("#newNoteTitle").val().trim()==="") {
            return "Please add a title!";
        } else if ($("#newNoteMessage").val().trim()==="") {
            return "Please enter a message!";
        } else if ($("#CategoryOptions").val()===null) {
            return "Please choose a category!";
        } else if ($("#newNoteDeadline").val()!=="") {
            var entry = $("#newNoteDeadline").val().trim();
            //regex test to make sure the date is sent to backend in such a form
            //that it may be utilized right off the bat by DateFormat
            entry = entry.replace(/(^\.+|\.+$)/mg, '');
            if (/(0[1-9]|[12][0-9]|3[01])\.(0[1-9]|1[012])\.(19|20)\d\d/.test(entry)===false) {
                return "Please enter a proper date!\nThe correct format is dd.mm.yyyy," 
                +"for example: 25.02.2019,\n"
                + "check also that the date you entered actually exists.";
            }
        }
        //success case
        return "ok";
    }

    //create and return URL based on the values in new note
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
        category = $("#CategoryOptions").val();
        var url = "resources/activenotes/"+title+"/"+target+"/"+author+"/"+message+"/"+deadline+"/"+category;
        return url;
    }
    
    //add one unique note to view
    function addNoteToView(title,active,target, expired) {
        //we need to know the state of these variables to choose correct image
        if (active==="true") {
            active=true;
        } else if (active==="false") {
            active=false;
        }
        if (expired==="true") {
            expired=false;
        } else if (expired==="false") {
            expired=true;
        }
        //get user from storage to make sure it comes through, this can bug out
        var storedUser = sessionStorage.getItem('loggedInUser');
        
        //test every possible scenario for the note, these have to all be checked
        //since each scenario has a customized image to signify that state
        if ($("#noteTable tr").length === 0) {
            if (active) {
                if (target !== storedUser && expired === true) {
                    $("#noteTable").append("<tr><td id='individualNote' class='individualNote'><p class='noteText'>" + title + "</p></td></tr>");
                } else if (target !== storedUser && expired === false) {
                    $("#noteTable").append("<tr><td id='individualNoteExp' class='individualNote'><p class='noteText'>" + title + "</p></td></tr>");
                } else if (target === storedUser && expired === true) {
                    $("#noteTable").append("<tr><td id='individualNotePe' class='individualNote'><p class='noteText'>" + title + "</p></td></tr>");
                } else if (target === storedUser && expired === false) {
                    $("#noteTable").append("<tr><td id='individualNotePeExp' class='individualNote'><p class='noteText'>" + title + "</p></td></tr>");
                }
            } else {
                if (target !== storedUser) {
                   $("#noteTable").append("<tr><td id='individualNoteCh' class='individualNote'><p class='noteText'>" + title + "</p></td></tr>"); 
                } else {
                   $("#noteTable").append("<tr><td id='individualNoteChPe' class='individualNote'><p class='noteText'>" + title + "</p></td></tr>");
                }
            }
            
        } else {
            var lastRowLength = $("#noteTable tr:last td").length;
            if (lastRowLength === 6) {
                if (active) {
                    if (target !== storedUser && expired === true) {
                        $('#noteTable tr:last').after("<tr><td id='individualNote' class='individualNote'><p class='noteText'>" + title + "</p></td></tr>");
                    } else if (target !== storedUser && expired === false) {
                        $('#noteTable tr:last').after("<tr><td id='individualNoteExp' class='individualNote'><p class='noteText'>" + title + "</p></td></tr>");
                    } else if (target === storedUser && expired === true) {
                        $('#noteTable tr:last').after("<tr><td id='individualNotePe' class='individualNote'><p class='noteText'>" + title + "</p></td></tr>");
                    } else if (target === storedUser && active === false) {
                        $('#noteTable tr:last').after("<tr><td id='individualNotePeExp' class='individualNote'><p class='noteText'>" + title + "</p></td></tr>");
                    }
                } else {
                    if (target !== storedUser) {
                        $('#noteTable tr:last').after("<tr><td id='individualNoteCh' class='individualNote'><p class='noteText'>" + title + "</p></td></tr>");
                    } else {
                        $('#noteTable tr:last').after("<tr><td id='individualNoteChPe' class='individualNote'><p class='noteText'>" + title + "</p></td></tr>");
                    }
                }
                
            } else {  
                if (active) {
                    if (target !== storedUser && expired === true) {
                        $('#noteTable tr:last td:last').after("<td id='individualNote' class='individualNote'><p class='noteText'>" + title + "</p></td>");
                    } else if (target !== storedUser && expired === false) {
                        $('#noteTable tr:last td:last').after("<td id='individualNoteExp' class='individualNote'><p class='noteText'>" + title + "</p></td>");
                    } else if (target === storedUser && expired === true) {
                        $('#noteTable tr:last td:last').after("<td id='individualNotePe' class='individualNote'><p class='noteText'>" + title + "</p></td>");
                    } else if (target === storedUser && expired === false) {
                        $('#noteTable tr:last td:last').after("<td id='individualNotePeExp' class='individualNote'><p class='noteText'>" + title + "</p></td>");
                    }
                } else {
                    if (target !== storedUser) {
                        $('#noteTable tr:last td:last').after("<td id='individualNoteCh' class='individualNote'><p class='noteText'>" + title + "</p></td>");
                    } else {
                        $('#noteTable tr:last td:last').after("<td id='individualNoteChPe' class='individualNote'><p class='noteText'>" + title + "</p></td>");
                    }
                }

            }
        }
        //give the notes their animations and click events
        whiteBorderAnimation($('#noteTable td'));
        refreshNoteClickEvents();
    }
    
    //The note is actually just one element, only the text is changed
    function changeNote(responseXML) {
        //parse the needed info from XML response and change note accordingly
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
        //when tect changed, show to user
        note.show();
    }
    
    //set the notification number
    function updateNotifications(number) {
        $("#notificationNumber").text(number);
        $("#notificationWindow").text(number + " new personal notes");
    }
    
};

$(document).ready(main);


