package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import uk.ac.ucl.model.*;

@WebServlet("/UpdateCategoryServlet")
public class UpdateCategoryServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        NoteHandler noteHandler = ModelFactory.getModel().getNoteHandler();
        CategoryHandler categoryHandler = ModelFactory.getModel().getCategoryHandler();

        try {
            UUID noteID = UUID.fromString(request.getParameter("noteID"));
            Note note = noteHandler.getContentFromID(noteID);
            UUID categoryID = UUID.fromString(request.getParameter("categoryID"));
            boolean isChecked = Boolean.parseBoolean(request.getParameter("checked"));

            if(note != null) {
                if(isChecked) {
                    categoryHandler.addNoteToCategory(note, categoryID);
                } else {
                    categoryHandler.removeNoteFromCategory(note, categoryID);
                }
                noteHandler.saveContent(note);
            }

            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}