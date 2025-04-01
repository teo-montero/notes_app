package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.tomcat.util.log.UserDataHelper;
import uk.ac.ucl.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@WebServlet("/CreateNoteServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class CreateNoteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get NoteHandler from ModelFactory
        NoteHandler noteHandler = (NoteHandler)ModelFactory.getModel().getNoteHandler();
        ImageHandler imageHandler = ModelFactory.getModel().getImageHandler();
        IndexHandler indexHandler = ModelFactory.getModel().getIndexHandler();

        // Get form parameters
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        Part image = request.getPart("image");
        String index = request.getParameter("indexID");

        // Convert content into a list of lines
        ArrayList<String> textContent = new ArrayList<>(Arrays.asList(content.split("\\r?\\n"))); // Splitting by new lines

        // Create a new note with a unique ID
        Note newNote = new Note(title, textContent);
        indexHandler.addContentToIndex(newNote.getID(), UUID.fromString(index));

        // Save the new note
        noteHandler.addContentToSystem(newNote.getID(), newNote);

        if(image != null && image.getSize() > 0) {
            noteHandler.addImageToNote(image, "upload", newNote.getID());
        }

        // Redirect back to the main page
        response.sendRedirect("index.jsp?indexID=" + request.getParameter("indexID"));
    }
}