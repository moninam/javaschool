package framework.util;

import app.model.Animal;

import java.net.URL;

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
                start+=paths[2];
                return start;
            }
        }
        return null;
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
