package com.encora.framework.server;

import com.encora.framework.context.ApplicationContext;
import com.sun.net.httpserver.HttpExchange;
import com.encora.framework.annotation.GET;
import com.encora.framework.serializer.FormatRequestParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;

//TODO: Separate the logic of this class and the Application Context class
public class ServerOperations {


    public static void getOperation(HttpExchange he) throws IOException {
        int code = 0;
        String response = "";
        URI requestedUri = he.getRequestURI();
        String path = requestedUri.getPath();
        String root = FormatRequestParser.getPath(path);
        int index = FormatRequestParser.parsePath(path);

        if (root != null) {
            try {
                Class<?> classItem = getClassEndpoint(root);
                Constructor<?> cons = classItem.getConstructor();
                Object obj = cons.newInstance();
                //Inject dependencies
                obj = ApplicationContext.injectDependencies(obj);
                ArrayList<Method> methods = ServerConfiguration.getInstance().getMethodByAnnotation(classItem, GET.class);
                Method method = null;

                for (int i = 0; i < methods.size(); i++) {
                    GET getAnnotation = (GET) methods.get(i).getAnnotation(GET.class);
                    if (getAnnotation != null) {
                        String annotationValue = getAnnotation.value();
                        if (index == -1 && annotationValue.equals("")) {
                            method = methods.get(i);
                            break;
                        } else if (index != -1 && !annotationValue.equals("")) {
                            method = methods.get(i);
                            break;
                        }
                    }
                }

                if (method != null) {
                    method.setAccessible(true);
                    if (index != -1) {
                        Object item = method.invoke(obj, index);
                        if (item != null) {
                            code = 200;
                            response = item.toString();
                        } else {
                            code = 404;
                            response = "Elemento no encontrado";
                        }
                    } else {
                        List<Object> list = (List<Object>) method.invoke(obj);
                        code = 200;
                        for (Object item : list) {
                            response += item.toString() + "\n";
                        }
                    }

                }

            } catch (Exception e) {
                code = 500;
                response = "Error interno del servidor";
                e.printStackTrace();
            }

            he.sendResponseHeaders(code, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.getBytes());
            os.close();

        }
    }

    public static void postOperation(HttpExchange he) throws IOException {
        //Read Post Body
        InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);

        int b;
        StringBuilder buf = new StringBuilder();
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }

        br.close();
        isr.close();
        String response = buf.toString();
        //TODO: Parse the request body to the object of the com.encora.app.controller
        //TODO: Get the method for the POST VERB and invoke it
    }

    public static void putOperation(HttpExchange he) throws IOException {
        //TODO: Implement the logic to the put operation
    }

    public static void deleteOperation(HttpExchange he) throws IOException {
        // TODO : Implement the logic to the Delete operation
    }

    private static Class<?> getClassEndpoint(String endpoint) {
        return ApplicationContext.getController(endpoint);
    }
}