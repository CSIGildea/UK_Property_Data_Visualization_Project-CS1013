package project10;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

/**
 * @author Catalina
 *
 */
public class Checkbox extends QueryButton {
	public static final int NO_EVENT = -1;
	int x, y;
	float width, height;
	PImage clicked, unClicked;
	int event;
	PApplet parent;
	String label;
	boolean tint = false, pressed = false;
	int CHARCOAL;

	Checkbox(int x, int y, float width, float height, PImage clicked, PImage unClicked, int event, String label,
			Project10 p) {
		super(x, y, width, height, clicked, unClicked, event, p);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.clicked = clicked;
		this.unClicked = unClicked;
		this.event = event;
		this.label = label;
		parent = p;
		CHARCOAL = parent.color(46);
	}

	public float getHeight() {
		return height;
	}

	public String getLabel() {
		return label;
	}

	public void clear() {
		pressed = false;
	}

	public void set() {
		pressed = true;
	}

	public void changeState() {
		if (pressed)
			pressed = false;
		else
			pressed = true;
	}

	public void setDefault() {
		pressed = true;
	}

	public int getEvent(int mX, int mY) {
		if (mX > xpos && mX < xpos + width && mY > ypos && mY < ypos + height) {
			return event;
		}
		return NO_EVENT;
	}

	public void changeImages(PImage image1, PImage image2) {
		unClicked = image1;
		clicked = image2;
	}

	public void draw() {
		parent.fill(CHARCOAL);
		parent.stroke(80);
		parent.rect(xpos, ypos, width, height);
		if (!pressed) {
			parent.image(unClicked, xpos + xpos / 150, ypos + ypos / 100);
		} else {
			parent.image(clicked, xpos + xpos / 150, ypos + ypos / 100);
		}
		String alternativelabel = label;
		String label2 = null;
		Boolean secondtext = false;
		if (label.length() > 14 && label.contains(" ")) {
			String[] words = label.split(" ");
			alternativelabel = "";
			label2 = "";
			secondtext = true;
			//int mid= label.length()/2;
			//int length=0;
			for (int i = 0; i < words.length; i++) {
				//length += words[i].length();
				if (i>=words.length/2) {
					label2 += words[i] + " ";
				} else {
					alternativelabel += words[i] + " ";
				}
			}
		}
		float level = height/4;
		float levelW=width/12;
		parent.fill(220);
		parent.textFont(Project10.checkboxFont);
		if (!secondtext) {
			parent.textAlign(PConstants.CENTER, PConstants.CENTER);
			parent.text(alternativelabel, (float) (xpos + width / 2+levelW), (float) (ypos + height / 2));
		}
		else{
			parent.textAlign(PConstants.CENTER, PConstants.CENTER);
			parent.text(alternativelabel, (float) (xpos + width / 2+levelW), (float) (ypos + level));
			parent.text(label2, (float) (xpos + width / 2+levelW), (float) (ypos + level*3));
		}

	}

	public void draw(int x, int y) {
		xpos = x;
		ypos = y;
		parent.fill(CHARCOAL);
		parent.stroke(80);
		parent.rect(x, y, width, height);
		if (!pressed) {

			parent.image(unClicked, x + x / 150, y + y / 100);

		} else {
			parent.image(clicked, x + x / 150, y + y / 100);
		}
		parent.fill(220);
		parent.textAlign(PConstants.CENTER, PConstants.CENTER);
		parent.textFont(Project10.checkboxFont);
		String alternativelabel = label;
		String label2 = null;
		Boolean secondtext = false;
		if (label.length() > 14 && label.contains(" ")) {
			String[] words = label.split(" ");
			alternativelabel = "";
			label2 = "";
			secondtext = true;
			int mid= label.length()/2;
			int length=0;
			for (int i = 0; i < words.length; i++) {
				length += words[i].length();
				if (length>mid && i>0) {
					label2 += words[i] + " ";
				} else {
					alternativelabel += words[i] + " ";
				}
			}
		}
		float level = height/4;
		float levelW=width/12;
		if (!secondtext) {
			parent.textAlign(PConstants.CENTER, PConstants.CENTER);
			parent.text(alternativelabel, (float) (x + width / 2+levelW), (float) (y + height / 2));
		}
		else{
			parent.textAlign(PConstants.CENTER, PConstants.CENTER);
			parent.text(alternativelabel, (float) (x + width / 2+levelW), (float) (y + level));
			parent.text(label2, (float) (x + width / 2+levelW), (float) (y + level*3));
		}
	}
}