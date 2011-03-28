
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

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;


public class Rectangle extends AbstractElement {
	

	public Rectangle(int id, int x, int y, int width, int height) {
		super(id, x,y,width,height);
		mShape = new ShapeDrawable(new RectShape());
		((ShapeDrawable)mShape).getPaint().setAntiAlias(true);
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
			((ShapeDrawable)mShape).getPaint().setColor(borderColor);
		} else {
			fill.inset(1, 1);
			((ShapeDrawable)mShape).getPaint().setColor(borderColor);
		}
		drawShape.setBounds(border);
		drawShape.draw(canvas);
		
		drawShape.setBounds(fill);
		drawShape.getPaint().setColor(fillColor);
		drawShape.draw(canvas);
		
	}	
		

	@Override
	public boolean contains(int x, int y) {
		return mShape.getBounds().contains(x, y);
	}

}
