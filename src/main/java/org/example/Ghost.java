package org.example;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Ghost implements Runnable {
    private int row, col;
    private final int initialRow, initialCol;
    private final GameState gameState;
    private Image ghostImage;

    private enum Direction {UP, DOWN, LEFT, RIGHT}

    private Direction currentDirection = Direction.RIGHT;
    private int speed;
    private int originalSpeed;
    private final Object lock = new Object();

    public Ghost(int startRow, int startCol, GameState gameState, String mazeType) {
        this.row = startRow;
        this.col = startCol;
        this.initialRow = startRow;
        this.initialCol = startCol;
        this.gameState = gameState;
        this.originalSpeed = 10;
        this.speed = originalSpeed;
        loadGhostImage(mazeType);
    }

    private void loadGhostImage(String mazeType) {
        switch (mazeType) {
            case "Maze2":
                ghostImage = new ImageIcon(getClass().getResource("/yellowGhost.jpg")).getImage();
                break;
            case "Maze3":
                ghostImage = new ImageIcon(getClass().getResource("/ghostImage.jpeg")).getImage();
                break;
            default:
                ghostImage = new ImageIcon(getClass().getResource("/greenGhost.jpg")).getImage();
                break;
        }
        if (ghostImage == null) {
            System.err.println("Error: Ghost image not found for maze type: " + mazeType);
            ghostImage = new ImageIcon(getClass().getResource("/greenGhost.jpg")).getImage();
        }
    }
    public int getSpeed() {
        return speed;
    }
    public int getOriginalSpeed() {
        return originalSpeed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void run() {
        while (true) {
            synchronized (lock) {
                if (gameState.getCurrentLevel() == 3) {
                    int targetRow = gameState.getPacmanRow();
                    int targetCol = gameState.getPacmanCol();

                    int bestRow = row, bestCol = col;
                    double bestDistance = Double.MAX_VALUE;


                    for (int[] dir : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
                        int newRow = row + dir[0];
                        int newCol = col + dir[1];

                        if (newRow >= 0 && newRow < gameState.getRows() && newCol >= 0 && newCol < gameState.getCols() && gameState.isValidMove(newRow, newCol)) {
                            double distance = Math.sqrt(Math.pow(newRow - targetRow, 2) + Math.pow(newCol - targetCol, 2));
                            if (gameState.isPowerMode() && distance > bestDistance) {
                                bestRow = newRow;
                                bestCol = newCol;
                                bestDistance = distance;
                            }
                            else if (!gameState.isPowerMode() && distance < bestDistance) {
                                bestRow = newRow;
                                bestCol = newCol;
                                bestDistance = distance;
                            }
                        }
                    }
                    row = bestRow;
                    col = bestCol;
                }

            }
            try {
                if (speed != 0) {
                    Thread.sleep(500 / speed);
                } else {
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    protected void move() {
        List<Direction> possibleDirections = getValidDirections();
        if (!possibleDirections.isEmpty()) {
            currentDirection = possibleDirections.get((int) (Math.random() * possibleDirections.size()));
        }

        int nextRow = row;
        int nextCol = col;

        switch (currentDirection) {
            case UP:    nextRow--; break;
            case DOWN:  nextRow++; break;
            case LEFT:  nextCol--; break;
            case RIGHT: nextCol++; break;
        }

        if (!gameState.isWall(nextRow, nextCol) && !gameState.isCherryOrPellet(nextRow, nextCol)) {
            row = nextRow;
            col = nextCol;
        } else {
            // If we hit a wall or a pellet, reverse or choose a different path
            reverseOrFindNewPath();
        }
    }

    private List<Direction> getValidDirections() {
        List<Direction> validDirections = new ArrayList<>();
        if (!gameState.isWall(row - 1, col)) validDirections.add(Direction.UP);
        if (!gameState.isWall(row + 1, col)) validDirections.add(Direction.DOWN);
        if (!gameState.isWall(row, col - 1)) validDirections.add(Direction.LEFT);
        if (!gameState.isWall(row, col + 1)) validDirections.add(Direction.RIGHT);
        return validDirections;
    }

    private void reverseOrFindNewPath() {
        if (currentDirection == Direction.UP || currentDirection == Direction.DOWN) {
            currentDirection = Math.random() > 0.5 ? Direction.LEFT : Direction.RIGHT;
        } else {
            currentDirection = Math.random() > 0.5 ? Direction.UP : Direction.DOWN;
        }
    }

    public void resetPosition() {
        this.row = initialRow;
        this.col = initialCol;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void draw(Graphics g, int cellSize) {
        g.drawImage(ghostImage, col * cellSize, row * cellSize, cellSize, cellSize, null);
    }

    public boolean collidesWithPacMan(PacMan pacMan) {
        return this.row == pacMan.getRow() && this.col == pacMan.getCol();
    }
}
