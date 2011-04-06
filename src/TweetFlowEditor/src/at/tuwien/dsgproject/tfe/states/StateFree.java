package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.entities.AbstractElement;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;
import at.tuwien.dsgproject.tfe.views.EditorView.SnapMode;

public class StateFree extends State {
		
	public StateFree(EditorView editorView, TweetFlow tweetFlow) {
		super(editorView, tweetFlow);
	}
	
	public void onActionDown(MotionEvent event) {
		
		final int x = (int) event.getX();
		final int y = (int) event.getY();
			
		if (mTweetFlow.elementAt(mEditorView.scaledX(x), mEditorView.scaledY(y))) {
			mTweetFlow.setTouchElementModeMarked();
			mEditorView.setState(EDITOR_STATE.TOUCH_ELEMENT);	
			mEditorView.redraw();
//		} else if( editorView.snapMode == SnapMode.GRID && 
//				editorView.rasterOn && 
//				editorView.isTouchOnGrid(x) ) {
//			final int xGrid = editorView.getTouchOnGrid(x);
//			editorView.selectElementsOnGrid(xGrid);
//			editorView.setState(EDITOR_STATE.MOVE_SELECTED);				//TODO
//			
//			editorView.redraw();
    	} else {
			mEditorView.setState(EDITOR_STATE.TOUCH_VOID);
    	}
		
		mEditorView.setLastTouch(x, y);
		mEditorView.setActivePointerId(event.getPointerId(0)); 			
    } 
	
}
