package com.proj4.AST.nodes;

public class StringExp extends Expression{
   //Field
   private StringExpOperator operator;
   private String constant;

   //Constructor
   public StringExp(StringExpOperator operator, String constant){
       this.operator = operator;
       this.constant = constant;
   }

   //Method
   public StringExpOperator getOperator(){
       return operator;
   }

   public String getConstant(){
       return constant;
   }    
}
