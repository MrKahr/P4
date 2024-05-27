package com.proj4;

public abstract class TestingArgs {
    private static final String testPath = "src\\test\\java\\com\\proj4\\testfiles\\";
    public static final Boolean debugMode = true;
    public static final Boolean verbose = false;
    public static final Boolean showFinalScope = false;


    public static String getPath(){
        return testPath;
    }
}