package org.example;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
public class GameState {
    protected final int[][] grid;
    private final PacMan pacMan;
    private List<Ghost> ghosts;
    private int score = 0;
    private boolean powerMode = false;
    private int lives = 3;
    private Image cherryImage;
    private JLabel powerModeLabel;
    private int currentLevel = 1; // Track the current level
    private int highestScore = 0; // Track the highest score
    private Color wallColor;

public GameState(int rows, int cols, String difficulty, String maze) {
    cherryImage = new ImageIcon(getClass().getResource("/cherry.png")).getImage();
    grid = new int[rows][cols];
    initializeGrid(maze);
    pacMan = new PacMan(rows / 2, cols / 2, this);
    int numGhosts = 4;
    if (difficulty.equalsIgnoreCase("medium")) {
        numGhosts = 6;
    } else if (difficulty.equalsIgnoreCase("hard")) {
        numGhosts = 8;
    }

    // Create the ghosts and ensure they are not placed on walls
    ghosts = new ArrayList<>();
    for (int i = 0; i < numGhosts; i++) {
        int row, col;
        do {
            row = (int) (Math.random() * (rows - 2)) + 1;
            col = (int) (Math.random() * (cols - 2)) + 1;
        } while (isCellOccupied(row, col)); // Ensure ghosts don't overlap
        ghosts.add(new Ghost(row, col, this, maze));
    }

    for (Ghost ghost : ghosts) {
        new Thread(ghost).start();
    }

    powerModeLabel = new JLabel("Power Mode: OFF");
    powerModeLabel.setForeground(Color.YELLOW);
    powerModeLabel.setFont(new Font("Arial", Font.BOLD, 14));
}
    public int getPacmanRow() {
        return pacMan.getRow();
    }

    public int getPacmanCol() {
        return pacMan.getCol();
    }
    public int getRows() {
        return grid.length;
    }

    public int getCols() {
        return grid[0].length;
    }
    public boolean isValidMove(int row, int col) {
        return row >= 0 && row < getRows() && col >= 0 && col < getCols() && !isWall(row, col);
    }


    public synchronized boolean isCellOccupied(int row, int col) {
        for (Ghost ghost : ghosts) {
            if (ghost.getRow() == row && ghost.getCol() == col) {
                return true;
            }
        }
        if (pacMan.getRow() == row && pacMan.getCol() == col) {
            return true;
        }
        return false;
    }

    private void initializeGrid(String maze) {
        if (maze.equals("Maze2")) {
            wallColor = Color.RED;
            initializeMaze2();
        } else if (maze.equals("Maze3")) {
            wallColor = Color.BLUE;
            initializeMaze3();
        } else {
            wallColor = Color.GRAY;
            initializeMaze1();
        }
    }
    private void initializeMaze1() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (i == 0 || j == 0 || i == grid.length - 1 || j == grid[0].length - 1 || Math.random() < 0.2) {
                    grid[i][j] = 3; // Wall
                } else if (Math.random() < 0.1) {
                    grid[i][j] = 1; // Pellet
                } else if (Math.random() < 0.05) {
                    grid[i][j] = 2; // Cherry
                } else {
                    grid[i][j] = 0; // Path
                }
            }
        }
    }

    private void initializeMaze2() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (i == 0 || j == 0 || i == grid.length - 1 || j == grid[0].length - 1 || Math.random() < 0.3) {
                    grid[i][j] = 3; // Wall
                } else if (Math.random() < 0.12) {
                    grid[i][j] = 1; // Pellet
                } else if (Math.random() < 0.08) {
                    grid[i][j] = 2; // Cherry
                } else {
                    grid[i][j] = 0; // Path
                }
            }
        }
    }

    private void initializeMaze3() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (i == 0 || j == 0 || i == grid.length - 1 || j == grid[0].length - 1 || Math.random() < 0.15) {
                    grid[i][j] = 3; // Wall
                } else if (Math.random() < 0.08) {
                    grid[i][j] = 1; // Pellet
                } else if (Math.random() < 0.06) {
                    grid[i][j] = 2; // Cherry
                } else {
                    grid[i][j] = 0; // Path
                }
            }
        }
    }

    public synchronized void consumePellet(int row, int col) {
        if (grid[row][col] == 1) {
            score += 10; // Pellet
            grid[row][col] = 0;
        } else if (grid[row][col] == 2) {
            score += 50; // Cherry (Power Pellet)
            grid[row][col] = 0;
            activatePowerMode();
        }
        checkLevelCompletion();
    }

    private void checkLevelCompletion() {
        boolean allPelletsEaten = true;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 1 || grid[i][j] == 2) {
                    allPelletsEaten = false;
                    break;
                }
            }
            if (!allPelletsEaten) break;
        }

        if (allPelletsEaten) {
            advanceToNextLevel();
        }
    }

    private void advanceToNextLevel() {
        currentLevel++;
        if (currentLevel > 3) {
            JOptionPane.showMessageDialog(null, "Congratulations! You've completed all levels!");
            JOptionPane.showMessageDialog(null, "Highest Score: " + highestScore);
            System.exit(0);
        } else {
            int option = JOptionPane.showConfirmDialog(null, "Level " + currentLevel + " starts now! Click OK to continue.",
                    "Level Up!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                String nextMaze = "Maze" + currentLevel;
                initializeGrid(nextMaze);
                pacMan.resetPosition();
                JOptionPane.showMessageDialog(null, "Level " + currentLevel + " starts now!");
            } else {
                JOptionPane.showMessageDialog(null, "Game Paused. Click OK to resume.");
            }
        }
    }

    public boolean isWall(int row, int col) {
        return grid[row][col] == 3;
    }

