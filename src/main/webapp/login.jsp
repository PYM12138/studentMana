<%--
  Created by IntelliJ IDEA.
  User: 16023
  Date: 2025/5/10
  Time: 14:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录</title>
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        body {
            font-family: 'Arial', sans-serif;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            background: linear-gradient(135deg, #e0e7ff, #c3dafe); /* 渐变背景 */
            margin: 0;
        }
        .login-container {
            background-color: #ffffff;
            padding: 2rem;
            border-radius: 10px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
            max-width: 400px;
            width: 100%;
        }
        .login-container h2 {
            text-align: center;
            margin-bottom: 1.5rem;
            color: #1e3a8a; /* 深蓝色标题 */
        }
        .error {
            color: #dc3545;
            text-align: center;
            margin-bottom: 1rem;
            font-size: 0.9rem;
        }
        .btn-primary {
            background-color: #1e3a8a;
            border: none;
            padding: 0.75rem;
            font-size: 1rem;
            transition: background-color 0.3s ease;
        }
        .btn-primary:hover {
            background-color: #1e40af;
        }
        .form-control:focus {
            border-color: #1e3a8a;
            box-shadow: 0 0 0 0.2rem rgba(30, 58, 138, 0.25);
        }
    </style>
</head>
<body>
<div class="login-container">
    <h2>登录</h2>
    <% if (request.getAttribute("error") != null) { %>
    <p class="error"><%= request.getAttribute("error") %></p>
    <% } %>
    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="mb-3">
            <input type="text" name="username" class="form-control" placeholder="用户名" required>
        </div>
        <div class="mb-3">
            <input type="password" name="password" class="form-control" placeholder="密码" required>
        </div>
        <button type="submit" class="btn btn-primary w-100">登录</button>
    </form>
</div>
<!-- Bootstrap 5 JS CDN (可选，如果需要Bootstrap的JS交互功能) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>