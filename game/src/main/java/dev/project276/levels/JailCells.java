package dev.project276.levels;

import dev.project276.entity.EntityStateHandler;
import dev.project276.entity.Pathfinder;
import dev.project276.entity.Player;
import dev.project276.main.EntityFactory;
import dev.project276.main.GameInfo;

public class JailCells extends GameInfo {

    @Override
    public GameInfo restartLevel() {
        return new JailCells();
    }

    @Override
    public void initLevel() {
        EntityFactory factory = new EntityFactory(this);

        // Horizontal walls
        this.setManyWallsAlong(0,4, 13, 4);
        this.setManyWallsAlong(7,8, 18, 8);
        this.setManyWallsAlong(4,11, 18,11);
        //this.setManyWallsAlong(4,11, 6, 11);

        // Vertical walls
        this.setManyWallsAlong(5, 0, 5, 4);
        this.setManyWallsAlong(7,8, 7, 14);
        this.setManyWallsAlong(13,0, 13, 8);
        this.setManyWallsAlong(4,11, 4, 13);

        // Make openings in the walls
        this.setFloorAt(5,3);
        this.setFloorAt(13,1);
        this.setFloorAt(18,8);
        this.setFloorAt(11,8);
        this.setFloorAt(11,9);
        this.setFloorAt(4,13);

        this.setFloorAt(10,11);
        this.setFloorAt(16,11);

        // Set the doors
        factory.createAndSetDoor(5,3);
        factory.createAndSetDoor(13,1);
        factory.createAndSetDoor(18,8);
        factory.createAndSetDoor(11,7);
        factory.createAndSetDoor(11,8);
        factory.createAndSetDoor(4,13);

        // Set the keys
        factory.createAndSetKey(3,2);
        factory.createAndSetKey(6,1);
        factory.createAndSetKey(16,6);
        factory.createAndSetKey(12,5);
        factory.createAndSetKey(16,12);
        factory.createAndSetKey(10,12);

        // Set the ladder
        factory.createAndSetLadder(1,5);

        // Set the exit
        factory.createAndSetExit(6,13);

        // Create the player
        EntityStateHandler handler = new EntityStateHandler(this);
        Player player = factory.createAndSetPlayer(1,2, handler);

        // Set the enemies
        Pathfinder pathfinder = new Pathfinder(this, player);
        factory.createAndSetEnemy(10,2, pathfinder);
        factory.createAndSetEnemy(16,6, pathfinder);
        factory.createAndSetEnemy(5, 6, pathfinder);

        factory.createAndSetEnemy(18,13, pathfinder);
        //factory.createAndSetEnemy(9,13, pathfinder);

        factory.createAndSetPatrol(2,13, pathfinder);

        // Set the bonus coins
        factory.createAndSetCoin(18,12);
        factory.createAndSetCoin(6,12);
    }
}
