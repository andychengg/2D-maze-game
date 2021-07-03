package dev.project276.entity;

import dev.project276.main.GameInfo;

import java.awt.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Computes the walkable space of the gameBoard, marks points where Entities reside,
 * and handles the pathfinding for Enemies.
 */
public class Pathfinder {
    private Entity[][] gameBoard;
    ArrayList<Entity> entityList;

    private boolean[][] floorBoard;
    private boolean[][] entityBoard;
    private boolean[][] inspectedBoard;

    int boardCols;
    int boardRows;

    private Entity player;

    public Pathfinder(Entity[][] gameBoard, ArrayList<Entity> entityList, Entity player) {
        this.gameBoard = gameBoard;
        this.boardRows = gameBoard[0].length;
        this.boardCols = gameBoard.length;

        this.entityList = entityList;

        this.player = player;

        init();
    }

    public Pathfinder(GameInfo gameInfo, Entity player) {
        this.gameBoard = gameInfo.getGameBoard();
        this.boardRows = gameBoard[0].length;
        this.boardCols = gameBoard.length;

        this.entityList = gameInfo.getEntityList();

        this.player = player;

        init();
    }

    public Pathfinder(GameInfo gameInfo) {
        this.gameBoard = gameInfo.getGameBoard();
        this.boardRows = gameBoard[0].length;
        this.boardCols = gameBoard.length;

        this.entityList = gameInfo.getEntityList();

        init();
    }

    private void init() {
        // by default, all values are false in a boolean array
        floorBoard = new boolean[boardCols][boardRows];
        initFloorBoard();

        entityBoard = new boolean[boardCols][boardRows];
        initEntityBoard();

        inspectedBoard = new boolean[boardCols][boardRows];
    }

    /**
     * Initializes our 2D boolean array floorBoard, representing walkable (floors) and non-walkable (obstacle) tiles on the gameBoard.
     * Indexes containing true are a walkable tiles, false are obstacles.
     * The floorBoard does not usually change after initialization.
     */
    public void initFloorBoard() {
        for (int i = 0; i < boardCols; i++){
            for (int j = 0; j < boardRows; j++){
                //System.out.println("i: " + i + "; j: " + j);

                Entity currEntity = gameBoard[i][j];
                switch(currEntity.getEntityType()) {
                    case floor: floorBoard[i][j] = true; break;
                    case trap:
                    default: floorBoard[i][j] = false;
                }
            }
        }
    }

    /**
     * Prints the floorBoard to console.
     */
    public void printFloorBoard() {
        for(int i = 0; i < boardRows; i++) {
            for(int j = 0; j < boardCols; j++) {
                // Note the print loop is different, (i < boardCols) whereas usually is it (i < boardRows)
                if(floorBoard[j][i]) {
                    System.out.print("  ");
                }
                else {
                    System.out.print(" W");
                }
            }
            System.out.println();
        }
    }

    /**
     * Initializes our 2D boolean array entityBoard, representing the locations of all MovingEntities on the board.
     * true marks a MovingEntity's location, false marks anything else.
     * The entityBoard can change after initialization.
     */
    private void initEntityBoard() {
        for(int i = 1; i < entityList.size(); i++) {
            if (entityList.get(i).getEntityType() == Entity.EntityType.closedDoor){
                System.out.println("good");
            }
            // Obtain MovingEntity's location
            int x = entityList.get(i).getX();
            int y = entityList.get(i).getY();

            // Mark location on the board
            entityBoard[x][y] = true;
        }
    }

    public void updateEntityBoard(int prevX, int prevY, int newX, int newY) {
        entityBoard[prevX][prevY] = false;
        entityBoard[newX][newY] = true;
    }

    private void updateEntityBoardWithList(){
        for (Entity entity: entityList){
            if (entity.getEntityType() != Entity.EntityType.enemy && entity.getEntityType() != Entity.EntityType.player){
                int x = entity.getX();
                int y = entity.getY();
                switch (entity.getEntityType()){
                    case closedDoor:
                        entityBoard[x][y] = true;
                        break;
                    case openDoor:
                        entityBoard[x][y] = false;
                        break;
                }
            }
        }
    }

    /**
     * Resets instantiates the inspectedBoard values to be equal to floorBoard values.
     * false means that space has not been inspected.
     */
    private void resetInspectedBoard() {
        for(int i = 0; i < boardCols; i++) {
            for(int j = 0; j < boardRows; j++) {
                inspectedBoard[i][j] = false;
            }
        }
    }

    /**
     * Obtain the Manhattan distance between the 2 points (startX,startY), (destX, destY)
     */
    public int getDistanceBetween(int startX, int startY, int destX, int destY) {
        return Math.abs(startX - destX) + Math.abs(startY - destY);
    }

    /**
     * Obtain the Manhattan distance between the 2 Nodes
     */
    public int getDistanceBetween(Node n1, Node n2) {
        return Math.abs(n1.getX() - n2.getX()) + Math.abs(n1.getY() - n2.getY());
    }

