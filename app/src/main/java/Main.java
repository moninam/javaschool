package main.java;

import main.java.annotation.App;

@App("app")
public class Main {
    public static void main(String[] args){
        Framework.run(Main.class);
    }
}
