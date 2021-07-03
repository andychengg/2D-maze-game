package dev.project276.main;

import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import dev.project276.entity.*;

/**
 * Factory class which produces Entities of different types.
 * The create[Entity] methods are private because the
 * createAndSet[Entity] methods are more useful.
 * Also contains a method to create multiple Enemies
 * around a single point.
 */
public class EntityFactory {
    GameInfo gameInfo;
    public EntityFactory(GameInfo gi){ this.gameInfo = gi; }

    /**
     * Creates a new Player at the specified coordinates,
     * adds it to gameInfo's entityList.
     * Accepts a boolean strict; if it is false, then it can
     * spawn the player in a wall.
     * @param x X coordinate of the Enemy.
     * @param y Y coordinate of the Enemy.
     * @param strict Boolean, if false then it will NOT check bounds
     *               and NOT check if it spawns in a wall.
     * @return The Player created.
     */
    public Player createAndSetPlayer(int x, int y, KeyInput keyInput, boolean strict){
        if (strict){
            return createAndSetPlayer(x, y, keyInput);
        }
        else{
            Player p = new Player(x, y, keyInput);
            gameInfo.addEntity(p);
            return p;
        }
    }

    public Player createAndSetPlayer(int x, int y, KeyInput keyInput, EntityStateHandler stateHandler){
        Player player = createPlayer(x, y, keyInput, stateHandler);
        if (player != null){
            gameInfo.addEntity(player);
        }
        return player;
    }

    public Player createAndSetPlayer(int x, int y, KeyInput keyInput){
        Player player = createPlayer(x, y, keyInput, null);
        if (player != null){
            gameInfo.addEntity(player);
        }
        return player;
    }

    public Player createAndSetPlayer(int x, int y, EntityStateHandler stateHandler){
        Player player = createPlayer(x, y, stateHandler);
        if (player != null){
            gameInfo.addEntity(player);
        }
        return player;
    }

    /**
     * Creates a new Enemy at the specified coordinates,
     * adds it to gameInfo's entityList.
     * @param x X coordinate of the Enemy.
     * @param y Y coordinate of the Enemy.
     * @return The Enemy created.
     */
    public Enemy createAndSetEnemy(int x, int y, Pathfinder pathfinder){
        Enemy enemy = createEnemy(x, y, pathfinder);
        if (enemy != null){
            gameInfo.addEntity(enemy);
        }
        //System.out.println("Failed to create enemy at " + x + ", " + y);
        return enemy;
    }
    
    /**
     * Creates a new Knight at the specified location
     * and adds to gameInfo's entityList
     * @param x X coordinate of the Knight
     * @param y Y coordinate of the Knight
     * @return The Knight created
     */
    public Enemy createAndSetKnight(int x, int y, Pathfinder pathfinder){
        Enemy enemy = createEnemy(x, y, pathfinder, Enemy.EnemyType.knight);
        if (enemy != null){
            gameInfo.addEntity(enemy);
        }
        //System.out.println("Failed to create enemy at " + x + ", " + y);
        return enemy;
    }
    
    /**
     * Creates a new Patrol at the specified location
     * and
     * @param x X coordinate of the Patrol
     * @param y Y coordinate of the Patrol
     * @return The Patrol created
     */
    public Enemy createAndSetPatrol(int x, int y, Pathfinder pathfinder){
        Enemy enemy = createEnemy(x, y, pathfinder, Enemy.EnemyType.patrol_idle);
        if (enemy != null){
            gameInfo.addEntity(enemy);
        }
        //System.out.println("Failed to create enemy at " + x + ", " + y);
        return enemy;
    }
    
    /**
     * Creates a new Sword at the specified location
     * @param x X coordinate of the Sword
     * @param y Y coordinate of the Sword
     * @return The Sword created
     */
    public Reward createAndSetSword(int x, int y){
        Reward reward = createReward(x, y, Reward.RewardType.sword);
        if (reward != null){
            gameInfo.addEntity(reward);
        }
        return reward;
    }
    
    /**
     * Creates a new Key at the specified location
     * @param x X coordinate of the Key
     * @param y Y coordinate of the Key
     * @return The Key created
     */
    public Reward createAndSetKey(int x, int y){
        Reward reward = createReward(x, y, Reward.RewardType.key);
        if (reward != null){
            gameInfo.addEntity(reward);
        }
        return reward;
    }
    
    /**
     * Creates a new Ladder at the specified location
     * @param x X coordinate of the Ladder
     * @param y Y coordinate of the Ladder
     * @return The Ladder created
     */
    public Reward createAndSetLadder(int x, int y) {
        Reward reward = createReward(x, y, Reward.RewardType.ladder);
        if (reward != null) {
            gameInfo.addEntity(reward);
        }
        return reward;
    }

