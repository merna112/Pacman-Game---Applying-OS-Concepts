package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private final int CELL_SIZE = 30;
    private final int ROWS = 20;
    private final int COLS = 20;
    private final GameState gameState;
    private final Timer pacmanTimer;
    private final Timer ghostTimer;
    private final PacMan pacman;
    private boolean isPaused = false;
    private String difficulty;

    public GamePanel(String difficulty, String maze, PacMan pacman) {
        this.pacman = pacman;
        this.difficulty = difficulty;
        setPreferredSize(new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE));
        setBackground(Color.BLACK);
        gameState = new GameState(ROWS, COLS, difficulty, maze);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    togglePause();
                } else if (!isPaused) {
                    gameState.handleInput(e.getKeyCode());
                }
            }
        });
        setFocusable(true);
        int pacmanSpeed = getPacManSpeed(difficulty);
        pacmanTimer = new Timer(pacmanSpeed, e -> {
            if (!isPaused) {
                gameState.update();
                repaint();
            }
        });
        pacmanTimer.start();

        int ghostSpeed = getGhostSpeed(difficulty);
        int numberOfGhosts = getNumberOfGhosts(difficulty);
        ghostTimer = new Timer(ghostSpeed, e -> {
            if (!isPaused) {
                gameState.updateGhosts(numberOfGhosts);
                repaint();
            }
        });
        ghostTimer.start();
    }

    private int getPacManSpeed(String difficulty) {
        switch (difficulty) {
            case "Medium":
                return 90;  // Faster speed for medium difficulty
            case "Hard":
                return 70;  // Even faster speed for hard difficulty
            default:
                return 110;  // Slower speed for easy difficulty
        }
    }

    private int getGhostSpeed(String difficulty) {
        switch (difficulty) {
            case "Medium":
                return 110;  // Faster ghosts for medium difficulty
            case "Hard":
                return 90;   // Even faster ghosts for hard difficulty
            default:
                return 120;  // Slower ghosts for easy difficulty
        }
    }

    private int getNumberOfGhosts(String difficulty) {
        switch (difficulty) {
            case "Medium":
                return 4;  // More ghosts for medium difficulty
            case "Hard":
                return 6;  // Even more ghosts for hard difficulty
            default:
                return 3;  // Fewer ghosts for easy difficulty
        }
    }

    private synchronized void togglePause() {
        isPaused = !isPaused;  // Toggle the pause state
        if (isPaused) {
            pacmanTimer.stop();
            ghostTimer.stop();
        } else {
            notifyAll();
            pacmanTimer.start();
            ghostTimer.start();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameState.render(g, CELL_SIZE, null);

        if (isPaused) {
            g.setColor(new Color(255, 255, 255, 200));
            g.setFont(new Font("Arial", Font.BOLD, 30));
            FontMetrics fm = g.getFontMetrics();
            String message = "Game Paused!";
            int x = (getWidth() - fm.stringWidth(message)) / 2;
            int y = getHeight() / 2;
            g.drawString(message, x, y);
        }

        String powerModeText = gameState.isPowerMode() ? "Power Mode: ON" : "Power Mode: OFF";
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(powerModeText)) / 2;
        int y = 30;
        g.drawString(powerModeText, x, y);
    }
}
