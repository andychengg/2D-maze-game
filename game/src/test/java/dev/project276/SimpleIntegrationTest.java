package dev.project276;

import dev.project276.entity.*;
import dev.project276.main.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class SimpleIntegrationTest {

    GameInfo gameInfo;
    EntityStateHandler esh;
    EntityFactory ef;
    Pathfinder pf;
    GameLevelSwitcher gls;
    Player p;
    Enemy enemy2Left3Up;
    Knight knight2Up;
    Patrol patrol5Right;
    Coin coin1Down;
    Sword sword1Up;
    Key key1Right1Down;
    Door door2Right2Down;
    Ladder ladder4Left;
    Exit exit4Left2Down;
    Trap trap1Left;

    @BeforeEach
    public void init(){
        gls = new GameLevelSwitcher();
        gameInfo = new GameInfo();
        esh = new EntityStateHandler(gameInfo);
        ef = new EntityFactory(gameInfo);
        p = ef.createAndSetPlayer(10, 10, esh);
        p.setScore(0);
        p.removeSword();
        pf = new Pathfinder(gameInfo, p);
        gls.replaceCurrentLevel(gameInfo);
        assertNotNull(p);
        /*
        enemy2Left3Up = ef.createAndSetEnemy(8, 7, pf); // 2 to the left, 3 to the up
        knight2Up = (Knight)ef.createAndSetKnight(10, 8, pf);  // two moves away from reaching the player
        patrol5Right = (Patrol) ef.createAndSetPatrol(15, 10, pf); // 5 to the right
        coin1Down = (Coin) ef.createAndSetCoin(10, 11); // 1 down
        sword1Up = (Sword) ef.createAndSetSword(10, 9); // 1 up
        key1Right1Down = (Key) ef.createAndSetKey(11, 11); // 1 to the right, 1 down
        door2Right2Down = ef.createAndSetDoor(12, 12); // 2 to the right, 2 down
        ladder4Left = (Ladder) ef.createAndSetLadder(6, 10); // 4 to the left
        exit4Left2Down = ef.createAndSetExit(6, 12); // 4 to the left, 2 down
        trap1Left = ef.createAndSetTrap(9, 10); // 1 the the left
        */
    }

    /**
     * Creates an enemy,
     * updates two ticks.
     * Asserts that the enemy moved only once (towards the player)
     * due to movement delays.
     */
    @Test
    public void setEnemyAndUpdateTwoTicks(){
        enemy2Left3Up = ef.createAndSetEnemy(8, 7, pf);
        assertNotNull(enemy2Left3Up);
        gls.updateCurrentLevel();
        gls.updateCurrentLevel();
        assertTrue((enemy2Left3Up.getX() == 9 && enemy2Left3Up.getY() == 7) ||
                (enemy2Left3Up.getX() == 8 && enemy2Left3Up.getY() == 8));
    }

    @Test
    public void setKnightAndUpdateEightTicks(){
        knight2Up = (Knight) ef.createAndSetKnight(10, 8, pf);
        assertNotNull(knight2Up);
        for (int i = 0; i < (knight2Up.getMoveDelay()+1)*2; i++) {
            assertFalse(knight2Up.getX() == p.getX() && knight2Up.getY() == p.getY());
            gls.updateCurrentLevel();
        }
        assertAll("Move the knight towards the player",
                () -> assertTrue(knight2Up.getX() == p.getX() && knight2Up.getY() == p.getY()),
                () -> assertEquals(p.getHealth(), 2));
    }

    @Test
    public void setKnightAndMovePlayerOntoKnightAttackingTile(){
        knight2Up = (Knight) ef.createAndSetKnight(10, 8, pf);
        assertNotNull(knight2Up);
        movePlayerDir(Directions.Up);
        for (int i = 0; i < 2; i++) {
            movePlayerDir(Directions.Right);
        }
        gls.updateCurrentLevel();
        assertAll("Move the player 1 up, 2 right so the knight can attack the player",
                () -> assertEquals(12, p.getX()),
                () -> assertEquals(9, p.getY()),
                () -> assertTrue(knight2Up.getX() == p.getX() && knight2Up.getY() == p.getY()),
                () -> assertEquals(p.getHealth(), 2));
    }

    @Test
    public void collectCoinSwordTrap(){
        coin1Down = (Coin) ef.createAndSetCoin(10, 11); // 1 down
        sword1Up = (Sword) ef.createAndSetSword(10, 9); // 1 up
        trap1Left = ef.createAndSetTrap(9, 10); // 1 to the left
        movePlayerDir(Directions.Up);
        assertAll("Collect a sword",
                () -> assertEquals(sword1Up.getBonus(), Player.getScore()),
                () -> assertNotNull(p.getSword()));
        movePlayerDir(Directions.Down, 2);
        assertEquals(sword1Up.getBonus()*2+1, Player.getScore());
        movePlayerDir(Directions.Left);
        movePlayerDir(Directions.Up);
        assertAll("Run into a trap",
                () -> assertEquals(2, p.getHealth()),
                () -> assertEquals(sword1Up.getBonus()*2, Player.getScore()));
    }

    @Test
    public void getKeyUnlockDoor(){
        key1Right1Down = (Key) ef.createAndSetKey(11, 11); // 1 to the right, 1 down
        door2Right2Down = ef.createAndSetDoor(12, 12); // 2 to the right, 2 down
        movePlayerDir(Directions.Right, 2);
        movePlayerDir(Directions.Down, 2);
        assertAll("Try to run into a locked door",
                () -> assertEquals(12, p.getX()),
                () -> assertEquals(11, p.getY()),
                () -> assertEquals(Entity.EntityType.closedDoor, door2Right2Down.getEntityType()));
        movePlayerDir(Directions.Left);
        assertAll("Get a key",
                () -> assertEquals(11, p.getX()),
                () -> assertEquals(11, p.getY()),
                () -> assertEquals(1, p.getKeys()),
                () -> assertEquals(key1Right1Down.getBonus(), Player.getScore()),
                () -> assertFalse(gameInfo.hasEntity(key1Right1Down)));
        movePlayerDir(Directions.Right);
        movePlayerDir(Directions.Down);
        assertAll("Enter a door",
                () -> assertEquals(12, p.getX()),
                () -> assertEquals(12, p.getY()),
                () -> assertEquals(0, p.getKeys()),
                () -> assertEquals(Entity.EntityType.openDoor, door2Right2Down.getEntityType()));
    }

    @Test
    public void patrolBlockedByWallSoCantGoToPlayerTest(){
        patrol5Right = (Patrol) ef.createAndSetPatrol(15, 10, pf); // 5 to the right
        gameInfo.setWallAt(11, 10); // block the patrol's vision
        for (int i = 0; i < 1000; i++) {
            gls.updateCurrentLevel();
            assertAll("The patrol should be moving on the line y=10",
                    () -> assertTrue(patrol5Right.getX() > 11 && patrol5Right.getX() < 20),
                    () -> assertEquals(10, patrol5Right.getY()));
        }
        movePlayerDir(Directions.Up, 2); // let the patrol see the player
        for (int i = 0; i < 100; i++){
            gls.updateCurrentLevel();
        }
        assertAll("The patrol should kill the player",
                () -> assertTrue(p.getHealth() <= 0),
                () -> assertTrue(p.isDead()));
    }

    @Test
    public void getLadderEscapeLevel(){
        ladder4Left = (Ladder) ef.createAndSetLadder(6, 10); // 4 to the left
        exit4Left2Down = ef.createAndSetExit(6, 12); // 4 to the left, 2 down
        movePlayerDir(Directions.Down, 2);
        movePlayerDir(Directions.Left, 4);
        assertAll("Try to move into a hole with no ladder",
                () -> assertEquals(7, p.getX()),
                () -> assertEquals(12, p.getY()),
                () -> assertFalse(gls.isDone()),
                () -> assertEquals(0, gls.getCurrentLevelIndex()));

        movePlayerDir(Directions.Up, 2);
        movePlayerDir(Directions.Left, 1);
        assertAll("Collect a ladder",
                () -> assertEquals(6, p.getX()),
                () -> assertEquals(10, p.getY()),
                () -> assertEquals(1, p.getLadders()),
                () -> assertEquals(ladder4Left.getBonus(), Player.getScore()));

        movePlayerDir(Directions.Down, 2);
        assertAll("Enter the exit",
                () -> assertEquals(1, gls.getCurrentLevelIndex()),
                () -> assertTrue(p.getWinLevel()),
                () -> assertEquals(ladder4Left.getBonus(), Player.getScore()),
                () -> assertEquals(0, p.getLadders()));
    }

    private enum Directions{
        Up,
        Down,
        Left,
        Right
    }

    /**
     * Moves the player in the specified direction "times" times.
     * Also updates the entire game board, so be careful when using this with enemies.
     * @param direction The direction to move in
     * @param times How many times to move
     */
    private void movePlayerDir(Directions direction, int times){
        for (int i = 0; i < times; i++) {
            movePlayerDir(direction);
        }
    }

    /**
     * Moves the player in the specified direction.
     * Also updates the entire game board, so be careful when using this with enemies.
     * @param direction The direction to move in
     */
    private void movePlayerDir(Directions direction){
        switch (direction){
            case Up:
                Game.keyInput.up = true;
                gls.updateCurrentLevel();
                Game.keyInput.up = false;
                break;
            case Down:
                Game.keyInput.down = true;
                gls.updateCurrentLevel();
                Game.keyInput.down = false;
                break;
            case Left:
                Game.keyInput.left = true;
                gls.updateCurrentLevel();
                Game.keyInput.left = false;
                break;
            case Right:
                Game.keyInput.right = true;
                gls.updateCurrentLevel();
                Game.keyInput.right = false;
                break;
            default:
                fail();
        }
    }

}
