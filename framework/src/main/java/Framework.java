package main.java;

import main.java.context.ApplicationContext;
import main.java.server.HttpServerConfig;
import main.java.server.ServerConfiguration;

public class Framework {
    public static int port =9000;
    public static void run(Class<?> applicationClass,String... args){
        ServerConfiguration.getInstance().configureClasses(applicationClass);
        //TODO: Initialize my application context
        //TODO: Validate if the annotation is present and get it if exist
        ApplicationContext.init("");
        HttpServerConfig httpServer = new HttpServerConfig();
        httpServer.start(port,applicationClass);
        // TODO : Allow write the user use own configurations, create and read the properties file
    }
}
