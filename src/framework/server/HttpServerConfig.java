package framework.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpServer;

public class HttpServerConfig {
    private HttpServer server;

    public void start(int port,Class<?> clase){
        //Configure Context
        configure(clase);
        try{
            server = HttpServer.create(new InetSocketAddress(port),0);
            server.createContext("/",new Handlers.RootHandler());
            HashMap<String,Class> endpoints = ServerConfiguration.getInstance().getClases();

            for (Map.Entry<String, Class> set : endpoints.entrySet()) {
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
    private void configure(Class<?> clase){
        ServerConfiguration.getInstance().configureClasses(clase);
    }
}
