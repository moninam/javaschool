package main.java;

import main.java.annotation.App;
import main.java.context.ApplicationContext;
import main.java.server.HttpServerConfig;
import main.java.server.ServerConfiguration;

public class Framework {
    public static int port =9000;
    public static void run(Class<?> applicationClass,String... args){

        //TODO: Initialize my application context
        //TODO: Validate if the annotation is present and get it if exist
        if(applicationClass.isAnnotationPresent(App.class)){
            ApplicationContext.init("",Framework.class);
            HttpServerConfig httpServer = new HttpServerConfig();
            httpServer.start(port,applicationClass);
        }

    }


}
