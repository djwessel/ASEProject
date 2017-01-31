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
      var cDiv = $('<a href="/course.jsp?courseId=' + course.id + '" class="course">' + course.title + '</a>').appendTo($('#courses'));
    });
  });

  if (Cookies.get('user')) {
    if (Cookies.get("userType") === "student") {
      // Load user's groups
      $.get('/rest/user/' + Cookies.get('user') + '/attendances', function(data) {
        for (var course in data) {
          var rDiv = $('<a href="/attendance.jsp?groupId=' + data[course].id + '" class="record">' + course + ' (' + data[course].name + ')</a>').appendTo($('#records'));
        }
      });
    }
    else {
      // Show create course button
      $('#createCourseBtn').show().click(function() {
        $(this).hide();
        $('#courseCreate').show();
      })
    }
  }
});
