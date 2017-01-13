$(document).ready(function() {
  var courseId = QueryString.courseId;
  var groupId = QueryString.groupId;

  $.get('/rest/course/' + courseId, function(data) {
    $('#title').text(data.title)
    $('#courseInfo').empty()
      .append($('<div>Required Attendance count: ' + data.reqAtten + '</div>'))
      .append($('<div>Required Presentation count: ' + data.reqPresent + '</div>'));
  }).fail(function() {
    $('body').text('COURSE DOES NOT EXIST');
  });

  $.get('/rest/course/' + courseId + '/groups', function(data) {
    var groupsDiv = $('#groups').empty();
    data.forEach(function(group) {
      var groupDiv = $('<div class="group">' + group.name + '</div>').appendTo(groupsDiv);
      if (Cookies.get('user')) {
        $('<button>Signup</button>').click(function() {
          $.post('/rest/course/' + courseId + '/group/' + group.id + '/attendance', {user: Cookies.get('user')}, function() {
            alert('success');
          }).fail(function() {
            alert('fail');
          });
        }).appendTo(groupDiv);
      }
    });
    if (data.length == 0) {
      groupsDiv.text('No groups found for given course');
    }
  });

  $('#groupCreate').submit(function() {
    $.post('/rest/course/' + courseId + '/group', $(this).serialize()).done(function(data) {
      console.log("success", data);
      window.location.reload();
    }).fail(function(xhr) {
      console.log("fail", xhr);
      alert("Unable to create group");
    });

    return false;
  });
});
