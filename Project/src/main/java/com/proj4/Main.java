package com.proj4;


public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{"Template Card CONTAINS {\r\n" + //
                                "    Integer health;\r\n" + //
                                "    Integer cost;\r\n" + //
                                "    Integer power;\r\n" + //
                                "}\r\n" + //
                                "\r\n" + //
                                "Card[] deck;\r\n" + //
                                "FOR(Integer i IS 0; i LESS OR EQUALS 10; i IS i+1){\r\n" + //
                                "    deck[i] IS NEW Card{i; i+1; i+2;}\r\n" + //
                                "}"};
        }
        DBL interpreter = new DBL();
        interpreter.setVerbosity(true);
        interpreter.setDebugMode(true);
        interpreter.interpret(args[0]);
    }
}