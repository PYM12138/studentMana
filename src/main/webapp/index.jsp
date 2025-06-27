<%--
  Created by IntelliJ IDEA.
  User: 16023
  Date: 2025/5/10
  Time: 13:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>学生管理系统</title>
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        body {
            font-family: 'Arial', sans-serif;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            background: linear-gradient(135deg, #e0e7ff, #c3dafe); /* 与登录页面一致的渐变背景 */
            margin: 0;
        }
        .welcome-container {
            background-color: #ffffff;
            padding: 2rem;
            border-radius: 10px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
            text-align: center;
            max-width: 500px;
            width: 100%;
        }
        .welcome-container h2 {
            color: #1e3a8a; /* 深蓝色标题，与登录页面一致 */
            margin-bottom: 1.5rem;
        }
        .btn-login {
            background-color: #1e3a8a;
            color: white;
            padding: 0.75rem 1.5rem;
            border-radius: 5px;
            text-decoration: none;
            font-size: 1.1rem;
            transition: background-color 0.3s ease;
        }
        .btn-login:hover {
            background-color: #1e40af;
            color: white;
            text-decoration: none;
        }
    </style>
</head>
<body>
<div class="welcome-container">
    <h2>欢迎来到学生管理系统</h2>
    <a href="/login" class="btn-login">请前往登录</a>
</div>
<!-- Bootstrap 5 JS CDN (可选，增强交互效果) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>