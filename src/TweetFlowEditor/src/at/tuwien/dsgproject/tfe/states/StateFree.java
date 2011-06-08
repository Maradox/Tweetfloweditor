package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.common.RasterGridHelper;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;

public class StateFree extends State {
		
	private RasterGridHelper rasterGridHelper;
	
	public StateFree(EditorView editorView, TweetFlow tweetFlow) {
		super(editorView, tweetFlow);
		rasterGridHelper = editorView.getRasterGridHelper();
	}
	
	public void onActionDown(MotionEvent event) {
		super.onActionDown(event);
		final int x = (int) event.getX();
		final int y = (int) event.getY();
			
		if (mTweetFlow.elementAt(mEditorView.scaledX(x), mEditorView.scaledY(y))) {
			mTweetFlow.setTouchElementModeMarked();
			mEditorView.setState(EDITOR_STATE.TOUCH_ELEMENT);	
			mEditorView.redraw();			
		} else if (rasterGridHelper.getRasterOn() && 
				rasterGridHelper.getSnapMode() == RasterGridHelper.SNAP_GRID && 
				rasterGridHelper.isTouchOnGrid(mEditorView.scaledX(x)))	{
			mEditorView.setState(EDITOR_STATE.MOVE_GRID);	
			mEditorView.redraw();
    	} else {
			mEditorView.setState(EDITOR_STATE.TOUCH_VOID);
    	}
		
		mEditorView.setLastTouch(x, y);		
    } 
	
}
