package dev.project276.main;

import java.awt.*;
import java.util.*;

import dev.project276.entity.*;

/**
 * Utility class that holds vital info about the game,
 * such as the game board and the entity list, with utility functions to access/modify them.
 * Essentially, represents each level of the game.
 */
public class GameInfo {
    protected Entity[][] gameBoard;
    protected int gameBoardRows;
    protected int gameBoardCols;

    protected ArrayList<Entity> entityList;

    /**
     * Default constructor.
     * Game board is a maze with rows 15, cols = 20, walls all around it.
     */
    public GameInfo(){
        this.gameBoardRows = 15;
        this.gameBoardCols = 20;
        //this.gameBoard = (new MazeGenerator(numberOfRows, numberOfCols)).createMaze();
        this.gameBoard = new Entity[gameBoardCols][gameBoardRows];
        for(int i = 0; i < gameBoardRows; ++i) {
            for(int j=0; j < gameBoardCols; ++j) {
                if(i==0 || i == gameBoardRows-1 || j == 0 || j == gameBoardCols-1) {
                    gameBoard[j][i] = new Wall(j,i);
                }
                else {
                    gameBoard[j][i] = new Floor(j,i);
                }
            }
        }
        this.entityList = new ArrayList<>();

        initLevel();
    }

    /**
     * Constructor that takes in a user-defined game board.
     * @param gb Entity[][] that represents the gameboard.
     */
    public GameInfo(Entity[][] gb) {
        this.gameBoard = gb;
        gameBoardRows = this.gameBoard[0].length;
        gameBoardCols = this.gameBoard.length;
        this.entityList = new ArrayList<>();

        initLevel();
    }

    /**
     * Constructor that takes in a user-defined game board
     * and an list of pre-defined entities
     * @param gb Entity[][] that represents the gameboard.
     * @param entityArrayList ArrayList that contains the (non-floor/non-wall) entities.
     */
    public GameInfo(Entity[][] gb, ArrayList<Entity> entityArrayList){
        this.gameBoard = gb;
        gameBoardRows = this.gameBoard[0].length;
        gameBoardCols = this.gameBoard.length;
        this.entityList = entityArrayList;

        initLevel();
    }

    /**
     * Constructor that takes in rows and cols
     * and generates a random maze of size [rows][cols].
     * @param nRows Rows of the maze.
     * @param nCols Columns of the maze.
     */
    public GameInfo(int nRows, int nCols){
        this.gameBoardRows = nRows;
        this.gameBoardCols = nCols;
        this.gameBoard = (new MazeGenerator(gameBoardCols, gameBoardRows)).createMaze();
        this.entityList = new ArrayList<>();

        initLevel();
    }

    /**
     * Constructor that takes in rows, cols, and a list of pre-defined entities.
     * Generates a random maze of size [rows][cols].
     * @param nRows Rows of the maze.
     * @param nCols Columns of the maze.
     * @param entityArrayList ArrayList that contains the (non-floor/non-wall) entities.
     */
    public GameInfo(int nRows, int nCols, ArrayList<Entity> entityArrayList){
        this.gameBoardRows = nRows;
        this.gameBoardCols = nCols;
        this.gameBoard = (new MazeGenerator(gameBoardCols, gameBoardRows)).createMaze();
        this.entityList = entityArrayList;

        initLevel();
    }

    // Method be replaced by sub-classes
    // Return a new instance of this level to reset it
    public GameInfo restartLevel() {
        return new GameInfo();
    }

    public void initLevel() {}

    /**
     * Checks if the coordinates are valid (ie, won't cause an index error).
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return true if the coordinates are valid. false otherwise.
     */
    public boolean checkBoardBounds(int x, int y){
        return x >= 0 && x < gameBoardCols && y >= 0 && y < gameBoardRows;
    }

    public Entity[][] getGameBoard(){
        return gameBoard;
    }

    public int getRows() {
        return gameBoardRows;
    }

    public int getCols() {
        return gameBoardCols;
    }

    public int getArea() { return gameBoardRows * gameBoardCols;}

    public ArrayList<Entity> getEntityList() {
        return entityList;
    }

    /**
     * Checks if the given entity is in the list of entities.
     * @param entity The entity to check.
     * @return true if the entity is in the list. false otherwise.
     */
    public boolean hasEntity(Entity entity){
        return entityList.contains(entity);
    }

    public boolean hasEntityType(Entity.EntityType type){
        for (Entity entity: this.entityList){
            if (entity.getEntityType() == type){
                return true;
            }
        }
        return false;
    }

