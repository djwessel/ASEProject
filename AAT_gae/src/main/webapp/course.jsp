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
    <div id="groups" class="row">Loading groups...</div>

    <button id="createGroupBtn" class="btn btn-default" style="display: none;">Create New Group</button>
    <form id="groupCreate" class="form-horizontal" style="display: none;">
      <div class="form-group">
        <label for="inputName" class="col-sm-2 control-label">Name</label>
        <div class="col-sm-10">
          <input name="name" type="text" class="form-control" id="inputName" placeholder="Group Name">
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <input type="submit" class="btn btn-primary" value="Create New Group">
        </div>
      </div>
    </form>  
  </jsp:body>
</t:page>
  <%
    if (userType.equals("tutor")) {
  %>
  <%
    }
  %>
