package ua.knu.csc.iss.dao;

import ua.knu.csc.iss.model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    private final String jdbcURL = "jdbc:postgresql://localhost:5432/todo_db";
    private final String jdbcUsername = "todo_user";
    private final String jdbcPassword = "todo_pass";

    private static final String INSERT_TASK = "INSERT INTO tasks (title, description) VALUES (?, ?)";
    private static final String SELECT_TASK = "SELECT * FROM tasks WHERE id = ?";
    private static final String SELECT_ALL_TASKS = "SELECT * FROM tasks ORDER BY id";
    private static final String DELETE_TASK = "DELETE FROM tasks WHERE id = ?";
    private static final String UPDATE_TASK = "UPDATE tasks SET title = ?, description = ?, status = ? WHERE id = ?";

    private Connection getConnection() throws SQLException {
        try { // fixes java.sql.SQLException: No suitable driver found
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }

    public void insertTask(Task task) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_TASK)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.executeUpdate();
        }
    }

    public Task selectTask(int id) throws SQLException {
        Task task = null;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_TASK)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                task = new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getBoolean("status")
                );
            }
        }
        return task;
    }

    public List<Task> selectAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_TASKS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getBoolean("status")
                ));
            }
        }
        return tasks;
    }

    public boolean deleteTask(int id) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_TASK)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateTask(Task task) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_TASK)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setBoolean(3, task.isStatus());
            stmt.setInt(4, task.getId());
            return stmt.executeUpdate() > 0;
        }
    }
}
