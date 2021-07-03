package dev.project276.entity;


import dev.project276.display.ImageUtils;
import dev.project276.main.Game;
import dev.project276.main.KeyInput;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Entity class that represents the player.
 * Has many more attributes and methods to accommodate
 * the extra features a Player has.
 */
public class Player extends MovingEntity {

    // object that the Player checks to decide its movement
    private KeyInput keyInput;

    // used to check for any collisions after the player has moved
    private EntityStateHandler stateHandler = null;

    private static int score = 0;
    private int keys = 0;
    private int ladders = 0;
    private static Sword sword = null;

    //Ticks, determines how long until a certain effect wears off
    private int invincibilityTickMax = 5;
    private int invincibilityTicks = invincibilityTickMax +1;
    private int swordTickMax = 1;
    private int swordTicks = swordTickMax+1;

    private BufferedImage invincibleImage = null;
    private ArrayList<Point> attackedTile = new ArrayList<>();
    boolean swapped = false;
    boolean facingLeft = true;
    boolean invincibleImageFacingLeft = true;

    private boolean won = false;

    public Player(int x, int y, KeyInput keyInput) {
        super(x, y);
        this.keyInput = keyInput;
        init();

    }

    public Player(int x, int y, KeyInput keyInput, EntityStateHandler stateHandler){
        super(x, y);
        this.keyInput = keyInput;
        this.stateHandler = stateHandler;
        init();
    }

    public Player(int x, int y, EntityStateHandler stateHandler){
        super(x, y);
        this.keyInput = Game.keyInput;
        this.stateHandler = stateHandler;
        init();
    }

    /**
     * Initializes the stats of the Player along with the entity image and invincible images.
     */
    public void init(){
        this.type = EntityType.player;
        image = getEntityImage();
        invincibleImage = ImageUtils.makeTranslucent(image, 0.50f);
        health = 3;
        damage = 0;
    }

    /**
     * Update method of the player object.
     */
    @Override
    public void update(){
        // Track current score; Debug stuff
        int sc = score;

        // Handle movement inputs
        moveFromInput();

        // Handle attack inputs
        attackFromInput();

        // Check if the score has changed; Debug stuff
        if (score != sc){
            System.out.println("Score changed, before it was " + ((Integer)sc).toString() + " now it is " + ((Integer)score).toString());
        }

        // Update counter for invincibility and sword cool-down
        invincibilityTicks++;
        swordTicks++;

        // Check if our invincibility has worn off, change our images back to normal if so
        if (invincibilityTicks > invincibilityTickMax && swapped){
            swapImages();
        }

        //printStats();
    }

    /**
     * Player method to move based on given input.
     * Does nothing if the keyInput or stateHandler are null.
     */
    public void moveFromInput() {
        if(keyInput == null || stateHandler == null)
            return;

        // Track current position before moving
        int oldx = x;
        int oldy = y;

        // Check inputs and obtain next movement
        if(keyInput.up) {
            y -= 1;
        }
        else if(keyInput.down) {
            y += 1;
        }
        else if(keyInput.left) {
            x -= 1;
            if (!facingLeft){
                image = ImageUtils.flipHorizontal(image);
            }
            facingLeft = true;
        }
        else if(keyInput.right) {
            x += 1;
            if (facingLeft){
                image = ImageUtils.flipHorizontal(image);
            }
            facingLeft = false;
        }

        // Move if we're able, otherwise revert our position
        boolean canMove = stateHandler.updateEntityState(this);
        if(!canMove) {
            y = oldy;
            x = oldx;
        }
    }

    /**
     * A method to move the player without keyInput.
     * Moves the player by dx,dy relative to their current position.
     * Provided to more easily create test cases.
     * Note: +x is towards the right, +y is towards the bottom.
     */
    public void moveRelative(int dx, int dy) {
        if(stateHandler == null)
            return;

        x += dx;
        y += dy;
        boolean canMove = stateHandler.updateEntityState(this);
        if(!canMove) {
            x -= dx;
            y -= dy;
        }
    }

