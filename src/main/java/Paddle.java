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

//This "Paddle" class extends the "Structure" class. It is used for the player's paddle in the game.

//Imports

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//Class definition
public class Paddle extends Structure {
    //Variables
    private int xSpeed;
    private Ball ball;
    private JPanel parent;
    private PaddleController pc;
    private  int  stepRate = 1;

    public Paddle(Ball ball, Board parent) {
        this(Constants.PADDLE_X_START, Constants.PADDLE_Y_START, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, Color.BLACK);
        this.ball = ball;
        this.parent = parent;
        pc = new PaddleController(this.ball, this, parent);
    }

    //Constructor
    public Paddle(int x, int y, int width, int height, Color color)
    {
        super(x, y, width, height, color);
    }

    //Places the paddle back in starting position at center of screen
    public void reset() {
        x = Constants.PADDLE_X_START;
        y = Constants.PADDLE_Y_START;
    }

    //Checks if the ball hit the paddle
    public boolean hitPaddle(int ballX, int ballY) {
        if ((ballX >= x) && (ballX <= x + width) && ((ballY >= y) && (ballY <= y + height))) {
            return true;
        }
        return false;
    }

    //Resizes the paddle if it touches an item, then returns true or false
    public boolean caughtItem(Item i) {
        if ((i.getX() < x + width) && (i.getX() + i.getWidth() > x) && (y == i.getY() || y == i.getY() - 1)) {
            i.resizePaddle(this);
            return true;
        }
        return false;
    }

    public void checkCollisions() {
        int x1 = ball.getX();
        int y1 = ball.getY();

        if (hitPaddle(x1, y1)) {
            ball.invertY();
        }
        if (getX() <= 0) {
            setX(0);
        }
        if (getX() + getWidth() >= parent.getWidth()) {
            setX(parent.getWidth() - getWidth());
        }
    }

    public void tick()
    {
        pc.MoveTowardBall();
        checkCollisions();

    }

    public void stepLeft(){
        setX(getX() - stepRate);
    }

    public void stepRight(){
        setX(getX() + stepRate);

    }
}
