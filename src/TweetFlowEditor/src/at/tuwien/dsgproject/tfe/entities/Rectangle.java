package at.tuwien.dsgproject.tfe.entities;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;


public class Rectangle extends AbstractElement {
	

	public Rectangle(int id, int x, int y, int width, int height) {
		super(id, x,y,width,height);
		mShape = new ShapeDrawable(new RectShape());
		mShape.getPaint().setAntiAlias(true);
		mShape.setBounds(x, y, x+height, y+width);
	}

	@Override
	public void draw(Canvas canvas) {
		ShapeDrawable drawShape = new ShapeDrawable();
		Rect border = new Rect(mShape.getBounds());
		Rect fill = new Rect(border);
		
		if(mHighlighted) {
			fill.inset(2, 2);
			border.inset(-1,-1);
			mShape.getPaint().setColor(borderColor);
		} else {
			fill.inset(1, 1);
			mShape.getPaint().setColor(borderColor);
		}
		drawShape.setBounds(border);
		drawShape.draw(canvas);
		
		drawShape.setBounds(fill);
		drawShape.getPaint().setColor(fillColor);
		drawShape.draw(canvas);
		
	}	
		
		
	@Override
	public void scale(float scaleFactor) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean contains(int x, int y) {
		return mShape.getBounds().contains(x, y);
	}

}
