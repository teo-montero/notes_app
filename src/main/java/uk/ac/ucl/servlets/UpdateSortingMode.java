package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.CategoryHandler;
import uk.ac.ucl.model.IndexHandler;
import uk.ac.ucl.model.ModelFactory;
import uk.ac.ucl.model.SortMode;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/UpdateSortingMode")
public class UpdateSortingMode extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IndexHandler indexHandler = ModelFactory.getModel().getIndexHandler();

        String sortingMode = request.getParameter("sortingMode");

        indexHandler.setSortingMode(switch(sortingMode) {
            case "nameAscending" -> SortMode.NAME_ASCENDING;
            case "nameDescending" -> SortMode.NAME_DESCENDING;
            case "typeAscending" -> SortMode.TYPE_ASCENDING;
            case "typeDescending" -> SortMode.TYPE_DESCENDING;
            case "dateAscending" -> SortMode.DATE_ASCENDING;
            case "dateDescending" -> SortMode.DATE_DESCENDING;
            default -> SortMode.NONE;
        });

        response.sendRedirect("index.jsp?indexID=" + request.getParameter("indexID"));
    }
}