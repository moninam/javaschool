package com.encora.app;

import com.encora.framework.Framework;
import com.encora.framework.annotation.App;

@App("com.encora.app")
public class Main {
    public static void main(String[] args){
        Framework.run(Main.class);
    }
}
