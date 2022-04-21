package com.encora.framework;

import com.encora.framework.context.ClassFinder;
import com.encora.framework.server.HttpServerConfig;

import java.util.HashMap;
import java.util.Map;

public class Framework {
    public static int port =9000;

    public static void run(Class<?> applicationClass,String... args){

        HttpServerConfig httpServer = new HttpServerConfig();
        httpServer.start(port, applicationClass);

    }


}
