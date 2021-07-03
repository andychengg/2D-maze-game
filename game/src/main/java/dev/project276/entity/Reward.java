package dev.project276.entity;

/**
 * An abstract Entity representing Rewards, collectible items in the game.
 * Rewards give points and can be used by the Player when collected.
 */
public abstract class Reward extends Entity{
    public enum RewardType { sword, key, ladder, coin }

    protected RewardType rType;

    protected int BONUS;

    protected Reward() {
        init();
    }

    public Reward(int x, int y) {
        super(x,y);
        init();
    }

    public RewardType getRewardType() {
        return rType;
    }

    private void init() {
        type = EntityType.reward;
    }

    public int getBonus() {
        return BONUS;
    }

}
