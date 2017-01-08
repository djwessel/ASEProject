<!doctype html>

<html lang="en">
<head>
  <meta charset="utf-8">
  <script src="https://code.jquery.com/jquery-2.2.4.js" integrity="sha256-iT6Q9iMJYuQiMWNd9lDyBUStIq/8PuOW33aOqmvFpqI=" crossorigin="anonymous"></script>
  <script src="js/index.js"></script>
</head>
<body>
  <form id="userSignup">
    Email: <input type="text" name="email"><br>
    Password: <input type="password" name="password"><br>
    First Name: <input type="text" name="first"><br>
    Last Name: <input type="text" name="last"><br>
    <input type="hidden" name="type" value="Student">
    <input type="submit" value="Sign up">
  </form>
  <div id="courses"></div>
</body>
</html>
