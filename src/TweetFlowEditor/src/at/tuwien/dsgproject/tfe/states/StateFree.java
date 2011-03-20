package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.SnapMode;

public class StateFree extends State {
		
	public StateFree(EditorView editorView) {
		super(editorView);
	}
	
	public void onActionDown(MotionEvent event) {
		int xGrid;
		
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		
		editorView.mTouchElement = editorView.elementAt(x, y);	
    		
		if (editorView.mTouchElement != null) {
			editorView.mTouchElement.highlight();
			editorView.state = editorView.stateTouchElement;
			
			editorView.redraw();
		}   	
		else if(editorView.snapMode == SnapMode.GRID && editorView.rasterOn && ((xGrid = editorView.getTouchOnGrid(x)) != -111)) {
			editorView.selectElementsOnGrid(xGrid);
		//	mCurrMode = TouchMode.MOVE_ALL_GRID;	//TODO			
    	}
		else
	//		mCurrMode = TouchMode.TOUCH_VOID;	//TODO
		
		editorView.mOldX = x;
		editorView.mOldY = y;
		
		editorView.mActivePointerId = event.getPointerId(0); 			
    } 
    	
	
	public void onActionMove(MotionEvent event) {}
	public void onActionUp(MotionEvent event) {}
	
}
