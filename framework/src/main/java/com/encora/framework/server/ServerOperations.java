package com.encora.framework.server;

import com.encora.framework.annotation.*;
import com.encora.framework.context.ApplicationContext;
import com.encora.framework.serializer.JSONSerializer;
import com.sun.net.httpserver.HttpExchange;
import com.encora.framework.serializer.FormatRequestParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.util.*;

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
                Object obj = ApplicationContext.getBean(classItem);
                List<Method> methods = ApplicationContext.getMethodByAnnotation(classItem, GET.class);
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
                        response =  "[\n";
                        for (Object item : list) {
                            response += JSONSerializer.serialize(item) + ",\n";

                        }
                        response += "\n]";
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
        InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);

        int b;
        StringBuilder buf = new StringBuilder();
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }

        br.close();
        isr.close();
        String bodyJson = buf.toString();

        URI requestedUri = he.getRequestURI();
        String path = requestedUri.getPath();
        String root = FormatRequestParser.getPath(path);


        int code = 0;
        String response = "";

        if (root != null) {

            try {

                Class<?> classItem = getClassEndpoint(root);
                Object obj = ApplicationContext.getBean(classItem);
                List<Method> methods = ApplicationContext.getMethodByAnnotation(classItem, POST.class);
                Method method = null;
                if(methods.size() == 1){
                    method = methods.get(0);
                }
                if(method != null){
                    Parameter[] parameters = method.getParameters();
                    Parameter parameterBody = null;
                    for(int i = 0 ; i < parameters.length ; i++){
                        if(parameters[i].isAnnotationPresent(Body.class)){
                            parameterBody = parameters[i];
                            break;
                        }
                    }
                    if(parameterBody != null){
                        Class<?> bodyClass = parameterBody.getType();
                        Object bodyRequest = JSONSerializer.deserialize(bodyClass,bodyJson);
                        method.setAccessible(true);
                        method.invoke(obj,bodyRequest);
                        code = 200;
                        response = JSONSerializer.serialize(bodyRequest);

                    }
                }
            }catch (Exception e){
                code = 500;
                response = "Error interno del servidor";
                e.printStackTrace();
            }
        }
        he.sendResponseHeaders(code, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    public static void putOperation(HttpExchange he) throws IOException {
        InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);

        int b;
        StringBuilder buf = new StringBuilder();
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }

        br.close();
        isr.close();
        String bodyJson = buf.toString();

        URI requestedUri = he.getRequestURI();
        String path = requestedUri.getPath();
        String root = FormatRequestParser.getPath(path);
        int index = FormatRequestParser.parsePath(path);

        int code = 0;
        String response = "";

        if (root != null) {

            try {

                Class<?> classItem = getClassEndpoint(root);
                Object obj = ApplicationContext.getBean(classItem);
                List<Method> methods = ApplicationContext.getMethodByAnnotation(classItem, PUT.class);
                Method method = null;
                String pathParams = "";
                if(methods.size() == 1){
                    method = methods.get(0);
                    PUT annotation = method.getAnnotation(PUT.class);
                    if(annotation != null){
                        pathParams = annotation.value();
                    }
                }
                if(method != null){
                    Parameter[] parameters = method.getParameters();
                    Parameter parameterBody = null;
                    Parameter parameterId = null;
                    for(int i = 0 ; i < parameters.length ; i++){
                        if(parameters[i].isAnnotationPresent(Body.class)){
                            parameterBody = parameters[i];
                        }
                        if(parameters[i].isAnnotationPresent(PathParam.class)){
                            parameterId = parameters[i];
                        }
                    }

                    if(parameterBody != null && parameterId != null){
                        PathParam paramAnnotation = parameterId.getAnnotation(PathParam.class);
                        String paramName = paramAnnotation.value();
                        if(pathParams.contains(paramName) && index != -1){
                            Class<?> bodyClass = parameterBody.getType();
                            Object bodyRequest = JSONSerializer.deserialize(bodyClass,bodyJson);
                            method.setAccessible(true);
                            Object responseBody = method.invoke(obj,bodyRequest,index);
                            if(responseBody != null){
                                code = 200;
                                response = JSONSerializer.serialize(responseBody);
                            }else{
                                code = 404;
                                response = "No se encontro el elemento";
                            }
                        }else{
                            code = 400;
                            response = "Error en el formato de la peticion";
                        }
                    }else{
                        code = 400;
                        response = "Error en el formato de la peticion";
                    }
                }
            }catch (Exception e){
                code = 500;
                response = "Error interno del servidor";
                e.printStackTrace();
            }
        }
        he.sendResponseHeaders(code, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public static void deleteOperation(HttpExchange he) throws IOException {

        InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);

        int b;
        StringBuilder buf = new StringBuilder();
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }

        br.close();
        isr.close();

        URI requestedUri = he.getRequestURI();
        String path = requestedUri.getPath();
        String root = FormatRequestParser.getPath(path);
        int index = FormatRequestParser.parsePath(path);
        String pathParams = "";

        int code = 0;
        String response = "";

        if (root != null) {

            try {
                Class<?> classItem = getClassEndpoint(root);
                Object obj = ApplicationContext.getBean(classItem);
                List<Method> methods = ApplicationContext.getMethodByAnnotation(classItem, DELETE.class);
                Method method = null;
                if(methods.size() == 1){
                    method = methods.get(0);
                    DELETE annotation = method.getAnnotation(DELETE.class);
                    if(annotation != null){
                        pathParams = annotation.value();
                    }
                }
                if(method != null){
                    Parameter[] parameters = method.getParameters();
                    Parameter parameterId = null;
                    for(int i = 0 ; i < parameters.length ; i++){
                        if(parameters[i].isAnnotationPresent(PathParam.class)){
                            parameterId = parameters[i];
                            break;
                        }
                    }
                    if(parameterId != null){
                        PathParam paramAnnotation = parameterId.getAnnotation(PathParam.class);
                        String paramName = paramAnnotation.value();
                        if(pathParams.contains(paramName) && index != -1){
                            method.setAccessible(true);
                            Object responseBody = method.invoke(obj,index);
                            if(responseBody != null){
                                code = 200;
                                response = "El elemento ha sido eliminado con exito";
                            }else{
                                code = 404;
                                response = "No se encontro el elemento";
                            }
                        }else{
                            code = 400;
                            response = "Error en el formato de la peticion";
                        }
                    }else{
                        code = 400;
                        response = "Error en el formato de la peticion";
                    }
                }
            }catch (Exception e){
                code = 500;
                response = "Error interno del servidor";
                e.printStackTrace();
            }
        }
        he.sendResponseHeaders(code, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static Class<?> getClassEndpoint(String endpoint) {
        return ApplicationContext.getController(endpoint);
    }


}