let token = null;
let username = null;

function login(event) {
  event.preventDefault();
  const usernameInput = $("#username").val();
  const password = $("#password").val();

  fetch('http://http://ec2-3-38-102-180.ap-northeast-2.compute.amazonaws.com:8080/auth/sign-in', {
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
    username = usernameInput;
    sessionStorage.setItem("token", token);
    sessionStorage.setItem("username", username);
    console.log("Token stored in sessionStorage:", token);
    window.location.href = "/chat.html";
  })
  .catch(error => {
    $("#login-error").text("Invalid username or password");
  });
}

$(function () {
  $("#login-form").submit(login);
});
