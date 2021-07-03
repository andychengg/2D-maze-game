package dev.project276.entity;

/**
 * The Exit entity. Represented by a hole in the ground.
 * Players must collect the Ladder in the level before they can descend the Exit.
 */
public class Exit extends Door {
    public Exit(int x, int y) {
        super(x, y);
        init();
    }

    private void init(){
        type = EntityType.exit;
    }
}
