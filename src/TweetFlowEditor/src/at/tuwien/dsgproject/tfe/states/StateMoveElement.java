package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;
import at.tuwien.dsgproject.tfe.views.EditorView.SnapMode;

public class StateMoveElement extends State {
		
	public StateMoveElement(EditorView editorView, TweetFlow tweetFlow) {
		super(editorView, tweetFlow);
	}
	
	public void onActionDown(MotionEvent event) {} 
	
	public void onActionMove(MotionEvent event) {
		final int pointerIndex = event.findPointerIndex(mEditorView.getActivePointerId());
        final int x = (int)event.getX(pointerIndex);
        final int y = (int)event.getY(pointerIndex);
        final int offX = x - mEditorView.getLastTouchX();
		final int offY = y - mEditorView.getLastTouchY();
			
		mTweetFlow.moveTouchElement(offX, offY);
		mEditorView.setLastTouch(x, y);
		mEditorView.redraw();
	}	
	
	public void onActionUp(MotionEvent event) {
		final int x = (int)event.getX();
		final int y = (int)event.getY();
		
//		if(editorView.snapMode == SnapMode.RASTER) {
//			int rasterX = editorView.findRasterHorizontal(editorView.mOldX);
//			editorView.moveSingleOn(rasterX, y-editorView.mPosY);
//		}	
		
//		else if(editorView.snapMode == SnapMode.GRID && editorView.isThereGridHorizontal(x)) {
//			int gridX = editorView.findGridHorizontal(x);
//			editorView.moveSingleOn(gridX, y-editorView.mPosY);
//		}	
		
		mTweetFlow.setTouchElementModeNormal();
		if(!mTweetFlow.somethingSelected()) {
			mEditorView.setState(EDITOR_STATE.FREE);
		} else {
			mEditorView.setState(EDITOR_STATE.SELECTED);
		}
		mEditorView.redraw();
	}
	
	

}