    /**
     * Creates a new Coin at the specified location
     * @param x X coordinate of the Coin
     * @param y Y coordinate of the Coin
     * @return The Coin created
     */
    public Reward createAndSetCoin(int x, int y){
        Reward reward = createReward(x, y, Reward.RewardType.coin);
        if (reward != null) {
            gameInfo.addEntity(reward);
        }
        return reward;
    }
    
    /**
     * Creates a new Door at the specified location
     * @param x X coordinate of the Door
     * @param y Y coordinate of the Door
     * @return The Door created
     */
    public Door createAndSetDoor(int x, int y){
        Door door = createDoor(x, y);
        if (door != null){
            gameInfo.addEntity(door);
        }
        return door;
    }
    
    /**
     * Creates a new Trap at the specified location
     * @param x X coordinate of the Trap
     * @param y Y coordinate of the Trap
     * @return The Trap created
     */
    public Trap createAndSetTrap(int x, int y) {
        Trap trap = createTrap(x, y);
        if (trap != null) {
            gameInfo.addEntity(trap);
        }
        return trap;
    }
    
    /**
     * Create a new Exit at the specified location
     * @param x coordinate of the Exit
     * @param y coordinate of the Exit
     * @return The Exit created
     */
    public Exit createAndSetExit(int x, int y){
        Exit exit = createExit(x, y);
        if (exit != null){
            gameInfo.addEntity(exit);
        }
        return exit;
    }

    private Enemy createEnemy(int x, int y, Pathfinder pathfinder) {
        if (gameInfo.isPointReachable(x, y)){
            return new Enemy(x, y, pathfinder);
        }
        return null;
    }

    private Enemy createEnemy(int x, int y, Pathfinder pathfinder, Enemy.EnemyType enemyType){
        if (gameInfo.isPointReachable(x, y)){
            switch(enemyType){
                case normal : {
                    return new Enemy(x, y, pathfinder);
                }
                case knight : {
                    return new Knight(x, y, pathfinder);
                }
                case patrol_idle : {
                    return new Patrol(x, y, pathfinder);
                }
            }
        }
        return null;
    }

    private Player createPlayer(int x, int y, KeyInput keyInput, EntityStateHandler stateHandler){
        if (gameInfo.isPointReachable(x, y) && !gameInfo.hasEntityType(Entity.EntityType.player)){
            return new Player(x, y, keyInput, stateHandler);
        }
        return null;
    }

    private Player createPlayer(int x, int y, EntityStateHandler stateHandler){
        if (gameInfo.isPointReachable(x, y) && !gameInfo.hasEntityType(Entity.EntityType.player)){
            return new Player(x, y, stateHandler);
        }
        return null;
    }

    private Reward createReward(int x, int y, Reward.RewardType rType){
        if (gameInfo.isPointReachable(x, y)){
            switch (rType){
                case key: return new Key(x, y);
                case sword: return new Sword(x, y);
                case ladder: return new Ladder(x, y);
                case coin: return new Coin(x, y);
            }
        }
        return null;
    }

    private Door createDoor(int x, int y){
        if (gameInfo.isPointReachable(x, y)){
            return new Door(x, y);
        }
        return null;
    }

    private Trap createTrap(int x, int y){
        if (gameInfo.isPointReachable(x, y)){
            return new Trap(x, y);
        }
        return null;
    }

    private Exit createExit(int x, int y){
        if (gameInfo.isPointReachable(x, y)){
            return new Exit(x, y);
        }
        return null;
    }

    /**
     * Sets a number of enemies around a point.
     * @param x The X coordinate of the Point which to spawn the enemies around.
     * @param y The Y coordinate of the Point which to spawn the enemies around.
     * @param numberOfEnemies The number of enemies to spawn.
     * @return The list of enemies added.
     */
    public ArrayList<Enemy> createAndSetEnemiesAroundAPoint(int x, int y, Pathfinder pathfinder, int numberOfEnemies){
        ArrayList<Enemy> result = new ArrayList<>();
        if (gameInfo.isPointReachable(x, y)){
            result.add(createAndSetEnemy(x, y, pathfinder));
            numberOfEnemies--;
        }
        Point p = new Point(x, y);
        for (int i = 0; i < numberOfEnemies; i++){
            Point nextPoint = gameInfo.findClosestReachablePoint(p);
            result.add(createAndSetEnemy(nextPoint.x ,nextPoint.y, pathfinder));
        }
        return result;
    }
}
