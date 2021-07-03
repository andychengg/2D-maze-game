package dev.project276.entity;

/**
 * The Floor entity.
 * Essentially a 'default' entity.
 * */
public class Floor extends Entity {

    protected Floor() {
        init();
    }

    public Floor(int x, int y) {
        super(x,y);
        init();
    }

    private void init() {
        type = EntityType.floor;
    }
}
