<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Greeting Page with JSP</title>
    <script src="js/clock.js"></script>
</head>
<body>
    <h1>Welcome to Our Site!</h1>
    <p>Current server time: <strong>${serverTime}</strong></p>
    <p>Current browser time: <strong id="clientTime"></strong></p>
    <blockquote>"${quote}"</blockquote>
</body>
</html>