    public void addEntity(Entity entity){
        if (entity != null){
            this.entityList.add(entity);
        }
    }

    public void removeEntity(Entity entity){
        this.entityList.remove(entity);
    }

    /**
     * Gets the given wall/floor at (x, y).
     * @param x The x-coordinate of the entity.
     * @param y The y-coordinate of the entity.
     * @return If the coordinates are valid, the entity at that point.
     * Otherwise, returns null.
     */
    public Entity getEntityAt(int x, int y){
        if (checkBoardBounds(x, y)){
            return gameBoard[x][y];
        }
        return null;
    }

    /**
     * Gets the entity in the Entity list at (x, y),
     * returns null if there isn't one.
     * Returns the first occurrence of an entity.
     * @param x The x-coordinate of the entity.
     * @param y The y-coordinate of the entity.
     * @return If the coordinates are valid and
     * there is an entity in the list with coordinates
     * of (x, y), then the entity at that point.
     * Otherwise, returns null.
     */
    public Entity getListEntityAt(int x, int y){
        if (checkBoardBounds(x, y)){
            for (Entity entity: entityList){
                if (entity.getX() == x && entity.getY() == y){
                    return entity;
                }
            }
        }
        return null;
    }

    /**
     * Gets all the entities in the Entity list with coords (x, y).
     * Returns an empty ArrayList if there isn't any.
     * @param x The x-coordinate of the entity(ies).
     * @param y The y-coordinate of the entity(ies).
     * @return If the coordinates are valid and
     * there is an entity in the list with coordinates
     * of (x, y), then the entities at that point.
     * Otherwise, returns an empty ArrayList.
     */
    public ArrayList<Entity> getAllListEntitiesAt(int x, int y){
        ArrayList<Entity> result = new ArrayList<>();
        if (checkBoardBounds(x, y)){
            for (Entity entity: entityList){
                if (entity.getX() == x && entity.getY() == y){
                    result.add(entity);
                }
            }
        }
        return result;
    }

    /**
     * Sets a point on the gameBoard to a Wall.
     * @param x The x-coordinate of the new wall.
     * @param y The y-coordinate of the new wall.
     */
    public void setWallAt(int x, int y){
        if (checkBoardBounds(x, y)){
            this.gameBoard[x][y] = new Wall(x, y);
        }
    }

    /**
     * Sets a point on the gameBoard to a Floor.
     * @param x The x-coordinate of the new floor.
     * @param y The y-coordinate of the new floor.
     */
    public void setFloorAt(int x, int y){
        if (checkBoardBounds(x, y)){
            this.gameBoard[x][y] = new Floor(x, y);
        }
    }

    /**
     * Adds walls along the line between (startX,startY) and (endX,endY)
     * to the Gameboard.
     */
    public void setManyWallsAlong(int startX, int startY, int endX, int endY) {
        Point start = new Point(startX, startY);
        Point end = new Point(endX, endY);
        ArrayList<Point> walls = getLineBetweenTwoPoints(start, end);
        setManyWallsAt(walls);
    }

    /**
     * Adds all walls indicated by their points in the ArrayList
     * to the Gameboard.
     * @param points The List of points to set to walls.
     */
    public void setManyWallsAt(ArrayList<Point> points){
        for (Point p: points){
            setWallAt(p.x, p.y);
        }
    }

    /**
     * Expands the game board in accordance to
     * MazeGenerator.expandGameBoard.
     * Also multiplies rows and cols by 2.
     */
    public void expandGameBoard(){
        gameBoardRows *= 2;
        gameBoardCols *= 2;
        gameBoard = (new MazeGenerator(gameBoardRows, gameBoardCols)).expandGameBoard(gameBoard);
    }

