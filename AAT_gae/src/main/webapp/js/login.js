$(document).ready(function() {
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

  $('#userLogin').validator({ disable: false }).submit(function() {
    login($(this).serialize());

    return false;
  });

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

function login(data) {
    $.post('/rest/user/login', data).done(function(data) {
      Cookies.set('user', data, { expires: 1/24 });
      window.location.reload();
    }).fail(function(xhr) {
      console.log("fail", xhr);
      alert("Login failed");
    });
}
