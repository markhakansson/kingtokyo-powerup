# KingTokyo

This is a refactor of a King Tokyo Power Up game in Java for the course D7032E, Software Engineering at Lulea University of Technology. The purpose of this was to refactor a bad project as good as possible and make it easier to modify and extend for future updates/upgrades.

## Requirements

This program has been tested to work on OpenJDK version 1.8.0_222 and compiled with Javac version 1.8.0_222 on Linux.

## Compile instructions
To compile the source code and unit tests do
```
./compile
```

it will place all compiled files into the /bin directory.
If it does not work try it manually by running the following commands:
```
mkdir bin
javac -d ./bin @sources.txt
javac -d ./bin -cp .:lib/org.junit4-4.3.1.jar @tests.txt
```

## Run the game
The default game runs two bots. To start the game go to the /bin directory and first start the server
```
java  KingTokyo/server/Server
```
