<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Create a New Note</title>

  <!-- Bootstrap & Custom Styles -->
  <link rel="stylesheet" href="assets/css/bootstrap.min.css">
  <link rel="stylesheet" href="assets/css/styles.css">
</head>
<body>

<!-- Include Header -->
<jsp:include page="header.jsp" />

<div class="container mt-4">
  <h2 class="mb-3">Create a New Note</h2>

  <!-- Form Container -->
  <div class="card p-4 shadow-sm">
    <form action="CreateNoteServlet" method="post" enctype="multipart/form-data">

      <input type="hidden" name="indexID" value="<%= request.getParameter("indexID") %>">

      <div class="mb-3">
        <button type="button" class="btn btn-secondary" data-bs-toggle="modal" data-bs-target="#dumpModal">
          Dump File into Note
        </button>
      </div>

      <div class="mb-3">
        <label for="title" class="form-label">Title:</label>
        <input type="text" id="title" name="title" class="form-control" required>
      </div>

      <div class="mb-3">
        <label for="content" class="form-label">Content:</label>
        <textarea id="content" name="content" rows="5" cols="40" class="form-control"></textarea>
      </div>

      <div class="mb-3">
        <label for="imageInput" class="form-label">Upload Image:</label>
        <input class="form-control" type="file" id="imageInput" name="image" accept="image/*">
      </div>

      <div class="mb-3">
        <img id="imagePreview" alt="" src="" style="display:none; max-width: 400px; max-height: 400px;">
      </div>

      <hr>

      <button type="submit" class="btn btn-success">Save Note</button>
      <a href="index.jsp?indexID=<%= request.getParameter("indexID") %>" class="btn btn-secondary">Back to Notes</a>
    </form>
  </div>
</div>

<div class="modal fade" id="dumpModal" tabindex="-1" aria-labelledby="dumpModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="dumpModalLabel">Dump File Content</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <input type="file" id="fileInput" accept=".txt,.pdf,.docx" class="form-control">
        <br>
        <div class="alert alert-info p-2 small" role="alert">
          Select a .txt, .pdf, or .docx file - Its content will be appended to the note.
        </div>
        <div id="fileError" class="text-danger mt-2" style="display:none;"></div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-primary" id="dumpFileBtn">Dump</button>
      </div>
    </div>
  </div>
</div>

<!-- Include Footer -->
<jsp:include page="footer.jsp" />

<script>
  document.getElementById("imageInput").addEventListener("change", function(event) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = function(e) {
        document.getElementById("imagePreview").src = e.target.result;
        document.getElementById("imagePreview").style.display = "block";
      };
      reader.readAsDataURL(file);
    }
  });
</script>

<script src="assets/js/bootstrap.bundle.min.js"></script>

<!-- PDF.js -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/2.10.377/pdf.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/2.10.377/pdf.worker.min.js"></script>

<!-- Mammoth.js -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/mammoth/1.4.2/mammoth.browser.min.js"></script>

<!-- Dump Handler -->
<script src="assets/js/dumpFileIntoNote.js"></script>

</body>
</html>