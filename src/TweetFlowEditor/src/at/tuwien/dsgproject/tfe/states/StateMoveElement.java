package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;
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
		final int x = (int)event.getX();
		final int y = (int)event.getY();
		
		if(editorView.snapMode == SnapMode.RASTER) {
			int rasterX = editorView.findRasterHorizontal(editorView.mOldX);
			editorView.moveSingleOn(rasterX, y-editorView.mPosY);
		}	
		
		else if(editorView.snapMode == SnapMode.GRID && editorView.isThereGridHorizontal(x)) {
			int gridX = editorView.findGridHorizontal(x);
			editorView.moveSingleOn(gridX, y-editorView.mPosY);
		}	
		
		editorView.mTouchElement.modeNormal();
		editorView.setState(EDITOR_STATE.FREE);
		
		editorView.redraw();
	}
	
	

}
