<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Contact List</title>
</head>
<body>
    <h2>Submitted Contacts</h2>

    <c:choose>
        <c:when test="${empty contacts}">
            <p>No contact data available.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="5" cellspacing="0">
                <tr>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Message</th>
                </tr>
                <c:forEach var="row" items="${contacts}">
                    <tr>
                        <c:forEach var="col" items="${row}">
                            <td>${fn:escapeXml(col)}</td>
                        </c:forEach>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
</body>
</html>
