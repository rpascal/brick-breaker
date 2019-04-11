/*
 *    Brick Breaker, Version 1.2
 *    By Ty-Lucas Kelley
 *
 *	 **LICENSE**
 *
 *	 This file is a part of Brick Breaker.
 *
 *	 Brick Breaker is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Brick Breaker is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with Brick Breaker.  If not, see <http://www.gnu.org/licenses/>.
 */

//This "Board" class handles all game logic and displays items on the screen.

//Imports

import java.awt.*;
import javax.swing.*;
import java.lang.Thread;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

//Class definition
public class Board extends JPanel implements Runnable {
    //Items on-screen
    private Paddle paddle;
    private Ball ball;

    private java.util.List<Brick> bricks = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();

    private int score = 0, lives = Constants.MAX_LIVES, bricksLeft = 0, waitTime = 3;

    private Thread game;

    List<ActionListener> onClearBricksListners = new ArrayList<>();
    List<ActionListener> onOutOfBoundsListners = new ArrayList<>();
    List<ActionListener> onLoseListners = new ArrayList<>();


    //Constructor
    public Board(int width, int height) {
        super.setSize(width, height);
        addKeyListener(new BoardListener());
        setFocusable(true);
        ball = new Ball(this);

        makeBricks();
        paddle = new Paddle(ball, this);


        onClearBricksListners.add(e -> {
            ball.reset();
            makeBricks();
            lives++;
            score += 100;
        });

        onLoseListners.add(e -> {
            resetGame();
        });

        onOutOfBoundsListners.add(e -> {
            lives--;
            score -= 100;
            ball.reset();
        });

        game = new Thread(this);
        game.start();
    }

    //fills the array of bricks
    public void makeBricks() {
        bricksLeft = 0;

        bricks = new ArrayList<>();
        int horizontalBricks = Constants.WINDOW_WIDTH / Constants.BRICK_WIDTH;

        for (int i = 0; i < horizontalBricks; i++) {
            for (int j = 0; j < 5; j++) {
                Brick brick = new Brick(i, j, ball);

                brick.onDestoryEvents.add(e -> {
                    bricksLeft--;
                    score += 50;
                    addItem(brick.item);
                });
                bricksLeft++;
                bricks.add(brick);
            }
        }
    }


    //runs the game
    public void run() {
        while (true) {


            paddle.tick();
            ball.tick();

            for (Brick brick : bricks) {
                brick.checkCollisions();
            }

            for (Item item : items) {
                item.tick();
            }
            checkItemList();

            checkLives();


            repaint();
            try {
                game.sleep(waitTime);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }


    public void addItem(Item i) {
        items.add(i);
    }


    public void checkItemList() {
        for (int i = 0; i < items.size(); i++) {
            Item tempItem = items.get(i);
            if (paddle.caughtItem(tempItem) || tempItem.getY() > Constants.WINDOW_HEIGHT) {
                items.remove(i);
            }
        }
    }

    public void checkLives() {
        if (bricksLeft == 0) {
            for (ActionListener onClearBricks : onClearBricksListners) {
                onClearBricks.actionPerformed(new ActionEvent(this, 0, "bricks cleared"));
            }
        }
        if (lives == Constants.MIN_LIVES) {
            for (ActionListener onLose : onLoseListners) {
                onLose.actionPerformed(new ActionEvent(this, 0, "on lose"));
            }
        }
    }

    public void outOfBounds() {
        for (ActionListener onOutOfBounds : onOutOfBoundsListners) {
            onOutOfBounds.actionPerformed(new ActionEvent(this, 0, "on out of bounds"));
        }
    }

    //fills the board
    @Override
    public void paintComponent(Graphics g) {
        Toolkit.getDefaultToolkit().sync();
        super.paintComponent(g);
        paddle.draw(g);
        ball.draw(g);

        for (Brick brick : bricks) {
            brick.draw(g);
        }
        g.setColor(Color.BLACK);

        g.drawString("Lives: " + lives, 10, getHeight() - (getHeight() / 10));
        g.drawString("Score: " + score, 10, getHeight() - (2 * (getHeight() / 10)) + 25);

        for (Item i : items) {
            i.draw(g);
        }
    }

    public void resetGame() {
        paddle.setWidth(getWidth() / 7);
        lives = Constants.MAX_LIVES;
        score = 0;
        makeBricks();
    }


    private class BoardListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent ke) {
            int key = ke.getKeyCode();

            if (key == KeyEvent.VK_LEFT) {
                paddle.stepLeft();
            }
            if (key == KeyEvent.VK_RIGHT) {
                paddle.stepRight();
            }
        }
    }
}
