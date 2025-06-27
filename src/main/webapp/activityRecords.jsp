<%--
  Created by IntelliJ IDEA.
  User: 16023
  Date: 2025/6/25
  Time: 23:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>活动记录</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body {
      font-family: 'Arial', sans-serif;
      min-height: 100vh;
      background: linear-gradient(135deg, #e0e7ff, #c3dafe);
      margin: 0;
      padding: 2rem;
    }
    .container {
      background-color: #ffffff;
      padding: 2rem;
      border-radius: 10px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
      max-width: 800px;
    }
    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 1.5rem;
    }
    .header h2 {
      color: #1e3a8a;
      margin: 0;
    }
    .table th {
      background-color: #1e3a8a;
      color: white;
      text-align: center;
    }
    .table td {
      vertical-align: middle;
      text-align: center;
    }
    .btn-add, .btn-back {
      background-color: #1e3a8a;
      color: white;
      padding: 0.4rem 0.8rem;
      border: none;
      border-radius: 5px;
      text-decoration: none;
      transition: background-color 0.3s ease;
    }
    .btn-add:hover, .btn-back:hover {
      background-color: #1e40af;
      color: white;
      text-decoration: none;
    }
    .modal-header {
      background-color: #1e3a8a;
      color: white;
    }
    .modal-footer .btn-primary {
      background-color: #1e3a8a;
    }
  </style>
</head>
<body>
<div class="container">
  <div class="header">
    <h2>活动记录</h2>
    <button type="button" class="btn-add" data-bs-toggle="modal" data-bs-target="#activityModal">添加活动记录</button>
  </div>
  <p>学生 ID: ${studentId}</p>
  <c:if test="${not empty error}">
    <div class="alert alert-danger mt-3">${error}</div>
  </c:if>
  <table class="table table-striped table-bordered">
    <thead>
    <tr>
      <th>活动名称</th>
      <th>参与日期</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="record" items="${activityRecords}">
      <tr>
        <td>${record.activityName}</td>
        <td>${record.participationDate}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
  <a href="${pageContext.request.contextPath}/students" class="btn-back">返回学生列表</a>
</div>

<div class="modal fade" id="activityModal" tabindex="-1" aria-labelledby="activityModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="activityModalLabel">添加活动记录</h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <form action="${pageContext.request.contextPath}/addActivityRecord" method="post">
        <div class="modal-body">
          <input type="hidden" name="studentId" value="${studentId}">
          <div class="mb-3">
            <label for="activityId" class="form-label">活动</label>
            <select class="form-select" id="activityId" name="activityId" required>
              <option value="" disabled selected>请选择活动</option>
              <c:forEach var="activity" items="${activityList}">
                <option value="${activity.activityId}">${activity.activityName}</option>
              </c:forEach>
            </select>
          </div>
          <div class="mb-3">
            <label for="participationDate" class="form-label">参与日期</label>
            <input type="date" class="form-control" id="participationDate" name="participationDate" required>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
          <button type="submit" class="btn btn-primary">保存</button>
        </div>
      </form>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>