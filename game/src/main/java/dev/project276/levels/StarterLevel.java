package dev.project276.levels;

import dev.project276.entity.Entity;
import dev.project276.entity.EntityStateHandler;
import dev.project276.entity.Pathfinder;
import dev.project276.main.EntityFactory;
import dev.project276.main.GameInfo;

import java.awt.*;
import java.util.ArrayList;

public class StarterLevel extends GameInfo {

    @Override
    public GameInfo restartLevel() {
        return new StarterLevel();
    }

    @Override
    public void initLevel() {
        // Create player
        EntityStateHandler stateHandler = new EntityStateHandler(this);
        EntityFactory entityFactory = new EntityFactory(this);

        // All relevant classes now consistently use x,y
        // x is always the first one, y is always the second one
        ArrayList<Point> pts = new ArrayList<>();
        pts.add(new Point(2, 1));
        pts.add(new Point(2, 3));
        pts.add(new Point(4, 3));
        pts.add(new Point(6, 3));
        pts.add(new Point(2, 5));
        pts.add(new Point(4, 5));
        pts.add(new Point(6, 5));
        pts.add(new Point(2, 7));
        pts.add(new Point(4, 7));
        pts.add(new Point(4, 6));
        pts.add(new Point(4, 2));
        pts.add(new Point(4, 4));
        pts.add(new Point(4, 8));
        pts.add(new Point(6, 7));
        pts.add(new Point(6, 1));
        pts.add(new Point(6, 2));
        pts.add(new Point(6, 4));
        pts.add(new Point(6, 6));
        pts.add(new Point(2, 2));
        pts.add(new Point(2, 4));
        pts.add(new Point(2, 6));
        pts.add(new Point(8, 2));
        pts.add(new Point(7, 4));
        pts.add(new Point(8, 6));
        this.setManyWallsAt(pts);

        Entity player = entityFactory.createAndSetPlayer(1,1, stateHandler);

        Pathfinder pathfinder = new Pathfinder(this.getGameBoard(), this.getEntityList(), player);
        // Create an enemy
        Entity enemy = entityFactory.createAndSetEnemy(18, 1, pathfinder);

        entityFactory.createAndSetKnight(8,1, pathfinder);

        // Add some keys
        entityFactory.createAndSetKey(3, 1);
        entityFactory.createAndSetKey(5, 1);
        entityFactory.createAndSetKey(7, 1);

        // add a door
        entityFactory.createAndSetDoor(5, 7);
        entityFactory.createAndSetDoor(5, 6);
        entityFactory.createAndSetDoor(5, 8);
        entityFactory.createAndSetDoor(5, 2);
        entityFactory.createAndSetDoor(5, 3);
        entityFactory.createAndSetDoor(5, 4);

        // add a sword
        entityFactory.createAndSetSword(5, 5);

        // add a trap
        entityFactory.createAndSetTrap(3, 3);

        // add a ladder
        entityFactory.createAndSetLadder(16, 12);

        // add the exit
        entityFactory.createAndSetExit(17, 12);

        // add a coin
        entityFactory.createAndSetCoin(10, 10);
    }
}
