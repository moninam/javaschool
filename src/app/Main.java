package app;

import framework.Framework;
import framework.annotation.App;

@App("app")
public class Main {
    public static void main(String[] args){
        Framework.run(Main.class);
    }
}
