# Command-line Sudoku with Java

## Description
This is an individual coursework project for the module Algorithms and Data Structures in Edinburgh Napier University.

## Specification (brief)
Create a command-line Sudoku game with the language of your choosing.

## Run from terminal
- If you don't have a JDK set up, download the [newest version of JDK](https://www.oracle.com/java/technologies/downloads/ "JDK Downloads") first, install it and [add its location to the PATH system variable](https://www.ibm.com/docs/en/b2b-integrator/5.2?topic=installation-setting-java-variables-in-windows "Tutorial")
- Download the project as a .zip
- Unzip the contents of the archive
- Open a terminal in the folder where you unzipped the archive file to
- Enter `java -jar ./target/Sudoku-version.jar` (see [Releases]() for current version number)
- Play the game!

## Build instructions
The .jar was built with Maven due to the one external [Gson](https://github.com/google/gson) dependency.

## Additional notes
- Please do not delete or modify the automatically generated `replays.json` file because with doing so, you will not be able to access your previously recorded games
- This project has plenty of room for improvement, please feel free to fork it and add new features to make it more fun and interesting.