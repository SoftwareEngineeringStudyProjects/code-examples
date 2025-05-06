package ua.knu.csc.iss;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/contact")
public class ContactFormPRGServlet extends HttpServlet {
    private static final String FILE_PATH = "./contacts.csv"; // Change as needed

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name") != null ? request.getParameter("name").trim() : "";
        String email = request.getParameter("email") != null ? request.getParameter("email").trim() : "";
        String message = request.getParameter("message") != null ? request.getParameter("message").trim() : "";

        if (name.isEmpty() || email.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Name and email are required.");
            return;
        }

        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();

        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter writer = new PrintWriter(bw)) {

            writer.printf("\"%s\",\"%s\",\"%s\"%n", name, email, message.replace("\"", "\"\""));
        }

        // Use URL encoding to safely pass user input
        String redirectUrl = request.getContextPath() + "/contact-success" +
                "?name=" + URLEncoder.encode(name, StandardCharsets.UTF_8) +
                "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8) +
                "&message=" + URLEncoder.encode(message, StandardCharsets.UTF_8);

        response.sendRedirect(redirectUrl);
    }
}
