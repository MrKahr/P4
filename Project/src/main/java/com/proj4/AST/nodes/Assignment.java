package com.proj4.AST.nodes;

//the difference between PrimitiveDecl and Assignment is that with assignments
//we already know the type of the variable pointed to by the identifier
public class Assignment extends Statement implements Identifiable{
    //Field
    private String identifier;
    
    //Constructor
    public Assignment(String identifier){
        this.identifier = identifier;
    }
    
    //Method
    public String getIdentifier(){
        return identifier;
    }

    public Expression getNewValue(){
        return (Expression) getChildren().get(0);
    }

    //TODO: abstract syntax says a templateInstance (templateInit) can also be used in assignments.
    //TODO: templateInstance is a declaration of a variable of some template type, not the template itself.
    //TODO: this should be investigated further, as it must be possible to assign an existing instance of a template to an existing variable
}
