package project10;

import processing.core.PImage;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

public class RadioWidget extends Widget {
	int BEIGE;
	PFont hoverLabelFont;

	RadioWidget(Project10 p, float f, float y, int year, PImage radioImage) {
		parent = p;
		hoverLabelFont = parent.createFont("Arial Rounded MT Bold", parent.widthByPercent(1));
		this.x = f;
		this.y = y;
		this.year = year;
		this.radioImage = radioImage;
		width = 25;
		height = 25;
	}

	// SH - draw radio buttons and label on hover
	public void setFont(String fontS){
		//font = parent.createFont(fontS,parent.widthByPercent(1)); //~=12
	}
	void draw() {
		parent.image(radioImage, x, y);
		if (PApplet.dist(parent.mouseX, parent.mouseY, x + (width / 2), y + (height / 2)) < 15) {
			parent.fill(220);
			parent.textFont(hoverLabelFont);
			float textWidth = parent.textWidth(Integer.toString(year));
			float boxWidth = textWidth + Project10.DISPLAY_PADDING;
			float textHeight = parent.textAscent() + parent.textDescent();
			float boxHeight = textHeight + Project10.DISPLAY_PADDING;
			parent.rect(parent.mouseX - Project10.DISPLAY_PADDING*6, parent.mouseY - Project10.DISPLAY_PADDING*4, boxWidth, boxHeight, 10);
			parent.fill(0);
			parent.textAlign(PConstants.CENTER, PConstants.CENTER);
			parent.text(year, parent.mouseX - Project10.DISPLAY_PADDING*3, parent.mouseY - Project10.DISPLAY_PADDING*8/3);
		}
	}
	public void changeX(float x){
		this.x=x;
	}
	public void changeY(float y){
		this.y=y;
	}
}
