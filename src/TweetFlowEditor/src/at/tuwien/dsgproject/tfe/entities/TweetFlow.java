
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import android.content.Context;
import android.graphics.Canvas;
import at.tuwien.dsgproject.tfe.quickAction.QuickAction;
import at.tuwien.dsgproject.tfe.views.EditorView;


/**
 * TweetFlow
 * 
 * @author Matthias Neumayr
 * @author Martin Perebner
 * 
 * TweetFlow contains a collection of all Tweetflow-Elements and handles
 * operations/touch-events on the elements
 */
@Root
public class TweetFlow implements Serializable {
	
	/**
	 * serializable ID
	 */
	private static final long serialVersionUID = -1549177934192038606L;
	
	public static final int DISTANCE_FOR_AUTO_CONNECTION_X = 50;
	public static final int DISTANCE_FOR_AUTO_CONNECTION_Y = 70;
	public static final int DISTANCE_FOR_AUTO_DISCONNECTION_X = 80;
	public static final int DISTANCE_FOR_AUTO_DISCONNECTION_Y = 120;
	
	private Context mContext;
	
	@Attribute
	private String mName = "";
	
	@Attribute
	private Integer mElemCounter;
	
	@ElementMap(entry="element", key="id", attribute=true, inline=true, required=false, empty=true)
	private HashMap<Integer, AbstractElement> mElements;
	private HashMap<Integer, AbstractElement> mSelected;
	
	private AbstractElement mTouchElement = null;
	
	public TweetFlow(Context context) {
		this();
		
		mContext = context;
	}
	
	public TweetFlow() {
		mElemCounter = 0;
		mElements = new HashMap<Integer, AbstractElement>();
		mSelected = new HashMap<Integer, AbstractElement>();
		//mOpenSequences = new HashMap<Integer, OpenSequence>();
	}
	
	public boolean isInOpenSequence(AbstractElement element) {
		for(AbstractElement e : mElements.values()) {
			if(e instanceof OpenSequence) {
				if(element.getMiddleX() > e.getmX() && element.getMiddleX() < e.getRightX()
						&& element.getMiddleY() > e.getTopY() &&  element.getMiddleY() < e.getBotY())
					return true;
			}
		}
		return false;
	}
	
	public List<AbstractElement> getElementsInOpenSequence(OpenSequence element) {
		List<AbstractElement> list = new ArrayList<AbstractElement>();
		for(AbstractElement e : mElements.values()) {
			if(e instanceof ServiceRequest) {
				if(e.getMiddleX() > element.getmX() && e.getMiddleX() < element.getRightX()
						&& e.getMiddleY() > element.getTopY() &&  e.getMiddleY() < element.getBotY())
					list.add(e);
			}
		}
		return list;
	}
	
	public void addServiceRequest(int x, int y) {
		final ServiceRequest sr = new ServiceRequest(mElemCounter++, x-25, y-40);
		mElements.put(sr.getId(), sr);
		sr.setContextAndDrawables(mContext);
	}
	
	
	public void addOpenSequence() {
		final OpenSequence os = new OpenSequence(mElemCounter++, 100, 100, 250, 400);
		mElements.put(os.getId(), os);
		os.setContextAndDrawables(mContext);
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
    
    public void moveGridElements(ArrayList<ServiceRequest> gridElements, int offX, int offY) {
		for(AbstractElement e : gridElements) {
			e.move(offX, offY);
		}
		updateMoveGridConnections(gridElements);
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
		resetAllMaybeConnections();
    	for(AbstractElement selected : mSelected.values()) {
    		updateSelectedElementConnections(selected);
    	}
    }
    
    private void updateMoveGridConnections(ArrayList<ServiceRequest> gridElements) {
		resetAllMaybeConnections();
    	for(AbstractElement e : gridElements) {
    		updateElementConnections(e);
    	}
    }
    
    private void updateTouchElementConnections() {
    	mTouchElement.checkRemoveNext();
    	mTouchElement.checkRemovePrev();
    	resetAllMaybeConnections();
    	
    	if(!(mTouchElement instanceof OpenSequence)) {
	    	for(AbstractElement e : mElements.values()) {
	    		if(!mTouchElement.equals(e) && !(e instanceof OpenSequence)) {
	    			mTouchElement.checkMaybeConnections(e);
	    		}
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
    
  //improvements: select best = shortest maybe connection if several exist
    public void updateElementConnections(AbstractElement e) {
		// check if closed sequence connections still valid?
		if(e.mClosedSequenceNext != null) {
			if(!mSelected.containsKey(e.mClosedSequenceNext.getId())) {
    			e.checkRemoveNext();
			}
		}
		
		if(e.mClosedSequencePrev != null) {
			final AbstractElement prev = e.mClosedSequencePrev;
			if(!mSelected.containsKey(prev.getId())) {
				e.checkRemovePrev();
	    		
			}
		}
			
		if(e.mClosedSequenceNext == null ||
				e.mClosedSequencePrev == null) {
    		e.resetMaybeConnections();
    		for(AbstractElement candidate : mElements.values()) {
    			//no need to compare selected elements
    			if(!mSelected.containsKey(candidate.getId())) {
    				e.checkMaybeConnections(candidate);
    			}
    		}
		}
    }

    

    
    public void convertMaybeIntoFixConnection() {
    	for(AbstractElement e : mElements.values()) {
    		e.convertMaybeIntoFixed();
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
		for(AbstractElement elem : mElements.values()) {
			elem.setContextAndDrawables(mContext);
		}
	}


	public HashMap<Integer, AbstractElement> getmElements() {
		return mElements;
	}
	
	public ArrayList<AbstractElement> getmElementsAsList() {
		ArrayList<AbstractElement> list = new ArrayList<AbstractElement>();
		
		for(Map.Entry<Integer,AbstractElement> e : getmElements().entrySet()){
			list.add(e.getValue());
		}
		
		return list;
	}


	public void setmElements(HashMap<Integer, AbstractElement> mElements) {
		this.mElements = mElements;
	}
	
	
	public int getSelectedElementsCount() {
		return mSelected.size();
	}


	public HashMap<Integer, AbstractElement> getmSelected() {
		return mSelected;
	}
	
	public void updateClosedSequences() {
		for(AbstractElement e : mElements.values()) {
			e.updateClosedSequences();
		}
	}
	
	public void prepareQuickactions(QuickAction qa, EditorView view) {
		if(mTouchElement != null) mTouchElement.fillQuickActionMenu(qa, view);
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	


}
