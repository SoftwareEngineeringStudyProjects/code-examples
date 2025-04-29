package ua.knu.csc.iss;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

@WebServlet("/contact")
public class ContactFormServlet extends HttpServlet {
    private static final String FILE_PATH = "./contacts.csv";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name").trim();
        String email = request.getParameter("email").trim();
        String message = request.getParameter("message") == null ? "" : request.getParameter("message").trim();

        if (name.isEmpty() || email.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Name and email are required.");
            return;
        }
        File file = new File(FILE_PATH);

        // Ensure the parent directory exists
        file.getParentFile().mkdirs();

        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter writer = new PrintWriter(bw)) {
            writer.printf("\"%s\",\"%s\",\"%s\"%n", name, email, message.replace("\"", "\"\""));
            // Output success message to server log
            System.out.println("Contact info written to file: " + file.getName());
            System.out.println("Absolute file path: " + file.getAbsolutePath());
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<h3>Thank you for your submission!</h3>");
    }
}


