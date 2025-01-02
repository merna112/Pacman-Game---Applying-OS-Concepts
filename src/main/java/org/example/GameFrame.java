package org.example;
import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;

    public GameFrame(String difficulty, String maze) {
        super("Pac-Man Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        GameState gameState = new GameState(20, 20, difficulty, maze);
        PacMan pacman = new PacMan(10, 10, gameState);
        gamePanel = new GamePanel(difficulty, maze, pacman);
        add(gamePanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    public static void main(String[] args) {
        String difficulty = "easy";
        String maze = "Maze1";
        new GameFrame(difficulty, maze);
    }
}
