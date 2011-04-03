
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

package at.tuwien.dsgproject.tfe.views;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.entities.AbstractElement;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.quickAction.ActionItem;
import at.tuwien.dsgproject.tfe.quickAction.QuickAction;
import at.tuwien.dsgproject.tfe.states.State;
import at.tuwien.dsgproject.tfe.states.StateFree;
import at.tuwien.dsgproject.tfe.states.StateMoveAll;
import at.tuwien.dsgproject.tfe.states.StateMoveElement;
import at.tuwien.dsgproject.tfe.states.StateMoveSelected;
import at.tuwien.dsgproject.tfe.states.StateScale;
import at.tuwien.dsgproject.tfe.states.StateSelected;
import at.tuwien.dsgproject.tfe.states.StateTouchElement;
import at.tuwien.dsgproject.tfe.states.StateTouchVoid;

public class EditorView extends View {
	
	public enum EDITOR_STATE {
		FREE,
		SELECTED,
		TOUCH_ELEMENT,
		MOVE_ELEMENT,
		MOVE_SELECTED,
		TOUCH_VOID,
		MOVE_ALL,
		SCALE
	}

	private State mCurrState;
	private HashMap <EDITOR_STATE, State> mAvailableStates;
	
	
	public static final int RASTER_HORIZONTAL_WIDTH = 70;
	public static final int DISTANCE_FOR_AUTO_CONNECTION_X = 70;
	public static final int DISTANCE_FOR_AUTO_CONNECTION_Y = 120;
	
	public static final int MOVE_OFFSET = 8;
		
	


	private AbstractElement mTouchElement = null;

	public boolean setRaster = false;
	public int horizontalRasterCT;
	
	public boolean rasterOn = false;
	public SnapMode snapMode = SnapMode.NOTHING;
	
	public boolean openContextMenu = false;
			
	public enum SnapMode {	
		NOTHING,
		RASTER,
		GRID
	}
	
	
	private final int INVALID_POINTER_ID = -1;
	private int mActivePointerId = INVALID_POINTER_ID;
	
	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;
	
	// coordinates of last touch input
	private int mLastTouchX, mLastTouchY;
	// canvas offset
	private int mOffsetX, mOffsetY;
	// scale pivot coordinates
	private int mScalePivotX, mScalePivotY;
	
	private TweetFlow mTweetFlow;

	public EditorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mTweetFlow = new TweetFlow(getContext());
		mTweetFlow.fillElements();
		
		
		setOnLongClickListener(mOnLongClickListener);
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		
	
		prepareStates();
		

	}
	
	private void prepareStates() {
		mAvailableStates = new HashMap <EDITOR_STATE, State>();
		mAvailableStates.put(EDITOR_STATE.FREE, new StateFree(this, mTweetFlow));
		mAvailableStates.put(EDITOR_STATE.SELECTED, new StateSelected(this, mTweetFlow));
		mAvailableStates.put(EDITOR_STATE.TOUCH_ELEMENT, new StateTouchElement(this, mTweetFlow));
		mAvailableStates.put(EDITOR_STATE.MOVE_ELEMENT, new StateMoveElement(this, mTweetFlow));
		mAvailableStates.put(EDITOR_STATE.MOVE_SELECTED, new StateMoveSelected(this, mTweetFlow));
		mAvailableStates.put(EDITOR_STATE.TOUCH_VOID, new StateTouchVoid(this, mTweetFlow));
		mAvailableStates.put(EDITOR_STATE.MOVE_ALL, new StateMoveAll(this, mTweetFlow));
		mAvailableStates.put(EDITOR_STATE.SCALE, new StateScale(this, mTweetFlow));
		
		//TODO: just as a security measure
		for(EDITOR_STATE s : EDITOR_STATE.values()) {
			if (!mAvailableStates.containsKey(s)) {
				Toast.makeText(EditorView.this.getContext(), "STATE MISSING IN AVAILABLE STATES", Toast.LENGTH_SHORT).show();
			}
		}
		
		mCurrState = mAvailableStates.get(EDITOR_STATE.FREE);
	}
	
	
	public OnLongClickListener mOnLongClickListener = new OnLongClickListener() {
		public boolean onLongClick(View v) {
//	    	if(state instanceof StateTouchVoid) {
//	    		addRectangle(mOldX, mOldY);
//	    	} else if(state instanceof StateTouchElement) {
//	    		//openContextMenu = true; //TODO
//	    		openContextMenu();
//	    	}
    		
	    	return mCurrState.handleLongClick();
	    }
	};

		
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
	    @Override
	    public boolean onScale(ScaleGestureDetector detector) {
	        mScaleFactor *= detector.getScaleFactor();
	        
	        // Don't let the object get too small or too large.
	        mScaleFactor = Math.max(0.5f, Math.min(mScaleFactor, 3.0f));

//            mCurrMode = TouchMode.SCALE;
	        
	        mScalePivotX = (int)detector.getFocusX();
	        mScalePivotY = (int)detector.getFocusY();

	        invalidate();
	        return true;
	    }

