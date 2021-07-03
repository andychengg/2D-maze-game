package dev.project276.entity;

/**
 * The Ladder class.
 * Is a essentially a 'key', but only works on the Exit (the hole).
 */
public class Ladder extends Reward {
    private static int ladderCount = 0;
    public Ladder(int x, int y){
        super(x, y);
        init();
        ladderCount++;
    }
    private void init(){
        BONUS = 3;
        type = EntityType.reward;
        rType = RewardType.ladder;
    }

    public static int getLadderCount(){
        return ladderCount;
    }
}
