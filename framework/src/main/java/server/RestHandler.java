package main.java.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.annotation.Component;

import java.io.IOException;

@Component
public class RestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()){
            case "GET":
                ServerOperations.getOperation(exchange);
                break;
            case "POST":
                ServerOperations.postOperation(exchange);
                break;
            case "PUT":
                ServerOperations.putOperation(exchange);
                break;
            case "DELETE":
                ServerOperations.deleteOperation(exchange);
                break;
        }
    }
}
