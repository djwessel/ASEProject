$(document).ready(function() {
  var groupId = QueryString.groupId;

  $('#calendar').fullCalendar({ aspectRatio: 2.5 });

  $.get('/rest/user/' + Cookies.get('user') + '/group/' + groupId + '/attendance', function(data) {
    $('#attenCount').text('Attendance Count: ' + data.attendance.length)
    $('#presentCount').text('Presentation Count: ' + data.presentation.length)

    var events = []
    data.attendance.forEach(function(date) {
      events.push({
        title: 'Attended',
        start: moment(date),
        end: moment(date).add(1, 'week').format()
      });
    });
    data.presentation.forEach(function(date) {
      events.push({
        title: 'Presented',
        start: moment(date),
        end: moment(date).add(1, 'week').format(),
        color: 'purple'
      });
    });
    $('#calendar').fullCalendar('addEventSource',  events);
  });

});
