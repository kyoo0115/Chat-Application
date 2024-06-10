let stompClient = null;
let token = sessionStorage.getItem("token");
let username = sessionStorage.getItem("username");
let selectedRoomCode = null;

function setConnected(connected) {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  $("#enter").prop("disabled", !connected || !selectedRoomCode);
  $("#send").prop("disabled", true);
  if (connected) {
    $("#conversation").show();
  } else {
    $("#conversation").hide();
  }
  $("#greetings").val('');
}

function connect() {
  if (!token) {
    console.error("No token found. Please log in.");
    alert("No token found. Please log in.");
    return;
  }

  stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/ws/chat',
    connectHeaders: {
      Authorization: `Bearer ${token}`
    },
    debug: (str) => {
      console.log(str);
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000
  });

  stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    if (selectedRoomCode) {
      subscribeToRoom(selectedRoomCode);
    }
  };

  stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
  };

  stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
  };

  stompClient.activate();
}

function disconnect() {
  console.log("Disconnecting...");
  if (stompClient !== null) {
    stompClient.deactivate();
  }
  setConnected(false);
  console.log("Disconnected");
}

function enterChatRoom() {
  console.log("Entering chat room...");
  if (!selectedRoomCode) {
    alert("Please select a chat room first.");
    return;
  }
  const enterMessage = `${username} has entered the chat room.`;
  stompClient.publish({
    destination: "/app/chat/enter",
    body: JSON.stringify({
      'roomCode': selectedRoomCode,
      'sender': username,
      'message': enterMessage,
      'type': 'ENTER'
    })
  });
  showGreeting(enterMessage); // Append the enter message to the text area
  $("#send").prop("disabled", false);
  $("#enter").prop("disabled", true); // Disable the enter button after entering the room
}

function sendMessage() {
  console.log("Sending message...");
  if (!selectedRoomCode) {
    alert("Please select a chat room first.");
    return;
  }
  const chatMessage = $("#message").val();
  stompClient.publish({
    destination: "/app/chat/send",
    body: JSON.stringify({
      'roomCode': selectedRoomCode,
      'sender': username,
      'message': chatMessage,
      'type': 'CHAT'
    })
  });
  $("#message").val('');
}

function showGreeting(message) {
  $("#greetings").val($("#greetings").val() + message + "\n");
}

function subscribeToRoom(roomCode) {
  if (stompClient && stompClient.connected) {
    stompClient.subscribe(`/topic/chat/room/${roomCode}`, (message) => {
      const parsedMessage = JSON.parse(message.body);
      if (parsedMessage.type === 'CHAT') {
        showGreeting(`${parsedMessage.sender}: ${parsedMessage.message}`);
      } else {
        showGreeting(parsedMessage.message);
      }
    });
  }
}

function logout(event) {
  console.log("Logging out...");
  event.preventDefault();
  sessionStorage.removeItem("token");
  sessionStorage.removeItem("username");
  window.location.href = "/";
}

function createChatRoom(event) {
  event.preventDefault();
  const roomName = prompt("Enter chat room name:");
  if (!roomName) return;

  console.log("Creating chat room with token:", token);

  fetch('http://localhost:8080/api/chatroom/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({ roomName })
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Failed to create chat room');
    }
    return response.json();
  })
  .then(data => {
    alert("Chat room created successfully");
    loadChatRooms();
  })
  .catch(error => {
    alert("Error creating chat room");
  });
}

function loadChatRooms() {
  console.log("Loading chat rooms with token:", token);
  if (!token) {
    console.error("No token found. Please log in.");
    alert("No token found. Please log in.");
    return;
  }

  fetch('http://localhost:8080/api/chatroom/room', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Failed to load chat rooms');
    }
    return response.json();
  })
  .then(data => {
    $("#chatroom-list").html('');
    data.forEach(room => {
      $("#chatroom-list").append(`
        <li class="list-group-item" data-roomcode="${room.roomCode}">
          ${room.roomName}
          <button class="btn btn-secondary btn-sm float-right add-member-button">Add Member</button>
        </li>
      `);
    });
    $("#chatroom-list li").click(function() {
      selectedRoomCode = $(this).data('roomcode');
      $("#chatroom-list li").removeClass('active');
      $(this).addClass('active');
      $("#enter").prop("disabled", false);
    });
    $(".add-member-button").click(function(event) {
      event.stopPropagation();
      const roomCode = $(this).closest("li").data("roomcode");
      addMemberToChatRoom(roomCode);
    });
  })
  .catch(error => {
    alert("Error loading chat rooms");
  });
}

function addMemberToChatRoom(roomCode) {
  const memberUsername = prompt("Enter username to add to chat room:");
  if (!memberUsername) return;

  fetch('http://localhost:8080/api/chatroom', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({
      roomCode: roomCode,
      username: memberUsername
    })
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Failed to add member to chat room');
    }
    return response.json();
  })
  .then(data => {
    alert("Member added to chat room successfully");
  })
  .catch(error => {
    alert("Error adding member to chat room");
  });
}

$(function () {
  $("#welcome-username").text(username);
  $("#connect").click((e) => {
    e.preventDefault();
    connect();
  });
  $("#disconnect").click((e) => {
    e.preventDefault();
    disconnect();
  });
  $("#enter").click((e) => {
    e.preventDefault();
    enterChatRoom();
  });
  $("#send").click((e) => {
    e.preventDefault();
    sendMessage();
  });
  $("#logout").click(logout);
  $("#create-chatroom").click(createChatRoom);
  $("#view-chatrooms").click(loadChatRooms);
});
