package ua.knu.csc.iss;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

@WebServlet("/view-contacts")
public class ViewContactsServlet extends HttpServlet {
    private static final String FILE_PATH = "./contacts.csv"; // Match path from ContactFormServlet

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        File file = new File(FILE_PATH);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Contact List</title></head><body>");
        out.println("<h2>Submitted Contacts</h2>");

        if (!file.exists() || file.length() == 0) {
            out.println("<p>No contact data available.</p>");
        } else {
            out.println("<table border='1' cellpadding='5' cellspacing='0'>");
            out.println("<tr><th>Name</th><th>Email</th><th>Message</th></tr>");

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] columns = parseCsvLine(line);
                    out.println("<tr>");
                    for (String col : columns) {
                        out.println("<td>" + escapeHtml(col) + "</td>");
                    }
                    out.println("</tr>");
                }
            }
            out.println("</table>");
        }

        out.println("</body></html>");
    }

    // Parses a CSV line assuming comma-delimited, quotes handled
    private String[] parseCsvLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Handles quoted commas
    }

    // Escapes HTML special characters
    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}

