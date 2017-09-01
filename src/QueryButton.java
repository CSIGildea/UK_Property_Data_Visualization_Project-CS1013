package project10;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * @author Catalina
 *
 */
public class QueryButton {
	final int NO_EVENT = -1;
	int xpos, ypos;
	float height, width;
	int event;
	PApplet parent;
	PImage clicked;
	PImage unClicked;
	boolean hover = false, tint = false;

	QueryButton(int x, int y, float width, float height, PImage clicked, PImage unClicked, int event, Project10 p) {
		this.xpos = x;
		this.ypos = y;
		this.height = height;
		this.width = width;
		this.clicked = clicked;
		this.unClicked = unClicked;
		parent = p;
		this.event = event;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return width;
	}

	public int getY() {
		return ypos;
	}

	public void changeY(QueryButton Q) {
		ypos = (int) (Q.getY() + (Q.getHeight() / 10) * 2.5);
	}

	public int getEvent(int mX, int mY) {
		if (mX > xpos && mX < xpos + width && mY > ypos && mY < ypos + height) {
			hover = true;
			return event;
		}
		hover = false;
		return NO_EVENT;
	}

	public void clearBorder() {
		hover = false;
	}

	public void changeImage(PImage image, PImage image2) {
		unClicked = image2;
		clicked = image;
	}

	public void draw() {
		if (hover) {
			if (tint) {
				parent.tint(74, 214, 42);
			}
			parent.image(unClicked, xpos, ypos, width, height);
			parent.tint(255);
		} else {
			parent.image(clicked, xpos, ypos, width, height);
		}
	}

}
