package ua.knu.csc.iss.http;

import java.io.*;
import java.net.*;

public class SocketHttpServer {

    public static void main(String[] args) throws IOException {
        // Server listens on port 8080
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

        // Hard-coded message
        String message = "Hello world";

        while (true) {
            try {
                // Accept incoming client connections
                Socket clientSocket = serverSocket.accept();

                // Create input and output streams to communicate with the client
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                // Read the HTTP request from the client
                String requestLine;
                StringBuilder requestBuilder = new StringBuilder();
                while ((requestLine = in.readLine()) != null && !requestLine.isEmpty()) {
                    requestBuilder.append(requestLine).append("\r\n");
                }

                // Output the entire HTTP request to the console for debugging purposes
                System.out.println("Received HTTP request:\n" + requestBuilder.toString());

                // Process the request if it's a GET /hello request
                if (requestBuilder.toString().startsWith("GET /hello")) {

                    // Calculate the length of the message
                    int messageLength = message.length();

                    // Handle GET request for /hello
                    String httpResponse = "HTTP/1.1 200 OK\r\n"
                            + "Content-Type: text/plain\r\n"
                            + "Content-Length: " + messageLength + "\r\n"
                            + "\r\n"
                            + message;

                    // Send the HTTP response
                    System.out.println("Sending HTTP response:\n" + httpResponse);
                    out.writeBytes(httpResponse);
                } else {
                    // If the request is not for /hello, send a 404 response
                    String httpResponse = "HTTP/1.1 404 Not Found\r\n"
                            + "Content-Type: text/plain\r\n"
                            + "Content-Length: 9\r\n"
                            + "\r\n"
                            + "Not Found";

                    System.out.println("Sending HTTP response:\n" + httpResponse);
                    out.writeBytes(httpResponse);
                }


                // Close the input and output streams
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error handling client connection: " + e.getMessage());
            }
        }
    }
}