    /**
     * Player method to attack based on given input.
     * Does nothing if keyInput is null.
     */
    public void attackFromInput() {
        if(keyInput == null)
            return;

        // reset our attack's active area
        attackedTile = new ArrayList<>();

        // if the player input to Attack, and sword is off cool-down, and we have a sword at all, attack
        if (keyInput.space && swordTicks >= swordTickMax && sword != null){

            // Attack left if we're facing left, otherwise attack right
            if (facingLeft){
                attackedTile.add(new Point(x-1, y));
            }
            else{
                attackedTile.add(new Point(x+1, y));
            }
            attackedTile.add(new Point(x, y));

            // put sword on cool-down
            swordTicks = 0;
        }
    }

    /**
     * A method to attack without keyInput and without cool-down.
     * Provided to more easily create test cases.
     */
    public void attack() {
        // reset our attack's active area
        attackedTile = new ArrayList<>();

        if (facingLeft){
            attackedTile.add(new Point(x-1, y));
        }
        else{
            attackedTile.add(new Point(x+1, y));
        }
        attackedTile.add(new Point(x, y));
    }

    public void swapDirection(){
        facingLeft = !facingLeft;
    }

    private void printStats() {
        System.out.println("\tup: " + keyInput.up + "; down: " + keyInput.down + "; left: " + keyInput.left + "; right: " + keyInput.right);
        System.out.println("\tPlayerX: " + x + "; PlayerY: " + y);
    }

    public void addKey() {
        keys++;
    }
    public void useKey() {
        keys--;
    }
    public int getKeys() {
        return keys;
    }

    public void addLadder(){
        ladders++;
    }
    public void useLadder(){
        ladders--;
    }
    public int getLadders(){
        return ladders;
    }

    public void winLevel(){
        this.won = true;
    }
    public boolean getWinLevel(){
        return this.won;
    }
    public void resetWinLevel(){
        this.won = false;
    }

    public ArrayList<Point> getAttackedTile(){
        return attackedTile;
    }
    public Sword getSword(){
        return sword;
    }

    public void addSword(Sword foundSword){
        if (sword == null){
            sword = foundSword;
        }
    }

    public void removeSword(){
        sword = null;
    }

    public void addScore(int dScore){
        score += dScore;
    }
    public void setScore(int newScore) { score = newScore; }
    public void multiplyScore(double multiplier){
        score *= multiplier;
    }
    public static int getScore() {
        return score;
    }

    /**
     * Swap our player's image and invincibility image
     * for swapping between normal and invincible states
     */
    private void swapImages(){
        BufferedImage temp = image;
        image = invincibleImage;
        invincibleImage = temp;

        // Flag that we transitioned between states
        swapped = !swapped;

        // faces left by default
        // If facing right but we before transition we were facing left, face right
        if (!facingLeft && invincibleImageFacingLeft){
            image = ImageUtils.flipHorizontal(image);
            invincibleImageFacingLeft = !invincibleImageFacingLeft;
        }
        // If facing left but before transition we were facing right, face left
        if (facingLeft && !invincibleImageFacingLeft){
            image = ImageUtils.flipHorizontal(image);
            invincibleImageFacingLeft = !invincibleImageFacingLeft;
        }
    }


    /**
     * Lowers the player health if they are not invincible.
     * A player is invincible for 30 ticks after taking damage.
     * @param damage The damage to lower the player for.
     * @return true if the player took damage. false if they
     * were still invincible.
     */
    public boolean takeDamage(int damage){
        System.out.println(invincibilityTicks);
        if (invincibilityTicks > invincibilityTickMax){
            swapImages();
            setHealth(health - damage);
            invincibilityTicks = 0;
            return true;
        }
        return false;
    }

    /**
     * Method that determines if the Player lost.
     * @return true if the player lost, ie. if they have 0 or less health
     * or if they have score less than 0
     */
    public boolean isDead(){
        return health <= 0 || score < 0;
    }
}