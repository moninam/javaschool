package com.encora.framework.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

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
