package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import uk.ac.ucl.model.IndexHandler;
import uk.ac.ucl.model.ModelFactory;

@WebServlet("/DeleteIndexServlet")
public class DeleteIndexServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the NoteHandler instance
        IndexHandler indexHandler = ModelFactory.getModel().getIndexHandler();

        // Get the note ID from request
        String parentIDParam = request.getParameter("parentID");
        String indexIDParam = request.getParameter("indexID");

        UUID parentID = UUID.fromString(parentIDParam);
        UUID indexID = UUID.fromString(indexIDParam);

        indexHandler.removeIndex(indexID);
        indexHandler.removeContextFromIndex(indexID, parentID);

        response.sendRedirect("index.jsp?indexID=" + parentIDParam);
    }
}