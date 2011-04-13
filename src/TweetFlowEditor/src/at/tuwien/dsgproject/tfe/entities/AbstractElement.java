
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
	
	protected boolean mSelected;
	
	protected Drawable mShape;
	protected Rect mBounds;
	
	protected final int fillColor = Color.RED;
	protected final int borderColor = Color.BLACK;
	
	protected Integer mId;
	
	protected Context mContext;
	
	protected AbstractElement mClosedSequenceNext = null;
	protected AbstractElement mClosedSequencePrev = null;
	protected AbstractElement mClosedSequenceMaybeNext = null;
	
	protected AbstractElement mLoop = null;
	
	AbstractElement(Context context, int id, int x, int y, int width, int height) {
		mContext = context;
		mId = id;
		mX = x;
		mY = y;
		mWidth = width;
		mHeight = height;
		mBounds = new Rect(x, y, x+width, y+height);
	}
	
	public void draw(Canvas canvas) {
		if(mClosedSequenceNext != null) {			
			Paint paint = new Paint();
			paint.setStrokeWidth(5);
			paint.setColor(Color.DKGRAY);
			paint.setAntiAlias(true);
			canvas.drawLine(getMiddleX(), 
					getBotY(), 
					mClosedSequenceNext.getMiddleX(), 
					mClosedSequenceNext.getTopY(), 
					paint);
		}
		else if(mClosedSequenceMaybeNext != null) {
			Paint paint = new Paint();
			paint.setStrokeWidth(5);
			paint.setColor(Color.GRAY);
			paint.setAntiAlias(true);

			canvas.drawLine(getMiddleX(), 
					getBotY(), 
					mClosedSequenceMaybeNext.getMiddleX(), 
					mClosedSequenceMaybeNext.getTopY(), 
					paint);
		}
		
		if(mLoop != null)
			drawLoop(canvas);
	}
	
	public void drawLoop(Canvas canvas) {
		Paint paint = new Paint();
		paint.setStrokeWidth(5);
		paint.setColor(Color.GREEN);
		paint.setAntiAlias(true);
		
		
		if((mLoop.getRightX() - mX + 40) < 0) {
			canvas.drawLine(mX, 
					getMiddleY(), 
					mLoop.getRightX() + 20, 
					getMiddleY(), 
					paint);
			
			canvas.drawLine(mLoop.getRightX() + 20, 
					getMiddleY(), 
					mLoop.getRightX() + 20, 
					mLoop.getMiddleY(), 
					paint);
			
			
			canvas.drawLine(mLoop.getRightX() + 20, 
					mLoop.getMiddleY(), 
					mLoop.getRightX(), 
					mLoop.getMiddleY(), 
					paint);
		}
		else if ((getRightX() + 40 - mLoop.getmX()) < 0){		
			canvas.drawLine(mX, 
					getMiddleY(), 
					mX - 20, 
					getMiddleY(), 
					paint);
			
			canvas.drawLine(mX - 20, 
					getMiddleY(), 
					mX - 20, 
					mLoop.getMiddleY(), 
					paint);
			
			canvas.drawLine(mX - 20, 
					mLoop.getMiddleY(), 
					mLoop.getmX(), 
					mLoop.getMiddleY(), 
					paint);
		}	
		
		else {
			if(mX < mLoop.getmX()) {
				canvas.drawLine(mX, 
						getMiddleY(), 
						mX - 20, 
						getMiddleY(), 
						paint);
				
				canvas.drawLine(mX - 20, 
						getMiddleY(), 
						mX - 20, 
						mLoop.getMiddleY(), 
						paint);
				
				canvas.drawLine(mX - 20, 
						mLoop.getMiddleY(), 
						mLoop.getmX(), 
						mLoop.getMiddleY(), 
						paint);
			}
		 	else {
				canvas.drawLine(mX, 
						getMiddleY(), 
						mLoop.getmX() - 20, 
						getMiddleY(), 
						paint);
				
				canvas.drawLine(mLoop.getmX() - 20, 
						getMiddleY(), 
						mLoop.getmX() - 20, 
						mLoop.getMiddleY(), 
						paint);
				
				canvas.drawLine(mLoop.getmX() - 20, 
						mLoop.getMiddleY(), 
						mLoop.getmX(), 
						mLoop.getMiddleY(), 
						paint);
		 	}
		}
	}
	
//	private void drawClosedSequence(Canvas canvas) {
//		if(mClosedSequenceNext != null) {
//			final Paint paint = new Paint();
//			paint.setStrokeWidth(5);
//			paint.setColor(Color.BLACK);
//			paint.setAntiAlias(true);
//			canvas.drawLine(getMiddleX(), 
//					getBotY(), 
//					mClosedSequenceNext.getMiddleX(), 
//					mClosedSequenceNext.getTopY(), 
//					paint);
//		}
//	}
	
	public void move(int xOff, int yOff) {
		mX += xOff;
		mY += yOff;
		mBounds.offset(xOff, yOff);
		mShape.setBounds(mBounds);
	}
