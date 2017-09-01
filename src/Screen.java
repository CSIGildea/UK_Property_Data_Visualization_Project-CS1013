package project10;

import java.util.ArrayList;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import org.apache.commons.lang3.text.WordUtils;
import processing.core.PConstants;
import processing.core.PFont;

public class Screen {
	Project10 parent;
	ArrayList<RadioWidget> radioList = new ArrayList<RadioWidget>();
	String screenLabel;
	int backgroundColor;
	final int year;
	float x;
	float y;
	float recXPos;
	float recYPos;
	MarkerManager<Marker> markerManager = new MarkerManager<Marker>();
	project10.Chart chart;
	project10.PieChart pieChart;
	PFont screenFont;
	ArrayList<Integer> results = new ArrayList<Integer>();
	int CHARCOAL;

	Screen(Project10 parent, int backgroundColor, int year) {
		this.parent = parent;
		this.backgroundColor = backgroundColor;
		this.year = year;
		CHARCOAL = parent.color(46);
		screenFont = parent.createFont("Arial Rounded MT Bold", parent.widthByPercent(2.2));
	}
	
	public void addWidget(RadioWidget tempWidget, ArrayList<RadioWidget> tempList) {
		tempList.add(tempWidget);
	}

	public int getEvent(ArrayList<RadioWidget> tempList) {
		int event;
		for (int i = 0; i < tempList.size(); i++) {
			RadioWidget tempWidget = (RadioWidget) tempList.get(i);
			event = tempWidget.getEvent(parent.mouseX, parent.mouseY);
			if(event!=0)
				return event;
		}
		return Project10.EVENT_NULL;
	}
	public void setFont(String fontS){
		 //font = parent.createFont(fontS,(float)(parent.widthByPercent(2)));
	}
	// SH - draw label at top of screen and radio buttons
	void drawWidgets(int screenWidthYearView, int displayWidth, float sidebar, int widgetY,
			String analysis, String property, boolean trendView, boolean mapView, boolean pieView) {
		
		property = (property.equals("A") ? "All Properties" : property.equals("D") ? "Detached Houses" : 
			property.equals("S") ? "Semi-Detached Houses" : property.equals("T") ? "Terraced Houses" : 
				property.equals("F") ? "Flats/Maisonettes" : property.equals("O") ? "Other Properties" : "");
		analysis = WordUtils.capitalizeFully(analysis);
		
		if (!trendView && !pieView)
			screenLabel = analysis + " Of " + property + " In " + year;
		else if (pieView)
			screenLabel = analysis + " Of " + property + " In " + pieChart.pieCounty + " In " + year;
		else
			screenLabel = analysis + " Of " + property + " In " + chart.trendCounty;
		parent.stroke(80);
		parent.strokeWeight(1);
		parent.fill(CHARCOAL);
		parent.textFont(screenFont);
		parent.textAlign(PConstants.CENTER, PConstants.CENTER);
		float textWidth =parent.displayWidth;
		float boxWidth = textWidth ;    //+ Project10.DISPLAY_PADDING*2;
		float textHeight=parent.side.height;
		//float textHeight = parent.textAscent() + parent.textDescent();
		float boxHeight = textHeight ;//+ Project10.DISPLAY_PADDING*2;
		
		if (!mapView && !pieView)
		{
			recXPos = sidebar + ((displayWidth - sidebar)/2);
			recYPos = Project10.side.height / 2;
			parent.rect(0,0, boxWidth, boxHeight);
			parent.fill(220);
			parent.text(screenLabel, recXPos, widgetY+boxHeight/2 - Project10.DISPLAY_PADDING);
		}
		else if (mapView)
		{
			recXPos = sidebar + ((displayWidth - sidebar)/2);
			parent.rect(0, 0, boxWidth, boxHeight);
			parent.fill(220);
			parent.text(screenLabel, recXPos, widgetY+boxHeight/2 - Project10.DISPLAY_PADDING);
		}
		else if (pieView)
		{
			recXPos = sidebar + ((displayWidth - sidebar)/2);
			parent.rect(0, 0, boxWidth,boxHeight);
			parent.fill(220);
			parent.text(screenLabel, recXPos, widgetY+boxHeight/2 - Project10.DISPLAY_PADDING);
		}
		else
			recXPos = 100;

		for (int i = 0; i < radioList.size(); i++) {
			RadioWidget tempWidget = (RadioWidget) radioList.get(i);
			tempWidget.draw();
		}
	}

	public void updateRadio(int screenWidthYearView, float radioIncrement, int radioWidth) {
		radioList.get(11).changeX(screenWidthYearView/2-radioWidth/2);
		for(int index=0;index<11;index++)
		{
			radioList.get(index).changeX((screenWidthYearView/2)-(radioWidth/2)-(radioIncrement*(11-index)));
		}
		for(int index=0;index<11;index++)
		{
			radioList.get(index+12).changeX(screenWidthYearView/2-radioWidth/2+radioIncrement*(index+1));
		}
	}

	public void updateRadio(int screenWidthYearView, float radioIncrement, int radioWidth, float hamburgerIncrement) {
		radioList.get(11).changeX(hamburgerIncrement+(screenWidthYearView/2)-radioWidth/2);
		for(int index=0;index<11;index++)
		{
			radioList.get(index).changeX(hamburgerIncrement+screenWidthYearView/2-radioWidth/2-radioIncrement*(11-index));
		}
		for(int index=0;index<11;index++)
		{
			radioList.get(index+12).changeX(hamburgerIncrement+screenWidthYearView/2-radioWidth/2+radioIncrement*(index+1));
		}
	}
}
