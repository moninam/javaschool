package main.java.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

import com.sun.net.httpserver.HttpServer;


public class HttpServerConfig {
    private HttpServer server;

    public void start(int port,Class<?> clazz){
        //Configure Context
        configure(clazz);
        try{
            server = HttpServer.create(new InetSocketAddress(port),0);
            //TODO: Create the context with the Bean loaded previosly
            //server.createContext("/", ApplicationContext.getBean(HttpHandler.class));
            Map<String,Class<?>> endpoints = ServerConfiguration.getInstance().getClazzes();

            //TODO: Remove that part of code because we load the bean defined
            for (Map.Entry<String, Class<?>> set : endpoints.entrySet()) {
                server.createContext(set.getKey(),new Handlers.RestlHandler());
            }
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