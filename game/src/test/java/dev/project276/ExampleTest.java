package dev.project276;

import dev.project276.entity.*;
import dev.project276.main.*;
import dev.project276.display.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;

// you can do:
//      import org.junit.jupiter.api.Assertions;
// but then any assert function you have to call "Assertions.assertBlah(blahblah);" and thats annoying

/**
 * Simple example tests.
 * In IntelliJ, 'CTRL+SHIFT+F10' is a shortcut to run tests.
 *      If your cursor is in a specific method's scope, only that test runs.
 *      If your cursor is in the class' scope, it runs all tests.
 */
public class ExampleTest {

    /**
     * Rigorous Test :-)
     */
    @Disabled("Disabled test case example")
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }


    GameInfo info;  // Class variable, gets reset before every test case.

    /**
     * Example of a function that does a thing before every test case.
     */
    @BeforeEach
    public void setUpGameBoard() {
        info = new GameInfo();
    }

    @Test
    public void createWall() {
        Entity entity = new Wall(0,0);
        //assertSame(entity.getEntityType(), Entity.EntityType.wall);

        // example of doing multiple assertions without failing fast
        // all of these asserts will be checked, even if one fails earlier
        assertAll( "Correctly make a wall",
                () -> assertEquals(Entity.EntityType.wall, entity.getEntityType()),
                () -> assertEquals(0, entity.getX()),
                () -> assertEquals(0, entity.getY())
        );
    }

    @Test
    public void getGameBoard() {
        Entity[][] gameBoard = info.getGameBoard();
        assertNotNull(gameBoard);
    }

    @Test
    public void addEntityToBoard() {
        Entity entity = new Floor(0,0);
        info.addEntity(entity);
        Entity getEntity = info.getEntityAt(0,0);

        // Note this fails fast, if an assert fails
        // the ones after it don't run
        assertTrue(() -> info.hasEntityType(Entity.EntityType.floor));
        assertTrue(() -> info.hasEntity(entity));
        assertNotNull(getEntity);
    }

    /**
     * Test for getting an image that doesn't exist.
     */
    @Test
    public void nullImageUtils() {
        BufferedImage image = ImageUtils.loadImage("not_found_hue_hue.png");
        assertNull(image);
    }


}
