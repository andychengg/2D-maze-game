package dev.project276.entity;

import dev.project276.entity.Entity;

/**
 * The Wall entity.
 * Players cannot move into spaces occupied by Walls.
 * */
public class Wall extends Entity {

    protected Wall() {
        init();
    }

    public Wall(int x, int y) {
        super(x,y);
        init();
    }

    private void init() {
        type = EntityType.wall;
    }

}
