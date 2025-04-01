package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@WebServlet("/CreateIndexServlet")
public class CreateIndexServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IndexHandler indexHandler = ModelFactory.getModel().getIndexHandler();

        UUID parentID = UUID.fromString(request.getParameter("parentID"));
        UUID newIndexID = indexHandler.createIndex(request.getParameter("indexName"), parentID);

        response.sendRedirect("index.jsp?indexID=" + newIndexID);
    }
}