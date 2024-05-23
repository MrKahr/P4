package com.proj4;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TestScanner {
    // Field 
    private static TestScanner instance;
    private Scanner scanner;
    private Boolean currentlyTesting = false;

    // Constructor 
    TestScanner(){
        scanner = new Scanner(System.in);
    }

    // Method 
    public static TestScanner getInstance() {
        if(instance == null){
            // Default stream is system - TODO: change to any stream
            instance = new TestScanner();
        }
        return instance;
    }

    public void setTestStatus(Boolean testStatus){
        currentlyTesting = testStatus;
    }

    public Scanner getScanner() {
        return scanner;
    }
    
    public Boolean getTestStatus(){
        return currentlyTesting;
    }

    public void provideInput(String input) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(testIn);
    }


}
