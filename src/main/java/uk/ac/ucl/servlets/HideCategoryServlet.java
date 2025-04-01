package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.CategoryHandler;
import uk.ac.ucl.model.ModelFactory;
import uk.ac.ucl.model.Note;
import uk.ac.ucl.model.NoteHandler;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/HideCategoryServlet")
public class HideCategoryServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CategoryHandler categoryHandler = ModelFactory.getModel().getCategoryHandler();

        UUID categoryID = UUID.fromString(request.getParameter("categoryID"));
        boolean isChecked = Boolean.parseBoolean(request.getParameter("checked"));

        if(isChecked) {
            categoryHandler.hideCategory(categoryID);
        } else {
            categoryHandler.showCategory(categoryID);
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}