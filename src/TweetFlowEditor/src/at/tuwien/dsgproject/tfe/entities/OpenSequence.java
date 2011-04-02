package at.tuwien.dsgproject.tfe.entities;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import at.tuwien.dsgproject.tfe.R;

public class OpenSequence extends AbstractElement {
	
	final private int EDGE_RADIUS = 20;
	final private int CORNER_RADIUS = 30;
	
	private enum TouchFocus 
	{	TOP, 
		TOP_LEFT, 
		TOP_RIGHT, 
		BOTTOM, 
		BOTTOM_LEFT, 
		BOTTOM_RIGHT, 
		LEFT, 
		RIGHT, 
		INVALID
	}
	
	private TouchFocus mTouchFocus = TouchFocus.INVALID;
	

	public OpenSequence(Context context, int id, int x, int y, int width, int height) {
		super(context, id, x, y, width, height);
		mShape = mContext.getResources().getDrawable(R.drawable.shape_open_sequence);
		mShape.setBounds(mBounds);
	}

	@Override
	public void draw(Canvas canvas) {
		mShape.draw(canvas);
	}

	
	public boolean isFocused(int x, int y) {
		
		//TODO make this ugly ***** go away :(
		
		if(Math.abs(x - mX) < CORNER_RADIUS && 
				Math.abs(y - mY) < CORNER_RADIUS) {
			mTouchFocus = TouchFocus.TOP_LEFT;
			return true;
		} else if(Math.abs(x - mWidth - mX) < CORNER_RADIUS && 
				Math.abs(y - mY) < CORNER_RADIUS) {
			mTouchFocus = TouchFocus.TOP_RIGHT;
			return true;
		}  else if(Math.abs(x - mX) < CORNER_RADIUS && 
				Math.abs(y - mHeight - mY) < CORNER_RADIUS) {
			mTouchFocus = TouchFocus.BOTTOM_LEFT;
			return true;
		}  else if(Math.abs(x - mWidth - mX) < CORNER_RADIUS && 
				Math.abs(y - mHeight - mY) < CORNER_RADIUS) {
			mTouchFocus = TouchFocus.BOTTOM_RIGHT;
			return true;
		} else if(Math.abs(x - mX) < EDGE_RADIUS && 
				mY < y && mY + mHeight > y) {
			mTouchFocus = TouchFocus.LEFT;
			return true;
		} else if (Math.abs(x - mWidth - mX) < EDGE_RADIUS && 
				mY < y && mY + mHeight > y) {
			mTouchFocus = TouchFocus.RIGHT;
			return true;
		} else if (Math.abs(y - mY) < EDGE_RADIUS && 
				mX < x && mX + mWidth > x) {
			mTouchFocus = TouchFocus.TOP;
			return true;
		} else if (Math.abs(y - mHeight - mY) < EDGE_RADIUS && 
				mX < x && mX + mWidth > x ) { // bottom
			mTouchFocus = TouchFocus.BOTTOM;
			return true;
		} else {
			mTouchFocus = TouchFocus.INVALID;
			return false;
		}
	}
	
	
	@Override
	public void move(int xOff, int yOff) {
		if(mHighlighted) {
			super.move(xOff, yOff);
		} else {
			switch(mTouchFocus) {
			case TOP_LEFT:
				growTop(yOff);
				growLeft(xOff);
				break;
			
			case TOP:
				growTop(yOff);
				break;
		
			case TOP_RIGHT:
				growTop(yOff);
				growRight(xOff);
				break;
				
			case LEFT:
				growLeft(xOff);
				break;
				
			case RIGHT:
				growRight(xOff);
				break;
				
			case BOTTOM_LEFT:
				growBottom(yOff);
				growLeft(xOff);
				break;
		
			case BOTTOM:
				growBottom(yOff);
				break;
				
			case BOTTOM_RIGHT:
				growBottom(yOff);
				growRight(xOff);
				break;
				
			default:
				return;
			
			}
			updateBounds();
		}
	}
	
	
	private void growTop(int yOff) {
		mY += yOff;
		mHeight -= yOff;
	}
	
	private void growBottom(int yOff) {
		mHeight += yOff;
	}
	
	private void growLeft(int xOff) {
		mX += xOff;
		mWidth -= xOff;
	}
	
	private void growRight(int xOff) {
		mWidth += xOff;
	}
	
	private void updateBounds() {
		mBounds.set(mX, mY, mX+mWidth, mY+mHeight);
		mShape.setBounds(mBounds);
	}

	public void modeSelected() {
		mHighlighted = true;
		mMarked = false;
		mShape = mContext.getResources().getDrawable(R.drawable.shape_open_sequence_selected);
		mShape.setBounds(mBounds);	
	}
	
	public void modeNormal() {
		mHighlighted = false;
		mMarked = false;
		mShape = mContext.getResources().getDrawable(R.drawable.shape_open_sequence);
		mShape.setBounds(mBounds);
	}
		
	public void modeMarked() {
		mHighlighted = false;
		mMarked = true;
		mShape = mContext.getResources().getDrawable(R.drawable.shape_open_sequence_marked);
		mShape.setBounds(mBounds);
	}

}
