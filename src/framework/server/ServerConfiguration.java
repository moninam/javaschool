package framework.server;

import framework.controller.Autowire;
import framework.controller.RestController;
import framework.util.ClassUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class ServerConfiguration {
    private static ServerConfiguration instance;

    private HashMap<String,Class> clases;

    public static ServerConfiguration getInstance(){
        if(instance == null){
            instance = new ServerConfiguration();
        }
        return instance;
    }
    private ServerConfiguration(){
        clases = new HashMap<>();
    }


    public HashMap<String, Class> getClases(){
        return clases;
    }

    public void configureClasses(Class<?> clase){
        Package pack = clase.getPackage();
        String packageName = pack.getName();

        List<String> packageNames = ClassUtil.getInstance().findAllPackages(packageName);

        for(String item: packageNames){
            Set<Class> lista =ClassUtil.getInstance().findAllClassesUsingClassLoader(item);
            Iterator<Class> namesIterator = lista.iterator();
            while(namesIterator.hasNext()) {
               Class it = namesIterator.next();
               RestController controller = (RestController) it.getAnnotation(RestController.class);
               if(controller != null){
                   String path = controller.value();
                   clases.put(path,it);
               }
            }
        }

    }

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

}
