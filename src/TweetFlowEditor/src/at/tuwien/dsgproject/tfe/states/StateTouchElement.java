package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import android.widget.Toast;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;

public class StateTouchElement extends State {
	
	public StateTouchElement(EditorView editorView, TweetFlow tweetFlow) {
		super(editorView, tweetFlow);
	}
	
	public void onActionDown(MotionEvent event) {} 
	
	public void onActionMove(MotionEvent event) {	
		final int pointerIndex = event.findPointerIndex(mEditorView.getActivePointerId());
        final int x = (int)event.getX(pointerIndex);
        final int y = (int)event.getY(pointerIndex);
		final int offX = x - mEditorView.getLastTouchX();
		final int offY = y - mEditorView.getLastTouchY();
	
		if(Math.sqrt(offX*offX + offY*offY) > mEditorView.MOVE_OFFSET) {
			if(mTweetFlow.isSelected(mEditorView.getTouchElementId())) {
				mTweetFlow.moveSelected(offX, offY);
				mEditorView.setState(EDITOR_STATE.MOVE_SELECTED);
			} else {
				mEditorView.moveTouchElement(offX, offY);
				mEditorView.setState(EDITOR_STATE.MOVE_ELEMENT);
			}
			mEditorView.redraw();
		}				
		mEditorView.setLastTouch(x, y);
	}	
	
	public void onActionUp(MotionEvent event) {
		final Integer id = mEditorView.getTouchElementId();
		if(!mTweetFlow.isSelected(id)) { // add to selected elements, on first click
			mTweetFlow.selectElementById(id);
		} else { // deselect item on 2nd click
			mTweetFlow.deselectElementById(id);
		}
		
		if(!mTweetFlow.somethingSelected()) {
			mEditorView.setState(EDITOR_STATE.FREE);
		} else {
			mEditorView.setState(EDITOR_STATE.SELECTED);
		}
		
		mEditorView.redraw();
	}

	@Override
	public boolean handleLongClick() {
		mEditorView.openContextMenu();
		if(!mTweetFlow.somethingSelected()) {
			mEditorView.setState(EDITOR_STATE.FREE);
		} else {
			mEditorView.setState(EDITOR_STATE.SELECTED);
		}
		mEditorView.getTouchElement().modeNormal();
		return true;
	}
	
	
	
	
	

}
