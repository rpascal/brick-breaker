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

//This "Item" class extends the "Structure" class. It is used for the items that fall from some bricks.

//Imports
import java.awt.*;
import java.util.Random;


enum ItemType {
	INCREASE_SIZE,
	DECREASE_SIZE,
	SPLIT_BALL;


	public static ItemType getRandom() {
		Random random = new Random();
		return values()[random.nextInt(values().length)];
	}

}

//Class definition
public class Item extends Structure {
	//Variables
	private ItemType type;

	//Constructor
	public Item(int x, int y, int width, int height, Color color, ItemType type) {
		super(x, y, width, height, color);
		setType(type);
	}

	//Drop the item down towards the paddle at slow pace
	public void tick() {
		y += 2;
	}

	//Resize the paddle, depending on which item is caught. Changes in increments of 15 until min/max width is reached.
	public void resizePaddle(Paddle p) {
		switch(type){
			case INCREASE_SIZE:
				p.setWidth(p.getWidth() + 15);
				break;
			case DECREASE_SIZE:
				p.setWidth(p.getWidth() - 15);
				break;
		}
	}

	public boolean split(){
		return type == ItemType.SPLIT_BALL;
	}

	//Set the item's type
	public void setType(ItemType type) {
		this.type = type;
	}

	//Get the item's type
	public ItemType getType() {
		return type;
	}

	public boolean isGood(){
		switch(type){
			case INCREASE_SIZE:
			case SPLIT_BALL:
				return true;
			case DECREASE_SIZE:
				return false;
		}
		return false;
	}
}
