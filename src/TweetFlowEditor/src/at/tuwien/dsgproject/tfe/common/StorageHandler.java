package at.tuwien.dsgproject.tfe.common;


import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.CycleStrategy;
import org.simpleframework.xml.strategy.Strategy;

import android.content.Context;
import android.os.Environment;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;

public class StorageHandler {
	
	private boolean mExternalStorageAvailable = false;
	private boolean mExternalStorageWriteable = false;
	private File mFilesDir = null;
	

	public StorageHandler(Context context) {
		mFilesDir = new File(Environment.getExternalStorageDirectory(), "Tweetfloweditor");
		if(!mFilesDir.exists()) {
			mFilesDir.mkdir();
		}
	}
	
	public void checkStorageState() {
		final String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
	}
	
	
	public String[] listFiles() {
		checkStorageState();
		if(mExternalStorageAvailable) return mFilesDir.list();
		else return null;
	}
	
	public void write(String filename, TweetFlow tweetflow) {
		checkStorageState();
		if(mExternalStorageWriteable) {
			final File file = new File(mFilesDir, filename);
			if(!file.exists()) {
				Strategy strategy = new CycleStrategy("xml_id", "xml_ref");
				Serializer serializer = new Persister(strategy);
		        try {
					serializer.write(tweetflow, file);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				//overwrite? TODO: this
			}

		} else {
			//TODO: show/throw
		}
		
		
	}
	
	public TweetFlow openTweetFlowFile(String filename) {
		checkStorageState();
		if(mExternalStorageAvailable) {
			final File file = new File(mFilesDir, filename);
			if(file.exists()) {
				Strategy strategy = new CycleStrategy("xml_id", "xml_ref");
				Serializer serializer = new Persister(strategy);
		        try {
					TweetFlow tf = serializer.read(TweetFlow.class, file);
					//TODO: update context, etc. for tweetflow
					return tf;
		        } catch (Exception e) {
		        	e.printStackTrace();
		        }
			} else {
				//no file
				//TODO exception
			}
			
		} else {
			//TODO throw exception/handle this
		}
		return null;
	}

}
