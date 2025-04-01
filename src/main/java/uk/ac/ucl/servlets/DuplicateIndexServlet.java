package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import uk.ac.ucl.model.IndexHandler;
import uk.ac.ucl.model.NoteHandler;
import uk.ac.ucl.model.ModelFactory;

@WebServlet("/DuplicateIndexServlet")
public class DuplicateIndexServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the NoteHandler instance
        NoteHandler noteHandler = ModelFactory.getModel().getNoteHandler();
        IndexHandler indexHandler = ModelFactory.getModel().getIndexHandler();

        // Get the note ID from request
        String parentIDParam = request.getParameter("parentID");
        String indexIDParam = request.getParameter("indexID");

        UUID duplicatedIndex = indexHandler.duplicateIndex(UUID.fromString(indexIDParam), UUID.fromString(parentIDParam));

        response.sendRedirect("index.jsp?indexID=" + parentIDParam);
    }
}