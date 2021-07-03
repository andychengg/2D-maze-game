package dev.project276.entity;

/**
 * The Trap entity.
 * A stationary obstacle for the Player.
 * On contact, damages the Player, reduces their score, and disappears.
 */
public class Trap extends Entity{
    private int damage = 1;
    private int penalty = -1;

    public Trap (int x, int y){
        super(x, y);
        init();
    }

    private void init(){
        type = EntityType.trap;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }
}
