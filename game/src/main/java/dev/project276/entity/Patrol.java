package dev.project276.entity;

/**
 * Enemy class that moves towards the player only if
 * it can see the player.
 * Otherwise just moves in a line.
 */
public class Patrol extends Enemy{

    private boolean movingLeft = true;

    public Patrol(int x, int y, Pathfinder pathfinder) {
        super(x,y, pathfinder);
        init();
    }

    @Override
    public void init() {
        super.init();
        this.enemyType = EnemyType.patrol_idle;
        this.setMoveDelay(1);
    }

    /**
     * Updates the Enemy, including movement.
     */
    @Override
    public void update(){
        moveCounter++;
        if(moveCounter > moveDelay) {
            moveCounter = 0;

            Node nextMove = pathfinder.getNextMovePatrol(this);
            int nextX = nextMove.getX();
            int nextY = nextMove.getY();
            pathfinder.updateEntityBoard(x, y, nextX, nextY);
            setX(nextX);
            setY(nextY);
        }
    }

    @Override
    public void setEnemyType(EnemyType enemyType){
        if (this.enemyType != enemyType){
            this.image = null;
            this.enemyType = enemyType;
        }
    }

    public void changeDirection(){
        movingLeft = !movingLeft;
    }

    public boolean getMovingLeft(){
        return movingLeft;
    }
}
