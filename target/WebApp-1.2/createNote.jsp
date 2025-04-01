<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Create a New Note</title>
</head>
<body>
<h2>Create a New Note</h2>

<!-- Form to submit the new note -->
<form action="CreateNoteServlet" method="post">
  <label for="title">Title:</label>
  <input type="text" id="title" name="title" required>

  <br><br>

  <label for="content">Content:</label>
  <textarea id="content" name="content" rows="5" cols="40" required></textarea>

  <br><br>

  <button type="submit">Save Note</button>
</form>

<br>
<a href="index.jsp">Back to Notes</a>
</body>
</html>