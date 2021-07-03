package dev.project276.entity;

import static dev.project276.entity.Entity.EntityType.enemy;

/**
 * The Enemy entity. Enemies chase after the Player.
 * On contact, they deal damage.
 */
public class Enemy extends MovingEntity {

    protected EnemyType enemyType;

    public enum EnemyType {normal, knight, patrol_idle, patrol}

    protected Pathfinder pathfinder;
    protected int moveCounter = 0;
    protected int moveDelay = 1;

    public Enemy(int x, int y, Pathfinder pathfinder) {
        super(x,y);
        this.pathfinder = pathfinder;
        init();
    }

    /**
     * Updates the Enemy, including movement.
     */
    public void update(){
        moveCounter++;
        //System.out.println("Enemy: " + getX() + ", " + getY());

        // Move once we'ved delayed long enough
        if(moveCounter > moveDelay) {
            moveCounter = 0;

            // obtain the next move
            Node nextMove = pathfinder.getNextMoveToPlayer(this);
            int nextX = nextMove.getX();
            int nextY = nextMove.getY();
            //System.out.println("\tnextX: " + nextX + "; nextY: " + nextY);

            // Update pathfinder on the new location of this Enemy
            pathfinder.updateEntityBoard(x, y, nextX, nextY);

            // move to the new location
            setX(nextX);
            setY(nextY);

            //pathfinder.printFloorBoard();
        }
    }

    public void init(){
        this.type = enemy;
        this.enemyType = EnemyType.normal;
        damage = 1;
        health = 2;
    }

    /**
     * Represents when the enemy takes damage.
     * Returns false if health <= 0, true otherwise.
     * @param damage The damage this enemy receives.
     * @return true if health after taking damage > 0, false otherwise.
     */
    public boolean takeDamage(int damage){
        health -= damage;
        return health > 0;
    }

    public void setMoveDelay(int moveDelay){
        this.moveDelay = moveDelay;
    }

    public int getMoveDelay() {
        return moveDelay;
    }

    public EnemyType getEnemyType() {
        return this.enemyType;
    }

    public void setEnemyType(EnemyType enemyType){
        this.enemyType = enemyType;
    }
}
