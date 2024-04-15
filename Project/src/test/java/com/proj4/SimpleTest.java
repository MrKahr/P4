package com.proj4;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimpleTest {
    //LocalDateTime counts nanoseconds, so to avoid false negatives,
    //we check if a time is within 1 minute of another to compare them
    @Test

    public void SimpleEqualsTest(){
        
        assertTrue(1==2-1);
        

    }    
}