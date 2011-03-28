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
		super(id, x, y, width, height);
//		mShape = new ShapeDrawable(new RectShape());
//		mShape.getPaint().setAntiAlias(true);
//		mShape.setBounds(x, y, x+height, y+width);
//		mShape.getPaint().setStyle(Paint.Style.STROKE);
//		mShape.getPaint().setColor(context.getResources().getColor(R.color.blue_light));
//		mShape.getPaint().setStrokeWidth(3);
//		mShape.getPaint().setPathEffect( new DashPathEffect(new float[] {10,3}, 0) );
		mShape = context.getResources().getDrawable(R.drawable.shape_open_sequence);
		mShape.setBounds(x, y, x+height, y+width);

	}

	@Override
	public void draw(Canvas canvas) {
		mShape.draw(canvas);
	}

	@Override
	public boolean contains(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

}
