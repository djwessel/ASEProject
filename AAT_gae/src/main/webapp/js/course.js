(function() {
  var courseId = QueryString.courseId;
  
  $(document).ready(function() {
    // Load information about course
    $.get('/rest/course/' + courseId, function(data) {
      $('#title').text(data.title);
      $('#courseInfo').empty()
        .append($('<div>Required Attendance count: ' + data.reqAtten + '</div>'))
        .append($('<div>Required Presentation count: ' + data.reqPresent + '</div>'));

      $('#inputTitle').val(data.title);
      $('#inputAtten').val(data.reqAtten);
      $('#inputPresent').val(data.reqPresent);
    }).fail(function() {
      $('body').text('Course does not exist');
    });

    // Form submit for editing course
    $('#courseEdit').validator({ disable: false }).submit(function() {
        $.ajax({
          url: '/rest/course/' + courseId,
          type: 'PUT',
          data: $(this).serialize(),
          success: function(data) {
            window.location.reload();
          },
          error: function(xhr) {
            if (xhr.status === 409)
              alert('Course with given name already exists');
            else
              alert('Unable to update course details');
          }
        });

      return false;
    });
  
    // Load groups of course
    $.get('/rest/course/' + courseId + '/groups', function(data) {
      var groupsTable = $('#groups');
      if (Cookies.get('user') && Cookies.get('userType') === 'student') {
        $.get('/rest/user/' + Cookies.get('user') + '/attendances', function(userGroups) {
          data.forEach(function(group) {
            createGroup(group, groupsTable, userGroups);
          });
        });    
      }
      else {
        data.forEach(function(group) {
          createGroup(group, groupsTable, null);
        });
      }
  
      if (data.length == 0) {
        groupsTable.text('No groups found for given course');
      }
    });
  
    // Form submit for creating new groups
    $('#groupCreate').validator({ disable: false }).submit(function() {
      $.post('/rest/course/' + courseId + '/group', $(this).serialize()).done(function(data) {
        window.location.reload();
      }).fail(function(xhr) {
        if (xhr.status === 409)
          alert('Group with given name already exists for course');
        else
          alert('Unable to create group');
      });
  
      return false;
    });

    $('#createReportBtn').click(function() {
      $.get('/rest/course/' + courseId + '/report', function(data) {
        var reportTableBody = $('#reportTable').show().find('tbody');
        data.forEach(function(record) {
          var reportRow = $('<tr></tr>').appendTo(reportTable);
          $('<td></td>').text(record.student.firstName).appendTo(reportRow);
          $('<td></td>').text(record.student.lastName).appendTo(reportRow);
          $('<td></td>').text(record.student.email).appendTo(reportRow);
          $('<td></td>').text(record.numAttend).appendTo(reportRow);
          $('<td></td>').text(record.numPresent).appendTo(reportRow);
          $('<td></td>').text(record.bonus ? 'Yes' : 'No').appendTo(reportRow);
        });
      });
    })
  
  });
  
  function createGroup(group, groupsTable, userGroups) {
    var groupRow = $('<tr class="group">' + group.name + '</tr>').data('group-id', group.id).appendTo(groupsTable);
    var groupName = $('<td class="group-name">' + group.name + '</td>').appendTo(groupRow);
    var statusCell = $('<td>-</td>').appendTo(groupRow);
    
    
    if (userGroups) {
      // If student hasnt already signed up for a group, add signup button
      if (userGroups.reduce(function(a, b) { return a || b.group.id === group.id }, false)) {
        groupRow.addClass('signed-up info');
        statusCell.empty().append($('<a href="/attendance.jsp?groupId=' + group.id + '&courseId=' + courseId + '">Registered</a>'));
      }
      else {
        $('<button class="btn btn-default">Signup</button>').click(function() {
          if ($('.group.signed-up').length) {
            if (confirm('You are currently signed up for a different group in this course.' 
              + 'Are you sure you want to switch groups?\n' 
              + ' WARNING: Switching groups will delete ALL stored attendance information for previous group.')) {
              changeGroup(group);
            }
          }
          else {
            signUp(group);
          }
        }).appendTo(statusCell.empty());
      }
    }
    else if (Cookies.get('userType') === 'tutor'){
      var editName = $('<form class="form-inline">'
          + '<div class="form-group"><input name="name" class="form-control" placeholder="New Group Name"></div>'
          + '<button type="submit" class="btn btn-default">Update Group</button>'
        + '</form>').submit(function() {

        $.ajax({
          url: '/rest/course/' + courseId + '/group/' + group.id, 
          type: 'PUT',
          data: $(this).serialize(),
          success: function(data) {
            window.location.reload();
          },
          error: function(xhr) {
            if (xhr.status === 409)
              alert('Group with given name already exists for course');
            else
              alert('Unable to update group');
          }
        });

        return false;
      }).hide();
      $('<button class="btn"><span class="glyphicon glyphicon-pencil"></span></button>').click(function() {
        editName.show();
      }).appendTo(groupName);
      editName.appendTo(groupName);
    }
  }
  
  function changeGroup(group) {
    $.ajax({
      url: '/rest/user/' + Cookies.get('user') + '/course/' + courseId + '/group/' + $('.group.signed-up').data('group-id') + '/attendance',
      type: 'DELETE',
      dataType: 'text',
      success: function(data) {
        signUp(group);
      },
      error: function(xhr) {
        alert('Unable to change groups. Please try again.');
        console.log('fail', xhr);
      }
    });
  }
  
  function signUp(group) {
    $.post('/rest/course/' + courseId + '/group/' + group.id + '/attendance', {user: Cookies.get('user')}, function() {
      window.location.reload();
    }).fail(function() {
      alert('Unable to sign up for group. Please try again.');
    });
  }
})();
