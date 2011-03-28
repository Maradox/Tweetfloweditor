
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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.entities.AbstractElement;
import at.tuwien.dsgproject.tfe.entities.Rectangle;
import at.tuwien.dsgproject.tfe.quickAction.ActionItem;
import at.tuwien.dsgproject.tfe.quickAction.QuickAction;
import at.tuwien.dsgproject.tfe.states.StateFree;
import at.tuwien.dsgproject.tfe.states.StateMoveAll;
import at.tuwien.dsgproject.tfe.states.StateMoveElement;
import at.tuwien.dsgproject.tfe.states.StateMoveSelected;
import at.tuwien.dsgproject.tfe.states.StateSelected;
import at.tuwien.dsgproject.tfe.states.State;
import at.tuwien.dsgproject.tfe.states.StateTouchElement;
import at.tuwien.dsgproject.tfe.states.StateTouchVoid;

public class EditorView extends View {

	public State state;
	public StateFree stateFree;
	public StateSelected stateSelected;
	public StateTouchElement stateTouchElement;
	public StateMoveElement stateMoveElement;
	public StateMoveSelected stateMoveSelected;
	public StateTouchVoid stateTouchVoid;
	public StateMoveAll stateMoveAll;
	
	public static int RASTER_HORIZONTAL_WIDTH = 70;
	
	public final int mMoveOffset;
		
	public HashMap<Integer, AbstractElement> mElements;
	public HashMap<Integer, AbstractElement> mSelected;
	
	public int mOldX = 0, mOldY = 0;

	public Integer mElemCounter;
	
	//public int mTouchElementId;
	public AbstractElement mTouchElement = null;

	public boolean setRaster = false;
	public int horizontalRasterCT;
	
	public boolean rasterOn = false;
	public SnapMode snapMode = SnapMode.NOTHING;
	
	//public int xGlobalOffset = 0;
	
	public Point containerStart;
	public Point containerEnd;
	
	public boolean openContextMenu = false;
			
	public enum SnapMode {	
		NOTHING,
		RASTER,
		GRID
	}
	
	
	public int INVALID_POINTER_ID = -1;
	public int mActivePointerId = INVALID_POINTER_ID;
	
	public ScaleGestureDetector mScaleDetector;
	public float mScaleFactor = 1.f;
	

	public int mPosX;
	public int mPosY;
	
	private int mScalePivotX;
	private int mScalePivotY;
	
	ShapeDrawable mContainer;

	
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
		
