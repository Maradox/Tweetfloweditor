package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.views.EditorView;

public class StateTouchVoid extends State {
		
	public StateTouchVoid(EditorView editorView) {
		super(editorView);
	}
	
	public void onActionDown(MotionEvent event) {} 
	
	public void onActionMove(MotionEvent event) {	
		final int pointerIndex = event.findPointerIndex(editorView.mActivePointerId);
        final int x = (int)event.getX(pointerIndex);
        final int y = (int)event.getY(pointerIndex);
		final int offX = x - editorView.mOldX;
		final int offY = y - editorView.mOldY;
		
		if(Math.sqrt(offX*offX + offY*offY) > editorView.mMoveOffset) {
			editorView.mPosX += offX;
			editorView.mPosY += offY;
			
			editorView.state = editorView.stateMoveAll;
			
			editorView.redraw();
		}
		
		editorView.mOldX = x;
		editorView.mOldY = y;	
	}	
	
	public void onActionUp(MotionEvent event) {
		if(editorView.mSelected.isEmpty()) {
			editorView.state = editorView.stateFree;
		}
		else {
			editorView.state = editorView.stateSelected;
		}
	}
}
