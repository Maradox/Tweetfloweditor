package at.tuwien.dsgproject.tfe.entities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import at.tuwien.dsgproject.tfe.R;

public class ServiceRequest extends AbstractElement {
	
	private Rect mShapeBounds;
	private String mRequestText = "@foo foo.bar foobar";
	private Paint mTextPaint;

	public ServiceRequest(Context context, int id, int x, int y) {
		super(context, id, x, y, 150, 80);
		mShape = context.getResources().getDrawable(R.drawable.shape_service_request);
		mShapeBounds = new Rect(x, y+15, x+50, y+65);
		mShape.setBounds(mShapeBounds);
		
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(14);
		mTextPaint.setColor(Color.BLACK);
		mTextPaint.setSubpixelText(true);
		mTextPaint.setTypeface(Typeface.DEFAULT);
	}

	@Override
	public void draw(Canvas canvas) {
		mShape.draw(canvas);
		canvas.drawText(mRequestText, mX+60, mY+10, mTextPaint);

	}
	
	public void move(int xOff, int yOff) {
		mX += xOff;
		mY += yOff;
		mBounds.offset(xOff, yOff);
		mShapeBounds.offset(xOff, yOff);
		mShape.setBounds(mShapeBounds);
	}

	@Override
	public boolean isFocused(int x, int y) {
		return mShape.getBounds().contains(x, y);
	}

	public void modeSelected() {
		mSelected = true;
		mShape = mContext.getResources().getDrawable(R.drawable.shape_service_request_selected);
		mShape.setBounds(mShapeBounds);	
	}
	
	public void modeNormal() {
		mSelected = false;
		mShape = mContext.getResources().getDrawable(R.drawable.shape_service_request);
		mShape.setBounds(mShapeBounds);
	}
		
	public void modeMarked() {
		mShape = mContext.getResources().getDrawable(R.drawable.shape_service_request);
		mShape.setBounds(mShapeBounds);
	}

}
