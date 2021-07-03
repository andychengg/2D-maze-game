package dev.project276;

import dev.project276.entity.*;
import dev.project276.main.*;
import dev.project276.display.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;

/**
 * Simple example tests.
 * In IntelliJ, 'CTRL+SHIFT+F10' is a shortcut to run tests.
 *      If your cursor is in a specific method's scope, only that test runs.
 *      If your cursor is in the class' scope, it runs all tests.
 */
public class EntityFactoryTest {

    GameInfo info;  // Class variable, gets reset before every test case.
    EntityFactory ef;

    /**
     * Example of a function that does a thing before every test case.
     */
    @BeforeEach
    public void setUpGameBoard() {
        info = new GameInfo();
        ef = new EntityFactory(info);
    }

    @Test
    public void createEnemyTestNormal() {
        Enemy en = ef.createAndSetEnemy(5, 10, null);
        assertEquals(en.getX(), 5);
        assertEquals(en.getY(), 10);
    }

    /**
     * Since all EntityFactory methods use isPointReachable to bounds check,
     * only one test with invalid coordinates is required
     */
    @Test
    public void createEnemyInvalidCoordinates(){
        // negative coords
        assertNull(ef.createAndSetEnemy(-10, 10, null));
        assertNull(ef.createAndSetEnemy(10, -10, null));
        // out of bound coords
        assertNull(ef.createAndSetEnemy(20, 10, null));
        assertNull(ef.createAndSetEnemy(10, 15, null));
        // wall at (0, 0)
        assertNull(ef.createAndSetEnemy(0, 0, null));
    }

    @Test
    public void createTwoEntitiesOnAPoint(){
        Player pl = ef.createAndSetPlayer(10, 10, new EntityStateHandler(info));
        Enemy en = ef.createAndSetEnemy(10, 10, null);
        assertNull(en);
    }

    @Test
    public void createDifferentEnemies(){
        Enemy en = ef.createAndSetEnemy(10, 10, null);
        Knight kt = (Knight) ef.createAndSetKnight(10, 11, null);
        Patrol pt = (Patrol) ef.createAndSetPatrol(11, 10, null);
        assertNotNull(en);
        assertNotNull(kt);
        assertNotNull(pt);
        assertSame(en.getEntityType(), Entity.EntityType.enemy);
        assertSame(kt.getEntityType(), Entity.EntityType.enemy);
        assertSame(pt.getEntityType(), Entity.EntityType.enemy);
        assertSame(en.getEnemyType(), Enemy.EnemyType.normal);
        assertSame(kt.getEnemyType(), Enemy.EnemyType.knight);
        assertSame(pt.getEnemyType(), Enemy.EnemyType.patrol_idle);
    }

    @Test
    public void createDifferentRewards(){
        Sword sword = (Sword)ef.createAndSetSword(10, 10);
        Key key = (Key)ef.createAndSetKey(10, 11);
        Ladder ladder = (Ladder)ef.createAndSetLadder(11, 10);
        Coin coin = (Coin)ef.createAndSetCoin(12, 12);
        assertNotNull(sword);
        assertNotNull(key);
        assertNotNull(ladder);
        assertNotNull(coin);
        assertSame(sword.getEntityType(), Entity.EntityType.reward);
        assertSame(key.getEntityType(), Entity.EntityType.reward);
        assertSame(ladder.getEntityType(), Entity.EntityType.reward);
        assertSame(coin.getEntityType(), Entity.EntityType.reward);
        assertSame(sword.getRewardType(), Reward.RewardType.sword);
        assertSame(key.getRewardType(), Reward.RewardType.key);
        assertSame(ladder.getRewardType(), Reward.RewardType.ladder);
        assertSame(coin.getRewardType(), Reward.RewardType.coin);
    }

}
