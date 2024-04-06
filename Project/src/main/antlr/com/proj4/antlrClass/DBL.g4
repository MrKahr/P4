/*
*** DOCSTRING - PLEASE READ ***
This docstring describes important assumptions about our current grammar and describes some helpful parts of ANTLR that you need to know to develop on this grammar.   


*** OUR GRAMMAR *** 
1) We assume that (...)


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

grammar DBL;


// TODO: Consider whether statements should be ended by '\n' <--- They will not, as that's too much of a hassle
// TODO: Check om vi skal have statement/declarations/ruleDeclartions etc. i en bestemt rækkefølge. 
// TODO: Fjern left-recursion til CFG i den endelige rapport
// TODO: Sæt en repræsentativ testStreng op (kig på programmerne, vi har skrevet til f.eks. muno)
// TODO: Check wether we allow e.g.: Action ReadAction() RESULTS IN String 

////////////
// PARSER //
////////////
program 
    :   (template_decl)* (action_decl)* (rule_decl)* (state_decl)* stmt_list EOF
    ;

stmt
    :   if_block
    |   for_loop
    |   declaration
    |   assignment SEMICOLON
    |   action_call
    |   template_init
    ;

stmt_list
    :   (stmt)* 
    ;

// This is very much left-recursive - FIND WAY TO REMOVE IN CFG!
expr
    :   expr mult_op expr
    |   expr add_op expr
    |   template_access
    |   action_result
    |   action_call     // Ensure action calls are the last of parser definitions
    |   DIGIT
    |   IDENTIFIER
    ;

// This is very much left-recursive - FIND WAY TO REMOVE IN CFG!
boolExpr
    :   expr GT expr
    |   expr GTOE expr
    |   expr LT expr
    |   expr LTOE expr
    |   expr NOT? EQUALS expr
    |   stringExpr NOT? EQUALS stringExpr
    |   NOT boolExpr
    |   boolExpr AND boolExpr
    |   boolExpr OR  boolExpr
    |   BOOLEAN
    |   IDENTIFIER
    ;

stringExpr
    :   (string | expr) (ADD (string | expr))+      // Doing it this way means implicit type conversion from int to string!. Also, ensure types are correct! (int, str)
    |   string
    |   IDENTIFIER
    ;

assignment // Remember to add semicolon if relevant
    :   (template_access | IDENTIFIER) ASSIGN (expr | boolExpr | stringExpr)
    |   (template_access | IDENTIFIER) ASSIGN (array_access | array_init)
    |   (template_access | IDENTIFIER) ASSIGN (template_access | action_call)
    |   (template_access | IDENTIFIER) ASSIGN IDENTIFIER
    ;

type_primitive
    :   TYPEDEF_PRIMITIVE
    ;

typedef_user
    :   IDENTIFIER
    ;

declaration
    :   type_primitive IDENTIFIER SEMICOLON
    |   type_primitive assignment SEMICOLON
    |   typedef_user IDENTIFIER SEMICOLON
    |   typedef_user assignment SEMICOLON
    |   array_decl SEMICOLON
    ;

declaration_list
    :   (declaration)* 
    ;

body
    :   stmt_list
    ;

for_loop
    :   FOR BRAC_START typedef_user IDENTIFIER OF (template_access | action_call | IDENTIFIER) BRAC_END BODY_START body BODY_END
    |   FOR BRAC_START declaration boolExpr SEMICOLON (expr | assignment) BRAC_END BODY_START body BODY_END
    ;

if_block
    :   IF BRAC_START boolExpr BRAC_END BODY_START body BODY_END (ELSE IF BRAC_START boolExpr BRAC_END BODY_START body BODY_END)* (ELSE BODY_START body BODY_END)?
    ;

template_decl
    :   TEMPLATE typedef_user CONTAINS BODY_START declaration_list BODY_END
    ;

template_init
    :   NEW typedef_user IDENTIFIER BODY_START (template_init | (assignment SEMICOLON))* BODY_END
    ; 

template_access
    :   typedef_user (DOT IDENTIFIER)+
    ;

rule_decl
    :   RULE typedef_user WHEN SQB_START identifier_list SQB_END if_block
    ;

action_decl // Right now return is optional. Should it be like that?
    :   ACTION typedef_user BRAC_START parameter_list BRAC_END (RESULTS_IN (typedef_user | type_primitive | STATE) (BODY_START body return BODY_END)?)?
    |   ACTION typedef_user BRAC_START parameter_list BRAC_END BODY_START body BODY_END // Without RESULTS IN, return is not needed. Meaning this action does not return anything
    ;

action_call
    :   typedef_user BRAC_START argument_list BRAC_END
    ;

action_result   // NOTICE: currently action_result is an expr, which means it's allowed almost everywhere! The only valid use of it is in the "IF()" of an if_block in Rule. Semantic Analysis should handle this
    :   typedef_user DOT RESULT (DOT IDENTIFIER)*
    ;

state_decl
    :   STATE typedef_user ALLOWS SQB_START identifier_list SQB_END DO SQB_START action_call+ SQB_END
    |   STATE typedef_user ALLOWS SQB_START identifier_list SQB_END
    |   STATE typedef_user
    ; 

parameter_list
    :   ((typedef_user | type_primitive | STATE) IDENTIFIER (COMMA (typedef_user | type_primitive | STATE) IDENTIFIER)*)?
    ;

argument_list
    :   ((expr | boolExpr | stringExpr) (COMMA (expr | boolExpr | stringExpr))*)?
    ;

array_decl
    :   (typedef_user | type_primitive) SQB_START SQB_END IDENTIFIER (ASSIGN array_init)?
    ;

array_init
    :   SQB_START ((stringExpr | expr) (COMMA (stringExpr | expr))*)? SQB_END
    ;

array_access
    :   (template_access | IDENTIFIER) SQB_START expr SQB_END
    ;

return
    :   RESULT_IN (expr | boolExpr | stringExpr) SEMICOLON
    ;

// ANTLR4 handles left-recursive grammars by rule precedence. See ANTLR book p.247
mult_op
    :   MULT  // Precedence 6 
    |   DIV   // Precedence 5 
    ;


add_op
    :   ADD   // Precedence 4 
    |   SUB   // Precedence 3 
    ;

string
    :   STRING
    ;

identifier_list
    :   (IDENTIFIER (COMMA IDENTIFIER)*)?
    ;

///////////
// LEXER //
///////////

/*** Non-tokens ***/
LINE_COMMENT : '//' .*? '\r'? '\n' -> skip ; // ANTLR book p. 77
COMMENT : '/*' .*? '*/' -> skip ;            // ANTLR book p. 77
WHITESPACE: [ \t]+ -> skip ;             // ANTLR book p. 79 - note that newline is not included
NEWLINE: [\r\n] -> skip;

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
DO          : 'DO';
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