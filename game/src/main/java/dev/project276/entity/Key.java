package dev.project276.entity;

/**
 * The Key entity.
 * Used to unlock doors.
 * */

public class Key extends Reward {
    private static int keyCount = 0;

    protected Key() {
        init();
    }

    public Key(int x, int y) {
        super(x,y);
        init();
        keyCount++;
    }

    private void init() {
        type = EntityType.reward;
        rType = RewardType.key;
        BONUS = 1;
    }

    /**
     * Method to keep track of number of keys in maze.
     * Used to compare with player's number of keys
     * @return number of keys created
     */
    public static int getKeyCount() {
        return keyCount;
    }
}
