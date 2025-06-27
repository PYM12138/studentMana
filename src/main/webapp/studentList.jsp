<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>学生信息</title>
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
            max-width: 1000px;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
        }
        .title-group {
            display: flex;
            align-items: center;
            gap: 1rem;
        }
        .title-group h2 {
            color: #1e3a8a;
            margin: 0;
        }
        .user-info {
            color: #333;
        }
        .logout-link {
            color: #dc3545;
            text-decoration: none;
            font-weight: 500;
        }
        .logout-link:hover {
            color: #b02a37;
            text-decoration: underline;
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
        .btn-credit, .btn-add, .btn-edit, .btn-delete, .btn-attendance, .btn-activity, .btn-class-stats {
            background-color: #1e3a8a;
            color: white;
            padding: 0.4rem 0.8rem;
            border-radius: 5px;
            text-decoration: none;
            margin: 0 0.2rem;
        }
        .btn-delete {
            background-color: #dc3545;
        }
        .btn-credit:hover, .btn-add:hover, .btn-edit:hover, .btn-attendance:hover, .btn-activity:hover, .btn-class-stats:hover {
            background-color: #1e40af;
            color: white;
            text-decoration: none;
        }
        .btn-delete:hover {
            background-color: #b02a37;
            color: white;
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
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <div class="title-group">
            <h2>学生信息</h2>
            <button type="button" class="btn-add" data-bs-toggle="modal" data-bs-target="#studentModal" onclick="resetForm()">添加学生</button>
            <div class="dropdown">
                <button class="btn-class-stats dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                    班级统计
                </button>
                <ul class="dropdown-menu">
                    <c:forEach var="cls" items="${classList}">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/classStats?classId=${cls.classId}">${cls.className}</a></li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <div class="user-info">
            <p class="mt-2">当前用户: <%= session.getAttribute("user") %> | <a href="logout" class="logout-link">登出</a></p>
        </div>
    </div>
    <c:if test="${not empty error}">
        <div class="alert alert-danger mt-3">${error}</div>
    </c:if>
    <table class="table table-striped table-bordered">
        <thead>
        <tr>
            <th>学号</th>
            <th>姓名</th>
            <th>性别</th>
            <th>年龄</th>
            <th>班级</th>
            <th>总学分</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="student" items="${studentList}">
            <tr>
                <td>${student.studentId}</td>
                <td>${student.name}</td>
                <td>${student.gender}</td>
                <td>${student.age}</td>
                <td>${student.className}</td>
                <td>${student.totalCredit}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/creditStats?studentId=${student.studentId}" class="btn-credit">查看学分</a>
                    <a href="${pageContext.request.contextPath}/attendance?studentId=${student.studentId}" class="btn-attendance">查看考勤</a>
                    <a href="${pageContext.request.contextPath}/activityRecords?studentId=${student.studentId}" class="btn-activity">查看活动</a>
                    <button type="button" class="btn-edit" data-bs-toggle="modal" data-bs-target="#studentModal"
                            onclick="populateForm(${student.studentId}, '${student.name}', '${student.gender}', ${student.age}, ${student.classId})">编辑</button>
                    <a href="${pageContext.request.contextPath}/deleteStudent?studentId=${student.studentId}" class="btn-delete" onclick="return confirm('确定删除学生 ${student.name}？')">删除</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<div class="modal fade" id="studentModal" tabindex="-1" aria-labelledby="studentModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="studentModalLabel">添加学生</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="studentForm" action="${pageContext.request.contextPath}/addStudent" method="post">
                <div class="modal-body">
                    <input type="hidden" id="originalStudentId" name="originalStudentId">
                    <div class="mb-3">
                        <label for="studentId" class="form-label">学号</label>
                        <input type="number" class="form-control" id="studentId" name="studentId" placeholder="请输入学号" required readonly>
                        <div id="studentIdHelp" class="form-text text-danger" style="display: none;">学号不可更改！</div>
                    </div>
                    <div class="mb-3">
                        <label for="name" class="form-label">姓名</label>
                        <input type="text" class="form-control" id="name" name="name" placeholder="请输入姓名" required>
                    </div>
                    <div class="mb-3">
                        <label for="gender" class="form-label">性别</label>
                        <select class="form-select" id="gender" name="gender" required>
                            <option value="" disabled selected>请选择性别</option>
                            <option value="男">男</option>
                            <option value="女">女</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="age" class="form-label">年龄</label>
                        <input type="number" class="form-control" id="age" name="age" placeholder="请输入年龄" required min="1" max="100">
                    </div>
                    <div class="mb-3">
                        <label for="classId" class="form-label">班级</label>
                        <select class="form-select" id="classId" name="classId" required>
                            <option value="" disabled selected>请选择班级</option>
                            <c:forEach var="cls" items="${classList}">
                                <option value="${cls.classId}">${cls.className}</option>
                            </c:forEach>
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function resetForm() {
        document.getElementById('studentForm').action = '${pageContext.request.contextPath}/addStudent';
        document.getElementById('studentModalLabel').textContent = '添加学生';
        document.getElementById('studentForm').reset();
        document.getElementById('originalStudentId').value = '';
        document.getElementById('studentId').readOnly = false;
        document.getElementById('studentIdHelp').style.display = 'none';
    }

    function populateForm(studentId, name, gender, age, classId) {
        document.getElementById('studentForm').action = '${pageContext.request.contextPath}/editStudent';
        document.getElementById('studentModalLabel').textContent = '编辑学生';
        document.getElementById('originalStudentId').value = studentId;
        document.getElementById('studentId').value = studentId;
        document.getElementById('studentId').readOnly = true;
        document.getElementById('studentIdHelp').style.display = 'block';
        document.getElementById('name').value = name;
        document.getElementById('gender').value = gender;
        document.getElementById('age').value = age;
        document.getElementById('classId').value = classId;
    }

    document.getElementById('studentForm').addEventListener('submit', function(e) {
        let studentId = document.getElementById('studentId').value;
        let age = document.getElementById('age').value;
        let classId = document.getElementById('classId').value;
        if (!/^\d+$/.test(studentId)) {
            alert('学号必须是数字');
            e.preventDefault();
        }
        if (age < 1 || age > 100) {
            alert('年龄必须在1-100之间');
            e.preventDefault();
        }
        if (!classId) {
            alert('请选择一个班级');
            e.preventDefault();
        }
    });
</script>
</body>
</html>