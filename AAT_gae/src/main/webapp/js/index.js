$(document).ready(function() {
  $('#courseCreate').submit(function() {
    $.post('/rest/course', $(this).serialize()).done(function(data) {
      window.location.reload();
    }).fail(function(xhr) {
      alert("Unable to create course");
    });

    return false;
  });

  $.get('/rest/courses', function(data) {
    data.forEach(function(course) {
      var cDiv = $('<a href="/course.jsp?courseId=' + course.id + '" class="course">' + course.title + '</a>').appendTo($('#courses'));
    });
  });

  if (Cookies.get('user')) {
    if (Cookies.get("userType") === "student") {
      $.get('/rest/user/' + Cookies.get('user') + '/attendances', function(data) {
        for (var course in data) {
          if (!data.hasOwnProperty(course)) continue;

          var rDiv = $('<a href="/attendance.jsp?groupId=' + data[course].id + '" class="record">' + course + ' (' + data[course].name + ')</a>').appendTo($('#records'));
        }
      
      });
    }
    else {
      $('#createCourseBtn').show().click(function() {
        $(this).hide();
        $('#courseCreate').show();
      })
    }
  }
});
