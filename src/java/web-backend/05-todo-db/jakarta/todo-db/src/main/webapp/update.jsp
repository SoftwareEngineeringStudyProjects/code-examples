<%@ page import="ua.knu.csc.iss.dao.TaskDAO, ua.knu.csc.iss.model.Task" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    int id = Integer.parseInt(request.getParameter("id"));
    TaskDAO dao = new TaskDAO();
    Task task = dao.selectTask(id);
%>
<html>
<head>
    <title>Update Task</title>
</head>
<body>
<h2>Update Task</h2>

<form action="update" method="post">
    <input type="hidden" name="id" value="<%= task.getId() %>">

    <label>Title:</label><br>
    <input type="text" name="title" value="<%= task.getTitle() %>" required><br><br>

    <label>Description:</label><br>
    <textarea name="description" rows="4" cols="50"><%= task.getDescription() %></textarea><br><br>

    <label>Status:</label>
    <input type="checkbox" name="status" <%= task.isStatus() ? "checked" : "" %> > Completed<br><br>

    <input type="submit" value="Update Task">
</form>

<br>
<a href="tasks">Back to List</a>
</body>
</html>
