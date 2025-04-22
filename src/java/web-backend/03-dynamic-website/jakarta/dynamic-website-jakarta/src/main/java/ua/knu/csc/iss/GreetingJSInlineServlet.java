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

@WebServlet("/greeting-js-inline")
public class GreetingJSInlineServlet extends HttpServlet {

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

        response.setContentType("text/html");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' hh:mm:ss a");
        String formattedDateTime = now.format(formatter);

        Random rand = new Random();
        String quote = QUOTES[rand.nextInt(QUOTES.length)];

        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Greeting Page</title>");
        out.println("<script>");
        out.println("function updateTime() {");
        out.println("  const now = new Date();");
        out.println("  const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric'," +
                " hour: '2-digit', minute: '2-digit', second: '2-digit' };");
        out.println("  document.getElementById('clientTime').textContent = now.toLocaleString('en-US', options);");
        out.println("}");
        out.println("setInterval(updateTime, 1000);");
        out.println("window.onload = updateTime;");
        out.println("</script>");
        out.println("</head><body>");
        out.println("<h1>Welcome to Our Site!</h1>");
        out.println("<p>Current server time: <strong>" + formattedDateTime + "</strong></p>");
        out.println("<p>Current browser time: <strong id='clientTime'></strong></p>");
        out.println("<blockquote>\"" + quote + "\"</blockquote>");
        out.println("</body></html>");
    }

}
