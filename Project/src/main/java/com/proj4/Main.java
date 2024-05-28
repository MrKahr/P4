package com.proj4;


public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{"String sc IS \"fish\" + 1; String sd IS 1 + \"fish\";"};
        }
        DBL interpreter = new DBL();
        interpreter.setShowFinalScope(false);
        interpreter.setVerbosity(false);
        interpreter.setDebugMode(false);
        interpreter.interpret(args[0]);
    }
}