//		@Override
//		public boolean onScaleBegin(ScaleGestureDetector detector) {
//			state = stateScale;
//			return true;
//		}
//
//		@Override
//		public void onScaleEnd(ScaleGestureDetector detector) {
//			state = stateFree;
//			super.onScaleEnd(detector);
//		}
	    
	    
	}
	
	

	
	

	
	

	

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	
		canvas.drawText(mCurrState.toString(), 10, 10, new Paint(Color.BLACK));
		//TODO: check how to handle save/restore with our lines
		canvas.save();
		canvas.translate(mOffsetX, mOffsetY);
    	canvas.scale(mScaleFactor, mScaleFactor);
		//canvas.scale(mScaleFactor, mScaleFactor, mScalePivotX, mScalePivotY);
    	
//	    if(rasterOn) {
//			if(!setRaster) {
//				setRaster = true;
//				horizontalRasterCT = (canvas.getWidth() / RASTER_HORIZONTAL_WIDTH) + 3;
//			}
//			
//			Paint paint = new Paint();
//			paint.setPathEffect( new DashPathEffect(new float[] { 10, 3, 6, 3 },1) );
//						
//			if((snapMode == SnapMode.NOTHING) || (snapMode == SnapMode.RASTER)) {
//				paint.setColor(Color.BLUE);
//			
//				ArrayList<Integer> gridLines = createRasterLines();
//		    	
//		    	for(int i=0; i<gridLines.size(); i++) {
//		    		canvas.drawLine(gridLines.get(i), 0-mPosY, gridLines.get(i), canvas.getHeight()-mPosY, paint);
//		    		canvas.drawText(""+gridLines.get(i), gridLines.get(i), 0, paint);		//TODO delete
//		    	}
//			}	
//			
//			if(snapMode == SnapMode.GRID) {
//				paint.setColor(Color.RED);
//		
//				for(AbstractElement e : mElements.values()) {
//					if(e instanceof Rectangle) {
//						canvas.drawLine(e.getMiddleX(), 0-mPosY, e.getMiddleX(), canvas.getHeight()-mPosY, paint);
//						canvas.drawText(""+e.getMiddleX(), e.getMiddleX(), 1, paint);		//TODO delete
//					}					
//				}
//			}
//		}	

	    

		//TODO: check for possible optimizations (eg. invalidate/redraw only for changed elements)
	    //TODO: clipping
//		for (OpenSequence os : mOpenSequences.values()) {
//			os.draw(canvas);
//		}
		
	    mTweetFlow.draw(canvas);


