package project10;
import java.text.DecimalFormat;

import processing.core.PApplet;
import processing.core.PFont;

public class PieArc 
{
	Project10 parent;
	float angle;
	float x;
	float y;
	float diameter;
	float lastAngle;
	float color;
	float red;
	float green;
	float blue;
	float highlightRed;
	float highlightGreen;
	float highlightBlue;
	float currentRed;
	float currentGreen;
	float currentBlue;
	float currentColor;
	String county;
	int average;
	PFont hoverLabelFont;
	boolean done;
	float currentAngle;
	boolean skip;

	PieArc(Project10 p, float x, float y, float angle, float lastAngle, float diameter, float red, float green, float blue, String county, int average)
	{
		this.parent = p;
		hoverLabelFont = parent.createFont("Arial Rounded MT Bold", parent.widthByPercent(1.2));
		this.angle = angle;
		this.x = x;
		this.y = y;
		this.diameter = diameter;
		this.lastAngle = lastAngle;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.highlightRed = this.red+50;
		this.highlightGreen = this.green+50;
		this.highlightBlue = this.blue+50;
		if(highlightRed>255) highlightRed = 255;
		if(highlightGreen>255) highlightGreen = 255;
		if(highlightBlue>255) highlightBlue = 255;
		this.currentRed = this.red;
		this.currentGreen = this.green;
		this.currentBlue = this.blue;
		this.county = county;
		this.average = average;
		done = false;
		currentAngle = 0;
		//System.out.println(lastAngle + "  " + (lastAngle+parent.radians(angle)));
	}

	public void display()
	{
		if(skip){currentAngle = angle; done = true;}	
		parent.fill(currentRed, currentGreen, currentBlue);
		if((currentAngle)<(angle))
			{
				currentAngle+=3;
				done = false;
				Project10.firstPie();
			}
		if((currentAngle)>(angle))
			{
				currentAngle = angle;
				done = true;
			}
		parent.arc(x, y, diameter, diameter, lastAngle, lastAngle+PApplet.radians(currentAngle));
	}	

	public void highlight(){
		currentRed = highlightRed;
		currentGreen = highlightGreen;
		currentBlue = highlightBlue;
	}

	public void normal(){
		currentRed = red;
		currentGreen = green;
		currentBlue = blue;
	}
	public void setFont(String fontS){
		//font=parent.createFont(fontS,parent.widthByPercent(1.8)); //~=24
	}
	@SuppressWarnings("static-access")
	public void mouseOver()
	{
		if(done == true){
		if(parent.sq(parent.mouseX-x) + parent.sq(parent.mouseY - y) <= parent.sq(diameter/2)){
			float a = parent.atan2(parent.mouseY - y, parent.mouseX - x);
			if((a)>=lastAngle && a<=lastAngle+parent.radians(angle)||((2*Math.PI+a)>=lastAngle && 2*Math.PI+a<=(lastAngle+parent.radians(angle)))){
				DecimalFormat formatter = new DecimalFormat("#,###");
				parent.textFont(hoverLabelFont);
				String text = (county+": "+formatter.format(average));
				float textWidth = parent.textWidth(text);
				float boxWidth = textWidth + Project10.DISPLAY_PADDING*2;
				float textHeight = parent.textAscent() + parent.textDescent();
				float boxHeight = textHeight + Project10.DISPLAY_PADDING*2;
				parent.strokeWeight(2);
				parent.fill(220);
				parent.rect(parent.mouseX-(boxWidth/2), parent.mouseY - boxHeight - Project10.DISPLAY_PADDING, boxWidth, boxHeight, 20);
				parent.fill(0);
				parent.textAlign(parent.CENTER, parent.CENTER);
				parent.text(text, parent.mouseX, parent.mouseY - boxHeight/2 - Project10.DISPLAY_PADDING*3/2);
				highlight();
			}
			else normal();
		}
		else normal();
	}
	}
	
	public boolean isDone()
	{
		return done;
	}
	
	public void skip()
	{
		skip = true;
	}
}


