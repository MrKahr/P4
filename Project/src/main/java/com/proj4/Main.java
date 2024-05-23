package com.proj4;


public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{"String sc IS \"fish\" + 1; String sb IS \"John\" + \"Userman\";"};
        }
        DBL interpreter = new DBL();
        interpreter.setVerbosity(true);
        interpreter.setDebugMode(true);
        interpreter.interpret(args[0]);
    }
}