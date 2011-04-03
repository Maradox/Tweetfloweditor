
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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public abstract class AbstractElement {
	
	protected int mX, mY, mWidth, mHeight;
	
	protected boolean mHighlighted;
	protected boolean mMarked;
	
	protected Drawable mShape;
	protected Rect mBounds;
	
	protected final int fillColor = Color.RED;
	protected final int borderColor = Color.BLACK;
	
	protected Integer mId;
	
	protected Context mContext;
	
	protected AbstractElement mClosedSequenceNext = null;
	
	AbstractElement(Context context, int id, int x, int y, int width, int height) {
		mContext = context;
		mId = id;
		mX = x;
		mY = y;
		mWidth = width;
		mHeight = height;
		mBounds = new Rect(x, y, x+width, y+height);
	}
	
	abstract public void draw(Canvas canvas);
	
	
	private void drawClosedSequence(Canvas canvas) {
		if(mClosedSequenceNext != null) {
			final Paint paint = new Paint();
			paint.setStrokeWidth(5);
			paint.setColor(Color.BLACK);
			paint.setAntiAlias(true);
			canvas.drawLine(getMiddleX(), 
					getBotY(), 
					mClosedSequenceNext.getMiddleX(), 
					mClosedSequenceNext.getTopY(), 
					paint);
		}
	}
	
	public void move(int xOff, int yOff) {
		mX += xOff;
		mY += yOff;
		mBounds.offset(xOff, yOff);
		mShape.setBounds(mBounds);
	}
	
	public void moveOn(int centerX, int centerY) {
		mX = centerX-mWidth/2;
		mY = centerY-mHeight/2;
		mBounds.set(centerX-mWidth/2, centerY-mHeight/2, centerX+mWidth/2, centerY+mHeight/2);
		mShape.setBounds(mBounds);
	}
	
	abstract public boolean isFocused(int x, int y);

	// TODO: elements should have 3 visual states:
	// - normal
	// - highlighted -> if current element receives touch event
	// - selected -> if it has been selected, after ACTION_UP
	
	
	abstract public void modeSelected();
	
	abstract public void modeNormal();
	
	abstract public void modeMarked();
	
	
	public boolean isSelected() {
		return mHighlighted;
	}
	
	public int getId() {
		return mId;
	}

	public int getMiddleX() {
		return mX+mWidth/2;
	}
	
	public int getMiddleY() {
		return mY+mHeight/2;
	}
	
	public int getTopY() {
		return mY;
	}
	
	public int getBotY() {
		return mY+mHeight;
	}

	public Drawable getmShape() {
		return mShape;
	}

	public void setmShape(Drawable mShape) {
		this.mShape = mShape;
	}
	
	public void setClosedSequenceNext(AbstractElement elem) {
		mClosedSequenceNext = elem;
	}
	
	public void removeClosedSequenceNext() {
		mClosedSequenceNext = null;
	}
	
}
