package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;

public class StateTouchVoid extends State {
		
	public StateTouchVoid(EditorView editorView, TweetFlow tweetFlow) {
		super(editorView, tweetFlow);
	}
	
	public void onActionMove(MotionEvent event) {
		//TODO catch index exception with strange multitouch glitches
		final int pointerIndex = event.findPointerIndex(mEditorView.getActivePointerId());
        final int x = (int)event.getX(pointerIndex);
        final int y = (int)event.getY(pointerIndex);
        
        if(!mEditorView.scaleDetectorActive()) {
    		final int offX = x - mEditorView.getLastTouchX();
    		final int offY = y - mEditorView.getLastTouchY();
    		
    		if(Math.sqrt(offX*offX + offY*offY) > EditorView.MOVE_OFFSET) {
    			mEditorView.offset(offX, offY);
    			mEditorView.setState(EDITOR_STATE.MOVE_ALL);
    			mEditorView.redraw();
    		}
        }

		mEditorView.setLastTouch(x, y);
	}	
	
	public void onActionUp(MotionEvent event) {
		super.onActionUp(event);
		if(!mTweetFlow.somethingSelected()) {
			mEditorView.setState(EDITOR_STATE.FREE);
		}
		else {
			mEditorView.setState(EDITOR_STATE.SELECTED);
		}
	}

	@Override
	public boolean handleLongClick() {
		if(!mEditorView.scaleDetectorActive()) {
			mTweetFlow.addServiceRequest(mEditorView.getLastTouchX(), 
					mEditorView.getLastTouchY());
			mEditorView.redraw();
		}
		return true;
	}
	
	
}
