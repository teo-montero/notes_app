<%@ page import="java.util.UUID" %>
<%@ page import="uk.ac.ucl.model.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.io.File" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    NoteHandler noteHandler = ModelFactory.getModel().getNoteHandler();
    CategoryHandler categoryHandler = ModelFactory.getModel().getCategoryHandler();

    ArrayList<Category> categories = categoryHandler.getContents();

    UUID noteID = UUID.fromString(request.getParameter("id"));
    Note note = noteHandler.getContentFromID(noteID);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Note</title>
    <link rel="stylesheet" href="assets/css/styles.css">
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
</head>
<body>

<jsp:include page="header.jsp" />

<div class="container mt-4">
    <h2 class="mb-3">Edit Note</h2>

    <div class="card p-4 shadow-sm">
        <div class="d-flex gap-2">
            <div class="mb-3 dropdown">
                <button class="btn btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
                    Manage Categories
                </button>
                <ul class="dropdown-menu p-2">
                    <% for (Category category : categories) { %>
                    <li class="d-flex align-items-center">
                        <!-- Checkbox -->
                        <input type="checkbox" class="form-check-input me-2" id="category-<%= note.getID() %>-<%= category.getID() %>"
                            <%= note.belongsToCategory(category.getID()) ? "checked" : "" %>
                               onchange="updateCategory('<%= note.getID() %>', '<%= category.getID() %>', this.checked)">

                        <label class="form-check-label" for="category-<%= note.getID() %>-<%= category.getID() %>">
                            <%= category.getName() %>
                        </label>
                    </li>
                    <% } %>
                    <li><hr class="dropdown-divider"></li>
                    <li>
                        <button class="btn btn-primary w-100" data-bs-toggle="modal" data-bs-target="#addCategoryModal">Add Category</button>
                    </li>
                </ul>
            </div>

            <div class="mb-3">
                <button type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#dumpModal">
                    Dump File into Note
                </button>
            </div>

            <div class="mb-3 ms-auto">
                <button type="button" class="btn btn-danger" id="clearNoteBtn">
                    Clear Text Content
                </button>
            </div>
        </div>

        <form action="UpdateNoteServlet" method="post" enctype="multipart/form-data">

            <input type="hidden" name="noteID" value="<%= note.getID() %>">

            <div class="mb-3">
                <label for="title" class="form-label">Title:</label>
                <input type="text" id="title" name="title" class="form-control" value="<%= note.getTitle() %>" required>
            </div>

            <div class="mb-3">
                <label for="content" class="form-label">Content:</label>
                <textarea id="content" name="content" rows="5" class="form-control"><%= String.join("\n", note.getTextContent()).trim() %></textarea>
            </div>

            <div class="mb-3">
                <label for="imageInput" class="form-label">Upload Image:</label>
                <div class="row g-2">
                    <div class="col">
                        <input class="form-control" type="file" id="imageInput" name="image" accept="image/*">
                        <input type="hidden" id="imageAction" name="imageAction" value="keep">
                    </div>
                    <div class="col-auto">
                        <button type="button" class="btn btn-outline-danger" onclick="removeImage()" id="removeImageBtn">
                            Remove Image
                        </button>
                    </div>
                </div>
            </div>


            <% if (note.getImage() != null && !note.getImage().isEmpty()) { %>
            <div class="mb-2">
                <img id="currentImage" src="<%= note.getImage().substring(16) %>" alt="Note Image" style="max-width: 400px; max-height: 400px;">
            </div>
            <% } %>

            <div class="mb-3">
                <img id="imagePreview" alt="" src="" style="display:none; max-width: 400px; max-height: 400px;">
            </div>

            <hr>

            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-success">Save Note</button>

                <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#confirmDeleteModal">
                    Delete Note
                </button>

                <a href="index.jsp?indexID=<%= request.getParameter("indexID") %>" class="btn btn-secondary">Go Back</a>
            </div>
        </form>

    </div>
</div>

<div class="modal fade" id="confirmDeleteModal" tabindex="-1" aria-labelledby="confirmDeleteModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="confirmDeleteModalLabel">Confirm Deletion</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                Are you sure you want to delete this note?
            </div>
            <div class="modal-footer">
                <form action="DeleteNoteServlet" method="post">
                    <input type="hidden" name="noteID" value="<%= noteID %>">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-danger">Delete</button>
                </form>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="addCategoryModal" tabindex="-1" aria-labelledby="addCategoryModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addCategoryModalLabel">Add Category</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form action="CreateCategoryServlet" method="post">
                    <input type="hidden" name="from" value="editNote.jsp?id=<%= noteID %>">
                    <div class="mb-3">
                        <label for="categoryName" class="form-label">Category Name:</label>
                        <input type="text" id="categoryName" name="categoryName" class="form-control" required>
                    </div>

                    <button type="submit" class="btn btn-success">Add Category</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                </form>
            </div>
        </div>
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

<script src="assets/js/updateCategoryRequest.js"></script>

<!-- Include Footer -->
<jsp:include page="footer.jsp" />

<script>
    document.getElementById("clearNoteBtn").addEventListener("click", function () {
        const contentBox = document.querySelector("textarea[name='content']");
        if (contentBox) {
            contentBox.value = "";
        }
    });
</script>

<script>
    document.getElementById("imageInput").addEventListener("change", function(event) {
        const file = event.target.files[0];
        const preview = document.getElementById("imagePreview");
        const currentImage = document.getElementById("currentImage");
        const imageAction = document.getElementById("imageAction");

        if (file && file.type.startsWith("image/")) {
            const reader = new FileReader();
            reader.onload = function(e) {
                preview.src = e.target.result;
                preview.style.display = "block";
                if (currentImage) currentImage.style.display = "none";
                imageAction.value = "upload"; // mark for new image
            };
            reader.readAsDataURL(file);
        } else {
            // If file input is cleared
            preview.src = "";
            preview.style.display = "none";
            imageAction.value = "none";
        }
    });

    function removeImage() {
        const fileInput = document.getElementById("imageInput");
        const preview = document.getElementById("imagePreview");
        const currentImage = document.getElementById("currentImage");
        const imageAction = document.getElementById("imageAction");

        // Clear the file input (removes the new file if one was selected)
        if (fileInput) fileInput.value = "";
        if (preview) {
            preview.src = "";
            preview.style.display = "none";
        }

        // If an existing image was present → we want to remove it
        if (currentImage) {
            currentImage.style.display = "none";
            imageAction.value = "remove";
        } else {
            // No image on the note, just clearing selected file
            imageAction.value = "none";
        }
    }
</script>

<script src="assets/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/updateCategoryRequest.js"></script>


<!-- PDF.js -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/2.10.377/pdf.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/2.10.377/pdf.worker.min.js"></script>

<!-- Mammoth.js -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/mammoth/1.4.2/mammoth.browser.min.js"></script>

<!-- Dump Handler -->
<script src="assets/js/dumpFileIntoNote.js"></script>

</body>
</html>