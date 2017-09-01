
package project10;

import processing.core.PConstants;
import processing.core.PFont;

/**
 * inspired by code received during lecture improved by Catalina
 *
 */
public class TextWidget {
	public static final int NO_EVENT = -1;
	int maxlen;
	float x, y;
	float width, height, temporaryWidth = 0;
	String label;
	int color;
	PFont font;
	int event;
	boolean defaultText = true;
	boolean retract = false;
	Project10 parent;
	int CHARCOAL;

	TextWidget(float x, float y, float width, float height, String label, int event, int maxlen, Project10 p) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.label = label;
		this.event = event;
		this.maxlen = maxlen;
		parent = p;
		CHARCOAL = parent.color(46);
	}

	public void clear() {
		label = "|";
		defaultText = false;
	}

	public void waitingForText() {
		if (!label.equals("") && label.charAt(label.length() - 1) == '|') {
			label = label.substring(0, label.length() - 1);
		} else {
			label = label + "|";
		}
	}

	public void setTemporaryWidth(boolean retract) {
		this.retract = retract;
		if (!retract) {
			temporaryWidth = 0;
		}
		
	}

	public int getEvent(int mX, int mY) {
		if (mX > x && mX < x + width && mY > y && mY < y + height) {
			;
			return event;
		}
		return NO_EVENT;
	}

	public void setFont(String fontS) {
		font = parent.createFont(fontS, parent.widthByPercent(1.5)); // ~=18
	}

	public void draw() {
		parent.fill(CHARCOAL);
		parent.stroke(80);
		parent.rect(x, y, temporaryWidth, height);
		if (defaultText) {
			parent.fill(160);

		} else {
			parent.fill(220);
		}
		parent.textAlign(PConstants.LEFT, PConstants.LEFT);
		parent.textFont(font);
		if(!retract)
		parent.text(label, x + 10, y + height / 2 + 10);
		if (temporaryWidth < width && !retract) {
			temporaryWidth += 100;
		} else if(retract && temporaryWidth>Project10.hamburgerButton.width){
			temporaryWidth -= 100;
		}

	}

	public void append(char s) {
		if ((s >= 'A' && s <= 'z') || (s >='0' && s <='9') || s=='\b' || s == ' ') {
			if (!label.equals("") && label.charAt(label.length() - 1) == '|')
				label = label.substring(0, label.length() - 1);
			if (s == '\b') {
				if (!label.equals("|") && !label.equals(""))
					label = label.substring(0, label.length() - 1);
			} else if (label.length() < maxlen)
				label = label + s;
		}
	}
}
