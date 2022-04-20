package com.encora.framework.context;

import com.encora.framework.annotation.Autowire;
import com.encora.framework.annotation.Component;
import com.encora.framework.annotation.RestController;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public final class ApplicationContext {
    private ApplicationContext(){}

    private final static Map<Class<?>,Object> beans = new HashMap<>();
    private final static Map<String,Class<?>> controllers = new HashMap<>();


    public static void init(String packageName){
        loadControllers(packageName);
    }

    private static void loadBeans() {
    }

    private static void loadControllers(String packageName) {
        //TODO: Create the logic to load controllers
        List<String> packageNames = ClassFinder.getInstance().findAllPackages(packageName);

        for(String item: packageNames){
            Set<Class<?>> list = ClassFinder.getInstance().findAllClassesUsingClassLoader(item);
            Iterator<Class<?>> namesIterator = list.iterator();
            while(namesIterator.hasNext()) {
                Class<?> it = namesIterator.next();
                RestController controller = (RestController) it.getAnnotation(RestController.class);
                if(controller != null){
                    String path = controller.value();
                    controllers.put(path,it);
                }
            }
        }
    }
    public static Object injectDependencies(Object obj){
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Autowire tag = field.getAnnotation(Autowire.class);
            if (tag != null) {
                field.setAccessible(true);
                Class fieldClass = field.getType();
                try {
                    Constructor<?> cons = fieldClass.getConstructor();
                    Object objField = cons.newInstance();
                    field.set(obj, objField);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

    public static <T> T getBean(Class<?> clazz){
        return (T)beans.get(clazz);
    }

    public static Class<?> getController(String path){
        return controllers.get(path);
    }
    public Map<String, Class<?>> getControllers(){
        return Collections.unmodifiableMap(controllers);
    }

}