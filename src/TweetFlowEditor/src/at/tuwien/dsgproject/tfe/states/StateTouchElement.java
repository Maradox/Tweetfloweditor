package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import android.widget.Toast;
import at.tuwien.dsgproject.tfe.views.EditorView;

public class StateTouchElement extends State {
		
	public StateTouchElement(EditorView editorView) {
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
			if(Math.sqrt(offX*offX + offY*offY) > editorView.mMoveOffset) {
				editorView.moveSingleOn(x, y);
				editorView.state = editorView.stateMoveElement;
			}	
						
			editorView.redraw();
			
		} else {
			Toast.makeText(editorView.getContext(), "!!select mode, but element == null?", Toast.LENGTH_SHORT).show();		
		}
		
		editorView.mOldX = x;
		editorView.mOldY = y;
	}	
	
	public void onActionUp(MotionEvent event) {
		int id = editorView.mTouchElement.getId();
		if(!editorView.mSelected.containsKey(id)) { // add to selected elements, on first click
			editorView.mSelected.put(id, editorView.mTouchElement);
		} else { // deselect item on 2nd click
			editorView.mSelected.remove(id);
			editorView.mTouchElement.deHighlight();
			//mTouchElement = null;
			editorView.redraw();
		}
		
		if(editorView.mSelected.isEmpty()) {
			editorView.state = editorView.stateFree;
		}
		else {
			editorView.state = editorView.stateSelected;
		}
	}
	
	

}
