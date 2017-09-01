package project10;

public class SubScreen {
	int xpos;
	int ypos;
	float height;
	float width;
	Project10 parent;
	boolean pressed = false;
	int CHARCOAL;

	SubScreen(int x, int y, float width, float height, Project10 p) {
		xpos = x;
		ypos = y;
		this.width = width;
		this.height = height;
		parent = p;
		CHARCOAL = parent.color(46);
	}

	public void changeHeight() {
		if (pressed)
			pressed = false;
		else
			pressed = true;
	}

	public int getX() {
		return xpos;
	}

	public float getWidth() {
		return width;
	}

	public void draw() {
		parent.stroke(80);
		parent.fill(CHARCOAL);
		if (!pressed) {
			parent.rect(xpos, ypos, width, height);
		} else
			parent.rect(xpos, ypos, width, parent.displayHeight);
	}

	public int getY() {

		return ypos;
	}
}
