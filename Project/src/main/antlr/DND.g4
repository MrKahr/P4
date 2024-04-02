// Uppercase = Lexer
// Lowercase = Parser

// When rules conflict, ANTLR solves this by rule precedence. 
// This means that rules defined first are applied first. This solves problems such as: "Integer Integer" (ANTLR Book, p. 74)

grammar DND;

// TODO: Consider whether statements should be ended by '\n' 
// TODO: Check om vi skal have statement/declarations/ruleDeclartions etc. i en bestemt rækkefølge. 

// PARSER
program 
    :   stmtList
    |   (templateDecl'\n')
    |   (ruleDecl'\n')
    |   (actionDecl'\n')*
    ;

stmt
    :   declaration
    |   
    ;

stmtList
    :   (stmt'\n')* 
    ;

// ANTLR4 handles left-recursive grammars by rule precedence. See ANTLR book p.247
expr
    :   expr '*' expr // Precedence 6 
    |   expr '/' expr // Precedence 5
    |   expr '+' expr // Precedence 4...
    |   expr '-' expr
    |   DIGIT
    |   IDENTIFIER
    ;

boolExpr
    :   expr GT expr
    |   expr GTOE expr
    |   expr LT expr
    |   expr LTOE expr
    |   expr EQUALS expr
    |   NOT boolExpr
    |   boolExpr AND boolExpr
    |   boolExpr OR  boolExpr
    |   BOOLEAN
    ;

// This is very much left-recursive - FIND WAY TO REMOVE!
assignment
    :   IDENTIFIER ASSIGN expr
    |   IDENTIFIER ASSIGN boolExpr
    ;

type_primitive
    :   TYPEDEF_PRIMITIVE
    ;

type_complex
    :   TYPEDEF_COMPLEX
    ;

declaration
    :   type_primitive IDENTIFIER
    |   type_primitive assignment
    |   type_complex IDENTIFIER 
    |   type_complex assignment
    ;

// Make sure stmtList only contains valid statements for TEMPLATE declarations
templateDecl
    :   TEMPLATE IDENTIFIER CONTAINS BODY_START stmtList BODY_END
    ;

ruleDecl
    :
    ;

actionDecl
    :
    ;
stateDecl
    :
    ; 
    
identifierList
    :   (IDENTIFIER (',' IDENTIFIER)*)?
    ;



///////////
// LEXER //
///////////

/*** Non-tokens ***/
LINE_COMMENT : '//' .*? '\r'? '\n' -> skip ; // ANTLR book p. 77
COMMENT : '/*' .*? '*/' -> skip ;            // ANTLR book p. 77
WHITESPACE: [ \t\r]+ -> skip ;             // ANTLR book p. 79


/*** Keywords ***/
IF       : 'IF';
THEN     : 'THEN';
ELSE     : 'ELSE';
ACTION   : 'Action';
STATE    : 'State';
RULE     : 'Rule';
FOR      : 'FOR';
OF       : 'OF';
RESULT   : 'RESULTS';
TEMPLATE : 'Template';

/*** Operators ***/ 
ASSIGN      : 'IS';
CONTAINS : 'CONTAINS';
GT          : 'GREATER THAN';
GTOE        : 'GREATER OR EQUALS';
LT          : 'LESS THAN';
LTOE        : 'LESS OR EQUALS';
EQUALS      : 'EQUALS';
OR          : 'OR';
AND         : 'AND';
NOT         : 'NOT';
ADD         : '+';
SUB         : '-';
MULT        : '*';
DIV         : '/';

/***  Types ***/ 
TYPEDEF_PRIMITIVE
    :   'Integer'
    |   'Boolean'
    |   'String'
    ;

TYPEDEF_COMPLEX
    :   'Deck'
    |   'Card'
    |   'Board'
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

DIGIT
    : [0-9]+
    ;

IDENTIFIER  
    : [A-Za-z]+[0-9A-Za-z]*
    ;
// NOTE: ANTLR recognizes UPPERCASE as lexer and lowercase as parser!!!s