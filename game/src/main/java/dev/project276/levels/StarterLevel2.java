package dev.project276.levels;

import dev.project276.entity.Entity;
import dev.project276.entity.EntityStateHandler;
import dev.project276.entity.Pathfinder;
import dev.project276.main.EntityFactory;
import dev.project276.main.GameInfo;

import java.awt.*;
import java.util.ArrayList;

public class StarterLevel2 extends GameInfo {

    @Override
    public GameInfo restartLevel() {
        return new StarterLevel2();
    }

    @Override
    public void initLevel() {
        EntityFactory entityFactory = new EntityFactory(this);
        EntityStateHandler stateHandler = new EntityStateHandler(this);

        ArrayList<Point> line1 = GameInfo.getLineBetweenTwoPoints(new Point(5, 1), new Point(5, 5));
        this.setManyWallsAt(line1);
        this.setManyWallsAt(GameInfo.getLineBetweenTwoPoints(new Point(5, 9), new Point(5, 14)));
        this.setWallAt(6, 9);
        this.setWallAt(9, 9);
        this.setManyWallsAt(GameInfo.getLineBetweenTwoPoints(new Point(10, 1), new Point(10, 5)));
        this.setManyWallsAt(GameInfo.getLineBetweenTwoPoints(new Point(10, 9), new Point(10, 14)));
        this.setManyWallsAt(GameInfo.getLineBetweenTwoPoints(new Point(15, 1), new Point(15, 6)));
        this.setManyWallsAt(GameInfo.getLineBetweenTwoPoints(new Point(15, 8), new Point(15, 14)));
        this.setWallAt(12, 11);
        this.setWallAt(12, 12);
        this.setWallAt(12, 13);

        this.setWallAt(16, 3);
        this.setWallAt(18, 3);
        this.setWallAt(16, 11);
        this.setWallAt(18, 11);

        Entity player = entityFactory.createAndSetPlayer(1,1, stateHandler);
        Pathfinder pathfinder = new Pathfinder(this.getGameBoard(), this.getEntityList(), player);

        entityFactory.createAndSetKey(1, 13);
        entityFactory.createAndSetTrap(7, 9);
        entityFactory.createAndSetDoor(8, 9);
        entityFactory.createAndSetKey(6, 13);
        entityFactory.createAndSetKey(9, 13);
        entityFactory.createAndSetKey(9, 12);
        entityFactory.createAndSetDoor(15, 7);
        entityFactory.createAndSetDoor(17, 3);
        entityFactory.createAndSetLadder(18, 1);
        entityFactory.createAndSetDoor(17, 11);
        entityFactory.createAndSetExit(18, 13);
        entityFactory.createAndSetTrap(6, 4);
        entityFactory.createAndSetTrap(7, 4);
        entityFactory.createAndSetTrap(8, 4);
        entityFactory.createAndSetTrap(7, 2);
        entityFactory.createAndSetTrap(8, 2);
        entityFactory.createAndSetTrap(9, 2);
        entityFactory.createAndSetKey(9, 1);
        entityFactory.createAndSetDoor(11, 11);
        entityFactory.createAndSetCoin(11, 13);

        entityFactory.createAndSetEnemy(6, 2, pathfinder);
        entityFactory.createAndSetPatrol(9, 2, pathfinder);
    }
}
