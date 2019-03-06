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

//This "Brick" class extends the "Structure" class. It is for the bricks used in the game.

//Imports

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Class definition
public class Brick extends Structure {
    //Variables
    private int lives, hits;
    private boolean destroyed;
    public Item item;
    private Color itemColor;
    private int row;
    private int col;
    private Ball ball;

    List<ActionListener> onDestoryEvents = new ArrayList<>();

    //Colors for the bricks


    public Brick(int row, int col, Ball ball) {
        this(
                (row * Constants.BRICK_WIDTH),
                ((col * Constants.BRICK_HEIGHT) + (Constants.BRICK_HEIGHT / 2)),
                Constants.BRICK_WIDTH - 5,
                Constants.BRICK_HEIGHT - 5,
                Constants.colors[(new Random()).nextInt(7)][0],
                1,
                (new Random()).nextInt(3) + 1);

        this.ball = ball;
        this.row = row;
        this.col = col;
    }


    //Constructor
    public Brick(int x, int y, int width, int height, Color color, int lives, int itemType) {
        super(x, y, width, height, color);
        setLives(lives);
        setHits(0);
        setDestroyed(false);

        if (itemType == 1) {
            itemColor = Color.GREEN;
        }
        if (itemType == 2) {
            itemColor = Color.RED;
        }

        //Places an item of specified type inside the brick to fall when the brick is destroyed
        item = new Item(x + (width / 4), y + (height / 4), Constants.ITEM_WIDTH, Constants.ITEM_HEIGHT, itemColor, itemType);
    }

    //Draws a brick
    @Override
    public void draw(Graphics g) {
        if (!destroyed) {
            g.setColor(color);
            g.fillRect(x, y, width, height);
        }
    }

    //Add a hit to the brick, and destroy the brick when hits == lives
    public void addHit() {
        hits++;
        nextColor();
        if (hits == lives) {
            setDestroyed(true);
        }
    }

    //Change color to get lighter until the brick is destroyed
    public void nextColor() {
        if (color == Constants.colors[0][0] || color == Constants.colors[0][1] || color == Constants.colors[0][2]) {
            color = Constants.blueColors[hits];
        }
        if (color == Constants.colors[1][0] || color == Constants.colors[1][1] || color == Constants.colors[1][2]) {
            color = Constants.redColors[hits];
        }
        if (color == Constants.colors[2][0] || color == Constants.colors[2][1] || color == Constants.colors[2][2]) {
            color = Constants.purpleColors[hits];
        }
        if (color == Constants.colors[3][0] || color == Constants.colors[3][1] || color == Constants.colors[3][2]) {
            color = Constants.yellowColors[hits];
        }
        if (color == Constants.colors[4][0] || color == Constants.colors[4][1] || color == Constants.colors[4][2]) {
            color = Constants.pinkColors[hits];
        }
        if (color == Constants.colors[5][0] || color == Constants.colors[5][1] || color == Constants.colors[5][2]) {
            color = Constants.grayColors[hits];
        }
        if (color == Constants.colors[6][0] || color == Constants.colors[6][1] || color == Constants.colors[6][2]) {
            color = Constants.greenColors[hits];
        }
    }

    public void checkCollisions(){
        int x1 = ball.getX();
        int y1 = ball.getY();

        if (hitBottomTop(x1, y1)) {
            ball.invertY();
        }
        if (hitLeftRight(x1, y1)) {
            ball.invertX();
        }
    }

    //Detect if the brick has been hit on its bottom, top, left, or right sides
    public boolean hitBottomTop(int ballX, int ballY) {
        if (destroyed) {
            return false;
        }

        if (
                ((ballX >= x) && (ballX <= x + width + 1) && (ballY == y + height)) // Bottom
                        || ((ballX >= x) && (ballX <= x + width + 1) && (ballY == y)) // top
        ) {
            addHit();
            return true;
        }

        return false;
    }


    public boolean hitLeftRight(int ballX, int ballY) {
        if (destroyed) {
            return false;
        }

        if (
                ((ballY >= y) && (ballY <= y + height) && (ballX == x)) // Left
                        || ((ballY >= y) && (ballY <= y + height) && (ballX == x + width)) // Right
        ) {
            addHit();
            return true;
        }
        return false;
    }

    //Mutator methods
    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
        for (ActionListener onDestroy : onDestoryEvents) {
            onDestroy.actionPerformed(new ActionEvent(this, 0, ""));
        }
    }

    //Accessor methods
    public int getLives() {
        return lives;
    }

    public int getHits() {
        return hits;
    }

    public boolean isDestroyed() {
        return destroyed;
    }


}
