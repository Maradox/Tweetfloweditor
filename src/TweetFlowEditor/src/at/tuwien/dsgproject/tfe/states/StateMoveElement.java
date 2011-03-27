package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.SnapMode;

public class StateMoveElement extends State {
		
	public StateMoveElement(EditorView editorView) {
		super(editorView);
	}
	
	public void onActionDown(MotionEvent event) {} 
	
	public void onActionMove(MotionEvent event) {
		final int pointerIndex = event.findPointerIndex(editorView.mActivePointerId);
        final int x = (int)event.getX(pointerIndex);
        final int y = (int)event.getY(pointerIndex);
        final int offX = x - editorView.mOldX;
		final int offY = y - editorView.mOldY;
			
		if(editorView.mTouchElement != null) {
			editorView.moveSingle(offX, offY);
			editorView.redraw();		
		}
		
		editorView.mOldX = x;
		editorView.mOldY = y;
	}	
	
	public void onActionUp(MotionEvent event) {
		final int y = (int)event.getY();
		
		if(editorView.snapMode == SnapMode.RASTER) {
			int rasterX = editorView.findRasterHorizontal(editorView.mOldX);
			editorView.moveSingleOn(rasterX, y-editorView.mPosY);
		}	
		
		else if(editorView.snapMode == SnapMode.GRID) {
			int gridX = editorView.findGridHorizontal(editorView.mOldX);
			if(gridX != -111)
				editorView.moveSingleOn(gridX, y-editorView.mPosY);
		}	
		
		editorView.mTouchElement.deHighlight();
		editorView.state = editorView.stateFree;
		
		editorView.redraw();
	}
	
	

}
