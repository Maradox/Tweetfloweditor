package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;
import at.tuwien.dsgproject.tfe.views.EditorView.SnapMode;

public class StateFree extends State {
		
	public StateFree(EditorView editorView) {
		super(editorView);
	}
	
	public void onActionDown(MotionEvent event) {
		
		final int x = (int) event.getX();
		final int y = (int) event.getY();
			
		if (editorView.elementAt(x, y)) {
			editorView.mTouchElement.modeMarked();
			editorView.setState(EDITOR_STATE.TOUCH_ELEMENT);	
			editorView.redraw();
		} else if( editorView.snapMode == SnapMode.GRID && 
				editorView.rasterOn && 
				editorView.isTouchOnGrid(x) ) {
			final int xGrid = editorView.getTouchOnGrid(x);
			editorView.selectElementsOnGrid(xGrid);
			editorView.setState(EDITOR_STATE.MOVE_SELECTED);				//TODO
			
			editorView.redraw();
    	} else if (editorView.openSequenceAt(x, y)) {
    		editorView.setState(EDITOR_STATE.SELECTED_OPEN_SEQUENCE);
    		editorView.mTouchElement.modeSelected();
    	} else {
			editorView.setState(EDITOR_STATE.TOUCH_VOID);
    	}
		
		editorView.mOldX = x;
		editorView.mOldY = y;
		
		editorView.mActivePointerId = event.getPointerId(0); 			
    } 
    	
	
	public void onActionMove(MotionEvent event) {}
	public void onActionUp(MotionEvent event) {}
	
}
