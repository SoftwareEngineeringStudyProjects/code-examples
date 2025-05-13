package ua.knu.csc.iss.servlet;


import ua.knu.csc.iss.dao.TaskDAO;
import ua.knu.csc.iss.model.Task;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/update")
public class UpdateTaskServlet extends HttpServlet {
    private final TaskDAO taskDAO = new TaskDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        boolean status = request.getParameter("status") != null;

        Task task = new Task(id, title, description, status);

        try {
            taskDAO.updateTask(task);
            response.sendRedirect("tasks");
        } catch (SQLException e) {
            throw new ServletException("Failed to update task", e);
        }
    }
}
