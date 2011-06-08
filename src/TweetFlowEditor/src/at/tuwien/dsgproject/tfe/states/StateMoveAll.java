package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;


/**
 * StateMoveAll
 * 
 * @author Matthias Neumayr
 * @author Martin Perebner
 * 
 * StateMoveAll is used to move all elements 
 */
public class StateMoveAll extends State {
		
	public StateMoveAll(EditorView editorView, TweetFlow tweetFlow) {
		super(editorView, tweetFlow);
	}
	
	public void onActionMove(MotionEvent event) {
		final int pointerIndex = event.findPointerIndex(mEditorView.getActivePointerId());
        final int x = (int)event.getX(pointerIndex);
        final int y = (int)event.getY(pointerIndex);
	    if(!mEditorView.scaleDetectorActive()) {
	        final int offX = x - mEditorView.getLastTouchX();
			final int offY = y - mEditorView.getLastTouchY();
				
			mEditorView.offset(offX, offY);
			mEditorView.redraw();
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
	
	

}
