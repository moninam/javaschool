package server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class HttpServerConfig {
    private HttpServer server;

    public void start(int port){
        try{
            server = HttpServer.create(new InetSocketAddress(port),0);
            server.createContext("/",new Handlers.RootHandler());
            server.createContext("/animalsGet",new Handlers.GetHandler());
            server.createContext("/animalsPost",new Handlers.PostHandler());
            server.createContext("/animalsPut",new Handlers.PutHandler());
            server.createContext("/animalsDelete",new Handlers.DeleteHandler());
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
}
