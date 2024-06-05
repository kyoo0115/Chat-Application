$(function() {
  let usernameValid = false;
  let verificationNumberValid = false;

  $('#check-username').click(function() {
    const username = $('#username').val();
    $.ajax({
      url: '/auth/username-check',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({ username }),
      success: function(response) {
        usernameValid = true;
        $('#username-check-result').css('color', 'green').text('Username is available');
        checkFormValidity();
      },
      error: function(xhr, status, error) {
        usernameValid = false;
        $('#username-check-result').css('color', 'red').text('Username is already taken');
        checkFormValidity();
      }
    });
  });

  $('#send-verification-email').click(function() {
    const username = $('#username').val();
    const email = $('#email').val();
    $.ajax({
      url: '/auth/send-verification-email',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({ username, email }),
      success: function(response) {
        $('#email-check-result').css('color', 'green').text('Verification email sent');
      },
      error: function(xhr, status, error) {
        $('#email-check-result').css('color', 'red').text('Failed to send verification email');
      }
    });
  });

  $('#check-verification-number').click(function() {
    const username = $('#username').val();
    const email = $('#email').val();
    const verificationNumber = $('#verificationNumber').val();
    $.ajax({
      url: '/auth/check-verification-email',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({ username, email, verificationNumber }),
      success: function(response) {
        verificationNumberValid = true;
        $('#verification-check-result').css('color', 'green').text('Verification number is correct');
        checkFormValidity();
      },
      error: function(xhr, status, error) {
        verificationNumberValid = false;
        $('#verification-check-result').css('color', 'red').text('Incorrect verification number');
        checkFormValidity();
      }
    });
  });

  $('#signup-form').submit(function(event) {
    event.preventDefault();

    const username = $('#username').val();
    const name = $('#name').val();
    const password = $('#password').val();
    const email = $('#email').val();
    const birthDate = $('#birthDate').val();
    const verificationNumber = $('#verificationNumber').val();

    const signUpData = {
      username,
      name,
      password,
      email,
      birthDate,
      verificationNumber
    };

    $.ajax({
      url: '/auth/sign-up',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(signUpData),
      success: function(response) {
        window.location.href = '/chat.html';
      },
      error: function(xhr, status, error) {
        $('#signup-error').text('Sign-up failed: ' + xhr.responseText);
      }
    });
  });

  function checkFormValidity() {
    if (usernameValid && verificationNumberValid) {
      $('#signup-button').prop('disabled', false);
    } else {
      $('#signup-button').prop('disabled', true);
    }
  }
});