    /**
     * Searches like this (x = targets to compare to, o = already compared, t = target)
     * Search Depth 1:       Search Depth 2:
     *   x                         x
     * x t x                     x o x
     *   x                     x o t o x
     *                           x o x
     *                             x
     * @param source The Point to find the closest reachable point from.
     * @return The closest reachable point.
     */
    public Point findClosestReachablePoint(Point source){
        int LIMIT = gameBoardCols * gameBoardRows;
        int iterations = 0;
        Point result = source;
        Point current = source;
        // Queue of neighbours to check, the Points closest to the start (index 0)
        // are the Points closest to source
        LinkedList<Point> sourceNeighbours = getAnyNeighbours(current);

        // Set of visited nodes, don't check these again if we have already visited them
        HashSet<Point> visited = new HashSet<>();

        // loop until the result point is a different one from the one we started with,
        // we are under the limit for loops,
        // and the Queue of neighbours is non-empty.
        while (result.equals(source) && iterations < LIMIT && sourceNeighbours.size() > 0){
            // The point to analyze.
            current = sourceNeighbours.get(0);
            // Add it to the visited points.
            visited.add(current);

            // if we can reach this point, then that is the closest reachable one.
            // Set the result to this point, which breaks us out of the while loop.
            if (isPointReachable(current.x, current.y)) {
                result = current;
            } else {
                // FIFO: remove the first element
                sourceNeighbours.removeFirst();
                // get the neighbours of the current point
                // so we can get more candidate points
                LinkedList<Point> neighbours = getAnyNeighbours(current);
                // add the point to the neighbours list if it has not
                // already been visited.
                for (Point point: neighbours){
                    if (!visited.contains(point)){
                        sourceNeighbours.addLast(point);
                        visited.add(point);
                    }
                }
            }
            iterations++;
        }
        return result;
    }

    private LinkedList<Point> getAnyNeighbours(Point source){
        int sourceX = source.x;
        int sourceY = source.y;
        int gridMaxX = gameBoardRows;
        int gridMaxY = gameBoardCols;
        int gridMinX = 0;
        int gridMinY = 0;
        int left = sourceX-1;
        int right = sourceX+1;
        int top = sourceY-1;
        int bottom = sourceY+1;

        LinkedList<Point> result = new LinkedList<>();
        /*
          Check left, right, top, and bottom neighbours or source
          to see if they are floors (added to returned list)
          or walls (not added)
         */
        if (left >= gridMinX){
            result.addLast(new Point(left, sourceY));
        }
        if (right < gridMaxX){
            result.addLast(new Point(right, sourceY));
        }
        if (top >= gridMinY){
            result.addLast(new Point(sourceX, top));
        }
        if (bottom < gridMaxY){
            result.addLast(new Point(sourceX, bottom));
        }
        return result;
    }

    /**
     * Method that determines if a point on the board is reachable.
     * A point is reachable if it is a floor on the gameBoard,
     * and no Enemies or Players are on it.
     * @param x The x coordinate of the point to check.
     * @param y The y coordinate of the point to check.
     * @return true if the above conditions are met. false otherwise.
     */
    public boolean isPointReachable(int x, int y){
        if (getEntityAt(x, y) == null || getEntityAt(x, y).getEntityType() != Entity.EntityType.floor){
            return false;
        }
        for (Entity entity: entityList){
            if (entity.getX() == x && entity.getY() == y){
                switch (entity.getEntityType()){
                    case enemy:
                    case player:
                    case closedDoor:
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns the Player from the Entity List.
     * @return The Player if there is one.
     * Returns null if there isn't.
     */
    public Player getPlayer() {
        for (Entity entity : entityList) {
            if (entity.getEntityType() == Entity.EntityType.player) {
                return (Player) entity;
            }
        }
        return null;
    }

    /**
     * Utility method that computes the best-fit line between two Points.
     * The line is represented by an ArrayList of Points.
     * @param p1 The first endpoint of the line.
     * @param p2 The second endpoint of the line.
     * @return An ArrayList of Points representing the line, includes both endpoints.
     */
    public static ArrayList<Point> getLineBetweenTwoPoints(Point p1, Point p2){
        ArrayList<Point> result = new ArrayList<>();
        if (p1.equals(p2)){
            result.add(p1);
            return result;
        }
        Point leftpt, rightpt;
        if (p1.x < p2.x){
            leftpt = p1;
            rightpt = p2;
        }
        else{
            rightpt = p1;
            leftpt = p2;
        }
        int dx = rightpt.x - leftpt.x;
        int dy = rightpt.y - leftpt.y;
        float m;

        if (Math.abs(dy) > Math.abs(dx)){
            m = (float)dx/dy;
            int sign = leftpt.y < rightpt.y ? 1 : -1;
            for (int i = 0; i < Math.abs(dy)+1; i++){
                result.add(new Point((int)(leftpt.x+i*Math.abs(m)), leftpt.y+i*sign));
            }
        }
        else{
            m = (float)dy/dx;
            for (int i = 0; i < dx+1; i++){
                result.add(new Point(leftpt.x+i, (int)(leftpt.y+i*m)));
            }
        }
        return result;
    }

}
