package project10;

import java.util.ArrayList;
import processing.core.PApplet;

public class PieChart 
{	
	Project10 parent;
	float x;
	float y;
	float diameter;
	ArrayList<Integer> data = new ArrayList<Integer>();
	ArrayList<Float> newArray;
	ArrayList<LineMarkers> markerList = new ArrayList<LineMarkers>();
	ArrayList<PieArc> arcArray = new ArrayList<PieArc>();
	ArrayList<Integer> colors = new ArrayList<Integer>();
	String pieCounty;
	String[] properties = new String[5];
	Key key;

	PieChart(Project10 parent, float x, float y, float diameter, ArrayList<Integer> data, String county, String[] properties)
	{
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.diameter = diameter;
		this.data = data;
		newArray = toAngles(data);
		pieCounty = county;
		this.properties = properties;
		init_Slices();
		for (int i = 0; i < 15; i++)
		{
			if(i == 0){colors.add(46); colors.add(204); colors.add(113);}
			if(i == 1){colors.add(52); colors.add(152); colors.add(219);}
			if(i == 2){colors.add(155); colors.add(89); colors.add(182);}
			if(i == 3){colors.add(241); colors.add(196); colors.add(15);}
			if(i == 4){colors.add(231); colors.add(76); colors.add(60);}
		}
		this.key = new Key(parent, x+diameter/2, y+diameter/3, properties, colors, this.data);
		this.key.setFont("Arial Rounded MT Bold");
//		init_markers();
	}

	public ArrayList<Float> toAngles(ArrayList<Integer> data)
	{
		newArray = new ArrayList<Float>();
		float runningTotal = 0;
		for (int i = 0; i<data.size(); i++){
			runningTotal+=data.get(i);
		}
		float anglePerUnit = 360/runningTotal;
		for (int i = 0; i<data.size(); i++){
			float newValue = data.get(i)*anglePerUnit;
			newArray.add(newValue);
		}
		return newArray;
	}

	public void init_Slices(){
		float lastAngle = 0;
		float red = 0;
		float green = 0;
		float blue = 0;
		for (int i = 0; i<newArray.size();i++){
			if(i == 0){red = 46; green = 204; blue = 113;}
			if(i == 1){red = 52; green = 152; blue = 219;}
			if(i == 2){red = 155; green = 89; blue = 182;}
			if(i == 3){red = 241; green = 196; blue = 15;}
			if(i == 4){red = 231; green = 76; blue = 60;}
			PieArc arc=new PieArc(parent, this.x, this.y, newArray.get(i), lastAngle, this.diameter, red, green, blue, properties[i], data.get(i));
			arc.setFont("Arial Rounded MT Bold");
			arcArray.add(arc);
			lastAngle += PApplet.radians(newArray.get(i));
		}	
	}


	public void display()
	{
		for (int i = 0; i<newArray.size();i++){
			if(i == 0)arcArray.get(i).display();
			else if (arcArray.get(i-1).isDone() == true){
				arcArray.get(i).display();
				
			}
		}
//		drawMarkers();
		for (int i = 0; i<newArray.size();i++){
			arcArray.get(i).mouseOver();		
		}
		key.drawKey();
	}

	public void drawMarkers(){
		for (int i =0; i<newArray.size(); i++) 
		{
			markerList.get(i).display();
		}
		for (int i =0; i<newArray.size(); i++) 
		{
			if(markerList.get(i).overCircle()) arcArray.get(i).highlight();
			else arcArray.get(i).normal();
		}
	}

	public void init_markers()
	{
		float lastAngle = 0;
		for(int i=0;i<newArray.size();i++){
			float x1 = x + diameter/2*PApplet.cos(PApplet.radians(lastAngle+(newArray.get(i)/2)));
			float y1 = y + diameter/2*PApplet.sin(PApplet.radians(lastAngle+(newArray.get(i)/2)));
			markerList.add(new LineMarkers(parent, x1, y1, 8, data.get(i), properties[i], 255));
			//parent.ellipse(x1,y1, 10, 10);
			lastAngle += newArray.get(i);
		}
	}
	
	public void setSkip()
	{
		for(int i = 0;i<this.arcArray.size();i++)
		{
			this.arcArray.get(i).skip();
		}
	}

}
