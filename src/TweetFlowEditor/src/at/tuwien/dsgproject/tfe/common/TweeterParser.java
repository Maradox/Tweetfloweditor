
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

import at.tuwien.dsgproject.tfe.entities.AbstractElement;
import at.tuwien.dsgproject.tfe.entities.OpenSequence;
import at.tuwien.dsgproject.tfe.entities.ServiceRequest;

public abstract class TweeterParser {	//Todo Not-supported exception schreiben

	public static String parseTweetFlow(ArrayList<AbstractElement> elements) {
		
		String tweetFlowString = "";
		
		for(AbstractElement element : elements) {
			
			if((element.getClosedSequenceNext() == null) && (element.getClosedSequencePrev() == null)) {	// Open sequence
				tweetFlowString += createOpenSequence(element);
				tweetFlowString += "\n";
			}
			
			else if(element.getClosedSequencePrev() != null) {	// In the sequence
				continue;
			}
			
			else if(element.getClosedSequenceNext() != null) {	// Start of sequence
				tweetFlowString += createClosedSequence(element);
				tweetFlowString += "\n";
			}
		}
		
		return tweetFlowString;
		
	}
	
	private static String createOpenSequence(AbstractElement element) {
		
		String elementString = "";
		
		if(element instanceof ServiceRequest) {
			elementString = element.getId() + " TF " + "_name_" + " SR @" + "_user_" 
			+ " " + "_operation_" + "." + "_service_" + " " + "_inputdata_" + " [" + "_condition_" + "]"; 
		}
		
		if(element instanceof OpenSequence) {
			elementString = element.getId() + " Open sequence ";
		}
		
		return elementString;
		
	}
	
	private static String createClosedSequence(AbstractElement element) {
		
		String elementString = " TF " + "_name_";
		
		while(element != null) {
		
			if(element instanceof ServiceRequest) {
				elementString += element.getId() + " [SR @" + "_user_" 
				+ " " + "_operation_" + "." + "_service_" + " " + "_inputdata_" + " [" + "_condition_" + "]"; 
			}
			
			if(element instanceof OpenSequence) {
				elementString = element.getId() + " Open sequence ";
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
