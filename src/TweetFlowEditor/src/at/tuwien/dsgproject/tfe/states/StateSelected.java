package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;

public class StateSelected extends State {
	
	public StateSelected(EditorView editorView, TweetFlow tweetFlow) {
		super(editorView, tweetFlow);
	}
	
	public void onActionDown(MotionEvent event) {
		final int xGrid;
		final int x = (int) event.getX();
		final int y = (int) event.getY();
    	
		if (mTweetFlow.elementAt(mEditorView.scaledX(x), mEditorView.scaledY(y))) {
//			mEditorView.setTouchElement(elem);
			mTweetFlow.setTouchElementModeMarked();
			mEditorView.setState(EDITOR_STATE.TOUCH_ELEMENT);
			mEditorView.redraw();
		}   
//		else if(editorView.snapMode == SnapMode.GRID && editorView.rasterOn && (editorView.isTouchOnGrid(x))) {
//			xGrid = editorView.getTouchOnGrid(x);
//			editorView.selectElementsOnGrid(xGrid);
//			editorView.setState(EDITOR_STATE.MOVE_SELECTED); //TODO
//			editorView.redraw();
//    	}
		else {
			mEditorView.setState(EDITOR_STATE.TOUCH_VOID);
		}
		
		mEditorView.setLastTouch(x, y);
	}
	
}
