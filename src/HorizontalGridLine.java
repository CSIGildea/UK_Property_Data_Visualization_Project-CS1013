package project10;
import processing.core.PApplet;


public class HorizontalGridLine 
{
	PApplet parent;
	float x;
	float y;
	int stroke;
	float x2;
	float y2;
	String label;
	
	public HorizontalGridLine(PApplet p, float x, float y, float x2, float y2, int stroke, String label)
	{
		this.parent = p;
		this.x = x;
		this.y = y;
		this.stroke = stroke;
		this.x2 = x2;
		this.y2 = y2;
		this.label = label;
	}
	
	public void display()
	{
		parent.strokeWeight(stroke);
		parent.line(x, y, x2, y2);
	}
}
