
/*
 *  Tweetfloweditor - a graphical editor to create Tweetflows
 *  
 *  Copyright (C) 2011  Matthias Neumayr
 *  Copyright (C) 2011  Martin Perebner
 *  
 *  Tweetfloweditor is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  Tweetfloweditor is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with Tweetfloweditor.  If not, see <http://www.gnu.org/licenses/>.
 */

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
