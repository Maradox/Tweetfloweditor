package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.common.RasterGridHelper;
import at.tuwien.dsgproject.tfe.common.RasterGridHelper.SnapMode;
import at.tuwien.dsgproject.tfe.entities.AbstractElement;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;

public class StateMoveSelected extends State {
		
	private RasterGridHelper rasterGridHelper;
	
	public StateMoveSelected(EditorView editorView, TweetFlow tweetFlow) {
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
				
			mTweetFlow.moveSelected(offX, offY);
			mEditorView.redraw();
        }
        
		mEditorView.setLastTouch(x, y);	
	}	
	
	public void onActionUp(MotionEvent event) {
		super.onActionUp(event);
		
		if(mEditorView.getSnapMode() == SnapMode.RASTER) {
			for(AbstractElement e : mTweetFlow.getmSelected().values()) {
				int rasterX = rasterGridHelper.findRasterHorizontal(e.getMiddleX());
				int offX = rasterX - e.getMiddleX();
				e.move(offX, 0);
				mTweetFlow.updateElementConnections(e);
			}	
		}	
		
		else if(mEditorView.getSnapMode() == SnapMode.GRID) {
			for(AbstractElement e : mTweetFlow.getmSelected().values()) {
				if(rasterGridHelper.isThereGridNearElementHorizontal(e)){
					int gridX = rasterGridHelper.findGridNearElementHorizontal(e);
					int offX = gridX - e.getMiddleX();
					e.move(offX, 0);
					mTweetFlow.updateElementConnections(e);
				}	
			}	
		}
		
		mTweetFlow.convertMaybeIntoFixConnection();
		mTweetFlow.setTouchElementModeSelected();
		mEditorView.setState(EDITOR_STATE.SELECTED);
		mEditorView.redraw();
	}
	
	

}
