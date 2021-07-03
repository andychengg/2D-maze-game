package dev.project276.main;

import dev.project276.display.MenuScreen;

import javax.swing.*;

/**
 * Main class. Contains the main method, and calls to the Game object to start.
 * */
public class Main {


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MenuScreen.tg = new MenuScreen();
            MenuScreen.tg.setVisible(true);
        });
        /*

         */
    }
}
