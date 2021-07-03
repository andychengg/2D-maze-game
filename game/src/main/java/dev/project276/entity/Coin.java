package dev.project276.entity;

/**
 * The Coin entity.
 * A bonus that extends Reward. Acts as the optional collectable in the game.
 */
public class Coin extends Reward {

    private double scoreMultiplier = 2.0;

    public Coin(int x, int y) {
        super(x, y);
        init();
    }

    private void init(){
        BONUS = 1;
        type = EntityType.reward;
        rType = RewardType.coin;
    }

    public double getScoreMultiplier() {
        return scoreMultiplier;
    }

}
