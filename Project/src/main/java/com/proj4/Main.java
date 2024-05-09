package com.proj4;


public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{"Integer x IS 1; Integer y IS 2; Boolean z IS x EQUALS y;"};
        }
        DBL interpreter = new DBL();
        interpreter.interpret(args[0]);
    }
}