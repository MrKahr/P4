<h1 align="center">
    DBL Manual
</h1>
<p align="center">
  The sacred text detailing how to write DBL-code
</p>


## Things that need fixing
- [ ] The EQUALS and NOT EQUALS operator currently only supports comparison with Primitives (Integer, String, Boolean).
- [ ] Comparing Strings with the EQUALS operator, where one operand is the RESULT of an Action Call, only works if the String is on the left side, e.g. `"Play" EQUALS setState.RESULT`.
- [ ] If the last line of a DBL program is a comment, the interpreter will fail with a parse error. A quick work-around is to ensure that an empty line is always the last line in the program. 


## The Structure of a DBL Program
A valid DBL-program is structured in a specific manner as seen below. While any of the numbered elements may be omitted, if present, they must appear in the order described here.
1. Template declarations
2. Action declarations
3. Rule declarations
4. State declarations
5. Statements

Statements can be any of the following elements - in any combination desired.
* If-else
* For-loop
* Variable declaration
* Variable assignment
* Action calls

<h2 align="center">
    The Language Constructs of DBL
</h2>
<h3 align="center">
Keywords
</h3>

<div align="center">
    
DBL | Explanation
:---: | :---
`ALLOWS` | Specify Actions allowed in this State
`FOR`    | Create a for-loop<br> The syntax follows that of C
`IF`     | Create an If-else block<br> Two additional keywords, `ELSE IF` and `ELSE`, can be specified after an `IF`,<br> but both are optional. `ELSE IF` can be chained infinitely after the `IF`
`NEW`        | Create an instance of a Template
`RESULT`     | Get the return value of an Action
`RESULT IN`  | Equivalent to the keyword `return` present in many languages
`RESULTS IN` | Specify the type of the return value for this Action
`WITH LOOP`  | Specify which Statements will be executed when in this State
`WHEN`       | Specify Actions that will trigger this Rule

</div>
<h3 align="center">
Operators
</h3>

<div align="center">

Here we list operators which have syntax differing from "convention" specified in large languages such as C or Java.
DBL | Equivalent in Java
:---: | :---:
`NOT` | `!`
`AND` | `&&`
`OR`  | `\|\|`
`GREATER THAN`      | `>`
`GREATER OR EQUALS` | `>=`
`LESS THAN`         | `<`
`LESS OR EQUALS`    | `<=`
`EQUALS`            | `==`
`NOT EQUALS`        | `!=`
`IS` | `=`

</div>

<h3 align="center">
Domain-specific Keywords
</h3>

<div align="center">

DBL | Explanation
:---: | :---
`Template` | Declare a Template<br> A Template is akin to a struct in C
`Action` | Declare an Action<br> An Action behaves somewhat similar to that of functions in many imperative languages. However, they interact with Rules and their execution can be limited by States
`Rule` | Declare a Rule<br> A Rule is essentially an event listener that listens to select Actions. When an Action is called, any Rules listening to it will be invoked, in the order they are declared, just before the called Action returns
`State` | Declare a State<br> A State is a special place in the program execution flow in which only select Actions can be called. This is used to control the duration of a game

</div>

<h3 align="center">
Types
</h3>

<div align="center">

DBL | Explanation
:---: | :---
`Integer` | A number that can be written without a fractional component
`Boolean` | Truth values. Can be either `true` or `false`
`String`  | A sequence of characters encased in citation marks<br> For instance, `"Hello World"` is a string
***Identifier*** | A user-defined type<br> For instance, `Card` could be a user-defined type for a card Template

</div>


## Syntax
> [!IMPORTANT]
> The syntax format presented in this section is a simplified version of the DBL grammar.

> [!NOTE]
> When \<Expression> is mentioned it is the union of `Expression`, `String Expression`, and `Boolean Expression` as described in the DBL grammar.


