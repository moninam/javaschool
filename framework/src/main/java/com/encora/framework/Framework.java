package com.encora.framework;

import com.encora.framework.annotation.App;
import com.encora.framework.context.ApplicationContext;
import com.encora.framework.server.HttpServerConfig;

public class Framework {
    public static int port =9000;
    public static void run(Class<?> applicationClass,String... args){
        HttpServerConfig httpServer = new HttpServerConfig();
        httpServer.start(port,applicationClass);

    }


}
