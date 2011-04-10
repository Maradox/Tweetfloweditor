package at.tuwien.dsgproject.tfe.entities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import at.tuwien.dsgproject.tfe.R;

public class ServiceRequest extends AbstractElement {
	
	//private Rect mShapeBounds;
	private String mRequestText = "@foo foo.bar foobar";
	private Paint mTextPaint;
	private final int mTextOffsetX = 90;

	public ServiceRequest(Context context, int id, int x, int y) {
		super(context, id, x, y, 80, 80);
		final Resources res = context.getResources();
		mShape = res.getDrawable(R.drawable.shape_service_request);
		//mShapeBounds = new Rect(x, y, x+80, y+80);
		mShape.setBounds(mBounds);
		
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(res.getInteger(R.integer.sr_text_size));
		mTextPaint.setColor(res.getColor(R.color.sr_text));
		mTextPaint.setSubpixelText(true);
		mTextPaint.setTypeface(Typeface.DEFAULT);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		mShape.draw(canvas);
		canvas.drawText(mRequestText, mX+mTextOffsetX, mY+25, mTextPaint);
		canvas.drawText("more Text, >:D foobarfoobar", mX+mTextOffsetX, mY+45, mTextPaint);
		canvas.drawText("3rd line. last line", mX+mTextOffsetX, mY+65, mTextPaint);

	}
	
	public void move(int xOff, int yOff) {
		mX += xOff;
		mY += yOff;
		mBounds.offset(xOff, yOff);
		//mShapeBounds.offset(xOff, yOff);
		mShape.setBounds(mBounds);
	}

	@Override
	public boolean isFocused(int x, int y) {
		//TODO when is it focused? only touch on circle, or text also?
		//FIXME ugly hack
		if( x > mX && x < mX + 400 &&
				y > mY && y < mY + mHeight) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isTextFocused(int x, int y) {
		//TODO: ugly hack for testing
		if( x > mX+mTextOffsetX && x < mX + 400 &&
				y > mY && y < mY + mHeight) {
			return true;
		} else {
			return false;
		}
	}

	public void modeSelected() {
		mSelected = true;
		mShape = mContext.getResources().getDrawable(R.drawable.shape_service_request_selected);
		mShape.setBounds(mBounds);	
	}
	
	public void modeNormal() {
		mSelected = false;
		mShape = mContext.getResources().getDrawable(R.drawable.shape_service_request);
		mShape.setBounds(mBounds);
	}
		
	public void modeMarked() {
		mShape = mContext.getResources().getDrawable(R.drawable.shape_service_request_marked);
		mShape.setBounds(mBounds);
	}

}
