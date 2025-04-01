<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="java.util.ArrayList, java.util.UUID" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="uk.ac.ucl.model.*" %>

<%
  NoteHandler noteHandler = ModelFactory.getModel().getNoteHandler();
  CategoryHandler categoryHandler = ModelFactory.getModel().getCategoryHandler();
  IndexHandler indexHandler = ModelFactory.getModel().getIndexHandler();

  UUID indexID = request.getParameter("indexID") == null || request.getParameter("indexID").equals("null") ? indexHandler.getHomeIndex() : UUID.fromString(request.getParameter("indexID"));

  String query = request.getParameter("query");

  ArrayList<UUID> searchResults = noteHandler.searchQueryInNotes(query);
%>

<div class="container mt-4">
  <h2 class="mb-3">My Notes</h2>

  <div class="d-flex gap-2 mb-3">
    <h4 class="mb-0">
      Search Results: "<%= query %>"
    </h4>

    <div class="ms-auto">
      <form action="index.jsp" method="get" class="d-flex" role="search">
        <input type="hidden" name="action" value='search'>
        <input type="hidden" name="indexID" value='<%= indexID %>'>
        <input class="form-control me-2" type="search" name="query" placeholder="Search..." aria-label="Search">
        <button class="btn btn-outline-secondary" type="submit">Search</button>
      </form>
    </div>
  </div>

  <h5><%= searchResults.size() %> Found </h5>

  <% if (searchResults.isEmpty()) { %>
    <p class="text-muted">No matches found...</p>
  <% } else { %>
    <table class="table table-striped">
      <thead>
        <tr>
          <th>Title</th>
          <th>Type</th>
          <th>Path</th>
        </tr>
      </thead>
      <tbody>
        <% for (UUID element : searchResults) {
          Note note = noteHandler.getContentFromID(element);
          UUID noteParentIndex = indexHandler.getIndexToWhichNoteBelongs(note.getID());
          ArrayList<Index> indexPath = indexHandler.getIndexPath(noteParentIndex);
        %>
          <tr>
            <td>
              <a href="editNote.jsp?id=<%= note.getID() %>&indexID=<%= noteParentIndex %>" class="text-decoration-none">
                <i class="bi bi-file-earmark-text"></i> <%= note.getTitle() %>
              </a>
            </td>

            <td>
              <i class="bi bi-file-earmark-text"></i> Note
            </td>

            <td>
              <a href="index.jsp?indexID=<%= noteParentIndex %>" class="text-decoration-none">
                <% for(Index noteIndex : indexPath) { %>
                  <%= noteIndex.getName() %> >
                <% } %>
                <%= note.getTitle() %>
              </a>
            </td>
          </tr>
        <% } %>
      </tbody>
    </table>
  <% } %>

  <div class="d-flex gap-2">
    <div class="mt-3">
      <a href="index.jsp?indexID=<%= indexID %>" class="btn btn-secondary">Go Back</a>
    </div>
  </div>
</div>