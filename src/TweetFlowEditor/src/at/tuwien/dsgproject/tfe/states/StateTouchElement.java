package at.tuwien.dsgproject.tfe.states;

import android.view.MotionEvent;
import android.widget.Toast;
import at.tuwien.dsgproject.tfe.views.EditorView;
import at.tuwien.dsgproject.tfe.views.EditorView.EDITOR_STATE;

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
	
			if(editorView.mSelected.containsValue(editorView.mTouchElement)) {
				if(Math.sqrt(offX*offX + offY*offY) > editorView.mMoveOffset) {
					editorView.moveSelected(offX, offY);
					editorView.setState(EDITOR_STATE.SELECTED);
				}	
			}
			else {
				if(Math.sqrt(offX*offX + offY*offY) > editorView.mMoveOffset) {
					editorView.moveSingle(offX, offY);
					editorView.setState(EDITOR_STATE.MOVE_ELEMENT);
				}	
			}
						
			editorView.redraw();
			
		} else {
			Toast.makeText(editorView.getContext(), "!!select mode, but element == null?", Toast.LENGTH_SHORT).show();		
		}
		
		editorView.mOldX = x;
		editorView.mOldY = y;
	}	
	
	public void onActionUp(MotionEvent event) {
		final int id = editorView.mTouchElement.getId();
		if(!editorView.mSelected.containsKey(id)) { // add to selected elements, on first click
			editorView.mSelected.put(id, editorView.mTouchElement);
			editorView.mTouchElement.modeSelected();
		} else { // deselect item on 2nd click
			editorView.mSelected.remove(id);
			editorView.mTouchElement.modeNormal();
			//mTouchElement = null;
		}
		
		if(editorView.mSelected.isEmpty()) {
			editorView.setState(EDITOR_STATE.FREE);
		} else {
			editorView.setState(EDITOR_STATE.SELECTED);
		}
		
		editorView.redraw();
	}

	@Override
	public boolean handleLongClick() {
		editorView.openContextMenu();
		return true;
	}
	
	
	
	
	

}
