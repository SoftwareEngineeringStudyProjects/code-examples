package ua.knu.csc.iss;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

@WebServlet("/contact-success")
public class ContactSuccessServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");

        if (name == null || email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing data.");
            return;
        }

        String obfuscatedEmail = obfuscateEmail(email);

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<h3>Thank you for your submission!</h3>");
        out.println("<p><strong>Name:</strong> " + escapeHtml(name) + "</p>");
        out.println("<p><strong>Email:</strong> " + obfuscatedEmail + "</p>");
        out.println("<p><strong>Message:</strong> " + (message == null || message.isEmpty() ? "(none)" : escapeHtml(message)) + "</p>");
    }

    private String obfuscateEmail(String email) {
        if (!email.contains("@")) return "*invalid email*";
        String[] parts = email.split("@", 2);
        return maskPart(parts[0]) + "@" + maskPart(parts[1]);
    }

    private String maskPart(String part) {
        if (part.length() <= 2) return part;
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
