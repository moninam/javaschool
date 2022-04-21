package com.encora.framework.context;

import com.encora.framework.annotation.Autowire;
import com.encora.framework.annotation.RestController;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public final class ApplicationContext {
    private ApplicationContext(){}

    private final static Map<Class<?>,Object> beans = new HashMap<>();
    private final static Map<String,Class<?>> controllers = new HashMap<>();
    private final static Map<String,String> mapValues = new HashMap<>();


    public static void init(String packageName){
        loadControllers(packageName);
        readFile(packageName);
        loadBeans();
    }

    private static void loadBeans() {
        for (Map.Entry<String, Class<?>> set :controllers.entrySet()) {

            try {
                Class<?> classItem = set.getValue();
                Constructor<?> cons =  classItem.getConstructor();
                Object obj = cons.newInstance();
                //Inject dependencies
                obj = ApplicationContext.injectDependencies(obj);
                beans.put(classItem,obj);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadControllers(String packageName) {
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
    public static void readFile(String packagenName){
        try{
            Map<String,String> values = ClassFinder.getInstance().readFile(packagenName);
            mapValues.putAll(values);
        }catch (Exception e){
            e.printStackTrace();
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
    public static List<Method> getMethodByAnnotation(Class<?> clase, Class annotation){
        Method[] allMethods = clase.getDeclaredMethods();
        ArrayList<Method> methods = new ArrayList<>();
        for(Method item: allMethods){
            if(item.isAnnotationPresent(annotation)){
                methods.add(item);
            }
        }
        return methods;
    }
    public static String getFileProperty(String key){
        String value = "";
        for (Map.Entry<String, String> set : mapValues.entrySet()) {
            if(set.getKey().contains(key)){
                value = set.getValue();
            }
        }
        return value;
    }

    public static <T> T getBean(Class<?> clazz){
        return (T)beans.get(clazz);
    }

    public static Class<?> getController(String path){
        return controllers.get(path);
    }


}