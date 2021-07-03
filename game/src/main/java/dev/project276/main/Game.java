package dev.project276.main;
import dev.project276.display.Display;
import dev.project276.entity.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * The Game class. Contains the game logic and calls Display to render to the screen.
 * Implements the Runnable interface, and so runs in its own thread.
 * To safely start/stop the Game, call game.start() and game.stop() respectively.
 * */
public class Game implements Runnable {

    // Thread the game runs on, and variable to control game loop
    private Thread gameThread;
    private boolean running = false;

    Display display;

    public static KeyInput keyInput = new KeyInput();
    public GameLevelSwitcher switcher = new GameLevelSwitcher();
    GameInfo info = switcher.getCurrentLevel();
    public static boolean done = false;

    int tileLength = 64;

    int displayWidth;
    int displayHeight;

    // Info on FPS and updates
    double msPerUpdate = 1 / 60d; // 1 second divided by 60 frames, about 16.67 ms

    int framesPerSecond = 0;
    int updatesPerSecond = 0;
    double msPerPrintStats = 0;

    double timer = 0;
    int timerThreshold = 25;

    public Game() {
        displayWidth =  info.getCols()*tileLength;
        displayHeight = info.getRows()*tileLength;
        init();
    }

    /**
     * Initializes the Display instance, and sets up the gameBoard.
     * Currently just makes all edges of the board a Point, and anything else a Floor.
     */
    public void init() {
        //display = new Display(displayWidth, displayHeight, tileLength);
        //display.getFrame().addKeyListener(keyInput);

        //display = new Display(displayWidth, displayHeight, tileLength, keyInput);
        display = new Display(info, tileLength, keyInput);

        placeSampleBoard();

    }

    /**
     * The run() function of Game's runnable interface.
     * Contains the game's main loop.
     * FPS is currently not showing the proper number of frames.
     * */
    @Override
    public void run(){
        // Set up for calculating frames per second (FPS).
        double previous = System.currentTimeMillis();
        double lag = 0;

        // Print stats 1 second from now
        msPerPrintStats = System.currentTimeMillis() + 1000;

        while(running) {
            //display.render();

            double current = System.currentTimeMillis();
            double elapsed = (current - previous) / 1000d;
            previous = current;
            lag += elapsed;
            timer++;

            //System.out.println("current: " + current + "; previous: " + previous + "; elapsed: " + (current - previous));
            //System.out.println("Lag: " + lag + "; msPerUpdate: " + msPerUpdate);

            // Process game input here
            keyInput.update();

            // Loop to update game logic
            while(lag > msPerUpdate) {
                lag -= msPerUpdate;

                // Check if user pressed 'r' to reset
                if (keyInput.r){
                    Player player = info.getPlayer();
                    // Reset player score
                    player.addScore(-Player.getScore());

                    // Reset the level
                    switcher.initCurrentLevel();
                    GameInfo temp = switcher.getCurrentLevel();
                    if (temp != null){
                        info = temp;
                        display.setGameInfo(info);
                    }
                }

                // Check if the player is dead
                Player player = info.getPlayer();
                if (player != null && player.isDead() && !player.getWinLevel()){
                    //System.out.print("\rdead\n");
                }
                // Check if game was won
                else if (done){
                    System.out.println("done");
                }
                // If not dead and not won, update
                else{
                    update();
                }

                // Render game graphics here
                framesPerSecond++;
                // All rendering will be done by the Display class, after game logic is updated.
                display.render(info.getGameBoard(), info.getEntityList(), info.getCols(), info.getRows());
            }
            printStats();

            try {
                TimeUnit.MILLISECONDS.sleep((1000 / 60) - ((long)(System.currentTimeMillis()-current)));
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }

        }

        stop();
    }

    /**
     * Updates the game logic on each frame.
     */
    public void update() {
        updatesPerSecond++;
        // Only update Entity movement when timer says we can
        if(timer >= timerThreshold) {
            switcher.updateCurrentLevel();
            info = switcher.getCurrentLevel();
            if (switcher.isDone()){
                done = true;
            }
            display.setGameInfo(info);
            timer = 0;
        }

        //System.out.println("Update");
    }

    private void printStats() {
        if(System.currentTimeMillis() > msPerPrintStats) {
            System.out.println("FPS: " + framesPerSecond +"; UPS: " + updatesPerSecond);
            framesPerSecond = 0;
            updatesPerSecond = 0;
            msPerPrintStats = System.currentTimeMillis() + 1000;
        }
    }

    public void placeSampleBoard() {
    }

    /**
     * Utility method to safely start the gameThread.
     */
    public synchronized void start() {
        if(running) return;

        // Make the frame visible
        display.showFrame();

        // flag game loop to start running
        running = true;

        // Start a new thread for execution
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Utility method to safely terminate the running gameThread.
     */
    public synchronized void stop() {
        if(!running) return;

        // flag game loop to end looping
        running = false;
        try {
            // Wait for the game thread to terminate, then join into the main thread
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

