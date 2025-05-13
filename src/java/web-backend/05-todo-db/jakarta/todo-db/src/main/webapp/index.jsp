<%@ page import="java.util.*, ua.knu.csc.iss.model.Task" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>To-Do List</title>
</head>
<body>
<h2>To-Do Tasks</h2>

<a href="add.jsp">Add New Task</a>
<br><br>

<table border="1" cellpadding="10">
    <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Description</th>
        <th>Status</th>
        <th>Actions</th>
    </tr>
    <%
        List<Task> taskList = (List<Task>) request.getAttribute("taskList");
        if (taskList != null) {
            for (Task task : taskList) {
    %>
    <tr>
        <td><%= task.getId() %></td>
        <td><%= task.getTitle() %></td>
        <td><%= task.getDescription() %></td>
        <td><%= task.isStatus() ? "Done" : "Pending" %></td>
        <td>
            <form action="update.jsp" method="get" style="display:inline;">
                <input type="hidden" name="id" value="<%= task.getId() %>">
                <input type="submit" value="Edit">
            </form>
            <form action="delete" method="get" style="display:inline;">
                <input type="hidden" name="id" value="<%= task.getId() %>">
                <input type="submit" value="Delete" onclick="return confirm('Are you sure?');">
            </form>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>

</body>
</html>
