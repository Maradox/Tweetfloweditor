package at.tuwien.dsgproject.tfe.common;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import at.tuwien.dsgproject.tfe.entities.AbstractElement;
import at.tuwien.dsgproject.tfe.entities.TweetFlow;

public class StorageHandler {
	
	private boolean mExternalStorageAvailable = false;
	private boolean mExternalStorageWriteable = false;
	private File mFilesDir = null;
	

	public StorageHandler(Context context) {
		mFilesDir = context.getExternalFilesDir(null);
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
	
	public void write(String filename, String tweetFlowName, ArrayList<AbstractElement> elements) {
		checkStorageState();
		if(mExternalStorageWriteable) {
			final File file = new File(mFilesDir, filename);
			if(!file.exists()) {
				BufferedWriter out = null;
				try {
					if(file.createNewFile()) {
						out = new BufferedWriter(new FileWriter(file));
						writeToFile(out, tweetFlowName, elements);
						out.flush();
					} else {
						// could not create file TODO
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if(out != null) {
						try {
							out.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} else {
				//overwrite? TODO: this
			}

		} else {
			//TODO: show/throw
		}
		
		
	}
	
	private void writeToFile(BufferedWriter out,
			String tweetFlowName,
			ArrayList<AbstractElement> elements) throws IOException {
		
		out.write("TF " + tweetFlowName + "\n");
			
		for(AbstractElement e : elements) {
			out.write(e.getElementInfoString() + "\n");
		}
			
	}
	
	public TweetFlow openTweetFlowFile(String filename) {
		checkStorageState();
		if(mExternalStorageAvailable) {
			final File file = new File(mFilesDir, filename);
			if(file.exists()) {
				BufferedReader in = null;
				try {
					in = new BufferedReader(new FileReader(file));
					return parseFileToTweetFlow(in);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if(in != null) {
						try {
							in.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
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
	
	private TweetFlow parseFileToTweetFlow(BufferedReader in) throws IOException {
		TweetFlow tf = new TweetFlow(null);
		String s = null;
		while((s = in.readLine()) != null) {
			if(s.startsWith("TF")) {
				//set TweetFlow name
			} else if(s.startsWith("SR")) {
				//service request
			} else if(s.startsWith("OS")) {
				//open sequence
			} else {
				//TODO FAIL
			}
			
		}
		
		
		
		return null;
	}
	
	
	
	
	
	

}
