package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.common.RasterGridHelper;
import at.tuwien.dsgproject.tfe.common.RasterGridHelper.SnapMode;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;

public class StateCreateLoop extends State {
		
	private RasterGridHelper rasterGridHelper;
	private Integer startID;
	private Integer endID;
	
	public StateCreateLoop(EditorView editorView, TweetFlow tweetFlow) {
		super(editorView, tweetFlow);
		rasterGridHelper = editorView.getRasterGridHelper();
		startID = null;
		endID = null;
	}
	
	public void onActionDown(MotionEvent event) {
		super.onActionDown(event);
		final int x = (int) event.getX();
		final int y = (int) event.getY();
			
		if (mTweetFlow.elementAt(mEditorView.scaledX(x), mEditorView.scaledY(y))) {			
			if(startID == null) {
				startID = mTweetFlow.getTouchElement().getId();
				mTweetFlow.getTouchElement().modeMarked();
			} else {
				endID = mTweetFlow.getTouchElement().getId();
				mTweetFlow.getmElements().get(startID).modeNormal();
				mTweetFlow.getmElements().get(startID).setmLoop(mTweetFlow.getTouchElement());
				
				if(!mTweetFlow.somethingSelected()) {
					mEditorView.setState(EDITOR_STATE.FREE);
				} else {
					mEditorView.setState(EDITOR_STATE.SELECTED);
				}
				
				startID = null;
				endID = null;
			}
			
			mEditorView.redraw();			
		} else if (rasterGridHelper.getRasterOn() && rasterGridHelper.getSnapMode() == SnapMode.GRID && rasterGridHelper.isTouchOnGrid(x))	{
			mEditorView.setState(EDITOR_STATE.MOVE_GRID);	
			mEditorView.redraw();
			startID = null;
			endID = null;
    	} else {
			mEditorView.setState(EDITOR_STATE.TOUCH_VOID);
			startID = null;
			endID = null;
    	}
		
		mEditorView.setLastTouch(x, y);		
    }

	public void setStartID(Integer startID) {
		this.startID = startID;
	} 	
	
	
}
