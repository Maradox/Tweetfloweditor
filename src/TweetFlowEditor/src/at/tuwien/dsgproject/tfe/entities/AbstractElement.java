package at.tuwien.dsgproject.tfe.entities;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;

public abstract class AbstractElement {
	
	protected int mX, mY, mWidth, mHeight;
	protected double mScale;
	
	protected boolean mHighlighted;
	
	protected ShapeDrawable mShape;
	
	protected final int fillColor = Color.RED;
	protected final int borderColor = Color.BLACK;
	
	protected int mId;
	
	AbstractElement(int id, int x, int y, int width, int height, double scale) {
		mId = id;
		mX = x;
		mY = y;
		mWidth = width;
		mHeight = height;
		mScale = scale;
	}
	
	AbstractElement(int id, int x, int y, int width, int height) {
		this(id, x, y, width, height, 1);
	}

	abstract public void draw(Canvas canvas);
	
	public void move(int xOff, int yOff) {
		mShape.getBounds().offset(xOff, yOff);
	}
	
	abstract public void scale(float scaleFactor);
	
	abstract public boolean contains(int x, int y);

	// TODO: elements should have 3 visual states:
	// - normal
	// - highlighted -> if current element receives touch event
	// - selected -> if it has been selected, after ACTION_UP
	
	public void highlight() {
		mHighlighted = true;
	}
	
	public void deHighlight() {
		mHighlighted = false;
	}
	
	public boolean isSelected() {
		return mHighlighted;
	}
	
	public int getId() {
		return mId;
	}
	
}
