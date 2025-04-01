package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.main.Main;
import uk.ac.ucl.model.ModelFactory;

import java.io.IOException;

@WebServlet("/ShutdownServlet")
public class ShutdownServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("Server is shutting down...");

        // Optional: Cleanup resources before shutting down
        cleanupResources();

        // Shut down the Tomcat server
        System.exit(1);
    }

    private void cleanupResources() {
        ModelFactory.getModel().shutdown();
    }
}