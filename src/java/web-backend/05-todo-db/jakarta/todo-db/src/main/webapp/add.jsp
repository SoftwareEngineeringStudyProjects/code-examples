<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Add Task</title>
</head>
<body>
<h2>Add New Task</h2>

<form action="add" method="post">
    <label>Title:</label><br>
    <input type="text" name="title" required><br><br>

    <label>Description:</label><br>
    <textarea name="description" rows="4" cols="50"></textarea><br><br>

    <input type="submit" value="Add Task">
</form>

<br>
<a href="tasks">Back to List</a>
</body>
</html>
