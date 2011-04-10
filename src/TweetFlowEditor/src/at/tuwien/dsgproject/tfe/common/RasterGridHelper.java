package at.tuwien.dsgproject.tfe.common;

import java.util.ArrayList;
import java.util.HashMap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import at.tuwien.dsgproject.tfe.entities.AbstractElement;
import at.tuwien.dsgproject.tfe.entities.Rectangle;

public class RasterGridHelper {
	
	public static final int RASTER_HORIZONTAL_WIDTH = 70;
	
	private boolean rasterOn;
	private int horizontalRasterCT;
	private Integer mOffsetX, mOffsetY;
	private HashMap<Integer, AbstractElement> mElements;
	private AbstractElement mTouchElement;
	
	public SnapMode snapMode = SnapMode.NOTHING;
				
	public enum SnapMode {	
		NOTHING,
		RASTER,
		GRID
	}
	
	public RasterGridHelper(HashMap<Integer, AbstractElement> mElements, AbstractElement mTouchElement, Integer mOffsetX, Integer mOffsetY) {
		this.mOffsetX = mOffsetX;
		this.mOffsetY = mOffsetY;
		this.mElements = mElements;
		this.mTouchElement = mTouchElement;
	}
	
	public void draw(Canvas canvas) {
		if(rasterOn) {
			//if(!setRaster) {
			//	setRaster = true;
				horizontalRasterCT = (canvas.getWidth() / RASTER_HORIZONTAL_WIDTH) + 3;
			}
	
			Paint paint = new Paint();
			paint.setPathEffect( new DashPathEffect(new float[] { 10, 3, 6, 3 },1) );
						
			if((snapMode == SnapMode.NOTHING) || (snapMode == SnapMode.RASTER)) {
			paint.setColor(Color.BLUE);
		
			ArrayList<Integer> gridLines = createRasterLines();
    	
	    	for(int i=0; i<gridLines.size(); i++) {
	    		canvas.drawLine(gridLines.get(i), 0-mOffsetY, gridLines.get(i), canvas.getHeight()-mOffsetY, paint);
	    		canvas.drawText(""+gridLines.get(i), gridLines.get(i), 0, paint);		//TODO delete
	    	}
		}	

		if(snapMode == SnapMode.GRID) {
			paint.setColor(Color.RED);
	
			for(AbstractElement e : mElements.values()) {
				if(e instanceof Rectangle) {
					canvas.drawLine(e.getMiddleX(), 0-mOffsetY, e.getMiddleX(), canvas.getHeight()-mOffsetY, paint);
					canvas.drawText(""+e.getMiddleX(), e.getMiddleX(), 1, paint);		//TODO delete
				}					
			}
		}	
	}
	
	public ArrayList<Integer> createRasterLines() {
		ArrayList<Integer> gridLines = new ArrayList<Integer>();
		
		for(int i=0; i<horizontalRasterCT; i++) {
			gridLines.add((Integer)(i*RASTER_HORIZONTAL_WIDTH - RASTER_HORIZONTAL_WIDTH/2 + mOffsetX % RASTER_HORIZONTAL_WIDTH - mOffsetX));
		}
		
		return gridLines;
	}	
	
	 
	public boolean isThereGridHorizontal(int xScaled) {
		int x = xScaled - mOffsetX;
		int xDiff = Integer.MAX_VALUE;
		    	
		for(AbstractElement e : mElements.values()) {
			if(e instanceof Rectangle) {
				if(mTouchElement.getId() != e.getId()) {
					if((Math.abs(x - e.getMiddleX())) < xDiff) {
						xDiff = Math.abs(x - e.getMiddleX());
					}
				}
			}					
		}
		
		if(xDiff < 15) 
			return true;
		
		return false;
	}


	public int findGridHorizontal(int xScaled) {
		int x = xScaled - mOffsetX;
		int xDiff = Integer.MAX_VALUE;
		int xNew = 0;
		    	
		for(AbstractElement e : mElements.values()) {
			if(e instanceof Rectangle) {
				if(mTouchElement.getId() != e.getId()) {
					if((Math.abs(x - e.getMiddleX())) < xDiff) {
						xDiff = Math.abs(x - e.getMiddleX());
						xNew = e.getMiddleX();		
					}
				}
			}					
		}
		
		return xNew;
	}
	
	
	public boolean isTouchOnGrid(int xScaled) {
		int x = xScaled - mOffsetX;
		int xDiff = Integer.MAX_VALUE;
		
		for(AbstractElement e : mElements.values()) {
			if(e instanceof Rectangle) {
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
		
		for(AbstractElement e : mElements.values()) {
			if(e instanceof Rectangle) {
				if((Math.abs(x - e.getMiddleX())) < xDiff) {
					xDiff = Math.abs(x - e.getMiddleX());
					xNew = e.getMiddleX();		
				}
			}
		}	
	
	    return xNew;
	}
	/*
	public void selectElementsOnGrid(int x) {
		for(AbstractElement e : mElements.values()) {
			if(e instanceof Rectangle) {
				if(e.getMiddleX() == x) {
					mSelected.put(e.getId(), e);
					e.modeSelected();
				}
			}
		}
	}*/

	
}
