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

//This "Ball" class extends the "Structure" class. It is for the ball used in the game.

//Imports

import javax.swing.*;
import java.awt.*;
import java.util.Random;

//Class definition
public class Ball extends Structure {
    //Variables
    private boolean onScreen;
    private float xDir = 1, yDir = -1;
    private Board parent;

    public Ball(Board parent) {
        this(Constants.BALL_X_START, Constants.BALL_Y_START, Constants.BALL_WIDTH, Constants.BALL_HEIGHT, Color.BLACK);
        this.parent = parent;
    }

    //Constructor
    public Ball(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color);
        setOnScreen(true);
        randomizeStart();

    }
    private void randomizeStart(){
        int scaledWidth = Constants.WINDOW_WIDTH / 3;
        int xPosition = getRandomNumberInRange(-scaledWidth, scaledWidth);
        this.x += xPosition;

        //Random rand = new Random();
        //yDir = (rand.nextInt() %2 == 0  ? -1 : 1) * rand.nextFloat();

    }

    //Draw the ball
    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, width, height);
    }

    //Moves the ball
    public void move() {
        x += xDir;
        y += yDir;
    }

    //Resets the ball to original position at center of screen
    public void reset() {
        x = Constants.BALL_X_START;
        y = Constants.BALL_Y_START;
        xDir = 1;
        yDir = -1;
        randomizeStart();
    }

    //Mutator methods
    public void setXDir(int xDir) {
        this.xDir = xDir;
    }

    public void invertX() {
        this.xDir = -this.xDir;
    }

    public void invertY() {
        this.yDir = -this.yDir;
    }


    public void setYDir(int yDir) {
        this.yDir = yDir;
    }

    public void setOnScreen(boolean onScreen) {
        this.onScreen = onScreen;
    }

    //Accessor methods
    public float getXDir() {
        return xDir;
    }

    public float getYDir() {
        return yDir;
    }

    public boolean isOnScreen() {
        return onScreen;
    }


    public void checkCollisions() {
        if (getX() >= parent.getWidth() - getWidth() || getX() <= 0) {
            invertX();
        }
        if (getY() <= 0 || getY() >= parent.getHeight()) {
            invertY();
        }

        if (getY() > Constants.PADDLE_Y_START + 10) {
            parent.outOfBounds();
        }
    }


    private int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}
