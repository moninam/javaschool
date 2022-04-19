package framework.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import framework.context.ApplicationContext;

public class HttpServerConfig {
    private HttpServer server;

    public void start(int port,Class<?> clazz){
        //Configure Context
        configure(clazz);
        try{
            server = HttpServer.create(new InetSocketAddress(port),0);
            server.createContext("/", ApplicationContext.getBean(HttpHandler.class));
            Map<String,Class<?>> endpoints = ServerConfiguration.getInstance().getClazzes();

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
        ServerConfiguration.getInstance().configureClasses(clazz);
    }
}
