package com.proj4;


public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{"Integer x IS 2-2; Integer Y IS 4/2;"};
        }
        DBL interpreter = new DBL();
        interpreter.interpret(args[0]);
    }
}