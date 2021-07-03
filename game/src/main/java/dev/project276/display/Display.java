package dev.project276.display;

import dev.project276.entity.Entity;
import dev.project276.entity.Player;
import dev.project276.main.Game;
import dev.project276.main.GameInfo;
import dev.project276.main.KeyInput;
import dev.project276.main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Display class. Contains the rendering code for drawing things to the screen.
 * Contains the JFrame that we attach our KeyListener keyInput to.
 * May need to implement the Runnable interface, if running this in its own thread is required.
 * */
public class Display {
    public static JFrame frame = MenuScreen.tg;   // The window the game is displayed on. Contains the Canvas.
    Canvas canvas;  // The object we draw on to display to the user

    private int tileLength = 64; // Length of a single tile visually

    private BufferStrategy bufferStrategy;
    private Graphics graphics;
    Color backgroundColor = Color.WHITE;

    private int width = 640;    // Default window size.
    private int height = 480;

    private UserInterface UI;   // holds info on UI elements
    private KeyInput keyInput;  // attaches to own frame to register input

    private GameInfo gameInfo;  // holds info on the gameboard

    // Private this constructor so that anything making a Display must supply window dimensions.
    private Display() {
        makeDisplay();
    }

    // GameInfo constructor
    public Display(GameInfo gameInfo, int tileLength, KeyInput keyInput) {
        this.tileLength = tileLength;
        this.width = gameInfo.getCols() * tileLength;
        this.height = gameInfo.getRows() * tileLength;
        this.keyInput = keyInput;
        this.gameInfo = gameInfo;

        UI = new UserInterface();

        makeDisplay();
    }

    public Display(int width, int height, int tileLength, KeyInput keyInput) {
        this.width = width;
        this.height = height;
        this.tileLength = tileLength;
        this.keyInput = keyInput;

        UI = new UserInterface();

        makeDisplay();
    }

    public Display(int width, int height, int tileLength) {
        this.width = width;
        this.height = height;
        this.tileLength = tileLength;

        UI = new UserInterface();

        makeDisplay();
    }

