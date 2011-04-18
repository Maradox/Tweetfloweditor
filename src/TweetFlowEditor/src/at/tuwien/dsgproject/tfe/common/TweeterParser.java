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
