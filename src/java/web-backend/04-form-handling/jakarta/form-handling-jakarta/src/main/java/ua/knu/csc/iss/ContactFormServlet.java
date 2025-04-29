package ua.knu.csc.iss;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

@WebServlet("/contact")
public class ContactFormServlet extends HttpServlet {
    private static final String FILE_PATH = "./contacts.csv"; // Change as needed

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name") != null ? request.getParameter("name").trim() : "";
        String email = request.getParameter("email") != null ? request.getParameter("email").trim() : "";
        String message = request.getParameter("message") != null ? request.getParameter("message").trim() : "";

        if (name.isEmpty() || email.isEmpty()) {
            System.out.println("Validation failed: Missing required fields.");
            System.out.println("Received name: '" + name + "', email: '" + email + "'");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Name and email are required.");
            return;
        }

        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs(); // Ensure parent directory exists

        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter writer = new PrintWriter(bw)) {

            writer.printf("\"%s\",\"%s\",\"%s\"%n", name, email, message.replace("\"", "\"\""));

            System.out.println("Contact info written to file: " + file.getName());
            System.out.println("Absolute file path: " + file.getAbsolutePath());
        }

        // Obfuscate email
        String obfuscatedEmail = obfuscateEmail(email);

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<h3>Thank you for your submission!</h3>");
        out.println("<p><strong>Name:</strong> " + escapeHtml(name) + "</p>");
        out.println("<p><strong>Email:</strong> " + obfuscatedEmail + "</p>");
        out.println("<p><strong>Message:</strong> " + (message.isEmpty() ? "(none)" : escapeHtml(message)) + "</p>");
    }

    private String obfuscateEmail(String email) {
        if (!email.contains("@")) {
            return "*invalid email*"; // fallback
        }

        String[] parts = email.split("@", 2);
        String username = parts[0];
        String domain = parts[1];

        String obfuscatedUsername = maskPart(username);
        String obfuscatedDomain = maskPart(domain);

        return obfuscatedUsername + "@" + obfuscatedDomain;
    }

    private String maskPart(String part) {
        if (part.length() <= 2) {
            return part; // too short to mask
        }
        return part.charAt(0) + "*".repeat(part.length() - 2) + part.charAt(part.length() - 1);
    }


    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
