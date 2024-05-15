package com.proj4;


public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{"Boolean x IS true AND false;"};
        }
        DBL interpreter = new DBL(true);
        interpreter.interpret(args[0]);
    }
}