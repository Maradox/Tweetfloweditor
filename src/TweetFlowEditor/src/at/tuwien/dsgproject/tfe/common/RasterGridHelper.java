package at.tuwien.dsgproject.tfe.common;

import java.util.ArrayList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import at.tuwien.dsgproject.tfe.entities.AbstractElement;
import at.tuwien.dsgproject.tfe.entities.ServiceRequest;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;

public class RasterGridHelper {
	
	public static final int RASTER_HORIZONTAL_WIDTH = 70;
	
	private Boolean rasterOn = true;
	private int horizontalRasterCT;
	public Integer mOffsetX, mOffsetY;	
	private TweetFlow tweetFlow;
	public SnapMode snapMode = SnapMode.GRID;
				
	public enum SnapMode {	
		NOTHING,
		RASTER,
		GRID
	}
	
	public RasterGridHelper(TweetFlow tweetFlow, Integer mOffsetX, Integer mOffsetY) {
		this.mOffsetX = mOffsetX;
		this.mOffsetY = mOffsetY;
		this.tweetFlow = tweetFlow;
	}
	
	public void draw(Canvas canvas) {
		if(rasterOn) {		
			horizontalRasterCT = (canvas.getWidth() / RASTER_HORIZONTAL_WIDTH) + 3;
			
			Paint paint = new Paint();
			paint.setPathEffect( new DashPathEffect(new float[] { 10, 3, 6, 3 },1) );
						
			if((snapMode == SnapMode.NOTHING) || (snapMode == SnapMode.RASTER)) {
				paint.setColor(Color.BLUE);
			
				ArrayList<Integer> gridLines = createRasterLines();
	    	
		    	for(int i=0; i<gridLines.size(); i++) {
		    		canvas.drawLine(gridLines.get(i), 0-mOffsetY, gridLines.get(i), canvas.getHeight()-mOffsetY, paint);
		    	}
			}	

			if(snapMode == SnapMode.GRID) {
				paint.setColor(Color.RED);
		
				for(AbstractElement e : tweetFlow.getmElements().values()) {
					if(e instanceof ServiceRequest) {
						canvas.drawLine(e.getMiddleX(), 0-mOffsetY, e.getMiddleX(), canvas.getHeight()-mOffsetY, paint);
					}					
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
		
		if(xDiff < 15) 
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

	public SnapMode getSnapMode() {
		return snapMode;
	}

	public void setSnapMode(SnapMode snapMode) {
		this.snapMode = snapMode;
	}
	
	public void setOffset(Integer mOffsetX, Integer mOffsetY) {
		this.mOffsetX = mOffsetX;
		this.mOffsetY = mOffsetY;
	}
	
	
	

	
	
}