### Variable declaration
A variable may be declared in two ways; with or without a value.  
The format is as follows:
```
// Without assignment
<Type> <Identifier> ;

// With assignment
<Type> <Identifier> <Assignment> <Expression> ;
```

Example variable declarations:
```
Integer name;
Integer name IS 3;
```

### Variable assignment
The value of a variable may be assigned to another variable.
This holds true for variables referring to Templates or Arrays as well.

However, the type of both variables must be equal.
Additionally, arrays must also be of equal dimension, i.e. it is not valid to assign a 2-dimensional array to a 1-dimensional array.

The format is as follows:
```
<Expression> <Assignment> <Expression> ;

```

Example variable assignments:
```
name IS name + 1;                // Increment the variable by 1
name IS name2;                   // Assign a variable to another variable
name IS name2 + "Hello";         // String concatenation
name IS name2 + "World" + 99;    // Integers can be casted to strings as well
name[0] IS name2.health;         // Assign value of a template to an array
name2.cost IS names[1+5].price;  // Templates in arrays can be accessed too
```

### Arrays
Arrays can be declared as following:
* `Integer[] array;`
* `Integer[] array IS [1];`

Arrays can NOT be declared as:
* `Integer[] array IS [];`


## Semantics

### Templates
A Template is a collection of attributes. 
They can be declared with the following syntax:
```
Template <Identifier> CONTAINS {<declarations>}
```

Example template declarations:
```
Template Card CONTAINS {
  String color;
  Integer value;
}

Template Deck CONTAINS {
  Card[] cards;
}

Template Board CONTAINS {
    Player[] players;
    Deck[] decks;       
    Player activePlayer;
}
```
Note that unitialized fields are assigned a default value that a developer can overwrite.

Type | Default Value
:---: | :---
`Integer` | 0
`Boolean` | false
`String`  | ""
***Identifier*** | User-defined types will, eventually, point to one of<br> the primitives which then becomes its default value.

An instance of a template can be created with the following syntax:
```
NEW <TemplateIdentifier> {<Expressions> ; } ;

// Note: Templates can be instantiated recursively as such:
NEW <TemplateIdentifier> {<TemplateInstances> ; } ;

// These two methods can be combined, where "|" denotes logical OR, and is NOT part of the syntax!
NEW <TemplateIdentifier> {<Expressions> ; | <TemplateInstances> ; } ;
```

Examples of template instantiation:
```
Deck resultDeck IS NEW Deck{};

resultDeck IS addCard(NEW Card{colors[i]; values[j];}, resultDeck);

Player playerOne IS NEW Player {"John"; NEW Deck{};};

Board b1 IS NEW Board {
    [playerOne, playerTwo];
    [shuffle(makeCards())];
    playerOne;
};
```

### Actions
As mentioned earlier, an action behaves somewhat similar to that of functions in many imperative languages.
They are intended as the primary method for a developer to modify the program state (we do not mean the State keyword). 
An action can take a list of parameters and return a value of a specified type.
Both parameters and a return value are optional.
> [!IMPORTANT]
> If an action is supposed to return a value, the return-statement MUST be the last line of the action body.

Actions can be declared in multiple ways.  
The syntax is the following:
```
// No return value
Action <Identifier> (<parameters>) {<body>}

// With return value
Action <Identifier> (<parameters>) RESULTS IN <Type> {<body> RESULT IN <Expression> ; } 
```

Example action declarations:
```
Action checkWinner(){}

Action declareWinner(Player player) {
    write("Winner found: " + player.name + ". Congratulations!");
}

Action addNumber(Integer a, Integer b) RESULTS IN Integer {
    RESULT IN a+b;
}
```

Actions are treated as expressions and can be called anywhere a statement is allowed.  
They can be called using the following syntax: 
```
<ActionIdentifier> (<arguments>) ;
```

Example:
```
addNumber(1, 2);
```

The latest returned value of an action can be referred to using the .RESULT notation:
```
addNumber.RESULT;
```
This notation is also treated as an expression.

