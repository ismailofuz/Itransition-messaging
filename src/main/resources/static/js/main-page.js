$(document).ready(function (options){
    
    $("#send-message").click(function(e){
        $("#message-form").css("visibility","visible")
        e.preventDefault();
    })


    let socket = new SockJS('/chat-endpoint');
    let stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/user/' + $("#username").text() + '/new-message', function (message) {
            let messageObj = JSON.parse(message.body);
            console.log(messageObj);

            let messageStr = '<a href="message/'+messageObj.id+'" class="list-group-item">\n' +
            '                           <span class="name">'+messageObj.senderName+'</span> <span class="" >'+messageObj.title+'</span>\n' +
            '                            <span class="badge">'+messageObj.sentTime+'</span>\n' +
            '                        </a>'
            $("#messages").prepend( messageStr);
        });
    });

    // AUTO COMPLETION

    $( function() {
        var availableTags
        function getUsernames(){
            let users = $("#tags").val().split(", ");
            let key = users[users.length-1];
            console.log(key)
            $.ajax({
                url: "users/get-user/"+key,
                type: 'GET',
                contentType: 'application/json; charset=utf-8',
                async:true
            }).done(function(response){
                console.log(response);
                availableTags = response
            })
        }
        function split( val ) {
            return val.split( /,\s*/ );
        }
        function extractLast( term ) {
            return split( term ).pop();
        }

        $("#tags").on( "keyup", function( event ) {
                getUsernames()
                if ( event.keyCode === $.ui.keyCode.TAB &&
                    $( this ).autocomplete( "instance" ).menu.active ) {
                    event.preventDefault();
                }
            })
            .autocomplete({
                minLength: 0,
                source: function( request, response ) {
                    console.log("------------------------------")
                    console.log(availableTags);
                    // delegate back to autocomplete, but extract the last term
                    response( $.ui.autocomplete.filter(
                        availableTags, extractLast( request.term ) ) );
                },
                focus: function() {
                    // prevent value inserted on focus
                    return false;
                },
                select: function( event, ui ) {
                    var terms = split( this.value );
                    // remove the current input
                    terms.pop();
                    // add the selected item
                    terms.push( ui.item.value );
                    // add placeholder to get the comma-and-space at the end
                    terms.push( "" );
                    this.value = terms.join( ", " );
                    return false;
                }
            });
    } );
    
})