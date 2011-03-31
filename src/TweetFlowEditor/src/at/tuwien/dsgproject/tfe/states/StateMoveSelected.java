package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;

public class StateMoveSelected extends State {
		
	public StateMoveSelected(EditorView editorView) {
		super(editorView);
	}
	
	public void onActionDown(MotionEvent event) {} 
	
	public void onActionMove(MotionEvent event) {
		final int pointerIndex = event.findPointerIndex(editorView.mActivePointerId);
        final int x = (int)event.getX(pointerIndex);
        final int y = (int)event.getY(pointerIndex);
        final int offX = x - editorView.mOldX;
		final int offY = y - editorView.mOldY;
			
		editorView.moveSelected(offX, offY);
		editorView.redraw();
		
		editorView.mOldX = x;
		editorView.mOldY = y;				
	}	
	
	public void onActionUp(MotionEvent event) {
		editorView.setState(EDITOR_STATE.SELECTED);
	}
	
	

}
