<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="java.util.ArrayList, java.util.UUID" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="uk.ac.ucl.model.*" %>

<%
  NoteHandler noteHandler = ModelFactory.getModel().getNoteHandler();
  CategoryHandler categoryHandler = ModelFactory.getModel().getCategoryHandler();
  IndexHandler indexHandler = ModelFactory.getModel().getIndexHandler();

  UUID indexID = request.getParameter("indexID") == null || request.getParameter("indexID").equals("null") ? indexHandler.getHomeIndex() : UUID.fromString(request.getParameter("indexID"));

  Index index = indexHandler.getContentFromID(indexID);
  ArrayList<UUID> indexContent = index.getElements(indexHandler.getSortingMode());
  ArrayList<Category> categories = categoryHandler.getContents();
%>

<div class="container mt-4">
  <h2 class="mb-3">My Notes</h2>

  <div class="d-flex gap-2 mb-3">
    <h4 class="mb-0">
      <% for (Index indexInPath : indexHandler.getIndexPath(indexID)) { %>
      <a class="index-path-link" href="index.jsp?id=<%= indexInPath.getID() %>">
        <%= indexInPath.getName() %>
      </a>
      >
      <% } %>
    </h4>

    <div class="d-flex gap-2 ms-auto">
      <div class="dropdown">
        <button class="btn btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
          Sort
        </button>
        <ul class="dropdown-menu p-2">
          <li class="d-flex align-items-center dropdown-link-element">
            <form action="UpdateSortingMode" method="post" class="d-inline">
              <input type="hidden" name="sortingMode" value="none">
              <button type="submit" class="dropdown-link-style"><% if(indexHandler.getSortingMode() == SortMode.NONE) { %> <b> <% } %> None <% if(indexHandler.getSortingMode() == SortMode.NONE) { %> </b> <% } %> </button>
            </form>
          </li>
          <li class="d-flex align-items-center dropdown-link-element">
            <form action="UpdateSortingMode" method="post" class="d-inline">
              <input type="hidden" name="sortingMode" value="nameAscending">
              <button type="submit" class="dropdown-link-style"><% if(indexHandler.getSortingMode() == SortMode.NAME_ASCENDING) { %> <b> <% } %> Name ↑ <% if(indexHandler.getSortingMode() == SortMode.NAME_ASCENDING) { %> </b> <% } %></button>
            </form>
          </li>
          <li class="d-flex align-items-center dropdown-link-element">
            <form action="UpdateSortingMode" method="post" class="d-inline">
              <input type="hidden" name="sortingMode" value="nameDescending">
              <button type="submit" class="dropdown-link-style"><% if(indexHandler.getSortingMode() == SortMode.NAME_DESCENDING) { %> <b> <% } %> Name ↓ <% if(indexHandler.getSortingMode() == SortMode.NAME_DESCENDING) { %> </b> <% } %></button>
            </form>
          </li>
          <li class="d-flex align-items-center dropdown-link-element">
            <form action="UpdateSortingMode" method="post" class="d-inline">
              <input type="hidden" name="sortingMode" value="typeAscending">
              <button type="submit" class="dropdown-link-style"><% if(indexHandler.getSortingMode() == SortMode.TYPE_ASCENDING) { %> <b> <% } %> Type ↑ <% if(indexHandler.getSortingMode() == SortMode.TYPE_ASCENDING) { %> </b> <% } %></button>
            </form>
          </li>
          <li class="d-flex align-items-center dropdown-link-element">
            <form action="UpdateSortingMode" method="post" class="d-inline">
              <input type="hidden" name="sortingMode" value="typeDescending">
              <button type="submit" class="dropdown-link-style"><% if(indexHandler.getSortingMode() == SortMode.TYPE_DESCENDING) { %> <b> <% } %> Type ↓ <% if(indexHandler.getSortingMode() == SortMode.TYPE_DESCENDING) { %> </b> <% } %> </button>
            </form>
          </li>
          <li class="d-flex align-items-center dropdown-link-element">
            <form action="UpdateSortingMode" method="post" class="d-inline">
              <input type="hidden" name="sortingMode" value="dateAscending">
              <button type="submit" class="dropdown-link-style"><% if(indexHandler.getSortingMode() == SortMode.DATE_ASCENDING) { %> <b> <% } %> Last Edit Date ↑ <% if(indexHandler.getSortingMode() == SortMode.DATE_ASCENDING) { %> </b> <% } %> </button>
            </form>
          </li>
          <li class="d-flex align-items-center dropdown-link-element">
            <form action="UpdateSortingMode" method="post" class="d-inline">
              <input type="hidden" name="sortingMode" value="dateDescending">
              <button type="submit" class="dropdown-link-style"><% if(indexHandler.getSortingMode() == SortMode.DATE_DESCENDING) { %> <b> <% } %> Last Edit Date ↓ <% if(indexHandler.getSortingMode() == SortMode.DATE_DESCENDING) { %> </b> <% } %> </button>
            </form>
          </li>
        </ul>
      </div>


      <div class="dropdown m-auto">
        <button class="btn btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
          Hide Categories
        </button>
        <ul class="dropdown-menu p-2">
          <% for (Category category : categories) { %>
          <li class="d-flex align-items-center">
            <input type="checkbox" class="form-check-input me-2" id="hide-category-<%= category.getID() %>"
              <%= categoryHandler.isCategoryHidden(category.getID()) ? "checked" : "" %>
                   onchange="hideCategory('<%= index.getID() %>', '<%= category.getID() %>', this.checked)">
            <label class="form-check-label" for="hide-category-<%= category.getID() %>">
              <%= category.getName() %>
            </label>
          </li>
          <% } %>
        </ul>
      </div>

      <div class="m-auto">
        <form action="index.jsp" method="get" class="d-flex" role="search">
          <input type="hidden" name="action" value='search'>
          <input type="hidden" name="indexID" value='<%= indexID %>'>
          <input class="form-control me-2" type="search" name="query" placeholder="Search..." aria-label="Search">
          <button class="btn btn-outline-secondary" type="submit">Search</button>
        </form>
      </div>
    </div>
  </div>

  <% if (indexContent.isEmpty()) { %>
    <p class="text-muted">No notes available.</p>
  <% } else { %>
    <table class="table table-striped">
      <thead>
        <tr>
          <th>Title</th>
          <th>Type</th>
          <th>Categories</th>
          <th>Last Edited</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <% for (UUID element : indexContent) {
            if(!indexHandler.isIndex(element)) {
              Note note = noteHandler.getContentFromID(element);
              boolean hideNote = note.getCategories().stream().anyMatch(categoryHandler::isCategoryHidden);
              if(!hideNote) { %>
                <tr>
                  <td>
                    <a href="editNote.jsp?id=<%= note.getID() %>&indexID=<%= indexID %>" class="text-decoration-none">
                      <i class="bi bi-file-earmark-text"></i> <%= note.getTitle() %>
                    </a>
                  </td>
                  <td>
                    <i class="bi bi-file-earmark-text"></i> Note
                  </td>
                  <td>
                    <div class="dropdown">
                      <button class="btn btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
                        Manage Categories
                      </button>
                      <ul class="dropdown-menu p-2">
                        <% for (Category category : categories) { %>
                          <li class="d-flex align-items-center">
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
                  </td>

                  <td><%= note.getLastEditDate() %></td>

                  <td>
                    <div class="d-flex gap-2 mb-3">
                      <div class="dropdown">
                        <button class="btn btn-secondary dropdown-toggle" type="button" id="moveMenu" data-bs-toggle="dropdown">
                          Move To
                        </button>
                        <ul class="dropdown-menu" data-bs-auto-close="outside">
                          <% for(Index i : indexHandler.getContents()) { %>
                            <li>
                              <form action="MoveNoteServlet" method="post">
                                <input type="hidden" name="noteID" value="<%= note.getID() %>">
                                <input type="hidden" name="newIndexID" value="<%= i.getID() %>">
                                <input type="hidden" name="currentIndexID" value="<%= indexID %>">
                                <button type="submit" class="dropdown-item"><%= i.getName() %></button>
                              </form>
                            </li>
                          <% } %>
                        </ul>
                      </div>

                      <div class="dropdown">
                        <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenu<%= note.getID() %>" data-bs-toggle="dropdown" aria-expanded="false">
                          Actions
                        </button>
                        <ul class="dropdown-menu" aria-labelledby="dropdownMenu<%= note.getID() %>">
                          <li>
                            <form action="DuplicateNoteServlet" method="post">
                              <input type="hidden" name="noteID" value="<%= note.getID() %>">
                              <input type="hidden" name="indexID" value="<%= indexID %>">
                              <button type="submit" class="dropdown-item">Duplicate</button>
                              </form>
                          </li>
                          <li>
                            <button class="dropdown-item text-danger" data-bs-toggle="modal" data-bs-target="#confirmDeleteModal" onclick="setDeleteNoteID('<%= note.getID() %>')">
                              Delete
                            </button>
                          </li>
                        </ul>
                      </div>
                    </div>
                  </td>
                </tr>
              <% } %>
            <% } else {
                Index subindex = indexHandler.getContentFromID(element);
            %>
              <tr>
                <td>
                  <a href="index.jsp?indexID=<%= subindex.getID() %>" class="text-decoration-none">
                    <i class="bi bi-folder"></i> <%= subindex.getName() %>
                  </a>
                </td>
                <td>
                  <i class="bi bi-folder"></i> Index
                </td>
                <td>
                  -
                </td>
                <td>
                  -
                </td>
                <td>
                  <div class="dropdown">
                    <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenu<%= subindex.getID() %>" data-bs-toggle="dropdown" aria-expanded="false">
                      Actions
                    </button>
                    <ul class="dropdown-menu" aria-labelledby="dropdownMenu<%= subindex.getID() %>">
                      <li>
                        <form action="DuplicateIndexServlet" method="post">
                          <input type="hidden" name="indexID" value="<%= subindex.getID() %>">
                          <input type="hidden" name="parentID" value="<%= indexID %>">
                          <button type="submit" class="dropdown-item">Duplicate</button>
                        </form>
                      </li>
                      <li>
                        <form action="DeleteIndexServlet" method="post">
                          <input type="hidden" name="indexID" value="<%= subindex.getID() %>">
                          <input type="hidden" name="parentID" value="<%= indexID %>">
                          <button type="submit" class="dropdown-item text-danger">Delete</button>
                        </form>
                      </li>
                    </ul>
                  </div>
                </td>
              </tr>
            <% } %>
        <% } %>
      </tbody>
    </table>
  <% } %>

  <div class="d-flex gap-2">
    <div class="mt-3">
      <a href="createNote.jsp?indexID=<%= index.getID().toString() %>" class="btn btn-success">Create a New Note</a>
    </div>

    <div class="mt-3">
      <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addIndexModal">Create a New Index</button>
    </div>

    <% if(index.getParent() != null) { %>
      <div class="mt-3">
        <a href="index.jsp?indexID=<%= index.getParent() %>" class="btn btn-secondary">Go Back</a>
      </div>
    <% } %>
  </div>
</div>

<script src="assets/js/hideCategoryRequest.js"></script>
<script src="assets/js/updateCategoryRequest.js"></script>

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
          <input type="hidden" id="deleteNoteID" name="noteID">
          <input type="hidden" name="indexID" value="<%= indexID %>">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
          <button type="submit" class="btn btn-danger">Delete</button>
        </form>
      </div>
    </div>
  </div>
</div>

<script>
  function setDeleteNoteID(noteID) {
    document.getElementById("deleteNoteID").value = noteID;
  }
</script>


<div class="modal fade" id="addCategoryModal" tabindex="-1" aria-labelledby="addCategoryModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="addCategoryModalLabel">Add Category</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <form action="CreateCategoryServlet" method="post">
          <input type="hidden" name="from" value="index.jsp?indexID=<%= indexID %>">

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


<div class="modal fade" id="addIndexModal" tabindex="-1" aria-labelledby="addIndexModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="addIndexModalLabel">Add Index</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <form action="CreateIndexServlet" method="post">
          <input type="hidden" name="parentID" value="<%= indexID %>">
          <div class="mb-3">
            <label for="indexName" class="form-label">Index Name:</label>
            <input type="text" id="indexName" name="indexName" class="form-control" required>
          </div>

          <button type="submit" class="btn btn-success">Add Index</button>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
        </form>
      </div>
    </div>
  </div>
</div>