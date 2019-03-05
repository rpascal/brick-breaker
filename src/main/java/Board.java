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
import java.util.concurrent.atomic.AtomicBoolean;

//Class definition
public class Board extends JPanel implements Runnable {
    //Items on-screen
    private Paddle paddle;
    private Ball ball;
    //	private Brick[][] brick = new Brick[10][5];
    private java.util.List<Brick> bricks = new ArrayList<Brick>();

    //Initial Values for some important variables
    private int score = 0, lives = Constants.MAX_LIVES, bricksLeft = Constants.MAX_BRICKS, waitTime = 3, xSpeed;

    //The game
    private Thread game;

    //Data structures to handle high scores
    private ArrayList<Item> items = new ArrayList<>();


    //Constructor
    public Board(int width, int height) {
        super.setSize(width, height);
        addKeyListener(new BoardListener());
        setFocusable(true);

        makeBricks();
        paddle = new Paddle(Constants.PADDLE_X_START, Constants.PADDLE_Y_START, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, Color.BLACK);
        ball = new Ball(Constants.BALL_X_START, Constants.BALL_Y_START, Constants.BALL_WIDTH, Constants.BALL_HEIGHT, Color.BLACK);


        game = new Thread(this);
        game.start();
    }

    //fills the array of bricks
    public void makeBricks() {
        bricks = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                Brick brick = new Brick(i, j);

                brick.onDestoryEvents.add(e -> {
                    bricksLeft--;
                    score += 50;
                    addItem(brick.item);
                });

                bricks.add(brick);
            }
        }
    }



    //runs the game
    public void run() {
        xSpeed = 1;
        while (true) {
            int x1 = ball.getX();
            int y1 = ball.getY();

            //Makes sure speed doesnt get too fast/slow
            if (Math.abs(xSpeed) > 1) {
                if (xSpeed > 1) {
                    xSpeed--;
                }
                if (xSpeed < 1) {
                    xSpeed++;
                }
            }

            checkPaddle(x1, y1);
            checkWall(x1, y1);
            checkBricks(x1, y1);
            checkIfOut(y1);
            ball.move();
            dropItems();
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

    public void dropItems() {
        for (int i = 0; i < items.size(); i++) {
            Item tempItem = items.get(i);
            tempItem.drop();
            items.set(i, tempItem);
        }
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
        if (bricksLeft == Constants.NO_BRICKS) {

            ball.reset();
            bricksLeft = Constants.MAX_BRICKS;
            makeBricks();
            lives++;
            score += 100;

        }
        if (lives == Constants.MIN_LIVES) {
            resetGame();
        }
    }

    public void checkPaddle(int x1, int y1) {
        if (paddle.hitPaddle(x1, y1) && ball.getXDir() < 0) {
            ball.setYDir(-1);
            xSpeed = -1;
            ball.setXDir(xSpeed);
        }
        if (paddle.hitPaddle(x1, y1) && ball.getXDir() > 0) {
            ball.setYDir(-1);
            xSpeed = 1;
            ball.setXDir(xSpeed);
        }

        if (paddle.getX() <= 0) {
            paddle.setX(0);
        }
        if (paddle.getX() + paddle.getWidth() >= getWidth()) {
            paddle.setX(getWidth() - paddle.getWidth());
        }
    }

    public void checkWall(int x1, int y1) {
        if (x1 >= getWidth() - ball.getWidth()) {
            xSpeed = -Math.abs(xSpeed);
            ball.setXDir(xSpeed);
        }
        if (x1 <= 0) {
            xSpeed = Math.abs(xSpeed);
            ball.setXDir(xSpeed);
        }
        if (y1 <= 0) {
            ball.setYDir(1);
        }
        if (y1 >= getHeight()) {
            ball.setYDir(-1);
        }
    }

    public void checkBricks(int x1, int y1) {

        for (Brick brick : bricks) {
            if (brick.hitBottom(x1, y1)) {
                ball.setYDir(1);

            }
            if (brick.hitLeft(x1, y1)) {
                xSpeed = -xSpeed;
                ball.setXDir(xSpeed);

            }
            if (brick.hitRight(x1, y1)) {
                xSpeed = -xSpeed;
                ball.setXDir(xSpeed);

            }
            if (brick.hitTop(x1, y1)) {
                ball.setYDir(-1);

            }
        }

    }

    public void checkIfOut(int y1) {
        if (y1 > Constants.PADDLE_Y_START + 10) {
            lives--;
            score -= 100;
            ball.reset();
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

    public void resetGame(){
        paddle.setWidth(getWidth() / 7);
        lives = Constants.MAX_LIVES;
        score = 0;
        bricksLeft = Constants.MAX_BRICKS;
        makeBricks();
    }


    //Private class that handles gameplay and controls
    private class BoardListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent ke) {
            int key = ke.getKeyCode();

            if (key == KeyEvent.VK_LEFT) {
                paddle.setX(paddle.getX() - 50);
            }
            if (key == KeyEvent.VK_RIGHT) {
                paddle.setX(paddle.getX() + 50);
            }
        }

        @Override
        public void keyReleased(KeyEvent ke) {
            int key = ke.getKeyCode();
            if (key == KeyEvent.VK_LEFT) {
                paddle.setX(paddle.getX());
            }
            if (key == KeyEvent.VK_RIGHT) {
                paddle.setX(paddle.getX());
            }
        }
    }
}
