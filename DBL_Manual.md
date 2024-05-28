<h1 align="center">
    DBL Manual
</h1>
<p align="center">
  The sacred text detailing how to write DBL-code
</p>

## Things that need fixing
- [ ] The EQUALS and NOT EQUALS operator currently only supports comparison with Primitives (Integer, String, Boolean).
- [ ] Comparing Strings with the EQUALS operator, where one operand is the RESULT of an Action Call, only works if the String is on the left side, e.g. `"Play" EQUALS setState.RESULT`.


## The structure of a DBL program
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


## The Language Constructs of DBL
<h3 align="center">
Keywords
</h3>

DBL | Explanation
:---: | :---:
`ALLOWS` | Specify Actions allowed in this State
`FOR`    | Create a for-loop. The structure follows that of C
`IF`     | Create an If-else block. Two additional keywords, `ELSE IF` and `ELSE`, can be specified after an `IF`, but both are optional. `ELSE IF` can be chained infinitely after the `IF`.
`NEW`        | Create an instance of a Template
`RESULT`     | Get the return value of an Action
`RESULT IN`  | Equivalent to the keyword `return` present in many languages.
`RESULTS IN` | Specify the type of the return value for this Action
`WITH LOOP`  | Specify which Statements will be executed when in this State.
`WHEN`       | Specify Actions that will trigger this Rule

<h3 align="center">
Operators
</h3>

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

<h3 align="center">
Special words
</h3>

DBL | Explanation
:---: | :---:
`Template` | Declare a Template. A Template is akin to a struct in C
`Action` | Declare an Action. An Action behaves somewhat similar to that of functions in many imperative languages.  However, they interact with Rules and their execution can be limited by States
`Rule` | Declare a Rule. A Rule is essentially an event listener that listens to select Actions. When an Action is called, this Rule will be invoked when the called Action returns
`States` | Declare a State. A State is a special place in the program execution flow in which only select Actions can be called. This is used to control the duration of a game.

<h3 align="center">
Types
</h3>

DBL | Explanation
:---: | :---:
`Integer` | A number that can be written without a fractional component
`Boolean` | Truth values. Can be either `true` or `false`
`String`  | A sequence of characters encased in citation marks. For instance, `"Hello World"` is a string
***Identifier*** | A user-defined type. For instance, `Card` could be a user-defined type for a card Template

## Syntax
> [!IMPORTANT]
> The syntax formats presented in this section is a simplified version of the DBL grammar.


### Variable declaration
A variable may be declared in two ways; with or without a value.
The format is as follows:
```
Without assignment
<Type> <Identifier> <;>

With assignment
<Type> <Identifier> <Assignment> <Expression> <;>
```
> [!NOTE]
> `<Expression>` is the union of *Expression*, *String Expression*, and *Boolean Expression* as described in the DBL grammar.

Examples:
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
<Expression> <Assignment> <Expression> <;>

```
> [!NOTE]
> `<Expression>` is the union of *Expression*, *String Expression*, and *Boolean Expression* as described in the DBL grammar.

Examples:
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
Template are collections of values. 
They can be declared with the following syntax: 
```
Template Name CONTAINS{
  Integer i;
  Boolean j IS true;
  Inteteger[] integerArray;
  YourCustomTemplate[] templateArray;  
}
```
Note that unitialized fields are assigned a default value that a developer can overwrite. 

### Actions

### Rules

### Arrays
Elements can be appended to an existing array by indexing the array out of bounds.
Example:
```
Integer[] intArray; // Declaration
intArray[0] IS 3;   // Assignment out of bounds
```
