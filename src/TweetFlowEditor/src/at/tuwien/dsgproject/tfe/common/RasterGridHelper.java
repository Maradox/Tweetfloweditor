
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

package at.tuwien.dsgproject.tfe.common;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import at.tuwien.dsgproject.tfe.R;
import at.tuwien.dsgproject.tfe.entities.AbstractElement;
import at.tuwien.dsgproject.tfe.entities.ServiceRequest;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;
import at.tuwien.dsgproject.tfe.views.EditorView;


/**
 * RasterGridHelper
 * 
 * @author Matthias Neumayr
 * @author Martin Perebner
 * 
 * RasterGridHelper handles the drawing of the rasterlines and
 * the snapping of service-request elements to this lines
 */
public class RasterGridHelper {
	
	public final int RASTER_HORIZONTAL_WIDTH;
	
	private boolean rasterOn = true;
	private int horizontalRasterCT;
	private Integer mOffsetX, mOffsetY;	
	private TweetFlow tweetFlow;
	private int snapMode;
	private EditorView editorView;
				
	public static final int SNAP_OFF = 0;
	public static final int SNAP_RASTER = 1;
	public static final int SNAP_GRID = 2;
	
	public RasterGridHelper(Context context, TweetFlow tweetFlow, Integer mOffsetX, Integer mOffsetY, EditorView editorView) {
		this.mOffsetX = mOffsetX;
		this.mOffsetY = mOffsetY;
		this.tweetFlow = tweetFlow;
		RASTER_HORIZONTAL_WIDTH = context.getResources().getInteger(R.integer.raster_horiz_width);
		this.editorView = editorView;
		
		SharedPreferences settings = context.getSharedPreferences("TFE", Context.MODE_PRIVATE);
	    snapMode = settings.getInt("snapmode", 0);
	    rasterOn = settings.getBoolean("raster", false);
	}
	
	public void draw(Canvas canvas) {
		if(rasterOn) {		
			horizontalRasterCT = (canvas.getWidth() / RASTER_HORIZONTAL_WIDTH) + 3;
			
			Paint paint = new Paint();
			paint.setPathEffect( new DashPathEffect(new float[] { 10, 3, 6, 3 },1) );
			float scale = editorView.getmScaleFactor();
						
			if((snapMode == SNAP_OFF) || (snapMode == SNAP_RASTER)) {
				paint.setColor(Color.BLUE);
			
				ArrayList<Integer> gridLines = createRasterLines();
	    	
		    	for(int i=0; i<gridLines.size(); i++) {
		    		canvas.drawLine(gridLines.get(i),(int) ((0-mOffsetY)/scale), gridLines.get(i),(int) ((canvas.getHeight()-mOffsetY)/scale), paint);
		    	}
			}	

			if(snapMode == SNAP_GRID) {
				paint.setColor(Color.RED);
		
				for(AbstractElement e : tweetFlow.getmElements().values()) {
					if(e instanceof ServiceRequest) {
						canvas.drawLine(e.getMiddleX(),(int) ((0-mOffsetY)/scale), e.getMiddleX(),(int) ((canvas.getHeight()-mOffsetY)/scale), paint);
					}					
				}
			}	
		}	
	}
	
	public ArrayList<Integer> createRasterLines() {
		float scale = editorView.getmScaleFactor();
		ArrayList<Integer> gridLines = new ArrayList<Integer>();
		
		for(int i=0; i<horizontalRasterCT/scale; i++) {
			gridLines.add((Integer)(i*RASTER_HORIZONTAL_WIDTH - RASTER_HORIZONTAL_WIDTH/2 + mOffsetX % RASTER_HORIZONTAL_WIDTH - mOffsetX));
		}
		
		return gridLines;
	}	
	
	 
	public boolean isThereGridHorizontal() {
		int x = tweetFlow.getTouchElement().getMiddleX();
		int xDiff = Integer.MAX_VALUE;
		
		for(AbstractElement e : tweetFlow.getmElements().values()) {
			if(e instanceof ServiceRequest) {
				if(tweetFlow.getTouchElement().getId() != e.getId()) {
					if((Math.abs(x - e.getMiddleX())) < xDiff) {
						xDiff = Math.abs(x - e.getMiddleX());
					}
				}
			}					
		}
		
		if(xDiff < 30) 
			return true;
		
		return false;
	}
	
	public int findGridHorizontal() {
		int x = tweetFlow.getTouchElement().getMiddleX();
		int xDiff = Integer.MAX_VALUE;
		int xNew = 0;
		    	
		for(AbstractElement e : tweetFlow.getmElements().values()) {
			if(e instanceof ServiceRequest) {
				if(tweetFlow.getTouchElement().getId() != e.getId()) {
					if((Math.abs(x - e.getMiddleX())) < xDiff) {
						xDiff = Math.abs(x - e.getMiddleX());
						xNew = e.getMiddleX();		
					}
				}
			}					
		}
		
		return xNew;
	}
	
