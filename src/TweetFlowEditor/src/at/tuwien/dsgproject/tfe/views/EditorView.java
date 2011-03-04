package at.tuwien.dsgproject.tfe.views;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import at.tuwien.dsgproject.tfe.entities.AbstractElement;
import at.tuwien.dsgproject.tfe.entities.Rectangle;

public class EditorView extends View {
		
	private HashMap<Integer, AbstractElement> mElements;
	private HashMap<Integer, AbstractElement> mSelected;
	
	private int mOldX = 0, mOldY = 0;

	private Integer mElemCounter;
	
	//private int mTouchElementId;
	private AbstractElement mTouchElement = null;
	
	private enum TouchMode {	
		FREE,  		//no touch event
		TOUCH_VOID, // touch event on free space
		SELECTED,   //touch event on element
		MOVE_SELECTED, 	 //move selected elements
		MOVE_SINGLE,	 //move currently touched element
		MOVE_ALL	//move all elements
	}
	
	private TouchMode mCurrMode = TouchMode.FREE;
	
	
	public EditorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mElemCounter = 0;
		mElements = new HashMap<Integer, AbstractElement>();
		
		addRectangle(100,100,50,50);
		addRectangle(200,100,50,50);
		addRectangle(300,300,50,50);
		
		mSelected = new HashMap<Integer, AbstractElement>();
		
		this.setOnLongClickListener(onLongClickListener);
		
	}
	
	private void addRectangle(int x, int y, int height, int width) {
		mElemCounter++;
		mElements.put(mElemCounter, new Rectangle(mElemCounter, x, y, width, height));
	}
	
	
	/**
	 * Checks if an element is at the given location and returns it.
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return The element at the given location or null.
	 */
	private AbstractElement elementAt(int x, int y) {
		AbstractElement elem = null;
		for(AbstractElement e : mElements.values()) {
			if(e.contains(x,y)) {
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
		//AbstractElement elem;
		for (AbstractElement elem : mElements.values()) {
			elem.draw(canvas);
		}
	}
	
	
	// TODO: log all touchevents/modes to reproduce errors
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
    	int state = event.getAction();
    	int eventX = (int) event.getX();
    	int eventY = (int) event.getY();	    
    	
    	if (state == MotionEvent.ACTION_DOWN) {
    		onActionDown(eventX, eventY);
    		super.onTouchEvent(event); //For onLongClick
    		
    	} else if (state == MotionEvent.ACTION_UP) {	
    		onActionUp(eventX, eventY);
	    	
    	} else if (state == MotionEvent.ACTION_CANCEL) {
    		//TODO: when does this happen? + proper handling
    		//unset selected if TouchEvent ends
    		//mTouchElementId = -1;
    		mCurrMode = TouchMode.FREE;
    		
    	} else if (state == MotionEvent.ACTION_MOVE) {
    		onActionMove(eventX, eventY);
    		
    	}
    	
    	return true;	//TODO
    }
    
    private void onActionDown(int x, int y) {
		
    	if(mCurrMode == TouchMode.FREE) {
    		mTouchElement = elementAt(x, y);	
    		
    		if (mTouchElement != null) {
    			mCurrMode = TouchMode.SELECTED;
    			if(!mSelected.containsKey(mTouchElement.getId())) {
    				mTouchElement.highlight();
    				invalidate();
    			} 	
    		} else {
        		Toast.makeText(this.getContext(), "Nothing Selected", Toast.LENGTH_SHORT).show();
    			//TODO: long touch adds element
        		mCurrMode = TouchMode.TOUCH_VOID;
        	}
    		
    		mOldX = x;
    		mOldY = y;
    	}

    }
    
    
    private void onActionUp(int x, int y) {
    	if(mTouchElement != null) {
    		
	    	if(mCurrMode == TouchMode.MOVE_SINGLE) { // deselect item if it was the only one that has been moved			
				deselect(mTouchElement);
				invalidate();			
			} else if(mCurrMode == TouchMode.SELECTED) { 
				int id = mTouchElement.getId();
				if(!mSelected.containsKey(id)) { // add to selected elements, on first click
					mSelected.put(id, mTouchElement);
				} else { // deselect item on 2nd click
					mSelected.remove(id);
					mTouchElement.deHighlight();
					mTouchElement = null;
					invalidate();
				}
				mTouchElement = null;
				
			}
    	} else {
			//Toast.makeText(this.getContext(), "!! action up with no selected element", Toast.LENGTH_SHORT).show();
		}
    	mCurrMode = TouchMode.FREE;
    }
    
    private void onActionMove(int x, int y) {
		int offX = x - mOldX;
		int offY = y - mOldY;
		
		switch(mCurrMode) {
		case FREE:
			mCurrMode = TouchMode.MOVE_ALL;
			moveAll(offX, offY);
			break;
			
		case MOVE_ALL:
			moveAll(offX, offY);
			break;
		
		case SELECTED:
			if(mTouchElement != null) {	
				if( mSelected.size() >= 1 && mSelected.containsKey(mTouchElement.getId()) ) {
					// at least one element selected, and mTouchElement is one of them -> move selected
					mCurrMode = TouchMode.MOVE_SELECTED;
					moveSelected(offX, offY);		
				} else {
					// only move currently touched element
					mCurrMode = TouchMode.MOVE_SINGLE;
					moveSingle(offX, offY);	
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
			moveSelected(offX, offY);
			break;
			
		default:
			//should not come here ...
			Toast.makeText(this.getContext(), "!! invalid move mode?", Toast.LENGTH_SHORT).show();	
		}
		
		invalidate();
		mOldX = x;
		mOldY = y;
    }
    
    OnLongClickListener onLongClickListener = new OnLongClickListener() {
	    public boolean onLongClick(View v) {
	    	if(mCurrMode == TouchMode.TOUCH_VOID) {
	    		Toast.makeText(EditorView.this.getContext(), "Long Click on void", Toast.LENGTH_SHORT).show();	
	    	}
	    	else if(mCurrMode == TouchMode.SELECTED) {
	    		Toast.makeText(EditorView.this.getContext(), "Long Click on element", Toast.LENGTH_SHORT).show();	
	    	}
	    			
	        return false;
	    }
    };
    
    private void moveAll(int offX, int offY) {
		for(AbstractElement e : mElements.values()) {
			e.move(offX, offY);
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
    
    
    
    private void select(AbstractElement e) {
    	mSelected.put(e.getId(), e);
		e.highlight();
    }
    
    private void deselect(AbstractElement e) {
		mSelected.remove(e.getId());
		e.deHighlight();
    }
  
}
