<%
  String courseId = request.getParameter("courseId");
  if (courseId == null || courseId == "") {
    response.sendError(404);
  }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
    <c:if test="${cookie.userType.value == 'tutor'}">
      <button id="createReportBtn" class="btn btn-default">Create Attendance Report</button>
    </c:if>
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

    <c:if test="${cookie.userType.value == 'tutor'}">
      <button id="createGroupBtn" class="btn btn-primary" data-toggle="modal" data-target="#groupModal">Create New Group</button>
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
    </c:if>
  </jsp:body>
</t:page>
