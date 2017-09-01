package project10;
import processing.core.PConstants;
import processing.core.PFont;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Key {
	Project10 parent;
	float x;
	float y;
	float lineStroke;
	String[] values;
	ArrayList<Integer> colors = new ArrayList<Integer>(); 
	String label;
	float tempX;
	float tempY;
	float lastLine;
	float width = 0;
	float textHeight;
	float outlineHeight = 0;
	float singleCharacterWidth;
	int total = 0;
	PFont keyFont;
	ArrayList<Integer> data;
	DecimalFormat totalFormat = new DecimalFormat("#,###");
	DecimalFormat floatFormat = new DecimalFormat("#.##");

	public Key(Project10 p, float x, float y, String[] values, ArrayList<Integer> colors, ArrayList<Integer> data) {
		parent = p;
		this.x = x;
		this.y = y;
		this.values = values;
		this.data = data;
		keyFont = parent.createFont("Arial Rounded MT Bold", parent.widthByPercent(1));
		parent.textFont(keyFont);
		textHeight = parent.textAscent()+parent.textDescent();
		for (int i = 0; i<this.values.length;i++){
			label = (this.values[i] + ": " + floatFormat.format((float)this.data.get(i)*100/total) + "%");
			float textWidth = parent.textWidth(label);
			if(textWidth>this.width){
				this.width = textWidth;
			}
			outlineHeight+=textHeight;
		}
		outlineHeight+=textHeight;
		String singleCharacter = "1";
		singleCharacterWidth = parent.textWidth(singleCharacter);
		this.colors = colors;
		for (int i = 0; i < data.size(); i++)
			total += data.get(i);
	}
	public void setFont(String fontS){
		//keyFont = parent.createFont(fontS,parent.widthByPercent(1));
	}
	public void drawKey() {
		parent.textAlign(PConstants.LEFT, PConstants.TOP);
		parent.textFont(keyFont);
		drawOutline();
		tempX = this.x + textHeight+2;
		tempY = this.y;
		int i2 = 0;
		parent.fill(200);
		parent.rect(x, tempY, this.width + textHeight*3, textHeight);
		parent.fill(0);
		label = "Total Sales: " + totalFormat.format(total);
		parent.text(label, tempX, tempY);
		tempY+=textHeight;
		for (int i = 0; i < values.length; i++) {
			parent.fill(colors.get(i2), colors.get(i2+1), colors.get(i2+2));
			parent.rect(x, tempY, textHeight, textHeight);
			parent.fill(0);
			label = (values[i] + ": " + floatFormat.format((float)data.get(i)*100/total) + "%");
			parent.text(label, tempX, tempY);
			tempY+=textHeight;
			i2+=3;
		}
	}

	public void drawOutline() {
		parent.strokeWeight(1);
		parent.fill(230);
		parent.rect(this.x, this.y, this.width+textHeight*3, outlineHeight);
	}
}
