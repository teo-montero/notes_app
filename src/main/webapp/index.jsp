<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Notes App</title>
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/styles.css">
  <link rel="stylesheet" href="assets/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
</head>
<body>

<jsp:include page="header.jsp"/>

<%
  String indexID = request.getParameter("indexID");
  if (indexID == null) {
    indexID = "null";
  }
%>

<%
  boolean shouldSearch = "search".equals(request.getParameter("action")) && request.getParameter("query") != null;
%>

<% if(!shouldSearch) { %>
  <jsp:include page="displayIndex.jsp">
    <jsp:param name="indexID" value="<%= indexID %>" />
  </jsp:include>
<% } else { %>
  <jsp:include page="displaySearch.jsp">
    <jsp:param name="indexID" value="<%= indexID %>" />
    <jsp:param name="query" value='<%= request.getParameter("query") %>' />
  </jsp:include>
<% } %>

<jsp:include page="footer.jsp" />

<script src="assets/js/bootstrap.bundle.min.js"></script>

</body>
</html>