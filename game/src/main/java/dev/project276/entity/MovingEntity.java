package dev.project276.entity;

import dev.project276.entity.Entity;

/**
 * An abstract class that represents Entities who are able to move around the board.
 */
public abstract class MovingEntity extends Entity {
    protected int damage;
    protected int health;

    public MovingEntity(int x, int y){
        super(x, y);
    }

    public int getDamage() { return damage; }
    public int getHealth() { return health; }

    public void setDamage(int damage) { this.damage = damage; }
    public void setHealth(int health) { this.health = health; }

    /**
     * Move the MovingEntity by dx units right, dy units down.
     * (depends on board layout)
     **/
    public void move(int dx, int dy){
        setX(getX() + dx);
        setY(getY() + dy);
    }
}
