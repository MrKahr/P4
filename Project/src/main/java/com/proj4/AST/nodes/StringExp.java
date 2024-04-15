package com.proj4.AST.nodes;

public class StringExp extends Expression implements Identifiable{
   //Field
   private StringExpOperator operator;
   private String identifier;
   private String constant;

   //Constructor
   public StringExp(StringExpOperator operator, String identifier, String constant){
       this.operator = operator;
       this.identifier = identifier;
       this.constant = constant;
   }

   //Method
   public StringExpOperator getOperator(){
       return operator;
   }

   public String getIdentifier(){
       return identifier;
   }

   public String getConstant(){
       return constant;
   }    
}
