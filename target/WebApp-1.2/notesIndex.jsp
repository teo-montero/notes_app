<%@ page import="java.util.ArrayList, java.util.UUID" %>
<%@ page import="uk.ac.ucl.model.NoteHandler" %>
<%@ page import="uk.ac.ucl.model.Note" %>
<%@ page import="uk.ac.ucl.model.ModelFactory" %>
<%
  // Get the NoteHandler instance
  NoteHandler noteHandler = ModelFactory.getModel().getNoteHandler();

  ArrayList<Note> notes = noteHandler.getContents();
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>All Notes</title>
</head>
<body>

<h2>All Notes</h2>

<% if (notes.isEmpty()) { %>
<p>No notes available.</p>
<% } else { %>
<ul>
  <% for (Note note : notes) { %>
  <li>
    <a href="editNote.jsp?id=<%= note.getID() %>">
      <%= note.getTitle() %>
    </a>
  </li>
  <% } %>
</ul>
<% } %>

<br>
<a href="createNote.jsp">Create a New Note</a>
<br>
<a href="index.jsp">Back to Home</a>

</body>
</html>