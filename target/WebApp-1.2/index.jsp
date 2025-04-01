<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Patient Data</title>
</head>
<body>
<h2>Welcome to the Patient Data App</h2>

<nav>
  <ul>
    <li><a href="createNote.jsp">Create Note</a></li>
    <li><a href="notesIndex.jsp">List Notes</a></li>
  </ul>
</nav>

<%
  // Display a message if redirected after shutdown
  String shutdownMessage = request.getParameter("message");
  if (shutdownMessage != null) {
%>
<p style="color: red;"><%= shutdownMessage %></p>
<%
  }
%>

<form action="ShutdownServlet" method="post">
  <button type="submit">Shutdown Server</button>
</form>

</body>
</html>