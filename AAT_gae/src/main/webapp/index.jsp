<!doctype html>

<html lang="en">
<head>
  <meta charset="utf-8">
  <script src="https://code.jquery.com/jquery-2.2.4.js" integrity="sha256-iT6Q9iMJYuQiMWNd9lDyBUStIq/8PuOW33aOqmvFpqI=" crossorigin="anonymous"></script>
  <script src="js/util.js"></script>
  <script src="js/js.cookie.js"></script>
  <script src="js/index.js"></script>
</head>
<body>
  <%@include file="login.jsp"%>
  <div id="courses"></div>
  <form id="courseCreate">
    Title: <input type="text" name="title"><br>
    Required Attendance count: <input type="number" name="reqAtten"><br>
    Required Presentation count: <input type="number" name="reqPresent"><br>
    <input type="submit" value="Create New Course">
  </form>
</body>
</html>
