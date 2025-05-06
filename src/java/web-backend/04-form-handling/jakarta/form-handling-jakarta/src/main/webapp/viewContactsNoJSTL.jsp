<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.text.StringEscapeUtils" %>
<html>
<head>
    <title>Contact List</title>
</head>
<body>
    <h2>Submitted Contacts</h2>

<%
    List<List<String>> contacts = (List<List<String>>) request.getAttribute("contacts");
    if (contacts == null || contacts.isEmpty()) {
%>
    <p>No contact data available.</p>
<%
    } else {
%>
    <table border="1" cellpadding="5" cellspacing="0">
        <tr><th>Name</th><th>Email</th><th>Message</th></tr>
<%
        for (List<String> row : contacts) {
%>
        <tr>
<%
            for (String col : row) {
%>
            <td><%= org.apache.commons.text.StringEscapeUtils.escapeHtml4(col) %></td>
<%
            }
%>
        </tr>
<%
        }
%>
    </table>
<%
    }
%>

</body>
</html>