    /**
     * Obtain the Manhattan distance from the given Node to the player.
     * Returns -1 if player does not exist.
     */
    public int getDistanceToPlayer(Node node) {
        return player != null ? Math.abs(node.getX() - player.getX()) + Math.abs(node.getY() - player.getY()) : -1;
    }

    /**
     * A* search algorithm to get the next move from (sourceX, sourceY) to (destX, destY).
     */
    public Node getNextMoveTo(int sourceX, int sourceY, int destX, int destY) {
        updateEntityBoardWithList();

        // If we're already at the target, do nothing
        if(sourceX == destX && sourceY == destY) {
            //System.out.println("\tAlready at target");
            return new Node(sourceX, sourceY);
        }

        // Comparator for comparing the distances of 2 Nodes to the end position
        NodeComparator comparator = new NodeComparator();
        // Nodes to be inspected
        PriorityQueue<Node> inspectQueue = new PriorityQueue<>(comparator);

        // Reset our inspectedBoard, 2D boolean array of inspected spaces
        resetInspectedBoard();

        // Set up our starting node
        Node startNode = new Node(sourceX, sourceY);

        // Set our startNode's distance from itself to be 0
        startNode.setDistance(0);
        startNode.setHeuristic(0);
        startNode.setTotalDistance();
        inspectQueue.offer(startNode);

        // Node to inspect next
        Node currNode;

        //System.out.println("\tinspectQueue.isEmpty(): " + inspectQueue.isEmpty());

        // Loop until we isEmpty() == true (no path) or until we find the target
        while(!inspectQueue.isEmpty()) {
            currNode = inspectQueue.poll();

            int currX = currNode.getX();
            int currY = currNode.getY();

            // check if target is reached
            if(currX == destX && currY == destY && withinBounds(currNode)) {
                //System.out.println("Found target");
                return getMove(currNode, startNode);
            }


            // add the neighbors of currNode to be inspected
            Node currNeighbor = makeNeighbor(currX+1, currY, currNode, inspectQueue); // Right
            if(currNeighbor != null) {
                inspectQueue.add(currNeighbor);
            }

            currNeighbor = makeNeighbor(currX-1, currY, currNode, inspectQueue);   // Left
            if(currNeighbor != null) { inspectQueue.add(currNeighbor); }

            currNeighbor = makeNeighbor(currX, currY+1, currNode, inspectQueue);   // Lower
            if(currNeighbor != null) { inspectQueue.add(currNeighbor); }

            currNeighbor = makeNeighbor(currX, currY-1, currNode, inspectQueue);   // Upper
            if(currNeighbor != null) { inspectQueue.add(currNeighbor); }

            // Mark this node as inspected
            inspectedBoard[currX][currY] = true;

            //System.out.println("\tinspectQueue size: " + inspectQueue.size());

        }

        // No path found, do nothing
        //System.out.println("\tNo path to target");
        return new Node(sourceX, sourceY);
    }

    public Node getNextMoveTo(Entity source, Entity dest) {
        return getNextMoveTo(source.getX(), source.getY(), dest.getX(), dest.getX());
    }

    public Node getNextMoveTo(int startX, int startY, Entity dest) {
        return getNextMoveTo(startX, startY, dest.getX(), dest.getX());
    }

    /**
     * A* search algorithm to get the next move to (destX, destY).
     * @param source the Entity whose position we're starting from
     */
    public Node getNextMoveTo(Entity source, int destX, int destY) {
        return getNextMoveTo(source.getX(), source.getY(), destX, destY);
    }

    /**
     * A* search algorithm to get the next move to player.
     * @param source the Entity whose position we're starting from
     */
    public Node getNextMoveToPlayer(Entity source) {
        return getNextMoveToPlayer(source.getX(), source.getY());
    }

    public Node getNextMoveToPlayer(int sourceX, int sourceY) {
        if(player != null)
            return getNextMoveTo(sourceX, sourceY, player.getX(), player.getY());
        else
            return new Node(sourceX,sourceY);
    }

    /**
     * Function to handle the next move for Knight enemies.
     * A* search algorithm to get the next move from (sourceX, sourceY) to (destX, destY) with chess Knight moves.
     */
    public Node getNextMoveKnightTo(int sourceX, int sourceY, int destX, int destY) {
        updateEntityBoardWithList();

        // If we're already at the target, do nothing
        if(sourceX == destX && sourceY == destY) {
            //System.out.println("\tAlready at target");
            return new Node(sourceX, sourceY);
        }

        // Comparator for comparing the distances of 2 Nodes to the end position
        NodeComparator comparator = new NodeComparator();
        // Nodes to be inspected
        PriorityQueue<Node> inspectQueue = new PriorityQueue<>(comparator);

        // Reset our inspectedBoard, 2D boolean array of inspected spaces
        resetInspectedBoard();

        // Set up our starting node
        Node startNode = new Node(sourceX, sourceY);

        // Set our startNode's distance from itself to be 0
        startNode.setDistance(0);
        startNode.setHeuristic(0);
        startNode.setTotalDistance();
        inspectQueue.offer(startNode);

        // Node to inspect next
        Node currNode;

        //System.out.println("\tinspectQueue.isEmpty(): " + inspectQueue.isEmpty());

        Point[] knightMoves = {
                new Point(1, 2),
                new Point(1, -2),
                new Point(2, 1),
                new Point(2, -1),
                new Point(-1, 2),
                new Point(-1, -2),
                new Point(-2, 1),
                new Point(-2, -1)
        };

        // Loop until we isEmpty() == true (no path) or until we find the target
        while(!inspectQueue.isEmpty()) {
            currNode = inspectQueue.poll();

            int currX = currNode.getX();
            int currY = currNode.getY();

            // check if target is reached
            if(currX == destX && currY == destY && withinBounds(currNode)) {
                //System.out.println("Found target");
                return getMove(currNode, startNode);
            }


            // add the neighbors of currNode to be inspected
            for (Point move: knightMoves){
                Node currNeighbor = makeNeighbor(currX+move.x, currY+move.y, currNode, inspectQueue);
                if (currNeighbor != null){
                    inspectQueue.add(currNeighbor);
                }
            }

            // Mark this node as inspected
            inspectedBoard[currX][currY] = true;

            //System.out.println("\tinspectQueue size: " + inspectQueue.size());

        }

        // No path found, do nothing
        //System.out.println("\tNo path to target");
        return new Node(sourceX, sourceY);
    }

