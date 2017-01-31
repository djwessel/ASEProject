$(document).ready(function() {
  var groupId = QueryString.groupId;

  $('#calendar').fullCalendar({ aspectRatio: 3 });

  $.get('/rest/user/' + Cookies.get('user') + '/group/' + groupId + '/attendance', function(data) {
    $('#attenCount').text('Attendance Count: ' + data.attendance.length)
    $('#presentCount').text('Presentation Count: ' + data.presentation.length)

    var events = []
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
  });

});
