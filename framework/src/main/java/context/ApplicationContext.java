package main.java.context;

import main.java.annotation.Autowire;
import main.java.annotation.Component;
import main.java.annotation.RestController;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public final class ApplicationContext {
    private ApplicationContext(){}

    private final static Map<Class<?>,Object> beans = new HashMap<>();
    private final static Map<String,Class<?>> controllers = new HashMap<>();


    public static void init(String packageName,Class<?> clazz){
        loadControllers(packageName);
        loadBeans(clazz);
    }

    private static void loadBeans(Class<?> clazz) {
        //TODO: Create the logic to load beans
        Package pack = clazz.getPackage();
        String packageName = pack.getName();

        List<String> packageNames = ClassFinder.getInstance().findAllPackages(packageName);

        for(String item: packageNames){
            Set<Class<?>> lista = ClassFinder.getInstance().findAllClassesUsingClassLoader(item);
            Iterator<Class<?>> namesIterator = lista.iterator();
            while(namesIterator.hasNext()) {
                Class<?> it = namesIterator.next();
                Component component = (Component) it.getAnnotation(Component.class);
                if(component != null){
                    try{
                        Constructor<?> cons = it.getConstructor();
                        Object obj= cons.newInstance();
                        beans.put(it,obj);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
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