//		if(mCurrState instanceof StateMoveElement) {
//			Point ids = findElementForConnection();
//			Paint paint = new Paint();
//			paint.setStrokeWidth(5);
//			paint.setColor(Color.GRAY);
//			paint.setAntiAlias(true);
//			if(ids.x != -1) {
//				canvas.drawLine(mElements.get(ids.x).getMiddleX(),  mElements.get(ids.x).getTopY(), mTouchElement.getMiddleX(), mTouchElement.getBotY(), paint);
//			}	
//			if(ids.y != -1) {
//				canvas.drawLine(mElements.get(ids.y).getMiddleX(),  mElements.get(ids.y).getBotY(), mTouchElement.getMiddleX(), mTouchElement.getTopY(), paint);
//			}	
//		}
		
		canvas.restore();

	}
	
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	super.onTouchEvent(event);
    	
    	mScaleDetector.onTouchEvent(event);
    	mCurrState.onTouchEvent(event);
    	return true;	
    }
    
    public void setState(EDITOR_STATE state) {
    	if(!mAvailableStates.containsKey(state)) {
    		//TODO fixme
    		Toast.makeText(getContext(), "INVALID STATE", Toast.LENGTH_SHORT).show();
    	} else {
    		mCurrState = mAvailableStates.get(state);
    	}
    }
    
    
    public void moveTouchElement(int offX, int offY) {
    	if(mTouchElement != null) {
    		mTouchElement.move(offX, offY);
    		redraw();
    	}
    }
    
	private int scaledX(int x) {
		return (int)((x-mOffsetX)/mScaleFactor);
	}
	
	
	private int scaledY(int y) {
		return (int)((y-mOffsetY)/mScaleFactor);
	}

	
    public void moveSingleOn(int x, int y) {
    	if(mTouchElement != null)
    		mTouchElement.moveOn(x, y);
    }
     
    
    public void redraw() {
    	invalidate();
    }
    
    public int findRasterHorizontal(int xScaled) {
    	int x = xScaled - mOffsetX;
    	ArrayList<Integer> gridLines = createRasterLines();
    	
    	for(int i=0; i<gridLines.size()-1; i++) {
    		if(x>gridLines.get(i) && x<gridLines.get(i+1)) {
    			if((x-gridLines.get(i)) < (gridLines.get(i+1) - x)) {
    				return gridLines.get(i);
    			} else {
    				return gridLines.get(i+1);
    			}
    		}
    	}
    	
    	return -111;
    }
    
    public ArrayList<Integer> createRasterLines() {
    	ArrayList<Integer> gridLines = new ArrayList<Integer>();
    	
    	for(int i=0; i<horizontalRasterCT; i++) {
    		gridLines.add((Integer)(i*RASTER_HORIZONTAL_WIDTH - RASTER_HORIZONTAL_WIDTH/2 + mOffsetX % RASTER_HORIZONTAL_WIDTH - mOffsetX));
    	}
    	
    	return gridLines;
    }
    

    
