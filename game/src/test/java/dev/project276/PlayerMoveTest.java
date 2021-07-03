package dev.project276;

import dev.project276.entity.*;
import dev.project276.main.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class to test the Player and EntityStateHandler classes.
 */
public class PlayerMoveTest {

    GameInfo info;
    EntityStateHandler handler;
    EntityFactory factory;
    Player player;

    // Any position is fine to start, as long as player can move in all 4 directions
    int startX = 5;
    int startY = 5;

    int startScore = 10;
    int startHealth = 3;

    /**
     * Clear the board and player stats before each test.
     */
    @BeforeEach
    public void makeAPlayer() {
        info = new GameInfo();
        handler = new EntityStateHandler(info);
        factory = new EntityFactory(info);

        player = factory.createAndSetPlayer(startX, startY, handler);
        player.setScore(startScore);
        player.setHealth(startHealth);
    }

    @Test
    public void playerMoveNoObstacle() {
        // Move 1 right and 1 down
        player.moveRelative(1,1);

        assertAll( "Move a player 1 right, 1 down",
                () -> assertEquals(startX+1, player.getX()),
                () -> assertEquals(startY+1, player.getY())
        );
    }

    @Test
    public void playerMoveIntoWall() {
        // Set wall to right of player
        info.setWallAt(startX+1, startY);
        // Move into the wall
        player.moveRelative(1,0);

        assertEquals(startX, player.getX());
    }

    @Test
    public void playerMoveIntoClosedDoorNoKey() {
        factory.createAndSetDoor(startX+1, startY);
        player.moveRelative(1,0);

        assertEquals(startX, player.getX());
    }

    @Test
    public void playerMoveIntoClosedDoorWithKey() {
        factory.createAndSetDoor(startX+1, startY);
        player.addKey();
        player.moveRelative(1,0);

        assertAll( "Use a key to open a door",
                () -> assertEquals(startX+1, player.getX()),
                () -> assertEquals(0, player.getKeys())
        );
    }

    @Test
    public void playerMoveIntoKey() {
        Reward key = factory.createAndSetKey(startX+1, startY);
        int keys = player.getKeys();
        int bonus = key.getBonus();
        player.moveRelative(1,0);

        assertAll( "Walk onto a key, get a key, get some score",
                () -> assertEquals(startX+1, player.getX()),
                () -> assertEquals(keys+1, player.getKeys()),
                () -> assertEquals(startScore+bonus, Player.getScore())
        );
    }

    @Test
    public void playerMoveIntoCoin() {
        Coin coin = (Coin) factory.createAndSetCoin(startX+1, startY);
        int bonus = coin.getBonus();
        double mult = coin.getScoreMultiplier();
        player.moveRelative(1,0);

        assertAll( "Walk onto a coin, get a lot of score",
                () -> assertEquals(startX+1, player.getX()),
                () -> assertEquals((startScore*mult)+bonus, Player.getScore())
        );
    }

    @Test
    public void playerMoveIntoLadder() {
        Ladder ladder = (Ladder) factory.createAndSetLadder(startX+1, startY);
        int bonus = ladder.getBonus();
        int ladders = player.getLadders();
        player.moveRelative(1,0);

        assertAll( "Walk onto a ladder, obtain the ladder, get some score",
                () -> assertEquals(startX+1, player.getX()),
                () -> assertEquals(ladders+1, player.getLadders()),
                () -> assertEquals(startScore+bonus, Player.getScore())
        );
    }

    @Test
    public void playerMoveIntoExitNoLadder() {
        Exit exit = factory.createAndSetExit(startX+1, startY);
        player.moveRelative(1,0);

        assertEquals(startX, player.getX());
    }

    @Test
    public void playerMoveIntoExitWithLadder() {
        Exit exit = factory.createAndSetExit(startX+1, startY);
        player.addLadder();
        player.moveRelative(1,0);

        assertAll( "Use a ladder to enter an exit",
                () -> assertEquals(startX+1, player.getX()),
                () -> assertEquals(0, player.getLadders())
        );
    }

    @Test
    public void playerMoveIntoSword() {
        Sword sword = (Sword) factory.createAndSetSword(startX+1, startY);
        player.moveRelative(1,0);

        assertAll( "Walk onto sword, get sword",
                () -> assertEquals(startX+1, player.getX()),
                () -> assertNotNull(player.getSword())
        );
    }

    @Test
    public void playerMoveIntoTrap() {
        Trap trap = factory.createAndSetTrap(startX+1, startY);

        int penalty = trap.getPenalty();
        int damage = trap.getDamage();

        player.moveRelative(1,0);

        assertAll( "Walk onto a trap, lose score, take damage",
                () -> assertEquals(startX+1, player.getX()),
                () -> assertEquals(startScore+penalty, Player.getScore()),
                () -> assertEquals(startHealth-damage, player.getHealth())
        );
    }

    @Test
    public void playerMoveIntoEnemy() {
        Pathfinder pathfinder = new Pathfinder(info, player);
        Enemy enemy = factory.createAndSetEnemy(startX+1, startY, pathfinder);
        int damage = enemy.getDamage();
        player.moveRelative(1,0);

        assertAll( "Walk into enemy, take damage",
                () -> assertEquals(startX+1, player.getX()),
                () -> assertEquals(startHealth-damage, player.getHealth())
        );
    }

}
