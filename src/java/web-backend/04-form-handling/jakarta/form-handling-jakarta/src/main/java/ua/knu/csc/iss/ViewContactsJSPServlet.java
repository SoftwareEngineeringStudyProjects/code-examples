package ua.knu.csc.iss;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/view-contacts-jsp")
public class ViewContactsJSPServlet extends HttpServlet {
    private static final String FILE_PATH = "./contacts.csv"; // Match path from ContactFormServlet

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        File file = new File(FILE_PATH);
        List<List<String>> contacts = new ArrayList<>();

        if (file.exists() && file.length() > 0) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] columns = parseCsvLine(line);
                    contacts.add(Arrays.asList(columns));
                }
            }
        }

        request.setAttribute("contacts", contacts);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/viewContacts.jsp");
        dispatcher.forward(request, response);
    }

    private String[] parseCsvLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }
}

