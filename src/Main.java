import server.HttpServerConfig;

public class Main {
    public static int port =9000;
    public static void main(String[] args){
        HttpServerConfig httpServer = new HttpServerConfig();
        httpServer.start(port);
    }
}
