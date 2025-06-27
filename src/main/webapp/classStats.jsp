<%--
  Created by IntelliJ IDEA.
  User: 16023
  Date: 2025/6/25
  Time: 23:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>班级统计</title>
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
            max-width: 600px;
        }
        h2 {
            color: #1e3a8a;
        }
        .btn-back {
            background-color: #1e3a8a;
            color: white;
            padding: 0.4rem 0.8rem;
            border-radius: 5px;
            text-decoration: none;
        }
        .btn-back:hover {
            background-color: #1e40af;
            color: white;
            text-decoration: none;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>班级统计</h2>
    <p>班级 ID: ${classId}</p>
    <p>平均分: ${averageScore}</p>
    <p>考勤率: ${attendanceRate}%</p>
    <a href="${pageContext.request.contextPath}/students" class="btn-back">返回学生列表</a>
</div>
</body>
</html>
