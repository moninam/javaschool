package main.java.context;

import java.util.HashMap;
import java.util.Map;

public final class ApplicationContext {
    private ApplicationContext(){}
    private final static Map<Class<?>,Object> beans = new HashMap<>();


    public static void init(String packageName){
        loadControllers();
        loadBeans();
    }

    private static void loadBeans() {
        //TODO: Create the logic to load beans
    }

    private static void loadControllers() {
        //TODO: Create the logic to load controllers
    }

    public static <T> T getBean(Class<?> clazz){
        return (T)beans.get(clazz);
    }

}