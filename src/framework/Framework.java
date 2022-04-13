package framework;

import app.controller.AnimalController;
import app.model.Animal;
import framework.server.HttpServerConfig;
import framework.server.ServerConfiguration;
import framework.util.FormatRequestUtil;

import java.lang.reflect.Constructor;
import java.util.List;

public class Framework {
    public static int port =9000;
    public static void run(Class<?> applicationClass,String... args){
        //ServerConfiguration.getInstance().configureClasses(applicationClass);
        String temp = FormatRequestUtil.getPath("/animals");

        //HttpServerConfig httpServer = new HttpServerConfig();
        //httpServer.start(port,applicationClass);
    }
}
