
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

import java.util.HashMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;
import at.tuwien.dsgproject.tfe.common.RasterGridHelper;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.quickAction.QuickAction;
import at.tuwien.dsgproject.tfe.states.State;
import at.tuwien.dsgproject.tfe.states.StateCreateLoop;
import at.tuwien.dsgproject.tfe.states.StateFree;
import at.tuwien.dsgproject.tfe.states.StateMoveAll;
import at.tuwien.dsgproject.tfe.states.StateMoveElement;
import at.tuwien.dsgproject.tfe.states.StateMoveGrid;
import at.tuwien.dsgproject.tfe.states.StateMoveSelected;
import at.tuwien.dsgproject.tfe.states.StateSelected;
import at.tuwien.dsgproject.tfe.states.StateTouchElement;
import at.tuwien.dsgproject.tfe.states.StateTouchVoid;


/**
 * EditorView
 * 
 * @author Matthias Neumayr
 * @author Martin Perebner
 * 
 * This view implements the graphical TweetFlow editor, the handling of
 * touch-input and all menues to change/select Tweetflow elements.
 */
public class EditorView extends View {
	
	public enum EDITOR_STATE {
		FREE,
		SELECTED,
		TOUCH_ELEMENT,
		MOVE_ELEMENT,
		MOVE_SELECTED,
		TOUCH_VOID,
		MOVE_ALL,
		MOVE_GRID,
		CREATE_LOOP
	}

	private State mCurrState;
	private HashMap <EDITOR_STATE, State> mAvailableStates;
	
	
	public static final int MOVE_OFFSET = 8;
			
	public boolean openContextMenu = false;
			
	
	private final int INVALID_POINTER_ID = -1;
	private int mActivePointerId = INVALID_POINTER_ID;
	
	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;
	
	private GestureDetector mTapDetector;
	
	// coordinates of last touch input
	private int mLastTouchX, mLastTouchY;
	// canvas offset
	private Integer mOffsetX = 0, mOffsetY = 0;
	// scale pivot coordinates
	//private int mScalePivotX, mScalePivotY;	//TODO delete comment
	
	private TweetFlow mTweetFlow;
	private RasterGridHelper rasterGridHelper;

	public EditorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mTweetFlow = new TweetFlow(getContext());
		
		
		setOnLongClickListener(mOnLongClickListener);
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

		mTapDetector = new GestureDetector(context, new DoubleTapListener());		
		rasterGridHelper = new RasterGridHelper(context, mTweetFlow, mOffsetX, mOffsetY, this);

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
		mAvailableStates.put(EDITOR_STATE.MOVE_GRID, new StateMoveGrid(this, mTweetFlow));
		mAvailableStates.put(EDITOR_STATE.CREATE_LOOP, new StateCreateLoop(this, mTweetFlow));
		
		for(EDITOR_STATE s : EDITOR_STATE.values()) {
			if (!mAvailableStates.containsKey(s)) {
				Toast.makeText(EditorView.this.getContext(), "STATE MISSING IN AVAILABLE STATES", Toast.LENGTH_SHORT).show();
			}
		}
		
		mCurrState = mAvailableStates.get(EDITOR_STATE.FREE);
	}
	
	
	public OnLongClickListener mOnLongClickListener = new OnLongClickListener() {
		public boolean onLongClick(View v) {
	    	return mCurrState.handleLongClick();
	    }
	};

		
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
	    @Override
	    public boolean onScale(ScaleGestureDetector detector) {
	        mScaleFactor *= detector.getScaleFactor();
	        
	        // Don't let the object get too small or too large.
	        mScaleFactor = Math.max(0.5f, Math.min(mScaleFactor, 3.0f));

	        invalidate();
	        return true;
	    }
	       
	}
	
	
    private class DoubleTapListener extends GestureDetector.SimpleOnGestureListener {	
    	@Override
    	public boolean onDoubleTap(MotionEvent e) {
    		resetView();
    		return true;
    	}
    }
    
    protected void resetView() {
		mOffsetX = mOffsetY = 0;
		mScaleFactor = 1.f;
		redraw();
    }
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	
		canvas.drawText(mCurrState.toString(), 10, 10, new Paint(Color.BLACK));

		canvas.save();
		canvas.translate(mOffsetX, mOffsetY);
    	canvas.scale(mScaleFactor, mScaleFactor);
		
     	rasterGridHelper.setOffset(mOffsetX,mOffsetY);
    	rasterGridHelper.draw(canvas);
	    mTweetFlow.draw(canvas);
	
		canvas.restore();

	}
	
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	super.onTouchEvent(event);
    	mTapDetector.onTouchEvent(event);
    	mScaleDetector.onTouchEvent(event);
    	mCurrState.onTouchEvent(event);
    	return true;	
    }
    
    public void setState(EDITOR_STATE state) {
    	if(!mAvailableStates.containsKey(state)) {
    		Toast.makeText(getContext(), "INVALID STATE", Toast.LENGTH_SHORT).show();
    	} else {
    		mCurrState = mAvailableStates.get(state);
    	}
    }
    
    
    
	public int scaledX(int x) {
		return (int)((x-mOffsetX)/mScaleFactor);
	}
	
	public int scaledLastX() {
		return (int)((mLastTouchX-mOffsetX)/mScaleFactor);
	}
	
	
	public int scaledY(int y) {
		return (int)((y-mOffsetY)/mScaleFactor);
	}
	
	public int scaledLastY() {
		return (int)((mLastTouchY-mOffsetY)/mScaleFactor);
	}

    
    public void redraw() {
    	invalidate();
    }
    
    public void openContextMenu() {    	
    	final QuickAction qa = new QuickAction(this);
    	
    	class OwnOnDismissListener implements OnDismissListener{
			public void onDismiss() {
				redraw();
			}
		}
    	
    	qa.setOnDismissListener(new OwnOnDismissListener());
    	qa.setAnimStyle(QuickAction.ANIM_AUTO);
    	
    	mTweetFlow.prepareQuickactions(qa, this);

		Rect rect = new Rect();
		rect.set(mTweetFlow.getTouchElement().getmShape().getBounds());
		rect.offset(mOffsetX,this.getTop()+mOffsetY+24);
				
		qa.show(rect);
    }


	public boolean isRasterOn() {
		return rasterGridHelper.getRasterOn();
	}


	public void setRasterOn(boolean rasterOn) {
		rasterGridHelper.setRasterOn(rasterOn);
	}

	public int getSnapMode() {
		return rasterGridHelper.getSnapMode();
	}
	
	public void setSnapMode(int snapMode) {
		rasterGridHelper.setSnapMode(snapMode);
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

	
	public void setTweetFlow(TweetFlow tweetFlow) {
		mTweetFlow = tweetFlow;
		for(State s : mAvailableStates.values()) {
			s.setTweetFlow(tweetFlow);
		}
		rasterGridHelper.setTweetFlow(mTweetFlow);
		resetView();
		redraw();
	}
	
	public boolean scaleDetectorActive() {
		return mScaleDetector.isInProgress();
	}

	public RasterGridHelper getRasterGridHelper() {
		return rasterGridHelper;
	}

	public Integer getmOffsetX() {
		return mOffsetX;
	}

	public float getmScaleFactor() {
		return mScaleFactor;
	}
	
	public TweetFlow getTweetFlow() {
		return mTweetFlow;
	}
	
	public void setCreateLoopState(Integer id) {
		setState(EDITOR_STATE.CREATE_LOOP);
		mTweetFlow.getTouchElement().modeMarked();
		((StateCreateLoop) mCurrState).setStartID(id);
	}
	
	
	
	
  
}
