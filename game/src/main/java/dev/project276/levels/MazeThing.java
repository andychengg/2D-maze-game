package dev.project276.levels;

import dev.project276.entity.EntityStateHandler;
import dev.project276.entity.Pathfinder;
import dev.project276.entity.Player;
import dev.project276.main.EntityFactory;
import dev.project276.main.GameInfo;

public class MazeThing extends GameInfo {
    @Override
    public GameInfo restartLevel() {
        return new MazeThing();
    }

    @Override
    public void initLevel() {
        EntityFactory factory = new EntityFactory(this);

        // Long walls on left and right
        this.setManyWallsAlong(2,0, 2,14);
        this.setManyWallsAlong(17,0, 17,14);

        // Walls around Knights
        this.setWallAt(1,2);
        this.setWallAt(18,2);
        this.setWallAt(1,12);
        this.setWallAt(18,12);

        // Top six blocks, 2 under blocks
        this.setWallAt(4,2);
        this.setWallAt(6,2);
        this.setWallAt(8,2);
        this.setWallAt(11,2);
        this.setWallAt(13,2);
        this.setWallAt(15,2);

        this.setWallAt(4,4);
        this.setWallAt(15,4);

        // Bot six blocks, 2 above blocks
        this.setWallAt(4,12);
        this.setWallAt(6,12);
        this.setWallAt(8,12);
        this.setWallAt(11,12);
        this.setWallAt(13,12);
        this.setWallAt(15,12);

        this.setWallAt(4,10);
        this.setWallAt(15,10);

        // Middle horizontal walls
        this.setManyWallsAlong(5,7, 7,7);
        this.setManyWallsAlong(12,7, 14,7);

        // Middle L blocks
        this.setManyWallsAlong(7,4, 8,4);
        this.setWallAt(7,5);

        this.setManyWallsAlong(11,4, 12,4);
        this.setWallAt(12,5);

        this.setManyWallsAlong(7,10, 8,10);
        this.setWallAt(7,9);

        this.setManyWallsAlong(11,10, 12,10);
        this.setWallAt(12,9);

        // Add openings to long walls
        this.setFloorAt(2,3);
        this.setFloorAt(2,7);
        this.setFloorAt(2,11);

        this.setFloorAt(17,3);
        this.setFloorAt(17,7);
        this.setFloorAt(17,11);

        // Middle walls
        this.setManyWallsAlong(9,6, 10, 6);
        this.setManyWallsAlong(9,8, 10, 8);

        // Add opening for the Exit
        this.setFloorAt(0,7);
        factory.createAndSetExit(0,7);

        // Add the ladder
        factory.createAndSetLadder(18,3);
        factory.createAndSetLadder(18,11);

        // Set the doors
        factory.createAndSetDoor(3,2);
        factory.createAndSetDoor(2,3);

        factory.createAndSetDoor(16,2);
        factory.createAndSetDoor(17,3);

        factory.createAndSetDoor(2,11);
        factory.createAndSetDoor(3,12);

        factory.createAndSetDoor(17,11);
        factory.createAndSetDoor(16,12);

        // Set the keys
        factory.createAndSetKey(3,5);
        factory.createAndSetKey(3,9);
        factory.createAndSetKey(16,5);
        factory.createAndSetKey(16,9);

        // Create the player
        EntityStateHandler handler = new EntityStateHandler(this);
        Player player = factory.createAndSetPlayer(1,7, handler);

        // Set the Knights
        Pathfinder pathfinder = new Pathfinder(this, player);
        factory.createAndSetKnight(1,1, pathfinder);
        factory.createAndSetKnight(1,13, pathfinder);
        factory.createAndSetKnight(18,1, pathfinder);
        factory.createAndSetKnight(18,13, pathfinder);

        // Set the normal enemies
        factory.createAndSetEnemy(3,1, pathfinder);
        factory.createAndSetEnemy(3,13, pathfinder);
        factory.createAndSetEnemy(16,1, pathfinder);
        factory.createAndSetEnemy(16,13, pathfinder);

    }
}
