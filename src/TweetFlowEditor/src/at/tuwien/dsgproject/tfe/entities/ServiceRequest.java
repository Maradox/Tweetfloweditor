package at.tuwien.dsgproject.tfe.entities;

import java.io.IOException;

import org.simpleframework.xml.Element;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import at.tuwien.dsgproject.tfe.R;

public class ServiceRequest extends AbstractElement {
	
	//private Rect mShapeBounds;
	@Element
	private String mRequestText = "@foo foo.bar ";
	private Paint mTextPaint;
	private final int mTextOffsetX = 90;

	public ServiceRequest(Context context, int id, int x, int y) {
		super(context, id, x, y, 80, 80);
		final Resources res = context.getResources();
		mShape = res.getDrawable(R.drawable.shape_service_request);
		//mShapeBounds = new Rect(x, y, x+80, y+80);
		mShape.setBounds(mBounds);
		
		setTextPaint(res);
	}
	
	private void setTextPaint(Resources res) {
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
		canvas.drawText(mRequestText + toString(), mX+mTextOffsetX, mY+25, mTextPaint);
		canvas.drawText("mbnext: "+ ((mClosedSequenceMaybeNext != null) ? mClosedSequenceMaybeNext.toString() : " null "), mX+mTextOffsetX, mY+45, mTextPaint);
		canvas.drawText("p: "+ ((mClosedSequencePrev != null) ? mClosedSequencePrev.toString() : " N ") + 
				"n : "+ ((mClosedSequenceNext != null) ? mClosedSequenceNext.toString() : " N "), mX+mTextOffsetX, mY+65, mTextPaint);		

	}
	
	@Override
	public void drawSelfLoop(Canvas canvas) {
        canvas.drawBitmap(selfLoopImage, mX-15, mY, null);  
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
		if( x > mX && x < mX + 400 && y > mY && y < mY + mHeight) {
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
	
	@Override
	public String toString() {
		return "SR: "+ mId.toString();
	}
	
	@Override
	public void writeElementToXml(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "service_request");
		writeCommonTags(serializer);
		//TODO: element text
		serializer.text("fofofofo");
		serializer.endTag("", "service_request");
	}
	
//	@Override
//	public String getElementInfoString() {
//		String s = "SR ";
//		s += generalElementInfo();
//		
//		return s;
//	}
	
}

