
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
import org.simpleframework.xml.Element;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.dialogs.ChangeDataDialogServiceRequest;
import at.tuwien.dsgproject.tfe.quickAction.ActionItem;
import at.tuwien.dsgproject.tfe.quickAction.QuickAction;
import at.tuwien.dsgproject.tfe.views.EditorView;

public class ServiceRequest extends AbstractElement {
	
	//private Rect mShapeBounds;
	@Element
	private String mRequestText = "@foo foo.bar ";		//TODO delete
	
	//TODO add serialization annotation
	private String user = "";
	private String operation = "";
	private String service = "";
	private String inputdata = "";
	
	private Paint mTextPaint;
	private final int mTextOffsetX = 90;

	public ServiceRequest(@Attribute(name="mId") Integer id, 
			@Attribute(name="mX") int x, 
			@Attribute(name="mY") int y) {
		super(id, x, y, 80, 80);	
	}
	
	@Override
	public void setContextAndDrawables(Context context) {
		super.setContextAndDrawables(context);
		final Resources res = context.getResources();
		mShape = res.getDrawable(R.drawable.shape_service_request);
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
		
		int diff = 25;
		
		if(user.length() > 0) {
			canvas.drawText("@"+user, mX+mTextOffsetX, mY+diff, mTextPaint);
			diff += 20;
		}	
		String serviceOperation = service + "." + operation;
		if(serviceOperation.length() > 1) {
			canvas.drawText(serviceOperation, mX+mTextOffsetX, mY+diff, mTextPaint);
			diff += 20;
		}	
		if(inputdata.length() > 0) {
			canvas.drawText("Input: "+ inputdata, mX+mTextOffsetX, mY+diff, mTextPaint);
			diff += 20;
		}	
		if(getCondition().length() > 0) {
			canvas.drawText("Cond.: "+getCondition(), mX+mTextOffsetX, mY+diff, mTextPaint);
			diff += 20;
		}	

	}
	
	@Override
	public void drawSelfLoop(Canvas canvas) {
		if(mSelfLoopImage != null)
			canvas.drawBitmap(mSelfLoopImage, mX-15, mY, null);  
	}
	
	@Override
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

	@Override
	public void modeSelected() {
		mSelected = true;
		mShape = mContext.getResources().getDrawable(R.drawable.shape_service_request_selected);
		mShape.setBounds(mBounds);	
	}
	
	@Override
	public void modeNormal() {
		mSelected = false;
		mShape = mContext.getResources().getDrawable(R.drawable.shape_service_request);
		mShape.setBounds(mBounds);
	}
	
	@Override
	public void modeMarked() {
		mShape = mContext.getResources().getDrawable(R.drawable.shape_service_request_marked);
		mShape.setBounds(mBounds);
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getInputdata() {
		return inputdata;
	}

	public void setInputdata(String inputdata) {
		this.inputdata = inputdata;
	}

	
	@Override
	public String toString() {
		return "SR: "+ mId.toString();
	}

	@Override
	void fillQuickActionMenu(final QuickAction qa, final EditorView view) {
		ActionItem changeData = new ActionItem();
		changeData.setTitle("Edit");
		changeData.setIcon(mContext.getResources().getDrawable(R.drawable.production));
		changeData.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ChangeDataDialogServiceRequest changeDataDialog = new ChangeDataDialogServiceRequest(mContext, view , ServiceRequest.this);
				changeDataDialog.show();
				qa.dismiss();
			}
		});
		qa.addActionItem(changeData);
		
		ActionItem bigLoop = new ActionItem();
		bigLoop.setTitle("Big loop");
		bigLoop.setIcon(mContext.getResources().getDrawable(R.drawable.production));
		bigLoop.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				view.setCreateLoopState(mId);
				qa.dismiss();
			}
		});
		qa.addActionItem(bigLoop);
		
		fillCommonQuickactionItems(qa, view);
	}
	
}

