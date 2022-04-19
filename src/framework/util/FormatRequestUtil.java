package framework.util;

import app.model.Animal;
import framework.annotation.JsonIgnore;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FormatRequestUtil {
    public static int parsePath(String path){
        if(path != null){
            String paths[] = path.split("[/]");
            if(paths.length > 0){
                String route = paths[paths.length -1];
                try{
                    int index = Integer.parseInt(route);
                    return index;
                }catch (Exception e){
                }

            }
        }
        return -1;
    }
    public static String getPath(String path){
        String start = "/";
        if(path != null){
            String paths[] = path.split("[/]");
            if(paths.length > 1){
                start+=paths[1];
                return start;
            }
        }
        return null;
    }
    //TODO: Make The JSON Serializer
    public static Object parseRequest(String request,Class objectClass){

        if(request != null){
            String[] body = request.split("[,]");
            List<Field> fields = getFields(objectClass);

            if(body.length == fields.size()){

                for(int i = 0; i < fields.size(); i++){

                    fields.get(i).setAccessible(true);
                    Class fieldClass = fields.get(i).getType();
                    framework.annotation.Field fieldAnnotation = fields.get(i).getAnnotation(framework.annotation.Field.class);
                    String[] requestInfo = body[i].split("[:]");

                    if(requestInfo.length > 1){
                        String name = fieldAnnotation.name();
                        String tag = requestInfo[0];

                        if(name.equals(tag)){
                            try {
                                Constructor<?> cons = fieldClass.getConstructor();
                                Object fieldObj = cons.newInstance();

                                Method[] metodos = objectClass.getMethods();

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return objectClass;
    }
    private static List<Field> getFields(Class objectClass){
        Field[] fields = objectClass.getDeclaredFields();
        List<Field> fieldsList = new ArrayList<>();
        for(int i = 0 ; i< fields.length; i++){
            JsonIgnore tag = fields[i].getAnnotation(JsonIgnore.class);
            if(tag == null){
                fieldsList.add(fields[i]);
            }
        }
        return fieldsList;
    }
    public static Animal parseBody(String query){
        Animal animal = null;
        if(query != null) {
            String[] body = query.split("[,]");
            if(body.length == 2){
                String[] nombre = body[0].split("[:]");
                String[] raza = body[1].split("[:]");
                if(nombre.length == 2 && raza.length == 2){
                    String name = nombre[1];
                    String breed = raza[1];
                    animal = new Animal(name,breed);
                }
            }
        }
        return animal;
    }
}
