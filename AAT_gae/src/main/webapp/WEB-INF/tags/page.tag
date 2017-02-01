<%@tag description="Page template" pageEncoding="UTF-8"%>
<%@attribute name="head" fragment="true" %>
<%@attribute name="breadcrumbs" fragment="true" %>

<html>
  <head>
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-2.2.4.js" integrity="sha256-iT6Q9iMJYuQiMWNd9lDyBUStIq/8PuOW33aOqmvFpqI=" crossorigin="anonymous"></script>
    <!-- Bootstrap -->
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/1000hz-bootstrap-validator/0.11.9/validator.min.js"></script>

    <link rel="stylesheet" type="text/css" href="/css/page.css">

    <script src="js/util.js"></script>
    <script src="js/js.cookie.js"></script>
    <script src="js/login.js"></script>
    <jsp:invoke fragment="head"/>
  </head>
  <body>
    <nav class="navbar navbar-default">
      <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">AAT</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
          
          <ol class="breadcrumb nav navbar-breadcrumbs navbar-left">
            <jsp:invoke fragment="breadcrumbs"/>
          </ol>
          <ul class="nav navbar-nav navbar-right">
            <li><p id="userWelcome" class="navbar-text"></p></li>
            <li><button id="logout" class="btn btn-default navbar-btn" style="display: none;">Logout</button></li>
            <li><button id="loginModalBtn" class="btn btn-primary navbar-btn" style="display: none;" data-toggle="modal" data-target="#loginModal">Login</button></li>
          </ul>
        </div><!-- /.navbar-collapse -->
      </div><!-- /.container-fluid -->
    </nav>
    <div>
      <!-- Login Modal -->
      <div class="modal fade" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="loginModalLabel">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <h4 class="modal-title" id="loginModalLabel">Login</h4>
            </div>
            <div class="modal-body">
              <div id="modalBtns">
                <button id="newUserBtn" class="btn btn-default btn-block">New User</button>
                <button id="existUserBtn" class="btn btn-default btn-block">Existing User</button>
              </div>
              <form id="userSignup" class="form-horizontal" style="display: none;" data-toggle="validator">
                <div class="form-group">
                  <label for="inputEmail1" class="col-sm-2 control-label">Email</label>
                  <div class="col-sm-10">
                    <input name="email" type="email" class="form-control" id="inputEmail1" placeholder="Email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" data-error="Invalid email format" required>
                    <div class="help-block with-errors"></div>
                  </div>
                </div>
                <div class="form-group">
                  <label for="inputPassword3" class="col-sm-2 control-label">Password</label>
                  <div class="col-sm-5">
                    <input name="password" type="password" class="form-control" id="inputPassword3" placeholder="Password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" required>
                    <div class="help-block">Minimum of 8 characters, at least 1 lower case, at least 1 upper case and at least 1 number.</div>
                  </div>
                  <div class="col-sm-5">
                    <input type="password" class="form-control" id="inputPasswordConfirm" data-match="#inputPassword3" data-match-error="These dont match" placeholder="Confirm" required>
                    <div class="help-block with-errors"></div>
                  </div>
                </div>
                <div class="form-group">
                  <label for="inputFirst" class="col-sm-2 control-label">First Name</label>
                  <div class="col-sm-10">
                    <input name="first" type="first" class="form-control" id="inputFirst" placeholder="First Name" required>
                    <div class="help-block with-errors"></div>
                  </div>
                </div>
                <div class="form-group">
                  <label for="inputLast" class="col-sm-2 control-label">Last Name</label>
                  <div class="col-sm-10">
                    <input name="last" type="text" class="form-control" id="inputLast" placeholder="Last Name" required>
                    <div class="help-block with-errors"></div>
                  </div>
                </div>
                <input type="hidden" name="type" value="Student">
                <div class="form-group">
                  <div class="col-sm-offset-2 col-sm-10">
                    <input type="submit" class="btn btn-primary" value="Signup">
                  </div>
                </div>
              </form>
              <form id="userLogin" class="form-horizontal" style="display: none;"  data-toggle="validator">
                <div class="form-group">
                  <label for="inputEmail2" class="col-sm-2 control-label">Email</label>
                  <div class="col-sm-10">
                    <input name="email" type="email" class="form-control" id="inputEmail2" placeholder="Email" required>
                    <div class="help-block with-errors"></div>
                  </div>
                </div>
                <div class="form-group">
                  <label for="inputPassword3" class="col-sm-2 control-label">Password</label>
                  <div class="col-sm-10">
                    <input name="password" type="password" class="form-control" id="inputPassword3" placeholder="Password" required>
                    <div class="help-block with-errors"></div>
                  </div>
                </div>
                <div class="form-group">
                  <div class="col-sm-offset-2 col-sm-10">
                    <input type="submit" class="btn btn-primary" value="Login">
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="container-fluid">
      <jsp:doBody/>
    </div>
  </body>
</html>
