$(document).ready(function() {
  $('#userLogin').submit(function() {
    $.post('/rest/user/login', $(this).serialize()).done(function(data) {
      console.log("success", data);
      window.location = '/success.jsp';
    }).fail(function(xhr) {
      console.log("fail", xhr);
      alert("Sign in failed");
    });
    return false;
  });

});
