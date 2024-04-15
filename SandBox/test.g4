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

grammar test;

// TODO: Consider whether statements should be ended by '\n' <--- They will not, as that's too much of a hassle
// TODO: Check om vi skal have statement/declarations/ruleDeclartions etc. i en bestemt rækkefølge.
// TODO: Fjern left-recursion til CFG i den endelige rapport
// TODO: Check wether we allow e.g.: Action ReadAction() RESULTS IN String

////////////
// PARSER //
////////////
program
    :   (templateDecl)* (actionDecl)* (ruleDecl)* (stateDecl)* stmtList EOF
    ;

stmt
    :   ifBlock
    |   forLoop
    |   declaration
    |   assignment SEMICOLON
    |   actionCall
    |   templateInit
    ;

stmtList
    :   (stmt)*
    ;

// This is very much left-recursive - FIND WAY TO REMOVE IN CFG!
expr
    :   BRAC_START expr BRAC_END   # parExpr
    |   expr multOp expr           # multExpr
    |   expr addOp expr            # addExpr
    |   templateAccess             # templateAccessExpr
    |   actionResult               # actionResultExpr
    |   actionCall                 # actionCallExpr     // Ensure action calls are the last of parser definitions
    |   DIGIT                      # digitExpr
    |   IDENTIFIER                 # idExpr
    ;

// This is very much left-recursive - FIND WAY TO REMOVE IN CFG!
boolExpr
    :   BRAC_START boolExpr BRAC_END        # parBool
    |   expr GT expr                        # GTBool
    |   expr GTOE expr                      # GTOEBool
    |   expr LT expr                        # LTBool
    |   expr LTOE expr                      # LTOEBool
    |   expr (NOTEQUALS | EQUALS) expr      # equalBool               
    |   stringExpr (NOTEQUALS | EQUALS) stringExpr  # equalStringBool
    |   NOT boolExpr                        # negateBool
    |   boolExpr AND boolExpr               # andBool
    |   boolExpr OR  boolExpr               # orBool
    |   BOOLEAN                             # litteralBool
    |   IDENTIFIER                          # idBool
    ;

stringExpr
    :   (string | expr) (ADD (string | expr))+  # addString      // Doing it this way means implicit type conversion from int to string! Also, ensure types are correct! (int, str)
    |   string                     # litteralString
    |   IDENTIFIER                 # idString
    ;

assignment // Remember to add semicolon if relevant
    :   (templateAccess | IDENTIFIER) ASSIGN expr             # exprAssign
    |   (templateAccess | IDENTIFIER) ASSIGN boolExpr         # boolExprAssign
    |   (templateAccess | IDENTIFIER) ASSIGN stringExpr       # stringExprAssign
    |   (templateAccess | IDENTIFIER) ASSIGN arrayAccess      # arrayAccessAssign
    |   (templateAccess | IDENTIFIER) ASSIGN arrayInit        # arrayInitAssign
    |   (templateAccess | IDENTIFIER) ASSIGN templateAccess   # templateAccessAssign
    |   (templateAccess | IDENTIFIER) ASSIGN actionCall       # actionCallAssign
    |   (templateAccess | IDENTIFIER) ASSIGN IDENTIFIER       # idAssign
    ;

typePrimitive
    :   TYPEDEF_PRIMITIVE
    ;

typedefUser
    :   IDENTIFIER
    ;

declaration
    :   typePrimitive IDENTIFIER SEMICOLON     # idDeclPrim
    |   typePrimitive assignment SEMICOLON     # assignDeclPrim
    |   typedefUser IDENTIFIER SEMICOLON       # idDeclUser
    |   typedefUser assignment SEMICOLON       # assignDeclUser
    |   arrayDecl SEMICOLON                    # declarrayDecl
    ;

declarationList
    :   (declaration)*
    ;

body
    :   stmtList
    ;

forLoop
    :   FOR BRAC_START typedefUser IDENTIFIER OF (templateAccess | actionCall | IDENTIFIER) BRAC_END BODY_START body BODY_END    # forOF
    |   FOR BRAC_START declaration boolExpr SEMICOLON (expr | assignment) BRAC_END BODY_START body BODY_END                      # forI
    ;

ifBlock
    :   IF BRAC_START boolExpr BRAC_END BODY_START body BODY_END (ELSE IF BRAC_START boolExpr BRAC_END BODY_START body BODY_END)* (ELSE BODY_START body BODY_END)?
    ;

templateDecl
    :   TEMPLATE typedefUser CONTAINS BODY_START declarationList BODY_END
    ;

templateInit
    :   NEW typedefUser IDENTIFIER BODY_START (templateInit | (assignment SEMICOLON))* BODY_END
    ;

templateAccess
    :   typedefUser (DOT IDENTIFIER)+
    ;

ruleDecl
    :   RULE typedefUser WHEN SQB_START identifierList SQB_END ifBlock
    ;

actionDecl
    :   ACTION typedefUser BRAC_START parameterList BRAC_END 
        (RESULTS_IN (typedefUser | typePrimitive | STATE) (BODY_START body return BODY_END)?)?    # returnActionDecl // Right now return is optional. Should it be like that?
    |   ACTION typedefUser BRAC_START parameterList BRAC_END BODY_START body BODY_END             # noReturnActionDecl       // Without RESULTS IN, return is not needed. Meaning this action does not return anything
    ;

actionCall
    :   typedefUser BRAC_START argumentList BRAC_END SEMICOLON
    ;

actionResult   // NOTICE: currently action_result is an expr, which means it's allowed almost everywhere! The only valid use of it is in the "IF()" of an if_block in Rule. Semantic Analysis should handle this
    :   typedefUser DOT RESULT (DOT IDENTIFIER)*
    ;

stateDecl
    :   STATE typedefUser ALLOWS SQB_START identifierList SQB_END WITH_LOOP SQB_START actionCall+ SQB_END    # idActionState
    |   STATE typedefUser ALLOWS SQB_START identifierList SQB_END                                            # idState
    |   STATE typedefUser                                                                                    # idStateDecl
    ;

parameterList
    :   ((typedefUser | typePrimitive | STATE) IDENTIFIER (COMMA (typedefUser | typePrimitive | STATE) IDENTIFIER)*)?
    ;

argumentList
    :   ((expr | boolExpr | stringExpr) (COMMA (expr | boolExpr | stringExpr))*)?
    ;

arrayDecl
    :   (typedefUser | typePrimitive) SQB_START SQB_END IDENTIFIER (ASSIGN arrayInit)?
    ;

arrayInit
    :   SQB_START ((stringExpr | expr) (COMMA (stringExpr | expr))*)? SQB_END
    ;

arrayAccess
    :   (templateAccess | IDENTIFIER) SQB_START expr SQB_END
    ;

return
    :   RESULT_IN (expr | boolExpr | stringExpr) SEMICOLON
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
THEN        : 'THEN';
ELSE        : 'ELSE';
WHEN        : 'WHEN';
ACTION      : 'Action';
STATE       : 'State';
RULE        : 'Rule';
FOR         : 'FOR';
OF          : 'OF';
RESULT      : 'RESULT';     // Get result of a specific action call. Used in Rule
RESULT_IN   : 'RESULT IN';  // Return from action
RESULTS_IN  : 'RESULTS IN'; // Declare return type for action
TEMPLATE    : 'Template';
ALLOWS      : 'ALLOWS';
WITH        : 'WITH';
WITH_LOOP   : 'WITH LOOP';
DOT         : '.';
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
    : 'TRUE'
    | 'FALSE'
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
// NOTE: ANTLR recognizes UPPERCASE as lexer and lowercase as parser!!!s