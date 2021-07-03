package dev.project276.display;

import java.awt.image.BufferedImage;
import java.nio.Buffer;


/**
 * A class representing elements of the game's UI elements,
 * such as their health, score, and items collected.
 * Used by Display to show these elements on-screen.
 */
public class UserInterface {

    private BufferedImage heart;
    private BufferedImage key, ladder, sword;

    private int spacing = 32;

    private int heartX = 0; // Position on-screen to draw the heart
    private int heartY = 0;

    private int keyX = 0;
    private int keyY = 32;

    UserInterface() {
        heart = ImageUtils.loadImage("/images/heart.png");
        key = ImageUtils.loadImage("/images/key_small.png");
        ladder = ImageUtils.loadImage("/images/ladder_small.png");
        sword = ImageUtils.loadImage("/images/sword_new_small.png");
    }

    public BufferedImage getHeartImage() {
        return heart;
    }

    public BufferedImage getKeyImage() {
        return key;
    }

    public BufferedImage getLadderImage() {
        return ladder;
    }

    public BufferedImage getSwordImage(){
        return sword;
    }

    public int getSpacing() {
        return spacing;
    }

    public int getHeartX() {
        return heartX;
    }

    public int getHeartY() {
        return heartY;
    }

    public int getKeyX() {
        return keyX;
    }

    public int getKeyY() {
        return keyY;
    }
}
