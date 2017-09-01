package project10;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.event.MouseEvent;

import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.events.PanMapEvent;
import de.fhpotsdam.unfolding.events.ZoomMapEvent;
import de.fhpotsdam.unfolding.interactions.MouseHandler;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class Project10 extends PApplet {
	public final int SEARCH = -2;
	public final int NO_EVENT = -1;
	public final int EVENT_HAMBURGER = 0;
	public static int DISPLAY_PADDING = 10;
	public final int CHECKBOX = 100;
	public final int START_EVENTS_TYPE = 10;
	public final int START_EVENTS_COUNTRY = 20;
	public final int START_EVENTS_COUNTIES = 30;
	public final int START_EVENTS_PROPERTIES = 200;
	public final int W_WIDTH = 480;
	public final int W_HEIGHT = 100;
	public final int GREY = color(240);
	public static final int EVENT_NULL = 0;
	public static final int EVENT_BUTTON_LEFT = 1;
	public static final int EVENT_BUTTON_RIGHT = 2;
	public final int MIN_SCREEN = 1995;
	public final int MAX_SCREEN = 2017;

	int[] events = new int[10];
	int[] boxEvents = new int[20];
	int[] countiesEvents = new int[150];
	int[] typeEvents = new int[10];
	int[] propertyEvents = new int[10];
	int currentButton = 0, lastButton, walesButton = 0, screenWidthForCharts, screenTracker = 2017, frame = 0;
	int countdown = 30, logoWidth = 148, logoHeight = 84;
	int radioWidgetY, screenHeightYearView, screenWidthYearView, boxWidth = 170, boxHeight = 70, radioWidth;

	float rotateX = 0.9f;
	float rotateZ = (float) 0;
	float rotateVelocityZ = 0.003f;
	float burgerHeight, radioIncrement, hamburgerIncrement;
	float backgroundTransparency = 0;
	float logoX = displayWidth / 2 - (float) logoWidth / 2;
	float logoY = displayHeight / 2 - (float) logoHeight / 2;
	double[] englandLongitude, englandLatitude, walesLongitude, walesLatitude;

	String[] country = { "England", "Wales" };
	String[] analysis = { "Average price", "Maximum price", "Number of transactions", "Sales by type" };
	String[] property = { "All", "Detached", "Semi-detached", "Terraced", "Flats/Maisonettes", "Other" };
	String[] englandCounties, walesCounties;
	String[] propertiesArray;
	public static String analysisType = "";
	String propertyType = "S";
	String preparedTable = "prepareddata";
	String fullTable = "fulldata";

	public static Hamburger hamburgerButton;
	public static Maps map;
	public static QueryButton[] queries, additionalQueries;
	public static PImage[] images, additionalImages;
	public static PImage tick, untick, burgerImage, radioSelected, radioUnselected, radioOnImage, radioOffImage,
			backgroundImage, teamLogo, analysisHover, search, dataUnclicked, dataClicked, mapClicked, mapUnclicked,
			noProperty;
	public static Checkbox[] countryBoxes, englandBoxes, walesBoxes, typeBoxes, propertyBoxes;
	public static SubScreen side;
	public static PieChart pieChart;
	public static Query query;
	public static Screen trendScreen;
	public static Screen currentScreen;
	public static TextWidget text;
	public static QueryButton searchButton;
	public static PFont font, checkboxFont;
	LinkedHashMap<String, Boolean> booleans = new LinkedHashMap<String, Boolean>();

	boolean trend = false, printedOnce = false, pieView = false, introDrawn = false, logoGrowing = true,
			logoMoving = false, firstClick = false, enableWriting = false, widgetsOn = false, mapView = false,
			localConnection = false, displayingInfo = false, multipleChoices = true, clickPermitted = false,
			available = true;
	static boolean firstPie = true;

	UnfoldingMap geoMap;
	UnfoldingMap aboveMap;
	UnfoldingMap twoDMap;
	EventDispatcher eventDispatcher;
	de.fhpotsdam.unfolding.geo.Location centreOfEngland = new de.fhpotsdam.unfolding.geo.Location(53.5f, -1.34f);
	de.fhpotsdam.unfolding.geo.Location boundTopLeft = new de.fhpotsdam.unfolding.geo.Location(63.8, -17.6);
	de.fhpotsdam.unfolding.geo.Location boundBottomRight = new de.fhpotsdam.unfolding.geo.Location(43.0, 13);

	ArrayList<Screen> screenStructureList = new ArrayList<Screen>();
	ArrayList<String> countiesForMap = new ArrayList<String>();
	ArrayList<RadioWidget> mainRadioList;
	ArrayList<String[]> propertyInfo = new ArrayList<String[]>();
	ArrayList<TableField[]> infoTable = new ArrayList<TableField[]>();

	PFont hoverLabelFont;

	public static void main(String[] args) {
		PApplet.main("project10.Project10");
	}

	public void settings() {
		fullScreen(P2D);
	}

	public void setup() {
		DISPLAY_PADDING = (int) widthByPercent(.55);
		hoverLabelFont = createFont("Arial Rounded MT Bold", widthByPercent(1.2));
		initializeBooleans();
		initializeObjects();
		createHashmaps();
		loadImages();
		createEvents();
		createButtons();
		yearViewSetup();
		setupUnfoldingMap();
		setFonts();

		// query.getPostcodeInfo(fullTable, "RG9 4JN"); // NE44 6HZ, N21 3DA
	}

	public void draw() {
		background(GREY);
		frame++;
		if (!introDrawn) {
			drawIntro();
		}
		tint(255, backgroundTransparency);
		image(backgroundImage, 0, 0);
		tint(255);
		// checkBoundingBox();

		if (mapView)
			drawGeoMap();

		if (introDrawn) {
			if (booleans.get("barChartShown")) {
				if (pieView)
					currentScreen.pieChart.display();
				else
					currentScreen.chart.display();
			}

			if (widgetsOn)
				currentScreen.drawWidgets(screenWidthYearView, displayWidth, widthByPercent(15.5),
						(int) heightByPercent(1), analysisType, propertyType, trend, mapView, pieView);
			if (widgetsOn & mouseX < widthByPercent(30)) {
				mute();
			} else {
				listen();
			}

			drawButtons();
			buttonHover();
			if (firstClick && frame % 20 == 0)
				text.waitingForText();

		}

		if (displayingInfo)
			drawPropertyInfoTable();

		image(teamLogo, logoX, logoY);
		mapHoverText();
	}

	public void setFonts() {
		text.setFont("Arial Rounded MT Bold");
		trendScreen.setFont("Arial Rounded MT Bold");
		currentScreen.setFont("Arial Rounded MT Bold");

		checkboxFont = createFont("Arial Rounded MT Bold", widthByPercent(1.2));
	}

	// SH - opening animation and background transparency
	public void drawIntro() {
		int lastX = (int) (hamburgerButton.xpos + hamburgerButton.width + widthByPercent(.4));
		if (backgroundTransparency < 50)
			backgroundTransparency += .6;
		teamLogo.resize(logoWidth, logoHeight);
		if (logoGrowing) {
			if (logoWidth < displayWidth / 4) {
				logoWidth += widthByPercent(1.5);
				logoHeight += heightByPercent(1.5);
				logoX = displayWidth / 2 - (float) logoWidth / 2;
				logoY = displayHeight / 2 - (float) logoHeight / 2;
			} else {
				countdown--;
				if (countdown == 0) {
					logoGrowing = false;
					logoMoving = true;
				}
			}
		} else {
			if (logoWidth > widthByPercent(7.8)) {
				logoWidth -= widthByPercent(1.4);
				logoHeight -= heightByPercent(1.4);
			}
			if (logoX >= lastX)
				logoX -= widthByPercent(1.85);
			if (logoY > hamburgerButton.ypos)
				logoY -= heightByPercent(2.25);
			if (logoX < lastX && logoY < hamburgerButton.ypos) {
				logoX = lastX;
				logoY = hamburgerButton.ypos;
				teamLogo = loadImage("/data/images/teamLogo.png");
				teamLogo.resize((int) widthByPercent(7.8), (int) heightByPercent(7.8));
				introDrawn = true;
			}
		}
	}

	public void setupUnfoldingMap() {
		twoDMap = new UnfoldingMap(this, new Microsoft.RoadProvider());
		aboveMap = new UnfoldingMap(this, new Microsoft.AerialProvider());
		// MapUtils.createMouseEventDispatcher(this, geoMap);
		eventDispatcher = new EventDispatcher();
		MouseHandler mouseHandler = new MouseHandler(this, aboveMap, twoDMap);
		eventDispatcher.addBroadcaster(mouseHandler);
		aboveMap.zoomAndPanTo(7, centreOfEngland);
		aboveMap.setZoomRange(7, 19);
		float maxPanningDistance = 50; // in km
		twoDMap.setPanningRestriction(centreOfEngland, maxPanningDistance);
		twoDMap.zoomAndPanTo(7, centreOfEngland);
		twoDMap.setZoomRange(7, 20);
		twoDMap.setPanningRestriction(centreOfEngland, maxPanningDistance);
		geoMap = twoDMap;
		listen();
	}

	public void drawGeoMap() {
		for (int i = 0; i < screenStructureList.size(); i++)
			geoMap.removeMarkerManager(screenStructureList.get(i).markerManager);

		if (geoMap.getZoomLevel() == 6) {
			float maxPanningDistance = 50; // in km
			geoMap.setPanningRestriction(centreOfEngland, maxPanningDistance);
		} else {
			geoMap.setPanningRestriction(centreOfEngland, 500);
		}
		geoMap.addMarkerManager(currentScreen.markerManager);
		geoMap.draw();
	}

	// SH - create a screen object for each year
	public void yearViewSetup() {
		radioWidgetY = (int) (displayHeight - heightByPercent(5));
		for (int year = 1995; year <= 2017; year++) {
			Screen screen = new Screen(this, GREY, year);
			screen.setFont("Arial Rounded MT Bold");
			screenStructureList.add(screen);
		}

		trendScreen = new Screen(this, GREY, 0);
		trendScreen.setFont("Arial Rounded MT Bold");
		mainRadioList = new ArrayList<RadioWidget>();

		screenHeightYearView = displayHeight;
		screenWidthYearView = displayWidth;
		hamburgerIncrement = widthByPercentOfAvailable(15, screenHeightYearView);
		radioIncrement = (screenWidthYearView - (DISPLAY_PADDING * 2)) / 23;

		float radioX = screenWidthYearView / 2 - radioIncrement * 11 - radioWidth / 2;
		for (int year = 1995; year <= 2017; year++) {
			RadioWidget radio = new RadioWidget(this, radioX, radioWidgetY, year, radioOffImage);
			radio.setFont("Arial Rounded MT Bold");
			mainRadioList.add(radio);
			radioX += radioIncrement;
		}

		for (int i = 0; i < screenStructureList.size(); i++) {
			Screen tempScreen = (Screen) screenStructureList.get(i);
			for (int j = 0; j < mainRadioList.size(); j++) {
				RadioWidget tempWidget = (RadioWidget) mainRadioList.get(j);
				tempScreen.addWidget(tempWidget, tempScreen.radioList);
			}
		}
		setRadios();
		currentScreen = screenStructureList.get(screenStructureList.size() - 1);
	}

	public void mouseWheel(MouseEvent ev) {
		if (mouseX < widthByPercent(29.5)) {
			float e = ev.getCount();
			if (booleans.get("showEngland") && booleans.get("showWales"))
				lastButton = englandBoxes.length + walesBoxes.length - 16;
			else if (booleans.get("showEngland"))
				lastButton = englandBoxes.length - 17;
			else if (booleans.get("showWales")) {
				lastButton = walesBoxes.length - 16;
				currentButton = 0;
			}

			if (e > 0) {
				if (currentButton < lastButton && currentButton <= englandBoxes.length && booleans.get("showEngland"))
					currentButton += 1;
				if (booleans.get("incrementWalesButton") && walesButton + currentButton < lastButton)
					walesButton++;
			} else if (e < 0) {
				if (currentButton > 0 && walesButton == 0) {
					currentButton -= 1;
					booleans.put("incrementWalesButton", false);
				}
				if (booleans.get("incrementWalesButton")) {
					if (walesButton > 0)
						walesButton--;
				}
			}
		}
	}

	public void readCounties() throws FileNotFoundException {
		Scanner textFile = new Scanner(new File("data/files/englandCounties.txt"));
		int numberOfCounties = textFile.nextInt();
		textFile.nextLine();
		englandBoxes = new Checkbox[numberOfCounties + 1];
		englandCounties = new String[numberOfCounties + 1];
		englandCounties[0] = "All England Counties";
		for (int i = 1; i < englandCounties.length; i++) {
			String temp = textFile.nextLine();
			temp = WordUtils.capitalizeFully(temp, ' ', '-');
			englandCounties[i] = temp;
		}
		textFile = new Scanner(new File("data/files/walesCounties.txt"));
		numberOfCounties = textFile.nextInt();
		walesBoxes = new Checkbox[numberOfCounties + 1];
		textFile.nextLine();
		walesCounties = new String[numberOfCounties + 1];
		walesCounties[0] = "All Wales Counties";
		for (int i = 1; i < walesCounties.length; i++) {
			String temp = textFile.nextLine();
			temp = WordUtils.capitalizeFully(temp, ' ', '-');
			walesCounties[i] = temp;
		}
		textFile.close();
	}

	public void buttonHover() {
		boolean changedCursor = false;
		if (hamburgerButton.getEvent(mouseX, mouseY) == 0) {
			cursor(HAND);
			changedCursor = true;
		}

		if (booleans.get("showButtons")) {
			for (int i = 0; i < queries.length; i++)
				queries[i].getEvent(mouseX, mouseY);
			if (booleans.get("showAdditional")) {
				for (int i = 0; i < additionalQueries.length; i++) {
					additionalQueries[i].getEvent(mouseX, mouseY);
				}
			}
		}
		if (booleans.get("showSearch")) {
			if (text.getEvent(mouseX, mouseY) == SEARCH) {
				cursor(TEXT);
				changedCursor = true;
			}
		}
		if (!changedCursor) {
			cursor(ARROW);
		}
	}

	public void changeImagesForFilter(PImage image1, PImage image2) {
		for (int i = 0; i < countryBoxes.length; i++) {
			countryBoxes[i].changeImages(image1, image2);
		}
		for (int i = 0; i < walesBoxes.length; i++) {
			walesBoxes[i].changeImages(image1, image2);
		}
		for (int i = 0; i < englandBoxes.length; i++) {
			englandBoxes[i].changeImages(image1, image2);
		}
	}

	public void loadImages() {
		search = loadImage("/data/images/search.png");
		backgroundImage = loadImage("/data/images/houseMarket.png");
		backgroundImage.resize(displayWidth, displayHeight);
		teamLogo = loadImage("/data/images/teamLogo.png");
		teamLogo.resize((int) widthByPercent(7.8), (int) heightByPercent(7.8));

		radioOnImage = loadImage("/data/images/radioButtonOn.png");
		radioOffImage = loadImage("/data/images/radioButtonOff.png");
		radioOnImage.resize(0, 35);
		radioOffImage.resize(0, 35);
		radioWidth = radioOffImage.width;

		burgerImage = loadImage("/data/images/menu.png");
		images[0] = loadImage("/data/images/analysisUnclicked.png");
		images[1] = loadImage("/data/images/analysisClicked.png");
		images[2] = loadImage("/data/images/propertyUnclicked.png");
		images[3] = loadImage("/data/images/propertyClicked.png");
		images[4] = loadImage("/data/images/ButtonUnclicked.png");
		images[5] = loadImage("/data/images/filterClicked.png");
		images[6] = loadImage("/data/images/noMap.png");
		images[7] = loadImage("/data/images/noMap.png");
		images[8] = loadImage("/data/images/noData.png");
		images[9] = loadImage("/data/images/noData.png");
		images[10] = loadImage("/data/images/countryUnclicked.png");
		images[11] = loadImage("/data/images/countryClicked.png");
		images[12] = loadImage("/data/images/countyUnclicked.png");
		images[13] = loadImage("/data/images/countyClicked.png");

		mapUnclicked = loadImage("/data/images/WeatherUnclicked.png");
		mapClicked = loadImage("/data/images/WeatherClicked.png");
		dataUnclicked = loadImage("/data/images/goUnclicked.png");
		dataClicked = loadImage("/data/images/goClicked.png");
		noProperty = loadImage("/data/images/noProperty.png");

		tick = loadImage("/data/images/tick-checked.png");
		untick = loadImage("/data/images/tick-unchecked.png");
		radioSelected = loadImage("/data/images/radio-selected.png");
		radioUnselected = loadImage("/data/images/radio-unselected.png");

		additionalImages[0] = loadImage("/data/images/averageUnclicked.png");
		additionalImages[1] = loadImage("/data/images/averageClicked.png");
		additionalImages[2] = loadImage("/data/images/maximumUnclicked.png");
		additionalImages[3] = loadImage("/data/images/maximumClicked.png");
		additionalImages[4] = loadImage("/data/images/transactionsUnclicked.png");
		additionalImages[5] = loadImage("/data/images/transactionsClicked.png");
		additionalImages[6] = loadImage("/data/images/salesUnclicked.png");
		additionalImages[7] = loadImage("/data/images/salesClicked.png");
	}

	public void initializeBooleans() {
		booleans.put("showButtons", false);
		booleans.put("showAdditional", false);
		booleans.put("barChartShown", false);
		booleans.put("incrementWalesButton", false);
		booleans.put("showTypes", false);
		booleans.put("showPropertyBox", false);
		booleans.put("showCountryBox", false);
		booleans.put("showCounties", false);
		booleans.put("showEngland", false);
		booleans.put("showWales", false);
		booleans.put("showSearch", false);
	}

	public void initializeObjects() {
		query = new Query(this, localConnection);
		queries = new QueryButton[5];
		countryBoxes = new Checkbox[2];
		additionalQueries = new QueryButton[2];
		typeBoxes = new Checkbox[4];
		propertyBoxes = new Checkbox[6];
		images = new PImage[(queries.length + additionalQueries.length) * 2];
		additionalImages = new PImage[typeBoxes.length * 2];
	}

	public void createHashmaps() {
		map = new Maps();
		map.populateCountries();
		try {
			readCounties();
			map.populateCounties(englandCounties, walesCounties);
		} catch (FileNotFoundException e) {
			System.out.println("The file has not been found");
		}
		map.populateTypes();
		map.populateProperties();
	}

	public void createEvents() {
		for (int i = 0; i < events.length; i++) {
			events[i] = i;
		}
		int start = START_EVENTS_TYPE;
		for (int i = 0; i < typeEvents.length; i++) {
			typeEvents[i] = start;
			start++;
		}
		start = START_EVENTS_COUNTRY;
		for (int i = 0; i < boxEvents.length; i++) {
			boxEvents[i] = start;
			start++;
		}
		start = START_EVENTS_COUNTIES;
		for (int i = 0; i < countiesEvents.length; i++) {
			countiesEvents[i] = start++;

		}
		start = START_EVENTS_PROPERTIES;
		for (int i = 0; i < propertyEvents.length; i++) {
			propertyEvents[i] = start++;

		}
	}

	public void createButtons() {
		side = new SubScreen(0, 0, widthByPercent(15.5), heightByPercent(8), this);
		text = new TextWidget(side.xpos + side.width, side.ypos, displayWidth - side.width, side.height,
				"Search for house by postcode... (e.g RG9 4JN)", SEARCH, 100, this);
		searchButton = new QueryButton(side.xpos + (int) side.width - (int) widthByPercent(4),
				side.ypos + DISPLAY_PADDING, widthByPercent(3), heightByPercent(5), search, search, -3, this);
		hamburgerButton = new Hamburger(DISPLAY_PADDING, DISPLAY_PADDING, widthByPercent(3), heightByPercent(5),
				burgerImage, EVENT_HAMBURGER, this);
		int x = DISPLAY_PADDING;
		int y = (int) heightByPercent(8);
		int imageCounter = 0;
		int eventCounter = 1;
		for (int i = 0; i < queries.length; i++) {
			queries[i] = new QueryButton(x, y, widthByPercent(14), heightByPercent(6), images[imageCounter],
					images[imageCounter + 1], events[eventCounter], this);
			y += (queries[i].getHeight() / 10) * 2.5;
			imageCounter += 2;
			eventCounter++;
		}

		x = DISPLAY_PADDING + DISPLAY_PADDING / 2;
		y = (int) (queries[2].getY() + (queries[2].getHeight() / 10) * 2.5);
		for (int i = 0; i < additionalQueries.length; i++) {
			additionalQueries[i] = new QueryButton(x, y, widthByPercent(13), heightByPercent(5), images[imageCounter],
					images[imageCounter + 1], events[eventCounter], this);
			y += (additionalQueries[i].getHeight() / 10) * 2.3;
			imageCounter += 2;
			eventCounter++;
		}
		x = side.getX();
		x += (int) (side.getWidth());
		y = additionalQueries[0].getY();
		for (int i = 0; i < countryBoxes.length; i++) {
			countryBoxes[i] = new Checkbox(x, y, widthByPercent(14), heightByPercent(6), tick, untick, boxEvents[i],
					country[i], this);
			y += countryBoxes[i].getHeight();
		}

		x = side.getX();
		x += (int) (side.getWidth());
		y = queries[0].getY();
		for (int i = 0; i < typeBoxes.length; i++) {
			typeBoxes[i] = new Checkbox(x, y, widthByPercent(14), heightByPercent(6), radioSelected, radioUnselected,
					typeEvents[i], analysis[i], this);
			y += typeBoxes[i].getHeight();
		}
		typeBoxes[0].setDefault();

		x = side.getX();
		x += (int) (side.getWidth());
		y = queries[1].getY();
		for (int i = 0; i < propertyBoxes.length; i++) {
			propertyBoxes[i] = new Checkbox(x, y, widthByPercent(14), heightByPercent(6), radioSelected,
					radioUnselected, propertyEvents[i], property[i], this);
			y += propertyBoxes[i].getHeight();
		}
		propertyBoxes[0].setDefault();

		eventCounter = 0;

		for (int i = 0; i < englandBoxes.length; i++) {

			englandBoxes[i] = new Checkbox(0, 0, widthByPercent(14), heightByPercent(6), tick, untick,
					countiesEvents[eventCounter], englandCounties[i], this);
			eventCounter++;
		}
		for (int i = 0; i < walesBoxes.length; i++) {
			walesBoxes[i] = new Checkbox(0, 0, widthByPercent(14), heightByPercent(6), tick, untick,
					countiesEvents[eventCounter], walesCounties[i], this);
			eventCounter++;
		}

	}

	public void drawButtons() {
		side.draw();
		hamburgerButton.draw();
		searchButton.draw();
		if (booleans.get("showButtons")) {
			if (mouseX < widthByPercent(15.5)) {
				mute();
			} else {
				listen();
			}
			for (int i = 0; i < queries.length; i++) {
				queries[i].draw();
			}

			if (booleans.get("showAdditional")) {
				for (int i = 0; i < additionalQueries.length; i++) {
					additionalQueries[i].draw();
				}
			}
			if (booleans.get("showCountryBox")) {
				for (int i = 0; i < countryBoxes.length; i++) {
					countryBoxes[i].draw();
				}
			}
			if (booleans.get("showTypes")) {
				for (int i = 0; i < typeBoxes.length; i++) {
					typeBoxes[i].draw();
				}
			}
			if (booleans.get("showPropertyBox")) {
				for (int i = 0; i < propertyBoxes.length; i++) {
					propertyBoxes[i].draw();
				}
			}
			if (booleans.get("showCounties")) {
				int y = side.getY();
				int x = (int) side.getWidth();
				if (mouseX < widthByPercent(29.5)) {
					mute();
				} else {
					listen();
				}
				if (booleans.get("showEngland")) {
					for (int i = currentButton; i < englandBoxes.length; i++) {
						englandBoxes[i].draw(x, y);

						y += englandBoxes[i].getHeight();

					}
					if (currentButton >= englandBoxes.length && !booleans.get("incrementWalesButton")) {
						booleans.put("incrementWalesButton", true);
					}
					if (booleans.get("showWales")) {
						for (int i = walesButton; i < walesBoxes.length; i++) {
							walesBoxes[i].draw(x, y);
							y += walesBoxes[i].getHeight();
						}

					}
				} else if (booleans.get("showWales")) {

					for (int i = walesButton; i < walesBoxes.length; i++) {
						walesBoxes[i].draw(x, y);
						y += walesBoxes[i].getHeight();
					}
				}

			}
		}
		if (booleans.get("showSearch")) {
			text.draw();
		}

	}

	public void resetValuesChosen() {
		for (int i = 0; i < countryBoxes.length; i++) {
			countryBoxes[i].clear();
		}
		map.countries.put("England", false);
		map.countries.put("Wales", false);
		for (int i = 0; i < englandBoxes.length; i++) {
			englandBoxes[i].clear();
			map.england.put(englandCounties[i], false);
		}
		for (int i = 0; i < walesBoxes.length; i++) {
			walesBoxes[i].clear();
			map.wales.put(walesCounties[i], false);
		}
	}

	public void checkForClickPermission() {
		boolean selected = false;
		for (int i = 0; i < englandBoxes.length; i++) {
			if (map.england.get(englandCounties[i])) {
				selected = true;

			}
		}
		for (int i = 0; i < walesBoxes.length; i++) {
			if (map.wales.get(walesCounties[i])) {
				selected = true;
			}
		}
		if (selected) {
			queries[3].changeImage(mapUnclicked, mapClicked);
			queries[4].changeImage(dataUnclicked, dataClicked);
			clickPermitted = true;
		} else {
			queries[3].changeImage(images[6], images[7]);
			queries[4].changeImage(images[8], images[9]);
			clickPermitted = false;
		}
		if (!available) {
			queries[3].changeImage(images[6], images[7]);
		}
	}

	public void doubleCheck() {
		if (map.countries.get("England")) {
		} else {
			
			for (int i = 0; i < englandBoxes.length; i++) {
				englandBoxes[i].clear();
				map.england.put(englandCounties[i], false);

			}
		}

		if (map.countries.get("Wales")) {

		} else {
			for (int i = 0; i < walesBoxes.length; i++) {
				walesBoxes[i].clear();
				map.wales.put(walesCounties[i], false);
			}
		}

	}

	public void mousePressed() {

		if (searchButton.getEvent(mouseX, mouseY) == -3) {

			if (booleans.get("showSearch")) {
				booleans.put("showSearch", false);
				displayingInfo = false;
				text.setTemporaryWidth(true);
			} else {
				booleans.put("showSearch", true);
				displayingInfo = true;
				text.setTemporaryWidth(false);
			}
		}
		if (booleans.get("showSearch")) {
			if (text.getEvent(mouseX, mouseY) == SEARCH) {
				if (!firstClick) {
					text.clear();
					firstClick = true;
				}
				enableWriting = true;
				displayingInfo = true;
			} else {
				enableWriting = false;
				displayingInfo = false;
			}
		}

		if (hamburgerButton.getEvent(mouseX, mouseY) == 0) {
			changeBooleansMaps(-1);
			side.changeHeight();
			booleans.put("showAdditional", false);
			queries[3].changeY(queries[2]);
			queries[4].changeY(queries[3]);
			if (booleans.get("showButtons")) {
				booleans.put("showButtons", false);
			} else
				booleans.put("showButtons", true);
		}

		if (booleans.get("showButtons")) {
			int event = checkForQuery();
			switch (event) {
			case 1:
				changeBooleansMaps(4);
				break;
			case 2:
				if (available) {
					changeBooleansMaps(5);
				}
				break;
			case 3:
				changeBooleansMaps(-1);
				if (!booleans.get("showAdditional")) {
					queries[3].changeY(additionalQueries[additionalQueries.length - 1]);
					booleans.put("showAdditional", true);
				} else {
					queries[3].changeY(queries[2]);
					booleans.put("showAdditional", false);
				}
				queries[4].changeY(queries[3]);
				break;
			case 4:
				if (clickPermitted & available) {
					mapView = true;
					booleans.put("barChartShown", false);
					go();
				}
				break;
			case 5:
				if (clickPermitted) {
					changeBooleansMaps(-1);
					booleans.put("barChartShown", true);
					mapView = false;
					doubleCheck();
					print();
					go();
				}
				break;
			case 6:
				changeBooleansMaps(6);
				break;
			case 7:
				changeBooleansMaps(7);
				if (map.countries.get("Wales")) {
					booleans.put("showWales", true);
					booleans.put("incrementWalesButton", true);
				}
				if (map.countries.get("England")) {
					booleans.put("showEngland", true);
					booleans.put("incrementWalesButton", false);
				}
				currentButton = 0;
				walesButton = 0;
				break;

			}
			if (booleans.get("showButtons")) {
				if (booleans.get("showTypes")) {
					int typeEvent = checkTypeSelected();
					int imageI = 0;
					if (typeEvent != -1) {
						changeOtherStates(typeEvent - START_EVENTS_TYPE, typeBoxes, map.analysis);
						if (!map.analysis.get(typeBoxes[typeEvent - START_EVENTS_TYPE].getLabel())) {
							modifyMap(map.analysis, typeBoxes, typeEvent - START_EVENTS_TYPE);
							switch (typeEvent) {
							case 10:
								queries[0].changeImage(additionalImages[imageI], additionalImages[imageI + 1]);
								break;
							case 11:
								queries[0].changeImage(additionalImages[imageI + 2], additionalImages[imageI + 3]);
								break;
							case 12:
								queries[0].changeImage(additionalImages[imageI + 4], additionalImages[imageI + 5]);
								break;
							case 13:
								queries[0].changeImage(additionalImages[imageI + 6], additionalImages[imageI + 7]);
							}
						}
						if (map.analysis.get("Sales by type")) {
							changeImagesForFilter(radioUnselected, radioSelected);
							multipleChoices = false;
							queries[1].changeImage(noProperty, noProperty);
							available = false;
							resetValuesChosen();
						} else {
							changeImagesForFilter(untick, tick);
							if (!available)
								resetValuesChosen();
							queries[1].changeImage(images[2], images[3]);
							multipleChoices = true;
							available = true;
						}
					}
				}
				if (booleans.get("showCountryBox")) {
					int countryEvent = checkCountrySelected();
					if (countryEvent != -1) {
						if (multipleChoices) {
							modifyMap(map.countries, countryBoxes, countryEvent - START_EVENTS_COUNTRY);
						} else {
							changeOtherStates(countryEvent - START_EVENTS_COUNTRY, countryBoxes, map.countries);
							if (!map.countries.get(countryBoxes[countryEvent - START_EVENTS_COUNTRY].getLabel())) {
								modifyMap(map.countries, countryBoxes, countryEvent - START_EVENTS_COUNTRY);
							}
						}

					}
				}
				if (booleans.get("showCounties")) {
					int countyEvent = checkCountySelected();
					if (countyEvent != -1) {
						if (countyEvent < START_EVENTS_COUNTIES + englandCounties.length) {
							if (multipleChoices)
								modifyMap(map.england, englandBoxes, countyEvent - START_EVENTS_COUNTIES);
							else {
								changeOtherStates(countyEvent - START_EVENTS_COUNTIES, englandBoxes, map.england);
								if (!map.england.get(englandBoxes[countyEvent - START_EVENTS_COUNTIES].getLabel())) {
									modifyMap(map.england, englandBoxes, countyEvent - START_EVENTS_COUNTIES);
								}
							}
						} else {
							int range = (START_EVENTS_COUNTIES + englandCounties.length);
							if (multipleChoices)
								modifyMap(map.wales, walesBoxes, countyEvent - range);
							else {
								changeOtherStates(countyEvent - range, walesBoxes, map.wales);
								if (!map.wales.get(walesBoxes[countyEvent - range].getLabel())) {
									modifyMap(map.wales, walesBoxes, countyEvent - range);
								}
							}
						}
					}
				}
				if (booleans.get("showPropertyBox")) {
					int propertyEvent = checkPropertyTypeSelected();
					if (propertyEvent != -1) {
						changeOtherStates(propertyEvent - START_EVENTS_PROPERTIES, propertyBoxes, map.propertyTypes);
						if (!map.propertyTypes.get(propertyBoxes[propertyEvent - START_EVENTS_PROPERTIES].getLabel()))
							modifyMap(map.propertyTypes, propertyBoxes, propertyEvent - START_EVENTS_PROPERTIES);
					}
				}
			}
		}

		// SH - select a year screen with radio buttons
		int yearEvent = currentScreen.getEvent(currentScreen.radioList);
		for (int i = 0; i < screenStructureList.size(); i++)
			if (yearEvent == screenStructureList.get(i).year) {
				screenTracker = yearEvent;
				setScreen();
				setRadios();
			}

		// SH - select a bar from a chart and go to trendview
		if (booleans.get("barChartShown") && !trend && !pieView) {
			int barEvent = checkBarSelected();
			if (barEvent != -1) {
				barClicked(currentScreen.chart.barList.get(barEvent).county, propertyType, analysisType);
			}
		}
		checkForClickPermission();
		
	}

	public void changeBooleansMaps(int index) {
		int current = 0;
		Iterator<Entry<String, Boolean>> it = booleans.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Boolean> pair = (Map.Entry<String, Boolean>) it.next();
			if (current == index) {
				if (pair.getValue()) {
					booleans.put(pair.getKey(), false);

				} else {
					booleans.put(pair.getKey(), true);
				}

			} else if (current > 3) {
				booleans.put(pair.getKey(), false);
			}

			current++;
		}
	}

	public void changeOtherStates(int state, Checkbox[] box, HashMap<String, Boolean> m) {
		for (int i = 0; i < box.length; i++) {
			if (i != state) {
				box[i].clear();
				String label = box[i].getLabel();
				m.put(label, false);
			}
		}
	}

	public void modifyMap(HashMap<String, Boolean> m, Checkbox[] box, int limit) {
		String label = box[limit].getLabel();
		box[limit].changeState();
		if (!m.get(label))
			m.put(label, true);
		else
			m.put(label, false);

	}

	public int checkCountySelected() {
		int j = currentButton;
		if (map.countries.get("England")) {
			for (int i = currentButton; i < englandBoxes.length; i++) {
				if (englandBoxes[i].getEvent(mouseX, mouseY) == countiesEvents[j]) {
					if (i == 0) {
						checkAll(englandBoxes, map.england, englandCounties);
					}
					return countiesEvents[j];
				}
				j++;
			}
		}
		j = englandBoxes.length;
		for (int i = walesButton; i < walesBoxes.length; i++) {
			if (walesBoxes[i].getEvent(mouseX, mouseY) == countiesEvents[j + walesButton]) {
				if (i == 0)
					checkAll(walesBoxes, map.wales, walesCounties);
				return countiesEvents[j + walesButton];
			}
			j++;
		}
		return -1;
	}

	public void checkAll(Checkbox[] box, HashMap<String, Boolean> map, String[] counties) {
		if (!map.get(box[0].getLabel())) {
			for (int i = 1; i < box.length; i++) {
				box[i].set();
				map.put(counties[i], true);
			}
		} else
			for (int i = 1; i < box.length; i++) {
				box[i].clear();
				map.put(counties[i], false);
			}
	}

	public int checkCountrySelected() {

		for (int i = 0; i < countryBoxes.length; i++) {

			if (countryBoxes[i].getEvent(mouseX, mouseY) == boxEvents[i]) {
				return boxEvents[i];
			}
		}
		return -1;
	}

	public int checkPropertyTypeSelected() {
		for (int i = 0; i < propertyBoxes.length; i++) {
			if (propertyBoxes[i].getEvent(mouseX, mouseY) == propertyEvents[i]) {
				return propertyEvents[i];
			}
		}
		return -1;
	}

	public int checkTypeSelected() {
		for (int i = 0; i < typeBoxes.length; i++) {
			if (typeBoxes[i].getEvent(mouseX, mouseY) == typeEvents[i]) {
				return typeEvents[i];
			}
		}
		return -1;
	}

	// SH - return which bar on the chart has been clicked, if any
	public int checkBarSelected() {
		if (!currentScreen.chart.lineGraph) {
			for (int i = 0; i < currentScreen.chart.barList.size(); i++) {
				if (currentScreen.chart.barList.get(i).mouseOver)
					return i;
			}
		}
		return -1;
	}

	public int checkForQuery() {
		int event = -1;
		int startingPoint = 1;
		for (int i = 0; i < queries.length; i++) {
			if (queries[i].getEvent(mouseX, mouseY) == events[startingPoint]) {
				event = events[startingPoint];
			}
			startingPoint++;
		}
		if (booleans.get("showAdditional")) {
			for (int i = 0; i < additionalQueries.length; i++) {
				if (additionalQueries[i].getEvent(mouseX, mouseY) == events[startingPoint]) {
					event = events[startingPoint];
				}
				startingPoint++;
			}
		}
		if (event != -1) {
			// clearBorders(event);
		}
		return event;
	}

	public void go() {
		booleans.put("showButtons", false);
		side.changeHeight();
		for (int i = 0; i < screenStructureList.size(); i++) {
			if (mapView && screenStructureList.get(i).markerManager != null) {
				geoMap.removeMarkerManager(screenStructureList.get(i).markerManager);
				screenStructureList.get(i).markerManager.clearMarkers();
			}
		}
		// SH - reset screen to default, find which queries and counties have
		// been selected
		widgetsOn = true;
		trend = false;
		screenTracker = 2017;
		ArrayList<String> counties = new ArrayList<String>();
		Iterator<Entry<String, Boolean>> it = map.england.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Boolean> pair = (Map.Entry<String, Boolean>) it.next();
			if (pair.getValue() && pair.getKey() != "All England Counties")
				counties.add(pair.getKey());
		}
		it = map.wales.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Boolean> pair = (Map.Entry<String, Boolean>) it.next();
			if (pair.getValue() && pair.getKey() != "All Wales Counties")
				counties.add(pair.getKey());
		}

		it = map.analysis.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Boolean> pair = (Map.Entry<String, Boolean>) it.next();
			if (pair.getValue())
				analysisType = pair.getKey();
		}

		it = map.propertyTypes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Boolean> pair = (Map.Entry<String, Boolean>) it.next();
			if (pair.getValue())
				propertyType = pair.getKey().substring(0, 1);
		}

		// SH - reset results for each screen
		for (int i = 0; i < screenStructureList.size(); i++)
			screenStructureList.get(i).results = new ArrayList<Integer>();

		// SH - generate results for selected query and property type
		for (int i = 0; i < counties.size(); i++) {
			for (int j = 0; j < screenStructureList.size(); j++) {
				switch (analysisType) {
				case "Average price":
					pieView = false;
					if (propertyType.equals("A"))
						screenStructureList.get(j).results
								.add(query.getAvgPrice(preparedTable, (j + 1995), counties.get(i)));
					else
						screenStructureList.get(j).results
								.add(query.getAvgPrice(preparedTable, (j + 1995), counties.get(i), propertyType));
					break;
				case "Maximum price":
					pieView = false;
					if (propertyType.equals("A"))
						screenStructureList.get(j).results
								.add(query.getMaxPrice(preparedTable, (j + 1995), counties.get(i)));
					else
						screenStructureList.get(j).results
								.add(query.getMaxPrice(preparedTable, (j + 1995), counties.get(i), propertyType));
					break;
				case "Number of transactions":
					pieView = false;
					if (propertyType.equals("A"))
						screenStructureList.get(j).results
								.add(query.getNumSales(preparedTable, (j + 1995), counties.get(i)));
					else
						screenStructureList.get(j).results
								.add(query.getNumSales(preparedTable, (j + 1995), counties.get(i), propertyType));
					break;
				case "Sales by type":
					pieView = true;
					for (int k = 1; k < property.length; k++) {
						screenStructureList.get(j).results.add(query.getNumSales(preparedTable, (j + 1995),
								counties.get(i), property[k].substring(0, 1)));
					}
					break;
				}
			}
		}

		// SH - separate query if sales by type for all counties selected
		if (analysisType.equals("Sales by type")
				&& (map.england.get("All England Counties") || map.wales.get("All Wales Counties"))) {
			pieView = true;
			for (int i = 0; i < screenStructureList.size(); i++) {
				for (int j = 1; j < property.length; j++) {
					int result = 0;
					if (map.england.get("All England Counties")) {
						for (int e = 1; e < englandCounties.length; e++)
							result += query.getNumSales(preparedTable, (i + 1995), englandCounties[e],
									property[j].substring(0, 1));
					} else if (map.wales.get("All Wales Counties")) {
						for (int w = 1; w < walesCounties.length; w++)
							result += query.getNumSales(preparedTable, (i + 1995), walesCounties[w],
									property[j].substring(0, 1));
					}
					screenStructureList.get(i).results.add(result);
				}
			}
		}

		// SH - calculate minimum and maximum results for scaling
		int minResult = MAX_INT;
		int maxResult = 0;
		for (int i = 0; i < screenStructureList.size(); i++) {
			for (int j = 0; j < screenStructureList.get(i).results.size(); j++) {
				if (screenStructureList.get(i).results.get(j) > maxResult)
					maxResult = screenStructureList.get(i).results.get(j);
				if (screenStructureList.get(i).results.get(j) < minResult)
					minResult = screenStructureList.get(i).results.get(j);
			}
		}
		float power10 = 100000;
		while (maxResult < power10) {
			power10 /= 10;
		}
		float tempPower = power10;
		while (power10 < maxResult)
			power10 += tempPower;
		maxResult = (int) power10;

		// SH - create chart/pie chart for every screen
		if (!mapView) {
			String county;
			if (map.england.get("All England Counties"))
				county = "All England Counties";
			else if (map.wales.get("All Wales Counties"))
				county = "All Wales Counties";
			else
				county = counties.get(0);

			for (int i = 0; i < screenStructureList.size(); i++)
				if (pieView) {
					firstPie = true;
					screenStructureList.get(i).pieChart = new PieChart(this, widthByPercent(50), heightByPercent(50),
							widthByPercent(40), screenStructureList.get(i).results, county,
							Arrays.copyOfRange(property, 1, property.length));
				} else {
					screenStructureList.get(i).chart = new Chart(this,
							widthByPercent(100) - side.width - countryBoxes[0].width - DISPLAY_PADDING * 2,
							heightByPercent(92), screenStructureList.get(i).results, counties, false, maxResult,
							screenStructureList.get(i).year, "");
					// screenStructureList.get(i).chart.setFont("Arial Rounded
					// MT Bold");
				}

		} else {
			countiesForMap = counties;
			geoMap = twoDMap;
			twoDMap.zoomAndPanTo(6, centreOfEngland);
			for (int i = 0; i < screenStructureList.size(); i++) {
				for (int index = 0; index < counties.size(); index++) {
					String[] GPS = query.getGPS(preparedTable, counties.get(index));
					double latitude = Double.parseDouble(GPS[0]);
					double longitude = Double.parseDouble(GPS[1]);

					de.fhpotsdam.unfolding.geo.Location LocationToAdd = new de.fhpotsdam.unfolding.geo.Location(
							latitude, longitude);

					if (LocationToAdd != null) {
						SimpleMarker marker = new SimpleMarker(LocationToAdd);
						float normBub = map(screenStructureList.get(i).results.get(index), minResult - 1, maxResult, 0,
								pow(45, 2) * PI);
						float radius = sqrt(normBub / PI);

						// Set size of marker
						marker.setRadius(radius);
						marker.setColor(color(150, 0, 0, 150));
						marker.setSelectedColor(color(34,139,34,150));
						screenStructureList.get(i).markerManager.addMarker(marker);
					}
				}
			}
		}
		setScreen();
		setRadios();
	}

	// SH - open trend view after clicking on a bar in the chart
	public void barClicked(String county, String propertyType, String analysisType) {
		trendScreen.results = new ArrayList<Integer>();
		ArrayList<String> years = new ArrayList<String>();
		for (int year = 1995; year <= 2017; year++) {
			years.add(Integer.toString(year));
			switch (analysisType) {
			case "Average price":
				if (propertyType.equals("A"))
					trendScreen.results.add(query.getAvgPrice(preparedTable, year, county));
				else
					trendScreen.results.add(query.getAvgPrice(preparedTable, year, county, propertyType));
				break;
			case "Maximum price":
				if (propertyType.equals("A"))
					trendScreen.results.add(query.getMaxPrice(preparedTable, year, county));
				else
					trendScreen.results.add(query.getMaxPrice(preparedTable, year, county, propertyType));
				break;
			case "Number of transactions":
				if (propertyType.equals("A"))
					trendScreen.results.add(query.getNumSales(preparedTable, year, county));
				else
					trendScreen.results.add(query.getNumSales(preparedTable, year, county, propertyType));
				break;
			}
		}

		Chart c = new Chart(this, widthByPercent(100) - side.width - countryBoxes[0].width - DISPLAY_PADDING * 2,
				heightByPercent(92.5), trendScreen.results, years, true, 0, trendScreen.year, county);
		// c.setFont("Arial Rounded MT Bold");
		trendScreen.chart = c;

		trend = true;
		currentScreen = trendScreen;
	}

	public float widthByPercent(double percent) {
		return (float) (percent * (displayWidth / 100));
	}

	public float heightByPercent(double percent) {
		return (float) (percent * (displayHeight / 100));
	}

	public float widthByPercentOfAvailable(double percent, int availableScreenSize) {
		return (float) (percent * (availableScreenSize / 100));
	}

	public float heightByPercentOfAvailable(double percent, int availableScreenSize) {
		return (float) (percent * (availableScreenSize / 100));
	}

	public void keyPressed() {
		if (!trend) {
			if (key == CODED) {
				if (keyCode == LEFT) {
					if (screenTracker > MIN_SCREEN)
						screenTracker--;
				} else if (keyCode == RIGHT) {
					if (screenTracker < MAX_SCREEN)
						screenTracker++;
				}
			} else if (key == '1') {
				aboveMap.zoomAndPanTo(geoMap.getZoomLevel(), geoMap.getCenter());
				geoMap = aboveMap;
			} else if (key == '2') {
				if (!geoMap.equals(twoDMap)) {
					twoDMap.zoomAndPanTo(geoMap.getZoomLevel(), geoMap.getCenter());
					geoMap = twoDMap;
				}
			}
			setScreen();
			setRadios();
		} else {
			if (keyCode == BACKSPACE) {
				trend = false;
				setScreen();
			}
		}
		if (enableWriting) {
			if (key == ENTER) {
				mapView = true;
				trend = false;
				widgetsOn = false;
				booleans.put("barChartShown", false);
				System.out.println(booleans.get("showButtons"));
				if (booleans.get("showButtons"))
					side.changeHeight();
				booleans.put("showButtons", false);
				infoTable = new ArrayList<TableField[]>();
				if (text.label.charAt(text.label.length() - 1) == '|')
					text.label = text.label.substring(0, text.label.length() - 1);
				mapToPostCode(text.label);
				getPropertyInfoTable(query.getPostcodeInfo(fullTable, text.label));
				currentScreen.markerManager.clearMarkers();
			}
			text.append(key);
		}
	}

	// SH - populate the ArrayList infoTable with arrays of TableFields
	public void getPropertyInfoTable(ArrayList<String[]> propertyInfo) {
		int CHARCOAL = color(46);
		int LIGHTER_CHARCOAL = color(80);
		float[] maxWidths = { 0, 0, 0, 0, 0, 0 };
		float tableY = text.y + text.height;
		String[] titles = { "Address Of Property", "Date Of Sale", "Price", "Property Type", "Age", "Ownership" };

		TableField[] titleRow = new TableField[6];

		float tableX = text.x;
		for (int i = 0; i < titleRow.length; i++) {
			titleRow[i] = new TableField(this, CHARCOAL, tableX, tableY, titles[i]);
			titleRow[i].setFont("Arial Rounded MT Bold");
			tableX += titleRow[i].width;
			if (titleRow[i].width > maxWidths[i])
				maxWidths[i] = titleRow[i].width;
			if (i == 5)
				tableY += titleRow[i].height;
		}
		infoTable.add(titleRow);

		for (int i = 0; i < propertyInfo.size(); i++) {
			String[] temp = propertyInfo.get(i);
			TableField[] tableRow = new TableField[6];
			tableX = text.x;
			for (int j = 0; j < temp.length; j++) {
				tableRow[j] = new TableField(this, LIGHTER_CHARCOAL, tableX, tableY, temp[j]);
				tableX += tableRow[j].width;
				if (tableRow[j].width > maxWidths[j])
					maxWidths[j] = tableRow[j].width;
				if (j == 5)
					tableY += tableRow[j].height;
			}
			infoTable.add(tableRow);
		}

		// set columns widths to all be equal to the largest
		for (int i = 0; i < infoTable.size(); i++) {
			TableField[] temp = infoTable.get(i);
			for (int j = 0; j < temp.length; j++) {
				if (temp[j].width < maxWidths[j]) {
					float difference = maxWidths[j] - infoTable.get(i)[j].width;
					infoTable.get(i)[j].width = maxWidths[j];
					infoTable.get(i)[j].x -= difference;
					int k = j;
					while (k < temp.length) {
						infoTable.get(i)[k++].x += difference;
					}
				}
			}
		}
	}

	public void drawPropertyInfoTable() {
		for (int i = 0; i < infoTable.size(); i++) {
			TableField[] temp = infoTable.get(i);
			for (int j = 0; j < temp.length; j++) {
				// temp[j].setFont("Arial Rounded MT Bold");
				temp[j].draw();
			}
		}
	}

	// SH - set the current yearview radio button to on and all others to false
	public void setRadios() {
		for (int i = 0; i < mainRadioList.size(); i++) {
			RadioWidget tempWidget = (RadioWidget) mainRadioList.get(i);
			if (i + MIN_SCREEN != screenTracker)
				tempWidget.radioImage = radioOffImage;
			else
				tempWidget.radioImage = radioOnImage;
		}
	}

	// SH - set the current screen according to the screenTracker
	public void setScreen() {
		if (widgetsOn) {
			for (int i = 0; i < screenStructureList.size(); i++) {
				if (screenStructureList.get(i).year == screenTracker) {
					if (!mapView && !pieView) {
						if (!currentScreen.chart.lineGraph) {
							ArrayList<Float> heights = currentScreen.chart.returnHeights();
							screenStructureList.get(i).chart.changeHeights(heights);
						}
					}
					else if (!mapView && pieView)
					{
						if(!firstPie)screenStructureList.get(i).pieChart.setSkip();
					}
					currentScreen = screenStructureList.get(i);
				}
			}
		}
	}
	
	public static void firstPie()
	{
		firstPie = false;
	}

	// Credit to Unfolding Maps Founder Till Nagel for the majority of code in
	// this function
	public void checkBoundingBox() {
		de.fhpotsdam.unfolding.geo.Location mapTopLeft = geoMap.getTopLeftBorder();
		de.fhpotsdam.unfolding.geo.Location mapBottomRight = geoMap.getBottomRightBorder();
		ScreenPosition mapTopLeftPos = geoMap.getScreenPosition(mapTopLeft);
		ScreenPosition boundTopLeftPos = geoMap.getScreenPosition(boundTopLeft);
		if (boundTopLeft.getLon() > mapTopLeft.getLon()) {
			geoMap.panBy(mapTopLeftPos.x - boundTopLeftPos.x, 0);
		}
		if (boundTopLeft.getLat() < mapTopLeft.getLat()) {
			geoMap.panBy(0, mapTopLeftPos.y - boundTopLeftPos.y);
		}
		ScreenPosition mapBottomRightPos = geoMap.getScreenPosition(mapBottomRight);
		ScreenPosition boundBottomRightPos = geoMap.getScreenPosition(boundBottomRight);
		if (boundBottomRight.getLon() < mapBottomRight.getLon()) {
			geoMap.panBy(mapBottomRightPos.x - boundBottomRightPos.x, 0);
		}
		if (boundBottomRight.getLat() > mapBottomRight.getLat()) {
			geoMap.panBy(0, mapBottomRightPos.y - boundBottomRightPos.y);
		}
	}

	public void mapToPostCode(String postCode) {
		try {

			if (!postCode.equals("")) {
				String page[] = loadStrings("http://postcodes.io/postcodes/" + postCode);
				String html = page[0];
				int p = html.indexOf("latitude");
				int from = html.indexOf(":", p);
				int to = html.indexOf(",", from);
				String lat = html.substring(from + 1, to);
				double latitude = Double.parseDouble(lat);
				int point = html.indexOf("longitude");
				int start = html.indexOf(":", point);
				int end = html.indexOf(",", start);
				String longitudeString = html.substring(start + 1, end);
				double longitude = Double.parseDouble(longitudeString);
				geoMap = aboveMap;
				geoMap.zoomAndPanTo(17, new de.fhpotsdam.unfolding.geo.Location(latitude, longitude));
			}
		} catch (Exception e) {
			System.err.println("An exception was thrown");
		}
	}

	public void mapHoverText() {
		DecimalFormat formatter = new DecimalFormat("#,###");
		boolean markerSet = false;
		for (int i = 0; i < currentScreen.markerManager.getMarkers().size(); i++) {
			currentScreen.markerManager.getMarkers().get(i).setSelected(false);
			if (currentScreen.markerManager.getMarkers().get(i).isInside(geoMap, mouseX, mouseY) & (!markerSet)) {
				markerSet = true;
				currentScreen.markerManager.getMarkers().get(i).setSelected(true);
				String text = (countiesForMap.get(i) + ": £" + formatter.format(currentScreen.results.get(i)));
				textFont(hoverLabelFont);
				float textWidth = textWidth(text);
				float boxWidth = textWidth + Project10.DISPLAY_PADDING * 2;
				float textHeight = textAscent() + textDescent();
				float boxHeight = textHeight + Project10.DISPLAY_PADDING * 2;
				strokeWeight(2);
				fill(220);
				rect(mouseX - (boxWidth / 2), mouseY - boxHeight * 2 - Project10.DISPLAY_PADDING, boxWidth, boxHeight,
						20);
				fill(0);
				textAlign(CENTER, CENTER);
				text(text, mouseX, mouseY - boxHeight * 3 / 2 - Project10.DISPLAY_PADDING * 3 / 2);
			}
		}
	}

	public void listen() {
		eventDispatcher.register(geoMap, PanMapEvent.TYPE_PAN, geoMap.getId());
		eventDispatcher.register(geoMap, ZoomMapEvent.TYPE_ZOOM, geoMap.getId());
	}

	public void mute() {
		eventDispatcher.unregister(geoMap, PanMapEvent.TYPE_PAN, geoMap.getId());
		eventDispatcher.unregister(geoMap, ZoomMapEvent.TYPE_ZOOM, geoMap.getId());
	}

}