    /**
     * Sets up the JFrame for our window, and our Canvas object for drawing.
     * */
    public void makeDisplay() {
        //frame = new JFrame();
        frame = MenuScreen.tg;

        // Try to request focus for the frame for KeyInput
        // Continuously request 100 times. If none succeed, then create a new window
        int its = 0;
        frame.toFront();
        while (!frame.requestFocusInWindow() && its < 100){its++;}
        if (its >= 100){
            frame = new JFrame();
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Project 276 Game");
        frame.setResizable(false);
        frame.toFront();

        // Our Canvas object. We obtain our BufferStrategy and Graphics objects from this for drawing.
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMinimumSize(canvas.getPreferredSize());
        canvas.setMaximumSize((canvas.getPreferredSize()));
        canvas.setBackground(backgroundColor);
        canvas.setFocusable(false);

        frame.add(canvas);
        frame.addKeyListener(keyInput);
        System.out.println(Arrays.toString(MenuScreen.tg.getKeyListeners()));
        // call these three in this order to center the frame properly
        frame.pack();
        frame.setLocationRelativeTo(null);
        //frame.setVisible(true);
    }

    public void showFrame() {
        frame.setVisible(true);
    }

    /**
     * Main render function. Makes calls to render the gameBoard, Entities, and UI.
     * */
    public void render(Entity[][] gameBoard, ArrayList<Entity> entityList, int gameBoardCols, int gameBoardRows) {
        // Initialize our buffer strategy for drawing. If there no buffer strategy, make it and return.
        bufferStrategy = canvas.getBufferStrategy();
        if(bufferStrategy == null) {
            canvas.createBufferStrategy(3);
            return;
        }
        // Set our graphics up with the buffer strategy.
        graphics = bufferStrategy.getDrawGraphics();
        graphics.clearRect(0,0, width, height);     // Clear our screen and prep for drawing.

        renderGameBoard(graphics, gameBoard, gameBoardCols, gameBoardRows);
        renderEntities(graphics, entityList);
        renderUI(graphics);

        bufferStrategy.show();  // Make our canvas visible.
        graphics.dispose();     // Frees the graphics from memory. The last thing we call in our render.
    }

    /**
     * Iterates over the gameBoard object and renders it according to tileLength.
     * We pass in the Graphics object to avoid having to remake our BufferStrategy.
     * */
    private void renderGameBoard(Graphics graphics, Entity[][] gameBoard, int gameBoardCols, int gameBoardRows) {
        // iterate over the gameBoard, going top-down, row-by-row, i.e. [j][i] instead of [i][j].
        for(int i=0; i < gameBoardRows; ++i) {
            for(int j=0; j < gameBoardCols; ++j) {
                // Draw each space according to the Entity's properties.
                BufferedImage image = gameBoard[j][i].getEntityImage();
                graphics.drawImage(image, j*tileLength, i*tileLength, tileLength, tileLength,null);
            }
        }
    }

    /**
     * Renders all the entities to the screen.
     * @param graphics The Graphics to draw the entities onto.
     * @param entityList The ArrayList of entities to draw.
     */
    private void renderEntities(Graphics graphics, ArrayList<Entity> entityList) {
        for (Entity entity: entityList){
            BufferedImage image = entity.getEntityImage();
            graphics.drawImage(image, entity.getX()*tileLength, entity.getY()*tileLength, tileLength, tileLength, null);
        }
    }

    /**
     * Render all of the UI components of the game
     * onto the screen.
     * Includes Player health, keys, score, attacks.
     * Also handles win/loss screen logic.
     * @param graphics The Graphics to draw the components onto.
     */
    private void renderUI(Graphics graphics) {
        // get the info from the player...
        // not the cleanest way to do it
        // but prevents a null pointer exception
        Player player = gameInfo.getPlayer();
        int hearts, keys, score, ladders;
        boolean sword = false;
        ArrayList<Point> attackedTiles = new ArrayList<>();
        // default
        if (player == null){
            hearts = 3;
            keys = 0;
            score = 0;
            ladders = 0;
            sword = false;
        }
        else{
            hearts = player.getHealth();
            keys = player.getKeys();
            score = Player.getScore();
            ladders = player.getLadders();
            if (player.getSword() != null){
                sword = true;
            }
            attackedTiles = player.getAttackedTile();
            if (Game.done){
                showWinScreen(graphics);
            }
            else if (player.isDead()){
                showDeadScreen(graphics);
            }
        }

        // draw hearts
        for(int i=0; i < hearts; i++) {
            int heartX = UI.getHeartX() + i * UI.getSpacing();
            int heartY = UI.getHeartY();
            graphics.drawImage(UI.getHeartImage(), heartX, heartY,null);
        }

        // draw keys
        for (int i = 0; i < keys; i++) {
            int keyX = UI.getKeyX() + i * UI.getSpacing();
            int keyY = UI.getKeyY();
            graphics.drawImage(UI.getKeyImage(), keyX, keyY, null);
        }

        // draw ladder
        if (ladders > 0) graphics.drawImage(UI.getLadderImage(), 5*UI.getSpacing() + UI.getHeartX(), UI.getHeartY(), null);

        // draw sword
        if (sword) graphics.drawImage(UI.getSwordImage(), 6*UI.getSpacing() + UI.getHeartX(), UI.getHeartY(), null);

        // draw score
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setFont(new Font("Serif", Font.PLAIN, 40));
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString("Score: " + ((Integer)score).toString(), 0, 96);

        // draw attacked tiles
        for (Point p: attackedTiles){
            graphics.drawImage(player.getSword().getEntityImage(), p.x*tileLength, p.y*tileLength, null);
        }

    }

    /**
     * Draws the text that shows when the player's health is 0 or less.
     * @param graphics The Graphics to draw the text onto.
     */
    public void showDeadScreen(Graphics graphics){
        // draw score
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setFont(new Font("Serif", Font.PLAIN, 96));
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString("You lost!", 400, 400);
        graphics2D.drawString("Press 'R' to restart", 400, 500);
    }

    /**
     * Draws the text that shows when the player exits the final level.
     * @param graphics The Graphics to draw the text onto.
     */
    public void showWinScreen(Graphics graphics){
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setFont(new Font("Serif", Font.PLAIN, 96));
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString("You Won!", 400, 400);
        graphics2D.drawString("Final Score:" + ((Integer)Player.getScore()), 400, 500);
    }

    /**
     * Sets the GameInfo (ie level) of the Display.
     * This Display will draw the parameter gameInfo in subsequent render() calls.
     * @param gameInfo The GameInfo to set to.
     */
    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }
}
