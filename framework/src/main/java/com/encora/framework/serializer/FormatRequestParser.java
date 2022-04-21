package com.encora.framework.serializer;

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

}