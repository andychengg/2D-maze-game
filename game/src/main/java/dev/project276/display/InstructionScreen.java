package dev.project276.display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A class for displaying the instructions.
 * Has a button to go back to the main menu.
 */
public class InstructionScreen extends JFrame implements ActionListener {

    JPanel backToMenuPanel, mainPanel, prevPanel;
    JButton backToMenuButton;
    Font instFont = new Font("Times New Roman", Font.PLAIN, 25);
    Font buttonFont = new Font("Times New Roman", Font.PLAIN, 35);

    public InstructionScreen(JPanel prevPanel){
        this.prevPanel = prevPanel;
    }

    /**
     * Handles the click of the button
     * @param e The actionEvent
     */
    public void actionPerformed(ActionEvent e) {
        enterMenu();
    }

    /**
     * When the exit to menu button is clicked,
     * set this screen to invisible and the menu screen to visible
     */
    public void enterMenu(){
        prevPanel.setVisible(true);
        mainPanel.setVisible(false);
    }

    /**
     * Draws all the components of the InstructionScreen
     */
    public void start(){
        System.out.println("In InstructionScreen");
        mainPanel = new JPanel();

        backToMenuButton = new JButton("BACK TO MENU");
        backToMenuButton.setForeground(Color.WHITE);
        backToMenuButton.setBackground(Color.black);
        backToMenuButton.addActionListener(this);
        backToMenuButton.setFocusPainted(false);
        backToMenuButton.setFont(buttonFont);

        backToMenuPanel = new JPanel();
        backToMenuPanel.setBackground(Color.black);
        backToMenuPanel.setBounds(640-300-20, 0, 300, 60);
        backToMenuPanel.add(backToMenuButton);

        String[] instructions = new String[]{
                "INSTRUCTIONS: ",
                "You are an adventurer trapped in a dungeon",
                "And you need to find a way out!",
                "Use keys to unlock doors,",
                "and ladders to escape out of holes",
                "You will also have to dodge or fight enemies",
                "CONTROLS:",
                "Up/left/down/right arrow keys to",
                "move up/left/down/right",
                "Spacebar to attack if you collected a sword",
                "R to restart if you died (hearts <= 0 or score < 0)"};
        renderLines(instructions, 50, 200);

        mainPanel.add(backToMenuPanel);
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setVisible(true);
        mainPanel.setLayout(null);
        MenuScreen.tg.add(mainPanel);
    }

    /**
     * Method that draws multiple JLabels onto JPanels onto the mainPanel.
     * Each String entry in the array is drawn on a separate line
     * The first entry starts at (x, y) and subsequent entries are spaced 30px
     * For some reason the text is centered
     * @param text The array of Strings to draw
     * @param x The x-coordinate of the starting line
     * @param y The y-coordinate of the starting line
     */
    private void renderLines(String[] text, int x, int y){
        for (int i = 0; i < text.length; i++){
            String line = text[i];
            JLabel instLabel = new JLabel(line);
            instLabel.setFont(instFont);
            instLabel.setForeground(Color.WHITE);

            JPanel instPanel = new JPanel();
            instPanel.setBackground(Color.BLACK);
            instPanel.setBounds(x, y+i*30,  500, 30);
            instPanel.add(instLabel);

            mainPanel.add(instPanel);
        }
    }
}
