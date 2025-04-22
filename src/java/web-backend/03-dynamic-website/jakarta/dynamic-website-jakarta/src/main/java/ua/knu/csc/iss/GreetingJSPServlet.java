package ua.knu.csc.iss;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@WebServlet("/greeting-jsp")
public class GreetingJSPServlet extends HttpServlet {


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

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy hh:mm:ss a");
        String formattedDateTime = now.format(formatter);

        Random rand = new Random();
        String quote = QUOTES[rand.nextInt(QUOTES.length)];

        // Set attributes to pass to JSP
        request.setAttribute("serverTime", formattedDateTime);
        request.setAttribute("quote", quote);

        // Forward request to JSP page
        request.getRequestDispatcher("/greeting.jsp").forward(request, response);
    }



}
