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
	public static final int DISTANCE_FOR_AUTO_CONNECTION_X = 50;
	public static final int DISTANCE_FOR_AUTO_CONNECTION_Y = 70;
	public static final int DISTANCE_FOR_AUTO_DISCONNECTION_X = 80;
	public static final int DISTANCE_FOR_AUTO_DISCONNECTION_Y = 120;
	
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
			elem.removeClosedSequencePrev();
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
		updateMoveSelectedConnections();
    }
    
	public void moveTouchElement(int offX, int offY) {
		if(mTouchElement != null) {
			mTouchElement.move(offX, offY);
			updateTouchElementConnections();
		}
	}
    
    public void deselectAll() {
    	for(AbstractElement e : mSelected.values()) {
    		e.modeNormal();
    	}
    	mSelected.clear();
    }

    
    private void updateMoveSelectedConnections() {
    	//TODO: needed?
		resetAllMaybeConnections();
    	for(AbstractElement selected : mSelected.values()) {
    		updateSelectedElementConnections(selected);
    	}
    }
    
    private void updateTouchElementConnections() {
    	mTouchElement.checkRemoveNext();
    	mTouchElement.checkRemovePrev();
    	//TODO: needed?
    	resetAllMaybeConnections();
    	for(AbstractElement e : mElements.values()) {
    		if(!mTouchElement.equals(e)) {
    			mTouchElement.checkMaybeConnections(e);
    		}
    	}
    	
    }
    
    
    private void resetAllMaybeConnections() {
    	for(AbstractElement e : mElements.values()) {
    		e.resetMaybeConnections();
    	}
    }
    
    //improvements: select best = shortest maybe connection if several exist
    private void updateSelectedElementConnections(AbstractElement selected) {
		// check if closed sequence connections still valid?
		if(selected.mClosedSequenceNext != null) {
			if(!mSelected.containsKey(selected.mClosedSequenceNext.getId())) {
    			selected.checkRemoveNext();
			}
		}
		
		if(selected.mClosedSequencePrev != null) {
			final AbstractElement prev = selected.mClosedSequencePrev;
			if(!mSelected.containsKey(prev.getId())) {
				selected.checkRemovePrev();
	    		
			}
		}
			
		if(selected.mClosedSequenceNext == null ||
				selected.mClosedSequencePrev == null) {
    		selected.resetMaybeConnections();
    		for(AbstractElement candidate : mElements.values()) {
    			//no need to compare selected elements
    			if(!mSelected.containsKey(candidate.getId())) {
    				selected.checkMaybeConnections(candidate);
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
	
	public int getSelectedElementsCount() {
		return mSelected.size();
	}
}
