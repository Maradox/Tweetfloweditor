
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
import java.util.List;

import at.tuwien.dsgproject.tfe.entities.AbstractElement;
import at.tuwien.dsgproject.tfe.entities.OpenSequence;
import at.tuwien.dsgproject.tfe.entities.ServiceRequest;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;

public abstract class TweeterParser {	//Todo Not-supported exception schreiben

	public static String parseTweetFlow(TweetFlow tweetFlow) {
		
		List<AbstractElement> elements = tweetFlow.getmElementsAsList();
		
		String tweetFlowString = "TF didBegin.Tweetflow\n";
		
		for(AbstractElement element : elements) {
			
			if(element instanceof OpenSequence) {			// Is an open sequence
				String cos = createOpenSequence(tweetFlow,(OpenSequence) element);
				tweetFlowString += cos;
				if(cos.length() > 0) {
					tweetFlowString += "\n";
				}	
				continue;
			}	
			
			if(tweetFlow.isInOpenSequence(element)) {		// Is in an open sequence
				continue;
			}	
			if((element.getClosedSequenceNext() == null) && (element.getClosedSequencePrev() == null)) {	// Open sequence Request
				tweetFlowString += createOpenSequenceRequest(element);
				tweetFlowString += "\n";
				continue;
			}
			
			else if(element.getClosedSequencePrev() != null) {	// In the closed sequence
				continue;
			}
			
			else if(element.getClosedSequenceNext() != null) {	// Start of sequence
				tweetFlowString += createClosedSequence(element);
				tweetFlowString += "\n";
				continue;
			}
		}
		
		tweetFlowString += "TF didFinish.Tweetflow"; 
		return tweetFlowString;
		
	}
	
	private static String createOpenSequence(TweetFlow tweetFlow, OpenSequence e) {
		List<AbstractElement> list = tweetFlow.getElementsInOpenSequence(e);
		String elementString = "";
		
		if(list.size() > 0) {	
			elementString += "OpenSequence Begin\n";
			for(AbstractElement element : list) {
							
				if((element.getClosedSequenceNext() == null) && (element.getClosedSequencePrev() == null)) {	// Open sequence Request
					elementString += createOpenSequenceRequest(element);
					elementString += "\n";
				}
				
				else if(element.getClosedSequencePrev() != null) {	// In the closed sequence
					continue;
				}
				
				else if(element.getClosedSequenceNext() != null) {	// Start of sequence
					elementString += createClosedSequence(element);
					elementString += "\n";
				}
			}
			
			elementString += "OpenSequence End";
		}
		
		return elementString;
	}
	
	private static String createOpenSequenceRequest(AbstractElement element) {
		
		String elementString = "";
		
		if(element instanceof ServiceRequest) {
			elementString = "SR @" + ((ServiceRequest)element).getUser() 
			+ " " + ((ServiceRequest)element).getOperation() + "." + ((ServiceRequest)element).getService();
			if(((ServiceRequest)element).getInputdata().length() > 0)
				elementString += " " + ((ServiceRequest)element).getInputdata();
			if(((ServiceRequest)element).getCondition().length() > 0)
				elementString += " [" + ((ServiceRequest)element).getCondition() + "]";

		}
		
		if(element instanceof OpenSequence) {
			elementString = element.getId() + " Open sequence ";
		}
		
		return elementString;
	}
	
	private static String createClosedSequence(AbstractElement element) {
		
		String elementString = "[";
		
		while(element != null) {
		
			if(element instanceof ServiceRequest) {
				elementString += "SR @" + ((ServiceRequest)element).getUser() 
				+ " " + ((ServiceRequest)element).getOperation() + "." + ((ServiceRequest)element).getService();
				if(((ServiceRequest)element).getInputdata().length() > 0)
					elementString += " " + ((ServiceRequest)element).getInputdata();
				if(((ServiceRequest)element).getCondition().length() > 0)
					elementString += " [" + ((ServiceRequest)element).getCondition() + "]";
			}
			
			if(element instanceof OpenSequence) {
				elementString = element.getId() + " This cannot be done. ";	//TODO exception
			}
			
			if(element.getClosedSequenceNext() != null) 
				elementString += " | ";
			else {
				elementString += "]";
			}
			
			element = element.getClosedSequenceNext();
		}	
		
		return elementString;
	}
	
}