private void activatePowerMode() {
    powerMode = true;
    powerModeLabel.setText("Power Mode: ON");

    for (Ghost ghost : ghosts) {
        ghost.setSpeed(ghost.getSpeed() / 2);  // Slow down the ghosts
    }
    new Thread(() -> {
        try {
            Thread.sleep(10000);  // Power mode lasts for 10 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        powerMode = false;
        powerModeLabel.setText("Power Mode: OFF");
        for (Ghost ghost : ghosts) {
            ghost.setSpeed(ghost.getOriginalSpeed());
        }
    }).start();
}
    public boolean isPowerMode() {
        return powerMode;
    }

    public void render(Graphics g, int cellSize, String mazeType) {
        if (mazeType == null) {
            mazeType = "Maze1";
        }
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == 1) {
                    g.setColor(Color.WHITE);
                    g.fillOval(col * cellSize + cellSize / 4, row * cellSize + cellSize / 4, cellSize / 2, cellSize / 2);
                } else if (grid[row][col] == 2) {
                    g.drawImage(cherryImage, col * cellSize, row * cellSize, cellSize, cellSize, null);
                } else if (grid[row][col] == 3) {
                    g.setColor(wallColor);
                    g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                }
            }
        }
        pacMan.draw(g, cellSize);
        for (Ghost ghost : ghosts) {
            ghost.draw(g, cellSize);
        }
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 10);
        g.drawString("Lives: " + lives, 10, 25);
    }

    public void handleInput(int keyCode) {
        pacMan.setDirection(keyCode);
    }

    public void update() {
        pacMan.move();
        for (Ghost ghost : ghosts) {
            if (ghost.getRow() == pacMan.getRow() && ghost.getCol() == pacMan.getCol()) {
                handleCollision(ghost);
            }
        }
    }
    private void handleCollision(Ghost ghost) {
        if (powerMode) {
            score += 200; // PacMan eats a ghost in power mode for double points
            ghost.resetPosition();
        } else {
            lives--;
            pacMan.resetPosition();
            if (lives <= 0) {
                JOptionPane.showMessageDialog(null, "Game Over! You lost at Level " + currentLevel);
                if (score > highestScore) {
                    highestScore = score;
                }
                JOptionPane.showMessageDialog(null, "Highest Score: " + highestScore);
                System.exit(0);
            }
        }
    }
        public synchronized void updateGhosts(int numberOfGhosts) {
        for (Ghost ghost : ghosts) {
            ghost.move();
            if (ghost.collidesWithPacMan(pacMan)) {
                handleCollision(ghost);
            }
        }
    }
    boolean isCherryOrPellet(int row, int col) {
        return grid[row][col] == 1 || grid[row][col] == 2; // 1 = Pellet, 2 = Cherry
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}
