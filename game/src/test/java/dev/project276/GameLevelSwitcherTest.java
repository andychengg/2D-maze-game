package dev.project276;


import dev.project276.main.GameLevelSwitcher;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * Class to test if the levels change after the next level
 *
 */

public class GameLevelSwitcherTest {
    GameLevelSwitcher state;

    @BeforeEach
    public void init() {
       state = new GameLevelSwitcher();
       state.goNextLevel();
       state.getCurrentLevelIndex();

    }
    //test to check the next level
    @Test
    public void goNextLevelTest() {
        init();
        //assert if not null
        assertNotNull(state);
        //assert if 1st level is = current level
        assertEquals(1,state.getCurrentLevelIndex());

    }
    //test to check when game is finished
    @Test
    public void isDoneTest(){
        init();
        //second level
        state.goNextLevel();
        //third level
        state.goNextLevel();
        //assert if null
        assertNotNull(state);
        //assert if all levels have been exhausted
        assertEquals(true,state.isDone());
    }

    //test if isdonetest works when game is not finished
    @Test
    public void isNotDoneTest() {
        init();
        assertFalse(state.isDone());
    }



}
