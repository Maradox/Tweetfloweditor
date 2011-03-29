package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.SnapMode;

public class StateSelected extends State {
		
	public StateSelected(EditorView editorView) {
		super(editorView);
	}
	
	public void onActionDown(MotionEvent event) {
		int xGrid;
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		
		editorView.mTouchElement = editorView.elementAt(x, y);	
    	
		if (editorView.mTouchElement != null) {
			editorView.state = editorView.stateTouchElement;
			
			editorView.redraw();
		}   
		else if(editorView.snapMode == SnapMode.GRID && editorView.rasterOn && (editorView.isTouchOnGrid(x))) {
			xGrid = editorView.getTouchOnGrid(x);
			editorView.selectElementsOnGrid(xGrid);
			editorView.state = editorView.stateMoveSelected;				//TODO
			
			editorView.redraw();
    	}
		else {
			editorView.state = editorView.stateTouchVoid;
		}
		
		editorView.mOldX = x;
		editorView.mOldY = y;
	} 
	
	public void onActionMove(MotionEvent event) {}	
	
	public void onActionUp(MotionEvent event) {}
	
	

}
