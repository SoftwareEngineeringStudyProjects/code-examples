package ua.knu.csc.iss.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class BuiltinHttpClient {

    public static void main(String[] args) {
        try {
            // Define the URL for the /hello endpoint
            String url = "http://localhost:8080/hello";

            // Create a URL object
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            // Set the request method
            connection.setRequestMethod("GET");

            // Log the HTTP request
            System.out.println("Sending HTTP request:");
            System.out.println("Request Method: " + connection.getRequestMethod());
            System.out.println("Request URL: " + connection.getURL());

            // Log request headers
            System.out.println("Request Headers:");
            connection.getRequestProperties().forEach((key, value) -> {
                System.out.println(key + ": " + String.join(", ", value));
            });

            // Get and log the response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Log the response headers
            System.out.println("Response Headers:");
            connection.getHeaderFields().forEach((key, value) -> {
                if (key != null) {  // Filter out the null key (this represents the status line)
                    System.out.println(key + ": " + String.join(", ", value));
                }
            });

            // Read the response body
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder responseBody = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                responseBody.append(inputLine);
            }
            in.close();

            // Log the response body
            System.out.println("Response Body:");
            System.out.println(responseBody.toString());

            // Close the connection
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
