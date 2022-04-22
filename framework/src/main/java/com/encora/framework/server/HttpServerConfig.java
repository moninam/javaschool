package com.encora.framework.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.encora.framework.annotation.App;
import com.encora.framework.exception.ServerErrorException;
import com.sun.net.httpserver.HttpServer;
import com.encora.framework.context.ApplicationContext;


public class HttpServerConfig {
    private HttpServer server;

    public void start(int port,Class<?> clazz){

        try{

            configure(clazz);
            int portServer = port;
            try{
                portServer = Integer.parseInt(ApplicationContext.getFileProperty("port"));
            }catch (Exception e){

            }
            server = HttpServer.create(new InetSocketAddress(portServer),0);
            server.createContext("/", new RestHandler());
            server.setExecutor(null);
            server.start();
        }catch (IOException | ServerErrorException exception){
            exception.printStackTrace();
        }
    }
    public void stop(){
        server.stop(0);
        System.out.println("Server stopped");
    }
    private void configure(Class<?> clazz) throws ServerErrorException {
        if(clazz.isAnnotationPresent(App.class)) {
            App clazzAnnotation = clazz.getAnnotation(App.class);
            String packageName = clazzAnnotation.value();
            ApplicationContext.init(packageName);
        }else{
            stop();
            throw new ServerErrorException("Error al ejecutar el servidor, no hay ninguna clase definida");
        }
    }
}