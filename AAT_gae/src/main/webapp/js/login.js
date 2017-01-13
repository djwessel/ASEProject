$(document).ready(function() {
  $('#userSignup').submit(function() {
    var formData = $(this).serialize();
    $.post('/rest/user', formData).done(function(data) {
      login(formData);
    }).fail(function(xhr) {
      console.log("fail", xhr);
      alert("Sign up failed");
    });

    return false;
  });

  $('#userLogin').submit(function() {
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
      $('#userWelcome').text('Welcome: ' + data.firstName + ' ' + data.lastName);
    });
    $('#logout').show();
  }
  else {
    $('#userSignup').show();
    $('#userLogin').show();
  }
});

function login(data) {
    $.post('/rest/user/login', data).done(function(data) {
      Cookies.set('user', data);
      window.location.reload();
    }).fail(function(xhr) {
      console.log("fail", xhr);
      alert("Login failed");
    });
}
