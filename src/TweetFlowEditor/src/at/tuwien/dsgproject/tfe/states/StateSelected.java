package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import at.tuwien.dsgproject.tfe.views.EditorView;

public class StateSelected extends State {
		
	public StateSelected(EditorView editorView) {
		super(editorView);
	}
	
	public void onActionDown(MotionEvent event) {
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		
		editorView.mTouchElement = editorView.elementAt(x, y);	
    	
		if (editorView.mTouchElement != null) {
			editorView.mTouchElement.highlight();
			editorView.state = editorView.stateTouchElement;
			
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
