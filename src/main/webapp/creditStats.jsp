<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>修读学分统计</title>
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
        <h2>已修读学分统计</h2>
        <div>
            <button type (type="button" class="btn-add" data-bs-toggle="modal" data-bs-target="#enrollmentModal">添加修读记录</button>
            <a href="${pageContext.request.contextPath}/exportCreditStats?studentId=${studentId}" class="btn-export">导出学分统计</a>
            <a href="${pageContext.request.contextPath}/exportEnrollments?studentId=${studentId}" class="btn-export">导出成绩</a>
        </div>
    </div>
    <p>学生 ID: ${studentId} | 总学分: ${totalCredit}</p>
    <c:if test="${not empty error}">
        <div class="alert alert-danger mt-3">${error}</div>
    </c:if>
    <table class="table table-striped table-bordered">
        <thead>
        <tr>
            <th>课程名称</th>
            <th>学分</th>
            <th>成绩</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="enrollment" items="${enrollmentList}">
            <tr>
                <td>${enrollment.course.courseName}</td>
                <td>${enrollment.course.credit}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/updateEnrollment" method="post" style="display:inline;">
                        <input type="hidden" name="enrollmentId" value="${enrollment.enrollmentId}">
                        <input type="hidden" name="studentId" value="${studentId}">
                        <input type="number" step="0.01" name="grade" value="${enrollment.grade}" min="0" max="100" required>
                        <button type="submit" class="btn btn-primary btn-sm">更新</button>
                    </form>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/deleteEnrollment?enrollmentId=${enrollment.enrollmentId}&studentId=${studentId}"
                       class="btn-delete" onclick="return confirm('确定删除课程 ${enrollment.course.courseName} 的修读记录？')">删除</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a href="${pageContext.request.contextPath}/students" class="btn-back">返回学生列表</a>
    <a href="${pageContext.request.contextPath}/attendance?studentId=${studentId}" class="btn-back">查看考勤</a>
</div>

<!-- 添加修读记录模态框 -->
<div class="modal fade" id="enrollmentModal" tabindex="-1" aria-labelledby="enrollmentModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="enrollmentModalLabel">添加修读记录</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="enrollmentForm" action="${pageContext.request.contextPath}/addEnrollment" method="post">
                <div class="modal-body">
                    <input type="hidden" name="studentId" value="${studentId}">
                    <div class="mb-3">
                        <label for="courseId" class="form-label">课程</label>
                        <select class="form-select" id="courseId" name="courseId" required>
                            <option value="" disabled selected>请选择课程</option>
                            <c:forEach var="course" items="${courseList}">
                                <option value="${course.courseId}">${course.courseName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="grade" class="form-label">成绩</label>
                        <input type="number" step="0.01" class="form-control" id="grade" name="grade"
                               placeholder="请输入成绩（0-100）" required min="0" max="100">
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
    document.getElementById('enrollmentForm').addEventListener('submit', function(e) {
        let courseId = document.getElementById('courseId').value;
        let grade = document.getElementById('grade').value;
        if (!courseId) {
            alert('请选择一门课程');
            e.preventDefault();
        }
        if (isNaN(grade) || grade < 0 || grade > 100) {
            alert('成绩必须在 0-100 之间');
            e.preventDefault();
        }
    });
</script>
</body>
</html>