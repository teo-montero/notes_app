package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import org.apache.tomcat.util.log.UserDataHelper;
import uk.ac.ucl.model.Index;
import uk.ac.ucl.model.IndexHandler;
import uk.ac.ucl.model.NoteHandler;
import uk.ac.ucl.model.ModelFactory;

@WebServlet("/DuplicateNoteServlet")
public class DuplicateNoteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the NoteHandler instance
        NoteHandler noteHandler = ModelFactory.getModel().getNoteHandler();
        IndexHandler indexHandler = ModelFactory.getModel().getIndexHandler();

        // Get the note ID from request
        String noteIDParam = request.getParameter("noteID");
        String indexIDParam = request.getParameter("indexID");

        if (noteIDParam != null && !noteIDParam.isEmpty()) {
            UUID clonedNoteID = noteHandler.cloneNote(UUID.fromString(noteIDParam));
            indexHandler.addContentToIndex(clonedNoteID, UUID.fromString(indexIDParam));
        }

        response.sendRedirect("index.jsp?indexID=" + indexIDParam);
    }
}