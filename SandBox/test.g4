/*
*** DOCSTRING - PLEASE READ ***
This docstring describes important assumptions about our current grammar and describes some helpful parts of ANTLR that you need to know to develop on this grammar.   


*** OUR GRAMMAR *** 
1) We assume that (...)


*** IMPORTANT ANTLR-FEATURES *** 
ANTLR defines a grammar with the grammar keyword and an identifier. 
This is the name of the file that you can use in the [Run ANTLR.bat]-file.grammar
You can optionally add a [SOMENAME.txt] file as a test file that you can try the lexer and parser on. 

ANTLR grammars use Extended Backus-Naur form to define its grammar; it therefore closely resembles a CFG (p. 264-265). 
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

// TODO: Consider whether statements should be ended by '\n' 
// TODO: Check om vi skal have statement/declarations/ruleDeclartions etc. i en bestemt rækkefølge. 
// TODO: Fjern left-recursion til CFG i den endelige rapport
// TODO: Sæt en repræsentativ testStreng op (kig på programmerne, vi har skrevet til f.eks. muno)
// TODO: Implement dot notation 
// TODO: Implement array
// TODO: Implement RESULT IN keyword

////////////
// PARSER //
////////////
program 
    :   (template_decl)* (action_decl)* (rule_decl)*  (state_decl)* stmt_list EOF
    ;

stmt
    :   if_block
    |   for_loop
    |   declaration SEMICOLON
    |   assignment SEMICOLON
    |   action_call
    ;

stmt_list
    :   (stmt)* 
    ;

expr
    :   expr simple_op expr 
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

// This is very much left-recursive - FIND WAY TO REMOVE IN CFG!
assignment
    :   IDENTIFIER ASSIGN expr
    |   IDENTIFIER ASSIGN boolExpr
    ;

type_primitive
    :   TYPEDEF_PRIMITIVE
    ;

// type_complex
//     :   TYPEDEF_USER
//     ;

typedef_user
    :   IDENTIFIER
    ;

declaration
    :   type_primitive IDENTIFIER SEMICOLON
    |   type_primitive assignment SEMICOLON
    |   IDENTIFIER IDENTIFIER SEMICOLON
    |   IDENTIFIER assignment SEMICOLON
    ;

declaration_list
    :   (declaration)* 
    ;

body
    :   stmt_list
    ;

for_loop
    :   FOR declaration OF IDENTIFIER BODY_START body BODY_END
    |   FOR declaration COMMA boolExpr COMMA expr BODY_START body BODY_END
    ;

if_block
    :   IF BRAC_START boolExpr BRAC_END BODY_START body BODY_END (ELSE IF BRAC_START boolExpr BRAC_END BODY_START body BODY_END)* ELSE BODY_START body BODY_END
    ;

// Make sure stmtList only contains valid statements for TEMPLATE declarations
template_decl
    :   TEMPLATE typedef_user CONTAINS BODY_START declaration_list BODY_END
    ;

rule_decl
    :   RULE typedef_user WHEN SQB_START identifiers SQB_END IF SQB_START boolExpr SQB_END THEN SQB_START action_call* SQB_END
    |   RULE typedef_user WHEN SQB_START identifiers SQB_END IF SQB_START boolExpr SQB_END THEN SQB_START action_call* SQB_END (ELSE IF SQB_START boolExpr SQB_END THEN SQB_START action_call* SQB_END)*
    ;

action_decl
    :   ACTION typedef_user 
    ;

action_call
    :   typedef_user BODY_START identifiers BODY_END
    ;

state_decl
    :   STATE IDENTIFIER ALLOWS SQB_START identifiers SQB_END DO SQB_START action_call+ SQB_END
    |   STATE IDENTIFIER ALLOWS SQB_START identifiers SQB_END
    |   STATE IDENTIFIER
    ; 

// arguments
//     :   (IDENTIFIER (',' IDENTIFIER)*)?
//     ;

parameters
    :   ((typedef_user | type_primitive) IDENTIFIER (COMMA (typedef_user | type_primitive) IDENTIFIER)*)?
    ;

// ANTLR4 handles left-recursive grammars by rule precedence. See ANTLR book p.247
simple_op
    :   MULT  // Precedence 6 
    |   DIV   // Precedence 5 
    |   ADD   // Precedence 4 
    |   SUB   // Precedence 3 
    ;

identifiers
    :   (IDENTIFIER (COMMA IDENTIFIER)*)?
    ;

///////////
// LEXER //
///////////

/*** Non-tokens ***/
LINE_COMMENT : '//' .*? '\r'? '\n' -> skip ; // ANTLR book p. 77
COMMENT : '/*' .*? '*/' -> skip ;            // ANTLR book p. 77
WHITESPACE: [ \t]+ -> skip ;             // ANTLR book p. 79 - note that newline is not included
LINEBREAK: [\r\n] -> skip;

/*** Keywords ***/
IF       : 'IF';
THEN     : 'THEN';
ELSE     : 'ELSE';
WHEN     : 'WHEN';
ACTION   : 'Action';
STATE    : 'State';
RULE     : 'Rule';
FOR      : 'FOR';
OF       : 'OF';
RESULT   : 'RESULTS';
TEMPLATE : 'Template';
ALLOWS   : 'ALLOWS';
WITH     : 'WITH';
DO       : 'DO';
DOT      : '.';

/*** Operators ***/ 
ASSIGN      : 'IS';
CONTAINS    : 'CONTAINS';
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

// TYPEDEF_COMPLEX
//     :   'Deck'
//     |   'Card'
//     |   'Board'
//     ;

// TYPEDEF_USER
//     :   IDENTIFIER
//     ; 

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