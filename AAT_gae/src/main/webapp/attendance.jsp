<%
  String groupId = request.getParameter("groupId");
  if (groupId == null || groupId == "") {
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
    <h1>Loading Course Title...</h1>
    <p id="attenCount">Loading Attendance Count...</p>
    <p id="presentCount">Loading Presentation Count...</p>
    <div id="calendar"></div>
  </jsp:body>
</t:page>
