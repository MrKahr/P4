grammar templateLexer;
greeting  : 'Hello,' ENEMY ;         // match keyword hello followed by an identifier
ENEMY : TITLE INT; 
TITLE:[A-Z]+[a-z]+;
INT: [0-9];
WS : [ \t\r\n]+ -> skip ;