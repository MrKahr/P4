package com.proj4.AST.nodes;

//Expressions can have up to two operands (which are also expressions)
//note that one or both operands may be null if the operator type is CONSTANT or IDENTIFIER
public abstract class Expression extends Statement{

    public Expression getFirstOperand(){
        return (Expression) getChildren().get(0);
    }

    public Expression getSecondOperand(){
        return (Expression) getChildren().get(1);
    }
}
/* TODO: here's a problem:
 * Accessing arrays or template instances is currently impossible.
 * This operation cannot happen at the moment, because there is no node for it,
 * and the existing expressions don't support it.
 * There is, however, some hope:
 * It would take some doing, but creating new operator types to index arrays or templates would work.
 * Say all operator-enumerations get expanded with the type "ARRAY".
 * This would make an expression return the value of the array bound the value in the "identifier-field"
 * indexed by the value returned by the first operand of the expression, which would have to be a MathExp.
 * We could do the same for templates with the operator type "TEMPLATE" which would work the same as ARRAY,
 * except the first operand of the expression would have to be a StringExp to access the needed field.
 * 
 * We could also let ARRAY and TEMPLATE not use the identifier field,
 * and instead let them use the first operand to find the identifier, which would either be an expression or a new class "Identifier",
 * and the second operator to do the indexing as discussed before.
 */