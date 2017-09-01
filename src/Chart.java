package project10;
import java.text.DecimalFormat;

import java.util.ArrayList;

import processing.core.PConstants;
import processing.core.PFont;

public class Chart { 
	Project10 parent;
	float space;
	ArrayList<Bar> barList = new ArrayList<Bar>();
	ArrayList<LineMarkers> markerList = new ArrayList<LineMarkers>();
	float chartWidth;
	float chartHeight;
	float maxValue;
	int yAxisValues;
	float barWidth;
	ArrayList<Integer> averages = new ArrayList<Integer>();
	float x = Project10.side.width;
	float y = Project10.side.ypos + Project10.side.height+parent.DISPLAY_PADDING;
	float lineStroke;
	ArrayList<String> counties;
	boolean lineGraph;
	Grid grid;
	int year;
	String trendCounty;
	boolean firstDraw = true;
	float t;
	int drawCounter = 0;
	PFont chartFont;

	public Chart (Project10 p, float width, float height, ArrayList<Integer> averages, ArrayList<String> counties,
			boolean lineGraph, int maxResult, int year, String trendCounty) {
		parent = p;
		chartWidth = width; 
		chartHeight = height;
		this.averages = averages;
		this.counties = counties;
		barWidth = (float) ((width*.8)/averages.size());
		space = (float) ((width*.2)/(averages.size()+1));
		if(maxResult == 0)findMax();
		else this.maxValue = maxResult;
		init_Bars();
		lineStroke = chartWidth/150;
		yAxisValues = 10;
		init_Markers();
		init_Grid();
		this.lineGraph = lineGraph;
		this.year = year;
		this.trendCounty = trendCounty;
		chartFont = parent.createFont("Arial Rounded MT Bold", parent.widthByPercent(1.8));
		t = 0;
	}
	
	public void init_Grid()
	{
		grid = new Grid(parent, x, y, chartWidth, chartHeight, (int)maxValue, lineGraph);
	}

	public void findMax() {
		for (int i = 0; i<averages.size(); i++) {
			if (averages.get(i)>maxValue) maxValue = averages.get(i);
		}
		float power10 = 100000;
		while(maxValue<power10)
		{
			power10/=10;
		}
		float tempPower = power10;
		while(power10<maxValue){
			power10+=tempPower;
		}
		maxValue=power10;
	}

	public void init_Bars() 
	{
		for (int i=0; i<averages.size(); i++) 
		{
			String string = (""+(i+1));
			float barHeight = (averages.get(i)*chartHeight)/maxValue;
			barList.add(new Bar(parent, barHeight, barWidth, string, averages.get(i), counties.get(i), chartHeight));
		}
	}
	
	public void drawMarks(){
		float tempY = y;
		parent.fill(0);
		float lapGap = chartHeight/yAxisValues;
		for (int i = 0;i<yAxisValues;i++){
			parent.rect(this.x-lineStroke*2,tempY,lineStroke,lineStroke);
			tempY+=lapGap;
		}   
	}

	public void drawBars(){
		float barX = x + space;
		float barY = y + chartHeight;
		for (int i =0; i<averages.size(); i++) {
			barY = y+chartHeight;
			Bar aBar = (Bar)barList.get(i);
			barY-=aBar.getHeight();
			aBar.display(barX, barY);
			barX+=space;
			barX+=barWidth;
		}
	}
	public void setFont(String fontS){
		 //font = parent.createFont(fontS,parent.widthByPercent(1.8));  //~=24
	}

	public void drawLabels(){
		float tempY = y - lineStroke;
		parent.textAlign(PConstants.RIGHT, PConstants.TOP);
		int label = (int)maxValue;
		parent.textFont(chartFont);
		int step = (int)maxValue/yAxisValues;
		parent.fill(0);
		float lapGap = chartHeight/yAxisValues;
		for (int i = 0;i<yAxisValues;i++){
			DecimalFormat formatter = new DecimalFormat("#,###");
			parent.text(formatter.format(label), x-lineStroke*2,tempY);
			tempY+=lapGap;
			label-=step;
		}    
	}

