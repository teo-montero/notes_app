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

@WebServlet("/DeleteNoteServlet")
public class DeleteNoteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the NoteHandler instance
        NoteHandler noteHandler = ModelFactory.getModel().getNoteHandler();
        IndexHandler indexHandler = ModelFactory.getModel().getIndexHandler();

        // Get the note ID from request
        String noteIDParam = request.getParameter("noteID");
        String indexIDParam = request.getParameter("indexID");

        if (noteIDParam != null && !noteIDParam.isEmpty()) {
            UUID noteID = UUID.fromString(noteIDParam);
            noteHandler.removeContentFromSystem(noteID);
            indexHandler.removeContextFromIndex(noteID, UUID.fromString(indexIDParam));
        }

        response.sendRedirect("index.jsp");
    }
}