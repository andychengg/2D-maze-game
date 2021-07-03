package dev.project276;

import dev.project276.entity.*;
import dev.project276.main.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PathfinderTest {

    GameInfo info;
    Pathfinder pathfinder;
    Entity source;

    int startX = 5;
    int startY = 5;

    int topLeftX = 1;
    int topLeftY = 1;

    //int currX;
    //int currY;

    public boolean targetFound(int startX, int startY, int destX, int destY) {
        return startX == destX && startY == destY;
    }

    @BeforeEach
    public void setupPathfinder() {
        info = new GameInfo();
        pathfinder = new Pathfinder(info);
        source = new Floor(startX, startY);
    }


    @Test
    public void printDefaultBoard() {
        pathfinder.printFloorBoard();
    }

    @Test
    public void pathNormalShortestPath() {
        // Top left corner
        int currX = topLeftX;
        int currY = topLeftY;

        // Bot right corner
        int destX = info.getCols()-2;
        int destY = info.getRows()-2;

        // Number of moves needed
        int distance = pathfinder.getDistanceBetween(currX, currY, destX, destY);

        for(int i=0; i<distance; i++) {
            Node nextMove = pathfinder.getNextMoveTo(currX, currY, destX, destY);
            currX = nextMove.getX();
            currY = nextMove.getY();
        }

        assertEquals(destX, currX);
        assertEquals(destY, currY);
    }

    @Test
    public void pathKnightReachablePath() {
        // Top left corner
        int currX = topLeftX;
        int currY = topLeftY;

        // Bot right corner
        int destX = info.getCols()-2;
        int destY = info.getRows()-2;

        // Maximum number of moves
        int max = info.getArea();

        for(int i=0; i<max; i++) {
            Node nextMove = pathfinder.getNextMoveKnightTo(currX, currY, destX, destY);
            currX = nextMove.getX();
            currY = nextMove.getY();
        }

        assertEquals(destX, currX);
        assertEquals(destY, currY);
    }

    @Test
    public void pathStraightWallOneOpening() {
        // Set a wall straight down the middle of the board, with an opening at the bottom
        int col = info.getCols();
        int row = info.getRows();
        int mid = col / 2 ;
        info.setManyWallsAlong(mid, 0, mid, row-3);

        // Set pathfinder to this board
        pathfinder.initFloorBoard();
        pathfinder.printFloorBoard();

        // Start at the top left corner
        int currX = topLeftX;
        int currY = topLeftY;

        // Try to reach top right corner
        int destX = col-2;
        int destY = topLeftY;

        // Maximum number of moves
        int max = info.getArea();

        for(int i=0; i<max && !targetFound(currX,currY,destX,destY); i++) {
            Node nextMove = pathfinder.getNextMoveTo(currX, currY, destX, destY);
            currX = nextMove.getX();
            currY = nextMove.getY();
        }

        assertEquals(destX, currX);
        assertEquals(destY, currY);
    }

    @Test
    public void pathNormalNoPath() {
        // Set a wall straight down the middle of the board, no openings
        int col = info.getCols();
        int row = info.getRows();
        int mid = col / 2 ;
        info.setManyWallsAlong(mid, 0, mid, row-2);

        // Set pathfinder to this board
        pathfinder.initFloorBoard();
        pathfinder.printFloorBoard();

        // Start at the top left corner
        int currX = topLeftX;
        int currY = topLeftY;

        // Try to reach bot right corner
        int destX = col-2;
        int destY = row-2;

        // Maximum number of moves
        int max = info.getArea();

        for(int i=0; i<max && !targetFound(currX,currY,destX,destY); i++) {
            Node nextMove = pathfinder.getNextMoveTo(currX, currY, destX, destY);
            currX = nextMove.getX();
            currY = nextMove.getY();
        }

        assertNotEquals(destX, currX);
        assertNotEquals(destY, currY);
    }

    @Test
    public void pathKnightNoPath() {
        // Start at the top left corner
        int currX = topLeftX;
        int currY = topLeftY;

        // Set walls on Knight's possible moves
        info.setWallAt(currX+1, currY+2);
        info.setWallAt(currX+2, currY+1);

        // Set pathfinder to this board
        pathfinder.initFloorBoard();
        pathfinder.printFloorBoard();

        // Try to reach bot right corner
        int destX = info.getCols()-2;
        int destY = info.getRows()-2;

        // Maximum number of moves
        int max = info.getArea();

        for(int i=0; i<max && !targetFound(currX,currY,destX,destY); i++) {
            Node nextMove = pathfinder.getNextMoveKnightTo(currX, currY, destX, destY);
            currX = nextMove.getX();
            currY = nextMove.getY();
        }

        assertNotEquals(destX, currX);
        assertNotEquals(destY, currY);
    }
}