//    public boolean isThereGridHorizontal(int xScaled) {
//    	int x = xScaled - mPosX;
//    	int xDiff = Integer.MAX_VALUE;
//    	    	
//    	for(AbstractElement e : mElements.values()) {
//			if(e instanceof Rectangle) {
//				if(mTouchElement.getId() != e.getId()) {
//					if((Math.abs(x - e.getMiddleX())) < xDiff) {
//						xDiff = Math.abs(x - e.getMiddleX());
//					}
//				}
//			}					
//		}
//    	
//    	if(xDiff < 15) 
//    		return true;
//    	
//    	return false;
//    }
//    
//    
//    public int findGridHorizontal(int xScaled) {
//    	int x = xScaled - mPosX;
//    	int xDiff = Integer.MAX_VALUE;
//    	int xNew = 0;
//    	    	
//    	for(AbstractElement e : mElements.values()) {
//			if(e instanceof Rectangle) {
//				if(mTouchElement.getId() != e.getId()) {
//					if((Math.abs(x - e.getMiddleX())) < xDiff) {
//						xDiff = Math.abs(x - e.getMiddleX());
//						xNew = e.getMiddleX();		
//					}
//				}
//			}					
//		}
//    	
//    	return xNew;
//    }
//
//    
//    public boolean isTouchOnGrid(int xScaled) {
//    	int x = xScaled - mPosX;
//    	int xDiff = Integer.MAX_VALUE;
//    	
//    	for(AbstractElement e : mElements.values()) {
//			if(e instanceof Rectangle) {
//				if((Math.abs(x - e.getMiddleX())) < xDiff) {
//					xDiff = Math.abs(x - e.getMiddleX());
//				}
//			}
//    	}	
//    	
//		if(xDiff < 30) 
//	    	return true;
//    	
//		return false;
//    }
//   
//    public int getTouchOnGrid(int xScaled) {
//    	int x = xScaled - mPosX;
//    	int xDiff = Integer.MAX_VALUE;
//    	int xNew = 0;
//    	
//    	for(AbstractElement e : mElements.values()) {
//			if(e instanceof Rectangle) {
//				if((Math.abs(x - e.getMiddleX())) < xDiff) {
//					xDiff = Math.abs(x - e.getMiddleX());
//					xNew = e.getMiddleX();		
//				}
//			}
//    	}	
//   
//	    return xNew;
//    }
//    
//    public void selectElementsOnGrid(int x) {
//    	for(AbstractElement e : mElements.values()) {
//			if(e instanceof Rectangle) {
//				if(e.getMiddleX() == x) {
//					mSelected.put(e.getId(), e);
//					e.modeSelected();
//				}
//			}
//    	}
//    }
    
    public void undo() {
    	//TODO
    }
    
    public void redo() {
    	//TODO
    }
    
    public void openContextMenu() {    	
    	final QuickAction qa = new QuickAction(this);
    	
    	class OwnOnDismissListener implements OnDismissListener{
			public void onDismiss() {
				redraw();
			}
		}
    	
    	qa.setOnDismissListener(new OwnOnDismissListener());
    	
    	ActionItem delete = new ActionItem();
    	delete.setTitle("Delete");
    	delete.setIcon(getResources().getDrawable(R.drawable.chart));
    	delete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mTweetFlow.deleteElement(mTouchElement.getId());
				redraw();
				qa.dismiss();
			}
		});
		qa.addActionItem(delete);

		ActionItem changeData = new ActionItem();
		changeData.setTitle("Change data");
		changeData.setIcon(getResources().getDrawable(R.drawable.production));
		changeData.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(EditorView.this.getContext(), "Change data", Toast.LENGTH_SHORT).show();
				redraw();
				qa.dismiss();
			}
		});
		qa.addActionItem(changeData);

		if(mTouchElement.isSelected()) {
			ActionItem deselect = new ActionItem();
			deselect.setTitle("Deselect");
			deselect.setIcon(getResources().getDrawable(R.drawable.production));
			deselect.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mTweetFlow.deleteElement(mTouchElement.getId());
					redraw();
					qa.dismiss();
				}
			});
			qa.addActionItem(deselect);
		}	
				
		qa.setAnimStyle(QuickAction.ANIM_AUTO);
		
		Rect rect = new Rect();
		rect.set(mTouchElement.getmShape().getBounds().left, mTouchElement.getmShape().getBounds().top, mTouchElement.getmShape().getBounds().right, mTouchElement.getmShape().getBounds().bottom);
		rect.offset(mOffsetX,this.getTop()+mOffsetY+24);
				
		qa.show(rect);
    }


	public boolean isRasterOn() {
		return rasterOn;
	}


	public void setRasterOn(boolean rasterOn) {
		this.rasterOn = rasterOn;
	}


	public SnapMode getSnapMode() {
		return snapMode;
	}


	public void setSnapMode(SnapMode snapMode) {
		this.snapMode = snapMode;
	}
	
	public void invalidatePointerId() {
		mActivePointerId = INVALID_POINTER_ID;
	}
	
	public int getActivePointerId() {
		return mActivePointerId;
	}
	
	public void setActivePointerId(int id) {
		mActivePointerId = id;
	}
	
	
	public int getLastTouchX() {
		return mLastTouchX;
	}
	
	public int getLastTouchY() {
		return mLastTouchY;
	}

	public void setLastTouch(int x, int y) {
		mLastTouchX = x;
		mLastTouchY = y;
	}
	
	public void offset(int offX, int offY) {
		mOffsetX += offX;
		mOffsetY += offY;
	}
	
	public void setTouchElement(AbstractElement elem) {
		mTouchElement = elem;
	}
	
	public AbstractElement getTouchElement() {
		return mTouchElement;
	}

	public Integer getTouchElementId() {
		if(mTouchElement != null) {
			return mTouchElement.getId();
		} else {
			return -1;
		}
	}
	
	public void setTweetFlow(TweetFlow tweetFlow) {
		mTweetFlow = tweetFlow;
		for(State s : mAvailableStates.values()) {
			s.setTweetFlow(tweetFlow);
		}
		redraw();
	}
	
	public boolean scaleDetectorActive() {
		return mScaleDetector.isInProgress();
	}
	
  
}
