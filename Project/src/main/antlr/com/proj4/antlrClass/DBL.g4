/*
*** DOCSTRING - PLEASE READ ***
This docstring describes important assumptions about our current grammar and describes some helpful parts of ANTLR that you need to know to develop on this grammar.


*** OUR GRAMMAR ***
1) We disallow program statements that are not given in a set order
2) We do not eliminate direct left recursion


*** IMPORTANT ANTLR-FEATURES ***
ANTLR defines a grammar with the grammar keyword and an identifier.
This is the name of the file that you can use in the [Run ANTLR.bat]-file.grammar
You can optionally add a [SOMENAME.txt] file as a test file that you can try the lexer and parser on.

ANTLR grammars use Extended Backus-Naur form (EBNF) to define its grammar; it therefore closely resembles a CFG (p. 264-265).
ANTLR handles left recursion and operator precedence by applying the rule that is defined first (ANTLR book, p. 71).
This means that rules defined first are applied first. This solves problems such as: "Integer Integer" (ANTLR Book, p. 74).
However, we still have to eliminate left recursion in our CFG.

In each rule you define, you can use regular operators such as (*,+,?).
For more information, refer to the ANTLR reference (ANTLR book p. 265 onwards)

When you hover over a rule, you can see the definition of its derivation.
This is nice to know if you want to debug a rule, and want to trace the derivations by hand.


*** IMPORTANT NOTATION ***
Uppercase = Lexer rule (ANTLR book, p. 80)
Lowercase = Parser rule (ANTLR book p. 80)
*/

// TODO: Check om vi skal have statement/declarations/ruleDeclartions etc. i en bestemt rækkefølge.
// TODO: Fjern left-recursion til CFG i den endelige rapport
grammar DBL;
////////////
// PARSER //
////////////
program
    :   templateDecl* actionDecl* ruleDecl* stateDecl* stmtList EOF
    ;

stmt
    :   ifBlock
    |   forLoop
    |   assignment SEMICOLON
    |   declaration
    |   actionCall SEMICOLON
    ;

stmtList
    :   stmt*
    ;

expr
    :   BRAC_START expr BRAC_END   # parExpr
    |   expr DOT IDENTIFIER        # templateAccessExpr
    |   expr SQB_START expr SQB_END# arrayAccessExpr
    |   expr multOp expr           # multExpr
    |   expr addOp expr            # addExpr
    |   templateInit               #templateInitExpr
    |   arrayInit                  # arrayInitExpr
    |   actionResult               # actionResultExpr
    |   actionCall                 # actionCallExpr     // Ensure action calls are the last of parser definitions
    |   DIGIT                      # digitExpr
    |   IDENTIFIER                 # idExpr
    ;

boolExpr
    :   BRAC_START boolExpr BRAC_END        # parBool
    |   expr GT expr                        # exprGTBool
    |   expr GTOE expr                      # exprGTOEBool
    |   expr LT expr                        # exprLTBool
    |   expr LTOE expr                      # exprLTOEBool
    |   expr (NOTEQUALS | EQUALS) expr      # exprEqualBool // TODO: Merge this with stringexpr EQUALS below - fix in parsetreevisitor
    |   stringExpr (NOTEQUALS | EQUALS) stringExpr  # stringEqualBool
    |   NOT boolExpr                        # negateBool
    |   boolExpr EQUALS boolExpr            # equalBool
    |   boolExpr AND boolExpr               # andBool
    |   boolExpr OR  boolExpr               # orBool
    |   actionCall                          # actionCallBool
    |   BOOLEAN                             # litteralBool
    |   IDENTIFIER                          # idBool
    ;

stringExpr
    :   stringExpr ADD stringExpr  # addString
    |   stringExpr ADD expr        #addStringexpr1
    |   expr ADD stringExpr        #addStringexpr2
    |   string                     # litteralString
    |   actionCall                 # actionCallString
    |   IDENTIFIER                 # idString
    ;

assignment // Remember to add semicolon if relevant
    :   expr ASSIGN expr          # exprAssign
    |   expr ASSIGN boolExpr      # boolExprAssign
    |   expr ASSIGN stringExpr    # stringExprAssign
    ;

typePrimitive
    :   TYPEDEF_PRIMITIVE
    ;

typedefUser
    :   IDENTIFIER
    ;

declaration
    :   typePrimitive IDENTIFIER SEMICOLON  # idDeclPrim
    |   typePrimitive assignment SEMICOLON  # assignDeclPrim
    |   typedefUser IDENTIFIER SEMICOLON    # idDeclUser
    |   typedefUser assignment SEMICOLON    # assignIdUserDecl
    |   arrayType IDENTIFIER SEMICOLON      # declArrayDecl
    |   arrayType assignment SEMICOLON      # declArrayInit
    ;

declarationList
    :   declaration+
    ;

body
    :   stmtList
    ;

forLoop
    :   FOR BRAC_START declaration boolExpr SEMICOLON assignment BRAC_END BODY_START body BODY_END  # forI
    ;

