# Maze Caller

A dungeon escape game

## Getting Started

### Getting the JAR/Javadocs
cd to the /game directory, then execute ```mvn clean package```. You should see the JAR file under /target and the HTML files asssociated with the Javadocs in /target/site.

### How to Run
Ensure that you are in the /game directory, then run ```java -cp target/project276-1.0-SNAPSHOT.jar dev.project276.main.Main``` from the command line.

### How to Test
cd to the /game directory, the execute ```mvn clean compile``` and ```mvn test```.

### How to Play
Up/left/down/right arrow keys to move up/left/down/right on the gameboard, space to attack if you have a sword, and R to restart the level if you lost.  
Collect keys to unlock doors, and collect the ladder to be able to go into the hole to exit the level.
Collect coins for bonus points and collect swords to attack enemies.  
**Tip**: Hold the arrow keys while moving for the best results.  
![Alt key](game/src/main/resources/images/key_big.png?raw=true "A key")
![Alt door](game/src/main/resources/images/door.png?raw=true "A door")
![Alt ladder](game/src/main/resources/images/ladder.png?raw=true "A ladder")
![Alt exit](game/src/main/resources/images/hole.png?raw=true "An exit")
![Alt coin](game/src/main/resources/images/coin.png?raw=true "A coin")
![Alt sword](game/src/main/resources/images/sword_new.png?raw=true "A sword")

### Testing
To run the test, If your cursor is in a specific method's scope, only that test runs. If your cursor is in the class' scope, it runs all tests.
If using Intellij 'CTRL + SHIFT + F10' is a shortcut to run test

## Contibutors

Made for CMPT276 by Andy Cheng, Martin Lau, Andy Lu and Jackson Nguyen
#2d-maze-game
