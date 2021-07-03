package dev.project276.display;

import dev.project276.main.Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


/**
 * The class representing the front page of the game when the player opens the game.
 * Has buttons for starting the game, instructions, and music.
 */
public class MenuScreen extends JFrame implements ActionListener {
    JLabel titleLabel;
    JButton startButton, instructionsButton, musicButton;
    JPanel startPanel, instructionsPanel, titlePanel, musicPanel;
    JPanel mainPanel;
    String musicSound,musicPlay;
    ButtonHandler buttonH = new ButtonHandler();
    AudioPlayer music = new AudioPlayer();
    public static MenuScreen tg;
    Font titleFont = new Font("Times New Roman", Font.PLAIN, 75);
    Font buttonFont = new Font("Times New Roman", Font.PLAIN, 35);
    Font musicFont = new Font("Times New Roman", Font.PLAIN, 10 );

    public MenuScreen(){
        init();
    }
    private void init(){

        setFrame();
        requestFocus();
        Menu();
    }
    private void setFrame() {
        setSize(640, 640);
        setResizable(false);
        setTitle("CMPT276 Game Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Sets all components of the Menu
     */
    private void Menu() {
        mainPanel = new JPanel();

        titleLabel = new JLabel("Maze Caller");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.black);

        titlePanel = new JPanel();
        titlePanel.setBounds(50, 50, 550, 100);
        titlePanel.setBackground(Color.green);
        titlePanel.add(titleLabel);

        startButton = new JButton("ENTER MAZE");
        startButton.setBackground(Color.black);
        startButton.setForeground(Color.white);
        startButton.setFocusPainted(false);
        startButton.setFont(buttonFont);
        startButton.addActionListener(this);

        instructionsButton = new JButton("HOW TO PLAY");
        instructionsButton.setBackground(Color.black);
        instructionsButton.setForeground(Color.white);
        instructionsButton.setFocusPainted(false);
        instructionsButton.setFont(buttonFont);
        instructionsButton.addActionListener(this);

        startPanel = new JPanel();
        startPanel.setBackground(Color.black);
        startPanel.setBounds(170, 250, 260, 100);
        startPanel.add(startButton);

        instructionsPanel = new JPanel();
        instructionsPanel.setBackground(Color.black);
        instructionsPanel.setBounds(160, 400, 280, 60);
        instructionsPanel.add(instructionsButton);

        musicPanel = new JPanel();
        musicPanel.setBounds(500,500, 75, 50);
        musicPanel.setBackground(Color.black);
        musicButton = new JButton("Music");
        musicButton.setFont(musicFont);
        musicButton.setBackground(Color.black);
        musicButton.setForeground(Color.white);
        musicButton.setFocusPainted(false);
        musicButton.addActionListener(buttonH);
        musicPanel.add(musicButton);

        musicSound = "/music/Music.wav";
        musicPlay = "off";


        mainPanel.add(startPanel);
        mainPanel.add(musicPanel);
        mainPanel.add(instructionsPanel);
        mainPanel.add(titlePanel);
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setPreferredSize(new Dimension(640,640));
        mainPanel.setLayout(null);
        mainPanel.setVisible(true);

        this.add(mainPanel);
    }
    //button handler to handle the music when the button is clicked
    public class ButtonHandler implements ActionListener {


        public void actionPerformed(ActionEvent e) {
            if(musicPlay.equals("off")) {
                music.setFile(musicSound);
                music.play();
                music.loop();
                musicPlay = "on";
                musicButton.setText("Music On");
            }
            else if(musicPlay.equals("on")) {
                music.stop();
                musicPlay = "off";
                musicButton.setText("Music Off");
            }

        }
    }

    /**
     * Button click will switch from menu panel to game
     * Checks if the button is enter the game, or instruction page
     * If no buttons match, will enter game by default
     */
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        String result = e.getActionCommand();
        if (result.equals("ENTER MAZE")){
            mainPanel.setVisible(false);
            startGame();
        }
        else if (result.equals("HOW TO PLAY")){
            mainPanel.setVisible(false);
            instructions();
        }
        else{
            mainPanel.setVisible(false);
            startGame();
        }
    }

    public void startGame(){
        Game game = new Game();
        game.start();
    }

    public void instructions(){
        InstructionScreen inst = new InstructionScreen(mainPanel);
        inst.start();
    }

}
