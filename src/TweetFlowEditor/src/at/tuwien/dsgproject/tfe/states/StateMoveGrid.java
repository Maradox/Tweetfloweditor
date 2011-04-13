package at.tuwien.dsgproject.tfe.states;

import java.util.ArrayList;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.common.RasterGridHelper;
import at.tuwien.dsgproject.tfe.common.RasterGridHelper.SnapMode;
import at.tuwien.dsgproject.tfe.entities.ServiceRequest;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;

public class StateMoveGrid extends State {
		
	private RasterGridHelper rasterGridHelper;
	private ArrayList<ServiceRequest> gridElements;
	private boolean isRunning = false;
	
	public StateMoveGrid(EditorView editorView, TweetFlow tweetFlow) {
		super(editorView, tweetFlow);
		rasterGridHelper = editorView.getRasterGridHelper();
	}
	
	public void onActionMove(MotionEvent event) {
		final int pointerIndex = event.findPointerIndex(mEditorView.getActivePointerId());
        final int x = (int)event.getX(pointerIndex);
        final int y = (int)event.getY(pointerIndex);
        
        if(!isRunning) {
        	int xGrid = rasterGridHelper.getTouchOnGrid(mEditorView.getLastTouchX());
    		gridElements = rasterGridHelper.getElementsOnGrid(xGrid);
    		isRunning = true;
        }
        
        if(!mEditorView.scaleDetectorActive()) {
	        final int offX = x - mEditorView.getLastTouchX();
			final int offY = y - mEditorView.getLastTouchY();
				
			mTweetFlow.moveGridElements(gridElements,offX, offY);
			mEditorView.redraw();
        }
        
		mEditorView.setLastTouch(x, y);	
	}	
	
	public void onActionUp(MotionEvent event) {
		super.onActionUp(event);
		
		if(mEditorView.getSnapMode() == SnapMode.GRID && rasterGridHelper.isThereGridNearGridHorizontal(gridElements)) {
			int gridX = rasterGridHelper.findGridNearGridHorizontal(gridElements);
			final int offX = gridX - gridElements.get(0).getMiddleX();
			mTweetFlow.moveGridElements(gridElements,offX, 0);
		}
		isRunning = false;
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
