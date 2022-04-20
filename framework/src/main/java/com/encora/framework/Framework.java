package com.encora.framework;

import com.encora.framework.annotation.App;
import com.encora.framework.context.ApplicationContext;
import com.encora.framework.server.HttpServerConfig;

public class Framework {
    public static int port =9000;
    public static void run(Class<?> applicationClass,String... args){

        //TODO: Initialize my application com.encora.framework.context
        //TODO: Validate if the com.encora.framework.annotation is present and get it if exist
        if(applicationClass.isAnnotationPresent(App.class)){
            App clazzAnnotation = applicationClass.getAnnotation(App.class);
            String packageName = clazzAnnotation.value();
               ApplicationContext.init(packageName,Framework.class);
            HttpServerConfig httpServer = new HttpServerConfig();
            httpServer.start(port,applicationClass);
        }

    }


}
