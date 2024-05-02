@ECHO OFF
SET "CWD=%~dp0"

:: Path stuff ::
CD ..
SET "GRAMMAR_PATH=%CD%\Project\src\main\antlr\com\proj4\antlrClass\DBL.g4"
CD "%CWD%"
::::::::::::::::

SET OUTFOLDER=out
SET "OUTPATH=%CWD%%OUTFOLDER%"
SET DO_COMPILE=True
SET ANTLRJAVA=antlr-4.13.1-complete.jar
SET URL=https://www.antlr.org/download/%ANTLRJAVA%

:::: CLASSPATH stuff ::::
SET "CP_ANTLR=%CWD%%ANTLRJAVA%"
SET CLASSPATH=.;%CP_ANTLR%;%OUTPATH%
:::::::::::::::::::::::::

IF NOT EXIST "%OUTPATH%" (mkdir "%OUTPATH%")

:INTRO
    echo:
    echo: ______________________________________________________________
    echo:
    echo:  Hello there!
    echo:  This script is intended to help run ANTLR Java Edition.
    echo:  To do this, the following steps will be executed:		         
    echo:
    echo:  - Download '%ANTLRJAVA%' from
    echo:    %URL%
    echo:  - Get grammar file from the user (that's YOU!)
    echo:    (this step is needed since grammar files can be anything)
    echo:  - Define correct classpath to jar files
    echo:    (not permanent, so don't worry)
    echo:  - Run %ANTLRJAVA% with your grammar file
    echo:    (Generates java source code ^+ other stuff)
    echo:  - Run the ANTLR TestRig (grun)
    echo:    (the lexer/parser using your grammar)
    echo: ______________________________________________________________
    echo:


:MENU
    echo:
    echo: ____Start______________
    echo:  1. Run ANTLR4
    echo:
    echo: ____Settings___________
    echo:  s1. Compile project: %DO_COMPILE%
    echo:  s2. Change grammar
    echo:      Current grammar is: %GRAMMAR_PATH%
    echo:
    set /p menuChoice="Select option: "

    IF %menuChoice%==1 GOTO ANTLR
    IF %menuChoice%==s1 (
        SET DO_COMPILE=False
        CLS
        GOTO INTRO
    )
    IF %menuChoice%==s2 GOTO CHANGE_GRAMMAR
    GOTO MENU

:CHANGE_GRAMMAR
    CLS
    echo:
    echo:  Please enter your grammar file   (format: filename.extention)
    echo:  NOTE: Only one dot (.) is allowed!
    echo:        The path must be relative to '%CWD%'
    echo:  Example: hello.txt
    echo:
    set /p grammarfile="Enter file: "

    IF NOT EXIST %grammarfile% (
        echo:
        echo:  __________________________________________
        echo:
        echo:  ERROR!
        echo:  Cannot find file '%grammarfile%'
        echo:  Please make sure the file is in the same folder as this script
        echo:  Hint: place the grammar file in: %CWD%
        echo:  __________________________________________
        echo:
        GOTO CHANGE_GRAMMAR
    )
    SET GRAMMAR_PATH = %grammarfile%
    GOTO MENU

:ANTLR
    CLS
    for /f "tokens=1,2 delims=." %%a in ('dir /B "%GRAMMAR_PATH%"') do (
        set FILENAME=%%a
        set FILEEXT=%%b
    )

    IF NOT EXIST %ANTLRJAVA% (
        echo:  __________________________________
        echo:
        echo:  Downloading %ANTLRJAVA% from %URL%
        cd /D "%CWD%"
        @ECHO ON
        curl -O %URL%
        @ECHO OFF
        echo:
        echo:  Placed in: %CWD%%ANTLRJAVA%
        echo:  Done!
        echo:  __________________________________
    )
    IF %DO_COMPILE%==True (
        GOTO COMPILE
    ) ELSE (
        GOTO INTRO_FILETEST
    )

:COMPILE
    echo:
    echo: Generating java source files
    ::@ECHO ON
    java -jar %ANTLRJAVA% -o "%OUTFOLDER%" "%GRAMMAR_PATH%"
    ::@ECHO OFF

    echo: Compiling java sources files
    ::@ECHO ON
    FOR /R "%OUTPATH%" %%P IN (*.java) DO javac "%%P" 
    ::@ECHO OFF


:INTRO_FILETEST
    echo:
    echo: ___________________________
    echo: 
    echo:  And now for the fun part
    echo:  Let's test the grammar!
    echo:  To to this, first create a text file (.txt)
    echo:  Then, open this text file and sinply write some text!
    echo:  (Optionally, some text which the grammar describes)
    echo: ___________________________
    echo:


:FILETEST
    echo:  Now, please enter your test file   (format: filename.txt)
    echo:  This file is where you write strings to test your grammar against
    echo:
    set /p testfile="Enter file: "

    IF NOT EXIST %testfile% (
        echo:
        echo:  __________________________________________
        echo:
        echo:  ERROR!
        echo:  Cannot find file '%testfile%'
        echo:  Please make sure the file is in the same folder as this script
        echo:  Hint: place the test file file in: %CWD%
        echo:  __________________________________________
        echo:
        GOTO FILETEST
    )

    echo:
    echo:  Next, a starting rule is required!
    echo:  (Defined in the grammar)
    echo:
    set /p rule="Enter starting rule: "

    echo:
    echo: Generating parse tree
    @ECHO ON
    java -cp "%CLASSPATH%" org.antlr.v4.gui.TestRig %FILENAME% %rule% %testfile% -gui
    @ECHO OFF

echo:
echo: Reached end of the script!
pause 