package framework.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import app.respository.AnimalRepository;

import java.io.*;

public class Handlers {
    public static AnimalRepository animalRepository = new AnimalRepository();

    public static class RootHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange he) throws IOException {
            String response = "Server start success";
            he.sendResponseHeaders(200,response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public static class RestlHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            switch (exchange.getRequestMethod()){
                case "GET":
                    ServerOperations.handleGet(exchange);
                    break;
                case "POST":
                    ServerOperations.handlePost(exchange);
                    break;
                case "PUT":
                    ServerOperations.handlePut(exchange);
                    break;
                case "DELETE":
                    ServerOperations.handleDelete(exchange);
                    break;
            }
        }
    }


}
