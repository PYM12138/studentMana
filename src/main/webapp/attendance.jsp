<%--
  Created by IntelliJ IDEA.
  User: 16023
  Date: 2025/6/25
  Time: 22:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>考勤管理</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
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
        .table {
            border-radius: 8px;
            overflow: hidden;
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
        .table-striped tbody tr:nth-of-type(odd) {
            background-color: #f8f9fa;
        }
        .btn-add, .btn-delete, .btn-back, .btn-export {
            background-color: #1e3a8a;
            color: white;
            padding: 0.4rem 0.8rem;
            border-radius: 5px;
            border: none;
            text-decoration: none;
            transition: background-color 0.3s ease;
            margin: 0 0.2rem;
        }
        .btn-delete {
            background-color: #dc3545;
        }
        .btn-add:hover, .btn-back:hover, .btn-export:hover {
            background-color: #1e40af;
            color: white;
            text-decoration: none;
        }
        .btn-delete:hover {
            background-color: #b02a37;
            color: white;
            text-decoration: none;
        }
        .modal-content {
            border-radius: 10px;
        }
        .modal-header {
            background-color: #1e3a8a;
            color: white;
        }
        .modal-footer .btn-primary {
            background-color: #1e3a8a;
        }
        .modal-footer .btn-primary:hover {
            background-color: #1e40af;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h2>考勤管理</h2>
        <div>
            <button type="button" class="btn-add" data-bs-toggle="modal" data-bs-target="#attendanceModal">添加考勤记录</button>
            <a href="${pageContext.request.contextPath}/exportAttendance?studentId=${studentId}" class="btn-export">导出考勤</a>
        </div>
    </div>
    <p>学生 ID: ${studentId}</p>
    <c:if test="${not empty error}">
        <div class="alert alert-danger mt-3">${error}</div>
    </c:if>
    <table class="table table-striped table-bordered">
        <thead>
        <tr>
            <th>日期</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="attendance" items="${attendanceList}">
            <tr>
                <td>${attendance.date}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/updateAttendance" method="post" style="display:inline;">
                        <input type="hidden" name="attendanceId" value="${attendance.attendanceId}">
                        <input type="hidden" name="studentId" value="${studentId}">
                        <select name="status" required>
                            <option value="签到" ${attendance.status == '签到' ? 'selected' : ''}>签到</option>
                            <option value="请假" ${attendance.status == '请假' ? 'selected' : ''}>请假</option>
                            <option value="旷课" ${attendance.status == '旷课' ? 'selected' : ''}>旷课</option>
                        </select>
                        <button type="submit" class="btn btn-primary btn-sm">更新</button>
                    </form>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/deleteAttendance?attendanceId=${attendance.attendanceId}&studentId=${studentId}"
                       class="btn-delete" onclick="return confirm('确定删除该考勤记录？')">删除</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
<%--    <a href="${pageContext.request.contextPath}/creditStats?studentId=${studentId}" class="btn-back">返回学分统计</a>--%>
    <a href="${pageContext.request.contextPath}/students" class="btn-back">返回学生列表</a>
</div>

<!-- 添加考勤记录模态框 -->
<div class="modal fade" id="attendanceModal" tabindex="-1" aria-labelledby="attendanceModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="attendanceModalLabel">添加考勤记录</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="attendanceForm" action="${pageContext.request.contextPath}/addAttendance" method="post">
                <div class="modal-body">
                    <input type="hidden" name="studentId" value="${studentId}">
                    <div class="mb-3">
                        <label for="date" class="form-label">日期</label>
                        <input type="date" class="form-control" id="date" name="date" required>
                    </div>
                    <div class="mb-3">
                        <label for="status" class="form-label">状态</label>
                        <select class="form-select" id="status" name="status" required>
                            <option value="" disabled selected>请选择状态</option>
                            <option value="签到">签到</option>
                            <option value="请假">请假</option>
                            <option value="旷课">旷课</option>
                        </select>
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script>
    document.getElementById('attendanceForm').addEventListener('submit', function(e) {
        let date = document.getElementById('date').value;
        let status = document.getElementById('status').value;
        if (!date) {
            alert('请选择日期');
            e.preventDefault();
        }
        if (!status) {
            alert('请选择状态');
            e.preventDefault();
        }
    });
</script>
</body>
</html>
