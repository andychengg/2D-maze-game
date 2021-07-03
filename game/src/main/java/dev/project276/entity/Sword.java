package dev.project276.entity;

/**
 * The Sword entity. Acts as an optional item for the player to collect.
 * Once collected, the Player is able to attack Enemies.
 */
public class Sword extends Reward {
    private int damage = 1;

    protected Sword() {
        init();
    }

    public Sword(int x, int y) {
        super(x,y);
        init();
    }

    private void init() {
        type = EntityType.reward;
        rType = RewardType.sword;
        BONUS = 2;
    }

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }


}
