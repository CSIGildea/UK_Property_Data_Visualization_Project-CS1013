package project10;

import java.util.HashMap;

import processing.core.PGraphics;
import processing.core.PVector;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;

/**
 * Marker representing a single location. Use directly to display as simple circle, or extend it for custom styles.
 */
public class SimpleMarker extends AbstractMarker {
	private int color;
	private int selectedColor;
	protected float diameter = 20f;

	/**
	 * Creates an empty point marker. Used internally by the MarkerFactory.
	 */
	public SimpleMarker() {
		this(null, null);
	}

	/**
	 * Creates a point marker for the given location.
	 * 
	 * @param location
	 *            The location of this Marker.
	 */
	public SimpleMarker(Location location) {
		this(location, null);
	}

	/**
	 * Creates a point marker for the given location and properties.
	 * 
	 * @param location
	 *            The location of this Marker.
	 * @param properties
	 *            Some data properties for this marker.
	 */
	public SimpleMarker(Location location, HashMap<String, Object> properties) {
		super(location, properties);
	}

	/**
	 * Draws this point marker as circle in the defined style. If no style has been set, Unfolding's default one is
	 * used.
	 */
	@Override
	public void draw(PGraphics pg, float x, float y) {
		if (isHidden())
			return;

		pg.pushStyle();
		pg.strokeWeight(strokeWeight);
		if (isSelected()) {
			pg.strokeWeight(0);
			pg.fill(this.selectedColor);
			pg.stroke(this.selectedColor);
		} else {
			pg.strokeWeight(0);
			pg.fill(this.color);
			pg.stroke(this.color);
		}
		pg.ellipse((int) x, (int) y, diameter, diameter);
		pg.popStyle();
	}

	@Override
	public boolean isInside(float checkX, float checkY, float x, float y) {
		PVector pos = new PVector(x, y);
		return pos.dist(new PVector(checkX, checkY)) < diameter / 2;
	}

	/**
	 * Sets the radius of this marker. Used for the displayed ellipse and hit test.
	 * 
	 * @param radius The radius of the circle in pixel.
	 * @deprecated Fixed behavior! (value was wrongly used as diameter). Use {@link #setDiameter(float)} instead. 
	 */
	public void setRadius(float radius) {
		if(radius>0)
		{
			this.diameter = radius * 2;
		}
	}

	/**
	 * Sets the diameter of this marker. Used for the displayed ellipse and hit test.
	 * 
	 * @param diameter The diameter of the circle in pixel.
	 */
	public void setDiameter(float diameter) {
		if(diameter>0)
		{
			this.diameter = diameter;
		}
	}
	public void setColor(int color) {
		this.color = color;
	}
	public void setSelectedColor(int color) {
		this.selectedColor = color;
	}
	public float getDiameter() {
		return this.diameter;
	}
	public int getColor() {
		return this.color;
	}
	public int getSelectedColor() {
		return this.selectedColor;
	}
	public float getRadius() {
		return this.diameter;
	}
}
