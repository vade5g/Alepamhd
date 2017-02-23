var main = function() {
    // hover animations for new note and history images
    var newNoteImg = $("#newNoteImg");
    var historyViewImg = $("#viewHistoryImg");
    whiteBorderAnimation(newNoteImg);
    whiteBorderAnimation(historyViewImg);
    
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
};

$(document).ready(main);


