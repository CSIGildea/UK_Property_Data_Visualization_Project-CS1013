package project10;


import processing.core.PImage;

public class Hamburger {
	public static final int NO_EVENT = -1;
	int xpos, ypos; 
	float width, height;
	PImage image;
	int event;
	Project10 parent;

	Hamburger(int x, int y, float width, float height, PImage image, int event, Project10 p) {

		xpos = x;
		ypos = y;
		this.width = width;
		this.height = height;
		this.image = image;
		this.event = event;
		parent = p;
	}
	public float getHeight(){
		return height;
	}
	public int getEvent(int mX, int mY) {
		if (mX > xpos && mX < xpos + width && mY > ypos && mY < ypos + height) {
			return event;
		}
		return NO_EVENT;
	}
	public int getY(){
		return ypos;
	}
	public void draw() {
		parent.image(image, xpos, ypos, width, height);
	}
}


