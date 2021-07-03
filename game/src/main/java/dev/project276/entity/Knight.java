package dev.project276.entity;

/**
 * Enemy class that moves in an L-pattern,
 * like a knight in chess.
 */
public class Knight extends Enemy{
    public Knight(int x, int y, Pathfinder pathfinder) {
        super(x,y, pathfinder);
        init();
    }

    @Override
    public void init() {
        super.init();
        this.enemyType = EnemyType.knight;
        this.setMoveDelay(3);
        this.setHealth(1);
    }

    /**
     * Updates the Enemy, including movement.
     */
    @Override
    public void update(){
        moveCounter++;
        if(moveCounter > moveDelay) {
            moveCounter = 0;

            Node nextMove = pathfinder.getNextMoveKnightToPlayer(this);
            int nextX = nextMove.getX();
            int nextY = nextMove.getY();
            pathfinder.updateEntityBoard(x, y, nextX, nextY);
            setX(nextX);
            setY(nextY);
        }
    }
}
