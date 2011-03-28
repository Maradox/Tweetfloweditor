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
    	 
    	case MotionEvent.ACTION_CANCEL:
	    	editorView.state = editorView.stateFree;
	    	editorView.mActivePointerId = editorView.INVALID_POINTER_ID;
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
	
    protected void onActionPointerUp(int action, MotionEvent event) {
        // get index of the pointer that left the screen
		final int pIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) 
        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
	    final int pId = event.getPointerId(pIndex);
	    if (pId == editorView.mActivePointerId) {
	        // choose new active pointer
	        final int newPointerIndex = pIndex == 0 ? 1 : 0;
	        editorView.mOldX = (int)event.getX(newPointerIndex);
	        editorView.mOldY = (int)event.getY(newPointerIndex);
	        editorView.mActivePointerId = event.getPointerId(newPointerIndex);
	    }    
    }
	
	
	
	protected int scaleX(int x) {
		return (int)((x-editorView.mPosX)/editorView.mScaleFactor);
	}
	
	protected int scaleY(int y) {
		return (int)((y-editorView.mPosY)/editorView.mScaleFactor);
	}
}
