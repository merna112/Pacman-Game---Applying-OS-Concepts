package org.example;

import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class GameLauncher extends JFrame {
    private JRadioButton maze1, maze2, maze3;
    private ButtonGroup mazeGroup;
    private Clip introMusicClip;

    public GameLauncher() {
        setTitle("Pac-Man Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        File file = new File(getClass().getClassLoader().getResource("introMusic.wav").getFile());
        playIntroMusic(file.getPath());
        JLabel title = new JLabel("Pac-Man Game", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 36));
        title.setForeground(Color.YELLOW);
        title.setOpaque(true);
        title.setBackground(new Color(0, 0, 0, 200));
        add(title, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Create a gradient background
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 30, 30), getWidth(), getHeight(), new Color(0, 0, 50));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        optionsPanel.setLayout(new GridBagLayout());
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel mazePanel = new JPanel();
        mazePanel.setBorder(BorderFactory.createTitledBorder("Select Maze"));
        mazePanel.setLayout(new GridLayout(3, 1));

        maze1 = new JRadioButton("Maze 1", true);
        maze2 = new JRadioButton("Maze 2");
        maze3 = new JRadioButton("Maze 3");
        mazeGroup = new ButtonGroup();
        mazeGroup.add(maze1);
        mazeGroup.add(maze2);
        mazeGroup.add(maze3);

        maze1.setFont(new Font("Arial", Font.PLAIN, 18));
        maze2.setFont(new Font("Arial", Font.PLAIN, 18));
        maze3.setFont(new Font("Arial", Font.PLAIN, 18));

        mazePanel.add(maze1);
        mazePanel.add(maze2);
        mazePanel.add(maze3);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        optionsPanel.add(mazePanel, gbc);

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setBackground(new Color(0, 150, 20));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setPreferredSize(new Dimension(200, 60));
        startButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        startButton.addActionListener(e -> {
            stopIntroMusic();
            startGame();
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        optionsPanel.add(startButton, gbc);
        add(optionsPanel, BorderLayout.CENTER);
    }

    private void playIntroMusic(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            introMusicClip = AudioSystem.getClip();
            introMusicClip.open(audioStream);
            introMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            introMusicClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing intro music: " + e.getMessage());
        }
    }
    private void stopIntroMusic() {
        if (introMusicClip != null && introMusicClip.isRunning()) {
            introMusicClip.stop();
            introMusicClip.close();
        }
    }

    private void startGame() {
        String maze = maze1.isSelected() ? "Maze1" : maze2.isSelected() ? "Maze2" : "Maze3";
        new GameFrame("easy", maze);  // Difficulty is fixed as "easy"
        dispose();  // Close the launcher window
    }
}
