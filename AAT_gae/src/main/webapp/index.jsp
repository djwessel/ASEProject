<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:page>
  <jsp:attribute name="head">
    <script src="js/index.js"></script>

    <link rel="stylesheet" type="text/css" href="/css/index.css">
  </jsp:attribute>
  <jsp:attribute name="breadcrumbs">
  </jsp:attribute>
  <jsp:body>
    <!!-- Student Registered Groups -->
    <c:if test="${cookie.userType.value == 'student'}">
      <h3>Your registered groups</h3>
      <table class="table table-hover">
        <thead>
          <tr>
            <th>Course Name</th>
            <th>Group Name</th>
          </tr>
        </thead>
        <tbody id="records"></tbody>
      </table>
    </c:if>

    <h3>All courses</h3>
    <table class="table table-hover">
      <thead>
        <tr>
          <th>Course Name</th>
        </tr>
      </thead>
      <tbody id="courses"></tbody>
    </table>

    <!-- Tutor Course Creation -->
    <c:if test="${cookie.userType.value == 'tutor'}">
      <button id="createCourseBtn" class="btn btn-primary" data-toggle="modal" data-target="#courseModal">Create New Course</button>
      <!-- Course Modal -->
      <div class="modal fade" id="courseModal" tabindex="-1" role="dialog" aria-labelledby="courseModalLabel">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <h4 class="modal-title" id="courseModalLabel">Create Course</h4>
            </div>
            <div class="modal-body">
              <form id="courseCreate" class="form-horizontal" data-toggle="validator">
                <div class="form-group">
                  <label for="inputTitle" class="col-sm-2 control-label">Title</label>
                  <div class="col-sm-10">
                    <input name="title" type="text" class="form-control" id="inputTitle" placeholder="Course title" required>
                    <div class="help-block with-errors"></div>
                  </div>
                </div>
                <div class="form-group">
                  <label for="inputAtten" class="col-sm-2 control-label">Required Attendance Count</label>
                  <div class="col-sm-10">
                    <input name="reqAtten" type="number" class="form-control" id="inputAtten" placeholder="0" min="0" required>
                    <div class="help-block with-errors"></div>
                  </div>
                </div>
                <div class="form-group">
                  <label for="inputPresent" class="col-sm-2 control-label">Required Presentation Count</label>
                  <div class="col-sm-10">
                    <input name="reqPresent" type="number" class="form-control" id="inputPresent" placeholder="0" min="0" required>
                    <div class="help-block with-errors"></div>
                  </div>
                </div>
                <div class="form-group">
                  <div class="col-sm-offset-2 col-sm-10">
                    <input type="submit" class="btn btn-primary" value="Create New Course">
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