ifBlock
    :   IF BRAC_START boolExpr BRAC_END BODY_START body BODY_END (ELSEIF BRAC_START boolExpr BRAC_END BODY_START body BODY_END)* (ELSE BODY_START body BODY_END)?
    ;

templateDecl
    :   TEMPLATE typedefUser CONTAINS BODY_START declarationList BODY_END
    ;

templateInit
    :   NEW typedefUser BODY_START ((expr SEMICOLON | stringExpr SEMICOLON | boolExpr SEMICOLON | templateInit))* BODY_END
    ;

ruleDecl
    :   RULE typedefUser WHEN SQB_START identifierList SQB_END ifBlock
    ;

actionDecl
    :   ACTION typedefUser BRAC_START parameterList BRAC_END resultsIn BODY_START body return BODY_END   # returnActionDecl
    |   ACTION typedefUser BRAC_START parameterList BRAC_END BODY_START body BODY_END             # noReturnActionDecl       // Without RESULTS IN, return is not needed. Meaning this action does not return anything
    ;

actionCall
    :   typedefUser BRAC_START argumentList BRAC_END
    ;

actionResult
    :   typedefUser DOT RESULT (DOT IDENTIFIER)*
    ;

stateDecl
    :   STATE typedefUser ALLOWS SQB_START identifierList SQB_END WITH_LOOP BODY_START body BODY_END    # idActionStateLoop
    |   STATE typedefUser ALLOWS SQB_START identifierList SQB_END                                       # idActionState
    ;

parameterList
    :   ((typedefUser | typePrimitive | STATE | arrayType) IDENTIFIER (COMMA (typedefUser | typePrimitive | STATE | arrayType) IDENTIFIER)*)?
    ;

argumentList
    :   ((expr | boolExpr | stringExpr) (COMMA (expr | boolExpr | stringExpr))*)?
    ;


arrayType
    :   (typedefUser | typePrimitive) (SQB_START SQB_END)+
    ;

arrayInit
    :   SQB_START ((stringExpr | expr | boolExpr | arrayInit) (COMMA (stringExpr | expr | boolExpr | arrayInit))*)? SQB_END
    ;

return
    :   RESULT_IN (expr | boolExpr | stringExpr) SEMICOLON
    ;

resultsIn
    :   RESULTS_IN (typedefUser | typePrimitive | arrayType)
    ;

multOp
    :   MULT
    |   DIV
    ;

addOp
    :   ADD
    |   SUB
    ;

string
    :   STRING
    ;

identifierList
    :   (IDENTIFIER (COMMA IDENTIFIER)*)?
    ;

///////////
// LEXER //
///////////
/*** Non-tokens ***/
LINE_COMMENT : '//' .*? '\r'? '\n' -> skip ; // ANTLR book p. 77
COMMENT : '/*' .*? '*/' -> skip ;            // ANTLR book p. 77
WHITESPACE: [ \t]+ -> skip ;                 // ANTLR book p. 79 - note that newline is not included
NEWLINE: [\r\n] -> skip;                     // This may allow newlines to be used as termination chars instead of semicolon in the parser

/*** Keywords ***/
IF          : 'IF';
ELSEIF      : 'ELSE IF';
ELSE        : 'ELSE';
WHEN        : 'WHEN';
ACTION      : 'Action';
STATE       : 'State';
RULE        : 'Rule';
FOR         : 'FOR';
RESULT      : 'RESULT';     // Get result of a specific action call. Used in Rule
RESULT_IN   : 'RESULT IN';  // Return from action
RESULTS_IN  : 'RESULTS IN'; // Declare return type for action
TEMPLATE    : 'Template';
ALLOWS      : 'ALLOWS';
WITH_LOOP   : 'WITH LOOP';
NEW         : 'NEW';

/*** Operators ***/
ASSIGN      : 'IS';
CONTAINS    : 'CONTAINS';
GT          : 'GREATER THAN';
GTOE        : 'GREATER OR EQUALS';
LT          : 'LESS THAN';
LTOE        : 'LESS OR EQUALS';
EQUALS      : 'EQUALS';
NOTEQUALS   : 'NOT EQUALS';
OR          : 'OR';
AND         : 'AND';
NOT         : 'NOT';
MULT        : '*';
DIV         : '/';
ADD         : '+';
SUB         : '-';
DOT         : '.';

/***  Types ***/
TYPEDEF_PRIMITIVE
    :   'Integer'
    |   'Boolean'
    |   'String'
    ;

STRING
    :   '"'.*?'"'
    ;

BOOLEAN
    : 'true'
    | 'false'
    ;

BODY_START
    :   '{'
    ;

BODY_END
    :   '}'
    ;

SQB_START
    :   '['
    ;

SQB_END
    :   ']'
    ;

BRAC_START
    :   '('
    ;

BRAC_END
    :   ')'
    ;

COMMA
    :   ','
    ;

DIGIT
    : [0-9]+
    ;

SEMICOLON
    :   ';'
    ;

IDENTIFIER
    : [A-Za-z]+[0-9A-Za-z]*
    ;