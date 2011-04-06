package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;

public class StateMoveSelected extends State {
		
	public StateMoveSelected(EditorView editorView, TweetFlow tweetFlow) {
		super(editorView, tweetFlow);
	}
	
	public void onActionDown(MotionEvent event) {} 
	
	public void onActionMove(MotionEvent event) {
		final int pointerIndex = event.findPointerIndex(mEditorView.getActivePointerId());
        final int x = (int)event.getX(pointerIndex);
        final int y = (int)event.getY(pointerIndex);
        final int offX = x - mEditorView.getLastTouchX();
		final int offY = y - mEditorView.getLastTouchY();
			
		mTweetFlow.moveSelected(offX, offY);
		mEditorView.setLastTouch(x, y);	
		mEditorView.redraw();
		
	}	
	
	public void onActionUp(MotionEvent event) {
		mTweetFlow.setTouchElementModeSelected();
		mEditorView.setState(EDITOR_STATE.SELECTED);
		mEditorView.redraw();
	}
	
	

}
