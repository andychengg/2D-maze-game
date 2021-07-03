package dev.project276.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * A class for processing input from the user.
 */
public class KeyInput implements KeyListener {

    private boolean[] keys;
    public boolean up, down, left, right, space, r;
    private ArrayList<Boolean> inputQueue;

    public KeyInput() {
        keys = new boolean[256];
    }

    public void update() {
        up = keys[KeyEvent.VK_UP];
        down = keys[KeyEvent.VK_DOWN];
        left = keys[KeyEvent.VK_LEFT];
        right = keys[KeyEvent.VK_RIGHT];
        space = keys[KeyEvent.VK_SPACE];
        r = keys[KeyEvent.VK_R];
    }

    private void resetKeys() {
        keys[KeyEvent.VK_UP] = false;
        keys[KeyEvent.VK_DOWN] = false;
        keys[KeyEvent.VK_LEFT] = false;
        keys[KeyEvent.VK_RIGHT] = false;
        keys[KeyEvent.VK_R] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() != KeyEvent.VK_SPACE){
            resetKeys();
        }
        keys[e.getKeyCode()] = true;
        //System.out.println("pressed");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
}



