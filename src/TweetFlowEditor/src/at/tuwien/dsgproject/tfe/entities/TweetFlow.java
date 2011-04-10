package at.tuwien.dsgproject.tfe.entities;

import java.io.Serializable;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Canvas;

public class TweetFlow implements Serializable {
	
	/**
	 * serializable ID
	 */
	private static final long serialVersionUID = -1549177934192038606L;
	public static final int DISTANCE_FOR_AUTO_CONNECTION_X = 70;
	public static final int DISTANCE_FOR_AUTO_CONNECTION_Y = 120;
	public static final int DISTANCE_FOR_AUTO_DISCONNECTION_X = 100;
	public static final int DISTANCE_FOR_AUTO_DISCONNECTION_Y = 150;
	
	public static final String TF_ID = "TweetFlow";

	private Context mContext;
	
	private HashMap<Integer, AbstractElement> mElements;
	private HashMap<Integer, AbstractElement> mSelected;
	//public HashMap<Integer, OpenSequence> mOpenSequences;
	
	private AbstractElement mTouchElement = null;
	
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
		addServiceRequest(100,100);
		addServiceRequest(200,350);
		addServiceRequest(300,500);
		
		addOpenSequence();
	}
	
	
	public void addServiceRequest(int x, int y) {
		mElements.put(mElemCounter, new ServiceRequest(mContext, mElemCounter++, x-25, y-40));
	}
	
	
	public void addOpenSequence() {
		mElements.put(mElemCounter, new OpenSequence(mContext, mElemCounter++, 100, 100, 250, 400));
	}
	
	public void deleteElement(Integer id) {
		AbstractElement elem = mElements.get(id);
		if(elem != null) {
			elem.removeFromClosedSequence();
		}
		mSelected.remove(id);
		mElements.remove(id);
	}
	
	/**
	 * Checks if an element is at the given location.
	 * If an element is found it is set as the current touch element and its mode 
	 * is set to modeMarked()
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true if element was fount and set as current touch element, false otherwise
	 */
	public boolean elementAt(int x, int y) {
		for(AbstractElement r : mElements.values()) {
			if(r.isFocused(x,y)) {
				mTouchElement = r;
				return true;
			}	
		}
		mTouchElement = null;
		return false;
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
    
	public void moveTouchElement(int offX, int offY) {
		if(mTouchElement != null) {
			mTouchElement.move(offX, offY);
		}
	}
    
    public void deselectAll() {
    	for(AbstractElement e : mSelected.values()) {
    		e.modeNormal();
    	}
    	mSelected.clear();
    }
    
    /*
    public Point findElementForConnection() {	
    	Point ids = new Point(-1,-1);
    	
    	for(AbstractElement e : mElements.values()) {
			if(e.getId() != mTouchElement.getId()) {
				int offX = mTouchElement.getMiddleX()-e.getMiddleX();
				int offY = mTouchElement.getMiddleY()-e.getMiddleY();
				
				if((Math.abs(offX) < DISTANCE_FOR_AUTO_CONNECTION_X) && (Math.abs(offY) < DISTANCE_FOR_AUTO_CONNECTION_Y)) {
					if(offY < 0) 
						ids.x = e.getId();
					else 
						ids.y = e.getId();
				}
			}	
    	}
    	return ids;
    }
    */
    
    public void setMaybeConnection() {	 
    	mTouchElement.setClosedSequenceMaybeNext(null);
    	for(AbstractElement e : mElements.values()) {
    		e.setClosedSequenceMaybeNext(null);
    	}	
    	
    	for(AbstractElement e : mElements.values()) {
			if(e.getId() != mTouchElement.getId()) {
				int offX = mTouchElement.getMiddleX()-e.getMiddleX();
				int offY = mTouchElement.getMiddleY()-e.getMiddleY();
				
				if(offY > 0 && mTouchElement.getClosedSequenceNext() == e) {
					mTouchElement.setClosedSequenceNext(null);
				}
				else if(offY < 0 && e.getClosedSequenceNext() == mTouchElement) {
					e.setClosedSequenceNext(null);
				}
				
				if((Math.abs(offX) > DISTANCE_FOR_AUTO_DISCONNECTION_X) || 
						(Math.abs(offY) > DISTANCE_FOR_AUTO_DISCONNECTION_Y)) {
					if(offY > 0) {
						if(e.getClosedSequenceNext() == mTouchElement)
							e.setClosedSequenceNext(null);
					}	
					else {
						if(mTouchElement.getClosedSequenceNext() == e)
							mTouchElement.setClosedSequenceNext(null);
					}
				}
				
				else if((Math.abs(offX) < DISTANCE_FOR_AUTO_CONNECTION_X) && 
						(Math.abs(offY) < DISTANCE_FOR_AUTO_CONNECTION_Y)) {
					if(offY > 0) {
						if(e.getClosedSequenceNext() == null) {
							e.setClosedSequenceMaybeNext(mTouchElement);
						}
					}	
					else {
						if(mTouchElement.getClosedSequenceNext() == null) {
						}	
					}
				}
			}	
    	}

    }
    
    public void convertMaybeIntoFixConnection() {
    	for(AbstractElement e : mElements.values()) {
    		if(e.getClosedSequenceMaybeNext() != null)
    			e.setClosedSequenceNext(e.getClosedSequenceMaybeNext());
    		e.setClosedSequenceMaybeNext(null);
    	}	
    }
    
    
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
	
	public boolean selectTouchElement() {
		if(mTouchElement != null) {
			mSelected.put(mTouchElement.getId(), mTouchElement);
			mTouchElement.modeSelected();
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
	
	public boolean deselectTouchElement() {
		if(mTouchElement != null) {
			mTouchElement.modeNormal();
			mSelected.remove(mTouchElement.getId());
			return true;
		} else {
			return false;
		}	
	}
	
	public AbstractElement getTouchElement() {
		return mTouchElement;
	}
	
	public boolean isTouchElementSelected() {
		if(mTouchElement != null) {
			return mTouchElement.isSelected();
		} else {
			return false;
		}
	}
	
	public void toggleTouchElementSelected() {
		if(mTouchElement != null) {
			if(mSelected.containsKey(mTouchElement.getId())) {
				deselectTouchElement();
			} else {
				selectTouchElement();
			}
		}
	}
	
	public void unmarkTouchElement() {
		if(mTouchElement != null) {
			if(mTouchElement.isSelected()) mTouchElement.modeSelected();
			else mTouchElement.modeNormal();
		}
	}
	
	public void setTouchElementModeNormal() {
		if(mTouchElement != null) mTouchElement.modeNormal();
	}
	
	public void setTouchElementModeMarked() {
		if(mTouchElement != null) mTouchElement.modeMarked();
	}
	
	public void setTouchElementModeSelected() {
		if(mTouchElement != null) mTouchElement.modeSelected();
	}
	
	public void setContext(Context context) {
		mContext = context;
	}
	
	public boolean isTextFocused(int x, int y) {
		if(mTouchElement != null) {
			return mTouchElement.isTextFocused(x, y);
		} else {
			return false;
		}
	}
}
