package ua.knu.csc.iss.http;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.util.Map;

public class BuiltinHttpServer {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        // Create an HTTP server that listens on port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Define the context to listen for requests to "/hello"
        server.createContext("/hello", new HelloHandler());

        // Start the server
        System.out.println("Server is listening on port "+port+"...");
        server.start();
    }

    // This handler will handle requests to the /hello endpoint
    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Log the HTTP request details
            System.out.println("Received HTTP request:");
            System.out.println("Request Method: " + exchange.getRequestMethod());
            System.out.println("Request URI: " + exchange.getRequestURI());

            // Log request headers
            Map<String, java.util.List<String>> requestHeaders = exchange.getRequestHeaders();
            System.out.println("Request Headers:");
            for (Map.Entry<String, java.util.List<String>> header : requestHeaders.entrySet()) {
                System.out.println(header.getKey() + ": " + String.join(", ", header.getValue()));
            }

            // Log request body (if any)
            InputStream requestBodyStream = exchange.getRequestBody();
            String requestBody = new String(requestBodyStream.readAllBytes());
            if (!requestBody.isEmpty()) {
                System.out.println("Request Body:");
                System.out.println(requestBody);
            }
            System.out.println();

            // Create the response message
            String response = "Hello World";

            // Capture the response headers
            exchange.getResponseHeaders().set("Content-Type", "text/plain");

            // Send the response headers with 200 status and content length
            exchange.sendResponseHeaders(200, response.getBytes().length);

            // Log the response headers
            Map<String, java.util.List<String>> responseHeaders = exchange.getResponseHeaders();
            System.out.println("Response Headers:");
            for (Map.Entry<String, java.util.List<String>> header : responseHeaders.entrySet()) {
                System.out.println(header.getKey() + ": " + String.join(", ", header.getValue()));
            }

            // Capture the response body using a ByteArrayOutputStream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (OutputStream os = byteArrayOutputStream) {
                os.write(response.getBytes());
            }

            // Log the response body
            System.out.println("Response Body:");
            System.out.println(byteArrayOutputStream.toString());

            // Write the response to the output stream
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(byteArrayOutputStream.toByteArray());
            }
        }
    }
}
