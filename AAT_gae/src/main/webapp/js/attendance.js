$(document).ready(function() {
  var groupId = QueryString.groupId;
  var courseId = QueryString.courseId;

  // Only logged in students should be able to access this page
  if (Cookies.get('userType') !== 'student') {
    window.location.replace('/course.jsp?courseId=' + courseId);
  }

  // Create calendar.
  $('#calendar').fullCalendar({ aspectRatio: 3 });

  // Get information about course
  $.get('/rest/course/' + courseId, function(data) {
    $('#title').text(data.title);
    $('#reqAtten').text(data.reqAtten);
    $('#reqPresent').text(data.reqPresent);
  }).fail(function() {
    $('body').text('Attendance Record does not exist');
  });

  // Get attendance record information
  $.get('/rest/user/' + Cookies.get('user') + '/course/' + courseId + '/group/' + groupId + '/attendance', function(data) {
    $('#attenCount').text(data.attendance.length);
    $('#presentCount').text(data.presentation.length);

    // Set attendance and presentation data.
    var events = [];
    data.attendance.forEach(function(date) {
      events.push({
        title: 'Attended',
        start: moment(date).startOf('week').format('YYYY-MM-DD'),
        end: moment(date).endOf('week').add(1, 'day').format('YYYY-MM-DD')
      });
    });
    data.presentation.forEach(function(date) {
      events.push({
        title: 'Presented',
        start: moment(date).startOf('week').format('YYYY-MM-DD'),
        end: moment(date).endOf('week').add(1, 'day').format('YYYY-MM-DD'),
        color: 'purple'
      });
    });
    $('#calendar').fullCalendar('addEventSource',  events);
  }).fail(function() {
    $('bodý').text('Attendance Record does not exist');
  });

});
