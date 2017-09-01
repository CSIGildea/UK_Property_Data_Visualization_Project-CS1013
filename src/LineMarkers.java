package project10;

import java.text.DecimalFormat;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

public class LineMarkers {
	Project10 parent;
	float radius;
	String label;
	int value;
	boolean mouseOver;
	String county;
	float x;
	float y;
	float color;
	PFont hoverLabelFont;

	public LineMarkers(Project10 p,float x, float y, float radius, int value, String county, float color){
		parent = p;
		hoverLabelFont = parent.createFont("Arial Rounded MT Bold", parent.widthByPercent(1.2));
		this.value = value;
		this.county = county;
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.color = color;
	}

	public void display(){
		parent.fill(color);
		parent.ellipse(x, y, radius, radius);
	}
	public void setFont(String fontS){
		 //font = parent.createFont(fontS,parent.widthByPercent(1.8));  //~24
	}
	boolean overCircle() {
		float disX = x - parent.mouseX;
		float disY = y - parent.mouseY;
		if(PApplet.sqrt(PApplet.sq(disX) + PApplet.sq(disY)) < radius ) {
			DecimalFormat formatter = new DecimalFormat("#,###");
			String text = (county+": "+ (Project10.analysisType.equals("Average price") ? "£"
					: (Project10.analysisType.equals("Maximum price") ? "£" : "")) + formatter.format(value));
			parent.textFont(hoverLabelFont);
			float textWidth = parent.textWidth(text);
			float boxWidth = textWidth + Project10.DISPLAY_PADDING*2;
			float textHeight = parent.textAscent()+parent.textDescent();
			float boxHeight = textHeight + Project10.DISPLAY_PADDING*2;
			parent.strokeWeight(2);
			parent.fill(220);
			parent.rect(parent.mouseX-(boxWidth/2), parent.mouseY - boxHeight - Project10.DISPLAY_PADDING, boxWidth, boxHeight, 20);
			parent.fill(0);
			parent.textAlign(PConstants.CENTER, PConstants.CENTER);
			parent.text(text, parent.mouseX, parent.mouseY - boxHeight/2 - Project10.DISPLAY_PADDING*3/2);
			return true;
		}
		return false;
	}
	
	float returnX(){
		return x;
	}
	
	float returnY(){
		return y;
	}
}

