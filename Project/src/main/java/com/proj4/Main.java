package com.proj4;


public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{"String sa IS \"fish\"; String sb IS \"John\" + \"Userman\";"};
        }
        DBL interpreter = new DBL();
        interpreter.interpret(args[0]);
    }
}