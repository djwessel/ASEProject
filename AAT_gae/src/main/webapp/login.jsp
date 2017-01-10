<script src="js/login.js"></script>
<div id="userWelcome"></div>
<form id="userSignup" style="display: none;">
  New User<br>
  Email: <input type="email" name="email"><br>
  Password: <input type="password" name="password"><br>
  First Name: <input type="text" name="first"><br>
  Last Name: <input type="text" name="last"><br>
  <input type="hidden" name="type" value="Student">
  <input type="submit" value="Sign up">
</form>
<form id="userLogin" style="display: none;">
  Existing User<br>
  Email: <input type="email" name="email"><br>
  Password: <input type="password" name="password"><br>
  <input type="submit" value="Login">
</form>
<button id="logout" style="display: none;">Logout</button>
