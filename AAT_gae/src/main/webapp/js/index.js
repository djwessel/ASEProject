$(document).ready(function() {
  $('#courseCreate').submit(function() {
    $.post('/rest/course', $(this).serialize()).done(function(data) {
      console.log("success", data);
      window.location.reload();
    }).fail(function(xhr) {
      console.log("fail", xhr);
      alert("Unable to create course");
    });

    return false;
  });

  $.get('/rest/courses', function(data) {
    console.log(data);
    data.forEach(function(course) {
      var cDiv = $('<a href="/course.jsp?courseId=' + course.id + '" class="course">' + course.title + '</a>').appendTo($('#courses'));
    });
  });
});
