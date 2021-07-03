package dev.project276;

import dev.project276.entity.*;
import dev.project276.main.*;
import dev.project276.display.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


/**
 * Note: Enemies start with 2 HP
 */
public class PlayerDamageTest {
    EntityFactory ef;
    GameInfo gameInfo;
    Player p;
    Enemy enemyCloseToPlayer;
    Enemy enemyOnPlayer;
    EntityStateHandler esh;

    @BeforeEach
    public void init(){
        gameInfo = new GameInfo();
        ef = new EntityFactory(gameInfo);
        esh = new EntityStateHandler(gameInfo);
        p = ef.createAndSetPlayer(10, 10, esh);
        enemyCloseToPlayer = ef.createAndSetEnemy(9, 10, null);
        enemyOnPlayer = new Enemy(10, 10, null); // need this to prevent EntityFactory's isPointReachable checks
        gameInfo.addEntity(enemyOnPlayer);
    }

    @Test
    public void hitEnemyNormal(){
        p.addSword(new Sword(10, 10));
        attackAndUpdatePlayer();
        assertEquals(1, enemyCloseToPlayer.getHealth());
        assertEquals(1, enemyOnPlayer.getHealth());
    }

    @Test
    public void hitEnemyWithNoSword(){
        p.removeSword();
        attackAndUpdatePlayer();
        assertEquals(2, enemyCloseToPlayer.getHealth());
        assertEquals(2, enemyOnPlayer.getHealth());
    }

    @Test
    public void killEnemyWithSword(){
        p.addSword(new Sword(10, 10));
        attackAndUpdatePlayer();
        attackAndUpdatePlayer();
        assertTrue(!gameInfo.getEntityList().contains(enemyCloseToPlayer) && !gameInfo.getEntityList().contains(enemyOnPlayer));
        assertEquals(0, enemyOnPlayer.getHealth());
        assertEquals(0, enemyCloseToPlayer.getHealth());
    }

    @Test
    public void changeDirectionAndAttack() {
        p.addSword(new Sword(10, 10));
        attackAndUpdatePlayer();
        p.swapDirection();
        attackAndUpdatePlayer();
        assertTrue(gameInfo.getEntityList().contains(enemyCloseToPlayer) && !gameInfo.getEntityList().contains(enemyOnPlayer));
        assertEquals(0, enemyOnPlayer.getHealth());
        assertEquals(1, enemyCloseToPlayer.getHealth());
    }

    @Test
    public void outOfBoundsSwordTest(){
        Player p2 = new Player(1, 1, esh);
        gameInfo.addEntity(p2);
        p2.addSword(new Sword(1, 1));
        Enemy e2 = new Enemy(1, 1, null);
        gameInfo.addEntity(e2);
        Enemy e3 = new Enemy(0, 1, null);
        gameInfo.addEntity(e3);
        p2.attack();
        p2.moveRelative(0, 0);
        assertEquals(1, e2.getHealth());
        assertEquals(1, e3.getHealth());
    }

    private void attackAndUpdatePlayer(){
        p.attack();
        p.moveRelative(0, 0); // updates Player EntityStateHandler
    }

}
