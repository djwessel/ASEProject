<%
  String courseId = request.getParameter("courseId");
  if (courseId == null || courseId == "") {
    response.sendError(404);
  }

  Cookie[] cookies = request.getCookies();
  String userType = "";
  if (cookies != null) {
    for (Cookie cookie: cookies) {
      if (cookie.getName().equals("userType")) {
        userType = cookie.getValue().toLowerCase();
      }
    }
  }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:page>
  <jsp:attribute name="head">
    <script src="js/course.js"></script>

    <link rel="stylesheet" type="text/css" href="/css/course.css">
  </jsp:attribute>
  <jsp:attribute name="breadcrumbs">
    <li><a href="/">Home</a></li>
    <li class="active">Course</li>
  </jsp:attribute>
  <jsp:body>
    <h1 id="title">Loading course title...</h1>
    <div id="courseInfo">Loading course info...</div>
    <h3>Groups</h3>
    <table class="table table-hover">
      <thead>
        <tr>
          <th>Group Name</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody id="groups"></tbody>
    </table>

    <button id="createGroupBtn" class="btn btn-primary" style="display: none;" data-toggle="modal" data-target="#groupModal">Create New Group</button>
    <!-- Group Modal -->
    <div class="modal fade" id="groupModal" tabindex="-1" role="dialog" aria-labelledby="groupModalLabel">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="groupModalLabel">Create Group</h4>
          </div>
          <div class="modal-body">
            <form id="groupCreate" class="form-horizontal" data-toggle="validator">
              <div class="form-group">
                <label for="inputName" class="col-sm-2 control-label">Name</label>
                <div class="col-sm-10">
                  <input name="name" type="text" class="form-control" id="inputName" placeholder="Group Name" required>
                  <div class="help-block with-errors"></div>
                </div>
              </div>
              <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                  <input type="submit" class="btn btn-primary" value="Create New Group">
                </div>
              </div>
            </form>  
          </div>
        </div>
      </div>
    </div>
  </jsp:body>
</t:page>
  <%
    if (userType.equals("tutor")) {
  %>
  <%
    }
  %>
