<h1 align="center">
    P4: DBL (Deck Builder Language) 
</h1>
<p align="center">
    <a href="/DBL_Manual.md">DBL Manual</a>
</p>

## Introduction

A student project at Aalborg University that describes a domain-specific programming language, called DBL, for creating high-fidelity deck builder protoypes.

## Running from source

> [!IMPORTANT]
> **Prerequisites**
> - JDK 21
> - Maven version 3.9.5 or newer

1. Clone this repository.
2. Open a terminal in the project directory (`<root-folder>/P4/project`).
3. Type `mvn clean install` to generate Java files and compile the entire project.
4. Provide the absolute path to the DBL source file as an argument when running main. 

## Troubleshooting

If you're having trouble with the installation process, you can try these steps.
> [!NOTE]
> This section assumes a Windows enviroment.
> 
> Remember to close any open terminals after making changes<br> to the enviroment variables as your changes are not saved in open terminals.

### JAVA_HOME not found

If Maven complains about a missing JAVA_HOME, you need to create a new enviroment variable pointing to your Java installation.  
This can be done in the following ways:

<details>
<summary>Using Control Panel</summary>

<details>
<summary>Quick shortcut to Control Panel</summary>

Press CTRL + R on your keyboard.  
Paste `explorer.exe shell:::{BB06C0E4-D293-4f75-8A90-CB05B6477EEE}` into the Run dialog and hit enter.  
Then, you can go directly to step 3.
</details>

1. Open Control Panel and find "System".
2. Right click "System" and click "Open" in the dialog.
3. Click on "Advanced system settings" in the top-left panel.
4. Click on "Enviroment variables" in the buttom-right.
5. Under "System variables", click the "New..." button.
6. Under "Variable name", write JAVA_HOME. The value of the variable is your Java installation directory, e.g. `C:\Program Files\Java\jdk-17`.

> **WARNING**  
> Do not include `/bin` in your JAVA_HOME. Things WILL break!
</details>

<details>
<summary>Using CMD</summary>

If you have some experience with terminals and commands this might be an easier way.  
> **Note**  
> When JAVA_PATH is mentioned, it means the path to your Java installation directory, e.g. `C:\Program Files\Java\jdk-17`.

1. Open a CMD terminal in Administrator mode
2. Paste the following into the terminal and hit enter (with quotes):

```cmd
setx /M JAVA_HOME "JAVA_PATH"
```

> **WARNING**  
> Do not include `/bin` in your JAVA_HOME. Things WILL break!
</details>

## Documentation for DBL
Please refer to the [DBL Manual](/DBL_Manual.md)

## Acknowledgements

Built using:

- [ANTLR](https://www.antlr.org/)
