<%
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
    <script src="js/index.js"></script>

    <link rel="stylesheet" type="text/css" href="/css/index.css">
  </jsp:attribute>
  <jsp:attribute name="breadcrumbs">
  </jsp:attribute>
  <jsp:body>
    <h3>Your registered groups</h3>
    <div id="records" class="row"></div>
    <h3>All courses</h3>
    <div id="courses" class="row"></div>
    <button id="createCourseBtn" class="btn btn-default" style="display: none;">Create New Course</button>
    <form id="courseCreate" class="form-horizontal" style="display: none;">
      <div class="form-group">
        <label for="inputTitle" class="col-sm-2 control-label">Title</label>
        <div class="col-sm-10">
          <input name="title" type="text" class="form-control" id="inputTitle" placeholder="Course title">
        </div>
      </div>
      <div class="form-group">
        <label for="inputAtten" class="col-sm-2 control-label">Required Attendance count</label>
        <div class="col-sm-10">
          <input name="reqAtten" type="number" class="form-control" id="inputAtten" placeholder="0">
        </div>
      </div>
      <div class="form-group">
        <label for="inputPresent" class="col-sm-2 control-label">Required Presentation count</label>
        <div class="col-sm-10">
          <input name="reqPresent" type="number" class="form-control" id="inputPresent" placeholder="0">
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <input type="submit" class="btn btn-primary" value="Create New Course">
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
