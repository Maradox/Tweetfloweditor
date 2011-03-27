package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.views.EditorView;

public class StateMoveAll extends State {
		
	public StateMoveAll(EditorView editorView) {
		super(editorView);
	}
	
	public void onActionDown(MotionEvent event) {} 
	
	public void onActionMove(MotionEvent event) {
		final int pointerIndex = event.findPointerIndex(editorView.mActivePointerId);
        final int x = (int)event.getX(pointerIndex);
        final int y = (int)event.getY(pointerIndex);
        final int offX = x - editorView.mOldX;
		final int offY = y - editorView.mOldY;
			
		editorView.mPosX += offX;
		editorView.mPosY += offY;		
		
		editorView.redraw();
		
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
