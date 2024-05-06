package com.proj4.AST.visitors;

import com.proj4.AST.nodes.*;
import com.proj4.AST.visitors.InterpreterVisitors.*;

public class InterpreterDecider implements VisitorDecider {
     //decide which visitor class to use for the given node
     public void decideVisitor(AST node){
        switch (node.getClass().getSimpleName()) {
            case "ActionCall":
                node.acceptVisitor(new ActionCallInterpreter());
                break;
            case "ActionDecl":
                node.acceptVisitor(new ActionDeclInterpreter());
                break;
            case "ArrayInstance":
                node.acceptVisitor(new ArrayInstanceInterpreter());
                break;
            case "Assignment":
                node.acceptVisitor(new AssignmentInterpreter());
                break;
            case "Expression":
                node.acceptVisitor(new ExpressionInterpreter());
                break;
            case "ForLoop":
                node.acceptVisitor(new ForLoopInterpreter());
                break;
            case "IfElse":
                node.acceptVisitor(new IfElseInterpreter());
                break;
            case "Declaration":
                node.acceptVisitor(new DeclarationInterpreter());
                break;
            case "Program":
                node.acceptVisitor(new ProgramInterpreter());
                break;
            case "Return":
                node.acceptVisitor(new ReturnInterpreter());
                break;
            case "RuleDecl":
                node.acceptVisitor(new RuleDeclInterpreter());
                break;
            case "StateDecl":
                node.acceptVisitor(new StateDeclInterpreter());
                break;
            case "TemplateDecl":
                node.acceptVisitor(new TemplateDeclInterpreter());
                break;
            case "TemplateInstance":
                node.acceptVisitor(new TemplateInstanceInterpreter());
                break;
            case "Body":
                node.acceptVisitor(new BodyInterpreter());
                break;
            case "Variable":
                node.acceptVisitor(new VariableInterpreter());
                break;
            default:
                System.err.println("UNRECOGNIZED NODE TYPE!\nGOT " + node.getClass().getSimpleName());
                break;
        }
    }
}
