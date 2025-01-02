# Pacman Game - Applying OS Concepts

## Overview
This is a simple **Pacman** game built in Java, showcasing several **Operating System (OS) concepts**. The project focuses on implementing key OS principles such as **multithreading**, **process synchronization**, and **event handling** to create a functional game environment. The game mimics the classic arcade **Pacman** game, where the player navigates Pacman through a maze while avoiding ghosts and collecting pellets.

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [OS Concepts Applied](#os-concepts-applied)
- [Game Instructions](#game-instructions)
- [Installation](#installation)
- [Contributing](#contributing)
- [License](#license)

## Features
- Classic **Pacman gameplay** with 3 mazes navigation.
- **Ghost AI** that follows Pacman around the maze.
- **Multithreading** to handle real-time game mechanics (Pacman movement, ghost AI, and user input).
- **Event handling** for user input and collision detection.
- **Basic graphics** using Java Swing for the GUI.
- **Score tracking** and **game over screen with points high tracker**.

## Technologies Used
- **Java**: Programming language used to develop the game.
- **Java Swing**: For creating the graphical user interface (GUI).
- **Multithreading**: To handle concurrent tasks (game loop, ghost movement, and user input).

## OS Concepts Applied
1. **Multithreading**: 
   - The game utilizes multiple threads to handle the movement of Pacman and ghosts, as well as user input.
   - The game loop and ghost AI run on separate threads to simulate real-time gameplay.

2. **Process Synchronization**: 
   - Synchronization is used to ensure that Pacman and the ghosts don't interfere with each other’s movements, preventing race conditions and ensuring smooth interaction with the maze.

3. **Event Handling**:
   - Key events such as user input (keyboard presses) are handled through event listeners, ensuring that player actions are captured and responded to in real time.

4. **Deadlock Prevention**:
   - The game's logic ensures that Pacman and ghosts can never be "trapped" in a way that would cause a deadlock or a freeze in the game.

5. **Memory Management**:
   - Objects are properly created and destroyed, ensuring efficient use of memory during the game’s execution, with a focus on handling images and sounds dynamically.

6. **Process Creation and Termination**:
   - New processes are created for managing individual threads for different game elements (e.g., Pacman’s movement, ghost AI).
   - The processes are terminated properly when the game ends or when Pacman dies.

## Game Instructions
1. **Start the Game**:
   - To begin, run the program by executing `PacManGame.java`.
   
2. **Game Controls**:
   - Use the **arrow keys** to navigate Pacman through the maze:
     - **Up Arrow**: Move Up
     - **Down Arrow**: Move Down
     - **Left Arrow**: Move Left
     - **Right Arrow**: Move Right
   - Avoid the ghosts while collecting the pellets.

3. **Objective**:
   - The goal is to collect all the pellets in the maze while avoiding being caught by the ghosts. Collecting the fruits gives you extra points.

4. **Game Over**:
   - The game ends when Pacman collides with a ghost. You can restart the game by clicking the “Restart” button after the game over screen.

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/merna112/Pacman-Game---Applying-OS-Concepts.git
