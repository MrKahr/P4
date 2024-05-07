package com.proj4.AST.visitors;

import com.proj4.AST.nodes.*;
import com.proj4.AST.visitors.CheckVisitors.*;

public class CheckDecider implements VisitorDecider {
     //decide which visitor class to use for the given node
     public void decideVisitor(AST node){
        System.out.println("Type checking " + node.getClass().getSimpleName() + ".");
        switch (node.getClass().getSimpleName()) {
            case "ActionCall":
                node.acceptVisitor(new ActionCallTypeChecker());
                break;
            case "ActionDecl":
                node.acceptVisitor(new ActionDeclTypeChecker());
                break;
            case "ArrayInstance":
                node.acceptVisitor(new ArrayInstanceTypeChecker());
                break;
            case "Assignment":
                node.acceptVisitor(new AssignmentTypeChecker());
                break;
            case "Expression":
                node.acceptVisitor(new ExpressionTypeChecker());
                break;
            case "ForLoop":
                node.acceptVisitor(new ForLoopTypeChecker());
                break;
            case "IfElse":
                node.acceptVisitor(new IfElseTypeChecker());
                break;
            case "Declaration":
                node.acceptVisitor(new DeclarationTypeChecker());
                break;
            case "Program":
                node.acceptVisitor(new ProgramTypeChecker());
                break;
            case "Return":
                node.acceptVisitor(new ReturnTypeChecker());
                break;
            case "RuleDecl":
                node.acceptVisitor(new RuleDeclTypeChecker());
                break;
            case "StateDecl":
                node.acceptVisitor(new StateDeclTypeChecker());
                break;
            case "TemplateDecl":
                node.acceptVisitor(new TemplateDeclTypeChecker());
                break;
            case "TemplateInstance":
                node.acceptVisitor(new TemplateInstanceTypeChecker());
                break;
            case "Body":
                node.acceptVisitor(new BodyTypeChecker());
                break;
            case "Variable":
                node.acceptVisitor(new VariableTypeChecker());
                break;
            default:
                System.err.println("UNRECOGNIZED NODE TYPE!\nGOT " + node.getClass().getSimpleName());
                break;
        }
    }
}
