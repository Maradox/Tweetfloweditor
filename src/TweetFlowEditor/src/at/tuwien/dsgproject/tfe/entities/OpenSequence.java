package at.tuwien.dsgproject.tfe.entities;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import at.tuwien.dsgproject.tfe.R;

public class OpenSequence extends AbstractElement {
	
	//private Paint mFillPaint, mStrokePaint;

	public OpenSequence(Context context, int id, int x, int y, int width, int height) {
		super(context, id, x, y, width, height);
//		mShape = new ShapeDrawable(new RectShape());
//		mShape.getPaint().setAntiAlias(true);
//		mShape.setBounds(x, y, x+height, y+width);
//		mShape.getPaint().setStyle(Paint.Style.STROKE);
//		mShape.getPaint().setColor(context.getResources().getColor(R.color.blue_light));
//		mShape.getPaint().setStrokeWidth(3);
//		mShape.getPaint().setPathEffect( new DashPathEffect(new float[] {10,3}, 0) );
		mShape = mContext.getResources().getDrawable(R.drawable.shape_open_sequence);
		mShape.setBounds(x, y, x+height, y+width);

	}

	@Override
	public void draw(Canvas canvas) {
		mShape.draw(canvas);
	}

	@Override
	public boolean isFocused(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean isLineFocused(int x, int y) {
		final int r = 5;
		//TODO corner detection
		//TODO all in one if/else - check specific side/edge in move event
		if(Math.abs(x - mX) < r && mY - r < y && mY + mHeight + r > y) {
			//left
			return true;
		} else if (Math.abs(x - mWidth - mX) < r && mY - r < y && mY + mHeight + r > y) {
			//right
			return true;
		} else if (Math.abs(y - mY) < r && mX - r < x && mX + mWidth + r > x) {
			//top
			return true;
		} else if (Math.abs(y - mHeight - mY) < r && mX - r < x && mX + mWidth + r > x ) { // bottom
			//bottom
			return true;
		} else {
			return false;
		}
	}
	
	public void highlight() {
		mHighlighted = true;
		mShape = mContext.getResources().getDrawable(R.drawable.shape_open_sequence_selected);
		mShape.setBounds(mBounds);	
	}
	
	public void deHighlight() {
		mHighlighted = false;
		mShape = mContext.getResources().getDrawable(R.drawable.shape_open_sequence);
		mShape.setBounds(mBounds);
	}

}
