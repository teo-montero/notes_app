package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Category;
import uk.ac.ucl.model.CategoryHandler;
import uk.ac.ucl.model.ModelFactory;
import uk.ac.ucl.model.Note;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@WebServlet("/CreateCategoryServlet")
public class CreateCategoryServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CategoryHandler categoryHandler = ModelFactory.getModel().getCategoryHandler();

        String categoryName = request.getParameter("categoryName");

        if (categoryName == null || categoryName.trim().isEmpty()) {
            response.sendRedirect("index.jsp?error=InvalidCategory");
            return;
        }

        Category newCategory = new Category(categoryName.trim());

        categoryHandler.addContentToSystem(newCategory.getID(), newCategory);

        response.sendRedirect(request.getParameter("from"));
    }
}