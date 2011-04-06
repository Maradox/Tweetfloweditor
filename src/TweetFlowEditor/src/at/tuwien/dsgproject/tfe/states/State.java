package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;

public abstract class State {

	protected EditorView mEditorView;	
	protected TweetFlow mTweetFlow;
	
	public State(EditorView editorView, TweetFlow tweetFlow) {
		mEditorView = editorView;
		mTweetFlow = tweetFlow;
	}
	
	final public void onTouchEvent(MotionEvent event) {
		final int action = event.getAction();
    	
    	switch (action & MotionEvent.ACTION_MASK) {
    	case MotionEvent.ACTION_DOWN:
    		onActionDown(event);
    		break;
    	
    	case MotionEvent.ACTION_UP:	
    		onActionUp(event);
    		break;
    		
    	case MotionEvent.ACTION_MOVE:
    		onActionMove(event);
    		break;
    	 
    	case MotionEvent.ACTION_CANCEL:
//    		mEditorView.setState(EDITOR_STATE.FREE);
//    		mEditorView.invalidatePointerId();
    		onActionUp(event);
    		break;
    		 	
    	case MotionEvent.ACTION_POINTER_UP:
    		onActionPointerUp(action, event);
            break;
            
    	default:
    		//nothing
    	
    	}
	}
	
	protected void onActionDown(MotionEvent event) {}
	protected void onActionMove(MotionEvent event) {}
	protected void onActionUp(MotionEvent event) {}
	
    final protected void onActionPointerUp(int action, MotionEvent event) {
        // get index of the pointer that left the screen
		final int pIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) 
        		>> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
	    final int pId = event.getPointerId(pIndex);
	    if (pId == mEditorView.getActivePointerId()) {
	        // choose new active pointer
	        final int newPointerIndex = pIndex == 0 ? 1 : 0;
	        mEditorView.setLastTouch((int)event.getX(newPointerIndex),
	        		(int)event.getY(newPointerIndex));
	        mEditorView.setActivePointerId(event.getPointerId(newPointerIndex));
	    }    
    }
    
    
    public boolean handleLongClick() {
    	return false;
    }

	public void setTweetFlow(TweetFlow tweetFlow) {
		this.mTweetFlow = tweetFlow;
	}
    
    
	
	
	
//	final protected int scaleX(int x) {
//		return (int)((x-editorView.mPosX)/editorView.mScaleFactor);
//	}
//	
//	final protected int scaleY(int y) {
//		return (int)((y-editorView.mPosY)/editorView.mScaleFactor);
//	}
}
