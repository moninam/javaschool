package main.java.server;

import main.java.annotation.Autowire;
import main.java.annotation.RestController;
import main.java.context.ClassFinder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

//TODO: Move the logic of this part to Application Context
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

        List<String> packageNames = ClassFinder.getInstance().findAllPackages(packageName);

        for(String item: packageNames){
            Set<Class<?>> lista = ClassFinder.getInstance().findAllClassesUsingClassLoader(item);
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

    public ArrayList<Method> getMethodByAnnotation(Class<?> clase, Class annotation){
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