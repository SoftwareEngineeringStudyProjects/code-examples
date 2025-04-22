package ua.knu.csc.iss;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@WebServlet("/greeting")
public class GreetingServlet extends HttpServlet {

    private static final String[] QUOTES = {
            "Keep it simple.",
            "Stay hungry, stay foolish.",
            "The best way to get started is to quit talking and begin doing.",
            "Dream big. Start small. Act now.",
            "Code is like humor. When you have to explain it, it’s bad."
    };

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set content type
        response.setContentType("text/html");

        // Get current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy hh:mm:ss a");
        String formattedDateTime = now.format(formatter);

        // Pick a random quote
        Random rand = new Random();
        String quote = QUOTES[rand.nextInt(QUOTES.length)];

        // Generate HTML response
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Greeting Page</title></head><body>");
        out.println("<h1>Welcome to Our Site!</h1>");
        out.println("<p>Current server time: <strong>" + formattedDateTime + "</strong></p>");
        out.println("<blockquote>\"" + quote + "\"</blockquote>");
        out.println("</body></html>");
    }
}
