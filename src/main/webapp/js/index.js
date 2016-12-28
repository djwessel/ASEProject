$(document).ready(function() {
  $('#userSignup').submit(function() {
    $.post('/rest/user', $(this).serialize()).done(function(data) {
      console.log("success", data);
      window.location = '/success.jsp';
    }).fail(function(xhr) {
      console.log("fail", xhr);
      alert("Sign up failed");
    });
    return false;
  });

  $.get('/rest/course/6016527627190272', function(data) {
    console.log(data);
  });

});
