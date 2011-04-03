package at.tuwien.dsgproject.tfe.entities;

import java.util.HashMap;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;

public class TweetFlow {
	
	private Context mContext;
	
	private HashMap<Integer, AbstractElement> mElements;
	private HashMap<Integer, AbstractElement> mSelected;
	//public HashMap<Integer, OpenSequence> mOpenSequences;
	
	private Integer mElemCounter;
	
	public TweetFlow(Context context) {
		mContext = context;
		
		mElemCounter = 0;
		mElements = new HashMap<Integer, AbstractElement>();
		mSelected = new HashMap<Integer, AbstractElement>();
		//mOpenSequences = new HashMap<Integer, OpenSequence>();
		
		fillElements();
	}
	
	
	public void fillElements() {
		addRectangle(100,100);
		addRectangle(200,350);
		addRectangle(300,500);
		
		addOpenSequence();
	}
	
	
	public void addRectangle(int x, int y) {
		//ugly hack to insert rectangle centered on touch event
		//maybe x and y should be the center of AbstractElements instead of the upper left corner
		mElements.put(mElemCounter, new Rectangle(mContext, mElemCounter++, x-75, y-40, 150, 80));
	}
	
	
	public void addOpenSequence() {
		mElements.put(mElemCounter, new OpenSequence(mContext, mElemCounter++, 100, 100, 250, 400));
	}
	
	public void deleteElement(Integer id) {
		mSelected.remove(id);
		mElements.remove(id);
	}
	
	/**
	 * Checks if an element is at the given location and returns it.
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return The element at the given location or null.
	 */
	public AbstractElement elementAt(int x, int y) {
		for(AbstractElement r : mElements.values()) {
			if(r.isFocused(x,y)) {
				return r;
			}	
		}
		return null;
	}
	
	public void draw(Canvas canvas) {
		for (AbstractElement e : mElements.values()) {
			e.draw(canvas);
		}
	}
	
    public void moveSelected(int offX, int offY) {
		for(AbstractElement e : mSelected.values()) {
			e.move(offX, offY);
		}
    }
    
    public void deselectAll() {
    	for(AbstractElement e : mSelected.values()) {
    		e.modeNormal();
    	}
    	mSelected.clear();
    }
    
//    public Point findElementForConnection() {	
//    	Point ids = new Point(-1,-1);
//    	
//    	for(AbstractElement e : mElements.values()) {
//			if(e.getId() != mTouchElement.getId()) {
//				int offX = mTouchElement.getMiddleX()-e.getMiddleX();
//				int offY = mTouchElement.getMiddleY()-e.getMiddleY();
//				
//				if((Math.abs(offX) < DISTANCE_FOR_AUTO_CONNECTION_X) && (Math.abs(offY) < DISTANCE_FOR_AUTO_CONNECTION_Y)) {
//					if(offY < 0) 
//						ids.x = e.getId();
//					else 
//						ids.y = e.getId();
//				}
//			}	
//    	}
//    	return ids;
//    }
    
    
	public boolean somethingSelected() {
		return !(mSelected.isEmpty());
	}
	
	public boolean isSelected(Integer id) {
		return mSelected.containsKey(id);
	}
	
	public boolean selectElementById(Integer id) {
		final AbstractElement elem = mElements.get(id);
		if(elem != null) {
			mSelected.put(id, elem);
			elem.modeSelected();
			return true;
		} else {
			return false;
		}	
	}
	
	public boolean deselectElementById(Integer id) {
		final AbstractElement elem = mSelected.get(id);
		if(elem != null) {
			elem.modeNormal();
			mSelected.remove(id);
			return true;
		} else {
			return false;
		}	
	}
    
    
	


}
