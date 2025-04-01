package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.*;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/MoveNoteServlet")
public class MoveNoteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IndexHandler indexHandler = ModelFactory.getModel().getIndexHandler();

        UUID noteID = UUID.fromString(request.getParameter("noteID"));
        UUID currentIndexID = UUID.fromString(request.getParameter("currentIndexID"));
        UUID newIndexID = UUID.fromString(request.getParameter("newIndexID"));

        indexHandler.addContentToIndex(noteID, newIndexID);
        indexHandler.removeContextFromIndex(noteID, currentIndexID);

        response.sendRedirect("index.jsp?indexID=" + currentIndexID);
    }
}