Lastly, DBL has inbuilt actions.

Inbuilt action | Explanation
:---: | :---
`setState(String state);` | Sets the current State to the state matching the supplied argument.
`size(Any[] array);` | Returns the amount of elements in the array supplied as argument. The array can have any type.
`write(String message);` | Writes the string provided as argument to the connected terminal (stdout).


### Rules
As mentioned earlier, a rule is essentially an eventlistener which triggers when select actions are called.
Rules also have a condition which must be satisfied in order for the rule body to be executed. 
Since a rule can call actions, and thus invoke other rules (or itself!), they have a specific execution order.
When a rule's action calls activate other rules, the newest activated rule is handled first. This means Rules are evaluated according to the LIFO principle (last in first out). 

For instance, suppose we have rules *r1* and *r2* listening to action *a*, and rule *r3* listening to action *b*. 
Assuming that rule *r1* calls action *b*, the rules are evaluated in the order *r1*, *r3*, *r2*.

The syntax for declaring a rule is as follows:
```
Rule <Identifier> WHEN [<ActionIdentifiers>] IF (<condition>)
```
> [!NOTE]
> The IF-statement at the end of a rule declaration is an ordinary IF-ELSE statement, i.e. the same syntax/semantics apply.

Example rule declaration:
```
Rule winner WHEN [checkWinner] IF (size(storeBoard.RESULT.activePlayer.hand.cards) LESS THAN 1) {
    declareWinner(storeBoard.RESULT.activePlayer);
    setState("End");
}
```
> [!IMPORTANT]
> Rules can only access variables through the RESULT field of actions.
> This behaviour is intended, but is planned to change in a future update (if we decide to continue work on this project, that is).

### States
 * A State allows developers to create a model of a game which transitions between states by performing Actions that may or may not be selected by a player. 
 * A State can allow 0 or more actions which are the only ones that may be executed (by the player/user) when in this state.
 * Having states is not mandatory, and a program will terminate when all statements have been evaluated and the current state allows no more actions to be performed.
 * Defining no states will simply be treated as if no actions are allowed by the current state.
A State can be declared in two ways:
```
State <Identifier> ALLOWS [<ActionIdentifiers>]
State <Identifier> ALLOWS [<ActionIdentifiers>] WITH LOOP {<body>}
```
The \<body> of the control structure *WITH LOOP* executes Statements whose nature are defined in [The structure of a DBL program](#the-structure-of-a-dbl-program).
This code is executed before input from a player is read. 
When a state is active, the state loop runs every time an action has been selected and run to completion, including the rules it has triggered. 
This is generally intended to provide developers with an opportunity to make changes to the game state every time a user takes an action.

Example state declarations:
```
State Play ALLOWS [playCard]

State AutoPlay ALLOWS [] WITH LOOP {
    FOR(Integer i; "End" NOT EQUALS setState.RESULT; i IS i) {
        Integer validCardIndex IS findValidCard();
        playCard(validCardIndex);
    }
}

State End ALLOWS []
```

### Arrays
Elements can be appended to an existing array by indexing the array out of bounds.
Example:
```
Integer[] intArray; // Declaration
intArray[0] IS 3;   // Assignment out of bounds
```

When specifying the type of an array for use with variables, the dimension of the array, in addition to its type, must be supplied.  
For instance:
```
// This action takes a 1D-array an returns it. How convenient!
// (actually, it IS pretty convenient, but that's a long story)
Action seedStore(Integer[] seeds) RESULTS IN Integer[] {
    RESULT IN seeds;
}

// This action takes two 2D-arrays an merges them to a 3D-array
Action tad1(Integer[][] arrA, Integer[][] arrB) RESULTS IN Integer[][][] {
    Integer[][][] arrC IS [arrA, arrB];
    RESULT IN arrC;
}
```

Oh, and as you've probably guessed, arrays can be of arbitrary dimension (though we've only tested up to 3D-arrays)
