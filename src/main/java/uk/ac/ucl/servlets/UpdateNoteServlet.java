package uk.ac.ucl.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;
import uk.ac.ucl.model.Note;
import uk.ac.ucl.model.NoteHandler;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@WebServlet("/UpdateNoteServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class UpdateNoteServlet extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        ServletContext context = getServletContext();
        NoteHandler noteHandler = ModelFactory.getModel().getNoteHandler();

        UUID noteID = UUID.fromString(request.getParameter("noteID"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String imageAction = request.getParameter("imageAction");
        Part image = request.getPart("image");

        noteHandler.updateNote(noteID, title, new ArrayList<>(List.of(content.split("\\r?\\n"))), image, imageAction);
        response.sendRedirect("editNote.jsp?id=" + noteID);
    }
}