package framework.server;

import com.sun.net.httpserver.HttpExchange;
import app.model.Animal;
import app.respository.AnimalRepository;
import framework.annotation.GET;
import framework.util.FormatRequestUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;

public class ServerOperations {
    public static AnimalRepository animalRepository = new AnimalRepository();


    public static void getOperation(HttpExchange he) throws IOException{
        int code = 0;
        String response = "";
        URI requestedUri = he.getRequestURI();
        String path = requestedUri.getPath();
        String root = FormatRequestUtil.getPath(path);
        int index = FormatRequestUtil.parsePath(path);

        if(root != null){
            try{
                Class classItem = getClassEndpoint(root);
                Constructor<?> cons = classItem.getConstructor();
                Object obj = cons.newInstance();
                //Inject dependencies
                obj = ServerConfiguration.getInstance().injectDependencies(obj);
                ArrayList<Method> methods = ServerConfiguration.getInstance().getMethodByAnnotation(classItem, GET.class);
                Method method = null;

                for(int i = 0; i< methods.size(); i++){
                    GET getAnnotation = (GET) methods.get(i).getAnnotation(GET.class);
                    if(getAnnotation != null){
                        String annotationValue = getAnnotation.value();
                        if(index == -1 && annotationValue.equals("")){
                            method = methods.get(i);
                            break;
                        }else if(index != -1 && !annotationValue.equals("")){
                            method = methods.get(i);
                            break;
                        }
                    }
                }

                if(method != null){
                    method.setAccessible(true);
                    if(index != -1){
                        Object item = method.invoke(obj,index);
                        if(item != null){
                            code = 200;
                            response = item.toString();
                        }else{
                            code = 404;
                            response = "Elemento no encontrado";
                        }
                    }else{
                        List<Object> list = (List<Object>) method.invoke(obj);
                        code = 200;
                        for(Object item: list){
                            response += item.toString() + "\n";
                        }
                    }

                }

            }catch (Exception e){
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
    public static void postOperation(HttpExchange he) throws IOException{
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
        Object obj = FormatRequestUtil.parseRequest(response,Animal.class);
    }
    public static void putOperation(HttpExchange he) throws  IOException{

    }
    public static void deleteOperation(HttpExchange he) throws  IOException{

    }

    private static Class<?> getClassEndpoint(String endpoint){
        Map<String,Class<?>> map = ServerConfiguration.getInstance().getClazzes();
        if(map != null){
            for (Map.Entry<String, Class<?>> set : map.entrySet()) {
                if(set.getKey().equals(endpoint)){
                    return set.getValue();
                }
            }
        }

        return null;
    }

    /*
    public static void handleGet(HttpExchange he) throws IOException {
        URI requestedUri = he.getRequestURI();

        String path = requestedUri.getPath();
        String response = "";
        int code = 0;

        int index = FormatRequestUtil.parsePath(path);
        if(index != -1){
            Animal animal = animalRepository.getAnimal(index);
            if(animal != null){
                response = animal.getName() + " -" + animal.getBreed();
                code = 200;
            }else{
                response = "Animal no encontrado";
                code = 404;
            }
        }else{
            ArrayList<Animal> list = animalRepository.getAnimals();
            code = 200;
            for(Animal item: list){
                response += item.getName() + " -" + item.getBreed() + "\n";
            }
        }


        he.sendResponseHeaders(code, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    public static void handlePost(HttpExchange he) throws IOException {
        System.out.println("POST received successfully");
        int code = 0;
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
        Animal animal = FormatRequestUtil.parseBody(response);
        if(animal != null){
            code = 200;
            animalRepository.addAnimal(animal);
            response = animal.toString();
        }else{
            code = 500;
            response = "Error al agregar el animal";
        }
        //Send post body
        he.sendResponseHeaders(code, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
    public static void handlePut(HttpExchange he) throws IOException{
        URI requestedUri = he.getRequestURI();

        String path = requestedUri.getPath();
        String response = "";
        int code = 0;

        int index = FormatRequestUtil.parsePath(path);
        if(index != -1){
            Animal animal = animalRepository.getAnimal(index);
            if(animal != null){
                //Detectar cuerpo
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
                String rsp = buf.toString();
                Animal animalBody = FormatRequestUtil.parseBody(rsp);
                if(animalBody != null){
                    Animal edicion = animalRepository.editAnimal(animalBody,index);
                    if(edicion != null){
                        code = 201;
                        response += "Animal editado " + "\n" + animalBody.toString();
                    }else{
                        response = "Animal no encontrado";
                        code = 404;
                    }

                }else{
                    code = 400;
                    response = "Error en el formato de la peticion";
                }
            }else{
                response = "Animal no encontrado";
                code = 404;
            }
        }else{
            code = 400;
            response = "Formato no adecuado, favor de intentarlo de nuevo";
        }


        he.sendResponseHeaders(code, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    public static void handleDelete(HttpExchange he) throws IOException{
        URI requestedUri = he.getRequestURI();

        String path = requestedUri.getPath();
        String response = "";
        int code = 0;

        int index = FormatRequestUtil.parsePath(path);
        if(index != -1){
            Animal animal = animalRepository.removeAnimal(index);
            if(animal != null){
                code = 200;
                response = "Animal eliminado con exito";
            }else{
                code = 404;
                response = "Animal no encontrado";
            }
        }
        he.sendResponseHeaders(code, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }*/


}
