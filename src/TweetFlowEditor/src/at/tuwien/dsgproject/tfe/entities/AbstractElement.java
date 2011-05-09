
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

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.quickAction.ActionItem;
import at.tuwien.dsgproject.tfe.quickAction.QuickAction;
import at.tuwien.dsgproject.tfe.views.EditorView;

public abstract class AbstractElement {
	
	@Attribute
	protected Integer mId;
	
	//Dimensions
	@Attribute
	protected int mX;
	@Attribute
	protected int mY;
	@Attribute
	protected int mWidth;
	@Attribute
	protected int mHeight;

	//Loops
	private String mSelfLoopCondition;
	protected Boolean mSelfLoop = false;
	
	private String closedLoopCondition;
	private String condition = "";
	protected AbstractElement mLoop = null;
	
	@Element(required=false)
	protected AbstractElement mClosedSequenceNext = null;
	// @Element(required=false) //NOT as @Element, set with TweetFlow.updateClosedSequences() after deserialization
	protected AbstractElement mClosedSequencePrev = null;
	protected AbstractElement mClosedSequenceMaybeNext = null;
	
	protected boolean mSelected;

	//Context and Drawables
	protected Context mContext;
	protected Drawable mShape;
	protected Rect mBounds;
	protected Bitmap mSelfLoopImage;

	AbstractElement(int id, int x, int y, int width, int height) {
		mId = id;
		mX = x;
		mY = y;
		mWidth = width;
		mHeight = height;
		mBounds = new Rect(x, y, x+width, y+height);	           
	}
	
	public void setContextAndDrawables(Context context) {
		mContext = context;
		mSelfLoopImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.loop); 
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
		
		if(mSelfLoop) {
			drawSelfLoop(canvas);
		}
	}
	
	public void drawSelfLoop(Canvas canvas) {
		if(mSelfLoopImage != null)
			canvas.drawBitmap(mSelfLoopImage, mX-18, mY, null);  
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

	// elements have 3 visual states:
	// - normal
	// - marked -> if current element receives touch event
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
	
	
	
	public AbstractElement getClosedSequencePrev() {
		return mClosedSequencePrev;
	}

	public void setClosedSequencePrev(AbstractElement mClosedSequencePrev) {
		this.mClosedSequencePrev = mClosedSequencePrev;
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

	public Boolean isSelfLoop() {
		return mSelfLoop;
	}

	public void setSelfLoop(Boolean selfLoop) {
		mSelfLoop = selfLoop;
	}

	public String getSelfLoopCondition() {
		return mSelfLoopCondition;
	}

	public void setSelfLoopCondition(String selfLoopCondition) {
		mSelfLoopCondition = selfLoopCondition;
	}

	public String getClosedLoopCondition() {
		return closedLoopCondition;
	}

	public void setClosedLoopCondition(String closedLoopCondition) {
		this.closedLoopCondition = closedLoopCondition;
	}	
	
	public void updateClosedSequences() {
		if(mClosedSequenceNext != null) {
			mClosedSequenceNext.mClosedSequencePrev = this;
		}
	}
	
	
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	abstract void fillQuickActionMenu(final QuickAction qa, final EditorView view);
	
	protected final void fillCommonQuickactionItems(final QuickAction qa, final EditorView view) {
		final Resources res = mContext.getResources();
		
    	ActionItem delete = new ActionItem();
    	delete.setTitle("Delete");
    	delete.setIcon(res.getDrawable(R.drawable.chart));
    	delete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				view.getTweetFlow().deleteElement(mId);
				qa.dismiss();
			}
		});
		qa.addActionItem(delete);
		/*
		ActionItem bigLoop = new ActionItem();
		bigLoop.setTitle("Big loop");
		bigLoop.setIcon(res.getDrawable(R.drawable.production));
		bigLoop.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				view.setCreateLoopState(mId);
				qa.dismiss();
			}
		});
		qa.addActionItem(bigLoop);
		*/
		/*
		ActionItem selfLoop = new ActionItem();
		if(mSelfLoop) {
			selfLoop.setTitle("Loop");
			selfLoop.setIcon(res.getDrawable(R.drawable.production));
			selfLoop.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mSelfLoop = true;
					qa.dismiss();
				}
			});
			qa.addActionItem(selfLoop);	
		} else {
			selfLoop.setTitle("Unloop");
			selfLoop.setIcon(res.getDrawable(R.drawable.production));
			selfLoop.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mSelfLoop = false;
					qa.dismiss();
				}
			});
			qa.addActionItem(selfLoop);	
		}*/
	}
}
