package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.views.EditorView;

public abstract class State {

	EditorView editorView;
	
	public State(EditorView editorView) {
		this.editorView = editorView;
	}
	
	public void onTouchEvent(MotionEvent event) {
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
    		
    		
    		/*
    	 
    	case MotionEvent.ACTION_CANCEL:
    		mCurrMode = TouchMode.FREE;
    		mActivePointerId = INVALID_POINTER_ID;
    		break;
    		
    	
    	 	
    	case MotionEvent.ACTION_POINTER_UP:
    		onActionPointerUp(action, event);
            break;
    	*/
    	default:
    		//nothing
    	
    	}
	}
	
	protected void onActionDown(MotionEvent event) {}
	protected void onActionMove(MotionEvent event) {}
	protected void onActionUp(MotionEvent event) {}
	
	
	
	protected int scaleX(int x) {
		return (int)((x-editorView.mPosX)/editorView.mScaleFactor);
	}
	
	protected int scaleY(int y) {
		return (int)((y-editorView.mPosY)/editorView.mScaleFactor);
	}
}
