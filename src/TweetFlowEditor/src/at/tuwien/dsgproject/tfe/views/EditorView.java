
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
import java.util.Map.Entry;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.entities.AbstractElement;
import at.tuwien.dsgproject.tfe.entities.Rectangle;

public class EditorView extends View {

	private static int RASTER_HORIZONTAL_WIDTH = 70;
	
	private final int mMoveOffset;
		
	private HashMap<Integer, AbstractElement> mElements;
	private HashMap<Integer, AbstractElement> mSelected;
	
	private int mOldX = 0, mOldY = 0;

	private Integer mElemCounter;
	
	//private int mTouchElementId;
	private AbstractElement mTouchElement = null;

	private boolean setRaster = false;
	private int horizontalRasterCT;
	
	private boolean rasterOn = true;
	private SnapMode snapMode = SnapMode.NOTHING;
	
	//private int xGlobalOffset = 0;
	
	private Point containerStart;
	private Point containerEnd;
		
	
	public enum TouchMode {	
		FREE,  			//no touch event
		TOUCH_VOID, 	//touch event on free space
		SELECTED,   	//touch event on element
		MOVE_SELECTED, 	//move selected elements
		MOVE_SINGLE,	//move currently touched element
		MOVE_ALL,		//move all elements
		MOVE_ALL_GRID,	//move all elements on a grid
		NEW_ELEMENT, 	//a new element has been inserted
		ELEMENT_MENU, 	//after long touch on element
		SCALE,			//pinch2zoom gesture is detected
		CONTAINER_DOWN,
		CONTAINER_MOVE
	}
	
	public enum SnapMode {	
		NOTHING,
		RASTER,
		GRID
	}
	
	private TouchMode mCurrMode = TouchMode.FREE;
	
	private int INVALID_POINTER_ID = -1;
	private int mActivePointerId = INVALID_POINTER_ID;
	
	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;
	
	private int mPosX;
	private int mPosY;

	
	public EditorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mElemCounter = 0;
		mElements = new HashMap<Integer, AbstractElement>();
		
		addRectangle(100,100);
		addRectangle(200,100);
		addRectangle(300,300);
		
		mSelected = new HashMap<Integer, AbstractElement>();
		containerStart = new Point();
		containerEnd = new Point();
		
		this.setOnLongClickListener(mOnLongClickListener);		
		
