package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Animal;
import respository.AnimalRepository;
import util.FormatRequestUtil;

import java.io.*;
import java.net.URI;
import java.util.*;

public class Handlers {
    public static AnimalRepository animalRepository = new AnimalRepository();

    public static class RootHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange he) throws IOException {
            String response = "Server start success";
            he.sendResponseHeaders(200,response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public static  class GetHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException{
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
    }
    public static  class PostHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException {
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
    }
    public static  class PutHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange he) throws IOException{
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
    }
    public static class DeleteHandler implements  HttpHandler{
        @Override
        public void handle(HttpExchange he) throws IOException{
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
        }
    }

}
