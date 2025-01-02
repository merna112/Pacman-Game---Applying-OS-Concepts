package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class PacMan {
    public int row, col, direction;
    private final GameState gameState;
    private boolean mouthOpen = true;
    private final int animationSpeed = 1;
    public Image pacmanOpenRight, pacmanClosedRight;
    public Image pacmanOpenLeft, pacmanClosedLeft;

    public PacMan(int startRow, int startCol, GameState gameState) {
        pacmanOpenRight = new ImageIcon(getClass().getResource("/pacman_open_right.jpeg")).getImage();
        pacmanClosedRight = new ImageIcon(getClass().getResource("/pacman_closed_right.jpeg")).getImage();
        pacmanOpenLeft = new ImageIcon(getClass().getResource("/pacman_open_left.jpeg")).getImage();
        pacmanClosedLeft = new ImageIcon(getClass().getResource("/pacman_closed_left.jpeg")).getImage();
        this.row = startRow;
        this.col = startCol;
        this.gameState = gameState;
        this.direction = KeyEvent.VK_RIGHT;
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(animationSpeed);
                    mouthOpen = !mouthOpen;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void move() {
        int nextRow = row, nextCol = col;
        switch (direction) {
            case KeyEvent.VK_UP -> nextRow--;
            case KeyEvent.VK_DOWN -> nextRow++;
            case KeyEvent.VK_LEFT -> nextCol--;
            case KeyEvent.VK_RIGHT -> nextCol++;
        }
        if (!gameState.isWall(nextRow, nextCol)) {
            row = nextRow;
            col = nextCol;
            gameState.consumePellet(row, col);
        }

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    public void resetPosition() {
        row = gameState.grid.length / 2;
        col = gameState.grid[0].length / 2;
    }
    public void draw(Graphics g, int cellSize) {
        Image currentImage;
        switch (direction) {
            case KeyEvent.VK_LEFT:
                currentImage = mouthOpen ? pacmanOpenLeft : pacmanClosedLeft;
                break;
            case KeyEvent.VK_RIGHT:
            default:
                currentImage = mouthOpen ? pacmanOpenRight : pacmanClosedRight;
                break;
        }
        g.drawImage(currentImage, col * cellSize, row * cellSize, cellSize, cellSize, null);
    }
}
