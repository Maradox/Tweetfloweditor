package at.tuwien.dsgproject.tfe.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;

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
	
	public void write(String filename, String content) {
		checkStorageState();
		if(mExternalStorageWriteable) {
			final File file = new File(mFilesDir, filename);
			if(!file.exists()) {
				FileOutputStream out = null;
				try {
					if(file.createNewFile()) {
						out = new FileOutputStream(file);
						out.write(content.getBytes());
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

		}
		
		
	}
	
	
	
	

}