	public boolean isThereGridNearGridHorizontal(ArrayList<ServiceRequest> gridElements) {
		if(gridElements == null) return false;
		
		if(gridElements.isEmpty()) return false;
		
		int x = gridElements.get(0).getMiddleX();
		int xDiff = Integer.MAX_VALUE;
		
		for(AbstractElement e : tweetFlow.getmElements().values()) {
			if(e instanceof ServiceRequest) {
				if((!gridElements.contains(e)) && ((Math.abs(x - e.getMiddleX())) < xDiff)) {
					xDiff = Math.abs(x - e.getMiddleX());
				}
			}					
		}
		
		if(xDiff < 15) 
			return true;
		
		return false;
	}


	public int findGridNearGridHorizontal(ArrayList<ServiceRequest> gridElements) {		
		int x = gridElements.get(0).getMiddleX();
		int xDiff = Integer.MAX_VALUE;
		int xNew = 0;
		    	
		for(AbstractElement e : tweetFlow.getmElements().values()) {
			if(e instanceof ServiceRequest) {
				if((!gridElements.contains(e)) && ((Math.abs(x - e.getMiddleX())) < xDiff)) {
					xDiff = Math.abs(x - e.getMiddleX());
					xNew = e.getMiddleX();		
				}
			}					
		}
		
		return xNew;
	}
	
	public boolean isThereGridNearElementHorizontal(AbstractElement element) {
		int x = element.getMiddleX();
		int xDiff = Integer.MAX_VALUE;
		
		for(AbstractElement e : tweetFlow.getmElements().values()) {
			if(e instanceof ServiceRequest) {
				if((element.getId() != e.getId()) && ((Math.abs(x - e.getMiddleX())) < xDiff)) {
					xDiff = Math.abs(x - e.getMiddleX());
				}
			}					
		}
		
		if(xDiff < 15) 
			return true;
		
		return false;
	}
	
	public int findGridNearElementHorizontal(AbstractElement element) {		
		int x = element.getMiddleX();
		int xDiff = Integer.MAX_VALUE;
		int xNew = 0;
		    	
		for(AbstractElement e : tweetFlow.getmElements().values()) {
			if(e instanceof ServiceRequest) {
				if((element.getId() != e.getId()) && ((Math.abs(x - e.getMiddleX())) < xDiff)) {
					xDiff = Math.abs(x - e.getMiddleX());
					xNew = e.getMiddleX();		
				}
			}					
		}
		
		return xNew;
	}
		
	public int findRasterHorizontal(int xScaled) {
    	int x = xScaled;
    	ArrayList<Integer> gridLines = createRasterLines();
    	
    	for(int i=0; i<gridLines.size()-1; i++) {
    		if(x>gridLines.get(i) && x<gridLines.get(i+1)) {
    			if((x-gridLines.get(i)) < (gridLines.get(i+1) - x)) {
    				return gridLines.get(i);
    			} else {
    				return gridLines.get(i+1);
    			}
    		}
    	}
    	
    	return -111;
    }
	
	public boolean isTouchOnGrid(int xScaled) {
		int x = xScaled - mOffsetX;
		int xDiff = Integer.MAX_VALUE;
		
		for(AbstractElement e : tweetFlow.getmElements().values()) {
			if(e instanceof ServiceRequest) {
				if((Math.abs(x - e.getMiddleX())) < xDiff) {
					xDiff = Math.abs(x - e.getMiddleX());
				}
			}
		}	
		
		if(xDiff < 30) 
	    	return true;
		
		return false;
	}
	
	public int getTouchOnGrid(int xScaled) {
		int x = xScaled - mOffsetX;
		int xDiff = Integer.MAX_VALUE;
		int xNew = 0;
		
		for(AbstractElement e : tweetFlow.getmElements().values()) {
			if(e instanceof ServiceRequest) {
				if((Math.abs(x - e.getMiddleX())) < xDiff) {
					xDiff = Math.abs(x - e.getMiddleX());
					xNew = e.getMiddleX();		
				}
			}
		}	
	
	    return xNew;
	}
	
	
	public void selectElementsOnGrid(int x) {
		for(AbstractElement e : tweetFlow.getmElements().values()) {
			if(e instanceof ServiceRequest) {
				if(e.getMiddleX() == x) {
					tweetFlow.getmSelected().put(e.getId(), e);
					e.modeSelected();
				}
			}
		}
	}
	
	public ArrayList<ServiceRequest> getElementsOnGrid(int x) {
		ArrayList<ServiceRequest> gridElements = new ArrayList<ServiceRequest>();
		for(AbstractElement e : tweetFlow.getmElements().values()) {
			if(e instanceof ServiceRequest) {
				if(e.getMiddleX() == x) {
					gridElements.add((ServiceRequest) e);
				}
			}
		}
		return gridElements;
	}
	
	public void setTweetFlow(TweetFlow tweetFlow) {
		this.tweetFlow = tweetFlow;
	}

	public Boolean getRasterOn() {
		return rasterOn;
	}

	public void setRasterOn(Boolean rasterOn) {
		this.rasterOn = rasterOn;
	}

	public int getSnapMode() {
		return snapMode;
	}

	public void setSnapMode(int snapMode) {
		this.snapMode = snapMode;
	}
	
	public void setOffset(Integer mOffsetX, Integer mOffsetY) {
		this.mOffsetX = mOffsetX;
		this.mOffsetY = mOffsetY;
	}
		
}
