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
        final int offX = x - mEditorView.getLastTouchX();
		final int offY = y - mEditorView.getLastTouchY();
		
    	final int xGrid = rasterGridHelper.getTouchOnGrid(mEditorView.scaledX(x));
		gridElements = rasterGridHelper.getElementsOnGrid(xGrid);
		
        if(!isRunning && Math.sqrt(offX*offX + offY*offY) > EditorView.MOVE_OFFSET) {
    		isRunning = true;
        }
        
        if(isRunning && !mEditorView.scaleDetectorActive()) {
			final float scale = mEditorView.getmScaleFactor();		
			mTweetFlow.moveGridElements(gridElements,(int) (offX / scale),(int) (offY / scale));
			mEditorView.redraw();
        }
        
		mEditorView.setLastTouch(x, y);	
	}	
	
	public void onActionUp(MotionEvent event) {
		super.onActionUp(event);
		
		if(mEditorView.getSnapMode() == SnapMode.GRID && 
				rasterGridHelper.isThereGridNearGridHorizontal(gridElements)) {
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
	
	@Override
	public boolean handleLongClick() {
		if(!mEditorView.scaleDetectorActive()) {
			mTweetFlow.addServiceRequest(mEditorView.scaledLastX(), 
					mEditorView.scaledLastY());
			mEditorView.redraw();
		}
		return true;
	}
	
	

}
