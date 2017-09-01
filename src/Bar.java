package project10;

import java.text.DecimalFormat;

import processing.core.PConstants;
import processing.core.PFont;

public class Bar {
	Project10 parent;
	float height; 
	float currentHeight;
	float width;
	String label;
	int average;
	String county;
	float redSat;
	float yellowSat;
	float currentRedSat;
	float currentYellowSat;
	float highlightRedSat;
	float highlightYellowSat;
	int highlightColor;
	int barDrawSpeed = 15;
	int color;
	int GREY;
	boolean mouseOver;
	PFont hoverLabelFont;

	public Bar(Project10 p,float height, float width, String label, int average, String county, float chartHeight){
		parent = p;
		this.height = height;
		this.currentHeight = 0;
		this.width = width;
		this.label = label;
		this.average = average;
		this.county = county;
		hoverLabelFont = parent.createFont("Arial Rounded MT Bold", parent.widthByPercent(1.2));
		redSat = 255;
		yellowSat = 255*((chartHeight-height)/chartHeight);
		color = parent.color(redSat,yellowSat,0);
		highlightColor = parent.color(redSat+50, yellowSat+50, 50 );
		GREY = parent.color(220);
	}

	public float getHeight(){
		return this.height; 
	}
	public void setFont(){
		hoverLabelFont = parent.createFont("Arimo-Bold",parent.widthByPercent(.2)); //~=24
	}

	public void display(float x, float y){
		if(currentHeight<this.height)currentHeight+=barDrawSpeed;
		if(currentHeight>this.height)currentHeight-=barDrawSpeed;
		if(currentHeight > this.height-25 && currentHeight < this.height+25) currentHeight = this.height;
		if(!mouseOver)parent.fill(color);
		else parent.fill(highlightColor);
		parent.rect(x,y+this.height,this.width,-this.currentHeight);
	}

	public void mouseOver(float x, float y)
	{
		if((parent.mouseX<=x+this.width)&&(parent.mouseX>=x)&&(parent.mouseY>=y)&&(parent.mouseY<=y+this.height))
		{
			mouseOver = true;
			DecimalFormat formatter = new DecimalFormat("#,###");
			String text = (county+": " + (Project10.analysisType.equals("Average price") ? "£"
					: (Project10.analysisType.equals("Maximum price") ? "£" : "")) + formatter.format(average));
			parent.textFont(hoverLabelFont);
			float textWidth = parent.textWidth(text);
			float boxWidth = textWidth + Project10.DISPLAY_PADDING*2;
			float textHeight = parent.textAscent() + parent.textDescent();
			float boxHeight = textHeight + Project10.DISPLAY_PADDING*2;
			parent.fill(220);
			parent.strokeWeight(2);
			parent.rect(parent.mouseX-(boxWidth/2), parent.mouseY - boxHeight - Project10.DISPLAY_PADDING, boxWidth, boxHeight, 20);
			parent.fill(0);
			parent.textAlign(PConstants.CENTER, PConstants.CENTER);
			parent.text(text, parent.mouseX, parent.mouseY - boxHeight/2 - Project10.DISPLAY_PADDING*3/2);
		}
		else mouseOver = false;
	}
	
	public float returnHeight(){
		return currentHeight;
	}
	
	public void changeHeight(float newHeight){
		currentHeight = newHeight;
	}
}

