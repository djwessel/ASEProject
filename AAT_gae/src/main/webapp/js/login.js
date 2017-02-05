(function() {
  var isLoggingIn = false;
  $(document).ready(function() {
    // Sign up user submit handler
    $('#userSignup').validator({ disable: false }).submit(function() {
      var formData = $(this).serialize();
      $.post('/rest/user', formData).done(function(data) {
        login(formData);
      }).fail(function(xhr) {
        if (xhr.status === 409)
          alert('User with email already exists.');
        else
          alert('Unable to signup.');
      });
  
      return false;
    });

    // Log in user submit handler
    $('#userLogin').validator({ disable: false }).submit(function() {
      login($(this).serialize());
  
      return false;
    });

    // Logout button
    $('#logout').click(function() {
      $.ajax({
        url: '/rest/user/' + Cookies.get('user') + '/logout',
        type: 'DELETE',
        dataType: 'text',
        success: function(data) {
          Cookies.remove('user');
          window.location.reload();
        },
        error: function(xhr) {
          console.log('fail', xhr);
        }
      });
    });

    // If user signed in, display user name and the logout button, otherwise display the login button
    if (Cookies.get('user')) {
      $.get('/rest/user/' + Cookies.get('user')).done(function(data) {
        console.log(data);
        $('#userWelcome').text('Signed in as ' + data.firstName + ' ' + data.lastName);
      });
      $('#logout').show();
    }
    else {
      $('#loginModalBtn').show().click(function() {
        $('#modalBtns').show();
        $('#userSignup').hide();
        $('#userLogin').hide();
      });
    }

    // Buttons for user login modal dialog
    $('#newUserBtn').click(function() { 
      $(this).parent().hide();
      $('#loginModalLabel').text('New User');
      $('#userSignup').show();
    });
    $('#existUserBtn').click(function() { 
      $(this).parent().hide();
      $('#loginModalLabel').text('Existing User');
      $('#userLogin').show();
    });
  });

  // Logs in user
  function login(data) {
    if (!isLoggingIn) {
      isLoggingIn = true;
      $.post('/rest/user/login', data).done(function(data) {
        Cookies.set('user', data, { expires: 1/24 });
        window.location.reload();
      }).fail(function(xhr) {
        console.log("fail", xhr);
        alert("Login failed");
        isLoggingIn = false;
      });
    }
  }
})();
