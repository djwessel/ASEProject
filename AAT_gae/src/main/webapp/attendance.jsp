<%
  String groupId = request.getParameter("groupId");
  String courseId = request.getParameter("courseId");
  if (groupId == null || groupId.equals("") || courseId == null || courseId.equals("")) {
    response.sendError(404);
  }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:page>
  <jsp:attribute name="head">
    <script src='//cdnjs.cloudflare.com/ajax/libs/moment.js/2.17.1/moment.min.js'></script>
    <script src='//cdnjs.cloudflare.com/ajax/libs/fullcalendar/3.1.0/fullcalendar.min.js'></script>
    <script src="js/attendance.js"></script>

    <link rel="stylesheet" type="text/css" href="/css/attendance.css">
    <link rel='stylesheet' href='//cdnjs.cloudflare.com/ajax/libs/fullcalendar/3.1.0/fullcalendar.min.css' />
  </jsp:attribute>
  <jsp:attribute name="breadcrumbs">
    <li><a href="/">Home</a></li>
    <li class="active">Attendance Information</li>
  </jsp:attribute>
  <jsp:body>
    <h1 id="title">Loading Course Title...</h1>
    <p>Attendance Count: <span id="attenCount"></span> / <span id="reqAtten"></span></p>
    <p>Presentation Count: <span id="presentCount"></span> / <span id="reqPresent"></span></p>
    <div id="calendar"></div>
  </jsp:body>
</t:page>
