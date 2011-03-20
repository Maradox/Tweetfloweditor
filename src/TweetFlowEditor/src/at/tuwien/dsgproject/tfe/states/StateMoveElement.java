package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.views.EditorView;

public class StateMoveElement extends State {
		
	public StateMoveElement(EditorView editorView) {
		super(editorView);
	}
	
	public void onActionDown(MotionEvent event) {} 
	
	public void onActionMove(MotionEvent event) {
		final int pointerIndex = event.findPointerIndex(editorView.mActivePointerId);
        final int x = (int)event.getX(pointerIndex);
        final int y = (int)event.getY(pointerIndex);
			
		if(editorView.mTouchElement != null) {
			editorView.moveSingleOn(x, y);
			editorView.redraw();		
		}
		
		editorView.mOldX = x;
		editorView.mOldY = y;
	}	
	
	public void onActionUp(MotionEvent event) {
		editorView.mTouchElement.deHighlight();
		editorView.state = editorView.stateFree;
		
		editorView.redraw();
	}
	
	

}
