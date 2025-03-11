package ua.knu.csc.iss.http;

import java.io.*;
import java.net.*;

public class SocketHttpClient {

    public static void main(String[] args) {
        String host = "localhost"; // Server address
        int port = 8080;           // Server port
        String path = "/hello";    // Endpoint to request

        try (Socket socket = new Socket(host, port)) {
            // Create input and output streams for communication
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // Send an HTTP GET request
            String request = "GET " + path + " HTTP/1.1\r\n"
                    + "Host: " + host + ":" + port + "\r\n"
                    + "User-Agent: SocketHttpClient/1.0\r\n"
                    + "Accept: */*\r\n"
                    + "\r\n";

            // Output the HTTP request for debugging purposes
            System.out.println("HTTP Request sent to server:\n" + request);

            out.writeBytes(request);  // Send the HTTP request

            // Read the server's response
            String responseLine;
            StringBuilder response = new StringBuilder();

            while ((responseLine = in.readLine()) != null) {
                response.append(responseLine).append("\n");
            }

            // Output the response from the server
            System.out.println("Response from server:\n" + response.toString());

        } catch (IOException e) {
            System.err.println("Error connecting to the server: " + e.getMessage());
        }
    }
}

