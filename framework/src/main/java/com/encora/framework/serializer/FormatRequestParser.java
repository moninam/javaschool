package com.encora.framework.serializer;

import com.encora.framework.annotation.Column;
import com.encora.framework.annotation.JsonIgnore;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//TODO: Create the com.encora.framework.serializer class for JSON inputs
public class FormatRequestParser {
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
                    Column fieldAnnotation= fields.get(i).getAnnotation(Column.class);
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

}