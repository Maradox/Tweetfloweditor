package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;

public class StateTouchElement extends State {
	
	public StateTouchElement(EditorView editorView, TweetFlow tweetFlow) {
		super(editorView, tweetFlow);
	}
	
	public void onActionMove(MotionEvent event) {	
		final int pointerIndex = event.findPointerIndex(mEditorView.getActivePointerId());
        final int x = (int)event.getX(pointerIndex);
        final int y = (int)event.getY(pointerIndex);
        
        if(!mEditorView.scaleDetectorActive()) {
			final int offX = x - mEditorView.getLastTouchX();
			final int offY = y - mEditorView.getLastTouchY();
		
			if(Math.sqrt(offX*offX + offY*offY) > mEditorView.MOVE_OFFSET) {
				if(mTweetFlow.isTouchElementSelected()) {
					mTweetFlow.moveSelected(offX, offY);
					mEditorView.setState(EDITOR_STATE.MOVE_SELECTED);
				} else {
					mTweetFlow.moveTouchElement(offX, offY);
					mEditorView.setState(EDITOR_STATE.MOVE_ELEMENT);
				}
				mEditorView.redraw();
			}				
        }
		mEditorView.setLastTouch(x, y);
	}	
	
	public void onActionUp(MotionEvent event) {
		super.onActionUp(event);
		mTweetFlow.toggleTouchElementSelected();
		
		if(!mTweetFlow.somethingSelected()) {
			mEditorView.setState(EDITOR_STATE.FREE);
		} else {
			mEditorView.setState(EDITOR_STATE.SELECTED);
		}
		
		mEditorView.redraw();
	}

	@Override
	public boolean handleLongClick() {
		if(!mEditorView.scaleDetectorActive()) {
			mEditorView.openContextMenu();
			if(!mTweetFlow.somethingSelected()) {
				mEditorView.setState(EDITOR_STATE.FREE);
			} else {
				mEditorView.setState(EDITOR_STATE.SELECTED);
			}
			mTweetFlow.setTouchElementModeNormal();
		}
		return true;
	}
	
	
	
	
	

}
