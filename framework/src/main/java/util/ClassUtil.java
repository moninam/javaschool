package main.java.util;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class ClassUtil {
    private static ClassUtil instance;

    private  ClassUtil(){}

    public static ClassUtil getInstance(){
        if(instance == null){
            instance = new ClassUtil();
        }
        return instance;
    }
    public Set findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }
    public List<String> findAllPackages(String packageName){
        InputStream stream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        List<String> temp = reader.lines()
                .map(line -> packageName + "."+ line)
                .filter(line -> !line.endsWith(".class"))
                .collect(toList());

        return temp;
    }


    private Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }
}
