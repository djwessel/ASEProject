$(document).ready(function() {
  // Course Create form submit handler
  $('#courseCreate').validator({ disable: false }).submit(function() {
    $.post('/rest/course', $(this).serialize()).done(function(data) {
      window.location.reload();
    }).fail(function(xhr) {
      if (xhr.status === 409)
        alert('Course with given name already exists');
      else
        alert('Unable to create course');
    });

    return false;
  });

  // Load courses
  $.get('/rest/courses', function(data) {
    data.forEach(function(course) {
      var cRow = $('<tr><td><a href="/course.jsp?courseId=' + course.id + '">' + course.title + '</a></td></tr>').appendTo('#courses');
    });
  });

  if (Cookies.get('user')) {
    if (Cookies.get("userType") === "student") {
      // Load user's groups
      $.get('/rest/user/' + Cookies.get('user') + '/attendances', function(data) {
       data.forEach(function(data) { 
          var rRow = $('<tr></tr>').appendTo('#records');
          $('<td>' + data.courseName + '</td>').appendTo(rRow);
          $('<td><a href="/attendance.jsp?groupId=' + data.group.id + '&courseId=' + data.courseId + '" class="record">' + data.group.name + '</a></td>').appendTo(rRow);
        });
      });
    }
    else {
      // Show create course button
      $('#createCourseBtn').show();
    }
  }
});
