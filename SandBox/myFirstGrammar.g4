// Define a grammar called myFirstGrammar
grammar myFirstGrammar;            // Define a grammar called Hello
r  : 'Hello,' enemy;         // match keyword hello followed by an identifier
enemy: 'General' NAME;
NAME : [A-Z][a-z]* ;             // match lower-case identifiers
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines, \r (Windows)

// NOTE: ANTLR recognizes UPPERCASE as lexer and lowercase as parser!!!s