		Resources res = getResources();
		mMoveOffset = res.getInteger(R.integer.move_offset);
		
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		
	}
	
	OnLongClickListener mOnLongClickListener = new OnLongClickListener() {
		    public boolean onLongClick(View v) {
		    	if(mCurrMode == TouchMode.TOUCH_VOID) {
		    		addRectangle(mOldX, mOldY);
		    	} else if(mCurrMode == TouchMode.SELECTED) {
		    		mCurrMode = TouchMode.ELEMENT_MENU;
		    		// show element menu mTouchElement
		    		Toast.makeText(EditorView.this.getContext(), "Long Click on element", Toast.LENGTH_SHORT).show();	
		    	}
	    			
		    	return false;
		    }
		};
		
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
	    @Override
	    public boolean onScale(ScaleGestureDetector detector) {
	        mScaleFactor *= detector.getScaleFactor();
	        
	        // Don't let the object get too small or too large.
	        mScaleFactor = Math.max(0.5f, Math.min(mScaleFactor, 3.0f));
	        mCurrMode = TouchMode.SCALE;

	        invalidate();
	        return true;
	    }
	}
	    
	
	private void addRectangle(int x, int y) {
		final int xScaled = scaleX(x);
		final int yScaled = scaleY(y);
		mElemCounter++;
		//ugly hack to insert rectangle centered on touch event
		//maybe x and y should be the center of AbstractElements instead of the upper left corner
		mElements.put(mElemCounter, new Rectangle(mElemCounter, xScaled-25, yScaled-25, 50, 50));
		invalidate();
	}
	
	
	/**
	 * Checks if an element is at the given location and returns it.
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return The element at the given location or null.
	 */
	private AbstractElement elementAt(int x, int y) {
		final int xScaled = scaleX(x);
		final int yScaled = scaleY(y);
		AbstractElement elem = null;
		for(AbstractElement e : mElements.values()) {
			if(e.contains(xScaled,yScaled)) {
				elem = e;
				break;
			}	
		}
		return elem;
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);

	    canvas.save();
	    canvas.translate(mPosX, mPosY);
	    canvas.scale(mScaleFactor, mScaleFactor);


		if(rasterOn) {
			if(!setRaster) {
				setRaster = true;
				horizontalRasterCT = (canvas.getWidth() / RASTER_HORIZONTAL_WIDTH) + 3;
			}
			
			Paint paint = new Paint();
			paint.setPathEffect( new DashPathEffect(new float[] { 10, 3, 6, 3 },1) );
			
			if((snapMode == SnapMode.NOTHING) || (snapMode == SnapMode.RASTER)) {
				paint.setColor(Color.BLUE);
			
				ArrayList<Integer> gridLines = createRasterLines();
		    	
		    	for(int i=0; i<gridLines.size(); i++) {
		    		canvas.drawLine(gridLines.get(i)- mPosX, 0, gridLines.get(i)- mPosX, canvas.getHeight(), paint);
		    	}
			}	
			
			if(snapMode == SnapMode.GRID) {
				paint.setColor(Color.RED);
		
				for(AbstractElement e : mElements.values()) {
					if(e instanceof Rectangle) {
						canvas.drawLine(e.getMiddleX(), 0, e.getMiddleX(), canvas.getHeight(), paint);
					}					
				}
			}
		}	

	    canvas.save();
	    canvas.translate(mPosX, mPosY);
	    canvas.scale(mScaleFactor, mScaleFactor);

		//TODO: check for possible optimizations (eg. invalidate/redraw only for changed elements)
	    //TODO: clipping
		for (AbstractElement elem : mElements.values()) {
			elem.draw(canvas);
		}
		

		//TODO: Container als "Element" übernimmt zeichnen selbst
		if(mCurrMode == TouchMode.CONTAINER_MOVE) {
			Paint paint = new Paint();
			paint.setStrokeWidth(5);
			paint.setColor(Color.GREEN);
			
			canvas.drawLine(containerStart.x,  containerStart.y, containerStart.x,  containerEnd.y, paint);
			canvas.drawLine(containerEnd.x,  containerStart.y, containerEnd.x,  containerEnd.y, paint);
			canvas.drawLine(containerStart.x,  containerStart.y, containerEnd.x,  containerStart.y, paint);
			canvas.drawLine(containerStart.x,  containerEnd.y, containerEnd.x,  containerEnd.y, paint);
		}
		
		canvas.restore();

	}
	
	// TODO: log all touchevents/modes to reproduce errors
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	 // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(event);

    	final int action = event.getAction();
    	
    	switch (action & MotionEvent.ACTION_MASK) {
    	case MotionEvent.ACTION_DOWN:
    		onActionDown(event);
    		super.onTouchEvent(event); //For onLongClick
    		break;
    	
    	case MotionEvent.ACTION_UP:	
    		onActionUp(event);
 
    		break;
    	 
    	case MotionEvent.ACTION_CANCEL:
    		//TODO: when does this happen? + proper handling
    		//unset selected if TouchEvent ends
    		//mTouchElementId = -1;
    		mCurrMode = TouchMode.FREE;
    		mActivePointerId = INVALID_POINTER_ID;
    		break;
    		
    	case MotionEvent.ACTION_MOVE:
    		onActionMove(event);
    		break;
    	 	
    	case MotionEvent.ACTION_POINTER_UP:
    		onActionPointerUp(action, event);
            break;
    	
    	default:
    		//nothing
    	
    	}
    	
    	return true;	//TODO
    }
    
    private void onActionDown(MotionEvent event) {
		int xGrid;
		
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		
    	if(mCurrMode == TouchMode.FREE) {    	
    	
    		mTouchElement = elementAt(x, y);	
    		
    		if (mTouchElement != null) {
    			mCurrMode = TouchMode.SELECTED;
    			if(!mSelected.containsKey(mTouchElement.getId())) {
    				mTouchElement.highlight();
    				invalidate();
    			} 	
    		}   	
    		else if(snapMode == SnapMode.GRID && rasterOn && ((xGrid = getTouchOnGrid(x)) != -111)) {
    			selectElementsOnGrid(xGrid);
    			mCurrMode = TouchMode.MOVE_ALL_GRID;			
        	}
    		else
				mCurrMode = TouchMode.TOUCH_VOID;
    		
    		mOldX = x;
    		mOldY = y;
    			
    	} else if(mCurrMode == TouchMode.CONTAINER_DOWN) {
			containerStart.set(x,y);
			mCurrMode = TouchMode.CONTAINER_MOVE;
		}
    	
    	//save current pointer id
		mActivePointerId = event.getPointerId(0);

    }
    
    private void onActionUp(MotionEvent event) {  
    	final int x = (int)event.getX();
    	final int y = (int)event.getY();


    	if(mTouchElement != null) {
    		switch(mCurrMode) {
    		case MOVE_SINGLE:
    			if(snapMode == SnapMode.RASTER) {
	    			int rasterX = findRasterHorizontal(mOldX);
	    			moveSingleOn(rasterX, y);
    			}	
    			
    			else if(snapMode == SnapMode.GRID) {
	    			int gridX = findGridHorizontal(mOldX);
	    			if(gridX != -111)
	    				moveSingleOn(gridX, y);
    			}	
    			//fallthrough
    			
    		case ELEMENT_MENU:
    			mTouchElement.deHighlight();
				invalidate();
				break;
				
    		case SELECTED:
    			int id = mTouchElement.getId();
				if(!mSelected.containsKey(id)) { // add to selected elements, on first click
					mSelected.put(id, mTouchElement);
				} else { // deselect item on 2nd click
					mSelected.remove(id);
					mTouchElement.deHighlight();
					//mTouchElement = null;
					invalidate();
				}
				break;
				
    		case MOVE_ALL_GRID:	
    			delesectAll();
    			invalidate();
			
    		case CONTAINER_MOVE:
    			containerEnd.set(x, y);	//TODO
    			invalidate();
			default:
				//TODO: (Toast)/Logging
					
    		}
    		mTouchElement = null;
    	} else {
    		switch(mCurrMode) {
	    		case MOVE_ALL_GRID:	
	    			delesectAll();
	    			invalidate();
    		}			
    	}
    	    	
			//Toast.makeText(this.getContext(), "!! action up with no selected element", Toast.LENGTH_SHORT).show();		
    	mCurrMode = TouchMode.FREE;
   		mActivePointerId = INVALID_POINTER_ID;
    }
    
    private void onActionMove(MotionEvent event) {
    	
		final int pointerIndex = event.findPointerIndex(mActivePointerId);
        final int x = (int)event.getX(pointerIndex);
        final int y = (int)event.getY(pointerIndex);
		final int offX = x - mOldX;
		final int offY = y - mOldY;
		
		switch(mCurrMode) {
		case TOUCH_VOID:
			if(Math.sqrt(offX*offX + offY*offY) > mMoveOffset) {
				mCurrMode = TouchMode.MOVE_ALL;
				//fallthrough
			} else {
				break;
			}
			
		case MOVE_ALL:
			mPosX += offX;
			mPosY += offY;

			break;
		
		case SELECTED:
			if(mTouchElement != null) {
				if(Math.sqrt(offX*offX + offY*offY) > mMoveOffset) {
					if( mSelected.size() >= 1 && mSelected.containsKey(mTouchElement.getId()) ) {
						// at least one element selected, and mTouchElement is one of them -> move selected
						mCurrMode = TouchMode.MOVE_SELECTED;
						moveSelected(offX, offY);		
					} else {
						// only move currently touched element
						mCurrMode = TouchMode.MOVE_SINGLE;
						moveSingle(offX, offY);	
					}
				}
			} else {
				//should not be here ...
				Toast.makeText(this.getContext(), "!!select mode, but element == null?", Toast.LENGTH_SHORT).show();		
			}
			break;
				
		case MOVE_SINGLE:
			moveSingle(offX, offY);
			break;
		
		case MOVE_SELECTED:
		case MOVE_ALL_GRID:	
			moveSelected(offX, offY);
			break;

		case SCALE:
			//TODO: center canvas on gesture
			break;
			
		case NEW_ELEMENT:
			break;
			
		case ELEMENT_MENU:
			break;
			
		case CONTAINER_MOVE:
			containerEnd.set(x, y);
			invalidate();
			break;
			
		//TODO handle/ignore all modes here
			
		default:
			//....
			Toast.makeText(this.getContext(), "!! invalid move mode?", Toast.LENGTH_SHORT).show();	

		}
			
		mOldX = x;
		mOldY = y;
		invalidate();
    }
    
    
    private void onActionPointerUp(int action, MotionEvent event) {
        // get index of the pointer that left the screen
        final int pIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) 
                >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pId = event.getPointerId(pIndex);
        if (pId == mActivePointerId) {
            // choose new active pointer
            final int newPointerIndex = pIndex == 0 ? 1 : 0;
            mOldX = (int)event.getX(newPointerIndex);
            mOldY = (int)event.getY(newPointerIndex);
            mActivePointerId = event.getPointerId(newPointerIndex);
        }
    }
    
    
    private void moveSelected(int offX, int offY) {
		for(AbstractElement e : mSelected.values()) {
			e.move(offX, offY);
		}
    }
    
    private void moveSingle(int offX, int offY) {
    	if(mTouchElement != null)
    		mTouchElement.move(offX, offY);
    }
    
	private int scaleX(int x) {
		return (int)((x-mPosX)/mScaleFactor);
	}
	
	
	private int scaleY(int y) {
		return (int)((y-mPosY)/mScaleFactor);
	}

	
    private void moveSingleOn(int x, int y) {
    	if(mTouchElement != null)
    		mTouchElement.moveOn(x, y);
    }
    
    
    public void delesectAll() {
    	for(Entry<Integer, AbstractElement> e : mSelected.entrySet()) {
    		e.getValue().deHighlight();
    	}
    	mSelected = new HashMap<Integer, AbstractElement>();
    	mCurrMode = TouchMode.FREE;
    }
    
    public void redraw() {
    	invalidate();
    }
    
    public int findRasterHorizontal(int x) {
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
    		gridLines.add((Integer)(i*RASTER_HORIZONTAL_WIDTH) + (RASTER_HORIZONTAL_WIDTH/2) - RASTER_HORIZONTAL_WIDTH +(mPosX % RASTER_HORIZONTAL_WIDTH));
    	}
    	
    	return gridLines;
    }
    
    public int findGridHorizontal(int x) {
    	int xDiff = Integer.MAX_VALUE;
    	int xNew = -111;
    	
    	for(AbstractElement e : mElements.values()) {
			if(e instanceof Rectangle) {
				if(mTouchElement.getId() != e.getId()) {
					if((Math.abs(x - e.getMiddleX())) < xDiff) {
						xDiff = Math.abs(x - e.getMiddleX());
						xNew = e.getMiddleX();		
					}
				}
			}					
		}
    	
    	if(xDiff < 15) 
    		return xNew;
    	
    	return -111;
    }

    
    public int getTouchOnGrid(int x) {
    	int xDiff = Integer.MAX_VALUE;
    	int xNew = -111;
    	
    	for(AbstractElement e : mElements.values()) {
			if(e instanceof Rectangle) {
				if((Math.abs(x - e.getMiddleX())) < xDiff) {
					xDiff = Math.abs(x - e.getMiddleX());
					xNew = e.getMiddleX();		
				}
			}
    	}	
    	
		if(xDiff < 30) 
	    	return xNew;
    	
		return -111;
    }
    
    public void selectElementsOnGrid(int x) {
    	for(AbstractElement e : mElements.values()) {
			if(e instanceof Rectangle) {
				if(e.getMiddleX() == x) {
					mSelected.put(e.getId(), e);
					e.highlight();
				}
			}
    	}
    }
    
    
    public void undo() {
    	//TODO
    }
    
    public void redo() {
    	//TODO
    }
    
    
	public TouchMode getmCurrMode() {
		return mCurrMode;
	}


	public void setmCurrMode(TouchMode mCurrMode) {
		this.mCurrMode = mCurrMode;
	}
	
	public boolean somethingSelected() {
		return !(mSelected.isEmpty());
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
  
}
