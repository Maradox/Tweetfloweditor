package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;

public class StateMoveAll extends State {
		
	public StateMoveAll(EditorView editorView, TweetFlow tweetFlow) {
		super(editorView, tweetFlow);
	}
	
	public void onActionDown(MotionEvent event) {} 
	
	public void onActionMove(MotionEvent event) {
		final int pointerIndex = event.findPointerIndex(mEditorView.getActivePointerId());
        final int x = (int)event.getX(pointerIndex);
        final int y = (int)event.getY(pointerIndex);
        final int offX = x - mEditorView.getLastTouchX();
		final int offY = y - mEditorView.getLastTouchY();
			
		mEditorView.offset(offX, offY);
		mEditorView.setLastTouch(x, y);
		mEditorView.redraw();
	}	
	
	public void onActionUp(MotionEvent event) {
		if(!mTweetFlow.somethingSelected()) {
			mEditorView.setState(EDITOR_STATE.FREE);
		}
		else {
			mEditorView.setState(EDITOR_STATE.SELECTED);
		}
	}
	
	

}