	public void drawStroke(){
		parent.fill(0);
		parent.rect(x-lineStroke,y,lineStroke,chartHeight);
		parent.rect(x-lineStroke,y+chartHeight,chartWidth+lineStroke,lineStroke);
	}

	public void mouseOver(){
		if(!lineGraph){
			float barX = x + space;
			float barY = y + chartHeight;
			for (int i =0; i<averages.size(); i++) {
				barY = y+chartHeight;
				Bar aBar = (Bar)barList.get(i);
				barY-=aBar.getHeight();
				aBar.mouseOver(barX, barY);
				barX+=space;
				barX+=barWidth;
			}
		}
	}

	public void init_Markers(){
		float lineX = x;
		float lineY = y + chartHeight;
		float point1X = lineX;
		float point1Y = lineY;
		for (int i =0; i<averages.size(); i++) {
			lineY = y+chartHeight;
			Bar aBar = (Bar)barList.get(i);	
			point1X = lineX+barWidth/2;
			point1Y = lineY-aBar.getHeight();
			if(i!=averages.size()-1){
				aBar = (Bar)barList.get(i+1);}
			markerList.add(new LineMarkers(parent, point1X, point1Y, 8, averages.get(i), counties.get(i), 0));
			lineX+=barWidth;
		}
	}
	public void drawLines(int numberOfLines){
		for (int i =0; i<numberOfLines; i++) {
			parent.strokeWeight(2);
			if(numberOfLines <= markerList.size())
			parent.line(markerList.get(i).returnX(), markerList.get(i).returnY(), markerList.get(i+1).returnX(), markerList.get(i+1).returnY());
		}
	}

	public void drawMarkers(){
		for (int i =0; i<averages.size(); i++) 
		{
			markerList.get(i).display();
		}
		for (int i =0; i<averages.size(); i++) 
		{
			markerList.get(i).overCircle();
		}
	}
	
	public void gradualLineDraw(float[][] v)
	{
		float step = .25f;
		if (t < 1.0) {
			parent.strokeWeight(2);
			// Draw a line from (v[0][0], v[0][1]) to (x, y)
			// Where (x, y) is approaching (v[1][0], v[1][1])
			float deltaX = v[1][0]-v[0][0];
			float deltaY = v[1][1]-v[0][1];
			float x = v[0][0]+deltaX*t;
			float y = v[0][1]+deltaY*t;
			// Draw up to where t currently is
			parent.line(v[0][0], v[0][1], x, y);

			// Make t larger
			t += step;
			System.out.println(t);
		}
		else 
		{
			parent.line(v[0][0], v[0][1], v[1][0], v[1][1]);
			System.out.println("pass");
		}
	}
	
	public ArrayList<Float> returnHeights(){
		ArrayList<Float> heights = new ArrayList<Float>();
		for (int i = 0; i<barList.size();i++){
			heights.add(barList.get(i).returnHeight());
		}
		return heights;
	}
	
	public void changeHeights(ArrayList<Float> heights){
		for (int i = 0; i<barList.size();i++){
			barList.get(i).changeHeight(heights.get(i));
		}
	}

	public void display() {
		drawStroke();
		drawMarks();
		drawLabels();
		grid.display();
		if(!lineGraph){
			drawBars();
			mouseOver();}
		else
		{
			if(firstDraw)
			{
				float[][] v = new float[2][2];
				if(drawCounter<markerList.size()-1)
				{
					v[0][0] = markerList.get(drawCounter).returnX();
					v[0][1] = markerList.get(drawCounter).returnY();
					v[1][0] = markerList.get(drawCounter+1).returnX();
					v[1][1] = markerList.get(drawCounter+1).returnY();
				}
				gradualLineDraw(v);
				if(t>=1)
				{
					t=0;
					drawCounter+=1;
				}
				if(drawCounter > markerList.size())
				{
					firstDraw = false;
				}
			}
			if(drawCounter == markerList.size()) drawCounter = markerList.size()-1;
			drawLines(drawCounter);
			drawMarkers();
		}	
		}
}
