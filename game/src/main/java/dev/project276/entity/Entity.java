package dev.project276.entity;

import dev.project276.display.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The basic Entity class, which all other entities are subclasses of.
 * The enum EntityType is public, so all other classes can switch() based on the entity's type.
 * */
public abstract class Entity {

    public enum EntityType { player, enemy, trap, reward, key, wall, openDoor, closedDoor, floor, exit }

    private String name;
    protected EntityType type;
    protected Color color;
    protected BufferedImage image;
    protected static BufferedImage notFoundImage = ImageUtils.loadImage("/images/not_found.png");

    public int x = 0;
    int y = 0;
    private int velX = 0;
    private int velY = 0;

    // To be overwritten by subclasses
    public void update() {}

    // Protected constructor so anything making an Entity must supply coordinates,
    // but subclasses can still use the constructor.
    protected Entity() {

    }

    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
        image = null;
        //notFoundImage = ImageUtils.loadImage("/images/not_found.png");
    }

    public String getName() { return name; }

    public int getX() { return x; }
    public int getY() { return y; }

    public void setName(String name) { this.name = name; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setVelX(int x) {
        this.x = velX;
    }
    public void setVelY(int y) {
        this.y = velY;
    }


    public EntityType getEntityType() {
        return type;
    }

    /** *
     * Returns a Color object representing this entity.
     * If the entity's type is unrecognized, it will return MAGENTA by default.
     */
    public Color getEntityColor() {
        switch(type) {
            case player: color = Color.GREEN; break;
            case enemy: color = Color.RED; break;
            case trap: color = Color.ORANGE; break;
            case reward: color = Color.CYAN; break;
            case key: color = Color.YELLOW; break;
            case wall: color = Color.GRAY; break;
            case closedDoor: color = Color.BLUE; break;
            case openDoor: color = Color.LIGHT_GRAY; break;
            case floor: color = Color.LIGHT_GRAY; break;
            default: color = Color.MAGENTA; break;
        }
        return color;
    }

    /**
     * Gets the image representing this entity and returns it as a BufferedImage.
     * Returns the default image "not_found.png" if this entity's type is unrecognized or the desired image is not found.
     * Do not remove the default image, or this will break the function.
     */
    public BufferedImage getEntityImage() {
        switch(type) {
            case player: image = safelyGetImage("/images/Player.png"); break;
            case enemy:
                switch(((Enemy)this).getEnemyType()){
                    case normal:
                        image = safelyGetImage("/images/knight.png");
                        break;
                    case knight:
                        image = safelyGetImage("/images/horse.png");
                        break;
                    case patrol_idle:
                        image = safelyGetImage("/images/patrol_idle.png");
                        break;
                    case patrol:
                        image = safelyGetImage("/images/patrol.png");
                        break;
                }

            case trap: image = safelyGetImage("/images/trap.png"); break;
            case reward:
                switch(((Reward)this).getRewardType()) {
                    case key:
                        image = safelyGetImage("/images/key_big.png");
                        break;
                    case sword:
                        image = safelyGetImage("/images/sword_new.png");
                        break;
                    case ladder:
                        image = safelyGetImage("/images/ladder.png");
                        break;
                    case coin:
                        image = safelyGetImage("/images/coin.png");
                        break;
                }
                break;
            case wall: image = safelyGetImage("/images/wall.png"); break;
            case openDoor: image = safelyGetImage("/images/door_open.png"); break;
            case closedDoor: image = safelyGetImage("/images/door.png"); break;
            case floor: image = safelyGetImage("/images/floor.png"); break;
            case exit: image = safelyGetImage("/images/hole.png"); break;
            default: image = notFoundImage; break;
        }
        if(image == null) {
            image = notFoundImage;
        }
        return image;
    }

    /**
     * Given the image path, loads an image only once if necessary.
     * @param imagePath The string that contains the path to the image.
     * @return The BufferedImage representing the Entity's image.
     */
    public BufferedImage safelyGetImage(String imagePath){
        if (image == null){
            image = ImageUtils.loadImage(imagePath);
        }
        return image;
    }
}
