package com.encora.framework.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.*;

//TODO: Move the logic of this class to our file REST Handler
public class Handlers {


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


}