//	
//	public void moveOn(int centerX, int centerY) {
//		mX = centerX-mWidth/2;
//		mY = centerY-mHeight/2;
//		mBounds.set(centerX-mWidth/2, centerY-mHeight/2, centerX+mWidth/2, centerY+mHeight/2);
//		mShape.setBounds(mBounds);
//	}
	
	abstract public boolean isFocused(int x, int y);
	
	public boolean isTextFocused(int x, int y) {
		return false;
	}

	// TODO: elements should have 3 visual states:
	// - normal
	// - highlighted -> if current element receives touch event
	// - selected -> if it has been selected, after ACTION_UP
	
		
	abstract public void modeSelected();
	
	abstract public void modeNormal();
	
	abstract public void modeMarked();

	
	public boolean isSelected() {
		return mSelected;
	}
	
	public int getId() {
		return mId;
	}

	public int getMiddleX() {
		return mX+mWidth/2;
	}
	
	public int getRightX() {
		return mX+mWidth;
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
		
	public AbstractElement getClosedSequenceMaybeNext() {
		return mClosedSequenceMaybeNext;
	}

	public void setClosedSequenceMaybeNext(AbstractElement closedSequenceMaybeNext) {
		mClosedSequenceMaybeNext = closedSequenceMaybeNext;
	}

	public AbstractElement getClosedSequenceNext() {
		return mClosedSequenceNext;
	}

	public void setClosedSequenceNext(AbstractElement closedSequenceNext) {
		if(closedSequenceNext != null) {
			mClosedSequenceNext = closedSequenceNext;
			closedSequenceNext.mClosedSequencePrev = this;
		}
	}
	
	public void removeClosedSequencePrev() {
		if(mClosedSequencePrev != null) {
			mClosedSequencePrev.mClosedSequenceNext = null;
			mClosedSequencePrev = null;
		}
	}
	
	public void removeClosedSequenceNext() {
		if(mClosedSequenceNext != null) {
			mClosedSequenceNext.mClosedSequencePrev = null;
			mClosedSequenceNext = null;
		}
	}
	
    
    public void checkRemoveNext() {
    	if(mClosedSequenceNext != null) {
    		final int offX = getMiddleX()-mClosedSequenceNext.getMiddleX();
    		final int offY = mClosedSequenceNext.getTopY()-getBotY();
    		if(offY > TweetFlow.DISTANCE_FOR_AUTO_DISCONNECTION_Y || offY < 0 ||
    				Math.abs(offX) > TweetFlow.DISTANCE_FOR_AUTO_DISCONNECTION_X) {
    			removeClosedSequenceNext();
    		}
    	}
    }
    
    
    public void checkRemovePrev() {
    	if(mClosedSequencePrev != null) {
			final int offX = getMiddleX()-mClosedSequencePrev.getMiddleX();
			final int offY = getTopY()-mClosedSequencePrev.getBotY();
			if(offY > TweetFlow.DISTANCE_FOR_AUTO_DISCONNECTION_Y || offY < 0 ||
					Math.abs(offX) > TweetFlow.DISTANCE_FOR_AUTO_DISCONNECTION_X) {
				removeClosedSequencePrev();
			}
    	}
	}
    
    //TODO: reorder to improve performance
    public void checkMaybeConnections(AbstractElement candidate) {
    	final int offX = getMiddleX()-candidate.getMiddleX();
		//selected element above new candidate to be cs next
		//next element is null and candidate has no previous element
    	if(Math.abs(offX) < TweetFlow.DISTANCE_FOR_AUTO_CONNECTION_X) {
    		if(mClosedSequenceNext == null && 
    				candidate.mClosedSequencePrev == null) {
    			final int offY = candidate.getTopY()-getBotY();
    			//selected above e?
    			if(offY > 0 && offY < TweetFlow.DISTANCE_FOR_AUTO_CONNECTION_Y) {
    				mClosedSequenceMaybeNext = candidate;
    			}
    		
    		} 
    		//candidate above selected??
    		if (candidate.mClosedSequenceNext == null &&
    				mClosedSequencePrev == null ){
    			final int offY = getTopY()-candidate.getBotY();
    			//selected below e?
    			if(offY > 0 && offY < TweetFlow.DISTANCE_FOR_AUTO_CONNECTION_Y) {
    				candidate.mClosedSequenceMaybeNext = this;
    			}
    		} 
    	}
    }
	
	
	
	
	
	
	public AbstractElement getmLoop() {
		return mLoop;
	}

	public void setmLoop(AbstractElement mLoop) {
		this.mLoop = mLoop;
	}

	public void resetMaybeConnections() {
		mClosedSequenceMaybeNext = null;
	}
	
	public boolean equals(AbstractElement e) {
		return this.mId == e.mId;
	}

	public int getmX() {
		return mX;
	}
	
	
	
	
	
	
}
