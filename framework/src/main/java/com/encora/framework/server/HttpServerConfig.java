package com.encora.framework.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.encora.framework.context.ApplicationContext;


public class HttpServerConfig {
    private HttpServer server;

    public void start(int port,Class<?> clazz){
        //Configure Context
        configure(clazz);
        try{
            server = HttpServer.create(new InetSocketAddress(port),0);
            //TODO: Create the com.encora.framework.context with the Bean loaded previosly
            server.createContext("/", new RestHandler());
            server.setExecutor(null);
            server.start();
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }
    public void stop(){
        server.stop(0);
        System.out.println("Server stopped");
    }
    private void configure(Class<?> clazz){
        //TODO: Load package By Annotation
        //TODO: Load components
        // TODO: Load Contollers
        ServerConfiguration.getInstance().configureClasses(clazz);
    }
}