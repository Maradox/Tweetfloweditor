package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.common.RasterGridHelper;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;


/**
 * StateMoveElement
 * 
 * @author Matthias Neumayr
 * @author Martin Perebner
 * 
 * This state is used to move a single element
 */
public class StateMoveElement extends State {
		
	private RasterGridHelper rasterGridHelper;
	
	public StateMoveElement(EditorView editorView, TweetFlow tweetFlow) {
		super(editorView, tweetFlow);
		rasterGridHelper = editorView.getRasterGridHelper();
	}
	
	public void onActionMove(MotionEvent event) {
		final int pointerIndex = event.findPointerIndex(mEditorView.getActivePointerId());
        final int x = (int)event.getX(pointerIndex);
        final int y = (int)event.getY(pointerIndex);
        
	    if(!mEditorView.scaleDetectorActive()) {
	        final int offX = x - mEditorView.getLastTouchX();
			final int offY = y - mEditorView.getLastTouchY();
			
			float scale = mEditorView.getmScaleFactor();
			mTweetFlow.moveTouchElement((int) (offX / scale),(int) (offY / scale));
			mEditorView.redraw();
	       }
		mEditorView.setLastTouch(x, y);
		
	}	
	
	public void onActionUp(MotionEvent event) {
		super.onActionUp(event);
		
		if(mEditorView.getSnapMode() == RasterGridHelper.SNAP_RASTER) {
			int rasterX = rasterGridHelper.findRasterHorizontal(mTweetFlow.getTouchElement().getMiddleX());
			final int offX = rasterX - mTweetFlow.getTouchElement().getMiddleX();
			mTweetFlow.moveTouchElement(offX, 0);
		}	
		
		else if(mEditorView.getSnapMode() == RasterGridHelper.SNAP_GRID && 
				rasterGridHelper.isThereGridHorizontal()) {
			int gridX = rasterGridHelper.findGridHorizontal();
			final int offX = gridX - mTweetFlow.getTouchElement().getMiddleX();
			mTweetFlow.moveTouchElement(offX, 0);
		}
		
		mTweetFlow.convertMaybeIntoFixConnection();
		
		mTweetFlow.setTouchElementModeNormal();
		if(!mTweetFlow.somethingSelected()) {
			mEditorView.setState(EDITOR_STATE.FREE);
		} else {
			mEditorView.setState(EDITOR_STATE.SELECTED);
		}
		mEditorView.redraw();
	}
	
	

}
