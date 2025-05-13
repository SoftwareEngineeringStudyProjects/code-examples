package ua.knu.csc.iss.servlet;

import ua.knu.csc.iss.dao.TaskDAO;
import ua.knu.csc.iss.model.Task;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/add")
public class AddTaskServlet extends HttpServlet {
    private final TaskDAO taskDAO = new TaskDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);

        try {
            taskDAO.insertTask(task);
            response.sendRedirect("tasks");
        } catch (SQLException e) {
            throw new ServletException("Failed to add task", e);
        }
    }
}

