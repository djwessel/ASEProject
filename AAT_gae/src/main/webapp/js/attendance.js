$(document).ready(function() {
  var groupId = QueryString.groupId;
  var courseId = QueryString.courseId;

  if (Cookies.get('userType') !== 'student') {
    window.location.replace('/course.jsp?courseId=' + courseId);
  }

  $('#calendar').fullCalendar({ aspectRatio: 3 });

  $.get('/rest/course/' + courseId, function(data) {
    $('#title').text(data.title);
    $('#reqAtten').text(data.reqAtten);
    $('#reqPresent').text(data.reqPresent);
  }).fail(function() {
    $('body').text('Attendance Record does not exist');
  });

  $.get('/rest/user/' + Cookies.get('user') + '/course/' + courseId + '/group/' + groupId + '/attendance', function(data) {
    $('#attenCount').text(data.attendance.length);
    $('#presentCount').text(data.presentation.length);

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
