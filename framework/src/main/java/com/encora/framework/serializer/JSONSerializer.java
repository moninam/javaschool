package com.encora.framework.serializer;

import com.encora.framework.annotation.JsonIgnore;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class JSONSerializer {
    public static <T> String serialize(T src) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> classItem = src.getClass();
        Field[] fields = classItem.getDeclaredFields();
        List<String> values = new ArrayList<>(fields.length);

        for(int i = 0 ; i < fields.length ; i++){
            final Field item = fields[i];
            if(item.isAnnotationPresent(JsonIgnore.class)){
                continue;
            }
            Method fieldGet = classItem.getDeclaredMethod(getMethodName(item,true));
            Object obj = fieldGet.invoke(src);
            String result = obj != null ? obj.toString() : null;
            values.add("\"" + item.getName() + "':'" + result + "\"");
        }
        String json = Arrays.toString(values.toArray());
        return "{" + json.substring(1,json.length() - 1) + "}";
    }
    public static <T> T deserialize(Class<T> target, String json) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Constructor<?> cons = target.getConstructor();
        Object object = cons.newInstance();
        String[] values = json.substring(1,json.length()).split(",\\s");
        for(int i= 0; i< values.length ; i++){
            String valueMap = values[i].replaceAll("\\s+","");
            String fieldName = valueMap.substring(1,valueMap.indexOf(":") -1);
            Object fieldValue = valueMap.substring(valueMap.indexOf(":") +2,valueMap.lastIndexOf("\""));
            final Field field = target.getDeclaredField(fieldName);

            if(field.isAnnotationPresent(JsonIgnore.class)){
                continue;
            }
            Method fieldSet = target.getDeclaredMethod(getMethodName(field,false), field.getType());

            if(field.getType().getSimpleName().toLowerCase(Locale.ROOT).contains("boolean")){
                fieldValue = Boolean.parseBoolean(fieldValue.toString());
            } else if(field.getType().getSimpleName().toLowerCase(Locale.ROOT).contains("int")){
                fieldValue = Integer.parseInt(fieldValue.toString());
            } else if(field.getType().getSimpleName().toLowerCase(Locale.ROOT).contains("double")){
                fieldValue = Double.parseDouble(fieldValue.toString());
            }
            fieldSet.invoke(object,fieldValue);
        }
        return (T) object;
    }
    private static String getMethodName(Field field, boolean isGetter){
        String fieldName = field.getName();
        String camelFieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        String prefix = "";
        if(isGetter){
            prefix = "get";
            if(field.getType().getSimpleName().toLowerCase(Locale.ROOT).contains("boolean")){
                prefix = "is";
            }
        }else{
            prefix = "set";
        }
        return prefix + camelFieldName;
    }

}
