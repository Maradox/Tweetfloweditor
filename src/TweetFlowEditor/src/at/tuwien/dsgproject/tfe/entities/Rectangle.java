
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

import android.content.Context;
import android.graphics.Canvas;
import at.tuwien.dsgproject.tfe.R;


public class Rectangle extends AbstractElement {
	

	public Rectangle(Context context, int id, int x, int y, int width, int height) {
		super(context, id, x,y,width,height);
		mShape = context.getResources().getDrawable(R.drawable.shape_rectangle);
		mShape.setBounds(mBounds);
	}

	@Override
	public void draw(Canvas canvas) {
		mShape.draw(canvas);	
	}	
	
	public void modeSelected() {
		mSelected = true;
		mShape = mContext.getResources().getDrawable(R.drawable.shape_rectangle_selected);
		mShape.setBounds(mBounds);	
	}
	
	public void modeNormal() {
		mSelected = false;
		mShape = mContext.getResources().getDrawable(R.drawable.shape_rectangle);
		mShape.setBounds(mBounds);
	}
		
	public void modeMarked() {
		mShape = mContext.getResources().getDrawable(R.drawable.shape_rectangle_marked);
		mShape.setBounds(mBounds);
	}
	

	@Override
	public boolean isFocused(int x, int y) {
		return mShape.getBounds().contains(x, y);
	}

}
