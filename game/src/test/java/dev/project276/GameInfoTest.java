package dev.project276;

import dev.project276.entity.*;
import dev.project276.main.*;
import dev.project276.display.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameInfoTest {
    int maxX, maxY, limitX, limitY;
    Entity[][] gameBoard;
    ArrayList<Entity> lst;
    GameInfo gameInfo;

    @BeforeEach
    public void init(){
        maxX = 20; // cols
        maxY = 15; // rows
        limitX = maxX-1;
        limitY = maxY-1;
        gameBoard = new Entity[maxX][maxY];
        lst = new ArrayList<>();
        gameInfo = new GameInfo();
    }

    /**
     * Testing for consistency across game board
     */
    @Test
    public void baseConstructorBoundsCheck(){
        GameInfo normalGameInfo = new GameInfo();
        gameBoard = normalGameInfo.getGameBoard();
        assertTrue(testGameBoardBounds(limitX, limitY));
    }

    @Test
    public void gameBoardConstructorBoundsCheck(){
        GameInfo gameInfoWithGameBoard = new GameInfo(gameBoard);
        gameBoard = gameInfoWithGameBoard.getGameBoard();
        assertTrue(testGameBoardBounds(limitX, limitY));
    }

    @Test
    public void gameBoardAndEntityListConstructorBoundsCheck(){
        GameInfo gameInfoWithGameBoardAndEntityList = new GameInfo(gameBoard, lst);
        gameBoard = gameInfoWithGameBoardAndEntityList.getGameBoard();
        assertTrue(testGameBoardBounds(limitX, limitY));
    }

    @Test
    public void rowsAndColsConstructorBoundsCheck(){
        GameInfo gameInfoWithRowsAndCols = new GameInfo(maxY, maxX);
        gameBoard = gameInfoWithRowsAndCols.getGameBoard();
        assertTrue(testGameBoardBounds(limitX, limitY));
    }
    
    @Test
    public void rowsColsAndEntityListConstructorBoundsCheck(){
        GameInfo gameInfoWithRowsColsAndEntityList = new GameInfo(maxY, maxX, lst);
        gameBoard = gameInfoWithRowsColsAndEntityList.getGameBoard();
        assertTrue(testGameBoardBounds(limitX, limitY));
    }

    @Test
    public void checkBoardBoundsTestStandardInput(){
        assertTrue(gameInfo.checkBoardBounds(10, 10));
    }

    @Test
    public void checkBoardBoundsTestExceptionalInput(){
        assertFalse(gameInfo.checkBoardBounds(-10, 10));
    }

    @Test
    public void checkBoardBoundsOnPoint(){
        assertFalse(gameInfo.checkBoardBounds(10, maxY));
        assertFalse(gameInfo.checkBoardBounds(maxX, 10));
    }

    @Test
    public void checkBoardBoundsOffPoint(){
        assertTrue(gameInfo.checkBoardBounds(10, maxY - 1));
        assertTrue(gameInfo.checkBoardBounds(maxX - 1, 10));
    }

    @Test
    public void hasEntityTestWithEntity(){
        Floor fl = new Floor(10, 10);
        gameInfo.getEntityList().add(fl);
        assertTrue(gameInfo.hasEntity(fl));
    }

    @Test
    public void hasEntityTestWithoutEntity(){
        Floor fl = new Floor(10, 10);
        gameInfo.getEntityList().add(fl);
        gameInfo.getEntityList().remove(fl);
        assertFalse(gameInfo.hasEntity(fl));
    }

    @Test
    public void hasEntityTypeTestWithType(){
        Wall wall = new Wall(10, 10);
        gameInfo.getEntityList().add(wall);
        assertTrue(gameInfo.hasEntityType(Entity.EntityType.wall));
    }

    @Test
    public void hasEntityTypeTestWithoutType(){
        Wall wall = new Wall(10, 10);
        gameInfo.getEntityList().add(wall);
        assertFalse(gameInfo.hasEntityType(Entity.EntityType.floor));
    }

    @Test
    public void addEntityTestNormal(){
        gameInfo.addEntity(new Sword(10, 10));
        gameInfo.addEntity(new Door(5, 5));
        assertEquals(gameInfo.getEntityList().size(), 2);
    }

    @Test
    public void addEntityTestNull(){
        gameInfo.addEntity(new Key(10, 10));
        gameInfo.addEntity(null);
        assertEquals(gameInfo.getEntityList().size(), 1);
    }

    @Test
    public void getEntityAtTestNormal(){
        assertNotNull(gameInfo.getEntityAt(10, 10));
    }

    @Test
    public void getEntityAtTestExceptional(){
        assertNull(gameInfo.getEntityAt(10, -10));
    }

    @Test
    public void getEntityAtTestOnPoints(){
        assertNull(gameInfo.getEntityAt(maxX, 10));
        assertNull(gameInfo.getEntityAt(10, maxY));
        assertNull(gameInfo.getEntityAt(maxX, maxY));
    }

    @Test
    public void getEntityAtOffPoints(){
        assertNotNull(gameInfo.getEntityAt(maxX-1, 10));
        assertNotNull(gameInfo.getEntityAt(10, maxY-1));
        assertNotNull(gameInfo.getEntityAt(maxX-1, maxY-1));
    }

    @Test
    public void getAllListEntitiesAtNormal(){
        gameInfo.getEntityList().add(new Enemy(10, 10, null));
        gameInfo.getEntityList().add(new Player(10, 10, (KeyInput) null));
        gameInfo.getEntityList().add(new Coin(10, 10));
        assertEquals(gameInfo.getAllListEntitiesAt(10, 10).size(), 3);
    }

    @Test
    public void getAllListEntitiesAtExceptional(){
        assertEquals(gameInfo.getAllListEntitiesAt(10, -10).size(), 0);
    }

    @Test
    public void setWallAtTests(){
        gameInfo.setWallAt(0, 5);
        assertEquals(gameInfo.getGameBoard()[0][5].getEntityType(), Entity.EntityType.wall);
        gameInfo.setWallAt(7, 10);
        assertEquals(gameInfo.getGameBoard()[7][10].getEntityType(), Entity.EntityType.wall);
    }

    @Test
    public void findClosestReachablePointTest(){
        Entity[][] gb = gameInfo.getGameBoard();
        gb[9][10] = new Wall(9, 10);
        gb[9][11] = new Wall(9, 11);
        gb[9][12] = new Wall(9, 12);
        gb[10][12] = new Wall(10, 12);
        gb[11][12] = new Wall(11, 12);
        gb[11][11] = new Wall(11, 11);
        gb[10][9] = new Wall(10, 9);
        /*
        Looks like this:
        W  W  W
        W src
        W  W  W
         */
        assertEquals(new Point(10, 10), gameInfo.findClosestReachablePoint(new Point(11, 10)));
    }

    @Test
    public void isPointReachableTestNormal(){
        assertTrue(gameInfo.isPointReachable(10, 10));
    }

    @Test
    public void isPointReachableTestExceptional(){
        assertFalse(gameInfo.isPointReachable(-10, 10));
    }

    @Test
    public void isPointReachableTestWall(){
        gameInfo.getGameBoard()[10][10] = new Wall(10, 10);
        assertFalse(gameInfo.isPointReachable(10, 10));
    }

    @Test
    public void isPointReachableTestFloor(){
        gameInfo.getGameBoard()[10][10] = new Wall(10, 10);
        gameInfo.getGameBoard()[10][10] = new Floor(10, 10);
        assertTrue(gameInfo.isPointReachable(10, 10));
    }

    @Test
    public void isPointReachableTestNonWallFalse(){
        ArrayList<Entity> el = gameInfo.getEntityList();
        el.add(new Player(10, 10, (KeyInput) null));
        el.add(new Enemy(7, 10, null));
        el.add(new Door(10, 7)); // door is closed by default
        el.add(new Knight(8, 10, null));
        el.add(new Patrol(10, 8, null));
        assertFalse(gameInfo.isPointReachable(10, 10));
        assertFalse(gameInfo.isPointReachable(7, 10));
        assertFalse(gameInfo.isPointReachable(10, 7));
        assertFalse(gameInfo.isPointReachable(8, 10));
        assertFalse(gameInfo.isPointReachable(10, 8));
    }

    @Test
    public void isPointReachableTestNonWallTrue(){
        ArrayList<Entity> el = gameInfo.getEntityList();
        el.add(new Coin(10, 10));
        el.add(new Sword(10, 7));
        el.add(new Trap(7, 10));
        el.add(new Key(11, 11));
        el.add(new Exit(11, 10));
        el.add(new Ladder(10, 11));
        assertTrue(gameInfo.isPointReachable(10, 10));
        assertTrue(gameInfo.isPointReachable(10, 7));
        assertTrue(gameInfo.isPointReachable(7, 10));
        assertTrue(gameInfo.isPointReachable(11, 11));
        assertTrue(gameInfo.isPointReachable(11, 10));
        assertTrue(gameInfo.isPointReachable(10, 11));
    }

    @Test
    public void isPointReachableTestNonWallBoth(){
        ArrayList<Entity> el = gameInfo.getEntityList();
        el.add(new Enemy(10, 10, null));
        el.add(new Coin(10, 10));
        assertFalse(gameInfo.isPointReachable(10, 10));
    }

    /**
     * Tests if the given point is within the game board bounds.
     * Since the game board is accessed with [x][y], we must ensure
     * that 0 <= x < number of arrays in the game board, and
     * that 0 <= y < number of elements in the array
     * @return true if the point is within the game board bounds,
     * i.e. meets the conditions described above. Returns false otherwise.
     */
    private boolean testGameBoardBounds(int x, int y){
        return x >= 0 && x < gameBoard.length && y >= 0 && y < gameBoard[0].length;
    }

}
