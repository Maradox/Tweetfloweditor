package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;

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
			
			editorView.setState(EDITOR_STATE.MOVE_ALL);
			
			editorView.redraw();
		}
		
		editorView.mOldX = x;
		editorView.mOldY = y;	
	}	
	
	public void onActionUp(MotionEvent event) {
		if(editorView.mSelected.isEmpty()) {
			editorView.setState(EDITOR_STATE.FREE);
		}
		else {
			editorView.setState(EDITOR_STATE.SELECTED);
		}
	}

	@Override
	public boolean handleLongClick() {
		editorView.addRectangle(editorView.mOldX, editorView.mOldY);
		return true;
	}
	
	
}
