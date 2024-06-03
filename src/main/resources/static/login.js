let token = null;
let username = null;

function login(event) {
  event.preventDefault();
  const usernameInput = $("#username").val();
  const password = $("#password").val();

  fetch('http://localhost:8080/auth/sign-in', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ username: usernameInput, password })
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Login failed');
    }
    return response.json();
  })
  .then(data => {
    token = data.token;
    username = usernameInput; // Use the input username as it is already validated
    sessionStorage.setItem("token", token);
    sessionStorage.setItem("username", username);
    window.location.href = "/chat.html";
  })
  .catch(error => {
    $("#login-error").text("Invalid username or password");
  });
}

$(function () {
  $("#login-form").submit(login);
});
