package project10;

import processing.core.PFont;
import processing.core.PImage;

public class Widget {
	Project10 parent;
	float x;
	float y;
	int width, height, year;
	String label;
	int widgetColor, labelColor, borderColor;
	PFont widgetFont;
	boolean mouseOver = false;
	PImage radioImage, arrowImage;
	boolean pressed;

	public int getEvent(int mX, int mY) {
		if (mX > x && mX < x + width && mY > y && mY < y + height) {
			mouseOver = true;
			return year;
		} else
			mouseOver = false;
		return Project10.EVENT_NULL;
	}
}