<%@ page import="java.util.UUID" %>
<%@ page import="uk.ac.ucl.model.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    NoteHandler noteHandler = ModelFactory.getModel().getNoteHandler();

    UUID noteID = UUID.fromString(request.getParameter("id"));
    Note note = noteHandler.getContentFromID(noteID);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Note</title>
</head>
<body>
<h2>Edit Note</h2>

<!-- Editable Form -->
<form action="UpdateNoteServlet" method="post">
    <!-- Hidden field to store Note ID -->
    <input type="hidden" name="noteID" value="<%= note.getID() %>">

    <!-- Editable Title Field -->
    <label for="title">Title:</label>
    <input type="text" id="title" name="title" value="<%= note.getTitle() %>" required>

    <br><br>

    <!-- Editable Content Field -->
    <label for="content">Content:</label>
    <textarea id="content" name="content" rows="5" cols="40"><%= String.join("\n", note.getTextContent()) %></textarea>

    <br><br>

    <!-- Save Button -->
    <button type="submit">Save</button>
</form>
</body>
</html>
