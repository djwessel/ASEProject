(function() {
  var courseId = QueryString.courseId;
  
  $(document).ready(function() {
    // Load information about course
    $.get('/rest/course/' + courseId, function(data) {
      $('#title').text(data.title);
      $('#courseInfo').empty()
        .append($('<div>Required Attendance count: ' + data.reqAtten + '</div>'))
        .append($('<div>Required Presentation count: ' + data.reqPresent + '</div>'));
    }).fail(function() {
      $('body').text('Course does not exist');
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
  
    if (Cookies.get('user') && Cookies.get("userType") === "tutor") {
      $('#createGroupBtn').show();
    }
  });
  
  function createGroup(group, groupsTable, userGroups) {
    var groupRow = $('<tr class="group">' + group.name + '</tr>').data('group-id', group.id).appendTo(groupsTable);
    $('<td>' + group.name + '</td>').appendTo(groupRow);
    var statusCell = $('<td>-</td>').appendTo(groupRow);
    
    
    if (userGroups) {
      signedUp = false;
      for (var course in userGroups)
        if (userGroups[course].id === group.id)
          signedUp = true;
      // If student hasnt already signed up for a group, add signup button
      if (signedUp) {
        groupRow.addClass('signed-up info');
        statusCell.text('Registered');
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
  }
  
  function changeGroup(group) {
    $.ajax({
      url: '/rest/user/' + Cookies.get('user') + '/group/' + $('.group.signed-up').data('group-id') + '/attendance',
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
