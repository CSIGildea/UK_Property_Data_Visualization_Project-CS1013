package project10;
import processing.core.PApplet;
import java.util.ArrayList;

public class Grid {
	PApplet parent;
	float chartWidth;
	float chartHeight;
	int maxValue;
	float spacing;
	int stroke = 1;
	float chartX;
	float chartY;
	int numberOfLines = 10;
	ArrayList<HorizontalGridLine> horiArray = new ArrayList<HorizontalGridLine>();
	int numberOfVert;
	int numberOfHori;
	boolean isLineGraph;
	
	public Grid(PApplet p, float chartX, float chartY, float chartWidth, float chartHeight, int maxValue, boolean isLineGraph)
	{
		this.parent = p;
		this.chartWidth = chartWidth;
		this.chartHeight = chartHeight;
		this.maxValue = maxValue;
		this.chartX = chartX;
		this.chartY = chartY;
		this.isLineGraph = isLineGraph;
		findSpacing();
		init_Hori();
	}
	
	public void init_Hori()
	{
		numberOfHori = 0;
		float currentY = chartY;
		for(int i = 0; i<numberOfLines; i++)
		{
			String string = (""+currentY);
			horiArray.add(new HorizontalGridLine(parent, chartX, currentY, chartX+chartWidth, currentY, stroke, string));
			currentY+=spacing;
			numberOfHori+=1;
		}
	}
	
	public float findSpacing()
	{
		this.spacing = this.chartHeight/numberOfLines;
		return this.spacing;
	}
	
	public void display() 
	{
		for(int i = 0; i < numberOfHori; i++)
		{
			horiArray.get(i).display();
		}
	}
}
