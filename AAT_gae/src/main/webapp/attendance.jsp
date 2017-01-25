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
    <script src="js/attendance.js"></script>

    <link rel="stylesheet" type="text/css" href="/css/attendance.css">
  </jsp:attribute>
  <jsp:attribute name="breadcrumbs">
    <li><a href="/">Home</a></li>
    <li class="active">Attendance Information</li>
  </jsp:attribute>
  <jsp:body>
    <h1>Attendance Information</h1>
  </jsp:body>
</t:page>