		mMoveOffset = 7;
		
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		
		stateFree = new StateFree(this);
		stateSelected = new StateSelected(this);
		stateTouchElement = new StateTouchElement(this);
		stateMoveElement = new StateMoveElement(this);
		stateMoveSelected = new StateMoveSelected(this);
		stateTouchVoid = new StateTouchVoid(this);
		stateMoveAll= new StateMoveAll(this);
		state = stateFree;
		
	}
	
	
	
	OnLongClickListener mOnLongClickListener = new OnLongClickListener() {
		public boolean onLongClick(View v) {
	    	if(state instanceof StateTouchVoid) {
	    		addRectangle(mOldX, mOldY);
	    	} else if(state instanceof StateTouchElement) {
	    		//openContextMenu = true; //TODO
	    		openContextMenu();
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

	  //      mCurrMode = TouchMode.SCALE;
	        
	        mScalePivotX = (int)detector.getFocusX();
	        mScalePivotY = (int)detector.getFocusY();


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
	public AbstractElement elementAt(int x, int y) {
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
		//canvas.drawColor(Color.WHITE);

		
		//TODO: check how to handle save/restore with our lines
		canvas.save();
		canvas.translate(mPosX, mPosY);
    	canvas.scale(mScaleFactor, mScaleFactor);
		//canvas.scale(mScaleFactor, mScaleFactor, mScalePivotX, mScalePivotY);
    	
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
		    		canvas.drawLine(gridLines.get(i), 0-mPosY, gridLines.get(i), canvas.getHeight()-mPosY, paint);
		    		canvas.drawText(""+gridLines.get(i), gridLines.get(i), 0, paint);		//TODO delete
		    	}
			}	
			
			if(snapMode == SnapMode.GRID) {
				paint.setColor(Color.RED);
		
				for(AbstractElement e : mElements.values()) {
					if(e instanceof Rectangle) {
						canvas.drawLine(e.getMiddleX(), 0-mPosY, e.getMiddleX(), canvas.getHeight()-mPosY, paint);
						canvas.drawText(""+e.getMiddleX(), e.getMiddleX(), 1, paint);		//TODO delete
					}					
				}
			}
		}	

	    

		//TODO: check for possible optimizations (eg. invalidate/redraw only for changed elements)
	    //TODO: clipping
		for (AbstractElement elem : mElements.values()) {
			elem.draw(canvas);
		}
		

		//TODO: Container als "Element" übernimmt zeichnen selbst
//		if(mCurrMode == TouchMode.CONTAINER_MOVE) {
//			Paint paint = new Paint();
//			paint.setStrokeWidth(5);
//			paint.setColor(Color.GREEN);
//			
//			canvas.drawLine(containerStart.x,  containerStart.y, containerStart.x,  containerEnd.y, paint);
//			canvas.drawLine(containerEnd.x,  containerStart.y, containerEnd.x,  containerEnd.y, paint);
//			canvas.drawLine(containerStart.x,  containerStart.y, containerEnd.x,  containerStart.y, paint);
//			canvas.drawLine(containerStart.x,  containerEnd.y, containerEnd.x,  containerEnd.y, paint);
//		}
		
		if(mContainer != null) {
			mContainer.draw(canvas);
		}	
		
		canvas.restore();

	}
	
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	super.onTouchEvent(event);
    	
		state.onTouchEvent(event);
        mScaleDetector.onTouchEvent(event);
        mScalePivotX = mScalePivotY = 0;

    	return true;	
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
    
    
    public void moveSelected(int offX, int offY) {
		for(AbstractElement e : mSelected.values()) {
			e.move(offX, offY);
		}
    }
    
    public void moveSingle(int offX, int offY) {
    	if(mTouchElement != null)
    		mTouchElement.move(offX, offY);
    }
    
	private int scaleX(int x) {
		return (int)((x-mPosX)/mScaleFactor);
	}
	
	
	private int scaleY(int y) {
		return (int)((y-mPosY)/mScaleFactor);
	}

	
    public void moveSingleOn(int x, int y) {
    	if(mTouchElement != null)
    		mTouchElement.moveOn(x, y);
    }
    
    
    public void delesectAll() {
    	for(Entry<Integer, AbstractElement> e : mSelected.entrySet()) {
    		e.getValue().deHighlight();
    	}
    	mSelected = new HashMap<Integer, AbstractElement>();
    //	mCurrMode = TouchMode.FREE;
    }
    
    public void redraw() {
    	invalidate();
    }
    
    public int findRasterHorizontal(int xScaled) {
    	int x = xScaled - mPosX;
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
    		gridLines.add((Integer)(i*RASTER_HORIZONTAL_WIDTH - RASTER_HORIZONTAL_WIDTH/2 + mPosX % RASTER_HORIZONTAL_WIDTH -mPosX));
    	}
    	
    	return gridLines;
    }
    
    public boolean isThereGridHorizontal(int xScaled) {
    	int x = xScaled - mPosX;
    	int xDiff = Integer.MAX_VALUE;
    	    	
    	for(AbstractElement e : mElements.values()) {
			if(e instanceof Rectangle) {
				if(mTouchElement.getId() != e.getId()) {
					if((Math.abs(x - e.getMiddleX())) < xDiff) {
						xDiff = Math.abs(x - e.getMiddleX());
					}
				}
			}					
		}
    	
    	if(xDiff < 15) 
    		return true;
    	
    	return false;
    }
    
    
    public int findGridHorizontal(int xScaled) {
    	int x = xScaled - mPosX;
    	int xDiff = Integer.MAX_VALUE;
    	int xNew = 0;
    	    	
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
    	
    	return xNew;
    }

    
    public boolean isTouchOnGrid(int xScaled) {
    	int x = xScaled - mPosX;
    	int xDiff = Integer.MAX_VALUE;
    	
    	for(AbstractElement e : mElements.values()) {
			if(e instanceof Rectangle) {
				if((Math.abs(x - e.getMiddleX())) < xDiff) {
					xDiff = Math.abs(x - e.getMiddleX());
				}
			}
    	}	
    	
		if(xDiff < 30) 
	    	return true;
    	
		return false;
    }
   
    public int getTouchOnGrid(int xScaled) {
    	int x = xScaled - mPosX;
    	int xDiff = Integer.MAX_VALUE;
    	int xNew = 0;
    	
    	for(AbstractElement e : mElements.values()) {
			if(e instanceof Rectangle) {
				if((Math.abs(x - e.getMiddleX())) < xDiff) {
					xDiff = Math.abs(x - e.getMiddleX());
					xNew = e.getMiddleX();		
				}
			}
    	}	
   
	    return xNew;
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
    
    public void createContainer() {	//TODO
    	float[] outerR = new float[] { 12, 12, 12, 12, 12, 12, 12, 12 };
    	mContainer = new ShapeDrawable(new RoundRectShape(outerR, null,null));
    	mContainer.getPaint().setColor(Color.TRANSPARENT);
    	mContainer.setBounds(10, 10, 100, 200);
    
    }
    
    public void undo() {
    	//TODO
    }
    
    public void redo() {
    	//TODO
    }
    
    public void openContextMenu() {    	
    	final QuickAction qa = new QuickAction(this);
    	
    	ActionItem delete = new ActionItem();
    	delete.setTitle("Delete");
    	delete.setIcon(getResources().getDrawable(R.drawable.chart));
    	delete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mSelected.remove(mTouchElement.getId());
				mElements.remove(mTouchElement.getId());
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

		if(mSelected.containsValue(mTouchElement)) {
			ActionItem deselect = new ActionItem();
			changeData.setTitle("Deselect");
			changeData.setIcon(getResources().getDrawable(R.drawable.production));
			changeData.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mSelected.remove(mTouchElement.getId());
					redraw();
					qa.dismiss();
				}
			});
			qa.addActionItem(deselect);
		}	
		
		
		qa.setAnimStyle(QuickAction.ANIM_AUTO);
		
		Rect rect = new Rect();
		rect.set(mTouchElement.getmShape().getBounds().left, mTouchElement.getmShape().getBounds().top, mTouchElement.getmShape().getBounds().right, mTouchElement.getmShape().getBounds().bottom);
		rect.offset(mPosX,this.getTop()+mPosY+24);
				
		qa.show(rect);
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
