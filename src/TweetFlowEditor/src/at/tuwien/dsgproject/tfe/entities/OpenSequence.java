
/* 
 *  Tweetfloweditor - a graphical editor to create Tweetflows
 *  
 *  Copyright (C) 2011  Matthias Neumayr
 *  Copyright (C) 2011  Martin Perebner
 *  
 *  Tweetfloweditor is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  Tweetfloweditor is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with Tweetfloweditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.tuwien.dsgproject.tfe.entities;

import org.simpleframework.xml.Attribute;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.dialogs.ChangeDataDialogOpenSequence;
import at.tuwien.dsgproject.tfe.quickAction.ActionItem;
import at.tuwien.dsgproject.tfe.quickAction.QuickAction;
import at.tuwien.dsgproject.tfe.views.EditorView;

public class OpenSequence extends AbstractElement {
		
	private final int EDGE_TOUCH_RADIUS = 20;
	private final int CORNER_TOUCH_RADIUS = 30;
	private final int MIN_WIDTH = 100;
	private final int MIN_HEIGHT = 100;
	
	private Paint mTextPaint;	
	
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
	

	public OpenSequence(@Attribute(name="mId") Integer id, 
			@Attribute(name="mX") int x, 
			@Attribute(name="mY") int y, 
			@Attribute(name="mWidth") int width, 
			@Attribute(name="mHeight") int height) {
		super(id, x, y, width, height);
	}
	
	@Override
	public void setContextAndDrawables(Context context) {
		super.setContextAndDrawables(context);
		mShape = mContext.getResources().getDrawable(R.drawable.shape_open_sequence);
		mShape.setBounds(mBounds);
		final Resources res = context.getResources();
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
		
		if(getCondition().length() > 0) {
			canvas.drawText("Cond.: "+getCondition(), getRightX()+10, mY+20, mTextPaint);
		}
	}

	@Override
	public void drawSelfLoop(Canvas canvas) {
		if(mSelfLoopImage != null)
			canvas.drawBitmap(mSelfLoopImage, mX-20, mY-20, null);  
	}
	
	public boolean isFocused(int x, int y) {
		if(Math.abs(x - mX) < CORNER_TOUCH_RADIUS && 
				Math.abs(y - mY) < CORNER_TOUCH_RADIUS) {
			mTouchFocus = TouchFocus.TOP_LEFT;
			return true;
		} else if(Math.abs(x - mWidth - mX) < CORNER_TOUCH_RADIUS && 
				Math.abs(y - mY) < CORNER_TOUCH_RADIUS) {
			mTouchFocus = TouchFocus.TOP_RIGHT;
			return true;
		}  else if(Math.abs(x - mX) < CORNER_TOUCH_RADIUS && 
				Math.abs(y - mHeight - mY) < CORNER_TOUCH_RADIUS) {
			mTouchFocus = TouchFocus.BOTTOM_LEFT;
			return true;
		}  else if(Math.abs(x - mWidth - mX) < CORNER_TOUCH_RADIUS && 
				Math.abs(y - mHeight - mY) < CORNER_TOUCH_RADIUS) {
			mTouchFocus = TouchFocus.BOTTOM_RIGHT;
			return true;
		} else if(Math.abs(x - mX) < EDGE_TOUCH_RADIUS && 
				mY < y && mY + mHeight > y) {
			mTouchFocus = TouchFocus.LEFT;
			return true;
		} else if (Math.abs(x - mWidth - mX) < EDGE_TOUCH_RADIUS && 
				mY < y && mY + mHeight > y) {
			mTouchFocus = TouchFocus.RIGHT;
			return true;
		} else if (Math.abs(y - mY) < EDGE_TOUCH_RADIUS && 
				mX < x && mX + mWidth > x) {
			mTouchFocus = TouchFocus.TOP;
			return true;
		} else if (Math.abs(y - mHeight - mY) < EDGE_TOUCH_RADIUS && 
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
		if(mSelected) {
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
		final int newHeight = (mHeight - yOff);
		if(newHeight > MIN_HEIGHT) {
			mY += yOff;
			mHeight = newHeight;
		}
	}
	
	private void growBottom(int yOff) {
		final int newHeight = (mHeight + yOff);
		if(newHeight > MIN_HEIGHT) {
			mHeight = newHeight;
		}
	}
	
	private void growLeft(int xOff) {
		final int newWidth = (mWidth - xOff);
		if(newWidth > MIN_WIDTH) {
			mX += xOff;
			mWidth = newWidth;
		}
	}
	
	private void growRight(int xOff) {
		final int newWidth = (mWidth + xOff);
		if(newWidth > MIN_WIDTH) {
			mWidth = newWidth;
		}
	}
	
	private void updateBounds() {
		mBounds.set(mX, mY, mX+mWidth, mY+mHeight);
		mShape.setBounds(mBounds);
	}

	public void modeSelected() {
		mSelected = true;
		mShape = mContext.getResources().getDrawable(R.drawable.shape_open_sequence_selected);
		mShape.setBounds(mBounds);	
	}
	
	public void modeNormal() {
		mSelected = false;
		mShape = mContext.getResources().getDrawable(R.drawable.shape_open_sequence);
		mShape.setBounds(mBounds);
	}
		
	public void modeMarked() {
		mShape = mContext.getResources().getDrawable(R.drawable.shape_open_sequence_marked);
		mShape.setBounds(mBounds);
	}
	
	@Override
	void fillQuickActionMenu(final QuickAction qa, final EditorView view) {
		ActionItem changeData = new ActionItem();
		changeData.setTitle("Edit");
		changeData.setIcon(mContext.getResources().getDrawable(R.drawable.production));
		changeData.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ChangeDataDialogOpenSequence changeDataDialog = new ChangeDataDialogOpenSequence(mContext, view , OpenSequence.this);
				changeDataDialog.show();
				qa.dismiss();
			}
		});
		qa.addActionItem(changeData);
		
		fillCommonQuickactionItems(qa, view);
		
	}

}