    /**
     * Function to handle the next move for Knight enemies.
     * A* search algorithm to get the next move to player with chess Knight moves.
     * @param source the Entity whose position we're starting from
     */
    public Node getNextMoveKnightToPlayer(Entity source) {
        if(player != null)
            return getNextMoveKnightTo(source.getX(), source.getY(), player.getX(), player.getY());
        else
            return new Node(source.getX(),source.getY());
    }

    /**
     * Function to handle the next move for Patrol enemies.
     * Uses A* search algorithm to get the next move to player, if they are within line-of-sight.
     * @param source the Entity whose position we're starting from
     */
    public Node getNextMovePatrol(Entity source) {
        Patrol patrol = (Patrol)source;
        ArrayList<Point> line = GameInfo.getLineBetweenTwoPoints(new Point(source.getX(), source.getY()),
                new Point(player.getX(), player.getY()));
        boolean canSeePlayer = true;
        for (Point p: line) {
            if (gameBoard[p.x][p.y].getEntityType() == Entity.EntityType.wall) {
                canSeePlayer = false;
                break;
            }
        }
        if (canSeePlayer){
            patrol.setEnemyType(Enemy.EnemyType.patrol);
            return getNextMoveToPlayer(source);
        }
        else{
            patrol.setEnemyType(Enemy.EnemyType.patrol_idle);
            // surrounded by walls
            Node left = withinBounds(new Node(source.getX()-1, source.getY())) ? new Node(source.getX()-1, source.getY()) : null;
            Node right = withinBounds(new Node(source.getX()+1, source.getY())) ? new Node(source.getX()+1, source.getY()) : null;
            if (patrol.getMovingLeft()){
                if (left == null || gameBoard[left.getX()][left.getY()].getEntityType() == Entity.EntityType.wall){
                    patrol.changeDirection();
                    return right;
                }
                else{
                    return left;
                }
            }
            else{
                if (right == null || gameBoard[right.getX()][right.getY()].getEntityType() == Entity.EntityType.wall){
                    patrol.changeDirection();
                    return left;
                }
                else{
                    return right;
                }
            }
        }
    }

    private Node makeNeighbor(int x, int y, Node parent, PriorityQueue<Node> inspectQueue) {
        Node currNeighbor = new Node(x, y);

        // Check if we've already added this node to our inspectQueue
        if(inspectQueue.contains(currNeighbor)) {
            //System.out.println("\tinspectQueue contains currNeighbor");
            return null;
        }

        // Bounds check
        if(!withinBounds(currNeighbor)) {
            //System.out.println("\tcurrNeighbor is out of bounds");
            return null;
        }

        // Conditions: If it has not been inspected, and it is walkable, and no entity exists there, create it
        //System.out.println("\tinspectedBoard: " + !inspectedBoard[x][y]);
        //System.out.println("\tfloorBoard: " + floorBoard[x][y]);
        //System.out.println("\tentityBoard: " + !entityBoard[x][y]);
        if(!inspectedBoard[x][y] && floorBoard[x][y] && !entityBoard[x][y]) {
            currNeighbor.setParent(parent);
            currNeighbor.setDistance(parent.getDistance() + getDistanceBetween(parent, currNeighbor));
            currNeighbor.setHeuristic(getDistanceToPlayer(currNeighbor));
            currNeighbor.setTotalDistance();

            return currNeighbor;
        }

        //System.out.println("\tdid not make neighbor");
        return null;
    }

    private Node getMove(Node currNode, Node startNode) {
        Node currParent = currNode.getParent();
        Node nextMove = currNode;
        // While we have not reached the start, traverse our path backwards
        while(!currParent.equals(startNode)) {
            nextMove = currParent;
            currParent = nextMove.getParent();
        }
        return nextMove;
    }

    private boolean withinBounds(Node node) {
        return (node.getX() > 0 && node.getX() < boardCols && node.getY() > 0 && node.getY() < boardRows);
    }

}
