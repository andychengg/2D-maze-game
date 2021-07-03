package dev.project276.entity;

/**
 * The Door entity.
 * The Player must have a key to be able to pass through a Door.
 * If they do, the key is used up and the Door is in an open state, and can be walked through.
 */
public class Door extends Entity {

    // The door is closed by default
    private boolean open = false;

    public Door (int x, int y){
        super(x, y);
        init();
    }

    private void init(){
        type = EntityType.closedDoor;
    }

    public void setOpen(){
        type = EntityType.openDoor;
        open = true;
        // reset the image so it can be updated to an open door
        image = null;
    }

    public boolean isOpen(){
        return open;
    }
}
