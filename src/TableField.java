package project10;
import processing.core.PConstants;
import processing.core.PFont;

public class TableField
{
	Project10 parent;
	float x;
	float y;
	float width;
	float height;
	String info;
	int backgroundColor;
	int NAVY;
	int DARK_GREY;
	PFont font;
	
	TableField(Project10 parent, int backgroundColor, float x, float y, String info)
	{
		this.parent = parent;
		font=parent.createFont("Arial Rounded MT Bold",parent.widthByPercent(1.2));
		this.x = x;
		this.y = y;
		this.info = info;
		this.backgroundColor = backgroundColor;
		NAVY = parent.color(44,65,88);
		DARK_GREY = parent.color(60);
		parent.textFont(font);
		float textWidth = parent.textWidth(info);
		width = textWidth + Project10.DISPLAY_PADDING;
		float textHeight = parent.textAscent() + parent.textDescent();
		height = textHeight*2 + Project10.DISPLAY_PADDING;
	}
	public void setFont(String fontS){
		font=parent.createFont("Arimo-Bold",parent.widthByPercent(1)); //~=18
	}
	public void draw()
	{
		parent.stroke(DARK_GREY);
		parent.fill(backgroundColor);
		parent.rect(x, y, width, height);
		parent.stroke(0);
		parent.textAlign(PConstants.CENTER, PConstants.CENTER);
		parent.fill(220);
		parent.textFont(font);
		parent.text(info, x + width/2, y + height/2);
	}
}
