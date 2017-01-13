<%
  String courseId = request.getParameter("courseId");
  if (courseId == null || courseId == "") {
    response.sendError(404);
  }

/*  String groupId = request.getParameter("groupId");
  if (groupId == "") {
    response.sendError(404);
  }*/
%>
<!doctype html>

<html lang="en">
<head>
  <meta charset="utf-8">
  <script src="https://code.jquery.com/jquery-2.2.4.js" integrity="sha256-iT6Q9iMJYuQiMWNd9lDyBUStIq/8PuOW33aOqmvFpqI=" crossorigin="anonymous"></script>
  <script src="js/util.js"></script>
  <script src="js/js.cookie.js"></script>
  <script src="js/course.js"></script>
<body>
  <%@include file="login.jsp"%>
  <h1 id="title">Loading course title...</h1>
  <div id="courseInfo">Loading course info...</div>
  <div id="groups">Loading groups...</div>

  <form id="groupCreate">
    Create New Group<br>
    Group Name: <input type="text" name="name"><br>
    <input type="submit" value="Create New Group">
  </form>  
</body>
</html>
