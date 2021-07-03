package dev.project276.main;

import dev.project276.entity.Entity;
import dev.project276.entity.Floor;
import dev.project276.entity.Wall;

import java.awt.*;
import java.util.*;

/**
 * Class that generates a random maze given rows, cols,
 * and optionally the starting point to generate the maze.
 * Usually use the MazeGenerator.createMaze() to obtain
 * a maze of type Entity[][].
 * Could be changed to give player and enemies more room;
 * right now each passage is just one tile wide.
 */
public class MazeGenerator {
    private Entity[][] theMaze;
    private int rows;
    private int cols;
    private Point initial;

    public MazeGenerator(int r, int c){
        theMaze = new Entity[r][c];
        initTheMaze();
        rows = r;
        cols = c;
        initial = new Point(0, 0);
    }
    public MazeGenerator(int r, int c, Point initialPoint) {
        theMaze = new Entity[r][c];
        initTheMaze();
        rows = r;
        cols = c;
        initial = initialPoint;
    }

    private void initTheMaze(){
        for (int row = 0; row < theMaze.length; row++){
            for (int col = 0; col < theMaze[0].length; col++){
                theMaze[row][col] = new Wall(col, row);
            }
        }
    }

    public void setTheMazeToFloor(Point pointToSet){
        int x = pointToSet.x;
        int y = pointToSet.y;
        theMaze[y][x] = new Floor(x, y);
    }

    /**
     * Creates the maze.
     * On failure, probably just returns a maze filled with walls.
     * A way to call it is: <br>
     * MazeGenerator generator = new MazeGenerator(gameBoardRows, gameBoardCols, player.x, player.y); <br>
     * Entity[][] gameBoard = generator.createMaze();
     * @return A 2-D array with entities set to floors.
     * For some reason two sides are always walls.
     */
    public Entity[][] createMaze(){
        HashSet<Point> visited = new HashSet<>();
        Stack<Point> toBacktrack = new Stack<>();
        visited.add(initial);
        toBacktrack.push(initial);
        setTheMazeToFloor(initial);
        while (!toBacktrack.isEmpty()){
            Point current = toBacktrack.pop();
            HashMap<Point, Point> nonVisitedNeighbours = getNonVisitedNeighbours(current, visited);
            if (nonVisitedNeighbours.size() > 0){
                toBacktrack.push(current);
                Point[] randomUnvisitedNeighbourAndInBetween = getRandomPointsFromHashMap(nonVisitedNeighbours);
                Point randomUnvisitedNeighbour = randomUnvisitedNeighbourAndInBetween[0];
                Point randomUnvisitedNeighboursInBetweenPoint = randomUnvisitedNeighbourAndInBetween[1];
                setTheMazeToFloor(randomUnvisitedNeighbour);
                setTheMazeToFloor(randomUnvisitedNeighboursInBetweenPoint);
                visited.add(randomUnvisitedNeighbour);
                toBacktrack.push(randomUnvisitedNeighbour);
            }
        }
        return theMaze;
    }

    private Point[] getRandomPointsFromHashMap(HashMap<Point, Point> hashMap){
        ArrayList<Point> keyList = new ArrayList<>(hashMap.keySet());
        Point randKey = keyList.get(new Random().nextInt(keyList.size()));
        return new Point[]{randKey, hashMap.get(randKey)};
    }

    private HashMap<Point, Point> getNonVisitedNeighbours(Point source, HashSet<Point> visited){
        int sourceX = source.x;
        int sourceY = source.y;
        int gridMaxX = cols;
        int gridMaxY = rows;
        int gridMinX = 0;
        int gridMinY = 0;
        int left = sourceX-2;
        int right = sourceX+2;
        int top = sourceY-2;
        int bottom = sourceY+2;
        Point leftPoint = new Point(left, sourceY);
        Point rightPoint = new Point(right, sourceY);
        Point topPoint = new Point(sourceX, top);
        Point bottomPoint = new Point(sourceX, bottom);
        Point leftPointInBetween = new Point(left+1, sourceY);
        Point rightPointInBetween = new Point(right-1, sourceY);
        Point topPointInBetween = new Point(sourceX, top+1);
        Point bottomPointInBetween = new Point(sourceX, bottom-1);

        HashMap<Point, Point> result = new HashMap<>();
        /*
          Check left, right, top, and bottom neighbours of source
          to see if they are not visited (added to returned list)
          or visited (not added)
         */
        if (left >= gridMinX){
            if (!visited.contains(leftPoint)){
                result.put(leftPoint, leftPointInBetween);
            }
        }
        if (right < gridMaxX){
            if (!visited.contains(rightPoint)){
                result.put(rightPoint, rightPointInBetween);
            }
        }
        if (top >= gridMinY){
            if (!visited.contains(topPoint)){
                result.put(topPoint, topPointInBetween);
            }
        }
        if (bottom < gridMaxY){
            if (!visited.contains(bottomPoint)){
                result.put(bottomPoint, bottomPointInBetween);
            }
        }
        return result;
    }

    /**
     * Should be run after invoking MazeGenerator.createMaze().
     * Creates a maze with the tiles 2x the size.
     * @return An Entity[][] that is 2x the width and height of the old one.
     * Each tile of the old gameBoard is effectively turned into a 2x2
     * tile on the returned one.
     */
    public Entity[][] expandGameBoard(){
        rows *= 2;
        cols *= 2;
        return expandGameBoard(theMaze);
    }

    public Entity[][] expandGameBoard(Entity[][] gameBoard){
        int nRows = gameBoard.length; int nCols = gameBoard[0].length;
        Entity[][] result = new Entity[nRows*2][nCols*2];
        for (int oldRowInd = 0; oldRowInd < nRows; oldRowInd++){
            for (int oldColInd = 0; oldColInd < nCols; oldColInd++){
                for (int newRowInd = oldRowInd*2; newRowInd < oldRowInd*2+2; newRowInd++){
                    for (int newColInd = oldColInd*2; newColInd < oldColInd*2+2; newColInd++){
                        result[newRowInd][newColInd] = gameBoard[oldRowInd][oldColInd];
                    }
                }

            }
        }
        return result;
    }

}


