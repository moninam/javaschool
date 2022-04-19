package main.java.server;

import main.java.annotation.Autowire;
import main.java.annotation.RestController;
import main.java.util.ClassUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ServerConfiguration {
    private static ServerConfiguration instance;


    private final Map<String,Class<?>> clazzes = new HashMap<>();

    public static ServerConfiguration getInstance(){
        if(instance == null){
            instance = new ServerConfiguration();
        }
        return instance;
    }
    private ServerConfiguration(){

    }


    public Map<String, Class<?>> getClazzes(){
        return Collections.unmodifiableMap(clazzes);
    }

    //TODO: Change all Spanish words to english
    public void configureClasses(Class<?> clase){
        Package pack = clase.getPackage();
        String packageName = pack.getName();

        List<String> packageNames = ClassUtil.getInstance().findAllPackages(packageName);

        for(String item: packageNames){
            Set<Class<?>> lista = ClassUtil.getInstance().findAllClassesUsingClassLoader(item);
            Iterator<Class<?>> namesIterator = lista.iterator();
            while(namesIterator.hasNext()) {
                Class<?> it = namesIterator.next();
                RestController controller = (RestController) it.getAnnotation(RestController.class);
                if(controller != null){
                    String path = controller.value();
                    clazzes.put(path,it);
                }
            }
        }

    }

    // TODO: Fix Class Convention
    public Object injectDependencies(Object obj){
        Class clase = obj.getClass();
        Field[] fields = clase.getDeclaredFields();
        for(int i = 0; i < fields.length ; i++){
            Autowire tag = fields[i].getAnnotation(Autowire.class);
            if(tag != null){
                fields[i].setAccessible(true);
                Class fieldClass = fields[i].getType();
                //We need to create a new instance and asign to our class
                try {
                    Constructor<?> cons = fieldClass.getConstructor();
                    Object objField = cons.newInstance();
                    fields[i].set(obj,objField);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

    public ArrayList<Method> getMethodByAnnotation(Class clase, Class annotation){
        Method[] allMethods = clase.getDeclaredMethods();
        ArrayList<Method> methods = new ArrayList<>();
        for(Method item: allMethods){
            if(item.isAnnotationPresent(annotation)){
                methods.add(item);
            }
        }
        return methods;
    }

}