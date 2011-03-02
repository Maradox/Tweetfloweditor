package at.tuwien.dsgproject.tfe.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class EditorView extends View {
		
	public EditorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setFocusable(true);
	}
				
	@Override protected void onDraw(Canvas canvas) {
		//TODO
    }
	
	public boolean onTouchEvent(MotionEvent event) {
		//TODO
		return false;
	}
  
}
