package ua.knu.csc.iss.servlet;

import ua.knu.csc.iss.dao.TaskDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/delete")
public class DeleteTaskServlet extends HttpServlet {
    private final TaskDAO taskDAO = new TaskDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        try {
            taskDAO.deleteTask(id);
            response.sendRedirect("tasks");
        } catch (SQLException e) {
            throw new ServletException("Failed to delete task", e);
        }
    }
}

