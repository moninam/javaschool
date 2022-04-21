package com.encora.framework.context;


import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

//TODO: Move the class obtention and package to com.encora.framework.context module
public class ClassFinder {
    private static ClassFinder instance;

    private ClassFinder(){}

    public static ClassFinder getInstance(){
        if(instance == null){
            instance = new ClassFinder();
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
    public final Map<String,String> readFile(String packagenName) throws IOException {
        Map<String,String> values = new HashMap<>();

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("application.properties").getFile());
        InputStream inputStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        int b;
        StringBuilder buf = new StringBuilder();
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }

        br.close();

        String bodyJson = buf.toString();
        String[] filelines = bodyJson.split("\n");
        for(int i = 0; i < filelines.length ; i++){
            String[] wordsValues = filelines[i].split("=");
            if(wordsValues.length == 2){
                values.put(wordsValues[0],wordsValues[1]);
            }
        }
        return Collections.unmodifiableMap(values);
    }

    private Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the com.encora.framework.exception
        }
        return null;
    }
}
