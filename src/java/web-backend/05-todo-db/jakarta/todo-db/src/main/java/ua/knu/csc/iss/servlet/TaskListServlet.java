package ua.knu.csc.iss.servlet;


import ua.knu.csc.iss.dao.TaskDAO;
import ua.knu.csc.iss.model.Task;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/tasks")
public class TaskListServlet extends HttpServlet {
    private final TaskDAO taskDAO = new TaskDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Task> taskList = taskDAO.selectAllTasks();
            request.setAttribute("taskList", taskList);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Unable to retrieve tasks", e);
        }
    }
}

